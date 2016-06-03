package cn.grgbanking.feeltm.report.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.domain.testsys.MonthReport;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.report.dao.MonthReportDao;
import cn.grgbanking.framework.util.Page;

@Service("monthReportService")
@Transactional
public class MonthReportService {
	
	@Autowired
	private MonthReportDao monthReportDao;
	
	public boolean add(MonthReport report){
		boolean flag=false;
		try{
			monthReportDao.addObject(report);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	public boolean delete(String[] ids){
		return monthReportDao.remove(ids);
	}
	
	public boolean delete(MonthReport report){
		boolean flag = false;
		try {
			monthReportDao.removeObject(MonthReport.class, report.getId());
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	public boolean update(MonthReport report){
		boolean flag=false;
		try{
			monthReportDao.updateObject(report);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	@Transactional(readOnly = true)
	public MonthReport getMonthReportById(String id){
		return (MonthReport)monthReportDao.getObject(MonthReport.class, id);
	}
	
	@Transactional(readOnly = true)
	public Page getPage(String start,String end,String groupName,int pageNum,int pageSize) {
		return monthReportDao.getPage(start,end,groupName,pageNum,pageSize);
	}
	
	@Transactional(readOnly = true)
	public boolean isExist(String groupName,Date startDate){
		return monthReportDao.isExist(groupName, startDate);
	}
	
	
	
}
