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
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

/**
 * id int NOT NULL auto_increment,
  start_date date NOT NULL,
  prj_name varchar(50) NOT NULL,
  task_desc varchar(100) NOT NULL,
  attachment varchar(100) NOT NULL,
  finish_rate varchar(10) ,
  status char(10) ,
  task_reason  varchar(100) ,
  management int,
  requirement	int,
  design int,
  code int,
  _test int,
  other int,
  project int,
  subtotal int,
  user_id int NOT NULL,
 * @author yt
 *
 */
@Entity
@Table(name="REPORT_INFO")
public class WorkReport implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	@Column(name = "id", unique = true, nullable = false)
	private String id;
	@Column(name="start_date")
	@Temporal(TemporalType.DATE)
	private Date startDate;
	@Column(name="prj_name")
	private String prjName;
	@Column(name="task_desc")
	private String taskDesc;
	private String attachment;
	@Column(name="finish_rate")
	private String finishRate;
	private String status;
	@Column(name="task_reason")
	private String taskReason;
	@Column(name="management")
	private double managerment=0;
	private double requirement=0;
	private double design=0;
	private double test=0;
	private double other=0;
	private  double project=0;
	private double subtotal=0;
	private double code=0;
	@Column(name="user_id")
	private String username;     //此处对应SysUser里的username
	@Column(name="group_name")
	private String groupName;
	@Column(name = "modify_date")
	private String modifyDate;
	@Transient
	private String dateString;
	public WorkReport() {
	}
	public WorkReport(String id) {
		this.id=id;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public String getPrjName() {
		return prjName;
	}
	public void setPrjName(String prjName) {
		this.prjName = prjName;
	}
	public String getTaskDesc() {
		return taskDesc;
	}
	public void setTaskDesc(String taskDesc) {
		this.taskDesc = taskDesc;
	}
	public String getAttachment() {
		return attachment;
	}
	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}
	public String getFinishRate() {
		return finishRate;
	}
	public void setFinishRate(String finishRate) {
		this.finishRate = finishRate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTaskReason() {
		return taskReason;
	}
	public void setTaskReason(String taskReason) {
		this.taskReason = taskReason;
	}

	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	public String getDateString() {
		return dateString;
	}
	public void setDateString(String dateString) {
		this.dateString = dateString;
	}

	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public double getManagerment() {
		return managerment;
	}
	public void setManagerment(double managerment) {
		this.managerment = managerment;
	}
	public double getRequirement() {
		return requirement;
	}
	public void setRequirement(double requirement) {
		this.requirement = requirement;
	}
	public double getDesign() {
		return design;
	}
	public void setDesign(double design) {
		this.design = design;
	}
	public double getTest() {
		return test;
	}
	public void setTest(double test) {
		this.test = test;
	}
	public double getOther() {
		return other;
	}
	public void setOther(double other) {
		this.other = other;
	}
	public double getProject() {
		return project;
	}
	public void setProject(double project) {
		this.project = project;
	}
	public double getSubtotal() {
		return subtotal;
	}
	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}
	public double getCode() {
		return code;
	}
	public void setCode(double code) {
		this.code = code;
	}
	public String getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}
	
	
	
}
