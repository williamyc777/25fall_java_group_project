# 测试指南 / Testing Guide

## 重启服务器 / Restart Server

### 方法1：使用脚本（推荐）
```bash
cd /Users/mac/25fall-java
./restart-server.sh
```

### 方法2：手动重启
```bash
# 1. 停止现有服务器
lsof -ti :8082 | xargs kill -9

# 2. 启动服务器
cd backend
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### 方法3：在IDE中重启
- 如果使用IDE运行，直接停止当前运行，然后重新运行

## 检查服务器状态

```bash
# 检查端口是否被占用
lsof -i :8082

# 检查服务器日志
tail -f /tmp/spring-boot.log

# 测试服务器是否响应
curl http://localhost:8082/
```

## 测试管理员功能

### 1. 登录管理员账号
- 访问：http://localhost:8082/
- 使用管理员账号登录（通常是 `admin` 或 `user1`）

### 2. 访问管理员界面
- 访问：http://localhost:8082/admin
- 或从导航栏进入（如果已添加管理员链接）

### 3. 测试功能

#### 用户管理
- ✅ 查看用户列表（应显示：ID、Username、Name、Password、Permission）
- ✅ 点击 "Edit Permission" 按钮
- ✅ 在模态框中修改权限（Can Create Event、Can Enroll Events、Can Comment）
- ✅ 点击 "Save" 保存
- ✅ 验证权限是否更新

#### 活动管理
- ✅ 切换到 "Event Management" 标签
- ✅ 查看活动列表（应显示：ID、Title、Author）
- ✅ 点击 "Delete" 按钮删除活动
- ✅ 确认删除对话框
- ✅ 验证活动是否被删除

#### 帖子管理
- ✅ 切换到 "Post Management" 标签
- ✅ 查看帖子列表（应显示：ID、Title、Author）
- ✅ 点击 "Delete" 按钮删除帖子
- ✅ 确认删除对话框
- ✅ 验证帖子是否被删除

## 测试普通用户功能

### 1. 登录普通用户
- 使用普通用户账号登录（如 `user2`、`user3`）

### 2. 测试功能
- ✅ 查看活动列表
- ✅ 创建活动
- ✅ 报名活动
- ✅ 查看帖子
- ✅ 发布帖子
- ✅ 点赞/收藏帖子
- ✅ 评论帖子
- ✅ 查看个人中心
- ✅ 编辑个人资料

## 常见问题排查

### 服务器无法启动
```bash
# 检查端口占用
lsof -i :8082

# 查看错误日志
tail -50 /tmp/spring-boot.log
```

### 页面无法访问
1. 确认服务器已启动（检查日志中的 "Started BackendApplication"）
2. 清除浏览器缓存（Cmd+Shift+R）
3. 检查浏览器控制台错误（F12）

### 功能不工作
1. 打开浏览器开发者工具（F12）
2. 查看 Console 标签的错误信息
3. 查看 Network 标签的 API 请求和响应

