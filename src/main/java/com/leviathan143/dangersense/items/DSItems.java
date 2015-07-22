package com.leviathan143.dangersense.items;

import net.minecraft.item.Item;

public class DSItems 
{
	public static Item dangerPotion;
	public static Item soulQuartzSword;
	
	public static void init()
	{
		dangerPotion = new DangerPotion();
	}
}
