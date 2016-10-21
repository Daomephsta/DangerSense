package leviathan143.dangersense.common;

import leviathan143.dangersense.common.DangerSenseMain.Constants;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Constants.MODID, name = Constants.MODNAME, version = Constants.VERSION)
public class DangerSenseMain 
{	
	public class Constants
	{
		public static final String MODNAME = "DangerSense";
		public static final  String  MODID = "dangersense";
		public static final  String  VERSION = "1.0.0";
		public static final  String  CLIENT_PROXY_PATH = "leviathan143.dangersense.client.ClientProxy";
		public static final  String  SERVER_PROXY_PATH = "leviathan143.dangersense.common.CommonProxy";
	}

	@SidedProxy(serverSide=Constants.SERVER_PROXY_PATH, clientSide=Constants.CLIENT_PROXY_PATH)
	public static CommonProxy proxy;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		proxy.preInit(event);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event)
	{
		proxy.init(event);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		proxy.postInit(event);
	}
}
