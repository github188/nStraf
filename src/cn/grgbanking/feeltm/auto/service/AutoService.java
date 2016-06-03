package cn.grgbanking.feeltm.auto.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.auto.dao.AutoDao;
import cn.grgbanking.feeltm.auto.domain.AutoListInfo;
import cn.grgbanking.feeltm.domain.testsys.ExecInfo;
import cn.grgbanking.feeltm.domain.testsys.Overtime;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.framework.util.Page;


@Service("autoService")
@Transactional
public class AutoService {
	
	@Autowired
	private AutoDao autoDao;
	
	public boolean add(AutoListInfo autoListInfo){
		boolean flag=false;
		try{
			autoDao.addObject(autoListInfo);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	public boolean delete(String[] ids){
		return autoDao.remove(ids);
	}
	
	public boolean update(AutoListInfo autoListInfo){
		boolean flag=false;
		try{
			autoDao.updateObject(autoListInfo);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	@Transactional(readOnly = true)
	public ExecInfo getExecInfoById(String id){
		return (ExecInfo)autoDao.getObject(ExecInfo.class, id);
	}
	
	@Transactional(readOnly = true)
	public Page getPage(String start,String end,int pageNum,int pageSize,String projName) {
		return autoDao.getPage(start,end, pageNum, pageSize,projName);
	}
	
	@Transactional(readOnly = true)
	public String getFile(String id){
		return autoDao.getFile(id);
	}
	
	@Transactional(readOnly = true)
	public AutoListInfo getInfo(String id){
		return autoDao.getInfo(id);
	}
}
