package com.zydgbbs.zerocopy;

import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class NewIOClient {
    public static void main(String[] args) throws Exception{
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost",7001));
        String fileName = "protoc-win32.zip";
        FileChannel fileChannel = new FileInputStream(fileName).getChannel();
        //准备发送
        long startTime = System.currentTimeMillis();
        //在Linux下一个transferTo方法就可以完成传输
        //在Windows下transferTo一次调用只能发送8M的文件，就需要分段传输文件，要注意传输时的位置
        //transferTo底层使用了零拷贝
        long transferCount = fileChannel.transferTo(0,fileChannel.size(),socketChannel);
        System.out.println("发送的总的字节数为"+transferCount+"，耗时"+(System.currentTimeMillis()-startTime));

    }
}
