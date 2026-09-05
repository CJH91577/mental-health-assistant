package org.example.aispingboot.DTO.query;

import lombok.Data;

/**
 * 知识文章分页查询参数
 */
@Data
public class ArticleListQueryDTO {
    private Integer currentPage;
    private Integer size;
    private String title;
    private Long categoryId;
    /** 状态筛选：1-已发布 2-已下线 0/空-全部（公开列表默认只看已发布） */
    private Integer status;
    /** 排序字段：publishedAt / readCount */
    private String sortField;
    /** 排序方向：asc / desc */
    private String sortDirection;
}
