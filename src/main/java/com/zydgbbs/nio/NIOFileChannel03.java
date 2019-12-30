package com.zydgbbs.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel03 {
    public static void main(String[] args) throws Exception{
        //文件输入流
        File file = new File("file01.txt");
        FileInputStream fileInputStream = new FileInputStream(file);
        FileChannel fileChannel01 = fileInputStream.getChannel();

        FileOutputStream fileOutputStream = new FileOutputStream("file02.txt");
        FileChannel fileChannel02 = fileOutputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(512);

        //循环读取
        while (true){
            //清空Buffer
            byteBuffer.clear();
            int read = fileChannel01.read(byteBuffer);
            if(read==-1){
                break;
            }
            //将Buffer中的数据写入到fileChannel02->file02.txt
            byteBuffer.flip();
            fileChannel02.write(byteBuffer);
        }
        fileInputStream.close();
        fileOutputStream.close();
    }
}
