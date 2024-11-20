package com.dbapi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor  // 自动生成带参数的构造方法
public class LineChartRecord {
    private String x;  // 横坐标的值（如日期或其他）
    private int y;     // 纵坐标的值
}
