package com.dbapi.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dbapi.common.Result;
import com.dbapi.annotation.Permissions;
import com.dbapi.annotation.RequireLogin;
import com.dbapi.entity.SysRole;
import com.dbapi.entity.SysUser;
import com.dbapi.manager.PermissionsManager;
import com.dbapi.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
 * (SysUser)表控制层
 *
 * @author zfang
 * @time 2024-05-22 15:15:38
 */
@RestController
@RequestMapping("user")
public class SysUserController {

    @Resource
    private SysUserService sysUserService;


    @Autowired
    private PermissionsManager permissionsManager;

    /**
     * 注册
     *
     * @return
     */
    @PostMapping("/register")
    public Result register(@RequestBody SysUser user) {
        return sysUserService.register(user);
    }

    /**
     * 新建用户
     *
     * @return
     */
    @PostMapping("/create")
    @Permissions("sys:user:add")
    public Result create(@RequestBody SysUser user) {
        user.setId(null);
        return sysUserService.register(user);
    }

    /**
     * 登录
     *
     * @return
     */
    @PostMapping("/login")
    public Result login(@RequestBody SysUser user) {
        return sysUserService.login(user);
    }

    /**
     * 用户列表
     *
     * @return
     */
    @PostMapping("/list")
    @Permissions("sys:user:list")
    public Result list(Page<SysUser> page, SysUser user) {
        Page<SysUser> sysUserPage = sysUserService.selectPage(page, user);
        return Result.success(sysUserPage);
    }

    /**
     * 删除用户
     *
     * @return
     */
    @GetMapping("/delete")
    @Permissions("sys:user:delete")
    public Result delete(Integer id) {
        return sysUserService.delete(id);
    }

    /**
     * 更新用户
     *
     * @return
     */
    @PostMapping("/update")
    @RequireLogin
    public Result update(@RequestBody SysUser user) {
        return sysUserService.update(user);
    }

    /**
     * 更新用户状态
     *
     * @return
     */
    @GetMapping("/switchStatus")
    @RequireLogin
    public Result switchStatus(Integer id) {
        return sysUserService.switchStatus(id);
    }

    /**
     * 查询当前用户
     *
     * @return
     */
    @RequireLogin
    @PostMapping("/info")
    public Result info() {
        return sysUserService.info();
    }

    /**
     * 查询当前用户所拥有角色列表
     *
     * @return
     */
    @GetMapping("/roles")
    @Permissions("sys:user:roles")
    public Result roles(Integer id) {
        List<SysRole> roles = permissionsManager.getCurrentUserRoles(id);
        return Result.success(roles);
    }

    /**
     * 更新用户角色
     *
     * @return
     */
    @PostMapping("/update-roles")
    @Permissions("sys:user:roles")
    public Result updateRoles(@RequestBody SysUser sysUser) {
        boolean statu = permissionsManager.setUserRoles(sysUser);
        if (statu) {
            return Result.success("角色分配成功~");
        } else {
            return Result.fail("角色分配失败~");
        }
    }

    /**
     * 上传头像
     *
     * @param file 文件
     * @return
     */
    @PostMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile file) {
        return sysUserService.upload(file);
    }

    /**
     * 检查token
     *
     * @return
     */
    @RequireLogin
    @PostMapping("/checkToken")
    public Result checkToken(String token) {
        return sysUserService.checkToken(token);
    }

    /**
     * 退出登录
     *
     * @return
     */
    @PostMapping("/logout")
    public Result logout() {
        // 调用服务层方法处理登出逻辑
        return sysUserService.logout();
    }
}

