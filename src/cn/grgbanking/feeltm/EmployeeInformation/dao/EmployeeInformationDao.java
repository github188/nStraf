package cn.grgbanking.feeltm.EmployeeInformation.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.domain.SysUser;
import cn.grgbanking.framework.dao.BaseDao;
import cn.grgbanking.framework.dao.hibernate3.BaseHibernateTemplate;
import cn.grgbanking.framework.util.Page;

@Repository("employeeInformationDao")
public class EmployeeInformationDao extends BaseDao<SysUser>{
	public Page getPage(String groupname,String username,
			int pageNum, int pageSize,String status) {
		String hql ="";
		hql = "select new cn.grgbanking.feeltm.domain.SysUser(userid,row_number()over(order by userid) as 序列号,SYSDATE-hiredate as yearnum,username,hiredate,education,technicaltitle,position,groupName,mobile) from SysUser where 1=1 ";
		//hql = "select new cn.grgbanking.feeltm.performance.domain.Perfor(pno,id,rownum,month_date,user_id,group_name,subtotal_s,remark,update_man,modify_date) from (select t.pno,t.id,rownum,t.month_date,t.user_id,t.group_name,t.subtotal_s,t.remark,t.update_man,t.modify_date from (select pno,id,rownum,month_date,user_id,group_name,subtotal_s,remark,update_man,modify_date from Performance order by subtotal_s desc) t) where 1=1 ";
		if(groupname!=null && !groupname.equals("全选")){
			hql += " and group_name like '%"+groupname.trim()+"%'";
		}
		if (username != null){
			hql += " and user_id like '%"+username.trim()+"%'";
		}
		if (status != null && !status.equals("")){
			hql += " and status like '%"+ status.trim()+"%'";
		}
		hql += "order by hiredate desc";
		System.out.println(hql);
		List list = getHibernateTemplate().find(hql);
		Page page = ((BaseHibernateTemplate) getHibernateTemplate()).getPageByList(list,pageNum,pageSize);
		return page;
	}
	public List<String> getAllNames(){
		List<String> list=null;
		String hql="select username from SysUser where groupName is not null and lower(trim(username)) not in('汤飞','王全胜','开发员')  order by level";
		list=this.getHibernateTemplate().find(hql);
		return list;
	}
   public String getUserGroup_name(String username){
		
		String hql="select groupName from SysUser where username like '%"+username.trim()+"%'";
		List list=this.getHibernateTemplate().find(hql);
		String str=null;
		if(list!=null&&list.size()>=0){
			str=(String)list.get(0);		     
		}
		return str;
	}
   public List<Object> getUsernamesByGroup(String groupName){
		String query="select user.username from SysUser user where 1=1 and lower(trim(user.username)) not in('汤飞','王全胜','开发员','管理员')";
		if(!groupName.equals("全选")){
			query+="and user.groupName like '%"+groupName+"%'";
		}
		List<Object> names= this.getHibernateTemplate().find(query);
		names.add("全选");
//		names.add(0, "全选");
		return names;
	}
}