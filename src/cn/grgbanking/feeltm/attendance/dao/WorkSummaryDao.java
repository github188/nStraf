package cn.grgbanking.feeltm.attendance.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.attendance.domain.WorkSummaryList;
import cn.grgbanking.framework.dao.BaseDao;
import cn.grgbanking.framework.dao.hibernate3.BaseHibernateTemplate;
import cn.grgbanking.framework.util.Page;

@Repository("workSummaryDao")
public class WorkSummaryDao  extends BaseDao<WorkSummaryList>{
	public Page getPage(String start,String end,int pageNum,int pageSize)
	{
		String hql = "FROM WorkSummaryList work WHERE 1=1 ";
		if(start!=null&&!start.equals("") ){
			hql += " and work.begindate >= to_date('"+start.trim()+"','yyyy-MM-dd') ";
		}
		if(end!=null&&!end.equals("")){
			hql += " and work.enddate <= to_date('"+end.trim()+"','yyyy-MM-dd') ";
		}
		hql += " order by employeeid";
		List list = getHibernateTemplate().find(hql);
		Page page = ((BaseHibernateTemplate) getHibernateTemplate()).getPageByList(list,pageNum,pageSize);
		return page;
	}
}
