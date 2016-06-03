/**
 * 
 */
package cn.grgbanking.feeltm.um.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.context.BaseApplicationContext;
import cn.grgbanking.feeltm.domain.UsrRole;
import cn.grgbanking.feeltm.um.dao.UserRoleInfoDao;
import cn.grgbanking.feeltm.um.dao.UserRolePurviewDao;
import cn.grgbanking.framework.service.BaseService;

@SuppressWarnings("unchecked")
@Service
@Transactional
public class UserRoleInfoService extends BaseService {
	@Autowired
	private UserRoleInfoDao dao;


	public List getAllRoleInfoList() {
		return dao.getAllRoleInfoList();
	}
	
	public boolean isExitxRole(UsrRole userRole) {
		return dao.isExitxRole(userRole);
	}
	
	public void addUserRoleInfo(UsrRole userRole) {
		dao.addUserRoleInfo(userRole);
		
	}
	
	public UsrRole findOnebyRoleInfo(String rolecode) {
		return dao.findOnebyRoleInfo(rolecode);
	}

	public void updateUserRoleInfo(UsrRole userRole) {
		dao.updateUserRoleInfo(userRole);

	}
	
	public int removeUserRoleInfo(String[] rolecode) {
		UserRolePurviewDao	roleRurviewDAO=(UserRolePurviewDao)BaseApplicationContext.getAppContext().getBean("userRolePurviewDao");
		roleRurviewDAO.removeRoleFunc(rolecode);
		return dao.removeUserRoleInfo(rolecode);
	}

}
