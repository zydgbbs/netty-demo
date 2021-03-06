package com.zydgbbs.netty.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class MyClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private int count;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        byte[] bytes = new byte[msg.readableBytes()];
        msg.readBytes(bytes);
        String s = new String(bytes, CharsetUtil.UTF_8);
        System.out.println("客户端接收到消息："+s);
        System.out.println("客户接收消息数量："+(++this.count));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
       //使用客户端发送10条数据Hello，server+编号
        for (int i=0;i<10;i++){
            ByteBuf buf = Unpooled.copiedBuffer("Hello,Server " + i, CharsetUtil.UTF_8);
            ctx.writeAndFlush(buf);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
