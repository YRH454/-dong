package vo;

public class CurrentUser {
String id,mc,role,urlprefix;
String adminid;
String dp;

public String getAdminid() {
	return adminid;
}

public void setAdminid(String adminid) {
	this.adminid = adminid;
}

public String getDp() {
	return dp;
}

public void setDp(String dp) {
	this.dp = dp;
}

public String getId() {
	return id;
}

public void setId(String id) {
	this.id = id;
}

public String getMc() {
	return mc;
}

public void setMc(String mc) {
	this.mc = mc;
}

public String getRole() {
	return role;
}

public void setRole(String role) {
	this.role = role;
}

public String getUrlprefix() {
	return urlprefix;
}

public void setUrlprefix(String urlprefix) {
	this.urlprefix = urlprefix;
}

}
