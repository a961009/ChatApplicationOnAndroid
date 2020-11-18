package com.Yangshengyuan.NettyChat.Service.impl;

import com.Yangshengyuan.NettyChat.Service.ChatService;
import com.Yangshengyuan.NettyChat.mapper.TbChatRecordMapper;
import com.Yangshengyuan.NettyChat.pojo.TbChatRecord;
import com.Yangshengyuan.NettyChat.pojo.TbChatRecordExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ChatServiceImpl implements ChatService {

    @Autowired
    private TbChatRecordMapper tbChatRecordMapper;


    @Override
    public void insert(TbChatRecord chatRecord) {
        tbChatRecordMapper.insert(chatRecord);
    }

    @Override
    public List<TbChatRecord> findByUserIdAndFriendId(String userid, String friendid) {
        TbChatRecordExample example = new TbChatRecordExample();
        TbChatRecordExample.Criteria criteria1 = example.createCriteria();
        TbChatRecordExample.Criteria criteria2 = example.createCriteria();
        criteria1.andUseridEqualTo(userid);
        criteria1.andFriendidEqualTo(friendid);
        criteria1.andHasDeleteEqualTo(0);

        criteria2.andUseridEqualTo(friendid);
        criteria2.andFriendidEqualTo(userid);
        criteria2.andHasDeleteEqualTo(0);
        example.or(criteria1);
        example.or(criteria2);

        List<TbChatRecord> tbChatRecords = tbChatRecordMapper.selectByExample(example);
        return tbChatRecords;
    }
}
