package io.github.daomephsta.dangersense;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.potion.Effect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.ObjectHolder;

@Mod(DangerSense.MOD_ID)
public class DangerSense
{
    public static final String MOD_ID = "dangersense";
    @ObjectHolder(value = DangerSense.MOD_ID + ":dangersense")
    public static final Effect EFFECT = null;
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
        location("main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

    public DangerSense()
    {
        MinecraftForge.EVENT_BUS.register(this);
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
        DangerSenseConfig.register();
    }
    
    @SubscribeEvent
    public void commonSetup(FMLCommonSetupEvent event)
    {
        DangerSenseData.register();
        PacketSyncSense.registerAsIndex(0);
    }

    public static ResourceLocation location(String id)
    {
        return new ResourceLocation(MOD_ID, id);
    }
    
    private static <T extends IForgeRegistryEntry<T>> T withId(String id, T item)
    {
        item.setRegistryName(location(id));
        return item;
    }
    
    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().registerAll(
            withId("sandy_quartz", new Item(new Item.Properties().tab(ItemGroup.TAB_MATERIALS))),
            withId("soul_quartz", new Item(new Item.Properties().tab(ItemGroup.TAB_MATERIALS))),
            withId("soul_quartz_dagger", new SoulQuartzDaggerItem(new Item.Properties()
                .stacksTo(1)
                .defaultDurability(9)
                .tab(ItemGroup.TAB_COMBAT))));
    }
    
    @SubscribeEvent
    public void registerEffects(RegistryEvent.Register<Effect> event)
    {
        event.getRegistry().register(withId("dangersense", new DangerSenseEffect()));
    }
}
