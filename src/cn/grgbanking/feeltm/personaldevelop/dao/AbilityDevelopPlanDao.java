package cn.grgbanking.feeltm.personaldevelop.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.domain.testsys.AbilityDevelopPlan;
import cn.grgbanking.framework.dao.BaseDao;

@Repository("abilityDevelopPlanDao")
public class AbilityDevelopPlanDao extends BaseDao<AbilityDevelopPlan>{
	
	public boolean remove(String[] ids){
		boolean flag=false;
		List<AbilityDevelopPlan> configs=new ArrayList<AbilityDevelopPlan>();
		for (int j = 0; j < ids.length; j++) {	
				configs.add(new AbilityDevelopPlan(ids[j]));
			}
		try{
			this.getHibernateTemplate().deleteAll(configs);
			flag=true;
		}catch(Exception e){
			System.out.println("AbilityDevelopPlan delete error!!!!!!!!!!");
		}
		return flag;
	}
}
