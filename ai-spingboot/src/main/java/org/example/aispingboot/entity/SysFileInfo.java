package org.example.aispingboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 系统文件信息实体类
 */
@Data
@Builder
@TableName("sys_file_info")
public class SysFileInfo {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("original_name")
    private String originalName;

    @TableField("file_path")
    private String filePath;

    @TableField("file_size")
    private Long fileSize;

    @TableField("file_type")
    private String fileType;

    @TableField("business_type")
    private String businessType;

    @TableField("business_id")
    private String businessId;

    @TableField("business_field")
    private String businessField;

    @TableField("upload_user_id")
    private Long uploadUserId;

    @TableField("is_temp")
    private Integer isTemp;

    private Integer status;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("expire_time")
    private LocalDateTime expireTime;
}
