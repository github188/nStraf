package cn.grgbanking.commonPlatform.monthlyManage.bean;

import java.util.ArrayList;

/**
 * 月度考勤统计用的BEAN
 * @author zzhui
 *
 */
public class AttendanceInfoBean {
	private int year;
	private int month;
	private ArrayList<AtdDetail> detail;
	
	public ArrayList<AtdDetail> getDetail() {
		return detail;
	}
	public void setDetail(ArrayList<AtdDetail> detail) {
		this.detail = detail;
	}
	public int getYear() {
		return year;
	}
	public int getMonth() {
		return month;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public void setMonth(int month) {
		this.month = month;
	}
}
