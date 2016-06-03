package cn.grgbanking.feeltm.personaldevelop.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.domain.testsys.AbilityAnalyse;
import cn.grgbanking.framework.dao.BaseDao;
import cn.grgbanking.framework.dao.hibernate3.BaseHibernateTemplate;
import cn.grgbanking.framework.util.Page;

@Repository("abilityanalyseDao")
public class AbilityAnalyseDao extends BaseDao<AbilityAnalyse>{
	/**
	 * 
	 * @param type  机器型号
	 * @param seriaNum  设备编号
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public Page getPage(String name,String year,String status,String querygroup,String queryname,int querylevel,int pageNum,int pageSize)
	{
		String hql = "FROM AbilityAnalyse WHERE 1=1 ";
			if(name!=null && !name.equals("")){
				hql += " and name like '%"+name.trim()+"%' ";
			}
			if(year!=null && !year.equals("")){
				hql += " and createyear like '%"+year.trim()+"%' ";
			}
			if(status!=null && !status.equals("")){
				hql += " and status like '%"+status.trim()+"%' ";
			}
			if(querylevel == 1)
			{
				hql += " and groupname like '%"+querygroup.trim()+"%' ";
			}
			if(querylevel == 2)
			{
				hql += " and name like '%"+queryname.trim()+"%' ";
			}
		hql += " order by name desc,createyear desc";
		System.out.println(hql);
		Page page=((BaseHibernateTemplate) getHibernateTemplate()).getPage(hql,pageNum,pageSize);
		return page;
	}
	
	public boolean remove(String[] ids){  //通过pl/sql关联进行删除其他内容
		boolean flag=false;
		List<AbilityAnalyse> atms=new ArrayList<AbilityAnalyse>();
		for (int j = 0; j < ids.length; j++) {	
				atms.add(new AbilityAnalyse(ids[j]));
			}
		try{
			this.getHibernateTemplate().deleteAll(atms);
			flag=true;
		}catch(Exception e){
			System.out.println("ServerDao delete error!!!!!!!!!!");
		}
		return flag;
	}

	public String getHeadman(String groupname){
		
		String hql="select userid from SysUser where groupName like '%"+groupname.trim()+"%' and  level = 1";
		System.out.println(hql);
		List list=this.getHibernateTemplate().find(hql);
		String str=null;
		if(list!=null&&list.size()>=0){
			str=list.get(0).toString();		     
		}
		return str;
	}
	
	public String getUserid(String name){
		
		String hql="select userid from SysUser where username like '%"+name.trim()+"%'";
		System.out.println(hql);
		List list=this.getHibernateTemplate().find(hql);
		String str=null;
		if(list!=null&&list.size()>=0){
			str=list.get(0).toString();		     
		}
		return str;
	}
	
	public boolean check(String deviceNo) {   //true为可用，false为不可用
		boolean flag=true;
		String hql = "FROM AbilityAnalyse atm WHERE atm.name='"+deviceNo+"'";		
		List list=this.getHibernateTemplate().find(hql);
		if(list!=null&&list.size()>0)
			flag=false;
		return flag;
	}
}
