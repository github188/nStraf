package cn.grgbanking.feeltm.domain.testsys;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name ="oa_holiday")
public class Holiday implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	@Column(name = "id", unique = true, nullable = false)
	private String id;
	
	/** 年份,如2014 */
	@Column(name="CHECK_YEAR")
	private String checkYear;
	
	/** 月份,如201410 */
	@Column(name="CHECK_MONTH")
	private String checkMonth;
	
	/** 日期 ,如20141001*/
	@Column(name="CHECK_DATE")
	private String checkDate;
	
	/** 类型   0工作日    1休息日（周末）  2法定节假日 */
	@Column(name="type")
	private String type;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(String checkDate) {
		this.checkDate = checkDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCheckYear() {
		return checkYear;
	}

	public void setCheckYear(String checkYear) {
		this.checkYear = checkYear;
	}

	public String getCheckMonth() {
		return checkMonth;
	}

	public void setCheckMonth(String checkMonth) {
		this.checkMonth = checkMonth;
	}


}
