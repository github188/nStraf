package cn.grgbanking.feeltm.signrecord.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.signrecord.dao.NoAttendanceDataDao;
import cn.grgbanking.framework.service.BaseService;
import cn.grgbanking.framework.util.Page;

@Service("noAttendanceDataService")
@Transactional
public class NoAttendanceDataService extends BaseService {
	@Autowired
	private NoAttendanceDataDao noAttendanceDataDao;
	
	@Transactional(readOnly = true)
	public Page getPage(int pageNum,int pageSize,UserModel userModel,String year,String month,String type,String prjname){
		return noAttendanceDataDao.getPage(pageNum, pageSize, userModel,year,month,type,prjname);
	}
}
