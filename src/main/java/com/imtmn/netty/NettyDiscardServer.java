package com.imtmn.netty;

import java.util.logging.Logger;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyDiscardServer {
    private final int serverPort;
    ServerBootstrap bootstrap = new ServerBootstrap();
    
    public NettyDiscardServer(int port){
        this.serverPort = port;
    }

    public void runServer() throws InterruptedException{
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try{
            bootstrap.group(bossGroup,workGroup);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.localAddress(serverPort);
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
    
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    // TODO Auto-generated method stub
                    ch.pipeline().addLast(new NettyDiscardHandler());
                }
                
            });
    
            ChannelFuture channelFuture = bootstrap.bind().sync();
            Logger.getGlobal().info("服务启动成功");
            ChannelFuture closeFuture = channelFuture.channel().closeFuture();
            closeFuture.sync();
        }finally{
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
        
    }


    public static void main(String[] args) throws InterruptedException {
        new NettyDiscardServer(8090).runServer();;
    }

}
