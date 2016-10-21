package leviathan143.dangersense.common.recipes;

import java.util.List;

import com.google.common.collect.Lists;

import leviathan143.dangersense.common.effects.EffectDangerSense;
import leviathan143.dangersense.common.items.ItemSoulQuartzDagger;
import leviathan143.dangersense.common.items.ModItems;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

public class RecipeHostileDetector implements IRecipe 
{	
	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) 
	{
		List<String> requiredSouls = Lists.newArrayList("Skeleton", "Creeper", "Spider", "Zombie");
		for(int slot = 0; slot < inv.getSizeInventory(); slot++)
		{
			ItemStack slotStack = inv.getStackInSlot(slot);
			if(slotStack != null)
				requiredSouls.remove(ItemSoulQuartzDagger.getSoul(slotStack));
		}
		return requiredSouls.isEmpty();
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) 
	{
		ItemStack dagger = new ItemStack(ModItems.SOUL_QUARTZ_DAGGER);
		ItemSoulQuartzDagger.setSoul(dagger, EffectDangerSense.DETECT_ALL_HOSTILES);
		return dagger;
	}

	@Override
	public int getRecipeSize() 
	{
		return 5;
	}

	@Override
	public ItemStack getRecipeOutput() 
	{
		return null;
	}

	@Override
	public ItemStack[] getRemainingItems(InventoryCrafting inv) 
	{
		ItemStack[] consumedItems = new ItemStack[inv.getSizeInventory()];
		for(int slot = 0; slot < inv.getSizeInventory(); slot++)
		{
			ItemStack stack = inv.getStackInSlot(slot);
			if(stack != null && stack.getItem().hasContainerItem(stack))
			{
				consumedItems[slot] = ForgeHooks.getContainerItem(stack);
			}
		}
		return consumedItems;
	}

}
