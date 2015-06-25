package com.leviathan143.dangersense;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import com.leviathan143.dangersense.config.Config;
import com.leviathan143.dangersense.lib.DSConstants;
import com.leviathan143.dangersense.lib.DangerSenseHandler;

@Mod(modid = DSConstants.MODID, name = DSConstants.MODNAME, version = DSConstants.VERSION)
public class DangerSenseMain 
{	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		Config.preinit(event);
		DangerSenseHandler.setGuiOpen();
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
	}
}
