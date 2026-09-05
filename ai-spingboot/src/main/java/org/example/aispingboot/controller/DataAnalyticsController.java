package org.example.aispingboot.controller;

import org.example.aispingboot.DTO.response.AnalyticsOverviewDTO;
import org.example.aispingboot.common.Result;
import org.example.aispingboot.common.ResultCode;
import org.example.aispingboot.service.DataAnalyticsService;
import org.example.aispingboot.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 数据分析接口（管理端看板）
 */
@RestController
@RequestMapping("/api/data-analytics")
public class DataAnalyticsController {

    @Autowired
    private DataAnalyticsService dataAnalyticsService;

    @GetMapping("/overview")
    public Result<AnalyticsOverviewDTO> overview() {
        if (!JwtTokenUtil.isCurrentUserAdmin()) {
            return Result.error(ResultCode.AUTHORIZED_ERROR.getCode(), "无权限访问", null);
        }
        return Result.ok(dataAnalyticsService.overview());
    }
}
