package cn.grgbanking.feeltm.audit.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.audit.dao.AuditLogDao;
import cn.grgbanking.feeltm.domain.AuditInfo;
import cn.grgbanking.feeltm.domain.AuditLog;
import cn.grgbanking.feeltm.domain.RegulationDeliver;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.framework.service.BaseService;
import cn.grgbanking.framework.util.Page;

@SuppressWarnings("unchecked")
@Service
@Transactional
public class AuditLogService extends BaseService {
	@Autowired
	private AuditLogDao auditLogDao;

	public boolean addAuditLog(AuditLog obj) {
		boolean flag = false;
		try {
			auditLogDao.addObject(obj);
			flag = true;
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}

	public boolean deleteAuditLog(AuditLog obj) {
		boolean flag = false;
		try {
			auditLogDao.removeObject(AuditLog.class, obj.getId());
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}

	public boolean updateAuditLog(AuditLog obj) {
		boolean flag = false;
		try {
			auditLogDao.updateObject(obj);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			throw new NullPointerException();
		}
		return flag;
	}

	@Transactional(readOnly = true)
	public Page getAuditLogPage(AuditLog audit, int pageNum, int pageSize) {
		String sql = "  from AuditLog ";
		return auditLogDao.getObjectPage(sql, pageNum, pageSize);
	}

	@Transactional(readOnly = true)
	public List getAuditLogList(String obj) {

		String sql = "  from AuditLog  where applyId='" + obj + "'";
		return auditLogDao.getObjectList(sql);
	}

	@Transactional(readOnly = true)
	public AuditLog getAuditLogObject(String id) {
		return (AuditLog) auditLogDao.getObject(AuditLog.class, id);
	}
	@Transactional(readOnly = true)
	public List<RegulationDeliver> getRegulationDeliverByApplyId(String applyId){
		String sql = " from RegulationDeliver where applyId='"+applyId+"'";
		return auditLogDao.getObjectList(sql);
	}
	@Transactional(readOnly = true)
	public List<AuditLog> getAuditLogByApplyId(String applyId) {
		String hql = " from AuditLog where applyId='" + applyId + "'";
		hql += " order by applayDate ";
		return auditLogDao.getObjectList(hql);
	}

	@Transactional(readOnly = true)
	public List<AuditInfo> getAuditInfoByApplyId(String applyId){
		String hql = "from AuditInfo where applyId='"+applyId+"'";
		return auditLogDao.getObjectList(hql);
	}
	@Transactional(readOnly = true)
	public Page getAuditLogPage(AuditLog obj, int pageNum, int pageSize,
			String beginDate, String endDate) {
		String sql = " from AuditLog auditLog where 1=1 ";
		if(obj.getApplyType() != null && !obj.getApplyType().equals(""))
			sql += " and auditLog.applyType='" + obj.getApplyType() + "'";
		
		if(obj.getApplyStatus() != null && !obj.getApplyStatus().equals(""))
			sql += " and auditLog.applyStatus='" + obj.getApplyStatus() + "'";
		
		if (beginDate != null && !beginDate.equals("")) {
			sql += " and auditLog.applayDate >='"
					+ beginDate + "'";
		}
		if (endDate != null && !endDate.equals("")) {
				sql += " and auditLog.applayDate <'"
						+ endDate + "'";
		}
		if (obj.getApplyId() != null && !obj.getApplyId().equals("")){
			sql += " and auditLog.applyId like '%" + obj.getApplyId() + "%'";
			//sql += " and auditLog.applayDate =(select max(b.applayDate) from AuditLog b where b.applyId ='"+obj.getApplyId()+"' ";
		}
		sql += " and auditLog.applayDate = (select max(b.applayDate) from AuditLog b where b.applyId = auditLog.applyId)";
		
		sql += " order by auditLog.applayDate desc";
		return auditLogDao.getObjectPage(sql, pageNum, pageSize);
	}

}
