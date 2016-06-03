package cn.grgbanking.feeltm.attendance.domain;
public class ExpenseList {
	private String username;
	private String workdate;
	private String stime;
	private String etime;
	private String mintime;
	private String maxtime;
	private String money;
	
	public ExpenseList(){
		
	}
	public ExpenseList(String username,String workdate,String stime,String etime,String mintime,String maxtime,String money){
		this.username = username;
		this.workdate = workdate;
		this.stime = stime;
		this.etime = etime;
		this.mintime = mintime;
		this.maxtime = maxtime;
		this.money = money;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getWorkdate() {
		return workdate;
	}
	public void setWorkdate(String workdate) {
		this.workdate = workdate;
	}
	public String getStime() {
		return stime;
	}
	public void setStime(String stime) {
		this.stime = stime;
	}
	public String getEtime() {
		return etime;
	}
	public void setEtime(String etime) {
		this.etime = etime;
	}
	public String getMintime() {
		return mintime;
	}
	public void setMintime(String mintime) {
		this.mintime = mintime;
	}
	public String getMaxtime() {
		return maxtime;
	}
	public void setMaxtime(String maxtime) {
		this.maxtime = maxtime;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	
}
