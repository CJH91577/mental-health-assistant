package org.example.aispingboot.DTO.query;

import lombok.Data;

/**
 * 咨询会话列表查询参数（兼容前端 pageNum/pageSize 与 currentPage/size 两种命名）
 */
@Data
public class ConsultationSessionQueryDTO {
    /** 页码（用户端用 pageNum/currentPage，管理端用 currentPage） */
    private Integer pageNum;
    private Integer currentPage;
    private Integer current;
    /** 每页条数 */
    private Integer pageSize;
    private Integer size;
}
