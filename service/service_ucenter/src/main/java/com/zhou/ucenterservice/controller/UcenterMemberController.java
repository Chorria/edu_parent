package com.zhou.ucenterservice.controller;


import com.zhou.commonutils.R;
import com.zhou.commonutils.frontVO.UcenterMemberVO;
import com.zhou.ucenterservice.entity.UcenterMember;
import com.zhou.ucenterservice.entity.vo.LoginVO;
import com.zhou.ucenterservice.entity.vo.RegisterVO;
import com.zhou.ucenterservice.service.UcenterMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author Zhou
 * @since 2022-07-21
 */
@Api(description = "用户登录注册管理")
@RestController
@RequestMapping("/eduucenter/member")
@CrossOrigin
public class UcenterMemberController {
    @Autowired
    private UcenterMemberService ucenterMemberService;

    @ApiOperation(value = "用户注册")
    @PostMapping("/register")
    public R registerUser(@RequestBody RegisterVO registerVO){
        ucenterMemberService.registerUser(registerVO);
        return R.ok();
    }

    @ApiOperation(value = "用户登录")
    @PostMapping("/login")
    public R login(@RequestBody LoginVO loginVO){
        //member对象封装手机号和密码
        //调用service方法实现登录
        //返回token值，使用jwt生成
        String token = ucenterMemberService.login(loginVO);
        return R.ok().data("token",token);
    }

    @ApiOperation(value = "根据token获取登录信息")
    @GetMapping("/getMemberInfo")
    public R getMemberInfo(HttpServletRequest request){
        UcenterMember ucenterMember = ucenterMemberService.getMemberInfo(request);
        return R.ok().data("userInfo",ucenterMember);
    }

    @ApiOperation(value = "根据用户id获取用户信息")
    @GetMapping("/getMemberInfoById/{id}")
    public UcenterMemberVO getMemberInfoById(@ApiParam(name = "id",value = "用户id",required = true) @PathVariable String id){
        UcenterMember ucenterMember = ucenterMemberService.getById(id);
        UcenterMemberVO ucenterMemberVO = new UcenterMemberVO();
        BeanUtils.copyProperties(ucenterMember,ucenterMemberVO);
        return ucenterMemberVO;
    }

    @ApiOperation(value = "获取生成订单所需的用户信息")
    @GetMapping("/getMemberInfoForOrder/{id}")
    public UcenterMemberVO getMemberInfoForOrder(@ApiParam(name = "id",value = "用户id",required = true) @PathVariable String id){
        UcenterMember ucenterMember = ucenterMemberService.getById(id);
        UcenterMemberVO ucenterMemberVO = new UcenterMemberVO();
        BeanUtils.copyProperties(ucenterMember,ucenterMemberVO);
        return ucenterMemberVO;
    }

    @ApiOperation(value = "获取当日注册用户人数")
    @GetMapping("/getRegisterNumber/{date}")
    public R getRegisterNumber(@ApiParam(name = "date",value = "日期参数",required = true)@PathVariable String date){
        Integer ret = ucenterMemberService.getRegisterNumber(date);
        return R.ok().data("registerNumber",ret);
    }



}

