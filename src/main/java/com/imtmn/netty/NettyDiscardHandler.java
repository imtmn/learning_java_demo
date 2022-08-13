package com.imtmn.netty;

import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class NettyDiscardHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
       ByteBuf in = (ByteBuf)msg;
       Logger.getGlobal().info("收到消息：");
       while(in.isReadable()){
        Logger.getGlobal().info(""+(char)in.readByte());
       }
    }
}
