package io.github.daomephsta.dangersense.item;


import java.util.Optional;

import io.github.daomephsta.dangersense.DangerSense;
import io.github.daomephsta.dangersense.DangerSenseConfig;
import io.github.daomephsta.dangersense.client.DangerSenseOverlayRenderer;
import io.github.daomephsta.dangersense.util.Chat;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

public class EyeOfHostilityItem extends Item
{
    private static final String TAG_FOCUS = "focus",
                                TAG_DEGRADATION_TIMESTAMP = "degradation_timestamp";
    
    public EyeOfHostilityItem(Properties properties)
    {
        super(properties);
        ItemModelsProperties.register(this, DangerSense.location("is_open"), 
            (stack, world, entity) -> DangerSenseOverlayRenderer.isEyeActive(stack) ? 1.0F : 0.0F);
    }
    
    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand)
    {
        ItemStack stack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        return ActionResult.success(stack);
    }
    
    @Override
    public ItemStack finishUsingItem(ItemStack stack, World world, LivingEntity entity)
    {
        double distance = 8.0D;
        Vector3d rayStart = entity.getEyePosition(1.0F),
                 rayEnd = rayStart.add(entity.getViewVector(1.0F).scale(distance));
        EntityRayTraceResult result = ProjectileHelper.getEntityHitResult(world, entity, rayStart, rayEnd, 
            entity.getBoundingBox().expandTowards(rayEnd).inflate(distance), e -> true);
        if(result != null) 
        {
            EntityType<?> type = result.getEntity().getType();
            stack.getOrCreateTag().putString(TAG_FOCUS, type.getRegistryName().toString());
        }
        else
            stack.getOrCreateTag().remove(TAG_FOCUS);
        if (entity instanceof PlayerEntity) 
            Chat.pushToActionbar((PlayerEntity) entity, this.getName(stack));
        return stack;
    }
    
    @Override
    public ITextComponent getName(ItemStack stack)
    {
        return getName(stack, super.getName(stack));
    }

    public ITextComponent getName(ItemStack stack, ITextComponent name)
    {
        if (hasFocus(stack))
        {
            return getFocus(stack)
                .map(type -> new TranslationTextComponent(getDescriptionId(stack) + ".focus_specific", 
                    name, type.getDescription()))
                // Entity type from removed mod, or old ID
                .orElseGet(() -> new TranslationTextComponent(getDescriptionId(stack) + ".focus_null", name));
        }
        else
            return new TranslationTextComponent(getDescriptionId(stack) + ".focus_hostiles", name);
    }
    
    @Override
    public void inventoryTick(ItemStack stack, World world, Entity owner, int slot, boolean selected)
    {
        if (!stack.getOrCreateTag().contains(TAG_DEGRADATION_TIMESTAMP))
        {
            stack.getTag().putLong(TAG_DEGRADATION_TIMESTAMP, 
                world.getGameTime() + DangerSenseConfig.eyeOfHostility.degradationTicks.get());
        }
        else if (world.getGameTime() >= stack.getTag().getLong(TAG_DEGRADATION_TIMESTAMP))
        {
            ItemStack degraded = new ItemStack(DangerSense.DEGRADED_EYE_OF_HOSTILITY);
            if (hasFocus(stack))
                degraded.addTagElement(TAG_FOCUS, StringNBT.valueOf(stack.getTag().getString(TAG_FOCUS)));
            owner.setSlot(slot, degraded);
        }
    }

    public boolean hasFocus(ItemStack stack)
    {
        return stack.getOrCreateTag().contains(TAG_FOCUS);
    }

    public Optional<EntityType<?>> getFocus(ItemStack stack)
    {
        ResourceLocation id = new ResourceLocation(stack.getTag().getString(TAG_FOCUS));
        return ForgeRegistries.ENTITIES.containsKey(id) 
            ? Optional.of(ForgeRegistries.ENTITIES.getValue(id))
            : Optional.empty();
    }

    @Override
    public UseAction getUseAnimation(ItemStack stack)
    {
        return UseAction.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack)
    {
        return 32;
    }
}
