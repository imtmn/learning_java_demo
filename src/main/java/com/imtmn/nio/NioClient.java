package com.imtmn.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NioClient {
    public static void main(String[] args) throws IOException {
        InetSocketAddress address = new InetSocketAddress("127.0.0.1",8090);
        SocketChannel channel = SocketChannel.open(address);
        try{
            channel.configureBlocking(false);
            while(!channel.finishConnect()){
                System.out.println("等待连接完成……");
            }
            System.out.println("连接成功,开始写入数据");
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            buffer.put("hello nio".getBytes());
            buffer.flip();
            channel.write(buffer);
        }finally{
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            channel.shutdownOutput();
            channel.close();
        }
    }
}
