package com.leviathan143.dangersense.recipes;

import com.leviathan143.dangersense.config.Config;
import com.leviathan143.dangersense.items.DSItems;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;

public class DSRecipes 
{
	public static void init()
	{
		if (Config.easyRecipe)
		{
			GameRegistry.addRecipe(new ItemStack(DSItems.dangerPotion), new Object[]
			{
				" E ",
				"BWR",
				"  ",'W', Items.potionitem,'E',Items.spider_eye,'B',Items.bone,'R', Items.rotten_flesh
			});
		}
		else
		{
			GameRegistry.addRecipe(new ItemStack(DSItems.dangerPotion), new Object[]
			{
				" E ",
				"BWR",
				" S ",'W', Items.potionitem,'E',Items.spider_eye,'S', Blocks.soul_sand,'B',Items.bone,'R', Items.rotten_flesh
			});
		}
	}
}
