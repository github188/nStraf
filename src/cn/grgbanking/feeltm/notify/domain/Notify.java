package cn.grgbanking.feeltm.notify.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 通知 2014-4-29
 * @author lhyan3 
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name="OA_NOTIFY")
public class Notify implements Serializable{

	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	@Column(name="C_ID", unique = true, nullable = false)
	private String id;//主键id
	@Column(name="C_NOTIFYNUM")
	private String notifyNum;//流水号
	@Column(name="C_TYPE")
	private String type;//类型
	@Column(name="C_TITLE")
	private String title;//标题
	@Column(name="C_CONTENT")
	private String content;//内容
	@Column(name="I_OATYPE")
	private int oatype=0;//oa发送 默认为0不发送
	@Column(name="I_EMAILTYPE")
	private int emailtype=0;//邮件发送
	@Column(name="I_MOBILETYPE")
	private int mobiletype=0;//手机发送
	@Column(name="C_STATUS")
	private String status;//状态
	@Column(name="D_WRITETIME")
	private Date writeTime;//填写时间
	@Column(name="C_SENDER")
	private String sender;//发送人id
	@Column(name="D_SENDTIME")
	private Date sendTime;//发送时间
	@Column(name="C_APPROVER")
	private String approver;//审批人
	@Column(name="C_USERNAME")
	private String username;
	@Column(name="C_APPROVERNAME")
	private String approverName;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNotifyNum() {
		return notifyNum;
	}
	public void setNotifyNum(String notifyNum) {
		this.notifyNum = notifyNum;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getOatype() {
		return oatype;
	}
	public void setOatype(int oatype) {
		this.oatype = oatype;
	}
	public int getEmailtype() {
		return emailtype;
	}
	public void setEmailtype(int emailtype) {
		this.emailtype = emailtype;
	}
	public int getMobiletype() {
		return mobiletype;
	}
	public void setMobiletype(int mobiletype) {
		this.mobiletype = mobiletype;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public Date getSendTime() {
		return sendTime;
	}
	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
	public String getApprover() {
		return approver;
	}
	public void setApprover(String approver) {
		this.approver = approver;
	}
	public Date getWriteTime() {
		return writeTime;
	}
	public void setWriteTime(Date writeTime) {
		this.writeTime = writeTime;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getApproverName() {
		return approverName;
	}
	public void setApproverName(String approverName) {
		this.approverName = approverName;
	}
}
