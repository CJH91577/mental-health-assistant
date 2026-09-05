# 💚 心理健康助手 —— 大学生心理健康 AI 陪伴平台

> **Vue 3 + Spring Boot 3 全栈应用**：AI 心理咨询（DeepSeek 流式对话）·
> 情绪日记与 AI 情绪分析 · 心理健康知识库 · 管理端数据看板。

面向大学生群体的心理健康应用，围绕「**温暖陪伴**」与「**专业支持**」两条主线设计：

- 对话侧：AI 心理咨询基于 DeepSeek-V3 大模型**流式回复（SSE）**，带会话记忆（近 30 条上下文），
  系统提示词内置共情与危机干预原则，全程中文温暖交流；
- 记录侧：情绪日记记录每日情绪、睡眠与压力（1-10 评分 + 8 种情绪卡片），
  提交后自动调用大模型生成**结构化 AI 情绪分析**（主要情绪/评分/风险等级/改善建议）；
- 运营侧：心理健康知识库（富文本文章 + 分类 + 阅读量）+ 管理端数据看板
  （情绪趋势 / 咨询活动 / 用户活跃度三类 ECharts 图表）。

```
浏览器（Vue 3 + Element Plus + ECharts + wangEditor，打包进后端 static/）
   │  /api/**（JWT 认证）   /files/**（上传文件静态服务）   SPA 深层路由回退
   ▼
Spring Boot 3.5 后端（端口 1235）
   ├─ 用户模块      登录 / 注册 / 登出 / 当前用户（BCrypt + JWT）
   ├─ 心理咨询      会话管理 + SSE 流式对话 + 会话情绪分析（ChatMemory 记忆）
   ├─ 情绪日记      日记 CRUD（同日唯一）+ 大模型情绪分析
   ├─ 知识库        分类 / 文章分页 / 详情阅读量 / 富文本 CRUD / 上下线
   ├─ 文件管理      封面上传（扩展名白名单）→ /files 静态服务
   └─ 数据统计      概览统计卡 + 近 7 天趋势（情绪 / 咨询 / 活跃度，缺失日期补 0）
   ▼
MySQL 8（mental_health_assistant，9 张表，含演示数据）
   ▼
DeepSeek-V3（硅基流动 / 任意 OpenAI 兼容端点，Spring AI 接入）
```

## ✨ 功能特性

| 模块 | 能力 |
|---|---|
| **AI 心理咨询**（用户端） | SSE 流式逐字回复、多会话管理、对话记忆、情绪花园展示、会话删除 |
| **情绪日记**（用户端） | 1-10 情绪评分 + 8 种情绪卡片 + 睡眠/压力指标，同一用户同一天仅一条 |
| **AI 情绪分析** | 日记提交后自动生成结构化分析：主要情绪 / 情绪评分 / 风险等级(0-3) / 关键词 / 专业建议 / 改善方案 |
| **知识库** | 分类浏览、文章详情（阅读量统计）、标签；管理端 wangEditor 富文本编辑 + 封面上传 + 发布/下线 |
| **数据看板**（管理端） | 统计卡（总用户/情绪日志/咨询会话/平均情绪）+ 情绪趋势、咨询活动、用户活跃度图表 |
| **咨询记录 / 情绪日志**（管理端） | 分页检索、会话详情回看、AI 情绪分析查看、删除 |
| **用户体系** | JWT 无状态认证，普通用户 / 管理员双角色，前端路由守卫 + 后端接口双重控制 |

## 🔐 安全设计

- **认证**：无状态 JWT（HMAC256，24h 过期），BCrypt 密码哈希，token 失效自动清理并跳转登录
- **角色隔离**：管理员接口服务端校验（非管理员拒绝），前端路由按角色分流
- **密钥安全**：AI Key、数据库密码、JWT 密钥**全部环境变量注入**，仓库不含任何明文密钥
- **上传安全**：封面图片扩展名白名单（jpg/jpeg/png/gif/webp），拒绝脚本等恶意文件
- **XSS 防护**：文章详情 DOMPurify 消毒、Markdown 渲染链接协议白名单、用户消息 HTML 转义

## 🚀 快速开始（Windows）

### 1. 环境要求

JDK 17+ · Maven 3.9+ · Node.js 20+ · MySQL 8

### 2. 初始化数据库

```bash
mysql -u root -p < 资料/mental_health_assistant.sql
```

### 3. 配置环境变量（本机设置一次即可）

| 环境变量 | 说明 | 示例 |
|---|---|---|
| `SILICONFLOW_API_KEY` | 硅基流动 API Key（siliconflow.cn 控制台申请） | `sk-xxxx` |
| `AI_API_KEY` / `AI_BASE_URL` / `AI_MODEL` | 其他 OpenAI 兼容平台（如 DeepSeek 官方） | `sk-xxxx` / `https://api.deepseek.com` / `deepseek-chat` |
| `MYSQL_PASSWORD` | 本机 MySQL 密码（默认 123456） | 你的密码 |
| `JWT_SECRET` | JWT 签名密钥（生产环境务必覆盖默认值） | 随机字符串 |

Windows 设置方法：`Win+R` → `sysdm.cpl` → 高级 → 环境变量；或管理员 CMD 执行 `setx 变量名 "值"`（设置后需重启后端进程）。

### 4. 启动

```bash
# 后端（已内置打包好的前端页面，无需单独启动前端）
cd ai-spingboot
mvn -DskipTests clean package
java -jar target/ai-spingboot-0.0.1-SNAPSHOT.jar
# 浏览器访问 http://localhost:1235
```

也可直接双击 `启动后端.bat`；前端开发模式（改前端代码时）双击 `启动前端开发模式.bat`。

**演示账号**（密码均为 `123456`）：`admin`（管理员）/ `test`、`ces`（普通用户）。

## 📁 目录结构

```
├── ai-vue/              # 前端源码（Vue 3 + Vite + Element Plus + ECharts + wangEditor）
├── ai-spingboot/        # 后端源码（Spring Boot 3 + Spring AI + MyBatis-Plus + Security）
│   └── src/main/resources/static/   # 打包后的前端页面（SPA 回退支持深层路由）
├── 资料/                # 建库 SQL、前后端技术文档
├── 启动后端.bat / 启动前端开发模式.bat
└── 项目说明书.md         # 部署与运维说明（接口清单、修复记录、故障排查）
```

开发文档见 `资料/前端技术文档.md`、`资料/后端技术文档.md`。

## 🐞 常见问题

- **AI 提示"服务暂时不可用"**：检查 Key 是否正确、设置环境变量后是否重启了后端、账户是否有余额；后端控制台会打印真实错误原因。
- **启动失败 "Port 1235 in use"**：任务管理器结束所有 `java.exe` 后重新启动。
- **上传的图片**：保存在 `ai-spingboot/uploads/`（已加入 .gitignore，不入仓库）。

## 🏷 标签

`心理健康` `mental-health` `AI心理咨询` `DeepSeek` `chatbot` `SSE流式对话` `Spring Boot` `Spring AI` `Vue3` `Element Plus` `ECharts` `MyBatis-Plus` `JWT` `情绪日记` `情绪分析` `知识库` `数据可视化` `大学生心理` `全栈项目`
