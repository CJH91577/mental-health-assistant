package org.example.aispingboot.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文件上传响应
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadResponseDTO {
    private Long id;
    private String originalName;
    /** 相对访问路径，如 /files/bussiness/article/xxx.png（前端自行拼接 fileBaseUrl） */
    private String filePath;
    private Long fileSize;
    private String fileType;
}
