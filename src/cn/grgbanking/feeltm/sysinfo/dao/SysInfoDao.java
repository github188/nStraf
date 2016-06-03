package cn.grgbanking.feeltm.sysinfo.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.domain.testsys.SysInfo;
import cn.grgbanking.framework.dao.BaseDao;
import cn.grgbanking.framework.dao.hibernate3.BaseHibernateTemplate;
import cn.grgbanking.framework.util.Page;


@Repository("sysInfoDao")
public class SysInfoDao extends BaseDao<SysInfo> {
	public Page getPage(String username, String groupname,String status,
			int pageNum, int pageSize) {
		String hql ="";
		hql = " select new cn.grgbanking.feeltm.sysinfo.domain.Perfor(id,rownum,username,groupname,workstation,workingdate,startworddtae,education,mobile,birthplace,status,qq) from SysInfo  where 1=1 ";
		if (username != null && !username.equals("全选")) {
			hql += " and username like '%"+username.trim()+"%'";
		}
		if (groupname != null && !groupname.equals("全选")) {
			hql += " and groupname like '%"+groupname.trim()+"%'";
		}
		if (status != null && !status.equals("全选")) {
			hql += " and status like '%"+status.trim()+"%'";
		}
		hql += " order by workingdate asc";
		System.out.println(hql);	
		List list = getHibernateTemplate().find(hql);
		Page page = ((BaseHibernateTemplate) getHibernateTemplate()).getPage(hql,pageNum,pageSize);
		return page;
	}
	
	public List<Object> getUsernamesByGroup(String groupName){
		String query="from SysInfo user where 1=1 and lower(trim(user.username)) not in('汤飞','王全胜','杜高峰','开发员','管理员')";
		if(!groupName.equals("全选")){
			query+="and user.groupName like '%"+groupName+"%'";
		}
		List<Object> names= this.getHibernateTemplate().find(query);
		names.add("全选");
//		names.add(0, "全选");
		return names;
	}
	
	public String getUsernamesByname(String name){
		String query="select user.id from SysInfo user where 1=1 and lower(trim(user.username)) not in('汤飞','王全胜','杜高峰','开发员','管理员')";
		if(!name.equals("全选")){
			query+="and user.username like '%"+name+"%'";
		}
		List<String> ids= this.getHibernateTemplate().find(query);
		String id = ids.get(0).toString(); 
		return id;
	}
	
	public boolean getExistRecord(String username){
		String hql ="";
		hql = "from SysInfo  where 1=1 ";
		if (username != null && !username.equals("")) {
			hql += " and username like '%"+username.trim()+"%'";
		}
		List<Object> names= this.getHibernateTemplate().find(hql);
		if(names.size()>0)
		{
			return true;
		}
		return false;
	}
	
}
