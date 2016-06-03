package cn.grgbanking.feeltm.tmlInfo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.domain.TmlInfo;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.tmlInfo.dao.TmlInfoDao;
import cn.grgbanking.framework.service.BaseService;
import cn.grgbanking.framework.util.Page;

@SuppressWarnings("unchecked")
@Service
@Transactional
public class TmlInfoService extends BaseService {
	@Autowired
	private TmlInfoDao tmlInfoDao;

	public boolean addTmlInfo(TmlInfo obj) {
		boolean flag = false;
		try {
			tmlInfoDao.addObject(obj);
			flag = true;
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}

	public boolean deleteTmlInfo(TmlInfo obj) {
		boolean flag = false;
		try {
			tmlInfoDao.removeObject(TmlInfo.class, obj.getId());
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}

	public boolean updateTmlInfo(TmlInfo obj) {
		boolean flag = false;
		try {
			tmlInfoDao.updateObject(obj);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			throw new NullPointerException();
		}
		return flag;
	}

	public Page findTmlInfoPage(TmlInfo form, int pageNum, int pageSize) {
		return tmlInfoDao.findTmlInfoPage(form, pageNum, pageSize);
	}

	@Transactional(readOnly = true)
	public Page getTmlInfoPage(Object[] obj, int pageNum, int pageSize) {
		String sql = "  from TmlInfo ";
		return tmlInfoDao.getObjectPage(sql, pageNum, pageSize);
	}

	@Transactional(readOnly = true)
	public List getTmlInfoList(Object[] obj) {
		String sql = "  from TmlInfo ";
		return tmlInfoDao.getObjectList(sql);
	}

	@Transactional(readOnly = true)
	public TmlInfo getTmlInfoObject(String id) {
		return (TmlInfo) tmlInfoDao.getObject(TmlInfo.class, id);
	}

}
