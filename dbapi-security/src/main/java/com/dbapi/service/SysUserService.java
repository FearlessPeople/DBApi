package com.dbapi.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dbapi.common.Result;
import com.dbapi.entity.SysUser;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

/**
 * (SysUser)表服务接口
 *
 * @author zfang
 * @time 2024-05-22 15:15:17
 */
public interface SysUserService extends IService<SysUser> {
    /**
     * 注册
     *
     * @return
     */
    @PostMapping("/register")
    Result register(SysUser user);

    /**
     * 登录
     *
     * @return
     */
    @PostMapping("/login")
    Result login(SysUser user);

    /**
     * 查询当前用户
     *
     * @return
     */
    Result info();

    /**
     * 上传头像
     *
     * @param file
     * @return
     */
    Result upload(MultipartFile file);

    /**
     * 查询用户
     *
     * @return
     */
    Result findAll();

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    Result delete(Integer id);

    /**
     * 更新用户
     *
     * @param user
     * @return
     */
    Result update(SysUser user);

    /**
     * 切换用户状态
     *
     * @param id
     * @return
     */
    Result switchStatus(Integer id);

    /**
     * 分页查询
     *
     * @return
     */
    Page<SysUser> selectPage(Page<SysUser> page, SysUser user);

    /**
     * 检查token
     *
     * @param token
     * @return
     */
    Result checkToken(String token);

    /***
     * 退出登录
     * @return
     */
    Result logout();
}

