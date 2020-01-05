package com.zydgbbs.netty.dubborpc.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;

public class NettyClientHandler extends ChannelInboundHandlerAdapter implements Callable {

    private ChannelHandlerContext context;//上下文
    private String result;//调用后返回的结果
    private String para;//客户端调用方法时传入的参数

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    //3
    //被代理对象调用，发送数据给服务器 ->wait，等待被唤醒（channelRead） ->返回结果
    @Override
    public synchronized Object call() throws Exception {
        System.out.println("call1被调用...");
        context.writeAndFlush(para);
        //进行wait
        wait();//等待channelRead 方法获取到服务器的结果后，唤醒
        System.out.println("call2被调用...");
        return result;//服务方返回的结果
    }

    //1
    //与服务器的连接创建后，就会调用
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelActive被调用");
        context = ctx;//因为我们在其他方法会使用到ctx
    }

    //4
    //收到服务器数据后，就会被调用
    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("channelRead被调用...");
        result = msg.toString();
        notify();//唤醒等待的线程

    }

    //2
    void setPara(String para){
        System.out.println("setPara被调用...");
        this.para = para;
    }
}
