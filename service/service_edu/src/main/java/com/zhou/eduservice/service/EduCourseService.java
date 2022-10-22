package com.zhou.eduservice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhou.eduservice.entity.EduCourse;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhou.eduservice.entity.frontVO.CourseQueryVO;
import com.zhou.eduservice.entity.frontVO.CourseWebVO;
import com.zhou.eduservice.entity.vo.CourseInfoVO;
import com.zhou.eduservice.entity.vo.CoursePublishVO;

import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author Zhou
 * @since 2022-06-28
 */
public interface EduCourseService extends IService<EduCourse> {
    String saveCourseInfo(CourseInfoVO courseInfoVO);

    void updateCourseInfo(CourseInfoVO courseInfoVO);

    CourseInfoVO getCourseInfo(String id);

    CoursePublishVO getCoursePublishVoById(String id);

    void publishCourseById(String id);

    int removeCourseInfoById(String id);

    Map<String, Object> getCourseFrontList(Page<EduCourse> eduCoursePage, CourseQueryVO courseQueryVO);

    CourseWebVO getCourseInfoFront(String id);
}
