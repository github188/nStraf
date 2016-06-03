package cn.grgbanking.feeltm.domain.testsys;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * ID VARCHAR2(32) primary key,
  en_name varchar2(32),
  ch_name varchar2(32),
  dept varchar2(32),
  role varchar2(32)
 * @author feel
 *
 */
@Entity
@Table(name="qc_user")
public class QCUser {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	@Column(name = "id", unique = true, nullable = false)
	private String id;
	
	private String en_name;
	private String ch_name;
	private String dept;
	private String role;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEn_name() {
		return en_name;
	}
	public void setEn_name(String enName) {
		en_name = enName;
	}
	public String getCh_name() {
		return ch_name;
	}
	public void setCh_name(String chName) {
		ch_name = chName;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	
}
