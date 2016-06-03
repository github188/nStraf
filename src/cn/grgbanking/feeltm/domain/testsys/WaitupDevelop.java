package cn.grgbanking.feeltm.domain.testsys;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="WAITUP_DEVELOP")
public class WaitupDevelop {
	private static final long serialVersionUID = 1L;
	
	@Id
	private String id;
		
	@Column(name="capability_name")
	private String capabilityname;
	
	public WaitupDevelop(){
		
	}
	public WaitupDevelop(String id) {
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

	public String getCapabilityname() {
		return capabilityname;
	}
	public void setCapabilityname(String capabilityname) {
		this.capabilityname = capabilityname;
	}
}
