package cn.grgbanking.feeltm.projectPlan.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.projectPlan.domain.ProjectPlan;
import cn.grgbanking.feeltm.projectPlan.domain.ProjectPlanTask;
import cn.grgbanking.framework.dao.BaseDao;
import cn.grgbanking.framework.dao.hibernate3.BaseHibernateTemplate;
import cn.grgbanking.framework.util.Page;

@Repository("projectPlanDao")
public class ProjectPlanDao extends BaseDao<Object>{
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Page getPage(ProjectPlan plan, int pageNum, int pageSize, String planStartTime1, String planStartTime2, String planEndTime1, String planEndTime2) {
		try{
			String hql="from ProjectPlan plan where 1=1 ";
			/*if(condition.get("groupList")!=null && ((List)condition.get("groupList")).size()>0){
				hql+=" and (1=0 ";
				List<UsrGroup> groupList=(List<UsrGroup>)condition.get("groupList");
				for(UsrGroup group:groupList){
					hql +=" or plan.groupName='"+group.getGrpname()+"'";
				}
				hql+=" ) ";
				
			}*/
			
			if(plan!=null && StringUtils.isNotBlank(plan.getProjectName())){
				hql+=" and plan.projectName='"+(String)plan.getProjectName()+"'";
			}
			
			if(plan!=null && StringUtils.isNotBlank(plan.getGroupName())){
				hql+=" and plan.groupName='"+plan.getGroupName()+"'";
			}
			if(plan!=null && StringUtils.isNotBlank(plan.getProjectManager())){
				hql+=" and plan.projectManager='"+plan.getProjectManager()+"'";
			}
			
			if(StringUtils.isNotBlank(planStartTime1)){
				hql += " and plan.planStartDate >=to_date('"+planStartTime1+"','yyyy-MM-dd') ";
			}
			if(StringUtils.isNotBlank(planStartTime2)){
				hql += " and plan.planStartDate <=to_date('"+planStartTime2+"','yyyy-MM-dd') ";
			}
			if(StringUtils.isNotBlank(planEndTime1)){
				hql += " and plan.planEndDate >=to_date('"+planEndTime1+"','yyyy-MM-dd') ";
			}
			if(StringUtils.isNotBlank(planEndTime2)){
				hql += " and plan.planEndDate <=to_date('"+planEndTime2+"','yyyy-MM-dd') ";
			}
			
			
			Page page=((BaseHibernateTemplate) getHibernateTemplate()).getPage(hql,pageNum, pageSize);
			return page;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	/**根据项目id 获取项目的详细计划
	 * wtjiao 2014年5月21日 下午3:02:48
	 * @param projectId
	 * @return
	 */
	public List<ProjectPlanTask> getPlanTaskList(String projectId) {
		try{
			String hql="from ProjectPlanTask task where task.planId=?";
			List<ProjectPlanTask> list=(List<ProjectPlanTask>)((BaseHibernateTemplate) getHibernateTemplate()).find(hql, new Object[]{projectId});
			return list;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	/**保存项目基本信息
	 * wtjiao 2014年5月26日 下午4:09:56
	 * @param project
	 * @return
	 */
	public String saveProject(ProjectPlan project) {
		((BaseHibernateTemplate) getHibernateTemplate()).save(project);
		String id=project.getId();
		return id;
	}
	
	public boolean updateProject(ProjectPlan project) {
		((BaseHibernateTemplate) getHibernateTemplate()).update(project);;
		return true;
	}

	/**保存项目任务详情
	 * wtjiao 2014年5月26日 下午4:10:05
	 */
	public void saveTask(ProjectPlanTask projectPlanTask) {
		((BaseHibernateTemplate) getHibernateTemplate()).save(projectPlanTask);
	}

	/**删除指定项目的任务列表
	 * wtjiao 2014年5月26日 下午4:29:53
	 * @param projectId
	 */
	public void removeProjectTaskByProjectId(String projectId) {
		String hql="delete from ProjectPlanTask task where task.planId=?";
		((BaseHibernateTemplate)getHibernateTemplate()).bulkUpdate(hql, new Object[]{projectId});
	}

	
}
