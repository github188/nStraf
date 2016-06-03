package cn.grgbanking.feeltm.staffEntry.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "OA_ONBOARD_FLOW_CHECK_TEMPLATE")
public class OnBoardFlowCheckCondition implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	@Column(name = "C_ID", unique = true, nullable = false)
	private String id;

	@Column(name="C_FOLDER")
	private String folder;

	@Column(name = "C_TYPE")
	private String type;

	@Column(name = "C_CONTENT")
	private String content;

	@Column(name = "C_JOINT_DEPT")
	private String jointDept;
	
	@Column(name = "C_NOTE")
	private String note;
	
	@Column(name="D_UPDATETIME")
	private Date updateTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	

	public String getJointDept() {
		return jointDept;
	}

	public void setJointDept(String jointDept) {
		this.jointDept = jointDept;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	public boolean usefulFieldEmpty(){
		if(StringUtils.isBlank(content)&&StringUtils.isBlank(jointDept)&&StringUtils.isEmpty(note)&&StringUtils.isBlank(type)){
			return true;
		}else{
			return false;
		}
	}
}
