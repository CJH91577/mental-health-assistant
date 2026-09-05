package org.example.aispingboot.DTO.response;

import lombok.Data;

import java.util.List;

/**
 * 数据分析概览响应（对应管理端 dashboard.vue）
 */
@Data
public class AnalyticsOverviewDTO {
    private SystemOverview systemOverview;
    private List<EmotionTrendItem> emotionTrend;
    private ConsultationStats consultationStats;
    private List<UserActivityItem> userActivity;

    @Data
    public static class SystemOverview {
        private long totalUsers;
        private long activeUsers;
        private long totalDiaries;
        private long todayNewDiaries;
        private long totalSessions;
        private long todayNewSessions;
        private double avgMoodScore;
    }

    @Data
    public static class EmotionTrendItem {
        private String date;
        private double avgMoodScore;
        private long recordCount;
    }

    @Data
    public static class ConsultationStats {
        private long totalSessions;
        private double avgDurationMinutes;
        private List<DailyTrendItem> dailyTrend;
    }

    @Data
    public static class DailyTrendItem {
        private String date;
        private long sessionCount;
        private long userCount;
    }

    @Data
    public static class UserActivityItem {
        private String date;
        private long activeUsers;
        private long newUsers;
        private long diaryUsers;
        private long consultationUsers;
    }
}
