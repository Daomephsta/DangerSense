package leviathan143.dangersense.common.items;

import leviathan143.dangersense.common.config.Config;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class DangerPotion extends Item
{	
	@Override
	public ItemStack onItemUseFinish(ItemStack itemstackIn, World worldIn, EntityLivingBase entityIn) 
	{
		if(entityIn instanceof EntityPlayer)
		{
			entityIn.addPotionEffect(new PotionEffect(ModPotions.dangerSense, Config.potionDuration * 20));
			if(worldIn.isRemote)			
			{
				entityIn.addChatMessage(new TextComponentTranslation("dangersense.potion.startMessage"));
			}
			if (!((EntityPlayer) entityIn).isCreative())
				return new ItemStack(Items.GLASS_BOTTLE);
		}
		return itemstackIn;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack itemstack)
	{
		return EnumAction.DRINK;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack itemstack)
	{
		return 32;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) 
	{
		playerIn.setActiveHand(hand);
		return ActionResult.newResult(EnumActionResult.SUCCESS, itemStackIn);
	}


}
