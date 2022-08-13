package com.imtmn.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NioService{
    public static void main(String[] args) throws IOException {
        // 通道
        ServerSocketChannel channel = ServerSocketChannel.open();
        channel.configureBlocking(false);
        channel.bind(new InetSocketAddress(8090));
        System.out.println("启动成功");
        //注册选择器
        Selector selector = Selector.open();
        channel.register(selector, SelectionKey.OP_ACCEPT);
        while(selector.select() > 0){
            Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
            while(keys.hasNext()){
                SelectionKey key = keys.next();
                if(key.isAcceptable()){
                    accept(channel,selector);
                } else if(key.isReadable()){
                    read(key);
                } 
            }
            keys.remove();
        }
        channel.close();
    }

    /**
     * socket 连接准备就绪
     * @param channel
     * @param selector
     * @throws IOException
     */
    private static void accept(ServerSocketChannel channel, Selector selector) throws IOException {
        System.out.println("socket 连接准备就绪");
        SocketChannel sc = channel.accept();
        sc.configureBlocking(false);
        sc.register(selector, SelectionKey.OP_READ);
    }

    /**
     * 读取socket 参数直接丢弃
     * @param key
     * @throws IOException
     */
    private static void read(SelectionKey key) throws IOException {
        System.out.println("读取数据通道数据并丢弃");
        SocketChannel channel = (SocketChannel)key.channel();
        try{
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while(channel.read(buffer)>0){
                buffer.flip();
                System.out.println(new String(buffer.array(),0,buffer.array().length));
                buffer.clear();
            }
        }finally{
            channel.close();
        }
    }

}