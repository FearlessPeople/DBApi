package com.dbapi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 接口请求日志表(ApiRequestLog)表实体类
 *
 * @author zfang
 * @time 2024-11-18 16:37:52
 */
@Data
@TableName("api_request_log")
public class ApiRequestLog extends Model<ApiRequestLog> {
    //主键ID
    @TableId(type = IdType.AUTO)
    private Long id;
    //关联的API ID
    private Integer apiId;
    //API名称
    private String apiName;
    //API路径
    private String apiPath;
    //请求参数
    private String requestParams;
    //响应数据
    private String responseData;
    //响应状态码
    private Integer responseStatus;
    //响应消息
    private String responseMessage;
    //请求耗时（毫秒）
    private Long requestDuration;
    //客户端IP
    private String clientIp;
    //创建时间
    private Date createdTime;
    // 前端传递的时间范围
    @TableField(exist = false)
    private List<String> createdTimeRange;

}

