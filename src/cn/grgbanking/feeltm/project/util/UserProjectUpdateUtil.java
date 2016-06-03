package cn.grgbanking.feeltm.project.util;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.grgbanking.feeltm.domain.SysUser;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.project.domain.Project;
import cn.grgbanking.feeltm.project.domain.ProjectResourcePlan;
import cn.grgbanking.feeltm.project.domain.UserProject;
import cn.grgbanking.feeltm.project.service.ProjectService;
import cn.grgbanking.feeltm.staff.service.StaffInfoService;
import cn.grgbanking.feeltm.util.Constants;

@Service
public class UserProjectUpdateUtil {
	@Autowired
	private StaffInfoService staffInfoService;
	@Autowired
	private ProjectService projectService;
	private HttpServletRequest request; 
	
	
	/**将ProjcetResourcePlan 中的数据同步到UserProject中
	 * wtjiao 2014年9月5日 上午8:51:01
	 */
	public void synchronizeUserProject(){
	    SysLog.info(new Date()+"项目信息开始同步");
		List<Project> projects=projectService.listAllGroup();
		if(projects!=null){
			for(Project p:projects){
				operateSynchronize(p);
			}
		}
		SysLog.info(new Date()+"项目信息同步结束");
	}

	/**同步某个项目中的人员
	 * wtjiao 2014年9月5日 上午8:54:50
	 * @param projectId
	 */
	public void synchronizeUserProject(Project p){
		if(p!=null){
			operateSynchronize(p);
		}
	}
	
	private void operateSynchronize(Project p){
		try{
			List<ProjectResourcePlan> rps=p.getProjectResourcePlan();
			if(rps!=null){
				//删除现有UserProject对象
				projectService.deleteUserProject(p);
				Set<String> distinctUserSet=new HashSet<String>();
				for (ProjectResourcePlan rp : rps) {
					//当前日期
					long nowDate=Calendar.getInstance().getTime().getTime();
					//计划中的日期，起止时间
					long startDate=rp.getFactStartTime().getTime();
					long endDate=rp.getFactEndTime().getTime();
					String userId=rp.getUserid();
					SysUser user=staffInfoService.findUserByUserid(userId);
					//当前日期在计划分配表中的指定时间内，并且在前面的循环中该人员没有被加入UserProject
					if(userId!=null && user!=null && startDate<=nowDate && endDate>=nowDate && !distinctUserSet.contains(userId)){
						//该员工已离职,则不再加入项目组,同时在ResourcePlan中更新其项目离开时间
						if(user.getGrgEndDate()!=null && user.getGrgEndDate().getTime()<=nowDate){
							rp.setFactEndTime(user.getGrgEndDate());
							projectService.update(rp);
							continue;//判断下一个人员
						}
						distinctUserSet.add(userId);//该人员已经加入
						UserProject up=new UserProject();
						up.setUserid(userId);
						up.setUsername(rp.getUsername());
						up.setDeptName(user.getDeptName());
						up.setEntryTime(rp.getFactStartTime());
						up.setExitTime(rp.getFactEndTime());
						up.setPhone(user.getMobile());
						up.setProject(p);
						up.setProjectDuty(rp.getProjectDuty());
						up.setProjectRole(rp.getProjectRole());
						up.setProjectname(p.getName());
						up.setUpdateTime(new Date());
						if(request!=null){
							up.setUpdateuserId(((UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY)).getUserid());
							up.setUpdateUsername(((UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY)).getUsername());
						}else{
							up.setUpdateuserId("admin");
							up.setUpdateUsername("系统管理员");
						}
						projectService.saveUserProject(up);
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public UserProjectUpdateUtil registerRequest(HttpServletRequest request) {
		this.request = request; 
		return this;
	}

}
