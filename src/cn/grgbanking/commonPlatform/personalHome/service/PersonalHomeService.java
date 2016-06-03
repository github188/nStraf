package cn.grgbanking.commonPlatform.personalHome.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.cardRecord.dao.CardRecordDao;
import cn.grgbanking.feeltm.integralCenter.dao.IntegralCenterDao;
import cn.grgbanking.feeltm.projectweekplan.dao.ProjectWeekPlanTaskDao;
import cn.grgbanking.feeltm.projectweekplan.domain.ProjectWeekPlanTask;
import cn.grgbanking.framework.service.BaseService;

/**
 * 个人首页
 * 
 * @author zzhui1
 * 
 */
@Service
@Transactional
public class PersonalHomeService extends BaseService {
	@Autowired
	private IntegralCenterDao integralCenterDao;
	@Autowired
	private ProjectWeekPlanTaskDao projectWeekPlanTaskDao;
	@Autowired
	private CardRecordDao cardRecordDao;

	/**
	 * 根据部门查询
	 * 
	 * @param deptId
	 * @return
	 */
	public List<Object> getPointTopFiveByDeptid(String deptId) {
		List<Object> list = integralCenterDao.orderByDept(deptId);

		return list;
	}

	/**
	 * 查询周计划模块对应表数据:本周本人的任务完成情况
	 * @param userId 用户ID
	 * @param startDate 周日
	 * @param endDate 周六
	 * @return 周计划模块对应表数据List
	 */
	public List<ProjectWeekPlanTask> getTaskInfoByWeek(String userId,
			String startDate, String endDate) {
		return projectWeekPlanTaskDao.getTaskInfoByWeek(userId, startDate,
				endDate);
	}
}
