package com.zhou.ossservice.service;

import org.springframework.web.multipart.MultipartFile;

public interface OSSService {
    String uploadFileAvatar(MultipartFile file);
}
