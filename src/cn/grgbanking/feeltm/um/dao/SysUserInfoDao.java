/**
 * 
 */
package cn.grgbanking.feeltm.um.dao;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.type.Type;
import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.domain.SysUser;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.util.SqlHelper;
import cn.grgbanking.framework.dao.BaseDao;
import cn.grgbanking.framework.dao.hibernate3.BaseHibernateTemplate;
import cn.grgbanking.framework.util.Page;



@Repository  
@SuppressWarnings("unchecked")  
public class SysUserInfoDao extends BaseDao<SysUser> {
	
	/*
	 * 根据用户id获取sysUser对象
	 */
	
	public SysUser findSysUserByUserId(String userid) {
		
	    try{
	    	SysUser su =null;
			String sql = " FROM  SysUser  su WHERE su.userid= '"+userid+"'";
			List list=this.getHibernateTemplate(). find(sql);
			if(list.size()>0)
				su=(SysUser)list.get(0);
			return su;
	    }catch(Exception e){
	    	SysLog.error(e);
	    	return null;
	    }					
	}
	
	/*
	 * 根据机构id获取机构层次
	 */
	public String getOrgfloorByOrgId(SysUser sysUser)
	{
		String orgfloor ="";
		try{
		String orgid = sysUser.getOrgid();
		List  lst = this.getHibernateTemplate().find("SELECT bt.orgfloor FROM OrgInfo as bt WHERE bt.orgid='"+orgid+"'");
		if(lst.size()>0){
			orgfloor = lst.get(0).toString();
		}
	    }catch(Exception e){
		 SysLog.error(e);
		 e.printStackTrace();
	  }
	    return orgfloor;
	}
	
	public Page findUserInfoPage(SysUser form, int pageNum, int pageSize) {
		String hql = "from SysUser as user where  user.userid like ? and user.username like ? and user.orgfloor like ? ";
		Page page = null;
		
	    Object[] obj = new Object[3];
	    Type[] types = new Type[3];
		if (form.getUserid() != null)
			obj[0] = "%" + form.getUserid().trim() + "%";
		else
			obj[0] = "%%";
		types[0] = Hibernate.STRING;

		if (form.getUsername() != null)
			obj[1] = "%" + form.getUsername().trim() + "%";
		else
			obj[1] = "%%";
		types[1] = Hibernate.STRING;
		
        if (form.getOrgfloor() != null)
            obj[2] = "%" + form.getOrgfloor().trim() + "%";
        else
            obj[2] = "%%";
        types[2] = Hibernate.STRING;

        
		page = ((BaseHibernateTemplate) getHibernateTemplate()).getPage(
				hql, obj, types, pageNum, pageSize);

		return page;
	}
	
	public String getGroupNameByUserId(String userId) {
		String grpname="" ;
		String hql=" select ug.grpname from UsrGroup ug where ug.grpcode in (select uu.grpcode  from UsrUsrgrp uu where uu.userid='"+userId+"') " ;
		List list = this.getHibernateTemplate().find(hql);
		if(list!=null && list.size()>0){
			for(int i=0 ;i<list.size() ;i++){
				grpname=grpname+","+(String)list.get(i);
			}
		}
		if(grpname.length()>0){
			return grpname.substring(1) ;
		}else{
			return "" ;
		}
	}
	
	public int removeSysUserInfo(String[] userids) {
		String hql = "delete SysUser as user where "
				+ SqlHelper.fitStrInCondition("user.userid", userids);
		int i = 0;
		
		i+=this.getHibernateTemplate().bulkUpdate(hql);
		return i;
	}

	
	/**
	 * by wjie5 2014-4-24
	 * 根据部门名称查询该部门下的人员
	 * @param deptName
	 * @param deptvalue 
	 * @param groupName
	 * @return
	 */
	public List<Object[]> getNamesByDept(String deptName, String deptvalue) {
		String hql = " select user.username,user.userid from SysUser user where 1=1 ";
		if(deptName!=null&&!deptName.equals(deptvalue)){
			hql +=" and user.deptName = '%"+deptName+"%'"; 
		}
		List<Object[]> names= this.getHibernateTemplate().find(hql);
		return names;
	} 
	
}
