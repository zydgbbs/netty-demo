package com.zydgbbs.netty.dubborpc.provider;

import com.zydgbbs.netty.dubborpc.publicinterface.HelloService;

public class HelloServiceImpl implements HelloService{

    private static int count;

    //当有消费方调用该方法时就返回结果
    @Override
    public String hello(String msg) {
        System.out.println("收到客户端消息="+msg);
        //根据msg返回不同的结果
        if(msg != null){
            return "你好，客户端，我已经收到你的消息["+msg+"]，这是第"+(++this.count)+"次调用";
        }else{
            return "你好，客户端，我已经收到你的消息";
        }
    }
}
