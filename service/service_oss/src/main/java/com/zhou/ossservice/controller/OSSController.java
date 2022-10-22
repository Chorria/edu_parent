package com.zhou.ossservice.controller;

import com.zhou.commonutils.R;
import com.zhou.ossservice.service.OSSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/eduoss/fileoss")
@CrossOrigin
public class OSSController {
    @Autowired
    private OSSService ossService;

    //上传头像的方法
    @PostMapping
    public R uploadFileAvatar(MultipartFile file){
        //获取上传文件    MultipartFile
        //返回上传到OSS的路径
        String avatarURL = ossService.uploadFileAvatar(file);
        return R.ok().data("avatarURL",avatarURL);
    }

}
