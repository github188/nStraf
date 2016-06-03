package cn.grgbanking.feeltm.rule.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.audit.dao.AuditInfoDao;
import cn.grgbanking.feeltm.audit.dao.AuditLogDao;
import cn.grgbanking.feeltm.domain.AuditInfo;
import cn.grgbanking.feeltm.domain.AuditLog;
import cn.grgbanking.feeltm.domain.RepeatRegulation;
import cn.grgbanking.feeltm.domain.UsrUsrgrp;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.rule.dao.RepeatRegulationDao;
import cn.grgbanking.feeltm.um.service.UsrUsrgrpDao;
import cn.grgbanking.framework.service.BaseService;
import cn.grgbanking.framework.util.Page;

@SuppressWarnings("unchecked")
@Service
@Transactional
public class RepeatRegulationService extends BaseService {
	@Autowired
	private RepeatRegulationDao repeatRegulationDao;

	@Autowired
	private UsrUsrgrpDao usrUsrgrpDao;

	@Autowired
	private AuditInfoDao auditInfoDao;

	@Autowired
	private AuditLogDao auditLogDao;

	public boolean addRepeatRegulation(RepeatRegulation obj) {
		boolean flag = false;
		try {
			repeatRegulationDao.addObject(obj);
			flag = true;
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}

	public boolean deleteRepeatRegulation(RepeatRegulation obj) {
		boolean flag = false;
		obj.setRegulationStatus("10");
		try {
			repeatRegulationDao.updateObject(obj);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}

	public boolean updateRepeatRegulation(RepeatRegulation obj) {
		boolean flag = false;
		try {
			repeatRegulationDao.updateObject(obj);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			throw new NullPointerException();
		}
		return flag;
	}

	/**
	 * @author Yondy Chow
	 */
	@Transactional(readOnly = true)
	public Page getRepeatRegulationPage(RepeatRegulation obj, int pageNum,
			int pageSize, String createName) {
		boolean flag = false;
		String sql = "  from RepeatRegulation o where o.createName='"
				+ createName + "' and o.regulationStatus!='10' ";
		if (obj.getReguStatus() != null && !obj.getReguStatus().equals("")) {
			sql += " and o.reguStatus = '" + obj.getReguStatus() + "'";
			flag = true;
		} else if(obj.getReguStatus() != null && obj.getReguStatus().equals("")) {
			flag = true;
		}
		
		if(flag)
			sql += " order by o.createDate desc";
		else
			sql += " and o.reguStatus='1' order by o.createDate desc";

		return repeatRegulationDao.getObjectPage(sql, pageNum, pageSize);
	}

	@Transactional(readOnly = true)
	public List getRepeatRegulationList(Object[] obj) {
		String sql = "  from RepeatRegulation ";
		return repeatRegulationDao.getObjectList(sql);
	}

	@Transactional(readOnly = true)
	public List<RepeatRegulation> getRepeatRegulationList() {
		String sql = "  from RepeatRegulation ";
		return repeatRegulationDao.getObjectList(sql);
	}

	/**
	 * 得到最新审核通过的重号规则
	 * 
	 * @author Yondy Chow
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<RepeatRegulation> getLastRepeatRegulationList() {
		String sql = " from RepeatRegulation o where o.regulationStatus='9' order by o.reversionDate";
		return repeatRegulationDao.getObjectList(sql);
	}

	@Transactional(readOnly = true)
	public RepeatRegulation getRepeatRegulationObject(String id) {
		return (RepeatRegulation) repeatRegulationDao.getObject(
				RepeatRegulation.class, id);
	}

	@Transactional(readOnly = true)
	public List<UsrUsrgrp> getSysUserGroupList(String grpCode) {
		String sql = " from UsrUsrgrp  usrUsrgrp where usrUsrgrp.grpcode='"
				+ grpCode + "'";
		return usrUsrgrpDao.getObjectList(sql);
	}

	@Transactional(readOnly = true)
	public List<RepeatRegulation> getRepeatRegulationByApplyId(String applyIds) {
		String hql = " from RepeatRegulation repeatRegulation where repeatRegulation.applyId='"
				+ applyIds + "'";
		return repeatRegulationDao.getObjectList(hql);
	}

	public boolean addAuditRepeatRegulation(RepeatRegulation obj,
			AuditInfo obj2, AuditLog obj3) {
		boolean flag = false;
		try {
			repeatRegulationDao.addObject(obj);
			auditInfoDao.addObject(obj2);
			auditLogDao.addObject(obj3);
			flag = true;
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}

}
