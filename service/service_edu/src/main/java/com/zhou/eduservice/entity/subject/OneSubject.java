package com.zhou.eduservice.entity.subject;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

//一级分类的实体类
@Data
public class OneSubject {
    private String id;

    private String title;

    private List<TwoSubject> children = new ArrayList<>();
}
