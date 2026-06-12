package com.byxt.controller;

import com.byxt.dao.*;
import com.byxt.model.*;
import com.byxt.util.Encrypt;
import com.byxt.util.JdbcUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import java.sql.*;
import java.util.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @GetMapping("/stats")
    public Map<String, Object> getStats(HttpServletRequest req) {
        Map<String, Object> result = new HashMap<>();
        String adminId = (String) req.getAttribute("adminId");
        try (Connection con = JdbcUtil.getConnection()) {
            String cond = " where adminid='" + adminId + "'";
            // Student count
            PreparedStatement ps = con.prepareStatement("select count(*) from student" + cond);
            ResultSet rs = ps.executeQuery(); rs.next(); int studentCnt = rs.getInt(1); rs.close(); ps.close();
            // Teacher count
            ps = con.prepareStatement("select count(*) from teacher" + cond);
            rs = ps.executeQuery(); rs.next(); int teacherCnt = rs.getInt(1); rs.close(); ps.close();
            // Topic count
            ps = con.prepareStatement("select count(*) from tm" + cond);
            rs = ps.executeQuery(); rs.next(); int topicCnt = rs.getInt(1); rs.close(); ps.close();
            // Selected count
            ps = con.prepareStatement("select count(*) from tm" + cond + " and xh!=''");
            rs = ps.executeQuery(); rs.next(); int selectedCnt = rs.getInt(1); rs.close(); ps.close();
            // ZT
            int zt = new ZtDao().query(adminId);

            result.put("code", 0);
            result.put("studentCount", studentCnt);
            result.put("teacherCount", teacherCnt);
            result.put("topicCount", topicCnt);
            result.put("selectedCount", selectedCnt);
            result.put("zt", zt);
        } catch (Exception e) {
            result.put("code", 1); result.put("msg", e.getMessage());
        }
        return result;
    }

    @PostMapping("/toggle-zt")
    public Map<String, Object> toggleZt(@RequestBody Map<String, String> body, HttpServletRequest req) {
        Map<String, Object> result = new HashMap<>();
        try {
            String adminId = (String) req.getAttribute("adminId");
            int zt = Integer.parseInt(body.get("zt"));
            new ZtDao().update(zt, adminId);
            result.put("code", 0); result.put("msg", "状态已更新");
        } catch (Exception e) {
            result.put("code", 1); result.put("msg", e.getMessage());
        }
        return result;
    }

    @GetMapping("/unselected-students")
    public Map<String, Object> getUnselected(HttpServletRequest req) {
        Map<String, Object> result = new HashMap<>();
        try {
            String adminId = (String) req.getAttribute("adminId");
            List<Student> list = new StudentDao().wxt(adminId);
            result.put("code", 0); result.put("list", list);
        } catch (Exception e) {
            result.put("code", 1); result.put("msg", e.getMessage());
        }
        return result;
    }

    @GetMapping("/teacher-list")
    public Map<String, Object> getTeacherList(HttpServletRequest req) {
        Map<String, Object> result = new HashMap<>();
        try {
            String adminId = (String) req.getAttribute("adminId");
            String cond = " where adminid='" + adminId + "'";
            List<com.byxt.model.Teacher> list = new TeacherDao().query(cond, 1, 9999);
            result.put("code", 0); result.put("list", list);
        } catch (Exception e) {
            result.put("code", 1); result.put("msg", e.getMessage());
        }
        return result;
    }

    @GetMapping("/student-list")
    public Map<String, Object> getStudentList(HttpServletRequest req) {
        Map<String, Object> result = new HashMap<>();
        try {
            String adminId = (String) req.getAttribute("adminId");
            String cond = " where adminid='" + adminId + "'";
            List<Student> list = new StudentDao().query(cond, 1, 9999);
            result.put("code", 0); result.put("list", list);
        } catch (Exception e) {
            result.put("code", 1); result.put("msg", e.getMessage());
        }
        return result;
    }

    @GetMapping("/topic-list")
    public Map<String, Object> getTopicList(HttpServletRequest req) {
        Map<String, Object> result = new HashMap<>();
        try {
            String adminId = (String) req.getAttribute("adminId");
            String cond = " where adminid='" + adminId + "' order by gh";
            List<Tm> list = new TmDao().querytm(cond);
            result.put("code", 0); result.put("list", list);
        } catch (Exception e) {
            result.put("code", 1); result.put("msg", e.getMessage());
        }
        return result;
    }

    @PostMapping("/delete-topic")
    public Map<String, Object> deleteTopic(@RequestBody Map<String, String> body) {
        Map<String, Object> result = new HashMap<>();
        try {
            new TmDao().delete(Integer.parseInt(body.get("id")));
            result.put("code", 0); result.put("msg", "删除成功");
        } catch (Exception e) {
            result.put("code", 1); result.put("msg", e.getMessage());
        }
        return result;
    }

    @PostMapping("/clear-topics")
    public Map<String, Object> clearTopics(@RequestBody Map<String, String> body, HttpServletRequest req) {
        Map<String, Object> result = new HashMap<>();
        try {
            String adminId = (String) req.getAttribute("adminId");
            new TmDao().qingkong(adminId);
            result.put("code", 0); result.put("msg", "已清空");
        } catch (Exception e) {
            result.put("code", 1); result.put("msg", e.getMessage());
        }
        return result;
    }

    @PostMapping("/delete-teacher")
    public Map<String, Object> deleteTeacher(@RequestBody Map<String, String> body) {
        Map<String, Object> result = new HashMap<>();
        try {
            new TeacherDao().delete(Integer.parseInt(body.get("id")));
            result.put("code", 0); result.put("msg", "删除成功");
        } catch (Exception e) {
            result.put("code", 1); result.put("msg", e.getMessage());
        }
        return result;
    }

    @PostMapping("/reset-teacher-pwd")
    public Map<String, Object> resetTeacherPwd(@RequestBody Map<String, String> body, HttpServletRequest req) {
        Map<String, Object> result = new HashMap<>();
        try {
            String adminId = (String) req.getAttribute("adminId");
            String gh = body.get("gh");
            new TeacherDao().modipwd(gh, Encrypt.MD5(gh), adminId);
            result.put("code", 0); result.put("msg", "密码已重置为工号");
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
            new UserDao().modipwd(userId, Encrypt.MD5(body.get("newPwd")));
            result.put("code", 0); result.put("msg", "密码修改成功");
        } catch (Exception e) {
            result.put("code", 1); result.put("msg", e.getMessage());
        }
        return result;
    }

    @PostMapping("/update-department")
    public Map<String, Object> updateDp(@RequestBody Map<String, String> body, HttpServletRequest req) {
        Map<String, Object> result = new HashMap<>();
        try {
            String userId = (String) req.getAttribute("userId");
            String dp = body.get("dp");
            UserDao ud = new UserDao();
            User u = ud.findUserByDp(dp);
            if (u == null || u.getUserid().equals(userId)) {
                ud.modidp(userId, dp);
                result.put("code", 0); result.put("msg", "部门名称已更新");
            } else {
                result.put("code", 1); result.put("msg", "该部门名称已存在");
            }
        } catch (Exception e) {
            result.put("code", 1); result.put("msg", e.getMessage());
        }
        return result;
    }
}
