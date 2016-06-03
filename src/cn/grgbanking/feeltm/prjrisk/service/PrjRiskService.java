package cn.grgbanking.feeltm.prjrisk.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.domain.testsys.PrjRisk;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.prjrisk.dao.PrjRiskDao;
import cn.grgbanking.framework.util.Page;

@Service("prjRiskService")
@Transactional
public class PrjRiskService {
	@Autowired
	private PrjRiskDao prjRiskDao;
	public boolean add(PrjRisk prjRisk){
		boolean flag=false;
		try{
			prjRiskDao.addObject(prjRisk);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
/*	public boolean delete(String[] ids){
		return suggestionDao.remove(ids);
	}*/
	
	public boolean delete(PrjRisk prjRisk){
		boolean flag = false;
		try {
			prjRiskDao.removeObject(PrjRisk.class, prjRisk.getId());
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	public boolean update(PrjRisk prjRisk){
		boolean flag=false;
		try{
			prjRiskDao.updateObject(prjRisk);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	public PrjRisk getprjRiskById(String id){
		return (PrjRisk)prjRiskDao.getObject(PrjRisk.class, id);
	}
	
	@Transactional(readOnly = true)
	public Page getPage(String prjname, String summary,String type,String status,String urgent,String pond, Date startDate,Date endDate,
			int pageNum, int pageSize,String createman,UserModel userModel) {
		return prjRiskDao.getPage( prjname,summary,  type, status, urgent, pond,  startDate, endDate,
				 pageNum,  pageSize,createman, userModel);
	}
	
	@Transactional(readOnly=true)
	public String getNextNo(){
		return prjRiskDao.getNextNo();
	}
	
}
