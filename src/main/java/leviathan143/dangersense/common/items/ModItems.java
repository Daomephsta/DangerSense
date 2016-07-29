package leviathan143.dangersense.common.items;

import leviathan143.dangersense.common.DangerSenseMain.Constants;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModItems 
{
	public static Item dangerPotion;
	public static Item soulQuartzSword;
	
	public static void register()
	{
		dangerPotion = registerItem(new DangerPotion(), "dangerPotion");
	}
	
	private static Item registerItem(Item item, String name)
	{
		item.setUnlocalizedName(Constants.MODID + "." +  name);
		item.setRegistryName(name);
		item.setCreativeTab(CreativeTabs.BREWING);
		return GameRegistry.register(item);
	}
}
