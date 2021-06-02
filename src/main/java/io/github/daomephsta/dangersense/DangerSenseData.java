package io.github.daomephsta.dangersense;

import io.github.daomephsta.dangersense.client.DangerSenseOverlayRenderer;
import io.github.daomephsta.dangersense.util.DataCapabilityHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = DangerSense.MOD_ID)
public class DangerSenseData implements ICapabilitySerializable<CompoundNBT>
{
	private static final ResourceLocation KEY = DangerSense.location("dangersense_data");
    @CapabilityInject(DangerSenseData.class)
    private static Capability<DangerSenseData> CAPABILITY;
    private EntityType<?> target;
    private final LazyOptional<DangerSenseData> lazyThis = LazyOptional.of(() -> this);

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction side)
    {
        if (capability == CAPABILITY)
            return lazyThis.cast();
        return LazyOptional.empty();
    }

	public EntityType<?> getTarget() 
	{
	    return target;
	}

	public void setTarget(EntityType<?> target) 
	{
	    this.target = target;
	    if (target == null)
	        DangerSenseOverlayRenderer.setActive(false);
	}

    public void clearTarget() 
    {
        setTarget(null);
    }
	
	public static LazyOptional<DangerSenseData> get(Entity entity)
	{
	    return entity.getCapability(CAPABILITY);
	}
	
	public static void register()
	{
	    DataCapabilityHelper.register(DangerSenseData.class);
	}

    @Override
    public CompoundNBT serializeNBT()
    {
        CompoundNBT tag = new CompoundNBT();
        if (target != null)
            tag.putString("target", target.getRegistryName().toString());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt)
    {
        CompoundNBT tag = (CompoundNBT) nbt;
        this.target = tag.contains("target") 
            ? ForgeRegistries.ENTITIES.getValue(new ResourceLocation(tag.getString("target")))
            : null;
    }
	
    @SubscribeEvent
    public static void attachCaps(AttachCapabilitiesEvent<Entity> event)
    {
        if(event.getObject() instanceof PlayerEntity)
        {
            DangerSenseData data = new DangerSenseData();
            event.addCapability(KEY, data);
            event.addListener(data.lazyThis::invalidate);
        }
    }

    @SubscribeEvent
    public static void syncDangerSenseCap(EntityJoinWorldEvent event)
    {
        if (!(event.getEntity() instanceof ServerPlayerEntity))
            return;
        ServerPlayerEntity player = (ServerPlayerEntity) event.getEntity();
        EntityType<?> target = DangerSenseData.get(player).resolve()
            .map(DangerSenseData::getTarget).orElse(null);
        PacketSyncSense.send(target, player);
    }
}
