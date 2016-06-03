package cn.grgbanking.commonPlatform.monthlyManage.bean;

import java.util.ArrayList;

/**
 * 月度考勤统计用的BEAN（子BEAN）
 * @author zzhui1
 *
 */
public class AtdDetail {
	/** 天 */
	private int day;
	/** 详情 */
	private ArrayList<DeptDetail> deptDetail;

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}
	
	public ArrayList<DeptDetail> getDeptDetail() {
		return deptDetail;
	}

	public void setDeptDetail(ArrayList<DeptDetail> deptDetail) {
		this.deptDetail = deptDetail;
	}

	class DeptDetail {
		private String deptName;
		private String deptId;
		private int absentNum;
		private int lateNum;

		public String getDeptName() {
			return deptName;
		}

		public int getAbsentNum() {
			return absentNum;
		}

		public int getLateNum() {
			return lateNum;
		}

		public void setAbsentNum(int absentNum) {
			this.absentNum = absentNum;
		}

		public void setLateNum(int lateNum) {
			this.lateNum = lateNum;
		}

		public String getDeptId() {
			return deptId;
		}

		public void setDeptName(String deptName) {
			this.deptName = deptName;
		}

		public void setDeptId(String deptId) {
			this.deptId = deptId;
		}
	}
}
