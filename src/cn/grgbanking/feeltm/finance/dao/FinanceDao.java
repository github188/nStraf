package cn.grgbanking.feeltm.finance.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.domain.testsys.DepartFinance;
import cn.grgbanking.feeltm.finance.domain.BalanceInfo;
import cn.grgbanking.framework.dao.BaseDao;
import cn.grgbanking.framework.dao.hibernate3.BaseHibernateTemplate;
import cn.grgbanking.framework.util.Page;

@Repository("financeDao")
public class FinanceDao extends BaseDao<DepartFinance>{
	/**
	 * 
	 * @param start  开始日期
	 * @param end  结束日期
	 * @param type  收支类型 0支出，1收入  ，当为0时，即支出不为0;当为1时，即收入为0
	 * @param responsible   经手人
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public Page getPage(String start,String end,String  type,String responsible,int pageNum,int pageSize)
	{
		String hql = " from DepartFinance where 1=1 ";
			if(type!=null && !type.equals("")){
				if(type.equals("0")){
					hql += " and pay <> '0' ";
				}else{					
					hql += " and income  <> '0' ";
				}
			}
			if(start!=null&&!start.equals("") ){
				hql += " and activityDate >= to_date('"+start.trim()+"','yyyy-MM-dd') ";
			}
			if(end!=null&&!end.equals("")){
				hql += " and activityDate <= to_date('"+end.trim()+"','yyyy-MM-dd') ";
			}
			if(responsible!=null&&!responsible.equals("")){
				hql +=" and responsible like  '%"+responsible.trim()+"%'";
			}
			
		hql += " order by activityDate desc,responsible asc ";
		System.out.println(hql);
		List list=getHibernateTemplate().find(hql);
		return ((BaseHibernateTemplate) getHibernateTemplate()).getPageByList(list, pageNum, pageSize);
	}
	
	public BalanceInfo getBalance(String start,String end){
		BalanceInfo info=new BalanceInfo(0, 0, 0);
		String hql="SELECT new cn.grgbanking.feeltm.finance.domain.BalanceInfo(SUM(pay),SUM(income),SUM(income)-SUM(pay)) from DepartFinance where 1=1";
		if(start!=null&&!start.equals("") ){
			hql += " and activityDate >= to_date('"+start.trim()+"','yyyy-MM-dd') ";
		}
		if(end!=null&&!end.equals("")){
			hql += " and activityDate <= to_date('"+end.trim()+"','yyyy-MM-dd') ";
		}
		List list=null;
		try{
		 list=this.getHibernateTemplate().find(hql);
		}catch(Exception e){
			e.printStackTrace();
		}
		if(list!=null&&list.size()>0){
			info=(BalanceInfo)list.get(0);
		}
		
		return info;
	}
}
