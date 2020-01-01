package com.zydgbbs.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {
    public static void main(String[] args) throws Exception{
        //创建ServerSocketChannel->ServerSocket
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //得到一个Selector对象
        Selector selector = Selector.open();
        //绑定端口6666，在服务器端监听
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));
        //设置为非阻塞
        serverSocketChannel.configureBlocking(false);
        //把ServerSocketChannel注册到Selector上，关心事件为OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("serverSocketChannel注册后，注册到的SelectionKey的数量为："+selector.keys().size());
        //循环等待客户端连接
        while (true){
            //这里我们等待1秒，如果没有事件发生，返回
            if(selector.select(1000)==0){//没有事件发生
                System.out.println("服务器等待了1秒，无连接...");
                continue;
            }
            //如果返回的不是0，就获取到相关的selectionKey集合
            //1 如果返回的>0，表示已经获取到关注的事件
            //2 selector.selectedKeys()返回关注事件的集合
            //3 通过selectionKeys反向获取通道
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            System.out.println("有事件触发的SelectionKey的数量为："+selectionKeys.size());
            //遍历Set<SelectionKey>，使用迭代器遍历
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
            while (keyIterator.hasNext()){
                //获取到SelectionKey
                SelectionKey key = keyIterator.next();
                //根据key对应的通道发生的事件做相应的处理
                if(key.isAcceptable()){//如果是OP_ACCEPT事件，有新客户端连接
                    //给该客户端生成一个SocketChannel
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    //将SocketChannel设为非阻塞的
                    socketChannel.configureBlocking(false);
                    //将当前的SocketChannel注册到Selector，关注事件为OP_READ，同时给该SocketChannel关联一个Buffer
                    socketChannel.register(selector,SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                    System.out.println("客户端连接成功，生成了一个SocketChannel，hashCode="+socketChannel.hashCode());
                    System.out.println("客户端连接后，注册到的SelectionKey的数量为："+selector.keys().size());
                }
                //发生了OP_READ
                if(key.isReadable()){
                    //通过key反向获取到对应的Channel
                    SocketChannel channel = (SocketChannel)key.channel();
                    //获取到该Channel关联的buffer

                    //方法一
                    /*ByteBuffer buffer = ByteBuffer.allocate(512);
                    int read = channel.read(buffer);
                    System.out.println("from 客户端 "+new String(buffer.array(),0,read));*/

                    //方法二
                    ByteBuffer buffer = (ByteBuffer)key.attachment();
                    channel.read(buffer);
                    System.out.println("from 客户端 "+new String(buffer.array()));
                }
                //手动从集合中移除当前的selectKey，防止重复操作
                keyIterator.remove();

            }

        }

    }
}
