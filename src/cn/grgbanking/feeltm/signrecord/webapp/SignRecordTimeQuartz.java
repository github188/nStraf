package cn.grgbanking.feeltm.signrecord.webapp;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.grgbanking.feeltm.config.BusnDataDir;
import cn.grgbanking.feeltm.domain.SignRecord;
import cn.grgbanking.feeltm.project.domain.Project;
import cn.grgbanking.feeltm.project.domain.ProjectAttendance;
import cn.grgbanking.feeltm.project.service.ProjectService;
import cn.grgbanking.feeltm.signrecord.service.SignRecordService;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.DateUtil;
import cn.grgbanking.feeltm.util.GeocodeAddressUtil;
@Service
public class SignRecordTimeQuartz {

	@Autowired
	private SignRecordService signRecordService;

	@Autowired
	private ProjectService projectService;
	
	/** 定时更新的project列表 */
	private List<Project> regularlyUpdateProjectList;
	/** 最近一次定时更新的时间 */
	private Date lastRegularlyUpdateTime;

	/**
	 * 定时获取地址 lhyan3 2014年6月17日
	 */
	public void getAddressTime() {
		List<SignRecord> records = signRecordService.getRecordAddrisNull();//获取所有移动签到中签到地址为null的jilu
		//定时器定时执行，每隔15分钟更新一次
		regularlyUpdateProjectList = projectService.listAllGroup();//获取所有的项目
		lastRegularlyUpdateTime=new Date();
		if (records != null && records.size() > 0) {
			for (SignRecord record : records) {
				attendanceAnalyser(record);
			}
		}
	}
	
	public void attendanceAnalyser(String recordId){
		SignRecord record=signRecordService.findById(recordId);
		attendanceAnalyser(record);
	}
	
	/**将检查考勤状态和识别考勤地址的方法独立出来，方便用户在签到后即刻查询
	 * wtjiao 2014年12月12日 上午8:36:23
	 * @param record
	 */
	public void attendanceAnalyser(SignRecord record){
		//如果定时器没执行，或者由于某些错误，导致40分钟内没有更新
		if(regularlyUpdateProjectList==null || (lastRegularlyUpdateTime==null || new Date().getTime()-lastRegularlyUpdateTime.getTime()>40*60*1000)){
			regularlyUpdateProjectList=projectService.listAllGroup();
			lastRegularlyUpdateTime=new Date();
		}
		
		String saparationTime=getSaparationTime();
		String deviationTime=getDeviationTime();
		
		double longitude = record.getLongitude();
		double latitude = record.getLatitude();
		Date signTime = record.getSignTime();
		Date entryTime = null;
		Date exitTime = null;
		if (record.getFlag() == 1) {
			// html5定位需要矫正偏差
			try {
				String[] latlng = GeocodeAddressUtil.changeToBaidu(
						record.getLongitude(), record.getLatitude());
				if (latlng != null && latlng.length == 2) {
					longitude = Double.valueOf(latlng[1]);
					latitude = Double.valueOf(latlng[0]);
				}
			} catch (Exception e) {
				e.printStackTrace();
				//抛出异常后进入下一个
				return ;
			}
		}
		//得到签到人所在在项目，可能有1个以上在项目
		String[] recordProjectArray = null;
		//if (record.getGrpName() != null) {
		//	recordProjectArray = record.getGrpName().split(",");
		//}
		//人员所在项目，根据项目管理中的ling.tu2015-02-03
		recordProjectArray = signRecordService.getGroupnameByuserid(record.getUserId(), DateUtil.getDateString(record.getSignTime())).split(",");
		// 判断有效性
		record.setVilid("0");
		if (recordProjectArray != null ) {
			for (Project project : regularlyUpdateProjectList) {
				for (int i = 0; i < recordProjectArray.length; i++) {
					if (project.getId().equals(recordProjectArray[i])) {
						for (ProjectAttendance attendance : project.getAttendances()) {
							if (attendance.getLatitude() != null) {
								boolean flag = GeocodeAddressUtil.getDistance(
										attendance.getLongitude(),
										attendance.getLatitude(), longitude, latitude);
								if (flag) {
									// 有效就退出
									record.setVilid("1");
									//判断有效性的同时,取出当前的考勤时间段用于判断迟到早退还是正常上班
									entryTime = attendance.getEntryTime();
									exitTime = attendance.getExitTime();
									break;
								}
							}
						}//end for
					}//end if
				}//end for
			}//end for
		}
		
		/**
		 * 考勤状态的处理.
		 */
		//判断是出勤时间还是退勤时间,分界点13:00:00之前出勤时间,之后为退勤时间
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
		// 根据得到的值更新到签到表的状态 
		record.setAttendanceStatus(statusKey);
		
		// 逆编码
		try {
			String address = GeocodeAddressUtil.getAddressBylanlng(
					record.getLongitude(), record.getLatitude());
			record.setAreaName(address);
		} catch (Exception e) {
			e.printStackTrace();
			//抛出异常后进入下一个
			return;
		}
		signRecordService.update(record);
	}
	
	private String getDeviationTime() {
		Map<String,String> attendanceConfigDir = BusnDataDir.getMapKeyValue("systemConfig.systemParamConfig");
		String deviationTime = (String)attendanceConfigDir.get(Constants.DEVIATION_TIME_KEY);
		if (StringUtils.isBlank(deviationTime)) {
			deviationTime = "180";
		}
		return deviationTime;
	}

	private String getSaparationTime() {
		Map<String,String> attendanceConfigDir = BusnDataDir.getMapKeyValue("systemConfig.systemParamConfig");
		String saparationTime = (String)attendanceConfigDir.get(Constants.SEPARATION_TIME_KEY);
		if (StringUtils.isBlank(saparationTime)) {
			saparationTime = "13:00:00";
		}
		return saparationTime;
	}

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
	 * 考勤时间(只取时分秒，去掉日期来计算秒数)
	 * @param time
	 * @return 秒数
	 */
	private long getTimes(Date time){
		long times = (time.getHours())* 60 * 60 + (time.getMinutes() * 60) + time.getSeconds();
		return times;
	}
	
	/**
	 * 
	 * @param signedTime 移动签到时间
	 * @param workTime   项目规定的考勤时间点
	 * @param signedType 考勤类型(1:出勤/2:退勤)
	 * @param deviationTime 允许的误差时间(分钟)
	 * @return 返回为0则为迟到或早退, 1为正常打卡, -1为异常的项目规定的考勤数据
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

	public SignRecordService getSignRecordService() {
		return signRecordService;
	}

	public void setSignRecordService(SignRecordService signRecordService) {
		this.signRecordService = signRecordService;
	}

	public static void main(String[] args) throws Exception {
//		boolean distance = GeocodeAddressUtil.getDistance(113.45299505747,
//				23.148164516301, 113.452821, 23.148187);
		/*
		 * if(distance<=standard){ System.out.println("在范围内"); }else{
		 * System.out.println("不在范围内"); }
		 */
//		System.out.println(distance);
		
		/*Date dt1 = new Date();
		String hms = getDateToHHMMSS(dt1);
		Date dt2 = new Date();
		String time = "2";
		int d = checkAttendanceTime(dt1,dt2,1,time);
		d = checkAttendanceTime(dt2,dt1,1,time);
		d = checkAttendanceTime(dt2,dt1,2,time);
		d = checkAttendanceTime(dt1,dt2,2,time);*/
		
		new SignRecordTimeQuartz().getAddressTime();
	}

	/*
	 * public static void aaaa() throws ParseException{ //String str =
	 * "POLYGON ((114.171033 22.566715, 419164481 143702737, 419164494 143702527,114.171033 22.566715))"
	 * ; //String str = "POINT ((114.171033 22.566715),100)"; String str =
	 * "POINT(22.566715,114.171033,1000)"; WKTReader wkt = new WKTReader();
	 * Geometry geojudge1 = wkt.read(str); double xpoi = 114.171034; double ypoi
	 * = 22.566715; Geometry geojudge2 = wkt.read("POINT(" + ypoi + " " + xpoi +
	 * "))"); if(geojudge1.intersects(geojudge2)) {
	 * System.out.println("xpoi、ypoi 在这个面里"); } else{
	 * System.out.println("不在这里面"); } }
	 */

	public ProjectService getProjectService() {
		return projectService;
	}

	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}

}
