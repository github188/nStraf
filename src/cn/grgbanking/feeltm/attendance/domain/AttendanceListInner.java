package cn.grgbanking.feeltm.attendance.domain;
public class AttendanceListInner {
	private String username;
	private String workday;
	private String mornlast;
	private String leave;
	private String sjia;
	private String sjiadesc;
	private String bjia;
	private String bjiadesc;
	private String njia;
	private String njiadesc;
	private String otherjia;
	private String otherdesc;
	private String tripday;
	private String tripdesc;
	private String afterrestday;
	private String tripaddday;
	private String tripadddesc;
	private String overday;
	private String overdesc;
	private String bxjia;
	private String bxjiadesc;
	private String monthrestday;
	private String before_deferred;
	private String month_deferred;
	private String userid;
	private String attendancetype;
	
	
	public AttendanceListInner(){
		
	}
	public AttendanceListInner(String username,String workdate,String mornlast,String leave,String sjia,String sjiadesc,String bjia,
			String bjiadesc,String njia,String njiadesc,String otherjia,String otherdesc,String tripday,String tripdesc,String afterrestday,
			String tripaddday,String tripadddesc,String overday,String overdesc,String bxjia,String bxjiadesc,String monthrestday,
			String before_deferred,String month_deferred,String userid,String attendancetype){
		this.username = username;
		this.workday = workdate;
		this.mornlast = mornlast;
		this.leave = leave;
		this.sjia = sjia;
		this.sjiadesc = sjia;
		this.bjia = bjia;
		this.bjiadesc = bjiadesc;
		this.njia = njia;
		this.njiadesc = njiadesc;
		this.otherjia = otherjia;
		this.otherdesc = otherdesc;
		this.tripday = tripday;
		this.tripdesc = tripdesc;
		this.afterrestday = afterrestday;
		this.tripaddday = tripaddday;
		this.tripadddesc = tripadddesc;
		this.overday = overday;
		this.overdesc = overdesc;
		this.bxjia = bxjia;
		this.bxjiadesc = bxjiadesc;
		this.monthrestday = monthrestday;
		this.before_deferred = before_deferred;
		this.month_deferred = month_deferred;
		this.userid = userid;
		this.attendancetype = attendancetype;
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
	public String getLeave() {
		return leave;
	}
	public void setLeave(String leave) {
		this.leave = leave;
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
	public String getTripday() {
		return tripday;
	}
	public void setTripday(String tripday) {
		this.tripday = tripday;
	}
	public String getTripdesc() {
		return tripdesc;
	}
	public void setTripdesc(String tripdesc) {
		this.tripdesc = tripdesc;
	}
	public String getAfterrestday() {
		return afterrestday;
	}
	public void setAfterrestday(String afterrestday) {
		this.afterrestday = afterrestday;
	}
	public String getTripaddday() {
		return tripaddday;
	}
	public void setTripaddday(String tripaddday) {
		this.tripaddday = tripaddday;
	}
	public String getTripadddesc() {
		return tripadddesc;
	}
	public void setTripadddesc(String tripadddesc) {
		this.tripadddesc = tripadddesc;
	}
	public String getOverday() {
		return overday;
	}
	public void setOverday(String overday) {
		this.overday = overday;
	}
	public String getOverdesc() {
		return overdesc;
	}
	public void setOverdesc(String overdesc) {
		this.overdesc = overdesc;
	}
	public String getBxjia() {
		return bxjia;
	}
	public void setBxjia(String bxjia) {
		this.bxjia = bxjia;
	}
	public String getBxjiadesc() {
		return bxjiadesc;
	}
	public void setBxjiadesc(String bxjiadesc) {
		this.bxjiadesc = bxjiadesc;
	}
	public String getMonthrestday() {
		return monthrestday;
	}
	public void setMonthrestday(String monthrestday) {
		this.monthrestday = monthrestday;
	}
	public String getBefore_deferred() {
		return before_deferred;
	}
	public void setBefore_deferred(String before_deferred) {
		this.before_deferred = before_deferred;
	}
	public String getMonth_deferred() {
		return month_deferred;
	}
	public void setMonth_deferred(String month_deferred) {
		this.month_deferred = month_deferred;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getAttendancetype() {
		return attendancetype;
	}
	public void setAttendancetype(String attendancetype) {
		this.attendancetype = attendancetype;
	}
	
}
	