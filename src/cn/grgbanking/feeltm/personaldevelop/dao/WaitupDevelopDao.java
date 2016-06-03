package cn.grgbanking.feeltm.personaldevelop.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.domain.testsys.WaitupDevelop;
import cn.grgbanking.framework.dao.BaseDao;

@Repository("waitupDevelopDao")
public class WaitupDevelopDao extends BaseDao<WaitupDevelop>{
	/**
	 * 增加一个版本修改日志记录
	 */
	
	public boolean remove(String[] ids){
		boolean flag=false;
		List<WaitupDevelop> versions=new ArrayList<WaitupDevelop>();
		for (int j = 0; j < ids.length; j++) {	
			versions.add(new WaitupDevelop(ids[j]));
			}
		try{
			this.getHibernateTemplate().deleteAll(versions);
			flag=true;
		}catch(Exception e){
			System.out.println("WaitupDevelop delete error!!!!!!!!!!");
		}
		return flag;
	}
}
