package ru.nbdev.cloud.common;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import ru.nbdev.cloud.common.messages.AuthErrorMessage;
import ru.nbdev.cloud.common.messages.AuthOkMessage;
import ru.nbdev.cloud.common.messages.ClientAuthMessage;
import ru.nbdev.cloud.common.messages.ClientRegisterMessage;

public class InboundAuthHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client connected...");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg == null) {
            return;
        }

        try {
            System.out.println(msg.getClass());
            if (msg instanceof ClientAuthMessage) {
                userAuth(ctx, (ClientAuthMessage) msg);
            } else if (msg instanceof ClientRegisterMessage) {
                userRegister(ctx, (ClientRegisterMessage) msg);
            } else {
                System.out.println("Need auth firstly!");
                ctx.writeAndFlush(new AuthErrorMessage());
                ctx.close();
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    private void userAuth(ChannelHandlerContext ctx, ClientAuthMessage msg) {
        if (msg.getName().equals("test") && msg.getPass().equals("pass")) {
            System.out.println("Auth OK");
            ctx.writeAndFlush(new AuthOkMessage());
            ctx.pipeline().remove(this);
        } else {
            System.out.println("Auth Error");
            ctx.writeAndFlush(new AuthErrorMessage());
        }
    }

    private void userRegister(ChannelHandlerContext ctx, ClientRegisterMessage msg) {

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
