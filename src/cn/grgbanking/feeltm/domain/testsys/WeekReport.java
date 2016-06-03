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
 * <td><s:property value="startTime"/></td> <td><s:property value="endTime"/></td>
 * <td><s:property value="groupName"/></td> <td><s:property value="finishInfo"/>
 * </td> <td ><s:property value="responsor"/></a></td>
 * 
 * @author Administrator <td align="center" bgcolor="#999999"><div
 *         align="center">任务概述：</div></td> <td ><input
 *         name="weekReport.taskOverview" type="text" id="taskDesc"
 *         maxlength="200" size="60" value=''>&nbsp;</td> <td align="center" bgcolor="#999999">
 *         <div align="center">完成情况：</div></td> <td ><div align="left"> <select
 *         name="weekReport.finishInfo" id="finishInfo" style="width: 133px"
 *         onChange="cheshit(this)"> <option value="0">很差</option> <option
 *         value="1">较差</option> <option value="2" selected="true">正常</option>
 *         <option value="3">良好</option> <option value="4">优秀</option>
 *         </select></div></td> </tr>
 *         <tr>
 *         <td><div align="center">任务描述：<font color="#FF0000">*</font></div></td>
 *         <td colspan="3"><div align="left"><textarea
 *         name="weekReport.taskDesc" type="text" id="taskInfo" rows="6"
 *         cols="82" ></textarea></div></td>
 *         </tr>
 *         <tr>
 *         <td><div align="center">备注：</div></td>
 *         <td colspan="3"><div align="left"><textarea name="weekReport.note"
 *         type="text" id="note" rows="3" cols="82" ></textarea></div></td>
 *         </tr>
 *         </table>
 * 
 */

@Entity
@Table(name="REPORT_WEEK")
public class WeekReport implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	@Column(name = "id", unique = true, nullable = false)
	private String id;
	@Column(name = "month_day")
	@Temporal(TemporalType.DATE)
	private Date monthDay;
	@Column(name = "group_name")
	private String groupName;
	@Column(name = "start_date")
	@Temporal(TemporalType.DATE)
	private Date startDate;
	
	@Column(name = "modify_date")
	private String modifyDate;
	@Column(name = "end_date")
	@Temporal(TemporalType.DATE)
	private Date endDate;
	@Column(name = "task_overview")
	private String taskOverview;
	@Column(name = "finish_info")
	private int finishInfo;
	@Column(name = "task_desc")
	private String taskDesc;
	private String note;
	private String responsor;
	@Column(name="lock_flag")
	private String lockFlag;   //'0' is init,'1'is busy
	@Column(name="current_name")
	private String currentName;   //'0' is init,'1'is busy
	@Column(name = "handle_time")
	private String handle_time;
	
	@Transient
	private String startString;
	@Transient
	private String endString;
	@Transient
	private String finishInfoString;
	@Transient
	private String monthDayString;
	
	@Transient
	private Date nextStartDate;
	
	@Transient
	private Date nextEndDate;
	
	
	
	public WeekReport() {
		
	}

	public WeekReport(String id) {
		super();
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getMonthDay() {
		return monthDay;
	}

	public void setMonthDay(Date monthDay) {
		this.monthDay = monthDay;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getTaskOverview() {
		return taskOverview;
	}

	public void setTaskOverview(String taskOverview) {
		this.taskOverview = taskOverview;
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

	public String getStartString() {
		return startString;
	}

	public void setStartString(String startString) {
		this.startString = startString;
	}

	public String getEndString() {
		return endString;
	}

	public void setEndString(String endString) {
		this.endString = endString;
	}

	public String getFinishInfoString() {
		return finishInfoString;
	}

	public void setFinishInfoString(String finishInfoString) {
		this.finishInfoString = finishInfoString;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getMonthDayString() {
		return monthDayString;
	}

	public void setMonthDayString(String monthDayString) {
		this.monthDayString = monthDayString;
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
	
	

	public Date getNextStartDate() {
		return nextStartDate;
	}

	public void setNextStartDate(Date nextStartDate) {
		this.nextStartDate = nextStartDate;
	}

	public Date getNextEndDate() {
		return nextEndDate;
	}

	public void setNextEndDate(Date nextEndDate) {
		this.nextEndDate = nextEndDate;
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
		WeekReport other = (WeekReport) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String getHandle_time() {
		return handle_time;
	}

	public void setHandle_time(String handle_time) {
		this.handle_time = handle_time;
	}
	
	
	
}
