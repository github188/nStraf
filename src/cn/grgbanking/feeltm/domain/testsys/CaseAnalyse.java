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
@Table(name ="case_analyse")
//@SequenceGenerator(name="pnoSeq",sequenceName="pno_seq",initialValue=00001,allocationSize=1)
public class CaseAnalyse implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	@Column(name = "id", unique = true, nullable = false)
	private String id;// id
	@Column(name = "create_man")
	private String create_man; //提出者
	@Column(name = "summary")
	private String summary;   //概要
	@Column(name = "category")
	private String category; //类别
	@Column(name = "description")
	private String description; //描述
	@Column(name = "analyse")
	private String analyse; //描述
	@Column(name = "solution")
	private String solution;//解决措施
	@Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
	private Date update_date; //更新时间
	@Column(name = "create_date")
	@Temporal(TemporalType.DATE)
	private Date create_date;  //提出时间
	@Column(name = "effect_score")
	private String effect_score; //推荐度
	@Column(name = "update_man")
	private String  update_man;//最近更新者
	@Transient
	private String createDateString;
	@Transient
	private String updateDateString;
	
//	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pnoSeq")
	@Column(name="pno",updatable=false)
	private String pno;
	
	public CaseAnalyse(){
		
	}
   public CaseAnalyse( String id){
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
	public String getAnalyse() {
		return analyse;
	}
	public void setAnalyse(String analyse) {
		this.analyse = analyse;
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
	public String getPno() {
		return pno;
	}
	public void setPno(String pno) {
		this.pno = pno;
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

	public String getEffect_score() {
		return effect_score;
	}
	public void setEffect_score(String effect_score) {
		this.effect_score = effect_score;
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
}
