package com.zydgbbs.nio;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel02 {
    public static void main(String[] args) throws Exception{
        //创建文件输入流
        File file = new File("file01.txt");
        FileInputStream fileInputStream = new FileInputStream(file);
        //通过fileInputStream 获取对应的FileChannel->实际类型FileChannelImpl
        FileChannel fileChannel = fileInputStream.getChannel();
        //创建缓存区
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());
        //将通道的数据读入到Buffer中
        fileChannel.read(byteBuffer);
        //将字节转字符串
        System.out.println(new String(byteBuffer.array()));
        //关闭流
        fileInputStream.close();
    }
}
