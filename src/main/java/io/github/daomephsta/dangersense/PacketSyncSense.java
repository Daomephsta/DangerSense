package io.github.daomephsta.dangersense;


import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

public class PacketSyncSense 
{
	private EntityType<?> sense;
	
	private PacketSyncSense(EntityType<?> sense) 
	{
		this.sense = sense;
	}
	
	public static void registerAsIndex(int index)
	{
	    DangerSense.CHANNEL.<PacketSyncSense>registerMessage(index, PacketSyncSense.class, 
            PacketSyncSense::toBytes, PacketSyncSense::fromBytes, (packet, context) ->
            {
                NetworkEvent.Context ctx = context.get();
                ctx.enqueueWork(packet::receive);
                ctx.setPacketHandled(true);
            });
	}
	
	public static void send(EntityType<?> target, ServerPlayerEntity serverPlayer)
	{
	    DangerSense.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new PacketSyncSense(target));
	}
	
	private void receive()
	{
	    DangerSenseData.get(Minecraft.getInstance().player)
	        .ifPresent(dangerSense -> dangerSense.setTarget(sense));
	}

	private static PacketSyncSense fromBytes(PacketBuffer bytes) 
    {
        EntityType<?> target = bytes.readBoolean() 
            ? bytes.readRegistryIdSafe(EntityType.class)
            : null;
        return new PacketSyncSense(target);
    }
	
	private void toBytes(PacketBuffer bytes) 
	{
	    if (sense != null)
        {
            bytes.writeBoolean(true);
            bytes.writeRegistryId(sense);
        }
        else bytes.writeBoolean(false);
	}
}
