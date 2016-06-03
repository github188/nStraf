package cn.grgbanking.feeltm.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@SuppressWarnings("serial")
@Entity
@Table(name = "SYS_USER")
public class SysUser implements Serializable {

	/** identifier field */
	private String userid;//用户标识

	/** identifier field */
	private String username;//用户姓名

	/** identifier field */
	private String workid;//工号

	/** identifier field */
	private String workcompany;//所在单位

	/** identifier field */
	private String tel;//电话

	/** identifier field */
	private String mobile;//手机

	/** identifier field */
	private String email;//邮箱

	/** identifier field */
	private String userpwd;//用户密码

	/** identifier field */
	private Date createdate;//创建时间

	/** identifier field */
	private String isvalid;//用户有效

	/** identifier field */
	private String flag;

	/** identifier field */
	private Date invaliddate;//用户失效时间

	/** identifier field */
	private String orgid;

	/** identifier field */
	private String areaid;

	/** identifier field */
	private String id;//用户id

	/** identifier field */
	private String orgLevel;

	private String orgfloor;

	private String acceptemailType;
	
	private String groupName;
	
	private Integer level;//用户级别 如总经理
	
	//2014-4-22 卢海燕 运通信息平台
	private String relativeName;//紧急联系人
	private String relativeTel;//紧急联系人电话
	private String address;//家庭住址
	private String major;//专业
	private String graduateSchool;//毕业学校
	private String education;//学历
	private Date graduateDate;//毕业时间
	private String status;//状态
	private Date grgBegindate;//入职日期
	/** 离职日期 */
	private Date grgEndDate;//入职日期
	private Date workBegindate;//参加工作日期
	private Date birthDate;//出生日期
	private String idCardNo;//身份证号码
	private String deptName;//部门
	private String postLevel;//岗位级别如高级工程师

	
	private String jobNumber;//公司工号
	private String outNumber;//外派工号：外派人员所在单位工号
	
	private Date updateTime;//更新时间


	/** default constructor */
	public SysUser() {
	}

	/** full constructor */


	@Column(name = "C_USERID")
	public String getUserid() {
		return this.userid;
	}

	public SysUser(String userid, String username, String workid,
			String workcompany, String tel, String mobile, String email,
			String userpwd, Date createdate, String isvalid, String flag,
			Date invaliddate, String orgid, String areaid, String id,
			String orgLevel, String orgfloor, String acceptemailType,
			String groupName, Integer level, String relativeName,
			String relativeTel, String address, String major,
			String graduateSchool, String education, Date graduateDate,
			String status, Date grgBegindate, Date grgEndDate,Date workBegindate,
			Date birthDate, String idCardNo, String deptName, String postLevel,
			String jobNumber, String outNumber, Date updateTime) {
		super();
		this.userid = userid;
		this.username = username;
		this.workid = workid;
		this.workcompany = workcompany;
		this.tel = tel;
		this.mobile = mobile;
		this.email = email;
		this.userpwd = userpwd;
		this.createdate = createdate;
		this.isvalid = isvalid;
		this.flag = flag;
		this.invaliddate = invaliddate;
		this.orgid = orgid;
		this.areaid = areaid;
		this.id = id;
		this.orgLevel = orgLevel;
		this.orgfloor = orgfloor;
		this.acceptemailType = acceptemailType;
		this.groupName = groupName;
		this.level = level;
		this.relativeName = relativeName;
		this.relativeTel = relativeTel;
		this.address = address;
		this.major = major;
		this.graduateSchool = graduateSchool;
		this.education = education;
		this.graduateDate = graduateDate;
		this.status = status;
		this.grgBegindate = grgBegindate;
		this.grgEndDate = grgEndDate;
		this.workBegindate = workBegindate;
		this.birthDate = birthDate;
		this.idCardNo = idCardNo;
		this.deptName = deptName;
		this.postLevel = postLevel;
		this.jobNumber = jobNumber;
		this.outNumber = outNumber;
		this.updateTime = updateTime;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	@Column(name = "C_USERNAME")
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "C_WORKID")
	public String getWorkid() {
		return this.workid;
	}

	public void setWorkid(String workid) {
		this.workid = workid;
	}
    
	@Column(name = "C_JOBNUMBER")
	public String getJobNumber() {
		return jobNumber;
	}

	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}
	
	@Column(name = "C_OUTNUMBER")
	public String getOutNumber() {
		return outNumber;
	}

	public void setOutNumber(String outNumber) {
		this.outNumber = outNumber;
	}

	@Column(name = "C_WORKCOMPANY")
	public String getWorkcompany() {
		return this.workcompany;
	}

	public void setWorkcompany(String workcompany) {
		this.workcompany = workcompany;
	}

	@Column(name = "C_TEL")
	public String getTel() {
		return this.tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	@Column(name = "C_MOBILE")
	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Column(name = "C_EMAIL")
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "C_USERPWD")
	public String getUserpwd() {
		return this.userpwd;
	}

	public void setUserpwd(String userpwd) {
		this.userpwd = userpwd;
	}

	@Column(name = "D_CREATEDATE")
	public Date getCreatedate() {
		return this.createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	@Column(name = "C_ISVALID")
	public String getIsvalid() {
		return this.isvalid;
	}

	public void setIsvalid(String isvalid) {
		this.isvalid = isvalid;
	}

	@Column(name = "C_FLAG")
	public String getFlag() {
		return this.flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	@Column(name = "D_INVALIDDATE")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getInvaliddate() {
		return this.invaliddate;
	}

	public void setInvaliddate(Date invaliddate) {
		this.invaliddate = invaliddate;
	}

	@Column(name = "C_ORGID")
	public String getOrgid() {
		return this.orgid;
	}

	public void setOrgid(String orgid) {
		this.orgid = orgid;
	}

	@Column(name = "C_AREAID")
	public String getAreaid() {
		return this.areaid;
	}

	public void setAreaid(String areaid) {
		this.areaid = areaid;
	}

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	@Column(name = "C_ID", unique = true, nullable = false)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "C_ORG_LEVEL")
	public String getOrgLevel() {
		return this.orgLevel;
	}

	public void setOrgLevel(String orgLevel) {
		this.orgLevel = orgLevel;
	}

	@Column(name = "C_ORGFLOOR")
	public String getOrgfloor() {
		return orgfloor;
	}

	public void setOrgfloor(String orgfloor) {
		this.orgfloor = orgfloor;
	}

	@Column(name = "C_ACCEPTEMAIL_TYPE")
	public String getAcceptemailType() {
		return acceptemailType;
	}

	public void setAcceptemailType(String acceptemailType) {
		this.acceptemailType = acceptemailType;
	}
	
	@Column(name="group_name")
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	@Column(name="I_LEVEL")
	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	@Column(name="C_RELATIVE_NAME")
	public String getRelativeName() {
		return relativeName;
	}

	public void setRelativeName(String relativeName) {
		this.relativeName = relativeName;
	}

	@Column(name="C_RELATIVE_TEL")
	public String getRelativeTel() {
		return relativeTel;
	}

	public void setRelativeTel(String relativeTel) {
		this.relativeTel = relativeTel;
	}

	@Column(name="C_ADDRESS")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name="C_MAJOR")
	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	@Column(name="C_GRADUATE_SCHOOL")
	public String getGraduateSchool() {
		return graduateSchool;
	}

	public void setGraduateSchool(String graduateSchool) {
		this.graduateSchool = graduateSchool;
	}

	@Column(name="C_EDUCATION")
	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	@Column(name="D_GRADUATE_DATE")
	public Date getGraduateDate() {
		return graduateDate;
	}

	public void setGraduateDate(Date graduateDate) {
		this.graduateDate = graduateDate;
	}

	@Column(name="C_STATUS")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name="D_GRG_BEGINDATE")
	public Date getGrgBegindate() {
		return grgBegindate;
	}

	public void setGrgBegindate(Date grgBegindate) {
		this.grgBegindate = grgBegindate;
	}

	/**
	 * @return 离职日期
	 */
	@Column(name="C_GRG_ENDDATE")
	public Date getGrgEndDate() {
		return grgEndDate;
	}

	/**
	 * @param grgEndDate 离职日期
	 */
	public void setGrgEndDate(Date grgEndDate) {
		this.grgEndDate = grgEndDate;
	}

	@Column(name="D_WORK_BEGINDATE")
	public Date getWorkBegindate() {
		return workBegindate;
	}

	public void setWorkBegindate(Date workBegindate) {
		this.workBegindate = workBegindate;
	}

	@Column(name="D_BIRTH_DATE")
	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	@Column(name="C_ID_CARD_NO")
	public String getIdCardNo() {
		return idCardNo;
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}

	@Column(name="C_DETNAME")
	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	@Column(name="C_POST_LEVEL")
	public String getPostLevel() {
		return postLevel;
	}

	public void setPostLevel(String postLevel) {
		this.postLevel = postLevel;
	}
	
	@Column(name="D_UPDATE_TIME")
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	
	

}
