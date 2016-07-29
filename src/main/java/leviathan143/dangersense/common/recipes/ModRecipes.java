package leviathan143.dangersense.common.recipes;

import leviathan143.dangersense.common.config.Config;
import leviathan143.dangersense.common.items.ModItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModRecipes 
{
	public static void init()
	{
		if (Config.easyRecipe)
		{
			GameRegistry.addRecipe(new ItemStack(ModItems.dangerPotion), new Object[]
			{
				" E ",
				"BWR",
				"  ",'W', Items.POTIONITEM,'E',Items.SPIDER_EYE,'B',Items.BONE,'R', Items.ROTTEN_FLESH
			});
		}
		else
		{
			GameRegistry.addRecipe(new ItemStack(ModItems.dangerPotion), new Object[]
			{
				" E ",
				"BWR",
				" S ",'W', Items.POTIONITEM,'E',Items.SPIDER_EYE,'S', Blocks.SOUL_SAND,'B',Items.BONE,'R', Items.ROTTEN_FLESH
			});
		}
	}
}
