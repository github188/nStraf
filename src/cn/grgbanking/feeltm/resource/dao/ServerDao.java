package cn.grgbanking.feeltm.resource.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.domain.testsys.Server;
import cn.grgbanking.framework.dao.BaseDao;
import cn.grgbanking.framework.dao.hibernate3.BaseHibernateTemplate;
import cn.grgbanking.framework.util.Page;

@Repository("serverDao")
public class ServerDao extends BaseDao<Server>{
	public Page getPage(String deviceName,String netIP,int pageNum,int pageSize)
	{
		String hql = "FROM Server server WHERE 1=1 ";
			if(deviceName!=null && !deviceName.equals("")){
				hql += " and server.deviceName like '%"+deviceName.trim()+"%' ";
			}
			if(netIP!=null && !netIP.equals("")){
				hql += " and server.netIP like '%"+netIP.trim()+"%' ";
			}
		hql += " order by server.deviceName ";
		System.out.println(hql);
		Page page=((BaseHibernateTemplate) getHibernateTemplate()).getPage(hql,pageNum,pageSize);
		return page;
	}
	
	public boolean remove(String[] ids){
		boolean flag=false;
		List<Server> servers=new ArrayList<Server>();
		for (int j = 0; j < ids.length; j++) {	
				servers.add(new Server(ids[j]));
			}
		try{
			this.getHibernateTemplate().deleteAll(servers);
			flag=true;
		}catch(Exception e){
			System.out.println("ServerDao delete error!!!!!!!!!!");
		}
		return flag;
	}
}
