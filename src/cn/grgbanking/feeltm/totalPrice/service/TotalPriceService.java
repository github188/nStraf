package cn.grgbanking.feeltm.totalPrice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.domain.testsys.TotalPrice;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.totalPrice.dao.TotalPriceDao;
import cn.grgbanking.framework.service.BaseService;
import cn.grgbanking.framework.util.Page;

@Service("totalPriceService")
@Transactional
public class TotalPriceService extends BaseService{
	@Autowired
	private TotalPriceDao totalPriceDao;
	public boolean add(TotalPrice overtime){
		boolean flag=false;
		try{
			totalPriceDao.addObject(overtime);
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
			flag = totalPriceDao.checkMonthDate(_monthDate);
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
			flag = totalPriceDao.checkEditLock(_monthDate);
		} catch (Exception e1) {
			flag = false;
			e1.printStackTrace();
			SysLog.error(e1);
		}
		return flag;
	}
	
	public boolean delete(String[] ids){
		return totalPriceDao.remove(ids);
	}
	
	public boolean delete(TotalPrice overtime){
		boolean flag = false;
		try {
			totalPriceDao.removeObject(TotalPrice.class, overtime.getId());
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	

	public boolean update(TotalPrice overtime){
		boolean flag=false;
		try{
			totalPriceDao.updateObject(overtime);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	

	@Transactional(readOnly = true)
	public TotalPrice getTotalPriceById(String id){
		return (TotalPrice)totalPriceDao.getObject(TotalPrice.class, id);
	}
	
	@Transactional(readOnly = true)
	public Page getPage(String groupname,String username,int pageNum, int pageSize, String _monthDate, int querylevel) {
		return totalPriceDao.getPage(groupname,username,pageNum,pageSize,_monthDate, querylevel);
	}
	
	@Transactional(readOnly=true)
	public String getUserGroup_name(String username){
		return totalPriceDao.getUserGroup_name(username);
	}
	
	@Transactional(readOnly = true)
	public List<Object> getNames(String groupName) {
		return totalPriceDao.getUsernamesByGroup(groupName);
	}
	
	@Transactional(readOnly = true)
	public List<String> getAllNames(){
		return totalPriceDao.getAllNames();
	}
	
	@Transactional(readOnly = true)
	public List<String> getAllIds(String _monthDate){
		return totalPriceDao.getIdByTotalPrice(_monthDate);
	}
	
}
