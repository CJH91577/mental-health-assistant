package org.example.aispingboot.controller;

import org.example.aispingboot.DTO.response.FileUploadResponseDTO;
import org.example.aispingboot.common.Result;
import org.example.aispingboot.service.FileService;
import org.example.aispingboot.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传接口
 */
@RestController
@RequestMapping("/api/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public Result<FileUploadResponseDTO> upload(@RequestParam("file") MultipartFile file,
                                                @RequestParam(value = "businessType", required = false) String businessType,
                                                @RequestParam(value = "businessId", required = false) String businessId,
                                                @RequestParam(value = "businessField", required = false) String businessField) {
        Long userId = JwtTokenUtil.getCurrentUserId();
        return Result.ok(fileService.upload(file, businessType, businessId, businessField, userId));
    }
}
