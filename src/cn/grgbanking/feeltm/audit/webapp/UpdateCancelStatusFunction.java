package cn.grgbanking.feeltm.audit.webapp;

import java.util.Map;

import cn.grgbanking.feeltm.audit.dao.AuditInfoDao;
import cn.grgbanking.feeltm.context.BaseApplicationContext;

import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;

@SuppressWarnings("unchecked")
// 审核完成后，用于更新黑名单和重号规则的
public class UpdateCancelStatusFunction implements FunctionProvider {
	public void execute(Map transientVars, Map args, PropertySet ps) {

		String applyId = (String) transientVars.get("applyId");
		String regulationStatus = "8";// 取消
		String flowName = (String) transientVars.get("flowName");
		AuditInfoDao auditInfoDao = (AuditInfoDao) BaseApplicationContext
				.getAppContext().getBean("auditInfoDao");
		auditInfoDao
				.updateRuleStatus(applyId, flowName, regulationStatus, null);
	}
}
