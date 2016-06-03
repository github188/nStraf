package cn.grgbanking.feeltm.attendance.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.attendance.dao.WorkSummaryDao;
import cn.grgbanking.framework.service.BaseService;
import cn.grgbanking.framework.util.Page;

@Service("workSummaryService")
@Transactional
public class WorkSummaryService extends BaseService{
	@Autowired
	private WorkSummaryDao workSummaryDao;
	
	@Transactional(readOnly = true)
	public Page getPage(String start,String end,int pageNum,int pageSize){
		return workSummaryDao.getPage(start, end, pageNum, pageSize);
	}
	
}
