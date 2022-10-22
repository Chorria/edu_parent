package com.zhou.eduservice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhou.eduservice.entity.EduTeacher;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author Zhou
 * @since 2022-03-10
 */
public interface EduTeacherService extends IService<EduTeacher> {

    Map<String, Object> getTeacherFrontList(Page<EduTeacher> eduTeacherPage);
}
