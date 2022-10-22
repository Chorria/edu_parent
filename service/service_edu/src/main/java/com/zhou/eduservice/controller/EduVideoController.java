package com.zhou.eduservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhou.commonutils.R;
import com.zhou.eduservice.client.VODClient;
import com.zhou.eduservice.entity.EduVideo;
import com.zhou.eduservice.service.EduVideoService;
import com.zhou.servicebase.exceptionhandler.ZhouException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ddf.EscherDump;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author Zhou
 * @since 2022-06-28
 */
@Api(description = "章节小节管理")
@RestController
@RequestMapping("/eduservice/video")
@CrossOrigin
public class EduVideoController {
    @Autowired
    private EduVideoService eduVideoService;
    @Autowired
    private VODClient vodClient;

    @ApiOperation(value = "添加小节")
    @PostMapping("/addVideo")
    public R addVideo(@ApiParam(name = "eduVideo",value = "小节实体",required = true) @RequestBody EduVideo eduVideo){
        eduVideoService.save(eduVideo);
        return R.ok();
    }

    @ApiOperation(value = "删除小节")
    @DeleteMapping("/removeVideo/{id}")
    public R removeVideo(@ApiParam(name = "id",value = "小节ID",required = true)@PathVariable String id){
        //删除阿里云点播的视频
        EduVideo eduVideo = eduVideoService.getById(id);
        String videoSourceId = eduVideo.getVideoSourceId();
        //若存在阿里云点播的视频
        if (!StringUtils.isEmpty(videoSourceId)) {
            //删除点播视频
            R result = vodClient.removeVODVideo(videoSourceId);
            //根据视频id，远程调用实现视频删除
            if (result.getCode() == 20001) {
                throw new ZhouException(20001,"删除单个视频失败,熔断器开始执行!");
            }
        }
        boolean ret = eduVideoService.removeById(id);
        if (ret) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    @ApiOperation(value = "修改小节")
    @PostMapping("/updateVideo")
    public R updateVideo(@ApiParam(name = "eduVideo",value = "小节实体",required = true)@RequestBody EduVideo eduVideo){
        eduVideoService.updateById(eduVideo);
        return R.ok();
    }

    @ApiOperation(value = "查询小节")
    @GetMapping("/getVideo/{id}")
    public R getVideo(@ApiParam(name = "id",value = "小节ID",required = true)@PathVariable String id){
        EduVideo eduVideo = eduVideoService.getById(id);
        return R.ok().data("eduVideo",eduVideo);
    }
}

