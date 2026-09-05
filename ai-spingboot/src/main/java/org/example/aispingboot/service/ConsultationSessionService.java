package org.example.aispingboot.service;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.aispingboot.DTO.command.ConsultationSessionCreateDTO;
import org.example.aispingboot.DTO.response.SessionListResponseDTO;
import org.example.aispingboot.common.PageResult;
import org.example.aispingboot.entity.ConsultationSession;
import org.example.aispingboot.entity.User;
import org.example.aispingboot.exception.BusinessException;
import org.example.aispingboot.mapper.ConsultationSessionMapper;
import org.example.aispingboot.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ConsultationSessionService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ConsultationSessionMapper consultationSessionMapper;

    public ConsultationSession createSession(Long userId, ConsultationSessionCreateDTO createDTO) {
        // 验证用户是否存在
        User user =userMapper.selectById(userId);
        if (user != null) {
            // 创建会话记录
             ConsultationSession session = ConsultationSession.builder()
                    .userId(userId)
                    .sessionTitle(createDTO.getSessionTitle())
                    .startedAt(LocalDateTime.now())
                    .build();
            // 如果未提供标题
            if (StrUtil.isBlank(createDTO.getSessionTitle())) {
                session.setSessionTitle(String.format("宁渡AI助手 - " + DateUtil.format(LocalDateTime.now(), "MM-dd HH:mm")));
            }

            // 插入记录
            consultationSessionMapper.insert(session);
            return session;
        }

        return null;
    }

    /**
     * 会话分页列表：管理员看全部，普通用户仅看自己的
     */
    public PageResult<SessionListResponseDTO> getSessionsPage(Long userId, boolean isAdmin, int pageNum, int pageSize) {
        IPage<SessionListResponseDTO> page = consultationSessionMapper.selectSessionPage(
                new Page<>(pageNum, pageSize), userId, isAdmin);

        // 计算时长并补默认值
        for (SessionListResponseDTO row : page.getRecords()) {
            if (row.getMessageCount() == null) {
                row.setMessageCount(0);
            }
            if (row.getStartedAt() != null && row.getLastMessageTime() != null) {
                row.setDurationMinutes((int) Math.max(0, Duration.between(row.getStartedAt(), row.getLastMessageTime()).toMinutes()));
            } else {
                row.setDurationMinutes(0);
            }
        }

        // 批量填充用户昵称/头像（管理端展示）
        List<Long> userIds = page.getRecords().stream().map(SessionListResponseDTO::getUserId)
                .filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (!userIds.isEmpty()) {
            Map<Long, User> userMap = userMapper.selectBatchIds(userIds).stream()
                    .collect(Collectors.toMap(User::getId, Function.identity()));
            for (SessionListResponseDTO row : page.getRecords()) {
                User user = userMap.get(row.getUserId());
                if (user != null) {
                    row.setUserNickname(StrUtil.isNotBlank(user.getNickname()) ? user.getNickname() : user.getUsername());
                    row.setUserAvatar(user.getAvatar());
                }
            }
        }

        return new PageResult<>(page.getRecords(), page.getTotal(), page.getCurrent(), page.getSize());
    }

    /**
     * 删除会话（消息由数据库外键级联删除）
     */
    public void deleteSession(Long userId, boolean isAdmin, Long sessionId) {
        ConsultationSession session = consultationSessionMapper.selectById(sessionId);
        if (session == null) {
            throw new BusinessException("会话不存在");
        }
        if (!isAdmin && !session.getUserId().equals(userId)) {
            throw new BusinessException("无权删除该会话");
        }
        consultationSessionMapper.deleteById(sessionId);
    }

    /**
     * 获取会话的最后一次情绪分析结果
     */
    public Object getSessionEmotion(Long sessionId) {
        ConsultationSession session = consultationSessionMapper.selectById(sessionId);
        if (session == null) {
            throw new BusinessException("会话不存在");
        }
        String analysis = session.getLastEmotionAnalysis();
        if (StrUtil.isBlank(analysis)) {
            return null;
        }
        try {
            return cn.hutool.json.JSONUtil.parse(analysis);
        } catch (Exception e) {
            return null;
        }
    }
}
