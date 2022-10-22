package com.zhou.eduservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhou.commonutils.R;
import com.zhou.eduservice.entity.EduTeacher;
import com.zhou.eduservice.entity.vo.TeacherQuery;
import com.zhou.eduservice.service.EduTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author Zhou
 * @since 2022-03-10
 */
@Api(description = "讲师管理")
@RestController
@RequestMapping("/eduservice/teacher")
@CrossOrigin
public class EduTeacherController {
    //注入EduTeacherService
    @Autowired
    private EduTeacherService eduTeacherService;

    /**
     * 查询讲师表中的所有数据
     * @return  回传所查询的讲师信息
     */
    @ApiOperation(value = "所有讲师列表")
    @GetMapping("/queryAll")
    public R queryAll(){
        //调用Service的方法实现查询所有的操作
        List<EduTeacher> teacherList = eduTeacherService.list(null);
        return R.ok().data("items",teacherList);
    }

    /**
     * 逻辑删除讲师的方法
     * @param id 讲师的Id
     * @return 回传是否成功的信息
     */
    @ApiOperation(value = "根据Id删除讲师")
    @DeleteMapping("/{id}")
    public R removeById(@ApiParam(name = "id",value = "讲师Id",required = true)
            @PathVariable("id") String id){
        boolean flag = eduTeacherService.removeById(id);
        //判断是否删除成功
        if(flag){
            return R.ok();
        } else {
            return R.error();
        }
    }

    /**
     * 分页查询讲师的方法
     * @param current 当前页
     * @param limit 每页记录数
     * @return  回传总记录数和查询的所有讲师信息
     */
    @ApiOperation(value = "分页查询讲师")
    @GetMapping("/pageTeacher/{current}/{limit}")
    public R pageListTeacher(@ApiParam(name = "current",value = "当前页",required = true)@PathVariable long current,
                             @ApiParam(name = "limit",value = "每页数据的条数",required = true)@PathVariable long limit){

        //创建Page对象
        Page<EduTeacher> teacherPage = new Page<>(current,limit);
        //调用方法显示分页
        //调用方法时,底层封装,把分页所有数据封装到teacherPage对象里面
        eduTeacherService.page(teacherPage, null);
        long total = teacherPage.getTotal();    //总记录数
        List<EduTeacher> records = teacherPage.getRecords();    //数据List集合

        return R.ok().data("total",total).data("records",records);

        /**
         * 返回方式二
         * Map<String,Object> map = new HashMap<>();
         * map.put("total",total);
         * map.put("records",records);
         * return R.ok().data(map);
         */
    }

    /**
     * 条件查询分页的方法
     */
    @ApiOperation(value = "条件查询讲师并分页")
    @PostMapping("/pageTeacherCondition/{current}/{limit}")
    public R pageTeacherCondition(@ApiParam(name = "current",value = "当前页",required = true)@PathVariable long current,
                                  @ApiParam(name = "limit",value = "每页数据条数",required = true)@PathVariable long limit,
                                  @ApiParam(name = "teacherQuery",value = "讲师查询条件的实体类",required = false)@RequestBody(required = false) TeacherQuery teacherQuery){
        //构建条件
        QueryWrapper<EduTeacher> teacherQueryWrapper = new QueryWrapper<>();
        //获取讲师的查询条件
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();
        //判断条件值是否为空,如果不为空,则拼接条件.形同于MyBatis中的动态Sql
        if(!StringUtils.isEmpty(name)){
            teacherQueryWrapper.like("name",name);
        }
        if (!StringUtils.isEmpty(level)){
            teacherQueryWrapper.eq("level",level);
        }
        if (!StringUtils.isEmpty(begin)){
            teacherQueryWrapper.ge("gmt_create",begin);
        }
        if (!StringUtils.isEmpty(end)){
            teacherQueryWrapper.le("gmt_create",end);
        }
        //降序排列
        teacherQueryWrapper.orderByDesc("gmt_create");
        //创建Page对象
        Page<EduTeacher> teacherPage = new Page<>(current,limit);
        //调用方法实现条件分页查询
        eduTeacherService.page(teacherPage, teacherQueryWrapper);
        //记录总记录数及查询的List集合
        long total = teacherPage.getTotal();
        List<EduTeacher> records = teacherPage.getRecords();

        return R.ok().data("total",total).data("records",records);
    }

    /**
     * 添加讲师接口的方法
     */
    @ApiOperation("添加讲师接口")
    @PostMapping("/addTeacher")
    public R addTeacher(@ApiParam(name = "eduTeacher",value = "添加的讲师信息",required = true)@RequestBody EduTeacher eduTeacher){
        boolean flag = eduTeacherService.save(eduTeacher);
        if(flag){
            return R.ok();
        } else {
            return R.error();
        }
    }

    @ApiOperation("根据讲师Id进行查询")
    @GetMapping("/getTeacher/{id}")
    public R getTeacher(@ApiParam(name = "id",value = "讲师Id",required = true)@PathVariable String id){
        EduTeacher teacher = eduTeacherService.getById(id);
        return R.ok().data("teacher",teacher);
    }

    @ApiOperation("修改讲师信息接口")
    @PostMapping("/updateTeacher")
    public R updateTeacher(@ApiParam(name = "teacher",value = "被修改的讲师的信息",required = true)@RequestBody EduTeacher eduTeacher){
        boolean flag = eduTeacherService.updateById(eduTeacher);
        if (flag){
            return R.ok();
        } else {
            return R.error();
        }
    }
}

