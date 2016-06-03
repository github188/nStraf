package cn.grgbanking.feeltm.rule.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.domain.RegulationDeliver;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.rule.dao.RegulationDeliverDao;
import cn.grgbanking.framework.service.BaseService;
import cn.grgbanking.framework.util.Page;

@SuppressWarnings("unchecked")
@Service
@Transactional
public class RegulationDeliverService extends BaseService {
	@Autowired
	private RegulationDeliverDao regulationDeliverDao;

	public boolean addRegulationDeliver(RegulationDeliver obj) {
		boolean flag = false;
		try {
			regulationDeliverDao.addObject(obj);
			flag = true;
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}

	public boolean deleteRegulationDeliver(RegulationDeliver obj) {
		boolean flag = false;
		try {
			regulationDeliverDao.removeObject(RegulationDeliver.class, obj
					.getId());
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}

	public boolean updateRegulationDeliver(RegulationDeliver obj) {
		boolean flag = false;
		try {
			regulationDeliverDao.updateObject(obj);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			throw new NullPointerException();
		}
		return flag;
	}

	@Transactional(readOnly = true)
	public Page getRegulationDeliverPage(Object[] obj, int pageNum, int pageSize) {
		String sql = "  from RegulationDeliver o where o.status='" + "1'";
		return regulationDeliverDao.getObjectPage(sql, pageNum, pageSize);
	}

	@Transactional(readOnly = true)
	public Page getRegulationDeliverPage(RegulationDeliver regulationDeliver,
			int pageNum, int pageSize) {
		String sql = " from RegulationDeliver r ";
		boolean flag = false;
		String termid = regulationDeliver.getTermid();
		String type = regulationDeliver.getType();
		String status = regulationDeliver.getStatus();
		if (termid != null && !"".equals(termid)) {
			if (flag) {
				sql += "and r.termid ='" + termid + "' ";
			} else {
				sql += "where r.termid = '" + termid + "' ";
			}
			flag = true;
		}
		if (type != null && !"".equals(type)) {
			if (flag) {
				sql += "and r.type='" + type + "' ";
			} else {
				sql += "where r.type ='" + type + "' ";
			}
			flag = true;
		}
		if (status != null && !"".equals(status)) {
			if (flag) {
				sql += "and r.status = '" + status + "' ";
			} else {
				sql += "where r.status ='" + status + "' ";
			}
			flag = true;
		}
		sql += "order by r.date desc ";
		return regulationDeliverDao.getObjectPage(sql, pageNum, pageSize);
	}

	@Transactional(readOnly = true)
	public List getRegulationDeliverList(Object[] obj) {
		String sql = "  from RegulationDeliver ";
		return regulationDeliverDao.getObjectList(sql);
	}

	@Transactional(readOnly = true)
	public RegulationDeliver getRegulationDeliverObject(String id) {
		return (RegulationDeliver) regulationDeliverDao.getObject(
				RegulationDeliver.class, id);
	}

	@Transactional(readOnly = true)
	public List<RegulationDeliver> getRegulationDeliListByApplyId(String applyId) {
		String sql = " from RegulationDeliver o where o.applyId='" + applyId
				+ "'";
		return regulationDeliverDao.getObjectList(sql);
	}

	@Transactional(readOnly = true)
	public List<RegulationDeliver> getRegulationDeliverListByApplyId(
			String applyId) {
		String sql = " from RegulationDeliver o where o.applyId='" + applyId
				+ "' and o.version != null";
		return regulationDeliverDao.getObjectList(sql);
	}

	/**
	 * 根据终端编号得到规则下发列表，并按照规则版本排序
	 * @param termid
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<RegulationDeliver> getRegulationDeliverListByTermid(
			String termid) {
		String sql = " from RegulationDeliver o where o.termid='" + termid
				+ "' and o.version!=null order by o.version desc";
		return regulationDeliverDao.getObjectList(sql);
	}
}
