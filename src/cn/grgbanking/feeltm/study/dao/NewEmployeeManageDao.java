package cn.grgbanking.feeltm.study.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.domain.testsys.NewEmployeeManage;
import cn.grgbanking.framework.dao.BaseDao;
import cn.grgbanking.framework.dao.hibernate3.BaseHibernateTemplate;
import cn.grgbanking.framework.util.Page;

@Repository("newEmployeeManageDao")
public class NewEmployeeManageDao extends BaseDao<NewEmployeeManage>{
	public Page getPage(String uname, String groupName,String detName,int pageNum, int pageSize) {
		String hql = " from NewEmployeeManage  where 1=1 ";
		if (uname != null && !uname.equals("")) {
			hql += " and (uname like '%" + uname.trim() + "%' or userId like '%" + uname.trim()+"%')";
		}
		if (groupName != null && !groupName.equals("")) {
			hql += " and groupName like '%"+groupName.trim()+"%'";
		}
		if (detName != null && !detName.equals("")) {
			hql += " and detName like '%"+detName.trim()+"%'";
		}
		
		hql += " order by entryDate desc";
		System.out.println(hql);
		List list = getHibernateTemplate().find(hql);
		Page page=((BaseHibernateTemplate) getHibernateTemplate()).getPage(hql,pageNum,pageSize);
		return page;
	}
	
	
	
	
}	
