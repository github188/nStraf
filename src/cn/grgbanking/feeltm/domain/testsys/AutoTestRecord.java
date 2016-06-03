package cn.grgbanking.feeltm.domain.testsys;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "AUTOTESTRECORD")
public class AutoTestRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	@Column(name = "id", unique = true, nullable = false)
	private String id;

	@Column(name = "PRJ_NAME")
	private String PrjName;

	@Column(name = "VERSION_NO")
	private String VersionNo;
	
	@Column(name = "STATUS")
	private String Status;

	//@Temporal(TemporalType.DATE)
	@Column(name="EXEC_TIME")
	private Date ExecTime;

	@Column(name = "TOTAL_CASE")
	private String TotalCase;

	@Column(name = "PASS_CASE")
	private String PassCase;
	
	@Column(name = "FAIL_CASE")
	private String FailCase;
	
	@Column(name="EXEC_MAN")
	private String ExecMan;
	
	@Column(name="ALL_TIME")
	private String AllTime;

    @Column(name="NOTE")
	private String Note;
    
    public AutoTestRecord(){
		
	}

	public AutoTestRecord(String id) {
		//super();
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPrjName() {
		return PrjName;
	}

	public void setPrjName(String prjName) {
		PrjName = prjName;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public String getVersionNo() {
		return VersionNo;
	}

	public void setVersionNo(String versionNo) {
		VersionNo = versionNo;
	}

	public Date getExecTime() {
		return ExecTime;
	}

	public void setExecTime(Date execTime) {
		ExecTime = execTime;
	}

	public String getTotalCase() {
		return TotalCase;
	}

	public void setTotalCase(String totalCase) {
		TotalCase = totalCase;
	}

	public String getPassCase() {
		return PassCase;
	}

	public void setPassCase(String passCase) {
		PassCase = passCase;
	}

	public String getFailCase() {
		return FailCase;
	}

	public void setFailCase(String failCase) {
		FailCase = failCase;
	}

	public String getAllTime() {
		return AllTime;
	}

	public void setAllTime(String allTime) {
		AllTime = allTime;
	}

	public String getNote() {
		return Note;
	}

	public void setNote(String note) {
		Note = note;
	}

	public String getExecMan() {
		return ExecMan;
	}

	public void setExecMan(String execMan) {
		ExecMan = execMan;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

}
