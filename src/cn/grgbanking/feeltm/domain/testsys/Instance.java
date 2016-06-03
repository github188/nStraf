package cn.grgbanking.feeltm.domain.testsys;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name ="instance")
//@SequenceGenerator(name="pnoSeq",sequenceName="pno_seq",initialValue=00001,allocationSize=1)
public class Instance implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	@Column(name = "id", unique = true, nullable = false)
	private String id;// id
	@Column(name = "create_man")
	private String create_man; //提出者
	@Column(name = "up_man")
	private String up_man; //提出者
	@Column(name = "summary")
	private String summary;   //概要
	@Column(name = "category")
	private String category; //类别
	@Column(name = "description")
	private String description; //描述
	@Column(name = "solution")
	private String solution;//解决措施
	@Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
	private Date update_date; //更新时间
	@Column(name = "create_date")
	@Temporal(TemporalType.DATE)
	private Date create_date;  //提出时间
	@Column(name = "status")
	private String status; //公开状态
	@Column(name = "anon")
	private String anon; //公开状态
	
	@Column(name = "update_man")
	private String  update_man;//最近更新者
	@Column(name = "embracer_man")
	private String  embracer_man;//最近更新者
	@Column(name = "embracerids")
	private String  embracerids;//最近更新者
	@Transient
	private String createDateString;
	@Transient
	private String updateDateString;
	
	@Column(name = "CONFIRMMAN")
	private String confirmMan;//确认人
	
	@Column(name = "CONFIRMSTATUS")
	private String confirmStatus;//确认状态
	
	@Column(name = "CONFIRMDESC")
	private String confirmDesc;//确认说明
	
	@Column(name = "CONFIRMTIME")
	private Date confirmTime;//确认时间
	
	@Column(name = "CREATE_MANID")
	private String create_manId;//提出人
	
	
//	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pnoSeq")
	@Column(name="ino",updatable=false)
	private String ino;
	
	public Instance(){
		
	}
   public Instance( String id){
		this.id=id;
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreate_man() {
		return create_man;
	}
	public void setCreate_man(String create_man) {
		this.create_man = create_man;
	}

	public Date getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}
	public String getCreateDateString() {
		return createDateString;
	}
	public void setCreateDateString(String createDateString) {
		this.createDateString = createDateString;
	}
	public String getUpdateDateString() {
		return updateDateString;
	}
	public void setUpdateDateString(String updateDateString) {
		this.updateDateString = updateDateString;
	}
	public String getIno() {
		return ino;
	}
	public void setIno(String ino) {
		this.ino = ino;
	}
	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSolution() {
		return solution;
	}

	public void setSolution(String solution) {
		this.solution = solution;
	}

	public Date getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	public String getUpdate_man() {
		return update_man;
	}
	public void setUpdate_man(String update_man) {
		this.update_man = update_man;
	}
	public String getEmbracer_man() {
		return embracer_man;
	}
	public void setEmbracer_man(String embracer_man) {
		this.embracer_man = embracer_man;
	}
	public String getAnon() {
		return anon;
	}
	public void setAnon(String anon) {
		this.anon = anon;
	}
	public String getUp_man() {
		return up_man;
	}
	public void setUp_man(String up_man) {
		this.up_man = up_man;
	}
	public String getEmbracerids() {
		return embracerids;
	}
	public void setEmbracerids(String embracerids) {
		this.embracerids = embracerids;
	}
	public String getConfirmMan() {
		return confirmMan;
	}
	public void setConfirmMan(String confirmMan) {
		this.confirmMan = confirmMan;
	}
	public String getConfirmStatus() {
		return confirmStatus;
	}
	public void setConfirmStatus(String confirmStatus) {
		this.confirmStatus = confirmStatus;
	}
	public String getConfirmDesc() {
		return confirmDesc;
	}
	public void setConfirmDesc(String confirmDesc) {
		this.confirmDesc = confirmDesc;
	}
	public Date getConfirmTime() {
		return confirmTime;
	}
	public void setConfirmTime(Date confirmTime) {
		this.confirmTime = confirmTime;
	}
	public String getCreate_manId() {
		return create_manId;
	}
	public void setCreate_manId(String create_manId) {
		this.create_manId = create_manId;
	}
	
}
