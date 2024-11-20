package com.dbapi.entity;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * 接口分组表(ApiGroup)表实体类
 *
 * @author zfang
 * @time 2024-10-24 14:28:36
 */
@Data
@TableName("api_group")
public class ApiGroup extends Model<ApiGroup> {
    //自增ID
    @TableId(type = IdType.AUTO)
    private Integer id;
    //分组名称
    private String groupName;
    //接口列表
    @TableField(exist = false)
    private List<ApiList> apiList;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;

}

