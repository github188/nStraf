/**
 * 
 */
package cn.grgbanking.feeltm.um.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.domain.UsrGroup;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.util.SqlHelper;
import cn.grgbanking.framework.dao.BaseDao;

@Repository  
@SuppressWarnings("unchecked") 
public class UserGroupInfoDao extends BaseDao<UsrGroup>{

	public List getAllUserGroupInfoList() {
		String hql="from UsrGroup ";
		List list=this.getHibernateTemplate().find(hql);

		return list;
	}
	public List getAllUserProjectInfoList() {
		String hql="from Project ";
		List list=this.getHibernateTemplate().find(hql);
		
		return list;
	}
	
	//�ж������������Ƿ����
	public boolean isExitxUserGroup(UsrGroup userGroup) {
		boolean result=false;
		String hql="from UsrGroup as grp where grp.grpcode='"+userGroup.getGrpcode().trim()+"' or grp.grpname='"+userGroup.getGrpname().trim()+"'";
		List list=this.getHibernateTemplate().find(hql);

		if (list.size()>0)
			result=true;
		return result;
	}
	
	public void addUserGroupInfo(UsrGroup userGroup) {
		this.getHibernateTemplate().save(userGroup);	
	}
	
	public UsrGroup findOnebyUserGroupInfo(String groupcode) {
		return (UsrGroup)this.getHibernateTemplate().get(UsrGroup.class,groupcode);	
	}

	public int removeUserGroupInfo(String[] groupcode) {
		String hql = "delete UsrGroup as userGroup where "
			+ SqlHelper.fitStrInCondition("userGroup.grpcode", groupcode);
		int i = 0;
		i+=this.getHibernateTemplate().bulkUpdate(hql);

		return i;	
	}
	public void updateUserGroupInfo(UsrGroup userGroup) {
		this.getHibernateTemplate().update(userGroup);
	}
	public List getUserGroupInfoListByGrpLevel(String userid,String grpLevel){
		try{
			String hql=" from UsrGroup u where u.grpLevel='"+grpLevel+"' ";
			if(userid==null)
				userid="";
			if(!(userid.equals("administrator")||!userid.equals("develpoer"))){
				hql+="  and u.grpcode!='development' and u.grpcode!='administrator'";
			}
			List list=this.getHibernateTemplate().find(hql);
			return list;
		}catch(Exception e){
			SysLog.error(e);
			return null;
		}
	}
	
}
