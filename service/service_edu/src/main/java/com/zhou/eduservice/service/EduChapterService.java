package com.zhou.eduservice.service;

import com.zhou.eduservice.entity.EduChapter;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhou.eduservice.entity.chapter.ChapterVO;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author Zhou
 * @since 2022-06-28
 */
public interface EduChapterService extends IService<EduChapter> {
    List<ChapterVO> getChapterVideoByCourseId(String courseId);

    boolean removeChapter(String id);

    void removeChapterByCourseId(String courseId);
}
