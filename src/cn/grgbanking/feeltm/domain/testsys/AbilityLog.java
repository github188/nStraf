package cn.grgbanking.feeltm.domain.testsys;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ABILITY_LOG")
public class AbilityLog {
	private static final long serialVersionUID = 1L;
	
	@Id
	private String id;
		
	@Column(name = "log")
	private String log;
	
	public AbilityLog(){
		
	}
	public AbilityLog(String id) {
		this.id=id;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
    public String getLog() {
		return log;
	}
	public void setLog(String log) {
		this.log = log;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}


	
}
