package org.example.aispingboot.controller;

import jakarta.validation.Valid;
import org.example.aispingboot.DTO.command.ArticleCreateDTO;
import org.example.aispingboot.DTO.command.ArticleStatusUpdateDTO;
import org.example.aispingboot.DTO.query.ArticleListQueryDTO;
import org.example.aispingboot.DTO.response.ArticleResponseDTO;
import org.example.aispingboot.DTO.response.CategorySimpleDTO;
import org.example.aispingboot.common.PageResult;
import org.example.aispingboot.common.Result;
import org.example.aispingboot.common.ResultCode;
import org.example.aispingboot.service.KnowledgeService;
import org.example.aispingboot.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 知识库接口（文章浏览公开；增删改仅管理员）
 */
@RestController
@RequestMapping("/api/knowledge")
public class KnowledgeController {

    @Autowired
    private KnowledgeService knowledgeService;

    /**
     * 分类列表
     */
    @GetMapping("/category/tree")
    public Result<List<CategorySimpleDTO>> categoryTree() {
        return Result.ok(knowledgeService.categoryTree());
    }

    /**
     * 文章分页（公开浏览：未传 status 时仅返回已发布文章）
     */
    @GetMapping("/article/page")
    public Result<PageResult<ArticleResponseDTO>> articlePage(ArticleListQueryDTO query) {
        return Result.ok(knowledgeService.articlePage(query));
    }

    /**
     * 文章详情（阅读数+1）
     */
    @GetMapping("/article/{id}")
    public Result<ArticleResponseDTO> articleDetail(@PathVariable String id) {
        return Result.ok(knowledgeService.articleDetail(id));
    }

    /**
     * 创建文章（仅管理员）
     */
    @PostMapping("/article")
    public Result<ArticleResponseDTO> createArticle(@Valid @RequestBody ArticleCreateDTO dto) {
        if (!JwtTokenUtil.isCurrentUserAdmin()) {
            return Result.error(ResultCode.AUTHORIZED_ERROR.getCode(), "无权限访问", null);
        }
        Long userId = JwtTokenUtil.getCurrentUserId();
        return Result.ok(knowledgeService.createArticle(userId, dto));
    }

    /**
     * 更新文章（仅管理员）
     */
    @PutMapping("/article/{id}")
    public Result<Void> updateArticle(@PathVariable String id, @Valid @RequestBody ArticleCreateDTO dto) {
        if (!JwtTokenUtil.isCurrentUserAdmin()) {
            return Result.error(ResultCode.AUTHORIZED_ERROR.getCode(), "无权限访问", null);
        }
        knowledgeService.updateArticle(id, dto);
        return Result.ok();
    }

    /**
     * 更新文章状态（仅管理员）
     */
    @PutMapping("/article/{id}/status")
    public Result<Void> changeStatus(@PathVariable String id, @Valid @RequestBody ArticleStatusUpdateDTO dto) {
        if (!JwtTokenUtil.isCurrentUserAdmin()) {
            return Result.error(ResultCode.AUTHORIZED_ERROR.getCode(), "无权限访问", null);
        }
        knowledgeService.changeStatus(id, dto.getStatus());
        return Result.ok();
    }

    /**
     * 删除文章（仅管理员）
     */
    @DeleteMapping("/article/{id}")
    public Result<Void> deleteArticle(@PathVariable String id) {
        if (!JwtTokenUtil.isCurrentUserAdmin()) {
            return Result.error(ResultCode.AUTHORIZED_ERROR.getCode(), "无权限访问", null);
        }
        knowledgeService.deleteArticle(id);
        return Result.ok();
    }
}
