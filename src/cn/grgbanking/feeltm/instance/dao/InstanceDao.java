package cn.grgbanking.feeltm.instance.dao;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.domain.SysUser;
import cn.grgbanking.feeltm.domain.testsys.Instance;
import cn.grgbanking.framework.dao.BaseDao;
import cn.grgbanking.framework.dao.hibernate3.BaseHibernateTemplate;
import cn.grgbanking.framework.util.Page;


@Repository("InstanceDao")
public class InstanceDao extends BaseDao<Instance> {
	public Page getPage(String createDate, String createMan,String summary,String category,String username,
			int pageNum, int pageSize,String raiseEndDate,boolean hasComfirmRight) {
		String hql ="";
		hql = " from Instance  where 1=1 ";
		if (createDate != null && !createDate.equals("")) {
			hql += " and create_date >= to_date('" + createDate.trim()
					+ "','yyyy-MM-dd') ";
		}
		if (createMan != null && !createMan.equals("")) {
			hql += " and embracer_man like '%" + createMan.trim() + "%' ";
		}
		if (summary != null && !summary.equals("")) {
			hql += " and summary like '%" + summary.trim() + "%' ";
		}
		if (category != null && !category.equals("")) {
			hql += " and category like '%"+category.trim()+"%'";
		}
		if (raiseEndDate != null && !raiseEndDate.equals("")) {
			hql += " and create_date <= to_date('" + raiseEndDate.trim()
					+ "','yyyy-MM-dd') ";
		}
		if (hasComfirmRight) {
			hql += " order by ino desc";
		}
		else{
			if(!username.equals("汤飞") && !username.equals("王全胜") && !username.equals("杜高峰"))
			{
				hql += " and ((status like '公开') or (status like '不公开' and  (embracer_man like '%"+username.trim()+"%' or up_man like '%"+username.trim()+"%')))";
			}
			hql += " order by ino desc";
		}
		System.out.println(hql);	
		List list = getHibernateTemplate().find(hql);
		Page page = ((BaseHibernateTemplate) getHibernateTemplate()).getPage(hql,pageNum,pageSize);
		return page;
	}
	
	public  String  getNextNo(){
		String str=null;
		List list=this.getHibernateTemplate().find("select max(p.ino) from Instance p");
		if(list!=null&&list.size()>=0){
		      String p=(String)list.get(0);
		      if(p!=null){
		    	  if(p.contains("Y")){
			    	  p=p.substring(1);
			    	  long d=Long.parseLong(p)+1;
			    	  DecimalFormat format=new DecimalFormat("Y0000");
			  		  str=format.format(d);
			      }
		      }
		      else
		      {
					str = "Y0001";
		      }
		     
		}
		return str;		
	}
	
	
	public List<String> getAllNames(){
		List<String> list=null;
		String hql="select username from SysUser where groupName is not null and level != 3  and lower(trim(username)) not in('汤飞','开发员') order by groupName desc,level asc";
		list=this.getHibernateTemplate().find(hql);
		return list;
	}
	
	public Map<String,String> getAllNameAndEmail(){
		Map<String,String> map=new HashMap<String,String>();
		String hql="from SysUser where groupName is not null";
		List<SysUser> list=this.getHibernateTemplate().find(hql);
		for(SysUser user:list){
			map.put(user.getUsername(), user.getEmail());
		}
		return map;
	}
	
	/**
	 * 根据String[] userIds中人员 查询不在其中的所有人员
	 * 
	 * @param hql
	 * @return lhyan3 2014年6月26日
	 */
	@SuppressWarnings("unchecked")
	public List<SysUser> getNotInInstanceByProject(String hql) {
		return this.getHibernateTemplate().find(hql);
	}
}
