package cn.grgbanking.feeltm.report.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Transient;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.domain.testsys.WorkReport;
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
	
	public Long selectSum(Date createDate,String username){
		String queryString="select sum(report.subtotal) from WorkReport report group by report.startDate,report.username having report.startDate=? and report.username=?";
		List list=this.getHibernateTemplate().find(queryString, new Object[]{createDate,username});
		Long i=0L;
		if(list!=null){
			 i=(Long)list.get(0);
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
	
	public void updateAll(String username,Date createDate,List<WorkReport> reports){
		boolean flag=remove(username, createDate);
		if(flag){
			add(reports);
		}
	}
	
	//(Date createDate, String username, String groupName,Integer subsum)hql += " and report.prjName like '%"+info.getPrjName().trim()+"%' ";
	public Page getPage(ReportDayInfo info,int pageNum,int pageSize)
	{
		//String hql = "select new cn.grgbanking.feeltm.report.domain.ReportDay(startDate,username,max(groupName),sum(subtotal)) from WorkReport group by startDate,username having 1=1 ";
//		String hql="select startDate,username,max(groupName),sum(subtotal) from WorkReport group by startDate,username having 1=1";
		String hql = "select new cn.grgbanking.feeltm.report.domain.ReportDay(startDate,username,max(groupName),sum(subtotal)) from WorkReport group by startDate,username having 1=1 ";
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
		hql += " order by max(groupName) asc,username asc,startDate desc ";
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
	
	
}
	
	
	
	
