package cn.grgbanking.feeltm.postremove.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.domain.testsys.PostRemove;
import cn.grgbanking.framework.dao.BaseDao;
import cn.grgbanking.framework.dao.hibernate3.BaseHibernateTemplate;
import cn.grgbanking.framework.util.Page;


@Repository("postremoveDao")
public class PostRemoveDao extends BaseDao<PostRemove> {
	public Page getPage(String name,String advencedate,int pageNum, int pageSize) {
		String hql ="";
		hql = " from PostRemove  where 1=1 ";
		if (name != null && !name.equals("")) {
			hql += " and name like '%" + name.trim() + "%' ";
		}
		if (advencedate != null && !advencedate.equals("")) {
			hql += " and startdate <= to_date('" + advencedate.trim()
					+ "','yyyy-MM-dd')";
		}
		if (advencedate != null && !advencedate.equals("")) {
			hql += " and finishdate >= to_date('" + advencedate.trim()
					+ "','yyyy-MM-dd')";
		}
		hql += " order by advencedate desc,name desc";
		System.out.println(hql);	
		List list = getHibernateTemplate().find(hql);
		Page page = ((BaseHibernateTemplate) getHibernateTemplate()).getPage(hql,pageNum,pageSize);
		return page;
	}
	
	public List<String> getAllNames(){
		List<String> list=null;
		String hql="select username from SysUser where groupName is not null and level != 3  and lower(trim(username)) not in('汤飞','开发员') order by groupName desc,level asc";
		list=this.getHibernateTemplate().find(hql);
		return list;
	}
	
	
	
}
