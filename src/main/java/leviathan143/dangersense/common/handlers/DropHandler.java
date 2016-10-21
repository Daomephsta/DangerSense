package leviathan143.dangersense.common.handlers;

import leviathan143.dangersense.common.config.Config;
import leviathan143.dangersense.common.items.DamageSourceSoulExtraction;
import leviathan143.dangersense.common.items.ItemSoulQuartzDagger;
import leviathan143.dangersense.common.items.ModItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DropHandler 
{
	/*
	 * Prevent entities killed via soul extraction from dropping anything.
	 * Also set the sword's soul.
	 */
	@SubscribeEvent
	public static void onLivingDrops(LivingDropsEvent event)
	{	
		DamageSource source = event.getSource();
		EntityPlayer player = null;
		ItemStack heldItem = null;
		if(source instanceof DamageSourceSoulExtraction)
		{
			if(source.getEntity() instanceof EntityPlayer)
			{
				player = (EntityPlayer) source.getEntity();
				heldItem = player.getHeldItemMainhand();
				
				if(heldItem != null && heldItem.getItem() == ModItems.SOUL_QUARTZ_DAGGER && !ItemSoulQuartzDagger.hasSoul(heldItem) 
						&& !Config.isEntityBlacklisted(event.getEntityLiving()))
				{
					ItemSoulQuartzDagger.setSoul(heldItem, event.getEntityLiving());
					heldItem.damageItem(1, (EntityLivingBase) source.getEntity());
				}
			}
			event.setCanceled(true);
		}
	}
}
