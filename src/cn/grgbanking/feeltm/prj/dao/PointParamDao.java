package cn.grgbanking.feeltm.prj.dao;

import java.util.List;
import java.util.ArrayList;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.domain.testsys.StandardDic;
import cn.grgbanking.framework.dao.BaseDao;

@Repository("pointDao")
public class PointParamDao extends BaseDao<StandardDic>{
	public List<StandardDic>  getPointParamsByCategory(String category){
		List<StandardDic> dics=new ArrayList<StandardDic>();
	    if(category!=null&&!category.equals("")){
	    	String hql="from StandardDic dic where dic.category='"+category+"' order by dic.fixOrder";
	    	dics=this.getHibernateTemplate().find(hql);
	    }
	    return dics;
	}
	
	
}
