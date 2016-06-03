package cn.grgbanking.feeltm.signrecord.webapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import cn.grgbanking.feeltm.approval.domain.ApprovalRecord;
import cn.grgbanking.feeltm.approval.service.ApprovalService;
import cn.grgbanking.feeltm.attendance.service.AttendanceAnalysisService;
import cn.grgbanking.feeltm.cardRecord.domain.CardRecord;
import cn.grgbanking.feeltm.cardRecord.service.CardRecordService;
import cn.grgbanking.feeltm.common.util.NStrafFileUtils;
import cn.grgbanking.feeltm.config.BusnDataDir;
import cn.grgbanking.feeltm.config.Configure;
import cn.grgbanking.feeltm.config.UserRoleConfig;
import cn.grgbanking.feeltm.context.BaseApplicationContext;
import cn.grgbanking.feeltm.domain.SignRecord;
import cn.grgbanking.feeltm.domain.SysUser;
import cn.grgbanking.feeltm.holiday.service.HolidayService;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.project.domain.Project;
import cn.grgbanking.feeltm.project.service.ProjectService;
import cn.grgbanking.feeltm.signrecord.domain.NoAttendanceData;
import cn.grgbanking.feeltm.signrecord.domain.SignBind;
import cn.grgbanking.feeltm.signrecord.service.NoAttendanceDataService;
import cn.grgbanking.feeltm.signrecord.service.SignRecordService;
import cn.grgbanking.feeltm.staff.dao.StaffInfoDao;
import cn.grgbanking.feeltm.staff.service.StaffInfoService;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.DateUtil;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.feeltm.util.StringUtil;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;

@SuppressWarnings( { "serial", "unchecked" })
public class SignRecordAction extends BaseAction implements ModelDriven<SignRecord> {

	@Autowired
	private SignRecordService signRecordService;
	
	@Autowired
	private StaffInfoService staffInfoService;
	
	@Autowired
	private AttendanceAnalysisService attendanceAnalysisService;
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private ApprovalService approvalService;
	
	@Autowired
	private NoAttendanceDataService noAttendanceDataService;
	
	@Autowired
	private HolidayService holidayService;
	
	private List<SignRecord> list = new ArrayList<SignRecord>();
	private SignRecord signRecord = new SignRecord();
	public String pageSize;
	public String pageNum;
	
	public String form;
	
	private File file;
	
	private FileInputStream inputStream;
	
	private CardRecordService cardRecordService;

	public InputStream getInputStream() throws Exception {
		return inputStream;
	}
	
	public void bindRefresh(){
		try {
			recordBind();
		} catch (Exception e) {
			SysLog.error("error in (NotifyAction.java-refresh())");
			e.printStackTrace();
			SysLog.error(e);
		}
	}
	
	/**
	 * 绑定列表
	 * @return
	 * lhyan3
	 * 2014年6月9日
	 */
	public String recordBind(){
		try{
			String deptname = request.getParameter("deptname");
			String grpname = request.getParameter("grpname");
			String userid = request.getParameter("userid");
			String status = request.getParameter("status");
			int pagenum = 1;
			int pagesize = 20;
			if (request.getParameter("pageNum") != null && request.getParameter("pageNum").length() > 0)
				pagenum = Integer.parseInt(request.getParameter("pageNum"));
			if (request.getParameter("pageSize") != null && request.getParameter("pageSize").length() > 0)
				pagesize = Integer.parseInt(request.getParameter("pageSize"));
			Page page = signRecordService.findAllBind(deptname,grpname,userid,status,pagenum,pagesize);
			request.setAttribute("currPage", page);
			List list = page.getQueryResult();
			if(list!=null && list.size()>0){
				for(Object o:list){
					SignBind bind = (SignBind) o;
					bind.setDeptname(staffInfoService.getDeptNameValueByUserId(bind.getUserid()));
					bind.setGroupname(projectService.getProjectNameByUserid(bind.getUserid()));
				}
			}
			if (form != null && form.equals("refresh")) {
				List<Object> l = new ArrayList<Object>();
				for (int i = 0; i < list.size(); i++) {
					l.add((Object) list.get(i));
				}
				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf(page.getPageCount()));
				map.put("recordCount", String.valueOf(page.getRecordCount()));
				JSONUtil jsonUtil = new JSONUtil("cn.grgbanking.feeltm.signrecord.domain.SignBind");
				JSONArray jsonArray = jsonUtil.toJSON(list, map);
				JSONObject input = new JSONObject();
				input.put("pageCount", String.valueOf(page.getPageCount()));
				input.put("recordCount", String.valueOf(page.getRecordCount()));
				input.put("jsonObj", jsonArray);								
				PrintWriter out = response.getWriter();
				out.print(input);
				return "";
			}else {
				ActionContext.getContext().put("bind", list);
				return "bindlist";
			}
		}catch(Exception e){
			SysLog.error(request, "error in (SignRecordAction.java-recordBind())");
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			return "msgBox";
		}
	}

	/**
	 * 解除绑定
	 * @return
	 * lhyan3
	 * 2014年6月9日
	 */
	public String releaseBind(){
		String userids = request.getParameter("userids");
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		try {
			if(userids!=null && userids.length()>0){
				signRecordService.releaseBind(userids);
				SysLog.operLog(request, Constants.OPER_PWRESET_VALUE, userModel.getUserid());// 记录日志
				SysLog.info(userModel.getUserid() + " release " + userids
						+ " phoneid");
				MsgBox msgBox = new MsgBox(request,
						getText("sign.bind.success"), "um", null);
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			}else{
				MsgBox msgBox = new MsgBox(request, getText("audit.ActionFailed"));
				msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			return "msgBox";
		}
		return "msgBox";
	}
	
	/**
	 * 刷新页面
	 */
	public void refresh() {
		try {
			listAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		;
	}
	
	/**
	 * 列表查询
	 * @return
	 * lhyan3
	 * 2014年7月8日
	 */
	public String listAll(){
		UserModel userModel = (UserModel)request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		try{
			int pagenum = 1;
			int pagesize =20;
			String from = request.getParameter("from");
			String deptName = request.getParameter("deptName");
			String grpName = request.getParameter("grpName");
			String userId = request.getParameter("userId");
			String approvePerson = request.getParameter("approvePerson");
			String approveStatus = request.getParameter("approveStatus");
			String signEndTime = null;
			String signTime = null;
			//获取组别，如果是部门经理、项目经理、行政人员则默认显示自己审核中的数据;approveStatus不为null时则按approveStatus当前值查询
//			if(null==approveStatus){
//				String [] arr = userModel.getGroupids();
//				String grpCodes = "";
//				for(int i=0;i<arr.length;i++){
//					grpCodes = grpCodes + arr[i] +",";
//				}
//				if(grpCodes.indexOf("deptManager")!=-1){
//					approveStatus = "3";//部门经理审核中
//					approvePerson = userModel.getUsername();
//				}else if(grpCodes.indexOf("groupManager")!=-1){
//					approveStatus = "2";//项目经理审核中
//					approvePerson = userModel.getUsername();
//				}/*else if(grpCodes.indexOf("hr")!=-1){
//					approveStatus = "4";//行政审核中
//				}*/
//				//用于前台获取
//				request.setAttribute("grpCodes", grpCodes);
//				request.setAttribute("curusername", userModel.getUsername());
//			}

			if(pageNum!=null && pageNum.length()>0){
				pagenum = Integer.parseInt(pageNum);
			}
			if(pageSize!=null && pageSize.length()>0){
				pagesize = Integer.parseInt(pageSize);
			}
			if(request.getParameter("signEndTime")!=null && request.getParameter("signEndTime").length()>0){
				signEndTime = request.getParameter("signEndTime");
			}
			if(request.getParameter("signTime")!=null && request.getParameter("signTime").length()>0){
				signTime = request.getParameter("signTime");
			}

			Page page = signRecordService.getPageByCondition(deptName,grpName,userId,pagenum, pagesize, signTime, signEndTime, userModel,approvePerson,approveStatus);
			StaffInfoDao userDao =  (StaffInfoDao) BaseApplicationContext.getAppContext().getBean("staffInfoDao");
			//考勤状态数据字典（正常/迟到/早退/不识别..）
			Map<String, String> attendanceStatusDir = BusnDataDir.getMapKeyValue("hrManager.attendanceStatus");
			if(page!=null&&page.getQueryResult()!=null){
				List<SignRecord> list = page.getQueryResult();
				for(int i=0;i<list.size();i++){
					if(list.get(i).getApprovePerson()!=null){
						list.get(i).setApprovePerson(userDao.getUsernameById(list.get(i).getApprovePerson()));
					}else{
						list.get(i).setApprovePerson("");
					}
					//考勤状态的取得
					String status = StringUtil.trim(list.get(i).getAttendanceStatus());
					list.get(i).setAttendanceStatusValue(attendanceStatusDir.get(status));
				}
				page.setQueryResult(list);
			}
			request.setAttribute("currPage", page);
			List list = (List<Object>)page.getQueryResult();
		/*	for(Object li:list){
				SignRecord sign = (SignRecord) li;
				SysUser user = staffInfoService.findUserByUserid(sign.getUserId());
				sign.setUsername(user.getUsername());
				sign.setDeptName(BusnDataDir.getValue(BusnDataDir.getMapKeyValue("staffManager.department"),user.getDeptName()));
			}*/
			if(from!=null && from.equals("refresh")){
				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf(page.getPageCount()));
				map.put("recordCount", String.valueOf(page.getRecordCount()));
				JSONUtil jsonUtil = new JSONUtil(
						"cn.grgbanking.feeltm.domain.SignRecord");
				JSONArray jsonObj = jsonUtil.toJSON(list, map);//将查询数据转换为jsonArray，
				JSONObject input = new JSONObject();
				if(page.getRecordCount()==0){
					input.put("pageCount", String.valueOf(page.getPageCount()+1));	
					input.put("recordCount", String.valueOf(page.getRecordCount()));
					input.put("jsonObj", jsonObj);	
				}
				else{
					input.put("pageCount", String.valueOf(page.getPageCount()));
					input.put("recordCount", String.valueOf(page.getRecordCount()));
					input.put("jsonObj", jsonObj);	
				}
				input.put("jsonObj", jsonObj);		//将数据以json的方式传入前台						
				PrintWriter out = response.getWriter();
				out.print(input);
				return "";
			} else {
				ActionContext.getContext().put("signRecordlist", list);
				return "listsuccess";
			}
		}catch(Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "listsuccess";
	}
	
	
	/**
	 * @author wjie5
	 * @return 至补签到页面
	 * @throws Exception
	 * @Time 2014-08-05
	 */
	public String toresignPage() throws Exception{
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		
		SignRecord resign = new SignRecord();
		Map<String,String> deptkeyValue = BusnDataDir.getMapKeyValue("staffManager.department");
		resign.setUserId(userModel.getUserid());
		resign.setUsername(userModel.getUsername());
		resign.setDeptName(deptkeyValue.get(userModel.getDeptName()));
		resign.setGrpName(userModel.getGroupName());
		resign.setSignTime(new Date());
		List<String> arealist = signRecordService.getLastAreaByUserId(userModel.getUserid());
		String deptid = staffInfoService.getDeptkeyByValue(signRecord.getDeptName());
		List<SysUser> approveList = UserRoleConfig.getUserRoleInfoByUser(userModel,deptid);
		request.setAttribute("arealist", arealist);
		request.setAttribute("approvePerson", approveList);
		if(UserRoleConfig.getGrpList(userModel).contains("deptManager")){
			request.setAttribute("isDeptManager", "true");
		}
		ActionContext.getContext().getValueStack().pop();				//清空页面堆栈中的顶层数据
		ActionContext.getContext().getValueStack().push(resign); 	//将需修改的原数据放入顶层堆栈中，方便页面获取
		return "resginPage";
	}
	
	/**
	 * 新增或者修改补签到信息，当页面无id时则为新增补签到信息，当页面有id传入，则页面为修改该id对应的补签到信息
	 * @author wjie5
	 * @return
	 * @throws Exception
	 * @Time 2014-08-05
	 */
	public String saveResign() throws Exception{
		MsgBox msgBox = null;
		Map<String,String> deptkeyValue = BusnDataDir.getMapKeyValue("staffManager.department");
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		try{
			if(signRecord.getId()==null||signRecord.getId().trim().equals("")){
				//添加是否超过锁定日期的判断begin
				String str = DateUtil.getDateString(signRecord.getSignTime());
				if(!DateUtil.flagSigntimeTimeOut(DateUtil.stringToDate(str, "yyyy-MM-dd"))){
					msgBox = new MsgBox(request, "签到日期已经超出锁定的日期，不能再进行操作了");
					msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
					return "msgBox";
				}//end
				//新增
				signRecord.setUserId(userModel.getUserid());
				signRecord.setUsername(userModel.getUsername());
				signRecord.setDeptName(deptkeyValue.get(userModel.getDeptName()));
				signRecord.setGrpName(userModel.getGroupName());
				signRecord.setType("3");//补签到
				if(UserRoleConfig.getGrpList(userModel).contains("deptManager")){//部门经理补签到，保存直接通过
					signRecord.setApproveStatus("6");
					signRecord.setVilid("1");
					signRecord.setApprovePerson(userModel.getUserid());
					//根据考勤时间得出考勤状态
					String s_signtime = cn.grgbanking.feeltm.util.DateUtil.getTimeString(signRecord.getSignTime()).split(" ")[1];
					int status = signRecordService.getAttendanceStatus(s_signtime);
					signRecord.setAttendanceStatus(status);
				}else{
					signRecord.setVilid("0");//无效
					signRecord.setApproveStatus("0");
				}
				
				
//				String areaname = signRecord.getAreaName().trim();
//				if(areaname.substring(areaname.length()-1, areaname.length()).equals(",")){
//					areaname = areaname.substring(0, areaname.length()-1);
//					signRecord.setAreaName(areaname);
//				}
				String areaName = (signRecord.getAreaName().trim()).replaceAll("，", ",");
				signRecord.setAreaName(areaName.split(",")[0]);
				signRecordService.add(signRecord);
				
				SysLog.operLog(request, Constants.OPER_ADD_VALUE,
						userModel.getUserid());// 记录日志
				SysLog.info("User:" + userModel.getUserid()
						+ " resign a signRecord : " + signRecord.getId());
				msgBox = new MsgBox(request, getText("add.ok"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			}else if(signRecord.getId()!=null||!signRecord.getId().trim().equals("")){
				//修改
				SignRecord resign = signRecordService.findById(signRecord.getId());
				//添加是否超过锁定日期的判断begin
				String str = DateUtil.getDateString(signRecord.getSignTime());
				if(!DateUtil.flagSigntimeTimeOut(DateUtil.stringToDate(str, "yyyy-MM-dd"))){
					msgBox = new MsgBox(request, "签到日期已经超出锁定的日期，不能再进行操作了");
					msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
					return "msgBox";
				}//end
				
				resign.setApprovePerson(signRecord.getApprovePerson());
				resign.setAreaName(signRecord.getAreaName().split(",")[0].trim());
				resign.setSignTime(signRecord.getSignTime());
				resign.setSignNote(signRecord.getSignNote());
				resign.setApproveStatus("1");//修改中
				
				signRecordService.update(resign);
				
				SysLog.operLog(request, Constants.OPER_ADD_VALUE,
						userModel.getUserid());// 记录日志
				SysLog.info("User:" + userModel.getUserid()
						+ " resign a signRecord : " + signRecord.getId());
				msgBox = new MsgBox(request, "修改成功");
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			}
		} catch (Exception e) {
			SysLog.error(request, "error in(SginRecordAction-saveResign())");
			SysLog.error(e);
			msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
		}
		return "msgBox";
	}
	
	/**
	 * @author wjie5
	 * @return 至修改补签或者备注签到页面(如果type=3,则进入修改补签页面，否则进入备注签到)
	 * @throws Exception
	 * @Time 2014-08-06
	 */
	public String toModifyResignPage() throws Exception{
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		signRecord  = signRecordService.findById(signRecord.getId());
		if(!signRecord.getUserId().equals(userModel.getUserid())){
			String msg = "您只能备注自己的范围外的正常签到信息";
			if(signRecord.getType().equals("3"))
				msg = "您只能修改自己的补签到信息";
			MsgBox msgBox1 = new MsgBox(request,getText("operInfoform.updatefaile")+msg);
			msgBox1.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}
		String deptid = staffInfoService.getDeptkeyByValue(signRecord.getDeptName());
		List<SysUser> approveList = UserRoleConfig.getUserRoleInfoByUser(userModel,deptid);
		ActionContext.getContext().getValueStack().pop();				//清空页面堆栈中的顶层数据
		ActionContext.getContext().getValueStack().push(signRecord); 	//将需修改的原数据放入顶层堆栈中，方便页面获取
		request.setAttribute("approvePerson", approveList);
		this.setRecordToPage(signRecord.getId());
		if(signRecord.getType().equals("3")){
			List<String> arealist = signRecordService.getLastAreaByUserId(userModel.getUserid());
			request.setAttribute("arealist", arealist);
			request.setAttribute("areanameV", signRecord.getAreaName());
			return "resginPage";
		}
		if(UserRoleConfig.getGrpList(userModel).contains("deptManager")){//部门经理补签到，保存直接通过
			request.setAttribute("isDeptManager", "true");
		}
		return "marksginPage";
	}
	
	/**
	 * 备注签到
	 * @author wjie5
	 * @return
	 * @throws Exception
	 * @Time 2014-08-07
	 */
	public String updateMarksign() throws Exception{
		MsgBox msgBox = null;
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		try{
			SignRecord resign = signRecordService.findById(signRecord.getId());
			//添加是否超过锁定日期的判断begin
			String str = DateUtil.getDateString(signRecord.getSignTime());
			if(!DateUtil.flagSigntimeTimeOut(DateUtil.stringToDate(str, "yyyy-MM-dd"))){
				msgBox = new MsgBox(request, "签到日期已经超出锁定的日期，不能再进行操作了");
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}
			//end
			
			resign.setApprovePerson(signRecord.getApprovePerson());
			resign.setSignNote(signRecord.getSignNote());
			resign.setSignTime(signRecord.getSignTime());
			if(UserRoleConfig.getGrpList(userModel).contains("deptManager")){//部门经理补签到，保存直接通过
				resign.setApproveStatus("6");
				resign.setVilid("1");
				resign.setApprovePerson(userModel.getUserid());
				//根据考勤时间得出考勤状态
				String s_signtime = cn.grgbanking.feeltm.util.DateUtil.getTimeString(resign.getSignTime()).split(" ")[1];
				int status = signRecordService.getAttendanceStatus(s_signtime);
				resign.setAttendanceStatus(status);
			}else{
				resign.setApproveStatus("1");//修改中
			}
			
			signRecordService.update(resign);
			
			SysLog.operLog(request, Constants.OPER_ADD_VALUE,
					userModel.getUserid());// 记录日志
			SysLog.info("User:" + userModel.getUserid()
					+ " resign a signRecord : " + signRecord.getId());
			msgBox = new MsgBox(request, "备注成功");
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		} catch (Exception e) {
			SysLog.error(request, "error in(SginRecordAction-updateMarksign())");
			SysLog.error(e);
			msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
		}
		return "msgBox";
	}
	
	/**
	 * 提交签到信息审核
	 * @author wjie5
	 * @return
	 * @throws Exception
	 * @Time 2014-08-08
	 */
	public String submitApproval() throws Exception{
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		MsgBox msgBox;
		try {
			String id = request.getParameter("ids");
			if (id != null && !"".equals(id)) {
				String resultMsg = signRecordService.updateSignList(id,userModel);
				msgBox = new MsgBox(request, getText("operInfoform.toauditsuccess")+resultMsg);
				msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			} else {
				msgBox = new MsgBox(request, getText("audit.ActionFailed"));
				msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			}
		} catch (Exception e) {
			SysLog.error(request, "error in(LeaveAction-auditing())");
			SysLog.error(e);
			msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
		}
		return "msgBox";
	}
	
	/**
	 * @return 至审核签到页面
	 * @return
	 * @throws Exception
	 */
	public String toapprovalsign() throws Exception{
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		signRecord  = signRecordService.findById(signRecord.getId());
		if(!signRecord.getApprovePerson().equals(userModel.getUserid())){
			String msg = "您只能审核审核人是自己的签到信息";
			MsgBox msgBox1 = new MsgBox(request,getText("operInfoform.auditfaile")+msg);
			msgBox1.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}
		
		ActionContext.getContext().getValueStack().pop();				//清空页面堆栈中的顶层数据
		ActionContext.getContext().getValueStack().push(signRecord); 	//将需修改的原数据放入顶层堆栈中，方便页面获取
		List<SysUser> approveList = new ArrayList<SysUser>();
		if(signRecord.getApproveStatus()!=null&&!signRecord.getApproveStatus().equals("4")){
			String deptid = staffInfoService.getDeptkeyByValue(signRecord.getDeptName());
			approveList = UserRoleConfig.getUserRoleInfoByUser(userModel,deptid);

		}
		//用于前台获取
		request.setAttribute("appstatus", signRecord.getApproveStatus());
		request.setAttribute("approvePerson", approveList);
		this.setRecordToPage(signRecord.getId());
		return "approvalSignPage";
	}
	
	/**
	 * 审核签到信息，主要是审核补签到以及备注签到的信息
	 * @author wjie5
	 * @return
	 * @throws Exception
	 * @Time 2014-08-08
	 */
	public String approvalsign()throws Exception{
		MsgBox msgBox = null;
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		try{
			
			String option = request.getParameter("signResult");
			
			signRecordService.approvalsign(userModel,signRecord,option,approvalService);
			
			SysLog.operLog(request, Constants.OPER_ADD_VALUE,
					userModel.getUserid());// 记录日志
			SysLog.info("User:" + userModel.getUserid()
					+ " resign a signRecord : " + signRecord.getId());
			msgBox = new MsgBox(request,getText("operInfoform.auditsuccess"));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		} catch (Exception e) {
			SysLog.error(request, "error in(SginRecordAction-approvalsign())");
			SysLog.error(e);
			msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
		}
		return "msgBox";
	}
	
	/**
	 * 查看详情页面
	 * @return
	 * @throws Exception
	 */
	public String view() throws Exception{
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		signRecord  = signRecordService.findById(signRecord.getId());
		StaffInfoDao userDao =  (StaffInfoDao) BaseApplicationContext.getAppContext().getBean("staffInfoDao");
		signRecord.setApprovePerson(userDao.getUsernameById(signRecord.getApprovePerson()));
		ActionContext.getContext().getValueStack().pop();				//清空页面堆栈中的顶层数据
		ActionContext.getContext().getValueStack().push(signRecord); 	//将需修改的原数据放入顶层堆栈中，方便页面获取
		this.setRecordToPage(signRecord.getId());
		return "view";
	}
	
	private void setRecordToPage(String recordid) {
		List<ApprovalRecord> records = approvalService.getRecordByName(recordid);
		String approveRecord = "";
		for (int i =0;i<records.size();i++) {
			String result = "[审核通过]";
			if(records.get(i).getResult().equals("5"))
				result = "[审核不通过]";
			String opinion = records.get(i).getOpinion();
			approveRecord += "<font style='font-weight: bold'>" + result+"</font> "+records.get(i).getApprovalUser()+"("
						+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(records.get(i).getApprovalTime())+"): "
						+ (opinion==null?"":opinion) +"\n<br>";
		}
		request.setAttribute("approveRecord", approveRecord);
	}
	
	/**
	 * 跳转到导出excel页面
	 * @return
	 * @throws Exception
	 */
	public String toExportPage() throws Exception{
		MsgBox msgBox;
		try {
			List<Project> projects = projectService.listAllGroup();
			request.setAttribute("projects", projects);
			return "toExportData";
		} catch (Exception e) {
			SysLog.error(request, "error in (UserHolsAction.java-addPage())");
			SysLog.error(e);
			msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			return "msgBox";
		}
	}
	
	/**
	 * 导出excel表
	 */
	public String exportData() throws Exception{
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		String deptName = request.getParameter("deptName");
		String grpName = request.getParameter("grpName");
		String userId = request.getParameter("userId");
		String approvePerson = request.getParameter("approvePerson");
		String approveStatus = request.getParameter("approveStatus");
		String signEndTime = null;
		String signTime = null;
		//获取组别，如果是部门经理、项目经理、行政人员则默认显示自己审核中的数据;approveStatus不为null时则按approveStatus当前值查询

		if(request.getParameter("signEndTime")!=null && request.getParameter("signEndTime").length()>0){
			signEndTime = request.getParameter("signEndTime");
		}
		if(request.getParameter("signTime")!=null && request.getParameter("signTime").length()>0){
			signTime = request.getParameter("signTime");
		}

		if(!UserRoleConfig.ifHr(userModel)){
			MsgBox msgBox = new MsgBox(request,getText("非行政人员没有导出签到信息的权限!"));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}
		
		
		List list = signRecordService.getListByCondition(deptName, grpName, userId, signTime, signEndTime, userModel, approvePerson, approveStatus);
		String filename="移动签到";
		OutputStream  os = response.getOutputStream();// 取得输出流 
		response.reset();// 清空输出流  
		response.setHeader("Content-disposition","attachment;filename="+new String(filename.getBytes("gb2312"), "iso8859-1")+".xls");// 设定输出文件头
        response.setContentType("application/vnd.ms-excel");
     	//第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("SignRecord");
		/**	** ** *设置字体* ** ** **/
		HSSFFont defaultFont = wb.createFont(); 
		defaultFont.setFontName("宋体");
		defaultFont.setFontHeightInPoints((short)12);
		
		HSSFFont headfont = wb.createFont();
		headfont.setFontName("黑体");
		headfont.setFontHeightInPoints((short)16);// 字体大小   
		headfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		
		HSSFFont cellfont = wb.createFont();
		headfont.setFontName("黑体");
		headfont.setFontHeightInPoints((short)16);// 字体大小   
		headfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		
		HSSFFont cellredfont = wb.createFont();
		cellredfont.setFontName("宋体");
		cellredfont.setColor(HSSFColor.RED.index);
		cellredfont.setFontHeightInPoints((short)10);// 字体大小   
		
		HSSFFont allFont = wb.createFont();
		allFont.setFontName("黑体");
		allFont.setColor(HSSFColor.RED.index);
		allFont.setFontHeightInPoints((short)12);// 字体大小   
		allFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		
		/**	** ** *设置样式* ** ** **/
		HSSFCellStyle defaultStyle = this.getCellStyle(wb,true, defaultFont,(short)-2, (short)-2, HSSFCellStyle.ALIGN_CENTER);
		HSSFCellStyle tileStyle = this.getCellStyle(wb, false,headfont, (short)-2, (short)-2, HSSFCellStyle.ALIGN_CENTER);
		HSSFCellStyle dateStyle = this.getCellStyle(wb, false,defaultFont,(short)-2, (short)-2, HSSFCellStyle.ALIGN_CENTER);
		HSSFCellStyle upDateStyle = this.getCellStyle(wb, true,defaultFont,(short)-2, (short)-2, HSSFCellStyle.ALIGN_CENTER);
		HSSFCellStyle allStyle = this.getCellStyle(wb, false, cellfont, (short)-2, (short)-2, HSSFCellStyle.ALIGN_LEFT);
		HSSFCellStyle allredStyle = this.getCellStyle(wb, false, cellredfont, (short)-2, (short)-2, HSSFCellStyle.ALIGN_LEFT);
		
		/**	** ** *设置文本格式* ** ** **/
		HSSFDataFormat format = wb.createDataFormat();
		defaultStyle.setDataFormat(format.getFormat("@"));
		dateStyle.setDataFormat(format.getFormat("yyyy-MM-dd hh:mm:ss"));
		upDateStyle.setDataFormat(format.getFormat("yyyy-MM-dd hh:mm:ss"));
		
		/**		创建标题		*/
		HSSFRow titleRow = sheet.createRow((int) 0);
		titleRow.setHeight((short)(512));
		wb = this.setExcelValue(wb, 0, "签到数据", 0, (short)0, tileStyle);
		sheet.addMergedRegion(new Region(0, (short)0, 0, (short)5));
		
		/**		创建列名		*/
		HSSFRow cellNameRow = sheet.createRow((int) 1);
		cellNameRow.setHeight((short)(512));
		wb = this.setExcelValue(wb, 0, "姓名", 1, (short)0, allStyle);
		wb = this.setExcelValue(wb, 0, "账号", 1, (short)1, allStyle);
		wb = this.setExcelValue(wb, 0, "签到时间", 1, (short)2, allStyle);
		wb = this.setExcelValue(wb, 0, "签到地址", 1,(short) 3, allStyle);
		wb = this.setExcelValue(wb, 0, "在误差范围内", 1, (short)4, allStyle);
		wb = this.setExcelValue(wb, 0, "考勤状态", 1, (short)5, allStyle);
		
		int i=0;
		/**	** ** *循环数据，并将数据列入Excel表中* ** ** **/
		for(;i<list.size();i++){
			String typecss = "allStyle";
			Object[] record = (Object[])list.get(i);
			// sign.C_USERNAME,sign.C_USERID,TO_CHAR(sign.D_SIGNTIME,'yyyy-mm-dd hh24:mi:ss'),sign.C_AREANAME,sign.C_VILID,"
			String celldate = "";
			if(record[2]==null){
				celldate="";
			}else{
				celldate=record[2].toString();//签到时间
				if(!"".equals(celldate)){
					String s_date = celldate.split(" ")[0];
					//判断日期是否为工作日，如果不是continue
					if(holidayService.isHoliday(DateUtil.to_date(s_date))){
						continue;
					}
					String s_time = celldate.split(" ")[1];
					if("00:00:00".equals(s_time)){
						celldate = s_date;
						typecss="allredStyle";
					}
					if("23:59:59".equals(s_time)){
						celldate = s_date;
						typecss="allredStyle";
					}
				}
			}
			String status = "";
			if(record[5]==null){//考勤状态
				status="";
			}else{
				status=record[5].toString();
				
				
			}
			HSSFRow row = sheet.createRow(i+2);
			row.setHeight((short)512);
			HSSFCell cell = row.createCell((short)0);
			
			cell = row.createCell((short)0);
			if("allStyle".equals(typecss)){
				cell.setCellStyle(allStyle);
			}else{
				cell.setCellStyle(allredStyle);
			}
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			//姓名
			if(record[0]==null){
				cell.setCellValue("");
			}else{
				cell.setCellValue(record[0].toString());
			}
			
			cell = row.createCell((short)1);
			if("allStyle".equals(typecss)){
				cell.setCellStyle(allStyle);
			}else{
				cell.setCellStyle(allredStyle);
			}
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellValue(record[1].toString());
			
			cell = row.createCell((short)2);
			if("allStyle".equals(typecss)){
				cell.setCellStyle(allStyle);
			}else{
				cell.setCellStyle(allredStyle);
			}
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			//时间
			if(record[2]==null){
				cell.setCellValue("");
			}else{
				cell.setCellValue(record[2].toString());
			}
			sheet.setColumnWidth((short)2, (short) (14*512));
			
			cell = row.createCell((short)3);
			if("allStyle".equals(typecss)){
				cell.setCellStyle(allStyle);
			}else{
				cell.setCellStyle(allredStyle);
			}
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			//签到地点
			if(record[3]==null){
				cell.setCellValue("");
			}else{
				cell.setCellValue(record[3].toString());
			}
			sheet.setColumnWidth((short)3, (short) (20*512));
			
			cell = row.createCell((short)4);
			if("allStyle".equals(typecss)){
				cell.setCellStyle(allStyle);
			}else{
				cell.setCellStyle(allredStyle);
			}
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			if(record[4]==null){
				cell.setCellValue("");
			}else{
				//1：有效  0：无效
				String valid = record[4].toString();
				if( "0".equals(valid)){
					cell.setCellValue("范围外");
				}else if(  "1".equals(valid) ){
					cell.setCellValue("范围内");
				}
				
			}
			sheet.setColumnWidth((short)4, (short) (8*512));
			
			cell = row.createCell((short)5);
			if("allStyle".equals(typecss)){
				cell.setCellStyle(allStyle);
			}else{
				cell.setCellStyle(allredStyle);
			}
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			//考勤状态
			if(record[5]==null){
				cell.setCellValue("");
			}else{
				//考勤状态   0:无效、不识别考勤 1:出勤 2:退勤3:迟到4:早退
				String statu = record[5].toString();
				if("0".equals(statu) ){
					cell.setCellValue("无效、不识别考勤");
				}else if("1".equals(statu)){
					cell.setCellValue("出勤");
				}else if("2".equals(statu)){
					cell.setCellValue("退勤");
				}else if("3".equals(statu)){
					cell.setCellValue("迟到");
				}else if("4".equals(statu)){
					cell.setCellValue("早退");
				}
			}
		}
		
		wb.write(os);
		os.close();
        
		//清理刷新缓冲区，将缓存中的数据将数据导出excel
		os.flush();
		//关闭os
		if(os!=null){
			os.close();
		}
		MsgBox msgBox = new MsgBox(request, getText("add.ok"));
		msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		this.addActionMessage(getText("add.ok"));
		return "msgBox";
	}
	
	/**
	 * 设置样式方法，
	 * @param wb
	 * @param hasBorder
	 * @param defaultFont
	 * @param FillForeColor
	 * @param FillbackColor
	 * @param local
	 * @return
	 */
	private HSSFCellStyle getCellStyle(HSSFWorkbook wb,boolean hasBorder,HSSFFont defaultFont,short FillForeColor,short FillbackColor,short local){
		HSSFCellStyle style = wb.createCellStyle();	
		
		/**  黑色粗体边框    */
		if(hasBorder){
			style.setBorderBottom(HSSFCellStyle.BORDER_THIN);	
			style.setBottomBorderColor(HSSFColor.BLACK.index);	
			style.setBorderLeft(HSSFCellStyle.BORDER_THIN);		
			style.setLeftBorderColor(HSSFColor.BLACK.index);
			style.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style.setRightBorderColor(HSSFColor.BLACK.index);
			style.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style.setTopBorderColor(HSSFColor.BLACK.index);
		}
		/** 	fontColor字体		*/
		if(defaultFont!=null){
			style.setFont(defaultFont);
		}
		
		/**		FillColor前景填充		*/
		if(FillForeColor!=(short)-2){
			style.setFillForegroundColor(FillForeColor);
			style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		}
		if(FillForeColor!=(short)-2){
			style.setFillBackgroundColor(FillForeColor);
			style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		}
		/**		设置格式 		*/
		if(local!=-2){
			style.setAlignment(local);
		}
		style.setWrapText(true);//自动换行
		return style;
	}
	
	/**
	 * 设置每一个cell值以及其样式
	 * @param wb
	 * @param sheetNo
	 * @param value
	 * @param rowNo
	 * @param cellNo
	 * @param defaultStyle
	 * @return
	 */
	private HSSFWorkbook setExcelValue(HSSFWorkbook wb,int sheetNo,Object value,int rowNo,short cellNo,HSSFCellStyle defaultStyle){
		HSSFSheet sheet = wb.getSheetAt(sheetNo);
		HSSFRow row = sheet.getRow(rowNo);
		HSSFCell cell = row.createCell(cellNo);
		if(value instanceof Integer){
			cell.setCellValue(Integer.valueOf(value.toString()));
		}else if(value instanceof String){
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellValue(value.toString());
		}else if(value instanceof Date){
			cell.setCellValue((Date)value);
		}else if(value instanceof Double){
			cell.setCellValue((Double)value);
		}else if(value instanceof Calendar){
			cell.setCellValue((Calendar)value);
		}else if(value instanceof Boolean){
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellValue(value.toString().equals("true")?"是":"否");
		}else if(value == null){
			cell.setCellValue(value.toString());
		}else {
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellValue(value.toString());
		}
		if(defaultStyle!=null){
			cell.setCellStyle(defaultStyle);
		}
		return wb;
	}
	
	/**
	 * 判断是否迟到或早退
	 * @return
	 */
	public String isLateOrLeave(Date signTime){
		SimpleDateFormat fo=new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat simple = new SimpleDateFormat("08:30:00");
		SimpleDateFormat format = new SimpleDateFormat("17:30:00");
		SimpleDateFormat sdf = new SimpleDateFormat("12:00:00");
		Date date = null;
		Date time = null;
		date = signTime;
		time = signTime;
		  if(simple.format(time).compareTo(fo.format(date))<0 && fo.format(date).compareTo(sdf.format(date))<0){
		     return "迟到";
		      }
		 if(format.format(time).compareTo(fo.format(date))>0 && fo.format(date).compareTo(sdf.format(date))>0){
			 return "早退";
		 }
		
		return "";
	}
	
	@SuppressWarnings("deprecation")
	public String importData(){
		try{
			int existNum = 0;	//系统中已经存在的记录数
			int noUsernum = 0;	//系统中不存在对应工号的个数
			String noUsername = ""; //系统中不存在工号的用户姓名
			String noUserCard = "";
			int outtimeNum = 0;//超出锁定日期的记录数
			
			UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
			if (file == null) {
				throw new Exception("文件为空！");
			}
			//获取文件流，并创建对应的excel对象
			POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(file));
			HSSFWorkbook wb =  new HSSFWorkbook(fs);
			int sheetNumber = wb.getNumberOfSheets();
			
			List<SignRecord> SignRecordList = new ArrayList<SignRecord>();	//数据添加集合
			String error_UsrContact = "";//错误信息
			
			/**	** ** *循环每一个sheet，添加数据* ** ** **/
			for(int shNum=0;shNum<sheetNumber;shNum++){ 
				HSSFSheet sheet = wb.getSheetAt(shNum);//获得sheet
				HSSFRow headRow = sheet.getRow(0);//获得sheet的第一行
				if(headRow==null){//判断sheet中第一行是否有数据，没有数据则不进行下一步
					continue;
				}
				HSSFRow cNameRow = sheet.getRow(0);	//获取列标题行，并判断对应列标题是否正确，防止数据导入出错
				String cellUserName = "";//员工姓名
				String cellUserId = "";//工号
				String cellDeptName = "";//所属部门
				String cellAttenceTime = "";//考勤时间
				if(cNameRow.getCell((short)1)!=null){
					cNameRow.getCell((short)1).setCellType(HSSFCell.CELL_TYPE_STRING);
				}else{
					continue;
				}
				cellUserName = cNameRow.getCell((short)1).getStringCellValue()==null?"": cNameRow.getCell((short)1).getStringCellValue();
				if(cNameRow.getCell((short)2)!=null)
					cNameRow.getCell((short)2).setCellType(HSSFCell.CELL_TYPE_STRING);
				cellUserId = cNameRow.getCell((short)2).getStringCellValue()==null?"": cNameRow.getCell((short)2).getStringCellValue();
				if(cNameRow.getCell((short)3)!=null)
					cNameRow.getCell((short)3).setCellType(HSSFCell.CELL_TYPE_STRING);
				cellDeptName = cNameRow.getCell((short)3).getStringCellValue()==null?"": cNameRow.getCell((short)3).getStringCellValue();
				if(cNameRow.getCell((short)4)!=null)
					cNameRow.getCell((short)4).setCellType(HSSFCell.CELL_TYPE_STRING);
				cellAttenceTime = cNameRow.getCell((short)4).getStringCellValue()==null?"": cNameRow.getCell((short)4).getStringCellValue();
				if(!cellUserName.equals("员工姓名")){
					error_UsrContact += "此文件员工姓名列不存在或者格式不对；";
				}else if(!cellUserId.equals("工号")){
					error_UsrContact += "此文件工号列不存在或者格式不对；";
				}else if(!cellDeptName.equals("所属部门")){
					error_UsrContact += "此文件所属部门列不存在或者格式不对；";
				}else if(!cellAttenceTime.equals("考勤时间")){
					error_UsrContact += "此文件考勤时间列不存在或者格式不对；";
				}else {
					/**	** ** *循环excel表格中的数据，并判断格式，存入或修改数据库相应值* ** ** **/
					long rowNumber = sheet.getPhysicalNumberOfRows();	//sheet中的总行数
					SimpleDateFormat sdf =new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy",Locale.US);
					for(int rowNum=1;rowNum<rowNumber;rowNum++){ 
						HSSFRow currentRow = sheet.getRow(rowNum);//循环到当前行
						if(currentRow==null){
							continue;
						}
						if(currentRow.getZeroHeight()){//如果是隐藏行，则跳过[将之前的poi2.0修改为poi3.7，poi2.0无判断是否为隐藏行的方法，poi3.7中去掉了setEncoding方法，在该版本中无需设置编码]
							continue;
						}
						String userName = "";//姓名
						String userId = "";//工号
						String deptName = "";//所属部门
						String signTime = "";//签到时间	
						if(currentRow.getCell((short)1)!=null){
							currentRow.getCell((short)1).setCellType(HSSFCell.CELL_TYPE_STRING);
						}else{
							continue;
						}
						userName = currentRow.getCell((short)1).getStringCellValue()==null?"": currentRow.getCell((short)1).getStringCellValue();
						if(currentRow.getCell((short)2)!=null)
							currentRow.getCell((short)2).setCellType(HSSFCell.CELL_TYPE_STRING);
						userId = currentRow.getCell((short)2).getStringCellValue()==null?"": currentRow.getCell((short)2).getStringCellValue();
						if(!staffInfoService.isExitStaffByUserNumber(userId, "1")) {//判断外派工号userid是否有对应的sysuser信息，如果无则记录下来，在界面上填补
							if(staffInfoService.isExitStaffByUserName(userName)){//如果外派工号在sysuser中无对应数据时，检测姓名是否在sysuser中，如果无，则任务不会本公司的用户，排除该数据
								if(noUserCard.indexOf(userId)!=-1){
									continue;
								}
								noUsernum += 1;
								noUsername = noUsername + "," + userName;
								noUserCard = noUserCard + "," + userId;
								continue;
							}else{
								continue;
							}
						}
						SysUser sysUser = staffInfoService.findUserByUserNumber(userId, "1");//"1"表示该工号是人员外派所在单位的工号(外派工号)
						userId = sysUser.getUserid();//由工号获得用户标识符
						if(currentRow.getCell((short)3)!=null)
							currentRow.getCell((short)3).setCellType(HSSFCell.CELL_TYPE_STRING);
						deptName = currentRow.getCell((short)3).getStringCellValue()==null?"": currentRow.getCell((short)3).getStringCellValue();
						if(currentRow.getCell((short)4)!=null) {
							if(currentRow.getCell((short)4).getCellType()==HSSFCell.CELL_TYPE_STRING)
								signTime = currentRow.getCell((short)4).getStringCellValue()==null?"": currentRow.getCell((short)4).getStringCellValue();
							else {
								signTime = currentRow.getCell((short)4).getDateCellValue()==null?"": String.valueOf(currentRow.getCell((short)4).getDateCellValue());
							}
						}
						//是否超出锁定日期，
						if(!DateUtil.flagSigntimeTimeOut(sdf.parse(signTime))){
							outtimeNum++;
							continue;
						}
						//如果当前用户已经有了这一时间点的考勤记录，则不存入数据库
						boolean flag = signRecordService.checkOt(userId, sdf.parse(signTime));
						if(!flag){
							existNum += 1;
							continue;
						}
						String oa_deptname = staffInfoService.getDeptNameValueByUserId(userId);//根据用户ID取出OA中的部门名称
						SignRecord signRecord = new SignRecord(); 
						signRecord.setUserId(userId);
						signRecord.setUsername(userName);
						signRecord.setDeptName(oa_deptname);
						signRecord.setGrpName(deptName);
						signRecord.setSignTime(sdf.parse(signTime));
						signRecord.setType("1");
						signRecord.setSignwhere("1");
						signRecord.setVilid("1");
						SignRecordList.add(signRecord);
					}
				}
			}
			if(noUsernum>0){
				request.setAttribute("nousername", noUsername.substring(1));
				request.setAttribute("nousercard", noUserCard.substring(1));
				return "saveCard";
			}
			
			//考勤表插入数据
			if(SignRecordList.size()>0){
				signRecordService.addSignRecordListInfo(SignRecordList);
				attendanceAnalysisService.execOutcard_Proc();
				//导入数据后，修改考勤状态
				//cardRecordService.updateGFAttendanceStatus();
			}
			
			SysLog.operLog(request, Constants.OPER_ADD_VALUE, SignRecordList.size()+"考勤记录");// 记录日志
			SysLog.info("User:" + userModel.getUserid()+ " add "+SignRecordList.size()+" SignRecord "+error_UsrContact );
			String retMsg = ",";
			if(existNum!=0){
				retMsg += "系统中已存在记录数："+existNum+"条";
			}
			if(SignRecordList.size()!=0){
				if(existNum!=0){
					retMsg += "，成功导入："+SignRecordList.size()+"条";
				}else{
					retMsg += "成功导入："+SignRecordList.size()+"条";
				}
			}else{
				if(existNum!=0){
					retMsg += "，成功导入：0条";
				}
			}
			if(outtimeNum>0){
				retMsg += "，超出锁定日期的记录数有："+outtimeNum+"条";
			}
			if(",".equals(retMsg)){
				retMsg = "未导入任何数据，请查看文件格式是否正确";
			}else{
				retMsg = retMsg.substring(1);
			}
			MsgBox msgBox = new MsgBox(request,retMsg);
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}catch(Exception e){
			e.printStackTrace();
			MsgBox msgBox = new MsgBox(request,"文件格式错误");
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}
	}
	
	public String importAttendanceData() {
		try{
			if (file == null) {
				MsgBox msgBox = new MsgBox(request,"文件为空");
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}
			POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(file));
			HSSFWorkbook wb =  new HSSFWorkbook(fs);
			int sheetNumber = wb.getNumberOfSheets();
			List<SignRecord> singRecordList = new ArrayList<SignRecord>();
			int existNum = 0;	//系统中已经存在的记录数
			int noUsernum = 0;	//系统中不存在对应工号的个数
			String noUsername = ""; //系统中不存在工号的用户姓名
			for(int shNum=0;shNum<sheetNumber;shNum++){
				HSSFSheet sheet = wb.getSheetAt(shNum);
				long rowNumber = sheet.getPhysicalNumberOfRows();
				String dept_name = ""; 
				String user_name = "";
				String user_id = "";
				for(int rowNum=0;rowNum<rowNumber;rowNum++){
					HSSFRow row = sheet.getRow(rowNum);
					if(row.getCell((short)0)==null){
						continue;
					}
					String title = row.getCell((short)0).getStringCellValue();
					if("考勤打卡明细表".equals(title)){
						continue;
					}
					String printdate = row.getCell((short)0).getStringCellValue();
					if("打印日期".equals(printdate)){
						continue;
					}
					String deptname = row.getCell((short)0).getStringCellValue();
					if("部门".equals(deptname)){
						dept_name = row.getCell((short)1).getStringCellValue();
						continue;
					}
					String username = row.getCell((short)0).getStringCellValue();
					if("人员".equals(username)){
						user_name = row.getCell((short)1).getStringCellValue();
						user_id = row.getCell((short)3).getStringCellValue();
						//根据工号获取用户信息
//						boolean flag = staffInfoService.isExitStaffByUserNumber(user_id, "0");
//						if(!flag){
//							noUsernum += 1;
//							noUsername += noUsername + ",";
//							continue;
//						}
//						SysUser userinfo=staffInfoService.findUserByUserNumber(user_id, "0");
//						user_id=userinfo.getUserid();
						continue;
					}
					String blackLine = row.getCell((short)0).getStringCellValue();
					if("".equals(blackLine)){
						continue;
					}else if(blackLine==null){
						break;
					}
					String filedTitle = row.getCell((short)0).getStringCellValue();
					if("日期".equals(filedTitle)){
						continue;
					}
					int cellNumber = row.getPhysicalNumberOfCells();//获取当前行的总列数
					String signdate = row.getCell((short)0).getStringCellValue();
					for(int cellNum=1;cellNum<cellNumber-1;cellNum++){
						String signtime = row.getCell((short)cellNum).getStringCellValue();
						if("".equals(signtime)){
							continue;
						}else{
							SignRecord record = new SignRecord();
							String signdatetime = signdate+" "+signtime;
							Date sign_date = DateUtil.stringToDate(signdatetime, "yyyy-MM-dd HH:mm:ss");
							record.setUserId(user_id);
							record.setUsername(user_name);
							record.setDeptName(dept_name);
							String prjName = projectService.getProjectNameByUserid(user_id);
							record.setGrpName(prjName);
							record.setSignTime(sign_date);
							record.setType("0");
							record.setSignwhere("0");
							//判断用户是否存在该时间段的记录
							boolean flag = signRecordService.checkOt(user_id, sign_date);
							if(!flag){
								existNum += 1;
								continue;
							}
							singRecordList.add(record);
						}
					}
				}
			}
			//考勤表插入数据
			if(singRecordList.size()>0){
				signRecordService.updateStatus();
				signRecordService.addSignRecordListInfo(singRecordList);
//				attendanceCountService.getInnerAttendanceData();
//				attendanceCountService.updateAttendanceData();
			}
			
			SysLog.operLog(request, Constants.OPER_ADD_VALUE, singRecordList.size()+"考勤记录");// 记录日志
			String retMsg = ",";
			if(existNum!=0){
				retMsg += "系统中已存在记录数："+existNum;
			}
			if(noUsernum!=0){
				retMsg += "无对应工号的用户数："+noUsernum+"。无对应工号的用户为："+noUsername;
			}
			if(singRecordList.size()!=0){
				retMsg += "成功导入："+singRecordList.size()+"条记录";
			}
			if(",".equals(retMsg)){
				retMsg = "未导入任何数据，请查看文件格式是否正确";
			}else{
				retMsg = retMsg.substring(1);
			}
			MsgBox msgBox = new MsgBox(request,retMsg);
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}catch(Exception e){
			e.printStackTrace();
			MsgBox msgBox = new MsgBox(request,"文件格式错误");
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}
	}

	/**
	 * 判断是否是行政人员
	 * @return
	 * zqsheng1
	 * 2014年7月4日
	 */
	public String ifHR() {
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		try {
			JSONObject input = new JSONObject();
			PrintWriter out;
			if(UserRoleConfig.ifHr(userModel)){ 
				input.put("code", "0");//是行政人员
			} else {
				input.put("code", "-1");//不是行政人员
			}
			out = response.getWriter();
			out.print(input);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取下载的文件名
	 * @return
	 * lhyan3
	 * 2014年7月17日
	 */
	public String getDownloadFileName() {
		String downFileName = Configure.getProperty("AttendanceTemplateRealName");
		try {
			downFileName = new String(downFileName.getBytes("gb2312"), "ISO8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return downFileName;
	}

	/**
	 * 下载文件
	 * 
	 * @return lhyan3 2014年6月19日
	 */
	public String download() {
		String downfileMessage = "";
		try {
			String dir = NStrafFileUtils.getAttendanceTemplatePath();
			File file = new File(dir);
			inputStream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			downfileMessage = "服务器上不存在该文件,请联系管理员";
			e.printStackTrace();
			request.setAttribute("downfileMessage", downfileMessage);
			return "error";
		}catch (Exception e) {
			downfileMessage = "其它未知错误,请联系管理员";
			request.setAttribute("downfileMessage", downfileMessage);
			e.printStackTrace();
			return "error";
		}
		request.setAttribute("downfileMessage", downfileMessage);
		return "download";
	}
	
	
	public String saveUserId(){
		String outcard = request.getParameter("nousercard");
		String nousername = request.getParameter("nousername");
		String[] outcards = outcard.split(",");
		String[] usernames = nousername.split(",");
		String[] userids = new String[outcards.length];
		for(int i=0;i<outcards.length;i++){
			String userid = request.getParameter("userid"+i);
			userids[i]=userid;
		}
		String result = signRecordService.saveUserIdForOutCard(userids, outcards,usernames);
		if(!"".equals(result)){
			MsgBox msgBox = new MsgBox(request, "因用户:"+result+",找不到对应OA账号，其数据无法保存");
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			return "msgBox";
		}
		return "importOutData";
	}
	
	/**
	 * 判断输入的OA账号是否有对应的外派工号了
	 * @return
	 */
	public String flagHasUserid(){
		String userid = request.getParameter("user_id");
		String[] userids = userid.split(",");
		String hasUserid = "";//oa账号有对应的外派工号
		for(int i=0;i<userids.length;i++){
			if(signRecordService.flagHasUserid(userids[i])){
				if(hasUserid.indexOf(userids[i])==-1){
					hasUserid+=","+userids[i];
				}
			}
		}
		if(hasUserid.length()>0){
			hasUserid=hasUserid.substring(1);
		}
		ajaxPrint("{\"hasUserid\":\"" + hasUserid + "\"}");
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
	/**
	 * 列出所有未导入数据的page
	 * @return
	 */
	public String noExportExcelDataPage(){
		try{
			UserModel userModel = (UserModel)request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
			String from = request.getParameter("form");
			String years = request.getParameter("year");
			String months = request.getParameter("month");
			String prjname = request.getParameter("prjname");
			if(prjname!=null && !"".equals(prjname)){
				prjname = URLDecoder.decode(prjname, "utf-8");
			}
			Date date = new Date();
			java.text.SimpleDateFormat sf = new java.text.SimpleDateFormat("yyyy-MM-dd");
			String currentDate = sf.format(date);
			String curYear = currentDate.split("-")[0];
			if(years==null || "".equals(years)){
				years=curYear;
			}
			String type = "";
			if(months==null || "".equals(months)){
				type = "all";
			}
			int pageNum = 1;
			int pageSize = 20;
			if (request.getParameter("pageNum") != null && request.getParameter("pageNum").length() > 0)
				pageNum = Integer.parseInt(request.getParameter("pageNum"));
			if (request.getParameter("pageSize") != null && request.getParameter("pageSize").length() > 0)
				pageSize = Integer.parseInt(request.getParameter("pageSize"));
			Page page = noAttendanceDataService.getPage(pageNum, pageSize, userModel, years, months, type,prjname);
			request.setAttribute("currPage", page);
			List<Object> list = (List<Object>)page.getQueryResult();
			if (from != null && from.equals("refresh")) {
				for (int i = 0; i < list.size(); i++) {
					NoAttendanceData noAttendanceData=(NoAttendanceData)list.get(i);
				}
				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf(page.getPageCount()));
				map.put("recordCount", String.valueOf(page.getRecordCount()));
				JSONUtil jsonUtil = new JSONUtil("cn.grgbanking.feeltm.signrecord.domain.NoAttendanceData");
				JSONArray jsonObj = jsonUtil.toJSON(list, map);
				JSONObject input = new JSONObject();
				input.put("pageCount", String.valueOf(page.getPageCount()));
				input.put("recordCount", String.valueOf(page.getRecordCount()));
				input.put("jsonObj", jsonObj);								
				PrintWriter out = response.getWriter();
				out.print(input);
				return null;
			} else {
				ActionContext.getContext().put("noDataList", list);
				Map attendancePrjname = BusnDataDir.getMapKeyValue("hrManager.attendancePrjname");
				request.setAttribute("attendancePrjname", attendancePrjname);
				return "noDataList";
			}
		}catch(Exception e){
			SysLog.error(request, "error in (SignRecordAction.java-recordBind())");
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			return "msgBox";
		}
	}
	
	/**
	 * 定时同步移动签到数据到考勤表中
	 */
	public void synchronizateSignToCard(){
		signRecordService.synchronizateSignToCard();
	}
	
	/**
	 * 判断签到日期是否超过锁定日期
	 * true:可以修改
	 * false:不可以修改
	 * @return
	 */
	public String flagOuttime(){
		String str = request.getParameter("signtime");
		boolean flag = true;
		if("".equals(str)){
			str = DateUtil.getDateString(new Date());
			flag = DateUtil.flagSigntimeTimeOut(DateUtil.stringToDate(str, "yyyy-MM-dd"));
		}
		flag = DateUtil.flagSigntimeTimeOut(DateUtil.stringToDate(str, "yyyy-MM-dd"));
		ajaxPrint("{\"flag\":\"" + flag + "\"}");
		return null;
	}
	
	/**
	 * 列出所有考勤数据
	 * @return
	 */
	public String listCardAll(){
		UserModel userModel = (UserModel)request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		try{
			int pagenum = 1;
			int pagesize =20;
			String from = request.getParameter("from");
			String deptName = request.getParameter("deptName");
			String grpName = request.getParameter("grpName");
			String userId = request.getParameter("userId");
			String signEndTime = null;
			String signTime = null;
			if (request.getParameter("pageNum") != null && request.getParameter("pageNum").length() > 0)
				pagenum = Integer.parseInt(request.getParameter("pageNum"));
			if (request.getParameter("pageSize") != null && request.getParameter("pageSize").length() > 0)
				pagesize = Integer.parseInt(request.getParameter("pageSize"));
			if(request.getParameter("signEndTime")!=null && request.getParameter("signEndTime").length()>0)
				signEndTime = request.getParameter("signEndTime");
			if(request.getParameter("signTime")!=null && request.getParameter("signTime").length()>0)
				signTime = request.getParameter("signTime");
			Page page = cardRecordService.getPage(pagenum, pagesize, userId, deptName, grpName, signTime, signEndTime, userModel);
			request.setAttribute("currPage", page);
			List<Object> list = (List<Object>)page.getQueryResult();
			if (from != null && from.equals("refresh")) {
				for (int i = 0; i < list.size(); i++) {
					CardRecord card=(CardRecord)list.get(i);
				}
				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf(page.getPageCount()));
				map.put("recordCount", String.valueOf(page.getRecordCount()));
				JSONUtil jsonUtil = new JSONUtil("cn.grgbanking.feeltm.cardRecord.domain.CardRecord");
				JSONArray jsonObj = jsonUtil.toJSON(list, map);
				JSONObject input = new JSONObject();
				input.put("pageCount", String.valueOf(page.getPageCount()));
				input.put("recordCount", String.valueOf(page.getRecordCount()));
				input.put("jsonObj", jsonObj);								
				PrintWriter out = response.getWriter();
				out.print(input);
				return null;
			}else {
				ActionContext.getContext().put("cardlist", list);
				return "cardQuery";
			}
		}catch(Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "cardQuery";
	}
	
	/**
	 * 跳转到批量审核页面
	 * 批量审核前一个月至当前日期的记录
	 * @return
	 * @throws Exception
	 */
	public String toapprovalmoresign() throws Exception{
		try{
			UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
			//判断当前用户的权限，如果是部门经理或项目经理，则有审批的权限，否则无
			//如果当前用户既是项目经理，又是部门经理，首先判断是否有项目项目经理审核的数据，在判断是否有部门经理审核的数据
			String status = "";
			if(UserRoleConfig.getGrpList(userModel).contains("groupManager") && UserRoleConfig.getGrpList(userModel).contains("deptManager")){//项目+部门
				List list = signRecordService.getApprovalRecord(userModel.getUserid(), "2");
				if(list==null || list.size()==0){
					list = signRecordService.getApprovalRecord(userModel.getUserid(), "3");
					if(list==null || list.size()==0){
						MsgBox msgBox = new MsgBox(request,"你没有审核数据的权限");
						msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
						return "msgBox";
					}else{
						status="3";
					}
				}else{
					status="2";
				}
			}else if(UserRoleConfig.getGrpList(userModel).contains("deptManager")){//部门经理审批
				status="3";
			}else if(UserRoleConfig.getGrpList(userModel).contains("groupManager")){//项目经理审批
				status="2";
			}else{//其他人员
				MsgBox msgBox = new MsgBox(request,"你没有审核数据的权限");
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}
			//根据当前用户角色及状态，获取批量审核记录
			Page page = signRecordService.getApprovalMoreSign(1, 1000, userModel,status);
			List list = page.getQueryResult();
			//判断是否有需要进行审批的记录
			if(list==null || list.size()==0){
				MsgBox msgBox = new MsgBox(request,"暂时没有要审批的记录");
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}
			ActionContext.getContext().put("signlist", list);
			//获取审核人员的数据
			List<SysUser> approveList = new ArrayList<SysUser>();
			String deptid = staffInfoService.getDeptkeyByValue(signRecord.getDeptName());
			approveList = UserRoleConfig.getUserRoleInfoByUser(userModel,deptid);
			request.setAttribute("approvePerson", approveList);
			request.setAttribute("appstatus", status);
			//根据状态显示部门经理信息及项目经理信息
			for(Object o:list){
				SignRecord record = (SignRecord) o;
				if("3".equals(status)){//部门经理批量审批时，列表上列出上一环节的项目经理信息
					String username = approvalService.getGroupmanageInfo(record.getId());
					if("".equals(username)){
						record.setSignwhere(record.getUsername());
					}else{
						record.setSignwhere(username);
					}
				}else if("2".equals(status)){//项目经理批量审批，列表上列出下一环节的部门经理信息
					String people = signRecordService.getDeptApprovePeople(record.getDeptName());//返回值 cxjin,陈晓金
					if(people.split(",").length>1){
						record.setSignwhere(people.split(",")[1]);
					}else if("".equals(people)){
						record.setSignwhere("");
					}
				}
			}
//			//获取审核记录信息，暂时去掉
//			String getid = "";
//			for(Object o:list){
//				SignRecord record = (SignRecord) o;
//				getid += ",'" + record.getId()+"'";
//			}
//			if(!"".equals(getid))
//				getid = getid.substring(1);
//			this.setMoreRecordToPage(getid);
			return "approvalMoreSignPage";
		}catch(Exception e){
			SysLog.error(request, "error in (SignRecordAction.java-recordBind())");
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			return "msgBox";
		}
	}
	
	/**
	 * 批量处理审核数据
	 * @return
	 * @throws Exception
	 */
	public String approvalmoresign()throws Exception{
		MsgBox msgBox = null;
		try{
			UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
			String option = request.getParameter("signResult");//审核信息
			String approveStatus = request.getParameter("approveStatus");//审核结果
			String noDealId = request.getParameter("noDealId");//列表上去掉的记录ID
			noDealId = noDealId==null?"":noDealId;
			//根据当前用户角色及状态，获取批量审核记录
			String status = "";
			if(UserRoleConfig.getGrpList(userModel).contains("groupManager") && UserRoleConfig.getGrpList(userModel).contains("deptManager")){//项目+部门
				List list = signRecordService.getApprovalRecord(userModel.getUserid(), "2");
				if(list==null || list.size()==0){
					list = signRecordService.getApprovalRecord(userModel.getUserid(), "3");
					if(list==null || list.size()==0){
						msgBox = new MsgBox(request,"你没有审核数据的权限");
						msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
						return "msgBox";
					}else{
						status="3";
					}
				}else{
					status="2";
				}
			}else if(UserRoleConfig.getGrpList(userModel).contains("deptManager")){//部门经理审批
				status="3";
			}else if(UserRoleConfig.getGrpList(userModel).contains("groupManager")){//项目经理审批
				status="2";
			}
			List list = signRecordService.getApprovalRecord(userModel.getUserid(), status);
			//判断是否有需要进行审批的记录
			if(list==null || list.size()==0){
				msgBox = new MsgBox(request,"暂时没有要审批的记录");
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}
			String currentDate = DateUtil.getTimeString(new Date());//批量审核时间
			int successCount = 0;//成功审核多少条记录数
			String noDeptApprover = "";//无部门经理的部门
			int outDealTime = 0; //超出处理时间的记录数
			for(Object o:list){
				SignRecord record = (SignRecord) o;
				//判断id是否在不需要处理的id中
				String getid = record.getId();
				if(noDealId.indexOf(getid)!=-1){
					continue;
				}
				//判断当前记录是否超过了正常处理时间
				String signtime = DateUtil.getDateString(record.getSignTime());
				boolean flag = DateUtil.flagSigntimeTimeOut(DateUtil.stringToDate(signtime, "yyyy-MM-dd"));
				if(!flag){
					outDealTime++;
				}
				//判断该部门是否有部门审核人
				String people = signRecordService.getDeptApprovePeople(record.getDeptName());
				if("".equals(people)){
					noDeptApprover += "、" + record.getDeptName();
					continue;
				}
				if(people.split(",").length>1){
					record.setApprovePerson(people.split(",")[0]);
				}
				//批量处理签到数据
				record.setApproveStatus(approveStatus);
				signRecordService.approvalmoresign(userModel,record,option,approvalService,currentDate);
				successCount++;
			}
			String message = "";
			if(successCount>0){
				message = "成功审核"+successCount+"条记录";
			}
			if(outDealTime>0){
				if(message.equals("")){
					message = "共审核"+list.size()+"条记录，其中有"+outDealTime+"条记录已经超过处理时间";
				}else{
					message += "；其中有"+outDealTime+"条记录已经超过处理时间";
				}
			}
			if(!"".equals(noDeptApprover)){
				if(message.equals("")){
					message = "共审核"+list.size()+"条记录，其中"+noDeptApprover.substring(1)+"未指定部门审核人";
				}else{
					message += "；其中"+noDeptApprover+"未指定部门审核人";
				}
			}
			if("".equals(message)){
				message = "未审核任何数据";
			}
			msgBox = new MsgBox(request,message);
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		} catch (Exception e) {
			SysLog.error(request, "error in(SginRecordAction-approvalsign())");
			SysLog.error(e);
			msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
		}
		return "msgBox";
	}
	
	/**
	 * 获取多条记录的审批信息
	 * @param recordid
	 */
	private void setMoreRecordToPage(String recordid) {
		List list = approvalService.getMoreRecordByName(recordid);
		String approveRecord = "";
		for (int i =0;i<list.size();i++) {
			Object[] record = (Object[])list.get(i);
			String result = "[审核通过]";
			if(record[2].toString().equals("5"))
				result = "[审核不通过]";
			String opinion = "同意";
			if(record[1]!=null){
				opinion = record[1].toString();
			}
			String timedate = record[4].toString();
			approveRecord += "<font style='font-weight: bold'>" + result+"</font> "+record[3].toString()+"("
						+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(DateUtil.stringToDate(timedate, "yyyy-MM-dd HH:mm:ss"))+"): "
						+ (opinion==null?"":opinion) +"\n<br>";
		}
		request.setAttribute("approveRecord", approveRecord);
	}
	public SignRecordService getSignRecordService() {
		return signRecordService;
	}

	public void setSignRecordService(SignRecordService signRecordService) {
		this.signRecordService = signRecordService;
	}


	public List<SignRecord> getList() {
		return list;
	}


	public void setList(List<SignRecord> list) {
		this.list = list;
	}


	public SignRecord getSignRecord() {
		return signRecord;
	}


	public void setSignRecord(SignRecord signRecord) {
		this.signRecord = signRecord;
	}


	public String getPageSize() {
		return pageSize;
	}


	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}


	public String getPageNum() {
		return pageNum;
	}


	public void setPageNum(String pageNum) {
		this.pageNum = pageNum;
	}

	public SignRecord getModel() {
		
		return signRecord;
	}
	public String getForm() {
		return form;
	}
	public void setForm(String form) {
		this.form = form;
	}
	
	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public ApprovalService getApprovalService() {
		return approvalService;
	}

	public void setApprovalService(ApprovalService approvalService) {
		this.approvalService = approvalService;
	}

	public CardRecordService getCardRecordService() {
		return cardRecordService;
	}

	public void setCardRecordService(CardRecordService cardRecordService) {
		this.cardRecordService = cardRecordService;
	}
	
}
