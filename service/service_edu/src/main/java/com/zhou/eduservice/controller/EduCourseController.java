package com.zhou.eduservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhou.commonutils.R;
import com.zhou.eduservice.entity.EduCourse;
import com.zhou.eduservice.entity.vo.CourseQuery;
import com.zhou.eduservice.entity.vo.CourseInfoVO;
import com.zhou.eduservice.entity.vo.CoursePublishVO;
import com.zhou.eduservice.service.EduCourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
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
@Api(description = "课程信息管理")
@RestController
@RequestMapping("/eduservice/course")
@CrossOrigin
public class EduCourseController {
    @Autowired
    private EduCourseService eduCourseService;

    @ApiOperation(value = "添加课程信息")
    @PostMapping("/addCourseInfo")
    public R addCourseInfo(@ApiParam(name = "courseInfoVO",value = "课程信息实体",required = true) @RequestBody CourseInfoVO courseInfoVO){
        String id = eduCourseService.saveCourseInfo(courseInfoVO);
        return R.ok().data("id",id);
    }

    @ApiOperation(value = "删除课程信息")
    @DeleteMapping("/removeCourseInfo/{id}")
    public R removeCourseInfo(@ApiParam(name = "id",value = "课程信息ID", required = true) @PathVariable String id){
        int ret = eduCourseService.removeCourseInfoById(id);
        if (ret > 0) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    @ApiOperation(value = "修改课程信息")
    @PostMapping("/updateCourseInfo")
    public R updateCourseInfo(@ApiParam(name = "courseInfoVO",value = "课程信息实体",required = true) @RequestBody CourseInfoVO courseInfoVO){
        eduCourseService.updateCourseInfo(courseInfoVO);
        return R.ok();
    }

    @ApiOperation(value = "查询课程信息")
    @GetMapping("/getCourseInfo/{id}")
    public R getCourseInfo(@ApiParam(name = "id",value = "课程ID",required = true) @PathVariable String id){
        CourseInfoVO courseInfoVO = eduCourseService.getCourseInfo(id);
        return R.ok().data("courseInfoVO",courseInfoVO);
    }

    @ApiOperation(value = "查询发布的课程信息")
    @GetMapping("/getCoursePublishById/{id}")
    public R getCoursePublishById(@ApiParam(name = "id",value = "课程ID",required = true)@PathVariable String id){
        CoursePublishVO coursePublishVo = eduCourseService.getCoursePublishVoById(id);
        return R.ok().data("coursePublishVO",coursePublishVo);
    }

    @ApiOperation(value = "发布课程")
    @PutMapping("/publishCourse/{id}")
    public R publicCourse(@ApiParam(name = "id",value = "课程ID",required = true)@PathVariable String id){
        eduCourseService.publishCourseById(id);
        return R.ok();
    }

    @ApiOperation(value = "条件查询课程并分页")
    @PostMapping("/pageCourseCondition/{current}/{limit}")
    public R pageCourseCondition(@ApiParam(name = "current", value = "当前页", required = true) @PathVariable long current,
                                 @ApiParam(name = "limit", value = "每页数据条数", required = true) @PathVariable long limit,
                                 @ApiParam(name = "courseQuery", value = "课程查询条件实体", required = false) @RequestBody CourseQuery courseQuery){
        //构建条件
        QueryWrapper<EduCourse> eduCourseQueryWrapper = new QueryWrapper<>();
        //获取查询参数
        String title = courseQuery.getTitle();
        String teacherId = courseQuery.getTeacherId();
        String subjectParentId = courseQuery.getSubjectParentId();
        String subjectId = courseQuery.getSubjectId();
        //判断查询参数是否为空
        if (!StringUtils.isEmpty(title)) {
            eduCourseQueryWrapper.like("title",title);
        }
        if (!StringUtils.isEmpty(teacherId)) {
            eduCourseQueryWrapper.eq("teacher_id",teacherId);
        }
        if (!StringUtils.isEmpty(subjectParentId)) {
            eduCourseQueryWrapper.eq("subject_parent_id",subjectParentId);
        }
        if (!StringUtils.isEmpty(subjectId)) {
            eduCourseQueryWrapper.eq("subject_id",subjectId);
        }
        //降序排列
        eduCourseQueryWrapper.orderByDesc("gmt_create");
        //创建Page对象
        Page<EduCourse> eduCoursePage = new Page<>(current,limit);
        //调用方法实现条件分页查询
        eduCourseService.page(eduCoursePage, eduCourseQueryWrapper);
        //记录总记录数及查询的List集合
        long total = eduCoursePage.getTotal();
        List<EduCourse> eduCourseList = eduCoursePage.getRecords();
        return R.ok().data("total",total).data("eduCourseList",eduCourseList);
    }
}

