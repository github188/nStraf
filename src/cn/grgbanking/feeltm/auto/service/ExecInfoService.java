package cn.grgbanking.feeltm.auto.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.auto.dao.AutoDao;
import cn.grgbanking.feeltm.auto.dao.ExecInfoDao;
import cn.grgbanking.feeltm.domain.testsys.ExecInfo;
import cn.grgbanking.feeltm.domain.testsys.Overtime;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.framework.util.Page;


@Service("execInfoService")
@Transactional
public class ExecInfoService {
	
	@Autowired
	private ExecInfoDao execInfoDao;
	
	public boolean add(ExecInfo execInfo){
		boolean flag=false;
		try{
			execInfoDao.addObject(execInfo);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	public boolean delete(String[] ids){
		return execInfoDao.remove(ids);
	}
	
	public boolean delete(ExecInfo execInfo){
		boolean flag = false;
		try {
			execInfoDao.removeObject(ExecInfo.class, execInfo.getId());
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	public boolean update(ExecInfo execInfo){
		boolean flag=false;
		try{
			execInfoDao.updateObject(execInfo);
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
		return (ExecInfo)execInfoDao.getObject(ExecInfo.class, id);
	}
	
	@Transactional(readOnly = true)
	public Page getPage(String start,String end,int pageNum,int pageSize) {
		return execInfoDao.getPage(start,end, pageNum, pageSize);
	}
	
	@Transactional(readOnly = true)
	public String getFile(String id){
		return execInfoDao.getFile(id);
	}
}
