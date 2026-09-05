package org.example.aispingboot.DTO.command;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

/**
 * 添加情绪日记命令
 */
@Data
public class EmotionDiaryCreateDTO {
    @NotNull(message = "日记日期不能为空")
    private LocalDate diaryDate;

    @NotNull(message = "情绪评分不能为空")
    @Min(value = 1, message = "情绪评分最低为1分")
    @Max(value = 10, message = "情绪评分最高为10分")
    private Integer moodScore;

    @Size(max = 50, message = "主要情绪长度不能超过50个字符")
    private String dominantEmotion;

    private String emotionTriggers;

    private String diaryContent;

    @Min(value = 1, message = "睡眠质量最低为1")
    @Max(value = 5, message = "睡眠质量最高为5")
    private Integer sleepQuality;

    @Min(value = 1, message = "压力水平最低为1")
    @Max(value = 5, message = "压力水平最高为5")
    private Integer stressLevel;
}
