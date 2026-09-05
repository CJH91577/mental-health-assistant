package org.example.aispingboot.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import org.example.aispingboot.DTO.response.FileUploadResponseDTO;
import org.example.aispingboot.entity.SysFileInfo;
import org.example.aispingboot.exception.BusinessException;
import org.example.aispingboot.mapper.SysFileInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Set;

/**
 * 文件服务（上传到本地 uploads 目录，通过 /files/** 静态映射访问）
 */
@Service
public class FileService {

    /** 允许的图片扩展名（防止脚本等恶意文件上传） */
    private static final Set<String> ALLOWED_EXT = Set.of("jpg", "jpeg", "png", "gif", "webp");

    @Autowired
    private SysFileInfoMapper sysFileInfoMapper;

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    public FileUploadResponseDTO upload(MultipartFile file, String businessType, String businessId,
                                        String businessField, Long userId) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("上传文件不能为空");
        }
        String originalName = StrUtil.blankToDefault(file.getOriginalFilename(), "file");
        String ext = "";
        int dot = originalName.lastIndexOf('.');
        if (dot >= 0) {
            ext = originalName.substring(dot + 1).toLowerCase(Locale.ROOT);
        }
        if (!ALLOWED_EXT.contains(ext)) {
            throw new BusinessException("不支持的文件类型，仅支持图片（jpg/jpeg/png/gif/webp）");
        }

        String bizType = StrUtil.blankToDefault(businessType, "common").toLowerCase(Locale.ROOT);
        String fileName = System.currentTimeMillis() + "_" + IdUtil.fastSimpleUUID().substring(0, 8) + "." + ext;
        String relativePath = "/files/bussiness/" + bizType + "/" + fileName;

        try {
            Path dir = Paths.get(uploadDir).toAbsolutePath().normalize().resolve("bussiness").resolve(bizType);
            Files.createDirectories(dir);
            Path target = dir.resolve(fileName);
            file.transferTo(target.toFile());
        } catch (IOException e) {
            throw new BusinessException("文件保存失败：" + e.getMessage());
        }

        SysFileInfo info = SysFileInfo.builder()
                .originalName(originalName)
                .filePath(relativePath)
                .fileSize(file.getSize())
                .fileType("IMG")
                .businessType(StrUtil.blankToDefault(businessType, "COMMON").toUpperCase(Locale.ROOT))
                .businessId(StrUtil.blankToDefault(businessId, "null"))
                .businessField(businessField)
                .uploadUserId(userId)
                .isTemp(0)
                .status(1)
                .createTime(LocalDateTime.now())
                .build();
        sysFileInfoMapper.insert(info);

        return new FileUploadResponseDTO(info.getId(), originalName, relativePath, info.getFileSize(), "IMG");
    }
}
