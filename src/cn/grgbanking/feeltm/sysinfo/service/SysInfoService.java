package cn.grgbanking.feeltm.sysinfo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.domain.testsys.SysInfo;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.sysinfo.dao.SysInfoDao;
import cn.grgbanking.framework.util.Page;

@Service("sysInfoService")
@Transactional
public class SysInfoService {
	@Autowired
	private SysInfoDao sysInfoDao;
	public boolean add(SysInfo Case1){
		boolean flag=false;
		try{
			sysInfoDao.addObject(Case1);
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
	public List<Object> getNames(String groupname) {
		return sysInfoDao.getUsernamesByGroup(groupname);
	}
	
	@Transactional(readOnly = true)
	public String getUsernamesByname(String name) {
		return sysInfoDao.getUsernamesByname(name);
	}
	
	@Transactional(readOnly = true)
	public boolean getExistRecord(String username) {
		return sysInfoDao.getExistRecord(username);
	}
	
	
	public boolean delete(SysInfo Case1){
		boolean flag = false;
		try {
			sysInfoDao.removeObject(SysInfo.class, Case1.getId());
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	

	public boolean update(SysInfo Case1){
		boolean flag=false;
		try{
			sysInfoDao.updateObject(Case1);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	public SysInfo getCaseById(String id){
		return (SysInfo)sysInfoDao.getObject(SysInfo.class, id);
	}
	//String createDate, String createMan,String updateman,String category,String ano,int pageNum, int pageSize,String raiseEndDate
	@Transactional(readOnly = true)
	public Page getPage(String username, String groupname,String status,int pageNum, int pageSize) {
		return sysInfoDao.getPage(username,groupname,status,pageNum,pageSize);
	}
	
}
