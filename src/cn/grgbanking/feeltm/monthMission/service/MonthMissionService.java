package cn.grgbanking.feeltm.monthMission.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.domain.testsys.MonthMission;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.monthMission.dao.MonthMissionDao;
import cn.grgbanking.framework.service.BaseService;
import cn.grgbanking.framework.util.Page;

@Service("monthMissionService")
@Transactional
public class MonthMissionService extends BaseService{
	@Autowired
	private MonthMissionDao monthMissionDao;
	public boolean add(MonthMission overtime){
		boolean flag=false;
		try{
			monthMissionDao.addObject(overtime);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
		
	}
	
/*	public boolean delete(String[] ids){
		return suggestionDao.remove(ids);
	}*/
	
	public boolean checkMonthDate(String _monthDate)
	{
		boolean flag = false;
		try {
			flag = monthMissionDao.checkMonthDate(_monthDate);
		} catch (Exception e1) {
			flag = false;
			e1.printStackTrace();
			SysLog.error(e1);
		}
		return flag;
	}
	
	public boolean checkEditLock(String _monthDate)
	{
		boolean flag = false;
		try {
			flag = monthMissionDao.checkEditLock(_monthDate);
		} catch (Exception e1) {
			flag = false;
			e1.printStackTrace();
			SysLog.error(e1);
		}
		return flag;
	}
	
	public boolean delete(String[] ids){
		return monthMissionDao.remove(ids);
	}
	
	public boolean delete(MonthMission overtime){
		boolean flag = false;
		try {
			monthMissionDao.removeObject(MonthMission.class, overtime.getId());
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	

	public boolean update(MonthMission overtime){
		boolean flag=false;
		try{
			monthMissionDao.updateObject(overtime);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	

	@Transactional(readOnly = true)
	public MonthMission getMonthMissionById(String id){
		return (MonthMission)monthMissionDao.getObject(MonthMission.class, id);
	}
	
	@Transactional(readOnly = true)
	public Page getPage(String groupname,String username,int pageNum, int pageSize, String _monthDate, String _examRank, int querylevel) {
		return monthMissionDao.getPage(groupname,username,pageNum,pageSize,_monthDate,_examRank, querylevel);
	}
	
	@Transactional(readOnly=true)
	public String getUserGroup_name(String username){
		return monthMissionDao.getUserGroup_name(username);
	}
	
	@Transactional(readOnly = true)
	public List<Object> getNames(String groupName) {
		return monthMissionDao.getUsernamesByGroup(groupName);
	}
	
	@Transactional(readOnly = true)
	public List<String> getAllNames(){
		return monthMissionDao.getAllNames();
	}
	
	@Transactional(readOnly = true)
	public List<String> getAllIds(String _monthDate){
		return monthMissionDao.getIdByMonthDate(_monthDate);
	}
	
}
