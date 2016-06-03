package cn.grgbanking.feeltm.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "OA_PRJCHANCE_OTHERINFO")
public class PrjChanceOtherInfo {
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	
	@Column(name = "C_ID", unique = true, nullable = false)
	private String id;//主键
	
	@Column(name = "c_chanceid")
	private String chanceId;//商机id

	@Column(name = "c_relMan")
	private String relMan;//联系人
	
	@Column(name = "c_relDept")
	private String relDept;//联系人部门
	
	@Column(name = "c_relPosition")
	private String relPosition;//联系人职位

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getChanceId() {
		return chanceId;
	}

	public void setChanceId(String chanceId) {
		this.chanceId = chanceId;
	}

	public String getRelMan() {
		return relMan;
	}

	public void setRelMan(String relMan) {
		this.relMan = relMan;
	}

	public String getRelDept() {
		return relDept;
	}

	public void setRelDept(String relDept) {
		this.relDept = relDept;
	}

	public String getRelPosition() {
		return relPosition;
	}

	public void setRelPosition(String relPosition) {
		this.relPosition = relPosition;
	}

	

}
