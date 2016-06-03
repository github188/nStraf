package cn.grgbanking.feeltm.attendance.domain;
public class AttendanceListOut {
	private String username;
	private String workday;
	private String mornlast;
	private String afterlast;
	private String desc1;
	private String leave;
	private String desc2;
	private String nowork;
	private String desc3;
	private String catchnum;
	private String sjia;
	private String sjiadesc;
	private String bjia;
	private String bjiadesc;
	private String njia;
	private String njiadesc;
	private String otherjia;
	private String otherdesc;
	
	public AttendanceListOut(){
		
	}
	public AttendanceListOut(String username,String workdate,String mornlast,String afterlast,String desc1,String leave,String desc2,String nowork,
			String desc3,String catchnum,String sjia,String sjiadesc,String bjia,String bjiadesc,String njia,String njiadesc,String otherjia,String otherdesc){
		this.username = username;
		this.workday = workdate;
		this.mornlast = mornlast;
		this.afterlast = afterlast;
		this.desc1 = desc1;
		this.leave = leave;
		this.desc2 = desc2;
		this.nowork = nowork;
		this.desc3 = desc3;
		this.catchnum = catchnum;
		this.sjia = sjia;
		this.sjiadesc = sjia;
		this.bjia = bjia;
		this.bjiadesc = bjiadesc;
		this.njia = njia;
		this.njiadesc = njiadesc;
		this.otherjia = otherjia;
		this.otherdesc = otherdesc;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getWorkday() {
		return workday;
	}
	public void setWorkday(String workday) {
		this.workday = workday;
	}
	public String getMornlast() {
		return mornlast;
	}
	public void setMornlast(String mornlast) {
		this.mornlast = mornlast;
	}
	public String getAfterlast() {
		return afterlast;
	}
	public void setAfterlast(String afterlast) {
		this.afterlast = afterlast;
	}
	public String getDesc1() {
		return desc1;
	}
	public void setDesc1(String desc1) {
		this.desc1 = desc1;
	}
	public String getLeave() {
		return leave;
	}
	public void setLeave(String leave) {
		this.leave = leave;
	}
	public String getDesc2() {
		return desc2;
	}
	public void setDesc2(String desc2) {
		this.desc2 = desc2;
	}
	public String getNowork() {
		return nowork;
	}
	public void setNowork(String nowork) {
		this.nowork = nowork;
	}
	public String getDesc3() {
		return desc3;
	}
	public void setDesc3(String desc3) {
		this.desc3 = desc3;
	}
	public String getCatchnum() {
		return catchnum;
	}
	public void setCatchnum(String catchnum) {
		this.catchnum = catchnum;
	}
	public String getSjia() {
		return sjia;
	}
	public void setSjia(String sjia) {
		this.sjia = sjia;
	}
	public String getSjiadesc() {
		return sjiadesc;
	}
	public void setSjiadesc(String sjiadesc) {
		this.sjiadesc = sjiadesc;
	}
	public String getBjia() {
		return bjia;
	}
	public void setBjia(String bjia) {
		this.bjia = bjia;
	}
	public String getBjiadesc() {
		return bjiadesc;
	}
	public void setBjiadesc(String bjiadesc) {
		this.bjiadesc = bjiadesc;
	}
	public String getNjia() {
		return njia;
	}
	public void setNjia(String njia) {
		this.njia = njia;
	}
	public String getNjiadesc() {
		return njiadesc;
	}
	public void setNjiadesc(String njiadesc) {
		this.njiadesc = njiadesc;
	}
	public String getOtherjia() {
		return otherjia;
	}
	public void setOtherjia(String otherjia) {
		this.otherjia = otherjia;
	}
	public String getOtherdesc() {
		return otherdesc;
	}
	public void setOtherdesc(String otherdesc) {
		this.otherdesc = otherdesc;
	}
	
}
