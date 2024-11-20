package com.dbapi.service.impl;

import com.dbapi.service.DataSourceProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MySQL 数据库数据源提供者
 */
@Service
@Qualifier("mySQLDataSourceProvider")
public class MySQLDataSourceProvider implements DataSourceProvider {

    private final DataSource dataSource;

    public MySQLDataSourceProvider(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    @Override
    public Map<String, Object> executeQuery(String sqlQuery) throws SQLException {
        Map<String, Object> resultMap = new HashMap<>();
        List<Map<String, Object>> resultList = new ArrayList<>();
        List<String> columns = new ArrayList<>();
        int totalCount = 0;
        long executionTime = 0;

        // 记录查询开始时间
        long startTime = System.currentTimeMillis();

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sqlQuery)) {

            // 获取列信息
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            // 获取列名
            for (int i = 1; i <= columnCount; i++) {
                columns.add(metaData.getColumnName(i));
            }

            // 获取查询结果并计算行数
            while (resultSet.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(metaData.getColumnName(i), resultSet.getObject(i));
                }
                resultList.add(row);
            }

            // 获取总记录数（当前查询结果的行数）
            totalCount = resultList.size();
        }

        // 记录查询结束时间并计算耗时
        long endTime = System.currentTimeMillis();
        executionTime = endTime - startTime;

        // 将结果放入返回的 Map 中
        resultMap.put("columns", columns);
        resultMap.put("data", resultList);
        resultMap.put("count", totalCount);
        resultMap.put("executionTime", executionTime + " ms");

        return resultMap;
    }


    @Override
    public List<String> getTables() {
        List<String> tables = new ArrayList<>();
        String sql = "SHOW TABLES";
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                tables.add(resultSet.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tables;
    }

    @Override
    public List<Map<String, Object>> getColumns(String tableName) {
        List<Map<String, Object>> columns = new ArrayList<>();
        String sql = "SHOW COLUMNS FROM " + tableName;
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Map<String, Object> column = new HashMap<>();
                column.put("Field", resultSet.getString("Field"));
                column.put("Type", resultSet.getString("Type"));
                columns.add(column);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return columns;
    }
}
