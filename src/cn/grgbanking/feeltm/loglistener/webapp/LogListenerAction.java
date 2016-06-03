package cn.grgbanking.feeltm.loglistener.webapp;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;


import cn.grgbanking.feeltm.common.util.SystemHelper;
import cn.grgbanking.feeltm.common4Wechat.util.SendMsgToWechatSocketer;
import cn.grgbanking.feeltm.config.BusnDataDir;
import cn.grgbanking.feeltm.domain.SysDatadir;
import cn.grgbanking.feeltm.domain.SysUser;
import cn.grgbanking.feeltm.holiday.service.HolidayService;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.loglistener.domain.LogListenerMonitor;
import cn.grgbanking.feeltm.loglistener.domain.WarnInfo;
import cn.grgbanking.feeltm.loglistener.service.LogListenerService;
import cn.grgbanking.feeltm.loglistener.service.TemplateEmailService;
import cn.grgbanking.feeltm.loglistener.service.WarnInfoService;
import cn.grgbanking.feeltm.loglistener.util.LogListenerSyschronizeUtil;
import cn.grgbanking.feeltm.project.domain.Project;
import cn.grgbanking.feeltm.project.service.ProjectService;
import cn.grgbanking.feeltm.staff.service.StaffInfoService;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;

/**
 * 日志监控
 * @author lhyan3
 * 2014年7月28日
 */
@SuppressWarnings("serial")
public class LogListenerAction extends BaseAction{
	
	@Autowired
	private LogListenerService service;
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private StaffInfoService staffInfoService;
	
	@Autowired
	private WarnInfoService warnInfoService;
	
	@Autowired
	private HolidayService holidayService;
	
	@Autowired
	private LogListenerSyschronizeUtil listenerSyschronizeUtil;
	
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	
	/** 监控 ID */
	private String id;
	
	/**组织类型*/
	private String orgType;
	/** 项目Id */
	private String projectId;
	/** 项目监控人员 */
	private String projectMonitorName;
	/** 项目监控人员id */
	private String projectMonitorId;
	/** 项目被监控人员 */
	private String projectWatchedName;
	/** 项目被监控人员id */
	private String projectWatchedId;
	
	/** 部门Id */
	private String deptId;
	/** 部门监控人员 */
	private String deptMonitorName;
	/** 部门监控人员id */
	private String deptMonitorId;
	/** 部门被监控人员 */
	private String deptWatchedName;
	/** 部门被监控人员id */
	private String deptWatchedId;
	
	/**判断是否是服务器
	 * wtjiao 2014年10月30日 下午4:49:10
	 * @return
	 */
	private boolean isServerMachine(){
		try{
			
			boolean isServer=SystemHelper.isServerMachine();
			SysLog.record(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+" serverMachine:"+isServer);
			return isServer;
			
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		
	}
	
	/**
	 * 删除
	 * @return
	 * lhyan3
	 * 2014年7月29日
	 */
	public String delete(){
		MsgBox msgBox;
		try {
			String ids = request.getParameter("ids");
			if(ids!=null && !"".equals(ids)){
				String[] idss = ids.split(",");
				if(idss!=null && idss.length>0){
					for(String id:idss){
						service.delete(id);
					}
					msgBox = new MsgBox(request, getText("删除成功"));
					msgBox.setButtonType(MsgBox.BUTTON_RETURN);
				}else{
					msgBox = new MsgBox(request, getText("删除失败,数据异常"));
					msgBox.setButtonType(MsgBox.BUTTON_RETURN);
				}
			}else{
				msgBox = new MsgBox(request, getText("删除失败,数据异常"));
				msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(request, "error in (LogListenerAction.java-validates())");
			SysLog.error(e);
			msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
		}
		return "msgBox";
	}
	
	
	/**
	 * 设为手动更新
	 * @return
	 * lhyan3
	 * 2014年7月29日
	 */
	public String invalidate(){
		MsgBox msgBox;
		try {
			String ids = request.getParameter("ids");
			if(ids!=null && !"".equals(ids)){
				String[] idss = ids.split(",");
				if(idss!=null && idss.length>0){
					for(String id:idss){
						LogListenerMonitor listener = service.getLogListenerById(id);
						listener.setAutoUpdate("0");;
						service.update(listener);
					}
					msgBox = new MsgBox(request, getText("设置手动更新成功"));
					msgBox.setButtonType(MsgBox.BUTTON_RETURN);
				}else{
					msgBox = new MsgBox(request, getText("设置失败,数据异常"));
					msgBox.setButtonType(MsgBox.BUTTON_RETURN);
				}
			}else{
				msgBox = new MsgBox(request, getText("设置失败,数据异常"));
				msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(request, "error in (LogListenerAction.java-validates())");
			SysLog.error(e);
			msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
		}
		return "msgBox";
	}
	
	/**
	 * 设为自动更新
	 * @return
	 * lhyan3
	 * 2014年7月29日
	 */
	public String validates(){
		MsgBox msgBox;
		try {
			String ids = request.getParameter("ids");
			if(ids!=null && !"".equals(ids)){
				String[] idss = ids.split(",");
				if(idss!=null && idss.length>0){
					for(String id:idss){
						LogListenerMonitor listener = service.getLogListenerById(id);
						listener.setAutoUpdate("1");
						service.update(listener);
					}
					msgBox = new MsgBox(request, getText("设置自动更新成功"));
					msgBox.setButtonType(MsgBox.BUTTON_RETURN);
				}else{
					msgBox = new MsgBox(request, getText("设置失败,数据异常"));
					msgBox.setButtonType(MsgBox.BUTTON_RETURN);
				}
			}else{
				msgBox = new MsgBox(request, getText("设置失败,数据异常"));
				msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(request, "error in (LogListenerAction.java-validates())");
			SysLog.error(e);
			msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
		}
		return "msgBox";
	}
	
	
	
	/**发送人员去重 建议使用比较器实现
	 * wtjiao 2014年8月15日 下午1:28:05
	 * @param sendPersonList
	 */
	private List<SysUser> removeSameUser(List<SysUser> sendPersonList) {
		try{
			if(sendPersonList==null || sendPersonList.size()<=0){
				return sendPersonList;
			}
			List<SysUser> userList=new ArrayList<SysUser>();
			for(int i=0;i<sendPersonList.size();i++){
				SysUser o=sendPersonList.get(i);
				SysUser n=new SysUser();
				n.setAcceptemailType(o.getAcceptemailType());
				n.setAddress(o.getAddress());
				n.setAreaid(o.getAreaid());
				n.setBirthDate(o.getBirthDate());
				n.setCreatedate(o.getCreatedate());
				n.setDeptName(o.getDeptName());
				n.setEducation(o.getEducation());
				n.setEmail(o.getEmail());
				n.setFlag(o.getFlag());
				n.setGraduateDate(o.getGraduateDate());
				n.setGraduateSchool(o.getGraduateSchool());
				n.setGrgBegindate(o.getGrgBegindate());
				n.setGroupName(o.getGroupName());
				n.setId(o.getId());
				n.setIdCardNo(o.getIdCardNo());
				n.setInvaliddate(o.getInvaliddate());
				n.setIsvalid(o.getIsvalid());
				n.setJobNumber(o.getJobNumber());
				n.setLevel(o.getLevel());
				n.setMajor(o.getMajor());
				n.setMobile(o.getMobile());
				n.setOrgfloor(o.getOrgfloor());
				n.setOrgid(o.getOrgid());
				n.setOrgLevel(o.getOrgLevel());
				n.setOutNumber(o.getOutNumber());
				n.setPostLevel(o.getPostLevel());
				n.setRelativeName(o.getRelativeName());
				n.setRelativeTel(o.getRelativeTel());
				n.setStatus(o.getStatus());
				n.setTel(o.getTel());
				n.setUserid(o.getUserid());
				n.setUsername(o.getUsername());
				n.setUserpwd(o.getUserpwd());
				n.setWorkBegindate(o.getWorkBegindate());
				n.setWorkcompany(o.getWorkcompany());
				n.setWorkid(o.getWorkid());
				userList.add(n);
			}
			for(int i=userList.size()-1;i>=0;i--){
				String curUserEmail=userList.get(i).getEmail();
				for(int j=sendPersonList.size()-1;j>=0;j--){
					String compareUserEmail=sendPersonList.get(j).getEmail();
					if((StringUtils.isBlank(curUserEmail)&&StringUtils.isBlank(compareUserEmail))||(curUserEmail!=null&&curUserEmail.equals(compareUserEmail)&&i!=j)){
						userList.remove(i);
						sendPersonList.remove(i);
						break;
					}
				}
			}
			return userList;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
	}

	
	public List<SysUser> getRemindSendPersonList(){
		try{
			Date curDate=new Date();
			boolean isHoliday=isHoliday(curDate);
			
			if(!isHoliday){
				//所有监控
				List<LogListenerMonitor> listeners = service.getAllListener();
				//检查监控的项目名称是否已修改或删除
				checkListeners(listeners);
				
				//获取未填写日志名单
				List<SysUser> sendPersonList = new ArrayList<SysUser>();
				if(listeners!=null && listeners.size()>0){ 
					for(int i = 0;i<listeners.size();i++){
						LogListenerMonitor listener=listeners.get(i);
						List<SysUser> notWriteLogUserList=new ArrayList<SysUser>();
						if("project".equals(listener.getOrgType())){
							notWriteLogUserList = service.getNotLogByPrj(listener.getOrgId(), format.format(curDate));
						}else if("dept".equals(listener.getOrgType())){
							notWriteLogUserList=service.getNotLogByDept(listener.getOrgId(), format.format(curDate));
						}
						//获取受监控的人员
						List<SysUser> watchedUserList=userStrToList(listener.getWatchedId());
						//获取待发送邮件的用户列表
						List<SysUser> sendEmailUserList=getSendEmailUser(notWriteLogUserList,watchedUserList);
						sendPersonList.addAll(sendEmailUserList);
					}
					
					//去除发送人员中的重复数据
					sendPersonList=removeSameUser(sendPersonList);
				}
				return sendPersonList;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return new ArrayList<SysUser>();
	
	}
	
	
	/**
	 * 定时发送未填写日志的名单给领导
	 * lhyan3
	 * 2014年7月29日
	 */
	public synchronized void sendEmailForLeader(){
		
		//非服务器，不进行任何提示
		if(!this.isServerMachine()){
			return;
		}
		
		try{
			Date curDate=new Date();
			String title = format.format(curDate)+" 没有填写日志名单";
			boolean isHoliday=isHoliday(curDate);
			
			if(!isHoliday){
				//所有监控
				List<LogListenerMonitor> listeners = service.getAllListener();
				checkListeners(listeners);			
				String content="";
				
				
				SysUser sysUser = null;
				/*发送邮件名单*/
				List<SysUser>  sendUserList = new ArrayList<SysUser>();
				
				if(listeners!=null && listeners.size()>0){ 
					for (int i = 0; i < listeners.size(); i++) {
						LogListenerMonitor listener = listeners.get(i);
						// 获取没有填写日志的人员
						List<SysUser> notWriteLogUserList = new ArrayList<SysUser>();
						if ("project".equals(listener.getOrgType())) {
							notWriteLogUserList = service
									.getNotLogByPrj(listener.getOrgId(),
											format.format(curDate));
						} else if ("dept".equals(listener.getOrgType())) {
							notWriteLogUserList = service
									.getNotLogByDept(listener.getOrgId(),
											format.format(curDate));
							// 获取需要被监控的人员
							List<SysUser> watchedUserList = userStrToList(listener
									.getWatchedId());
							// 获取真正需要被监控的人员
							List<SysUser> realWatchedUserList = getSendEmailUser(
									notWriteLogUserList, watchedUserList);
							// 去除发送人员中的重复数据
							realWatchedUserList = removeSameUser(realWatchedUserList);
							String depname = listener.getOrgId();
							String deptNameVal = listener.getOrgName();
							if (realWatchedUserList != null
									&& realWatchedUserList.size() > 0) {
								
								List<SysUser> deptmanagerlist = new ArrayList<SysUser>();
								if (deptmanagerlist != null
										&& deptmanagerlist.size() > 0) {

									String titles = format.format(curDate)
											+ deptNameVal + "没有填写日志名单";
									String contents = "";
									// 组织邮件内容
									contents += "<br/>" + deptNameVal
											+ ":&nbsp;&nbsp; "
											+ realWatchedUserList.size()
											+ "<br/><br/>";
									contents += "&nbsp;&nbsp;&nbsp;&nbsp;"
											+ getUserNameFromUserList(realWatchedUserList)
											+ "<br/>";
									SysLog.info("邮件内容:" + contents);
								sendAndSaveLog2Leader(deptmanagerlist,
											titles, contents);
								}
							}
						}
						// 获取需要被监控的人员
						List<SysUser> watchedUserList = userStrToList(listener
								.getWatchedId());
						// 获取真正需要被监控的人员
						List<SysUser> realWatchedUserList = getSendEmailUser(
								notWriteLogUserList, watchedUserList);
						// 去除发送人员中的重复数据
						realWatchedUserList = removeSameUser(realWatchedUserList);

						// 拼凑项目人员
						if (realWatchedUserList != null
								&& realWatchedUserList.size() > 0) {
							content += "<br/>" + listener.getOrgName()
									+ ":<br/><br/>";
							content += "&nbsp;&nbsp;&nbsp;&nbsp;"
									+ getUserNameFromUserList(realWatchedUserList)
									+ "<br/>";
						}
					}
					
					//发送用户  projectMonitor.logListenMonitor.sendEmailUsers 
					String sendUserStr=BusnDataDir.getObjectDetail("projectMonitor.logListenMonitor.sendEmailUsers");
					String[] sendUserArray =  org.springframework.util.StringUtils.tokenizeToStringArray(sendUserStr, ",，");
					
					if(sendUserArray!=null&&sendUserArray.length>0){
						for(int l=0;l<sendUserArray.length;l++){
							String userid=sendUserArray[l].trim();
							sysUser = new SysUser();
							sysUser.setId(userid);
							sysUser.setEmail(this.getUserEmailById(new String[]{userid}));
							sendUserList.add(sysUser);
						}	
					}
					
					//发送并保存发送结果
					sendAndSaveLog2Leader(removeSameUser(sendUserList),title,content);
				}
				
			}
			
		}catch(Exception e){
			 
			SysLog.error(new Date()+e.toString());
		}
	}
	
	
	/**从list列表中获取人员字符串
	 * wtjiao 2014年10月28日 下午1:15:29
	 * @param sendEmailUserList
	 * @return
	 */
	private String getUserNameFromUserList(List<SysUser> userList) {
		String users="";
		if(userList!=null && userList.size()>0){
			for(SysUser user:userList){
				users+=user.getUsername()+",";
			}
		}
		if(userList.size()>=1){
			users=users.substring(0,users.length()-1);
		}
		return users;
	}
	
	public List<SysUser> removeTheSameUser(List<SysUser> sendPersonList){
	    
	    for (int i = 0,len = sendPersonList.size() - 1; i < len; i++) {
            for (int j = sendPersonList.size() - 1; j > i; j--) {
                if (sendPersonList.get(j).getId().equals(sendPersonList.get(i).getId())) {
                    sendPersonList.remove(j);
                }
            }
        }
	    
	    return sendPersonList;
	}
	
	
	
	/**发送邮件并记录日志（to用户）
	 * wtjiao 2014年10月27日 下午3:54:08
	 * @param sendPersonList
	 */
	public synchronized void sendEmailAndSaveLog2User() {
		//非服务器，不进行任何提示
		if(!isServerMachine()){
			return ;
		}
		
		Date curDate=new Date();
		String title = format.format(curDate)+"日志未填写提醒";
		List<SysUser> sendPersonList=removeTheSameUser(getRemindSendPersonList());
		TemplateEmailService emailService=new TemplateEmailService();

		
		
		
		//记录发送成功的人员
		//List<SysUser> sendedList=new ArrayList<SysUser>();
		SysLog.info(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(Calendar.getInstance().getTime())+"邮件发送给用户中。。。。");
		for(int i=0;i<sendPersonList.size();i++){
			SysUser user=sendPersonList.get(i);
			SysLog.info(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(Calendar.getInstance().getTime())+"邮件发送给用户"+user.getUsername());
			//填充模版中的指定字段
			Map<String,String> templateTagMap=new HashMap<String,String>();
			templateTagMap.put("username",user.getUsername());
			templateTagMap.put("noticeDate", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(Calendar.getInstance().getTime()));
			emailService.sendTemplateMail(user.getEmail(), title, "daylogRemindToUser.ftl", templateTagMap);
//			emailService.sendTemplateMail("zzwen6@grgbanking.com", title, "daylogRemindToUser.ftl", templateTagMap);
			//保存发送记录
			/*if(!"0".equals(sendContent)){
				WarnInfo warn=new WarnInfo();
				warn.setToUserId(user.getUserid());
				warn.setToUserName(user.getUsername());
				warn.setWarnWay("email");
				warn.setWarnType("remind");
				warn.setWarnTime(Calendar.getInstance().getTime());
				warn.setWarnContent(sendContent);
				
				warnInfoService.save(warn);
			}*/
		}
		SysLog.info(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(Calendar.getInstance().getTime())+"邮件发送给用户完成。。。。");
	}
	
	/**发送微信并记录日志（to用户）
	 * wtjiao 2014年10月27日 下午3:54:08
	 * @param sendPersonList
	 */
	public void sendWechatAndSaveLog2User() {
		//非服务器，不进行任何提示
		if(!isServerMachine()){
			return ;
		}
		
		List<SysUser> sendPersonList=getRemindSendPersonList();
		//发送微信提醒
		SendMsgToWechatSocketer wechatService=new SendMsgToWechatSocketer();
		List<String> useridList=userList2UserIdList(sendPersonList);
		String noticeDate=new SimpleDateFormat("yyyy-MM-dd HH:mm").format(Calendar.getInstance().getTime());
		List<String> sucessSendUserIdList=wechatService.sendMessageToWechat(useridList, noticeDate, "1");
		//发送成功的用户
		List<SysUser> sucessSendUserList=userIdList2UserList(sucessSendUserIdList);
		
		for(int i=0;i<sucessSendUserList.size();i++){
			SysUser user=sucessSendUserList.get(i);
			//保存发送记录
			WarnInfo warn=new WarnInfo();
			warn.setToUserId(user.getUserid());
			warn.setToUserName(user.getUsername());
			warn.setWarnWay("wechat");
			warn.setWarnType("remind");
			warn.setWarnTime(Calendar.getInstance().getTime());
			warn.setWarnContent(null);
			warnInfoService.save(warn);
		
		}
	}
	
	
	/**用户id列表转用户列表
	 * wtjiao 2014年10月28日 下午1:46:31
	 * @param sucessSendUserIdList
	 * @return
	 */
	private List<SysUser> userIdList2UserList(List<String> sucessSendUserIdList) {
		List<SysUser> userList=new ArrayList<SysUser>();
		if(sucessSendUserIdList!=null && sucessSendUserIdList.size()>0){
			for(int i=0;i<sucessSendUserIdList.size();i++){
				try{
					SysUser user=staffInfoService.findUserByUserid(sucessSendUserIdList.get(i));
					userList.add(user);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		return userList;
	}

	/**用户列表转为用户id列表
	 * wtjiao 2014年10月28日 下午1:34:01
	 * @param sendPersonList
	 * @return
	 */
	private List<String> userList2UserIdList(List<SysUser> sendPersonList) {
		List<String> useridList=new ArrayList<String>();
		if(sendPersonList!=null && sendPersonList.size()>0){
			for(int i=0;i<sendPersonList.size();i++){
				useridList.add(sendPersonList.get(i).getUserid());
			}
		}
		return useridList;
	}

	/**发送邮件并记录日志（to领导）
	 * wtjiao 2014年10月27日 下午3:54:08
	 * @param sendPersonList
	 * @param content 
	 */
	private void sendAndSaveLog2Leader(List<SysUser> sendPersonList,String title, String content) {
		TemplateEmailService emailService=new TemplateEmailService();

		//记录发送成功的人员
		List<SysUser> sendedList=new ArrayList<SysUser>();
		
		//发送邮件
		for(int i=0;i<sendPersonList.size();i++){
			SysUser user=sendPersonList.get(i);
			//填充模版中的指定字段
			Map<String,String> templateTagMap=new HashMap<String,String>();
			Date curDate=new Date();
			templateTagMap.put("logdate",format.format(curDate));
			templateTagMap.put("warnInfo",content);
			String sendContent =emailService.sendTemplateMail(user.getEmail(), title, "daylogNotificationToUser.ftl", templateTagMap);
			//String sendContent = "0";
			SysLog.info("日志统计邮件接收者："+sendPersonList.get(i).getUsername());
			//保存发送记录
			if(!"0".equals(sendContent)){
				WarnInfo warn=new WarnInfo();
				warn.setToUserId(user.getUserid());
				warn.setToUserName(user.getUsername());
				warn.setWarnWay("email");
				warn.setWarnType("notification");
				warn.setWarnTime(Calendar.getInstance().getTime());
				warn.setWarnContent(sendContent);
				
				warnInfoService.save(warn);
			}
		}
	}

	/**对比，获取需要发送邮件的用户列表
	 * wtjiao 2014年10月27日 下午3:27:06
	 * @param notWriteLogUserList  没有写日志的用户列表
	 * @param watchedUserList  被监控的用户列表
	 * @return
	 */
	private List<SysUser> getSendEmailUser(List<SysUser> notWriteLogUserList,List<SysUser> watchedUserList) {
		if(notWriteLogUserList==null || notWriteLogUserList.size()<=0 || watchedUserList==null || watchedUserList.size()<=0){
			return new ArrayList<SysUser>();
		}
		//获取额外不需监控的人员  从数据字典中获取
		String notWatchedStr=BusnDataDir.getObjectDetail("projectMonitor.logListenMonitor.addtionNotWatched");
		List<String> addtionNotWatchedUsers=new ArrayList<String>();
//		String[] notWatchedArray=notWatchedStr.split(",");
		String[] notWatchedArray =  org.springframework.util.StringUtils.tokenizeToStringArray(notWatchedStr, ",，");
		
		// String[] 转换成 List<String >
		if(StringUtils.isNotBlank(notWatchedStr)&&notWatchedArray!=null&&notWatchedArray.length>0){
			for(int i=0;i<notWatchedArray.length;i++){
				String userid=notWatchedArray[i];
				addtionNotWatchedUsers.add(userid);
			}	
		}
		
		//去掉不需要发邮件的账户 
		for(int i=0;i<addtionNotWatchedUsers.size();i++){
			for(int j=0;j<notWriteLogUserList.size();j++){
				String userid=addtionNotWatchedUsers.get(i);
				String notWriteLogUserId=notWriteLogUserList.get(j).getUserid();
				if(!"".equals(userid)  &&  !"".equals(notWriteLogUserId)  &&  userid.equals(notWriteLogUserId)){
					notWriteLogUserList.remove(j);//将不需要发送邮件的人员去掉。
					break;
				}
			}
		}
		
		//判断没写日志的账户是否在监控的list中
		List<SysUser> sendEmailList=new ArrayList<SysUser>();
		for(int h=0;h<notWriteLogUserList.size();h++){
			for(int k=0;k<watchedUserList.size();k++){
				String notWriteLogUserId=notWriteLogUserList.get(h).getUserid();
				String watchedUserId=watchedUserList.get(k).getUserid();
				if(  !"".equals(notWriteLogUserId)  &&  !"".equals(watchedUserId)  &&  notWriteLogUserId.equals(watchedUserId)){
					sendEmailList.add(notWriteLogUserList.get(h));
				}
			}
		}
		//取交集
		return sendEmailList;
		
	}
	
	private List<SysUser> getSendEmailLeader(boolean add,List<SysUser> monitorUserList) {
		//加上监控人
		List<SysUser> sendEmailLeaderList=new ArrayList<SysUser>();
		if(add){
			for(int i=0;i<monitorUserList.size();i++){
				try{}catch(Exception e){
					e.printStackTrace();
				}
				try{
					sendEmailLeaderList.add(staffInfoService.findUserByUserid(monitorUserList.get(i).getUserid()));
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		return sendEmailLeaderList;
	}

	/**用户 转为List
	 * wtjiao 2014年10月27日 下午3:21:15
	 * @param userId
	 * @return
	 */
	private List<SysUser> userStrToList(String userIds) {
		List<SysUser> list=new ArrayList();
		if(StringUtils.isNotBlank(userIds)){
			for(int i=0;i<userIds.split(",").length;i++){
				SysUser user=new SysUser();
				user.setUserid(userIds.split(",")[i]);
				list.add(user);
			}
		}
		return list;
	}

	/**判断是否是假期
	 * wtjiao 2014年10月27日 下午2:57:12
	 * @param curDate
	 * @return
	 */
	private boolean isHoliday(Date curDate) {
		try{
			return holidayService.isHoliday(curDate);
		}catch(Exception e){
			e.printStackTrace();
			return true;
		}
		
	}

	private void checkListeners(List<LogListenerMonitor> listeners) {
		if(listeners!=null){
			for(int i=listeners.size()-1;i>=0;i--){
				LogListenerMonitor ll=(LogListenerMonitor)listeners.get(i);
				if(ll.getOrgType().equals("project")){
					Project p=projectService.getBriefProjectById(ll.getOrgId());
					String deptName=staffInfoService.getDeptNameValueByKey(ll.getOrgId());
					if(p==null && StringUtils.isBlank(deptName)){//根据id取不到，说明该项目id发生变化，在数据库中将其标识，以便管理员及时修改
						ll.setAutoUpdate("-1");;//-1表示异常
						service.update(ll);
						listeners.remove(i);
					}	
				}
				
			}
		}
	}

	/**
	 * 新增保存
	 * @return
	 * lhyan3
	 * 2014年7月29日
	 */
	public String save(){
		MsgBox msgBox;
		try {
			LogListenerMonitor moni=new LogListenerMonitor();
			if("project".equals(orgType)){
				String[] idValue=projectId.split(",");
				projectId=idValue[0];
				Project project=projectService.getProjectById(projectId);
				moni.setOrgId(projectId);
				moni.setOrgName(project.getName());
				
				moni.setMonitorId(projectMonitorId);
				moni.setMonitorName(projectMonitorName);
				moni.setWatchedId(projectWatchedId);
				moni.setWatchedName(projectWatchedName);
			}else{
				String[] idValue=deptId.split(",");
				deptId=idValue[0];
				String deptName=staffInfoService.getDeptNameValueByKey(deptId);
				moni.setOrgId(deptId);
				moni.setOrgName(deptName);
				
				moni.setMonitorId(deptMonitorId);
				moni.setMonitorName(deptMonitorName);
				moni.setWatchedId(deptWatchedId);
				moni.setWatchedName(deptWatchedName);
			}
			moni.setOrgType(orgType);
			moni.setAutoUpdate("1");//默认自动更新
			service.save(moni);
			
			
			msgBox = new MsgBox(request, getText("add.ok"));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			this.addActionMessage(getText("add.ok"));
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(request, "error in (LogListenerAction.java-save())");
			SysLog.error(e);
			msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
		}
		return "msgBox";
	}
	
	private String getUserEmailById(String[] userids){
		String userEmail="";
		if(userids!=null){
			for(int i=0;i<userids.length;i++){
				try{
					try{
						userEmail+=staffInfoService.findUserByUserid(userids[i]).getEmail();
					}catch(Exception e){
						e.printStackTrace();
					}
				}catch(Exception e){}
				if(i!=userids.length-1){
					userEmail+=",";
				}
			}
		}
		return userEmail;
	}
	
	/**
	 * 跳往添加页面
	 * @return
	 * lhyan3
	 * 2014年7月29日
	 */
	public String addPage(){
		MsgBox msgBox;
		try {
			//获取未完结的项目
			List<Project> projects = projectService.listUnFinishedGroup();
			//从成本控制中获取要监控的部门
			List<SysDatadir> deptSysDataList=BusnDataDir.getObjectListInOrder("costControl.statisticDept");
			request.setAttribute("projects", projects);
			request.setAttribute("depts", deptSysDataList);
			return "add";
		} catch (Exception e) {
			SysLog.error(request, "error in (UserHolsAction.java-addPage())");
			SysLog.error(e);
			msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			return "msgBox";
		}
	}
	
	/**
	 * 跳往修改页面
	 * @return
	 * lhyan3
	 * 2014年7月30日
	 */
	public String modifyPage(){
		MsgBox msgBox;
		try {
			//获取未完结的项目
			List<Project> projects = projectService.listUnFinishedGroup();
			//从成本控制中获取要监控的部门
			List<SysDatadir> deptSysDataList=BusnDataDir.getObjectListInOrder("costControl.statisticDept");
			request.setAttribute("projects", projects);
			request.setAttribute("depts", deptSysDataList);
			String listenerId = request.getParameter("id");
			if(listenerId!=null && !"".equals(listenerId)){
				LogListenerMonitor listener = service.getLogListenerById(listenerId);
				request.setAttribute("listener", listener);
				return "modify";
			}else{
				msgBox = new MsgBox(request, getText("数据异常"));
				msgBox.setButtonType(MsgBox.BUTTON_RETURN);
				return "msgBox";
			}
		} catch (Exception e) {
			SysLog.error(request, "error in (UserHolsAction.java-addPage())");
			SysLog.error(e);
			msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			return "msgBox";
		}
	}
	
	/**
	 * 修改
	 * @return
	 * lhyan3
	 * 2014年7月30日
	 */
	public String update(){
		MsgBox msgBox;
		try {
			LogListenerMonitor moni=service.getLogListenerById(id);
			moni.setOrgType(orgType);
			if("project".equals(orgType)&& StringUtils.isNotBlank(id)){
				String[] idValue=projectId.split(",");
				projectId=idValue[0];
				Project project=projectService.getProjectById(projectId);
				moni.setOrgId(projectId);
				moni.setOrgName(project.getName());
				moni.setMonitorId(projectMonitorId);
				moni.setMonitorName(projectMonitorName);
				moni.setWatchedId(projectWatchedId);
				moni.setWatchedName(projectWatchedName);
			}else{
				String[] idValue=deptId.split(",");
				deptId=idValue[0];
				String deptName=staffInfoService.getDeptNameValueByKey(deptId);
				moni.setOrgId(deptId);
				moni.setOrgName(deptName);
				moni.setMonitorId(deptMonitorId);
				moni.setMonitorName(deptMonitorName);
				moni.setWatchedId(deptWatchedId);
				moni.setWatchedName(deptWatchedName);
			}
			service.update(moni);
			msgBox = new MsgBox(request, "修改成功!");
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		} catch (Exception e) {
			e.printStackTrace();
		}
		msgBox = new MsgBox(request, getText("audit.ActionFailed"));
		msgBox.setButtonType(MsgBox.BUTTON_RETURN);
		return "msgBox";
	}
	
	public void refresh(){
		try {
			list();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 列表
	 * @return
	 * lhyan3
	 * 2014年7月29日
	 */
	@SuppressWarnings("unchecked")
	public String list(){
		MsgBox msgBox;
		try {
			int pageNum = 1;
			int pageSize = 20;
			if (request.getParameter("pageSize") != null
					&& request.getParameter("pageSize").length() > 0) {
				pageSize = Integer.parseInt(request.getParameter("pageSize"));
			}
			if (request.getParameter("pageNum") != null
					&& request.getParameter("pageNum").length() > 0)
				pageNum = Integer.parseInt(request.getParameter("pageNum"));
			Page page = service.getAllListenerMonitor(pageSize,pageNum);
			List<Object> list = page.getQueryResult();
			request.setAttribute("currPage", page);
			String form = request.getParameter("from");
			if (form != null && form.equals("refresh")) {
				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf(page.getPageCount()));
				map.put("recordCount", String.valueOf(page.getRecordCount()));
				JSONUtil util = new JSONUtil(
						"cn.grgbanking.feeltm.loglistener.domain.LogListenerMonitor");
				JSONArray array = util.toJSON(list, map);
				JSONObject input = new JSONObject();
				input.put("pageCount", String.valueOf(page.getPageCount()));
				input.put("recordCount", String.valueOf(page.getRecordCount()));
				input.put("jsonObj", array);
				PrintWriter out = response.getWriter();
				out.print(input);
				return "";
			}
			request.setAttribute("listener", list);
			return "list";
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(request, "error in(LogListenerAction-list())");
			SysLog.error(e);
			msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			return "msgBox";
		}
		
	}
	
	
	/**获取某个角色的用户
	 * wtjiao 2014年11月21日 上午9:03:24
	 * @return
	 */
	public String getRoleUser(){
		String orgType=request.getParameter("orgType");
		List<SysUser> roleUsers=new ArrayList<SysUser>();
		if("project".equals(orgType)){
			String role=request.getParameter("role");
			String[] projectIdValues=request.getParameter("orgId").split(",");
			String projectId=projectIdValues[0];
			if(StringUtils.isNotBlank(projectId)){
				Project p=projectService.getProjectById(projectId);
				if("projectPM".equals(role)){//获取项目经理
					roleUsers.addAll(service.getRoleUser_ProjectManager(p));
					
				}else if("projectMB".equals(role)){//获取项目组成员
					roleUsers.addAll(service.getRoleUser_ProjectMember(p));
					
				}else if("projectDM".equals(role)){//获取部门经理
					roleUsers.addAll(service.getRoleUser_DeptManager(p));
				}
			}
		}else{
			String role=request.getParameter("role");
			String[] deptIdValues=request.getParameter("orgId").split(",");
			String deptId=deptIdValues[0];
			if(StringUtils.isNotBlank(deptId)){
				if("deptMB".equals(role)){//获取部门成员
					//不需要监控的人员
					String expectUserIds=BusnDataDir.getObjectListInOrder("costControl.statisticDeptExpectUserId").get(0).getValue();
					//加入需要监控的人员
					roleUsers.addAll(staffInfoService.getUserByDeptExpecetTheseUsers(deptId, expectUserIds));
				}else if("deptDM".equals(role)){//获取部门经理
					List<SysUser> deptManagers=staffInfoService.getAllNotLeaveDeptManagerByDept(deptId);
					roleUsers.addAll(deptManagers);
				}
			}
		}
		String json=net.sf.json.JSONArray.fromObject(roleUsers).toString();
		ajaxPrint(json);
		return null;
	}
	
	public String synchronize(){
		MsgBox msgBox;
		try{
			listenerSyschronizeUtil.sychronizeLogListener();	 
			msgBox = new MsgBox(request, getText("同步成功！"));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		}catch (Exception e){
			SysLog.error("error in (LogListenerAction.java-list())");
			e.printStackTrace();
			SysLog.error(e);
			msgBox = new MsgBox(request, getText("同步失败！"));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		}
		return "msgBox";
	}
	
	
	
	private void ajaxPrint(String str) {
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			PrintWriter writer = response.getWriter();
			writer.print(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public LogListenerService getService() {
		return service;
	}

	public void setService(LogListenerService service) {
		this.service = service;
	}

	public ProjectService getProjectService() {
		return projectService;
	}

	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}

	public StaffInfoService getStaffInfoService() {
		return staffInfoService;
	}

	public void setStaffInfoService(StaffInfoService staffInfoService) {
		this.staffInfoService = staffInfoService;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getOrgType() {
		return orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public String getProjectMonitorName() {
		return projectMonitorName;
	}

	public void setProjectMonitorName(String projectMonitorName) {
		this.projectMonitorName = projectMonitorName;
	}

	public String getProjectMonitorId() {
		return projectMonitorId;
	}

	public void setProjectMonitorId(String projectMonitorId) {
		this.projectMonitorId = projectMonitorId;
	}

	public String getProjectWatchedName() {
		return projectWatchedName;
	}

	public void setProjectWatchedName(String projectWatchedName) {
		this.projectWatchedName = projectWatchedName;
	}

	public String getProjectWatchedId() {
		return projectWatchedId;
	}

	public void setProjectWatchedId(String projectWatchedId) {
		this.projectWatchedId = projectWatchedId;
	}

	public String getDeptMonitorName() {
		return deptMonitorName;
	}

	public void setDeptMonitorName(String deptMonitorName) {
		this.deptMonitorName = deptMonitorName;
	}

	public String getDeptMonitorId() {
		return deptMonitorId;
	}

	public void setDeptMonitorId(String deptMonitorId) {
		this.deptMonitorId = deptMonitorId;
	}

	public String getDeptWatchedName() {
		return deptWatchedName;
	}

	public void setDeptWatchedName(String deptWatchedName) {
		this.deptWatchedName = deptWatchedName;
	}

	public String getDeptWatchedId() {
		return deptWatchedId;
	}

	public void setDeptWatchedId(String deptWatchedId) {
		this.deptWatchedId = deptWatchedId;
	}
	
	
}
