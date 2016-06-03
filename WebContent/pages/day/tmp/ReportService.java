package cn.grgbanking.feeltm.report.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.domain.SpecialRegulation;
import cn.grgbanking.feeltm.domain.testsys.WorkReport;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.report.dao.ReportDao;
import cn.grgbanking.feeltm.report.domain.ReportDayInfo;
import cn.grgbanking.feeltm.report.domain.ReportInfo;
import cn.grgbanking.framework.service.BaseService;
import cn.grgbanking.framework.util.Page;

@Service("reportService")
@Transactional
public class ReportService extends BaseService {
	@Autowired
	private ReportDao reportDao;
	
	
	/**
	 * boolean flag = false;
		try {
			specialRegulationDao.addObject(obj);
			flag = true;
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	 * @param report
	 * @return
	 */
	public boolean add(WorkReport report){
		boolean flag=false;
		try{
			reportDao.addObject(report);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	
	public boolean add(List<WorkReport> reports){
		boolean flag=false;
		try{
			reportDao.add(reports);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	public boolean delete(String[] ids){
		return reportDao.remove(ids);
	}
	
	public boolean delete(WorkReport report){
		boolean flag = false;
		try {
			reportDao.removeObject(WorkReport.class, report.getId());
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	public boolean update(WorkReport report){
		boolean flag=false;
		try{
			reportDao.updateObject(report);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	@Transactional(readOnly = true)
	public WorkReport getReportById(String id){
		return (WorkReport)reportDao.getObject(WorkReport.class, id);
	}
	
	@Transactional(readOnly = true)
	public Page getPage(ReportInfo info, int pageNum, int pageSize) {
		return reportDao.getPage(info, pageNum, pageSize);
	}
	
	@Transactional(readOnly = true)
	public Long getSum(Date createDate,String username){
		return reportDao.selectSum(createDate,username);
	}
	
	
	
	
	public boolean updateAll(String username,Date createDate,List<WorkReport> reports){
		boolean flag=false;
		try{
			reportDao.updateAll(username, createDate, reports);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	@Transactional(readOnly = true)
	public Page getPage(ReportDayInfo info, int pageNum, int pageSize) {
		return reportDao.getPage(info, pageNum, pageSize);
	}
	
	@Transactional(readOnly = true)
	public List<WorkReport> getReportsByDay(String username,Date createDate){
		return reportDao.selectReports(username, createDate);
	}
	
	public boolean remove(String username,Date createDate){
		return reportDao.remove(username, createDate);
	}
	
	@Transactional(readOnly = true)
	public List<Object> getNames(String groupName){
		return reportDao.getUsernamesByGroup(groupName);
	}
	
}
