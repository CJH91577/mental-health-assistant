package org.example.aispingboot.DTO.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 咨询会话列表项（用户端与管理端字段合并，按需填充）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionListResponseDTO {
    private Long id;
    private Long userId;
    private String sessionTitle;
    private String lastMessageContent;
    private Integer messageCount;
    private Integer durationMinutes;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startedAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastMessageTime;

    /** 管理端展示用 */
    private String userNickname;
    private String userAvatar;
}
