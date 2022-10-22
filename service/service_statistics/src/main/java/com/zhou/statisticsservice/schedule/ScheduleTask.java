package com.zhou.statisticsservice.schedule;

import com.zhou.statisticsservice.service.StatisticsDailyService;
import com.zhou.statisticsservice.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ScheduleTask {
    @Autowired
    private StatisticsDailyService statisticsDailyService;

    //每日一点执行统计前一日的注册人数
    @Scheduled(cron = "0 0 1 * * ?")
    public void countRegisterNumber(){
        String yesterday = DateUtil.formatDate(DateUtil.addDays(new Date(),-1));
        statisticsDailyService.countRegisterNumber(yesterday);
    }
}
