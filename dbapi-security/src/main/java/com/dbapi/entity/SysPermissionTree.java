package com.dbapi.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 权限表(SysPermissions)表实体类
 *
 * @author zfang
 * @time 2024-09-11 11:18:22
 */
@Data
public class SysPermissionTree extends Model<SysPermissionTree> {
    //权限唯一id
    private Integer key;
    //权限名称
    private String title;
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
    // 子节点
    private List<SysPermissionTree> children;

}

