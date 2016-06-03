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
@Table(name = "REPORT_MONTH")
public class MonthReport {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	@Column(name = "id", unique = true, nullable = false)
	private String id;

	@Column(name = "start_date")
	@Temporal(TemporalType.DATE)
	private Date startDate;

	@Column(name = "group_name")
	private String groupName;

	@Column(name = "finish_info")
	private int finishInfo;

	@Column(name = "task_desc")
	private String taskDesc;
	
	@Column(name = "modify_date")
	private String modifyDate;

	private String note;

	private String responsor;

	@Column(name="lock_flag")
	private String lockFlag;   //'0' is init,'1'is busy
	@Column(name="current_name")
	private String currentName;   //'0' is init,'1'is busy
	
	@Transient
	private String finishInfoString;
	@Transient
	private String startDateString;
	
	
	public MonthReport(){
		
	}
	
	public MonthReport(String id){
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

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public int getFinishInfo() {
		return finishInfo;
	}

	public void setFinishInfo(int finishInfo) {
		this.finishInfo = finishInfo;
	}

	public String getTaskDesc() {
		return taskDesc;
	}

	public void setTaskDesc(String taskDesc) {
		this.taskDesc = taskDesc;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getResponsor() {
		return responsor;
	}

	public void setResponsor(String responsor) {
		this.responsor = responsor;
	}
	
	
	

	public String getFinishInfoString() {
		return finishInfoString;
	}

	public void setFinishInfoString(String finishInfoString) {
		this.finishInfoString = finishInfoString;
	}

	public String getStartDateString() {
		return startDateString;
	}

	public void setStartDateString(String startDateString) {
		this.startDateString = startDateString;
	}
	
	

	public String getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MonthReport other = (MonthReport) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
}
