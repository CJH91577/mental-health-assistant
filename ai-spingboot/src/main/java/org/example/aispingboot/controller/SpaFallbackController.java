package org.example.aispingboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * SPA 前端路由回退：Vue Router 使用 history 模式，
 * 浏览器直接访问深层路径（如 /back/dashboard）时回退到 index.html。
 */
@Controller
public class SpaFallbackController {

    @GetMapping({"/", "/back", "/back/**", "/auth", "/auth/**",
            "/consultation", "/emotion-diary", "/knowledge", "/knowledge/**"})
    public String index() {
        return "forward:/index.html";
    }
}
