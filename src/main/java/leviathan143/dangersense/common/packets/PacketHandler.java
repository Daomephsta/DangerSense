package leviathan143.dangersense.common.packets;

import leviathan143.dangersense.common.DangerSenseMain.Constants;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler 
{
	public static final SimpleNetworkWrapper CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(Constants.MODID);
	private static int packetID = 0;
	
	public static void registerPackets()
	{
		registerPacket(PacketSyncSense.PacketSyncSenseHandler.class, PacketSyncSense.class, Side.CLIENT);
	}

	private static <REQ extends IMessage, REPLY extends IMessage> void registerPacket(Class<? extends IMessageHandler<REQ, REPLY>> messageHandler, Class<REQ> requestMessageType, Side side)
	{
		CHANNEL.registerMessage(messageHandler, requestMessageType, packetID, side);
		packetID++;
	}
}
