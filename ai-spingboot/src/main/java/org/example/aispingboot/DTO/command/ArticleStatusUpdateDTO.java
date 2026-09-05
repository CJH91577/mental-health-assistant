package org.example.aispingboot.DTO.command;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 文章状态更新命令
 */
@Data
public class ArticleStatusUpdateDTO {
    @NotNull(message = "状态不能为空")
    private Integer status;
}
