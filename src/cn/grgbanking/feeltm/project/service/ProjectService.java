package cn.grgbanking.feeltm.project.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.grgbanking.feeltm.config.BusnDataDir;
import cn.grgbanking.feeltm.domain.SysDatadir;
import cn.grgbanking.feeltm.domain.SysUser;
import cn.grgbanking.feeltm.project.dao.ProjectDao;
import cn.grgbanking.feeltm.project.domain.Project;
import cn.grgbanking.feeltm.project.domain.ProjectAttendance;
import cn.grgbanking.feeltm.project.domain.ProjectResourcePlan;
import cn.grgbanking.feeltm.project.domain.UserProject;
import cn.grgbanking.framework.service.BaseService;
import cn.grgbanking.framework.util.Page;

/**
 * wtjiao 2014年7月3日 上午9:02:37
 */
@Service
public class ProjectService extends BaseService{
	
	@Autowired
	private ProjectDao dao;
	/**
	 * 根据项目经理获取所负责的项目
	 * @return
	 * lhyan3
	 * 2014年6月26日
	 */
	public List<Project> getProjectByManager(String managerId){
		String hql = "from Project p where p.proManagerId='"+managerId+"'";
		return dao.getProjectByManager(hql); 
	}
	

	/**
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * lhyan3
	 * 2014年6月12日
	 */
	public Page getPage(int pageNum, int pageSize) {
		return dao.getPage(pageNum,pageSize);
	}
	
	
	/**
	 * 根据id获取project
	 * @param id
	 * @return
	 * lhyan3
	 * 2014年6月12日
	 */
	public Project getProjectById(String id) {
		Project prj=dao.getProjectById(id);
		if(prj!=null){
			prj.setUserProjects(sortUserProject(prj.getUserProjects()));
			prj.setProjectResourcePlan(sortProjectResourcePlan(prj.getProjectResourcePlan()));
		}
		return prj;
	}
	
	public Project getBriefProjectById(String id) {
		Project prj=dao.getProjectById(id);
		return prj;
	}

	@SuppressWarnings("unchecked")
	private List<ProjectResourcePlan> sortProjectResourcePlan(List<ProjectResourcePlan> prps) {
		Collections.sort(prps, new Comparator() {
			Map<String,Object> roleMap=BusnDataDir.getMapKeyObject("projectMonitor.projectRole");
			Map<String,Object> dutyMap=BusnDataDir.getMapKeyObject("projectMonitor.projectDuty");
			@Override
			public int compare(Object o1, Object o2) {
				ProjectResourcePlan prp1=(ProjectResourcePlan)o1;
				ProjectResourcePlan prp2=(ProjectResourcePlan)o2;
				long tmp= prp2.getFactEndTime().getTime() - prp1.getFactEndTime().getTime();//先以实际退出时间排序
				if(tmp!=0){
					return tmp>0?1:tmp==0?0:-1;
				}else{
					int roleOrder2=999;
					int roleOrder1=999;
					try{
						roleOrder2=((SysDatadir)roleMap.get(prp2.getProjectRole())).getOrder();//退出时间一致，按照项目角色排序
					}catch(Exception e){}
					try{
						roleOrder1=((SysDatadir)roleMap.get(prp1.getProjectRole())).getOrder();
					}catch(Exception e){}
					
					if(roleOrder2!=roleOrder1){
						return -1*(roleOrder2-roleOrder1);
					}else{
						int dutyOrder2=999;
						int dutyOrder1=999;
						try{
							roleOrder2=((SysDatadir)dutyMap.get(prp2.getProjectDuty())).getOrder();//角色一致，按照任务排序
						}catch(Exception e){}
						try{
							roleOrder1=((SysDatadir)dutyMap.get(prp1.getProjectDuty())).getOrder();
						}catch(Exception e){}
						return -1*(dutyOrder2-dutyOrder1);
					}
				}
			}
		});
		return prps;
	}


	@SuppressWarnings("unchecked")
	private List<UserProject> sortUserProject(List<UserProject> ups) {
		Collections.sort(ups, new Comparator() {
			Map<String,Object> roleMap=BusnDataDir.getMapKeyObject("projectMonitor.projectRole");
			Map<String,Object> dutyMap=BusnDataDir.getMapKeyObject("projectMonitor.projectDuty");
			@Override
			public int compare(Object o1, Object o2) {
				UserProject up1=(UserProject)o1;
				UserProject up2=(UserProject)o2;
				//先按项目角色排序(在数据字典中，项目经理排列最靠前，则在这里项目经理角色的成员放在最前面)
				int roleOrder2=999;
				try{
					roleOrder2=((SysDatadir)roleMap.get(up2.getProjectRole())).getOrder();
				}catch(Exception e){}
				int roleOrder1=999;
				try{
					roleOrder1=((SysDatadir)roleMap.get(up1.getProjectRole())).getOrder();
				}catch(Exception e){}
				if(roleOrder2!=roleOrder1){
					return -1*(roleOrder2-roleOrder1);
				}else{
					int dutyOrder2=((SysDatadir)dutyMap.get(up2.getProjectDuty())).getOrder();//角色一致，按照任务排序
					int dutyOrder1=((SysDatadir)dutyMap.get(up1.getProjectDuty())).getOrder();
					return -1*(dutyOrder2-dutyOrder1);
				}
			}
		});
		return ups;
	}


	/**
	 * 保存对象
	 * @param group
	 * lhyan3
	 * 2014年6月12日
	 */
	public String saveOrUpdate(Project p) {
		String id=p.getId();
		if(p.getId()==null){
			id=(String)dao.addObject(p);
		}else{
			dao.updateObject(p);
		}
		return id;
	}

	
	/**
	 * 更新对象
	 * @param g
	 * lhyan3
	 * 2014年6月12日
	 */
	public void update(Object g) {
		dao.updateObject(g);
	}

	/**
	 * 删除项目
	 * @param g
	 * lhyan3
	 * 2014年6月12日
	 */
	public void delete(Project g) {
		dao.removeObject(Project.class, g.getId());
	}

	/**
	 * 删除用户与项目的关联
	 * @param g
	 * lhyan3
	 * 2014年6月12日
	 */
	public void deleteUserProject(Project g) {
		dao.deleteUserProject(g);
	}

	/**
	 * 根据用户获取项目
	 * @param userid
	 * @return
	 * lhyan3
	 * 2014年6月13日
	 */
	public List getProjectByUserId(String userid) {
		return dao.getProjectByUserId(userid);
	}

	/**
	 * @param grpovalue
	 * @param deptvalue
	 * @param groupValue
	 * @param deptValue2
	 * @return
	 * lhyan3
	 * 2014年6月13日
	 */
	/*public List<Object[]> findUserListByGrpDept(String grpcode,
			String deptvalue, String groupValue, String deptValue2) {
		return dao.findUserListByGrpDept(grpcode, deptvalue,groupValue,deptValue2);
	}*/
	
	/**
	 * 获取所有的项目
	 * @return
	 * lhyan3
	 * 2014年6月13日
	 */
	@SuppressWarnings("unchecked")
	public List<Project> listAllGroup() {
		return dao.listAllGroup();
	}

	/**
	 * 获取所有未完结项目 
	 * @return List<Project>
	 * @author lping1 2014-10-16
	 */
	@SuppressWarnings("unchecked")
	public List<Project> listUnFinishedGroup() {
		return dao.listUnFinishedGroup();
	}
	
	/**
	 * 获取所有未完结项目 (除去“其他项目”)
	 * @return List<Project>
	 * @author lping1 2014-10-16
	 */
	@SuppressWarnings("unchecked")
	public List<Project> listUnFinishedGroupExpectOtherProject() {
		return dao.listUnFinishedGroupExpectOtherProject();
	}

	/**
	 * * 根据项目组或部门查询在该项目组部门中人员
	 * @param projectId  项目id
	 * @param deptId  部门key
	 * @return
	 * lhyan3
	 * 2014年6月26日
	 */
	public List<SysUser> getInProjectByProject(String projectId,String deptId) {
		String hql = "from SysUser s where s.userid in (select u.userid from UserProject u where u.project.id='"+projectId+"')";
		if(deptId!=null && !"".equals(deptId)){
			hql += " and s.deptName='"+deptId+"'";
		}
		return dao.getInProjectByProject(hql);
	}
	
	/** 查询项目组成员
	 * wtjiao 2014年6月27日 下午12:03:10
	 * @param projectId
	 * @param deptId
	 * @return
	 */
	public List<SysUser> getInProjectByProject(String projectId) {
		String hql = "from SysUser s where s.userid in (select u.userid from UserProject u where u.project.id='"+projectId+"')";
		return dao.getInProjectByProject(hql);
	}

	/**
	 * 根据项目名称查询项目对应成员
	 * @param projectName 项目名称
	 * @return List<SysUser> 项目组成员
	 * @author lping1 2014-10-17
	 */
	public List<SysUser> getInProjectUserByPrjName(String projectName){
		String hql = "from SysUser s where s.userid in (select u.userid from UserProject u where u.project.name='" + projectName + "')";
		return dao.getInProjectByProject(hql);
	}
	/**
	 * 根据项目id或部门key查询不在该项目部门中人员
	 * @param projectId  项目id
	 * @param deptId  部门key
	 * @param username
	 * @return
	 * lhyan3
	 * 014年6月26日
	 */
	public List<SysUser> getNotInProjectByProject(String projectId,String deptId,String username) {
		String hql = "from SysUser s where s.userid not in (select u.userid from UserProject u where u.project.id='"+projectId+"' )";
		if(deptId!=null && !"".equals(deptId)){
			hql += " and s.deptName='"+deptId+"'";
		}
		if(username!=null && !"".equals(username)){
			hql += " and (s.userid like '%"+username+"%' or s.username like '%"+username+"%')";
		}
		return dao.getNotInProjectByProject(hql);
	}
	
	
	/**查询人员
	 * wtjiao 2014年10月10日 下午2:39:05
	 * @param projectId
	 * @param deptId
	 * @param username
	 * @return
	 */
	public List<SysUser> getUserByIdOrName(String projectId,String deptId,String username) {
		String hql = "from SysUser s where 1=1 ";
		if(deptId!=null && !"".equals(deptId)){
			hql += " and s.deptName='"+deptId+"'";
		}
		if(username!=null && !"".equals(username)){
			hql += " and (s.userid like '%"+username+"%' or s.username like '%"+username+"%')";
		}
		return dao.getNotInProjectByProject(hql);
	}
	
	/**
	 * 获取除userid以外的所有人员选择
	 * @param userid
	 * @param deptId
	 * @param username
	 * @return
	 * lhyan3
	 * 2014年7月9日
	 */
	public List<SysUser> getAllNameIds(String userid,String deptId,String username) {
		String hql = "from SysUser u where 1=1";
		if(userid!=null && !"".equals(userid)){
			hql += " and u.userid!='"+userid+"'";
		}
		if(deptId!=null && !"".equals(deptId)){
			hql += " and u.deptName='"+deptId+"'";
		}
		if(username!=null && !"".equals(username)){
			hql += " and (u.userid like '%"+username+"%' or u.username like '%"+username+"%')";
		}
		return dao.getAllNameIds(hql);
	}

	/**
	 * 根据项目id获取项目关联
	 * @param projectId
	 * @return
	 * lhyan3
	 * 2014年6月30日
	 */
	public List<UserProject> getUserProject(String planStartTime,String planEndTime,String factStartTime,String factEndTime) {
		return dao.getUserProject(planStartTime,planEndTime,factStartTime,factEndTime);
	}

	/**
	 * group by time
	 * @param projectId
	 * @return
	 * lhyan3
	 * 2014年6月30日
	 */
	public List<Object[]> getUserProjectByTime(String projectId) {
		return dao.getUserProjectByTime(projectId);
	}

	/**
	 * 判断关联是否存在
	 * @param string
	 * @param projectId
	 * @return
	 * lhyan3
	 * 2014年6月30日
	 */
	public UserProject getUserProject(String userid, String projectId) {
		return dao.getUserProject(userid,projectId);
	}
	/**根据项目id获取该项目关联的userProject对象列表
	 * wtjiao 2014年8月15日 上午8:45:02
	 * @param projectId
	 */
	public List<UserProject> getUserProjectsByProjectId(String projectId) {
		return dao.getUserPojectByProjectId(projectId);
	}

	/**
	 * 删除关联
	 * @param exist
	 * lhyan3
	 * 2014年6月30日
	 */
	public void deleteUserProject(UserProject exist) {
		dao.deleteUserProject(exist);
	}
	



	/**
	 * 历史记录
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * lhyan3
	 * 2014年7月2日
	 */
	public Page getHistoryPage(int pageNum, int pageSize,String deptcode,String projectId,String userid,String entryTime,String exitTime) {
		String hql = "from UserProjectHistory h where 1=1 ";
		if(userid!=null && !"".equals(userid)){
			hql += " and h.userid = '"+userid+"'";
		}else{
			if(deptcode!=null && !"".equals(deptcode)){
				String usersql = "select u.userid from SysUser u where u.deptcode='"+deptcode+"'";
				hql += " and h.userid in ("+usersql+")";
			}
			if(projectId!=null && !"".equals(projectId)){
				hql += " and h.projectId = '"+projectId+"'";
			}
		}
		if(entryTime!=null && !"".equals(entryTime)){
			entryTime += " 00:00:00";
			hql += " and h.entryTime >= to_date('"+entryTime+"','yyyy-MM-dd HH24:mi:ss')";
		}
		if(exitTime!=null && !"".equals(exitTime)){
			exitTime += "23:59:59";
			hql += " and h.entryTime <= to_date('"+exitTime+"','yyyy-MM-dd HH24:mi:ss')";
		}
		return dao.getHistoryPage(hql,pageNum,pageSize);
	}


	/**获取名为"其他项目"的项目
	 * wtjiao 2014年7月3日 上午9:02:39
	 * @return
	 */
	public Project getOtherProject() {
		return dao.getOtherProject();
	}
	
	public List<Object[]> findUserListByGrpDept(String grpcode,
			String deptvalue, String groupValue, String deptValue2) {
		return dao.findUserListByGrpDept(grpcode, deptvalue,groupValue,deptValue2);
	}


	/**
	 * 根据用户名获取所有项目名称
	 * @param userid
	 * @return
	 * lhyan3
	 * 2014年7月17日
	 */
	public String getProjectNameByUserid(String userid) {
		return dao.getProjectNameByUserid(userid);
	}


	/**
	 * 根据项目获取项目经理
	 * @param code
	 * @return
	 * lhyan3
	 * 2014年7月29日
	 */
	public SysUser getManagerByprojectId(String projectId) {
		return dao.getManagerByProjectId(projectId);
	}


	public void deleteAttendance(Project project) {
		dao.deleteAttendance(project);
	}


	public void deleteResourcePlan(Project project) {
		dao.deleteResourcePlan(project);
	}

	public void saveResourcePlans(List<ProjectResourcePlan> plans) {
		for (ProjectResourcePlan projectResourcePlan : plans) {
			dao.addObject(projectResourcePlan);
		}
	}

	public void saveAttendances(List<ProjectAttendance> attendances){
		for (ProjectAttendance att : attendances) {
			dao.addObject(att);
		}
	}

	public void saveUserProject(UserProject up) {
		dao.addObject(up);
	}

	public List<Project> listAllGroupBriefExceptOtherProject() {
		return dao.listAllGroupBriefExceptOtherProject();
	}


	public List<Project> listAllGroupBriefProject() {
		return dao.listAllGroupBriefProject();
	}
	
	/**
	 * 根据是否完结排序获取项目Page
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public Page getPagebyNum(int pageNum, int pageSize,String prjName,String isEndValue) {
		return dao.getPagebyNum(pageNum, pageSize,prjName,isEndValue);
	}
	
	/**
	 * 根据任务计划的计划ID查询出所属 的项目
	 * @param planId 计划ID
	 * @return 所属 的项目
	 */
	public Project getProjectByWeekPlanTask(String planId){
		return dao.getProjectByWeekPlanTask(planId);
	}
	
	/**
	 * 根据项目名称找相似的，如果有多条，则取第一条
	 * @param prjname
	 * @return
	 */
	public String getProjectNameByInfo(String prjname){
		return dao.getProjectNameByInfo(prjname);
	}
	
	/**
	 * 获取当前用户负责的项目
	 * @return
	 */
	public List<Project> getMyDutyPrjname(String userid) {
		return dao.getMyDutyPrjname(userid);
	}
	/**
	 * 根据项目名称获取项目的id
	 * @param prjGroup  项目名称
	 * ljlian2 2015-03-09
	 */

	public List<Project> getProjectIdByName(String prjGroup) {
		return dao.getProjectIdByName(prjGroup);
	}

	/**
	 * 查询项目的信息
	 * @param projectName  项目名称
	 * @param isEndValue  是否结束（项目的状态）
	 * @return
	 */
	public List<Project> getProjectListByCondition(String projectName, String isEndValue) {
		// TODO Auto-generated method stub
		List<Project> list = dao.getProjectListByCondition(projectName,isEndValue);
		return list;
	}


	
}
