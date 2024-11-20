package com.dbapi.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dbapi.common.Result;
import com.dbapi.annotation.Permissions;
import com.dbapi.entity.SysPermissionTree;
import com.dbapi.entity.SysPermissions;
import com.dbapi.entity.SysRole;
import com.dbapi.manager.PermissionsManager;
import com.dbapi.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 角色表(SysRole)表控制层
 *
 * @author zfang
 * @time 2024-09-13 16:48:21
 */
@RestController
@RequestMapping("role")
public class SysRoleController {
    /**
     * 服务对象
     */
    @Resource
    private SysRoleService sysRoleService;

    @Autowired
    private PermissionsManager permissionsManager;


    /**
     * 角色列表
     *
     * @return
     */
    @PostMapping("/all")
    @Permissions("sys:role:list")
    public Result all() {
        List<SysRole> list = sysRoleService.list();
        return Result.success(list);
    }


    /**
     * 角色列表
     *
     * @return
     */
    @PostMapping("/list")
    @Permissions("sys:role:list")
    public Result list(Page<SysRole> page, SysRole role) {
        Page<SysRole> sysRolePage = sysRoleService.selectPage(page, role);
        return Result.success(sysRolePage);
    }

    /**
     * 新增角色
     *
     * @return
     */
    @PostMapping("/add")
    @Permissions("sys:role:add")
    public Result add(@RequestBody SysRole role) {
        boolean saveStatus = this.sysRoleService.save(role);
        if (saveStatus) {
            return Result.success("角色新增成功");
        } else {
            return Result.fail("角色新增失败");
        }
    }

    /**
     * 修改
     *
     * @return
     */
    @PostMapping("/edit")
    @Permissions("sys:role:edit")
    public Result edit(@RequestBody SysRole role) {
        boolean saveStatus = this.sysRoleService.updateById(role);
        if (saveStatus) {
            return Result.success("角色修改成功");
        } else {
            return Result.fail("角色修改失败");
        }
    }

    /**
     * 删除
     *
     * @return
     */
    @PostMapping("/delete")
    @Permissions("sys:role:delete")
    public Result delete(@RequestBody SysRole role) {
        boolean saveStatus = this.sysRoleService.removeById(role);
        if (saveStatus) {
            return Result.success("角色删除成功");
        } else {
            return Result.fail("角色删除失败");
        }
    }

    /**
     * 给角色分配权限
     *
     * @return
     */
    @PostMapping("/set-permissions")
    @Permissions("sys:role:permission")
    public Result setPermissions(@RequestBody SysRole role) {
        boolean status = this.permissionsManager.setRolePermissions(role);
        if (status) {
            return Result.success("权限设置成功");
        } else {
            return Result.fail("权限设置失败");
        }
    }


    /**
     * 获取角色对应的所有权限列表
     *
     * @return
     */
    @PostMapping("/get-permissions-list")
    @Permissions("sys:role:permission")
    public Result getPermissionsList(@RequestBody SysRole role) {
        List<SysPermissions> permissions = this.permissionsManager.getPermissionsByRoleId(role.getId());
        return Result.success("成功", permissions);
    }

    /**
     * 获取角色对应的所有权限列表树形结构
     *
     * @return
     */
    @PostMapping("/get-permissions-tree")
    @Permissions("sys:role:permission")
    public Result getPermissionsTree(@RequestBody SysRole role) {
        SysPermissionTree allPermissionsTree = this.permissionsManager.getAllPermissionsTree(role);
        return Result.success(allPermissionsTree);
    }

    /**
     * 获取系统中所有权限列表
     *
     * @return
     */
    @GetMapping("/all-permissions")
    @Permissions("sys:role:permission")
    public Result allPermissions() {
        SysPermissionTree allPermissionsTree = this.permissionsManager.getAllPermissionsTree();
        return Result.success(allPermissionsTree);
    }

}

