package cn.grgbanking.feeltm.borrow.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="oa_borrow_remind")
public class BorrowRemind {
/**
 * create by ywei9
 */
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid.hex")
	@Column(name="C_ID",unique=true,nullable=false)
	private String id;
	
	@Column(name="c_typeemail")
	private String typeemail;
	
	@Column(name="c_typeoa")
	private String typeoa;
	
	@Column(name="c_period")
	private String period;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTypeemail() {
		return typeemail;
	}

	public void setTypeemail(String typeemail) {
		this.typeemail = typeemail;
	}

	public String getTypeoa() {
		return typeoa;
	}

	public void setTypeoa(String typeoa) {
		this.typeoa = typeoa;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}
	
}
