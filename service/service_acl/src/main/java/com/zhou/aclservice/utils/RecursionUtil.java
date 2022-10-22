package com.zhou.aclservice.utils;

import com.zhou.aclservice.entity.Permission;

import java.util.ArrayList;
import java.util.List;

public class RecursionUtil {
    //获取所有的菜单
    public static List<Permission> queryAllMenu(List<Permission> permissionList) {
        //封装参数
        List<Permission> retList = new ArrayList<>();
        for (Permission permission : permissionList) {
            if ("0".equals(permission.getPid())) {
                permission.setLevel(1);
                retList.add(selectChildren(permission,permissionList));
            }
        }

        return retList;
    }

    //获取当前菜单的子菜单
    public static Permission selectChildren(Permission permission,List<Permission> permissionList) {
        //初始化
        permission.setChildren(new ArrayList<>());

        for (Permission permissionNode : permissionList) {
            if (permission.getId().equals(permissionNode.getPid())) {
                permissionNode.setLevel(permission.getLevel() + 1);
                permission.getChildren().add(selectChildren(permissionNode,permissionList));
            }
        }
        return permission;
    }
}
