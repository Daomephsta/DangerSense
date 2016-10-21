package leviathan143.dangersense.common.packets;

import io.netty.buffer.ByteBuf;
import leviathan143.dangersense.common.capabilities.CapabilityDangerSense;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncSense implements IMessage 
{
	private String sense;
	
	public PacketSyncSense() {}
	
	public PacketSyncSense(String sense) 
	{
		this.sense = sense;
	}
	
	public static class PacketSyncSenseHandler implements IMessageHandler<PacketSyncSense, IMessage>
	{
		@Override
		public IMessage onMessage(final PacketSyncSense message, final MessageContext ctx) 
		{
			Minecraft.getMinecraft().addScheduledTask(new Runnable() 
			{
				@Override
				public void run() 
				{
					processMessage(message, ctx);
				}
				
			});
			return null;
		}		
		
		private void processMessage(PacketSyncSense message, MessageContext ctx)
		{
			Minecraft.getMinecraft().thePlayer.getCapability(CapabilityDangerSense.DANGERSENSE_CAP, null).setSense(message.sense);
		}
	}

	@Override
	public void fromBytes(ByteBuf buf) 
	{
		this.sense = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) 
	{
		ByteBufUtils.writeUTF8String(buf, sense);
	}
}
