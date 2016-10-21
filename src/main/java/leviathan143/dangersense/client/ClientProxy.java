package leviathan143.dangersense.client;

import leviathan143.dangersense.common.CommonProxy;
import leviathan143.dangersense.common.DangerSenseMain.Constants;
import leviathan143.dangersense.common.handlers.RenderHandler;
import leviathan143.dangersense.common.items.ModItems;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

public class ClientProxy extends CommonProxy 
{
	private static final RenderHandler RENDER_HANDLER = new RenderHandler(); 
	
	@Override
	public void postInit(FMLPostInitializationEvent event) 
	{
		super.postInit(event);
		MinecraftForge.EVENT_BUS.register(RENDER_HANDLER);
	}

	@Override
	public void registerRenders() 
	{
		registerItemRender(ModItems.SOUL_QUARTZ_DAGGER);
		ModelLoader.setCustomModelResourceLocation(ModItems.SOUL_QUARTZ, 0, new ModelResourceLocation(Constants.MODID + ":soulQuartz" + "_unsmelted", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.SOUL_QUARTZ, 1, new ModelResourceLocation(Constants.MODID + ":soulQuartz" + "_smelted", "inventory"));
	}

	private void registerItemRender(Item item)
	{
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}
}
