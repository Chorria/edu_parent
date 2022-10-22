package com.zhou.ucenterservice.mapper;

import com.zhou.ucenterservice.entity.UcenterMember;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 会员表 Mapper 接口
 * </p>
 *
 * @author Zhou
 * @since 2022-07-21
 */
public interface UcenterMemberMapper extends BaseMapper<UcenterMember> {
    Integer selectRegisterNumber(String date);
}
