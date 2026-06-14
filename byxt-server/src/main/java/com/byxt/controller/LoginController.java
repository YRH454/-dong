package com.byxt.controller;

import com.byxt.dao.*;
import com.byxt.model.*;
import com.byxt.util.Encrypt;
import com.byxt.util.JdbcUtil;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api")
public class LoginController {

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> body) {
        Map<String, Object> result = new HashMap<>();
        String userid = body.get("userid"); String userpwd = body.get("userpwd");
        String sf = body.get("sf"); String dp = body.getOrDefault("dp", "");
        try {
            if ("student".equals(sf)) {
                StudentDao dao = new StudentDao();
                String adminid = dp.contains(",") ? dp.split(",")[0] : dp;
                Student s = dao.findStudentByIdAdminid(userid, adminid);
                if (s == null) { result.put("code", 1); result.put("msg", "User not found"); return result; }
                if (!s.getPwd().equals(Encrypt.MD5(userpwd))) { result.put("code", 1); result.put("msg", "Wrong password"); return result; }
                result.put("code", 0);
                result.put("token", "student:" + s.getXh() + ":" + adminid + ":" + s.getXm() + ":" + (dp.contains(",") ? dp.split(",")[1] : ""));
                result.put("role", "student"); result.put("name", s.getXm()); result.put("userId", s.getXh());
                result.put("dp", dp.contains(",") ? dp.split(",")[1] : ""); result.put("adminId", adminid);
            } else if ("teacher".equals(sf)) {
                TeacherDao dao = new TeacherDao();
                String adminid = dp.contains(",") ? dp.split(",")[0] : dp;
                Teacher t = dao.findTeacherByIdAdminid(userid, adminid);
                if (t == null) { result.put("code", 1); result.put("msg", "User not found"); return result; }
                if (!t.getPwd().equals(Encrypt.MD5(userpwd))) { result.put("code", 1); result.put("msg", "Wrong password"); return result; }
                result.put("code", 0);
                result.put("token", "teacher:" + t.getGh() + ":" + adminid + ":" + t.getXm() + ":" + (dp.contains(",") ? dp.split(",")[1] : ""));
                result.put("role", "teacher"); result.put("name", t.getXm()); result.put("userId", t.getGh());
                result.put("dp", dp.contains(",") ? dp.split(",")[1] : ""); result.put("adminId", adminid);
            } else if ("admin".equals(sf)) {
                User u = new UserDao().findUserById(userid);
                if (u == null) { result.put("code", 1); result.put("msg", "User not found"); return result; }
                if (!u.getUserpwd().equals(Encrypt.MD5(userpwd))) { result.put("code", 1); result.put("msg", "Wrong password"); return result; }
                if (!dp.startsWith(u.getUserid())) { result.put("code", 1); result.put("msg", "Wrong department"); return result; }
                result.put("code", 0); result.put("token", "admin:" + u.getUserid() + ":" + u.getUserid() + "::" + u.getDp());
                result.put("role", "admin"); result.put("name", ""); result.put("userId", u.getUserid());
                result.put("dp", u.getDp()); result.put("adminId", u.getUserid());
            } else if ("root".equals(sf)) {
                Root r = new RootDao().findUserById(userid);
                if (r == null) { result.put("code", 1); result.put("msg", "User not found"); return result; }
                if (!r.getRootpwd().equals(Encrypt.MD5(userpwd))) { result.put("code", 1); result.put("msg", "Wrong password"); return result; }
                result.put("code", 0); result.put("token", "root:" + r.getRootid() + ":::"); result.put("role", "root");
                result.put("name", ""); result.put("userId", r.getRootid());
            } else { result.put("code", 1); result.put("msg", "Invalid role"); }
        } catch (Exception e) { result.put("code", 1); result.put("msg", "Login error: " + e.getMessage()); }
        return result;
    }

    @GetMapping("/departments")
    public List<Map<String, String>> getDepartments() {
        List<Map<String, String>> list = new ArrayList<>();
        try { for (User u : new UserDao().query()) { Map<String, String> m = new HashMap<>(); m.put("value", u.getUserid() + "," + u.getDp()); m.put("label", u.getDp()); list.add(m); } }
        catch (Exception ignored) {}
        return list;
    }

    @GetMapping("/announcements/{adminId}")
    public List<Map<String,Object>> getAnnouncements(@PathVariable String adminId) {
        List<Map<String,Object>> list=new ArrayList<>();
        try(java.sql.Connection c=JdbcUtil.getConnection();java.sql.Statement s=c.createStatement()) {
            java.sql.ResultSet rs=s.executeQuery("SELECT id,title,content,create_time FROM announcement WHERE adminid='"+adminId+"' ORDER BY is_top DESC, create_time DESC LIMIT 5");
            while(rs.next()){Map<String,Object> m=new HashMap<>();m.put("id",rs.getInt("id"));m.put("title",rs.getString("title"));m.put("content",rs.getString("content"));m.put("createTime",rs.getString("create_time"));list.add(m);}
            rs.close();
        }catch(Exception ignored){}
        return list;
    }
}
