package cn.grgbanking.feeltm.expense.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * 费用报销明细
 * @author lhyan3
 * 2014年7月18日
 */
@Entity
@Table(name = "OA_EXPENSE_ACCOUNT_TRAVEL")
public class OAExpenseAccountTravelDetail implements Serializable{
private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	
	@Column(name = "C_ID", unique = true, nullable = false)
	private String id;//
	
	@Column(name = "C_DDTIME")
	private String ddTime;
	
	@Column(name = "C_DIDIAN")
	private String didian;
	
	@Column(name = "C_YEWU")
	private String yewu;
	
	@Column(name = "C_TRAFFIC")
	private String traffic;
	
	@Column(name = "C_ANOTHER")
	private String another;
	
	@Column(name = "C_NOTE")
	private String note;
	
	@Column(name = "C_UUID")
	private String uuid;
	
	@Column(name = "C_NUMBER")
	private int number;
	
	@Column(name="C_EXPENSEID")
	private String expenseId;
	
	@Column(name="c_prjname")
	private String prjname;

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDdTime() {
		return ddTime;
	}

	public void setDdTime(String ddTime) {
		this.ddTime = ddTime;
	}

	public String getDidian() {
		return didian;
	}

	public void setDidian(String didian) {
		this.didian = didian;
	}

	public String getYewu() {
		return yewu;
	}

	public void setYewu(String yewu) {
		this.yewu = yewu;
	}

	public String getTraffic() {
		return traffic;
	}

	public void setTraffic(String traffic) {
		this.traffic = traffic;
	}

	public String getAnother() {
		return another;
	}

	public void setAnother(String another) {
		this.another = another;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getUuid() {
		return uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public String getExpenseId() {
		return expenseId;
	}
	
	public void setExpenseId(String expenseId) {
		this.expenseId = expenseId;
	}

	public String getPrjname() {
		return prjname;
	}

	public void setPrjname(String prjname) {
		this.prjname = prjname;
	}
	

	
	
}
