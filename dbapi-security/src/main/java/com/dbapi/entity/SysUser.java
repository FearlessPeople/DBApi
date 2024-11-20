package com.dbapi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * (SysUser)表实体类
 *
 * @author zfang
 * @time 2024-09-10 10:41:36
 */
@Data
public class SysUser extends Model<SysUser> {
    //用户ID
    @TableId(type = IdType.AUTO)
    private Integer id;
    //昵称
    private String nickname;
    //用户名
    private String username;
    //密码
    private String password;
    //头像
    private String avatar;
    //邮箱
    private String email;
    //电话号码
    private String phoneNumber;
    //备注
    private String remark;
    //个人简介
    private String introduction;
    //用户状态 0正常 1禁用
    private Integer status;
    //是否管理员 0是 1否
    private Integer isAdmin;
    //是否删除 0是 1否
    private Integer isDelete;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    // 用户角色id列表
    @TableField(exist = false)
    private List<Integer> roleIds;
    // 用户权限列表
    @TableField(exist = false)
    private List<String> permissions;
    // 用户登录后的token
    @TableField(exist = false)
    private String token;
    // 用户验证码的codeId唯一值
    @TableField(exist = false)
    private String codeId;
    // 用户登录时输入的验证码
    @TableField(exist = false)
    private String checkCode;
}

