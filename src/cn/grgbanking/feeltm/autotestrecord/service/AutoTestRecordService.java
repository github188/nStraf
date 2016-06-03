package cn.grgbanking.feeltm.autotestrecord.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.autotestrecord.dao.AutoTestRecordDao;
import cn.grgbanking.feeltm.domain.testsys.AutoTestRecord;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.framework.service.BaseService;
import cn.grgbanking.framework.util.Page;

@Service("autoTestRecordService")
@Transactional
public class AutoTestRecordService extends BaseService{
	@Autowired
	public AutoTestRecordDao autoTestRecordDao;
	
	public boolean add(AutoTestRecord autoTestRecord){
		boolean flag=false;
		try{
			autoTestRecordDao.addObject(autoTestRecord);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	public AutoTestRecord getCaseById(String id){
		return (AutoTestRecord)autoTestRecordDao.getObject(AutoTestRecord.class, id);
	}
	
	@Transactional(readOnly = true)
	public List<String> getAllNames(){
		
		return autoTestRecordDao.getAllNames();
	}
	
	public boolean delete(String[] ids){
		return autoTestRecordDao.remove(ids);
	}
	
	public boolean delete(AutoTestRecord autoTestRecord){
		boolean flag = false;
		try {
			autoTestRecordDao.removeObject(AutoTestRecord.class, autoTestRecord.getId());
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	public boolean update(AutoTestRecord testRecord){
		boolean flag=false;
		try{
			autoTestRecordDao.updateObject(testRecord);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	@Transactional(readOnly = true)
	public AutoTestRecord getDetailById(String id){
		return (AutoTestRecord)autoTestRecordDao.getObject(AutoTestRecord.class, id);
	}
	
	@Transactional(readOnly = true)
	public Page getPage(String StartMonth,String EndMonth,String prjName,String version,String testStatus, int pageNum,int pageSize){
		return autoTestRecordDao.getPage(StartMonth,EndMonth,prjName, version, testStatus, pageNum, pageSize);
	}
	
}
