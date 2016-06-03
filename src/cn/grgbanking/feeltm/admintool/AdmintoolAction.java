package cn.grgbanking.feeltm.admintool;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import cn.grgbanking.feeltm.admintool.service.AdminToolService;
import cn.grgbanking.feeltm.cardRecord.service.CardRecordService;
import cn.grgbanking.feeltm.context.BaseApplicationContext;
import cn.grgbanking.feeltm.costControl.services.CostStatisticService;
import cn.grgbanking.feeltm.dayLog.service.DayLogService;
import cn.grgbanking.feeltm.signrecord.webapp.SignRecordTimeQuartz;
import cn.grgbanking.framework.webapp.BaseAction;

public class AdmintoolAction extends BaseAction{
	@Autowired
	private AdminToolService adminToolService;
	
	/**验证签到数据
	 * wtjiao 2014年12月11日 下午4:47:00
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String exeSign(){
		try{
			SignRecordTimeQuartz service=(SignRecordTimeQuartz) BaseApplicationContext.getAppContext().getBean("signRecordTimeQuartz");
			service.getAddressTime();
			ajaxPrint("true");
		}catch(Exception e){
			e.printStackTrace();
			ajaxPrint("false");
		}
		
		return null;
	}
	
	/**人日同步（人日统计模块 按项目）
	 * wtjiao 2014年12月11日 下午4:43:44
	 * @return
	 */
	public String exePersonday(){
		try{
			/*CommonData commonDataService=(CommonData) BaseApplicationContext.getAppContext().getBean("commonData");
			//同步人日数据
			commonDataService.countPersonDay();*/
			String operateTime = request.getParameter("operateTime");
			adminToolService.countPersonDay(operateTime);
			ajaxPrint("true");
		}catch(Exception e){
			e.printStackTrace();
			ajaxPrint("false");
		}
		
		return null;
	}
	
	/**人日同步（领导首页  按部门）
	 * wtjiao 2014年12月15日 上午10:09:44
	 * @return
	 */
	public String exePersondayByLeader(){
		try{
			/*CommonData service=(CommonData) BaseApplicationContext.getAppContext().getBean("commonData");
			service.countDeptMonthPersonDay();*/
			String operateTime = request.getParameter("operateTime");
			adminToolService.countDeptMonthPersonDay(operateTime);
			ajaxPrint("true");
		}catch(Exception e){
			e.printStackTrace();
			ajaxPrint("false");
		}
		
		return null;
	}
	
	/**月度管理报告
	 * wtjiao 2014年12月15日 上午10:08:36
	 * @return
	 */
	public String exeMonthly(){
		try{
			/*CommonData service=(CommonData) BaseApplicationContext.getAppContext().getBean("commonData");
			service.executeMonthlyManage();*/
			
			String operateTime = request.getParameter("operateTime");
			adminToolService.executeMonthlyManage(operateTime);
			ajaxPrint("true");
		}catch(Exception e){
			e.printStackTrace();
			ajaxPrint("false");
		}
		
		return null;
	}
	
	
	/**修复当月考勤数据
	 * wtjiao 2014年12月15日 上午10:08:36
	 * @return
	 */
	public String repairAttendance(){
		try{
			CardRecordService service=(CardRecordService) BaseApplicationContext.getAppContext().getBean("cardRecordService");
			service.updateEHRGFAttendanceStatus(2);
			ajaxPrint("true");
		}catch(Exception e){
			e.printStackTrace();
			ajaxPrint("false");
		}
		
		return null;
	}
	
	
	/**部门人日基本情况统计和详情统计
	 * @return
	 */
	public String costControl(){
		try{
			CostStatisticService service=(CostStatisticService) BaseApplicationContext.getAppContext().getBean("costStatisticService");
			String begin = request.getParameter("begin");
			String end=request.getParameter("end");
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			Date beginDate=sdf.parse(begin);
			Date endDate=sdf.parse(end);
			//进行统计
			service.statisticDeptCost(beginDate, endDate);
			
			ajaxPrint("true");
		}catch(Exception e){
			e.printStackTrace();
			ajaxPrint("false");
		}
		return null;
			
	}
	
	/**
	 * 统计日期确认工时
	 * @return
	 */
	public String costConfirmHour(){
		try{
			DayLogService service=(DayLogService) BaseApplicationContext.getAppContext().getBean("dayLogService");
			String begin = request.getParameter("begin");
			String end=request.getParameter("end");
			//进行统计
			service.getNoConfirmDayLogForTool(begin, end);
			ajaxPrint("true");
		}catch(Exception e){
			e.printStackTrace();
			ajaxPrint("false");
		}
		return null;
	}
	
	
	/**
	 * 向ajax请求返回数据
	 * 
	 * @param str
	 */
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
}
