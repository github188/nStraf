package cn.grgbanking.feeltm.overtime.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.domain.testsys.Overtime;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.overtime.dao.OvertimeDao;
import cn.grgbanking.framework.service.BaseService;
import cn.grgbanking.framework.util.Page;

@Service("overtimeService")
@Transactional
public class OvertimeService extends BaseService{
	@Autowired
	private OvertimeDao overtimeDao;
	public boolean add(Overtime overtime){
		boolean flag=false;
		try{
			overtimeDao.addObject(overtime);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
		
	}
	
	public boolean checkOt(Overtime overtime)
	{
		boolean flag = true;
		try {
			flag = overtimeDao.checkExist(overtime.getUserid(), overtime.getStartdate(), overtime.getEnddate());
		} catch (Exception e1) {
			flag = false;
			e1.printStackTrace();
			SysLog.error(e1);
		}
		return flag;
	}
	
	public boolean checkUpOt(Overtime overtime)
	{
		boolean flag = true;
		try {
			flag = overtimeDao.checkUpExist(overtime.getUserid(), overtime.getStartdate(), overtime.getEnddate(),overtime.getId());
		} catch (Exception e1) {
			flag = false;
			e1.printStackTrace();
			SysLog.error(e1);
		}
		return flag;
	}
	
/*	public boolean delete(String[] ids){
		return suggestionDao.remove(ids);
	}*/
	
	public boolean delete(String[] ids){
		return overtimeDao.remove(ids);
	}
	
	public boolean delete(Overtime overtime){
		boolean flag = false;
		try {
			overtimeDao.removeObject(Overtime.class, overtime.getId());
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	

	public boolean update(Overtime overtime){
		boolean flag=false;
		try{
			overtimeDao.updateObject(overtime);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	public boolean addOvertimeListInfo(List<Overtime> overtimeSaveList) {
		boolean flag = false;
		try {
			for(Overtime overtime:overtimeSaveList)
				add(overtime);
			flag = true;
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			SysLog.error(e);
			throw new NullPointerException();
		}
		return flag;
	}

	@Transactional(readOnly = true)
	public Overtime getOvertimeById(String id){
		return (Overtime)overtimeDao.getObject(Overtime.class, id);
	}
	
	@Transactional(readOnly = true)
	public Page getPage(String groupname,String username,String deptname, int pageNum, int pageSize,String ot, String prjname, String otDayEnd, UserModel userModel) {
		return overtimeDao.getPage(groupname,username,deptname,pageNum,pageSize,ot,prjname,otDayEnd,userModel);
	}
	
	@Transactional(readOnly = true)
	public Page getOveritmePage(String userid, int pageNum, int pageSize) {
		return overtimeDao.getOvertimePage(userid, pageNum, pageSize);
	}
	
	@Transactional(readOnly=true)
	public String getUserGroup_name(String username){
		return overtimeDao.getUserGroup_name(username);
	}
	
	@Transactional(readOnly = true)
	public List<Object> getNames(String groupName) {
		return overtimeDao.getUsernamesByGroup(groupName);
	}
	
}
