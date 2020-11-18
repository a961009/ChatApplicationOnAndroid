package com.Yangshengyuan.NettyChat.Netty;

import com.Yangshengyuan.NettyChat.Service.ChatService;
import com.Yangshengyuan.NettyChat.Utils.IdWorker;
import com.Yangshengyuan.NettyChat.Utils.SpringUtil;
import com.Yangshengyuan.NettyChat.pojo.TbChatRecord;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

public class ChatHandle extends SimpleChannelInboundHandler<TextWebSocketFrame> {



    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
       //这个方法在收到消息后会自动调用
        ObjectMapper objectMapper = new ObjectMapper();
        String text = msg.text();
        System.out.println("接受的消息是:" + text);
        Message message = objectMapper.readValue(text, Message.class);
        System.out.println(message);
        switch (message.getType()){
            case 0:
                String id = message.getChatRecord().getUserid();
                UserChannalMapper.put(id,ctx.channel());
                System.out.println("用户:" + id + " 通道已经建立");
                break;
            case 1:
                IdWorker idWorker = SpringUtil.getBean(IdWorker.class);
                ChatService chatService = SpringUtil.getBean(ChatService.class);
                //如果用户在线，则发给用户再插入表；如果用户不在线，则直接插入表
                Channel channel = UserChannalMapper.get(message.getChatRecord().getFriendid());
                String s = idWorker.nextId();
                message.getChatRecord().setId(s);
                message.getChatRecord().setHasDelete(0);
                message.getChatRecord().setCreatetime(new Date());
                if (channel != null){
                    message.getChatRecord().setHasRead(1);
                    channel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(message)));
                }
                chatService.insert(message.getChatRecord());
                //

        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        UserChannalMapper.removeByChannelId(ctx.channel().id().asLongText());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        String handlerID = ctx.channel().id().asLongText();
        UserChannalMapper.removeByChannelId(handlerID);
        System.out.println("已经移除handler: " +handlerID);
    }
}
