package com.dbapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SqlExecutor {

    @Autowired
    private DataSourceManager dataSourceManager;

    public List<Map<String, Object>> executeQuery(Integer id, String sql) throws SQLException {
        DataSource dataSource = dataSourceManager.getDataSource(id);
        if (dataSource == null) {
            throw new IllegalArgumentException("DataSource not found: " + id);
        }

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            // 获取列信息
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            List<Map<String, Object>> results = new ArrayList<>();

            // 遍历结果集
            while (resultSet.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    // 获取列名并将其转换为键
                    String columnName = metaData.getColumnLabel(i);
                    Object columnValue = resultSet.getObject(i);
                    row.put(columnName, columnValue);
                }
                results.add(row);
            }

            return results;
        }
    }
}
