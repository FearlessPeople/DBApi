package com.dbapi.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 数据源服务接口
 */
public interface DataSourceProvider {

    /**
     * 执行查询并返回结果
     * @param sqlQuery
     * @return
     */
    Map<String, Object> executeQuery(String sqlQuery)  throws SQLException;

    /**
     * 获取所有表名
     * @return
     */
    List<String> getTables();

    /**
     * 获取指定表的列信息
     * @param tableName
     * @return
     */
    List<Map<String, Object>> getColumns(String tableName);
}
