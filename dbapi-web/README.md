# 欢迎使用 DBApi

**DBApi** 是一个高效且灵活的解决方案，旨在缩短数据库与 Web API 之间的距离。通过 DBApi，开发者可以轻松地将 SQL 查询转换为 RESTful API，使数据集成与共享更加简单。

使用 DBApi，您可以：

- 轻松将复杂的 SQL 查询转换为简单的 RESTful 接口。
- 通过强大的身份验证和授权机制提升数据安全性。
- 根据不同的使用场景自定义 API 响应，包括 JSON、XML 等格式。
- 无缝集成到现有系统中，不论底层使用何种数据库。

DBApi 非常适合开发者、数据工程师和希望简化数据访问、缩短开发时间并提升生产力的企业。立即使用 DBApi，充分释放数据的潜力。

# 部署与运行

## 直接运行

1. 从release页面下载release包，解压后配置application.yml文件，配置数据库连接信息，然后直接运行jar包即可
2. 运行成功后，访问


## 源码运行

**环境要求**

- Java 11+
- Spring Boot 2.5+
- MySQL / PostgreSQL / MongoDB（或其他兼容数据库）
- Maven 3.6+

1. **克隆项目**

```bash
git clone https://github.com/yourusername/DBApi.git
cd DBApi
```

2. **初始化数据库**

创建数据库

```sql
CREATE DATABASE dbapi CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

初始化表：`init.sql`在resource目录下
```sql
source init.sql
```

3. **修改数据库配置**

根据需要修改application.yml对应配置内容

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/your_database
    username: your_username
    password: your_password
```

4. **运行项目**

运行`src/main/java/com/dbapi/DBApiApplication.java`启动项目
