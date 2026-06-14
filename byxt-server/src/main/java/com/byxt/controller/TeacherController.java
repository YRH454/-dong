package com.byxt.controller;

import com.byxt.dao.*;
import com.byxt.model.*;
import com.byxt.util.Encrypt;
import com.byxt.util.OpLogUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/teacher")
public class TeacherController {

    @GetMapping("/info")
    public Map<String, Object> getInfo(HttpServletRequest req) {
        Map<String, Object> result = new HashMap<>();
        try {
            String userId = (String) req.getAttribute("userId");
            String adminId = (String) req.getAttribute("adminId");
            TeacherDao td = new TeacherDao();
            Teacher t = td.findTeacherByIdAdminid(userId, adminId);
            TmDao tmDao = new TmDao();
            int count = tmDao.getRecordCount(" where gh='" + userId + "' and adminid='" + adminId + "'");
            result.put("code", 0); result.put("teacher", t); result.put("topicCount", count);
            result.put("remaining", t.getShangxian() - count);
        } catch (Exception e) { result.put("code", 1); result.put("msg", e.getMessage()); }
        return result;
    }

    @PostMapping("/add-topic")
    public Map<String, Object> addTopic(@RequestBody Map<String, String> body, HttpServletRequest req) {
        Map<String, Object> result = new HashMap<>();
        try {
            String userId = (String) req.getAttribute("userId");
            String userName = (String) req.getAttribute("userName");
            String adminId = (String) req.getAttribute("adminId");
            Tm tm = new Tm(); tm.setGh(userId); tm.setTxm(userName);
            tm.setTm(body.get("tm")); tm.setBz(body.getOrDefault("bz", ""));
            new TmDao().add(tm, adminId);
            OpLogUtil.log(adminId, "教师 " + userName + "(" + userId + ") 新增题目：" + body.get("tm"));
            result.put("code", 0); result.put("msg", "Topic added");
        } catch (Exception e) { result.put("code", 1); result.put("msg", e.getMessage()); }
        return result;
    }

    @PostMapping("/add-topics-batch")
    public Map<String, Object> addTopicsBatch(@RequestBody Map<String, String> body, HttpServletRequest req) {
        Map<String, Object> result = new HashMap<>();
        try {
            String userId = (String) req.getAttribute("userId");
            String userName = (String) req.getAttribute("userName");
            String adminId = (String) req.getAttribute("adminId");
            int count = Integer.parseInt(body.get("count"));
            String tmName = body.get("tm"); String bz = body.getOrDefault("bz", "");
            TmDao tmDao = new TmDao();
            Tm tm = new Tm(); tm.setGh(userId); tm.setTxm(userName); tm.setTm(tmName); tm.setBz(bz);
            for (int i = 0; i < count; i++) { tmDao.add(tm, adminId); }
            OpLogUtil.log(adminId, "教师 " + userName + "(" + userId + ") 批量出题 " + count + " 个：" + tmName);
            result.put("code", 0); result.put("msg", "Batch added: " + count);
        } catch (Exception e) { result.put("code", 1); result.put("msg", e.getMessage()); }
        return result;
    }

    @GetMapping("/my-topics")
    public Map<String, Object> myTopics(HttpServletRequest req) {
        Map<String, Object> result = new HashMap<>();
        try {
            String userId = (String) req.getAttribute("userId");
            String adminId = (String) req.getAttribute("adminId");
            TmDao tmDao = new TmDao();
            String cond = " where gh='" + userId + "' and adminid='" + adminId + "'";
            List<TmEx> list = tmDao.query(cond, adminId);
            int selected = tmDao.getRecordCount(" where gh='" + userId + "' and xh!='' and adminid='" + adminId + "'");
            result.put("code", 0); result.put("list", list); result.put("selectedCount", selected);
        } catch (Exception e) { result.put("code", 1); result.put("msg", e.getMessage()); }
        return result;
    }

    @PostMapping("/update-topic")
    public Map<String, Object> updateTopic(@RequestBody Map<String, String> body) {
        Map<String, Object> result = new HashMap<>();
        try {
            Tm tm = new Tm(); tm.setId(Integer.parseInt(body.get("id"))); tm.setTm(body.get("tm"));
            tm.setBz(body.getOrDefault("bz", "")); tm.setXh(body.getOrDefault("xh", ""));
            tm.setSxm(body.getOrDefault("sxm", "")); tm.setZy(body.getOrDefault("zy", ""));
            tm.setBj(body.getOrDefault("bj", ""));
            new TmDao().update(tm);
            result.put("code", 0); result.put("msg", "Updated");
        } catch (Exception e) { result.put("code", 1); result.put("msg", e.getMessage()); }
        return result;
    }

    @PostMapping("/delete-topic")
    public Map<String, Object> deleteTopic(@RequestBody Map<String, String> body, HttpServletRequest req) {
        Map<String, Object> result = new HashMap<>();
        try {
            String userId = (String) req.getAttribute("userId");
            String adminId = (String) req.getAttribute("adminId");
            int id = Integer.parseInt(body.get("id"));
            // Verify topic belongs to this teacher and is not confirmed
            java.sql.Connection c = com.byxt.util.JdbcUtil.getConnection();
            java.sql.PreparedStatement ps = c.prepareStatement("select gh,status from tm where id=?");
            ps.setInt(1, id);
            java.sql.ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                if (!rs.getString("gh").equals(userId)) { result.put("code", 1); result.put("msg", "Not your topic"); }
                else if (rs.getInt("status") == 2) { result.put("code", 1); result.put("msg", "Cannot delete confirmed topic"); }
                else { new TmDao().delete(id); result.put("code", 0); result.put("msg", "Deleted"); }
            } else { result.put("code", 1); result.put("msg", "Topic not found"); }
            rs.close(); ps.close(); c.close();
        } catch (Exception e) { result.put("code", 1); result.put("msg", e.getMessage()); }
        return result;
    }

    @PostMapping("/check-student")
    public Map<String, Object> checkStudent(@RequestBody Map<String, String> body, HttpServletRequest req) {
        Map<String, Object> result = new HashMap<>();
        try {
            String adminId = (String) req.getAttribute("adminId");
            String xh = body.get("xh");
            Student st = new StudentDao().findStudentByIdAdminid(xh, adminId);
            if (st == null) { result.put("code", 1); result.put("msg", "Student not found"); }
            else { result.put("code", 0); result.put("xm", st.getXm()); result.put("zy", st.getZy()); result.put("bj", st.getBj()); }
        } catch (Exception e) { result.put("code", 1); result.put("msg", e.getMessage()); }
        return result;
    }

    @GetMapping("/profile")
    public Map<String, Object> getProfile(HttpServletRequest req) {
        Map<String, Object> result = new HashMap<>();
        try { result.put("code", 0); result.put("profile", new TeacherDao().findTeacherByIdAdminid((String)req.getAttribute("userId"), (String)req.getAttribute("adminId"))); }
        catch (Exception e) { result.put("code", 1); result.put("msg", e.getMessage()); }
        return result;
    }

    @PostMapping("/update-profile")
    public Map<String, Object> updateProfile(@RequestBody Map<String, String> body, HttpServletRequest req) {
        Map<String, Object> result = new HashMap<>();
        try {
            String userId = (String) req.getAttribute("userId");
            String adminId = (String) req.getAttribute("adminId");
            Teacher t = new Teacher(); t.setGh(userId); t.setXm(body.get("xm")); t.setZhicheng(body.get("zhicheng"));
            t.setEmail(body.get("email")); t.setPhone(body.get("phone")); t.setQq(body.get("qq"));
            t.setBgdd(body.get("bgdd")); t.setAdminid(adminId);
            new TeacherDao().update_t(t); new TmDao().update3(userId, body.get("xm"), adminId);
            result.put("code", 0); result.put("msg", "Saved");
        } catch (Exception e) { result.put("code", 1); result.put("msg", e.getMessage()); }
        return result;
    }

    @PostMapping("/change-password")
    public Map<String, Object> changePwd(@RequestBody Map<String, String> body, HttpServletRequest req) {
        Map<String, Object> result = new HashMap<>();
        try { new TeacherDao().modipwd((String)req.getAttribute("userId"), Encrypt.MD5(body.get("newPwd")), (String)req.getAttribute("adminId")); result.put("code", 0); result.put("msg", "Password changed"); }
        catch (Exception e) { result.put("code", 1); result.put("msg", e.getMessage()); }
        return result;
    }

    @PostMapping("/confirm-student")
    public Map<String, Object> confirmStudent(@RequestBody Map<String, String> body, HttpServletRequest req) {
        Map<String, Object> result = new HashMap<>();
        try {
            new TmDao().confirmStudent(Integer.parseInt(body.get("id")));
            OpLogUtil.log((String)req.getAttribute("adminId"), "教师 " + req.getAttribute("userName") + " 确认了学生选题(ID:" + body.get("id") + ")");
            result.put("code", 0); result.put("msg", "Confirmed");
        } catch (Exception e) { result.put("code", 1); result.put("msg", e.getMessage()); }
        return result;
    }

    @PostMapping("/reject-student")
    public Map<String, Object> rejectStudent(@RequestBody Map<String, String> body, HttpServletRequest req) {
        Map<String, Object> result = new HashMap<>();
        try {
            new TmDao().rejectStudent(Integer.parseInt(body.get("id")));
            OpLogUtil.log((String)req.getAttribute("adminId"), "教师 " + req.getAttribute("userName") + " 驳回了学生选题(ID:" + body.get("id") + ")");
            result.put("code", 0); result.put("msg", "Rejected");
        } catch (Exception e) { result.put("code", 1); result.put("msg", e.getMessage()); }
        return result;
    }

    // Task book upload
    @PostMapping("/upload-taskbook")
    public Map<String,Object> uploadTaskBook(@RequestParam("file") org.springframework.web.multipart.MultipartFile file,
            @RequestParam("topicId") int topicId, HttpServletRequest req) {
        Map<String,Object> r=new HashMap<>();
        try {
            String fname = file.getOriginalFilename();
            if (fname == null || !fname.matches(".*\\.(pdf|doc|docx)$")) { r.put("code",1); r.put("msg","仅支持 PDF / Word (.doc .docx) 格式"); return r; }
            String adminId=(String)req.getAttribute("adminId");
            String dir="uploads/"+adminId+"/"+topicId;
            new java.io.File(dir).mkdirs();
            fname="taskbook_"+System.currentTimeMillis()+"_"+file.getOriginalFilename();
            file.transferTo(new java.io.File(dir+"/"+fname));
            // Update file_path in DB
            java.sql.Connection c=com.byxt.util.JdbcUtil.getConnection();
            java.sql.PreparedStatement ps=c.prepareStatement("UPDATE tm SET file_path=? WHERE id=?");
            ps.setString(1, dir+"/"+fname); ps.setInt(2, topicId); ps.executeUpdate(); ps.close(); c.close();
            OpLogUtil.log(adminId, "教师 " + req.getAttribute("userName") + " 上传了任务书(题目ID:" + topicId + ")");
            r.put("code",0); r.put("msg","Task book uploaded");
        } catch(Exception e) { r.put("code",1); r.put("msg",e.getMessage()); }
        return r;
    }

    // Save taskbook as text (mobile-friendly)
    @PostMapping("/save-taskbook-text")
    public Map<String,Object> saveTaskbookText(@RequestBody Map<String,String> body, HttpServletRequest req) {
        Map<String,Object> r=new HashMap<>();
        try {
            String userId=(String)req.getAttribute("userId");
            String adminId=(String)req.getAttribute("adminId");
            int topicId=Integer.parseInt(body.get("topicId"));
            String content=body.getOrDefault("content","");
            String baseDir="D:/byxt-data/taskbooks/"+adminId+"/"+topicId;
            new java.io.File(baseDir).mkdirs();
            String fname=baseDir+"/taskbook.txt";
            java.io.FileWriter fw=new java.io.FileWriter(fname);
            fw.write(content); fw.close();
            java.sql.Connection c=com.byxt.util.JdbcUtil.getConnection();
            java.sql.PreparedStatement ps=c.prepareStatement("UPDATE tm SET file_path=? WHERE id=?");
            ps.setString(1,fname); ps.setInt(2,topicId); ps.executeUpdate(); ps.close(); c.close();
            OpLogUtil.log(adminId, "教师 " + req.getAttribute("userName") + " 发布了任务书(题目ID:" + topicId + ")");
            r.put("code",0); r.put("msg","任务书发布成功");
        } catch(Exception e) { r.put("code",1); r.put("msg",e.getMessage()); }
        return r;
    }

    // Get report content as text
    @GetMapping("/get-report-content")
    public Map<String,Object> getReportContent(@RequestParam int reportId) {
        Map<String,Object> r=new HashMap<>();
        try(java.sql.Connection c=com.byxt.util.JdbcUtil.getConnection()) {
            java.sql.PreparedStatement ps=c.prepareStatement("SELECT file_path FROM report WHERE id=?");
            ps.setInt(1,reportId); java.sql.ResultSet rs=ps.executeQuery();
            if(rs.next()&&rs.getString(1)!=null){ r.put("code",0); r.put("content",new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(rs.getString(1))))); }
            else r.put("code",1); rs.close();ps.close();
        }catch(Exception e){r.put("code",1);r.put("msg",e.getMessage());}
        return r;
    }

    // Review report
    @PostMapping("/review-report")
    public Map<String,Object> reviewReport(@RequestBody Map<String,String> body) {
        Map<String,Object> r=new HashMap<>();
        try(java.sql.Connection c=com.byxt.util.JdbcUtil.getConnection()) {
            java.sql.PreparedStatement ps=c.prepareStatement("UPDATE report SET status=?,comment=? WHERE id=?");
            ps.setInt(1,Integer.parseInt(body.get("status")));ps.setString(2,body.get("comment"));
            ps.setInt(3,Integer.parseInt(body.get("id")));ps.executeUpdate();ps.close();
            r.put("code",0);r.put("msg","Reviewed");
        }catch(Exception e){r.put("code",1);r.put("msg",e.getMessage());}
        return r;
    }

    // My students list
    @GetMapping("/my-students")
    public Map<String,Object> myStudents(HttpServletRequest req) {
        Map<String,Object> r=new HashMap<>(); List<Map<String,Object>> list=new ArrayList<>();
        try {
            String userId=(String)req.getAttribute("userId");
            String adminId=(String)req.getAttribute("adminId");
            java.sql.Connection c=com.byxt.util.JdbcUtil.getConnection();
            java.sql.PreparedStatement ps=c.prepareStatement(
                "SELECT t.id,t.tm,t.xh,t.sxm,t.zy,t.bj,t.status,t.reason,t.file_path,s.email,s.phone,s.qq " +
                "FROM tm t LEFT JOIN student s ON t.xh=s.xh AND t.adminid=s.adminid " +
                "WHERE t.gh=? AND t.adminid=? AND t.xh!='' ORDER BY t.status DESC, t.id DESC");
            ps.setString(1,userId); ps.setString(2,adminId);
            java.sql.ResultSet rs=ps.executeQuery();
            while(rs.next()){
                Map<String,Object> m=new HashMap<>();
                m.put("topicId",rs.getInt("id")); m.put("tm",rs.getString("tm"));
                m.put("xh",rs.getString("xh")); m.put("sxm",rs.getString("sxm"));
                m.put("zy",rs.getString("zy")); m.put("bj",rs.getString("bj"));
                m.put("status",rs.getInt("status")); m.put("reason",rs.getString("reason"));
                m.put("taskbook",rs.getString("file_path"));
                m.put("email",rs.getString("email")); m.put("phone",rs.getString("phone"));
                m.put("qq",rs.getString("qq"));
                // Check report
                try(java.sql.Connection c2=com.byxt.util.JdbcUtil.getConnection();
                    java.sql.PreparedStatement ps2=c2.prepareStatement("SELECT id,status,comment FROM report WHERE topic_id=? ORDER BY id DESC LIMIT 1")) {
                    ps2.setInt(1,rs.getInt("id"));
                    java.sql.ResultSet rs2=ps2.executeQuery();
                    if(rs2.next()){ m.put("reportId",rs2.getInt("id")); m.put("reportStatus",rs2.getInt("status")); m.put("reportComment",rs2.getString("comment")); }
                    else { m.put("reportId",0); m.put("reportStatus",-1); m.put("reportComment",""); }
                    rs2.close();
                } catch(Exception ex) { m.put("reportId",0); m.put("reportStatus",-1); m.put("reportComment",""); }
                list.add(m);
            }
            rs.close(); ps.close(); c.close();
            r.put("code",0); r.put("list",list);
        }catch(Exception e){r.put("code",1);r.put("msg",e.getMessage());}
        return r;
    }
}