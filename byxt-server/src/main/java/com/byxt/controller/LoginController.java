package com.byxt.controller;

import com.byxt.dao.*;
import com.byxt.model.*;
import com.byxt.util.Encrypt;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api")
public class LoginController {

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> body) {
        Map<String, Object> result = new HashMap<>();
        String userid = body.get("userid");
        String userpwd = body.get("userpwd");
        String sf = body.get("sf");
        String dp = body.getOrDefault("dp", "");

        try {
            if ("student".equals(sf)) {
                StudentDao dao = new StudentDao();
                String adminid = dp.contains(",") ? dp.split(",")[0] : dp;
                Student s = dao.findStudentByIdAdminid(userid, adminid);
                if (s == null) { result.put("code", 1); result.put("msg", "用户名不存在"); return result; }
                if (!s.getPwd().equals(Encrypt.MD5(userpwd))) { result.put("code", 1); result.put("msg", "密码不正确"); return result; }
                result.put("code", 0);
                result.put("token", "student:" + s.getXh() + ":" + adminid + ":" + s.getXm() + ":" + (dp.contains(",") ? dp.split(",")[1] : ""));
                result.put("role", "student");
                result.put("name", s.getXm());
                result.put("userId", s.getXh());
                result.put("dp", dp.contains(",") ? dp.split(",")[1] : "");
                result.put("adminId", adminid);
            } else if ("teacher".equals(sf)) {
                TeacherDao dao = new TeacherDao();
                String adminid = dp.contains(",") ? dp.split(",")[0] : dp;
                Teacher t = dao.findTeacherByIdAdminid(userid, adminid);
                if (t == null) { result.put("code", 1); result.put("msg", "用户名不存在"); return result; }
                if (!t.getPwd().equals(Encrypt.MD5(userpwd))) { result.put("code", 1); result.put("msg", "密码不正确"); return result; }
                result.put("code", 0);
                result.put("token", "teacher:" + t.getGh() + ":" + adminid + ":" + t.getXm() + ":" + (dp.contains(",") ? dp.split(",")[1] : ""));
                result.put("role", "teacher");
                result.put("name", t.getXm());
                result.put("userId", t.getGh());
                result.put("dp", dp.contains(",") ? dp.split(",")[1] : "");
                result.put("adminId", adminid);
            } else if ("admin".equals(sf)) {
                UserDao dao = new UserDao();
                User u = dao.findUserById(userid);
                if (u == null) { result.put("code", 1); result.put("msg", "用户名不存在"); return result; }
                if (!u.getUserpwd().equals(Encrypt.MD5(userpwd))) { result.put("code", 1); result.put("msg", "密码不正确"); return result; }
                if (!dp.startsWith(u.getUserid())) { result.put("code", 1); result.put("msg", "部门不正确"); return result; }
                result.put("code", 0);
                result.put("token", "admin:" + u.getUserid() + ":" + u.getUserid() + "::" + u.getDp());
                result.put("role", "admin");
                result.put("name", "");
                result.put("userId", u.getUserid());
                result.put("dp", u.getDp());
                result.put("adminId", u.getUserid());
            } else if ("root".equals(sf)) {
                RootDao dao = new RootDao();
                Root r = dao.findUserById(userid);
                if (r == null) { result.put("code", 1); result.put("msg", "用户名不存在"); return result; }
                if (!r.getRootpwd().equals(Encrypt.MD5(userpwd))) { result.put("code", 1); result.put("msg", "密码不正确"); return result; }
                result.put("code", 0);
                result.put("token", "root:" + r.getRootid() + ":::");
                result.put("role", "root");
                result.put("name", "");
                result.put("userId", r.getRootid());
            } else {
                result.put("code", 1); result.put("msg", "无效的角色类型");
            }
        } catch (Exception e) {
            result.put("code", 1); result.put("msg", "登录异常: " + e.getMessage());
        }
        return result;
    }

    @GetMapping("/departments")
    public List<Map<String, String>> getDepartments() {
        List<Map<String, String>> list = new ArrayList<>();
        try {
            List<User> users = new UserDao().query();
            for (User u : users) {
                Map<String, String> m = new HashMap<>();
                m.put("value", u.getUserid() + "," + u.getDp());
                m.put("label", u.getDp());
                list.add(m);
            }
        } catch (Exception ignored) {}
        return list;
    }
}
