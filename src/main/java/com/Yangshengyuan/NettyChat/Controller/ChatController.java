package com.Yangshengyuan.NettyChat.Controller;


import com.Yangshengyuan.NettyChat.Service.ChatService;
import com.Yangshengyuan.NettyChat.pojo.TbChatRecord;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/chatrecord")
public class ChatController {
    @Autowired
    private ChatService chatService;



    @RequestMapping("/findByUserIdAndFriendId")
    public List<TbChatRecord> findByUserIdAndFriendId(String userid, String friendid){
        List<TbChatRecord> tbChatRecords = chatService.findByUserIdAndFriendId(userid, friendid);
        return tbChatRecords;
    }


}
