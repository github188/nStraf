package cn.grgbanking.feeltm.transmgr.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.domain.TransBanknoteSeq;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.transmgr.dao.TransBanknoteSeqDao;
import cn.grgbanking.feeltm.util.DateUtil;
import cn.grgbanking.framework.service.BaseService;
import cn.grgbanking.framework.util.Page;

@SuppressWarnings("unchecked")
@Service
@Transactional
public class TransBanknoteSeqService extends BaseService {
	@Autowired
	private TransBanknoteSeqDao transBanknoteSeqDao;

	public boolean addTransBanknoteSeq(TransBanknoteSeq obj) {
		boolean flag = false;
		try {
			transBanknoteSeqDao.addObject(obj);
			flag = true;
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}

	public boolean deleteTransBanknoteSeq(TransBanknoteSeq obj) {
		boolean flag = false;
		try {
			transBanknoteSeqDao.removeObject(TransBanknoteSeq.class, obj
					.getId());
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}

	public boolean updateTransBanknoteSeq(TransBanknoteSeq obj) {
		boolean flag = false;
		try {
			transBanknoteSeqDao.updateObject(obj);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			throw new NullPointerException();
		}
		return flag;
	}

	@Transactional(readOnly = true)
	public Page getTransBanknoteSeqPage(TransBanknoteSeq obj, int pageNum,
			int pageSize) {
		String sql = "  from TransBanknoteSeq ";
		return transBanknoteSeqDao.getObjectPage(sql, pageNum, pageSize);
	}

	@Transactional(readOnly = true)
	public List getTransBanknoteSeqList(Object[] obj) {
		String sql = "  from TransBanknoteSeq ";
		return transBanknoteSeqDao.getObjectList(sql);
	}

	@Transactional(readOnly = true)
	public TransBanknoteSeq getTransBanknoteSeqObject(String id) {
		return (TransBanknoteSeq) transBanknoteSeqDao.getObject(
				TransBanknoteSeq.class, id);
	}

	@Transactional(readOnly = true)
	public Page getTransBanknoteSeqPage(TransBanknoteSeq trans, int pageNum,
			int pageSize, String beginDateString, String endDateString) {
		String sql = "  from TransBanknoteSeq as trans";

		if (beginDateString != null && !beginDateString.equals("")) {
			beginDateString = DateUtil
					.stringyyyy_MM_ddHH_mm_ssToyyyyMMddHHmmss(beginDateString);
		}

		if (endDateString != null && !endDateString.equals("")) {
			endDateString = DateUtil
					.stringyyyy_MM_ddHH_mm_ssToyyyyMMddHHmmss(endDateString);
		}
		boolean flag = false;

		if (trans.getSeriaNo() != null && !trans.getSeriaNo().equals("")) {
			sql += " where trans.seriaNo like '%" + trans.getSeriaNo() + "%'";
			flag = true;
		}

		if (beginDateString != null && !beginDateString.equals("")) {
			if (flag)
				sql += " and trans.transDate||trans.tranTime >='"
						+ beginDateString + "'";
			else {
				sql += " where trans.transDate||trans.tranTime >='"
						+ beginDateString + "'";
				flag = true;
			}
		}
		if (endDateString != null && !endDateString.equals("")) {
			if (flag)
				sql += " and trans.transDate||trans.tranTime <'"
						+ endDateString + "'";
			else {
				sql += " where trans.transDate||trans.tranTime <'"
						+ endDateString + "'";
				flag = true;
			}
		}
		sql += "  order by trans.transDate||trans.tranTime desc";

		return transBanknoteSeqDao.getObjectPage(sql, pageNum, pageSize);
	}

	@Transactional(readOnly = true)
	public List<TransBanknoteSeq> getTransBanknoteSeqByTranId(String tranId) {
		String sql = " from TransBanknoteSeq as t where t.tranId='" + tranId
				+ "'";
		return (List<TransBanknoteSeq>) transBanknoteSeqDao.getObjectList(sql);
	}
}
