package com.zhou.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhou.eduservice.entity.EduTeacher;
import com.zhou.eduservice.mapper.EduTeacherMapper;
import com.zhou.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author Zhou
 * @since 2022-03-10
 */
@Service
public class EduTeacherServiceImpl extends ServiceImpl<EduTeacherMapper, EduTeacher> implements EduTeacherService {

    @Override
    public Map<String, Object> getTeacherFrontList(Page<EduTeacher> eduTeacherPage) {
        //查询条件
        QueryWrapper<EduTeacher> eduTeacherQueryWrapper = new QueryWrapper<>();
        eduTeacherQueryWrapper.orderByDesc("id");
        //把分页数据封装到eduTeacherPage对象
        baseMapper.selectPage(eduTeacherPage,eduTeacherQueryWrapper);

        //获取数据
        List<EduTeacher> records = eduTeacherPage.getRecords();
        long current = eduTeacherPage.getCurrent();
        long pages = eduTeacherPage.getPages();
        long size = eduTeacherPage.getSize();
        long total = eduTeacherPage.getTotal();
        boolean hasPrevious = eduTeacherPage.hasPrevious(); //是否有上一页
        boolean hasNext = eduTeacherPage.hasNext(); //是否有上一页

        //封装参数
        Map<String,Object> map = new HashMap<>();
        map.put("records",records);
        map.put("current",current);
        map.put("pages",pages);
        map.put("size",size);
        map.put("total",total);
        map.put("hasPrevious",hasPrevious);
        map.put("hasNext",hasNext);

        //根据响应,返回参数
        return map;
    }
}
