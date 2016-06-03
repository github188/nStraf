package cn.grgbanking.feeltm.personaldevelop.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.domain.testsys.AbilityLog;
import cn.grgbanking.framework.dao.BaseDao;

@Repository("abilityLogDao")
public class AbilityLogDao extends BaseDao<AbilityLog>{
	/**
	 * 增加一个版本修改日志记录
	 */
	
	public boolean remove(String[] ids){
		boolean flag=false;
		List<AbilityLog> versions=new ArrayList<AbilityLog>();
		for (int j = 0; j < ids.length; j++) {	
			versions.add(new AbilityLog(ids[j]));
			}
		try{
			this.getHibernateTemplate().deleteAll(versions);
			flag=true;
		}catch(Exception e){
			System.out.println("PostRemove delete error!!!!!!!!!!");
		}
		return flag;
	}
}
