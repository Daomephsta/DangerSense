package leviathan143.dangersense.common;

import leviathan143.dangersense.common.capabilities.CapabilityDangerSense;
import leviathan143.dangersense.common.capabilities.PlayerCapAttacher;
import leviathan143.dangersense.common.config.Config;
import leviathan143.dangersense.common.effects.ModEffects;
import leviathan143.dangersense.common.handlers.DropHandler;
import leviathan143.dangersense.common.items.ModItems;
import leviathan143.dangersense.common.packets.PacketHandler;
import leviathan143.dangersense.common.recipes.ModRecipes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy 
{	
	public void preInit(FMLPreInitializationEvent event)
	{
		Config.preinit(event);
		ModEffects.register();
		ModItems.register();
		CapabilityDangerSense.register();
		registerRenders();
	}
	
	public void init(FMLInitializationEvent event)
	{
		PacketHandler.registerPackets();
		ModRecipes.init();
	}
	
	public void postInit(FMLPostInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(DropHandler.class);
		MinecraftForge.EVENT_BUS.register(PlayerCapAttacher.class);
	}
	
	public void registerRenders()
	{
		
	}
}
