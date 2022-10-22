package com.zhou.ossservice.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.zhou.ossservice.service.OSSService;
import com.zhou.ossservice.utils.ConstantPropertiesUtil;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class OSSServiceImpl implements OSSService {
    //上传头像到OSS
    @Override
    public String uploadFileAvatar(MultipartFile file) {
        //工具类获取值
        String endpoint = ConstantPropertiesUtil.END_POINT;
        String accessKeyId = ConstantPropertiesUtil.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtil.ACCESS_KEY_SECRET;
        String bucketName = ConstantPropertiesUtil.BUCKET_NAME;
        //创建OSSClient实例
        OSS ossClient = new OSSClientBuilder().build(endpoint,accessKeyId,accessKeySecret);
        try {
            //获取上传文件的输入流
            InputStream inputStream = file.getInputStream();
            //获取上传文件的名称
            String filename = file.getOriginalFilename();

            //1 在文件名称中里面添加随机唯一的值
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            filename = uuid + filename;

            //2 把文件按照日期进行分类
            //获取当前日期
            String datePath = new DateTime().toString("yyyy/MM/dd");
            filename = datePath + "/" + filename;

            //调用oss方法实现上传
            //第一个参数为 Bucket名称
            //第二个参数为 上传文件名称
            //第三个参数为 上传文件的输入流
            ossClient.putObject(bucketName,filename,inputStream);

            //关闭OSSClient
            ossClient.shutdown();

            //把上传之后文件路径返回
            String fileURL = "http://" + bucketName + "." + endpoint + "/" + filename;
            return fileURL;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
