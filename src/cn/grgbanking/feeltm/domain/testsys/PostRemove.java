package cn.grgbanking.feeltm.domain.testsys;

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
@Table(name="POST_REMOVE")
public class PostRemove {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	@Column(name = "id", unique = true, nullable = false)
	private String id;
		
	@Column(name = "wish_gut")
	private String wishgut;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "lock_tag")
	private String locktag;
	
	@Column(name = "name")
	private String name;
		
	@Column(name="update_date")
	private String updatedate;
	
	@Column(name="advence_date")
	@Temporal(TemporalType.DATE)
	private Date advencedate;
	
	@Column(name="planstart_date")
	@Temporal(TemporalType.DATE)
	private Date planstartdate;
	
	@Column(name="planfinish_date")
	@Temporal(TemporalType.DATE)
	private Date planfinishdate;
	
	@Column(name="plantotal_time")
	private String plantotaltime;  
	
	@Column(name="endstart_date")
	@Temporal(TemporalType.DATE)
	private Date endstartdate;
	
	@Column(name="endfinish_date")
	@Temporal(TemporalType.DATE)
	private Date endfinishdate;
	
	@Column(name="endtotal_time")
	private String endtotaltime;  
		
	@Column(name="domain")
	private String domain;
	
	@Column(name="group_name")
	private String groupname;   //显卡

	@Column(name="update_man")
	private String updateman;
	
	@Column(name="self_score")
	private String selfscore;
	
	@Column(name="headman_score")
	private String headmanscore;
	
	@Column(name="manage_score")
	private String managescore;
	
	public PostRemove(){
		
	}
	public PostRemove(String id) {
		this.id=id;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
    public static long getSerialVersionUID() {
		return serialVersionUID;
	}


	public String getUpdatedate() {
		return updatedate;
	}
	public void setUpdatedate(String updatedate) {
		this.updatedate = updatedate;
	}
	
	public String getGroupname() {
		return groupname;
	}
	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}
	public String getUpdateman() {
		return updateman;
	}
	public void setUpdateman(String updateman) {
		this.updateman = updateman;
	}

	public String getHeadmanscore() {
		return headmanscore;
	}
	public void setHeadmanscore(String headmanscore) {
		this.headmanscore = headmanscore;
	}
	public String getManagescore() {
		return managescore;
	}
	public void setManagescore(String managescore) {
		this.managescore = managescore;
	}
	public Date getAdvencedate() {
		return advencedate;
	}
	public void setAdvencedate(Date advencedate) {
		this.advencedate = advencedate;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getSelfscore() {
		return selfscore;
	}
	public void setSelfscore(String selfscore) {
		this.selfscore = selfscore;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getWishgut() {
		return wishgut;
	}
	public void setWishgut(String wishgut) {
		this.wishgut = wishgut;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getPlanstartdate() {
		return planstartdate;
	}
	public void setPlanstartdate(Date planstartdate) {
		this.planstartdate = planstartdate;
	}
	public Date getPlanfinishdate() {
		return planfinishdate;
	}
	public void setPlanfinishdate(Date planfinishdate) {
		this.planfinishdate = planfinishdate;
	}
	public String getPlantotaltime() {
		return plantotaltime;
	}
	public void setPlantotaltime(String plantotaltime) {
		this.plantotaltime = plantotaltime;
	}
	public Date getEndstartdate() {
		return endstartdate;
	}
	public void setEndstartdate(Date endstartdate) {
		this.endstartdate = endstartdate;
	}
	public Date getEndfinishdate() {
		return endfinishdate;
	}
	public void setEndfinishdate(Date endfinishdate) {
		this.endfinishdate = endfinishdate;
	}
	public String getEndtotaltime() {
		return endtotaltime;
	}
	public void setEndtotaltime(String endtotaltime) {
		this.endtotaltime = endtotaltime;
	}
	public String getLocktag() {
		return locktag;
	}
	public void setLocktag(String locktag) {
		this.locktag = locktag;
	}
}
