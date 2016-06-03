package cn.grgbanking.feeltm.caseanalyse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.domain.testsys.CaseAnalyse;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.caseanalyse.dao.CaseAnalyseDao;
import cn.grgbanking.framework.util.Page;

@Service("caseAnalyseService")
@Transactional
public class CaseAnalyseService {
	@Autowired
	private CaseAnalyseDao caseAnalyseDao;
	public boolean add(CaseAnalyse Case1){
		boolean flag=false;
		try{
			caseAnalyseDao.addObject(Case1);
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
	
	public boolean delete(CaseAnalyse Case1){
		boolean flag = false;
		try {
			caseAnalyseDao.removeObject(CaseAnalyse.class, Case1.getId());
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	

	public boolean update(CaseAnalyse Case1){
		boolean flag=false;
		try{
			caseAnalyseDao.updateObject(Case1);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	public CaseAnalyse getCaseById(String id){
		return (CaseAnalyse)caseAnalyseDao.getObject(CaseAnalyse.class, id);
	}
	//String createDate, String createMan,String updateman,String category,String ano,int pageNum, int pageSize,String raiseEndDate
	@Transactional(readOnly = true)
	public Page getPage(String createDate, String createMan,String summary,String category,int pageNum, int pageSize,String raiseEndDate) {
		return caseAnalyseDao.getPage(createDate,createMan,summary,category,pageNum,pageSize,raiseEndDate);
	}
	
	@Transactional(readOnly=true)
	public String getNextNo(){
		return caseAnalyseDao.getNextNo();
	}
	
}
