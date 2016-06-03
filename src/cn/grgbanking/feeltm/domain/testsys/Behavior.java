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
 * 姓名		username 	  varchar2(32)
组别		groupName		varchar2(32)	
日期		startDate	date
奖励事项	prise           varchar2(500)
加分值		priseValue	number
惩罚事项	punish		varchar2(500)
扣分值		punishValue	number
改进意见	note		varchar2(500)
 * @author feel
 *
 */
@Entity
@Table(name="BEHAVIOR")
public class Behavior implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	@Column(name = "id", unique = true, nullable = false)
	private String id;
	
	private String username;
	
	@Column(name="group_name")
	private String groupName;
	
	@Column(name="start_date")
	@Temporal(TemporalType.DATE)
	private Date startDate;
	
	private String prise;
	
	@Column(name="prise_value")
	private double priseValue;
	
	private String punish;
	
	@Column(name="punish_value")
	private double punishValue;
	@Column(name = "update_name")
	private String updateName; //修改人
	@Column(name = "create_name")
	private String createName; //修改人
	
	private String note;
	
	@Transient
	private String dateString;
	
	@Column(name = "modify_date")
	private String modifyDate;
	
	public Behavior(){
		
	}
	
	public Behavior(String id){
		this.id=id;
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

	public String getPrise() {
		return prise;
	}

	public void setPrise(String prise) {
		this.prise = prise;
	}

	public double getPriseValue() {
		return priseValue;
	}

	public void setPriseValue(double priseValue) {
		this.priseValue = priseValue;
	}

	public String getPunish() {
		return punish;
	}

	public void setPunish(String punish) {
		this.punish = punish;
	}

	public double getPunishValue() {
		return punishValue;
	}

	public void setPunishValue(double punishValue) {
		this.punishValue = punishValue;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	
	
	public String getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
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
		Behavior other = (Behavior) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	
	public String getDateString() {
		return dateString;
	}

	public void setDateString(String dateString) {
		this.dateString = dateString;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getUpdateName() {
		return updateName;
	}

	public void setUpdateName(String updateName) {
		this.updateName = updateName;
	}

	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}


	
	
	
	
}
