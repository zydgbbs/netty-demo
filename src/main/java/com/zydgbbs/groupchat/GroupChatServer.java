package com.zydgbbs.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class GroupChatServer {
    //定义群聊属性
    private Selector selector;
    private ServerSocketChannel listenChannel;
    private static final int PORT = 6667;

    //构造器，完成初始化工作
    public GroupChatServer(){
        try{

            //得到选择器
            selector = Selector.open();
            //初始化ServerSocketChannel
            listenChannel = ServerSocketChannel.open();
            //绑定端口
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            //设置非阻塞模式
            listenChannel.configureBlocking(false);
            //将ServerSocketChannel注册到Selector上
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //监听
    public void listen(){
        try{
            //循环处理
            while (true){
                int count = selector.select();
                if(count>0){//有事件要处理
                    //遍历得到的SelectionKey集合
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()){
                        //取出SelectionKey
                        SelectionKey key = iterator.next();
                        //监听到accept
                        if(key.isAcceptable()){
                            SocketChannel sc = listenChannel.accept();
                            sc.configureBlocking(false);
                            //将该sc注册到Selector上
                            sc.register(selector,SelectionKey.OP_READ);
                            //提示
                            System.out.println(sc.getRemoteAddress()+"上线了...");
                        }
                        //通道发生Read事件，即通道是可读状态
                        if(key.isReadable()){
                            //处理读（专门写方法）
                            readData(key);
                        }
                        //当前的key删除，防止重复处理
                        iterator.remove();
                    }

                }else{
                    System.out.println("等待中...");
                }

            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {

        }
    }

    //读取客户端消息
    private void readData(SelectionKey key){
        //定义一个SocketChannel
        SocketChannel channel = null;
        try{
            //取到关联的Channel
            channel = (SocketChannel)key.channel();
            //创建缓冲Buffer
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int count = channel.read(buffer);
            //根据count的值做处理
            if(count>0){
                //把缓冲区的数据转成字符串
                String msg = new String(buffer.array(), 0, count);
                //输出该消息
                System.out.println("from 客户端："+msg);
                //向其他客户端转发消息(去掉自己)，专门写一个方法来处理
                sendInfoToOtherClients(msg,channel);
            }else{
                System.out.println(channel.getRemoteAddress()+"离线了...");
                //取消注册
                key.cancel();
                //关闭通道
                channel.close();
            }

        }catch (IOException e){
            //e.printStackTrace();
            try{
                System.out.println(channel.getRemoteAddress()+"离线了...");
                //取消注册
                key.cancel();
                //关闭通道
                channel.close();
            }catch (IOException ioe){
                ioe.printStackTrace();
            }

        }
    }

    //转发消息给其他客户(通道)
    private void sendInfoToOtherClients(String msg,SocketChannel self) throws IOException{
        System.out.println("服务器转发消息中...");
        //遍历所有的注册到Selector上的SocketChannel并排除自己
        for(SelectionKey key:selector.keys()){
            //通过key，取出对应的SocketChannel
            Channel targetChannel = key.channel();
            //排除自己
            //targetChannel instanceof SocketChannel排除了ServerSocketChannel
            if(targetChannel instanceof SocketChannel && targetChannel != self){
                //转型
                SocketChannel dest = (SocketChannel) targetChannel;
                //将msg存储到buffer
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                //将buffer的数据写入到通道
                dest.write(buffer);
            }

        }

    }

    public static void main(String[] args) {
        //创建一个服务器对象
        GroupChatServer groupChatServer = new GroupChatServer();
        groupChatServer.listen();

    }
}
