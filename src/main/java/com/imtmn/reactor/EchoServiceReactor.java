package com.imtmn.reactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class EchoServiceReactor implements Runnable{
    Selector selector;
    ServerSocketChannel socketChannel;

    EchoServiceReactor () throws IOException{
        socketChannel = ServerSocketChannel.open();
        socketChannel.bind(new InetSocketAddress(8090));
        socketChannel.configureBlocking(false);
        selector = Selector.open();
        SelectionKey sk = socketChannel.register(selector, SelectionKey.OP_ACCEPT);
        sk.attach(new AcceptorHandler());
    }

    @Override
    public void run() {
        try{
            while(!Thread.interrupted()){
                selector.select();
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = selectedKeys.iterator();
                while(it.hasNext()){
                    SelectionKey key = it.next();
                    dispatch(key);
                }
                selectedKeys.clear();
            }
        }catch (IOException e) {
            e.printStackTrace();
        } 
        
    }

    private void dispatch(SelectionKey key) {
        Runnable handler = (Runnable)key.attachment();
        if(handler != null){
            handler.run();
        }
    }

    class AcceptorHandler implements Runnable{

        @Override
        public void run() {
           try {
                SocketChannel channel = socketChannel.accept();
                if(channel != null){
                    new EchoHandler(selector,channel);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            
        }
        
    }

    public static void main(String[] args) throws IOException {
        new Thread(new EchoServiceReactor()).start();
    }
}
