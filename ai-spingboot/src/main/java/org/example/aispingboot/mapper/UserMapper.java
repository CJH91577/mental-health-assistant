package org.example.aispingboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.aispingboot.entity.User;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("SELECT DATE_FORMAT(created_at,'%Y-%m-%d') AS date, COUNT(*) AS newUsers " +
            "FROM user WHERE created_at >= #{start} " +
            "GROUP BY DATE_FORMAT(created_at,'%Y-%m-%d')")
    List<Map<String, Object>> selectNewUsersDaily(@Param("start") String start);
}
