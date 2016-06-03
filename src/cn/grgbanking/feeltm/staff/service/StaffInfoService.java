package cn.grgbanking.feeltm.staff.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.config.BusnDataDir;
import cn.grgbanking.feeltm.contact.service.ContactService;
import cn.grgbanking.feeltm.costControl.services.CostControlService;
import cn.grgbanking.feeltm.domain.SysUser;
import cn.grgbanking.feeltm.domain.UsrContacts;
import cn.grgbanking.feeltm.domain.UsrUsrgrp;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.project.dao.ProjectDao;
import cn.grgbanking.feeltm.project.domain.Project;
import cn.grgbanking.feeltm.project.domain.UserProject;
import cn.grgbanking.feeltm.staff.dao.StaffInfoDao;
import cn.grgbanking.feeltm.staff.utils.HttpRequest;
import cn.grgbanking.feeltm.staff.utils.PropertiesUtil;
import cn.grgbanking.feeltm.um.service.SysUserGroupService;
import cn.grgbanking.feeltm.util.DateUtil;
import cn.grgbanking.feeltm.util.HashUtil;
import cn.grgbanking.framework.service.BaseService;
import cn.grgbanking.framework.util.Page;
import electric.uddi.Contact;

/**
 * 2014-4-24 员工信息
 * @author lhyan3
 *
 */
@Service
@Transactional
public class StaffInfoService extends BaseService{
	@Autowired
	private StaffInfoDao staffInfoDao;
	
	@Autowired
	private ProjectDao projectDao;
	
	@Autowired
	private SysUserGroupService sysUserGroupService;
	@Autowired
	private CostControlService costControlService;
	@Autowired
	ContactService contactService;
	
	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	public Page findUserInfoPage(SysUser user,UserModel userModel, String grpCode, String startstr, String endstr, int pageNum, int pageSize,String orderField,String regulation) {
		//查看所有数据
		String hql = "from SysUser as use,SysDatadir sd where use.deptName=sd.key and sd.parentid='8a81d9704587f215014587ffec14006a' ";
/*		if(!UserRoleConfig.ifAdministratorOrHr(userModel)){
			//不是总经理或者管理员的要根据权限查看，这两个角色可以看到所有数据
			if(UserRoleConfig.isDeptManageGroup(userModel)){
				//部门数据
				hql += " and ((use.deptName='"+userModel.getDeptName()+"' ) or use.userid='"+userModel.getUserid()+"')";
			}else{
				//非部门经理，只查看自己的
				hql += " and use.userid = '"+userModel.getUserid()+"'";
			}
		}*/
		//条件查询
		if(null != user.getUserid() && !"".equals(user.getUserid())){
			hql += " and (use.userid like  '%"+user.getUserid()+"%' or use.username like  '%"+user.getUserid()+"%')" ;
		}
		if(null !=user.getDeptName() && !"".equals(user.getDeptName())){
			hql += " and use.deptName= '"+user.getDeptName()+"'";
		}
		if(null != grpCode && !"".equals(grpCode)){
			hql += " and use.userid in (select u.userid from UserProject u where u.project.id='"+grpCode+"')";
		}
		if(null != startstr && !"".equals(startstr)){
			hql += " and use.grgBegindate>=to_date('"+startstr+"','yyyy-MM-dd')";
		}
		if(null!=endstr && !"".equals(endstr)){
			hql += " and use.grgBegindate<=to_date('"+endstr+"','yyyy-MM-dd')";
		}
		//默认不查离职人员，如查询条件选择了则查询
		if(null!=user.getStatus() && !"".equals(user.getStatus())){
			hql += " and use.status = '" + user.getStatus() + "'";			
		}else {
			hql += " and use.status!='leave' and use.status!='离职'";						
		}
		if(orderField!=null && !"".equals(orderField)){
			//如果排序字段为deptName，则根据数据字典的ORDER进行排序
			if("deptName".equals(orderField)){
				hql += " order by sd.order";
			}else{
				hql += " order by use." + orderField; 				
			}
			//未指定排序方式的话默认为递增
			if(regulation!=null && !"".equals(regulation)){
				hql += " " + regulation; 
			}else {
				hql +=" asc";
			}
		}else{
			//默认根据数据字典的ORDER进行排序
			hql += " order by sd.order asc";
		}
		//只取出SYSUSER
		Page page =  staffInfoDao.findUserInfoPage(hql, pageNum, pageSize);
		List<Object[]> objArrList = page.getQueryResult();
		List<SysUser> list = new ArrayList();
		for(Object [] arr : objArrList){
			list.add((SysUser)arr[0]);
		}
		page.setQueryResult(list);
		return page;
	}

	/**lhy 2014-4-25
	 * 判断工号是否已经存在
	 * @param userid
	 * @return
	 */
	public boolean isExitStaffByUserid(String userid) {
		return staffInfoDao.isExitStaffByUserid(userid);
	}
	
	public String getUserIdByUsername(String username){
		return staffInfoDao.getUseridByUsername(username);
	}

	/**
	 * lhy 2014-4-25
	 * 保存
	 * @param staff
	 */
	public void addStaffInfo(SysUser staff) {
		staffInfoDao.addStaffInfo(staff);
	}

	/**
	 * lhy 2014-4-25
	 * 根据userid查询用户
	 * @param id
	 * @return
	 */
	public SysUser findUserByUserid(String id) {
		return staffInfoDao.findUserByUserid(id);
	}
	
	/**
	 * lhy 2014-4-27
	 * 更新
	 * @param user
	 */
	public void updateStaffInfo(SysUser user) {
		staffInfoDao.updateObject(user);
		
	}
	/**
	 * ljlian2 2014-12-16
	 * 用户自己更新电话和邮箱
	 * @param userid 
	 * @param 
	 * @return 
	 */
	public boolean updatePartStaffInfo(String mobile,String email, String userid) {
		return staffInfoDao.updatePartStaffInfo(mobile,email,userid);
		
	}

	/**
	 * lhy 2014-4-27
	 * 删除
	 * @param string
	 * @return
	 */
	public int removeStaffInfo(String userids) {
		String strids[] = userids.split(",");
		//先删除与组的关系
		sysUserGroupService.removeSysUserGroup(strids);
		return staffInfoDao.removeStaffInfo(strids);
	}

	/**
	 * lhy 2014-5-4
	 * 根据部门的key获取该部门的员工
	 * @param key
	 * @return
	 */
	public List<SysUser> getStaffByDeptKey(String key) {
		return staffInfoDao.getStaffByDeptKey(key);
	}


	/**
	 * lhy 2014-5-5
	 * 获取部门经理及以上人员
	 */
	public List<SysUser> getdeptManagerUp() {
		return staffInfoDao.getdeptManagerUp();
	}
	
	/**
	 * 获取项目经理
	 * @return
	 */
	public List<SysUser> getGroupManager(){
		return staffInfoDao.getGroupManager();
	}

	/**
	 * lhy 2014-5-8
	 * 根据userId获取邮件
	 * @param string
	 * @return
	 */
	public List<String> getUserIdEmailMap(String str) {
		List<String> list = new ArrayList<String>();
		list = staffInfoDao.getEmailByUserid(str);
		return list;
	}

	public String getUsernameById(String sender) {
		return staffInfoDao.getUsernameById(sender);
	}

	 /**wtjiao 2014-5-6
	  * 根据部门名称的key获取value
	  * @return
	  */
	 public String getDeptNameValueByKey(String key){
	  return (String)BusnDataDir.getMapKeyValue("staffManager.department").get(key);
	 }
	 
	 /**wtjiao 2014-5-6
	  * 根据用户id获取用户所在部门
	  * @return
	  */
	 public String getDeptNameValueByUserId(String userId){
		 if(findUserByUserid(userId)!=null){
			 String key=findUserByUserid(userId).getDeptName();
			 return getDeptNameValueByKey(key);
		 }else{
			 return "";
		 }
	 }

	public List<SysUser> getgroupManagerUp() {
		return staffInfoDao.getgroupManagerUp();
	}
	
	/**
	 * 返回岗位级别名称
	 * @param userid
	 * @return
	 * lhyan3
	 * 2014年6月3日
	 */
	public String getLevelName(String userid){
		SysUser user = findUserByUserid(userid);
		if(user!=null){
			return BusnDataDir.getValue(BusnDataDir.getMapKeyValue("staffManager.postlevel"), user.getPostLevel());
		}else{
			return null;
		}
	}

	/**
	 * @param deptName
	 * @param deptvalue
	 * @return
	 * lhyan3
	 * 2014年6月13日
	 */
	public List<Object[]> getNames(String deptName, String deptvalue,String groupId) {
		return staffInfoDao.getNamesByDept(deptName,deptvalue,groupId);
	}


	
	/**
	 * 根据工号判断用户是否存在
	 * @param userNumber 工号
	 * @param NumType 工号类型：0(公司工号);1(外派工号)
	 * @return
	 * zqsheng1
	 * 2014年7月1日
	 */
	public boolean isExitStaffByUserNumber(String userNumber,String numType) {
		return staffInfoDao.isExitStaffByUserNumber(userNumber, numType);
	}
	
	public boolean isExitStaffByUserName(String username){
		return staffInfoDao.isExitStaffByUserName(username);
	}
	/**
	 * 根据工号查找用户信息
	 * @param userNumber 工号
	 * @param numType 工号类型：0(公司工号);1(外派工号)
	 * @return
	 * zqsheng1
	 * 2014年7月1日
	 */
	public SysUser findUserByUserNumber(String userNumber,String numType) {
		return staffInfoDao.findUserByUserNumber(userNumber, numType);
	}

	/**
	 * 根据部门获取人员
	 * @param code
	 * lhyan3
	 * 2014年7月29日
	 */
	public List<SysUser> getUserByDept(String code) {
		return staffInfoDao.getUserByDept(code);
	}

	/**
	 * 根据部门获取部门经理
	 * @param code
	 * @return
	 * lhyan3
	 * 2014年7月29日
	 */
	public SysUser getDeptManagerByDept(String code) {
		return staffInfoDao.getDeptManagerByDept(code);
	}
	/**
	 * 根据部门获取部门经理
	 * @param code
	 * @return
	 * lhyan3
	 * 2014年7月29日
	 */
	public List<SysUser> getDeptManagerByDeptname(String code) {
		return staffInfoDao.getDeptManagerByDeptname(code);
	}
	
	public List<SysUser> getAllNotLeaveDeptManagerByDept(String code) {
		return staffInfoDao.getAllNotLeaveDeptManagerByDept(code);
	}
	
	/**
	 * 更新从EHR获取的新员工信息，初始化密码为userid以及用户组为普通用户
	 * @author lping1 2014-9-23
	 */
	public void initNewUserFromEHR(){
		List<SysUser> newUserList = staffInfoDao.getNewUserFromEHR();
		for(SysUser su : newUserList){
			su.setLevel(3);//默认级别为普通用户
			su.setPostLevel("0");//默认岗位为普通员工
			su.setStatus("probation");//默认状态为使用
			su.setUserpwd(HashUtil.hash(su.getUserid()));
			su.setUpdateTime(new Date());
			staffInfoDao.updateObject(su);
			UsrUsrgrp usrUsrgrp = new UsrUsrgrp();
			usrUsrgrp.setUserid(su.getUserid());
			usrUsrgrp.setGrpcode("guest");
			staffInfoDao.addObject(usrUsrgrp);
		}
	}

	public List<SysUser> listAllUser() {
		return staffInfoDao.listAllUser();
	}

	/**根据部门名称和用户姓名查找用户，排除指定的用户
	 * wtjiao 2014年10月21日 下午5:39:03
	 * @param deptname
	 * @param username
	 * @return
	 */
	public List<SysUser> queryUserByDeptAndUsernameExpectTheseUsers(String deptId,String username,String[] userIds) {
		String hql = "from SysUser s where 1=1 ";
		if(deptId!=null && !"".equals(deptId)){
			hql += " and s.deptName='"+deptId+"'";
		}
		if(username!=null && !"".equals(username)){
			hql += " and (s.userid like '%"+username+"%' or s.username like '%"+username+"%')";
		}
		//排除指定的用户
		if(userIds!=null && userIds.length!=0){
			for(int i=0;i<userIds.length;i++){
				hql+=" and s.userid!='"+userIds[i]+"' ";
			}
		}
		return staffInfoDao.queryUser(hql);
	}

	/**根据userIds得到user
	 * wtjiao 2014年10月22日 上午8:16:47
	 * @param userIds
	 * @return
	 */
	public List<SysUser> findUserByUserIds(String[] userIds) {
		String hql = "from SysUser s where 1=0 ";
		if(userIds!=null && userIds.length>0){
			for(int i=0;i<userIds.length;i++){
				hql+=" or s.userid='"+userIds[i]+"' ";
			}
		}
		return staffInfoDao.queryUser(hql);
	}

	/**查找指定userIds外的所有用户
	 * wtjiao 2014年10月22日 下午2:59:51
	 * @param userIds
	 * @return
	 */
	public List<SysUser> listUserExpectTheseUsers(String[] userIds) {
		String hql = "from SysUser s where 1=1 ";
		if(userIds!=null && userIds.length!=0){
			for(int i=0;i<userIds.length;i++){
				hql+=" and s.userid!='"+userIds[i]+"' ";
			}
		}
		return staffInfoDao.queryUser(hql);
	}

	/**根据用户获取其部门经理对象
	 * wtjiao 2014年10月23日 下午3:14:28
	 * @param userIds
	 * @return
	 */
	public List<SysUser> getDeptManagerByUser(List<String> userIds) {
		return staffInfoDao.getDeptManagerByUser(userIds);
	}
	
	/**
	 * 根据value值获取部门key值
	 * @param value
	 * @return
	 */
	public String getDeptkeyByValue(String value){
		return staffInfoDao.getDeptkeyByValue(value);
	}
	
	public String getEmailForUserid(String userid){
		return staffInfoDao.getEmailForUserid(userid);
	}
	
	/**获取在职的部门人员
	 * @param deptKey
	 * @return
	 */
	public List<SysUser> getNotLeaveStaffByDeptKey(String deptKey) {
		return staffInfoDao.getNotLeaveStaffByDeptKey(deptKey);
	}
	
	/**
	 * 判断用户是否离职
	 * @param userid
	 * @return
	 */
	public boolean flagUserisLeave(String userid){
		return staffInfoDao.flagUserisLeave(userid);
	}
	
	public List<SysUser> getUserByDeptExpecetTheseUsers(String deptKey,String expectUserIds){
		//获取该部门下在职的部门人员
		List<SysUser> deptUsers=getNotLeaveStaffByDeptKey(deptKey);
		//统计的部门人员
		List<SysUser> statisticDeptUsers=new ArrayList<SysUser>();
		for(int i=0;i<deptUsers.size();i++){
			String userid=deptUsers.get(i).getUserid();
			boolean containsUser=containsUser(expectUserIds, userid);
			if(!containsUser){
				statisticDeptUsers.add(deptUsers.get(i));
			}
		}
		return statisticDeptUsers;
	}
	
	/**
	 * 
	 * @param deptKey 部门key
	 * @param expectUserIds   额外不用统计的人员
	 * @param exceptNotWriteDaylog  额外不用写日志的人员
	 * @return
	 */
	public List<SysUser> getUserByDeptExpecetTheseUsers(String deptKey,String expectUserIds,String exceptNotWriteDaylog){
		//获取该部门下在职的部门人员
		List<SysUser> deptUsers=getNotLeaveStaffByDeptKey(deptKey);
		
		String exceptMan=exceptNotWriteDaylog+","+expectUserIds;
		//统计的部门人员
		List<SysUser> statisticDeptUsers=new ArrayList<SysUser>();
		for(int i=0;i<deptUsers.size();i++){
			String userid=deptUsers.get(i).getUserid();
			boolean containsUser=containsUser(exceptMan, userid);
			/*if(containsUser){
				System.out.println("exceptMa======被包含的人id"+userid+"        "+deptUsers.get(i).getDeptName());
			}*/
			if(!containsUser){
				statisticDeptUsers.add(deptUsers.get(i));
			}
		}
		return statisticDeptUsers;
	}
	/**指定的用户id中是否包含特定的用户
	 * @param expectUserIds
	 * @param userid
	 * @return
	 */
	private boolean containsUser(String expectUserIds, String userid) {
		try{
			String[] users= org.springframework.util.StringUtils.tokenizeToStringArray(expectUserIds, ",，");
			for(String user:users){
				if(user.equals(userid)){
					return true;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}

	/**获取所有用户的map对象，key为userid，value为user对象
	 * @return
	 */
	public Map<String, SysUser> listAllUserMap() {
		Map<String,SysUser> users=new HashMap<String, SysUser>();
		for(SysUser usr:staffInfoDao.listAllUser()){
			users.put(usr.getUserid(), usr);
		}
		return users;
	}

	public List<SysUser> getnotwritedaylogdeptlist(
			List<SysUser> realWatchedUserList, String depname) {
		return staffInfoDao.getnotwritedaylogdeptlist(realWatchedUserList,depname);
	}

	public void synSysUserFromEHR() throws JSONException {
		SysLog.info("ytxx员工信息数据同步开始!====="+sdf.format(new Date()));
		//ehr 库中不存在的数据 作战系统有的数据     // 离职的人员
		List<SysUser> ytxxUser=staffInfoDao.listAllUser();
		
		try {
			String s = HttpRequest.sendGet(PropertiesUtil.get("synEMPYTXX"), "");
			//从ehr接口中获取运通系统没有离职的员工信息
			//SysLog.info("同步的数据"+s);
			
			JSONArray jsonArray = new JSONArray(s);	
			
			List<String> employeeIdList = new ArrayList<String>();
			
			for(int i=0;i<jsonArray.length();i++){
				
				JSONObject jso = jsonArray.getJSONObject(i);
				String employeeid=jso.getString("employeeid");//员工工号
				employeeIdList.add(employeeid);
				//根据员工工号 查找 作战系统db中是否存在该数据   若存在则更新 若不存在 则插入
				List<SysUser> userList = staffInfoDao.findStaffByEmployeeId(employeeid);
				
				if(userList!=null && userList.size()>0){
					//在职就更新
					//新用户 设置默认密码
					Object oauserid = jso.get("oauserid");
					SysUser user=setSysUser(jso,userList.get(0));
					staffInfoDao.updateObject(user);
					
					/* ---- 更新通讯录  ------*/
					UserModel userModel = new UserModel();
					userModel.setUsername("adminSyn");
					userModel.setUserid("adminSyn");
					String userId = user.getUserid();
					UsrContacts  usrContacts = contactService.getContactByUserId(userId);
					if(  usrContacts != null ){
						contactService.updateContactByUser(user, userModel);
						SysLog.info(" 通讯录更新成功! ======="+user.getUserid() +"==="+user.getUsername());
					}else{
						contactService.saveNewConstact("adminSyn", user);
						SysLog.info(" 通讯录添加成功! ======="+user.getUserid() +"==="+user.getUsername());
					}
					
				}else{
					//不存在该员工 则新增一个员工
					SysUser user=setSysUser(jso,new SysUser());
					staffInfoDao.addStaffInfo(user);
					SysLog.info("同步新入职员工    "+user.getUsername()+"  成功");
					//设置默认的权限  普通用户
					sysUserGroupService.removeSysUserGroup(user.getUserid());
					UsrUsrgrp usrgrp = new UsrUsrgrp();
					usrgrp.setUserid(user.getUserid());
					usrgrp.setGrpcode("guest");
					sysUserGroupService.saveSysUserGroup(usrgrp);
					SysLog.info("设置普通用户权限 成功===="+user.getUsername());
					
					
					/*---------- 添加通讯录   --------------*/
					contactService.saveNewConstact("adminSyn", user);
					SysLog.info(" 通讯录添加成功! ======="+user.getUserid() +"==="+user.getUsername());
				}
			}
			//ytxxUser ytxxUser 运通信息作战系统中所的用户
			
			List<UserProject> userProjects = null;
			//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			for(SysUser user : ytxxUser){
				// 系统中有该用户 ，ehr系统中没有  则将该用户设置为离职状态
				if(!employeeIdList.contains(user.getJobNumber().trim())){
					user.setStatus("leave");
					//user.setIsvalid("N");//将离职的员工设置为无效用户
					staffInfoDao.updateObject(user);
					//System.out.println(user.getUsername());
					// 将员工退出项目的时间更新为离职时间
					userProjects = projectDao.getUserProjectByUserId(user);
					Date now = new Date();
					for (UserProject userProject : userProjects) {
					    
					    
                        if (now.before(userProject.getExitTime())) {
                            userProject.setExitTime(now);
                            
                            projectDao.updateObject(userProject);
                        }
                    }
					
					
					//将员工离职的日志设置为该员工离开项目组的时间     
					//updateProjectResourcePlan(user);
					
				}
			}
			SysLog.info("ytxx员工数据同步结束====="+sdf.format(new Date()));
			
		} catch (Exception e) {
			SysLog.info("ytxx员工数据同步失败======="+sdf.format(new Date()));
			SysLog.error(e);
		}
		
		
		
	}
	/**
	 * 更新项目的人员分布数据   
	 * @param user  
	 */
	/*private void updateProjectResourcePlan(SysUser user) {
		//找到该员工所在的所有项目  
		
	}*/

	/**
	 * [{"birthday":"1982-09-15","employeeid":"G0105238","departmentid":765,
	 * "sex":"2","phone":"18565258355","employeestatus":"正式","employeename":"姚立军",
	 * "idcard":"430525198209157035",
	 * 
	 * "positionname":null,"stationid":"经理助理",    
	 * "internalmanagerlevel":"员工",
	 * 未处理
	 * 
	 * 
	 * "enterdate":"2013-08-20","nationality":"1",
	 * "operatedate":"2015-06-11","email":null,"oauserid":"yljun"},
	 * @param jso
	 * @return
	 */
	private SysUser setSysUser(JSONObject jso,SysUser user) {
		try {
			if(user.getUserpwd()==null){
				user.setUserpwd(HashUtil.hash(jso.get("oauserid").toString()));
			}
			String status = jso.get("employeestatus").toString();
			if(status!=null ){
				if("其它".equals(status)){
					user.setStatus("other");
				}
				else if("实习".equals(status)){
					user.setStatus("study");
				}
				else if("试用".equals(status)){
					user.setStatus("probation");
				}
				else if("正式".equals(status)){
					user.setStatus("entry");
				}
				user.setIsvalid("Y");
			}else{
				user.setStatus("leave");//
				user.setIsvalid("N");//用户的状态还不确定的时候，该用户设置为不可用
			}
			
			//新用户 设置默认密码
			Object oauserid = jso.get("oauserid");
			if(oauserid !=null){
				user.setUserid(oauserid.toString());
			}
			
 			Object birthday = jso.get("birthday");
			if(birthday !=null){
				user.setBirthDate(DateUtil.stringToDate(birthday.toString(), "yyyy-MM-dd"));//设置生日
			}
			Object employeeid=jso.get("employeeid");

			if(employeeid!=null){
				user.setJobNumber(employeeid.toString());//设置工号
			}
			//部门名称  更改
			Object departmentid=jso.get("departmentid");
			if(departmentid!=null){
				try {
					String deptName = PropertiesUtil.get(departmentid.toString());
					if(StringUtils.isNotBlank(deptName)){
						user.setDeptName(deptName);
					}else{
						user.setDeptName("");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			
			Object phone=jso.get("phone");
			if(phone!=null && !"null".equals(phone.toString())){
				user.setTel(phone.toString());//设置电话
				user.setMobile(phone.toString());
			
			}
			//设置员工姓名
			Object employeename=jso.get("employeename");//设置员工姓名
			if(employeename!=null){
				user.setUsername(employeename.toString());
			}
			//设置idcard
			Object idcard=jso.get("idcard");
			if(idcard!=null && !"null".equals(idcard.toString())){
				user.setIdCardNo(idcard.toString());
			}
			//设置入职日期
			Object enterdate=jso.get("enterdate");
			if(enterdate!=null){
				user.setWorkBegindate(DateUtil.stringToDate(enterdate.toString(), "yyyy-MM-dd"));
				user.setGrgBegindate(DateUtil.stringToDate(enterdate.toString(), "yyyy-MM-dd"));
			}
			//设置开始工作的日期
			Object operatedate=jso.get("operatedate");
			if(operatedate!=null){
				user.setWorkBegindate(DateUtil.stringToDate(operatedate.toString(), "yyyy-MM-dd"));
			}
			user.setInvaliddate(DateUtil.stringToDate("9999-12-31", "yyyy-MM-dd"));
			user.setFlag("0");
			
			//设置员工的邮箱   日志提醒等  需要邮箱信息
			Object e_mail=jso.get("email");
			if(e_mail!=null){
				if( e_mail.toString() != null && !"".equals(e_mail.toString()) && !"null".equals(e_mail.toString()) ){
					user.setEmail(e_mail.toString());
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
		
	}
}
