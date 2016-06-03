package cn.grgbanking.feeltm.audit.dao;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.domain.AuditInfo;
import cn.grgbanking.feeltm.domain.BlackRegulation;
import cn.grgbanking.feeltm.domain.RepeatRegulation;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.framework.dao.BaseDao;

@Repository
@SuppressWarnings("unchecked")
public class AuditInfoDao extends BaseDao<AuditInfo> {
	/*
	 * 如果结束流程,根据申请id更新重号规则和黑名单规则的状态为“9”通过, 更新下发状态为“1”待下发。
	 */
	public void updateRuleStatus(String applyId, String flowName,
			String regulationStatus, String reguStatus) {
		try {
			//String hql = "";

			if (flowName != null && flowName.equals("blacklist_apply")) {
				updateBlackRegulationByApplyId(applyId, regulationStatus, reguStatus);
			} else if (flowName != null && flowName.equals("repeat_apply")) {
				updateRepeatRegulationByApplyId(applyId, regulationStatus, reguStatus);
			}

		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
	}

	/**
	 * 根据黑名单规则编号更新规则状态和下发状态
	 * @param applyId 规则编号
	 * @param regulationStatus 规则状态
	 * @param reguStatus 规则下发状态
	 * @return
	 */
	public boolean updateBlackRegulationByApplyId(String applyId,
			String regulationStatus, String reguStatus) {
		boolean flag = false;
		try {
			List list = this.getHibernateTemplate().find(
					" from BlackRegulation t where t.applyId ='" + applyId
							+ "'");
			BlackRegulation blackRegulation = null;
			if (list != null && list.size() > 0) {
				blackRegulation = (BlackRegulation) list.get(0);
				blackRegulation.setRegulationStatus(regulationStatus);
				blackRegulation.setReguStatus(reguStatus);
				blackRegulation.setReversionDate(new Date());
				this.getHibernateTemplate().update(blackRegulation);
				flag = true;
			}
			return flag;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			return flag;
		}
	}

	/**
	 * 根据规则编号更新规则状态和下发状态
	 * @param applyId 编号
	 * @param regulationStatus 规则状态
	 * @param reguStatus 下发状态
	 * @return
	 */
	public boolean updateRepeatRegulationByApplyId(String applyId,
			String regulationStatus, String reguStatus) {
		boolean flag = false;
		try {
			List list = this.getHibernateTemplate().find(
					" from RepeatRegulation t where t.applyId ='" + applyId
							+ "'");
			RepeatRegulation repeatRegulation = null;
			if (list != null && list.size() > 0) {
				repeatRegulation = (RepeatRegulation) list.get(0);
				repeatRegulation.setRegulationStatus(regulationStatus);
				repeatRegulation.setReguStatus(reguStatus);
				repeatRegulation.setReversionDate(new Date());
				this.getHibernateTemplate().update(repeatRegulation);
				flag = true;
			}
			return flag;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			return flag;
		}
	}
}
