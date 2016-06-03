package cn.grgbanking.feeltm.domain.testsys;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="ABILITY_ANALYSE")
public class AbilityAnalyse {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	@Column(name = "id", unique = true, nullable = false)
	private String id;
	
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "analyse")
	private String analyse;
	
	@Column(name="create_year")
	private String createyear;  
		
	@Column(name="name")
	private String name;
	
	@Column(name="group_name")
	private String groupname;   //显卡
	
	@Column(name="update_date")
	private String updatedate;
		
	@Column(name="update_man")
	private String updateman;
	
	@Column(name="headman_upauditing")
	private String headmanupditing;
	
	@Column(name="manage_upauditing")
	private String manageupauditing;
	
	@Column(name="suggest")
	private String suggest;
		
	public AbilityAnalyse(){
		
	}
	public AbilityAnalyse(String id) {
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
	
	public String getAnalyse() {
		return analyse;
	}
	public void setAnalyse(String analyse) {
		this.analyse = analyse;
	}
	public String getUpdatedate() {
		return updatedate;
	}
	public void setUpdatedate(String updatedate) {
		this.updatedate = updatedate;
	}

	public String getUpdateman() {
		return updateman;
	}
	public void setUpdateman(String updateman) {
		this.updateman = updateman;
	}
	public String getCreateyear() {
		return createyear;
	}
	public void setCreateyear(String createyear) {
		this.createyear = createyear;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGroupname() {
		return groupname;
	}
	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getHeadmanupditing() {
		return headmanupditing;
	}
	public void setHeadmanupditing(String headmanupditing) {
		this.headmanupditing = headmanupditing;
	}
	public String getManageupauditing() {
		return manageupauditing;
	}
	public void setManageupauditing(String manageupauditing) {
		this.manageupauditing = manageupauditing;
	}
	public String getSuggest() {
		return suggest;
	}
	public void setSuggest(String suggest) {
		this.suggest = suggest;
	}
	
}
