package cn.grgbanking.feeltm.workloadstatistics.webapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import cn.grgbanking.feeltm.config.BusnDataDir;
import cn.grgbanking.feeltm.domain.SysDatadir;
import cn.grgbanking.feeltm.domain.SysUser;
import cn.grgbanking.feeltm.holiday.service.HolidayService;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.loglistener.domain.WarnInfo;
import cn.grgbanking.feeltm.loglistener.service.TemplateEmailService;
import cn.grgbanking.feeltm.loglistener.service.WarnInfoService;
import cn.grgbanking.feeltm.project.domain.Project;
import cn.grgbanking.feeltm.staff.service.StaffInfoService;
import cn.grgbanking.feeltm.util.DatetimeUtil;
import cn.grgbanking.feeltm.workloadstatistics.service.WorkLoadStatisticService;
import cn.grgbanking.framework.webapp.BaseAction;

public class WorkloadStatisticAction extends BaseAction {
	@Autowired
	private StaffInfoService staffInfoService;
	@Autowired
	private WarnInfoService warnInfoService;
	@Autowired
	private WorkLoadStatisticService workLoadStatisticService;
	@Autowired
	private HolidayService holidayService;

	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	public void workloadStatistic() {
		String content = "";
		String title = "本周的工作量統計";

		try {

			String mondayofWeek = DatetimeUtil.getMondayOFWeek();// 周一
			Date start = sdf.parse(mondayofWeek);
			String starttime = sdf.format(start);
			Date end = new Date(start.getTime() + 5 * 24 * 60 * 60 * 1000);// 周五
			String endtime = sdf.format(end);// 统计的结束时间

			// 获取要统计的部门
			List<SysDatadir> deptSysDataList = BusnDataDir
					.getObjectListInOrder("costControl.statisticDept");
			String notWatchedStr = BusnDataDir
					.getObjectDetail("projectMonitor.logListenMonitor.addtionNotWatched");
			// 不用填写日志的名单
			String expectUserIds = (BusnDataDir.getObjectListInOrder(
					"costControl.statisticDeptExpectUserId").get(0).getValue());
			String exceptUserStr = expectUserIds + "," + notWatchedStr;
			String[] exceptUserArray = splitedArray(exceptUserStr);
			// 获取有效的 部门统计人数 (应该填写的人数)
			content += "<br/><p style='font-size:15px;font-weight:bold;'>部门                                          需要填写日志的人数                         </p><br/><br/>";
			if (deptSysDataList != null && deptSysDataList.size()>0) {

				for (int i = 0; i < deptSysDataList.size(); i++) {
					content += "<br/>";
					String depname = deptSysDataList.get(i).getKey();
					List<SysUser> allUser = workLoadStatisticService
							.getNeedUser(exceptUserArray, depname);
					content += deptSysDataList.get(i).getValue() + " &nbsp;&nbsp; "
							+ allUser.size() + "<br/>";

				}
			}

			// 獲取 郵件的發送 內容 未填寫日誌的明單
			content += getNotWriteDayLogList(start, end, deptSysDataList,
					exceptUserArray);

			// 未确认 明单(周一到周五)
			// 部门经理未确认 经理
			List<SysUser> deptMangagerNotConfirm = workLoadStatisticService
					.getNoConfirmDept(starttime, endtime);
			content += "<br/><p style='font-size:15px;font-weight:bold;'>本周非项目任务工作量未确认的部门经理名单  ：</p><br/><br/>";
			if(deptMangagerNotConfirm!=null && deptMangagerNotConfirm.size()>0){
				for (int i = 0; i < deptMangagerNotConfirm.size(); i++) {
					if (i % 10 == 0) {
						content += "<br/>";
					} else{
					content += deptMangagerNotConfirm.get(i).getUsername() + "&nbsp;&nbsp;" ;
					}
					
					content += "<br/>";
				}
			}
			// 项目经理未确认的项目信息
			List<Project> prjManagerNotConfirm = workLoadStatisticService
					.getNoConfirmPrjname(starttime, endtime);
			content += "<br/><p style='font-size:15px;font-weight:bold;'>本周项目任务工作量未确认的项目经理名单  ：</p><br/>";
			if(prjManagerNotConfirm!=null && prjManagerNotConfirm.size()>0){
				for (int i = 0; i < prjManagerNotConfirm.size(); i++) {
					if (i % 10 == 0) {
						content += "<br/>";
					} else {
						// 列出 项目经理的名字
						content += prjManagerNotConfirm.get(i).getProManager()+"&nbsp;&nbsp;"+ prjManagerNotConfirm.get(i).getName()
								+ "  ";
					}
					content += "<br/>";
				}
			
			}
			SysLog.info(" 工作量统计的邮件内容： " +content);
			// 郵件發送人
			List<SysUser> sendEmailList = getEmailList();
			// 发送并保存发送结果
			sendAndSaveLog2Leader(sendEmailList,title,content); 
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	/**
	 * 获取没有填写  日志的名单
	 * @param start  开始日期
	 * @param end    结束日期
	 * @param deptSysDataList  需要统计的部门
	 * @param exceptUserArray   不需要统计的人
	 * @return
	 */

	public String getNotWriteDayLogList(Date start, Date endtime,
			List<SysDatadir> deptSysDataList, String[] exceptUserArray) {
		String content = "";
		List<SysUser> notwriteDaylogUser = new ArrayList<SysUser>();

		content += "<br/><p style='font-size:15px;font-weight:bold;'>没有写日志的名单  </p>  <br/><br/>";

		Date  end=endtime;
		// 循环统计 每一天没有填写日志的 用户
		while (start.before(end)) {
			// Date d=start;
			String querytime = sdf.format(start);
			boolean isHoliday = holidayService.isHoliday(start);
			if(!isHoliday){
				if (deptSysDataList != null  &&  deptSysDataList.size()>0) {
					for (int j = 0; j < deptSysDataList.size(); j++) {
						content += "<br/>";
						String depname = deptSysDataList.get(j).getKey();
						if( depname!=null  &&  depname.length()>0){
						notwriteDaylogUser = workLoadStatisticService
								.getNotWriteDaylogUserNameList(exceptUserArray,
										depname, querytime);
						}
						content += deptSysDataList.get(j).getValue() + " &nbsp;&nbsp;  "
								+ querytime ;
						if (notwriteDaylogUser != null && notwriteDaylogUser.size()>0 ) {
							content +=" &nbsp;  "+notwriteDaylogUser.size()+"<br/>";
							for (int k = 0; k < notwriteDaylogUser.size(); k++) {
								if (k % 10 == 0) {
									content += " &nbsp;" +notwriteDaylogUser.get(k)
											.getUsername() + "<br/>";
								} else {
									content +="&nbsp;" +notwriteDaylogUser.get(k)
											.getUsername() ;
								}

							}
							content += "<br/>";

						}
					}
					content += "<br/>";

				}
			}

			start = new Date(start.getTime() + 1 * 24 * 60 * 60 * 1000);
		}
		content += "<br/>";

		return content;

	}
	
	

	/**
	 * 獲取郵件接收人的名單
	 * 
	 * @return
	 */
	public List<SysUser> getEmailList() {
		/* 发送邮件名单 */
		List<SysUser> sendUserList = new ArrayList<SysUser>();
		SysUser sysUser = null;
		// 郵件的發送人
		String sendUserStr = BusnDataDir
				.getObjectDetail("projectMonitor.logListenMonitor.sendEmailUsers");
		String[] sendUserArray = org.springframework.util.StringUtils
				.tokenizeToStringArray(sendUserStr, ",，");

		if (sendUserArray != null && sendUserArray.length > 0) {
			for (int l = 0; l < sendUserArray.length; l++) {
				String userid = sendUserArray[l].trim();
				sysUser = new SysUser();
				sysUser.setId(userid);
				sysUser.setEmail(this.getUserEmailById(new String[] { userid }));
				sendUserList.add(sysUser);
			}
		}

		return sendUserList;
	}

	/**
	 * 指定的用户id中是否包含特定的用户
	 * 
	 * @param expectUserIds
	 * @param userid
	 * @return
	 */
	private boolean containsUser(String expectUserIds, String userid) {
		try {
			String[] users = org.springframework.util.StringUtils
					.tokenizeToStringArray(expectUserIds, ",，");
			for (String user : users) {
				if (user.equals(userid)) {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 根据 ,， split一个字符串
	 * 
	 * @param neededsplit
	 *            需要被split 的字符串
	 * @return
	 */
	public String[] splitedArray(String neededsplit) {
		String[] exceptUserArray = null;
		try {
			exceptUserArray = org.springframework.util.StringUtils
					.tokenizeToStringArray(neededsplit, ",，");
			return exceptUserArray;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return exceptUserArray;
	}

	private String getUserEmailById(String[] userids) {
		String userEmail = "";
		if (userids != null  &&  userids.length>0) {
			for (int i = 0; i < userids.length; i++) {
				try {
					try {
						userEmail += staffInfoService.findUserByUserid(
								userids[i]).getEmail();
					} catch (Exception e) {
						e.printStackTrace();
					}
				} catch (Exception e) {
				}
				if (i != userids.length - 1) {
					userEmail += ",";
				}
			}
		}
		return userEmail;
	}

	/**
	 * 发送邮件并记录日志 wtjiao 2014年10月27日 下午3:54:08
	 * 
	 * @param sendPersonList
	 *            郵件發送人的list
	 * @param content
	 */
	private void sendAndSaveLog2Leader(List<SysUser> sendPersonList,
			String title, String content) {
		TemplateEmailService emailService = new TemplateEmailService();

		// 记录发送成功的人员
		List<SysUser> sendedList = new ArrayList<SysUser>();
		// 发送邮件
		for (int i = 0; i < sendPersonList.size(); i++) {
			SysUser user = sendPersonList.get(i);
			// 填充模版中的指定字段
			Map<String, String> templateTagMap = new HashMap<String, String>();
			Date curDate = new Date();
			templateTagMap.put("logdate", format.format(curDate));
			templateTagMap.put("warnInfo", content);

			String sendContent = emailService.sendTemplateMail(user.getEmail(),
					title, "workloadsatistic.ftl", templateTagMap);

			// 保存发送记录
			if (!"0".equals(sendContent)) {
				WarnInfo warn = new WarnInfo();
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

}
