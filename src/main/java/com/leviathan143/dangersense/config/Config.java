package com.leviathan143.dangersense.config;

import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class Config 
{
	private static Configuration config;
	public static float detectionRange;
	public static int scanTimeInTicks;
	public static int potionID;
	public static boolean easyRecipe;
	public static int potionDuration;
	public static float lowerBounds;
	
	public static void preinit(FMLPreInitializationEvent event)
	{
		config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		//Mod Detection
		detectionRange = config.getFloat("Detection Range", "Mob Detection", 5, 3, 10, "How near to the player a hostile mob must be to activate the danger sense");
		lowerBounds = config.getFloat("Lower Bounds", "Mob Detection", 2, 0, 3, "How far down the mob detection algorithm looks for mobs");
		scanTimeInTicks = config.getInt("Scan Time", "Mob Detection", 20, 10, 100, "The number of ticks that will pass between checks for hostile mobs");

		//Dangersense Potion
		potionID = config.getInt("Potion ID", "Dangersense Potion", 42, 32, 256, "The id of the Dangersense Potion");
		easyRecipe = config.getBoolean("Easy Recipe", "Dangersense Potion", false, "Enables the easy recipe for the Dangersense Potion");
		potionDuration = config.getInt("Potion Duration", "Dangersense Potion", 0, 300, 180, "How many seconds the Dangersense Potion lasts"); 
		config.save();
	}
}
