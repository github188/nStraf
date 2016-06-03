package cn.grgbanking.feeltm.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "USR_ROLE")
public class UsrRole implements Serializable {

	/** identifier field */
	private String rolecode;

	/** persistent field */
	private String rolename;

	/** full constructor */
	public UsrRole(String rolename) {
		this.rolename = rolename;
	}

	/** default constructor */
	public UsrRole() {
	}

	@Id
	@Column(name = "C_ROLECODE", unique = true, nullable = false)
	public String getRolecode() {
		return this.rolecode;
	}

	public void setRolecode(String rolecode) {
		this.rolecode = rolecode;
	}

	@Column(name = "C_ROLENAME")
	public String getRolename() {
		return this.rolename;
	}

	public void setRolename(String rolename) {
		this.rolename = rolename;
	}

}
