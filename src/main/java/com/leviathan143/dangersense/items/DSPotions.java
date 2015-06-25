package com.leviathan143.dangersense.items;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.leviathan143.dangersense.config.Config;

import net.minecraft.potion.Potion;


public class DSPotions 
{
	private static final Logger logger = LogManager.getLogger();
	
	public static Potion dangerSense;
	public static void init()
	{
		dangerSense = new SensePotion(Config.potionID, false, 0);
	}
	
	
	public static void expandPotions()
	{
		Potion[] potionTypes = null;

		for (Field f : Potion.class.getDeclaredFields()) 
		{
			f.setAccessible(true);
			try 
			{
				if (f.getName().equals("potionTypes") || f.getName().equals("field_76425_a")) 
				{
					Field modfield = Field.class.getDeclaredField("modifiers");
					modfield.setAccessible(true);
					modfield.setInt(f, f.getModifiers() & ~Modifier.FINAL);

					potionTypes = (Potion[])f.get(null);
					final Potion[] newPotionTypes = new Potion[256];
					System.arraycopy(potionTypes, 0, newPotionTypes, 0, potionTypes.length);
					f.set(null, newPotionTypes);
					logger.info("potionTypes successfully expanded");
				}
			}
			catch (Exception e) 
			{
				logger.error("Failed to expand potionTypes, potions will not be added");
				System.err.println(e);
			}
		}
	}
}
