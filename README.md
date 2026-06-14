# 毕业设计选题系统

## 版本
- **v2.0（当前）**: Spring Boot 3.2 + Vue3 + Vant4 前后端分离
- **v1.0**: 原始 JSP/Servlet + Tomcat 8 版本（见 v1-original-jsp/）

## v2.0 技术栈
| 层 | 技术 |
|-----|------|
| 后端 | Spring Boot 3.2 + JDBC + MySQL 8.0 |
| 前端 | Vue3 + Vite + Vant4 + Axios |
| 数据库 | 阿里云 RDS MySQL 8.0 |

## 快速启动

### 后端
```
cd byxt-server
mvn clean package -DskipTests
java -jar target/byxt-server-1.0.0.jar
```
运行在 http://localhost:8088

### 前端
```
cd byxt-vue
npm install
npm run dev
```
运行在 http://localhost:3000

## 测试账号
- 学生: S001 / 123456
- 教师: T001 / 123456
- 管理员: admin / admin
- 超级管理员: root / root
