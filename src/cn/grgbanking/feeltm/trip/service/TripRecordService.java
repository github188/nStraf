package cn.grgbanking.feeltm.trip.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.attendance.dao.AttendanceAnalysisDao;
import cn.grgbanking.feeltm.leave.service.LeaveRecordService;
import cn.grgbanking.feeltm.trip.dao.TripRecordDao;
import cn.grgbanking.feeltm.trip.domain.TripRecord;
import cn.grgbanking.feeltm.util.DateUtil;
import cn.grgbanking.framework.service.BaseService;

@Service("tripRecordService")
@Transactional
public class TripRecordService extends BaseService{
	@Autowired
	private TripRecordDao tripRecordDao;
	@Autowired
	private AttendanceAnalysisDao attendanceAnalysisDao;
	
	/**
	 * 新增数据
	 * @param trip
	 */
	public void add(TripRecord trip) {
		tripRecordDao.addObject(trip);
	}
	
	/**
	 * 分析所有出差数据到List
	 */
	public void getTripRecord(){
		try{
			List list = tripRecordDao.getLeaveRecord();
			List<TripRecord> tripRecordList = new ArrayList<TripRecord>();
			for(int k=0;k<list.size();k++){
				Object[] obj = (Object[])list.get(k);
				String userid = obj[0].toString();
				String sdate = obj[1].toString();
				String edate = obj[2].toString();
				String oarunid = obj[4]==null?"":obj[4].toString();
				
				if(sdate.equals(edate)){
					TripRecord record = new TripRecord();
					record.setUserid(userid);
					record.setStartdate(sdate);
					record.setEnddate(edate);
					record.setOarunid(oarunid);
					record.setSumtime(1);
					tripRecordList.add(record);
				}else{
					Date s_date = DateUtil.stringToDate(sdate, "yyyy-MM-dd");
					Date e_date = DateUtil.stringToDate(edate, "yyyy-MM-dd");
					if(e_date.before(s_date)){
						continue;
					}
					String[] days = getDaysArray(sdate,edate);
					for(int i=0;i<days.length;i++){
						//判断是否为周末或节假日，如果是continue，否则计算
						if(!"0".equals(flagIsWorkday(days[i]))){
							continue;
						}else{
							TripRecord record = new TripRecord();
							record.setUserid(userid);
							record.setOarunid(oarunid);
							record.setStartdate(days[i]);
							record.setEnddate(days[i]);
							record.setSumtime(1);
							tripRecordList.add(record);
						}
					}
				}
			}
			for(TripRecord record:tripRecordList)
				add(record);
		}catch(Exception e){
			e.printStackTrace();
		}
			
	}
	
	/**
	 *  执行存储过程，修改有打卡记录又有移动签到的记录
	 */
	public void execProcedure(){
		tripRecordDao.execProcedure();
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
