package leviathan143.dangersense.common.items;

import leviathan143.dangersense.common.DangerSenseMain.Constants;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModItems 
{
	public static Item SOUL_QUARTZ;
	public static Item SOUL_QUARTZ_DAGGER;
	
	public static void register()
	{
		SOUL_QUARTZ = registerItem(new ItemResource(), "soulQuartz").setCreativeTab(CreativeTabs.MATERIALS);
		SOUL_QUARTZ_DAGGER = registerItem(new ItemSoulQuartzDagger(), "soulQuartzDagger").setCreativeTab(CreativeTabs.COMBAT);
	}
	
	private static Item registerItem(Item item, String name)
	{
		item.setUnlocalizedName(Constants.MODID + "." +  name);
		item.setRegistryName(name);
		return GameRegistry.register(item);
	}
}
