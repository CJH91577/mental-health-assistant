package org.example.aispingboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.aispingboot.DTO.response.SessionListResponseDTO;
import org.example.aispingboot.entity.ConsultationSession;

import java.util.List;
import java.util.Map;

@Mapper
public interface ConsultationSessionMapper extends BaseMapper<ConsultationSession> {

    /**
     * 分页查询会话（附带最后一条消息内容/时间、消息数）
     */
    @Select("SELECT s.id, s.user_id, s.session_title, s.started_at, " +
            "(SELECT COUNT(*) FROM consultation_message m WHERE m.session_id = s.id) AS message_count, " +
            "(SELECT m2.content FROM consultation_message m2 WHERE m2.session_id = s.id ORDER BY m2.created_at DESC, m2.id DESC LIMIT 1) AS last_message_content, " +
            "(SELECT m3.created_at FROM consultation_message m3 WHERE m3.session_id = s.id ORDER BY m3.created_at DESC, m3.id DESC LIMIT 1) AS last_message_time " +
            "FROM consultation_session s " +
            "WHERE (#{isAdmin} = true OR s.user_id = #{userId}) " +
            "ORDER BY s.started_at DESC")
    IPage<SessionListResponseDTO> selectSessionPage(IPage<?> page,
                                                    @Param("userId") Long userId,
                                                    @Param("isAdmin") boolean isAdmin);

    @Select("SELECT COUNT(*) FROM consultation_session")
    Long countAll();

    @Select("SELECT COUNT(*) FROM consultation_session WHERE DATE(started_at) = CURDATE()")
    Long countToday();

    @Select("SELECT DATE_FORMAT(started_at,'%Y-%m-%d') AS date, COUNT(*) AS sessionCount, COUNT(DISTINCT user_id) AS userCount " +
            "FROM consultation_session WHERE started_at >= #{start} " +
            "GROUP BY DATE_FORMAT(started_at,'%Y-%m-%d')")
    List<Map<String, Object>> selectDailyTrend(@Param("start") String start);

    @Select("SELECT AVG(TIMESTAMPDIFF(MINUTE, first_time, last_time)) FROM (" +
            "SELECT s.id, MIN(m.created_at) AS first_time, MAX(m.created_at) AS last_time " +
            "FROM consultation_session s JOIN consultation_message m ON m.session_id = s.id GROUP BY s.id) t")
    Double avgDurationMinutes();

    @Select("SELECT COUNT(DISTINCT user_id) FROM (" +
            "SELECT user_id, DATE(started_at) AS d FROM consultation_session " +
            "UNION ALL " +
            "SELECT user_id, diary_date AS d FROM emotion_diary) t WHERE t.d = #{date}")
    Long countActiveUsersOn(@Param("date") String date);

    @Select("SELECT COUNT(DISTINCT user_id) FROM (" +
            "SELECT user_id FROM consultation_session WHERE started_at >= #{start} " +
            "UNION " +
            "SELECT user_id FROM emotion_diary WHERE diary_date >= #{start}) t")
    Long countActiveUsersSince(@Param("start") String start);
}
