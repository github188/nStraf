package cn.grgbanking.commonPlatform.personalHome.bean;

import java.util.List;
import java.util.Map;

/**
 * 个人首页数据（网页端）
 * 
 * @author zzhui1
 * 
 */
public class PersonalHomeBean {
	/** 个人积分TOP5 */
	private List<IntegralInfoSubBean> integralList;
	/** 本月个人考勤情况 */
	private List<AttendanceBean> attendanceList;
	/** 本月个人日志填写情况 */
	private List<Map<String, Object>> daylogList;
	/** 工时展示的信息 */
	private List<Map<String, Object>> workhoursList;
	/** 任务完成情况的信息 */
	private List<ProjectWeekPlanTaskSubBean> taskProgressList;
	/**
	 * @return integralList
	 */
	public List<IntegralInfoSubBean> getIntegralList() {
		return integralList;
	}
	/**
	 * @return attendanceList
	 */
	public List<AttendanceBean> getAttendanceList() {
		return attendanceList;
	}
	/**
	 * @return daylogList
	 */
	public List<Map<String, Object>> getDaylogList() {
		return daylogList;
	}
	/**
	 * @return workhoursList
	 */
	public List<Map<String, Object>> getWorkhoursList() {
		return workhoursList;
	}
	/**
	 * @return taskProgressList
	 */
	public List<ProjectWeekPlanTaskSubBean> getTaskProgressList() {
		return taskProgressList;
	}
	/**
	 * @param integralList
	 */
	public void setIntegralList(List<IntegralInfoSubBean> integralList) {
		this.integralList = integralList;
	}
	/**
	 * @param attendanceList
	 */
	public void setAttendanceList(List<AttendanceBean> attendanceList) {
		this.attendanceList = attendanceList;
	}
	/**
	 * @param daylogList
	 */
	public void setDaylogList(List<Map<String, Object>> daylogList) {
		this.daylogList = daylogList;
	}
	/**
	 * @param workhoursList
	 */
	public void setWorkhoursList(List<Map<String, Object>> workhoursList) {
		this.workhoursList = workhoursList;
	}
	/**
	 * @param taskProgressList
	 */
	public void setTaskProgressList(
			List<ProjectWeekPlanTaskSubBean> taskProgressList) {
		this.taskProgressList = taskProgressList;
	}
}
