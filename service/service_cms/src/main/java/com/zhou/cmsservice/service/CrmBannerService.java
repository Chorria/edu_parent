package com.zhou.cmsservice.service;

import com.zhou.cmsservice.entity.CrmBanner;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务类
 * </p>
 *
 * @author Zhou
 * @since 2022-07-20
 */
public interface CrmBannerService extends IService<CrmBanner> {
    List<CrmBanner> queryAllBanner();
}
