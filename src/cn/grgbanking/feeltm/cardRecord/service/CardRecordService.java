package cn.grgbanking.feeltm.cardRecord.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.commonPlatform.monthlyManage.constants.MonthlyManagerConstants;
import cn.grgbanking.feeltm.cardRecord.dao.CardRecordDao;
import cn.grgbanking.feeltm.cardRecord.domain.CardRecord;
import cn.grgbanking.feeltm.config.BusnDataDir;
import cn.grgbanking.feeltm.config.Configure;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.DateUtil;
import cn.grgbanking.framework.service.BaseService;
import cn.grgbanking.framework.util.Page;

/**
 * 统计考勤表的来自EHR和广发的考勤状态
 * @author zzhui1
 *
 */
@Service
@Transactional
public class CardRecordService extends BaseService {
	@Autowired
	private CardRecordDao cardRecordDao;
	
	/**
	 * 从Date格式的时间取时分秒HH:MM:SS
	 * @param time
	 * @return
	 */
	private String getDateToHHMMSS(Date time){
		if (time != null){
			return DateUtil.getTimeString(time).substring(11);
		}else{
			return "";
		}
	}
	
	/**
	 * 取得考勤状态
	 * @param saparationTime 分割时间
	 * @param deviationTime 误差时间
	 * @param entryTime 规定出勤时间
	 * @param exitTime 规定退勤时间
	 * @param signTime 实际考勤时间
	 * @return 考勤状态
	 */
	private int getAttendanceStatus(String saparationTime, String deviationTime, 
			Date entryTime, Date exitTime, Date signTime){
		String signTimeHMS = getDateToHHMMSS(signTime);
		int status = 0;
		int statusKey = 1;
		if(saparationTime.compareTo(signTimeHMS) >= 0){
			//作为出勤时间
			//判断状态
			status = checkAttendanceTime(signTime, entryTime, 1, deviationTime);
			if(status == 1){
				//正常出勤
				statusKey = Constants.ATTENDACE_ENTRY_KEY;
			}else if(status == 0) {
				//迟到
				statusKey = Constants.ATTENDACE_LATE_KEY;
			}else{
				//异常数据
				statusKey = Constants.ATTENDACE_UNDEFINED_KEY;
			}
		}else{
			//作为退勤时间
			//判断状态
			status = checkAttendanceTime(signTime, exitTime, 2, deviationTime);
			if (status == 1) {
				//正常退勤
				statusKey = Constants.ATTENDACE_EXIT_KEY;
			} else if(status == 0)  {
				//早退
				statusKey = Constants.ATTENDACE_EARLY_KEY;
			}else{
				//异常数据
				statusKey = Constants.ATTENDACE_UNDEFINED_KEY;
			}
		}
		return statusKey;
	}

	/**
	 * 考勤时间(只取时分秒，去掉日期来计算秒数)
	 * @param time
	 * @return 秒数
	 */
	private long getTimes(Date time){
		long times = (time.getHours())* 60 * 60 + (time.getMinutes() * 60) + time.getSeconds();
		return times;
	}
	
	/**
	 * 根据考勤时间判断考勤状态
	 * 
	 * @param signedTime
	 *            移动签到时间
	 * @param workTime
	 *            项目规定的考勤时间点
	 * @param signedType
	 *            考勤类型(1:出勤/2:退勤)
	 * @param deviationTime
	 *            允许的误差时间(分钟)
	 * @return 返回为0则为迟到或早退, 1为正常打卡, -1为异常的项目规定的考勤数据(该项目没有填写规定的上下班时间)
	 */
	private int checkAttendanceTime(Date signedTime, Date workTime, int signedType, String deviationTime){
		if (workTime == null) {
			return -1;
		}
		String signTimeHMS = getDateToHHMMSS(signedTime);
		String workTimeHMS = getDateToHHMMSS(workTime);
		if (signedType == 1){
			//出勤			
			if(signTimeHMS.compareTo(workTimeHMS) > 0){
				//迟到的判断
				long comparedTime = getTimes(signedTime) - getTimes(workTime);
				long lngDeviationTime = Long.parseLong(deviationTime);
				if (comparedTime > lngDeviationTime){
					//迟到
					return 0;
				}else{
					//没迟到
					return 1;
				}
			}else{
				//正常出勤
				return 1;
			}
		}else{
			//退勤
			if(signTimeHMS.compareTo(workTimeHMS) < 0){
				//早退的判断
				long comparedTime = getTimes(workTime) - getTimes(signedTime);
				long lngDeviationTime = Long.parseLong(deviationTime);
				if (comparedTime > lngDeviationTime){
					//早退
					return 0;
				}else{
					//没早退
					return 1;
				}
			}else{
				//正常退勤
				return 1;
			}
		}
	}
	
	/**
	 * 取出勤退勤标准时间
	 * @param time
	 * @return yyyy-MM-dd hh:mm:ss
	 */
	private Date getStartEndTime(String time){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			//为了拼成时间格式的字符串年月日暂用2000-01-01，并无实际意义
			date =  sdf.parse("2000-01-01 " + time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	/**
	 * @param cardRecord 考勤数据表(广发银行.产业园.移动签到)
	 * @return
	 */
	private boolean checkAttendanceStatus(CardRecord cardRecord,String saparationTime){
		List<CardRecord>  list = cardRecordDao.getStandardStatusByIdDate(cardRecord,saparationTime);
		Date signTime = cardRecord.getSigntime();
		boolean isNormal = false; 
		for (CardRecord item : list) {
			if(signTime.equals(item.getSigntime())){
				isNormal = true;
				break;
			}
		}
		return isNormal;
	}
	
	/**
	 * 更新考勤状态数据 
	 * @param eventType 1：通过定时任务后台凌晨执行  2：通过页面按钮操作执行当月的数据
	 * @return 更新结果
	 */
	public boolean updateEHRGFAttendanceStatus(int eventType){
		//是否初始化该月的数据
		//String doInitPreMonthAttendanceStatus = Configure.getProperty("doInitPreMonthAttendanceStatus");
		//修复未统计的考勤状态的日期的数据
		String repairDate = Configure.getProperty("repairDate");
		
		//boolean doInit = Boolean.parseBoolean(doInitPreMonthAttendanceStatus);
		List<String> dateList = DateUtil.getWorkdayList();
		List<String> dateListForSearch = new ArrayList<String>();
		//1：通过定时任务后台凌晨执行  2：通过页面按钮操作执行当月的数据
		if (eventType == 2) {
			dateListForSearch = dateList;
		} else {
			dateListForSearch.add(dateList.get(dateList.size()-1));
		}
		//更新考勤日期的补丁，通过viewconfig文件的配置日期更新指定的日期的数据
		if (StringUtils.isNotBlank(repairDate)) {
			String[] repairDateArray = repairDate.split(",");
			dateListForSearch.addAll(Arrays.asList(repairDateArray));
		}
		
		//从数据字典获取相关的参数
		Map<String,String> attendanceTimeDir = BusnDataDir.getMapKeyValue("hrManager.attendanceTime");
		//广发考勤时间为08:30:00—17:30:00
		String standardEntryTimeGF = "";
		standardEntryTimeGF = (String)attendanceTimeDir.get(MonthlyManagerConstants.GF_ENTRY_TIME_KEY);
		if (StringUtils.isBlank(standardEntryTimeGF)) {
			 standardEntryTimeGF = MonthlyManagerConstants.GF_ENTRY_TIME;
		}
		Date standardEntryDateGF = getStartEndTime(standardEntryTimeGF);
		
		String standardExitTimeGF = "";
		standardExitTimeGF = (String)attendanceTimeDir.get(MonthlyManagerConstants.GF_EXIT_TIME_KEY);
		if (StringUtils.isBlank(standardExitTimeGF)) {
			standardExitTimeGF = MonthlyManagerConstants.GF_EXIT_TIME;
		}
		Date standardExitDateGF = getStartEndTime(standardExitTimeGF);
		
		//产业园考勤时间为08:00:00—17:00:00
		String standardEntryTimeEHR = "";
		standardEntryTimeEHR = (String)attendanceTimeDir.get(MonthlyManagerConstants.GRG_ENTRY_TIME_KEY);
		if (StringUtils.isBlank(standardEntryTimeEHR)) {
			standardEntryTimeEHR = MonthlyManagerConstants.GRG_ENTRY_TIME;
		}
		Date standardEntryDateEHR= getStartEndTime(standardEntryTimeEHR);
		
		String standardExitTimeEHR = "";
		standardExitTimeEHR = (String)attendanceTimeDir.get(MonthlyManagerConstants.GRG_EXIT_TIME_KEY);
		if (StringUtils.isBlank(standardExitTimeEHR)) {
			standardExitTimeEHR = MonthlyManagerConstants.GRG_EXIT_TIME;
		}
		Date standardExitDateEHR = getStartEndTime(standardExitTimeEHR);
		
		Map<String,String> attendanceConfigDir = BusnDataDir.getMapKeyValue("systemConfig.systemParamConfig");
		String saparationTime = (String)attendanceConfigDir.get(Constants.SEPARATION_TIME_KEY);
		String deviationTime = MonthlyManagerConstants.DEVIATION_TIME;
		if (StringUtils.isBlank(saparationTime)) {
			saparationTime = MonthlyManagerConstants.SAPARATION_TIME;
		}
		//查询考勤记录
		for (String yyyy_MM_dd : dateListForSearch) {
			List<CardRecord>  list = cardRecordDao.getCardRecordList(yyyy_MM_dd);
			if (list == null || list.size() == 0) {
				SysLog.info(yyyy_MM_dd + "无考勤状态更新数据");
				continue;
			}
			
			//更新考勤状态
			try {
				for (CardRecord cardRecord : list) {
					Date signTime = cardRecord.getSigntime();
					String type = cardRecord.getType();
					int status = 0;
					boolean isNormalAttendance = checkAttendanceStatus(cardRecord, saparationTime);
					if (isNormalAttendance) {
						if (MonthlyManagerConstants.GRG_ATTENDANCE.equals(type)) {
							//EHR
							status = getAttendanceStatus(saparationTime, deviationTime, 
									standardEntryDateEHR, standardExitDateEHR, signTime);
						} else if(MonthlyManagerConstants.GF_ATTENDANCE.equals(type)) {
							//GF
							status = getAttendanceStatus(saparationTime, deviationTime, 
									standardEntryDateGF, standardExitDateGF, signTime);
						}else{
							//移动签到的状态已经处理了。
						}
					} else {
						status = -1;
					}
					cardRecord.setAttendanceStatus(status);
					cardRecordDao.updateObject(cardRecord);
					SysLog.info(yyyy_MM_dd + "更新数据：" + cardRecord.getUserid() + "|" + cardRecord.getUsername());
				}
			} catch (Exception e) {
				SysLog.error("考勤状态更新中异常：" + e.toString());
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 取得当天的考勤记录
	 * @param userId 用户ID
	 * @param yyyy_MM_dd 考勤日期
	 * @return 当天的考勤记录
	 */
	public List<CardRecord> getAttendanceByUseridDate(String userId, String yyyy_MM_dd){
		return cardRecordDao.getAttendanceByUseridDate(userId, yyyy_MM_dd);
	}
	
	/**
	 * 列出所有的考勤数据
	 * @param pageNum
	 * @param pageSize
	 * @param username
	 * @param deptname
	 * @param groupname
	 * @param signTime
	 * @param signEndTime
	 * @param userModel
	 * @return
	 */
	@Transactional(readOnly = true)
	public Page getPage(int pageNum, int pageSize,String username,String deptname,String groupname, String signTime, String signEndTime, UserModel userModel){
		return cardRecordDao.getPage(pageNum, pageSize, username, deptname, groupname, signTime, signEndTime, userModel);
	}
	
	/**
	 * 导入广发数据后，立马更新广发考勤数据状态 
	 * @return 更新结果
	 */
	public boolean updateGFAttendanceStatus(){
		//获取广发未处理考勤状态的数据
		List<String> dateListForSearch = cardRecordDao.getGFCardRecordList();
		//从数据字典获取相关的参数
		Map<String,String> attendanceTimeDir = BusnDataDir.getMapKeyValue("hrManager.attendanceTime");
		//广发考勤时间为08:30:00—17:30:00
		String standardEntryTimeGF = "";
		standardEntryTimeGF = (String)attendanceTimeDir.get(MonthlyManagerConstants.GF_ENTRY_TIME_KEY);
		if (StringUtils.isBlank(standardEntryTimeGF)) {
			 standardEntryTimeGF = MonthlyManagerConstants.GF_ENTRY_TIME;
		}
		Date standardEntryDateGF = getStartEndTime(standardEntryTimeGF);
		
		String standardExitTimeGF = "";
		standardExitTimeGF = (String)attendanceTimeDir.get(MonthlyManagerConstants.GF_EXIT_TIME_KEY);
		if (StringUtils.isBlank(standardExitTimeGF)) {
			standardExitTimeGF = MonthlyManagerConstants.GF_EXIT_TIME;
		}
		Date standardExitDateGF = getStartEndTime(standardExitTimeGF);
		
		//产业园考勤时间为08:00:00—17:00:00
		String standardEntryTimeEHR = "";
		standardEntryTimeEHR = (String)attendanceTimeDir.get(MonthlyManagerConstants.GRG_ENTRY_TIME_KEY);
		if (StringUtils.isBlank(standardEntryTimeEHR)) {
			standardEntryTimeEHR = MonthlyManagerConstants.GRG_ENTRY_TIME;
		}
		Date standardEntryDateEHR= getStartEndTime(standardEntryTimeEHR);
		
		String standardExitTimeEHR = "";
		standardExitTimeEHR = (String)attendanceTimeDir.get(MonthlyManagerConstants.GRG_EXIT_TIME_KEY);
		if (StringUtils.isBlank(standardExitTimeEHR)) {
			standardExitTimeEHR = MonthlyManagerConstants.GRG_EXIT_TIME;
		}
		Date standardExitDateEHR = getStartEndTime(standardExitTimeEHR);
		
		Map<String,String> attendanceConfigDir = BusnDataDir.getMapKeyValue("systemConfig.systemParamConfig");
		String saparationTime = (String)attendanceConfigDir.get(Constants.SEPARATION_TIME_KEY);
		String deviationTime = MonthlyManagerConstants.DEVIATION_TIME;
		if (StringUtils.isBlank(saparationTime)) {
			saparationTime = MonthlyManagerConstants.SAPARATION_TIME;
		}
		//查询考勤记录
		for (String yyyy_MM_dd : dateListForSearch) {
			List<CardRecord>  list = cardRecordDao.getCardRecordList(yyyy_MM_dd);
			if (list == null || list.size() == 0) {
				return true;
			}
			
			//更新考勤状态
			try {
				for (CardRecord cardRecord : list) {
					Date signTime = cardRecord.getSigntime();
					String type = cardRecord.getType();
					int status = 0;
					boolean isNormalAttendance = checkAttendanceStatus(cardRecord, saparationTime);
					if (isNormalAttendance) {
						if (MonthlyManagerConstants.GRG_ATTENDANCE.equals(type)) {
							//EHR
							status = getAttendanceStatus(saparationTime, deviationTime, 
									standardEntryDateEHR, standardExitDateEHR, signTime);
						} else if(MonthlyManagerConstants.GF_ATTENDANCE.equals(type)) {
							//GF
							status = getAttendanceStatus(saparationTime, deviationTime, 
									standardEntryDateGF, standardExitDateGF, signTime);
						}else{
							//移动签到的状态已经处理了。
						}
					} else {
						status = -1;
					}
					cardRecord.setAttendanceStatus(status);
					cardRecordDao.updateObject(cardRecord);
				}
			} catch (Exception e) {
				SysLog.error("考勤状态更新中异常：" + e.toString());
				return false;
			}
		}
		return true;
	}
}
