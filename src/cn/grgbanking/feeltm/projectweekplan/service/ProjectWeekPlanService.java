package cn.grgbanking.feeltm.projectweekplan.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.config.BusnDataDir;
import cn.grgbanking.feeltm.project.domain.Project;
import cn.grgbanking.feeltm.projectweekplan.dao.ProjectWeekPlanDao;
import cn.grgbanking.feeltm.projectweekplan.domain.ProjectWeekPlan;
import cn.grgbanking.feeltm.projectweekplan.domain.ProjectWeekPlanTarget;
import cn.grgbanking.feeltm.projectweekplan.domain.ProjectWeekPlanTask;
import cn.grgbanking.framework.util.Page;

@Service("projectWeekPlanService")
@Transactional
public class ProjectWeekPlanService {

	@Autowired
	private ProjectWeekPlanDao projectWeekPlanDao;


	/**获取用户的项目任务
	 * @param projectId 项目id
	 * @param userKey 用户id
	 * @param startDate 任务范围的起始时间
	 * @param endDate 任务范围的结束时间
	 * @return
	 */
	public List<ProjectWeekPlanTask> getTaskInProject(String projectId,String userKey,Date startDate,Date endDate){
		return projectWeekPlanDao.getTaskInProject(projectId,userKey,startDate,endDate);
	}
	
	@Transactional(readOnly = true)
	public Page getPage(ProjectWeekPlan plan, int pageNum, int pageSize) {
		Page page=projectWeekPlanDao.getPage(plan,pageNum,pageSize);
		List list=page.getQueryResult();
		if(list!=null && list.size()>0){
			for(int i=0;i<list.size();i++){
				ProjectWeekPlan wPlan=(ProjectWeekPlan)list.get(i);
				wPlan.setTargetList(projectWeekPlanDao.getProjectWeekPlanTargetList(wPlan.getId()));
				wPlan.setTaskList(projectWeekPlanDao.getProjectWeekTaskList(wPlan.getId()));
				//wPlan.setCustomerName(getCustomerName(wPlan.getCustomerKey()));
				try{
					wPlan.setProjectName(getProjectName(wPlan.getProjectId()));
				}catch(Exception e){
					wPlan.setProjectName("[无]");
					e.printStackTrace();
				}
			}
		}
		return page;
	}
	
	//根据数据字典查询客户名称
	public String getCustomerName(String key){
		return (String)BusnDataDir.getMapKeyValue("projectManage.customer").get(key);
	}
	
	//获取项目的项目名称
	public String getProjectName(String projectId){
		return ((Project)projectWeekPlanDao.getObject(Project.class, projectId)).getName();
	}


	/**获取项目的目标列表
	 * wtjiao 2014年5月21日 下午3:01:56
	 * @param projectId
	 * @return
	 */
	public List<ProjectWeekPlanTarget> getProjectWeekPlanTargetList(String planId) {
		return projectWeekPlanDao.getProjectWeekPlanTargetList(planId);
	}
	
	/**获取项目的任务
	 * wtjiao 2014年5月21日 下午3:01:56
	 * @param projectId
	 * @return
	 */
	public List<ProjectWeekPlanTask> getProjectWeekTaskList(String planId) {
		return projectWeekPlanDao.getProjectWeekTaskList(planId);
	}



	/** 保存周计划
	 * wtjiao 2014年5月26日 下午2:13:42
	 * @param plan
	 * @return
	 */
	public String saveProjectWeekPlan(ProjectWeekPlan plan) {
		//保存基本信息
		String planId=projectWeekPlanDao.saveProjectWeekPlan(plan);
		return planId;
	}
	
	
	//保存目标
	public void saveProjectWeekPlanTargetList(List targetList){
		projectWeekPlanDao.saveProjectWeekPlanTargetList(targetList);
	}
	
	//保存任务
	public void saveProjectWeekPlanTaskList(List taskList){
		 projectWeekPlanDao.saveProjectWeekPlanTaskList(taskList);
	}

	/** 更新周计划
	 * wtjiao 2014年6月25日 下午5:56:49
	 * @param plan
	 * @return
	 */
	public boolean updateProjectWeekPlan(ProjectWeekPlan plan) {
		//更新基本信息
		projectWeekPlanDao.updateProjectWeekPlan(plan);
		//删除目标
		projectWeekPlanDao.removeProjectWeekPlanTargets(plan.getId());
		//保存目标
		projectWeekPlanDao.saveProjectWeekPlanTargetList(plan.getTargetList());
		//删除任务
		projectWeekPlanDao.removeProjectWeekPlanTasks(plan.getId());
		//保存任务
		projectWeekPlanDao.saveProjectWeekPlanTaskList(plan.getTaskList());
		return true;
	}


	/** 获取周计划详情
	 * wtjiao 2014年5月28日 上午8:49:39
	 * @param id
	 * @return
	 */
	public ProjectWeekPlan getWeekPlanById(String id) {
		ProjectWeekPlan plan= (ProjectWeekPlan)projectWeekPlanDao.getObject(ProjectWeekPlan.class, id);
		plan.setProjectName(getProjectName(plan.getProjectId()));;
		plan.setTargetList(projectWeekPlanDao.getProjectWeekPlanTargetList(id));
		plan.setTaskList(projectWeekPlanDao.getProjectWeekTaskList(id));
		return plan;
	}



	/**删除周计划
	 * wtjiao 2014年5月29日 上午9:40:40
	 * @param plan
	 */
	public void removeProjectWeekPlan(ProjectWeekPlan plan) {
		//删除列表
		projectWeekPlanDao.removeProjectWeekPlanTargets(plan.getId());
		projectWeekPlanDao.removeProjectWeekPlanTasks(plan.getId());
		//删除基本信息
		projectWeekPlanDao.removeObject(ProjectWeekPlan.class, plan.getId());
	}
	
	/**
	 * 根据日志信息完善周计划中的任务完成度、偏差度及确认工时
	 */
	public void getDataForDayLogToPrjWeekPlan(String userid,String planid){
		projectWeekPlanDao.getDataForDayLogToPrjWeekPlan(userid, planid);
	}

	public String getPlanTaskDescById(String plan_id){
		return projectWeekPlanDao.getPlanTaskDescById(plan_id);
	}
}
