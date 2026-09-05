package org.example.aispingboot.controller;

import jakarta.validation.Valid;
import org.example.aispingboot.DTO.command.EmotionDiaryCreateDTO;
import org.example.aispingboot.DTO.query.EmotionDiaryQueryDTO;
import org.example.aispingboot.DTO.response.EmotionDiaryResponseDTO;
import org.example.aispingboot.common.PageResult;
import org.example.aispingboot.common.Result;
import org.example.aispingboot.common.ResultCode;
import org.example.aispingboot.service.EmotionDiaryService;
import org.example.aispingboot.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 情绪日记接口
 */
@RestController
@RequestMapping("/api/emotion-diary")
public class EmotionDiaryController {

    @Autowired
    private EmotionDiaryService emotionDiaryService;

    /**
     * 添加情绪日记（当前登录用户）
     */
    @PostMapping
    public Result<Void> add(@Valid @RequestBody EmotionDiaryCreateDTO dto) {
        Long userId = JwtTokenUtil.getCurrentUserId();
        emotionDiaryService.addDiary(userId, dto);
        return Result.ok();
    }

    /**
     * 后台分页查询（仅管理员）
     */
    @GetMapping("/admin/page")
    public Result<PageResult<EmotionDiaryResponseDTO>> adminPage(EmotionDiaryQueryDTO query) {
        if (!JwtTokenUtil.isCurrentUserAdmin()) {
            return Result.error(ResultCode.AUTHORIZED_ERROR.getCode(), "无权限访问", null);
        }
        return Result.ok(emotionDiaryService.getAdminPage(query));
    }

    /**
     * 后台删除（仅管理员）
     */
    @DeleteMapping("/admin/{id}")
    public Result<Void> adminDelete(@PathVariable Long id) {
        if (!JwtTokenUtil.isCurrentUserAdmin()) {
            return Result.error(ResultCode.AUTHORIZED_ERROR.getCode(), "无权限访问", null);
        }
        emotionDiaryService.deleteById(id);
        return Result.ok();
    }
}
