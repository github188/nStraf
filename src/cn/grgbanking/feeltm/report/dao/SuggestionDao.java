package cn.grgbanking.feeltm.report.dao;

import java.text.DecimalFormat;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.domain.testsys.ProblemOrSuggestion;
import cn.grgbanking.framework.dao.BaseDao;
import cn.grgbanking.framework.dao.hibernate3.BaseHibernateTemplate;
import cn.grgbanking.framework.util.Page;

@Repository("suggestionDao")
public class SuggestionDao extends BaseDao<ProblemOrSuggestion> {

	public Page getPage(String raiseDate, String raiseMan,String resloveMan, String status,String category,String summary, String description,String pno,String planFinishDate,
			int pageNum, int pageSize,String raiseEndDate) {
		String hql = " from ProblemOrSuggestion  where 1=1 ";
		if (raiseMan != null && !raiseMan.equals("")) {
			hql += " and raise_man like '%" + raiseMan.trim() + "%' ";
		}
		if (resloveMan != null && !resloveMan.equals("")) {
			hql += " and resolve_man like '%" + resloveMan.trim() + "%' ";
		}
		if (raiseDate != null && !raiseDate.equals("")) {
			hql += " and raise_date >= to_date('" + raiseDate.trim()
					+ "','yyyy-MM-dd') ";
		}
		if (raiseEndDate != null && !raiseEndDate.equals("")) {
			hql += " and raise_date <= to_date('" + raiseEndDate.trim()
					+ "','yyyy-MM-dd') ";
		}
		if (status != null && !status.equals("")) {
			hql += " and status = '"+status+"'";
		}
		if (category != null && !category.equals("")) {
			hql += " and category = '"+category+"'";
		}
		if (summary != null && !summary.equals("")) {
			hql += " and summary like '%" + summary.trim() + "%' ";
		}
		if (description != null && !description.equals("")) {
			hql += " and description like '%" + description.trim() + "%' ";
		}
		
		if (pno != null && !pno.equals("")) {
			hql += " and pno like '%" + pno.trim() + "%' ";
		}
		if (planFinishDate != null && !planFinishDate.equals("")) {
			hql += " and finishing_date <= to_date('" + planFinishDate.trim()+ "','yyyy-MM-dd') ";
		}
		
		hql += " order by pno desc";
		System.out.println(hql);
		List list = getHibernateTemplate().find(hql);
		Page page=((BaseHibernateTemplate) getHibernateTemplate()).getPage(hql,pageNum,pageSize);
		
		return page;
	}
	
	public  String  getNextNo(){
		String str=null;
		List list=this.getHibernateTemplate().find("select max(p.pno) from ProblemOrSuggestion p");
		if(list!=null&&list.size()>=0){
		      String p=(String)list.get(0);
		      if(p.contains("P")){
		    	  p=p.substring(1);
		    	  long d=Long.parseLong(p)+1;
		    	  DecimalFormat format=new DecimalFormat("P0000");
		  		  str=format.format(d);
		      }
		}
		return str;
		
	}
	
	
	
	
}
