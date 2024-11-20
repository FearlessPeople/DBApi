package com.dbapi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.util.Date;

/**
 * 接口SQL(ApiSql)表实体类
 *
 * @author zfang
 * @time 2024-10-24 14:29:56
 */
@Data
@TableName("api_sql")
public class ApiSql extends Model<ApiSql> {
    //自增ID
    @TableId(type = IdType.AUTO)
    private Integer id;
    //接口ID
    private Integer apiId;
    //接口SQL
    private String apiSql;
    //数据源ID
    private Integer datasourceId;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;

}

