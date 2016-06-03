package cn.grgbanking.feeltm.audit.webapp;

import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.grgbanking.feeltm.audit.dao.AuditInfoDao;
import cn.grgbanking.feeltm.context.BaseApplicationContext;
import cn.grgbanking.feeltm.domain.BlackRegulation;
import cn.grgbanking.feeltm.domain.RegulationDeliver;
import cn.grgbanking.feeltm.domain.RepeatRegulation;
import cn.grgbanking.feeltm.rule.service.BlackRegulationService;
import cn.grgbanking.feeltm.rule.service.RegulationDeliverService;
import cn.grgbanking.feeltm.rule.service.RepeatRegulationService;
import cn.grgbanking.feeltm.util.DateUtil;

import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;

@SuppressWarnings("unchecked")
// 审核完成后，用于更新黑名单和重号规则的
public class UpdatePassStatusFunction implements FunctionProvider {
	public void execute(Map transientVars, Map args, PropertySet ps) {

		String applyId = (String) transientVars.get("applyId");
		String regulationStatus = "9";// 通过
		String reguStatus = "1"; // 规则下发状态更新为 "待下发"
		String flowName = (String) transientVars.get("flowName");

		AuditInfoDao auditInfoDao = (AuditInfoDao) BaseApplicationContext
				.getAppContext().getBean("auditInfoDao");
		auditInfoDao.updateRuleStatus(applyId, flowName, regulationStatus,
				reguStatus);

		RegulationDeliverService regulationDeliverService = (RegulationDeliverService) BaseApplicationContext
				.getAppContext().getBean("regulationDeliverService"); // 得到RegulationDeliverService
		BlackRegulationService blackRegulationService = (BlackRegulationService) BaseApplicationContext
				.getAppContext().getBean("blackRegulationService");
		RepeatRegulationService repeatRegulationService = (RepeatRegulationService) BaseApplicationContext
				.getAppContext().getBean("repeatRegulationService"); // 得到RepeatRegulationService

		List<RegulationDeliver> reList = regulationDeliverService
				.getRegulationDeliListByApplyId(applyId); // 根据申请编号得到规则下发列表
		int reListSize = reList.size();

		/** 重组规则 */
		String str_date = DateUtil.getTimeYYYYMMDDHHMMSSString(new Date());
		String str_version = "v_" + Long.toString(System.currentTimeMillis());

		List<BlackRegulation> blList = blackRegulationService
				.getBlackRegulationByApplyId(applyId); // 根据ApplyId得到当前审核规则
		// 黑名单规则
		StringBuffer blackRegu = new StringBuffer();
		if (blList != null && blList.size() != 0) {
			BlackRegulation blObj = blList.get(0);
			blackRegu.append(blObj.getMoneyType()); // 币种
			blackRegu.append(blObj.getMoneyDenomination()); // 面额
			blackRegu.append(blObj.getRegulation()); // 规则
		} else {
			blackRegu.append("0000");
		}

		// 重号规则
		List<RepeatRegulation> rList = repeatRegulationService
				.getRepeatRegulationByApplyId(applyId); // 根据ApplyId得到当前审核规则
		// 重号规则
		StringBuffer repeatRegu = new StringBuffer();
		if (rList != null && rList.size() != 0) {
			RepeatRegulation temp = rList.get(0);
			repeatRegu.append(temp.getRepeatnum() + ","
					+ temp.getDealwithMode() + "," + temp.getLogMode() + ","
					+ temp.getCreenMode() + "," + temp.getEnterAccountMode());
		} else {
			repeatRegu.append("0000");
		}

		for (int i = 0; i < reListSize; i++) {
			RegulationDeliver temp = reList.get(i);
			List<RegulationDeliver> rdList = regulationDeliverService
					.getRegulationDeliverListByTermid(temp.getTermid());
			if (rdList != null && rdList.size() != 0) {
				temp.setRole(rdList.get(0).getRole());
			} else {
				temp.setRole("0000|0000");
			}
			temp.setNote(temp.getRole());
			
			temp.setDate(str_date);
			// temp.setStatus("1"); // 待下发状态
			temp.setVersion(str_version);

			String role_str = temp.getRole();
			int index = role_str.indexOf("|");
			String bl_str = role_str.substring(0, index);
			//String re_str = role_str.substring(index + 1);
			if (!bl_str.equals("0000")) {
				if (!blackRegu.toString().equals("0000")) {
					bl_str += "," + blackRegu.toString();
				}
			} else {
				bl_str = blackRegu.toString();
			}
			String re_str = role_str.substring(index + 1);
			if(re_str.equals("0000") || !repeatRegu.toString().equals("0000")) {
				re_str = repeatRegu.toString();
			}
			temp.setRole(bl_str + "|" + re_str);

			regulationDeliverService.updateRegulationDeliver(temp);
		}
	}
}
