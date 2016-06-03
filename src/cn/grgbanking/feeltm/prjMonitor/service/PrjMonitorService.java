package cn.grgbanking.feeltm.prjMonitor.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.grgbanking.feeltm.config.BusnDataDir;
import cn.grgbanking.feeltm.domain.SysDatadir;
import cn.grgbanking.feeltm.domain.SysUser;
import cn.grgbanking.feeltm.prjMonitor.bean.StaffBean;
import cn.grgbanking.feeltm.prjMonitor.dao.ColorSettingDao;
import cn.grgbanking.feeltm.prjMonitor.dao.PrjMonitorDAO;
import cn.grgbanking.feeltm.prjMonitor.domain.DeptColor;
import cn.grgbanking.feeltm.project.dao.ProjectDao;
import cn.grgbanking.feeltm.project.domain.Project;
import cn.grgbanking.feeltm.project.domain.UserProject;
import cn.grgbanking.feeltm.project.service.ProjectService;
import cn.grgbanking.feeltm.staff.dao.StaffInfoDao;
import cn.grgbanking.feeltm.staff.service.StaffInfoService;
import cn.grgbanking.framework.service.BaseService;

@Service
public class PrjMonitorService extends BaseService {
	@Autowired
	private ProjectDao projectGroupDao;
	@Autowired
	private ProjectService projectService;
	@Autowired
	private PrjMonitorDAO prjMonitorDao;
	@Autowired
	private StaffInfoService staffInfoService;
	@Autowired
	private StaffInfoDao staffInfoDao;
	@Autowired
	private ColorSettingDao colorSettingDao;
	
	//private Map<String,List<Project>> mgrPrjMap ;//项目经理所在项目
	private Map<String,List<Project>> mbrPrjMap;//员工所在项目


	/**
	 * 获取人力资源分布map
	 * @param 
	 * @return Map
	 * @author lping1 2014-9-23
	 */
	public Map getStaffAllocMap() {
		Map map = BusnDataDir.getMapKeyValue("projectMonitor.projectType");
		List<SysDatadir> prjTypeList = BusnDataDir.getObjectListInOrder("projectMonitor.projectType");//项目类型LIST
		Map prjTypeMap = new LinkedHashMap<String, Map>();// <项目类型，项目>
		//mgrPrjMap = new HashMap();//项目经理所在项目个数
		mbrPrjMap = new HashMap();//员工所在项目个数
		List<DeptColor>  deptList = prjMonitorDao.getMonDeptColor();//被监控的部门
		List<Project> projects = projectService.listAllGroupBriefExceptOtherProject();//获得“其它项目”以外的项目
		if (prjTypeList != null) {
			//遍历项目类型，查询该类型下的项目
			for (SysDatadir type : prjTypeList) {
				List<Project> prjList  =new ArrayList();
				for(Project prj : projects){
					//获取已完结以外的该类型的所有项目
					if(type.getKey().equals(prj.getProjectType())){
						prjList.add(prj);
					}
				}
				//Map prjNameMap = new TreeMap<String, List>();// <项目名称，组员>
				//使用LinkedHashMap，否则不会按添加的先后排序
				Map prjNameMap = new LinkedHashMap<String, List>();// <项目名称，组员>
				if (prjList != null) {
					//遍历项目，寻找该项目下所有成员，包括项目经理
					for (int i= 0;i<prjList.size();i++ ) {
						Project prj = prjList.get(i);
						 
						List<UserProject> userList = projectGroupDao.getUserPojectByProjectId(prj.getId());
						List<StaffBean> userNameList = new ArrayList();
						//Map userMap = new TreeMap<String, String>();// <组员名称，部门>
						//使用LinkedHashMap，否则不会按添加的先后排序
						Map userMap = new LinkedHashMap<String, String>();// <组员名称，部门>
						StaffBean staff ;//员工BEAN
						SysUser prjManager = staffInfoService
								.findUserByUserid(prj.getProManagerId());
						List<Project> userPrjList;
						//如果是被监控的部门才加入list
						if (prjManager != null && isMonDept(prjManager.getDeptName())) {
							String deptId = prjManager.getDeptName();
							//添加项目经理
							 for(DeptColor d : deptList){
								 if(deptId!=null && deptId.equals(d.getDeptId())){
									 staff = new StaffBean();
									 //统计项目经理所在项目个数
									 String userid = prjManager.getUserid();
									 List<Project> userPrj = mbrPrjMap.get(userid);
									 if(userPrj==null){
										 userPrj = new ArrayList();
									 }
									 if (!userPrj.contains(prj)) {
										 userPrj.add(prj);
									 }
									 //mgrPrjMap.put(userid,userPrj);
									 mbrPrjMap.put(userid, userPrj);
									
									 staff.setUserId(prjManager.getUserid());
									 boolean isOnjob = staffInfoDao.beonthejob(userid);
									 if(isOnjob){
										 staff.setUserName(prjManager.getUsername());
										 staff.setPrjList(userPrj);
									 }else{
										 staff.setUserName("无");
									 }
									 staff.setDeptId(prjManager.getDeptName());
									 staff.setDeptName(BusnDataDir.getValue(map,prjManager.getDeptName()));
									 staff.setDeptColor(d.getDeptColorVal());
									 userNameList.add(staff);// 将项目经理放在首位
									 break;
								 }
							 }
						}
						List<DeptColor> deptColorList = getMonDeptColor();
						//添加项目成员
						if (userList != null) {
							for(DeptColor d : deptColorList){
								for (UserProject user : userList) {
									String deptId = user.getDeptName();
									userMap = new TreeMap<String, String>();
									//如果是被监控的部门才加入list
									if(deptId!=null && deptId.equals(d.getDeptId())){
										staff = new StaffBean();
										//统计员工所在项目个数
										String userid = user.getUserid();
										 List<Project> userPrj = mbrPrjMap.get(userid);
										 if(userPrj==null){
											 userPrj = new ArrayList();
										 }
										 if (!userPrj.contains(prj)) {
											 userPrj.add(prj);
										 }
										 mbrPrjMap.put(userid,userPrj);
										 staff.setPrjList(userPrj);
										 staff.setUserId(user.getUserid());
										 staff.setUserName(user.getUsername());
										 staff.setDeptId(user.getDeptName());
										 staff.setDeptName(BusnDataDir.getValue(map,user.getDeptName()));
										 staff.setDeptColor(d.getDeptColorVal());
										 boolean isOnjob = staffInfoDao.beonthejob(userid);
										 if(isOnjob){
											 userNameList.add(staff);
										 }
										 
									}
								}
							}
						}
						prjNameMap.put(prj.getName(), userNameList);
					}
				}
				prjTypeMap.put(type.getValue(), prjNameMap);
			}
		}
		return prjTypeMap;
	}
		
	/**
	 * 判断是否是被监控的部门
	 * @param deptId 部门ID
	 * @return true:是 false:否
	 * */
	public boolean isMonDept(String deptId){
		boolean flag = false;
		List<DeptColor> deptColorList = getMonDeptColor();
		for(DeptColor d : deptColorList){
			if(deptId!=null && deptId.equals(d.getDeptId())){
				flag = true;
				break;
			}
		}
		return flag;
	}
	
	
	/**
	 * 获取被监控的部门及颜色
	 * @return 
	 * */
	public List<DeptColor> getMonDeptColor(){
		Map deptMap=BusnDataDir.getMapKeyValue("staffManager.department");
		List<DeptColor>  deptColorList = prjMonitorDao.getMonDeptColor();
		List<SysDatadir> deptDirList  = BusnDataDir.getObjectListInOrder("staffManager.department");
		List<DeptColor> temp = new ArrayList();
		//按照数据字典排序顺序进行添加
		 for(SysDatadir sdd : deptDirList){
			 String deptId = sdd.getKey();
			 for(DeptColor d : deptColorList){
				 if(deptId!=null && deptId.equals(d.getDeptId())){
					 d.setDeptName(sdd.getValue());
					 temp.add(d);
				 }
			 }
		 }
		return temp;
	}
	
	/**
	 * 获取空闲人员
	 */
	public List<StaffBean> getRestStaff(){
		//20150319 wtjiao 增加指定人员不监控功能
		String notMonitors=getNotMonitors();
		List<SysUser> userList = staffInfoDao.getRestStaff();//获取空闲人员
		List<StaffBean> restList = new ArrayList();
		List<DeptColor> deptList = getMonDeptColor();//获取被监控的部门及颜色
		StaffBean bean;
		if(deptList!=null &&deptList.size()>0){
			for(DeptColor d : deptList){
				if(deptList!=null && deptList.size()>0){
					for(SysUser user : userList){
						if(d!=null && user!=null ){
							boolean inNotMonitors=inNotMonitors(notMonitors,user.getUserid());//在不在被监控的人员里面
							String deptId = user.getDeptName();
							bean = new StaffBean();
							//在被监控的部门中 空闲人员是需要在人力白白板中显示（空闲人员指的是不在任何一个项目中并且也不在人力资源白板的额外不需要监控的人员列表中）
							//将这些人放入到 人力资源白板的空闲资源池中
							if(deptId!=null && deptId.equals(d.getDeptId()) && !inNotMonitors){
								bean.setUserName(user.getUsername());
								bean.setDeptId(deptId);
								bean.setDeptColor(d.getDeptColorVal());
								restList.add(bean);
							}
						}
					}
				}
			}
		}
		return restList;
	}
	
	/**用户是否在不监控人员列表中
	 * @param notMonitors 不监控人员列表
	 * @param userid 指定人员（不在任何一个项目中）
	 * @return
	 */
	private boolean inNotMonitors(String notMonitors, String userid) {
		if(StringUtils.isNotBlank(notMonitors)){
			String[] monitors=notMonitors.split(",");
			for(String moni:monitors){
				if(moni.equals(userid)){
					return true;
				}
			}
		}
		return false;
	}

	/**获取不监控人员列表
	 * @return
	 */
	private String getNotMonitors() {
		//获取人力资源白板  额外不需要监控的人
		List<SysDatadir> dirs=BusnDataDir.getObjectListInOrder("projectMonitor.staffAllocation");
		if(dirs!=null){
			for(int i=0;i<dirs.size();i++){
				if(dirs.get(i).getKey().equals("notInRestUserIds")){
					return dirs.get(i).getValue();
				}
			}
		}
		return "";
	}

	/**
	 * 根据部门ID获取部门统计数据
	 * @return List<String> [部门名称,部门颜色,部门总人数,空闲人数,本月新增人数]
	 * */
	public List<String> getCountsByDeptId(String deptId){
		 List<SysUser> userList = staffInfoDao.getUsersByDeptId(deptId);//部门人员列表
		 List<StaffBean> restList =  getRestStaff();//闲置人员
		 List<DeptColor> deptList = getMonDeptColor();//部门颜色
		 List<String> countList = new ArrayList();//
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M");
		 String now = sdf.format(new Date());//当前年月
		 int newCounts = 0;
		 int restCounts = 0;
		 //获取部门名称及颜色
		 for(DeptColor d : deptList){
			 if(deptId!=null && deptId.equals(d.getDeptId())){
				 countList.add(d.getDeptName());
				 countList.add(d.getDeptColorVal());
				 break;
			 }
		 }
		 countList.add(String.valueOf(userList.size()));//部门总人数
		 //统计空闲人数
		 for(StaffBean bean : restList){
				 if(deptId!=null && deptId.equals(bean.getDeptId())){
					 restCounts++;
				 }
		 }
		 if(userList!=null  && userList.size()>0){
			 try {
				 //统计本月新增人数
				 for(SysUser user : userList){
					 if(user == null){
						 break;
					 }
					 Date d=user.getGrgBegindate();
					 if(d!=null){
						 String when = sdf.format(d);//入职年月
						 if(when.equals(now)){
							 newCounts++;
						 } 
					 }else{
						 continue;
					 }
					
				 }
			 } catch (Exception e) {
					// TODO: handle exception
				 logger.error(e.getMessage());
				}
		 }
		
		countList.add(String.valueOf(restCounts));
		countList.add(String.valueOf(newCounts));
		return countList;
	}
}
