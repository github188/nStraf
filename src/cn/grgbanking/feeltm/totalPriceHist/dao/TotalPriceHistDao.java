package cn.grgbanking.feeltm.totalPriceHist.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.domain.testsys.TotalPriceHist;
import cn.grgbanking.framework.dao.BaseDao;
import cn.grgbanking.framework.dao.hibernate3.BaseHibernateTemplate;
import cn.grgbanking.framework.util.Page;


@Repository("totalPriceHistDao")
public class TotalPriceHistDao extends BaseDao<TotalPriceHist> {
	public Page getPage(String groupname,String username,
			int pageNum, int pageSize, String _monthDate, int querylevel) {
		String hql ="";
		hql = "from TotalPriceHist where 1=1 ";
		if (username != null && !username.equals("全选")){
			hql += " and currentName like '%"+username.trim()+"%'";
		}
		if(_monthDate!=null){
			hql += " and monthDate like '%"+_monthDate.trim()+"%'";
		}
		if(querylevel == 1)
		{
			hql += " and groupName like '%"+groupname.trim()+"%' ";
		}
		/*else if(querylevel == 2)
		{
			hql += " and currentName like '%"+username.trim()+"%' ";
		}*/
		hql += " order by monthDate desc, totalScore desc, groupName desc";
		System.out.println(hql);
		List list = getHibernateTemplate().find(hql);
		Page page = ((BaseHibernateTemplate) getHibernateTemplate()).getPageByList(list,pageNum,pageSize);
		return page;
	}
	
	public boolean checkMonthDate(String _monthDate) throws Exception{
		boolean flag = false;
		String hql = "from TotalPriceHist where 1=1 ";
		hql += " and monthDate like '%" + _monthDate.trim()+"%'";
		List list = getHibernateTemplate().find(hql);
		if(list.size()!=0)
		{
			flag = true;
		}
		return flag;
	}
	
	public boolean checkEditLock(String _monthDate)  throws Exception{
		boolean flag = false;
		String hql = "from TotalPriceHist where 1=1 ";
		hql += " and monthDate like '%" + _monthDate.trim()+"%'";
		hql += " and editLock='1' ";
		List list = getHibernateTemplate().find(hql);
		if(list.size()!=0)
		{
			flag = true;
		}
		return flag;
	}
	
	public boolean remove(String[] ids){
		boolean flag=false;
		List<TotalPriceHist> bs=new ArrayList<TotalPriceHist>();
		for (int j = 0; j < ids.length; j++) {	
				bs.add(new TotalPriceHist(ids[j]));
			}
		try{
			this.getHibernateTemplate().deleteAll(bs);
			flag=true;
		}catch(Exception e){
			System.out.println("TestToolDao delete error!!!!!!!!!!");
		}
		return flag;
	}
	
	public List<String> getAllNames(){
		List<String> list=null;
		String hql="select username from SysUser where groupName is not null and level != 3  and level != 3  and lower(trim(username)) not in('汤飞','开发员')  order by level";
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
	
	public String getUserlevel(String username){
		
		String hql="select level from SysUser where username like '%"+username.trim()+"%'";
		List list=this.getHibernateTemplate().find(hql);
		String str=null;
		if(list!=null&&list.size()>=0){
			str=list.get(0).toString();		     
		}
		return str;
	}
	
	public List<Object> getUsernamesByGroup(String groupName){
		String query="select user.username from SysUser user where 1=1 and lower(trim(user.username)) not in('王全胜','杜高峰','开发员','管理员')";
		if(!groupName.equals("全选")){
			query+="and user.groupName like '%"+groupName+"%'";
		}
		List<Object> names= this.getHibernateTemplate().find(query);
		names.add("全选");
//		names.add(0, "全选");
		return names;
	}
	
	public List<String> getIdByTotalPrice(String _monthDate){
		String query="select mm.id from TotalPriceHist mm where 1=1 and monthDate like '%" + _monthDate + "%'";
		List<String> ids= this.getHibernateTemplate().find(query);
		return ids;
	}
}
