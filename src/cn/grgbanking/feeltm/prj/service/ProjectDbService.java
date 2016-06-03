package cn.grgbanking.feeltm.prj.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.domain.testsys.ProjectDB;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.prj.dao.ProjectDbDao;
import cn.grgbanking.framework.service.BaseService;
import cn.grgbanking.framework.util.Page;

@Service("dbService")
@Transactional
public class ProjectDbService extends BaseService{
	
	@Autowired
	private ProjectDbDao dbDao;

	
	public boolean add(ProjectDB params){
		boolean flag=false;
		try{
			dbDao.addObject(params);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	public boolean delete(String[] ids){
		return dbDao.remove(ids);
	}
	
	public boolean delete(ProjectDB params){
		boolean flag = false;
		try {
			dbDao.removeObject(ProjectDB.class, params.getId());
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	public boolean update(ProjectDB params){
		boolean flag=false;
		try{
			dbDao.updateObject(params);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	@Transactional(readOnly = true)
	public ProjectDB getPrjDbById(String id){
		return (ProjectDB)dbDao.getObject(ProjectDB.class, id);
	}
	
	@Transactional(readOnly = true)
	public Page getPage(String prjName,int pageNum,int pageSize) {
		return dbDao.getPage(prjName,pageNum, pageSize);
	}
	
	@Transactional(readOnly = true)
	public ProjectDB getDB(String prjName){
		return dbDao.getDB(prjName);
	}
	
	
	@Transactional(readOnly = true)
	public List<String> getPrjTypes(){
		return dbDao.findAll();
	}
	
}
