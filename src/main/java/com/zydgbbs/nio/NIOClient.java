package com.zydgbbs.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NIOClient {
    public static void main(String[] args) throws Exception{
        //得到一个网络通道
        SocketChannel socketChannel = SocketChannel.open();
        //设置非阻塞模式
        socketChannel.configureBlocking(false);
        //提供服务器端的IP和端口
        InetSocketAddress inetSocketAddress = new InetSocketAddress("localhost", 6666);
        //连接服务器
        if(!socketChannel.connect(inetSocketAddress)){
            while (!socketChannel.finishConnect()){
                System.out.println("因为连接需要时间，客户端不会阻塞，可以做其他工作");
            }
        }
        //如果连接成功，就发送数据
        String str = "hello，资源帝国~";
        ByteBuffer buffer = ByteBuffer.wrap(str.getBytes());
        System.out.println("发送的数据是："+new String(buffer.array()));
        //发送数据，将buffer数据写入Channel
        socketChannel.write(buffer);
        //让客户端停在这
        System.in.read();

    }
}
