package com.zhou.eduservice.controller.front;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhou.commonutils.JwtUtils;
import com.zhou.commonutils.R;
import com.zhou.commonutils.frontVO.UcenterMemberVO;
import com.zhou.eduservice.client.UcenterMemberClient;
import com.zhou.eduservice.entity.EduComment;
import com.zhou.eduservice.service.EduCommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(description = "前端课程评论管理")
@RestController
@RequestMapping("/eduservice/commentFront")
@CrossOrigin
public class CommentFrontController {
    @Autowired
    private EduCommentService eduCommentService;
    @Autowired
    private UcenterMemberClient ucenterMemberClient;

    @ApiOperation("分页查询课程的评论")
    @GetMapping("/queryCommentForPageByCondition/{current}/{limit}/{courseId}")
    public R queryCommentForPageByCondition(@ApiParam(name = "current",value = "当前页",required = true) @PathVariable long current,
                                            @ApiParam(name = "limit",value = "每页记录数",required = true) @PathVariable long limit,
                                            @ApiParam(name = "courseId",value = "courseId",required = true) @PathVariable String courseId){
        Page<EduComment> eduCommentPage = new Page<>(current,limit);

        QueryWrapper<EduComment> eduCommentQueryWrapper = new QueryWrapper<>();
        eduCommentQueryWrapper.eq("course_id",courseId);
        //封装page对象
        eduCommentService.page(eduCommentPage,eduCommentQueryWrapper);
        //获取参数
        List<EduComment> eduCommentList = eduCommentPage.getRecords();
        //总页数
        long pages = eduCommentPage.getPages();
        //每页记录数
        long size = eduCommentPage.getSize();
        //当前页
        long currentPage = eduCommentPage.getCurrent();
        //总记录数
        long total = eduCommentPage.getTotal();
        boolean hasPrevious = eduCommentPage.hasPrevious();
        boolean hasNext = eduCommentPage.hasNext();
        //封装参数
        Map<String,Object> retMap = new HashMap<>();
        retMap.put("eduCommentList",eduCommentList);
        retMap.put("pages",pages);
        retMap.put("size",size);
        retMap.put("current",currentPage);
        retMap.put("total",total);
        retMap.put("hasPrevious",hasPrevious);
        retMap.put("hasNext",hasNext);
        //根据响应,返回参数
        return R.ok().data(retMap);
    }

    @ApiOperation("添加评论")
    @PostMapping("/saveComment")
    public R saveComment(@ApiParam(name = "eduComment",value = "添加的评论实体",required = true) @RequestBody EduComment eduComment,
                         @ApiParam(name = "request",value = "请求实体") HttpServletRequest request) {
        //通过jwt工具类获取用户id
        String ucenterMemberId = JwtUtils.getMemberIdByJwtToken(request);
        if (StringUtils.isEmpty(ucenterMemberId)){
            return R.error().code(28004).message("未登录无法评论,请登录!");
        }

        UcenterMemberVO ucenterMemberVO = ucenterMemberClient.getMemberInfoById(ucenterMemberId);
        //封装参数
        eduComment.setNickname(ucenterMemberVO.getNickname());
        eduComment.setAvatar(ucenterMemberVO.getAvatar());
        eduComment.setMemberId(ucenterMemberId);
        //调用service方法,保存评论
        boolean ret = eduCommentService.save(eduComment);
        if (ret){
            return R.ok();
        } else {
            return R.error().message("添加评论失败!");
        }
    }

}
