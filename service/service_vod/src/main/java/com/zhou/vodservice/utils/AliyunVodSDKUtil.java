package com.zhou.vodservice.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.profile.DefaultProfile;

public class AliyunVodSDKUtil {
    public static DefaultAcsClient initVODClient(String accessKeyId,String accessKeySecret){
        //点播服务接入地区
        String regionId = "cn-shanghai";
        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
        DefaultAcsClient client = new DefaultAcsClient(profile);
        return client;
    }
}
