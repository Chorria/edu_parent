package com.zhou.eduservice.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhou.eduservice.entity.EduSubject;
import com.zhou.eduservice.entity.excel.SubjectData;
import com.zhou.eduservice.service.EduSubjectService;
import com.zhou.servicebase.exceptionhandler.ZhouException;

public class SubjectExcelListener extends AnalysisEventListener<SubjectData> {
    //因为SubjectExcelListener不能交给Spring管理，需要自己new，不能注入其他对象
    //不能实现数据库操作
    public EduSubjectService eduSubjectService;

    public SubjectExcelListener(){

    }

    public SubjectExcelListener(EduSubjectService eduSubjectService) {
        this.eduSubjectService = eduSubjectService;
    }

    //读取excel内容，一行一行进行读取
    @Override
    public void invoke(SubjectData subjectData, AnalysisContext analysisContext) {
        if (subjectData == null) {
            throw new ZhouException(20001,"文件数据为空!");
        }
        //一行一行读取,每次读取两个值,第一个值为一级分类,第二个值为二级分类
        //判断一级分类是否重复
        EduSubject oneEduSubject = this.existOneSubject(eduSubjectService, subjectData.getOneSubjectName());
        if (oneEduSubject == null) {
            oneEduSubject = new EduSubject();
            oneEduSubject.setTitle(subjectData.getOneSubjectName());
            oneEduSubject.setParentId("0");
            eduSubjectService.save(oneEduSubject);
        }
        //添加二级分类
        //判断二级分类是否重复
        EduSubject twoEduSubject = this.existTwoSubject(eduSubjectService, subjectData.getTwoSubjectName(), oneEduSubject.getId());
        if (twoEduSubject == null) {
            twoEduSubject = new EduSubject();
            twoEduSubject.setTitle(subjectData.getTwoSubjectName());
            twoEduSubject.setParentId(oneEduSubject.getId());
            eduSubjectService.save(twoEduSubject);
        }

    }

    //判断一级分类不能重复添加
    public EduSubject existOneSubject(EduSubjectService subjectService,String title){
        QueryWrapper<EduSubject> eduSubjectQueryWrapper = new QueryWrapper<>();
        eduSubjectQueryWrapper.eq("title",title);
        eduSubjectQueryWrapper.eq("parent_id","0");
        EduSubject eduSubject = subjectService.getOne(eduSubjectQueryWrapper);
        return eduSubject;
    }

    //判断二级分类不能重复添加
    public EduSubject existTwoSubject(EduSubjectService eduSubjectService,String title,String parentId){
        QueryWrapper<EduSubject> eduSubjectQueryWrapper = new QueryWrapper<>();
        eduSubjectQueryWrapper.eq("title",title);
        eduSubjectQueryWrapper.eq("parent_id",parentId);
        EduSubject eduSubject = eduSubjectService.getOne(eduSubjectQueryWrapper);
        return eduSubject;
    }

    //读取excel内容后所执行的方法
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
