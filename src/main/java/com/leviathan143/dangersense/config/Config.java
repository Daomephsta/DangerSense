package com.leviathan143.dangersense.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class Config 
{
	private static Configuration config;
	public static float detectionRange;
	public static int scanTimeInTicks;
	public static void preinit(FMLPreInitializationEvent event)
	{
		config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		detectionRange = config.getFloat("detectionRange", Configuration.CATEGORY_GENERAL, 5, 3, 10, "How near to the player a hostile mob must be to activate the danger sense");
		scanTimeInTicks = config.getInt("scanTimeInTicks", Configuration.CATEGORY_GENERAL, 20, 10, 100, "The number of ticks that will pass between checks for hostile mobs");
		config.save();
	}
}
