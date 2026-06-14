package com.byxt.controller;

import com.byxt.dao.*;
import com.byxt.model.*;
import com.byxt.util.Encrypt;
import com.byxt.util.OpLogUtil;
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
            int pageSize = Integer.parseInt(req.getParameter("pageSize") != null ? req.getParameter("pageSize") : "200");

            TmDao tmDao = new TmDao();
            String c1 = " where xh='" + userId + "' and adminid='" + adminId + "' and status=2";
            if (tmDao.getRecordCount(c1) != 0) {
                result.put("selected", true); result.put("code", 0); return result;
            }
            int choiceCount = tmDao.getRecordCount(" where xh='" + userId + "' and adminid='" + adminId + "' and status=1");
            result.put("choiceCount", choiceCount);

            ZtDao ztDao = new ZtDao();
            if (ztDao.query(adminId) == 0) { result.put("closed", true); result.put("code", 0); return result; }

            String cond = " where adminid='" + adminId + "'";
            if (keyword != null && !keyword.isEmpty()) cond += " and (txm like '%" + keyword + "%' or tm like '%" + keyword + "%')";
            List<Tm> list = tmDao.query(cond, page, pageSize);
            int total = tmDao.getRecordCount(cond);
            result.put("code", 0); result.put("list", list); result.put("total", total); result.put("page", page); result.put("pageSize", pageSize);
        } catch (Exception e) { result.put("code", 1); result.put("msg", e.getMessage()); }
        return result;
    }

    @PostMapping("/select-topic")
    public Map<String, Object> selectTopic(@RequestBody Map<String, String> body, HttpServletRequest req) {
        Map<String, Object> result = new HashMap<>();
        try {
            String userId = (String) req.getAttribute("userId");
            String adminId = (String) req.getAttribute("adminId");
            int tmid = Integer.parseInt(body.get("tmid"));
            int choiceNo = Integer.parseInt(body.getOrDefault("choice", "1"));
            String reason = body.getOrDefault("reason", "");

            // Limit: max 2 choices total
            TmDao td = new TmDao();
            int existing = td.getRecordCount(" where xh='"+userId+"' and adminid='"+adminId+"' and (status=1 or status=2)");
            if (existing >= 2) { result.put("code", 1); result.put("msg", "You already have 2 choices submitted"); return result; }

            StudentDao sd = new StudentDao();
            Student st = sd.findStudentByIdAdminid(userId, adminId);
            int r = TmDao.xtWithStatus(tmid, userId, st.getXm(), st.getZy(), st.getBj(), reason, choiceNo);
            if (r == 1) {
                OpLogUtil.log(adminId, "学生 " + st.getXm() + "(" + userId + ") 选择了题目ID:" + tmid + "，志愿:" + choiceNo + "，理由:" + reason);
                result.put("code", 0); result.put("msg", "Submitted, waiting for confirmation");
            }
            else { result.put("code", 1); result.put("msg", "Topic unavailable"); }
        } catch (Exception e) { result.put("code", 1); result.put("msg", e.getMessage()); }
        return result;
    }

    @GetMapping("/teacher-detail")
    public Map<String, Object> getTeacherDetail(@RequestParam String gh, HttpServletRequest req) {
        Map<String, Object> result = new HashMap<>();
        try {
            String adminId = (String) req.getAttribute("adminId");
            Teacher t = new TeacherDao().findTeacherByIdAdminid(gh, adminId);
            if (t != null) { result.put("code", 0); result.put("teacher", t); }
            else { result.put("code", 1); result.put("msg", "Teacher not found"); }
        } catch (Exception e) { result.put("code", 1); result.put("msg", e.getMessage()); }
        return result;
    }

    @GetMapping("/my-result")
    public Map<String, Object> getMyResult(@RequestParam(defaultValue = "") String action, @RequestParam(defaultValue = "") String xh, HttpServletRequest req) {
        Map<String, Object> result = new HashMap<>();
        try {
            String userId = (String) req.getAttribute("userId");
            String adminId = (String) req.getAttribute("adminId");
            TmDao tmDao = new TmDao();
            if ("delete".equals(action)) { tmDao.qcjl(userId, adminId); result.put("msg", "Deleted"); }
            String cond = " where xh='" + userId + "' and adminid='" + adminId + "'";
            List<TmEx> list = tmDao.query(cond, adminId);
            ZtDao ztDao = new ZtDao();
            result.put("code", 0); result.put("list", list); result.put("canDelete", ztDao.query(adminId) != 0);
        } catch (Exception e) { result.put("code", 1); result.put("msg", e.getMessage()); }
        return result;
    }

    @GetMapping("/profile")
    public Map<String, Object> getProfile(HttpServletRequest req) {
        Map<String, Object> result = new HashMap<>();
        try {
            Student st = new StudentDao().findStudentByIdAdminid((String)req.getAttribute("userId"), (String)req.getAttribute("adminId"));
            result.put("code", 0); result.put("profile", st);
        } catch (Exception e) { result.put("code", 1); result.put("msg", e.getMessage()); }
        return result;
    }

    @PostMapping("/update-profile")
    public Map<String, Object> updateProfile(@RequestBody Map<String, String> body, HttpServletRequest req) {
        Map<String, Object> result = new HashMap<>();
        try {
            String userId = (String) req.getAttribute("userId");
            String adminId = (String) req.getAttribute("adminId");
            Student s = new Student(); s.setXh(userId); s.setXm(body.get("xm")); s.setZy(body.get("zy"));
            s.setBj(body.get("bj")); s.setEmail(body.get("email")); s.setPhone(body.get("phone"));
            s.setQq(body.get("qq")); s.setAdminid(adminId);
            new StudentDao().update(s);
            new TmDao().update2(userId, body.get("xm"), body.get("zy"), body.get("bj"), adminId);
            result.put("code", 0); result.put("msg", "Saved");
        } catch (Exception e) { result.put("code", 1); result.put("msg", e.getMessage()); }
        return result;
    }

    @PostMapping("/change-password")
    public Map<String, Object> changePwd(@RequestBody Map<String, String> body, HttpServletRequest req) {
        Map<String, Object> result = new HashMap<>();
        try {
            new StudentDao().modipwd((String)req.getAttribute("userId"), Encrypt.MD5(body.get("newPwd")), (String)req.getAttribute("adminId"));
            result.put("code", 0); result.put("msg", "Password changed");
        } catch (Exception e) { result.put("code", 1); result.put("msg", e.getMessage()); }
        return result;
    }

    // Download task book
    @GetMapping("/download-taskbook")
    public org.springframework.http.ResponseEntity<org.springframework.core.io.Resource> downloadTaskBook(@RequestParam int topicId, HttpServletRequest req) {
        try {
            java.sql.Connection c=com.byxt.util.JdbcUtil.getConnection();
            java.sql.PreparedStatement ps=c.prepareStatement("SELECT t.file_path,t.xh,t.sxm,t.gh,t.adminid FROM tm t WHERE t.id=?");
            ps.setInt(1, topicId);
            java.sql.ResultSet rs=ps.executeQuery();
            if(rs.next() && rs.getString(1)!=null && !rs.getString(1).isEmpty()) {
                String fp=rs.getString(1);
                java.io.File f=new java.io.File(fp);
                if(f.exists()) {
                    String xh=rs.getString(2); String sxm=rs.getString(3); String gh=rs.getString(4); String aid=rs.getString(5);
                    OpLogUtil.log(aid, "学生 " + sxm + "(" + xh + ") 下载了教师(" + gh + ")的任务书");
                    org.springframework.core.io.Resource res=new org.springframework.core.io.FileSystemResource(f);
                    rs.close(); ps.close(); c.close();
                    return org.springframework.http.ResponseEntity.ok()
                        .header("Content-Disposition","attachment; filename=\""+f.getName()+"\"")
                        .body(res);
                }
            }
            rs.close(); ps.close(); c.close();
        } catch(Exception ignored) {}
        return org.springframework.http.ResponseEntity.notFound().build();
    }

    // Submit report as text (mobile-friendly)
    @PostMapping("/submit-report-text")
    public Map<String,Object> submitReportText(@RequestBody Map<String,String> body, HttpServletRequest req) {
        Map<String,Object> r=new HashMap<>();
        try {
            String userId=(String)req.getAttribute("userId");
            String adminId=(String)req.getAttribute("adminId");
            int topicId=Integer.parseInt(body.get("topicId"));
            String content=body.getOrDefault("content","");
            String baseDir="D:/byxt-data/reports/"+adminId+"/"+topicId;
            new java.io.File(baseDir).mkdirs();
            String fname=baseDir+"/report.txt";
            java.io.FileWriter fw=new java.io.FileWriter(fname);
            fw.write(content); fw.close();
            java.sql.Connection c=com.byxt.util.JdbcUtil.getConnection();
            // Check existing report
            java.sql.PreparedStatement ck=c.prepareStatement("SELECT id FROM report WHERE topic_id=? AND student_xh=? ORDER BY id DESC LIMIT 1");
            ck.setInt(1,topicId); ck.setString(2,userId);
            java.sql.ResultSet ckRs=ck.executeQuery();
            if(ckRs.next()){
                int rid=ckRs.getInt(1); ckRs.close(); ck.close();
                java.sql.PreparedStatement up=c.prepareStatement("UPDATE report SET file_path=?,status=0 WHERE id=?");
                up.setString(1,fname); up.setInt(2,rid); up.executeUpdate(); up.close();
            } else {
                ckRs.close(); ck.close();
                java.sql.PreparedStatement in=c.prepareStatement("INSERT INTO report(topic_id,student_xh,file_path,status) VALUES(?,?,?,0)");
                in.setInt(1,topicId); in.setString(2,userId); in.setString(3,fname); in.executeUpdate(); in.close();
            }
            c.close();
            OpLogUtil.log(adminId, "学生 " + userId + " 提交了开题报告(题目ID:" + topicId + ")");
            r.put("code",0);r.put("msg","报告提交成功");
        }catch(Exception e){r.put("code",1);r.put("msg",e.getMessage());}
        return r;
    }

    // Get my own report content
    @GetMapping("/get-my-report-content")
    public Map<String,Object> getMyReportContent(@RequestParam int topicId) {
        Map<String,Object> r=new HashMap<>();
        try(java.sql.Connection c=com.byxt.util.JdbcUtil.getConnection()) {
            java.sql.PreparedStatement ps=c.prepareStatement("SELECT file_path FROM report WHERE topic_id=? ORDER BY id DESC LIMIT 1");
            ps.setInt(1,topicId); java.sql.ResultSet rs=ps.executeQuery();
            if(rs.next()&&rs.getString(1)!=null){ r.put("code",0); r.put("content",new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(rs.getString(1))))); }
            else r.put("code",1); rs.close();ps.close();
        }catch(Exception e){r.put("code",1);r.put("msg",e.getMessage());}
        return r;
    }

    // Get taskbook content as text
    @GetMapping("/get-taskbook-content")
    public Map<String,Object> getTaskbookContent(@RequestParam int topicId) {
        Map<String,Object> r=new HashMap<>();
        try(java.sql.Connection c=com.byxt.util.JdbcUtil.getConnection()) {
            java.sql.PreparedStatement ps=c.prepareStatement("SELECT file_path FROM tm WHERE id=?");
            ps.setInt(1,topicId); java.sql.ResultSet rs=ps.executeQuery();
            if(rs.next()&&rs.getString(1)!=null){ r.put("code",0); r.put("content",new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(rs.getString(1))))); }
            else r.put("code",1); rs.close();ps.close();
        }catch(Exception e){r.put("code",1);r.put("msg",e.getMessage());}
        return r;
    }

    // Opening report upload (file)
    @PostMapping("/upload-report")
    public Map<String,Object> uploadReport(@RequestParam("file") org.springframework.web.multipart.MultipartFile file,
            @RequestParam("topicId") int topicId, HttpServletRequest req) {
        Map<String,Object> r=new HashMap<>();
        try {
            String fname = file.getOriginalFilename();
            if (fname == null || !fname.matches(".*\\.(pdf|doc|docx)$")) { r.put("code",1); r.put("msg","仅支持 PDF / Word (.doc .docx) 格式"); return r; }
            String userId=(String)req.getAttribute("userId");
            String adminId=(String)req.getAttribute("adminId");
            String dir="reports/"+adminId+"/"+topicId;
            new java.io.File(dir).mkdirs();
            fname="report_"+System.currentTimeMillis()+"_"+file.getOriginalFilename();
            file.transferTo(new java.io.File(dir+"/"+fname));
            java.sql.Connection c=com.byxt.util.JdbcUtil.getConnection();
            java.sql.PreparedStatement ps=c.prepareStatement("INSERT INTO report(topic_id,student_xh,file_path,status) VALUES(?,?,?,0) ON DUPLICATE KEY UPDATE file_path=?,status=0");
            ps.setInt(1,topicId);ps.setString(2,userId);ps.setString(3,dir+"/"+fname);ps.setString(4,dir+"/"+fname);
            ps.executeUpdate();ps.close();c.close();
            OpLogUtil.log(adminId, "学生 " + userId + " 上传了开题报告(题目ID:" + topicId + ")");
            r.put("code",0);r.put("msg","Report submitted");
        }catch(Exception e){r.put("code",1);r.put("msg",e.getMessage());}
        return r;
    }

    @GetMapping("/my-report")
    public Map<String,Object> getMyReport(@RequestParam int topicId) {
        Map<String,Object> r=new HashMap<>();
        try(java.sql.Connection c=com.byxt.util.JdbcUtil.getConnection()) {
            java.sql.PreparedStatement ps=c.prepareStatement("SELECT * FROM report WHERE topic_id=? ORDER BY id DESC LIMIT 1");
            ps.setInt(1,topicId);
            java.sql.ResultSet rs=ps.executeQuery();
            if(rs.next()){r.put("code",0);r.put("id",rs.getInt("id"));r.put("file_path",rs.getString("file_path"));r.put("status",rs.getInt("status"));r.put("comment",rs.getString("comment"));r.put("createTime",rs.getString("create_time"));}
            else r.put("code",1);
            rs.close();ps.close();
        }catch(Exception e){r.put("code",1);}
        return r;
    }
}