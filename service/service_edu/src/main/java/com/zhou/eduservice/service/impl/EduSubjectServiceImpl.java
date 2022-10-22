package com.zhou.eduservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.builder.ExcelReaderSheetBuilder;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.zhou.eduservice.entity.EduSubject;
import com.zhou.eduservice.entity.excel.SubjectData;
import com.zhou.eduservice.entity.subject.OneSubject;
import com.zhou.eduservice.entity.subject.TwoSubject;
import com.zhou.eduservice.listener.SubjectExcelListener;
import com.zhou.eduservice.mapper.EduSubjectMapper;
import com.zhou.eduservice.service.EduSubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author Zhou
 * @since 2022-06-23
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {
    //添加课程分类
    @Override
    public void saveSubject(MultipartFile file, EduSubjectService eduSubjectService) {
        try {
            //文件输入流
            InputStream inputStream = file.getInputStream();
            //调用方法进行读取
            EasyExcel.read(inputStream,SubjectData.class,new SubjectExcelListener(eduSubjectService)).sheet().doRead();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<OneSubject> getAllOneTwoSubject() {
        //查询所有的一级分类
        QueryWrapper<EduSubject> oneSubjectQueryWrapper = new QueryWrapper<>();
        oneSubjectQueryWrapper.eq("parent_id","0");
        List<EduSubject> oneEduSubjectList = baseMapper.selectList(oneSubjectQueryWrapper);

        //查询出所有的二级分类
        QueryWrapper<EduSubject> twoSubjectQueryWrapper = new QueryWrapper<>();
        twoSubjectQueryWrapper.ne("parent_id","0");
        List<EduSubject> twoEduSubjectList = baseMapper.selectList(twoSubjectQueryWrapper);

        //遍历所查出的一级分类
        OneSubject oneSubject = null;
        List<OneSubject> oneSubjectList = new ArrayList<>();
        for (int i=0; i < oneEduSubjectList.size();i++) {
            oneSubject = new OneSubject();
            EduSubject oneEduSubject = oneEduSubjectList.get(i);
            BeanUtils.copyProperties(oneEduSubject,oneSubject);
            oneSubjectList.add(oneSubject);
            //遍历查询出的二级分类
            TwoSubject twoSubject = null;
            List<TwoSubject> twoSubjectList = new ArrayList<>();
            for (int j=0; j < twoEduSubjectList.size(); j++) {
                EduSubject twoEduSubject = twoEduSubjectList.get(j);

                //将二级分类加入一级分类中
                if (twoEduSubject.getParentId().equals(oneEduSubject.getId())) {
                    twoSubject = new TwoSubject();
                    BeanUtils.copyProperties(twoEduSubject,twoSubject);
                    twoSubjectList.add(twoSubject);
                }
            }
            oneSubject.setChildren(twoSubjectList);
        }
        return oneSubjectList;
    }
}
