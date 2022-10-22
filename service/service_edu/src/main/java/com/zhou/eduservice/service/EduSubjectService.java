package com.zhou.eduservice.service;

import com.zhou.eduservice.entity.EduSubject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhou.eduservice.entity.subject.OneSubject;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author Zhou
 * @since 2022-06-23
 */
public interface EduSubjectService extends IService<EduSubject> {
    void saveSubject(MultipartFile file,EduSubjectService eduSubjectService);

    List<OneSubject> getAllOneTwoSubject();
}
