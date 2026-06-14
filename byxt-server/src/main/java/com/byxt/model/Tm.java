package com.byxt.model;

public class Tm {
	int id;
	String gh,txm,tm,bz,xh,sxm,zy,bj;
	String adminid;
	int status; // 0=open 1=pending 2=confirmed 3=rejected
	int choice; // 1=first 2=second
	String reason;
	String file_path;

	public String getAdminid() { return adminid; }
	public void setAdminid(String adminid) { this.adminid = adminid; }
	public int getId() { return id; }
	public void setId(int id) { this.id = id; }
	public String getGh() { return gh; }
	public void setGh(String gh) { this.gh = gh; }
	public String getTxm() { return txm; }
	public void setTxm(String txm) { this.txm = txm; }
	public String getTm() { return tm; }
	public void setTm(String tm) { this.tm = tm; }
	public String getBz() { return bz; }
	public void setBz(String bz) { this.bz = bz; }
	public String getXh() { return xh; }
	public void setXh(String xh) { this.xh = xh; }
	public String getSxm() { return sxm; }
	public void setSxm(String sxm) { this.sxm = sxm; }
	public String getZy() { return zy; }
	public void setZy(String zy) { this.zy = zy; }
	public String getBj() { return bj; }
	public void setBj(String bj) { this.bj = bj; }
	public int getStatus() { return status; }
	public void setStatus(int status) { this.status = status; }
	public int getChoice() { return choice; }
	public void setChoice(int choice) { this.choice = choice; }
	public String getReason() { return reason; }
	public void setReason(String reason) { this.reason = reason; }
	public String getFile_path() { return file_path; }
	public void setFile_path(String file_path) { this.file_path = file_path; }
}
