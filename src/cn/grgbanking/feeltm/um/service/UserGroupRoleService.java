/**
 * 
 */
package cn.grgbanking.feeltm.um.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.context.BaseApplicationContext;
import cn.grgbanking.feeltm.domain.UsrGrprole;
import cn.grgbanking.feeltm.domain.UsrRole;
import cn.grgbanking.feeltm.um.dao.UserGroupRoleDao;
import cn.grgbanking.feeltm.um.dao.UserRoleInfoDao;
import cn.grgbanking.feeltm.um.webapp.vo.UsrGrproleVO;

import cn.grgbanking.framework.service.BaseService;

@SuppressWarnings("unchecked")
@Service
@Transactional
public class UserGroupRoleService extends BaseService {
	@Autowired
	private UserGroupRoleDao dao;
	

	public List findGroupRoleList(String groupcode) {
		return dao.findGroupRoleList(groupcode);
	}
	public List getAllRoleList() {
		UserRoleInfoDao	roleList=(UserRoleInfoDao)BaseApplicationContext.getAppContext().getBean("userRoleInfoDao");
		return roleList.getAllRoleInfoList();
	}

	public List findGrpRole(String grpcode) {
		List roleLst = this.getAllRoleList();
		List grpRoleLst = new ArrayList();
		grpRoleLst = this.findGroupRoleList(grpcode);
		
		List grpRoleFormLst = new ArrayList();
		for (int i = 0; i < roleLst.size(); i++) {
			UsrRole role = (UsrRole) roleLst.get(i);
			UsrGrproleVO usrGrproleVO = new UsrGrproleVO();
			try {
				BeanUtils.copyProperties(usrGrproleVO, role);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
	
			
			if (grpRoleLst.size() != 0 && grpRoleLst != null) {
				for (int j = 0; j < grpRoleLst.size(); j++) {
					UsrGrprole grprole = (UsrGrprole) grpRoleLst.get(j);
					if (grprole.getRolecode().equals(usrGrproleVO.getRolecode())) {
						usrGrproleVO.setChecked("checked");
					}
				}
			}
			usrGrproleVO.setGrpcode(grpcode);
			
			grpRoleFormLst.add(usrGrproleVO);
		}

		return grpRoleFormLst;
		
	}

	public void saveGroupRole(UsrGrprole usrGrprole) {
		dao.saveGroupRole(usrGrprole);
		
	}

	public int removeGroupRole(String[] grpcodes) {
		return dao.removeGroupRole(grpcodes);
	}

	public int removeGroupRole(UsrGrprole usrGrprole) {
		return dao.removeGroupRole(usrGrprole);
	}

	public int removeGroupRole(String grpcode) {
		return dao.removeGroupRole(grpcode);
	}

}
