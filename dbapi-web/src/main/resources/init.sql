-- MySQL dump 10.13  Distrib 8.0.12, for macos10.13 (x86_64)
--
-- Host: 111.229.110.183    Database: dbapi
-- ------------------------------------------------------
-- Server version	5.6.51

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
SET NAMES utf8mb4 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `api_group`
--

DROP TABLE IF EXISTS `api_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
SET character_set_client = utf8mb4 ;
CREATE TABLE `api_group` (
                             `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
                             `group_name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '分组名称',
                             `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                             `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                             PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='接口分组表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `api_group`
--

LOCK TABLES `api_group` WRITE;
/*!40000 ALTER TABLE `api_group` DISABLE KEYS */;
INSERT INTO `api_group` VALUES (1,'默认分组','2024-10-24 15:24:40','2024-10-31 10:29:16');
/*!40000 ALTER TABLE `api_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `api_list`
--

DROP TABLE IF EXISTS `api_list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
SET character_set_client = utf8mb4 ;
CREATE TABLE `api_list` (
                            `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '接口ID',
                            `api_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '接口名称',
                            `api_path` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '接口路径',
                            `api_desc` text COLLATE utf8mb4_unicode_ci COMMENT '接口描述',
                            `api_group` int(11) NOT NULL COMMENT '接口分组',
                            `publish_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
                            `status` int(1) NOT NULL DEFAULT '0' COMMENT '接口状态 0发布 1未发布',
                            `create_by` int(1) NOT NULL DEFAULT '1' COMMENT '创建人',
                            `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                            PRIMARY KEY (`id`),
                            UNIQUE KEY `api_list_api_path_uindex` (`api_path`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `api_list`
--

LOCK TABLES `api_list` WRITE;
/*!40000 ALTER TABLE `api_list` DISABLE KEYS */;
INSERT INTO `api_list` VALUES (1,'test','area/all','接口描述信息',1,'2024-11-20 10:28:46',0,1,'2024-11-20 10:28:46','2024-11-20 11:06:38');
/*!40000 ALTER TABLE `api_list` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `api_request_log`
--

DROP TABLE IF EXISTS `api_request_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
SET character_set_client = utf8mb4 ;
CREATE TABLE `api_request_log` (
                                   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                   `api_id` bigint(20) DEFAULT NULL COMMENT '关联的API ID',
                                   `api_name` varchar(255) DEFAULT NULL COMMENT 'API名称',
                                   `api_path` varchar(255) NOT NULL COMMENT 'API路径',
                                   `request_params` text COMMENT '请求参数',
                                   `response_data` longtext COMMENT '响应数据',
                                   `response_status` int(11) NOT NULL COMMENT '响应状态码',
                                   `response_message` text COMMENT '响应消息',
                                   `request_duration` bigint(20) DEFAULT NULL COMMENT '请求耗时（毫秒）',
                                   `client_ip` varchar(45) DEFAULT NULL COMMENT '客户端IP',
                                   `created_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                   PRIMARY KEY (`id`),
                                   KEY `idx_created_time` (`created_time`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='接口请求日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `api_request_log`
--

LOCK TABLES `api_request_log` WRITE;
/*!40000 ALTER TABLE `api_request_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `api_request_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `api_sql`
--

DROP TABLE IF EXISTS `api_sql`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
SET character_set_client = utf8mb4 ;
CREATE TABLE `api_sql` (
                           `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
                           `api_id` int(10) unsigned NOT NULL COMMENT '接口ID',
                           `api_sql` longtext COLLATE utf8mb4_unicode_ci COMMENT '接口SQL',
                           `datasource_id` int(10) unsigned NOT NULL COMMENT '数据源ID',
                           `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                           `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                           PRIMARY KEY (`id`),
                           UNIQUE KEY `api_id` (`api_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='接口SQL';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `api_sql`
--

LOCK TABLES `api_sql` WRITE;
/*!40000 ALTER TABLE `api_sql` DISABLE KEYS */;
INSERT INTO `api_sql` VALUES (1,4,'SELECT * FROM table_name4',1,'2024-11-01 15:19:32','2024-11-14 16:56:52');
/*!40000 ALTER TABLE `api_sql` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `api_sql_param`
--

DROP TABLE IF EXISTS `api_sql_param`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
SET character_set_client = utf8mb4 ;
CREATE TABLE `api_sql_param` (
                                 `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
                                 `api_id` int(10) unsigned NOT NULL COMMENT '接口ID',
                                 `param_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '参数名称',
                                 `param_type` int(11) DEFAULT NULL COMMENT '接口类型 1字符串 2数值 3日期 4SQL表达式',
                                 `param_value` text COLLATE utf8mb4_unicode_ci COMMENT '接口SQL描述',
                                 `is_required` int(11) DEFAULT NULL COMMENT '是否必填 0是 1否',
                                 `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='接口SQL参数';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `api_sql_param`
--

LOCK TABLES `api_sql_param` WRITE;
/*!40000 ALTER TABLE `api_sql_param` DISABLE KEYS */;
/*!40000 ALTER TABLE `api_sql_param` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_datasource`
--

DROP TABLE IF EXISTS `sys_datasource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
SET character_set_client = utf8mb4 ;
CREATE TABLE `sys_datasource` (
                                  `id` int(11) NOT NULL AUTO_INCREMENT,
                                  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '数据源key',
                                  `jdbc_url` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '数据源jdbc url',
                                  `username` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
                                  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
                                  `remark` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
                                  `db_type` int(11) DEFAULT '1' COMMENT '数据库类型 1:MySQL 2:PostgreSQL 3:Doris',
                                  `status` int(1) NOT NULL DEFAULT '0' COMMENT '状态 0正常 1禁用',
                                  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                  PRIMARY KEY (`id`),
                                  UNIQUE KEY `sys_datasource_id_uindex` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_datasource`
--

LOCK TABLES `sys_datasource` WRITE;
/*!40000 ALTER TABLE `sys_datasource` DISABLE KEYS */;
INSERT INTO `sys_datasource` VALUES (19,'test','jdbc:mysql://localhost:3306/test?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai','test','H62573G@^&2k1','',1,0,'2024-11-20 10:45:02','2024-11-20 10:45:02');
/*!40000 ALTER TABLE `sys_datasource` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_permissions`
--

DROP TABLE IF EXISTS `sys_permissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
SET character_set_client = utf8mb4 ;
CREATE TABLE `sys_permissions` (
                                   `id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '角色id',
                                   `name` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '权限名称',
                                   `expression` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '权限路径表达式',
                                   `remark` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
                                   `status` int(1) NOT NULL DEFAULT '0' COMMENT '状态 0正常 1禁用',
                                   `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                   `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_permissions`
--

LOCK TABLES `sys_permissions` WRITE;
/*!40000 ALTER TABLE `sys_permissions` DISABLE KEYS */;
INSERT INTO `sys_permissions` VALUES (1,'用户列表','sys:user:list','用户列表',0,'2024-09-13 10:29:44','2024-09-13 10:29:44'),(2,'用户编辑','sys:user:edit','用户编辑',0,'2024-09-13 10:29:45','2024-09-13 10:29:45'),(3,'用户删除','sys:user:delete','用户删除',0,'2024-09-13 10:29:45','2024-09-13 10:29:45'),(4,'用户新建','sys:user:add','用户新建',0,'2024-09-13 10:29:45','2024-09-13 10:29:45'),(5,'用户角色管理','sys:user:roles','用户角色管理',0,'2024-09-23 14:50:12','2024-09-23 15:02:05'),(6,'角色列表','sys:role:list','角色列表',0,'2024-09-13 16:45:57','2024-09-23 15:02:09'),(7,'角色新建','sys:role:add','角色新建',0,'2024-09-13 17:37:36','2024-09-23 15:02:18'),(8,'角色编辑','sys:role:edit','角色编辑',0,'2024-09-13 17:49:40','2024-09-23 15:02:23'),(9,'角色删除','sys:role:delete','角色删除',0,'2024-09-13 17:49:40','2024-09-23 15:02:26'),(10,'角色权限管理','sys:role:permission','角色权限管理',0,'2024-09-23 14:53:03','2024-09-23 14:53:03');
/*!40000 ALTER TABLE `sys_permissions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role`
--

DROP TABLE IF EXISTS `sys_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
SET character_set_client = utf8mb4 ;
CREATE TABLE `sys_role` (
                            `id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '角色id',
                            `name` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '角色名称',
                            `remark` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
                            `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role`
--

LOCK TABLES `sys_role` WRITE;
/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;
INSERT INTO `sys_role` VALUES (1,'管理员',NULL,'2024-09-11 15:47:00','2024-09-11 15:47:00'),(2,'普通用户','','2024-09-11 15:47:16','2024-10-17 14:51:57');
/*!40000 ALTER TABLE `sys_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role_permissions`
--

DROP TABLE IF EXISTS `sys_role_permissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
SET character_set_client = utf8mb4 ;
CREATE TABLE `sys_role_permissions` (
                                        `role_id` int(100) NOT NULL COMMENT '角色ID',
                                        `permission_id` int(100) NOT NULL COMMENT '权限ID',
                                        `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                        `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role_permissions`
--

LOCK TABLES `sys_role_permissions` WRITE;
/*!40000 ALTER TABLE `sys_role_permissions` DISABLE KEYS */;
INSERT INTO `sys_role_permissions` VALUES (1,1,'2024-09-11 15:48:54','2024-09-11 15:48:54'),(3,1,'2024-10-17 11:06:41','2024-10-17 11:06:41'),(3,2,'2024-10-17 11:06:41','2024-10-17 11:06:41'),(3,3,'2024-10-17 11:06:41','2024-10-17 11:06:41'),(2,5,'2024-10-17 14:52:00','2024-10-17 14:52:00'),(2,6,'2024-10-17 14:52:00','2024-10-17 14:52:00'),(2,7,'2024-10-17 14:52:00','2024-10-17 14:52:00'),(2,8,'2024-10-17 14:52:00','2024-10-17 14:52:00');
/*!40000 ALTER TABLE `sys_role_permissions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
SET character_set_client = utf8mb4 ;
CREATE TABLE `sys_user` (
                            `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '用户ID',
                            `nickname` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '昵称',
                            `username` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
                            `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
                            `avatar` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '头像',
                            `email` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮箱',
                            `phone_number` varchar(15) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '电话号码',
                            `remark` text COLLATE utf8mb4_unicode_ci COMMENT '备注',
                            `introduction` text COLLATE utf8mb4_unicode_ci COMMENT '个人简介',
                            `status` int(1) NOT NULL DEFAULT '0' COMMENT '用户状态 0正常 1禁用',
                            `is_admin` int(1) NOT NULL DEFAULT '1' COMMENT '是否管理员 0是 1否',
                            `is_delete` int(1) DEFAULT '1' COMMENT '是否删除 0是 1否',
                            `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                            PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` VALUES (1,'超级管理员','admin','21232f297a57a5a743894a0e4a801fc3','/files/20241120/ujixol0qms_1732068959280.jpeg','13888888888@163.com','13888888888','管理员大大，不要随便改信息。','一个好人一个好人一个好人一个好人一个好人',0,0,1,'2024-09-04 17:49:26','2024-11-20 10:15:59');
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user_roles`
--

DROP TABLE IF EXISTS `sys_user_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
SET character_set_client = utf8mb4 ;
CREATE TABLE `sys_user_roles` (
                                  `user_id` int(100) NOT NULL COMMENT '用户ID',
                                  `role_id` int(100) NOT NULL COMMENT '角色ID',
                                  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user_roles`
--

LOCK TABLES `sys_user_roles` WRITE;
/*!40000 ALTER TABLE `sys_user_roles` DISABLE KEYS */;
INSERT INTO `sys_user_roles` VALUES (1,1,'2024-09-23 14:56:59','2024-09-23 14:56:59'),(2,2,'2024-11-20 10:54:15','2024-11-20 10:54:15');
/*!40000 ALTER TABLE `sys_user_roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'dbapi'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-11-20 12:02:59
