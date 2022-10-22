package com.zhou.statisticsservice.service;

import com.zhou.statisticsservice.entity.StatisticsDaily;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务类
 * </p>
 *
 * @author Zhou
 * @since 2022-09-25
 */
public interface StatisticsDailyService extends IService<StatisticsDaily> {

    void countRegisterNumber(String date);

    Map<String, Object> getEchart(String type, String startDate, String endDate);
}
