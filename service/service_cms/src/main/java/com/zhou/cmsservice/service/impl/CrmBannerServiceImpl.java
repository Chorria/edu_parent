package com.zhou.cmsservice.service.impl;

import com.zhou.cmsservice.entity.CrmBanner;
import com.zhou.cmsservice.mapper.CrmBannerMapper;
import com.zhou.cmsservice.service.CrmBannerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务实现类
 * </p>
 *
 * @author Zhou
 * @since 2022-07-20
 */
@Service
public class CrmBannerServiceImpl extends ServiceImpl<CrmBannerMapper, CrmBanner> implements CrmBannerService {

    @Cacheable(value = "banner",key = "'selectIndexList'")
    @Override
    public List<CrmBanner> queryAllBanner() {
        List<CrmBanner> crmBannerList = baseMapper.selectList(null);
        return crmBannerList;
    }
}
