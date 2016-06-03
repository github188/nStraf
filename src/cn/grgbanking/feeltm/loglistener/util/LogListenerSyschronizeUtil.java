package cn.grgbanking.feeltm.loglistener.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.grgbanking.feeltm.config.BusnDataDir;
import cn.grgbanking.feeltm.domain.SysUser;
import cn.grgbanking.feeltm.loglistener.domain.LogListenerMonitor;
import cn.grgbanking.feeltm.loglistener.service.LogListenerService;
import cn.grgbanking.feeltm.project.domain.Project;
import cn.grgbanking.feeltm.project.service.ProjectService;
import cn.grgbanking.feeltm.staff.service.StaffInfoService;

@Service
public class LogListenerSyschronizeUtil {
	@Autowired
	private LogListenerService service;
	@Autowired
	private ProjectService projectService;
	@Autowired
	private StaffInfoService staffInfoService;
	
	/**全局同步最新的项目中的人员信息
	 * wtjiao 2014年11月21日 上午9:11:08
	 */
	public void sychronizeLogListener(){
		List<LogListenerMonitor> list=service.getAutoUpdateListener();
		if(list!=null){
			for(int i=0;i<list.size();i++){
				LogListenerMonitor lis=list.get(i);
				if("project".equals(lis.getOrgType())){
					Project p=projectService.getProjectById(lis.getOrgId());
					LogListenerMonitor moni=new LogListenerMonitor();
					//设置项目
					moni.setOrgId(p.getId());
					moni.setOrgName(p.getName());
					
					//获取监控人id
					List<SysUser> monitors=new ArrayList<SysUser>();
					monitors.addAll(service.getRoleUser_ProjectManager(p));
					monitors.addAll(service.getRoleUser_DeptManager(p));
					moni.setMonitorId(service.toUserIds(monitors));
					moni.setMonitorName(service.toUserNames(monitors));
					//获取被监控人
					List<SysUser> watched=service.getRoleUser_ProjectMember(p);
					moni.setWatchedId(service.toUserIds(watched));
					moni.setWatchedName(service.toUserNames(watched));
					
					//设置其他
					moni.setAutoUpdate(lis.getAutoUpdate());
					moni.setOrgType(lis.getOrgType());
					
					service.delete(lis.getId());
					service.save(moni);
				}else{
					String deptName=staffInfoService.getDeptNameValueByKey(lis.getOrgId());
					LogListenerMonitor moni=new LogListenerMonitor();
					
					//设置项目
					moni.setOrgId(lis.getOrgId());
					moni.setOrgName(deptName);
					
					//获取监控人id
					List<SysUser> deptManagers=staffInfoService.getAllNotLeaveDeptManagerByDept(lis.getOrgId());
					moni.setMonitorId(service.toUserIds(deptManagers));
					moni.setMonitorName(service.toUserNames(deptManagers));
					
					//获取被监控人
					
					String expectUserIds=BusnDataDir.getObjectListInOrder("costControl.statisticDeptExpectUserId").get(0).getValue();//不需要监控的人员
					List<SysUser> watched=staffInfoService.getUserByDeptExpecetTheseUsers(lis.getOrgId(), expectUserIds);
					moni.setWatchedId(service.toUserIds(watched));
					moni.setWatchedName(service.toUserNames(watched));
					
					//设置其他
					moni.setAutoUpdate(lis.getAutoUpdate());
					moni.setOrgType(lis.getOrgType());
					
					service.delete(lis.getId());
					service.save(moni);
				}
			}
		}
		
	}
	
}
