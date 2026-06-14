package com.byxt.model;

public class User {
private String userid;
private String userpwd;
String dp;
String lastlogin;
int xuhao;

public int getXuhao() {
	return xuhao;
}
public void setXuhao(int xuhao) {
	this.xuhao = xuhao;
}
public String getDp() {
	return dp;
}
public void setDp(String dp) {
	this.dp = dp;
}
public String getUserid() {
	return userid;
}
public void setUserid(String userid) {
	this.userid = userid;
}
public String getUserpwd() {
	return userpwd;
}
public void setUserpwd(String userpwd) {
	this.userpwd = userpwd;
}
public String getLastlogin() {
	return lastlogin;
}
public void setLastlogin(String lastlogin) {
	this.lastlogin = lastlogin;
}


}
