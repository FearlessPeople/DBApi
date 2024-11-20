package com.dbapi.service;

import com.dbapi.common.DatabaseType;
import com.dbapi.service.impl.MySQLDataSourceProvider;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * 数据源工厂
 *
 */
@Component
public class DataSourceProviderFactory {

    public static DataSourceProvider createProvider(int dbType, DataSource dataSource) {
        switch (dbType) {
            case DatabaseType.DB_MYSQL:
                return new MySQLDataSourceProvider(dataSource);
            // case DatabaseType.DB_POSTGRE:
            //     return new PostgreSQLDataSourceProvider(dataSource);
            // case DatabaseType.DB_HIVE:
            //     return new HiveDataSourceProvider(dataSource);
            // case DatabaseType.DB_PRESTO:
            //     return new PrestoDataSourceProvider(dataSource);
            default:
                throw new IllegalArgumentException("Unsupported database type: " + dbType);
        }
    }
}
