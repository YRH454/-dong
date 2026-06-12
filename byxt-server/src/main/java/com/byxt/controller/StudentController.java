package com.byxt.controller;

import com.byxt.dao.*;
import com.byxt.model.*;
import com.byxt.util.Encrypt;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    @GetMapping("/topics")
    public Map<String, Object> getTopics(HttpServletRequest req) {
        Map<String, Object> result = new HashMap<>();
        try {
            String userId = (String) req.getAttribute("userId");
            String adminId = (String) req.getAttribute("adminId");
            String keyword = req.getParameter("keyword");
            int page = Integer.parseInt(req.getParameter("page") != null ? req.getParameter("page") : "1");
            int pageSize = Integer.parseInt(req.getParameter("pageSize") != null ? req.getParameter("pageSize") : "10");

            // Check if student has confirmed selection
            TmDao tmDao = new TmDao();
            String c1 = " where xh='" + userId + "' and adminid='" + adminId + "' and status=2";
            if (tmDao.getRecordCount(c1) != 0) {
                result.put("selected", true);
                result.put("code", 0);
                return result;
            }
            // Check how many pending/rejected choices student has
            int choiceCount = tmDao.getRecordCount(" where xh='" + userId + "' and adminid='" + adminId + "' and (status=1 or status=3)");
            result.put("choiceCount", choiceCount);

            // Check zt
            ZtDao ztDao = new ZtDao();
            if (ztDao.query(adminId) == 0) {
                result.put("closed", true);
                result.put("code", 0);
                return result;
            }

            String cond = " where xh='' and adminid='" + adminId + "'";
            if (keyword != null && !keyword.isEmpty()) {
                cond += " and (txm like '%" + keyword + "%' or tm like '%" + keyword + "%')";
            }
            List<Tm> list = tmDao.query(cond, page, pageSize);
            int total = tmDao.getRecordCount(cond);

            result.put("code", 0);
            result.put("list", list);
            result.put("total", total);
            result.put("page", page);
            result.put("pageSize", pageSize);
        } catch (Exception e) {
            result.put("code", 1); result.put("msg", e.getMessage());
        }
        return result;
    }

    @PostMapping("/select-topic")
    public Map<String, Object> selectTopic(@RequestBody Map<String, String> body, HttpServletRequest req) {
        Map<String, Object> result = new HashMap<>();
        try {
            String userId = (String) req.getAttribute("userId");
            String userName = (String) req.getAttribute("userName");
            String adminId = (String) req.getAttribute("adminId");
            int tmid = Integer.parseInt(body.get("tmid"));
            int choiceNo = Integer.parseInt(body.getOrDefault("choice", "1"));
            String reason = body.getOrDefault("reason", "");

            StudentDao sd = new StudentDao();
            Student st = sd.findStudentByIdAdminid(userId, adminId);

            // Use new method with status tracking
            int r = TmDao.xtWithStatus(tmid, userId, st.getXm(), st.getZy(), st.getBj(), reason, choiceNo);
            if (r == 1) { result.put("code", 0); result.put("msg", "已提交志愿"+choiceNo+"，等待教师确认"); }
            else { result.put("code", 1); result.put("msg", "该题目已被选或不可选"); }
        } catch (Exception e) {
            result.put("code", 1); result.put("msg", e.getMessage());
        }
        return result;
    }

    @GetMapping("/teacher-detail")
    public Map<String, Object> getTeacherDetail(@RequestParam String gh, HttpServletRequest req) {
        Map<String, Object> result = new HashMap<>();
        try {
            String adminId = (String) req.getAttribute("adminId");
            Teacher t = new TeacherDao().findTeacherByIdAdminid(gh, adminId);
            if (t != null) { result.put("code", 0); result.put("teacher", t); }
            else { result.put("code", 1); result.put("msg", "未找到该教师"); }
        } catch (Exception e) { result.put("code", 1); result.put("msg", e.getMessage()); }
        return result;
    }

    @GetMapping("/my-result")
    public Map<String, Object> getMyResult(@RequestParam(defaultValue = "") String action,
            @RequestParam(defaultValue = "") String xh, HttpServletRequest req) {
        Map<String, Object> result = new HashMap<>();
        try {
            String userId = (String) req.getAttribute("userId");
            String adminId = (String) req.getAttribute("adminId");

            TmDao tmDao = new TmDao();
            if ("delete".equals(action)) {
                tmDao.qcjl(userId, adminId);
                result.put("msg", "已删除选题记录");
            }

            String cond = " where xh='" + userId + "' and adminid='" + adminId + "'";
            List<TmEx> list = tmDao.query(cond, adminId);

            ZtDao ztDao = new ZtDao();
            result.put("code", 0);
            result.put("list", list);
            result.put("canDelete", ztDao.query(adminId) != 0);
        } catch (Exception e) {
            result.put("code", 1); result.put("msg", e.getMessage());
        }
        return result;
    }

    @GetMapping("/profile")
    public Map<String, Object> getProfile(HttpServletRequest req) {
        Map<String, Object> result = new HashMap<>();
        try {
            String userId = (String) req.getAttribute("userId");
            String adminId = (String) req.getAttribute("adminId");
            StudentDao sd = new StudentDao();
            Student st = sd.findStudentByIdAdminid(userId, adminId);
            result.put("code", 0);
            result.put("profile", st);
        } catch (Exception e) {
            result.put("code", 1); result.put("msg", e.getMessage());
        }
        return result;
    }

    @PostMapping("/update-profile")
    public Map<String, Object> updateProfile(@RequestBody Map<String, String> body, HttpServletRequest req) {
        Map<String, Object> result = new HashMap<>();
        try {
            String userId = (String) req.getAttribute("userId");
            String adminId = (String) req.getAttribute("adminId");
            Student s = new Student();
            s.setXh(userId);
            s.setXm(body.get("xm"));
            s.setZy(body.get("zy"));
            s.setBj(body.get("bj"));
            s.setEmail(body.get("email"));
            s.setPhone(body.get("phone"));
            s.setQq(body.get("qq"));
            s.setAdminid(adminId);
            new StudentDao().update(s);
            new TmDao().update2(userId, body.get("xm"), body.get("zy"), body.get("bj"), adminId);
            result.put("code", 0); result.put("msg", "保存成功");
        } catch (Exception e) {
            result.put("code", 1); result.put("msg", e.getMessage());
        }
        return result;
    }

    @PostMapping("/change-password")
    public Map<String, Object> changePwd(@RequestBody Map<String, String> body, HttpServletRequest req) {
        Map<String, Object> result = new HashMap<>();
        try {
            String userId = (String) req.getAttribute("userId");
            String adminId = (String) req.getAttribute("adminId");
            new StudentDao().modipwd(userId, Encrypt.MD5(body.get("newPwd")), adminId);
            result.put("code", 0); result.put("msg", "密码修改成功");
        } catch (Exception e) {
            result.put("code", 1); result.put("msg", e.getMessage());
        }
        return result;
    }
}
