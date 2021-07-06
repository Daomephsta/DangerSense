package io.github.daomephsta.dangersense.client;

import io.github.daomephsta.dangersense.DangerSense;
import net.minecraft.item.ItemModelsProperties;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = DangerSense.MOD_ID, bus = Bus.MOD, value = Dist.CLIENT)
public class DangerSenseClient
{
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event)
    {
        ItemModelsProperties.register(DangerSense.EYE_OF_HOSTILITY, DangerSense.location("is_open"), 
            (stack, world, entity) -> DangerSenseOverlayRenderer.isEyeActive(stack) ? 1.0F : 0.0F);
    }
}
