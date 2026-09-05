package org.example.aispingboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 知识文章实体类
 */
@Data
@Builder
@TableName("knowledge_article")
public class KnowledgeArticle {
    /** 文章ID(UUID) */
    @TableId(type = IdType.INPUT)
    private String id;

    @TableField("category_id")
    private Long categoryId;

    private String title;

    private String summary;

    private String content;

    @TableField("cover_image")
    private String coverImage;

    private String tags;

    @TableField("author_id")
    private Long authorId;

    @TableField("read_count")
    private Integer readCount;

    /** 状态 1:已发布 0:已下线 */
    private Integer status;

    @TableField("published_at")
    private LocalDateTime publishedAt;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;
}
