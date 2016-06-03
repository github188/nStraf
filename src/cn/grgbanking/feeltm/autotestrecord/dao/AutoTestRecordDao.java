package cn.grgbanking.feeltm.autotestrecord.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.domain.testsys.AutoTestRecord;
import cn.grgbanking.framework.dao.BaseDao;
import cn.grgbanking.framework.dao.hibernate3.BaseHibernateTemplate;
import cn.grgbanking.framework.util.Page;

@Repository("autoTestRecordDao")
public class AutoTestRecordDao extends BaseDao<AutoTestRecord>{
	public Page getPage(String StartMonth,String EndMonth,String prjName,String version,String testStatus, int pageNum,int pageSize)
	{
		String hql = "FROM AutoTestRecord WHERE 1=1 ";
		
		if(StartMonth!=null&&!StartMonth.equals("") ){
			hql += " and ExecTime >= to_date('"+StartMonth.trim()+"','yyyy-MM-dd HH24:mi:ss') ";
		}
		
		if(EndMonth!=null&&!EndMonth.equals("")){
			hql += " and ExecTime <= to_date('"+EndMonth.trim()+"','yyyy-MM-dd HH24:mi:ss') ";
		}
		
		if(prjName!=null && !prjName.equals("")){
			hql += " and  PrjName like '%"+prjName.trim()+"%' ";
		}
		
		if(version!=null && !version.equals("")){
			hql += " and  VersionNo like '%"+version.trim()+"%' ";
		}
		
		if(testStatus!=null && !testStatus.equals("")){
			hql += " and  Status like '%"+testStatus.trim()+"%' ";
		}
		
		hql += " order by ExecTime desc,PrjName asc,VersionNo desc ";

		Page page=((BaseHibernateTemplate) getHibernateTemplate()).getPage(hql,pageNum,pageSize);
		return page;
	}
	
	public boolean remove(String[] ids){
		boolean flag=false;
		List<AutoTestRecord> bs=new ArrayList<AutoTestRecord>();
		for (int j = 0; j < ids.length; j++) {	
				bs.add(new AutoTestRecord(ids[j]));
			}
		try{
			this.getHibernateTemplate().deleteAll(bs);
			flag=true;
		}catch(Exception e){
			System.out.println("TestToolDao delete error!!!!!!!!!!");
		}
		return flag;
	}
	
	public List<String> getAllNames(){
		List<String> list=null;
		String hql="select username from SysUser where groupName is not null and lower(trim(username)) not in('汤飞','王全胜','开发员','杜高峰') order by groupName desc,level asc";
		list=this.getHibernateTemplate().find(hql);
		return list;
	}
	
}


