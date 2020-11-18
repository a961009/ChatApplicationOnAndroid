package com.Yangshengyuan.NettyChat.Service;

import com.Yangshengyuan.NettyChat.pojo.TbChatRecord;

import java.util.List;

public interface ChatService {
    void insert(TbChatRecord chatRecord);

    List<TbChatRecord> findByUserIdAndFriendId(String userid, String friendid);
}
