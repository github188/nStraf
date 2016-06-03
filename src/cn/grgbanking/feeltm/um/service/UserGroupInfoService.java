/**
 * 
 */
package cn.grgbanking.feeltm.um.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.context.BaseApplicationContext;
import cn.grgbanking.feeltm.domain.UsrGroup;
import cn.grgbanking.feeltm.um.dao.UserGroupInfoDao;
import cn.grgbanking.feeltm.um.dao.UserGroupRoleDao;
import cn.grgbanking.framework.service.BaseService;

@SuppressWarnings("unchecked")
@Service
@Transactional
public class UserGroupInfoService extends BaseService {
	@Autowired
	private UserGroupInfoDao dao;

	public List getAllUserGroupInfoList() {
		return dao.getAllUserGroupInfoList();
	}

	public boolean isExitxUserGroup(UsrGroup userGroup) {
		return dao.isExitxUserGroup(userGroup);
	}

	public void addUserGroupInfo(UsrGroup userGroup) {
		dao.addUserGroupInfo(userGroup);
	}

	public UsrGroup findOnebyUserGroupInfo(String groupcode) {
		return dao.findOnebyUserGroupInfo(groupcode);
	}

	public int removeUserGroupInfo(String[] groupcode) {
		UserGroupRoleDao grproledao = (UserGroupRoleDao) BaseApplicationContext
				.getAppContext().getBean("userGroupRoleDao");
		grproledao.removeGroupRole(groupcode);
		return dao.removeUserGroupInfo(groupcode);
	}

	public void updateUserGroupInfo(UsrGroup userGroup) {
		dao.updateUserGroupInfo(userGroup);

	}

}
