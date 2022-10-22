package com.zhou.eduservice.entity.chapter;

import lombok.Data;

import java.util.List;

@Data
public class ChapterVO {
    private String id;

    private String title;

    private List<VideoVO> children;
}
