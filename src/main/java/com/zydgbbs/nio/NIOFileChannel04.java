package com.zydgbbs.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class NIOFileChannel04 {
    public static void main(String[] args) throws Exception{

        //创建相关流
        FileInputStream fileInputStream = new FileInputStream("test1.jpeg");
        FileOutputStream fileOutputStream = new FileOutputStream("test2.jpeg");

        //获取各个流对应的fileChannel
        FileChannel source = fileInputStream.getChannel();
        FileChannel destCh = fileOutputStream.getChannel();

        //使用transferFrom完成拷贝
        destCh.transferFrom(source,0,source.size());
        //关闭相关通道和流
        source.close();
        destCh.close();
        fileInputStream.close();
        fileOutputStream.close();

    }
}
