package com.dbapi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.util.Date;

/**
 * (SysDatasource)表实体类
 *
 * @author zfang
 * @time 2024-10-12 14:35:14
 */
@Data
@TableName("sys_datasource")
public class SysDatasource extends Model<SysDatasource> {

    @TableId(type = IdType.AUTO)
    private Integer id;
    //数据源key
    private String name;
    //数据源jdbc url
    private String jdbcUrl;
    //用户名
    private String username;
    //密码
    private String password;
    //备注
    private String remark;
    //数据库类型 1:MySQL 2:PostgreSQL 3:Doris
    private Integer dbType;
    //状态 0正常 1禁用
    private Integer status;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;

}

