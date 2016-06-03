package cn.grgbanking.feeltm.report.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.domain.testsys.Behavior;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.report.dao.BehaviorDao;
import cn.grgbanking.feeltm.report.domain.ReportDayInfo;
import cn.grgbanking.framework.util.Page;

@Service("behaviorService")
@Transactional
public class BehaviorService {
	@Autowired
	private BehaviorDao behaviorDao;
	
	public boolean add(Behavior behavior){
		boolean flag=false;
		try{
			behaviorDao.addObject(behavior);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	public boolean delete(String[] ids){
		return behaviorDao.remove(ids);
	}
	
	public boolean delete(Behavior behavior){
		boolean flag = false;
		try {
			behaviorDao.removeObject(Behavior.class, behavior.getId());
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	public boolean update(Behavior behavior){
		boolean flag=false;
		try{
			behaviorDao.updateObject(behavior);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	@Transactional(readOnly = true)
	public Behavior getBehaviorById(String id){
		return (Behavior)behaviorDao.getObject(Behavior.class, id);
	}
	
	@Transactional(readOnly = true)
	public Page getPage(ReportDayInfo info, int pageNum, int pageSize) {
		return behaviorDao.getPage(info, pageNum, pageSize);
	}
	
}
