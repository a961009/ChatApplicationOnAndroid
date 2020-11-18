package com.Yangshengyuan.NettyChat.Service.impl;

import com.Yangshengyuan.NettyChat.Service.FriendService;
import com.Yangshengyuan.NettyChat.Utils.IdWorker;
import com.Yangshengyuan.NettyChat.mapper.TbFriendMapper;
import com.Yangshengyuan.NettyChat.mapper.TbFriendReqMapper;
import com.Yangshengyuan.NettyChat.mapper.TbUserMapper;
import com.Yangshengyuan.NettyChat.pojo.*;
import com.Yangshengyuan.NettyChat.pojo.Result.FriendReqDiaplay;
import com.Yangshengyuan.NettyChat.pojo.Result.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jws.soap.SOAPBinding;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
@Transactional
public class FriendServiceImpl implements FriendService {

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private TbFriendReqMapper tbFriendReqMapper;

    @Autowired
    private TbUserMapper tbUserMapper;

    @Autowired
    private TbFriendMapper tbFriendMapper;

    @Override
    public TbFriendReq addFriendRequest(TbFriendReq tbFriendReq) {
        try {
            tbFriendReqMapper.insert(tbFriendReq);
        } catch (Exception e) {
            throw new RuntimeException("申请好友错误");
        }
        return tbFriendReq;
    }

    @Override
    public List<FriendReqDiaplay> findFriendReqById(String userid) {
        TbFriendReqExample friendReqExample = new TbFriendReqExample();
        TbFriendReqExample.Criteria criteria = friendReqExample.createCriteria();
        criteria.andToUseridEqualTo(userid);
        criteria.andStatusEqualTo(0);
        List<TbFriendReq> tbFriendReqs = tbFriendReqMapper.selectByExample(friendReqExample);
        List<FriendReqDiaplay> users = new LinkedList<>();
        int index = 0;
        for (TbFriendReq tbFriendReq : tbFriendReqs) {
            String fromUserid = tbFriendReq.getFromUserid();
            TbUser tbUser = tbUserMapper.selectByPrimaryKey(fromUserid);
            FriendReqDiaplay tempUser = new FriendReqDiaplay();
            BeanUtils.copyProperties(tbUser,tempUser);
            tempUser.setId(tbFriendReqs.get(index).getId());
            index += 1;
            users.add(tempUser);
        }
        return users;
    }

    @Override
    public TbFriendReq acceptFriendReq(String reqid) {
        TbFriendReq tbFriendReq = tbFriendReqMapper.selectByPrimaryKey(reqid);
        try {
            tbFriendReq.setStatus(1);
            tbFriendReqMapper.updateByPrimaryKey(tbFriendReq);
            String fromUserid = tbFriendReq.getFromUserid();
            String toUserid = tbFriendReq.getToUserid();
            TbFriend tbFriend1 = new TbFriend();
            tbFriend1.setId(idWorker.nextId());
            tbFriend1.setCreatetime(new Date());
            tbFriend1.setUserid(fromUserid);
            tbFriend1.setFriendsId(toUserid);
            TbFriend tbFriend2 = new TbFriend();
            tbFriend2.setId(idWorker.nextId());
            tbFriend2.setCreatetime(new Date());
            tbFriend2.setUserid(toUserid);
            tbFriend2.setFriendsId(fromUserid);
            tbFriendMapper.insert(tbFriend1);
            tbFriendMapper.insert(tbFriend2);
        } catch (Exception e) {
            return null;
        }
        return tbFriendReq;
    }

    @Override
    public TbFriendReq ignoreFriendReq(String reqid) {
        TbFriendReq tbFriendReq = tbFriendReqMapper.selectByPrimaryKey(reqid);
        try {
            tbFriendReq.setStatus(1);
            tbFriendReqMapper.updateByPrimaryKey(tbFriendReq);
        } catch (Exception e) {
            return null;
        }
        return tbFriendReq;
    }

    @Override
    public List<User> findFriendByUserid(String userid) {
        TbFriendExample example = new TbFriendExample();
        TbFriendExample.Criteria criteria = example.createCriteria();
        criteria.andUseridEqualTo(userid);
        List<TbFriend> tbFriends = tbFriendMapper.selectByExample(example);
        LinkedList<User> users = new LinkedList<>();
        for (TbFriend tbFriend : tbFriends) {
            String friendsId = tbFriend.getFriendsId();
            TbUser tbUser = tbUserMapper.selectByPrimaryKey(friendsId);
            User tempUser = new User();
            BeanUtils.copyProperties(tbUser,tempUser);
            users.add(tempUser);
        }
        return users;
    }
}
