package cn.grgbanking.feeltm.report.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.domain.testsys.WeekReport;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.report.dao.WeekReportDao;
import cn.grgbanking.framework.util.Page;

@Service("weekReportService")
@Transactional
public class WeekReportService {
	
	@Autowired
	private WeekReportDao weekReportDao;
	
	public boolean add(WeekReport report){
		boolean flag=false;
		try{
			weekReportDao.addObject(report);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	public boolean delete(String[] ids){
		return weekReportDao.remove(ids);
	}
	
	public boolean delete(WeekReport report){
		boolean flag = false;
		try {
			weekReportDao.removeObject(WeekReport.class, report.getId());
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	public boolean update(WeekReport report){
		boolean flag=false;
		try{
			weekReportDao.updateObject(report);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	@Transactional(readOnly = true)
	public WeekReport getWeekReportById(String id){
		return (WeekReport)weekReportDao.getObject(WeekReport.class, id);
	}
	
	@Transactional(readOnly = true)
	public Page getPage(String start,String end,String groupName,int pageNum,int pageSize) {
		return weekReportDao.getPage(start,end,groupName,pageNum,pageSize);
	}
	
	@Transactional(readOnly = true)
	public WeekReport getNextWeekPlan(String groupName,Date currentEndDate){
		return weekReportDao.getNextWeekPlan(groupName, currentEndDate);
	}
	
	@Transactional(readOnly = true)
	public List<WeekReport> getAllReportsByWeek(){
		return weekReportDao.getAllReportsByWeek();
	}
}
