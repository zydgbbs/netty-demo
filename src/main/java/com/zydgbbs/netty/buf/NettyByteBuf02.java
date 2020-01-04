package com.zydgbbs.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;

public class NettyByteBuf02 {
    public static void main(String[] args) {
        //创建ByteBuf
        ByteBuf byteBuf = Unpooled.copiedBuffer("hello,world,中文", CharsetUtil.UTF_8);

        //使用相关的API
        if(byteBuf.hasArray()){//true
            byte[] content = byteBuf.array();
            //将content转成字符串
            System.out.println(new String(content, Charset.forName("utf-8")));
            System.out.println("byteBuf="+byteBuf);
            System.out.println(byteBuf.arrayOffset());//0
            System.out.println(byteBuf.readerIndex());//0
            System.out.println(byteBuf.writerIndex());//18
            System.out.println(byteBuf.capacity());//42
            //System.out.println(byteBuf.readByte());//下面的就变成了17
            //System.out.println(byteBuf.getByte(0));//下面的不受影响
            //
            System.out.println(byteBuf.readableBytes());//可读取字节的数量，18
            for(int i=0;i<byteBuf.readableBytes();i++){
                System.out.println(byteBuf.getByte(i));
                //System.out.println((char) byteBuf.getByte(i));
            }
            //按照某个范围读取
            System.out.println(byteBuf.getCharSequence(4,6,CharsetUtil.UTF_8));
        }
    }
}
