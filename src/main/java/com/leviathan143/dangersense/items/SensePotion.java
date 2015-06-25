package com.leviathan143.dangersense.items;


import net.minecraft.potion.Potion;

public class SensePotion extends Potion
{
	public SensePotion(int id, boolean isBad, int colour)
	{
		super(id, isBad, colour);
		setPotionName("potion.dangersense");
		setIconIndex(1, 2);
	}
}
