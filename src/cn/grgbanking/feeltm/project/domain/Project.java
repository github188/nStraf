package cn.grgbanking.feeltm.project.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

import cn.grgbanking.feeltm.base.BaseDomain;

/**
 * 项目
 * @author lhyan3
 * 2014年6月25日
 */
@Entity
@Table(name="OA_PROJECT")
public class Project extends BaseDomain{
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid.hex")
	@Column(name="C_ID",unique=true,nullable=false)
	private String id;
	@Column(name="C_NAME")
	private String name;//名字
	@Column(name="C_MANAGERKEY")
	private String proManagerId;//项目经理id
	@Column(name="C_MANAGERNAME")
	private String proManager;//项目经理名字
	@Column(name="D_PLAN_STARTDATE")
	private Date planStartTime;//计划开始时间
	@Column(name="D_PLAN_ENDDATE")
	private Date planEndTime;//计划结束时间
	@Column(name="D_FACT_STARTDATE")
	private Date factStartTime;//实际开始时间
	@Column(name="D_FACT_ENDDATE")
	private Date factEndTime;//实际结束时间
	@Column(name="C_DESC")
	private String description;//描述
	
	@Column(name="C_PROJECT_TYPE")
	private String projectType;//项目类型
	
	@Column(name="C_PROJECT_NO")
	private String projectNo;//项目编号
	
	@Column(name="C_CUSTOMER")
	private String customer;//客户名称

	@Column(name="I_MEMBERS")
	private Integer members;//成员数	
	
	@Column(name="C_ISEND")
	private String isEnd;//是否完结	0：否 1：是
	
	@Column(name="EXT1")
	private String isVisual;//是否虚拟项目 0：否 1：是
	
	@Column(name="C_PROVINCE")
	private String province;//项目所在省份
	
	@Column(name="C_CITY")
	private String city;//项目所在市
	
	//mappedby 表示关系交由对方维护，而不是新建一个表来维护
	@OneToMany(cascade=CascadeType.ALL, mappedBy="project",fetch=FetchType.EAGER)
	@Fetch(FetchMode.SELECT )
	private List<UserProject> userProjects;
	
	@OneToMany(cascade=CascadeType.ALL,mappedBy="project",fetch=FetchType.EAGER)
	@Fetch(FetchMode.SELECT )
	private List<ProjectAttendance> attendances;
	
	@OneToMany(cascade=CascadeType.ALL,mappedBy="project",fetch=FetchType.EAGER)
	@Fetch(FetchMode.SELECT )
	private List<ProjectResourcePlan> projectResourcePlan;
	/*@OneToMany(cascade=CascadeType.ALL,mappedBy="project",fetch=FetchType.LAZY)
	private List<UserProject> userProjects;
	
	@OneToMany(cascade=CascadeType.ALL,mappedBy="project",fetch=FetchType.LAZY)
	private List<ProjectAttendance> attendances;*/

	public Project(){}
	
	public Project(String id,String name,String proManagerId,String proManager,String projectType,String isEnd){
		this.id=id;
		this.name=name;
		this.proManagerId=proManagerId;
		this.proManager=proManager;
		this.projectType=projectType;
		this.isEnd = isEnd;
	}
	public Project(String id,String name,String proManagerId,String proManager,String projectType,String isEnd,String isVisual){
		this.id=id;
		this.name=name;
		this.proManagerId=proManagerId;
		this.proManager=proManager;
		this.projectType=projectType;
		this.isEnd = isEnd;
		this.isVisual = isVisual;
	}
	
	
	public Project(String id,String name,String proManagerId,String proManager,String projectType){
		this.id=id;
		this.name=name;
		this.proManagerId=proManagerId;
		this.proManager=proManager;
		this.projectType=projectType;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProManagerId() {
		return proManagerId;
	}
	public void setProManagerId(String proManagerId) {
		this.proManagerId = proManagerId;
	}
	public String getProManager() {
		return proManager;
	}
	public void setProManager(String proManager) {
		this.proManager = proManager;
	}
	public Date getPlanStartTime() {
		return planStartTime;
	}
	public void setPlanStartTime(Date planStartTime) {
		this.planStartTime = planStartTime;
	}
	public Date getPlanEndTime() {
		return planEndTime;
	}
	public void setPlanEndTime(Date planEndTime) {
		this.planEndTime = planEndTime;
	}
	public Date getFactStartTime() {
		return factStartTime;
	}
	public void setFactStartTime(Date factStartTime) {
		this.factStartTime = factStartTime;
	}
	public Date getFactEndTime() {
		return factEndTime;
	}
	public void setFactEndTime(Date factEndTime) {
		this.factEndTime = factEndTime;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getProjectType() {
		return projectType;
	}
	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}
	public String getProjectNo() {
		return projectNo;
	}
	public void setProjectNo(String projectNo) {
		this.projectNo = projectNo;
	}
	public String getCustomer() {
		return customer;
	}
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	public Integer getMembers() {
		return members;
	}
	public void setMembers(Integer members) {
		this.members = members;
	}
	
	
	public String getIsEnd() {
		return isEnd;
	}

	public void setIsEnd(String isEnd) {
		this.isEnd = isEnd;
	}

	public List<UserProject> getUserProjects() {
		return userProjects;
	}
	public void setUserProjects(List<UserProject> userProjects) {
		this.userProjects = userProjects;
	}
	public List<ProjectAttendance> getAttendances() {
		return attendances;
	}
	public void setAttendances(List<ProjectAttendance> attendances) {
		this.attendances = attendances;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<ProjectResourcePlan> getProjectResourcePlan() {
		return projectResourcePlan;
	}
	public void setProjectResourcePlan(List<ProjectResourcePlan> projectResourcePlan) {
		this.projectResourcePlan = projectResourcePlan;
	}

	public String getIsVisual() {
		return isVisual;
	}

	public void setIsVisual(String isVisual) {
		this.isVisual = isVisual;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
	
}
