package com.zydgbbs.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.util.CharsetUtil;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

/**
 * 1 我们自定义一个Handler需要继承Netty规定好的某个Handler适配器
 * 2 这时我们自定义的一个Handler，才能称为一个Handler
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * ctx:上下文对象，含有管道pipeline，通道Channel，地址
     * msg:就是客户端发送的数据，默认是Object形式
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        //比如这里我们有一个非常耗时的业务 ->异步执行 ->提交该Channel 对应的NIOEventLoop的taskQueue中

        //阻塞问题
        /*Thread.sleep(10*1000);
        ctx.writeAndFlush(Unpooled.copiedBuffer("Hello，客户端~耗时操作",CharsetUtil.UTF_8));
        System.out.println("go on ...");*/

        //解决方案1：用户程序自定义的普通任务，下面的两个是在同一个线程里跑的，共30秒
        /*ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10*1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("Hello，客户端~耗时操作",CharsetUtil.UTF_8));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println("发生异常");
                }
            }
        });
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(20*1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("Hello，客户端~耗时操作二",CharsetUtil.UTF_8));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println("发生异常");
                }
            }
        });*/
        //解决方案二：用户自定义定时任务->该任务提交到scheduleTaskQueue中
        /*ctx.channel().eventLoop().schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10*1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("Hello，客户端~定时耗时操作",CharsetUtil.UTF_8));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println("发生异常");
                }
            }
        },5, TimeUnit.SECONDS);*/



        //下面的先注释掉
        /*System.out.println("服务器读取线程："+Thread.currentThread().getName());
        System.out.println("server ctx = "+ctx);
        System.out.println("看看Channel和pipeline的关系");
        Channel channel = ctx.channel();
        ChannelPipeline pipeline = ctx.pipeline();//本质是一个双向链表，出栈入栈

        //将msg转成一个ByteBuf，是Netty提供的，不是NIO的ByteBuffer
        ByteBuf buf = (ByteBuf)msg;
        System.out.println("客户端发送消息是："+buf.toString(CharsetUtil.UTF_8));
        System.out.println("客户端地址："+ctx.channel().remoteAddress());
        */
    }

    /**
     * 数据读取完毕
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //ctx.fireChannelReadComplete();
        ctx.writeAndFlush(Unpooled.copiedBuffer("Hello，客户端~",CharsetUtil.UTF_8));

    }

    /**
     * 处理异常，一般需要关闭通道
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //ctx.fireExceptionCaught(cause);
        ctx.close();
    }

}
