package com.leviathan143.dangersense.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

import com.leviathan143.dangersense.lib.DSConstants;

import cpw.mods.fml.common.registry.GameRegistry;

public class DangerPotion extends Item
{
	private String name = "dangerPotion";
	
	public DangerPotion()
	{
		GameRegistry.registerItem(this, name);
		setUnlocalizedName(DSConstants.MODID + "_" + name);
		setTextureName(DSConstants.MODID + ":" + name);
	}
	
	@Override
	public ItemStack onEaten(ItemStack itemstack, World world, EntityPlayer player) 
	{
		player.addPotionEffect(new PotionEffect(DSPotions.dangerSense.id, 6000));
		if (player.capabilities.isCreativeMode)
		{
			return itemstack;
		}
		else
		{
			return new ItemStack(Items.glass_bottle);
		}
	}
	
	public EnumAction getItemUseAction(ItemStack itemstack)
	{
		return EnumAction.drink;
	}
	
	public int getMaxItemUseDuration(ItemStack itemstack)
	{
		return 32;
	}
	
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player)
    {
        player.setItemInUse(itemstack, this.getMaxItemUseDuration(itemstack));
        return itemstack;
    }
	
	
}
