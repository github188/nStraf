package cn.grgbanking.feeltm.fixasset.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="oa_fixuserecord")
public class FixAssetUse {
	private String id;
	private Date expactdate;
	private Date factdate;
	private String username;
	private String type;
	private String usereson;
	private Date date;
	private String fixid;
	private Date usedate;
	
	public FixAssetUse(){
		
	}
	public FixAssetUse(String id){
		this.id=id;
	}
	
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy="uuid.hex")
	@Column(name="c_id",unique=true,nullable=false)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Column(name="c_expactdate")
	@Temporal(TemporalType.DATE)
	public Date getExpactdate() {
		return expactdate;
	}
	public void setExpactdate(Date expactdate) {
		this.expactdate = expactdate;
	}
	@Column(name="c_factdate")
	@Temporal(TemporalType.DATE)
	public Date getFactdate() {
		return factdate;
	}
	public void setFactdate(Date factdate) {
		this.factdate = factdate;
	}
	@Column(name="c_username")
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	@Column(name="c_type")
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Column(name="c_usereson")
	public String getUsereson() {
		return usereson;
	}
	public void setUsereson(String usereson) {
		this.usereson = usereson;
	}
	@Column(name="c_date")
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	@Column(name="c_fixid")
	public String getFixid() {
		return fixid;
	}
	public void setFixid(String fixid) {
		this.fixid = fixid;
	}
	@Temporal(TemporalType.DATE)
	@Column(name="c_usedate")
	public Date getUsedate() {
		return usedate;
	}
	public void setUsedate(Date usedate) {
		this.usedate = usedate;
	}
	
}
