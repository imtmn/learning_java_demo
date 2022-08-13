package com.imtmn.reactor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class EchoHandler implements Runnable{
    private final SocketChannel channel;
    private final SelectionKey sk;
    private final ByteBuffer buffer = ByteBuffer.allocate(1024);
    private static int RECEVING = 0,SENDING = 1;
    int state = RECEVING;

    public EchoHandler(Selector selector, SocketChannel channel) throws IOException {
        this.channel = channel;
        channel.configureBlocking(false);
        sk = channel.register(selector, 0);
        sk.attach(this);
        sk.interestOps(SelectionKey.OP_READ);
        selector.wakeup();
    }

    @Override
    public void run() {
        try {
            if(state == RECEVING){
                while(channel.read(buffer) > 0){
                    System.out.println(new String(buffer.array(),0,buffer.array().length));
                    buffer.flip();
                    sk.interestOps(SelectionKey.OP_WRITE);
                    state = SENDING;
                }
            }else if(state == SENDING){
                channel.write(buffer);
                buffer.clear();
                sk.interestOps(SelectionKey.OP_READ);
                state = RECEVING;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    
}
