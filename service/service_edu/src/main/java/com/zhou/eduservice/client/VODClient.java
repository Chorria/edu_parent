package com.zhou.eduservice.client;

import com.zhou.commonutils.R;
import com.zhou.eduservice.client.impl.VodFileDegradeFeignClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "service-vod",fallback = VodFileDegradeFeignClient.class )
@Component("vodClient")
public interface VODClient {
    @DeleteMapping("/eduvod/video/removeVODVideo/{videoSourceId}")
    public R removeVODVideo(@PathVariable("videoSourceId") String videoSourceId);

    @DeleteMapping("/eduvod/video/removeVODVideo")
    public R removeVODVideoByIds(@RequestParam("videoSourceIdList") List<String> videoSourceIdList);
}
