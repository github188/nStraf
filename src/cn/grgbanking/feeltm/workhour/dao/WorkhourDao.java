package cn.grgbanking.feeltm.workhour.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.workhour.domain.WorkhourInfo;
import cn.grgbanking.feeltm.workhour.domain.WorkhourSummary;
import cn.grgbanking.framework.dao.BaseDao;

@Repository("workhourDao")
public class WorkhourDao extends BaseDao<WorkhourSummary> {
	
	public List getSummaryPage(WorkhourInfo info)
	{
		String searchtype = info.getSearchtype();
		String hql="";
		if("1".equals(searchtype)){
			hql = "select new cn.grgbanking.feeltm.workhour.domain.WorkSummary(username,deptname,groupname,prjName,type,sum(total),'',userlevel)"+
					 " from WorkhourSummary where 1=1";
		}else if("2".equals(searchtype)){
			hql = "select new cn.grgbanking.feeltm.workhour.domain.WorkSummary('',deptname,'',prjName,type,sum(total),to_char(wmsys.wm_concat(username)),userlevel)"+
					 " from WorkhourSummary where 1=1";
		}else if("3".equals(searchtype)){
			hql = "select new cn.grgbanking.feeltm.workhour.domain.WorkSummary('','',groupname,prjName,type,sum(total),to_char(wmsys.wm_concat(username)),userlevel)"+
					 " from WorkhourSummary where 1=1";
		}else if("4".equals(searchtype)){
			hql = "select new cn.grgbanking.feeltm.workhour.domain.WorkSummary('','','',prjName,type,sum(total),to_char(wmsys.wm_concat(username)),userlevel)"+
					 " from WorkhourSummary where 1=1";
		}else{
			hql = "select new cn.grgbanking.feeltm.workhour.domain.WorkSummary(username,deptname,groupname,prjName,type,sum(total),'',userlevel)"+
					 " from WorkhourSummary where 1=1";
		}
		if(info!=null){
			if(info.getStart()!=null&&!info.getStart().equals("") ){
				hql += " and logdate >= to_date('"+info.getStart().trim()+"','yyyy-MM-dd') ";
			}
			if(info.getEnd()!=null&&!info.getEnd().equals("")){
				hql += " and logdate <= to_date('"+info.getEnd().trim()+"','yyyy-MM-dd') ";
			}
			if(info.getGroupName()!=null && !info.getGroupName().equals("")){
				hql += " and groupname like '%"+info.getGroupName().trim()+"%' ";
			}
			if(info.getUsername()!=null && !info.getUsername().equals("")){
				hql += " and username like '%"+info.getUsername().trim()+"%' ";
			}
			if(info.getPrjName()!=null && !info.getPrjName().equals("")){
				hql += " and prjName like '%"+info.getPrjName().trim()+"%' ";
			}
			if(info.getDeptName()!=null && !info.getDeptName().equals("")){
				hql += " and deptname like '%"+info.getDeptName().trim()+"%' ";
			}
		}
		
		if("1".equals(searchtype)){
			hql=hql+" group by username,deptname,groupname,prjName,type,userlevel";
		}else if("2".equals(searchtype)){
			hql=hql+" group by deptname,prjName,type,userlevel";
		}else if("3".equals(searchtype)){
			hql=hql+" group by groupname,prjName,type,userlevel";
		}else if("4".equals(searchtype)){
			hql=hql+" group by deptname,prjName,type,userlevel";
		}else{
			hql=hql+" group by username,deptname,groupname,prjName,type,userlevel";
		}
		hql+=" order by prjName";
		System.out.println("hql==="+hql);
		List summ=this.getHibernateTemplate().find(hql);
		return summ;
	}
}
	
	
	
	
