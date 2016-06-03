package cn.grgbanking.feeltm.report.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.domain.testsys.Behavior;
import cn.grgbanking.feeltm.report.domain.ReportDayInfo;
import cn.grgbanking.framework.dao.BaseDao;
import cn.grgbanking.framework.dao.hibernate3.BaseHibernateTemplate;
import cn.grgbanking.framework.util.Page;


@Repository("behaviorDao")
public class BehaviorDao extends BaseDao<Behavior>{
	
	public boolean remove(String[] ids){
		boolean flag=false;
		List<Behavior> bs=new ArrayList<Behavior>();
		for (int j = 0; j < ids.length; j++) {	
				bs.add(new Behavior(ids[j]));
			}
		try{
			this.getHibernateTemplate().deleteAll(bs);
			flag=true;
		}catch(Exception e){
			System.out.println("TestToolDao delete error!!!!!!!!!!");
		}
		return flag;
	}
	
	public Page getPage(ReportDayInfo info,int pageNum,int pageSize)
	{
		String hql = " from Behavior where 1=1 ";
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
				hql +=" and groupName = '"+info.getGroupName().trim()+"'";
			}
			
		}
		hql += " order by startDate desc,groupName asc,username asc ";
		System.out.println(hql);
		List list=getHibernateTemplate().find(hql);
		return ((BaseHibernateTemplate) getHibernateTemplate()).getPageByList(list, pageNum, pageSize);
	}
}
