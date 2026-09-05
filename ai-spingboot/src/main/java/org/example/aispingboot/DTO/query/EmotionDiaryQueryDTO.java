package org.example.aispingboot.DTO.query;

import lombok.Data;

/**
 * 情绪日记后台分页查询参数（管理端使用 current/size）
 */
@Data
public class EmotionDiaryQueryDTO {
    private Integer current;
    private Integer size;
    private Integer userId;
    /** 情绪评分范围：1-3 / 4-6 / 7-10（前端参数名拼写即如此） */
    private String moodScreRange;
}
