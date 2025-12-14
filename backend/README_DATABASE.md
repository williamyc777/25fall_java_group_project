# 数据库配置说明

## 问题
如果遇到 `Communications link failure` 错误，说明MySQL数据库没有运行。

## 解决方案

### 方案1: 使用H2内存数据库（推荐用于开发测试）

1. 使用开发配置启动：
```bash
cd backend
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

或者设置环境变量：
```bash
export SPRING_PROFILES_ACTIVE=dev
mvn spring-boot:run
```

H2数据库会在应用启动时自动创建，应用关闭后数据会清空。

### 方案2: 启动MySQL数据库

#### macOS (使用Homebrew):
```bash
brew services start mysql
```

#### 或者手动启动:
```bash
mysql.server start
```

#### 创建数据库:
```bash
mysql -u root -p
CREATE DATABASE cs304_database;
```

然后使用默认配置启动：
```bash
cd backend
mvn spring-boot:run
```

### 方案3: 使用Docker运行MySQL

```bash
docker run --name mysql-dev -e MYSQL_ROOT_PASSWORD=LYX112601lyx -e MYSQL_DATABASE=cs304_database -p 3306:3306 -d mysql:8.0
```

## 配置文件说明

- `application.yml` - 默认配置（使用MySQL）
- `application-dev.yml` - 开发配置（使用H2内存数据库）
- `application-test.yml` - 测试配置（使用PostgreSQL）

## H2控制台

如果使用H2数据库，可以通过以下URL访问H2控制台：
- URL: http://localhost:8082/h2-console
- JDBC URL: `jdbc:h2:mem:testdb`
- 用户名: `sa`
- 密码: (留空)


