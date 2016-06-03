package cn.grgbanking.feeltm.prj.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.domain.testsys.ProjectDB;
import cn.grgbanking.framework.dao.BaseDao;
import cn.grgbanking.framework.dao.hibernate3.BaseHibernateTemplate;
import cn.grgbanking.framework.util.Page;

@Repository("dbDao")
public class ProjectDbDao extends BaseDao<ProjectDB>{
	public Page getPage(String prjName,int pageNum,int pageSize)
	{
		String hql = "FROM ProjectDB params WHERE 1=1 ";
			if(prjName!=null && !prjName.equals("")&&!prjName.equals("全选")){
				hql += " and params.prjName like '%"+prjName.trim()+"%' ";
			}
		hql += " order by params.prjName asc ";
		System.out.println(hql);
		Page page=((BaseHibernateTemplate) getHibernateTemplate()).getPage(hql,pageNum,pageSize);
		return page;
	}
	
	public List<String> findAll(){
		String hql="SELECT params.prjName FROM ProjectDB params";
		List<String> prjTypes=this.getHibernateTemplate().find(hql);
		return prjTypes;
	}
	
	public boolean remove(String[] ids){
		boolean flag=false;
		List<ProjectDB> params=new ArrayList<ProjectDB>();
		for (int j = 0; j < ids.length; j++) {	
			params.add(new ProjectDB(ids[j]));
			}
		try{
			this.getHibernateTemplate().deleteAll(params);
			flag=true;
		}catch(Exception e){
			System.out.println("paramsDao delete error!!!!!!!!!!");
		}
		return flag;
	}
	
	
	public ProjectDB getDB(String prjName){
		ProjectDB prjDB=null;
		String hql = "FROM ProjectDB params WHERE 1=1 ";
		if(prjName!=null && !prjName.equals("")){
			hql += " and params.prjName like '%"+prjName.trim()+"%' ";
		}
		List<ProjectDB> list=this.getHibernateTemplate().find(hql);
		if(list.size()==0){
			return null;
		}
		prjDB=list.get(0);
		return prjDB;
	}
}
