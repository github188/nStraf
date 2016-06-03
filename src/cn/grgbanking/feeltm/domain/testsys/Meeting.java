package cn.grgbanking.feeltm.domain.testsys;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "meeting")
public class Meeting {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	@Column(name = "id", unique = true, nullable = false)
	private String id;

	@Column(name = "cur_date_time")
	private Date currentDateTime;

	private String hour;

	private String addr;

	private String subject;

	private String compere; // 主持人

	private String writer; // 记录人

	@Column(name = "attend_persons")
	private String attendPersons;

	@Column(name = "absent_persons")
	private String absentPersons;

	private String content;

	@Column(name = "audit_status")
	private String auditStatus;

	@Column(name = "audit_person")
	private String auditPerson;

	@Column(name = "send_status")
	private String sendStatus;

	@Column(name = "reaudit_person")
	private String reauditPerson;
	
	@Column(name = "reaudit_personstatus")
	private String reauditPersonstatus;
	
	@Column(name = "reaudit_personsuggest")
	private String reauditPersonsuggest;
	
	@Column(name="lock_flag")
	private String lockFlag;   //'0' is init,'1'is busy
	@Column(name="current_name")
	private String currentName;   //'0' is init,'1'is busy
	
	private String main;  //主送
	
	private String copy;  //抄送
	
	private String reAudit;  //复核
	
	private String sign;  //签发

	public Meeting() {
	}

	public Meeting(String id) {
		super();
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCurrentDateTime() {
		return currentDateTime;
	}

	public void setCurrentDateTime(Date currentDateTime) {
		this.currentDateTime = currentDateTime;
	}

	public String getHour() {
		return hour;
	}

	public void setHour(String hour) {
		this.hour = hour;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getCompere() {
		return compere;
	}

	public void setCompere(String compere) {
		this.compere = compere;
	}

	public String getWriter() {
		return writer;
	}

	public void setWriter(String writer) {
		this.writer = writer;
	}

	public String getAttendPersons() {
		return attendPersons;
	}

	public void setAttendPersons(String attendPersons) {
		this.attendPersons = attendPersons;
	}

	public String getAbsentPersons() {
		return absentPersons;
	}

	public void setAbsentPersons(String absentPersons) {
		this.absentPersons = absentPersons;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}

	public String getAuditPerson() {
		return auditPerson;
	}

	public void setAuditPerson(String auditPerson) {
		this.auditPerson = auditPerson;
	}

	public String getSendStatus() {
		return sendStatus;
	}

	public void setSendStatus(String sendStatus) {
		this.sendStatus = sendStatus;
	}

	public String getReauditPerson() {
		return reauditPerson;
	}

	public void setReauditPerson(String reauditPerson) {
		this.reauditPerson = reauditPerson;
	}

	public String getMain() {
		return main;
	}

	public void setMain(String main) {
		this.main = main;
	}

	public String getCopy() {
		return copy;
	}

	public void setCopy(String copy) {
		this.copy = copy;
	}

	public String getReAudit() {
		return reAudit;
	}

	public void setReAudit(String reAudit) {
		this.reAudit = reAudit;
	}

	public String getReauditPersonstatus() {
		return reauditPersonstatus;
	}

	public void setReauditPersonstatus(String reauditPersonstatus) {
		this.reauditPersonstatus = reauditPersonstatus;
	}

	public String getReauditPersonsuggest() {
		return reauditPersonsuggest;
	}

	public void setReauditPersonsuggest(String reauditPersonsuggest) {
		this.reauditPersonsuggest = reauditPersonsuggest;
	}

	public String getLockFlag() {
		return lockFlag;
	}

	public void setLockFlag(String lockFlag) {
		this.lockFlag = lockFlag;
	}

	public String getCurrentName() {
		return currentName;
	}

	public void setCurrentName(String currentName) {
		this.currentName = currentName;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
}
