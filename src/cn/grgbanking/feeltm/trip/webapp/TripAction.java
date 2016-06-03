package cn.grgbanking.feeltm.trip.webapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import cn.grgbanking.feeltm.config.JdbcConnect;
import cn.grgbanking.feeltm.domain.SysUser;
import cn.grgbanking.feeltm.domain.testsys.BusinessTrip;
import cn.grgbanking.feeltm.hols.service.UserHolsService;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.project.service.ProjectService;
import cn.grgbanking.feeltm.staff.service.StaffInfoService;
import cn.grgbanking.feeltm.trip.service.TripService;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.DateUtil;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;

import com.opensymphony.xwork2.ActionContext;


public class TripAction extends BaseAction{
	private BusinessTrip trip;
	private TripService tripService;
	private StaffInfoService staffInfoService;
	private ApprovalService approvalService;
	private UserHolsService userHolsService;
	@Autowired
	private ProjectService projectService;
	private File file;
	
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public String add() {
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		//tripMan=userModel.getUsername();
		List<SysUser> approvallist = staffInfoService.getdeptManagerUp();
		request.setAttribute("auditing_man", approvallist);
		return "add";
	}
	public void refresh() {
		try {
			query();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public String save() throws Exception {
		try {
			boolean flag = false;
			UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
			String userid=userModel.getUserid();
			String username=staffInfoService.getUsernameById(userid);
			String groupname=projectService.getProjectNameByUserid(userid);
			String deptname=staffInfoService.getDeptNameValueByUserId(userid);
			trip.setUserid(userid);
			trip.setUsername(username);
			trip.setDetname(deptname);
			trip.setGroupname(groupname);
			trip.setUpdateman(username);
			trip.setUpdatedate(new Date());
			trip.setCreatedate(new Date());
			String manname=staffInfoService.getUsernameById(trip.getAuditing_man());
			trip.setAuditing_manname(manname);
			flag = tripService.checkOt(trip);
			if(flag){
				flag = tripService.add(trip);
			}
			if (flag == true) {
				MsgBox msgBox = new MsgBox(request, getText("add.ok"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				this.addActionMessage(getText("add.ok"));
			} else {
				MsgBox msgBox = new MsgBox(request,getText("add.fail", new String[]{"你已经存在该时间段的出差记录"}));
				addActionMessage(getText("add.fail", new String[]{"你已经存在该时间段的出差记录"}));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request,getText("operInfoform.addfaile"), new String[] { e.toString() });
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

	public String edit() throws Exception {
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		try {
			List<SysUser> approvallist = staffInfoService.getdeptManagerUp();
			String ids = request.getParameter("ids");
			trip =tripService.getDetailById(ids);
			request.setAttribute("auditing_man", approvallist);
			SimpleDateFormat fo=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String record = "";
			List<ApprovalRecord> records = approvalService.getRecordByName(trip.getId());
			for(ApprovalRecord r:records){
				record += fo.format(r.getApprovalTime())+"  "+r.getApprovalUser()+"  "+r.getResult()+"  "+"审核意见："+r.getOpinion();
				record += "\n";
			}
			request.setAttribute("record", record);
			if("新增".equals(trip.getStatus()) || "审核不通过".equals(trip.getStatus())){
				//判断用户是否为当前用户
				if(!userModel.getUserid().equals(trip.getUserid())){
					MsgBox msgBox = new MsgBox(request,"你不能修改别人的记录");
					msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
					return "msgBox";
				}
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
			trip.setUpdateman(userModel.getUsername());
			trip.setUpdatedate(new Date());
			String manname=staffInfoService.getUsernameById(trip.getAuditing_man());
			trip.setAuditing_manname(manname);
			flag = tripService.checkUpOt(trip);
			if(!flag){
				MsgBox msgBox = new MsgBox(request,getText("operInfoform.updatefaile", new String[]{"你已经存在该时间段的出差记录"}));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}
			flag = tripService.update(trip);
			if (flag == true) {
				MsgBox msgBox = new MsgBox(request,getText("operInfoform.updateok"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			} else {
				MsgBox msgBox = new MsgBox(request,getText("operInfoform.updatefaile"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request,getText("operInfoform.updatefaile"), new String[] { e.toString() });
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		}
		return "msgBox";
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
			String prjname = request.getParameter("prjname");
			
			int pageNum = 1;
			int pageSize = 20;
			if (request.getParameter("pageNum") != null && request.getParameter("pageNum").length() > 0)
				pageNum = Integer.parseInt(request.getParameter("pageNum"));
			if (request.getParameter("pageSize") != null && request.getParameter("pageSize").length() > 0)
				pageSize = Integer.parseInt(request.getParameter("pageSize"));
			Page page = tripService.getPage(startdate,enddate,prjname, username,deptname,groupname, pageNum, pageSize,userModel);
			request.setAttribute("currPage", page);
			List<Object> list = (List<Object>)page.getQueryResult();
			if (from != null && from.equals("refresh")) {
				for (int i = 0; i < list.size(); i++) {
					BusinessTrip trip=(BusinessTrip)list.get(i);
				}
				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf(page.getPageCount()));
				map.put("recordCount", String.valueOf(page.getRecordCount()));
				JSONUtil jsonUtil = new JSONUtil("cn.grgbanking.feeltm.domain.testsys.BusinessTrip");
				JSONArray jsonObj = jsonUtil.toJSON(list, map);
				JSONObject input = new JSONObject();
				input.put("pageCount", String.valueOf(page.getPageCount()));
				input.put("recordCount", String.valueOf(page.getRecordCount()));
				input.put("jsonObj", jsonObj);								
				PrintWriter out = response.getWriter();
				out.print(input);
				return "";
			}else {
				ActionContext.getContext().put("overtimeList", list);
				return "query";
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "query";
	}
	
	public String delete() throws Exception {
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		String loginName=userModel.getUsername();
		try {
			String ids = request.getParameter("ids");
			System.out.println("ids: "+ids);
			int iCount = 0;
			String[] sids = ids.split(",");
			BusinessTrip otObj = tripService.getDetailById(ids);
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
						BusinessTrip temp = tripService.getDetailById(sids[i]);
						tripService.delete(temp);
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
			MsgBox msgBox = new MsgBox(request, getText("operator.addfaile"));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		}
		return "msgBox";
	} 
	
	public String show() throws Exception {
		try {
			String ids = request.getParameter("ids");
			trip=tripService.getDetailById(ids);
			List<SysUser> approvallist = staffInfoService.getdeptManagerUp();
			request.setAttribute("auditing_man", approvallist);
			String record = "";
			List<ApprovalRecord> records = approvalService.getRecordByName(trip.getId());
			SimpleDateFormat fo=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			for(ApprovalRecord r:records){
				record += fo.format(r.getApprovalTime())+"  "+r.getApprovalUser()+"  "+r.getResult()+"  "+"审核意见："+r.getOpinion();
				record += "\n";
			}
			request.setAttribute("record", record);
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "show";
	}
	public String upauditing(){
		//新增、审核不通过的可以提交审核，如果不是，则不能提交。可以提交的是否有审核人
		try{
			String ids = request.getParameter("ids");
			trip=tripService.getDetailById(ids);
			if(!"".equals(trip.getAuditing_man()) && ("新增".equals(trip.getStatus()) || "审核不通过".equals(trip.getStatus()))){
				UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
				if(!userModel.getUserid().equals(trip.getUserid())){
					MsgBox msgBox = new MsgBox(request,"你不能提交别人的记录");
					msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
					return "msgBox";
				}
				trip.setStatus("待审核");
				String userid=userModel.getUserid();
				String username=staffInfoService.getUsernameById(userid);
				trip.setUpdatedate(new Date());
				trip.setUpdateman(username);
				String manname=staffInfoService.getUsernameById(trip.getAuditing_man());
				trip.setAuditing_manname(manname);
				tripService.update(trip);
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
			trip=tripService.getDetailById(ids);
			if(!"待审核".equals(trip.getStatus())){
				MsgBox msgBox = new MsgBox(request,"只能审核状态为待审核的记录");
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}else if(!userid.equals(trip.getAuditing_man())){
				MsgBox msgBox = new MsgBox(request,"你不是该记录的审核人");
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}else{
				request.setAttribute("auditing_man", approvallist);
				SimpleDateFormat fo=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				String record = "";
				List<ApprovalRecord> records = approvalService.getRecordByName(trip.getId());
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
			if("0".equals(trip.getResult())){
				trip.setStatus("审核通过");
				if("".equals(trip.getContent())){
					trip.setContent("同意");
				}
			}else{
				trip.setStatus("审核不通过");
			}
			UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
			String userid=userModel.getUserid();
			String username=staffInfoService.getUsernameById(userid);
			trip.setUpdatedate(new Date());
			trip.setUpdateman(username);
			trip.setAuditing_man(userid);
			trip.setAuditing_manname(username);
			String manname=staffInfoService.getUsernameById(trip.getAuditing_man());
			trip.setAuditing_manname(manname);
			tripService.update(trip);
			approvalService.makeRecored(trip.getId(), "", userModel.getUsername(), trip.getContent(), trip.getStatus());
			if("0".equals(trip.getResult())){
				double sumtime = getSumtime(trip);
				BigDecimal b = new BigDecimal(sumtime);
				sumtime = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				userHolsService.updateFreeTime(trip.getUserid(), sumtime);
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
	
	public String importData(){
		if (file == null) {
			try {
				throw new Exception("文件为空！");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try{
			POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(file));
			HSSFWorkbook wb =  new HSSFWorkbook(fs);
			int sheetNumber = wb.getNumberOfSheets();
			List<BusinessTrip> tripList = new ArrayList<BusinessTrip>();
			for(int shNum=0;shNum<sheetNumber;shNum++){
				HSSFSheet sheet = wb.getSheetAt(shNum);
				long rowNumber = sheet.getPhysicalNumberOfRows();
				for(int rowNum=1;rowNum<=rowNumber;rowNum++){
					HSSFRow row = sheet.getRow(rowNum);
					String tripcity = row.getCell((short)6).getStringCellValue().replace("'", "");//出差地
					String taskdesc = row.getCell((short)10).getStringCellValue().replace("'", "");//任务描述
					String start = row.getCell((short)40).getStringCellValue().replace("'", "");//出差开始日期
					String end = row.getCell((short)41).getStringCellValue().replace("'", "");//出差结束日期
					Date startdate = DateUtil.stringToDate(start, "yyyy-MM-dd");
					Date enddate = DateUtil.stringToDate(end, "yyyy-MM-dd");
					//可能会有多个用户申请一个出差记录，则每个用户生成一条数据
					String name = row.getCell((short)9).getStringCellValue().replace("'", "");//出差人员,多个逗号分隔
					if("".equals(name.replaceAll("," , ""))){
						break;
					}
					//根据用户名的出用户id,如果用户名重复，则用用户ID区分
					String[] names = name.split(",");
					for(int i=0;i<names.length;i++){
						if("".equals(names[i])){
							continue;
						}else{
							BusinessTrip trip = new BusinessTrip();
							String username=names[i].split("\\(")[0];
							username = username.split("\\（")[0];
							//判断该用户是否存在
							String userid = staffInfoService.getUserIdByUsername(username.trim());
							if("".equals(userid))
								continue;
							String user_name=staffInfoService.getUsernameById(userid);
							String groupname=projectService.getProjectNameByUserid(userid);
							String deptname=staffInfoService.getDeptNameValueByUserId(userid);
							trip.setUserid(userid);
							trip.setUsername(user_name);
							trip.setDetname(deptname);
							trip.setGroupname(groupname);
							trip.setCreatedate(new Date());
							trip.setStartdate(startdate);
							trip.setEnddate(enddate);
							trip.setTripcity(tripcity);
							trip.setTaskdesc(taskdesc);
							trip.setStatus("审核通过");
							//根据需求得出出差调休时间
							double sumtime = getSumtime(trip);
							BigDecimal b = new BigDecimal(sumtime);
							sumtime = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
							trip.setSumtime(sumtime);
							//判断该时间段是否有出差记录，如果有，则不再新增，如果没有则新增
							boolean flag = tripService.checkOt(trip);
							if(flag)
								tripList.add(trip);
						}
					}
				}
			}
			tripService.addTripListInfo(tripList);
			for(BusinessTrip trip:tripList){
				double sumtime = getSumtime(trip);
				BigDecimal b = new BigDecimal(sumtime);
				sumtime = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				userHolsService.updateFreeTime(trip.getUserid(), sumtime);
			}
			SysLog.operLog(request, Constants.OPER_ADD_VALUE, tripList.size()+"条记录");
			MsgBox msgBox = new MsgBox(request,"成功导入:"+tripList.size()+"条记录");
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}catch(Exception e){
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * 
	 * @param startdate
	 * @param enddate
	 * @param sql
	 * @return
	 */
	public long connectionOracle(Date startdate,Date enddate,String sql)throws Exception{
		Connection conn = JdbcConnect.getConnection();
		PreparedStatement pst = null;
		ResultSet st = null;
		try {
			pst = conn.prepareStatement(sql);
			java.sql.Date start = new java.sql.Date(startdate.getTime());
			pst.setDate(1, start);
			java.sql.Date end = new java.sql.Date(enddate.getTime());
			pst.setDate(2, end);
			st = pst.executeQuery();
			while(st.next()){
				return st.getLong("num");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				pst.close();
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return 0;
	}
	
	/**
	 * 根据时间段得出出差调休时间
	 * @param trip
	 * @return
	 */
	public double getSumtime(BusinessTrip trip)throws Exception{
		//先得出出差时间段共有多少个周末，在判断有多少个节假日，其中有几个是调休加班的
		String startdate = DateUtil.getDateString(trip.getStartdate());
		String enddate = DateUtil.getDateString(trip.getEnddate());
		long workday = DateUtil.getworkDay(startdate, enddate);//出差时间段的工作日共有多少天
		//得出出差时间段共有多少个节假日
		String sql = "select count(*)num from oa_holiday where holiday_date>=? and holiday_date<=? and isworkday='1'";
		long restday = connectionOracle(trip.getStartdate(),trip.getEnddate(),sql);
		//得出出差时间段共有多少个调休加班日
		sql = "select count(*)num from oa_holiday where work_date>=? and work_date<=?";
		long overday = connectionOracle(trip.getStartdate(),trip.getEnddate(),sql);
		//工作日-节假日+调休加班=出差天数
		double tripday = workday-restday+overday;
		System.out.println("tripday---------------------------"+tripday);
		double sumtime = 0;
		int count = (int)tripday/30;
		for(int i=0;i<=count;i++){
			if(tripday>=7 && tripday<14)
				sumtime += 1;
			if(tripday>=14 && tripday <28)
				sumtime += 2;
			if(tripday>=28)
				sumtime += 3;
			tripday -= 30;
		}
		//sumtime = sumtime*8;
		System.out.println("sumtimeget--------------------------"+sumtime);
		BigDecimal b = new BigDecimal(sumtime*8);
		sumtime = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		System.out.println("sumtimegetbbbb----------------------------"+sumtime);
		return sumtime;
	}
	/**
	 * 页面上获取休假天数
	 * @return
	 */
	public String getPageSumtime()throws Exception{
		String startdate = request.getParameter("startdate");
		String enddate = request.getParameter("enddate");
		BusinessTrip trip = new BusinessTrip();
		trip.setStartdate(DateUtil.stringToDate(startdate, "yyyy-MM-dd"));
		trip.setEnddate(DateUtil.stringToDate(enddate, "yyyy-MM-dd"));
		double sumtime = getSumtime(trip)/1.00;
		System.out.println("sumtime-------------------------------"+sumtime);
		BigDecimal b = new BigDecimal(sumtime);
		sumtime = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		System.out.println("sumtimebbbb----------------------------"+sumtime);
		ajaxPrint("{\"sumtime\":\"" + sumtime + "\"}");
		return null;
	}
	
	public void ajaxPrint(String str){
		try{
			HttpServletResponse response = ServletActionContext.getResponse(); 
	        response.setContentType("application/json");  
	        response.setCharacterEncoding("UTF-8");  
	        PrintWriter writer = response.getWriter();
	        writer.print(str);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public BusinessTrip getTrip() {
		return trip;
	}
	public void setTrip(BusinessTrip trip) {
		this.trip = trip;
	}
	public TripService getTripService() {
		return tripService;
	}
	public void setTripService(TripService tripService) {
		this.tripService = tripService;
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
