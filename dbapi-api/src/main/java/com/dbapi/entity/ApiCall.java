package com.dbapi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 接口调用包装类
 *
 * @author zfang
 * @time 2024-10-24 14:29:42
 */
@Data
public class ApiCall  {
    //接口路径
    private String apiPath;
    //接口参数
    private List<ApiSqlParam> apiSqlParams;

}

