package org.example.aispingboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.aispingboot.entity.EmotionDiary;

import java.util.List;
import java.util.Map;

@Mapper
public interface EmotionDiaryMapper extends BaseMapper<EmotionDiary> {

    @Select("SELECT DATE_FORMAT(diary_date,'%Y-%m-%d') AS date, ROUND(AVG(mood_score),1) AS avgMoodScore, COUNT(*) AS recordCount " +
            "FROM emotion_diary WHERE diary_date >= #{start} " +
            "GROUP BY DATE_FORMAT(diary_date,'%Y-%m-%d')")
    List<Map<String, Object>> selectEmotionTrend(@Param("start") String start);

    @Select("SELECT DATE_FORMAT(diary_date,'%Y-%m-%d') AS date, COUNT(DISTINCT user_id) AS userCount " +
            "FROM emotion_diary WHERE diary_date >= #{start} " +
            "GROUP BY DATE_FORMAT(diary_date,'%Y-%m-%d')")
    List<Map<String, Object>> selectDiaryDailyStats(@Param("start") String start);

    @Select("SELECT COUNT(*) FROM emotion_diary WHERE diary_date = CURDATE()")
    Long countToday();

    @Select("SELECT ROUND(AVG(mood_score),1) FROM emotion_diary")
    Double avgMoodScore();
}
