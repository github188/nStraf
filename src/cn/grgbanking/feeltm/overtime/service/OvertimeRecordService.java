package cn.grgbanking.feeltm.overtime.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.overtime.dao.OvertimeRecordDao;
import cn.grgbanking.feeltm.overtime.domain.OvertimeRecord;
import cn.grgbanking.feeltm.util.DateUtil;
import cn.grgbanking.framework.service.BaseService;

@Service("overtimeRecordService")
@Transactional
public class OvertimeRecordService extends BaseService{
	@Autowired
	private OvertimeRecordDao overtimeRecordDao;
	
	/**
	 * 新增数据
	 * @param trip
	 */
	public void add(OvertimeRecord overtime) {
		overtimeRecordDao.addObject(overtime);
	}
	
	/**
	 * 分析所有出差数据到List
	 */
	public void getOvertimeRecord(){
		try{
			List list = overtimeRecordDao.getOvertimeRecord();
			List<OvertimeRecord> overtimeRecordList = new ArrayList<OvertimeRecord>();
			for(int k=0;k<list.size();k++){
				Object[] obj = (Object[])list.get(k);
				String userid = obj[0].toString();
				String sdate = obj[1].toString();
				String stime = obj[2].toString();
				String edate = obj[3].toString();
				String etime = obj[4].toString();
				String oarunid = obj[6]==null?"":obj[6].toString();
				double sumtime = Double.parseDouble(getSumtime(sdate+" "+stime,edate+" "+etime));
				OvertimeRecord record = new OvertimeRecord();
				record.setUserid(userid);
				record.setStartdate(sdate+" "+stime);
				record.setEnddate(edate+" "+etime);
				record.setOarunid(oarunid);
				record.setSumtime(sumtime);
				overtimeRecordList.add(record);
			}
			for(OvertimeRecord record:overtimeRecordList)
				add(record);
		}catch(Exception e){
			e.printStackTrace();
		}
			
	}
	
	/**
	 *  执行存储过程，修改有打卡记录又有移动签到的记录
	 */
	public void execProcedure(){
		overtimeRecordDao.execProcedure();
	}
	
	public String getSumtime(String sdate,String edate){
		String startdate = sdate.split(" ")[0];
		String starttime = "";
		if(sdate.split(" ").length==1){
			starttime = "00:00:00";
		}else{
			starttime = sdate.split(" ")[1];
		}
		String enddate = edate.split(" ")[0];
		String endtime = edate.split(" ")[1];
		String start = startdate+" "+starttime;
		String end = enddate+" "+endtime;
		String rest_start = start.split(" ")[0]+" "+"12:00:00";
		String rest_end = start.split(" ")[0]+" "+"13:00:00";
		//加班时间段
		Date workstart_date = DateUtil.stringToDate(start.substring(0, 17)+"00", "yyyy-MM-dd HH:mm:ss");
		Date workend_date = DateUtil.stringToDate(end.substring(0, 17)+"00", "yyyy-MM-dd HH:mm:ss");
		//公司中午休息时间
		Date reststart_date = DateUtil.stringToDate(rest_start, "yyyy-MM-dd HH:mm:ss");
		Date restend_date = DateUtil.stringToDate(rest_end, "yyyy-MM-dd HH:mm:ss");
		double sumtime = 0;
		if(restend_date.before(workstart_date)){//if(mtime2<stime)
			sumtime += DateUtil.getHourMinute(workstart_date, workend_date);//stime,etime
		}else if(workstart_date.before(reststart_date) && restend_date.before(workend_date)){//if(stime<mtime and mtime2<etime)
			sumtime += DateUtil.getHourMinute(workstart_date, reststart_date);//stime,mtime
			sumtime += DateUtil.getHourMinute(restend_date, workend_date);//mtime2,etime
		}else if(workstart_date.before(reststart_date) && workend_date.before(reststart_date)){//if(stime<mtime && etime<mtime)
			sumtime += DateUtil.getHourMinute(workstart_date, workend_date);//stime,etime
		}else if(workstart_date.before(restend_date) && workstart_date.after(reststart_date)){//if(stime<mtime2 and stime>mtime)
			sumtime += DateUtil.getHourMinute(restend_date, workend_date);//mtime2,etime
		}else{
			sumtime += DateUtil.getHourMinute(workstart_date, reststart_date);//stime,mtime
		}
		BigDecimal b = new BigDecimal(sumtime);
		sumtime = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();  
		return sumtime+"";
	}
	public static void main(String[] args) {
		String aa="2014-6-28 18:30:00";
		System.out.println(aa.split(" ").length);
	}
}
