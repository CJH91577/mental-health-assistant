package org.example.aispingboot.DTO.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 情绪日记响应（后台列表行）
 */
@Data
public class EmotionDiaryResponseDTO {
    private Long id;
    private Long userId;
    private String username;
    private String nickname;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate diaryDate;

    private Integer moodScore;
    private Integer sleepQuality;
    private Integer stressLevel;
    private String emotionTriggers;
    private String diaryContent;
    private String dominantEmotion;

    /** AI情绪分析结果(JSON字符串) */
    private String aiEmotionAnalysis;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}
