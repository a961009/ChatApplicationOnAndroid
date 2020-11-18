package com.Yangshengyuan.NettyChat.Netty;

import com.Yangshengyuan.NettyChat.pojo.TbChatRecord;

public class Message {
    private Integer type;
    private TbChatRecord ChatRecord;
    private Object ext;

    @Override
    public String toString() {
        return "Message{" +
                "type=" + type +
                ", ChatRecord=" + ChatRecord +
                ", ext=" + ext +
                '}';
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public TbChatRecord getChatRecord() {
        return ChatRecord;
    }

    public void setChatRecord(TbChatRecord chatRecord) {
        ChatRecord = chatRecord;
    }

    public Object getExt() {
        return ext;
    }

    public void setExt(Object ext) {
        this.ext = ext;
    }
}
