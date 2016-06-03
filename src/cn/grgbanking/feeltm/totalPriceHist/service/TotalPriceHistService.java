package cn.grgbanking.feeltm.totalPriceHist.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.domain.testsys.TotalPriceHist;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.totalPriceHist.dao.TotalPriceHistDao;
import cn.grgbanking.framework.service.BaseService;
import cn.grgbanking.framework.util.Page;

@Service("totalPriceHistService")
@Transactional
public class TotalPriceHistService extends BaseService{
	@Autowired
	private TotalPriceHistDao totalPriceHistDao;
	public boolean add(TotalPriceHist overtime){
		boolean flag=false;
		try{
			totalPriceHistDao.addObject(overtime);
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
			flag = totalPriceHistDao.checkMonthDate(_monthDate);
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
			flag = totalPriceHistDao.checkEditLock(_monthDate);
		} catch (Exception e1) {
			flag = false;
			e1.printStackTrace();
			SysLog.error(e1);
		}
		return flag;
	}
	
	public boolean delete(String[] ids){
		return totalPriceHistDao.remove(ids);
	}
	
	public boolean delete(TotalPriceHist overtime){
		boolean flag = false;
		try {
			totalPriceHistDao.removeObject(TotalPriceHist.class, overtime.getId());
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	

	public boolean update(TotalPriceHist overtime){
		boolean flag=false;
		try{
			totalPriceHistDao.updateObject(overtime);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	

	@Transactional(readOnly = true)
	public TotalPriceHist getTotalPriceById(String id){
		return (TotalPriceHist)totalPriceHistDao.getObject(TotalPriceHist.class, id);
	}
	
	@Transactional(readOnly = true)
	public Page getPage(String groupname,String username,int pageNum, int pageSize, String _monthDate, int querylevel) {
		return totalPriceHistDao.getPage(groupname,username,pageNum,pageSize,_monthDate, querylevel);
	}
	
	@Transactional(readOnly=true)
	public String getUserGroup_name(String username){
		return totalPriceHistDao.getUserGroup_name(username);
	}
	
	@Transactional(readOnly = true)
	public List<Object> getNames(String groupName) {
		return totalPriceHistDao.getUsernamesByGroup(groupName);
	}
	
	@Transactional(readOnly = true)
	public List<String> getAllNames(){
		return totalPriceHistDao.getAllNames();
	}
	
	@Transactional(readOnly = true)
	public List<String> getAllIds(String _monthDate){
		return totalPriceHistDao.getIdByTotalPrice(_monthDate);
	}
	
}
