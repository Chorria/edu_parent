package com.zhou.eduservice.mapper;

import com.zhou.eduservice.entity.EduCourse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhou.eduservice.entity.frontVO.CourseWebVO;
import com.zhou.eduservice.entity.vo.CoursePublishVO;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author Zhou
 * @since 2022-06-28
 */
public interface EduCourseMapper extends BaseMapper<EduCourse> {
    /**
     * 获取发布课程的详情
     * @param id
     * @return
     */
    CoursePublishVO selectCoursePublishById(String id);

    /**
     * 获取客户端登录前台的课程详情
     * @return
     */
    CourseWebVO selectCourseInfoFront(String id);
}
