package cn.grgbanking.feeltm.domain.testsys;

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
@Entity
@Table(name = "oa_trip")
public class BusinessTrip {
	private String id;
	private String userid;
	private Date createdate;
	private String username;
	private Date startdate;
	private Date enddate;
	private String detname;
	private String groupname;
	private String tripcity;
	private String clientname;
	private String prjname;
	private String taskdesc;
	private String sumup;
	private String auditing_man;
	private String auditlog;
	private String status;
	private String updateman;
	private Date updatedate;
	private double sumtime;
	private String result;
	private String content;
	private String auditing_manname;
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
	@Temporal(TemporalType.DATE)
	@Column(name="d_createdate")
	public Date getCreatedate() {
		return createdate;
	}
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
	@Column(name="c_username")
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	@Temporal(TemporalType.DATE)
	@Column(name="d_startdate")
	public Date getStartdate() {
		return startdate;
	}
	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}
	@Temporal(TemporalType.DATE)
	@Column(name="d_enddate")
	public Date getEnddate() {
		return enddate;
	}
	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}
	@Column(name="c_detname")
	public String getDetname() {
		return detname;
	}
	public void setDetname(String detname) {
		this.detname = detname;
	}
	@Column(name="c_groupname")
	public String getGroupname() {
		return groupname;
	}
	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}
	@Column(name="c_tripcity")
	public String getTripcity() {
		return tripcity;
	}
	public void setTripcity(String tripcity) {
		this.tripcity = tripcity;
	}
	@Column(name="c_clientname")
	public String getClientname() {
		return clientname;
	}
	public void setClientname(String clientname) {
		this.clientname = clientname;
	}
	@Column(name="c_prjname")
	public String getPrjname() {
		return prjname;
	}
	public void setPrjname(String prjname) {
		this.prjname = prjname;
	}
	@Column(name="c_taskdesc")
	public String getTaskdesc() {
		return taskdesc;
	}
	public void setTaskdesc(String taskdesc) {
		this.taskdesc = taskdesc;
	}
	@Column(name="c_sumup")
	public String getSumup() {
		return sumup;
	}
	public void setSumup(String sumup) {
		this.sumup = sumup;
	}
	@Column(name="c_auditing_man")
	public String getAuditing_man() {
		return auditing_man;
	}
	public void setAuditing_man(String auditing_man) {
		this.auditing_man = auditing_man;
	}
	@Column(name="c_auditlog")
	public String getAuditlog() {
		return auditlog;
	}
	public void setAuditlog(String auditlog) {
		this.auditlog = auditlog;
	}
	@Column(name="c_status")
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Column(name="c_updateman")
	public String getUpdateman() {
		return updateman;
	}
	public void setUpdateman(String updateman) {
		this.updateman = updateman;
	}
	@Temporal(TemporalType.DATE)
	@Column(name="c_update")
	public Date getUpdatedate() {
		return updatedate;
	}
	public void setUpdatedate(Date updatedate) {
		this.updatedate = updatedate;
	}
	@Transient
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	@Transient
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@Column(name="i_sumtime")
	public double getSumtime() {
		return sumtime;
	}
	public void setSumtime(double sumtime) {
		this.sumtime = sumtime;
	}
	@Column(name="c_auditing_manname")
	public String getAuditing_manname() {
		return auditing_manname;
	}
	public void setAuditing_manname(String auditing_manname) {
		this.auditing_manname = auditing_manname;
	}
	
	
}
