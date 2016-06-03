package cn.grgbanking.commonPlatform.personalHome.webapp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;

import cn.grgbanking.commonPlatform.personalHome.bean.AttendanceBean;
import cn.grgbanking.commonPlatform.personalHome.bean.IntegralInfoSubBean;
import cn.grgbanking.commonPlatform.personalHome.bean.PersonalHomeBean;
import cn.grgbanking.commonPlatform.personalHome.bean.ProjectWeekPlanTaskSubBean;
import cn.grgbanking.commonPlatform.personalHome.constants.PersonalHomeConstants;
import cn.grgbanking.commonPlatform.personalHome.service.PersonalHomeService;
import cn.grgbanking.commonPlatform.utils.CommonUtil;
import cn.grgbanking.feeltm.cardRecord.domain.CardRecord;
import cn.grgbanking.feeltm.cardRecord.service.CardRecordService;
import cn.grgbanking.feeltm.dayLog.domain.DayLog;
import cn.grgbanking.feeltm.dayLog.service.DayLogService;
import cn.grgbanking.feeltm.domain.SysUser;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.project.domain.Project;
import cn.grgbanking.feeltm.project.service.ProjectService;
import cn.grgbanking.feeltm.projectweekplan.domain.ProjectWeekPlanTask;
import cn.grgbanking.feeltm.staff.service.StaffInfoService;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.DateUtil;
import cn.grgbanking.framework.webapp.BaseAction;

/**
 * 个人首页
 * 
 * @author zzhui1
 *
 */
public class PersonalHomeAction extends BaseAction{
	@Autowired
	private PersonalHomeService personalHomeService;
	@Autowired
	private StaffInfoService staffInfoService;
	@Autowired
	private ProjectService projectService;
	@Autowired
	private DayLogService dayLogService;
	@Autowired
	private CardRecordService cardRecordService;
	/** 接收的用户ID */
	private String userId;
	/** 跨域访问jsonp的回调函数 */
	private String jsonpCallBack;

	/**
	 * 获取个人积分TOP5
	 */
	private List<IntegralInfoSubBean> getPointTopFiveByDeptid(){
		SysUser usr= staffInfoService.findUserByUserid(userId);
		String deptId = usr.getDeptName();
		List<IntegralInfoSubBean> resultList = new ArrayList<IntegralInfoSubBean>();
		try {
			List<Object> integralInfo = personalHomeService.getPointTopFiveByDeptid(deptId);
			int maxSize = 0;
			if (integralInfo != null && integralInfo.size() > 0) {
				maxSize = integralInfo.size();
				if (maxSize >= 5) {
					maxSize = 5;
				}
			}
			int index = 0;
			for (int i = 0; i < maxSize; i++) {
				index = 0;
				Object[] item = (Object[]) integralInfo.get(i);
				IntegralInfoSubBean bean = new IntegralInfoSubBean();
				int integral = Integer.valueOf(item[index++].toString());
				bean.setIntegral(integral);
				bean.setDetName(item[index++].toString());
				bean.setUserName(item[index++].toString());
				bean.setUserId(item[index++].toString());
				resultList.add(bean);
			}
			CommonUtil.outputJson(resultList,jsonpCallBack);
		} catch (Exception e) {
			System.out.println(e.toString());
			SysLog.error("PersonalHomeAction.getPointTopFiveByDeptid执行有异常" + e.toString());
		}
		return resultList;
	}
	
	/**
	 * 加载任务完成情况的信息
	 */
	private List<ProjectWeekPlanTaskSubBean> getTaskProgressInfo(){
		String startDate = DateUtil.getDateByWeek(true);
		String endDate = DateUtil.getDateByWeek(false);
		List<ProjectWeekPlanTaskSubBean> resultList = new ArrayList<ProjectWeekPlanTaskSubBean>();
		try {
			List<ProjectWeekPlanTask> list = personalHomeService.getTaskInfoByWeek(userId, startDate, endDate);
			for (ProjectWeekPlanTask item : list) {
				ProjectWeekPlanTaskSubBean bean = new ProjectWeekPlanTaskSubBean();
				bean.setDeviation(item.getDeviation());
				bean.setFactWorkTime(item.getFactWorkTime());
				bean.setFinish(item.getFinish());
				bean.setPlanWorkTime(item.getPlanWorkTime());
				String prjId = item.getPlanId();
				String projectName = "-";
				Project project = projectService.getProjectByWeekPlanTask(prjId);
				if (project != null) {
					projectName = project.getName();
				} 
				bean.setProjectName(projectName);
				bean.setTaskContent(item.getTaskContent());
				resultList.add(bean);
			}
			CommonUtil.outputJson(resultList,jsonpCallBack);
		} catch (Exception e) {
			System.out.println(e.toString());
			SysLog.error("PersonalHomeAction.getTaskProgressInfo执行有异常" + e.toString());
		}
		return resultList;
	}
	
	/**
	 * 本月个人考勤情况
	 */
	private List<AttendanceBean> getAttendanceInfo(){
		List<String> dateList = DateUtil.getWorkdayList();
		List<AttendanceBean> resultList = new ArrayList<AttendanceBean>();
		try {
			for (String yyyy_MM_dd : dateList) {
				List<CardRecord> list = cardRecordService.getAttendanceByUseridDate(userId, yyyy_MM_dd);
				AttendanceBean bean = new AttendanceBean();
				bean.setSignDay(yyyy_MM_dd);
				if (list != null && list.size() >  0) {
					for (CardRecord cardRecord : list) {
						Date signTime = cardRecord.getSigntime();
						if (signTime == null) {
							continue;
						}
						//yyyy-MM-dd HH:mm:ss
						String signTimeStr = DateUtil.getTimeString(signTime).substring(11);
						int status = cardRecord.getAttendanceStatus();
						switch (status) {
						case 1:
						case 3:
							bean.setAttendanceStatusEntry(status);
							bean.setEntryTime(signTimeStr);
							break;
						case 2:
						case 4:
							bean.setAttendanceStatusExit(status);
							bean.setExitTime(signTimeStr);
							break;
						default:
							bean.setAttendanceStatusExit(0);
							bean.setEntryTime("");
							bean.setExitTime("");
							break;
						}
					}
				}
				resultList.add(bean);
			}
			CommonUtil.outputJson(resultList,jsonpCallBack);
		} catch (Exception e) {
			System.out.println(e.toString());
			SysLog.error("PersonalHomeAction.getAttendanceInfo执行有异常" + e.toString());
		}
		return resultList;
	}
	
	/**
	 * 本月个人日志填写情况
	 */
	private List<Map<String, Object>> getDayLogInfo(){
		List<String> dateList = DateUtil.getWorkdayList();
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		try {
			Map<String, Object> map = new HashMap<String,Object>();
			for (String YYYY_MM_DD : dateList) {
				DayLog  dayLog  =dayLogService.queryDayLogByUseridLogDate(userId, YYYY_MM_DD);
				map = new HashMap<String,Object>();
				map.put("date", YYYY_MM_DD);
				int status = 0;
				if (dayLog != null) {
					Date logDate = dayLog.getLogDate();
					Date fillDate = dayLog.getFillLogDate();
					if (fillDate == null) {
						fillDate = dayLog.getUpdateTime();
					}
					if (isDelayDaylog(logDate,fillDate)) {
						status = PersonalHomeConstants.FILLDAYLOG_DELAY;
					} else {
						status = PersonalHomeConstants.FILLDAYLOG_NOMAL;
					}
				} else {
					status = PersonalHomeConstants.FILLDAYLOG_NONE;
				}
				map.put("status", status);
				resultList.add(map);
			}
			CommonUtil.outputJson(resultList,jsonpCallBack);
		} catch (Exception e) {
			System.out.println(e.toString());
			SysLog.error("PersonalHomeAction.getDayLogInfo执行有异常" + e.toString());
		}
		return resultList;
	}
	
	/**
	 * 根据日志日期和填写日期，判断日志是否延迟填写
	 * @param logDate 日志日期
	 * @param fillDate 填写日期
	 * @return true : 表示延迟填写日志 false:表示当天按时填写日志
	 */
	private boolean isDelayDaylog(Date logDate, Date fillDate){
		String logDateStr = DateUtil.getyyyyMMddDate(logDate);
		String fillDateStr = DateUtil.getyyyyMMddDate(fillDate);
		boolean result = false;
		if (logDateStr.compareTo(fillDateStr) < 0) {
			result = true;
		}
		return result;
	}
	
	/**
	 * 加载工时展示的信息
	 */
	private List<Map<String, Object>> getWorkHoursInfo(){
		List<String> dateList = DateUtil.getWorkdayList();
		int year = Integer.valueOf(dateList.get(0).substring(0,4));
		int month = Integer.valueOf(dateList.get(0).substring(5,7));
		int lastDay = Integer.valueOf(dateList.get(dateList.size() - 1).substring(8));
		int lastDayForYearAndMonth = DateUtil.getLastDayForYearAndMonth(year, month);
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		try {
			Map<String, Object> map = new HashMap<String,Object>();
			int day = 1;
			Double workhours = 8d;
			for (String dateStr : dateList) {
				map = new HashMap<String,Object>();
				workhours = dayLogService.queryWorkhoursByUserIdDate(userId, dateStr);
				day = Integer.valueOf(dateStr.substring(8));
				map.put("day", day);
				map.put("workhours", workhours);
				resultList.add(map);
			}
			//对于当月未来的日期没有工时，则先取空的数据，用于页面显示
			for (int i = (lastDay + 1); i <= lastDayForYearAndMonth; i++) {
				map = new HashMap<String,Object>();
				map.put("day", i);
				map.put("workhours", 0);
				resultList.add(map);
			}
			CommonUtil.outputJson(resultList,jsonpCallBack);
		} catch (Exception e) {
			System.out.println(e.toString());
			SysLog.error("PersonalHomeAction.getWorkHoursInfo执行有异常" + e.toString());
		}
		return resultList;
	}
	
	/**
	 * 取得手机端的个人首页的数据
	 */
	public void getPersonalHomeInfo(){
		//获取用户
		userId = request.getParameter("userid");
		//跨域访问jsonp的回调函数
		jsonpCallBack = request.getParameter("jsoncallback");
		int actionFlag = Integer.valueOf(request.getParameter("actionFlag").toString());
		switch (actionFlag) {
		case 1:
			getPointTopFiveByDeptid();
			break;
		case 2:
			getAttendanceInfo();
			break;
		case 3:
			getDayLogInfo();
			break;
		case 4:
			getWorkHoursInfo();
			break;
		case 5:
			getTaskProgressInfo();
			break;
		default:
			break;
		}
	}
	
	/**
	 * 取得web端的个人首页的数据
	 */
	public void getPersonalHomeInfo4Web(){
		//获取用户信息
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		userId = userModel.getUserid();
		PersonalHomeBean personalHomeBean4Web = new PersonalHomeBean();
		personalHomeBean4Web.setIntegralList(getPointTopFiveByDeptid());
		personalHomeBean4Web.setAttendanceList(getAttendanceInfo());
		personalHomeBean4Web.setDaylogList(getDayLogInfo());
		personalHomeBean4Web.setWorkhoursList(getWorkHoursInfo());
		personalHomeBean4Web.setTaskProgressList(getTaskProgressInfo());
		JSONObject json = JSONObject.fromObject(personalHomeBean4Web);
		String personalHomeJson = json.toString();
		CommonUtil.ajaxPrint(personalHomeJson);
	}
}
