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

@Entity
@Table(name="auto_oa")
public class AutoOa implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	@Column(name = "id", unique = true, nullable = false)
	private String id;
	
	//private String cid;
	
	@Column(name="message_name")
	private String messageName;
	
	@Column(name="status")
	private String status;  //内训，外训，外派
	
	@Column(name="send_week")
	private String sendWeek;
	
	@Column(name="send_month")
	private String sendMonth;
	
	@Column(name="send_day")
	private String sendDay;
	
	
	@Column(name="send_date")
	private String sendDate;
	

	@Column(name="effect_start")
	@Temporal(TemporalType.DATE)
	private Date effectStart;  //培训开始时间 2019-09-19 23:21:11
	

	@Column(name="effect_end")
	@Temporal(TemporalType.DATE)
	private Date effectend;   //
	
	@Column(name="embracer_man")
	private String embracerMan;
	
	@Column(name="description")
	private String description;
	
	@Column(name="create_man")
	private String createMan;
	
	@Column(name="update_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;

	@Column(name="create_date")
	private Date createDate;
	
	@Column(name="send_cycle")
	private String sendCycle;
	
	@Transient
	private String createDateString;
	@Transient
	private String updateDateString;
	
	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getMessageName() {
		return messageName;
	}


	public void setMessageName(String messageName) {
		this.messageName = messageName;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getSendWeek() {
		return sendWeek;
	}


	public void setSendWeek(String sendWeek) {
		this.sendWeek = sendWeek;
	}


	public String getSendMonth() {
		return sendMonth;
	}


	public void setSendMonth(String sendMonth) {
		this.sendMonth = sendMonth;
	}


	public String getSendDay() {
		return sendDay;
	}


	public void setSendDay(String sendDay) {
		this.sendDay = sendDay;
	}


	public String getSendDate() {
		return sendDate;
	}


	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}


	public Date getEffectStart() {
		return effectStart;
	}


	public void setEffectStart(Date effectStart) {
		this.effectStart = effectStart;
	}


	public Date getEffectend() {
		return effectend;
	}


	public void setEffectend(Date effectend) {
		this.effectend = effectend;
	}


	public String getEmbracerMan() {
		return embracerMan;
	}


	public void setEmbracerMan(String embracerMan) {
		this.embracerMan = embracerMan;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getCreateMan() {
		return createMan;
	}


	public void setCreateMan(String createMan) {
		this.createMan = createMan;
	}


	public Date getUpdateDate() {
		return updateDate;
	}


	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}


	public Date getCreateDate() {
		return createDate;
	}


	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}


	public String getSendCycle() {
		return sendCycle;
	}


	public void setSendCycle(String sendCycle) {
		this.sendCycle = sendCycle;
	}


	public String getCreateDateString() {
		return createDateString;
	}


	public void setCreateDateString(String createDateString) {
		this.createDateString = createDateString;
	}


	public String getUpdateDateString() {
		return updateDateString;
	}


	public void setUpdateDateString(String updateDateString) {
		this.updateDateString = updateDateString;
	}


	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	
	
	
	
}
