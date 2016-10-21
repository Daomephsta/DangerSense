package leviathan143.dangersense.common.items;

import java.util.List;

import leviathan143.dangersense.common.DangerSenseMain.Constants;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemResource extends Item 
{	
	public ItemResource() 
	{
		this.setHasSubtypes(true);
	}
	
	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) 
	{
		subItems.add(new ItemStack(this, 1, 0));
		subItems.add(new ItemStack(this, 1, 1));
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) 
	{
		return "item." + Constants.MODID + (stack.getItem().getDamage(stack) == 0 ? ".quartzWithSoulSand" : ".soulQuartzCrystal");
	}
}

