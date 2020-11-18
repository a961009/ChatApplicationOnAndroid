package com.Yangshengyuan.NettyChat.Service;

import com.Yangshengyuan.NettyChat.pojo.Result.FriendReqDiaplay;
import com.Yangshengyuan.NettyChat.pojo.Result.User;
import com.Yangshengyuan.NettyChat.pojo.TbFriendReq;

import java.util.List;

public interface FriendService {
    TbFriendReq addFriendRequest(TbFriendReq tbFriendReq);

    List<FriendReqDiaplay> findFriendReqById(String userid);

    TbFriendReq acceptFriendReq(String reqid);

    TbFriendReq ignoreFriendReq(String reqid);

    List<User> findFriendByUserid(String userid);
}
