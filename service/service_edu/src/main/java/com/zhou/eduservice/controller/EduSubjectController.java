package com.zhou.eduservice.controller;


import com.zhou.commonutils.R;
import com.zhou.eduservice.entity.subject.OneSubject;
import com.zhou.eduservice.service.EduSubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author Zhou
 * @since 2022-06-23
 */
@Api(description = "课程分类管理")
@RestController
@RequestMapping("/eduservice/subject")
@CrossOrigin
public class EduSubjectController {

    @Autowired
    public EduSubjectService eduSubjectService;

    //添加课程分类
    //获取上传的文件,对文件内容进行读取
    @ApiOperation(value = "添加课程分类")
    @PostMapping("/addSubject")
    public R addSubject(@ApiParam(name = "file",value = "上传的文件",required = true) MultipartFile file){
        eduSubjectService.saveSubject(file,eduSubjectService);
        return R.ok();
    }

    //课程分类列表
    @ApiOperation(value = "查询课程分类")
    @GetMapping("/getAllSubject")
    public R getAllSubject(){
        return R.ok().data("subjectList",eduSubjectService.getAllOneTwoSubject());
    }
}

