package org.example.aispingboot.DTO.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 创建/更新知识文章命令
 */
@Data
public class ArticleCreateDTO {
    /** 文章ID(UUID)，前端创建时自带 */
    private String id;

    @NotNull(message = "文章分类不能为空")
    private Long categoryId;

    @NotBlank(message = "文章标题不能为空")
    @Size(max = 200, message = "文章标题长度不能超过200个字符")
    private String title;

    private String summary;

    private String content;

    /** 封面图片相对路径 */
    @Size(max = 500, message = "封面图片路径长度不能超过500个字符")
    private String coverImage;

    /** 标签（逗号分隔字符串） */
    @Size(max = 500, message = "标签长度不能超过500个字符")
    private String tags;

    /** 状态 1:已发布 2:已下线 */
    private Integer status;
}
