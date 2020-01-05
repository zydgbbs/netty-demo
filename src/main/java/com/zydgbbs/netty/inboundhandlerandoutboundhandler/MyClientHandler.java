package com.zydgbbs.netty.inboundhandlerandoutboundhandler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class MyClientHandler extends SimpleChannelInboundHandler<Long> {

    //重写ChannelActive发送数据
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("MyClientHandler发送数据...");
        //ctx.writeAndFlush(Unpooled.copiedBuffer(""))
        //ctx.writeAndFlush(123456l);//发送的是一个long
        //1 abcdabcdabcdabcd是16个字节
        //2 该处理器的前一个handler是MyLongToByteEncoder
        //3 MyLongToByteEncoder的父类是MessageToByteEncoder
        //4 父类中有write方法，如果不是要处理的long数据，则跳过encode
        ctx.writeAndFlush(Unpooled.copiedBuffer("abcdabcdabcdabcd", CharsetUtil.UTF_8));

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {
        System.out.println("收到服务器端发来的数据："+msg);
    }
}
