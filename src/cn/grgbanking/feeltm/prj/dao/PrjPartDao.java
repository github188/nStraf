package cn.grgbanking.feeltm.prj.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.domain.testsys.PrjPart;
import cn.grgbanking.feeltm.util.StringUtil;
import cn.grgbanking.framework.dao.BaseDao;
import cn.grgbanking.framework.dao.hibernate3.BaseHibernateTemplate;
import cn.grgbanking.framework.util.Page;

@Repository("prjPartDao")
public class PrjPartDao extends BaseDao<PrjPart>{
	
	public Page getPage(String prjType1,String prjName,String month,int pageNum,int pageSize)
	{
		String hql = "FROM PrjPart part WHERE 1=1 ";
			if(prjName!=null && !prjName.equals("")){
				hql += " and part.prjName like '%"+prjName.trim()+"%' ";
			}
			if(prjType1!=null && !prjType1.equals("")){
				hql += " and part.prjType like '%"+prjType1.trim()+"%' ";
			}
			if(month!=null && !month.equals("")){
				hql += " and part.staticMonth = ’"+month.trim()+"' ";			
			}
		hql += " order by part.prjName asc,part.staticMonth desc ";
		System.out.println(hql);
		Page page=((BaseHibernateTemplate) getHibernateTemplate()).getPage(hql,pageNum,pageSize);
		return page;
	}
	
	public List<PrjPart> getList(String prjName,String start,String end){
		String hql = "FROM PrjPart part WHERE 1=1 ";
		if(prjName!=null && !prjName.equals("")){
			hql += " and part.prjName like '%"+prjName.trim()+"%' ";
		}
		if(start!=null && !start.equals("")){
			hql += " and part.staticMonth >= '"+start.trim()+"' ";
		}
		if(end!=null && !end.equals("")){
			hql += " and part.staticMonth <= '"+end.trim()+"' ";
		}
	hql += " order by part.staticMonth ";
	System.out.println(hql);
	List<PrjPart> ps=this.getHibernateTemplate().find(hql);
	for(PrjPart p:ps){
		p.setProcessQualityStr(StringUtil.point2Level.get(p.getProcessQuality()));
		p.setProjectQualityStr(StringUtil.point2Level.get(p.getProjectQuality()));
		p.setProgressStr(StringUtil.point2Progress.get(p.getProgress()));
	}
	return ps;
	}
	
	/**
	 * 取得oracle中所有项目类型相同的项目统计数据，根据时间范围
	 * @param prjType
	 * @param start
	 * @param end
	 * @return
	 */
	public List<PrjPart> getAllList(String prjType,String start,String end){
		String hql = "FROM PrjPart part WHERE 1=1 ";
		if(prjType!=null && !prjType.equals("")){
			hql += " and part.prjType = '"+prjType.trim()+"' ";
		}
		if(start!=null && !start.equals("")){
			hql += " and part.start = '"+start.trim()+"' ";
		}
		if(end!=null && !end.equals("")){
			hql += " and part.staticMonth = '"+end.trim()+"' ";
		}
	hql += " order by part.staticMonth ";
	System.out.println(hql);
	List<PrjPart> ps=this.getHibernateTemplate().find(hql);
	for(PrjPart p:ps){
		p.setProcessQualityStr(StringUtil.point2Level.get(p.getProcessQuality()));
		p.setProgressStr(StringUtil.point2Progress.get(p.getProgress()));
		p.setProjectQualityStr(StringUtil.point2Level.get(p.getProjectQuality()));
	}
	return ps;
	}
	
	public boolean remove(String[] ids){
		boolean flag=false;
		ArrayList<PrjPart> params=new ArrayList<PrjPart>();
		for (int j = 0; j < ids.length; j++) {	
			params.add(new PrjPart(ids[j]));
			}
		try{
			this.getHibernateTemplate().deleteAll(params);
			flag=true;
		}catch(Exception e){
			System.out.println("paramsDao delete error!!!!!!!!!!");
		}
		return flag;
	}
	
	

	public boolean isExist(String prjName, String month) {
		boolean flag=true;
		String hql = "FROM PrjPart part WHERE 1=1 ";
		if(prjName!=null && !prjName.equals("")){
			hql += " and part.prjName = '"+prjName.trim()+"' ";
		}
		if(month!=null&&!month.equals("")){
			hql += " and params.staticMonth = '"+month.trim()+"' ";
		}
		int  size=this.getHibernateTemplate().find(hql).size();
		if(size!=0)
		{
			flag = false;
		}
		return flag;
	}
	
	
	public PrjPart getPartByParams(String prjName,String month){
		PrjPart part=null;
		Object[] os={prjName,month};
		String hql="FROM PrjPart part WHERE part.prjName=? and part.staticMonth=?";
		List<PrjPart> list=this.getHibernateTemplate().find(hql, os);
		if(list!=null&&list.size()>0){
			part=list.get(0);
		}
		return part;
	}
	
	
	

	
	
}
