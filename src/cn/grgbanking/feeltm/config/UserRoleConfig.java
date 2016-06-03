package cn.grgbanking.feeltm.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import cn.grgbanking.feeltm.context.BaseApplicationContext;
import cn.grgbanking.feeltm.domain.SysUser;
import cn.grgbanking.feeltm.domain.UsrGroup;
import cn.grgbanking.feeltm.login.dao.LoginDao;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.project.dao.ProjectDao;

/**
 * 2014-4-28
 * @author lhyan3
 * 权限控制参数配置
 */
public class UserRoleConfig {
	
	//管理员
	public static String admin = "administrator";
	
	//公司领导
	public static String leader = "leader";
	
	//人事行政
	public static String hr ="hr";
	
	//项目管理组
	public static String prjManagerGroup="prjManageGroup";
	
	//部门经理组
	public static String deptManagerGroup = "deptManager";
	
	//项目经理组
	public static String projectManagerGroup = "groupManager";
	
	//确认工时权限组
	public static String confirmRightGroup = "administrator,groupManager,prjManageGroup";
	
	static{
		if(StringUtils.isNotBlank(Configure.getProperty("defualtAdminGroup"))){
			admin=Configure.getProperty("defualtAdminGroup");
		}
		if(StringUtils.isNotBlank(Configure.getProperty("defaultHrGroup"))){
			hr=Configure.getProperty("defaultHrGroup");
		}
		if(StringUtils.isNotBlank(Configure.getProperty("prjManagerGroup"))){
			prjManagerGroup=Configure.getProperty("prjManagerGroup");
		}
		if(StringUtils.isNotBlank(Configure.getProperty("leaderGroup"))){
			leader=Configure.getProperty("leaderGroup");
		}
		if(StringUtils.isNotBlank(Configure.getProperty("deptManagerGroup"))){
			deptManagerGroup=Configure.getProperty("deptManagerGroup");
		}
		if(StringUtils.isNotBlank(Configure.getProperty("projectManagerGroup"))){
			projectManagerGroup=Configure.getProperty("projectManagerGroup");
		}
		if(StringUtils.isNotBlank(Configure.getProperty("confirmRightGroup"))){
			confirmRightGroup=Configure.getProperty("confirmRightGroup");
		}
	}
	
	/**
	 * 把组别转化成list
	 * @param userModel
	 * @return
	 * lhyan3
	 * 2014年7月28日
	 */
	public static List<String> getGrpList(UserModel userModel){
		String groupcodes[] = userModel.getGroupids();
		List<String> list = new ArrayList<String>();
		if(groupcodes!=null && groupcodes.length>0){
			//如果当前用户存在组权限
			for(int i=0;i<groupcodes.length;i++){
				list.add(groupcodes[i]);
			}
		}
		return list;
	}
	
	/**
	 * lhy 2014-4-29
	 * 根据当前登录用户获取可查看数据的组
	 * @param userModel 当前登陆用户
	 * @param usrGroups 所有组
	 * @return
	 */
	public static String getGroupByUsermodel(UserModel userModel, List<UsrGroup> usrGroups){
		String groupcodes[] = userModel.getGroupids();
		List<String> list = new ArrayList<String>();
		String str = "";
		if(groupcodes!=null && groupcodes.length>0){
			//如果当前用户存在组权限
			for(int i=0;i<groupcodes.length;i++){
				list.add(groupcodes[i]);
				str += groupcodes[i]+",";
			}
			if(contains(list, admin)|| contains(list, hr)){
				str = "";
				if(usrGroups!=null && usrGroups.size()>0){
					for(UsrGroup u:usrGroups){
						str += u.getGrpcode()+",";
					}
				}
			}
			str = str.substring(0, str.length()-1);
			return str;
		}else{
			//如果没有组
			return str;
		}
	}
	/**
	 * 2014-10-27 whxing	
	 * 权限中是否包含管理员权限
	 * @return
	 */
	public static boolean upAdminManager(UserModel userModel){
		List<String> list = getGrpList(userModel);
		if(contains(list, admin)){
			return true;
		}
		return false;
	}
	
	
	/**
	 * 2014-4-29 lhy
	 * 项目经理以上以及管理员
	 * @return
	 */
	public static boolean upGrpManager(UserModel userModel){
		List<String> list = getGrpList(userModel);
		if(contains(list, leader) || contains(list, admin) || contains(list, deptManagerGroup) || contains(list, projectManagerGroup)){
			return true;
		}
		return false;
	}
	
	/**
	 * 2014-4-29 lhy
	 * 部门经理以上以及管理员
	 * @return
	 */
	public static boolean upDeptManager(UserModel userModel){
		List<String> list = getGrpList(userModel);
		if(contains(list, leader)|| contains(list, admin)|| contains(list, deptManagerGroup)){
			return true;
		}
		return false;
	}
	
	/**
	 * 2014-4-29 lhy
	 * 是否是管理员或人事
	 * @return
	 */
	public static boolean ifAdministratorOrHr(UserModel userModel){
		List<String> list = getGrpList(userModel);
		//如果当前用户存在组权限
		if(contains(list, hr) || contains(list, admin)|| contains(list, leader)){
			return true;
		}
		return false;
	}
	/**
	 * 是否是人事
	 * @return 
	 */
	public static boolean ifHr(UserModel userModel){
		List<String> list = getGrpList(userModel);
		if(contains(list, hr)){
			return true;
		}
		return false;
	}
	
	/**是否在项目管理组中
	 * wtjiao 2014年7月3日 上午9:58:01
	 * @param userModel
	 * @return
	 */
	public static boolean isInPrjManageGroup(UserModel userModel){
		List<String> list = getGrpList(userModel);
		
		if(contains(list,prjManagerGroup)){
			return true;
		}
		return false;
	}
	
	private static boolean contains(List list,String str){
		if(list!=null){
			for(int i=0;i<list.size();i++){
				String str1=(String)list.get(i);
				if(str1.trim().equals(str.trim())){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 是否是部门经理
	 * @param userModel
	 * @return
	 * lhyan3
	 * 2014年7月28日
	 */
	public static boolean isDeptManageGroup(UserModel userModel){
		List<String> list = getGrpList(userModel);
		if(contains(list, deptManagerGroup)){
			return true;
		}
		return false;
	}
	
	/**
	 * 是否是项目经理
	 * @param userModel
	 * @return
	 * lhyan3
	 * 2014年7月28日
	 */
	public static boolean isProjectManageGroup(UserModel userModel){
		List<String> list = getGrpList(userModel);
		if(!isDeptManageGroup(userModel)){
			//判断是否是项目经理
			if(contains(list, projectManagerGroup)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 获取该登录用户的项目经理和部门经理
	 * @param userModel
	 * @return
	 */
	public static List<SysUser> getUpPrjListByUser(UserModel userModel){
		ProjectDao groupDao = (ProjectDao)BaseApplicationContext.getAppContext().getBean("projectDao");
		return groupDao.getUpPrjListByUser(userModel);
	}
	
	public static List getJob(String userid){
		LoginDao log = (LoginDao) BaseApplicationContext.getAppContext().getBean("loginDao");
		String[] jobs = log.searchUsrGrp(userid);
		List<String> list = new ArrayList<String>();
		if(jobs!=null && jobs.length>0){
			//如果当前用户存在组权限
			for(int i=0;i<jobs.length;i++){
				list.add(jobs[i]);
			}
		}
		return list;
	}
	
	public static boolean isProjectManager(String userid){
		if(UserRoleConfig.getJob(userid).contains(UserRoleConfig.deptManagerGroup)){
			return true;	//项目管理部，项目经理
		}
		ProjectDao groupDao = (ProjectDao)BaseApplicationContext.getAppContext().getBean("projectDao");
		if(groupDao.getPrjByManager(userid).size()>0){
			return true;	//项目负责人
		}
		return false;
	}
	
	/**
	 * 是否拥有确认工时权限
	 * @param  userModel 用户类
	 * @return boolean true:是 false:否
	 * @author lping1 2014-10-16
	 */
	public static boolean hasConfirmRight(UserModel userModel){
		String [] usrGrpIds = userModel.getGroupids();
		//检查用户是否项目经理以上
		boolean hasComfirmRight = false;
		 String[] authGroupArr =  confirmRightGroup.split(",");
		for(String grpId : authGroupArr){
			for(String usrGrpId : usrGrpIds){
				if(grpId.equals(usrGrpId)){
					hasComfirmRight = true;
					break;
				}					
			}
			if(hasComfirmRight){
				break;
			}
		}
		return hasComfirmRight;
	}
	
	/**
	 * 获取该登录用户的项目经理和部门经理
	 * @param userModel
	 * @return
	 */
	public static List<SysUser> getUserRoleInfoByUser(UserModel userModel,String deptid){
		ProjectDao groupDao = (ProjectDao)BaseApplicationContext.getAppContext().getBean("projectDao");
		return groupDao.getUserRoleInfoByUser(userModel,deptid);
	}
	
	/**
	 * 获取行政人员及财务人员
	 * @param userModel
	 * @return
	 */
	public static List<SysUser> getHrOaFinaByUser(UserModel userModel){
		ProjectDao groupDao = (ProjectDao)BaseApplicationContext.getAppContext().getBean("projectDao");
		return groupDao.getHrOaFinaByUser(userModel);
	}
	
	/**
	 * 获取财务人员数据
	 * @param userModel
	 * @return
	 */
	public static List<SysUser> getFinaByUser(){
		ProjectDao groupDao = (ProjectDao)BaseApplicationContext.getAppContext().getBean("projectDao");
		return groupDao.getFinaByUser();
	}
	
	/**
	 * 领导首页访问权限控制
	 * @param userModel
	 * @return 领导首页访问权限 true:能访问  false:不能访问
	 */
	public static boolean canAccessLeaderHome(UserModel userModel){
		List<String> dirGrpList = getGrpList(userModel);
		Map<String,String> leaderGroupMap = BusnDataDir.getMapKeyValue("leaderHomePage.leaderGroup");
		String leaderGroup = "";
		List<String> cfgGrpList = new ArrayList<String>();
		if (leaderGroupMap != null) {
			String leaderGroupTemp = leaderGroupMap.get("group");
			if (StringUtils.isNotBlank(leaderGroupTemp)) {
				leaderGroup = leaderGroupTemp;
			}else{
				leaderGroup = "hr,leader,director,administrator,deptManager";
			}
			cfgGrpList = Arrays.asList(leaderGroup.trim().split(","));
		} else {
			leaderGroup = "hr,leader,director,administrator,deptManager";
			cfgGrpList = Arrays.asList(leaderGroup.split(","));
		}
		
		for (int j = 0; j < cfgGrpList.size(); j++) {
			if (dirGrpList.contains(cfgGrpList.get(j))) {
				return true;
			}
		}
		return false;
	}
}
