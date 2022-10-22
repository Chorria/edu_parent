package com.zhou.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhou.commonutils.CourseConstants;
import com.zhou.eduservice.client.VODClient;
import com.zhou.eduservice.entity.EduCourse;
import com.zhou.eduservice.entity.EduCourseDescription;
import com.zhou.eduservice.entity.frontVO.CourseQueryVO;
import com.zhou.eduservice.entity.frontVO.CourseWebVO;
import com.zhou.eduservice.entity.vo.CourseInfoVO;
import com.zhou.eduservice.entity.vo.CoursePublishVO;
import com.zhou.eduservice.mapper.EduCourseMapper;
import com.zhou.eduservice.service.EduChapterService;
import com.zhou.eduservice.service.EduCourseDescriptionService;
import com.zhou.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhou.eduservice.service.EduVideoService;
import com.zhou.servicebase.exceptionhandler.ZhouException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author Zhou
 * @since 2022-06-28
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {
    @Autowired
    private EduCourseDescriptionService eduCourseDescriptionService;
    @Autowired
    private EduVideoService eduVideoService;
    @Autowired
    private EduChapterService eduChapterService;
    @Autowired
    private VODClient vodClient;

    //添加课程基本信息的方法
    @Override
    public String saveCourseInfo(CourseInfoVO courseInfoVO) {
        //1 向课程表添加课程基本信息
        //courseInfoVO转化为eduCourse
        EduCourse eduCourse = new EduCourse();
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        BeanUtils.copyProperties(courseInfoVO,eduCourse);
        //调用Mapper添加课程
        int ret = baseMapper.insert(eduCourse);
        if (ret == 0) {
            throw new ZhouException(20001,"添加课程失败!");
        }
        //获取添加之后的课程id
        String eduCourseId = eduCourse.getId();
        //2 向课程简介表添加课程简介
        //edu_course_description
        //设置描述id就是课程id
        eduCourseDescription.setId(eduCourse.getId());
        eduCourseDescription.setDescription(courseInfoVO.getDescription());
        //调用service方法,添加课程信息
        eduCourseDescriptionService.save(eduCourseDescription);
        return eduCourseId;
    }
    //修改课程信息
    @Override
    public void updateCourseInfo(CourseInfoVO courseInfoVO) {
        EduCourse eduCourse = new EduCourse();
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        //封装两个实体类
        BeanUtils.copyProperties(courseInfoVO,eduCourse);
        //修改课程表
        int ret = baseMapper.updateById(eduCourse);
        if (ret == 0) {
            throw new ZhouException(20001,"修改课程失败!");
        }
        //修改课程简介表
        eduCourseDescription.setId(eduCourse.getId());
        eduCourseDescription.setDescription(courseInfoVO.getDescription());
        eduCourseDescriptionService.updateById(eduCourseDescription);
    }
    //查询课程信息
    @Override
    public CourseInfoVO getCourseInfo(String id) {
        //返回参数
        CourseInfoVO courseInfoVO = new CourseInfoVO();
        //查修课程表和课程简介表,获取相关数据
        EduCourse eduCourse = baseMapper.selectById(id);
        EduCourseDescription eduCourseDescription = eduCourseDescriptionService.getById(id);
        //封装参数
        BeanUtils.copyProperties(eduCourse,courseInfoVO);
        courseInfoVO.setDescription(eduCourseDescription.getDescription());
        return courseInfoVO;
    }
    //查询发布的课程信息
    @Override
    public CoursePublishVO getCoursePublishVoById(String id) {
        CoursePublishVO coursePublishVO = baseMapper.selectCoursePublishById(id);
        return coursePublishVO;
    }
    //发布课程
    @Override
    public void publishCourseById(String id) {
        EduCourse eduCourse = new EduCourse();
        eduCourse.setId(id);
        eduCourse.setStatus(CourseConstants.COURSE_STATUS_NORMAL);
        baseMapper.updateById(eduCourse);
    }

    @Override
    public int removeCourseInfoById(String id) {
        //删除课程小节
        eduVideoService.removeVideoByCourseId(id);
        //删除课程章节
        eduChapterService.removeChapterByCourseId(id);
        //删除课程简介
        eduCourseDescriptionService.removeById(id);
        //删除课程
        int ret = baseMapper.deleteById(id);
        if (ret == 0) {
            throw new ZhouException(20001,"删除课程信息失败,请稍后再试!");
        }
        return ret;
    }

    @Override
    public Map<String, Object> getCourseFrontList(Page<EduCourse> eduCoursePage, CourseQueryVO courseQueryVO) {
        QueryWrapper<EduCourse> eduCourseQueryWrapper = new QueryWrapper<>();
        //非空判断
        //判断一级分类是否为空
        if (!StringUtils.isEmpty(courseQueryVO.getSubjectParentId())){
            eduCourseQueryWrapper.eq("subject_parent_id",courseQueryVO.getSubjectParentId());
        }
        //判断二级分类是否为空
        if (!StringUtils.isEmpty(courseQueryVO.getSubjectId())){
            eduCourseQueryWrapper.eq("subject_id",courseQueryVO.getSubjectId());
        }

        if (!StringUtils.isEmpty(courseQueryVO.getBuyCountSort())){
            //判断排序销量是否为空
            eduCourseQueryWrapper.orderByDesc("buy_count");
        }
        if (!StringUtils.isEmpty(courseQueryVO.getGmtCreateSort())){
            //判断最新时间排序是否为空
            eduCourseQueryWrapper.orderByDesc("gmt_create");
        }
        if (!StringUtils.isEmpty(courseQueryVO.getPriceSort())){
            //判断价格排序是否为空
            eduCourseQueryWrapper.orderByDesc("price");
        }
        //把分页数据封装到eduCoursePage对象
        baseMapper.selectPage(eduCoursePage,eduCourseQueryWrapper);

        List<EduCourse> records = eduCoursePage.getRecords();
        long pages = eduCoursePage.getPages();
        long total = eduCoursePage.getTotal();
        long current = eduCoursePage.getCurrent();
        long size = eduCoursePage.getSize();
        boolean hasPrevious = eduCoursePage.hasPrevious();
        boolean hasNext = eduCoursePage.hasNext();
        //封装参数
        Map<String,Object> map = new HashMap<>();
        map.put("records",records);
        map.put("pages",pages);
        map.put("total",total);
        map.put("current",current);
        map.put("size",size);
        map.put("hasPrevious",hasPrevious);
        map.put("hasNext",hasNext);
        //根据响应,返回参数
        return map;
    }

    @Override
    public CourseWebVO getCourseInfoFront(String id) {
        //获取课程详情
        CourseWebVO courseWebVO = baseMapper.selectCourseInfoFront(id);
        return courseWebVO;
    }
}
