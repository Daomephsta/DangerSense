package com.leviathan143.dangersense;

import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.MinecraftForge;

import com.leviathan143.dangersense.lib.DSConstants;
import com.leviathan143.dangersense.lib.DangerSenseHandler;

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
		
	}
	
	@Mod.EventHandler
	public void Init(FMLInitializationEvent event)
	{
		
	}
	
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(new DangerSenseHandler());
		FMLCommonHandler.instance().bus().register(new DangerSenseHandler());
		BiomeDictionary.registerAllBiomes();
	}
}
