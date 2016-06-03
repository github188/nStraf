package cn.grgbanking.feeltm.attendance.domain;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
@Entity
@Table(name ="oa_attendance")
public class AttendanceAnalysisCount {
	private String id;
	private String userid;
	private String username;
	private String deptname;
	private String groupname;
	private String innernum;
	private String outnum;
	private String workdate;
	private String stime;
	private String etime;
	private String mtime;
	private String worktype;
	private String attendancetype;
	private String time1;
	private String time2;
	private String time3;
	private String time4;
	private String time5;
	private String time6;
	private String morn_islast;
	private String after_isleave;
	private String noon_islast;
	private String noon_isleave;
	private String iscatch;
	private double worktime;
	private double all_worktime;
	private int workday;
	private int validateday;
	private String mtime2;
	private String mindate;
	private String maxdate;
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	@Column(name = "c_id", unique = true, nullable = false)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Column(name="c_userid")
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	@Column(name="c_username")
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	@Column(name="c_deptname")
	public String getDeptname() {
		return deptname;
	}
	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}
	@Column(name="c_groupname")
	public String getGroupname() {
		return groupname;
	}
	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}
	@Column(name="c_innernum")
	public String getInnernum() {
		return innernum;
	}
	public void setInnernum(String innernum) {
		this.innernum = innernum;
	}
	@Column(name="c_outnum")
	public String getOutnum() {
		return outnum;
	}
	public void setOutnum(String outnum) {
		this.outnum = outnum;
	}
	@Column(name="c_workdate")
	public String getWorkdate() {
		return workdate;
	}
	public void setWorkdate(String workdate) {
		this.workdate = workdate;
	}
	@Column(name="c_stime")
	public String getStime() {
		return stime;
	}
	public void setStime(String stime) {
		this.stime = stime;
	}
	@Column(name="c_etime")
	public String getEtime() {
		return etime;
	}
	public void setEtime(String etime) {
		this.etime = etime;
	}
	@Column(name="c_mtime")
	public String getMtime() {
		return mtime;
	}
	public void setMtime(String mtime) {
		this.mtime = mtime;
	}
	@Column(name="c_worktype")
	public String getWorktype() {
		return worktype;
	}
	public void setWorktype(String worktype) {
		this.worktype = worktype;
	}
	@Column(name="c_attendancetype")
	public String getAttendancetype() {
		return attendancetype;
	}
	public void setAttendancetype(String attendancetype) {
		this.attendancetype = attendancetype;
	}
	@Column(name="c_time1")
	public String getTime1() {
		return time1;
	}
	public void setTime1(String time1) {
		this.time1 = time1;
	}
	@Column(name="c_time2")
	public String getTime2() {
		return time2;
	}
	public void setTime2(String time2) {
		this.time2 = time2;
	}
	@Column(name="c_time3")
	public String getTime3() {
		return time3;
	}
	public void setTime3(String time3) {
		this.time3 = time3;
	}
	@Column(name="c_time4")
	public String getTime4() {
		return time4;
	}
	public void setTime4(String time4) {
		this.time4 = time4;
	}
	@Column(name="c_time5")
	public String getTime5() {
		return time5;
	}
	public void setTime5(String time5) {
		this.time5 = time5;
	}
	@Column(name="c_time6")
	public String getTime6() {
		return time6;
	}
	public void setTime6(String time6) {
		this.time6 = time6;
	}
	@Column(name="morn_islast")
	public String getMorn_islast() {
		return morn_islast;
	}
	public void setMorn_islast(String morn_islast) {
		this.morn_islast = morn_islast;
	}
	@Column(name="after_isleave")
	public String getAfter_isleave() {
		return after_isleave;
	}
	public void setAfter_isleave(String after_isleave) {
		this.after_isleave = after_isleave;
	}
	@Column(name="noon_islast")
	public String getNoon_islast() {
		return noon_islast;
	}
	public void setNoon_islast(String noon_islast) {
		this.noon_islast = noon_islast;
	}
	@Column(name="noon_isleave")
	public String getNoon_isleave() {
		return noon_isleave;
	}
	public void setNoon_isleave(String noon_isleave) {
		this.noon_isleave = noon_isleave;
	}
	@Column(name="iscatch")
	public String getIscatch() {
		return iscatch;
	}
	public void setIscatch(String iscatch) {
		this.iscatch = iscatch;
	}
	@Column(name="worktime")
	public double getWorktime() {
		return worktime;
	}
	public void setWorktime(double worktime) {
		this.worktime = worktime;
	}
	@Column(name="all_worktime")
	public double getAll_worktime() {
		return all_worktime;
	}
	public void setAll_worktime(double all_worktime) {
		this.all_worktime = all_worktime;
	}
	@Column(name="workday")
	public int getWorkday() {
		return workday;
	}
	public void setWorkday(int workday) {
		this.workday = workday;
	}
	@Column(name="validateday")
	public int getValidateday() {
		return validateday;
	}
	public void setValidateday(int validateday) {
		this.validateday = validateday;
	}
	@Column(name="c_mtime2")
	public String getMtime2() {
		return mtime2;
	}
	public void setMtime2(String mtime2) {
		this.mtime2 = mtime2;
	}
	@Column(name="mindate")
	public String getMindate() {
		return mindate;
	}
	public void setMindate(String mindate) {
		this.mindate = mindate;
	}
	@Column(name="maxdate")
	public String getMaxdate() {
		return maxdate;
	}
	public void setMaxdate(String maxdate) {
		this.maxdate = maxdate;
	}
}
