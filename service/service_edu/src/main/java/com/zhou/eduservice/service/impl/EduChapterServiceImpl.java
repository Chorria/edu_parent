package com.zhou.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhou.eduservice.entity.EduChapter;
import com.zhou.eduservice.entity.EduVideo;
import com.zhou.eduservice.entity.chapter.ChapterVO;
import com.zhou.eduservice.entity.chapter.VideoVO;
import com.zhou.eduservice.mapper.EduChapterMapper;
import com.zhou.eduservice.service.EduChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhou.eduservice.service.EduVideoService;
import com.zhou.servicebase.exceptionhandler.ZhouException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author Zhou
 * @since 2022-06-28
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {
    @Autowired
    private EduVideoService eduVideoService;
    //课程大纲列表,根据courseId查询
    @Override
    public List<ChapterVO> getChapterVideoByCourseId(String courseId) {
        //查询章节chapter
        QueryWrapper<EduChapter> eduChapterQueryWrapper = new QueryWrapper<>();
        eduChapterQueryWrapper.eq("course_id",courseId);
        List<EduChapter> eduChapterList = baseMapper.selectList(eduChapterQueryWrapper);
        //查询小节video
        QueryWrapper<EduVideo> eduVideoQueryWrapper = new QueryWrapper<>();
        eduVideoQueryWrapper.eq("course_id",courseId);
        List<EduVideo> eduVideoList = eduVideoService.list(eduVideoQueryWrapper);
        //封装参数
        ChapterVO chapterVO = null;
        List<ChapterVO> chapterVOList = new ArrayList<>();
        for(int i=0; i<eduChapterList.size(); i++) {
            EduChapter eduChapter = eduChapterList.get(i);
            chapterVO = new ChapterVO();
            BeanUtils.copyProperties(eduChapter,chapterVO);
            chapterVOList.add(chapterVO);

            //封装参数
            VideoVO videoVO = null;
            List<VideoVO> videoVOList = new ArrayList<>();
            for(int j=0; j<eduVideoList.size(); j++) {
                EduVideo eduVideo = eduVideoList.get(j);
                //判断该小节是否所属该章节
                if(eduVideo.getChapterId().equals(eduChapter.getId())) {
                    videoVO = new VideoVO();
                    BeanUtils.copyProperties(eduVideo,videoVO);
                    videoVOList.add(videoVO);
                }
            }
            chapterVO.setChildren(videoVOList);
        }

        return chapterVOList;
    }
    //删除章节
    @Override
    public boolean removeChapter(String id) {
        //若章节存在小节，则不删除
        QueryWrapper<EduVideo> eduVideoQueryWrapper = new QueryWrapper<>();
        eduVideoQueryWrapper.eq("chapter_id",id);
        int count = eduVideoService.count(eduVideoQueryWrapper);
        if (count > 0) {
            throw new ZhouException(20001,"该章节存在小节,不能直接删除该章节。须先删除章节里的小节才能删除该章节!");
        } else {
            int ret = baseMapper.deleteById(id);
            return ret > 0;
        }
    }
    //根据课程信息Id删除章节
    @Override
    public void removeChapterByCourseId(String courseId) {
        //若章节存在小节,则不删除
        QueryWrapper<EduVideo> eduVideoQueryWrapper = new QueryWrapper<>();
        eduVideoQueryWrapper.eq("course_id",courseId);
        int count = eduVideoService.count(eduVideoQueryWrapper);
        if (count > 0) {
            throw new ZhouException(20001,"该章节存在小节,不能直接删除该章节。须先删除章节里的小节才能删除该章节!");
        } else {
            QueryWrapper<EduChapter> eduChapterQueryWrapper = new QueryWrapper<>();
            eduChapterQueryWrapper.eq("course_id",courseId);
            //删除章节
            baseMapper.delete(eduChapterQueryWrapper);
        }
    }
}
