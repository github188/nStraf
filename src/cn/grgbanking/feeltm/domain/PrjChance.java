package cn.grgbanking.feeltm.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "OA_PRJCHANCE")
public class PrjChance implements Serializable{
private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	
	@Column(name = "C_ID", unique = true, nullable = false)
	private String id;//主键
	
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	@Column(name = "C_PRJNAME", nullable = false)
	private String prjName;//项目名称
	
	@Column(name = "C_CLIENT")
	private String client;//客户
	
	@Column(name = "C_CLIENTLINKMAN")
	private String clientLinkMan;//客户联系人
	
	@Column(name = "C_LINKMANDEPT")
	private String linkManDept;//联系人部门
	
	@Column(name = "C_LINKMANPOSITION")
	private String linkManPosition;//联系人职位
	
	@Column(name = "C_CLIENTTYPE")
	private String clientType;//客户类别
	
	@Column(name = "C_FOLLOWMAN")
	private String followMan;//跟进人
	
	@Column(name = "D_LASTFOLLOWDATE")
	private Date lastFollowDate;//最后跟进日期
	
	@Column(name = "D_CREATDATE")
	private Date creatDate;//登记日期
	
	@Column(name = "C_BUDGET")
	private String budget;//项目预算
	
	@Column(name = "C_PRJSTAGE")
	private String prjStage;//项目阶段
	
	@Column(name = "C_PRJRESULT")
	private String prjResult;//项目结果
	
	@Column(name = "C_NOTE")
	private String note;//备注
	
	@Column(name = "C_DESC")
	private String description;//商机描述
	
	@Column(name = "C_AREA")
	private String area;//所属区域
	
	@Column(name = "C_PROVINCE")
	private String province;//所在省份
	
	@Column(name = "C_CLIENTMANAGER")
	private String clientManager;//客户经理
	
	@Column(name = "C_COPYEMAIL")
	private String copyEmail;//跟进提醒邮件抄送人
	
	@Column(name = "C_UPDATEMAN")
	private String updateMan;//更新人
	
	@Column(name = "C_UPDATE")
	private Date upDate;//更新时间
	
	@Column(name = "D_LASTSENDEMAILTIME")
	private Date lastSendEmailTime;//最后一次发送邮件的时间
	
	@Column(name = "C_CONTACTORTEL")
	private String  contactortel;//联系人的联系方式
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPrjName() {
		return prjName;
	}

	public void setPrjName(String prjName) {
		this.prjName = prjName;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getClientLinkMan() {
		return clientLinkMan;
	}

	public void setClientLinkMan(String clientLinkMan) {
		this.clientLinkMan = clientLinkMan;
	}

	public String getLinkManDept() {
		return linkManDept;
	}

	public void setLinkManDept(String linkManDept) {
		this.linkManDept = linkManDept;
	}

	public String getLinkManPosition() {
		return linkManPosition;
	}

	public void setLinkManPosition(String linkManPosition) {
		this.linkManPosition = linkManPosition;
	}

	public String getClientType() {
		return clientType;
	}

	public void setClientType(String clientType) {
		this.clientType = clientType;
	}

	public String getFollowMan() {
		return followMan;
	}

	public void setFollowMan(String followMan) {
		this.followMan = followMan;
	}

	public Date getLastFollowDate() {
		return lastFollowDate;
	}

	public void setLastFollowDate(Date lastFollowDate) {
		this.lastFollowDate = lastFollowDate;
	}

	public Date getCreatDate() {
		return creatDate;
	}

	public void setCreatDate(Date creatDate) {
		this.creatDate = creatDate;
	}

	public String getBudget() {
		return budget;
	}

	public void setBudget(String budget) {
		this.budget = budget;
	}

	public String getPrjStage() {
		return prjStage;
	}

	public void setPrjStage(String prjStage) {
		this.prjStage = prjStage;
	}

	public String getPrjResult() {
		return prjResult;
	}

	public void setPrjResult(String prjResult) {
		this.prjResult = prjResult;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getClientManager() {
		return clientManager;
	}

	public void setClientManager(String clientManager) {
		this.clientManager = clientManager;
	}

	public String getUpdateMan() {
		return updateMan;
	}

	public void setUpdateMan(String updateMan) {
		this.updateMan = updateMan;
	}

	public Date getUpDate() {
		return upDate;
	}

	public void setUpDate(Date upDate) {
		this.upDate = upDate;
	}

	public Date getLastSendEmailTime() {
		return lastSendEmailTime;
	}

	public void setLastSendEmailTime(Date lastSendEmailTime) {
		this.lastSendEmailTime = lastSendEmailTime;
	}

	public String getCopyEmail() {
		return copyEmail;
	}

	public void setCopyEmail(String copyEmail) {
		this.copyEmail = copyEmail;
	}

	public String getContactortel() {
		return contactortel;
	}

	public void setContactortel(String contactortel) {
		this.contactortel = contactortel;
	}

	
	
	
}
