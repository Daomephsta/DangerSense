package leviathan143.dangersense.common;

import leviathan143.dangersense.common.config.Config;
import leviathan143.dangersense.common.handlers.DangerSenseHandler;
import leviathan143.dangersense.common.items.ModItems;
import leviathan143.dangersense.common.items.ModPotions;
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
		ModPotions.register();
		ModItems.register();
		registerRenders();
	}
	
	public void init(FMLInitializationEvent event)
	{
		ModRecipes.init();
	}
	
	public void postInit(FMLPostInitializationEvent event)
	{
		
	}
	
	public void registerRenders()
	{
		
	}
}
