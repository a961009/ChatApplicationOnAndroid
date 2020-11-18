package com.Yangshengyuan.NettyChat.Netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class WebSocketNettyServer {

    public static void start() {
        //创建两个线程池
        NioEventLoopGroup mainGrp = new NioEventLoopGroup();
        NioEventLoopGroup subGrp = new NioEventLoopGroup();
        try {


            //创建Netty服务器启动对象
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            //指定线程池、通道类型、通道初始化器
            serverBootstrap
                    .group(mainGrp,subGrp)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new webSocketInitializer());

            //绑定服务器端口
            ChannelFuture future = serverBootstrap.bind(9001).sync();

            //等待服务器关闭
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            System.out.println("启动发生错误");
        }finally {
            //关闭服务器
            mainGrp.shutdownGracefully();
            subGrp.shutdownGracefully();
        }

    }
}
