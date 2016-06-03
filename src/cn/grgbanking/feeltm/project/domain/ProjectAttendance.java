package cn.grgbanking.feeltm.project.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import cn.grgbanking.feeltm.base.BaseDomain;

@Entity
@Table(name="OA_PROJECT_ATTENDANCE")
public class ProjectAttendance extends BaseDomain{
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid.hex")
	@Column(name="C_ID",unique=true,nullable=false)
	private String id;
	@Column(name="C_ATTENDANCE_TYPE")
	private String attendanceType;//考勤类型
	@Column(name="D_ENTRY_TIME")
	private Date entryTime;//考勤进入时间
	@Column(name="D_EXIT_TIME")
	private Date exitTime;//考勤退出时间
	@Column(name="C_SIGNPLACE")
	private String signPlace;//签到地点
	@Column(name="D_LATITUDE")
	private Double latitude;//纬度
	@Column(name="D_LONGITUDE")
	private Double longitude;//经度
	/**
	 * 更新者ID
	 */
	@Column(name="C_UPDATEUSER_ID")
	private Double updateUserId;
	/**
	 * 更新者名
	 */
	@Column(name="C_UPDATEUSER_NAME")
	private Double updateUserName;
	/**
	 * 更新时间
	 */
	@Column(name="D_UPDATE_TIME")
	private Date updateTime;
	/**
	 * 扩展字段1
	 */
	@Column(name="EXT1")
	private String ext1;
	/**
	 * 扩展字段2
	 */
	@Column(name="EXT2")
	private String ext2;
	/**
	 * 扩展字段3
	 */
	@Column(name="EXT3")
	private String ext3;
	/**
	 * 扩展字段3
	 */
	@Column(name="EXT4")
	private String ext4;
	/**
	 * 扩展字段5
	 */
	@Column(name="EXT5")
	private Date ext5;
	/**
	 * 扩展字段6
	 */
	@Column(name="EXT6")
	private Date ext6;
	/**
	 * 扩展字段7
	 */
	@Column(name="EXT7")
	private Double ext7;
	/**
	 * 扩展字段8
	 */
	@Column(name="EXT8")
	private Double ext8;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="C_PROJECT_ID",referencedColumnName="C_ID")
	private Project project;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAttendanceType() {
		return attendanceType;
	}

	public void setAttendanceType(String attendanceType) {
		this.attendanceType = attendanceType;
	}
	/**
	 * 考勤进入时间
	 * @return
	 */
	public Date getEntryTime() {
		return entryTime;
	}

	public void setEntryTime(Date entryTime) {
		this.entryTime = entryTime;
	}
	/**
	 * 考勤退出时间
	 * @return
	 */
	public Date getExitTime() {
		return exitTime;
	}

	public void setExitTime(Date exitTime) {
		this.exitTime = exitTime;
	}

	public String getSignPlace() {
		return signPlace;
	}

	public void setSignPlace(String signPlace) {
		this.signPlace = signPlace;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	/**
	 * @return the updateUserId
	 */
	public Double getUpdateUserId() {
		return updateUserId;
	}

	/**
	 * @param updateUserId the updateUserId to set
	 */
	public void setUpdateUserId(Double updateUserId) {
		this.updateUserId = updateUserId;
	}

	/**
	 * @return the updateUserName
	 */
	public Double getUpdateUserName() {
		return updateUserName;
	}

	/**
	 * @param updateUserName the updateUserName to set
	 */
	public void setUpdateUserName(Double updateUserName) {
		this.updateUserName = updateUserName;
	}

	/**
	 * @return the updateTime
	 */
	public Date getUpdateTime() {
		return updateTime;
	}

	/**
	 * @param updateTime the updateTime to set
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	/**
	 * @return the ext1
	 */
	public String getExt1() {
		return ext1;
	}

	/**
	 * @param ext1 the ext1 to set
	 */
	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}

	/**
	 * @return the ext2
	 */
	public String getExt2() {
		return ext2;
	}

	/**
	 * @param ext2 the ext2 to set
	 */
	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}

	/**
	 * @return the ext3
	 */
	public String getExt3() {
		return ext3;
	}

	/**
	 * @param ext3 the ext3 to set
	 */
	public void setExt3(String ext3) {
		this.ext3 = ext3;
	}

	/**
	 * @return the ext4
	 */
	public String getExt4() {
		return ext4;
	}

	/**
	 * @param ext4 the ext4 to set
	 */
	public void setExt4(String ext4) {
		this.ext4 = ext4;
	}

	/**
	 * @return the ext5
	 */
	public Date getExt5() {
		return ext5;
	}

	/**
	 * @param ext5 the ext5 to set
	 */
	public void setExt5(Date ext5) {
		this.ext5 = ext5;
	}

	/**
	 * @return the ext6
	 */
	public Date getExt6() {
		return ext6;
	}

	/**
	 * @param ext6 the ext6 to set
	 */
	public void setExt6(Date ext6) {
		this.ext6 = ext6;
	}

	/**
	 * @return the ext7
	 */
	public Double getExt7() {
		return ext7;
	}

	/**
	 * @param ext7 the ext7 to set
	 */
	public void setExt7(Double ext7) {
		this.ext7 = ext7;
	}

	/**
	 * @return the ext8
	 */
	public Double getExt8() {
		return ext8;
	}

	/**
	 * @param ext8 the ext8 to set
	 */
	public void setExt8(Double ext8) {
		this.ext8 = ext8;
	}
	
}
