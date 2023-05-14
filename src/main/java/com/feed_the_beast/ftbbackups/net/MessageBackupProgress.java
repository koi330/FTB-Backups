package com.feed_the_beast.ftbbackups.net;

import com.feed_the_beast.ftbbackups.FTBBackupsClientEventHandler;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

/**
 * @author LatvianModder
 */
public class MessageBackupProgress implements IMessage {

    public int current, total;

    public MessageBackupProgress() {}

    public MessageBackupProgress(int c, int t) {
        current = c;
        total = t;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(total);
        buf.writeInt(current);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        total = buf.readInt();
        current = buf.readInt();
    }

    public static class Handler implements IMessageHandler<MessageBackupProgress, IMessage> {

        @Override
        public IMessage onMessage(MessageBackupProgress message, MessageContext ctx) {
            FTBBackupsClientEventHandler.currentBackupFile = message.current;
            FTBBackupsClientEventHandler.totalBackupFiles = message.total;
            return null;
        }
    }
}
