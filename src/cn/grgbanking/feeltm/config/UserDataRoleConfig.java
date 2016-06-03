package cn.grgbanking.feeltm.config;

import java.util.ArrayList;
import java.util.List;

import cn.grgbanking.feeltm.context.BaseApplicationContext;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.um.dao.SysUserGroupDao;

public class UserDataRoleConfig {
	/**
	 * 查看全部数据的组
	 * @param userModel
	 * @return
	 */
	public static boolean viewAllData(UserModel userModel, String holsall){
		List<String> list = getGrpList(userModel);
		if("".equals(holsall) || "null".equals(holsall) || holsall==null){
			return false;
		}
		//如果当前用户存在组权限
		String[] holsalls = holsall.split("]");
		if(holsalls.length>1){
			String[] users = holsalls[1].substring(1).split(",");
			for(int i=0;i<users.length;i++){
				if(!"".equals(users[i])){
					if(users[i].equals(userModel.getUserid())){
						list.add(users[i]);
					}
				}
			}
		}
		holsall = holsall.replace("[", "").replace("]","");
		for(int i=0;i<holsall.split(",").length;i++){
			if(contains(list, holsall.split(",")[i])){
				return true;
			}
		}
		return false;
	}
	/**
	 * 根据用户名及键值，得出用户角色
	 * @param userid
	 * @param key
	 * @return
	 */
	public static boolean viewAllDataByUserid(String userid, String key){
		SysUserGroupDao groupDao = (SysUserGroupDao)BaseApplicationContext.getAppContext().getBean("sysUserGroupDao");
		String holsall = Configure.getProperty(key);
		List<String> list = groupDao.getgroupcodeByUserid(userid);
		if(holsall==null){
			return false;
		}
		//如果当前用户存在组权限
		String[] holsalls = holsall.split("]");
		if(holsalls.length>1){
			String[] users = holsalls[1].substring(1).split(",");
			for(int i=0;i<users.length;i++){
				if(!"".equals(users[i])){
					if(users[i].equals(userid)){
						list.add(users[i]);
					}
				}
			}
		}
		holsall = holsall.replace("[", "").replace("]","");
		for(int i=0;i<holsall.split(",").length;i++){
			if(contains(list, holsall.split(",")[i])){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 查看部门数据
	 * @param userModel
	 * @return
	 */
	public static boolean viewDeptData(UserModel userModel, String holsdept){
		List<String> list = getGrpList(userModel);
		if("".equals(holsdept) || "null".equals(holsdept) || holsdept==null){
			return false;
		}
		//如果当前用户存在组权限
		String[] holsdepts = holsdept.split("]");
		if(holsdepts.length>1){
			String[] users = holsdepts[1].substring(1).split(",");
			for(int i=0;i<users.length;i++){
				if(!"".equals(users[i])){
					if(users[i].equals(userModel.getUserid())){
						list.add(users[i]);
					}
				}
			}
		}
		holsdept = holsdept.replace("[", "").replace("]","");
		for(int i=0;i<holsdept.split(",").length;i++){
			if(contains(list, holsdept.split(",")[i])){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 查看项目组数据
	 * @param userModel
	 * @return
	 */
	public static boolean viewProjectData(UserModel userModel, String holsproject){
		List<String> list = getGrpList(userModel);
		if("".equals(holsproject) || "null".equals(holsproject) || holsproject==null){
			return false;
		}
		//如果当前用户存在组权限
		String[] holsprojects = holsproject.split("]");
		if(holsprojects.length>1){
			String[] users = holsprojects[1].substring(1).split(",");
			for(int i=0;i<users.length;i++){
				if(!"".equals(users[i])){
					if(users[i].equals(userModel.getUserid())){
						list.add(users[i]);
					}
				}
			}
		}
		holsproject = holsproject.replace("[", "").replace("]","");
		for(int i=0;i<holsproject.split(",").length;i++){
			if(contains(list, holsproject.split(",")[i])){
				return true;
			}
		}
		return false;
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
	
}
