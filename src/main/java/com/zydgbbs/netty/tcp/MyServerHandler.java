package com.zydgbbs.netty.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.util.UUID;

public class MyServerHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private int count;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        byte[] buffer = new byte[msg.readableBytes()];
        msg.readBytes(buffer);
        //将buffer转成字符串
        String s = new String(buffer, CharsetUtil.UTF_8);
        System.out.println("服务器端接收到数据:"+s);
        System.out.println("服务器端接收到消息量="+(++this.count));
        //服务器回送数据给客户端,回送一个随机id，
        ByteBuf buf = Unpooled.copiedBuffer(UUID.randomUUID().toString()+" ", CharsetUtil.UTF_8);
        ctx.writeAndFlush(buf);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
