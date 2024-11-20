package com.dbapi.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import java.math.BigDecimal;

/**
 * 接口SQL参数(ApiSqlParam)表实体类
 *
 * @author zfang
 * @time 2024-11-12 15:26:36
 */
@Data
@TableName("api_sql_param")
public class ApiSqlParam extends Model<ApiSqlParam> {
    //自增ID
    @TableId(type = IdType.AUTO)
    private Integer id;
    //接口ID
    private Integer apiId;
    //参数名称
    private String paramName;
    //接口类型 1字符串 2数值 3日期 4SQL表达式
    private Integer paramType;
    //接口SQL描述
    private String paramValue;
    //是否必填
    private Integer isRequired;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;

}

