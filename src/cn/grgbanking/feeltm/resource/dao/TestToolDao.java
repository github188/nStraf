package cn.grgbanking.feeltm.resource.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.domain.testsys.TestTool;
import cn.grgbanking.framework.dao.BaseDao;
import cn.grgbanking.framework.dao.hibernate3.BaseHibernateTemplate;
import cn.grgbanking.framework.util.Page;

@Repository("toolDao")
public class TestToolDao extends BaseDao<TestTool>{
	
	public boolean remove(String[] ids){
		boolean flag=false;
		List<TestTool> tools=new ArrayList<TestTool>();
		for (int j = 0; j < ids.length; j++) {	
				tools.add(new TestTool(ids[j]));
			}
		try{
			this.getHibernateTemplate().deleteAll(tools);
			flag=true;
		}catch(Exception e){
			System.out.println("TestToolDao delete error!!!!!!!!!!");
		}
		return flag;
	}
	
	public Page getPage(String toolName,String assort,int pageNum,int pageSize)
	{
		String hql = "FROM TestTool tool WHERE 1=1 ";
			if(toolName!=null && !toolName.equals("")){
				hql += " and tool.toolName like '%"+toolName.trim()+"%' ";
			}
			if(assort!=null && !assort.equals("")){
				hql += " and tool.assort = '"+assort.trim()+"' ";
			}
		hql += " order by tool.toolName ";
		System.out.println(hql);
		Page page=((BaseHibernateTemplate) getHibernateTemplate()).getPage(hql,pageNum,pageSize);
		return page;
	}
}
