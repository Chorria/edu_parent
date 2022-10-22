package com.zhou.vodservice.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.DeleteVideoResponse;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.zhou.servicebase.exceptionhandler.ZhouException;
import com.zhou.vodservice.service.VODService;
import com.zhou.vodservice.utils.AliyunVodSDKUtil;
import com.zhou.vodservice.utils.ConstantPropertiesUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@Service
public class VODServiceImpl implements VODService {
    //上传视频
    @Override
    public String uploadVideo(MultipartFile multipartFile) {
        try {
            //获取上传的密钥和ID
            String accessKeyId = ConstantPropertiesUtil.ACCESS_KEY_ID;
            String accessKeySecret = ConstantPropertiesUtil.ACCESS_KEY_SECRET;
            //获取上传文件的原始名称
            String fileName = multipartFile.getOriginalFilename();
            //设置上传到阿里云后文件的名称
            String title = fileName.substring(0, fileName.lastIndexOf("."));
            //获取上传文件的输入流
            InputStream inputStream = multipartFile.getInputStream();
            UploadStreamRequest request = new UploadStreamRequest(accessKeyId,accessKeySecret,title,fileName,inputStream);

            UploadVideoImpl uploader = new UploadVideoImpl();
            UploadStreamResponse response = uploader.uploadStream(request);

            //获取上传视频的ID
            String videoId = null;
            if (response.isSuccess()) {
                videoId = response.getVideoId();
            } else {
                //如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因
                videoId = response.getVideoId();
            }
            return videoId;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    //删除单个视频
    @Override
    public void removeVODVideo(String videoSourceId) {
        try {
            //获取删除请求
            DeleteVideoRequest request = new DeleteVideoRequest();
            request.setVideoIds(videoSourceId);
            //获取客户端
            DefaultAcsClient client = AliyunVodSDKUtil.initVODClient(ConstantPropertiesUtil.ACCESS_KEY_ID, ConstantPropertiesUtil.ACCESS_KEY_SECRET);
            //删除视频,获取响应
            DeleteVideoResponse response = client.getAcsResponse(request);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ZhouException(20001,"删除单个视频失败!");
        }
    }
    //删除多个视频
    @Override
    public void removeVODVideoByIds(List<String> videoSourceIdList) {
        try {
            //创建删除视频request对象
            DeleteVideoRequest request = new DeleteVideoRequest();
            //videoIdList值转换成 1,2,3形式的字符串
            String videoSourceIds = StringUtils.join(videoSourceIdList.toArray(), ",");
            //向request设置视频id
            request.setVideoIds(videoSourceIds);
            //初始化对象
            DefaultAcsClient client = AliyunVodSDKUtil.initVODClient(ConstantPropertiesUtil.ACCESS_KEY_ID, ConstantPropertiesUtil.ACCESS_KEY_SECRET);
            //调用初始化对象的方法实现删除
            DeleteVideoResponse response = client.getAcsResponse(request);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ZhouException(20001,"删除多个视频失败!");
        }
    }

    //根据阿里云点播视频的id获取播放凭证
    @Override
    public String getVideoAuth(String id) {
        try {
            //创建初始化对象
            DefaultAcsClient client = AliyunVodSDKUtil.initVODClient(ConstantPropertiesUtil.ACCESS_KEY_ID, ConstantPropertiesUtil.ACCESS_KEY_SECRET);
            //创建获取凭证request对象
            GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
            //向request设置视频id
            request.setVideoId(id);
            //创建获取凭证response对象
            GetVideoPlayAuthResponse response = client.getAcsResponse(request);
            //获取播放凭证
            String playAuth = response.getPlayAuth();
            return playAuth;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ZhouException(20001,"获取视频凭证失败!");
        }
    }
}
