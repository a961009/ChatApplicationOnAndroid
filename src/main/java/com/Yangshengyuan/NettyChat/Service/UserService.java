package com.Yangshengyuan.NettyChat.Service;

import com.Yangshengyuan.NettyChat.pojo.Result.ResultVo;
import com.Yangshengyuan.NettyChat.pojo.Result.User;
import com.Yangshengyuan.NettyChat.pojo.TbUser;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {


    List<TbUser> findAll();


    User login(String username, String password);

    void register(TbUser tbUser);

    User upload(MultipartFile file, String userid);

    User updataNickname(String id, String nickname);

    User findById(String id);

    User findByUsername(String userid, String friendName);
}
