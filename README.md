# 心理健康助手（Mental Health Assistant）

一个面向大学生群体的心理健康应用：**AI 心理咨询 + 情绪日记 + 心理健康知识库 + 数据看板**。

## ✨ 功能

| 模块 | 说明 |
|------|------|
| AI 心理咨询 | 基于大模型（DeepSeek）的流式对话（SSE），支持多会话管理、情绪花园、对话记忆 |
| 情绪日记 | 情绪评分（1-10）+ 8 种情绪卡片 + 睡眠/压力记录，提交后自动生成 AI 情绪分析 |
| 知识科普 | 心理健康文章分类浏览、阅读量统计；后台支持发布/下线/富文本编辑/封面上传 |
| 数据统计 | 管理端看板：情绪趋势、咨询活动、用户活跃度（ECharts） |
| 用户体系 | JWT 登录注册，普通用户 / 管理员双角色双端界面 |

## 🛠 技术栈

- **前端**：Vue 3 + Vite + Element Plus + Pinia + Vue Router + ECharts + wangEditor（`ai-vue/`）
- **后端**：Spring Boot 3.5 + Spring AI（OpenAI 兼容协议）+ Spring Security + MyBatis-Plus + MySQL（`ai-spingboot/`）
- **AI 模型**：默认 DeepSeek-V3（经硅基流动平台），兼容任何 OpenAI 协议接口

## 📁 目录结构

```
├── ai-vue/            # 前端源码（Vue 3）
├── ai-spingboot/      # 后端源码（Spring Boot，static/ 内为打包后的前端）
├── 资料/              # 建库 SQL、技术文档
├── 启动后端.bat        # Windows 一键启动脚本
└── 启动前端开发模式.bat # Windows 前端开发模式脚本
```

## 🚀 快速开始（Windows）

**环境要求**：JDK 17+、Maven、Node.js 20+、MySQL 8。

1. **初始化数据库**：执行 `资料/mental_health_assistant.sql`（包含表结构与演示数据）。
2. **配置环境变量**（本机只需设一次，见下表）。
3. **启动后端**：双击 `启动后端.bat`（或 `cd ai-spingboot && mvn spring-boot:run`），访问 http://localhost:1235 —— 前端页面已打包进后端，无需单独启动前端。
4. **前端开发模式**（改前端代码时）：先启动后端，再双击 `启动前端开发模式.bat`。

**演示账号**（密码均为 `123456`）：`admin`（管理员）/ `test`、`ces`（普通用户）。

## 🔑 密钥配置（API Key 等一律不入库）

所有密钥通过**环境变量**配置，代码中只保留占位符，**不会因上传 GitHub 而泄露**：

| 环境变量 | 说明 | 示例 |
|---------|------|------|
| `SILICONFLOW_API_KEY` | 硅基流动 API Key（https://siliconflow.cn 控制台申请） | `sk-xxxx` |
| `AI_API_KEY` | 通用 AI Key（DeepSeek 官方等平台，优先级高于上面） | `sk-xxxx` |
| `AI_BASE_URL` | AI 接口地址（默认硅基流动 `https://api.siliconflow.cn`） | `https://api.deepseek.com` |
| `AI_MODEL` | 模型名（默认 `deepseek-ai/DeepSeek-V3`） | `deepseek-chat` |
| `MYSQL_PASSWORD` | 本机 MySQL 密码（默认 123456，与本机不符时必须设置） | 你的密码 |
| `JWT_SECRET` | JWT 签名密钥（生产环境请务必覆盖默认值） | 随机字符串 |

> 例：硅基流动用户只需设置 `SILICONFLOW_API_KEY` 一个变量；DeepSeek 官方用户设置 `AI_API_KEY` + `AI_BASE_URL=https://api.deepseek.com` + `AI_MODEL=deepseek-chat`。
> Windows 设置方法：`Win+R` → `sysdm.cpl` → 高级 → 环境变量；或管理员 CMD 执行 `setx 变量名 "值"`。设置后需重启后端进程。

未配置 Key 时 AI 对话会返回友好提示，其余功能不受影响。

## 🐞 常见问题

- **AI 提示"服务暂时不可用"**：检查 Key 是否正确、是否设置了环境变量后重启后端、账户是否有余额；后端控制台会打印真实错误原因。
- **启动失败 Port 1235 in use**：任务管理器结束所有 `java.exe` 后再启动。
- **上传的图片**：保存在 `ai-spingboot/uploads/`（已加入 .gitignore）。

## 📄 其他文档

- 详细部署与运维说明见 `项目说明书.md`；
- 开发参考见 `资料/宁渡课堂-前端技术文档.md`、`资料/宁渡课堂-后端技术文档.md`。
