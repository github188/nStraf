package cn.grgbanking.feeltm.prj.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.domain.testsys.ProjectParams;
import cn.grgbanking.framework.dao.BaseDao;
import cn.grgbanking.framework.dao.hibernate3.BaseHibernateTemplate;
import cn.grgbanking.framework.util.Page;

@Repository("paramsDao")
public class ParamsDao extends BaseDao<ProjectParams>{
	public Page getPage(String prjName,String versionNO,String prjType1,int pageNum,int pageSize)
	{
		String hql = "FROM ProjectParams params WHERE 1=1 ";
			if(prjName!=null && !prjName.equals("")){
				hql += " and params.prjName like '%"+prjName.trim()+"%' ";
			}
			if(versionNO!=null && !versionNO.equals("")){
				hql += " and params.versionNO like '%"+versionNO.trim()+"%' ";
			}
			if(prjType1!=null && !prjType1.equals("")){
				hql += " and params.prjType1 like '%"+prjType1.trim()+"%' ";
			}
		hql += " order by params.prjName asc,params.versionNO desc ";
		System.out.println(hql);
		Page page=((BaseHibernateTemplate) getHibernateTemplate()).getPage(hql,pageNum,pageSize);
		return page;
	}
	
	public boolean remove(String[] ids){
		boolean flag=false;
		List<ProjectParams> params=new ArrayList<ProjectParams>();
		for (int j = 0; j < ids.length; j++) {	
			params.add(new ProjectParams(ids[j]));
			}
		try{
			this.getHibernateTemplate().deleteAll(params);
			flag=true;
		}catch(Exception e){
			System.out.println("paramsDao delete error!!!!!!!!!!");
		}
		return flag;
	}
	
	public ProjectParams getParamsByPrjVersion(String prjName,String versionNO){
		ProjectParams parm=new ProjectParams();
		String hql = "FROM ProjectParams params WHERE 1=1 ";
		if(prjName!=null && !prjName.equals("")){
			hql += " and params.prjName = '"+prjName.trim()+"' ";
		}
		if(versionNO!=null&&!versionNO.equals("")){
			hql += " and params.versionNO = '"+versionNO.trim()+"' ";
		}
		List<ProjectParams> params=this.getHibernateTemplate().find(hql);
		if(params.size()!=0){
			parm=params.get(0);
		}
		return parm;
	}

	public boolean isExist(String prjName, String versionNO) {
		boolean flag=true;
		String hql = "FROM ProjectParams params WHERE 1=1 ";
		if(prjName!=null && !prjName.equals("")){
			hql += " and params.prjName = '"+prjName.trim()+"' ";
		}
		if(versionNO!=null&&!versionNO.equals("")){
			hql += " and params.versionNO = '"+versionNO.trim()+"' ";
		}
		int  size=this.getHibernateTemplate().find(hql).size();
		if(size!=0)
		{
			flag = false;
		}
		return flag;
	}
	
}
