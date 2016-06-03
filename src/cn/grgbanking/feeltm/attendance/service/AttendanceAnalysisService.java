package cn.grgbanking.feeltm.attendance.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.attendance.dao.AttendanceAnalysisDao;
import cn.grgbanking.feeltm.attendance.domain.AttendanceAnalysisCount;
import cn.grgbanking.feeltm.attendance.domain.AttendanceListInner;
import cn.grgbanking.feeltm.attendance.domain.AttendanceListOut;
import cn.grgbanking.feeltm.attendance.domain.ExpenseList;
import cn.grgbanking.feeltm.domain.SysUser;
import cn.grgbanking.feeltm.project.service.ProjectService;
import cn.grgbanking.feeltm.staff.service.StaffInfoService;
import cn.grgbanking.feeltm.util.DateUtil;
import cn.grgbanking.framework.service.BaseService;

@Service("attendanceAnalysisService")
@Transactional
public class AttendanceAnalysisService extends BaseService{
	@Autowired
	private AttendanceAnalysisDao attendanceAnalysisDao;
	@Autowired
	private StaffInfoService staffInfoService;
	@Autowired
	private ProjectService projectService;
	
	/**
	 * 批量新增数据
	 * @param leave
	 */
	public void save(AttendanceAnalysisCount attendance) {
		attendanceAnalysisDao.addObject(attendance);
	}
	
	/**
	 * 修改考勤数据
	 * @param attendance
	 * @param status
	 */
	public void update(AttendanceAnalysisCount attendance,String status){
		attendanceAnalysisDao.updateAttendanceDateForLeave(attendance,status);
	}
	
	/**
	 * 修噶假期说明信息
	 * @param record
	 * @param sdate
	 * @param edate
	 */
	public void updateDesc(AttendanceAnalysisCount record,String sdate,String edate){
		//attendanceAnalysisDao.updateLeaveDesc(record, sdate, edate);
	}
	
	/**
	 * 统计前从Ehr同步一次数据
	 */
	public void execDataForEhr(){
		attendanceAnalysisDao.execDataForEhr();
	}
	
	/**
	 * 统计前，执行一次存储过程，将移动签到数据同步到oa_cardrecord表中
	 */
	public void execMobilecard_Proc(){
		attendanceAnalysisDao.execMobilecard_Proc();
	}
	
	/**
	 * 统计前，执行一次存储过程，修改有打卡记录又有移动签到记录的数据
	 */
	public void execProcedure(){
		attendanceAnalysisDao.execProcedure();
	}
	
	/**
	 * 导入数据时，执行一次存储过程
	 */
	public void execOutcard_Proc(){
		attendanceAnalysisDao.execOutcard_Proc();
	}
	/**
	 * 分析所有签到时间，并对时间进行分段处理
	 * @param sdate
	 * @param edate
	 */
	public void getSignRecord(String sdate,String edate){
		try{
			List<AttendanceAnalysisCount> attendanceCountList = new ArrayList<AttendanceAnalysisCount>();
			List list = attendanceAnalysisDao.getSignRecord(sdate, edate);
			for(int i=0;i<list.size();i++){
				Object[] obj = (Object[])list.get(i);
				String userid = obj[0].toString();
				String username = obj[1].toString();
				String deptname = staffInfoService.getDeptNameValueByUserId(userid);
				String groupname = projectService.getProjectNameByUserid(userid);
				String c_date = obj[2].toString();
				String c_time = obj[3].toString();
				String c_type = obj[4].toString();
				SysUser user = staffInfoService.findUserByUserNumber(userid, "2");
				String innernum = user.getJobNumber();
				String outnum = user.getOutNumber();
				
				AttendanceAnalysisCount record = new AttendanceAnalysisCount();
				record.setUserid(userid);
				record.setUsername(username);
				record.setDeptname(deptname);
				record.setGroupname(groupname);
				record.setInnernum(innernum);
				record.setOutnum(outnum);
				record.setWorkdate(c_date);
				//判断工作地点，|1:广发 |0:公司 |2:其他
				if(c_type.indexOf("11")!=-1 || c_type.indexOf("10")!=-1){
					record.setAttendancetype("1");
				}else if(c_type.indexOf("00")!=-1 || c_type.indexOf("01")!=-1 || c_type.indexOf("02")!=-1){
					record.setAttendancetype("0");
				}else{
					record.setAttendancetype("2");
				}
				//判断工作类型
				String worktype = flagIsWorkday(c_date);
				record.setWorktype(worktype);
				//判断时间段
				String[] times = c_time.split("@");
				for(int j=0;j<times.length;j++){
					String signtime = times[j].split("\\|")[0];
					String sdatetime = c_date+" "+signtime;
					String time1 = c_date+" 00:00:00";
					String time2 = c_date+" 09:00:00";
					String time3 = c_date+" 13:00:00";
					String time4 = c_date+" 17:00:00";
					String time5 = c_date+" 18:00:00";
					String time6 = c_date+" 21:00:00";
					String time7 = c_date+" 23:59:59";
					Date datetime = DateUtil.stringToDate(sdatetime, "yyyy-MM-dd HH:mm:ss");
					Date dtime1 = DateUtil.stringToDate(time1, "yyyy-MM-dd HH:mm:ss");
					Date dtime2 = DateUtil.stringToDate(time2, "yyyy-MM-dd HH:mm:ss");
					Date dtime3 = DateUtil.stringToDate(time3, "yyyy-MM-dd HH:mm:ss");
					Date dtime4 = DateUtil.stringToDate(time4, "yyyy-MM-dd HH:mm:ss");
					Date dtime5 = DateUtil.stringToDate(time5, "yyyy-MM-dd HH:mm:ss");
					Date dtime6 = DateUtil.stringToDate(time6, "yyyy-MM-dd HH:mm:ss");
					Date dtime7 = DateUtil.stringToDate(time7, "yyyy-MM-dd HH:mm:ss");
					if((datetime.equals(dtime1) || datetime.after(dtime1)) && (datetime.equals(dtime2) || datetime.before(dtime2))){
						if(record.getTime1()==null || "".equals(record.getTime1()))
							record.setTime1(signtime);
					}else if(datetime.after(dtime2) && (datetime.equals(dtime3) || datetime.before(dtime3))){
						record.setTime2(signtime);
					}else if(datetime.after(dtime3) && (datetime.equals(dtime4) || datetime.before(dtime4))){
						if(record.getTime3()==null || "".equals(record.getTime3()))
							record.setTime3(signtime);
					}else if(datetime.after(dtime4) && (datetime.equals(dtime5) || datetime.before(dtime5))){
						record.setTime4(signtime);
					}else if(datetime.after(dtime5) && (datetime.equals(dtime6) || datetime.before(dtime6))){
						record.setTime5(signtime);
					}else if(datetime.after(dtime6) && (datetime.equals(dtime7) || datetime.before(dtime7))){
						record.setTime6(signtime);
					}
				}
				//判断工作点的上、中、下时间
				if(c_time.indexOf("|1")==-1 && c_time.indexOf("|2")==-1){
					record.setStime("08:00:00");
					record.setMtime("12:00:00");
					record.setMtime2("13:00:00");
					record.setEtime("17:00:00");
					record.setMindate(times[0].split("\\|")[0]);
					record.setMaxdate(times[times.length-1].split("\\|")[0]);
				}else if (c_time.indexOf("|0")==-1 && c_time.indexOf("|2")==-1){
					record.setStime("08:30:00");
					record.setMtime("12:00:00");
					record.setMtime2("14:00:00");
					record.setEtime("17:30:00");
					record.setMindate(times[0].split("\\|")[0]);
					record.setMaxdate(times[times.length-1].split("\\|")[0]);
				}else if (c_time.indexOf("|0")==-1 && c_time.indexOf("|1")==-1){
					record.setStime("08:30:00");
					record.setMtime("12:00:00");
					record.setMtime2("14:00:00");
					record.setEtime("17:30:00");
					record.setMindate(times[0].split("\\|")[0]);
					record.setMaxdate(times[times.length-1].split("\\|")[0]);
				}else{
					//有多个地点时。取第一个跟最后一个作为上下班时间，中午时间默认为12:00:00——14:00:00
					String begin = times[0];
					String end = times[times.length-1];
					String begin_time = begin.split("\\|")[0];
					String begin_where = begin.split("\\|")[1];
					String end_time = end.split("\\|")[0];
					String end_where = end.split("\\|")[1];
					if("0".equals(begin_where))
						record.setStime("08:00:00");
					else
						record.setStime("08:30:00");
					if("0".equals(end_where))
						record.setEtime("17:00:00");
					else
						record.setEtime("17:30:00");
					record.setMtime("12:00:00");
					record.setMtime2("14:00:00");
					record.setMindate(begin_time);
					record.setMaxdate(end_time);
				}
				attendanceCountList.add(record);
			}
			
			for(AttendanceAnalysisCount recordCount:attendanceCountList)
				save(recordCount);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据假期数据表中的数据，更新考勤分析表中的时间段
	 * @param sdate
	 * @param edate
	 */
	public void updateTimeForLeaveData(String sdate,String edate){
		try{
			List<AttendanceAnalysisCount> attendanceCountList = new ArrayList<AttendanceAnalysisCount>();
			List list = attendanceAnalysisDao.getLeaveData(sdate, edate);
			for(int i=0;i<list.size();i++){
				Object[] obj = (Object[])list.get(i);
				String userid = obj[0].toString();
				String c_date = obj[1].toString();
				String c_time = obj[2].toString();
				String[] times = c_time.split("@");
				
				AttendanceAnalysisCount record = new AttendanceAnalysisCount();
				record.setUserid(userid);
				record.setWorkdate(c_date);
				List hasList = attendanceAnalysisDao.flagLeaveIsInAttendance(c_date, userid);
				if(hasList.size()>0){
					Object[] timeobj = (Object[])hasList.get(0);
					record.setTime1(timeobj[0]==null?"":timeobj[0].toString());
					record.setTime2(timeobj[1]==null?"":timeobj[1].toString());
					record.setTime3(timeobj[2]==null?"":timeobj[2].toString());
					record.setTime4(timeobj[3]==null?"":timeobj[3].toString());
					record.setTime5(timeobj[4]==null?"":timeobj[4].toString());
					record.setTime6(timeobj[5]==null?"":timeobj[5].toString());
					record.setMindate(timeobj[6]==null?"":timeobj[6].toString());
					record.setMaxdate(timeobj[7]==null?"":timeobj[7].toString());
					for(int j=0;j<times.length;j++){
						String sdatetime = c_date+" "+times[j];
						String time1 = c_date+" 00:00:00";
						String time2 = c_date+" 09:00:00";
						String time3 = c_date+" 13:00:00";
						String time4 = c_date+" 17:00:00";
						String time5 = c_date+" 18:00:00";
						String time6 = c_date+" 21:00:00";
						String time7 = c_date+" 23:59:59";
						Date datetime = DateUtil.stringToDate(sdatetime, "yyyy-MM-dd HH:mm:ss");
						Date dtime1 = DateUtil.stringToDate(time1, "yyyy-MM-dd HH:mm:ss");
						Date dtime2 = DateUtil.stringToDate(time2, "yyyy-MM-dd HH:mm:ss");
						Date dtime3 = DateUtil.stringToDate(time3, "yyyy-MM-dd HH:mm:ss");
						Date dtime4 = DateUtil.stringToDate(time4, "yyyy-MM-dd HH:mm:ss");
						Date dtime5 = DateUtil.stringToDate(time5, "yyyy-MM-dd HH:mm:ss");
						Date dtime6 = DateUtil.stringToDate(time6, "yyyy-MM-dd HH:mm:ss");
						Date dtime7 = DateUtil.stringToDate(time7, "yyyy-MM-dd HH:mm:ss");
						if((datetime.equals(dtime1) || datetime.after(dtime1)) && (datetime.equals(dtime2) || datetime.before(dtime2))){
							if(record.getTime1()==null || "".equals(record.getTime1())){
								record.setTime1(times[j]);
								record.setMindate(times[j]);
							}
						}else if(datetime.after(dtime2) && (datetime.equals(dtime3) || datetime.before(dtime3))){
							if(record.getTime2()==null || "".equals(record.getTime2())){
								record.setTime2(times[j]);
								if("".equals(record.getTime1())){
									record.setMindate(times[j]);
								}
							}
						}else if(datetime.after(dtime3) && (datetime.equals(dtime4) || datetime.before(dtime4))){
							if(record.getTime3()==null || "".equals(record.getTime3())){
								record.setTime3(times[j]);
								if("".equals(record.getTime1()) && "".equals(record.getTime2())){
									record.setMindate(times[j]);
								}
							}
						}else if(datetime.after(dtime4) && (datetime.equals(dtime5) || datetime.before(dtime5))){
							if(record.getTime4()==null || "".equals(record.getTime4())){
								record.setTime4(times[j]);
								record.setMaxdate(times[j]);
							}
						}else if(datetime.after(dtime5) && (datetime.equals(dtime6) || datetime.before(dtime6))){
							if(record.getTime5()==null || "".equals(record.getTime5())){
								record.setTime5(times[j]);
								record.setMaxdate(times[j]);
							}
						}else if(datetime.after(dtime6) && (datetime.equals(dtime7) || datetime.before(dtime7))){
							if(record.getTime6()==null || "".equals(record.getTime6())){
								record.setTime6(times[j]);
								record.setMaxdate(times[j]);
							}
						}
					}
					attendanceCountList.add(record);
				}else{
					record.setStime("08:00:00");
					record.setMtime("12:00:00");
					record.setMtime2("13:00:00");
					record.setEtime("17:00:00");
					record.setMindate("08:00:00");
					record.setMaxdate("17:00:00");
					record.setTime1("08:00:00");
					record.setTime4("17:00:00");
					attendanceCountList.add(record);
				}
			}
			for(AttendanceAnalysisCount updateList:attendanceCountList)
				update(updateList,"times");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据时间段，判断迟到早退
	 * @param sdate
	 * @param edate
	 */
	public void updateStatusForAttendanceTime(String sdate,String edate){
		try{
			List<AttendanceAnalysisCount> attendanceCountList = new ArrayList<AttendanceAnalysisCount>();
			List list = attendanceAnalysisDao.getAttendanceData(sdate, edate);
			for(int i=0;i<list.size();i++){
				Object[] obj = (Object[])list.get(i);
				String userid = obj[0].toString();
				String c_date = obj[1]==null?"":obj[1].toString();
				String time1 = obj[2]==null?"":obj[2].toString();
				String time2 = obj[3]==null?"":obj[3].toString();
				String time3 = obj[4]==null?"":obj[4].toString();
				String mindate = obj[5]==null?"":obj[5].toString();
				String maxdate = obj[6]==null?"":obj[6].toString();
				String type = obj[7].toString();
				String stime = obj[8].toString();
				String etime = obj[9].toString();
				String mtime = obj[10].toString();
				String mtime2 = obj[11].toString();
				
				AttendanceAnalysisCount record = new AttendanceAnalysisCount();
				record.setUserid(userid);
				record.setWorkdate(c_date);
				
				Date time = DateUtil.stringToDate(c_date+" "+mindate, "yyyy-MM-dd HH:mm:ss");
				Date worktime = DateUtil.stringToDate(c_date+" "+stime, "yyyy-MM-dd HH:mm:ss");
				//Date catchTime = DateUtil.stringToDate(c_date+" 06:00:00", "yyyy-MM-dd HH:mm:ss");
				if(time.after(worktime))
					record.setMorn_islast("迟到");
				else
					record.setMorn_islast("");
				//if(time.before(catchTime))
				//	record.setIscatch("异常");
				
				time = DateUtil.stringToDate(c_date+" "+maxdate, "yyyy-MM-dd HH:mm:ss");
				worktime = DateUtil.stringToDate(c_date+" "+etime, "yyyy-MM-dd HH:mm:ss");
				if(time.before(worktime))
					record.setAfter_isleave("早退");
				else
					record.setAfter_isleave("");
				
				//只有type为1的才判断是否中午迟到早退
				if("1".equals(type)){
					if(!"".equals(time2)){
						time = DateUtil.stringToDate(c_date+" "+time2, "yyyy-MM-dd HH:mm:ss");
						Date worktime1 = DateUtil.stringToDate(c_date+" "+mtime, "yyyy-MM-dd HH:mm:ss");
						if(time.before(worktime1)){
							record.setNoon_isleave("早退");
						}else{
							record.setNoon_isleave("");
						}
					}
					if(!"".equals(time3)){
						time = DateUtil.stringToDate(c_date+" "+time3, "yyyy-MM-dd HH:mm:ss");
						Date worktime2 = DateUtil.stringToDate(c_date+" "+mtime2, "yyyy-MM-dd HH:mm:ss");
						if("".equals(time2)){
							if(time.after(worktime2)){
								record.setNoon_islast("迟到");
							}else{
								record.setNoon_islast("");
							}
						}else{
							record.setNoon_islast("");
						}
					}
					if("".equals(time2) && "".equals(time3)){
						record.setNoon_islast("无记录");
						record.setNoon_isleave("无记录");
					}else{
						if("".equals(time1) && !"".equals(time2)){
							String sdatetime = c_date+" "+time2;
							String aatime = c_date+" 11:00:00";
							String bbtime = c_date+" 14:00:00";
							Date datetime = DateUtil.stringToDate(sdatetime, "yyyy-MM-dd HH:mm:ss");
							Date daatime = DateUtil.stringToDate(aatime, "yyyy-MM-dd HH:mm:ss");
							Date dbbtime = DateUtil.stringToDate(bbtime, "yyyy-MM-dd HH:mm:ss");
							if((datetime.equals(daatime) || datetime.after(daatime)) && (datetime.equals(dbbtime) || datetime.before(dbbtime))){
								
							}else{
								record.setNoon_islast("无记录");
								record.setNoon_isleave("");
							}
						}
					}
				}
				attendanceCountList.add(record);
			}
			for(AttendanceAnalysisCount updateList: attendanceCountList)
				update(updateList,"status");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
//	public void updateAttendanceLeaveDesc(String sdate,String edate){
//		try{
//			List<AttendanceCount> attendanceCountList = new ArrayList<AttendanceCount>();
//			List list = attendanceAnalysisDao.getLeaveDateByDesc(sdate, edate);
//			for(int i=0;i<list.size();i++){
//				Object[] obj = (Object[])list.get(i);
//				String userid = obj[0].toString();
//				double sumtime = Double.parseDouble(obj[1].toString());
//				String type = obj[2].toString();
//				String oarunid = obj[3]==null?"":obj[3].toString();
//				AttendanceCount record = new AttendanceCount();
//				
//				record.setUserid(userid);
//				if("事假".equals(type)){
//					record.setSjia(sumtime);
//					record.setSjiadesc(oarunid);
//				}else if("病假".equals(type)){
//					record.setBjia(sumtime);
//					record.setBjiadesc(oarunid);
//				}else if("年假".equals(type)){
//					record.setNjia(sumtime);
//					record.setNjiadesc(oarunid);
//				}else if("补休假".equals(type)){
//					record.setBxjia(sumtime);
//					record.setBxjiadesc(oarunid);
//				}else{
//					record.setOtherjia(sumtime);
//					record.setOtherdesc(type+" "+oarunid);
//				}
//				attendanceCountList.add(record);
//			}
//			for(AttendanceCount record:attendanceCountList)
//				updateDesc(record,sdate,edate);
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//	}
	/**
	 * 返回当前日期的类型
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public String flagIsWorkday(String date) {
		List list = attendanceAnalysisDao.flagIsWorkday(date, "holiday_date");
		if(list!=null){
			int num = Integer.parseInt(list.get(0).toString());
			if(num>0)
				return "2";
		}
		list = attendanceAnalysisDao.flagIsWorkday(date, "work_date");
		if(list!=null){
			int num = Integer.parseInt(list.get(0).toString());
			if(num>0)
				return "0";
		}
		if (flagIsWeek(date))
			return "1";// 周末
		else
			return "0";// 工作日
	}
	
	/**
	 * 判断日期是否为周末，是true，否false
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public boolean flagIsWeek(String date){
		try{
		Date getdate = DateUtil.stringToDate(date, "yyyy-MM-dd");
		int day = getdate.getDay();
		if (day == 0 || day == 6) {
			return true;
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 导出广发考勤数据
	 * @param sdate
	 * @param edate
	 * @return
	 */
	public List exportOutAttendanceData(String sdate,String edate){
		List list = attendanceAnalysisDao.exportOutAttendanceData(sdate, edate);
		return list;
	}
	
	/**
	 * 导出广发考勤明细数据
	 * @param sdate
	 * @param edate
	 * @return
	 */
	public List exportAttendanceDetailTableData(String sdate,String edate){
		List list = attendanceAnalysisDao.exportAttendanceDetailTableData(sdate, edate);
		return list;
	}
	
	/**
	 * 导出广发考勤异常数据
	 * @param sdate
	 * @param edate
	 * @return
	 */
	public List exportAttendanceCatchData(String sdate,String edate){
		List list = attendanceAnalysisDao.exportAttendanceCatchData(sdate, edate);
		return list;
	}
	
	/**
	 * 导出公司考勤数据
	 * @param sdate
	 * @param edate
	 * @return
	 */
	public List exportAllAttendanceData(String sdate,String edate){
		List list = attendanceAnalysisDao.exportAllAttendanceData(sdate, edate);
		return list;
	}
	
	/**
	 * 执行考勤存储过程
	 */
	public void execAttendanceProc(){
		attendanceAnalysisDao.execAttendanceProc();
	}
	
	/**
	 * 广发考勤列表
	 * @param sdate
	 * @param edate
	 * @return
	 */
	public List getAttendanceOut(String sdate,String edate) {
		List list = attendanceAnalysisDao.getAttendanceOut(sdate,edate);
		List<AttendanceListOut> returnList = new ArrayList<AttendanceListOut>();
		for(int i=0;i<list.size();i++){
			Object[] obj = (Object[])list.get(i);
			String username = obj[0]==null?"":obj[0].toString();
			String workday = obj[1]==null?"":obj[1].toString();
			String mornlast = obj[2]==null?"":obj[2].toString();
			String afterlast = obj[3]==null?"":obj[3].toString();
			String desc1 = obj[4]==null?"":obj[4].toString();
			String leave = obj[5]==null?"":obj[5].toString();
			String desc2 = obj[6]==null?"":obj[6].toString();
			String nowork = obj[7]==null?"":obj[7].toString();
			String desc3 = obj[8]==null?"":obj[8].toString();
			String catchnum = obj[9]==null?"":obj[9].toString();
			String sjia = obj[10]==null?"":obj[10].toString();
			String sjiadesc = obj[11]==null?"":obj[11].toString();
			String bjia = obj[12]==null?"":obj[12].toString();
			String bjiadesc = obj[13]==null?"":obj[13].toString();
			String njia = obj[14]==null?"":obj[14].toString();
			String njiadesc = obj[15]==null?"":obj[15].toString();
			String otherjia = obj[16]==null?"":obj[16].toString();
			String otherdesc = obj[17]==null?"":obj[17].toString();
			AttendanceListOut record = new AttendanceListOut();
			record.setUsername(username);
			record.setWorkday(workday);
			record.setMornlast(mornlast);
			record.setAfterlast(afterlast);
			record.setDesc1(desc1);
			record.setLeave(leave);
			record.setDesc2(desc2);
			record.setNowork(nowork);
			record.setDesc3(desc3);
			record.setCatchnum(catchnum);
			record.setSjia(sjia);
			record.setSjiadesc(sjiadesc);
			record.setBjia(bjia);
			record.setBjiadesc(bjiadesc);
			record.setNjia(njia);
			record.setNjiadesc(njiadesc);
			record.setOtherjia(otherjia);
			record.setOtherdesc(otherdesc);
			returnList.add(record);
		}
		return returnList;
	}
	
	/**
	 * 公司月底汇总
	 * @param sdate
	 * @param edate
	 * @return
	 */
	public List getAttendanceInner(String sdate,String edate){
		List list = attendanceAnalysisDao.getAttendanceInner(sdate, edate);
		List<AttendanceListInner> returnList = new ArrayList<AttendanceListInner>();
		for(int i=0;i<list.size();i++){
			Object[] obj = (Object[])list.get(i);
			String username = obj[0]==null?"":obj[0].toString();
			String workday = obj[1]==null?"":obj[1].toString();
			String mornlast = obj[2]==null?"":obj[2].toString();
			String leave = obj[3]==null?"":obj[3].toString();
			String sjia = obj[4]==null?"":obj[4].toString();
			String sjiadesc = obj[5]==null?"":obj[5].toString();
			String bjia = obj[6]==null?"":obj[6].toString();
			String bjiadesc = obj[7]==null?"":obj[7].toString();
			String njia = obj[8]==null?"":obj[8].toString();
			String njiadesc = obj[9]==null?"":obj[9].toString();
			String otherjia = obj[10]==null?"":obj[10].toString();
			String otherdesc = obj[11]==null?"":obj[11].toString();
			String tripday = obj[12]==null?"":obj[12].toString();
			String tripdesc = obj[13]==null?"":obj[13].toString();
			String afterrestday = obj[14]==null?"":obj[14].toString();
			String tripaddday = obj[15]==null?"":obj[15].toString();
			String tripadddesc = obj[16]==null?"":obj[16].toString();
			String overday = obj[17]==null?"":obj[17].toString();
			String overdesc = obj[18]==null?"":obj[18].toString();
			String bxjia = obj[19]==null?"":obj[19].toString();
			String bxjiadesc = obj[20]==null?"":obj[20].toString();
			String monthrestday = obj[21]==null?"":obj[21].toString();
			String before_deferred = obj[22]==null?"":obj[22].toString();
			String month_deferred = obj[23]==null?"":obj[23].toString();
			String userid = obj[27]==null?"":obj[27].toString();
			String attendancetype = obj[30]==null?"":obj[30].toString();
			AttendanceListInner record = new AttendanceListInner();
			record.setUsername(username);
			record.setWorkday(workday);
			record.setMornlast(mornlast);
			record.setLeave(leave);
			record.setSjia(sjia);
			record.setSjiadesc(sjiadesc);
			record.setBjia(bjia);
			record.setBjiadesc(bjiadesc);
			record.setNjia(njia);
			record.setNjiadesc(njiadesc);
			record.setOtherjia(otherjia);
			record.setOtherdesc(otherdesc);
			record.setTripday(tripday);
			record.setTripdesc(tripdesc);
			record.setAfterrestday(afterrestday);
			record.setTripaddday(tripaddday);
			record.setTripadddesc(tripadddesc);
			record.setOverday(overday);
			record.setOverdesc(overdesc);
			record.setBxjia(bxjia);
			record.setBxjiadesc(bxjiadesc);
			record.setMonthrestday(monthrestday);
			record.setBefore_deferred(before_deferred);
			record.setMonth_deferred(month_deferred);
			record.setUserid(userid);
			record.setAttendancetype(attendancetype);
			returnList.add(record);
		}
		return returnList;
	}
	
	/**
	 * 报销列表
	 * @param sdate
	 * @param edate
	 * @return
	 */
	public List getExpenseData(String sdate,String edate){
		List<ExpenseList> returnList = new ArrayList<ExpenseList>();
		List list = attendanceAnalysisDao.getExpenseData(sdate, edate);
		for(int i=0;i<list.size();i++){
			Object[] obj = (Object[])list.get(i);
			String username = obj[0]==null?"":obj[0].toString();
			String workdate = obj[1]==null?"":obj[1].toString();
			String stime = obj[2]==null?"":obj[2].toString();
			String etime = obj[3]==null?"":obj[3].toString();
			String mintime = obj[4]==null?"":obj[4].toString();
			String maxtime = obj[5]==null?"":obj[5].toString();
			String money = obj[6]==null?"":obj[6].toString();
			ExpenseList record = new ExpenseList();
			record.setUsername(username);
			record.setWorkdate(workdate);
			record.setStime(stime);
			record.setEtime(etime);
			record.setMintime(mintime);
			record.setMaxtime(maxtime);
			record.setMoney(money);
			returnList.add(record);
		}
		return returnList;
	}
	
	/**
	 * 修改已经统计过的数据
	 * @param sdate
	 * @param edate
	 */
	public void updateCardDealStatus(String sdate,String edate){
		attendanceAnalysisDao.updateCardDealStatus(sdate, edate);
	}
	
	public void execTimeAttendanceProc(String sdate,String edate){
		attendanceAnalysisDao.execTimeAttendanceProc(sdate, edate);
	}
	
	public void execOvertimeProc(){
		attendanceAnalysisDao.execOvertimeProc();
	}
	
	public void execTripProc(){
		attendanceAnalysisDao.execTripProc();
	}
	
	public boolean updateUserHols(String userid,String deferred){
		attendanceAnalysisDao.updateUserHols(userid, deferred);
		return true;
	}
}
