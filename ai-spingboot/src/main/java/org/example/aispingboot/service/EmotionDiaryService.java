package org.example.aispingboot.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.aispingboot.DTO.command.EmotionDiaryCreateDTO;
import org.example.aispingboot.DTO.query.EmotionDiaryQueryDTO;
import org.example.aispingboot.DTO.response.EmotionDiaryResponseDTO;
import org.example.aispingboot.common.PageResult;
import org.example.aispingboot.entity.EmotionDiary;
import org.example.aispingboot.entity.User;
import org.example.aispingboot.exception.BusinessException;
import org.example.aispingboot.mapper.EmotionDiaryMapper;
import org.example.aispingboot.mapper.UserMapper;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 情绪日记服务
 */
@Service
public class EmotionDiaryService {

    @Autowired
    private EmotionDiaryMapper emotionDiaryMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    @Qualifier("open-ai")
    private ChatClient chatClient;

    @Value("${spring.ai.openai.api-key:}")
    private String apiKey;

    /**
     * 添加情绪日记（同一天仅一条；配置了 AI Key 时同步生成 AI 情绪分析）
     */
    public void addDiary(Long userId, EmotionDiaryCreateDTO dto) {
        // 同一用户同一天只允许一条日记
        LambdaQueryWrapper<EmotionDiary> dupQuery = new LambdaQueryWrapper<>();
        dupQuery.eq(EmotionDiary::getUserId, userId)
                .eq(EmotionDiary::getDiaryDate, dto.getDiaryDate());
        if (emotionDiaryMapper.selectCount(dupQuery) > 0) {
            throw new BusinessException("该日期已存在情绪日记，同一天只能记录一条");
        }

        LocalDateTime now = LocalDateTime.now();
        EmotionDiary diary = EmotionDiary.builder()
                .userId(userId)
                .diaryDate(dto.getDiaryDate())
                .moodScore(dto.getMoodScore())
                .dominantEmotion(dto.getDominantEmotion())
                .emotionTriggers(dto.getEmotionTriggers())
                .diaryContent(dto.getDiaryContent())
                .sleepQuality(dto.getSleepQuality())
                .stressLevel(dto.getStressLevel())
                .createdAt(now)
                .updatedAt(now)
                .build();
        emotionDiaryMapper.insert(diary);

        // 配置了硅基流动 API Key 时，同步进行 AI 情绪分析
        if (StrUtil.isNotBlank(apiKey) && !"you-key".equals(apiKey.trim())) {
            try {
                String analysisJson = analyzeWithAi(diary);
                if (StrUtil.isNotBlank(analysisJson)) {
                    diary.setAiEmotionAnalysis(analysisJson);
                    diary.setAiAnalysisUpdatedAt(LocalDateTime.now());
                    emotionDiaryMapper.updateById(diary);
                }
            } catch (Exception e) {
                // AI 分析失败不影响日记保存
                diary.setAiEmotionAnalysis(null);
            }
        }
    }

    /**
     * 调用大模型对日记进行情绪分析，返回 JSON 字符串
     */
    private String analyzeWithAi(EmotionDiary diary) {
        String prompt = "你是一位专业的心理健康分析师。请根据下面的情绪日记输出严格合法的JSON（不要输出任何其他文字或代码块标记），字段如下：" +
                "primaryEmotion(中文,主要情绪), emotionScore(0-100的整数,情绪积极程度), isNegative(布尔), riskLevel(0-3的整数), " +
                "keywords(中文关键词字符串数组), suggestion(中文专业建议), icon(一个表情符号), label(中文标签), " +
                "riskDescription(中文风险描述), improvementSuggestions(3到5条中文改善建议数组), timestamp(当前毫秒时间戳)。" +
                "情绪日记：情绪评分" + diary.getMoodScore() + "分(满分10)，主要情绪：" + StrUtil.blankToDefault(diary.getDominantEmotion(), "未填写") +
                "，触发因素：" + StrUtil.blankToDefault(diary.getEmotionTriggers(), "未填写") +
                "，日记内容：" + StrUtil.blankToDefault(diary.getDiaryContent(), "未填写") +
                "，睡眠质量" + (diary.getSleepQuality() == null ? "未知" : diary.getSleepQuality() + "/5") +
                "，压力水平" + (diary.getStressLevel() == null ? "未知" : diary.getStressLevel() + "/5") + "。";

        String response = chatClient.prompt().user(prompt).call().content();
        if (StrUtil.isBlank(response)) {
            return null;
        }
        String json = extractJson(response);
        JSONObject obj = JSONUtil.parseObj(json);
        return obj.toString();
    }

    private String extractJson(String text) {
        String t = text.trim();
        int start = t.indexOf('{');
        int end = t.lastIndexOf('}');
        if (start >= 0 && end > start) {
            return t.substring(start, end + 1);
        }
        return t;
    }

    /**
     * 后台分页查询情绪日记
     */
    public PageResult<EmotionDiaryResponseDTO> getAdminPage(EmotionDiaryQueryDTO query) {
        long pageNo = query.getCurrent() == null ? 1 : query.getCurrent();
        long pageSize = query.getSize() == null ? 10 : query.getSize();

        LambdaQueryWrapper<EmotionDiary> wrapper = new LambdaQueryWrapper<>();
        if (query.getUserId() != null) {
            wrapper.eq(EmotionDiary::getUserId, query.getUserId());
        }
        if (StrUtil.isNotBlank(query.getMoodScreRange())) {
            String[] parts = query.getMoodScreRange().split("-");
            if (parts.length == 2) {
                try {
                    wrapper.between(EmotionDiary::getMoodScore, Integer.parseInt(parts[0].trim()), Integer.parseInt(parts[1].trim()));
                } catch (NumberFormatException ignored) {
                }
            }
        }
        wrapper.orderByDesc(EmotionDiary::getDiaryDate).orderByDesc(EmotionDiary::getId);

        Page<EmotionDiary> page = emotionDiaryMapper.selectPage(new Page<>(pageNo, pageSize), wrapper);

        // 批量查询用户信息
        List<Long> userIds = page.getRecords().stream().map(EmotionDiary::getUserId).distinct().collect(Collectors.toList());
        Map<Long, User> userMap = userIds.isEmpty() ? Map.of()
                : userMapper.selectBatchIds(userIds).stream().collect(Collectors.toMap(User::getId, Function.identity()));

        List<EmotionDiaryResponseDTO> records = page.getRecords().stream().map(diary -> {
            EmotionDiaryResponseDTO dto = new EmotionDiaryResponseDTO();
            dto.setId(diary.getId());
            dto.setUserId(diary.getUserId());
            dto.setDiaryDate(diary.getDiaryDate());
            dto.setMoodScore(diary.getMoodScore());
            dto.setSleepQuality(diary.getSleepQuality());
            dto.setStressLevel(diary.getStressLevel());
            dto.setEmotionTriggers(diary.getEmotionTriggers());
            dto.setDiaryContent(diary.getDiaryContent());
            dto.setDominantEmotion(diary.getDominantEmotion());
            dto.setAiEmotionAnalysis(diary.getAiEmotionAnalysis());
            dto.setCreatedAt(diary.getCreatedAt());
            dto.setUpdatedAt(diary.getUpdatedAt());
            User user = userMap.get(diary.getUserId());
            if (user != null) {
                dto.setUsername(user.getUsername());
                dto.setNickname(user.getNickname());
            }
            return dto;
        }).collect(Collectors.toList());

        return new PageResult<>(records, page.getTotal(), page.getCurrent(), page.getSize());
    }

    /**
     * 删除日记
     */
    public void deleteById(Long id) {
        emotionDiaryMapper.deleteById(id);
    }
}
