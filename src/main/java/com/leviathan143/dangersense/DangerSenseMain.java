package com.leviathan143.dangersense;

import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.MinecraftForge;

import com.leviathan143.dangersense.config.Config;
import com.leviathan143.dangersense.handlers.DangerSenseHandler;
import com.leviathan143.dangersense.items.DSItems;
import com.leviathan143.dangersense.items.DSPotions;
import com.leviathan143.dangersense.lib.DSConstants;
import com.leviathan143.dangersense.recipes.DSRecipes;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = DSConstants.MODID, name = DSConstants.MODNAME, version = DSConstants.VERSION)
public class DangerSenseMain 
{	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		Config.preinit(event);
		DSPotions.expandPotions();
		DSPotions.init();
		DSItems.init();
		DSRecipes.init();
	}
	
	@Mod.EventHandler
	public void Init(FMLInitializationEvent event)
	{
		ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST, new WeightedRandomChestContent(new ItemStack(DSItems.dangerPotion), 1, 1, 100));
		ChestGenHooks.addItem(ChestGenHooks.MINESHAFT_CORRIDOR, new WeightedRandomChestContent(new ItemStack(DSItems.dangerPotion), 1, 1, 100));
		ChestGenHooks.addItem(ChestGenHooks.PYRAMID_DESERT_CHEST, new WeightedRandomChestContent(new ItemStack(DSItems.dangerPotion), 1, 1, 100));
		ChestGenHooks.addItem(ChestGenHooks.PYRAMID_JUNGLE_CHEST, new WeightedRandomChestContent(new ItemStack(DSItems.dangerPotion), 1, 1, 100));
		ChestGenHooks.addItem(ChestGenHooks.STRONGHOLD_CORRIDOR, new WeightedRandomChestContent(new ItemStack(DSItems.dangerPotion), 1, 1, 100));
		ChestGenHooks.addItem(ChestGenHooks.STRONGHOLD_CROSSING, new WeightedRandomChestContent(new ItemStack(DSItems.dangerPotion), 1, 1, 100));
		ChestGenHooks.addItem(ChestGenHooks.STRONGHOLD_LIBRARY, new WeightedRandomChestContent(new ItemStack(DSItems.dangerPotion), 1, 1, 100));
	}
	
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(new DangerSenseHandler());
		FMLCommonHandler.instance().bus().register(new DangerSenseHandler());
	}
}
