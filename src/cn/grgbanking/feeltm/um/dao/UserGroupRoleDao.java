/**
 * 
 */
package cn.grgbanking.feeltm.um.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.domain.UsrGrprole;

import cn.grgbanking.feeltm.util.SqlHelper;
import cn.grgbanking.framework.dao.BaseDao;

@Repository  
@SuppressWarnings("unchecked") 
public class UserGroupRoleDao extends BaseDao<UsrGrprole>{
	

	public List findGroupRoleList(String groupcode) {
		String hql = "from UsrGrprole as gr where gr.grpcode='" + groupcode
				+ "'";
		List list = this.getHibernateTemplate().find(hql);

		return list;
	}

	public void saveGroupRole(UsrGrprole usrGrprole) {
		this.getHibernateTemplate().save(usrGrprole);
		
	}

	// ��ɾ�����ɫ��ϵ
	public int removeGroupRole(String[] grpcodes) {
		String hql = "delete UsrGrprole as grpRole where "
				+ SqlHelper.fitStrInCondition("grpRole.grpcode", grpcodes);
		int i = 0;
		i+=this.getHibernateTemplate().bulkUpdate(hql);
		
		return i;
	}

	public int removeGroupRole(UsrGrprole usrGrprole) {
		String hql = "delete  UsrGrprole as grpRole where grpRole.grpcode='"
				+ usrGrprole.getGrpcode().trim() + "' and grpRole.rolecode='"
				+ usrGrprole.getRolecode().trim() + "'";
		int i = 0;
		i+=this.getHibernateTemplate().bulkUpdate(hql);
		
		return i;
	}
	public int removeGroupRole(String grpcode) {
		String hql = "delete  UsrGrprole as grpRole where grpRole.grpcode='"
				+grpcode + "'";
		int i = 0;
		i+=this.getHibernateTemplate().bulkUpdate(hql);

		return i;
	}
	
}
