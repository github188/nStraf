package cn.grgbanking.feeltm.autoOa.dao;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Date;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.domain.testsys.AutoOa;
import cn.grgbanking.feeltm.util.StringUtil;
import cn.grgbanking.framework.dao.BaseDao;
import cn.grgbanking.framework.dao.hibernate3.BaseHibernateTemplate;
import cn.grgbanking.framework.util.Page;


@Repository("autoOaDao")
public class AutoOaDao extends BaseDao<AutoOa> {
	public Page getPage(String createMan,String description, String sendCycle,String strat,
			int pageNum, int pageSize,String end) {
		String hql ="";
		hql = " from AutoOa  where 1=1 ";
		if (createMan != null && !createMan.equals("")) {
			hql += " and createMan like '%" + createMan.trim() + "%' ";
		}
		if (strat != null && !strat.equals("")) {
			hql += " and create_date >= to_date('" + strat.trim()
					+ "','yyyy-MM-dd')";
		}
		if (description != null && !description.equals("")) {
			hql += " and description like '%" + description.trim() + "%' ";
		}
		if (sendCycle != null && !sendCycle.equals("")) {
			hql += " and sendCycle like '%"+sendCycle.trim()+"%'";
		}
		if (end != null && !end.equals("")) {
			hql += " and create_date <= to_date('" + end.trim()
					+ "','yyyy-MM-dd')";
		}
		hql += " order by createDate desc";
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
