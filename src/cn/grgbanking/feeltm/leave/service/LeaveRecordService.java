package cn.grgbanking.feeltm.leave.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.attendance.dao.AttendanceAnalysisDao;
import cn.grgbanking.feeltm.config.TimeConfig;
import cn.grgbanking.feeltm.leave.dao.LeaveRecordDao;
import cn.grgbanking.feeltm.leave.domain.LeaveRecord;
import cn.grgbanking.feeltm.util.DateUtil;
import cn.grgbanking.framework.service.BaseService;

@Service("leaveRecordService")
@Transactional
public class LeaveRecordService extends BaseService{
	@Autowired
	private LeaveRecordDao leaveRecordDao;
	@Autowired
	private AttendanceAnalysisDao attendanceAnalysisDao;
	
	/**
	 * 新增数据
	 * @param leave
	 */
	public void add(LeaveRecord leave) {
		leaveRecordDao.addObject(leave);
	}
	
	/**
	 * 分析所有假期数据到List
	 */
	public void getLeaveRecord(){
		try{
			List list = leaveRecordDao.getLeaveRecord();
			List<LeaveRecord> leaveRecordList = new ArrayList<LeaveRecord>();
			for(int k=0;k<list.size();k++){
				Object[] leave = (Object[])list.get(k);
				String startdate = leave[0].toString();
				String enddate = leave[1].toString();
				String starttime = leave[2].toString()==null?"08:00:00":leave[2].toString();
				String endtime = leave[3].toString().equals("00:00:00")?"17:00:00":leave[3].toString();
				String userid = leave[4].toString();
				String username = leave[5].toString();
				String deptname = leave[6].toString();
				String s_starttime = starttime;
				String s_endtime = endtime;
				String type = leave[8].toString();
				String oarunid = leave[9]==null?"":leave[9].toString();
				
				if(startdate.equals(enddate)){
					LeaveRecord record = new LeaveRecord();
					record.setUserid(userid);
					record.setUsername(username);
					record.setDeptname(deptname);
					record.setType(type);
					record.setOarunid(oarunid);
					record.setStartdate(startdate);
					record.setEnddate(enddate);
					record.setStarttime(starttime);
					record.setEndtime(endtime);
					String sdate = startdate+" "+starttime;
					String edate = enddate+ " "+endtime;
					startdate = sdate.substring(0, sdate.length()-2)+"00";
					enddate = edate.substring(0,edate.length()-2)+"00";
					Date start_time = DateUtil.stringToDate(startdate, "yyyy-MM-dd HH:mm:ss");
					Date end_time = DateUtil.stringToDate(enddate, "yyyy-MM-dd HH:mm:ss");
					double dsumtime = TimeConfig.getSumTime(start_time, end_time);
					record.setSumtime(dsumtime);
					leaveRecordList.add(record);
				}else{
					String[] days = getDaysArray(startdate,enddate);
					for(int i=0;i<days.length;i++){
						//判断是否为周末或节假日，如果是continue，否则计算
						if(!"0".equals(flagIsWorkday(days[i]))){
							continue;
						}else{
							String sdate = "";
							String edate = "";
							if(days[i].equals(startdate)){
								sdate = days[i] + " " + starttime;
							}else{
								sdate = days[i] + " 08:00:00";
								s_starttime = "08:00:00";
							}
							if(days[i].equals(enddate)){
								edate = days[i] + " " + endtime;
							}else{
								edate = days[i] +" 17:00:00";
								s_endtime = "17:00:00";
							}
							sdate = sdate.substring(0, sdate.length()-2)+"00";
							edate = edate.substring(0,edate.length()-2)+"00";
							Date start_time = DateUtil.stringToDate(sdate, "yyyy-MM-dd HH:mm:ss");
							Date end_time = DateUtil.stringToDate(edate, "yyyy-MM-dd HH:mm:ss");
							if(end_time.before(start_time)){
								continue;
							}
							
							double dsumtime = TimeConfig.getSumTime(start_time, end_time);
							
							LeaveRecord record = new LeaveRecord();
							record.setUserid(userid);
							record.setUsername(username);
							record.setDeptname(deptname);
							record.setType(type);
							record.setOarunid(oarunid);
							record.setStartdate(days[i]);
							record.setEnddate(days[i]);
							record.setStarttime(s_starttime);
							record.setEndtime(s_endtime);
							record.setSumtime(dsumtime);
							leaveRecordList.add(record);
						}
					}
				}
			}
			for(LeaveRecord leaveRecord:leaveRecordList)
				add(leaveRecord);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 *  执行存储过程，修改有打卡记录又有移动签到的记录
	 */
	public void execProcedure(){
		leaveRecordDao.execProcedure();
	}
	
	/**
	 * 根据时间段，得出日期数组
	 * @param s1
	 * @param s2
	 * @return
	 * @throws Exception
	 */
	public String[] getDaysArray(String s1, String s2){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 	
		try{
			Date begin = sdf.parse(s1);      		
			Date end = sdf.parse(s2);      		
			double between=(end.getTime()-begin.getTime())/1000;//除以1000是为了转换成秒      		
			double day=between/(24*3600);	
			String[] days = new String[(int)(day+1)];
			for(int i = 1;i<=day+1;i++){
				Calendar cd = Calendar.getInstance();           
				cd.setTime(sdf.parse(s1));           
				cd.add(Calendar.DATE, i-1);//增加一天           
				//cd.add(Calendar.MONTH, n);//增加一个月        
				days[i-1] = sdf.format(cd.getTime());
			}
			return days;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public static void main(String[] args) {
		LeaveRecordService dao = new LeaveRecordService();
		String[] aa = dao.getDaysArray("2014-04-04","2014-04-25");
		for(int i=0;i<aa.length;i++){
			System.out.println(aa[i]);
		}
	}
	
	/**
	 * 判断日期是否为工作日、周末或节假日 
	 * 1：周末或节假日 0：工作日
	 * @param date
	 * @return
	 */
	public String flagIsWorkday(String date) {
		List list = attendanceAnalysisDao.flagIsWorkday(date, "holiday_date");
		if(list!=null){
			int num = Integer.parseInt(list.get(0).toString());
			if(num>0)
				return "1";
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
	 * 判断当天是否为周末 
	 * true：周末 false：工作日
	 * @param date
	 * @return
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
}
