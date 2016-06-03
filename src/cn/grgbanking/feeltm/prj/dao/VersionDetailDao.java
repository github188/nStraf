package cn.grgbanking.feeltm.prj.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.domain.testsys.VersionDetail;
import cn.grgbanking.framework.dao.BaseDao;

@Repository("versionDetailDao")
public class VersionDetailDao extends BaseDao<VersionDetail>{
	
	public List<VersionDetail> getList(String prjName,String staticMonth){
		String hql = "FROM VersionDetail part WHERE 1=1 ";
		if(prjName!=null && !prjName.equals("")){
			hql += " and part.prjName like '%"+prjName.trim()+"%' ";
		}
		if(staticMonth!=null && !staticMonth.equals("")){
			hql += " and part.staticMonth = '"+staticMonth.trim()+"' ";
		}
	hql += " order by part.versionNO ";
	System.out.println(hql);
	List<VersionDetail> ps=this.getHibernateTemplate().find(hql);
	return ps;
	}
	
	public void addAll(List<VersionDetail> ds){
		this.getHibernateTemplate().saveOrUpdateAll(ds);
	}
	
}
