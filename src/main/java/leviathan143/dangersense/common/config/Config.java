package leviathan143.dangersense.common.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class Config 
{
	private static final String CAT_MOB_DETECTION = "Mob Detection";
	private static final String CAT_POTION = "Dangersense Potion";
	private static final String CAT_EFFECT = "Dangersense Effect";
	private static final String CAT_CUSTOMISATION = "Customisation";
	
	
	private static Configuration config;
	
	public static int scanTimeInTicks;
	public static float detectionRange;
	public static float lowerBounds;
	public static List<String> entityBlackWhitelist = new ArrayList<String>();
	public static boolean whitelistMode;
	
	
	public static boolean hostilePotion;
	public static int potionDuration;
	
	public static float overlayColourR;
	public static float overlayColourG;
	public static float overlayColourB;
	
	private static ArrayList<String> custPropOrderHolder = new ArrayList<String>();
	private static ArrayList<String> EDPropOrderHolder = new ArrayList<String>();
	
	
	static
	{
		Collections.addAll(custPropOrderHolder, "Overlay Colour Red", "Overlay Colour Green", "Overlay Colour Blue");
		Collections.addAll(EDPropOrderHolder, "Detection Range", "Lower Bounds", "Scan Time", "Entity Blacklist/Whitelist", "Whitelist Mode");
	}
	
	public static void preinit(FMLPreInitializationEvent event)
	{
		config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();

		//Entity Detection
		config.setCategoryPropertyOrder(CAT_MOB_DETECTION, EDPropOrderHolder);
		config.addCustomCategoryComment(CAT_MOB_DETECTION, "Bosses cannot be detected. To get an entities name, you can right click it with a Soul Quartz Dagger while in Creative");
		detectionRange = config.getFloat("Detection Range", CAT_MOB_DETECTION, 5, 3, 10, "How near to the player a hostile mob must be to activate the danger sense");
		lowerBounds = config.getFloat("Lower Bounds", CAT_MOB_DETECTION, 2, 0, 3, "How far down the  algorithm looks for mobs");
		scanTimeInTicks = config.getInt("Scan Time", CAT_MOB_DETECTION, 20, 10, 100, "The number of ticks that will pass between checks for hostile mobs");
		Collections.addAll(entityBlackWhitelist, config.getStringList("Entity Blacklist/Whitelist", CAT_MOB_DETECTION, new String[]{}, "Add an entity's name to this list to blacklist it."
				+ "Blacklisted entities cannot have their souls absorbed  by the Soul Quartz Dagger and they will not be detected by the Dangersense effect."));
		whitelistMode = config.getBoolean("Whitelist Mode", CAT_MOB_DETECTION, false, "If true, the entity blacklist becomes a whitelist.");
		
		//Potion
		hostilePotion = config.getBoolean("Hostile Potion", CAT_POTION, false, "Enables/disables a recipe the hostile potions recipe(detects all hostiles)");

		//Effect
		potionDuration = config.getInt("Potion Duration", CAT_EFFECT, 150, 0, 300, "How many seconds the Dangersense effect lasts");
		
		//Customisation
		config.setCategoryPropertyOrder(CAT_CUSTOMISATION, custPropOrderHolder);
		overlayColourR = config.getInt("Overlay Colour Red", CAT_CUSTOMISATION, 255, 0, 255, "The red component of the overlay color");
		overlayColourG = config.getInt("Overlay Colour Green", CAT_CUSTOMISATION, 0, 0, 255, "The green component of the overlay color");
		overlayColourB = config.getInt("Overlay Colour Blue", CAT_CUSTOMISATION, 0, 0, 255, "The blue component of the overlay color");
		
		config.save();
		parseOverlayColour();
	}
	
	private static void parseOverlayColour()
	{
		overlayColourR = 1.0F - overlayColourR / 255.0F;
		overlayColourG = 1.0F - overlayColourG / 255.0F;
		overlayColourB = 1.0F - overlayColourB / 255.0F;
	}
	
	public static boolean isEntityBlacklisted(EntityLivingBase living)
	{
		String entityName = EntityList.getEntityString(living);
		if (living.isNonBoss())
		{
			if (Config.whitelistMode)
				return !entityBlackWhitelist.contains(entityName);
			else
				return entityBlackWhitelist.contains(entityName);
		}
		return false;
	}
}
