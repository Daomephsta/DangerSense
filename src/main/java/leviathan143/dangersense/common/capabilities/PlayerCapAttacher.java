package leviathan143.dangersense.common.capabilities;

import leviathan143.dangersense.common.packets.PacketHandler;
import leviathan143.dangersense.common.packets.PacketSyncSense;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PlayerCapAttacher 
{
	@SubscribeEvent
	public static void attachCaps(AttachCapabilitiesEvent.Entity event)
	{
		if(event.getEntity() instanceof EntityPlayer)
		{
			event.addCapability(PlayerDangerSense.CAP_KEY, new PlayerDangerSense());
		}
	}

	@SubscribeEvent
	public static void syncDangerSenseCap(EntityJoinWorldEvent event)
	{
		if (!(event.getEntity() instanceof EntityPlayer))
			return;
		EntityPlayer player = (EntityPlayer) event.getEntity();
		if(!player.worldObj.isRemote)
		{
			PacketHandler.CHANNEL.sendTo(new PacketSyncSense(player.getCapability(CapabilityDangerSense.DANGERSENSE_CAP, null).getSense())
					, (EntityPlayerMP) player);
		}
	}
}
