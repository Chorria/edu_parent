package com.zhou.cmsservice.controller;


import com.zhou.cmsservice.entity.CrmBanner;
import com.zhou.cmsservice.service.CrmBannerService;
import com.zhou.commonutils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 首页banner表 前端控制器
 * </p>
 *
 * @author Zhou
 * @since 2022-07-20
 */
@Api(description = "轮播图管理")
@RestController
@RequestMapping("/educms/bannerFront")
@CrossOrigin
public class BannerFrontController {
    @Autowired
    private CrmBannerService crmBannerService;

    @ApiOperation(value = "获取所有的轮播图")
    @GetMapping("/queryAllBanner")
    public R queryAllBanner(){
        List<CrmBanner> crmBannerList = crmBannerService.queryAllBanner();
        return R.ok().data("crmBannerList",crmBannerList);
    }

}

