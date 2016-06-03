/**
 * 
 */
package cn.grgbanking.feeltm.um.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.domain.UsrRole;
import cn.grgbanking.feeltm.util.SqlHelper;
import cn.grgbanking.framework.dao.BaseDao;


@Repository  
@SuppressWarnings("unchecked")
public class UserRoleInfoDao extends BaseDao<UsrRole>{
	
	public List getAllRoleInfoList() {
		String hql="from UsrRole ";
		List list=this.getHibernateTemplate().find(hql);
		
		return list;
	}
	
	//�жϽ�ɫ�Ƿ����
	public boolean isExitxRole(UsrRole userRole) {
		boolean result=false;
		String hql="from UsrRole as role where role.rolecode='"+userRole.getRolecode().trim()+"' or role.rolename='"+userRole.getRolename().trim()+"'";
		List list=this.getHibernateTemplate().find(hql);
		
		if (list.size()>0)
			result=true;
		return result;
	}
	
	public void addUserRoleInfo(UsrRole userRole) {
		this.getHibernateTemplate().save(userRole);
	}

	public UsrRole findOnebyRoleInfo(String rolecode) {
		
		return (UsrRole)this.getHibernateTemplate().get(UsrRole.class,rolecode);
	}
	

	public void updateUserRoleInfo(UsrRole userRole) {
		this.getHibernateTemplate().update(userRole);
		
	}
	
	public int removeUserRoleInfo(String[] rolecode) {
		String hql = "delete UsrRole as userRole where "
				+ SqlHelper.fitStrInCondition("userRole.rolecode", rolecode);
		int i = 0;
		i+=this.getHibernateTemplate().bulkUpdate(hql);
		
		return i;
	}	

}
