package ru.nbdev.cloud.common;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import ru.nbdev.cloud.common.protocol.Message;

public class OutboundHandler extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        Message message = (Message) msg;

        ByteBufAllocator al = new PooledByteBufAllocator();
        ByteBuf buf = al.buffer();// buffer(arr.length);

        Message.write(message, buf);

        ctx.writeAndFlush(buf);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}

