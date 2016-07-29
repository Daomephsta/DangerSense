package leviathan143.dangersense.client;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import leviathan143.dangersense.common.CommonProxy;
import leviathan143.dangersense.common.handlers.DangerSenseHandler;
import leviathan143.dangersense.common.items.ModItems;

public class ClientProxy extends CommonProxy 
{
	@Override
	public void postInit(FMLPostInitializationEvent event) 
	{
		super.postInit(event);
		MinecraftForge.EVENT_BUS.register(new DangerSenseHandler());
	}
	
	@Override
	public void registerRenders() 
	{
		ModelLoader.setCustomModelResourceLocation(ModItems.dangerPotion, 0
				, new ModelResourceLocation(ModItems.dangerPotion.getRegistryName(), "inventory"));
	}
}
