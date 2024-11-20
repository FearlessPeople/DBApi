package com.dbapi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.util.Date;

/**
 * 权限表(SysPermissions)表实体类
 *
 * @author zfang
 * @time 2024-09-11 11:18:22
 */
@Data
public class SysPermissions extends Model<SysPermissions> {
    //角色id
    @TableId(type = IdType.AUTO)
    private Integer id;
    //权限名称
    private String name;
    //权限路径表达式
    private String expression;
    //备注
    private String remark;
    //状态 0正常 1禁用
    private Integer status;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;

}

