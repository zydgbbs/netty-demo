package com.zydgbbs.netty.codec;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

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
        //读取从客户端发送的StudentPOJO.Student
        StudentPOJO.Student student = (StudentPOJO.Student)msg;
        System.out.println("客户端发送的数据 id="+student.getId()+",name="+student.getName());
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
