package com.zydgbbs.netty.dubborpc.netty;

import com.zydgbbs.netty.dubborpc.provider.HelloServiceImpl;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

//服务器这边的handler比较简单
public class NettyServerHandler extends ChannelInboundHandlerAdapter{
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //获取客户端发送的消息并调用我们的服务
        System.out.println("msg="+msg);
        //客户端在调用服务器的api时，我们需要定义一个协议
        //比如：我们要求每次发消息时都必须以某个字符串开头"HelloService#hello#你好"
        if(msg.toString().startsWith("HelloService#hello#")){
            String result = new HelloServiceImpl().hello(msg.toString().substring(msg.toString().lastIndexOf("#") + 1));
            ctx.writeAndFlush(result);
        }
    }
}
