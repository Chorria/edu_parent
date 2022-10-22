package com.zhou.vodservice.controller;

import com.zhou.commonutils.R;
import com.zhou.vodservice.service.VODService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Api(description = "上传视频接口")
@RestController
@RequestMapping("/eduvod/video")
@CrossOrigin
public class VODController {
    @Autowired
    private VODService vodService;

    @ApiOperation(value = "上传小节的课程视频")
    @PostMapping("/uploadVideo")
    public R uploadAlyVideo(@ApiParam(name = "multipartFile",value = "上传的视频",required = true) MultipartFile file){
        //返回上传视频id
        String videoID = vodService.uploadVideo(file);
        return R.ok().data("videoID",videoID);
    }

    @ApiOperation(value = "删除单个小节的课程视频")
    @DeleteMapping("/removeVODVideo/{videoSourceId}")
    public R removeVODVideo(@ApiParam(name = "videoSourceId",value = "阿里云视频点播ID",required = true)@PathVariable String videoSourceId){
        vodService.removeVODVideo(videoSourceId);
        return R.ok();
    }

    @ApiOperation(value = "删除多个小节的课程视频")
    @DeleteMapping("/removeVODVideo")
    public R removeVODVideoByIds(@ApiParam(name = "videoSourceIdList",value = "阿里云视频点播ID集合",required = true)@RequestParam("videoSourceIdList") List<String> videoSourceIdList){
        vodService.removeVODVideoByIds(videoSourceIdList);
        return R.ok();
    }

    @ApiOperation(value = "根据视频id获取播放凭证")
    @GetMapping("/playForVideo/{id}")
    public R videoPlayer(@ApiParam(name = "id",value = "阿里云视频点播ID",required = true) @PathVariable String id){
        String videoAuth = vodService.getVideoAuth(id);
        return R.ok().data("videoAuth",videoAuth);
    }


}
