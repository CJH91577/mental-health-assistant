package org.example.aispingboot.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 知识分类简单信息（category/tree 实际返回扁平列表）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategorySimpleDTO {
    private Long id;
    private String categoryName;
    private Long parentId;
    private Integer sortOrder;
}
