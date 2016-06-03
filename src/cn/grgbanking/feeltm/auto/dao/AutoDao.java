package cn.grgbanking.feeltm.auto.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.auto.domain.AutoListInfo;
import cn.grgbanking.feeltm.domain.testsys.ExecInfo;
import cn.grgbanking.feeltm.domain.testsys.Overtime;
import cn.grgbanking.framework.dao.BaseDao;
import cn.grgbanking.framework.dao.hibernate3.BaseHibernateTemplate;
import cn.grgbanking.framework.util.Page;

@Repository("autoDao")
public class AutoDao extends BaseDao<ExecInfo>{
	
//	返回page分页
	public Page getPage(String start,String end,int pageNum,int pageSize, String projName)
	{	//String id, String username, String startTime,
		//String endTime, String execTime, String status, String prjName,
		//private String testType; machineNo; versionNo;
		String hql = "SELECT new cn.grgbanking.feeltm.auto.domain.AutoListInfo(id,username,to_char(startTime,'yyyy-MM-dd HH24:mi:ss'),to_char(endTime,'yyyy-MM-dd HH24:mi:ss'),to_char(execTime,'yyyy-MM-dd HH24:mi:ss'),status,prjName,testType,machineNo,versionNo,allScripts,failScripts,rankScripts) FROM ExecInfo  WHERE 1=1 ";
			if(start!=null&&!start.equals("") ){
				hql += " and startTime >= to_date('"+start.trim()+"','yyyy-MM-dd HH24:mi:ss') ";
			}
			if(end!=null&&!end.equals("")){
				hql += " and startTime <= to_date('"+end.trim()+"','yyyy-MM-dd HH24:mi:ss') ";
			}
			if(projName!=null&&!projName.equals("")){
				hql += " and prjName like '%" + projName.trim() + "%'";
			}
			
		hql += " order by startTime desc,prjName asc,versionNo desc ";
		System.out.println(hql);
		Page page=((BaseHibernateTemplate) getHibernateTemplate()).getPage(hql,pageNum,pageSize);
		return page;
	}
	
	public String getFile(String id){
		String hql="select info.reportFile from ExecInfo info where 1=1 ";
		if(id!=null&&!id.equals("")){
			hql+="and info.id= '"+id+"'";
		}
		List<String> list=this.getHibernateTemplate().find(hql);
		return list.get(0);
	}
	
	public AutoListInfo getInfo(String id){
		String hql = "SELECT new cn.grgbanking.feeltm.auto.domain.AutoListInfo(username,to_char(startTime,'yyyy-MM-dd HH24:mi:ss'),to_char(endTime,'yyyy-MM-dd HH24:mi:ss'),to_char(execTime,'yyyy-MM-dd HH24:mi:ss'),status,prjName,testType,machineNo,versionNo,standard,netIP) FROM ExecInfo  WHERE 1=1 ";
		AutoListInfo info=null;
		if(id!=null&&!id.equals("")){
			hql+="and id= '"+id+"'";
			System.out.println("hql: "+hql);
			info=(AutoListInfo)(this.getHibernateTemplate().find(hql).get(0));
		}
		return info;
	}
	
	public boolean remove(String[] ids){
		boolean flag=false;
		List<ExecInfo> bs=new ArrayList<ExecInfo>();
		for (int j = 0; j < ids.length; j++) {	
				bs.add(new ExecInfo(ids[j]));
			}
		try{
			this.getHibernateTemplate().deleteAll(bs);
			flag=true;
		}catch(Exception e){
			System.out.println("TestToolDao delete error!!!!!!!!!!");
		}
		return flag;
	}
}
