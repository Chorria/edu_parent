package com.zhou.eduservice.controller.front;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhou.commonutils.R;
import com.zhou.eduservice.entity.EduCourse;
import com.zhou.eduservice.entity.EduTeacher;
import com.zhou.eduservice.service.EduCourseService;
import com.zhou.eduservice.service.EduTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(description = "课程及名师管理")
@RestController
@RequestMapping("/eduservice/front")
@CrossOrigin
public class IndexFrontController {
    @Autowired
    private EduCourseService eduCourseService;
    @Autowired
    private EduTeacherService eduTeacherService;

    @ApiOperation(value = "查询课程及名师")
    @GetMapping("/index")
    public R index(){
        //查询前8条热门课程
        QueryWrapper<EduCourse> eduCourseQueryWrapper = new QueryWrapper<>();
        eduCourseQueryWrapper.orderByDesc("id");
        eduCourseQueryWrapper.last("limit 8");
        List<EduCourse> eduCourseList = eduCourseService.list(eduCourseQueryWrapper);
        //查询前4条名师
        QueryWrapper<EduTeacher> eduTeacherQueryWrapper = new QueryWrapper<>();
        eduTeacherQueryWrapper.orderByDesc("id");
        eduTeacherQueryWrapper.last("limit 4");
        List<EduTeacher> eduTeacherList = eduTeacherService.list(eduTeacherQueryWrapper);
        return R.ok().data("eduCourseList",eduCourseList).data("eduTeacherList",eduTeacherList);
    }
}
