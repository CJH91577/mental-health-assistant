package org.example.aispingboot.DTO.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 知识文章响应
 */
@Data
public class ArticleResponseDTO {
    private String id;
    private Long categoryId;
    private String categoryName;
    private String title;
    private String summary;
    private String content;
    private String coverImage;
    private String tags;
    private List<String> tagArray;
    private Long authorId;
    private String authorName;
    private Integer readCount;
    /** 状态 1:已发布 2:已下线 */
    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}
