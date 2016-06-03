package cn.grgbanking.feeltm.loglistener.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.grgbanking.feeltm.loglistener.dao.WarnInfoDao;
import cn.grgbanking.feeltm.loglistener.domain.WarnInfo;
import cn.grgbanking.framework.service.BaseService;

@Service
public class WarnInfoService extends BaseService {

	@Autowired
	private WarnInfoDao dao;
	

	/**
	 * 保存
	 * @param logListener
	 */
	public void save(WarnInfo warnInfo) {
		dao.addObject(warnInfo);
	}

}
