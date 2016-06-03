package cn.grgbanking.feeltm.domain.testsys;

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


@Entity
@Table(name ="sys_info")
//@SequenceGenerator(name="pnoSeq",sequenceName="pno_seq",initialValue=00001,allocationSize=1)
public class SysInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	@Column(name = "C_ID", unique = true, nullable = false)
	private String id;// id
	@Column(name = "C_USERNAME")
	private String username; //姓名
	@Column(name = "C_TEL")
	private String tel; //电话
	@Column(name = "C_MOBILE")
	private String mobile;   //手机号码
	@Column(name = "C_EMAIL")
	private String email; //电子邮箱
	@Column(name = "group_name")
	private String groupname; //组别
	
	
	@Column(name = "C_CONTACTSMAN")
	private String contactsman; //紧急联系人
	@Column(name = "C_EDUCATION")
	private String education; //教育程度
	
	@Column(name = "C_TECHNICALTITLE")
	private String  technicaltitle; //技术职称
	
	@Column(name = "C_BIRTHDATE")
	@Temporal(TemporalType.DATE)
	private Date birthdate;  //出生日期
	
	@Column(name = "C_NATIONAL")
	private String  national; // 民族
	
	@Column(name = "C_POLITICALSTATUS")
	private String  politicalstatus;// 民族
	
	@Column(name = "C_BIRTHPLACE")
	private String  birthplace;// 籍贯
	
	
	@Column(name = "C_ENGLISHSKILL")
	private String  englishskill;// 英语水平
	
	@Column(name = "C_GRADUATESCHOOL")
	private String  graduateschool;// 毕业院校
	
	@Column(name = "C_STATUS")
	private String  status;// 状态
	
	@Column(name = "C_LEAVEDATE")
	@Temporal(TemporalType.DATE)
	private Date leavedate;  //离职日期
	
	@Column(name = "C_LEAVEREASON")
	private String  leavereason;// 离职原因
	
	@Column(name = "C_REMARK")
	private String  remark;// 备注
	
	@Column(name = "C_ADDRESS")
	private String  address;// 家庭住址
	
	
	@Column(name = "C_ADDRESSNUM")
	private String  addressnum;// 家庭住址邮政编码
	
	@Column(name = "C_RELATION")
	private String  relation;// 紧急联系人关系
	
	@Column(name = "C_CONTACTSTELNUM")
	private String  contactstelnum;// 紧急联系人电话
	
	@Column(name = "C_CALENDERTYPE")
	private String  calendertype;//历法类型
	
	@Column(name = "C_STARTWORDDATE")
	@Temporal(TemporalType.DATE)
	private Date startworddtae;  //参加工作日期
	
	@Column(name = "D_GRADUATEDATE")
	@Temporal(TemporalType.DATE)
	private Date graduatedate;  //毕业日期
	
	@Column(name = "C_STUDYSUBJECT")
	private String  studysubject;//历法类型
	
	@Column(name = "C_WORKINGDATE")
	@Temporal(TemporalType.DATE)
	private Date workingdate;  //入职日期
	
	@Column(name = "C_QQ")
	private String  qq;//QQ
	
	@Column(name = "C_LIKE")
	private String  like;//个人爱好
	
	@Column(name = "C_HUKOUADDRESS")
	private String  hukouadress;//户口地址
	
	@Column(name = "C_HUKOUNUM")
	private String  hukounum;//户口地址邮编
	
	@Column(name = "C_CONTACTSCALLNUM")
	private String  contactscallnum;//紧急联系人手机号码 
	
	@Column(name = "C_WORKSTATION")
	private String  workstation;//个人岗位
	
	@Column(name = "C_MARRYSTATUS")
	private String  marrystatus;//婚姻状况
	
	@Column(name = "C_HUKOUKIND")
	private String  hukoukind;//户口性质

	
	
	public SysInfo(){
		
	}
   public SysInfo( String id){
		this.id=id;
	}	

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getGroupname() {
		return groupname;
	}
	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}
	public String getContactsman() {
		return contactsman;
	}
	public void setContactsman(String contactsman) {
		this.contactsman = contactsman;
	}
	public String getEducation() {
		return education;
	}
	public void setEducation(String education) {
		this.education = education;
	}

	public Date getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}
	public String getNational() {
		return national;
	}
	public void setNational(String national) {
		this.national = national;
	}
	public String getPoliticalstatus() {
		return politicalstatus;
	}
	public void setPoliticalstatus(String politicalstatus) {
		this.politicalstatus = politicalstatus;
	}
	public String getBirthplace() {
		return birthplace;
	}
	public void setBirthplace(String birthplace) {
		this.birthplace = birthplace;
	}
	public String getEnglishskill() {
		return englishskill;
	}
	public void setEnglishskill(String englishskill) {
		this.englishskill = englishskill;
	}
	public String getGraduateschool() {
		return graduateschool;
	}
	public void setGraduateschool(String graduateschool) {
		this.graduateschool = graduateschool;
	}
	public Date getLeavedate() {
		return leavedate;
	}
	public void setLeavedate(Date leavedate) {
		this.leavedate = leavedate;
	}
	public String getLeavereason() {
		return leavereason;
	}
	public void setLeavereason(String leavereason) {
		this.leavereason = leavereason;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAddressnum() {
		return addressnum;
	}
	public void setAddressnum(String addressnum) {
		this.addressnum = addressnum;
	}
	public String getRelation() {
		return relation;
	}
	public void setRelation(String relation) {
		this.relation = relation;
	}
	public String getContactstelnum() {
		return contactstelnum;
	}
	public void setContactstelnum(String contactstelnum) {
		this.contactstelnum = contactstelnum;
	}
	public String getCalendertype() {
		return calendertype;
	}
	public void setCalendertype(String calendertype) {
		this.calendertype = calendertype;
	}
	public Date getStartworddtae() {
		return startworddtae;
	}
	public void setStartworddtae(Date startworddtae) {
		this.startworddtae = startworddtae;
	}
	public Date getGraduatedate() {
		return graduatedate;
	}
	public void setGraduatedate(Date graduatedate) {
		this.graduatedate = graduatedate;
	}
	public String getStudysubject() {
		return studysubject;
	}
	public void setStudysubject(String studysubject) {
		this.studysubject = studysubject;
	}

	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getTechnicaltitle() {
		return technicaltitle;
	}
	public void setTechnicaltitle(String technicaltitle) {
		this.technicaltitle = technicaltitle;
	}
	public String getLike() {
		return like;
	}
	public void setLike(String like) {
		this.like = like;
	}
	public String getHukouadress() {
		return hukouadress;
	}
	public void setHukouadress(String hukouadress) {
		this.hukouadress = hukouadress;
	}
	public String getHukounum() {
		return hukounum;
	}
	public void setHukounum(String hukounum) {
		this.hukounum = hukounum;
	}
	public String getContactscallnum() {
		return contactscallnum;
	}
	public void setContactscallnum(String contactscallnum) {
		this.contactscallnum = contactscallnum;
	}

	public String getMarrystatus() {
		return marrystatus;
	}
	public void setMarrystatus(String marrystatus) {
		this.marrystatus = marrystatus;
	}
	public String getHukoukind() {
		return hukoukind;
	}
	public void setHukouking(String hukoukind) {
		this.hukoukind = hukoukind;
	}
	public Date getWorkingdate() {
		return workingdate;
	}
	public void setWorkingdate(Date workingdate) {
		this.workingdate = workingdate;
	}
	public String getWorkstation() {
		return workstation;
	}
	public void setWorkstation(String workstation) {
		this.workstation = workstation;
	}

	public void setHukoukind(String hukoukind) {
		this.hukoukind = hukoukind;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
