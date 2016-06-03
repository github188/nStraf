package cn.grgbanking.commonPlatform.leaderHomePage.bean;

import java.math.BigDecimal;
/**
 * 领导首页折线图
 * @author xing
 *
 */
public class PersonDayInfo {

	private String year;//年
	private String month;//月
	private String color;//板块对应颜色
	private String name;//模块名称
	private BigDecimal[] data;//人日量
	
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public BigDecimal[] getData() {
		return data;
	}
	public void setData(BigDecimal[] data) {
		this.data = data;
	}
	
	
	
	
}
