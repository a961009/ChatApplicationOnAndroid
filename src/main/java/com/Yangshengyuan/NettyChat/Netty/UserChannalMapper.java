package com.Yangshengyuan.NettyChat.Netty;

import io.netty.channel.Channel;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class UserChannalMapper {
    private static Map<String, Channel> userChannalMap;

    static {
        userChannalMap = new HashMap<String, Channel>();
    }

    public static Channel get(String userid){
        for (String s : userChannalMap.keySet()) {
            if (s.equals(userid)) {
                return userChannalMap.get(s);
            }
        }
        return null;
    }

    public static void put(String userid,Channel channel){
        userChannalMap.put(userid,channel);
    }
    public static void remove(String userid){
        userChannalMap.remove(userid);
    }

    public static void removeByChannelId(String channelId){
        if (StringUtils.isNotBlank(channelId)) {
            for (String key : userChannalMap.keySet()) {
                if (userChannalMap.get(key).id().asLongText().equals(channelId)){
                    userChannalMap.remove(key);
                }
            }
        }

    }

    public static void printChannel(){
        for (String s : userChannalMap.keySet()) {
            System.out.println(s +" : " + userChannalMap.get(s).id());
        }
    }



}
