package cn.grgbanking.feeltm.rule.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.domain.AuditInfo;
import cn.grgbanking.feeltm.domain.AuditLog;
import cn.grgbanking.feeltm.domain.BlackRegulation;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.rule.dao.BlackRegulationDao;
import cn.grgbanking.framework.service.BaseService;
import cn.grgbanking.framework.util.Page;

@SuppressWarnings("unchecked")
@Service
@Transactional
public class BlackRegulationService extends BaseService {
	@Autowired
	private BlackRegulationDao blackRegulationDao;

	public boolean addBlackRegulation(BlackRegulation obj) {
		boolean flag = false;
		try {
			blackRegulationDao.addObject(obj);
			flag = true;
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}

	public boolean addAuditBlackRegulation(BlackRegulation obj, AuditInfo obj2,
			AuditLog obj3) {
		boolean flag = false;
		try {
			blackRegulationDao.addObject(obj);
			blackRegulationDao.addObject(obj2);
			blackRegulationDao.addObject(obj3);
			flag = true;
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}

	public boolean deleteBlackRegulation(BlackRegulation obj) {
		boolean flag = false;

		// 将黑名单规则设置为撤销状态，并不在数据中删除
		obj.setRegulationStatus("10");
		try {
			// blackRegulationDao.removeObject(BlackRegulation.class,
			// obj.getId());
			blackRegulationDao.updateObject(obj);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}

	public boolean updateBlackRegulation(BlackRegulation obj) {
		boolean flag = false;
		try {
			blackRegulationDao.updateObject(obj);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			throw new NullPointerException();
		}
		return flag;
	}

	@Transactional(readOnly = true)
	public Page getBlackRegulationPage(BlackRegulation obj, int pageNum,
			int pageSize, String createName) {
		String sql = "  from BlackRegulation b where b.createName = '"
				+ createName + "' and (b.regulationStatus not in( '10' ,'8'))";
		boolean flag = false;
		if (obj.getRegulation() != null && !obj.getRegulation().equals(""))
			sql += " and b.regulation like '%" + obj.getRegulation() + "%'";
		if (obj.getMoneyType() != null && !obj.getMoneyType().equals(""))
			sql += " and b.moneyType = '" + obj.getMoneyType() + "'";
		
		if (obj.getReguStatus() != null
				&& !obj.getReguStatus().equals("")) {
			sql += " and b.reguStatus = '" + obj.getReguStatus()
					+ "'";
			flag = true;
		} else if(obj.getReguStatus() != null
				&& obj.getReguStatus().equals("")) {
			flag = true;
		}
		if(!flag) {
			sql += " and b.reguStatus='1' ";
		}
		sql += " order by b.createDate desc";

		return blackRegulationDao.getObjectPage(sql, pageNum, pageSize);
	}

	@Transactional(readOnly = true)
	public List getBlackRegulationList(Object[] obj) {
		String sql = "  from BlackRegulation ";
		return blackRegulationDao.getObjectList(sql);
	}

	//得到审核通过的黑名单列表
	@Transactional(readOnly = true)
	public List<BlackRegulation> getBlackRegulationList() {
		String sql = " from BlackRegulation blackRegulation where blackRegulation.regulationStatus='9'";
		return blackRegulationDao.getObjectList(sql);
	}

	@Transactional(readOnly = true)
	public BlackRegulation getBlackRegulationObject(String id) {
		return (BlackRegulation) blackRegulationDao.getObject(
				BlackRegulation.class, id);
	}

	@Transactional(readOnly = true)
	public List<BlackRegulation> getBlackRegulationByApplyId(String applyIds) {
		String hql = " from BlackRegulation blackRegulation where blackRegulation.applyId='"
				+ applyIds + "'";
		return blackRegulationDao.getObjectList(hql);
	}
}
