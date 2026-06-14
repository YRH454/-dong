package com.byxt.controller;

import com.byxt.dao.*;
import com.byxt.model.*;
import com.byxt.util.Encrypt;
import com.byxt.util.JdbcUtil;
import com.byxt.util.OpLogUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import java.sql.*;
import java.util.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private boolean isRoot(HttpServletRequest req) { return "root".equals(req.getAttribute("role")); }
    private String adminCond(HttpServletRequest req) {
        if (isRoot(req)) return "";
        return " where adminid='" + req.getAttribute("adminId") + "'";
    }

    @GetMapping("/stats")
    public Map<String, Object> getStats(HttpServletRequest req) {
        Map<String, Object> result = new HashMap<>();
        String adminId = (String) req.getAttribute("adminId");
        boolean root = isRoot(req);
        try (Connection con = JdbcUtil.getConnection()) {
            String cond = root ? "" : " where adminid='" + adminId + "'";
            PreparedStatement ps; ResultSet rs;
            ps = con.prepareStatement("select count(*) from student" + cond); rs = ps.executeQuery(); rs.next(); int studentCnt = rs.getInt(1); rs.close(); ps.close();
            ps = con.prepareStatement("select count(*) from teacher" + cond); rs = ps.executeQuery(); rs.next(); int teacherCnt = rs.getInt(1); rs.close(); ps.close();
            ps = con.prepareStatement("select count(*) from tm" + cond); rs = ps.executeQuery(); rs.next(); int topicCnt = rs.getInt(1); rs.close(); ps.close();
            ps = con.prepareStatement("select count(*) from tm" + (root ? " where xh!=''" : cond + " and xh!=''")); rs = ps.executeQuery(); rs.next(); int selectedCnt = rs.getInt(1); rs.close(); ps.close();

            // For root, aggregate ZT across all departments
            int zt = 0;
            if (root) {
                ps = con.prepareStatement("select count(*) from zt");
                rs = ps.executeQuery(); rs.next(); int ztCount = rs.getInt(1); rs.close(); ps.close();
                ps = con.prepareStatement("select count(*) from zt where zt=1");
                rs = ps.executeQuery(); rs.next(); zt = rs.getInt(1); rs.close(); ps.close();
                if (ztCount > 0 && zt == ztCount) zt = 1; else zt = 0;
            } else {
                zt = new ZtDao().query(adminId);
            }

            List<Map<String,Object>> teacherStats = new ArrayList<>();
            ps = con.prepareStatement("select gh,xm,shangxian,adminid from teacher" + cond + " order by gh");
            rs = ps.executeQuery();
            while(rs.next()){
                Map<String,Object> m = new HashMap<>(); m.put("name",rs.getString(2) + (root ? "("+rs.getString(4)+")" : ""));
                String tAdminId = rs.getString(4);
                ps = con.prepareStatement("select count(*) from tm where gh='"+rs.getString(1)+"' and adminid='"+tAdminId+"'");
                ResultSet rs2 = ps.executeQuery(); rs2.next(); int total = rs2.getInt(1); rs2.close(); ps.close();
                ps = con.prepareStatement("select count(*) from tm where gh='"+rs.getString(1)+"' and xh!='' and adminid='"+tAdminId+"'");
                rs2 = ps.executeQuery(); rs2.next(); int sel = rs2.getInt(1); rs2.close(); ps.close();
                m.put("shangxian",rs.getInt(3)); m.put("total",total); m.put("selected",sel);
                teacherStats.add(m);
            }
            rs.close(); ps.close();
            result.put("code", 0);
            result.put("studentCount", studentCnt); result.put("teacherCount", teacherCnt);
            result.put("topicCount", topicCnt); result.put("selectedCount", selectedCnt);
            result.put("zt", zt); result.put("teacherStats", teacherStats);
            result.put("isRoot", root);
            if (root) {
                ps = con.prepareStatement("select count(*) from user");
                rs = ps.executeQuery(); rs.next(); result.put("deptCount", rs.getInt(1)); rs.close(); ps.close();
            }
        } catch (Exception e) { result.put("code", 1); result.put("msg", e.getMessage()); }
        return result;
    }

    @GetMapping("/export-topics")
    public Map<String, Object> exportTopics(HttpServletRequest req) {
        Map<String, Object> result = new HashMap<>();
        String adminId = (String) req.getAttribute("adminId");
        try {
            String cond = isRoot(req) ? "" : " where adminid='"+adminId+"'";
            result.put("code", 0); result.put("list", new TmDao().query(cond.trim(), adminId));
        } catch (Exception e) { result.put("code", 1); result.put("msg", e.getMessage()); }
        return result;
    }

    @PostMapping("/toggle-zt")
    public Map<String, Object> toggleZt(@RequestBody Map<String, String> body, HttpServletRequest req) {
        Map<String, Object> result = new HashMap<>();
        try {
            int ztVal = Integer.parseInt(body.get("zt"));
            if (isRoot(req)) {
                // Root toggles ALL departments
                try (Connection c = JdbcUtil.getConnection(); Statement s = c.createStatement()) {
                    s.executeUpdate("update zt set zt=" + ztVal);
                }
            } else {
                new ZtDao().update(ztVal, (String)req.getAttribute("adminId"));
            }
            OpLogUtil.log(isRoot(req) ? "system" : (String)req.getAttribute("adminId"),
                (isRoot(req) ? "[全局]" : "") + "管理员切换选题状态为：" + (ztVal == 1 ? "开放" : "关闭"));
            result.put("code", 0); result.put("msg", "Status updated");
        } catch (Exception e) { result.put("code", 1); result.put("msg", e.getMessage()); }
        return result;
    }

    @GetMapping("/unselected-students")
    public Map<String, Object> getUnselected(HttpServletRequest req) {
        Map<String, Object> result = new HashMap<>();
        String adminId = (String) req.getAttribute("adminId");
        try { result.put("code", 0); result.put("list", new StudentDao().wxt(isRoot(req) ? null : adminId)); }
        catch (Exception e) { result.put("code", 1); result.put("msg", e.getMessage()); }
        return result;
    }

    @GetMapping("/teacher-list")
    public Map<String, Object> getTeacherList(HttpServletRequest req) {
        Map<String, Object> result = new HashMap<>();
        String adminId = (String) req.getAttribute("adminId");
        try {
            String cond = isRoot(req) ? "" : " where adminid='" + adminId + "'";
            result.put("code", 0); result.put("list", new TeacherDao().query(cond, 1, 9999));
        } catch (Exception e) { result.put("code", 1); result.put("msg", e.getMessage()); }
        return result;
    }

    @GetMapping("/student-list")
    public Map<String, Object> getStudentList(HttpServletRequest req) {
        Map<String, Object> result = new HashMap<>();
        String adminId = (String) req.getAttribute("adminId");
        try {
            String cond = isRoot(req) ? "" : " where adminid='" + adminId + "'";
            result.put("code", 0); result.put("list", new StudentDao().query(cond, 1, 9999));
        } catch (Exception e) { result.put("code", 1); result.put("msg", e.getMessage()); }
        return result;
    }

    @GetMapping("/topic-list")
    public Map<String, Object> getTopicList(HttpServletRequest req) {
        Map<String, Object> result = new HashMap<>();
        String adminId = (String) req.getAttribute("adminId");
        try {
            String cond = isRoot(req) ? "" : " where adminid='" + adminId + "'";
            result.put("code", 0); result.put("list", new TmDao().querytm(cond));
        } catch (Exception e) { result.put("code", 1); result.put("msg", e.getMessage()); }
        return result;
    }

    @PostMapping("/delete-topic")
    public Map<String, Object> deleteTopic(@RequestBody Map<String, String> body) {
        Map<String, Object> result = new HashMap<>();
        try { new TmDao().delete(Integer.parseInt(body.get("id"))); result.put("code", 0); result.put("msg", "Deleted"); }
        catch (Exception e) { result.put("code", 1); result.put("msg", e.getMessage()); }
        return result;
    }

    @PostMapping("/clear-topics")
    public Map<String, Object> clearTopics(HttpServletRequest req) {
        Map<String, Object> result = new HashMap<>();
        String adminId = (String) req.getAttribute("adminId");
        try {
            if (isRoot(req)) {
                try (Connection c = JdbcUtil.getConnection(); Statement s = c.createStatement()) {
                    s.executeUpdate("delete from tm");
                }
            } else {
                new TmDao().qingkong(adminId);
            }
            result.put("code", 0); result.put("msg", "Cleared");
        } catch (Exception e) { result.put("code", 1); result.put("msg", e.getMessage()); }
        return result;
    }

    @PostMapping("/delete-teacher")
    public Map<String, Object> deleteTeacher(@RequestBody Map<String, String> body) {
        Map<String, Object> result = new HashMap<>();
        try { new TeacherDao().delete(Integer.parseInt(body.get("id"))); result.put("code", 0); result.put("msg", "Deleted"); }
        catch (Exception e) { result.put("code", 1); result.put("msg", e.getMessage()); }
        return result;
    }

    @PostMapping("/reset-teacher-pwd")
    public Map<String, Object> resetTeacherPwd(@RequestBody Map<String, String> body, HttpServletRequest req) {
        Map<String, Object> result = new HashMap<>();
        try { String gh = body.get("gh"); new TeacherDao().modipwd(gh, Encrypt.MD5(gh), (String)req.getAttribute("adminId")); result.put("code", 0); result.put("msg", "Password reset"); }
        catch (Exception e) { result.put("code", 1); result.put("msg", e.getMessage()); }
        return result;
    }

    @PostMapping("/change-password")
    public Map<String, Object> changePwd(@RequestBody Map<String, String> body, HttpServletRequest req) {
        Map<String, Object> result = new HashMap<>();
        try { new UserDao().modipwd((String)req.getAttribute("userId"), Encrypt.MD5(body.get("newPwd"))); result.put("code", 0); result.put("msg", "Password changed"); }
        catch (Exception e) { result.put("code", 1); result.put("msg", e.getMessage()); }
        return result;
    }

    @PostMapping("/update-department")
    public Map<String, Object> updateDp(@RequestBody Map<String, String> body, HttpServletRequest req) {
        Map<String, Object> result = new HashMap<>();
        try {
            String userId = (String) req.getAttribute("userId"); String dp = body.get("dp");
            User u = new UserDao().findUserByDp(dp);
            if (u == null || u.getUserid().equals(userId)) { new UserDao().modidp(userId, dp); result.put("code", 0); result.put("msg", "Updated"); }
            else { result.put("code", 1); result.put("msg", "Department name already exists"); }
        } catch (Exception e) { result.put("code", 1); result.put("msg", e.getMessage()); }
        return result;
    }

    // ===== Announcements =====
    @GetMapping("/announcements")
    public Map<String,Object> getAnnouncements(HttpServletRequest req) {
        Map<String,Object> r=new HashMap<>(); List<Map<String,Object>> list=new ArrayList<>();
        String aid=(String)req.getAttribute("adminId"); boolean root=isRoot(req);
        try(Connection c=JdbcUtil.getConnection();Statement s=c.createStatement()) {
            String sql = root ? "SELECT * FROM announcement ORDER BY is_top DESC, create_time DESC LIMIT 30"
                : "SELECT * FROM announcement WHERE adminid='"+aid+"' ORDER BY is_top DESC, create_time DESC LIMIT 10";
            ResultSet rs=s.executeQuery(sql);
            while(rs.next()){Map<String,Object> m=new HashMap<>();m.put("id",rs.getInt("id"));m.put("title",rs.getString("title"));m.put("content",rs.getString("content"));m.put("createTime",rs.getString("create_time"));m.put("isTop",rs.getInt("is_top"));m.put("adminid",rs.getString("adminid"));list.add(m);}
            rs.close(); r.put("code",0); r.put("list",list);
        }catch(Exception e){r.put("code",1);r.put("msg",e.getMessage());}
        return r;
    }

    @PostMapping("/announcement")
    public Map<String,Object> addAnnouncement(@RequestBody Map<String,String> body, HttpServletRequest req) {
        Map<String,Object> r=new HashMap<>();
        try(Connection c=JdbcUtil.getConnection()) {
            PreparedStatement ps=c.prepareStatement("INSERT INTO announcement(title,content,adminid,is_top) VALUES(?,?,?,?)");
            ps.setString(1,body.get("title"));ps.setString(2,body.get("content"));ps.setString(3,(String)req.getAttribute("adminId"));ps.setInt(4,Integer.parseInt(body.getOrDefault("isTop","0")));
            ps.executeUpdate();ps.close();
            OpLogUtil.log((String)req.getAttribute("adminId"), "管理员发布公告：" + body.get("title"));
            r.put("code",0);r.put("msg","Published");
        }catch(Exception e){r.put("code",1);r.put("msg",e.getMessage());}
        return r;
    }

    @DeleteMapping("/announcement/{id}")
    public Map<String,Object> delAnnouncement(@PathVariable int id) {
        Map<String,Object> r=new HashMap<>();
        try(Connection c=JdbcUtil.getConnection();PreparedStatement ps=c.prepareStatement("DELETE FROM announcement WHERE id=?")) {
            ps.setInt(1,id);ps.executeUpdate();r.put("code",0);r.put("msg","Deleted");
        }catch(Exception e){r.put("code",1);r.put("msg",e.getMessage());}
        return r;
    }

    @GetMapping("/op-logs")
    public Map<String,Object> getOpLogs(HttpServletRequest req) {
        Map<String,Object> r=new HashMap<>(); List<Map<String,Object>> list=new ArrayList<>();
        String aid=(String)req.getAttribute("adminId"); boolean root=isRoot(req);
        try(Connection c=JdbcUtil.getConnection();Statement s=c.createStatement()) {
            String sql = root ? "SELECT content,create_time FROM op_log ORDER BY create_time DESC LIMIT 50"
                : "SELECT content,create_time FROM op_log WHERE adminid='"+aid+"' ORDER BY create_time DESC LIMIT 20";
            ResultSet rs=s.executeQuery(sql);
            while(rs.next()){Map<String,Object> m=new HashMap<>();m.put("content",rs.getString("content"));m.put("createTime",rs.getString("create_time"));list.add(m);}
            rs.close(); r.put("code",0); r.put("list",list);
        }catch(Exception e){r.put("code",1);r.put("msg",e.getMessage());}
        return r;
    }

    // Add teacher
    @PostMapping("/add-teacher")
    public Map<String,Object> addTeacher(@RequestBody Map<String,String> body, HttpServletRequest req) {
        Map<String,Object> r=new HashMap<>();
        try {
            String aid = isRoot(req) ? body.get("adminId") : (String)req.getAttribute("adminId");
            if (aid == null || aid.isEmpty()) { r.put("code",1); r.put("msg","请指定所属部门"); return r; }
            String gh = body.get("gh"); if (gh==null||gh.isEmpty()) { r.put("code",1); r.put("msg","工号不能为空"); return r; }
            // Check duplicate
            java.sql.Connection c=JdbcUtil.getConnection();
            java.sql.PreparedStatement ps=c.prepareStatement("SELECT count(*) FROM teacher WHERE gh=?");
            ps.setString(1,gh); java.sql.ResultSet rs=ps.executeQuery(); rs.next();
            if(rs.getInt(1)>0){ rs.close();ps.close();c.close(); r.put("code",1);r.put("msg","工号 "+gh+" 已存在"); return r; }
            rs.close();ps.close();c.close();
            Teacher t=new Teacher(); t.setGh(gh); t.setXm(body.get("xm")); t.setZhicheng(body.get("zhicheng"));
            t.setEmail(body.get("email")); t.setPhone(body.get("phone")); t.setQq(body.get("qq"));
            t.setBgdd(body.get("bgdd")); t.setShangxian(Integer.parseInt(body.get("shangxian"))); t.setAdminid(aid);
            String pwd=Encrypt.MD5("123456");
            c=JdbcUtil.getConnection();
            ps=c.prepareStatement("INSERT INTO teacher(gh,xm,pwd,zhicheng,email,phone,qq,bgdd,shangxian,adminid) VALUES(?,?,?,?,?,?,?,?,?,?)");
            ps.setString(1,t.getGh());ps.setString(2,t.getXm());ps.setString(3,pwd);ps.setString(4,t.getZhicheng());
            ps.setString(5,t.getEmail());ps.setString(6,t.getPhone());ps.setString(7,t.getQq());ps.setString(8,t.getBgdd());
            ps.setInt(9,t.getShangxian());ps.setString(10,aid);ps.executeUpdate();ps.close();c.close();
            OpLogUtil.log(aid, "管理员添加教师：" + body.get("xm") + "(" + gh + ")，默认密码123456");
            r.put("code",0);r.put("msg","教师添加成功，默认密码 123456");
        }catch(Exception e){r.put("code",1);r.put("msg",e.getMessage());}
        return r;
    }

    // Add student
    @PostMapping("/add-student")
    public Map<String,Object> addStudent(@RequestBody Map<String,String> body, HttpServletRequest req) {
        Map<String,Object> r=new HashMap<>();
        try {
            String aid = isRoot(req) ? body.get("adminId") : (String)req.getAttribute("adminId");
            if (aid == null || aid.isEmpty()) { r.put("code",1); r.put("msg","请指定所属部门"); return r; }
            String xh = body.get("xh"); if (xh==null||xh.isEmpty()) { r.put("code",1); r.put("msg","学号不能为空"); return r; }
            // Check duplicate
            java.sql.Connection c=JdbcUtil.getConnection();
            java.sql.PreparedStatement ps=c.prepareStatement("SELECT count(*) FROM student WHERE xh=?");
            ps.setString(1,xh); java.sql.ResultSet rs=ps.executeQuery(); rs.next();
            if(rs.getInt(1)>0){ rs.close();ps.close();c.close(); r.put("code",1);r.put("msg","学号 "+xh+" 已存在"); return r; }
            rs.close();ps.close();c.close();
            String pwd=Encrypt.MD5("123456");
            c=JdbcUtil.getConnection();
            ps=c.prepareStatement("INSERT INTO student(xh,xm,pwd,zy,bj,email,phone,qq,adminid) VALUES(?,?,?,?,?,?,?,?,?)");
            ps.setString(1,xh);ps.setString(2,body.get("xm"));ps.setString(3,pwd);
            ps.setString(4,body.get("zy"));ps.setString(5,body.get("bj"));ps.setString(6,body.get("email"));
            ps.setString(7,body.get("phone"));ps.setString(8,body.get("qq"));ps.setString(9,aid);
            ps.executeUpdate();ps.close();c.close();
            OpLogUtil.log(aid, "管理员添加学生：" + body.get("xm") + "(" + xh + ")，默认密码123456");
            r.put("code",0);r.put("msg","学生添加成功，默认密码 123456");
        }catch(Exception e){r.put("code",1);r.put("msg",e.getMessage());}
        return r;
    }

    // Delete student
    @PostMapping("/delete-student")
    public Map<String,Object> deleteStudent(@RequestBody Map<String,String> body) {
        Map<String,Object> r=new HashMap<>();
        try { new StudentDao().delete(Integer.parseInt(body.get("id"))); r.put("code",0);r.put("msg","Deleted"); }
        catch(Exception e){r.put("code",1);r.put("msg",e.getMessage());}
        return r;
    }

    // ===== Root-only: Department & Admin Management =====
    @GetMapping("/departments-all")
    public Map<String,Object> getAllDepartments(HttpServletRequest req) {
        Map<String,Object> r=new HashMap<>();
        if (!isRoot(req)) { r.put("code",403); r.put("msg","Permission denied"); return r; }
        try(Connection c=JdbcUtil.getConnection();Statement s=c.createStatement()) {
            ResultSet rs=s.executeQuery("SELECT userid,dp FROM user ORDER BY userid");
            List<Map<String,String>> list=new ArrayList<>();
            while(rs.next()){Map<String,String> m=new HashMap<>();m.put("userid",rs.getString("userid"));m.put("dp",rs.getString("dp"));list.add(m);}
            rs.close(); r.put("code",0); r.put("list",list);
        }catch(Exception e){r.put("code",1);r.put("msg",e.getMessage());}
        return r;
    }

    @PostMapping("/add-department")
    public Map<String,Object> addDepartment(@RequestBody Map<String,String> body, HttpServletRequest req) {
        Map<String,Object> r=new HashMap<>();
        if (!isRoot(req)) { r.put("code",403); r.put("msg","Permission denied"); return r; }
        try(Connection c=JdbcUtil.getConnection()) {
            String userid = body.get("userid"); String dp = body.get("dp");
            if (userid==null||dp==null||userid.isEmpty()||dp.isEmpty()) { r.put("code",1); r.put("msg","请填写完整信息"); return r; }
            // Check duplicate
            PreparedStatement ck=c.prepareStatement("SELECT count(*) FROM user WHERE userid=?");
            ck.setString(1,userid); ResultSet rs=ck.executeQuery(); rs.next();
            if(rs.getInt(1)>0){ rs.close();ck.close(); r.put("code",1); r.put("msg","管理员账号 "+userid+" 已存在"); return r; }
            rs.close();ck.close();
            PreparedStatement ps=c.prepareStatement("INSERT INTO user(userid,userpwd,dp) VALUES(?,?,?)");
            ps.setString(1,userid); ps.setString(2,Encrypt.MD5("123456")); ps.setString(3,dp);
            ps.executeUpdate(); ps.close();
            PreparedStatement ps2=c.prepareStatement("INSERT INTO zt(adminid,zt) VALUES(?,0)");
            ps2.setString(1,userid); ps2.executeUpdate(); ps2.close();
            OpLogUtil.log("system", "超级管理员创建部门：" + dp + "(" + userid + ")，默认密码123456");
            r.put("code",0); r.put("msg","部门创建成功！管理员账号 "+userid+"，默认密码 123456");
        }catch(Exception e){r.put("code",1);r.put("msg",e.getMessage());}
        return r;
    }

    @PostMapping("/delete-department")
    public Map<String,Object> deleteDepartment(@RequestBody Map<String,String> body, HttpServletRequest req) {
        Map<String,Object> r=new HashMap<>();
        if (!isRoot(req)) { r.put("code",403); r.put("msg","Permission denied"); return r; }
        try(Connection c=JdbcUtil.getConnection()) {
            String userid = body.get("userid");
            c.createStatement().executeUpdate("DELETE FROM user WHERE userid='"+userid+"'");
            c.createStatement().executeUpdate("DELETE FROM zt WHERE adminid='"+userid+"'");
            c.createStatement().executeUpdate("DELETE FROM student WHERE adminid='"+userid+"'");
            c.createStatement().executeUpdate("DELETE FROM teacher WHERE adminid='"+userid+"'");
            c.createStatement().executeUpdate("DELETE FROM tm WHERE adminid='"+userid+"'");
            c.createStatement().executeUpdate("DELETE FROM announcement WHERE adminid='"+userid+"'");
            c.createStatement().executeUpdate("DELETE FROM op_log WHERE adminid='"+userid+"'");
            r.put("code",0); r.put("msg","Department and all related data deleted");
        }catch(Exception e){r.put("code",1);r.put("msg",e.getMessage());}
        return r;
    }

    @PostMapping("/reset-admin-pwd")
    public Map<String,Object> resetAdminPwd(@RequestBody Map<String,String> body, HttpServletRequest req) {
        Map<String,Object> r=new HashMap<>();
        if (!isRoot(req)) { r.put("code",403); r.put("msg","Permission denied"); return r; }
        try { new UserDao().modipwd(body.get("userid"), Encrypt.MD5(body.get("userid"))); r.put("code",0); r.put("msg","Password reset"); }
        catch(Exception e){r.put("code",1);r.put("msg",e.getMessage());}
        return r;
    }
}