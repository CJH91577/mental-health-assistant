package org.example.aispingboot.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.aispingboot.DTO.command.ArticleCreateDTO;
import org.example.aispingboot.DTO.query.ArticleListQueryDTO;
import org.example.aispingboot.DTO.response.ArticleResponseDTO;
import org.example.aispingboot.DTO.response.CategorySimpleDTO;
import org.example.aispingboot.common.PageResult;
import org.example.aispingboot.entity.KnowledgeArticle;
import org.example.aispingboot.entity.KnowledgeCategory;
import org.example.aispingboot.entity.User;
import org.example.aispingboot.exception.BusinessException;
import org.example.aispingboot.mapper.KnowledgeArticleMapper;
import org.example.aispingboot.mapper.KnowledgeCategoryMapper;
import org.example.aispingboot.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 知识库服务（文章 + 分类）
 */
@Service
public class KnowledgeService {

    @Autowired
    private KnowledgeArticleMapper articleMapper;

    @Autowired
    private KnowledgeCategoryMapper categoryMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * 分类列表（前端按扁平列表消费）
     */
    public List<CategorySimpleDTO> categoryTree() {
        LambdaQueryWrapper<KnowledgeCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(KnowledgeCategory::getStatus, 1)
                .orderByAsc(KnowledgeCategory::getSortOrder)
                .orderByAsc(KnowledgeCategory::getId);
        return categoryMapper.selectList(wrapper).stream()
                .map(c -> new CategorySimpleDTO(c.getId(), c.getCategoryName(), c.getParentId(), c.getSortOrder()))
                .collect(Collectors.toList());
    }

    /**
     * 文章分页列表。status: 1-仅已发布 2-仅已下线 0-全部；未传时默认仅已发布（面向公开列表）
     */
    public PageResult<ArticleResponseDTO> articlePage(ArticleListQueryDTO query) {
        long pageNo = query.getCurrentPage() == null ? 1 : query.getCurrentPage();
        long pageSize = query.getSize() == null ? 10 : query.getSize();

        LambdaQueryWrapper<KnowledgeArticle> wrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(query.getTitle())) {
            wrapper.like(KnowledgeArticle::getTitle, query.getTitle());
        }
        if (query.getCategoryId() != null) {
            wrapper.eq(KnowledgeArticle::getCategoryId, query.getCategoryId());
        }
        if (query.getStatus() != null && query.getStatus() == 1) {
            wrapper.eq(KnowledgeArticle::getStatus, 1);
        } else if (query.getStatus() != null && query.getStatus() == 2) {
            wrapper.eq(KnowledgeArticle::getStatus, 2);
        } else if (query.getStatus() == null || query.getStatus() == 0) {
            // 未指定时仅返回已发布文章（公开列表），管理端传 0 表示全部
            if (query.getStatus() == null) {
                wrapper.eq(KnowledgeArticle::getStatus, 1);
            }
        }

        // 排序
        boolean asc = "asc".equalsIgnoreCase(query.getSortDirection());
        if ("readCount".equals(query.getSortField())) {
            wrapper.orderBy(true, asc, KnowledgeArticle::getReadCount);
        } else if ("publishedAt".equals(query.getSortField())) {
            wrapper.orderBy(true, asc, KnowledgeArticle::getPublishedAt);
        } else {
            wrapper.orderByDesc(KnowledgeArticle::getPublishedAt);
        }
        wrapper.orderByDesc(KnowledgeArticle::getId);

        Page<KnowledgeArticle> page = articleMapper.selectPage(new Page<>(pageNo, pageSize), wrapper);

        Map<Long, String> categoryNameMap = categoryMapper.selectList(null).stream()
                .collect(Collectors.toMap(KnowledgeCategory::getId, KnowledgeCategory::getCategoryName, (a, b) -> a));
        List<Long> authorIds = page.getRecords().stream().map(KnowledgeArticle::getAuthorId)
                .filter(java.util.Objects::nonNull).distinct().collect(Collectors.toList());
        Map<Long, User> userMap = authorIds.isEmpty() ? Map.of()
                : userMapper.selectBatchIds(authorIds).stream().collect(Collectors.toMap(User::getId, Function.identity()));

        List<ArticleResponseDTO> records = page.getRecords().stream()
                .map(a -> toResponse(a, categoryNameMap, userMap))
                .collect(Collectors.toList());

        return new PageResult<>(records, page.getTotal(), page.getCurrent(), page.getSize());
    }

    /**
     * 文章详情（阅读数 +1）
     */
    public ArticleResponseDTO articleDetail(String id) {
        KnowledgeArticle article = articleMapper.selectById(id);
        if (article == null) {
            throw new BusinessException("文章不存在");
        }
        // 阅读数 +1
        UpdateWrapper<KnowledgeArticle> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id).setSql("read_count = read_count + 1");
        articleMapper.update(null, updateWrapper);
        article.setReadCount((article.getReadCount() == null ? 0 : article.getReadCount()) + 1);

        Map<Long, String> categoryNameMap = categoryMapper.selectList(null).stream()
                .collect(Collectors.toMap(KnowledgeCategory::getId, KnowledgeCategory::getCategoryName, (a, b) -> a));
        Map<Long, User> userMap = article.getAuthorId() == null ? Map.of()
                : userMapper.selectBatchIds(Collections.singletonList(article.getAuthorId())).stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));
        return toResponse(article, categoryNameMap, userMap);
    }

    /**
     * 创建文章
     */
    public ArticleResponseDTO createArticle(Long userId, ArticleCreateDTO dto) {
        KnowledgeCategory category = categoryMapper.selectById(dto.getCategoryId());
        if (category == null) {
            throw new BusinessException("文章分类不存在");
        }
        LocalDateTime now = LocalDateTime.now();
        String id = StrUtil.isNotBlank(dto.getId()) ? dto.getId() : UUID.randomUUID().toString();
        int status = dto.getStatus() == null ? 1 : dto.getStatus();
        KnowledgeArticle article = KnowledgeArticle.builder()
                .id(id)
                .categoryId(dto.getCategoryId())
                .title(dto.getTitle())
                .summary(dto.getSummary())
                .content(dto.getContent())
                .coverImage(dto.getCoverImage())
                .tags(dto.getTags())
                .authorId(userId)
                .readCount(0)
                .status(status)
                .publishedAt(status == 1 ? now : null)
                .createdAt(now)
                .updatedAt(now)
                .build();
        articleMapper.insert(article);
        return articleDetail(id);
    }

    /**
     * 更新文章
     */
    public void updateArticle(String id, ArticleCreateDTO dto) {
        KnowledgeArticle article = articleMapper.selectById(id);
        if (article == null) {
            throw new BusinessException("文章不存在");
        }
        article.setCategoryId(dto.getCategoryId());
        article.setTitle(dto.getTitle());
        article.setSummary(dto.getSummary());
        article.setContent(dto.getContent());
        article.setCoverImage(dto.getCoverImage());
        article.setTags(dto.getTags());
        article.setUpdatedAt(LocalDateTime.now());
        articleMapper.updateById(article);
    }

    /**
     * 更新文章状态（1 发布 / 2 下线）
     */
    public void changeStatus(String id, Integer status) {
        if (status == null || (status != 1 && status != 2)) {
            throw new BusinessException("无效的文章状态");
        }
        KnowledgeArticle article = articleMapper.selectById(id);
        if (article == null) {
            throw new BusinessException("文章不存在");
        }
        article.setStatus(status);
        article.setPublishedAt(status == 1 ? LocalDateTime.now() : article.getPublishedAt());
        article.setUpdatedAt(LocalDateTime.now());
        articleMapper.updateById(article);
    }

    /**
     * 删除文章
     */
    public void deleteArticle(String id) {
        articleMapper.deleteById(id);
    }

    private ArticleResponseDTO toResponse(KnowledgeArticle a, Map<Long, String> categoryNameMap, Map<Long, User> userMap) {
        ArticleResponseDTO dto = new ArticleResponseDTO();
        dto.setId(a.getId());
        dto.setCategoryId(a.getCategoryId());
        dto.setCategoryName(categoryNameMap.getOrDefault(a.getCategoryId(), "-"));
        dto.setTitle(a.getTitle());
        dto.setSummary(a.getSummary());
        dto.setContent(a.getContent());
        dto.setCoverImage(a.getCoverImage());
        dto.setTags(a.getTags());
        dto.setTagArray(StrUtil.isNotBlank(a.getTags())
                ? Arrays.stream(a.getTags().split(",")).map(String::trim).filter(StrUtil::isNotBlank).collect(Collectors.toList())
                : Collections.emptyList());
        dto.setAuthorId(a.getAuthorId());
        User author = userMap.get(a.getAuthorId());
        dto.setAuthorName(author != null ? (StrUtil.isNotBlank(author.getNickname()) ? author.getNickname() : author.getUsername()) : "管理员");
        dto.setReadCount(a.getReadCount() == null ? 0 : a.getReadCount());
        dto.setStatus(a.getStatus());
        dto.setPublishedAt(a.getPublishedAt());
        dto.setCreatedAt(a.getCreatedAt());
        dto.setUpdatedAt(a.getUpdatedAt());
        return dto;
    }
}
