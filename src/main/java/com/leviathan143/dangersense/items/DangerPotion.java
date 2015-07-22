package com.leviathan143.dangersense.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;

import com.leviathan143.dangersense.config.Config;
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
		setCreativeTab(CreativeTabs.tabBrewing);
	}
	
	@Override
	public ItemStack onEaten(ItemStack itemstack, World world, EntityPlayer player) 
	{
		player.addPotionEffect(new PotionEffect(DSPotions.dangerSense.id, Config.potionDuration * 20));
		if(world.isRemote)			
		{
			player.addChatMessage(new ChatComponentTranslation("dangersense.potion.startMessage", new Object[0]));
		}
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
