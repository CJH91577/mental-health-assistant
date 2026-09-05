package org.example.aispingboot.service;

import org.example.aispingboot.DTO.response.AnalyticsOverviewDTO;
import org.example.aispingboot.mapper.ConsultationSessionMapper;
import org.example.aispingboot.mapper.EmotionDiaryMapper;
import org.example.aispingboot.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据分析服务（管理端看板）
 */
@Service
public class DataAnalyticsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private EmotionDiaryMapper emotionDiaryMapper;

    @Autowired
    private ConsultationSessionMapper consultationSessionMapper;

    public AnalyticsOverviewDTO overview() {
        LocalDate today = LocalDate.now();
        LocalDate start = today.minusDays(6);
        String startStr = start.toString();

        AnalyticsOverviewDTO result = new AnalyticsOverviewDTO();

        // 1. 系统概览
        AnalyticsOverviewDTO.SystemOverview overview = new AnalyticsOverviewDTO.SystemOverview();
        overview.setTotalUsers(userMapper.selectCount(null));
        overview.setActiveUsers(consultationSessionMapper.countActiveUsersSince(startStr));
        overview.setTotalDiaries(emotionDiaryMapper.selectCount(null));
        overview.setTodayNewDiaries(emotionDiaryMapper.countToday());
        overview.setTotalSessions(consultationSessionMapper.countAll());
        overview.setTodayNewSessions(consultationSessionMapper.countToday());
        Double avgMood = emotionDiaryMapper.avgMoodScore();
        overview.setAvgMoodScore(avgMood == null ? 0 : round1(avgMood));
        result.setSystemOverview(overview);

        // 2. 情绪趋势（近7天，缺失日期补0）
        Map<String, AnalyticsOverviewDTO.EmotionTrendItem> trendMap = new HashMap<>();
        for (Map<String, Object> row : emotionDiaryMapper.selectEmotionTrend(startStr)) {
            AnalyticsOverviewDTO.EmotionTrendItem item = new AnalyticsOverviewDTO.EmotionTrendItem();
            item.setDate(String.valueOf(row.get("date")));
            item.setAvgMoodScore(toDouble(row.get("avgMoodScore")));
            item.setRecordCount(toLong(row.get("recordCount")));
            trendMap.put(item.getDate(), item);
        }
        List<AnalyticsOverviewDTO.EmotionTrendItem> emotionTrend = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            String date = start.plusDays(i).toString();
            AnalyticsOverviewDTO.EmotionTrendItem item = trendMap.get(date);
            emotionTrend.add(item != null ? item : emptyTrendItem(date));
        }
        result.setEmotionTrend(emotionTrend);

        // 3. 咨询活动统计
        AnalyticsOverviewDTO.ConsultationStats stats = new AnalyticsOverviewDTO.ConsultationStats();
        stats.setTotalSessions(consultationSessionMapper.countAll());
        Double avgDuration = consultationSessionMapper.avgDurationMinutes();
        stats.setAvgDurationMinutes(avgDuration == null ? 0 : round1(avgDuration));
        Map<String, AnalyticsOverviewDTO.DailyTrendItem> dailyMap = new HashMap<>();
        for (Map<String, Object> row : consultationSessionMapper.selectDailyTrend(startStr)) {
            AnalyticsOverviewDTO.DailyTrendItem item = new AnalyticsOverviewDTO.DailyTrendItem();
            item.setDate(String.valueOf(row.get("date")));
            item.setSessionCount(toLong(row.get("sessionCount")));
            item.setUserCount(toLong(row.get("userCount")));
            dailyMap.put(item.getDate(), item);
        }
        List<AnalyticsOverviewDTO.DailyTrendItem> dailyTrend = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            String date = start.plusDays(i).toString();
            AnalyticsOverviewDTO.DailyTrendItem item = dailyMap.get(date);
            if (item == null) {
                item = new AnalyticsOverviewDTO.DailyTrendItem();
                item.setDate(date);
                item.setSessionCount(0);
                item.setUserCount(0);
            }
            dailyTrend.add(item);
        }
        stats.setDailyTrend(dailyTrend);
        result.setConsultationStats(stats);

        // 4. 用户活跃度趋势
        Map<String, Long> newUsersMap = new HashMap<>();
        for (Map<String, Object> row : userMapper.selectNewUsersDaily(startStr)) {
            newUsersMap.put(String.valueOf(row.get("date")), toLong(row.get("newUsers")));
        }
        Map<String, Long> diaryUsersMap = new HashMap<>();
        for (Map<String, Object> row : emotionDiaryMapper.selectDiaryDailyStats(startStr)) {
            diaryUsersMap.put(String.valueOf(row.get("date")), toLong(row.get("userCount")));
        }
        Map<String, Long> consultationUsersMap = new HashMap<>();
        for (AnalyticsOverviewDTO.DailyTrendItem item : dailyTrend) {
            consultationUsersMap.put(item.getDate(), item.getUserCount());
        }

        List<AnalyticsOverviewDTO.UserActivityItem> userActivity = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            String date = start.plusDays(i).toString();
            AnalyticsOverviewDTO.UserActivityItem item = new AnalyticsOverviewDTO.UserActivityItem();
            item.setDate(date);
            item.setActiveUsers(consultationSessionMapper.countActiveUsersOn(date));
            item.setNewUsers(newUsersMap.getOrDefault(date, 0L));
            item.setDiaryUsers(diaryUsersMap.getOrDefault(date, 0L));
            item.setConsultationUsers(consultationUsersMap.getOrDefault(date, 0L));
            userActivity.add(item);
        }
        result.setUserActivity(userActivity);

        return result;
    }

    private double round1(double v) {
        return BigDecimal.valueOf(v).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    private double toDouble(Object o) {
        if (o == null) {
            return 0;
        }
        return Double.parseDouble(String.valueOf(o));
    }

    private long toLong(Object o) {
        if (o == null) {
            return 0;
        }
        if (o instanceof Number n) {
            return n.longValue();
        }
        return Long.parseLong(String.valueOf(o));
    }

    private AnalyticsOverviewDTO.EmotionTrendItem emptyTrendItem(String date) {
        AnalyticsOverviewDTO.EmotionTrendItem item = new AnalyticsOverviewDTO.EmotionTrendItem();
        item.setDate(date);
        item.setAvgMoodScore(0);
        item.setRecordCount(0);
        return item;
    }
}
