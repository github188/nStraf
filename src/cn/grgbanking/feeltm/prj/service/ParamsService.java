package cn.grgbanking.feeltm.prj.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.domain.testsys.ProjectParams;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.prj.dao.ParamsDao;
import cn.grgbanking.framework.service.BaseService;
import cn.grgbanking.framework.util.Page;

@Service("paramsService")
@Transactional
public class ParamsService extends BaseService{
	@Autowired
	private ParamsDao paramsDao;
	
	public boolean add(ProjectParams params){
		boolean flag=false;
		try{
			paramsDao.addObject(params);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	public boolean delete(String[] ids){
		return paramsDao.remove(ids);
	}
	
	public boolean delete(ProjectParams params){
		boolean flag = false;
		try {
			paramsDao.removeObject(ProjectParams.class, params.getId());
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	public boolean update(ProjectParams params){
		boolean flag=false;
		try{
			paramsDao.updateObject(params);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	@Transactional(readOnly = true)
	public ProjectParams getParamsById(String id){
		return (ProjectParams)paramsDao.getObject(ProjectParams.class, id);
	}
	
	@Transactional(readOnly = true)
	public ProjectParams getParamsByPrjVersion(String prjName,String versionNO){
		return paramsDao.getParamsByPrjVersion(prjName,versionNO);
	}
	
	
	@Transactional(readOnly = true)
	public Page getPage(String prjName,String versionNO,String prjType1,int pageNum,int pageSize) {
		return paramsDao.getPage(prjName, versionNO,prjType1, pageNum, pageSize);
	}
	
	
	@Transactional(readOnly = true)
	public boolean isExist(String prjName,String versionNO){
		return paramsDao.isExist(prjName,versionNO);
	}
	
	
	
}
