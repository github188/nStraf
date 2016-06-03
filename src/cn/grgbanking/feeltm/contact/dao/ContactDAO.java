package cn.grgbanking.feeltm.contact.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.domain.SysUser;
import cn.grgbanking.feeltm.domain.UsrContacts;
import cn.grgbanking.feeltm.domain.UsrGroup;
import cn.grgbanking.framework.dao.BaseDao;
import cn.grgbanking.framework.dao.hibernate3.BaseHibernateTemplate;
import cn.grgbanking.framework.util.Page;

@Repository("contactDao")
public class ContactDAO extends BaseDao<UsrContacts>{
	public Page getPage(int pageNum, int pageSize) {
		String hql = " from UsrContacts contact where contact.userId in(from SysUser su where su.status!='leave' and su.status!='离职') order by contact.userId";
		return ((BaseHibernateTemplate) getHibernateTemplate()).getPage(hql,pageNum, pageSize);
	}
	
	public Page getPageByGroup(String groupName, String userid,int pageNum, int pageSize){
		String hql = " from UsrContacts contact where contact.userId in(from SysUser su where su.status!='leave' and su.status!='离职') and contact.userId='"+userid+"' and contact.conGroup like '%$"+groupName+"$%' order by contact.userId";
		return ((BaseHibernateTemplate) getHibernateTemplate()).getPage(hql, pageNum, pageSize);
	}

	public List getContactInfoByUser(String userId) {
		String hql = " select user.username ,user.tel,user.mobile,user.email,user.deptName from SysUser user where user.userid = '"+userId+"' ";
		return ((BaseHibernateTemplate) getHibernateTemplate()).find(hql);
	}

	public Page getContactPageBycondition(UsrContacts usrContact, String orderField, String regulation,
			int pageNum, int pageSize) {
		String userId = usrContact.getUserId();
		String deptName = usrContact.getDeptName();
		String groupName = usrContact.getGroupName();
		StringBuilder hql = new StringBuilder();
		hql.append("select contact from UsrContacts contact , SysUser as us, SysDatadir as sd where us.status!='leave' and us.status!='离职' ");
		if(userId!=null&&!userId.equals("全选")){
			hql.append(" and (contact.userId like '%"+userId+"%' or contact.conName like '%"+userId+"%')"); 
		}
		hql.append(" and contact.userId in (select user.userid from SysUser user where 1=1 ");
		if(deptName!=null&&!deptName.equals("全选")){
			hql.append(" and user.deptName like '%"+deptName+"%'"); 
		}
		if(groupName!=null&&!groupName.equals("全选")){
			hql.append(" and  user.userid in (select usrUsrgrp.userid from  UserProject usrUsrgrp ");
			hql.append(" where usrUsrgrp.project.id='"+ groupName + "')");
		}
		hql.append(")");
		hql.append(" and  contact.userId=us.userid  and us.deptName=sd.key ");
		hql.append(" and sd.parentid = '8a81d9704587f215014587ffec14006a'");
		
		if(StringUtils.isNotBlank(orderField)){
			//如果排序字段为deptName，则根据数据字典的ORDER进行排序
			if("deptName".equals(orderField)){
				hql.append(" order by sd.order");
			}else{
				hql.append(" order by contact." + orderField); 				
			}
			
			if(StringUtils.isNotBlank(regulation)){
				hql.append(" " + regulation); 
			}else {
				hql.append(" asc ");
			}
		}else{
			//默认根据数据字典的ORDER进行排序
			hql.append(" order by sd.order asc ");
		}
		
		Page page = ((BaseHibernateTemplate) getHibernateTemplate()).getPage(hql.toString(),pageNum,pageSize);
		return page;
	}

	public List<Object> getNamesByDeptGroup(String deptName, String groupName) {
		String hql = " select user.username from SysUser user where 1=1 ";
		if(groupName!=null&&!groupName.equals("全选")){
			hql +=" and user.groupName like '%"+groupName+"%'"; 
		}
		if(deptName!=null&&!deptName.equals("全选")){
			hql +=" and user.deptName like '%"+deptName+"%'"; 
		}
		List<Object> names= this.getHibernateTemplate().find(hql);
		return names;
	}

	public UsrContacts findByUserName(String userId) {
		String hql = " from UsrContacts contact where contact.userId='"+userId+"'";
		List<UsrContacts> list = this.getHibernateTemplate().find(hql);
		if(!list.isEmpty()){
			return list.get(0);
		}
		return null;
	}

	public List<SysUser> getNoContacts(String deptName, String groupName, String deptValue) {
		String hql = " from SysUser user where 1=1 ";
		if(deptName!=null&&!deptName.equals(deptValue)){
			hql +=" and user.deptName like '%"+deptName+"%'"; 
		}
		hql += " and user.userid not in(select contact.userId from UsrContacts contact ) ";
		return this.getHibernateTemplate().find(hql);
	}

	public List<UsrGroup> getAllUserGroup() {
		String hql = " from UsrGroup ";
		return this.getHibernateTemplate().find(hql);
	}

	
}
