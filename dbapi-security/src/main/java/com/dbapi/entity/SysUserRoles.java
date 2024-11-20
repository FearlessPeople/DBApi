package com.dbapi.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.util.Date;

/**
 * 用户角色关系表(SysUserRoles)表实体类
 *
 * @author zfang
 * @time 2024-09-11 11:18:22
 */
@Data
public class SysUserRoles extends Model<SysUserRoles> {
    //用户ID
    private Integer userId;
    //角色ID
    private Integer roleId;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;

}

