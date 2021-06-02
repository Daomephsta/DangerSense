package io.github.daomephsta.dangersense.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.STitlePacket;
import net.minecraft.network.play.server.STitlePacket.Type;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;

public abstract class Chat
{
    private Chat() {}

    public static void sendMessage(PlayerEntity recipient, ITextComponent message)
    {
        if (!recipient.level.isClientSide)
            recipient.sendMessage(message, Util.NIL_UUID);
    }
    
    public static void pushToActionbar(PlayerEntity recipient, ITextComponent message)
    {
        if (!recipient.level.isClientSide)
            ((ServerPlayerEntity) recipient).connection.send(new STitlePacket(Type.ACTIONBAR, message));
    }
}
