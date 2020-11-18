package com.Yangshengyuan.NettyChat.Controller;


import com.Yangshengyuan.NettyChat.Service.UserService;
import com.Yangshengyuan.NettyChat.pojo.Result.ResultVo;
import com.Yangshengyuan.NettyChat.pojo.Result.User;
import com.Yangshengyuan.NettyChat.pojo.TbUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.transform.Result;
import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    @RequestMapping("/findall")
    public List<TbUser> findAll(){
        return userService.findAll();
    }

    @RequestMapping("/findByUsername")
    public ResultVo findByUserName(String userid,String friendUsername){
        User _user = new User();
        try {
            _user =  userService.findByUsername(userid,friendUsername);
        } catch (Exception e) {
            return new ResultVo(false,e.toString());
        }
        return new ResultVo(true,"搜索成功",_user);
    }




    @RequestMapping("/updateNickname")
    public ResultVo updateNickname(@RequestBody TbUser tbUser){
        User _user =  userService.updataNickname(tbUser.getId(),tbUser.getNickname());
        if(_user != null){
            return new ResultVo(true,"修改昵称成功",_user);
        }else {
            return new ResultVo(false,"修改昵称失败");
        }
    }

    @RequestMapping("/findById")
    public User findById(@RequestParam String userid){
        User _user = userService.findById(userid);
        return _user;
    }




    @RequestMapping("/login")
    public ResultVo login(@RequestBody TbUser tbUser){
        try {
            User _user = userService.login(tbUser.getUsername(),tbUser.getPassword());
            if (_user == null){
                return new ResultVo(false,"登录失败");
            }else {
                return new ResultVo(true,"登录成功",_user);
            }
        } catch (Exception e) {
            return new ResultVo(false,"登录失败");
        }
    }

    @RequestMapping("/register")
    public ResultVo register(@RequestBody TbUser tbUser){
        try {
            userService.register(tbUser);
        } catch (Exception e) {
            return new ResultVo(false,"注册失败，用户名已存在");
        }
        return new ResultVo(true,"注册成功");
    }

    @RequestMapping("/upload")
    public ResultVo saveImage(@RequestBody MultipartFile file, String userid){
        User user = userService.upload(file, userid);
        if (user != null) {
            return new ResultVo(true, "上传成功", user);
        }else {
            return new ResultVo(false,"上传失败");
        }

    }
}
