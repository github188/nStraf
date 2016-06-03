package cn.grgbanking.feeltm.autoOa.service;

import java.util.List;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.domain.testsys.AutoOa;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.autoOa.dao.AutoOaDao;
import cn.grgbanking.framework.util.Page;

@Service("autoOaService")
@Transactional
public class AutoOaService {
	@Autowired
	private AutoOaDao autoOaDao;
	public boolean add(AutoOa Case1){
		boolean flag=false;
		try{
			autoOaDao.addObject(Case1);
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
	
	public boolean delete(AutoOa Case1){
		boolean flag = false;
		try {
			autoOaDao.removeObject(AutoOa.class, Case1.getId());
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	

	public boolean update(AutoOa Case1){
		boolean flag=false;
		try{
			autoOaDao.updateObject(Case1);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	public AutoOa getCaseById(String id){
		return (AutoOa)autoOaDao.getObject(AutoOa.class, id);
	}
	//String createDate, String createMan,String updateman,String category,String ano,int pageNum, int pageSize,String raiseEndDate
	@Transactional(readOnly = true)
	public Page getPage(String createMan,String description, String sendCycle,String strat,int pageNum, int pageSize,String end) {
		return autoOaDao.getPage(createMan,description,sendCycle,strat,pageNum,pageSize,end);
	}
	
	@Transactional(readOnly = true)
	public List<String> getAllNames(){
		
		return autoOaDao.getAllNames();
	}
	
	

	
}
