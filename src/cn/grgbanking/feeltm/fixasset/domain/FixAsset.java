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
@Table(name="oa_estate")
public class FixAsset {
	private String id;
	private String no;
	private String type;
	private String name;
	private String use;
	private Date indate;
	private String inman;
	private String status;
	private String useman;
	private Date usedate;
	private String usereason;
	private String uselog;
	private Date expectdate;
	private Date factdate;
	private String usemanid;
	
	public FixAsset(){
		
	}
	public FixAsset(String id){
		this.id = id;
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
	@Column(name="c_no")
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	@Column(name="c_type")
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Column(name="c_name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(name="c_use")
	public String getUse() {
		return use;
	}
	public void setUse(String use) {
		this.use = use;
	}
	@Column(name="d_indate")
	@Temporal(TemporalType.DATE)
	public Date getIndate() {
		return indate;
	}
	public void setIndate(Date indate) {
		this.indate = indate;
	}
	@Column(name="c_inman")
	public String getInman() {
		return inman;
	}
	public void setInman(String inman) {
		this.inman = inman;
	}
	@Column(name="c_status")
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Column(name="c_useman")
	public String getUseman() {
		return useman;
	}
	public void setUseman(String useman) {
		this.useman = useman;
	}
	@Column(name="c_usedate")
	@Temporal(TemporalType.DATE)
	public Date getUsedate() {
		return usedate;
	}
	public void setUsedate(Date usedate) {
		this.usedate = usedate;
	}
	@Column(name="c_usereason")
	public String getUsereason() {
		return usereason;
	}
	public void setUsereason(String usereason) {
		this.usereason = usereason;
	}
	@Column(name="c_uselog")
	public String getUselog() {
		return uselog;
	}
	public void setUselog(String uselog) {
		this.uselog = uselog;
	}
	@Column(name="c_expectdate")
	@Temporal(TemporalType.DATE)
	public Date getExpectdate() {
		return expectdate;
	}
	public void setExpectdate(Date expectdate) {
		this.expectdate = expectdate;
	}
	@Column(name="c_factdate")
	@Temporal(TemporalType.DATE)
	public Date getFactdate() {
		return factdate;
	}
	public void setFactdate(Date factdate) {
		this.factdate = factdate;
	}
	@Column(name="c_usemanid")
	public String getUsemanid() {
		return usemanid;
	}
	public void setUsemanid(String usemanid) {
		this.usemanid = usemanid;
	}
	
}
