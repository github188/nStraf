package cn.grgbanking.feeltm.logmgr.service;


import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.logmgr.dao.OperLogDao;
import cn.grgbanking.framework.service.BaseService;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.feeltm.domain.SysOperLog;

@Service
@Transactional
public class OperLogService  extends BaseService{
	@Autowired
	private OperLogDao operLogDao;
	
	public Page getPage(HttpServletRequest request,SysOperLog operLog,int pageNum, int pageSize){
		return operLogDao.getPage(request,operLog,pageNum, pageSize);
	}
	
	public int deleteInfo(String[] chkDelete){
		return operLogDao.deleteInfo(chkDelete);
	}

	public OperLogDao getOperLogDAO() {
		return operLogDao;
	}

	public void setOperLogDAO(OperLogDao operLogDAO) {
		this.operLogDao = operLogDAO;
	}
}