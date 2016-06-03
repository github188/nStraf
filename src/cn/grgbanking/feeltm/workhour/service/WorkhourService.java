package cn.grgbanking.feeltm.workhour.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.workhour.dao.WorkhourDao;
import cn.grgbanking.feeltm.workhour.domain.WorkhourInfo;
import cn.grgbanking.framework.service.BaseService;

@Service("workhourService")
@Transactional
public class WorkhourService extends BaseService {
	@Autowired
	private WorkhourDao workhourDao;

	@Transactional(readOnly = true)
	public List getSummaryPage(WorkhourInfo info) {
		return workhourDao.getSummaryPage(info);
	}
}
