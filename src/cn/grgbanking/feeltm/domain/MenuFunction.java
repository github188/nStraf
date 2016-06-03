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
@Table(name = "MENU_FUNCTION")
public class MenuFunction implements Serializable {

	/** identifier field */
	private String funcid;

	/** persistent field */
	private String menuid;

	/** persistent field */
	private String operid;

	/** full constructor */
	public MenuFunction(String funcid, String menuid, String operid) {
		this.funcid = funcid;
		this.menuid = menuid;
		this.operid = operid;
	}

	/** default constructor */
	public MenuFunction() {
	}

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	@Column(name = "C_FUNCID", unique = true, nullable = false)
	public String getFuncid() {
		return this.funcid;
	}

	public void setFuncid(String funcid) {
		this.funcid = funcid;
	}

	@Column(name = "C_MENUID")
	public String getMenuid() {
		return this.menuid;
	}

	public void setMenuid(String menuid) {
		this.menuid = menuid;
	}

	@Column(name = "C_OPERID")
	public String getOperid() {
		return this.operid;
	}

	public void setOperid(String operid) {
		this.operid = operid;
	}

}
