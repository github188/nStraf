package cn.grgbanking.feeltm.caseanalyse.dao;

import java.text.DecimalFormat;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.domain.testsys.CaseAnalyse;
import cn.grgbanking.framework.dao.BaseDao;
import cn.grgbanking.framework.dao.hibernate3.BaseHibernateTemplate;
import cn.grgbanking.framework.util.Page;


@Repository("caseAnalyseDao")
public class CaseAnalyseDao extends BaseDao<CaseAnalyse> {
	public Page getPage(String createDate, String createMan,String summary,String category,
			int pageNum, int pageSize,String raiseEndDate) {
		String hql ="";
		hql = " from CaseAnalyse  where 1=1 ";
		if (createDate != null && !createDate.equals("")) {
			hql += " and create_date >= to_date('" + createDate.trim()
					+ "','yyyy-MM-dd') ";
		}
		if (createMan != null && !createMan.equals("")) {
			hql += " and create_man like '%" + createMan.trim() + "%' ";
		}
		if (summary != null && !summary.equals("")) {
			hql += " and summary like '%" + summary.trim() + "%' ";
		}
		if (category != null && !category.equals("")) {
			hql += " and category like '%"+category.trim()+"%'";
		}
		if (raiseEndDate != null && !raiseEndDate.equals("")) {
			hql += " and create_date <= to_date('" + raiseEndDate.trim()
					+ "','yyyy-MM-dd') ";
		}
		hql += " order by pno desc";
		System.out.println(hql);	
		List list = getHibernateTemplate().find(hql);
		Page page = ((BaseHibernateTemplate) getHibernateTemplate()).getPage(hql,pageNum,pageSize);
		return page;
	}
	
	public  String  getNextNo(){
		String str=null;
		List list=this.getHibernateTemplate().find("select max(p.pno) from CaseAnalyse p");
		if(list!=null&&list.size()>=0){
		      String p=(String)list.get(0);
		      if(p!=null){
		    	  if(p.contains("A")){
			    	  p=p.substring(1);
			    	  long d=Long.parseLong(p)+1;
			    	  DecimalFormat format=new DecimalFormat("A0000");
			  		  str=format.format(d);
			      }
		      }
		      else
		      {
					str = "A0001";
		      }
		     
		}
		return str;		
	}
	
}
