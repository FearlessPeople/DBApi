package com.dbapi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 角色表(SysRole)表实体类
 *
 * @author zfang
 * @time 2024-09-11 11:18:22
 */
@Data
public class SysRole extends Model<SysRole> {
    //角色id
    @TableId(type = IdType.AUTO)
    private Integer id;
    //角色名称
    private String name;
    //备注
    private String remark;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;

    // 前端传递过来的权限列表id
    @TableField(exist = false)
    private List<Integer> permissionIds;

}

