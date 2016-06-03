package cn.grgbanking.feeltm.domain.testsys;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="devdic")
public class StandardDic {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
	@Column(name = "id", unique = true, nullable = false)
	private String id;
	
	private String category;
	private String standard;
	private int point;
	private int lever;
	@Column(name="min_value")
	private String min;   //用来设定其级别:优良中等
	@Column(name="max_value")
	private String max;
	@Column(name="fix_order")
	private int fixOrder;  //固定的升序排序，以例好判断每个类别的得分情况
	
	
	public StandardDic(){
		
	}
	public StandardDic(String standard, int point) {
		super();
		this.standard = standard;
		this.point = point;
	}
	
	public StandardDic(String min,String max){
		super();
		this.min=min;
		this.max=max;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getStandard() {
		return standard;
	}
	public void setStandard(String standard) {
		this.standard = standard;
	}
	public int getPoint() {
		return point;
	}
	public void setPoint(int point) {
		this.point = point;
	}
	public int getLever() {
		return lever;
	}
	public void setLever(int lever) {
		this.lever = lever;
	}
	
	public String getMin() {
		return min;
	}
	public void setMin(String min) {
		this.min = min;
	}
	public String getMax() {
		return max;
	}
	public void setMax(String max) {
		this.max = max;
	}
	
	public int getFixOrder() {
		return fixOrder;
	}
	public void setFixOrder(int fixOrder) {
		this.fixOrder = fixOrder;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	
	
}
