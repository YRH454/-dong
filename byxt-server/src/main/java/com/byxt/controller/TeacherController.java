package com.byxt.controller;

import com.byxt.dao.*;
import com.byxt.model.*;
import com.byxt.util.Encrypt;
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
            result.put("code", 0);
            result.put("teacher", t);
            result.put("topicCount", count);
            result.put("remaining", t.getShangxian() - count);
        } catch (Exception e) {
            result.put("code", 1); result.put("msg", e.getMessage());
        }
        return result;
    }

    @PostMapping("/add-topic")
    public Map<String, Object> addTopic(@RequestBody Map<String, String> body, HttpServletRequest req) {
        Map<String, Object> result = new HashMap<>();
        try {
            String userId = (String) req.getAttribute("userId");
            String userName = (String) req.getAttribute("userName");
            String adminId = (String) req.getAttribute("adminId");
            Tm tm = new Tm();
            tm.setGh(userId);
            tm.setTxm(userName);
            tm.setTm(body.get("tm"));
            tm.setBz(body.getOrDefault("bz", ""));
            new TmDao().add(tm, adminId);
            result.put("code", 0); result.put("msg", "鍑洪鎴愬姛");
        } catch (Exception e) {
            result.put("code", 1); result.put("msg", e.getMessage());
        }
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
            String tmName = body.get("tm");
            String bz = body.getOrDefault("bz", "");
            TmDao tmDao = new TmDao();
            Tm tm = new Tm(); tm.setGh(userId); tm.setTxm(userName); tm.setTm(tmName); tm.setBz(bz);
            for (int i = 0; i < count; i++) { tmDao.add(tm, adminId); }
            result.put("code", 0); result.put("msg", "鎵归噺鍑洪鎴愬姛锛屽叡" + count + "鏉?);
        } catch (Exception e) {
            result.put("code", 1); result.put("msg", e.getMessage());
        }
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
            result.put("code", 0);
            result.put("list", list);
            result.put("selectedCount", selected);
        } catch (Exception e) {
            result.put("code", 1); result.put("msg", e.getMessage());
        }
        return result;
    }

    @PostMapping("/update-topic")
    public Map<String, Object> updateTopic(@RequestBody Map<String, String> body) {
        Map<String, Object> result = new HashMap<>();
        try {
            Tm tm = new Tm();
            tm.setId(Integer.parseInt(body.get("id")));
            tm.setTm(body.get("tm"));
            tm.setBz(body.getOrDefault("bz", ""));
            tm.setXh(body.getOrDefault("xh", ""));
            tm.setSxm(body.getOrDefault("sxm", ""));
            tm.setZy(body.getOrDefault("zy", ""));
            tm.setBj(body.getOrDefault("bj", ""));
            new TmDao().update(tm);
            result.put("code", 0); result.put("msg", "鏇存柊鎴愬姛");
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
            result.put("code", 0); result.put("msg", "鍒犻櫎鎴愬姛");
        } catch (Exception e) {
            result.put("code", 1); result.put("msg", e.getMessage());
        }
        return result;
    }

    @PostMapping("/check-student")
    public Map<String, Object> checkStudent(@RequestBody Map<String, String> body, HttpServletRequest req) {
        Map<String, Object> result = new HashMap<>();
        try {
            String adminId = (String) req.getAttribute("adminId");
            String xh = body.get("xh");
            StudentDao sd = new StudentDao();
            Student st = sd.findStudentByIdAdminid(xh, adminId);
            if (st == null) {
                result.put("code", 1); result.put("msg", "鏃犳瀛﹀彿");
            } else {
                result.put("code", 0);
                result.put("xm", st.getXm());
                result.put("zy", st.getZy());
                result.put("bj", st.getBj());
            }
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
            Teacher t = new TeacherDao().findTeacherByIdAdminid(userId, adminId);
            result.put("code", 0); result.put("profile", t);
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
            Teacher t = new Teacher();
            t.setGh(userId);
            t.setXm(body.get("xm"));
            t.setZhicheng(body.get("zhicheng"));
            t.setEmail(body.get("email"));
            t.setPhone(body.get("phone"));
            t.setQq(body.get("qq"));
            t.setBgdd(body.get("bgdd"));
            t.setAdminid(adminId);
            new TeacherDao().update_t(t);
            new TmDao().update3(userId, body.get("xm"), adminId);
            result.put("code", 0); result.put("msg", "淇濆瓨鎴愬姛");
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
            new TeacherDao().modipwd(userId, Encrypt.MD5(body.get("newPwd")), adminId);
            result.put("code", 0); result.put("msg", "瀵嗙爜淇敼鎴愬姛");
        } catch (Exception e) {
            result.put("code", 1); result.put("msg", e.getMessage());
        }
        return result;
    }

	@PostMapping("/confirm-student")
	public Map<String, Object> confirmStudent(@RequestBody Map<String, String> body) {
		Map<String, Object> result = new HashMap<>();
		try { new TmDao().confirmStudent(Integer.parseInt(body.get("id"))); result.put("code", 0); result.put("msg", "已确认选题"); }
		catch (Exception e) { result.put("code", 1); result.put("msg", e.getMessage()); }
		return result;
	}

	@PostMapping("/reject-student")
	public Map<String, Object> rejectStudent(@RequestBody Map<String, String> body) {
		Map<String, Object> result = new HashMap<>();
		try { new TmDao().rejectStudent(Integer.parseInt(body.get("id"))); result.put("code", 0); result.put("msg", "已驳回"); }
		catch (Exception e) { result.put("code", 1); result.put("msg", e.getMessage()); }
		return result;
	}
}