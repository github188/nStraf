package cn.grgbanking.feeltm.resource.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.domain.testsys.Server;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.resource.dao.ServerDao;
import cn.grgbanking.framework.service.BaseService;
import cn.grgbanking.framework.util.Page;

@Service("serverService")
@Transactional
public class ServerService extends BaseService{
	@Autowired
	private ServerDao serverDao;
	
	public boolean add(Server server){
		boolean flag=false;
		try{
			serverDao.addObject(server);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	public boolean delete(String[] ids){
		return serverDao.remove(ids);
	}
	
	public boolean delete(Server server){
		boolean flag = false;
		try {
			serverDao.removeObject(Server.class, server.getId());
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	public boolean update(Server server){
		boolean flag=false;
		try{
			serverDao.updateObject(server);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	@Transactional(readOnly = true)
	public Server getServerById(String id){
		return (Server)serverDao.getObject(Server.class, id);
	}
	
	@Transactional(readOnly = true)
	public Page getPage(String deviceName,String netIP, int pageNum, int pageSize) {
		return serverDao.getPage(deviceName, netIP, pageNum, pageSize);
	}
	
}
