package com.dbapi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.util.Date;

/**
 * (ApiList)表实体类
 *
 * @author zfang
 * @time 2024-10-24 14:29:42
 */
@Data
@TableName("api_list")
public class ApiList extends Model<ApiList> {
    //接口ID
    @TableId(type = IdType.AUTO)
    private Integer id;
    //接口名称
    private String apiName;
    //接口路径
    private String apiPath;
    //接口描述
    private String apiDesc;
    //接口分组
    private Integer apiGroup;
    //发布时间
    private Date publishTime;
    //接口状态 0发布 1未发布
    private Integer status;
    //创建人
    private Integer createBy;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;

}

