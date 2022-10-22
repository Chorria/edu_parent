package com.zhou.vodservice.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VODService {
    String uploadVideo(MultipartFile multipartFile);

    void removeVODVideo(String videoSourceId);

    void removeVODVideoByIds(List<String> videoSourceIdList);

    String getVideoAuth(String id);
}
