package com.zydgbbs.nio;

import java.nio.IntBuffer;

public class BasicBuffer {
    public static void main(String[] args) {
        //举例说明buffer的使用（简单说明）
        //创建一个Buffer，大小为5，即可以存放5个int
        IntBuffer intBuffer = IntBuffer.allocate(5);
        //向Buffer中存放数据
        for (int i=0;i<intBuffer.capacity();i++){
            intBuffer.put(i*2);
        }
        //从Buffer中读取数据
        //将Buffer转换，读写切换
        intBuffer.flip();
        //设置从哪个位置开始
        //intBuffer.position(1);
        //设置读取几个数据
        //intBuffer.limit(3);
        while (intBuffer.hasRemaining()){
            System.out.println(intBuffer.get());
        }

    }
}
