package com.zhou.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.zhou.commonutils.R;
import com.zhou.eduservice.client.VODClient;
import com.zhou.eduservice.entity.EduVideo;
import com.zhou.eduservice.mapper.EduVideoMapper;
import com.zhou.eduservice.service.EduVideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhou.servicebase.exceptionhandler.ZhouException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author Zhou
 * @since 2022-06-28
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {
    @Autowired
    private VODClient vodClient;
    //根据课程信息ID删除小节
    @Override
    public void removeVideoByCourseId(String courseId) {
        //根据课程id查询课程所有的视频id
        QueryWrapper<EduVideo> deleteVideoQueryWrapper = new QueryWrapper<>();
        deleteVideoQueryWrapper.eq("course_id",courseId);
        deleteVideoQueryWrapper.select("video_source_id");
        List<EduVideo> eduVideoList = baseMapper.selectList(deleteVideoQueryWrapper);

        //封装videoSourceId集合
        List<String> videoSourceIdList = new ArrayList<>();
        for (int i=0; i<eduVideoList.size(); i++) {
            EduVideo eduVideo = eduVideoList.get(i);
            String videoSourceId = eduVideo.getVideoSourceId();
            //判断该小节中是否有点播视频
            if (!StringUtils.isEmpty(videoSourceId)) {
                videoSourceIdList.add(videoSourceId);
            }
        }

        //根据多个视频id删除多个视频
        if (videoSourceIdList.size() > 0) {
            vodClient.removeVODVideoByIds(videoSourceIdList);
        }

        QueryWrapper<EduVideo> deleteQueryWrapper = new QueryWrapper<>();
        deleteQueryWrapper.eq("course_id",courseId);
        baseMapper.delete(deleteQueryWrapper);
    }
}
