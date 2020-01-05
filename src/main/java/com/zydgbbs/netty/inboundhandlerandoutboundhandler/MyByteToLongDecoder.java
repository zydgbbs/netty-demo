package com.zydgbbs.netty.inboundhandlerandoutboundhandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class MyByteToLongDecoder extends ByteToMessageDecoder {
    /**
     * decode会根据接收到的数据，被调用多次，直到确定没有新的元素被添加到list结束，
     * 或者ByteBuf没有更多的可读字节为止
     * 如果out不为空，就会将内容传递给下一个ChannelInboundHandler处理，该方法也会被调用多次
     * @param ctx 上下文
     * @param in 入站的ByteBuf
     * @param out list集合，将解码后的数据传给下一个handler处理
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("MyByteToLongDecoder decode被调用");
        //因为long是8个字节，需要判断有8个字节才能读取一个long
        if(in.readableBytes()>=8){
            out.add(in.readLong());
        }
    }
}
