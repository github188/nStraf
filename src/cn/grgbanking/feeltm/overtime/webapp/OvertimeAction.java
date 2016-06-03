package cn.grgbanking.feeltm.overtime.webapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import cn.grgbanking.feeltm.approval.domain.ApprovalRecord;
import cn.grgbanking.feeltm.approval.service.ApprovalService;
import cn.grgbanking.feeltm.domain.SysUser;
import cn.grgbanking.feeltm.domain.testsys.Overtime;
import cn.grgbanking.feeltm.hols.service.UserHolsService;
import cn.grgbanking.feeltm.leave.service.LeaveService;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.overtime.service.OvertimeService;
import cn.grgbanking.feeltm.project.service.ProjectService;
import cn.grgbanking.feeltm.staff.service.StaffInfoService;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.DateUtil;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;

import com.opensymphony.xwork2.ActionContext;
public class OvertimeAction extends BaseAction{
	private static final long serialVersionUID = 1L;
	private OvertimeService overtimeService;
	private Overtime overtime;
	private List<Overtime> OvertimeList;
	@Autowired
	private StaffInfoService staffInfoService;
	@Autowired
	private ApprovalService approvalService;
	@Autowired
	private UserHolsService userHolsService;
	
	@Autowired
	private ProjectService projectService;
	private File file;
	
	@Autowired
	private LeaveService service;
	
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public String add(){
		//用户和用户所在组
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		String userId = userModel.getUserid();
		SysUser usr= staffInfoService.findUserByUserid(userId);
		request.setAttribute("groupStr", projectService.getProjectNameByUserid(userId));
		request.setAttribute("usr", usr);
		//获取部门经理信息
		List<SysUser> approvallist = staffInfoService.getdeptManagerUp();
		request.setAttribute("auditing_man", approvallist);
		return "add";
	}
	public String areason(){
		return "areason";
	}
	/**
	 * 判断当前用户是否存在所选日期的加班记录，有则返回false 0，无返回true 1
	 * @return
	 */
	public String ottodayOrNot(){
		try{
			boolean flag = false;
			overtime.setCreatedate(new Date());
			flag = overtimeService.checkOt(overtime);
			if(flag){
				response.setHeader( "Cache-Control", "no-cache" );
				response.setCharacterEncoding("UTF-8");
				response.setContentType("text/javascript;charset=UTF-8");
				JSONObject input = new JSONObject();
				input.put("saveRet", 1);	
				PrintWriter out = response.getWriter();
				out.print(input);
				out.flush();
				out.close();
			}else{
				response.setHeader( "Cache-Control", "no-cache" );
				response.setCharacterEncoding("UTF-8");
				response.setContentType("text/javascript;charset=UTF-8");
				JSONObject input = new JSONObject();
				input.put("saveRet", 0);								
				PrintWriter out = response.getWriter();
				out.print(input);
				out.flush();
				out.close();
			}
		}catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return null;
	}
	
	public String otdirect(){
		try{
			boolean flag = false;
			overtime.setCreatedate(new Date());
			flag = overtimeService.checkOt(overtime);//有加班记录返回false，无加班记录返回true
			if(flag){
				//如果当前用户没有所选日期的加班记录，则新增数据
				flag=overtimeService.add(overtime);
			}
			if (flag == true) {
				response.setHeader( "Cache-Control", "no-cache" );
				response.setCharacterEncoding("UTF-8");
				response.setContentType("text/javascript;charset=UTF-8");
				JSONObject input = new JSONObject();
				input.put("saveRet", 1);	
				PrintWriter out = response.getWriter();
				out.print(input);
				out.flush();
				out.close();
			} 
		}catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return null;
	}
	
	public String save() throws Exception {     
		try {
			boolean flag = false;
			UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
			overtime.setCreatedate(new Date());
			String userid=userModel.getUserid();
			String username=staffInfoService.getUsernameById(userid);
			String groupname=projectService.getProjectNameByUserid(userid);
			String deptname=staffInfoService.getDeptNameValueByUserId(userid);
			overtime.setUserid(userid);
			overtime.setUsername(username);
			overtime.setGroupname(groupname);
			overtime.setDetname(deptname);
			overtime.setUpdatedate(new Date());
			overtime.setUpdateman(username);
			
			overtime.setGroupname(overtime.getPrjname());
			
			String manname=staffInfoService.getUsernameById(overtime.getAuditing_man());
			overtime.setAuditing_manname(manname);
			flag = overtimeService.checkOt(overtime);
			if(flag){
				flag=overtimeService.add(overtime);
			}
			if (flag == true) {
				MsgBox msgBox = new MsgBox(request, getText("add.ok"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				this.addActionMessage(getText("add.ok"));
			} else {
				MsgBox msgBox = new MsgBox(request,getText("add.fail", new String[]{"已存在当天加班记录"}));
				addActionMessage(getText("add.fail", new String[]{"已存在当天加班记录"}));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request,getText("add.fail"), new String[] { e.toString() });
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			this.addActionMessage(getText("add.fail", new String[]{"已存在当天加班记录"}));
			boolean hasActionMessage = this.hasActionMessages();
			request.setAttribute("hasActionMessage", hasActionMessage);
			return "msgBox";
		}
		boolean hasActionMessage = this.hasActionMessages();
		request.setAttribute("hasActionMessage", hasActionMessage);
		return "msgBox";
	}
	
	public String saveot() throws Exception {     
		try {
			boolean flag = false;
			overtime.setCreatedate(new Date());
			flag = overtimeService.checkOt(overtime);
			if(flag){
				flag=overtimeService.add(overtime);
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return null;
	}
				 
	public String delete() throws Exception {
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		String loginName=userModel.getUsername();
		try {
			String ids = request.getParameter("ids");
			int iCount = 0;
			String[] sids = ids.split(",");
			Overtime otObj = overtimeService.getOvertimeById(ids);
			String curName = otObj.getUsername();
			if(!loginName.equals(curName)){
				MsgBox msgBox = new MsgBox(request,"您不能操作其他用户的数据");
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}
			if("all".equals(sids[0])){
				String[] arr = new String[sids.length-1];
				System.arraycopy(sids, 1, arr, 0, arr.length);
				sids = arr;
			}
			if (sids != null) {
				for (int i = 0; i < sids.length; i++) {
					if("新增".equals(otObj.getStatus())){
						Overtime temp = overtimeService.getOvertimeById(sids[i]);
						overtimeService.delete(temp);
						iCount++;
					}else{
						MsgBox msgBox = new MsgBox(request,"只能删除新增状态下的记录");
						msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
						return "msgBox";
					}
				}
			}
			MsgBox msgBox = new MsgBox(request, getText("rule.del.ok"), "rule", new String[] { String.valueOf(iCount) });
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request, "删除失败");
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		}
		return "msgBox";
	} 

	public String edit() throws Exception {
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		try {
			List<SysUser> approvallist = staffInfoService.getdeptManagerUp();
			String ids = request.getParameter("ids");
			overtime = overtimeService.getOvertimeById(ids);
			request.setAttribute("auditing_man", approvallist);
			if("新增".equals(overtime.getStatus()) || "审核不通过".equals(overtime.getStatus())){
				//判断用户是否为当前用户
				if(!userModel.getUserid().equals(overtime.getUserid())){
					MsgBox msgBox = new MsgBox(request,"你不能修改别人的加班记录");
					msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
					return "msgBox";
				}
				SimpleDateFormat fo=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				String record = "";
				List<ApprovalRecord> records = approvalService.getRecordByName(overtime.getId());
				int i = 0;
				for(ApprovalRecord r:records){
					i++;
					record += fo.format(r.getApprovalTime())+"  "+r.getApprovalUser()+"  "+r.getResult()+"  "+"审核意见："+r.getOpinion();
					//record += "审批记录"+i+":"+r.getRecodeName()+":"+ r.getApprovalUser()+":"+r.getOpinion()+":"+r.getResult()+":"+r.getApprovalTime();
					record += "\n";
				}
				request.setAttribute("record", record);
				return "edit";
			}else{
				MsgBox msgBox = new MsgBox(request,"只能修改状态为新增、审核不通过的记录");
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request,"只能修改状态为新增、审核不通过的记录");
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}
	 }
		
	public String update() throws Exception {
		boolean flag = false;
		try {
			UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
			String userid=userModel.getUserid();
			String username=staffInfoService.getUsernameById(userid);
			overtime.setUpdatedate(new Date());
			overtime.setUpdateman(username);
			String manname=staffInfoService.getUsernameById(overtime.getAuditing_man());
			overtime.setAuditing_manname(manname);
			
			overtime.setGroupname(overtime.getPrjname());
			
			flag = overtimeService.checkUpOt(overtime);
			if(!flag){
				MsgBox msgBox = new MsgBox(request,getText("operInfoform.updatefaile", new String[]{"已存在此人当天加班记录"}));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}
			flag=overtimeService.update(overtime);
			if (flag == true) {
				MsgBox msgBox = new MsgBox(request,getText("operInfoform.updateok"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			} else {
				MsgBox msgBox = new MsgBox(request,getText("operInfoform.updatefaile", new String[]{"已存在当天加班记录"}));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request,getText("operInfoform.updatefaile", new String[]{"已存在当天加班记录"}));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		}
		return "msgBox";
	}

	public void refresh() {
		System.out.println("refesh");
		try {
			query();
		} catch (Exception e) {
			e.printStackTrace();
		}
		;
	}
    	
	public String query() throws Exception {
		try {
			UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
			String from = request.getParameter("from");
			String startdate = request.getParameter("startdate");
			String enddate = request.getParameter("enddate");
			String groupname = request.getParameter("groupname");
			String username = request.getParameter("username");
			String deptname = request.getParameter("deptname");
//			String prjname = request.getParameter("prjname");
			
			int pageNum = 1;
			int pageSize = 20;
			if (request.getParameter("pageNum") != null && request.getParameter("pageNum").length() > 0)
				pageNum = Integer.parseInt(request.getParameter("pageNum"));
			if (request.getParameter("pageSize") != null && request.getParameter("pageSize").length() > 0)
				pageSize = Integer.parseInt(request.getParameter("pageSize"));
			Page page = overtimeService.getPage(groupname, username, deptname, pageNum, pageSize, startdate, null, enddate, userModel);
			request.setAttribute("currPage", page);
			List<Object> list = (List<Object>)page.getQueryResult();
			if (from != null && from.equals("refresh")) {
				for (int i = 0; i < list.size(); i++) {
					Overtime overtime=(Overtime)list.get(i);
				}
				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf(page.getPageCount()));
				map.put("recordCount", String.valueOf(page.getRecordCount()));
				JSONUtil jsonUtil = new JSONUtil("cn.grgbanking.feeltm.domain.testsys.Overtime");
				JSONArray jsonObj = jsonUtil.toJSON(list, map);
				JSONObject input = new JSONObject();
				input.put("pageCount", String.valueOf(page.getPageCount()));
				input.put("recordCount", String.valueOf(page.getRecordCount()));
				input.put("jsonObj", jsonObj);								
				PrintWriter out = response.getWriter();
				out.print(input);
				return "";
			} else {
				ActionContext.getContext().put("overtimeList", list);
				return "query";
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "query";
	}
	
	public String overtimeList() throws Exception {
		try {
			String from = request.getParameter("from");
			String userid = request.getParameter("userid");
			
			int pageNum = 1;
			int pageSize = 20;
			if (request.getParameter("pageNum") != null && request.getParameter("pageNum").length() > 0)
				pageNum = Integer.parseInt(request.getParameter("pageNum"));
			if (request.getParameter("pageSize") != null && request.getParameter("pageSize").length() > 0)
				pageSize = Integer.parseInt(request.getParameter("pageSize"));
			Page page = overtimeService.getOveritmePage(userid, pageNum, pageSize);
			request.setAttribute("currPage", page);
			List<Object> list = (List<Object>)page.getQueryResult();
			ActionContext.getContext().put("overtimeList", list);
			
			page = service.leaveList(userid, pageNum, pageSize);
			request.setAttribute("currPage1", page);
			List<Object> leaveList = page.getQueryResult();
			ActionContext.getContext().put("leaveList", leaveList);
			
			return "overtimeList";
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "overtimeList";
	}
	
	public String queryNames() throws Exception{
		response.setHeader( "Cache-Control", "no-cache" );
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/javascript;charset=UTF-8");
		String groupName=request.getParameter("groupName");
		PrintWriter out=response.getWriter();
		List<Object> list=overtimeService.getNames(groupName);
		list.set(list.size()-1, "");
	//	JSONUtil jsonUtil=new JSONUtil("java.lang.String");
		//JSONArray jsonArray=jsonUtil.toJSON(list, null);
		JSONArray jsonArray=new JSONArray(list);
		out.print(jsonArray);
		out.flush();
		out.close();
		return null;
	}
	
	
	public String show() throws Exception {
		try {
			String ids = request.getParameter("ids");
			overtime=overtimeService.getOvertimeById(ids);
			List<SysUser> approvallist = staffInfoService.getdeptManagerUp();
			request.setAttribute("auditing_man", approvallist);
			String record = "";
			List<ApprovalRecord> records = approvalService.getRecordByName(overtime.getId());
			SimpleDateFormat fo=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			int i = 0;
			for(ApprovalRecord r:records){
				i++;
				record += fo.format(r.getApprovalTime())+"  "+r.getApprovalUser()+"  "+r.getResult()+"  "+"审核意见："+r.getOpinion();
				//record += "审批记录"+i+":"+r.getRecodeName()+":"+ r.getApprovalUser()+":"+r.getOpinion()+":"+r.getResult()+":"+r.getApprovalTime();
				record += "\n";
			}
			request.setAttribute("record", record);
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "show";
	}
	
	public String userAndGroup(){
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		try{
			response.setHeader( "Cache-Control", "no-cache" );
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/javascript;charset=UTF-8");
			JSONObject input = new JSONObject();
			input.put("userName", userModel.getUsername());	
			input.put("groupName", userModel.getGroupName());	
			PrintWriter out = response.getWriter();
			out.print(input);
			out.flush();
			out.close();
		}catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return null;
	}
	
	/**
	 * 提交审核
	 * @return
	 */
	public String upauditing(){
		//新增、审核不通过的可以提交审核，如果不是，则不能提交。可以提交的是否有审核人
		try{
			String ids = request.getParameter("ids");
			overtime=overtimeService.getOvertimeById(ids);
			if(!"".equals(overtime.getAuditing_man()) && ("新增".equals(overtime.getStatus()) || "审核不通过".equals(overtime.getStatus()))){
				UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
				if(!userModel.getUserid().equals(overtime.getUserid())){
					MsgBox msgBox = new MsgBox(request,"你不能提交别人的加班记录");
					msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
					return "msgBox";
				}
				overtime.setStatus("待审核");
				String userid=userModel.getUserid();
				String username=staffInfoService.getUsernameById(userid);
				overtime.setUpdatedate(new Date());
				overtime.setUpdateman(username);
				String manname=staffInfoService.getUsernameById(overtime.getAuditing_man());
				overtime.setAuditing_manname(manname);
				overtimeService.update(overtime);
				MsgBox msgBox = new MsgBox(request,"提交成功");
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}else{
				MsgBox msgBox = new MsgBox(request,"只能提交状态为新增、审核不通过的记录");
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}
		}catch(Exception e){
			e.printStackTrace();
			MsgBox msgBox = new MsgBox(request,"只能提交状态为新增、审核不通过的记录");
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}
	}
	
	public String auditing() throws Exception {
		try {
			UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
			String userid=userModel.getUserid();
			List<SysUser> approvallist = staffInfoService.getdeptManagerUp();
			String ids = request.getParameter("ids");
			overtime = overtimeService.getOvertimeById(ids);
			if(!"待审核".equals(overtime.getStatus())){
				MsgBox msgBox = new MsgBox(request,"只能审核状态为待审核的记录");
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}else if(!userid.equals(overtime.getAuditing_man())){
				MsgBox msgBox = new MsgBox(request,"你不是该记录的审核人");
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}else{
				request.setAttribute("auditing_man", approvallist);
				SimpleDateFormat fo=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				String record = "";
				List<ApprovalRecord> records = approvalService.getRecordByName(overtime.getId());
				int i = 0;
				for(ApprovalRecord r:records){
					i++;
					record += fo.format(r.getApprovalTime())+"  "+r.getApprovalUser()+"  "+r.getResult()+"  "+"审核意见："+r.getOpinion();
					//record += "审批记录"+i+":"+r.getRecodeName()+":"+ r.getApprovalUser()+":"+r.getOpinion()+":"+r.getResult()+":"+r.getApprovalTime();
					record += "\n";
				}
				request.setAttribute("record", record);
				return "audit";
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request,"只能修改状态为新增、审核不通过的记录");
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}
	 }
	public String audit(){
		try{
			if("0".equals(overtime.getResult())){
				overtime.setStatus("审核通过");
				if("".equals(overtime.getContent())){
					overtime.setContent("同意");
				}
			}else{
				overtime.setStatus("审核不通过");
			}
			UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
			String userid=userModel.getUserid();
			String username=staffInfoService.getUsernameById(userid);
			overtime.setUpdatedate(new Date());
			overtime.setUpdateman(username);
			overtime.setAuditing_man(userid);
			overtime.setAuditing_manname(username);
			
			overtime.setGroupname(overtime.getPrjname());//项目组和项目名称为一致的
			
			String manname=staffInfoService.getUsernameById(overtime.getAuditing_man());
			overtime.setAuditing_manname(manname);
			overtimeService.update(overtime);
			approvalService.makeRecored(overtime.getId(), "", userModel.getUsername(), overtime.getContent(), overtime.getStatus());
			if("0".equals(overtime.getResult())){
				double sumtime = overtime.getSumtime();
				BigDecimal b = new BigDecimal(sumtime);
				sumtime = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				userHolsService.updateFreeTime(overtime.getUserid(), sumtime);
			}
			MsgBox msgBox = new MsgBox(request,"审核成功");
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}catch(Exception e){
			e.printStackTrace();
			MsgBox msgBox = new MsgBox(request,"审核失败");
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}
	}
	
	public String importData() throws Exception{
		if (file == null) {
			throw new Exception("文件为空！");
		}
		POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(file));
		HSSFWorkbook wb =  new HSSFWorkbook(fs);
		int sheetNumber = wb.getNumberOfSheets();
		List<Overtime> overtimeList = new ArrayList<Overtime>();
		for(int shNum=0;shNum<sheetNumber;shNum++){
			HSSFSheet sheet = wb.getSheetAt(shNum);
			long rowNumber = sheet.getPhysicalNumberOfRows();
			for(int rowNum=1;rowNum<=rowNumber;rowNum++){
				HSSFRow row = sheet.getRow(rowNum);
				int cellNumber = row.getPhysicalNumberOfCells();//获取当前行的总列数
				for(int cellNum=5;cellNum<cellNumber;cellNum=cellNum+9){
					//可能会有多个用户申请一个加班，则每个用户生成一条数据
					String name = row.getCell((short)(cellNum)).getStringCellValue();//加班人员名字
					name=name.replace("'", "");
					if("".equals(name.replaceAll("," , ""))){
						break;
					}
					String userid = row.getCell((short)(cellNum+1)).getStringCellValue().replace("'", "");//加班人员ID
					String overdate = row.getCell((short)(cellNum+3)).getStringCellValue().replace("'", "");//加班日期
					String start = row.getCell((short)(cellNum+4)).getStringCellValue().replace("'", "");//开始时间
					String end = row.getCell((short)(cellNum+5)).getStringCellValue().replace("'", "");//结束时间
					double datelong = Double.parseDouble(row.getCell((short)(cellNum+7)).getStringCellValue().replace("'", ""));//加班时长
					String reason = row.getCell((short)(cellNum+8)).getStringCellValue().replace("'", "");//加班原因
					Date date = new Date();
					int currYear = date.getYear()+1900;
					int currMonth = date.getMonth()+1;
					int overMonth = Integer.parseInt(overdate.substring(0, 1).toString());
					if(currMonth<overMonth){
						currYear=currYear+1;
					}
					overdate = currYear+"-"+overdate.substring(0, 1)+"-"+overdate.substring(2, 3);
					start = overdate + " " + start + ":00";
					end = overdate + " " + end + ":00";
					Date startdate = DateUtil.stringToDate(start, "yyyy-MM-dd HH:mm:ss");
					Date enddate = DateUtil.stringToDate(end, "yyyy-MM-dd HH:mm:ss");
					if(!"".equals(userid)){
						String[] userids = userid.split(",");
						for(int i=0;i<userids.length;i++){
							if("".equals(userids[i])){
								continue;
							}else{
								Overtime overtime = new Overtime();
								String user_id=userids[i];
								//判断该用户是否存在
								boolean flag = staffInfoService.isExitStaffByUserid(user_id);
								if(!flag)
									continue;
								String username=staffInfoService.getUsernameById(user_id);
								String groupname=projectService.getProjectNameByUserid(user_id);
								String deptname=staffInfoService.getDeptNameValueByUserId(user_id);
								overtime.setUserid(userids[i]);
								overtime.setUsername(username);
								overtime.setDetname(deptname);
								overtime.setGroupname(groupname);
								overtime.setCreatedate(new Date());
								overtime.setStartdate(startdate);
								overtime.setEnddate(enddate);
								overtime.setReason(reason);
								overtime.setStatus("审核通过");
								BigDecimal b = new BigDecimal(datelong);
								datelong = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
								overtime.setSumtime(datelong);
								//判断该时间段是否有加班记录，如果有，则不再新增，如果没有则新增
								flag = overtimeService.checkOt(overtime);
								if(flag)
									overtimeList.add(overtime);
							}
						}
					}else{
						continue;
					}
				}
			}
		}
		overtimeService.addOvertimeListInfo(overtimeList);
		for(Overtime overtime:overtimeList)
			userHolsService.updateFreeTime(overtime.getUserid(), overtime.getSumtime());
		SysLog.operLog(request, Constants.OPER_ADD_VALUE, overtimeList.size()+"条记录");
		MsgBox msgBox = new MsgBox(request,"成功导入:"+overtimeList.size()+"条记录");
		msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		return "msgBox";
	}
	/**
	 * 计算页面上的调休时间
	 * @return
	 */
	public String getSumtime(){
		String start = request.getParameter("startdate");
		String end = request.getParameter("enddate");
		start = start.substring(0, 17)+"00";
		end = end.substring(0, 17)+"00";
		String rest_start = start.split(" ")[0]+" "+"12:00:00";
		String rest_end = start.split(" ")[0]+" "+"13:00:00";
		//加班时间段
		Date workstart_date = DateUtil.stringToDate(start, "yyyy-MM-dd HH:mm:ss");
		Date workend_date = DateUtil.stringToDate(end, "yyyy-MM-dd HH:mm:ss");
		//公司中午休息时间
		Date reststart_date = DateUtil.stringToDate(rest_start, "yyyy-MM-dd HH:mm:ss");
		Date restend_date = DateUtil.stringToDate(rest_end, "yyyy-MM-dd HH:mm:ss");
		double sumtime = 0;
		if(restend_date.before(workstart_date)){
			sumtime += DateUtil.getHourMinute(workstart_date, workend_date);
		}else if(workstart_date.before(reststart_date) && restend_date.before(workend_date)){
			sumtime += DateUtil.getHourMinute(workstart_date, reststart_date);
			sumtime += DateUtil.getHourMinute(restend_date, workend_date);
		}else if(workstart_date.before(reststart_date) && workend_date.before(reststart_date)){
			sumtime += DateUtil.getHourMinute(workstart_date, workend_date);
		}else if(workstart_date.before(restend_date) && workstart_date.after(reststart_date)){
			sumtime += DateUtil.getHourMinute(restend_date, workend_date);
		}else{
			sumtime += DateUtil.getHourMinute(workstart_date, reststart_date);
		}
		BigDecimal b = new BigDecimal(sumtime);
		sumtime = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();  

		ajaxPrint("{\"sumtime\":\"" + sumtime + "\"}");
		return null;
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
	public static void main(String[] args) {
		OvertimeAction action = new OvertimeAction();
		action.getSumtime();
	}
    public OvertimeService getOvertimeService() {
		return overtimeService;
	}
	public void setOvertimeService(OvertimeService overtimeService) {
		this.overtimeService = overtimeService;
	}
	public Overtime getOvertime() {
		return overtime;
	}
	public void setOvertime(Overtime overtime) {
		this.overtime = overtime;
	}
	public List<Overtime> getOvertimeList() {
		return OvertimeList;
	}
	public void setOvertimeList(List<Overtime> overtimeList) {
		OvertimeList = overtimeList;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public StaffInfoService getStaffInfoService() {
		return staffInfoService;
	}
	public void setStaffInfoService(StaffInfoService staffInfoService) {
		this.staffInfoService = staffInfoService;
	}
	public ApprovalService getApprovalService() {
		return approvalService;
	}
	public void setApprovalService(ApprovalService approvalService) {
		this.approvalService = approvalService;
	}
	public UserHolsService getUserHolsService() {
		return userHolsService;
	}
	public void setUserHolsService(UserHolsService userHolsService) {
		this.userHolsService = userHolsService;
	}
	
}
