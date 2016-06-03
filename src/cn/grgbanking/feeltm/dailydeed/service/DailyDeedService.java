package cn.grgbanking.feeltm.dailydeed.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.dailydeed.dao.DailyDeedDao;
import cn.grgbanking.feeltm.domain.testsys.DailyDeed;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.framework.service.BaseService;
import cn.grgbanking.framework.util.Page;

@Service("dailyDeedService")
@Transactional
public class DailyDeedService extends BaseService{
	@Autowired
	private DailyDeedDao dailyDeedDao;
	public boolean add(DailyDeed dailyDeed){
		boolean flag=false;
		try{
			dailyDeedDao.addObject(dailyDeed);
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
	@Transactional(readOnly = true)
	public String getUidByLevel(String groupName){
		return dailyDeedDao.getUidByLevel(groupName);
	}
	
	public boolean delete(DailyDeed dailyDeed){
		boolean flag = false;
		try {
			dailyDeedDao.removeObject(DailyDeed.class, dailyDeed.getId());
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	

	public boolean update(DailyDeed dailyDeed){
		boolean flag=false;
		try{
			dailyDeedDao.updateObject(dailyDeed);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	public DailyDeed getCaseById(String id){
		return (DailyDeed)dailyDeedDao.getObject(DailyDeed.class, id);
	}
	
	public boolean upEdit_lock(String month_date){
		return dailyDeedDao.upEdit_lock(month_date);
	}
	//String createDate, String createMan,String updateman,String category,String ano,int pageNum, int pageSize,String raiseEndDate
	@Transactional(readOnly = true)
	public Page getPage(String groupname,String username,int pageNum, int pageSize,String month) {
		return dailyDeedDao.getPage(groupname,username,pageNum,pageSize,month);
	}
	
	
	@Transactional(readOnly = true)
	public List<Object> getNames(String groupName) {
		return dailyDeedDao.getUsernamesByGroup(groupName);
	}
	
	
	@Transactional(readOnly=true)
	public String getNextNo(){
		return dailyDeedDao.getNextNo();
	}
	
	@Transactional(readOnly=true)
	public String getUserGroup_name(String username){
		return dailyDeedDao.getUserGroup_name(username);
	}
	
	
	@Transactional(readOnly=true)
	public String getUseredit_lock(String month_date){
		return dailyDeedDao.getUseredit_lock(month_date);
	}
	
	@Transactional(readOnly=true)
	public boolean getUserMonth_date(String month_date){
		return dailyDeedDao.getUserMonth_date(month_date);
	}
	
	@Transactional(readOnly=true)
	public String getUserlevel(String username){
		return dailyDeedDao.getUserlevel(username);
	}
	
	@Transactional(readOnly = true)
	public List<String> getAllNames(){
		
		return dailyDeedDao.getAllNames();
	}
	
	@Transactional(readOnly = true)
	public List<DailyDeed> getDailyDeed(String pno) {
		return dailyDeedDao.selectDailyDeed(pno);
	}
	
}
