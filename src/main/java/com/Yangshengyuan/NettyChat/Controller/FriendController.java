package com.Yangshengyuan.NettyChat.Controller;


import com.Yangshengyuan.NettyChat.Service.FriendService;
import com.Yangshengyuan.NettyChat.Utils.IdWorker;
import com.Yangshengyuan.NettyChat.mapper.TbFriendReqMapper;
import com.Yangshengyuan.NettyChat.pojo.Result.FriendReqDiaplay;
import com.Yangshengyuan.NettyChat.pojo.Result.ResultVo;
import com.Yangshengyuan.NettyChat.pojo.Result.User;
import com.Yangshengyuan.NettyChat.pojo.TbFriendReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.transform.Result;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/friend")
public class FriendController {

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private FriendService friendService;

    @RequestMapping("/sendRequest")
    public ResultVo sendRequest(@RequestBody TbFriendReq tbFriendReq){
        tbFriendReq.setId(idWorker.nextId());
        tbFriendReq.setStatus(0);
        tbFriendReq.setCreatetime(new Date());
        try {
            TbFriendReq req =  friendService.addFriendRequest(tbFriendReq);
        } catch (Exception e) {
            return new ResultVo(false,e.getMessage());
        }
        return new ResultVo(true,"添加好友成功",tbFriendReq);
    }

    @RequestMapping("/findFriendReqByUserid")
    public List<FriendReqDiaplay>  findFriendReqById(String userid){
        List<FriendReqDiaplay> tbFriendReqs = friendService.findFriendReqById(userid);
        return tbFriendReqs;
    }

    @RequestMapping("/acceptFriendReq")
    public ResultVo acceptFriendReq(String reqid){
        TbFriendReq tbFriendReq =  friendService.acceptFriendReq(reqid);
        if (tbFriendReq != null){
            return new ResultVo(true,"同意成功");
        }
        return new ResultVo(false,"同意失败");
    }

    @RequestMapping("/ignoreFriendReq")
    public ResultVo ignoreFriendReq(String reqid){
        TbFriendReq tbFriendReq =  friendService.ignoreFriendReq(reqid);
        if (tbFriendReq != null){
            return new ResultVo(true,"拒绝成功");
        }
        return new ResultVo(false,"拒绝失败");
    }

    @RequestMapping("/findFriendByUserid")
    public List<User> findFriendByUserid(String userid){
        List<User> users = friendService.findFriendByUserid(userid);
        return users;
    }





}
