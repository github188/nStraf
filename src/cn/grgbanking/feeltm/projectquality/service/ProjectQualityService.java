package cn.grgbanking.feeltm.projectquality.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.domain.testsys.ProjectDB;
import cn.grgbanking.feeltm.domain.testsys.ProjectQuality;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.projectquality.dao.ProjectQualityDao;
import cn.grgbanking.framework.service.BaseService;
import cn.grgbanking.framework.util.Page;

@Service("projectQualityService")
@Transactional
public class ProjectQualityService extends BaseService{
	@Autowired
	private ProjectQualityDao projectQualityDao;
	public boolean add(ProjectQuality projectQuality){
		boolean flag=false;
		try{
			projectQualityDao.addObject(projectQuality);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	@Transactional(readOnly = true)
	public List<ProjectDB> getDB(){
		return projectQualityDao.getDB();
	}
	
	@Transactional(readOnly = true)
	public String getUidByLevel(String groupName){
		return projectQualityDao.getUidByLevel(groupName);
	}
	
	public boolean delete(ProjectQuality ProjectQuality){
		boolean flag = false;
		try {
			projectQualityDao.removeObject(ProjectQuality.class, ProjectQuality.getId());
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	

	public boolean update(ProjectQuality ProjectQuality){
		boolean flag=false;
		try{
			projectQualityDao.updateObject(ProjectQuality);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	public ProjectQuality getCaseById(String id){
		return (ProjectQuality)projectQualityDao.getObject(ProjectQuality.class, id);
	}
	
	public boolean upEdit_lock(String month_date){
		return projectQualityDao.upEdit_lock(month_date);
	}
	//String createDate, String createMan,String updateman,String category,String ano,int pageNum, int pageSize,String raiseEndDate
	@Transactional(readOnly = true)
	public Page getPage(String querydate,String projectname,int pageNum, int pageSize) {
		return projectQualityDao.getPage(querydate,projectname,pageNum,pageSize);
	}
	
	
	@Transactional(readOnly = true)
	public List<Object> getNames(String groupName) {
		return projectQualityDao.getUsernamesByGroup(groupName);
	}
	
	
	@Transactional(readOnly=true)
	public String getNextNo(){
		return projectQualityDao.getNextNo();
	}
	
	@Transactional(readOnly=true)
	public String getUserlevel(String username){
		return projectQualityDao.getUserlevel(username);
	}
	
	@Transactional(readOnly = true)
	public List<String> getAllNames(){
		
		return projectQualityDao.getAllNames();
	}
	
	@Transactional(readOnly = true)
	public List<ProjectQuality> getProjectQualityByPno(String pno) {
		return projectQualityDao.selectProjectQuality(pno);
	}
	
}
