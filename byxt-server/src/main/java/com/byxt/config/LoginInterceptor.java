package com.byxt.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(401);
            response.getWriter().write("{\"code\":401,\"msg\":\"未登录或会话已过期\"}");
            return false;
        }
        String[] parts = token.split(":");
        if (parts.length < 2) {
            response.setStatus(401);
            response.getWriter().write("{\"code\":401,\"msg\":\"无效的凭证\"}");
            return false;
        }
        String url = request.getRequestURI();
        String role = parts[0];
        String userId = parts.length > 1 ? parts[1] : "";
        String adminId = parts.length > 2 ? parts[2] : "";
        String userName = parts.length > 3 ? parts[3] : "";
        String dp = parts.length > 4 ? parts[4] : "";

        // Allow access if URL matches the user's role
        boolean allowed = url.contains("/api/" + role + "/");
        // Root (super admin) can access all admin APIs
        if (!allowed && "root".equals(role) && (url.contains("/api/admin/") || url.contains("/api/teacher/") || url.contains("/api/student/"))) {
            allowed = true;
        }
        // Public endpoints
        if (!allowed && (url.contains("/api/departments") || url.contains("/api/announcements"))) {
            allowed = true;
        }

        if (!allowed) {
            response.setStatus(403);
            response.getWriter().write("{\"code\":403,\"msg\":\"权限不足\"}");
            return false;
        }

        request.setAttribute("userId", userId);
        request.setAttribute("adminId", adminId);
        request.setAttribute("role", role);
        request.setAttribute("userName", userName);
        request.setAttribute("dp", dp);
        return true;
    }
}
