package cn.grgbanking.feeltm.projectPlan.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.projectPlan.dao.ProjectPlanDao;
import cn.grgbanking.feeltm.projectPlan.domain.ProjectPlan;
import cn.grgbanking.feeltm.projectPlan.domain.ProjectPlanTask;
import cn.grgbanking.framework.util.Page;

@Service("projectPlanService")
@Transactional
public class ProjectPlanService {

	@Autowired
	private ProjectPlanDao projectPlanDao;


	@Transactional(readOnly = true)
	public Page getPage(ProjectPlan plan, int pageNum, int pageSize, String planStartTime1, String planStartTime2, String planEndTime1, String planEndTime2) {
		return projectPlanDao.getPage(plan,pageNum,pageSize,planStartTime1,planStartTime2,planEndTime1,planEndTime2);
	}


	/**根据项目id获取该项目的详细计划 
	 * wtjiao 2014年5月21日 下午3:01:56
	 * @param projectId
	 * @return
	 */
	public List<ProjectPlanTask> getPlanTaskList(String projectId) {
		return projectPlanDao.getPlanTaskList(projectId);
	}


	/** 保存项目基本信息
	 * wtjiao 2014年5月26日 下午2:13:42
	 * @param plan
	 * @return
	 */
	public String saveProject(ProjectPlan project) {
		return projectPlanDao.saveProject(project);
	}

	public boolean updateProject(ProjectPlan project) {
		return projectPlanDao.updateProject(project);
	}

	/** 保存项目任务列表
	 * wtjiao 2014年5月26日 下午2:14:07
	 * @param taskList
	 */
	public void saveTaskList(List<ProjectPlanTask> taskList) {
		if(taskList!=null){
			//先删除本项目已有的任务详情
			String projectId=taskList.get(0).getPlanId();
			projectPlanDao.removeProjectTaskByProjectId(projectId);
			for(int i=0;i<taskList.size();i++){
				projectPlanDao.saveTask(taskList.get(i));
			}
		}
	}


	/** 获取项目基本信息
	 * wtjiao 2014年5月28日 上午8:49:39
	 * @param id
	 * @return
	 */
	public ProjectPlan getPlanById(String id) {
		return (ProjectPlan)projectPlanDao.getObject(ProjectPlan.class, id);
	}


	/**获取项目的任务列表
	 * wtjiao 2014年5月28日 上午8:54:42
	 * @param id
	 * @return
	 */
	public List<ProjectPlanTask> getTaskListByPlanId(String id) {
		return projectPlanDao.getPlanTaskList(id);
	}


	/**删除项目计划
	 * wtjiao 2014年5月29日 上午9:40:40
	 * @param plan
	 */
	public void removeProjectPlan(ProjectPlan plan) {
		projectPlanDao.removeObject(ProjectPlan.class, plan.getId());
	}


	/**根据项目id删除项目计划任务列表
	 * wtjiao 2014年5月29日 上午9:43:48
	 * @param projectId
	 */
	public void removeProjectTaskByProjectId(String projectId) {
		projectPlanDao.removeProjectTaskByProjectId(projectId);
	}


	

}
