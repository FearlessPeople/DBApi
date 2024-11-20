package com.dbapi.constants;

/**
 * 数据库字段常量
 */
public class DbConstants {

    // 用户状态
    public static class UserStatus {
        public static final int NORMAL = 0; // 正常
        public static final int DISABLED = 1; // 禁用
    }

    // 是否管理员
    public static class IsAdmin {
        public static final int YES = 0; // 是
        public static final int NO = 1; // 否
    }

    // 是否删除
    public static class IsDeleted {
        public static final int YES = 0; // 是
        public static final int NO = 1; // 否
    }
}
