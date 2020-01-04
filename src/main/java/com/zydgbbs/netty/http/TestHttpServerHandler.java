package com.zydgbbs.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

/**
 * SimpleChannelInboundHandler是ChannelInboundHandlerAdapter的子类
 * HttpObject是客户端和服务器端相互通讯的数据被封装成HttpObject类型
 */
public class TestHttpServerHandler extends SimpleChannelInboundHandler<HttpObject>{
    //有读取事件时触发，读取客户端数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        //判断msg是不是httpRequest请求
        if(msg instanceof HttpRequest){

            //同一个请求会对应一套pipeline和handler，http请求后会断掉，不是长连接。
            System.out.println("pipeline hash "+ctx.pipeline().hashCode());
            System.out.println("TestHttpServerHandler hash "+this.hashCode());

            System.out.println("msg 类型="+msg.getClass());
            System.out.println("客户端地址："+ctx.channel().remoteAddress());

            HttpRequest httpRequest = (HttpRequest)msg;
            //获取uri，过滤特定资源
            URI uri = new URI(httpRequest.uri());
            if("/favicon.ico".equals(uri.getPath())){
                System.out.println("请求了favicon.ico，不做响应");
                return ;
            }

            //回复信息给浏览器[http协议]
            ByteBuf content = Unpooled.copiedBuffer("Hello，我是服务器", CharsetUtil.UTF_8);
            //构造一个http的相应，即httpresponse
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1
                    , HttpResponseStatus.OK
                    , content);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain;charset=utf-8");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH,content.readableBytes());
            //将构建好的response返回
            ctx.writeAndFlush(response);

        }
    }
}
