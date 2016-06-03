package cn.grgbanking.feeltm.domain.testsys;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ABILITYDEVELOP_PLAN")
public class AbilityDevelopPlan {
	private static final long serialVersionUID = 1L;
	
	@Id
	private String id;

	@Column(name="wish_capability")
	private String wishcapability;
	

	
	public AbilityDevelopPlan(){
		
	}
	public AbilityDevelopPlan(String id) {
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
	
	public String getWishcapability() {
		return wishcapability;
	}
	public void setWishcapability(String wishcapability) {
		this.wishcapability = wishcapability;
	}	
}
