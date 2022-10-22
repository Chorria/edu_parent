package com.zhou.eduservice.controller;


import com.zhou.commonutils.R;
import com.zhou.eduservice.entity.EduChapter;
import com.zhou.eduservice.entity.chapter.ChapterVO;
import com.zhou.eduservice.service.EduChapterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author Zhou
 * @since 2022-06-28
 */
@Api(description = "章节管理")
@RestController
@RequestMapping("/eduservice/chapter")
@CrossOrigin
public class EduChapterController {
    @Autowired
    private EduChapterService eduChapterService;

    @ApiOperation(value = "查询课程基本信息")
    @GetMapping("/getChapterVideo/{courseId}")
    public R getChapterVideo(@PathVariable String courseId){
        List<ChapterVO> chapterVOList = eduChapterService.getChapterVideoByCourseId(courseId);
        return R.ok().data("chapterVOList",chapterVOList);
    }

    @ApiOperation(value = "新增章节")
    @PostMapping("/addChapter")
    public R addChapter(@ApiParam(name = "eduChapter",value = "章节对象",required = true) @RequestBody EduChapter eduChapter){
        eduChapterService.save(eduChapter);
        return R.ok();
    }

    @ApiOperation(value = "删除章节")
    @DeleteMapping("/removeChapter/{id}")
    public R removeChapter(@ApiParam(name = "id",value = "课程id",required = true) @PathVariable String id){
        boolean ret = eduChapterService.removeById(id);
        if (ret) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    @ApiOperation(value = "根据ID修改章节")
    @PostMapping("/updateChapter")
    public R updateChapter(@ApiParam(name = "eduChapter",value = "章节对象",required = true) @RequestBody EduChapter eduChapter){
        eduChapterService.updateById(eduChapter);
        return R.ok();
    }

    @ApiOperation(value = "根据ID查询章节")
    @GetMapping("/getChapter/{id}")
    public R getChapter(@ApiParam(name = "id",value = "章节ID",required = true) @PathVariable String id){
        EduChapter eduChapter = eduChapterService.getById(id);
        return R.ok().data("eduChapter",eduChapter);
    }

}

