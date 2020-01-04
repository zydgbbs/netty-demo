package com.zydgbbs.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {
    public static void main(String[] args) throws Exception{
        //创建BossGroup和WorkGroup
        //1 创建两个线程组 BossGroup和workerGroup
        //2 bossGroup只处理连接请求，真正和客户端业务处理，会交给workerGroup完成
        //3 两个都是无限循环
        //4 bossGroup和WorkGroup含有的子线程（NioEventLoop）的个数
        //默认实际CPU核数*2
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        //创建服务器端的启动对象，配置参数
        ServerBootstrap bootstrap = new ServerBootstrap();
        try {
            //使用链式编程来进行设置
            bootstrap.group(bossGroup, workerGroup)//设置两个线程组
                    .channel(NioServerSocketChannel.class)//使用NioSocketChannel作为服务器的通道实现
                    .option(ChannelOption.SO_BACKLOG, 128)//设置线程队列得到连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true)//设置保持活动连接状态
                    //.handler(null)//该Handler对应的是BossGroup，childHandler对应的是WorkerGroup
                    .childHandler(new ChannelInitializer<SocketChannel>() {//创建一个通道初始化对象（匿名对象）
                        //给pipeline设置处理器
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //可以使用一个集合管理socketChannel，在推送消息时，可以将业务加入到各个Channel对应NIOEventLoop的taskQueue
                            //或者scheduleTaskQueue
                            System.out.println("客户socketChannel hashCode："+ch.hashCode());
                            ch.pipeline().addLast(new NettyServerHandler());
                        }
                    });//给我们的WorkGroup的EventLoop对应的管道设置处理器
            System.out.println("服务器准备好了...");
            //绑定一个端口并且同步，生成一个ChannelFuture对象
            ChannelFuture cf = bootstrap.bind(6668).sync();
            //给cf注册监听器，监控我们关系的事件
            cf.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if(cf.isSuccess())
                        System.out.println("监听端口6668成功");
                    else
                        System.out.println("监听端口失败");
                }
            });

            //对关闭通道进行监听
            cf.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
