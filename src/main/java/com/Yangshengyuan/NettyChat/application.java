package com.Yangshengyuan.NettyChat;


import com.Yangshengyuan.NettyChat.Netty.WebSocketNettyServer;
import com.Yangshengyuan.NettyChat.Utils.IdWorker;
import com.Yangshengyuan.NettyChat.Utils.QRCodeUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@MapperScan(basePackages = "com.Yangshengyuan.NettyChat.mapper")
public class application {
    public static void main(String[] args) {
        SpringApplication.run(application.class);
        WebSocketNettyServer.start();
    }


    //用于生成独一无二的ID
    @Bean
    public IdWorker idWorker(){
        return new IdWorker(0,0);
    }

    @Bean
    public QRCodeUtils qrCodeUtils(){
        return new QRCodeUtils();
    }

}
