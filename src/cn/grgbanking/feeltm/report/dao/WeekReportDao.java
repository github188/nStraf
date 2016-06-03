package cn.grgbanking.feeltm.report.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.domain.testsys.WeekReport;
import cn.grgbanking.framework.dao.BaseDao;
import cn.grgbanking.framework.dao.hibernate3.BaseHibernateTemplate;
import cn.grgbanking.framework.util.Page;

@Repository("weekReportDao")
public class WeekReportDao extends BaseDao<WeekReport>{
	public boolean remove(String[] ids){
		boolean flag=false;
		List<WeekReport> reports=new ArrayList<WeekReport>();
		for (int j = 0; j < ids.length; j++) {	
				reports.add(new WeekReport(ids[j]));
			}
		try{
			this.getHibernateTemplate().deleteAll(reports);
			flag=true;
		}catch(Exception e){
			System.out.println("WeekReportDao delete error!!!!!!!!!!");
		}
		return flag;
	}
	
//	返回page分页
	public Page getPage(String start,String end,String groupName,int pageNum,int pageSize)
	{
		String hql = "FROM WeekReport report WHERE 1=1 ";
			if(groupName!=null && !groupName.equals("")){
				hql += " and report.groupName = '"+groupName.trim()+"' ";
			}
			if(start!=null&&!start.equals("") ){
				hql += " and report.startDate = to_date('"+start.trim()+"','yyyy-MM-dd') ";
			}
			if(end!=null&&!end.equals("")){
				hql += " and report.endDate = to_date('"+end.trim()+"','yyyy-MM-dd') ";
			}
			
		hql += " order by report.startDate desc,report.groupName asc ";
		System.out.println(hql);
		Page page=((BaseHibernateTemplate) getHibernateTemplate()).getPage(hql,pageNum,pageSize);
		return page;
	}
	
	/**
	 * 
	 * @param groupName  组名
	 * @param currentEndDate   本周的结束日期
	 * @return  下周的计划
	 */
	public WeekReport getNextWeekPlan(String groupName,Date currentEndDate){
		Object[] obj={groupName,currentEndDate};
		WeekReport report=null;
		String hql = "FROM WeekReport report WHERE  report.groupName=? and report.startDate > ? order by report.startDate";
		List o=this.getHibernateTemplate().find(hql,obj);
		if(o!=null&&o.size()>0){
			report=(WeekReport)o.get(0);
		}
//		if(start!=null&&!start.equals("") ){
//			hql += " and report.startDate = to_date('"+start.trim()+"','yyyy-MM-dd') ";
//		}
//		if(end!=null&&!end.equals("")){
//			hql += " and report.endDate = to_date('"+end.trim()+"','yyyy-MM-dd') ";
//		}
		return report;
	}
	
	public List<WeekReport> getAllReportsByWeek(){
		final String hql = "FROM WeekReport report order by  report.startDate desc,report.groupName";
		List<WeekReport> reports=this.getHibernateTemplate().executeFind(new HibernateCallback() {
			
			@Override
			public Object doInHibernate(Session session) throws HibernateException,
					SQLException {
				List list=session.createQuery(hql).setFirstResult(0).setMaxResults(8).list();
				return list;
			}
		});
		return reports;
	}
}
