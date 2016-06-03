package cn.grgbanking.feeltm.report.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.domain.testsys.MonthReport;
import cn.grgbanking.framework.dao.BaseDao;
import cn.grgbanking.framework.dao.hibernate3.BaseHibernateTemplate;
import cn.grgbanking.framework.util.Page;

@Repository("monthReportDao")
public class MonthReportDao extends BaseDao<MonthReport>{
	public boolean remove(String[] ids){
		boolean flag=false;
		List<MonthReport> reports=new ArrayList<MonthReport>();
		for (int j = 0; j < ids.length; j++) {	
				reports.add(new MonthReport(ids[j]));
			}
		try{
			this.getHibernateTemplate().deleteAll(reports);
			flag=true;
		}catch(Exception e){
			System.out.println("MonthReportDao delete error!!!!!!!!!!");
		}
		return flag;
	}
	
//	返回page分页
	public Page getPage(String start,String end,String groupName,int pageNum,int pageSize)
	{
		String hql = "FROM MonthReport report WHERE 1=1 ";
			if(groupName!=null && !groupName.equals("")){
				hql += " and report.groupName = '"+groupName.trim()+"' ";
			}
			if(start!=null&&!start.equals("") ){
				hql += " and report.startDate >= to_date('"+start.trim()+"','yyyy-MM') ";
			}
			if(end!=null&&!end.equals("")){
				hql += " and report.startDate <= to_date('"+end.trim()+"','yyyy-MM') ";
			}
			
		hql += " order by report.startDate desc, report.groupName asc ";
		System.out.println(hql);
		Page page=((BaseHibernateTemplate) getHibernateTemplate()).getPage(hql,pageNum,pageSize);
		return page;
	}
	
	public boolean isExist(String groupName,Date startDate){
		boolean flag=false;
		String hql="from MonthReport report where report.groupName=? and report.startDate=?";
		int i=this.getHibernateTemplate().find(hql, new Object[]{groupName,startDate}).size();
		if(i==1){
			flag=true;
		}
		return flag;
	}
}
