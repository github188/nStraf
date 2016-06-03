package cn.grgbanking.feeltm.resource.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.domain.testsys.DeviceConfig;
import cn.grgbanking.feeltm.domain.testsys.Server;
import cn.grgbanking.framework.dao.BaseDao;

@Repository("deviceConfigDao")
public class DeviceConfigDao extends BaseDao<DeviceConfig>{
	
	public boolean remove(String[] ids){
		boolean flag=false;
		List<DeviceConfig> configs=new ArrayList<DeviceConfig>();
		for (int j = 0; j < ids.length; j++) {	
				configs.add(new DeviceConfig(ids[j]));
			}
		try{
			this.getHibernateTemplate().deleteAll(configs);
			flag=true;
		}catch(Exception e){
			System.out.println("configs delete error!!!!!!!!!!");
		}
		return flag;
	}
}
