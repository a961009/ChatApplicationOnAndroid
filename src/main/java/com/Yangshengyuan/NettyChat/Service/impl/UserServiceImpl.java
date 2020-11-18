package com.Yangshengyuan.NettyChat.Service.impl;

import com.Yangshengyuan.NettyChat.Service.UserService;
import com.Yangshengyuan.NettyChat.Utils.IdWorker;
import com.Yangshengyuan.NettyChat.Utils.ImageUtils;
import com.Yangshengyuan.NettyChat.Utils.QRCodeUtils;
import com.Yangshengyuan.NettyChat.mapper.TbFriendReqMapper;
import com.Yangshengyuan.NettyChat.mapper.TbUserMapper;
import com.Yangshengyuan.NettyChat.pojo.Result.ResultVo;
import com.Yangshengyuan.NettyChat.pojo.Result.User;
import com.Yangshengyuan.NettyChat.pojo.TbFriendReq;
import com.Yangshengyuan.NettyChat.pojo.TbFriendReqExample;
import com.Yangshengyuan.NettyChat.pojo.TbUser;
import com.Yangshengyuan.NettyChat.pojo.TbUserExample;
import com.google.zxing.qrcode.encoder.QRCode;
import io.netty.util.internal.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ClassUtils;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.print.DocFlavor;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;


@Service
@Transactional
public class UserServiceImpl implements UserService {


    @Autowired
    private QRCodeUtils qrCodeUtils;

    @Autowired
    private TbFriendReqMapper tbFriendReqMapper;

    @Autowired
    private TbUserMapper tbUserMapper;

    @Autowired
    private IdWorker idWorker;


    @Override
    public void register(TbUser tbUser) {
        TbUserExample tbUserExample = new TbUserExample();
        TbUserExample.Criteria criteria = tbUserExample.createCriteria();
        criteria.andUsernameEqualTo(tbUser.getUsername());
        List<TbUser> tbUsers = tbUserMapper.selectByExample(tbUserExample);

        if (tbUsers.size() != 0  && tbUsers != null){
            System.out.println("用户已经存在");
            throw new RuntimeException("用户已经存在");
        }
        TbUser user = new TbUser();
        user.setUsername(tbUser.getUsername());
        user.setId(idWorker.nextId());
        user.setPassword(DigestUtils.md5DigestAsHex(tbUser.getPassword().getBytes()));
        user.setPicSmall("");
        user.setPicNormal("");
        user.setNickname(tbUser.getUsername());
        String picSavePath = ClassUtils.getDefaultClassLoader().getResource("static").getPath();
        String qrPicName = user.getId() + ".png";

        qrCodeUtils.createQRCode(picSavePath + "/qrCodeImage/" +qrPicName,user.getId());
        String URL = "http://192.168.1.244:9000/";
        user.setQrcode(URL + "/qrCodeImage/" +qrPicName);
        user.setCreatetime(new Date());
        user.setClientId(tbUser.getClientId());
        tbUserMapper.insert(user);

    }

    @Override
    public User upload(MultipartFile file, String userid) {
        TbUser tbUser = tbUserMapper.selectByPrimaryKey(userid);
        String path = ClassUtils.getDefaultClassLoader().getResource("static").getPath();
        String subfix = file.getOriginalFilename().split("\\.")[1];
        String imageName = tbUser.getId() + "." + subfix;
        String url_path = "/normalImage/" + imageName;
        String savePath = path+File.separator+url_path;
        File saveFile = new File(savePath);
        try {
            file.transferTo(saveFile);  //将临时存储的文件移动到真实存储路径下
            ImageUtils.imgThumb(savePath,path +"/smallImage/" + imageName,200,200);

        } catch (IOException e) {
            return null;
        }

        String URL = "http://192.168.1.244:9000/";
        tbUser.setPicSmall(URL +"/smallImage/" + imageName);
        tbUser.setPicNormal(URL +url_path);
        tbUserMapper.updateByPrimaryKey(tbUser);
        User user = new User();
        BeanUtils.copyProperties(tbUser,user);
        return user;

    }

    @Override
    public User updataNickname(String id, String nickname) {
        TbUser tbUser = tbUserMapper.selectByPrimaryKey(id);
        try {
            tbUser.setNickname(nickname);
            tbUserMapper.updateByPrimaryKey(tbUser);
        } catch (Exception e) {
            return null;
        }
        User user = new User();
        BeanUtils.copyProperties(tbUser,user);
        return user;
    }

    @Override
    public User findById(String id) {
        TbUser tbUser = tbUserMapper.selectByPrimaryKey(id);
        User user = new User();
        BeanUtils.copyProperties(tbUser,user);
        return user;
    }

    @Override
    public User findByUsername(String userid, String friendName) {
        //1.不能添加自己作为好友
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(friendName);
        List<TbUser> tbUsers = tbUserMapper.selectByExample(example);
        if (tbUsers.size() == 0 || tbUsers == null){
            throw new RuntimeException("没有找到该用户");
        }
        if (tbUsers.get(0).getId().equals(userid)){
            throw new RuntimeException("不能添加自己为好友");
        }
        TbFriendReqExample tbFriendReqExample = new TbFriendReqExample();
        TbFriendReqExample.Criteria criteria1 = tbFriendReqExample.createCriteria();
        criteria1.andFromUseridEqualTo(userid);
        criteria1.andToUseridEqualTo(tbUsers.get(0).getId());
        criteria1.andStatusEqualTo(0);
        List<TbFriendReq> tbFriendReqs = tbFriendReqMapper.selectByExample(tbFriendReqExample);
        if (tbFriendReqs.size() != 0 ){
            throw new RuntimeException("已向该用户提交好友申请");
        }
        User user = new User();
        BeanUtils.copyProperties(tbUsers.get(0),user);
        return user;

    }

    @Override
    public List<TbUser> findAll() {
        List<TbUser> tbUsers = tbUserMapper.selectByExample(null);
        return tbUsers;
    }

    /**
     * 用于登录检查，登录成功返回用户对象，登录失败返回null
     * @param username
     * @param password
     * @return
     */

    @Override
    public User login(String username, String password) {
        if (StringUtils.isNotBlank(username)&&StringUtils.isNotBlank(password)) {

            TbUserExample tbUserExample = new TbUserExample();
            TbUserExample.Criteria criteria = tbUserExample.createCriteria();
            criteria.andUsernameEqualTo(username);
            List<TbUser> tbUsers = tbUserMapper.selectByExample(tbUserExample);
            if (tbUsers != null && tbUsers.size() == 1){
                //找到用户，对密码进行校验
                String encodingPassword =  DigestUtils.md5DigestAsHex(password.getBytes());
                if (encodingPassword.equals(tbUsers.get(0).getPassword())){
                    User user = new User();
                    BeanUtils.copyProperties(tbUsers.get(0),user);
                    return user;
                }
            }
        }
        return null;
    }
}
