package leviathan143.dangersense.common.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class Config 
{
	private static Configuration config;
	public static int scanTimeInTicks;
	public static float detectionRange;
	public static float lowerBounds;
	
	public static boolean easyRecipe;
	public static int potionDuration;
	
	public static float overlayColourR;
	public static float overlayColourG;
	public static float overlayColourB;
	
	public static void preinit(FMLPreInitializationEvent event)
	{
		config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		//Mod Detection
		detectionRange = config.getFloat("Detection Range", "Mob Detection", 5, 3, 10, "How near to the player a hostile mob must be to activate the danger sense");
		lowerBounds = config.getFloat("Lower Bounds", "Mob Detection", 2, 0, 3, "How far down the mob detection algorithm looks for mobs");
		scanTimeInTicks = config.getInt("Scan Time", "Mob Detection", 20, 10, 100, "The number of ticks that will pass between checks for hostile mobs");
		//Dangersense Potion
		easyRecipe = config.getBoolean("Easy Recipe", "Dangersense Potion", false, "Enables the easy recipe for the Dangersense Potion");
		potionDuration = config.getInt("Potion Duration", "Dangersense Potion", 0, 300, 180, "How many seconds the Dangersense Potion lasts");
		//Customisation
		overlayColourB = config.getInt("Overlay Colour Blue", "Customisation", 0, 0, 255, "The blue component of the overlay color");
		overlayColourG = config.getInt("Overlay Colour Green", "Customisation", 0, 0, 255, "The green component of the overlay color");
		overlayColourR = config.getInt("Overlay Colour Red", "Customisation", 255, 0, 255, "The red component of the overlay color");
		
		config.save();
		parseOverlayColour();
	}
	
	private static void parseOverlayColour()
	{
		overlayColourR = 1.0F - overlayColourR / 255.0F;
		overlayColourG = 1.0F - overlayColourG / 255.0F;
		overlayColourB = 1.0F - overlayColourB / 255.0F;
	}
}
