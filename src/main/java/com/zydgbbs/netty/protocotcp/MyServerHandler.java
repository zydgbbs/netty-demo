package com.zydgbbs.netty.protocotcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.util.UUID;

public class MyServerHandler extends SimpleChannelInboundHandler<MessageProtocol> {
    private int count;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        //接收到数据并处理
        int len = msg.getLen();
        byte[] content = msg.getContent();
        System.out.println("服务端接收到信息如下：");
        System.out.println("长度="+len);
        System.out.println("内容="+new String(content,CharsetUtil.UTF_8));
        System.out.println("服务器接收到消息的次数为："+(++this.count));
        //回复消息
        String resContent = UUID.randomUUID().toString();
        byte[] resContent2 = resContent.getBytes(CharsetUtil.UTF_8);
        int resLength = resContent.getBytes(CharsetUtil.UTF_8).length;
        MessageProtocol messageProtocol = new MessageProtocol();
        messageProtocol.setContent(resContent2);
        messageProtocol.setLen(resLength);
        ctx.writeAndFlush(messageProtocol);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
