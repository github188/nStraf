package cn.grgbanking.feeltm.report.dao;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.domain.testsys.WorkReport;
import cn.grgbanking.feeltm.report.domain.DayReportStatic;
import cn.grgbanking.feeltm.report.domain.ReportDayInfo;
import cn.grgbanking.feeltm.report.domain.ReportInfo;
import cn.grgbanking.framework.dao.BaseDao;
import cn.grgbanking.framework.dao.hibernate3.BaseHibernateTemplate;
import cn.grgbanking.framework.util.Page;

@Repository("reportDao")
public class ReportDao extends BaseDao<WorkReport> {
	
	/**
	 * 批量增加
	 * @param reports
	 */
	public void add(List<WorkReport> reports){
		Date date=new Date();
		SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String modifyDate=f.format(date);
		for(WorkReport report:reports){
			report.setModifyDate(modifyDate);
		}
		this.getHibernateTemplate().saveOrUpdateAll(reports);
	}
	
	/**
	 * 
	 * @param ids  一个report的id数组
	 * @return  i 返回为1代表成功删除成功,返加为0代表删除失败
	 */
	public boolean remove(String[] ids){
		boolean flag=false;
		List<WorkReport> reports=new ArrayList<WorkReport>();
		for (int j = 0; j < ids.length; j++) {	
				reports.add(new WorkReport(ids[j]));
			}
		try{
			this.getHibernateTemplate().deleteAll(reports);
			flag=true;
		}catch(Exception e){
			System.out.println("ReportDao delete error!!!!!!!!!!");
		}
		return flag;
	}
	
//	返回page分页
	public Page getPage(ReportInfo info,int pageNum,int pageSize)
	{
		String hql = "FROM WorkReport report WHERE 1=1 ";
		if(info!=null){
			if(info.getUsername()!=null && !info.getUsername().equals("")){
				hql += " and report.username = '"+info.getUsername().trim()+"' ";
			}
			if(info.getPrjName()!=null && !info.getPrjName().equals("")){
				hql += " and report.prjName like '%"+info.getPrjName().trim()+"%' ";
			}
			if(info.getStart()!=null&&!info.getStart().equals("") ){
				hql += " and report.startDate >= to_date('"+info.getStart().trim()+"','yyyy-MM-dd') ";
			}
			if(info.getEnd()!=null&&!info.getEnd().equals("")){
				hql += " and report.startDate <= to_date('"+info.getEnd().trim()+"','yyyy-MM-dd') ";
			}
		}
		hql += " order by report.startDate desc ";
		System.out.println(hql);
		Page page=((BaseHibernateTemplate) getHibernateTemplate()).getPage(hql,pageNum,pageSize);
		return page;
	}
	/*
	 * 	private double managerment=0;
	private double requirement=0;
	private double design=0;
	private double test=0;
	private double other=0;
	private  double project=0;
	private double subtotal=0;
	private double code=0;
	 */
	public List getSummaryPage(ReportInfo info)
	{
//		String hql = "SELECT new cn.grgbanking.feeltm.report.domain.WorkSummary(report.prjName,sum(report.managerment),sum(report.requirement),sum(report.design),sum(report.code),sum(report.test),sum(report.other),sum(report.project),sum(report.subtotal)) FROM WorkReport report WHERE  1=1 ";
		String hql = "SELECT new cn.grgbanking.feeltm.report.domain.WorkSummary(report.prjName,sum(report.managerment),sum(report.requirement),sum(report.design),sum(report.code),sum(report.test),sum(report.other),sum(report.project),round(sum(report.subtotal)/8,1),count(distinct report.username)) FROM WorkReport report WHERE  1=1 ";
		//if(info.getPrjName()!=null && info.getPrjName().equals("请假放假")){
			//hql = "SELECT new cn.grgbanking.feeltm.report.domain.WorkSummary(report.prjName,sum(report.managerment),sum(report.requirement),sum(report.design),sum(report.code),sum(report.test),sum(report.other),sum(report.project),sum(report.subtotal)) FROM WorkReport report WHERE  report.prjName= '请假放假' ";
		//}
		if(info!=null){
			if(info.getStart()!=null&&!info.getStart().equals("") ){
				hql += " and report.startDate >= to_date('"+info.getStart().trim()+"','yyyy-MM-dd') ";
			}
			if(info.getEnd()!=null&&!info.getEnd().equals("")){
				hql += " and report.startDate <= to_date('"+info.getEnd().trim()+"','yyyy-MM-dd') ";
			}
			if(info.getGroupName()!=null && !info.getGroupName().equals("")){
				hql += " and report.groupName = '"+info.getGroupName().trim()+"' ";
			}
			if(info.getUsername()!=null && !info.getUsername().equals("")){
				hql += " and report.username = '"+info.getUsername().trim()+"' ";
			}
			hql+=" group by report.prjName having 1=1 ";
			if(info.getPrjName()!=null && !info.getPrjName().equals("")){
				hql += " and report.prjName like '%"+info.getPrjName().trim()+"%' ";
			}
			hql+="order by report.prjName";
			
		}
		List summ=this.getHibernateTemplate().find(hql);
		return summ;
	}
	
	//保留小数点一位
	private String get1dec(double a) {
		BigDecimal bd = new BigDecimal(a);
		BigDecimal re = bd.divide(new BigDecimal(1), 1,
				BigDecimal.ROUND_HALF_UP);
		return re + "";
	}
	
	public Double selectSum(Date createDate,String username){
		String queryString="select sum(report.subtotal) from WorkReport report group by report.startDate,report.username having report.startDate=? and report.username=?";
		List list=this.getHibernateTemplate().find(queryString, new Object[]{createDate,username});
		Double i=0.0;
		if(list!=null){
			 i=(Double)list.get(0);
		}
		return i;
		
	}
	
	
	
	
	/**
	 * 
	 * @param username
	 * @param createDate
	 * @return  查询出来某用户某天的所有报告的详情
	 */
	
	public List<WorkReport> selectReports(String username,Date createDate){
		String query="from WorkReport report where report.username=? and report.startDate=?";
		List<WorkReport> reports=(List<WorkReport>)this.getHibernateTemplate().find(query, new Object[]{username,createDate});
//		List<WorkReport> reports=(List<WorkReport>)this.getObjectList(query, new Object[]{username,createDate});
		return reports;
	}
	
	
	public  boolean remove(String username,Date createDate){
		boolean flag=false;
		try{
			String queryString="delete from WorkReport report where report.username=? and report.startDate=?";
			this.getHibernateTemplate().bulkUpdate(queryString, new Object[]{username,createDate});
			flag=true;
		}catch(Exception e){
			System.out.println("ReportDao delete error!!!!!!!!!!");
		}
		return flag;
	}
	
	public void updateAll(String username,Date createDate,Date createDateHidden,List<WorkReport> reports){
		boolean flag=remove(username, createDateHidden);
		if(flag){
			for(WorkReport w:reports){
				w.setStartDate(createDate);
			}
			add(reports);
		}
	}
	
	//(Date createDate, String username, String groupName,Integer subsum)hql += " and report.prjName like '%"+info.getPrjName().trim()+"%' ";
	public Page getPage(ReportDayInfo info,int pageNum,int pageSize)
	{
		//String hql = "select new cn.grgbanking.feeltm.report.domain.ReportDay(startDate,username,max(groupName),sum(subtotal)) from WorkReport group by startDate,username having 1=1 ";
//		String hql="select startDate,username,max(groupName),sum(subtotal) from WorkReport group by startDate,username having 1=1";
		String hql = "select new cn.grgbanking.feeltm.report.domain.ReportDay(startDate,username,max(groupName),sum(subtotal),max(modifyDate)) from WorkReport group by startDate,username having 1=1 ";
		if(info!=null){
			if(info.getUsername()!=null && !info.getUsername().equals("")){
				hql += " and username = '"+info.getUsername().trim()+"' ";
			}
			if(info.getStart()!=null&&!info.getStart().equals("") ){
				hql += " and startDate >= to_date('"+info.getStart().trim()+"','yyyy-MM-dd') ";
			}
			if(info.getEnd()!=null&&!info.getEnd().equals("")){
				hql += " and startDate <= to_date('"+info.getEnd().trim()+"','yyyy-MM-dd') ";
			}
			if(info.getGroupName()!=null&&!info.getGroupName().equals("")){
				hql +=" and max(groupName) = '"+info.getGroupName().trim()+"'";
			}
			
		}
		hql += " order by startDate desc,max(groupName) asc,username asc ";
		System.out.println(hql);
		List list=getHibernateTemplate().find(hql);
		return ((BaseHibernateTemplate) getHibernateTemplate()).getPageByList(list, pageNum, pageSize);

//		Page page=((BaseHibernateTemplate) getHibernateTemplate()).getPage(hql,pageNum,pageSize);
	}
	
//	  private Page getPageList(List list,int pageNum,int pageSize)throws DataAccessException{
//	    	Page page = ((BaseHibernateTemplate) getHibernateTemplate()).getPageByList(list, pageNum, pageSize);
//	    	return page;
//	    }
	
	public List<Object> getUsernamesByGroup(String groupName){
		String query="select user.username from SysUser user where 1=1 ";
		if(!groupName.equals("全选")){
			query+="and user.groupName= '"+groupName+"'";
		}
		List<Object> names= this.getHibernateTemplate().find(query);
		names.add("全选");
//		names.add(0, "全选");
		return names;
	}
	
	/**
	 *  select t.start_date,to_char(t.start_date,'day'),count(distinct(t.start_date||t.user_id)),sum(t.subtotal) 
	 *  from report_info t
		group by t.start_date
		having to_char(t.start_date,'yyyy-MM')='2012-03'
		order by t.start_date desc 
	 */
	public List<DayReportStatic> dayStatic(final String currentMonth){
//		String hql="select new cn.grgbanking.feeltm.report.domain.DayReportStatic(t.startDate,to_char(t.startDate,'day'),count(distinct(t.startDate||t.username)),sum(t.subtotal)) from WorkReport t group by t.startDate having to_char(t.startDate,'yyyy-MM')=? order by t.startDate desc ";
		//String hql="select new cn.grgbanking.feeltm.report.domain.DayReportStatic(t.startDate,to_char(t.startDate,'day'),count(distinct(t.startDate||t.username)),sum(t.subtotal)) from WorkReport t group by t.startDate having to_char(t.startDate,'yyyy-MM')=? order by t.startDate desc ";
	//	List<DayReportStatic> ds=this.getHibernateTemplate().find(hql, currentMonth);
	//	List<DayReportStatic> ds=this.getHibernateTemplate().find(hql, currentMonth);
		List<DayReportStatic> list11=new ArrayList<DayReportStatic>();
		List<Object[]> sts=this.getHibernateTemplate().executeFind(new HibernateCallback() {
			
			@Override
			public Object doInHibernate(Session session) throws HibernateException,
					SQLException {
				StringBuffer sb=new StringBuffer();
				sb.append("select t.start_date,to_char(t.start_date,'day'),count(distinct(t.start_date||t.user_id)),sum(t.subtotal) ");
				sb.append("from report_info t ");
				sb.append("group by t.start_date having to_char(t.start_date,'yyyy-MM')=? ");
				sb.append("order by t.start_date desc");
				System.out.println(sb.toString());
				List list=null;
				try{
					//list=session.createSQLQuery(sb.toString()).setString(0, currentMonth).list();
					list=session.createSQLQuery(sb.toString()).setString(0, currentMonth).list();
				}catch(Exception e){
					e.printStackTrace();
				}
				return list;
			}
		});
		for(Object[] obj:sts){
			Date d=(Date)obj[0];
			String stTmp=(String)obj[1];
			int i=((BigDecimal)obj[2]).intValue();
			double dd=((BigDecimal)obj[3]).doubleValue();
			DayReportStatic drs=new DayReportStatic(d, stTmp, i, dd);
			list11.add(drs);
		}
		
		
		return list11;
	}
	
	/**
	 * select WM_CONCAT(u.c_username) from sys_user u where (u.level1=1 or u.level1=2) 
		and u.c_username not in (select user_id from report_info where to_char(start_date,'yyyy-MM-dd')='2012-03-05')	
	 * @param specifidDate
	 * @return
	 */
	public String getUnwritePersons(final Date specifidDate){
		String str="";
		 List<String> ss=this.getHibernateTemplate().executeFind(new HibernateCallback() {
			
			@Override
			public Object doInHibernate(Session session) throws HibernateException,
					SQLException {
				
				SimpleDateFormat fo=new SimpleDateFormat("yyyy-MM-dd");
				String date=fo.format(specifidDate);
				StringBuffer sb=new StringBuffer();
				sb.append("select WM_CONCAT(u.c_username) from sys_user u where (u.level1=1 or u.level1=2) ");
				sb.append("and u.c_username not in (select user_id from report_info where to_char(start_date,'yyyy-MM-dd')=?)");
				List  str=null;
				try{
				 str=session.createSQLQuery(sb.toString()).setString(0, date).list();
				}catch(Exception e){
					e.printStackTrace();
				}
				 return str;
			}
		});
		if(ss!=null&&ss.size()>0){
			str=ss.get(0);
		}
		return str;
	}
	
	public List<DayReportStatic> dayStatic(final String start,final String end){
	
		List<DayReportStatic> list11=new ArrayList<DayReportStatic>();
		List<Object[]> sts=this.getHibernateTemplate().executeFind(new HibernateCallback() {
			
			@Override
			public Object doInHibernate(Session session) throws HibernateException,
					SQLException {
				StringBuffer sb=new StringBuffer();
				sb.append("select t.start_date,to_char(t.start_date,'day'),count(distinct(t.start_date||t.user_id)),sum(t.subtotal) ");
				sb.append("from report_info t ");
				sb.append("group by t.start_date having to_char(t.start_date,'yyyy-MM-dd') between ? and ? ");
				sb.append("order by t.start_date desc");
				System.out.println(sb.toString());
				List list=null;
				try{
					//list=session.createSQLQuery(sb.toString()).setString(0, currentMonth).list();
					list=session.createSQLQuery(sb.toString()).setString(0, start).setString(1,end).list();
				}catch(Exception e){
					e.printStackTrace();
				}
				return list;
			}
		});
		for(Object[] obj:sts){
			Date d=(Date)obj[0];
			String stTmp=(String)obj[1];
			int i=((BigDecimal)obj[2]).intValue();
			double dd=((BigDecimal)obj[3]).doubleValue();
			DayReportStatic drs=new DayReportStatic(d, stTmp, i, dd);
			list11.add(drs);
		}
		return list11;
	}

	public String getWritePersons(final Date specifidDate) {
		String str="";
		 List<String> ss=this.getHibernateTemplate().executeFind(new HibernateCallback() {

			@Override
			public Object doInHibernate(Session session) throws HibernateException,
					SQLException {
			
				SimpleDateFormat fo=new SimpleDateFormat("yyyy-MM-dd");
				String date=fo.format(specifidDate);
				StringBuffer sb=new StringBuffer();
				sb.append("select WM_CONCAT(distinct user_id ) from report_info ");
				sb.append("where to_char(start_date,'yyyy-MM-dd')=? ");
				List  str=null;
				try{
				 str=session.createSQLQuery(sb.toString()).setString(0, date).list();
				}catch(Exception e){
					e.printStackTrace();
				}
				 return str;
			}
		});
		if(ss!=null&&ss.size()>0){
			str=ss.get(0);
		}
		return str;
	}
	
}
	
	
	
	
