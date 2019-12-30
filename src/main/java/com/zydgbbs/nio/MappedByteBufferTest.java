package com.zydgbbs.nio;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * MappedByteButter可以让文件直接在内存（堆外内存）修改，操作系统不需要拷贝一次
 */
public class MappedByteBufferTest{
    public static void main(String[] args) throws Exception{
        RandomAccessFile randomAccessFile = new RandomAccessFile("1.txt", "rw");
        //获取通道
        FileChannel channel = randomAccessFile.getChannel();
        /**
         * 参数一：FileChannel.MapMode.READ_WRITE使用的是读写模式
         * 参数二：0代表起始位置
         * 参数三：5是映射到内存的大小，即将1.txt的多少个字节映射到内存
         * 可以直接修改的范围就是0~5，不包含5
         * 实际类型是DirectByteBuffer
         */
        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
        mappedByteBuffer.put(0,(byte)'H');
        mappedByteBuffer.put(3,(byte)'9');
        //mappedByteBuffer.put(5,(byte)'Y');

        randomAccessFile.close();
        System.out.println("修改成功");
    }
}
