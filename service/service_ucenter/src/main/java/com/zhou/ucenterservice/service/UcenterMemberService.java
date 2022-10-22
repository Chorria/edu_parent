package com.zhou.ucenterservice.service;

import com.zhou.ucenterservice.entity.UcenterMember;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhou.ucenterservice.entity.vo.LoginVO;
import com.zhou.ucenterservice.entity.vo.RegisterVO;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author Zhou
 * @since 2022-07-21
 */
public interface UcenterMemberService extends IService<UcenterMember> {
    //注册用户
    void registerUser(RegisterVO registerVO);
    //用户登录
    String login(LoginVO loginVO);
    //根据token获取用户信息
    UcenterMember getMemberInfo(HttpServletRequest request);
    //获取当日用户注册的人数
    Integer getRegisterNumber(String date);
}
