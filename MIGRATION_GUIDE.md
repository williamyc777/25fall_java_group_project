# 前端框架迁移指南

## 概述

根据教授的反馈，项目需要将前端从 Vue.js (JavaScript) 迁移到 Java 框架。我们已经完成了基础架构的搭建，使用 **Thymeleaf** 作为模板引擎。

## 已完成的修改

### 1. 依赖配置
- ✅ 在 `pom.xml` 中添加了 `spring-boot-starter-thymeleaf` 依赖
- ✅ 将 `spring-boot-starter-web-services` 改为 `spring-boot-starter-web`（支持视图渲染）

### 2. Thymeleaf 配置
- ✅ 创建了 `ThymeleafConfig.java` 配置类
- ✅ 在 `application.yml` 中添加了 Thymeleaf 配置

### 3. 视图控制器
- ✅ 创建了 `ViewController.java`，包含所有主要页面的路由：
  - `/` - 登录页面
  - `/signup` - 注册页面
  - `/main` - 主页
  - `/event` - 活动页面
  - `/square` - 帖子广场
  - `/profile` - 个人资料
  - `/admin` - 管理员页面
  - `/message` - 消息页面
  - 等等...

### 4. 模板文件
- ✅ 创建了 `login.html` - 登录页面模板
- ✅ 创建了 `main.html` - 主页模板
- ✅ 创建了 `signup.html` - 注册页面模板
- ✅ 创建了 `event.html` - 活动详情页模板
- ✅ 创建了 `createEvent.html` - 创建活动页模板
- ✅ 创建了 `manageEvent.html` - 管理活动页模板
- ✅ 创建了 `square.html` - 帖子广场模板
- ✅ 创建了 `postDetail.html` - 帖子详情模板
- ✅ 创建了 `profile.html` - 个人资料页模板
- ✅ 创建了 `message.html` - 消息页模板
- ✅ 创建了 `admin.html` - 管理员页模板
- ✅ 创建了 `searchPost.html` - 搜索帖子页模板
- ✅ 创建了 `searchEvent.html` - 搜索活动页模板

### 5. 数据种子
- ✅ 创建了 `DataSeeder.java`，自动初始化测试数据：
  - 管理员账户 (admin/admin)
  - 不同权限组的用户（可创建活动、可报名、可评论等）
  - 多个测试用户账户

## 待完成的工作

### 1. 完善模板功能
所有主要模板文件已创建，但部分功能需要进一步完善：
- 完善帖子列表的加载和显示逻辑
- 完善评论功能的实现
- 完善搜索功能的API调用
- 添加更多交互细节和错误处理

### 2. 修改登录/注册逻辑
- 修改 `AbstractUserApp.java` 中的登录方法，支持 Cookie 设置
- 修改注册方法，注册成功后自动登录

### 3. 静态资源迁移
- 将 Vue 前端的静态资源（CSS、图片等）迁移到 `src/main/resources/static/`
- 可以使用 Bootstrap 或其他 CSS 框架（已在模板中使用 Bootstrap 5）

### 4. API 兼容性
- 保持现有的 `@RestController` 不变，用于 AJAX 请求
- 视图控制器使用 `@Controller` 返回模板
- 两者可以共存

## 技术栈说明

### 前端框架
- **Thymeleaf 3** - 服务器端模板引擎
- **Bootstrap 5** - UI 框架（已在模板中使用）
- **原生 JavaScript** - 用于 AJAX 请求和交互（最小化使用）

### 后端框架
- **Spring Boot 3.2.4**
- **Spring MVC** - Web 框架
- **Spring Data JPA** - 数据访问

## 开发建议

### 1. 模板开发
- 使用 Thymeleaf 语法进行数据绑定：`th:text="${variable}"`
- 使用 Thymeleaf URL：`th:href="@{/path}"`
- 条件渲染：`th:if="${condition}"`
- 循环：`th:each="item : ${list}"`

### 2. 前后端交互
- 表单提交：使用传统的 form 提交或 AJAX
- 数据获取：通过 AJAX 调用现有的 REST API
- Token 管理：使用 Cookie 存储 token

### 3. 样式和交互
- 使用 Bootstrap 组件快速构建 UI
- 使用原生 JavaScript 处理简单交互
- 复杂交互可以考虑使用轻量级 JS 库（如 Alpine.js）

## 部署注意事项

### 1. 容器化
根据教授反馈，需要容器化应用：
- 已有 `Dockerfile`，需要确保包含所有依赖
- 确保 Thymeleaf 模板和静态资源被正确打包

### 2. 数据种子
- `DataSeeder` 会在应用启动时自动运行
- 确保数据库连接正常
- 种子数据包括不同权限组的用户，便于测试

### 3. 测试
- 测试所有页面的访问
- 测试用户登录和权限控制
- 测试不同用户组的权限功能

## 下一步行动

1. **完成所有模板文件** - 将 Vue 组件转换为 Thymeleaf 模板
2. **迁移静态资源** - 将 CSS、图片等资源移到 static 目录
3. **完善交互逻辑** - 使用 JavaScript 实现必要的交互功能
4. **测试和调试** - 确保所有功能正常工作
5. **容器化部署** - 确保 Docker 配置正确

## 参考资源

- [Thymeleaf 官方文档](https://www.thymeleaf.org/documentation.html)
- [Spring Boot + Thymeleaf 教程](https://spring.io/guides/gs/serving-web-content/)
- [Bootstrap 5 文档](https://getbootstrap.com/docs/5.3/)

