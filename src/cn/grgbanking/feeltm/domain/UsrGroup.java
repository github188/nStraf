package cn.grgbanking.feeltm.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "USR_GROUP")
public class UsrGroup implements Serializable {

	/** identifier field */
	private String grpcode;

	/** persistent field */
	private String grpname;

	//private String grpLevel;
	

	/*@Column(name = "C_GRP_LEVEL")
	public String getGrpLevel() {
		return grpLevel;
	}

	public void setGrpLevel(String grpLevel) {
		this.grpLevel = grpLevel;
	}
*/
	/** full constructor */

	public UsrGroup(String grpname) {
		this.grpname = grpname;
	}

	/** default constructor */
	public UsrGroup() {
	}

	@Id
	@Column(name = "C_GRPCODE", unique = true, nullable = false)
	public String getGrpcode() {
		return this.grpcode;
	}

	public void setGrpcode(String grpcode) {
		this.grpcode = grpcode;
	}

	@Column(name = "C_GRPNAME")
	public String getGrpname() {
		return this.grpname;
	}

	public void setGrpname(String grpname) {
		this.grpname = grpname;
	}
	
	/*@Column(name="C_GRP_LEVEL")
	public String getGrpLevel() {
		return grpLevel;
	}
	
	public void setGrpLevel(String grpLevel) {
		this.grpLevel = grpLevel;
	}*/
	
}
