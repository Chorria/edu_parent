package com.zhou.eduservice.controller.front;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhou.commonutils.JwtUtils;
import com.zhou.commonutils.R;
import com.zhou.commonutils.frontVO.CourseWebVOOrder;
import com.zhou.eduservice.client.OrderClient;
import com.zhou.eduservice.entity.EduCourse;
import com.zhou.eduservice.entity.chapter.ChapterVO;
import com.zhou.eduservice.entity.frontVO.CourseQueryVO;
import com.zhou.eduservice.entity.frontVO.CourseWebVO;
import com.zhou.eduservice.service.EduChapterService;
import com.zhou.eduservice.service.EduCourseService;
import com.zhou.servicebase.exceptionhandler.ZhouException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Api(description = "前端课程接口管理")
@RestController
@RequestMapping("/eduservice/courseFront")
@CrossOrigin
public class CourseFrontController {
    @Autowired
    private EduCourseService eduCourseService;
    @Autowired
    private EduChapterService eduChapterService;
    @Autowired
    private OrderClient orderClient;

    @ApiOperation("分页查询课程接口")
    @PostMapping("/queryCourseForPageByCondition/{current}/{limit}")
    public R getCourseFrontList(@ApiParam(name = "current",value = "当前页",required = true) @PathVariable long current,
                                @ApiParam(name = "limit",value = "记录数",required = true) @PathVariable long limit,
                                @ApiParam(name = "courseQueryVO",value = "课程查询实体类",required = false) @RequestBody(required = false) CourseQueryVO courseQueryVO){
        Page<EduCourse> eduCoursePage = new Page<>(current,limit);
        Map<String,Object> retMap = eduCourseService.getCourseFrontList(eduCoursePage,courseQueryVO);
        return R.ok().data(retMap);
    }

    @ApiOperation("课程详情接口")
    @GetMapping("/getCourseInfo/{id}")
    public R getCourseInfo(@ApiParam(name = "id",value = "课程Id",required = true) @PathVariable String id, HttpServletRequest request){
        //根据课程id，编写sql语句查询课程信息
        CourseWebVO courseWebVO = eduCourseService.getCourseInfoFront(id);
        //根据课程id，编写sql语句查询课程信息
        List<ChapterVO> chapterVideoList = eduChapterService.getChapterVideoByCourseId(id);
        //查询该课程是否被购买
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        if (memberId == "") {
            throw new ZhouException(20001,"获取用户信息失败,请登录!");
        }
        Boolean isPay = orderClient.isPay(id, JwtUtils.getMemberIdByJwtToken(request));
        return R.ok().data("courseWebVO",courseWebVO).data("chapterVideoList",chapterVideoList).data("isPay",isPay);
    }

    @ApiOperation("获取生成订单所需的课程信息接口")
    @GetMapping("/getCourseInfoForOrder/{id}")
    public CourseWebVOOrder getCourseInfoForOrder(@ApiParam(name = "id",value = "课程id",required = true) @PathVariable String id){
        CourseWebVO courseWebVO = eduCourseService.getCourseInfoFront(id);
        CourseWebVOOrder courseWebVOOrder = new CourseWebVOOrder();
        BeanUtils.copyProperties(courseWebVO,courseWebVOOrder);
        return courseWebVOOrder;
    }
}
