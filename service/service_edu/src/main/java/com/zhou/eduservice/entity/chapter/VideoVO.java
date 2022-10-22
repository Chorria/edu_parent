package com.zhou.eduservice.entity.chapter;

import lombok.Data;

@Data
public class VideoVO {
    private String id;

    private String title;
    //阿里云点播视频的id
    private String videoSourceId;
}
