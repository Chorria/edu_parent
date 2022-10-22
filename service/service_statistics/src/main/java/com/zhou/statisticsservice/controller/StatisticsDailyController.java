package com.zhou.statisticsservice.controller;


import com.zhou.commonutils.R;
import com.zhou.statisticsservice.service.StatisticsDailyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author Zhou
 * @since 2022-09-25
 */
@Api(description = "数据统计管理模块")
@RestController
@RequestMapping("/edustatistics/statistics-daily")
@CrossOrigin
public class StatisticsDailyController {

    @Autowired
    private StatisticsDailyService statisticsDailyService;

    @ApiOperation(value = "记录当日注册用户人数")
    @PostMapping("/count/registerNumber/{date}")
    public R countRegisterNumber(@ApiParam(name = "date",value = "日期参数",required = true) @PathVariable String date){
        statisticsDailyService.countRegisterNumber(date);
        return R.ok();
    }

    @ApiOperation(value = "生成Echart数据图表")
    @GetMapping("/chart/createEchart/{type}/{startDate}/{endDate}")
    public R getEchart(@ApiParam(name = "type",value = "图表数据类型",required = true) @PathVariable String type,
                       @ApiParam(name = "startDate",value = "开始日期",required = true) @PathVariable String startDate,
                       @ApiParam(name = "endDate",value = "截止日期",required = true) @PathVariable String endDate){
        Map<String,Object> retMap = statisticsDailyService.getEchart(type,startDate,endDate);
        return R.ok().data(retMap);
    }

    


}

