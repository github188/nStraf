package cn.grgbanking.feeltm.EmployeeInformation.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.EmployeeInformation.dao.EmployeeInformationDao;
import cn.grgbanking.feeltm.domain.SysUser;
import cn.grgbanking.framework.service.BaseService;
import cn.grgbanking.framework.util.Page;

@Service("employeeInformationService")
@Transactional
public class EmployeeInformationService extends BaseService{
	@Autowired
	private EmployeeInformationDao employeeInformationDao;
	
	@Transactional(readOnly = true)
	public Page getPage(String groupname,String username,int pageNum, int pageSize,String status) {
		return employeeInformationDao.getPage(groupname, username, pageNum, pageSize, status);
	}
	public SysUser getCaseById(String id){
		return (SysUser)employeeInformationDao.getObject(SysUser.class, id);
	}
	
	@Transactional(readOnly = true)
	public List<Object> getNames(String groupName) {
		return employeeInformationDao.getUsernamesByGroup(groupName);
	}
	

	@Transactional(readOnly=true)
	public String getUserGroup_name(String username){
		return employeeInformationDao.getUserGroup_name(username);
	}
	
	@Transactional(readOnly = true)
	public List<String> getAllNames(){
		
		return employeeInformationDao.getAllNames();
	}
	public EmployeeInformationDao getEmployeeInformationDao() {
		return employeeInformationDao;
	}
	public void setEmployeeInformationDao(
			EmployeeInformationDao employeeInformationDao) {
		this.employeeInformationDao = employeeInformationDao;
	}
	
}