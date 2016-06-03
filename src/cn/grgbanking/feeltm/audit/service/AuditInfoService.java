package cn.grgbanking.feeltm.audit.service;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.audit.dao.AuditInfoDao;
import cn.grgbanking.feeltm.domain.AuditInfo;
import cn.grgbanking.feeltm.domain.AuditLog;
import cn.grgbanking.feeltm.domain.BlackRegulation;
import cn.grgbanking.feeltm.domain.SysUser;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.rule.dao.BlackRegulationDao;
import cn.grgbanking.feeltm.util.DateUtil;
import cn.grgbanking.feeltm.util.SqlHelper;
import cn.grgbanking.framework.service.BaseService;
import cn.grgbanking.framework.util.Page;

import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.basic.BasicWorkflow;
import com.opensymphony.workflow.query.Expression;
import com.opensymphony.workflow.query.FieldExpression;
import com.opensymphony.workflow.query.NestedExpression;
import com.opensymphony.workflow.query.WorkflowExpressionQuery;

@SuppressWarnings("unchecked")
@Service
@Transactional
public class AuditInfoService extends BaseService {
	@Autowired
	private AuditInfoDao auditInfoDao;
	
	@Autowired
	private BlackRegulationDao blackRegulationDao;

	public boolean addAuditInfo(AuditInfo obj) {
		boolean flag = false;
		try {
			auditInfoDao.addObject(obj);
			flag = true;
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}

	public boolean deleteAuditInfo(AuditInfo obj) {
		boolean flag = false;
		try {
			auditInfoDao.removeObject(AuditInfo.class, obj.getId());
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}

	public boolean updateAuditInfo(AuditInfo obj) {
		boolean flag = false;
		try {
			auditInfoDao.updateObject(obj);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			throw new NullPointerException();
		}
		return flag;
	}

	public boolean audit(AuditLog obj2) {
		boolean flag = false;
		try {			
			auditInfoDao.addObject(obj2);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			throw new NullPointerException();
		}
		return flag;
	}
	
	public boolean audit(AuditInfo obj, AuditLog obj2, BlackRegulation obj3) {
		boolean flag = false;
		try {
			auditInfoDao.updateObject(obj);
			auditInfoDao.addObject(obj2);
			blackRegulationDao.updateObject(obj3);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			throw new NullPointerException();
		}
		return flag;
	}

	@Transactional(readOnly = true)
	public Page getAuditInfoPage(AuditInfo auditInfo, int pageNum, int pageSize,
			String userid, String beginDate, String endDate,String params[]) {
		// 先取得当前用户下的工作任务列表
		StringBuffer applyIds = new StringBuffer();
		SysUser user = new SysUser();
		user.setUserid(userid);
		Workflow wf = new BasicWorkflow(user.getUserid());
		NestedExpression nestedExpression = null;
		WorkflowExpressionQuery query = new WorkflowExpressionQuery(
				nestedExpression);
		String flowStatus = "0";
		if(params==null||params[0]==null)
			flowStatus="0";
		else
		   flowStatus=params[0];
		String applyId = "";
//		if (obj.length > 1)
//			applyId = (String) obj[1];
		// 设置查询条件为流程正在办理中且OWNER为当前用户
		if (flowStatus != null && flowStatus.equals("0")) {
			nestedExpression = new NestedExpression(new Expression[] {
					new FieldExpression(FieldExpression.OWNER,
							FieldExpression.CURRENT_STEPS,
							FieldExpression.EQUALS, user.getUserid()),
					new FieldExpression(FieldExpression.STATUS,
							FieldExpression.CURRENT_STEPS,
							FieldExpression.EQUALS, "Underway") },
					NestedExpression.AND);
			query = new WorkflowExpressionQuery(nestedExpression);
		}
		// 查询OWNER为当前用户办理的所有历史记录
		else if (flowStatus != null && flowStatus.equals("1")) {
			//query = new WorkflowExpressionQuery(new FieldExpression(FieldExpression.OWNER, FieldExpression.HISTORY_STEPS,FieldExpression.EQUALS, user.getUserid()));
			 query = new WorkflowExpressionQuery(new FieldExpression(FieldExpression.OWNER, FieldExpression.HISTORY_STEPS, FieldExpression.EQUALS, user.getUserid())) ;
		}
		// 查询OWNER为当前用户办理的所有当前及历史记录
		else {
			nestedExpression = new NestedExpression(new Expression[] {
					new FieldExpression(FieldExpression.OWNER,
							FieldExpression.CURRENT_STEPS,
							FieldExpression.EQUALS, user.getUserid()),
					new FieldExpression(FieldExpression.OWNER,
							FieldExpression.HISTORY_STEPS,
							FieldExpression.EQUALS, user.getUserid()) },
					NestedExpression.OR);
			query = new WorkflowExpressionQuery(nestedExpression);
		}

		// System.out.println("********");

		try {
			List workflows = wf.query(query);
			for (Iterator iterator = workflows.iterator(); iterator.hasNext();) {
				String wfid = iterator.next().toString();
				applyIds.append("'" + wfid + "',");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		StringBuffer applyIds1 = new StringBuffer();
		if (applyId != null && !applyId.equals("")) {// 当未指定流程号时依流程状态查找流程号
			if (applyIds.indexOf(applyId) > 0) {
				applyIds1.append(" applyId in ('" + applyId + "')");

			}

		} else {
			if (applyIds.length() > 0) {
				String[] a = applyIds.toString().split(",");
				if (a.length > 0) {
					String b = SqlHelper.fitNumInCondition("applyId", a);
					applyIds1 = new StringBuffer(b);
				}

			} else {
				applyIds1 = applyIds;
			}

		}
		String sql = "";
		if (applyIds1.length() != 0) {
			sql = "  from AuditInfo as auditInfo where " + applyIds1.toString();
			if (beginDate != null && !beginDate.equals("")) {
				beginDate = DateUtil
						.stringyyyy_MM_ddHH_mm_ssToyyyyMMddHHmmss(beginDate);
			}
			if (endDate != null && !beginDate.equals("")) {
				endDate = DateUtil
						.stringyyyy_MM_ddHH_mm_ssToyyyyMMddHHmmss(endDate);
			}
			//boolean flag = false;
			if (auditInfo.getApplytyp() != null
					&& !((auditInfo.getApplytyp()).equals(""))) {
				sql += " and auditInfo.applytyp="
						+ auditInfo.getApplytyp();
				//flag = true;
			}
//			if (auditInfo.getAuditStatus() != null
//					&& !(auditInfo.getAuditStatus()).equals("")) {
//				sql += " and auditInfo.auditStatus="
//						+ auditInfo.getAuditStatus();
//				flag = true;
//			}
			
			if (beginDate != null && !beginDate.equals("")) {
					sql += " and auditInfo.applayDate||auditInfo.applayDate >='"
							+ beginDate + "'";
			}
			if (endDate != null && !endDate.equals("")) {
					sql += " and auditInfo.applayDate||auditInfo.applayDate <'"
							+ endDate + "'";
			}
			
			sql += " order by auditInfo.applayDate desc";
			
		} else
			sql = "  from AuditInfo where  applyId is null ";
		return auditInfoDao.getObjectPage(sql, pageNum, pageSize);
	}

	@Transactional(readOnly = true)
	public List getAuditInfoList(Object[] obj) {
		String sql = "  from AuditInfo ";
		return auditInfoDao.getObjectList(sql);
	}

	@Transactional(readOnly = true)
	public AuditInfo getAuditInfoObject(String id) {
		return (AuditInfo) auditInfoDao.getObject(AuditInfo.class, id);
	}

}
