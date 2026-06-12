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
        // Simple token check: format "role:userid:adminid"
        String[] parts = token.split(":");
        if (parts.length < 2) {
            response.setStatus(401);
            response.getWriter().write("{\"code\":401,\"msg\":\"无效的凭证\"}");
            return false;
        }
        String url = request.getRequestURI();
        String role = parts[0];
        if (url.contains("/api/" + role + "/")) {
            request.setAttribute("userId", parts[1]);
            request.setAttribute("adminId", parts.length > 2 ? parts[2] : "");
            request.setAttribute("role", role);
            if (parts.length > 3) request.setAttribute("userName", parts[3]);
            if (parts.length > 4) request.setAttribute("dp", parts[4]);
            return true;
        }
        response.setStatus(403);
        response.getWriter().write("{\"code\":403,\"msg\":\"权限不足\"}");
        return false;
    }
}
