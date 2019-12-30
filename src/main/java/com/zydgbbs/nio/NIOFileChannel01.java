package com.zydgbbs.nio;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel01 {
    public static void main(String[] args) throws Exception{
        String str = "hello,资源帝国";
        //创建一个输出流->channel
        FileOutputStream fileOutputStream = new FileOutputStream("file01.txt");
        //通过fileOutputStream获取对应的FileChannel
        //这个fileChannel真实类型是FileChannelImpl
        FileChannel fileChannel = fileOutputStream.getChannel();
        //创建一个缓冲区ByteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        //将str写入到byteBuffer中去
        byteBuffer.put(str.getBytes());
        //对ByteBuffer进行flip
        byteBuffer.flip();
        //将ByteBuffer里的数据写入到FileChannel中去
        fileChannel.write(byteBuffer);
        fileOutputStream.close();
    }
}
