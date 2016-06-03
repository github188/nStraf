package cn.grgbanking.feeltm.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@SuppressWarnings("serial")
@Entity
@Table(name = "USR_GRPROLE")
public class UsrGrprole implements Serializable {

	/** identifier field */
	private String id;

	/** persistent field */
	private String grpcode;

	/** persistent field */
	private String rolecode;

	/** full constructor */
	public UsrGrprole(String grpcode, String rolecode) {
		this.grpcode = grpcode;
		this.rolecode = rolecode;
	}

	/** default constructor */
	public UsrGrprole() {
	}

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	@Column(name = "C_ID", unique = true, nullable = false)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "C_GRPCODE")
	public String getGrpcode() {
		return this.grpcode;
	}

	public void setGrpcode(String grpcode) {
		this.grpcode = grpcode;
	}

	@Column(name = "C_ROLECODE")
	public String getRolecode() {
		return this.rolecode;
	}

	public void setRolecode(String rolecode) {
		this.rolecode = rolecode;
	}

}
