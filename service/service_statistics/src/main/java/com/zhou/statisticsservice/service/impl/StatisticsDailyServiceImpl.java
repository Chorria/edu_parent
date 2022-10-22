package com.zhou.statisticsservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhou.commonutils.R;
import com.zhou.commonutils.RandomUtil;
import com.zhou.servicebase.exceptionhandler.ZhouException;
import com.zhou.statisticsservice.client.UcenterClient;
import com.zhou.statisticsservice.entity.StatisticsDaily;
import com.zhou.statisticsservice.mapper.StatisticsDailyMapper;
import com.zhou.statisticsservice.service.StatisticsDailyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author Zhou
 * @since 2022-09-25
 */
@Service
public class StatisticsDailyServiceImpl extends ServiceImpl<StatisticsDailyMapper, StatisticsDaily> implements StatisticsDailyService {
    @Autowired
    private UcenterClient ucenterClient;

    @Override
    public void countRegisterNumber(String date) {
        //若数据存在,则删除
        QueryWrapper<StatisticsDaily> statisticsDailyQueryWrapper = new QueryWrapper<>();
        statisticsDailyQueryWrapper.eq("date_calculated",date);
        baseMapper.delete(statisticsDailyQueryWrapper);
        //统计当日用户注册的人数
        R r = ucenterClient.getRegisterNumber(date);
        Integer registerNumber = (Integer) r.getData().get("registerNumber");
        //封装参数
        StatisticsDaily statisticsDaily = new StatisticsDaily();
        statisticsDaily.setDateCalculated(date);
        statisticsDaily.setRegisterNum(registerNumber);
        statisticsDaily.setLoginNum(RandomUtils.nextInt(100,200));
        statisticsDaily.setVideoViewNum(RandomUtils.nextInt(100,200));
        statisticsDaily.setCourseNum(RandomUtils.nextInt(100,200));
        //调用mapper方法,记录当日用户注册人数
        baseMapper.insert(statisticsDaily);
    }

    @Override
    public Map<String, Object> getEchart(String type, String startDate, String endDate) {
        //查询在这个时间段中的数据
        QueryWrapper<StatisticsDaily> statisticsDailyQueryWrapper = new QueryWrapper<>();
        statisticsDailyQueryWrapper.between("date_calculated",startDate,endDate);
        List<StatisticsDaily> statisticsDailyList = baseMapper.selectList(statisticsDailyQueryWrapper);

        List<String> dateCalculatedList = new ArrayList<>();
        List<Integer> dataList = new ArrayList<>();
        for (int i = 0; i < statisticsDailyList.size(); i++) {
            //封装日期参数
            StatisticsDaily statisticsDaily = statisticsDailyList.get(i);
            dateCalculatedList.add(statisticsDaily.getDateCalculated());
            //封装数据参数
            switch (type) {
                case "register_num" :
                    dataList.add(statisticsDaily.getRegisterNum());
                    break;
                case "login_num" :
                    dataList.add(statisticsDaily.getLoginNum());
                    break;
                case "video_view_num" :
                    dataList.add(statisticsDaily.getVideoViewNum());
                    break;
                case "course_num" :
                    dataList.add(statisticsDaily.getCourseNum());
                    break;
                default :
                    break;
            }
        }
        //封装响应参数
        Map<String,Object> retMap = new HashMap<>();
        retMap.put("dateCalculatedList",dateCalculatedList);
        retMap.put("dataList",dataList);
        return retMap;
    }
}
