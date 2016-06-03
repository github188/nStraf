package cn.grgbanking.feeltm.prj.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.domain.testsys.PrjPart;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.prj.dao.PrjPartDao;
import cn.grgbanking.framework.service.BaseService;
import cn.grgbanking.framework.util.Page;

@Service("prjPartService")
@Transactional
public class PrjPartService extends BaseService{
	
	@Autowired
	private PrjPartDao prjPartDao;
	
	public boolean add(PrjPart params){
		boolean flag=false;
		try{
			prjPartDao.addObject(params);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	public boolean delete(String[] ids){
		return prjPartDao.remove(ids);
	}
	
	public boolean delete(PrjPart params){
		boolean flag = false;
		try {
			prjPartDao.removeObject(PrjPart.class, params.getId());
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	public boolean update(PrjPart params){
		boolean flag=false;
		try{
			prjPartDao.updateObject(params);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	@Transactional(readOnly = true)
	public PrjPart getPrjPartById(String id){
		return (PrjPart)prjPartDao.getObject(PrjPart.class, id);
	}
	
	@Transactional(readOnly = true)
	public PrjPart getPartByPrjVersion(String prjName,String month){
		return prjPartDao.getPartByParams(prjName,month);
	}
	
	
	@Transactional(readOnly = true)
	public boolean isExist(String prjName,String month){
		return prjPartDao.isExist(prjName,month);
	}
	
	@Transactional(readOnly = true)
	public Page getPage(String prjType,String prjName,String month,int pageNum,int pageSize){
		return prjPartDao.getPage(prjType, prjName, month, pageNum, pageSize);
	}
	
	
}
