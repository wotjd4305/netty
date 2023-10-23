package org.example.chapter1;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;


public class DiscardServerHandler extends SimpleChannelInboundHandler<Object> { @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        //nothing
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
