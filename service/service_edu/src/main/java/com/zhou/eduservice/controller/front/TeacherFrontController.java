package com.zhou.eduservice.controller.front;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhou.commonutils.R;
import com.zhou.eduservice.entity.EduCourse;
import com.zhou.eduservice.entity.EduTeacher;
import com.zhou.eduservice.service.EduCourseService;
import com.zhou.eduservice.service.EduTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(description = "前端讲师接口管理")
@RestController
@RequestMapping("/eduservice/teacherFront")
@CrossOrigin
public class TeacherFrontController {
    @Autowired
    private EduTeacherService eduTeacherService;
    @Autowired
    private EduCourseService eduCourseService;

    @ApiOperation("分页查询讲师接口")
    @GetMapping("/queryTeacherForPageByCondition/{current}/{limit}")
    public R getTeacherFrontList(@ApiParam(name = "current",value = "当前页",required = true) @PathVariable long current,
                                 @ApiParam(name = "limit",value = "记录数",required = true) @PathVariable long limit){
        Page<EduTeacher> eduTeacherPage = new Page<>(current,limit);
        Map<String,Object> retMap = eduTeacherService.getTeacherFrontList(eduTeacherPage);
        return R.ok().data(retMap);
    }

    @ApiOperation("查询讲师详情接口")
    @GetMapping("/queryTeacherByDetail/{id}")
    public R getTeacherFrontInfo(@ApiParam(name = "id",value = "讲师ID",required = true) @PathVariable String id){
        //查询讲师详情
        EduTeacher eduTeacher = eduTeacherService.getById(id);
        //查询讲师所讲的课程
        QueryWrapper<EduCourse> eduCourseQueryWrapper = new QueryWrapper<>();
        eduCourseQueryWrapper.eq("teacher_id",id);

        List<EduCourse> eduCourseList = eduCourseService.list(eduCourseQueryWrapper);
        //根据响应,返回信息
        return R.ok().data("eduTeacher",eduTeacher).data("eduCourseList",eduCourseList);
    }
}
