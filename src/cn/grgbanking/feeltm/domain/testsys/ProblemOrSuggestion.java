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

/**
 * --问题及建议 create table problem_suggestion ( ID VARCHAR2(32) primary key,
 * raise_man VARCHAR2(32), raise_date DATE, summary VARCHAR2(100), status
 * char(1),--0未解决，1已解决，2跟进中 priority char(1), -0,1，2依次为低，中高 desc VARCHAR2(2048),
 * solution VARCHAR2(2048), resolve_man VARCHAR2(32), resolve_date VARCHAR2(32),
 * );
 * 
 * @author feel
 * 
 */
@Entity
@Table(name ="problem_suggestion")
//@SequenceGenerator(name="pnoSeq",sequenceName="pno_seq",initialValue=00001,allocationSize=1)
public class ProblemOrSuggestion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	@Column(name = "id", unique = true, nullable = false)
	private String id;// id
	@Column(name = "raise_man")
	private String raise_man; //提出者
	@Column(name = "summary")
	private String summary;   //概要
	@Column(name = "status")
	private String status; //状态
	@Column(name = "category")
	private String category; //类别
	@Column(name = "description")
	private String description; //描述
	@Column(name = "solution")
	private String solution;//解决措施
	@Column(name = "resolve_man")
	private String resolve_man; //处理者
	@Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
	private Date update_date; //更新时间
	@Column(name = "raise_date")
	@Temporal(TemporalType.DATE)
	private Date raise_date;  //提出时间
	@Column(name = "finishing_date")
	@Temporal(TemporalType.DATE)
	private Date finishing_date; //计划完成时间
	@Column(name = "pratical_date")
	@Temporal(TemporalType.DATE)
	private Date pratical_date ; //实际完成时间
	@Column(name = "effect_score")
	private String effect_score; //效果分
	@Column(name = "price_score")
	private String  price_score;  //价值分
	@Column(name = "giving_solution")
	private String  giving_solution;  //建议措施
	@Column(name = "mamager_sugggestion")
	private String   mamager_sugggestion;  //经理意见
	@Column(name = "public_evaluation")
	private String   public_evaluation;  //大家评价
	@Column(name = "update_man")
	private String  update_man;//最近更新者
	@Transient
	private String finishing_dateString;
	@Transient
	private String   pratical_dateString;
	@Transient
	private String raiseDateString;
	@Transient
	private String updateDateString;
	
//	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pnoSeq")
	@Column(name="pno",updatable=false)
	private String pno;
	
	public ProblemOrSuggestion(){
		
	}
   public ProblemOrSuggestion( String id){
		this.id=id;
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRaise_man() {
		return raise_man;
	}

	public void setRaise_man(String raise_man) {
		this.raise_man =raise_man;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	

	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public Date getFinishing_date() {
		return finishing_date;
	}
	public void setFinishing_date(Date finishing_date) {
		this.finishing_date = finishing_date;
	}
	public Date getPratical_date() {
		return pratical_date;
	}
	public void setPratical_date(Date pratical_date) {
		this.pratical_date = pratical_date;
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

	public String getResolve_man() {
		return resolve_man;
	}

	public void setResolve_man(String resolve_man) {
		this.resolve_man = resolve_man;
	}

	public Date getRaise_date() {
		return raise_date;
	}

	public void setRaise_date(Date raise_date) {
		this.raise_date = raise_date;
	}

	
	public Date getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}

	public String getFinishing_dateString() {
		return finishing_dateString;
	}
	public void setFinishing_dateString(String finishing_dateString) {
		this.finishing_dateString = finishing_dateString;
	}
	public String getPratical_dateString() {
		return pratical_dateString;
	}
	public void setPratical_dateString(String pratical_dateString) {
		this.pratical_dateString = pratical_dateString;
	}
	public String getRaiseDateString() {
		return raiseDateString;
	}

	public void setRaiseDateString(String raiseDateString) {
		this.raiseDateString = raiseDateString;
	}

	public String getUpdateDateString() {
		return updateDateString;
	}

	public void setUpdateDateString(String updateDateString) {
		this.updateDateString = updateDateString;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	public String getEffect_score() {
		return effect_score;
	}
	public void setEffect_score(String effect_score) {
		this.effect_score = effect_score;
	}
	public String getPrice_score() {
		return price_score;
	}
	public void setPrice_score(String price_score) {
		this.price_score = price_score;
	}
	public String getGiving_solution() {
		return giving_solution;
	}
	public void setGiving_solution(String giving_solution) {
		this.giving_solution = giving_solution;
	}
	public String getMamager_sugggestion() {
		return mamager_sugggestion;
	}
	public void setMamager_sugggestion(String mamager_sugggestion) {
		this.mamager_sugggestion = mamager_sugggestion;
	}
	public String getPublic_evaluation() {
		return public_evaluation;
	}
	public void setPublic_evaluation(String public_evaluation) {
		this.public_evaluation = public_evaluation;
	}
	public String getUpdate_man() {
		return update_man;
	}
	public void setUpdate_man(String update_man) {
		this.update_man = update_man;
	}
	public String getPno() {
		return pno;
	}
	public void setPno(String pno) {
		this.pno = pno;
	}

	
	
}
