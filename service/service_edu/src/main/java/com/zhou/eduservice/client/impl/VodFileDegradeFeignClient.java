package com.zhou.eduservice.client.impl;

import com.zhou.commonutils.R;
import com.zhou.eduservice.client.VODClient;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 熔断器实现类
 */
@Component
public class VodFileDegradeFeignClient implements VODClient {
    @Override
    public R removeVODVideo(String videoSourceId) {
        return R.error().message("删除单个视频失败了!");
    }

    @Override
    public R removeVODVideoByIds(List<String> videoSourceIdList) {
        return R.error().message("删除多个视频失败了!");
    }
}
