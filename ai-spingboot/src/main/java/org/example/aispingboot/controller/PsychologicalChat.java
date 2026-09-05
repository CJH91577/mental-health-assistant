package org.example.aispingboot.controller;

import cn.hutool.json.JSONUtil;
import jakarta.validation.Valid;
import org.example.aispingboot.AiService.PsychologicalSupportService;
import org.example.aispingboot.AiService.StructOutPut;
import org.example.aispingboot.DTO.command.ConsultationSessionCreateDTO;
import org.example.aispingboot.DTO.command.ConsultationStreamDTO;
import org.example.aispingboot.DTO.query.ConsultationSessionQueryDTO;
import org.example.aispingboot.DTO.response.ConsultationMessageResponseDTO;
import org.example.aispingboot.DTO.response.SessionListResponseDTO;
import org.example.aispingboot.common.PageResult;
import org.example.aispingboot.common.Result;
import org.example.aispingboot.common.ResultCode;
import org.example.aispingboot.service.ConsultationMessageService;
import org.example.aispingboot.service.ConsultationSessionService;
import org.example.aispingboot.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/psychological-chat")
public class PsychologicalChat {
    @Autowired
    private PsychologicalSupportService psychologicalSupportService;

    @Autowired
    private ConsultationSessionService consultationSessionService;

    @Autowired
    private ConsultationMessageService consultationMessageService;

    @PostMapping("/session/start")
    public Result<StructOutPut.StreamChatSession> startSession(@Valid @RequestBody ConsultationSessionCreateDTO createDTO) {
        Long userId = JwtTokenUtil.getCurrentUserId();
        StructOutPut.StreamChatSession session = psychologicalSupportService.startSession(userId, createDTO);
        return Result.ok(session);
    }

    /**
     * 会话列表：用户端看自己的，管理端看全部
     */
    @GetMapping("/sessions")
    public Result<PageResult<SessionListResponseDTO>> sessions(ConsultationSessionQueryDTO query) {
        Long userId = JwtTokenUtil.getCurrentUserId();
        boolean isAdmin = JwtTokenUtil.isCurrentUserAdmin();
        Integer pageNum = query.getPageNum() != null ? query.getPageNum()
                : (query.getCurrentPage() != null ? query.getCurrentPage()
                : (query.getCurrent() != null ? query.getCurrent() : 1));
        Integer pageSize = query.getPageSize() != null ? query.getPageSize()
                : (query.getSize() != null ? query.getSize() : 10);
        pageNum = pageNum < 1 ? 1 : pageNum;
        pageSize = pageSize < 1 || pageSize > 100 ? 10 : pageSize;
        return Result.ok(consultationSessionService.getSessionsPage(userId, isAdmin, pageNum, pageSize));
    }

    /**
     * 删除会话
     */
    @DeleteMapping("/sessions/{sessionId}")
    public Result<Void> deleteSession(@PathVariable Long sessionId) {
        Long userId = JwtTokenUtil.getCurrentUserId();
        consultationSessionService.deleteSession(userId, JwtTokenUtil.isCurrentUserAdmin(), sessionId);
        return Result.ok();
    }

    /**
     * 会话消息列表（按时间正序的数组）
     */
    @GetMapping("/sessions/{sessionId}/messages")
    public Result<List<ConsultationMessageResponseDTO>> sessionMessages(@PathVariable Long sessionId) {
        return Result.ok(consultationMessageService.getMessagesBySessionId(sessionId));
    }

    /**
     * 会话情绪分析（路径参数兼容前端传 "session_数字" 的前缀形式）
     */
    @GetMapping("/session/{sessionId}/emotion")
    public Result<Object> sessionEmotion(@PathVariable String sessionId) {
        Long id = psychologicalSupportService.extractSessionId(sessionId);
        if (id == null) {
            // 前端也可能传纯数字
            try {
                id = Long.parseLong(sessionId);
            } catch (NumberFormatException e) {
                return Result.error(ResultCode.PARAM_ERROR.getCode(), "sessionId格式错误", null);
            }
        }
        return Result.ok(consultationSessionService.getSessionEmotion(id));
    }

    @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> streamChat(@Valid @RequestBody ConsultationStreamDTO streamDTO) {
        // 获取当前用户
        String token = JwtTokenUtil.getCurrentToken();
        if (token == null || token.isBlank()) {
            return Flux.just(ServerSentEvent.<String>builder()
                    .event("error")
                    .data(JSONUtil.toJsonStr(Result.error(ResultCode.UNAUTHORIZED.getCode(), ResultCode.UNAUTHORIZED.getMsg(), "用户未登录")))
                    .build());
        }

        // 开始流式对话
        return psychologicalSupportService.streamPsychologicalChat(streamDTO.getSessionId(), streamDTO.getUserMessage())
                .map(Fragment -> {
                    return ServerSentEvent.<String>builder()
                            .event("message")
                            .data(JSONUtil.toJsonStr(Result.ok(Map.of("content", Fragment, "type", "normal"))))
                            .build();
                })
                .concatWith(Flux.just(ServerSentEvent.<String>builder()
                        .event("done")
                        .data("{}")
                        .build()
                ))
                .delayElements(Duration.ofMillis(50)); // 添加延迟确保流式数据的体验
    }
}
