package com.dbapi.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.util.Date;

/**
 * 角色权限关系表(SysRolePermissions)表实体类
 *
 * @author zfang
 * @time 2024-09-11 11:18:22
 */
@Data
public class SysRolePermissions extends Model<SysRolePermissions> {
    //角色ID
    private Integer roleId;
    //权限ID
    private Integer permissionId;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;

}

