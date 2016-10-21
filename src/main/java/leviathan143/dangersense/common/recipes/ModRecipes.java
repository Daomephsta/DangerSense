package leviathan143.dangersense.common.recipes;

import leviathan143.dangersense.common.DangerSenseMain.Constants;
import leviathan143.dangersense.common.config.Config;
import leviathan143.dangersense.common.items.ModItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.RecipeSorter.Category;

public class ModRecipes 
{
	public static void init()
	{
		GameRegistry.addRecipe(new ItemStack(ModItems.SOUL_QUARTZ_DAGGER),
					"Q",
					"Q",
					"S"
					,'Q', new ItemStack(ModItems.SOUL_QUARTZ, 1, 1),'S', Items.GLASS_BOTTLE);
		GameRegistry.addRecipe(new ItemStack(ModItems.SOUL_QUARTZ, 1, 0),
				" S ",
				"SQS",
				" S "
				,'Q', Items.QUARTZ,'S', Blocks.SOUL_SAND);
		
		GameRegistry.addSmelting(new ItemStack(ModItems.SOUL_QUARTZ, 1, 0), new ItemStack(ModItems.SOUL_QUARTZ, 1, 1), 0.2F);
		if(Config.hostilePotion)
		{
			RecipeSorter.register(Constants.MODID + ":hostileDetector", RecipeHostileDetector.class, Category.SHAPELESS, "after:minecraft:shapeless");
			GameRegistry.addRecipe(new RecipeHostileDetector());
		}
	}
}
