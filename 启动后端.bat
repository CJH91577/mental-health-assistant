@echo off
title 心理健康助手 - 后端服务 (端口 1235)
cd /d "%~dp0ai-spingboot"
echo ==========================================
echo   心理健康助手 后端服务启动中...
echo   访问地址: http://localhost:1235
echo   （前端页面已打包进后端，直接访问即可）
echo   按 Ctrl+C 停止服务
echo ==========================================
java -jar target\ai-spingboot-0.0.1-SNAPSHOT.jar
pause