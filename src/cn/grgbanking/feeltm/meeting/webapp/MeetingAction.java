package cn.grgbanking.feeltm.meeting.webapp;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.grgbanking.feeltm.domain.testsys.Meeting;
import cn.grgbanking.feeltm.domain.testsys.WeekReport;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.meeting.service.MeetingService;
import cn.grgbanking.feeltm.report.domain.WeekRecord;
import cn.grgbanking.feeltm.report.service.WeekReportService;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.feeltm.util.OaOrEmail;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;

import com.opensymphony.xwork2.ActionContext;

import edu.emory.mathcs.backport.java.util.Collections;

public class MeetingAction extends BaseAction{
	
	private MeetingService meetingService;
	private WeekReportService weekReportService;
	
	private Meeting meeting ;
	
	private String start;
	private String end;
	private String subject;
	private String writer;
	private String compere;
	private String content;
	private String audit;
	
	private Map<String,String> umap=new LinkedHashMap<String,String>();
	private Map<String,String> umaps=new LinkedHashMap<String,String>();
	private List<String> unames;
	
	private String attendPersons;
	private String absentPersons;
	private String main;
	private String copy;
	private String reAudit;
	private String send_flag;
	private String PersonReauditstatus;
	
	
	private String content_tmp;	
	
	//白盒，黑盒，质量，自动化 新增周例会
	public String add(){
		//unames=(List<String>)session.get("empNames");
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		writer=userModel.getUsername();
		audit="汤飞";
		unames=meetingService.getAllNames();
		
		unames.add("汤飞");
		umap.put("罗攀峰", "罗攀峰");	
		umap.put("王全胜", "王全胜");
		umap.put("杜高峰", "杜高峰");
		umap.put("汤飞", "汤飞");
		umaps.put("王全胜(出差)","王全胜(出差)");
		umaps.put("王全胜(请假)","王全胜(请假)");
		umaps.put("汤飞(出差)","汤飞(出差)");
		umaps.put("汤飞(请假)","汤飞(请假)");
		int iNum = 1;
		int jNum = 1;
		String unamestmp;
		unamestmp = "";
		main = "";
		copy = "罗攀峰,王全胜";
		for(String u:unames){
				umap.put(u, u);
				umaps.put(u+"(出差)", u+"(出差)");
				umaps.put(u+"(请假)", u+"(请假)");
				if(iNum ==1){
					main=u;
				}	
				else
				{
					main+=(","+u);
				}
				iNum++;
		}	
		jNum=iNum;
		for(String u:unames){
           if(jNum==iNum && !u.equals("汤飞")){
        	   unamestmp = unamestmp + u;
           }
           else if(jNum!=iNum && !u.equals("汤飞"))
           {
        	   unamestmp = unamestmp + "、" + u;
           }
           jNum--;
        }
		
		String[] str={"white_plan","black_plan","quality_plan","auto_plan","white_summary","black_summary","quality_summary","auto_summary"};
		List<WeekReport> reports=weekReportService.getAllReportsByWeek();
		Map<String,String> mm=new HashMap<String,String>(); 
		//本周计划
		for(int i=0;i<8;i++){
			mm.put(str[i], reports.get(i).getTaskDesc());
		}
		
		StringBuffer sb=new StringBuffer();
		sb.append("一、会议内容").append("\r\n");
		sb.append("（一）工作问题交流和讨论").append("\r\n");
		sb.append("\r\n");
		
		sb.append("（二）部门总结").append("\r\n").append("\r\n");
		
		sb.append("（三）各小组汇报上周测试项目完成情况及本周工作计划").append("\r\n");
		
		sb.append("---基础软件测试组---").append("\r\n");
		sb.append(" {上周工作完成情况}").append("\r\n");
		sb.append(parseTaskDesc(mm.get("white_summary"))).append("\r\n");
		sb.append("{本周工作计划}").append("\r\n");
		sb.append(parseTaskDesc(mm.get("white_plan"))).append("\r\n");
		
		sb.append("---技术支持组---").append("\r\n");
		sb.append(" {上周工作完成情况}").append("\r\n");
		sb.append(parseTaskDesc(mm.get("black_summary"))).append("\r\n");
		sb.append("{本周工作计划}").append("\r\n");
		sb.append(parseTaskDesc(mm.get("black_plan"))).append("\r\n");
		
		sb.append("---应用软件测试组---").append("\r\n");
		sb.append(" {上周工作完成情况}").append("\r\n");
		sb.append(parseTaskDesc(mm.get("quality_summary"))).append("\r\n");
		sb.append("{本周工作计划}").append("\r\n");
		sb.append(parseTaskDesc(mm.get("quality_plan"))).append("\r\n");
		
		sb.append("---质量管理组---").append("\r\n");
		sb.append(" {上周工作完成情况}").append("\r\n");
		sb.append(parseTaskDesc(mm.get("auto_summary"))).append("\r\n");
		sb.append("{本周工作计划}").append("\r\n");
		sb.append(parseTaskDesc(mm.get("auto_plan"))).append("\r\n");

		sb.append("二、主持人人员顺序：").append("\r\n");
		sb.append(unamestmp).append("\r\n").append("\r\n");
		sb.append("三、会议记录人员顺序：").append("\r\n");
		sb.append(unamestmp).append("\r\n").append("\r\n");
		sb.append("四、下周会议主持人和记录人：").append("\r\n").append("\r\n");
		sb.append("（若本周主持人或记录人出差,自动跳转下一主持人或记录人）");
		
		content_tmp=sb.toString();
		return "add";
	}
	public String save() throws Exception {
		try {
			UserModel userModel = (UserModel) request.getSession().getAttribute(
					Constants.LOGIN_USER_KEY);
			boolean flag = false;
			meeting.setSendStatus("未发送");
			
			meeting.setAbsentPersons(absentPersons.replace(",","、"));
			meeting.setAttendPersons(attendPersons.replace(",","、"));
			meeting.setMain(main.replace(",","、"));
			meeting.setCopy(copy.replace(",","、"));
			meeting.setReAudit(reAudit.replace(",","、"));
			
			if(!meeting.getReAudit().equals(""))
			{
				String[] reaudittmp=meeting.getReAudit().trim().split("、");
				StringBuffer sb=new StringBuffer();
				for(String mm:reaudittmp){	    	
					sb.append(mm).append("$$");
					sb.append("待审核").append("@@@");
				}
				meeting.setReauditPersonstatus(sb.toString());
			}
			flag=meetingService.add(meeting);
			if (flag == true) {
				MsgBox msgBox = new MsgBox(request, getText("add.ok"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				this.addActionMessage(getText("add.ok"));
			
			} else {
				MsgBox msgBox = new MsgBox(request,
						getText("operInfoform.addfaile"));
				addActionMessage(getText("operInfoform.addfaile"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request,
					getText("operInfoform.addfaile"), new String[] { e
							.toString() });
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			this.addActionMessage(getText("operInfoform.addfaile"));
			boolean hasActionMessage = this.hasActionMessages();
			request.setAttribute("hasActionMessage", hasActionMessage);
			return "msgBox";
		}
		boolean hasActionMessage = this.hasActionMessages();
		request.setAttribute("hasActionMessage", hasActionMessage);
		return "msgBox";
	}

	
	public String UpAuditing() throws Exception {
		try {
			UserModel userModel = (UserModel) request.getSession().getAttribute(
					Constants.LOGIN_USER_KEY);
			boolean flag = false;		
			String ids = request.getParameter("ids");
			meeting=meetingService.getMeetingById(ids);
			if(!meeting.getAuditStatus().equals("未经审核通过"))
			{
				MsgBox msgBox1 = new MsgBox(request,getText("提交失败：已经在走审核流程"));
				msgBox1.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}
			if(!meeting.getWriter().equals(userModel.getUsername()))
			{
				MsgBox msgBox1 = new MsgBox(request,getText("提交失败：你没有提交审核该会议纪要的权限"));
				msgBox1.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}
			if(!meeting.getReAudit().equals(""))
			{
				String[] reaudittmp=meeting.getReAudit().trim().split("、");
		    	OaOrEmail oa=new OaOrEmail();
				SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
				String date=format.format(meeting.getCurrentDateTime());
			    for(String mm:reaudittmp){
					String uid=meetingService.getEmpByName(mm).getUserid();
					oa.oaContent(uid, "请审核会议纪要："+meeting.getSubject()+date);
			    }
				meeting.setAuditStatus("待审核");
				meeting.setSendStatus("未发送");
				meetingService.update(meeting);
				MsgBox msgBox = new MsgBox(request, getText("提交成功"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				this.addActionMessage(getText("提交成功"));
			}
			else
			{
				MsgBox msgBox1 = new MsgBox(request,getText("提交失败：该会议纪要没有选择复核人员"));
				msgBox1.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}
		
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request,
					getText("operInfoform.addfaile"), new String[] { e
							.toString() });
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			this.addActionMessage(getText("operInfoform.addfaile"));
			boolean hasActionMessage = this.hasActionMessages();
			request.setAttribute("hasActionMessage", hasActionMessage);
			return "msgBox";
		}
		boolean hasActionMessage = this.hasActionMessages();
		request.setAttribute("hasActionMessage", hasActionMessage);
		return "msgBox";
	}   
	
	public String delete() throws Exception {
		try {
			String ids = request.getParameter("ids");
			System.out.println("ids: "+ids);
			int iCount = 0;
			String[] sids = ids.split(",");
			if("all".equals(sids[0])){
				String[] arr = new String[sids.length-1];
				System.arraycopy(sids, 1, arr, 0, arr.length);
				sids = arr;
			}
			if (sids != null) {
				for (int i = 0; i < sids.length; i++) {
					Meeting temp = meetingService.getMeetingById(sids[i]);
					if(!"审核通过".equals(temp.getAuditStatus()))
					meetingService.delete(temp);
//					SysLog.operLog(request, Constants.OPER_MODIFY_VALUE, temp
//							.getTaskDesc());
//					SysLog.info("User:" + userModel.getUserid()
//							+ " to delete a SpecialRegulation: "
//							+ temp.getTaskDesc());

					iCount++;
				}
			}

			MsgBox msgBox = new MsgBox(request, getText("rule.del.ok"), "rule",
					new String[] { String.valueOf(iCount) });
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request, getText("operator.addfaile"));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		}
		return "msgBox";
	}

	public String edit() throws Exception {
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		String ids = request.getParameter("ids");
		unames=meetingService.getAllNames();
		unames.add("汤飞");
		umap.put("罗攀峰", "罗攀峰");
		umap.put("王全胜", "王全胜");
		umap.put("杜高峰", "杜高峰");
		umap.put("汤飞", "汤飞");
		umaps.put("王全胜(出差)","王全胜(出差)");
		umaps.put("王全胜(请假)","王全胜(请假)");
		umaps.put("汤飞(出差)","汤飞(出差)");
		umaps.put("汤飞(请假)","汤飞(请假)");
		int iNum = 1;
		int jNum = 1;
		String unamestmp;
		unamestmp = "";
		for(String u:unames){
				umap.put(u, u);
				umaps.put(u+"(出差)", u+"(出差)");
				umaps.put(u+"(请假)", u+"(请假)");
				iNum++;
		}	
		jNum=iNum;
		for(String u:unames){
           if(jNum==iNum && !u.equals("汤飞")){
        	   unamestmp = unamestmp + u;
           }
           else if(jNum!=iNum && !u.equals("汤飞"))
           {
        	   unamestmp = unamestmp + "、" + u;
           }
           jNum--;
        }
		meeting=meetingService.getMeetingById(ids);
		attendPersons=meeting.getAttendPersons();
		absentPersons=meeting.getAbsentPersons();
		main=meeting.getMain();
		copy=meeting.getCopy();
		reAudit=meeting.getReAudit();
		attendPersons=attendPersons.replace("、",",");
		absentPersons=absentPersons.replace("、",",");
		copy=copy.replace("、",",");
		main=main.replace("、",",");
		reAudit=reAudit.replace("、",",");
		String forwardPage="edit";
		try {
			if(meeting.getAuditStatus().equals("审核通过")&&(!meeting.getSign().equals(userModel.getUsername()))){
				MsgBox msgBox = new MsgBox(request,
						getText("operInfoform.updatefaile",new String[]{"审核通过的会议记录不能进行修改，如需修改，请联系签发人员"}));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}
			//unames=(List<String>)session.get("empNames");
			
			if(meeting.getAuditStatus().equals("未经审核通过"))
			{
				if(meeting.getWriter().equals(userModel.getUsername()))
				{
					forwardPage="edit";
					
					String[] str={"white_plan","black_plan","quality_plan","auto_plan","white_summary","black_summary","quality_summary","auto_summary"};
					List<WeekReport> reports=weekReportService.getAllReportsByWeek();
					Map<String,String> mm=new HashMap<String,String>(); 
					//本周计划
					for(int i=0;i<8;i++){
						mm.put(str[i], reports.get(i).getTaskDesc());
					}
					
					StringBuffer sb=new StringBuffer();
//					sb.append("一、会议内容").append("\r\n");
//					sb.append("（一）工作问题交流和讨论").append("\r\n");
//					sb.append("\r\n");
//					
//					sb.append("（二）部门总结").append("\r\n").append("\r\n");
					
					sb.append("（三）各小组汇报上周测试项目完成情况及本周工作计划").append("\r\n").append("\r\n");
					
					sb.append("---基础软件测试组---").append("\r\n");
					sb.append(" {上周工作完成情况}").append("\r\n");
					sb.append(parseTaskDesc(mm.get("white_summary"))).append("\r\n");
					sb.append("{本周工作计划}").append("\r\n");
					sb.append(parseTaskDesc(mm.get("white_plan"))).append("\r\n");
					
					sb.append("---技术支持组---").append("\r\n");
					sb.append(" {上周工作完成情况}").append("\r\n");
					sb.append(parseTaskDesc(mm.get("black_summary"))).append("\r\n");
					sb.append("{本周工作计划}").append("\r\n");
					sb.append(parseTaskDesc(mm.get("black_plan"))).append("\r\n");
					
					sb.append("---应用软件测试组---").append("\r\n");
					sb.append(" {上周工作完成情况}").append("\r\n");
					sb.append(parseTaskDesc(mm.get("quality_summary"))).append("\r\n");
					sb.append("{本周工作计划}").append("\r\n");
					sb.append(parseTaskDesc(mm.get("quality_plan"))).append("\r\n");
					
					sb.append("---质量管理组---").append("\r\n");
					sb.append(" {上周工作完成情况}").append("\r\n");
					sb.append(parseTaskDesc(mm.get("auto_summary"))).append("\r\n");
					sb.append("{本周工作计划}").append("\r\n");
					sb.append(parseTaskDesc(mm.get("auto_plan"))).append("\r\n");

//					sb.append("二、主持人人员顺序：").append("\r\n");
//					sb.append(unamestmp).append("\r\n").append("\r\n");
//					sb.append("三、会议记录人员顺序：").append("\r\n");
//					sb.append(unamestmp).append("\r\n").append("\r\n");
//					sb.append("三、下周会议主持人和记录人：").append("\r\n").append("\r\n");
//					sb.append("（若本周主持人或记录人出差,自动跳转下一主持人或记录人）");
					
					content_tmp=sb.toString();					
				}
				else
				{
					MsgBox msgBox = new MsgBox(request,
							getText("operInfoform.updatefaile",new String[]{"当前会议纪要只能由会议记录人修改！"}));
					msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
					return "msgBox";
				}
			}
			if(meeting.getAuditStatus().equals("待审核"))
			{
				boolean breaudit = false;
				String[] reaudittmp=meeting.getReAudit().trim().split("、");
				for(String mm:reaudittmp){
			    	if(userModel.getUsername().equals(mm))
			    	{
			    		breaudit = true;
			    		if("1".equals(meeting.getLockFlag())){
			    			String lockedUser = meeting.getCurrentName();
			    			MsgBox msgBox = new MsgBox(request,
			    						getText("operInfoform.updatefaile",new String[]{"当前会议纪要由 " + lockedUser + " 正在审核，请稍候"}));
			    			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			    			return "msgBox";
			    		}
                        forwardPage="edit_reAudit";	
			    		String[] record=meeting.getReauditPersonstatus().split("@@@");
			    		for(int i=0;i<record.length;i++){
			    			String[] recordField=record[i].split("\\$\\$");  //一行的所有字段
			    			if(recordField[0].equals(mm)){
			    				PersonReauditstatus=recordField[1];
			    		    }

			    		}
			    		meeting.setLockFlag("1");
			    			// 设置数据库中current_name值为当前用户名(ckai 20110318)
			    		meeting.setCurrentName(userModel.getUsername());
			    		meetingService.update(meeting);
			    	}
			    		
				}
				if(breaudit == false && !userModel.getUsername().equals("汤飞"))
	    		{
					MsgBox msgBox = new MsgBox(request,
							getText("operInfoform.updatefaile",new String[]{"当前会议纪要正由复核人审核中"}));
					msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
					return "msgBox";
	    		}	
				if(meeting.getSign().equals(userModel.getUsername()) && meeting.getReAudit().indexOf(userModel.getUsername()) == -1){
					forwardPage="audit";
				}
			}
			if(meeting.getAuditStatus().equals("待签发"))
			{
				if(meeting.getSign().equals(userModel.getUsername()))
				{
					forwardPage="audit";
				}
				else
				{
					MsgBox msgBox = new MsgBox(request,
							getText("operInfoform.updatefaile",new String[]{"当前会议纪要正由会议签发人审核中！"}));
					msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
					return "msgBox";
				}
				
			}
			if(meeting.getAuditStatus().equals("审核通过")&& meeting.getSign().equals(userModel.getUsername()))
			{
				forwardPage="audit";
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return forwardPage;
	}

	/**
	 * 记录人的更新
	 * @return
	 * @throws Exception
	 */
	public String updateByWriter() throws Exception {
		try {
			UserModel userModel = (UserModel) request.getSession().getAttribute(
					Constants.LOGIN_USER_KEY);
			meeting.setSendStatus("未发送");
			unames=meetingService.getAllNames();
			meeting.setAbsentPersons(absentPersons.replace(",","、"));
			meeting.setAttendPersons(attendPersons.replace(",","、"));
			meeting.setMain(main.replace(",","、"));
			meeting.setCopy(copy.replace(",","、"));
			meeting.setReAudit(reAudit.replace(",","、"));
			if(!meeting.getReAudit().equals(""))
			{
				String[] reaudittmp=meeting.getReAudit().trim().split("、");
				StringBuffer sb=new StringBuffer();
				for(String mm:reaudittmp){
					sb.append(mm).append("$$");
					sb.append("待审核").append("@@@");
				}
				meeting.setReauditPersonstatus(sb.toString());
			}
			StringBuffer sb1=new StringBuffer();
			if(!meeting.getReauditPersonsuggest().equals(""))
			   {
			    	sb1.append(meeting.getReauditPersonsuggest());
			   }
            if(!request.getParameter("suggest").equals(""))
            {
			    	Date now = new Date();
			    	String tmp = ""; 
			    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //显示日期，周，时间（精确到秒）
			    	String str5 = format.format(now);
			    	tmp = str5 + "  " + userModel.getUsername() + "  " + " 回复审核意见：" + request.getParameter("suggest");
			    	sb1.append(tmp).append("\r\n");
			    	meeting.setReauditPersonsuggest(sb1.toString());
            }
			boolean flag=meetingService.update(meeting);
			if (flag == true) {
				MsgBox msgBox = new MsgBox(request,
						getText("operInfoform.updateok"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				//针对于会议记录者只有其状态为'审核不通过时'才会发送相应的oa短信

			} else {
				MsgBox msgBox = new MsgBox(request,
						getText("operInfoform.updatefaile"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request,
					getText("operInfoform.updatefaile"), new String[] { e
							.toString() });
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		}
		return "msgBox";
	}
	
	/**
	 * 审核人的更新
	 * @return
	 * @throws Exception
	 */
	public String updateByAudit() throws Exception {
		try {
			UserModel userModel = (UserModel) request.getSession().getAttribute(
					Constants.LOGIN_USER_KEY);
			StringBuffer sb1=new StringBuffer();
			Date now = new Date();
			String tmp = ""; 
	    	SimpleDateFormat d5 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //显示日期，周，时间（精确到秒）
	    	String str5 = d5.format(now);
	    	meeting.setAbsentPersons(absentPersons.replace(",","、"));
			meeting.setAttendPersons(attendPersons.replace(",","、"));
			meeting.setMain(main.replace(",","、"));
			meeting.setCopy(copy.replace(",","、"));
			meeting.setReAudit(reAudit.replace(",","、"));
			if(!meeting.getReauditPersonsuggest().equals(""))
			{
			    sb1.append(meeting.getReauditPersonsuggest());
			}
			meeting.setAuditStatus(request.getParameter("PersonReauditstatus"));
			if(!request.getParameter("suggest").equals(""))
	    	{
	    		tmp = str5 + "  " + userModel.getUsername() + "  " +request.getParameter("PersonReauditstatus") + "  审核意见：" + request.getParameter("suggest");
	    	}
	    	else
	    	{
	    		tmp = str5 + "  " + userModel.getUsername() + "  " +request.getParameter("PersonReauditstatus");
	    	}
			 sb1.append(tmp).append("\r\n");
			 meeting.setReauditPersonsuggest(sb1.toString());
			if(meeting.getAuditStatus().equals("审核不通过")){
				 meeting.setAuditStatus("未经审核通过");
					if(!meeting.getReAudit().equals(""))
					{
						String[] reaudittmp=meeting.getReAudit().trim().split("、");
						StringBuffer sb2=new StringBuffer();
						for(String mm:reaudittmp){	    	
							sb2.append(mm).append("$$");
							sb2.append("待审核").append("@@@");
						}
						meeting.setReauditPersonstatus(sb2.toString());
					}
			}
			
			Meeting oldMeeting=meetingService.getMeetingById(meeting.getId());
			if(!oldMeeting.getSign().equals(meeting.getSign()))
			{
				meeting.setSendStatus(oldMeeting.getSendStatus());
				meetingService.update(meeting);
				OaOrEmail oa=new OaOrEmail();
				SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
				String date=format.format(meeting.getCurrentDateTime());
				String uid=meetingService.getEmpByName(meeting.getSign()).getUserid();
				oa.oaContent(uid, "会议纪要："+meeting.getSubject()+date+"已修改为由您签发，请进行审核并签发");
				MsgBox msgBox = new MsgBox(request,
						getText("operInfoform.updateok"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}
			
			
			meeting.setSendStatus(oldMeeting.getSendStatus());
			boolean flag=meetingService.update(meeting);
			if (flag == true) {
				MsgBox msgBox = new MsgBox(request,
						getText("operInfoform.updateok"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				OaOrEmail oa=new OaOrEmail();
				//针对于会议审核者只有其状态为'审核不通过时'才会发送相应的oa短信；通过时则发送邮件通知所有的部门人员
				if(meeting.getAuditStatus().equals("未经审核通过")){
					SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
					String date=format.format(meeting.getCurrentDateTime());
					String uid=meetingService.getEmpByName(meeting.getWriter()).getUserid();
					oa.oaContent(uid, "会议纪要："+meeting.getSubject()+date+"审核不通过，请进行修改");
				}else if(meeting.getAuditStatus().equals("审核通过")&&(send_flag!=null&&send_flag.equals("1"))){
					try{	
					Map<String,String> map=meetingService.getAllNameAndEmail();
					map.put("罗攀峰","lpfeng@grgbanking.com");//modify
				    String[] main=meeting.getMain().trim().split("、");
				    String[] copy=meeting.getCopy().trim().split("、");
				    StringBuffer mainSB=new StringBuffer();
				    StringBuffer copySB=new StringBuffer();
				    for(String mm:main){
				    	if(map.get(mm.trim())!=null)
				    	mainSB.append(map.get(mm.trim())).append(",");
				    }
					for(String cc:copy){
						if(map.get(cc.trim())!=null)
						copySB.append(map.get(cc.trim())).append(",");
					}
					oa.sendMailByMeeting(mainSB.toString(), copySB.toString(), meeting);
					meeting.setSendStatus("已发送");
					meetingService.update(meeting);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				
			} else {
				
				MsgBox msgBox = new MsgBox(request,
						getText("operInfoform.updatefaile"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request,
					getText("operInfoform.updatefaile"), new String[] { e
							.toString() });
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		}
		return "msgBox";
	}

	public String updateByreAudit() throws Exception {
		try {
			UserModel userModel = (UserModel) request.getSession().getAttribute(
			Constants.LOGIN_USER_KEY);
			StringBuffer sb=new StringBuffer();
			String Personstatustmp = meeting.getReauditPersonstatus();
		    String[] recordnew=Personstatustmp.split("@@@");
		    for(int i=0;i<recordnew.length;i++){
		    	String[] recordFieldnew=recordnew[i].split("\\$\\$");  //一行的所有字段
		    	sb.append(recordFieldnew[0]).append("$$");
		    	if(recordFieldnew[0].equals(userModel.getUsername())){
			    	sb.append(request.getParameter("PersonReauditstatus")).append("@@@");
			    }
			    else
			    {
					sb.append(recordFieldnew[1]).append("@@@");
			    }
			}
			meeting.setReauditPersonstatus(sb.toString());
			
			String[] recordold=meeting.getReauditPersonstatus().split("@@@");
			boolean flagpass = true,flagfinish = true;;
		    for(int i=0;i<recordold.length;i++){
		    	String[] recordFieldold=recordold[i].split("\\$\\$");  //一行的所有字段

		    	if(recordFieldold[1].equals("待审核")){
		    		flagfinish = false;
			    }
		    	if(recordFieldold[1].equals("审核不通过")){
		    		flagpass = false;
		    	}
			}

		    if(flagfinish == true && flagpass == true)
		    {
		    	meeting.setAuditStatus("待签发");
			    OaOrEmail oa=new OaOrEmail();
				SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
				String date=format.format(meeting.getCurrentDateTime());
			    String uid=meetingService.getEmpByName(meeting.getSign()).getUserid();
				oa.oaContent(uid, "请审核并签发会议纪要："+meeting.getSubject()+date);		    
		    }		    
		    if(flagfinish == true && flagpass == false)
		    {
		    	meeting.setAuditStatus("未经审核通过");
			    OaOrEmail oa=new OaOrEmail();
				SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
				String date=format.format(meeting.getCurrentDateTime());
			    String uid=meetingService.getEmpByName(meeting.getWriter()).getUserid();
				oa.oaContent(uid, "请修改会议纪要, 并再次提交审核："+meeting.getSubject()+date);
				if(!meeting.getReAudit().equals(""))
				{
					String[] reaudittmp=meeting.getReAudit().trim().split("、");
					StringBuffer sb2=new StringBuffer();
					for(String mm:reaudittmp){	    	
						sb2.append(mm).append("$$");
						sb2.append("待审核").append("@@@");
					}
					meeting.setReauditPersonstatus(sb2.toString());
				}
		    	
		    }
		    if(flagfinish == false)
		    {
		    	meeting.setAuditStatus("待审核");
		    }
		    StringBuffer sb1=new StringBuffer();
		    if(!meeting.getReauditPersonsuggest().equals(""))
		    {
		    	sb1.append(meeting.getReauditPersonsuggest());
		    }

		    	Date now = new Date();
		    	String tmp = ""; 
		    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //显示日期，周，时间（精确到秒）
		    	String str5 = format.format(now);
		    	if(!request.getParameter("suggest").equals(""))
		    	{
		    		tmp = str5 + "  " + userModel.getUsername() + "  " +request.getParameter("PersonReauditstatus") + "  审核意见：" + request.getParameter("suggest");
		    	}
		    	else
		    	{
		    		tmp = str5 + "  " + userModel.getUsername() + "  " +request.getParameter("PersonReauditstatus");
		    	}
		    	
		    	sb1.append(tmp).append("\r\n");
		    	meeting.setReauditPersonsuggest(sb1.toString());
		    	meeting.setSendStatus("未发送");
		    	meeting.setLockFlag("0");
		    	meeting.setCurrentName(userModel.getUsername());
			boolean flag=meetingService.update(meeting);
			if (flag == true) {
				MsgBox msgBox = new MsgBox(request,
						getText("operInfoform.updateok"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			
			} else {
				
				MsgBox msgBox = new MsgBox(request,
						getText("operInfoform.updatefaile"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request,
					getText("operInfoform.updatefaile"), new String[] { e
							.toString() });
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		}
		return "msgBox";
	}
	
	public void refresh() {
		try {
			query();
		} catch (Exception e) {
			e.printStackTrace();
		}
		;
	}

	public String query() throws Exception {
		try {
			String from = request.getParameter("from");


			
			int pageNum = 1;
			int pageSize = 20;
			if (request.getParameter("pageNum") != null
					&& request.getParameter("pageNum").length() > 0)
				pageNum = Integer.parseInt(request.getParameter("pageNum"));
			if (request.getParameter("pageSize") != null
					&& request.getParameter("pageSize").length() > 0)
				pageSize = Integer.parseInt(request.getParameter("pageSize"));
			Page page = meetingService.getPage(start, end, subject, writer, compere,content, pageNum, pageSize);
			request.setAttribute("currPage", page);
			List<Object> list = (List<Object>)page.getQueryResult();
			if (from != null && from.equals("refresh")) {
//				for (int i = 0; i < list.size(); i++) {
//					TestTool t=(TestTool)list.get(i);
//					t.setDateString(fo.format(report.getStartDate()));
//				}
				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf(page.getPageCount()));
				map.put("recordCount", String.valueOf(page.getRecordCount()));
				JSONUtil jsonUtil = new JSONUtil(
						"cn.grgbanking.feeltm.domain.testsys.Meeting");
				JSONArray jsonObj = jsonUtil.toJSON(list, map);
				JSONObject input = new JSONObject();
				input.put("pageCount", String.valueOf(page.getPageCount()));
				input.put("recordCount", String.valueOf(page.getRecordCount()));
				input.put("jsonObj", jsonObj);								
				PrintWriter out = response.getWriter();
				out.print(input);
				return "";
			} else {
				ActionContext.getContext().put("meetingList", list);
				return "query";
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "query";
	}

	public String show() throws Exception {
		try {
			String ids = request.getParameter("ids");
			meeting=meetingService.getMeetingById(ids);
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "show";
	}
	
	public String close() throws Exception{
		String id=request.getParameter("idFlag");
		meeting=meetingService.getMeetingById(id);
		meeting.setLockFlag("0");
		// 设置数据库中current_name值为空(ckai 20110318)
		//weekReport.setCurrentName("");
		// ckai 20110318
		meetingService.update(meeting);
		return "";
	}
	
	private String parseTaskDesc(String taskDesc) {
		StringBuffer sb=new StringBuffer();
		String[] record=taskDesc.split("@@@");
		
		//add
		List<WeekRecord> rs=new ArrayList<WeekRecord>();
		
		for(int i=0;i<record.length;i++){
			String[] recordField=record[i].split("\\$\\$");  //一行的所有字段
			
			String prjNameTmp=recordField[0];   //unuse
			String taskDescTmp=recordField[1];
			String prjTypeTmp=recordField[2].equals("0")?"计划":"新增";
			String responsibleTmp=recordField[3];
			String finishRateTmp=recordField[4];
			String delayReasonTmp=recordField[5];
			String fileTmp=recordField[6];
			if(fileTmp.equals("0")){
				fileTmp="无需归档";
			}else if(fileTmp.equals("1")){
				fileTmp="未归档";
			}else{
				fileTmp="已归档";
			}
			String auditTmp=recordField[7].equals("0")?"未审核":"已审核";
			//ADD
			WeekRecord r=new WeekRecord();
			r.setPrjName(prjNameTmp);
			r.setTaskDesc(taskDescTmp);
			r.setPrjType(prjTypeTmp);
			r.setResponsible(responsibleTmp);
			r.setFinishRate(finishRateTmp);
			r.setDelayReason(delayReasonTmp);
			r.setFile(fileTmp);
			r.setAudit(auditTmp);
			rs.add(r);
		}
			Collections.sort(rs);  //ADD
			
			//1.我完成了很多工作（熊磊，计划，30%，未归档，未审核）
			for(int i=0;i<rs.size();i++){
				WeekRecord wr=rs.get(i);  //add
				sb.append(i+1).append(".");  //序号
				//【】
				sb.append("【").append(wr.getPrjType()).append("】");
				sb.append("").append(wr.getPrjName()).append("").append("：");
				sb.append(wr.getTaskDesc()).append("；（");
				
				if(wr.getFinishRate().equals("0%")&&wr.getDelayReason().trim().equals("")&&wr.getFile().equals("未归档")&&wr.getAudit().equals("未审核")){
					sb.append(wr.getResponsible()).append("）");
				}else{
					sb.append(wr.getResponsible()).append("，");
					if(wr.getFinishRate().equals("100%")){
						sb.append("完成").append(wr.getFinishRate()).append("，");
						if(wr.getDelayReason()!=null&&!wr.getDelayReason().trim().equals(""))
						sb.append(wr.getDelayReason()).append("；");;
					//	sb.append("；");
					}else{
						sb.append("");
						sb.append("完成").append(wr.getFinishRate());
						sb.append("");
						sb.append("，");
						sb.append(wr.getDelayReason()).append("；");
					}
					if(wr.getFile().equals("无需归档")){
						sb.append("").append(wr.getFile()).append("");
					}else if(wr.getFile().equals("未归档")){
						sb.append("").append(wr.getFile()).append("");
					}else{
						sb.append(wr.getFile());
					}
					sb.append("）");
					sb.append("--");
					if(wr.getAudit().equals("未审核")){
						sb.append("").append(wr.getAudit()).append("");
					}else{
						sb.append(wr.getAudit());
					}
				}
				//sb.append(" ）");	
				//modify未完成原因
				//sb.append("\r\n\r\n");
				sb.append("\r\n");
				//sb.append("&#13;&#10;");
			//	sb.append("\r");
			}
		return sb.toString();
	}
	public MeetingService getMeetingService() {
		return meetingService;
	}
	public void setMeetingService(MeetingService meetingService) {
		this.meetingService = meetingService;
	}
	public Meeting getMeeting() {
		return meeting;
	}
	public void setMeeting(Meeting meeting) {
		this.meeting = meeting;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getWriter() {
		return writer;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}
	public String getCompere() {
		return compere;
	}
	public void setCompere(String compere) {
		this.compere = compere;
	}
	public List<String> getUnames() {
		return unames;
	}
	public void setUnames(List<String> unames) {
		this.unames = unames;
	}
	public String getAudit() {
		return audit;
	}
	public void setAudit(String audit) {
		this.audit = audit;
	}
	public String getAttendPersons() {
		return attendPersons;
	}
	public void setAttendPersons(String attendPersons) {
		this.attendPersons = attendPersons;
	}
	public String getAbsentPersons() {
		return absentPersons;
	}
	public void setAbsentPersons(String absentPersons) {
		this.absentPersons = absentPersons;
	}
	public String getMain() {
		return main;
	}
	public void setMain(String main) {
		this.main = main;
	}
	public String getCopy() {
		return copy;
	}
	public void setCopy(String copy) {
		this.copy = copy;
	}
	public String getPersonReauditstatus() {
		return PersonReauditstatus;
	}


	public void setPersonReauditstatus(String personReauditstatus) {
		PersonReauditstatus = personReauditstatus;
	}


	public String getReAudit() {
		return reAudit;
	}


	public void setReAudit(String reAudit) {
		this.reAudit = reAudit;
	}


	public String getSend_flag() {
		return send_flag;
	}
	public void setSend_flag(String sendFlag) {
		send_flag = sendFlag;
	}
	public WeekReportService getWeekReportService() {
		return weekReportService;
	}
	public void setWeekReportService(WeekReportService weekReportService) {
		this.weekReportService = weekReportService;
	}
	public String getContent_tmp() {
		return content_tmp;
	}
	public void setContent_tmp(String contentTmp) {
		content_tmp = contentTmp;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public Map<String, String> getUmap() {
		return umap;
	}


	public void setUmap(Map<String, String> umap) {
		this.umap = umap;
	}


	public Map<String, String> getUmaps() {
		return umaps;
	}


	public void setUmaps(Map<String, String> umaps) {
		this.umaps = umaps;
	}

}
