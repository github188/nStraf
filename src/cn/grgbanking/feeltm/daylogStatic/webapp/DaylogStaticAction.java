package cn.grgbanking.feeltm.daylogStatic.webapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import cn.grgbanking.feeltm.common.util.SystemHelper;
import cn.grgbanking.feeltm.config.BusnDataDir;
import cn.grgbanking.feeltm.dayLog.domain.DayLog;
import cn.grgbanking.feeltm.dayLog.service.DayLogService;
import cn.grgbanking.feeltm.domain.SysUser;
import cn.grgbanking.feeltm.holiday.service.HolidayService;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.loglistener.domain.WarnInfo;
import cn.grgbanking.feeltm.loglistener.service.TemplateEmailService;
import cn.grgbanking.feeltm.loglistener.service.WarnInfoService;
import cn.grgbanking.feeltm.staff.service.StaffInfoService;
import cn.grgbanking.framework.webapp.BaseAction;

@SuppressWarnings("serial")
public class DaylogStaticAction extends BaseAction {
	@Autowired
	private HolidayService holidayService;
	@Autowired
	private StaffInfoService staffInfoService;
	@Autowired
	private DayLogService dayLogService;
	@Autowired
	private WarnInfoService warnInfoService;
	
	static final SimpleDateFormat sdf1 = new SimpleDateFormat(
			"yyyy-MM-dd hh:mm:ss");
	static final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");

	// 判定是否是正式服务器
	/**
	 * @author ljlian
	 * @return
	 */
	private boolean isServerMachine() {
		try {

			boolean isServer = SystemHelper.isServerMachine();
			SysLog.record(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.format(new Date()) + " serverMachine:" + isServer);
			return isServer;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}
	/**
	 * 将各个部门的日志信息发送给对应部门经理
	 * @author ljlian
	 */
	public synchronized void daylogStaticToLeader() {
		try {
			// 非服务器，不进行任何提示
			if (!this.isServerMachine()) {
				return;
			}
			// 获取日志统计部门  
			// costControl.statisticDept
			Map daylogMonitorDept = BusnDataDir
					.getMapKeyValue("costControl.statisticDept");
			Date date = new Date();
			String currentdate = sdf2.format(date);
			String yesterday = dayLogService.getYesterdayWorkDate(currentdate);// 获取前一天的时间
			Date yes = sdf2.parse(yesterday);//将时间转换成Date
			//循环统计每个部门的没有填写日志的名单
			for (Object map : daylogMonitorDept.entrySet()) {
				String[] dept = map.toString().split("=");
				String deptId = dept[0];//部门  kaifaTwo
				String deptName = dept[1];//部门名称
				//获取部门经理
				List<SysUser> deptManagersList = staffInfoService
						.getAllNotLeaveDeptManagerByDept(deptId);
				if(yes !=null){
					// 判断是否为节假日
					if(!holidayService.isHoliday( date)){ //判断当前时间是否是节假日 是节假日则不发送邮件  不是节假日 ，就将前一天的日志数据发送给领导
						//前一天 指的是去掉 节假日的前一天  如  今天是周一 则前一天就是周五
						List<DayLog> daylogList = dayLogService.getAllDaylogListByDept(
								deptName, yesterday);//因为邮件是每天凌晨统计 前一天的日志
						//拼接邮件内容
						if (daylogList != null
								&& daylogList.size() > 0) {
							// 拼接邮件内容
							String titles = deptName+ yesterday+"员工日志详细信息";//邮件标题
							StringBuilder sb = new StringBuilder();
							sb.append("<caption align='top' style='height:60px;font-size:18px;font-weight:bold;'>"+ deptName+ yesterday+"员工日志详细信息</caption>");
							
							//统计没有填写日志的名单 
                            List<SysUser> notWriteDaylogUser = notWriteDaylog(deptId,daylogList);
                            sb.append("<tr style='background-color:#FFFFE0;'>");
                            sb.append("<td style='width:7%;min-height:50px'><div style='min-height:40px;");
                            sb.append("margin-top:10px;'></div></td>");
                            sb.append("<td colspan='2' style='font-size:15px;font-weight:bold;'>");
                            sb.append("没有填写日志名单：");
                            sb.append("</td>");
                            sb.append("<td colspan='3' style='font-size:14px;'>");
                            if( notWriteDaylogUser != null && notWriteDaylogUser.size() > 0 ){
                                for( int i = 0;i< notWriteDaylogUser.size();i++){
                                    String userName = notWriteDaylogUser.get(i).getUsername();
                                    sb.append(userName+" ");
                                }
                            }else{
                                sb.append("无");
                            }
                            sb.append("</td>");
                            sb.append("</tr>");
							
							
							
							
							
							sb.append("<tr style='height:70px;margin-bottom:35px;'>");
							sb.append( "<div >");
							sb.append("<td style='width:7%;'></td><td style='width:7%;font-size:15px;font-weight:bold;'>姓名</td>");
							sb.append( "<td width='15%' style='font-size:15px;font-weight:bold;'>项目名称</td>");
							sb.append( "<td width='5%' style='font-size:15px;font-weight:bold;'>工时</td>");
							sb.append( "<td style='font-size:15px;font-weight:bold;'>日志内容</td>");
							sb.append( "</div>");
							sb.append("</tr>");
							for(int i = 0;i<daylogList.size()-1;i++ ){
								DayLog daylog =daylogList.get(i);
									sb.append("<tr>");
									sb.append("<td style='width:7%;min-height:50px'><div style='min-height:40px;");
									sb.append("margin-top:10px;'></div></td>");
									sb.append("<td  width='7%' style='font-size:14px;'><div style='min-height:40px;padding-bottom:10px;'>");
									sb.append(daylog.getUserName()+"</div></td>");//员工姓名
									sb.append("<td  width='15%' style='font-size:14px;'><div style='min-height:40px;padding-bottom:10px;'>");
									sb.append(daylog.getGroupName()+"</div></td>");//项目名称
									sb.append("<td width='5%' style='font-size:14px;'><div style='min-height:40px;padding-bottom:10px;'>");
									sb.append(daylog.getSubTotal()+"</div></td>");//工时
									sb.append("<td  style='font-size:14px;'><div style='min-height:40px;40px;padding-bottom:10px;'>");
									if( daylog.getReason() != null){
										sb.append(daylog.getReason());//日志内容
									}
									sb.append("</div></td>");
									sb.append("</tr>");
							}
							//统计没有填写日志的名单
							//List<SysUser> notWriteDaylogUser = notWriteDaylog(deptId,daylogList);
							sb.append("<tr style='background-color:#FFFFE0;'>");
							sb.append("<td style='width:7%;min-height:50px'><div style='min-height:40px;");
							sb.append("margin-top:10px;'></div></td>");
							sb.append("<td colspan='2' style='font-size:15px;font-weight:bold;'>");
							sb.append("没有填写日志名单：");
							sb.append("</td>");
							sb.append("<td colspan='3' style='font-size:14px;'>");
							if( notWriteDaylogUser != null && notWriteDaylogUser.size() > 0 ){
								for( int i = 0;i< notWriteDaylogUser.size();i++){
									String userName = notWriteDaylogUser.get(i).getUsername();
									sb.append(userName+" ");
								}
							}else{
								sb.append("无");
							}
							sb.append("</td>");
							sb.append("</tr>");
							
							String contents = sb.toString();
							// 发送邮件
							sendAndSaveLog2Leader(deptManagersList,titles, contents);
							
						}
					}
				}
			}
			if(yes !=null){
				// 判断是否为节假日
				if(!holidayService.isHoliday( date)){ 
					daylogStaticToProjectManDept();
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}
	/**
	 * 
	 * 发送给项目管理部进行统计  （统计各个部门没有填写日志的名单）
	 */
	public void daylogStaticToProjectManDept() {
		try {
			// 非服务器，不进行任何提示
			if (!this.isServerMachine()) {
				return;
			}
			// 获取日志统计部门  
			// costControl.statisticDept
			Map daylogMonitorDept = BusnDataDir
					.getMapKeyValue("costControl.statisticDept");
			Date date = new Date();
			String currentdate = sdf2.format(date);
			String yesterday = dayLogService.getYesterdayWorkDate(currentdate);// 获取前一天的时间
			String titles = yesterday+"员工日志详细信息";//邮件标题
			StringBuilder sb = new StringBuilder();
			sb.append("<caption align='top' style='height:60px;font-size:18px;font-weight:bold;'>"+ yesterday+"没有填写日志名单</caption>");
			for (Object map : daylogMonitorDept.entrySet()) {
				String[] dept = map.toString().split("=");
				String deptId = dept[0];//部门  kaifaTwo
				String deptName = dept[1];//部门名称
				//获取部门经理
				List<SysUser> deptManagersList = staffInfoService
						.getAllNotLeaveDeptManagerByDept(deptId);
				Date yes = sdf2.parse(yesterday);
				if(yes !=null){
					// 判断是否为节假日
					if(!holidayService.isHoliday( date)){ //判断当前时间是否是节假日 是节假日则不发送邮件  不是节假日 ，就将前一天的日志数据发送给领导
						//前一天 指的是去掉 节假日的前一天  如  今天是周一 则前一天就是周五
						List<DayLog> daylogList = dayLogService.getAllDaylogListByDept(
								deptName, yesterday);//因为邮件是每天凌晨统计 前一天的日志
						//拼接邮件内容
						if (daylogList != null
								&& daylogList.size() > 0) {
							// 拼接邮件内容
							//统计没有填写日志的名单
							List<SysUser> notWriteDaylogUser = notWriteDaylog(deptId,daylogList);
							sb.append("<tr style='background-color:#FFFFE0;'>");
							sb.append("<td style='width:7%;min-height:50px'><div style='min-height:40px;");
							sb.append("margin-top:10px;'></div></td>");
							sb.append("<td colspan='2' style='font-size:15px;font-weight:bold;'>");
							sb.append(deptName);
							sb.append("</td>");
							sb.append("<td colspan='3' style='font-size:14px;'>");
							if( notWriteDaylogUser != null && notWriteDaylogUser.size() > 0 ){
								for( int i = 0;i< notWriteDaylogUser.size();i++){
									String userName = notWriteDaylogUser.get(i).getUsername();
									sb.append(userName+" ");
								}
							}else{
								sb.append("无");
							}
							sb.append("</td>");
							sb.append("</tr>");
						}
					}
				}
			}
			// 发送邮件
			String contents = sb.toString();
			sendEmail(titles, contents);
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 发送邮件函数 
	 * 这里可以将sendTemplateMail 中tomail的参数写成自己的，如zzwen6@grgbanking.com 这样就不会将测试邮件发到项目管理部或者经理处
	 * sendEmail:描述 <br/>
	 * 2016年4月8日 上午11:10:55
	 * @param titles 标题
	 * @param contents 内容
	 * @author 
	 * @修改记录: 方法注释添加 @author zzwen6 <br/>
	 */
	private void sendEmail(String titles, String contents) {
		try {
			Map daylogMail1 = BusnDataDir
				.getMapKeyValue("costControl.sendEmailUserId");
		 	String mailstr = daylogMail1.get("deptdetailuser").toString();
		 	String[] mailuser =mailstr.split(",");
			TemplateEmailService emailService=new TemplateEmailService();
			//填充模版中的指定字段
			Map<String,String> templateTagMap=new HashMap<String,String>();
			templateTagMap.put("warnInfo",contents);
			Date date = new Date();
			for( int i=0;i<mailuser.length;i++){
			    // 将sendTemplateMail 中tomail的参数写成自己的  ---->可测试
				String sendContent =emailService.sendTemplateMail(mailuser[i], titles, "daylogStaticToDeptLeader.ftl", templateTagMap);
//				String sendContent =emailService.sendTemplateMail("zzwen6@grgbanking.com", titles, "daylogStaticToDeptLeader.ftl", templateTagMap);
				if(!"0".equals(sendContent)){
					WarnInfo warn=new WarnInfo();
					warn.setWarnWay("email");
					warn.setWarnType("notification");
					warn.setWarnTime(Calendar.getInstance().getTime());
					warn.setWarnContent(sendContent);
					warnInfoService.save(warn);
				}
			}
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	/** 发送邮件并记录日志
	 * 注释添加：这里  指的是部门经理  也可修改成自己邮件作测试用
	 * 
	 * @param sendPersonList
	 * @param content 
	 * @author ljlian
	 */
	private synchronized void sendAndSaveLog2Leader(List<SysUser> sendPersonList,String title, String content) {
		TemplateEmailService emailService=new TemplateEmailService();
		try {
			//发送邮件
			for(int i=0;i<sendPersonList.size();i++){
				SysUser user=sendPersonList.get(i);
				//填充模版中的指定字段
				Map<String,String> templateTagMap=new HashMap<String,String>();
				templateTagMap.put("warnInfo",content);
				Date date = new Date();
				String sendContent =emailService.sendTemplateMail(sendPersonList.get(i).getEmail(), title, "daylogStaticToDeptLeader.ftl", templateTagMap);
//				String sendContent =emailService.sendTemplateMail("zzwen6@grgbanking.com", title, "daylogStaticToDeptLeader.ftl", templateTagMap);
				SysLog.info(sdf1.format(date)+"日志统计邮件接收者："+sendPersonList.get(i).getUsername());
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 统计没有填写日志的人员名单
	 * @param deptId  部门的ID
	 * @param daylogList  填写了日志的名单
	 * @return
	 */
	//没有填写日志的名单 应该是  本部门的总人数 - 统计部门例外人员 - 额外不需要统计人员
	public List<SysUser> notWriteDaylog( String deptId, List<DayLog> daylogList){
		List<SysUser> notWriteDaylog = null;
		try {
			//统计部门例外人员   经理等
			String exceptDaylogMonitorDept = BusnDataDir.getObjectDetail("costControl.statisticDeptExpectUserId.expecetUserIds").trim().toString();
			//额外不需要统计的人员
			String notWatchedStr = BusnDataDir.getObjectDetail("projectMonitor.logListenMonitor.addtionNotWatched").trim();
			String[]  notWatchedArr = notWatchedStr.split(",");
			//根据部门id 获取部门的所有员工信息
			List<SysUser> deptAllUser =  staffInfoService.getStaffByDeptKey(deptId);
			//部门总人数减去 填写了日志的人员
			if( daylogList != null && daylogList.size()>0){
				for( int i= 0; i<daylogList.size();i++){
					String userId = daylogList.get(i).getUserId();
					if( deptAllUser != null && deptAllUser.size()>0){
						for( int j =0;j<deptAllUser.size();j++){
							
							if( userId.equals(deptAllUser.get(j).getUserid())){
								deptAllUser.remove(j);//去掉不需要统计的人员
							}
						}
					}
				}
			}
			//减掉 	额外不需要统计的人员
			for( int i =0 ;i<notWatchedArr.length;i++){
					String notWatchedUserId = notWatchedArr[i].trim().toString();
					if (  deptAllUser != null && deptAllUser.size() >0){
						for( int j =0;j<deptAllUser.size();j++){
							String userId = deptAllUser.get(j).getUserid();
							if( userId !=null && !"".equals(notWatchedUserId)
									){
								if( userId.equals(notWatchedUserId)){
									deptAllUser.remove(j);//去掉不需要统计的人员
								}
							}
						}
					}
				}
			//减掉 统计部门例外人员
			String[] exceptDaylogMonitorDeptArr = exceptDaylogMonitorDept.split(",");
			for( int i= 0;i<exceptDaylogMonitorDeptArr.length;i++){
			   
				String daylogMonitorDeptUserId = exceptDaylogMonitorDeptArr[i].trim().toString();  // 去掉空白字符
				if (  deptAllUser != null && deptAllUser.size() >0){
					for( int j =0;j<deptAllUser.size();j++){
						String userId = deptAllUser.get(j).getUserid();
						if( userId !=null && !"".equals(daylogMonitorDeptUserId)
								){
							if( userId.equals(daylogMonitorDeptUserId)){
								deptAllUser.remove(j);//去掉不需要统计的人员
							}
						}
					}
				}
			}
			notWriteDaylog = deptAllUser;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return notWriteDaylog;
	}
		
		
		
	

	public HolidayService getHolidayService() {
		return holidayService;
	}

	public void setHolidayService(HolidayService holidayService) {
		this.holidayService = holidayService;
	}

	public StaffInfoService getStaffInfoService() {
		return staffInfoService;
	}

	public void setStaffInfoService(StaffInfoService staffInfoService) {
		this.staffInfoService = staffInfoService;
	}

	public DayLogService getDayLogService() {
		return dayLogService;
	}

	public void setDayLogService(DayLogService dayLogService) {
		this.dayLogService = dayLogService;
	}

	public WarnInfoService getWarnInfoService() {
		return warnInfoService;
	}

	public void setWarnInfoService(WarnInfoService warnInfoService) {
		this.warnInfoService = warnInfoService;
	}


	 
}
