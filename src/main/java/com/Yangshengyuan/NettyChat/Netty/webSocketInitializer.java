package com.Yangshengyuan.NettyChat.Netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;


//通道初始化器

public class webSocketInitializer extends ChannelInitializer<SocketChannel> {

    //初始化通道
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        //添加HTTP编解码器
        pipeline.addLast(new HttpServerCodec());
        //添加一个用于大数据流的支持
        pipeline.addLast(new ChunkedWriteHandler());
        //添加一个聚合器，这个聚合器讲httpMessage聚合为FullHttpRequest/Response
        pipeline.addLast(new HttpObjectAggregator(1024 * 64));

        //必须使用以ws结尾的url
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));

        //业务handle
        pipeline.addLast(new ChatHandle());

    }
}
