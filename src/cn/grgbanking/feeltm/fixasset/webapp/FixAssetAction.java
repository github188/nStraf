package cn.grgbanking.feeltm.fixasset.webapp;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

import cn.grgbanking.feeltm.config.BusnDataDir;
import cn.grgbanking.feeltm.domain.SysUser;
import cn.grgbanking.feeltm.fixasset.domain.FixAsset;
import cn.grgbanking.feeltm.fixasset.domain.FixAssetUse;
import cn.grgbanking.feeltm.fixasset.service.FixAssetService;
import cn.grgbanking.feeltm.fixasset.service.FixAssetUseService;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.project.domain.Project;
import cn.grgbanking.feeltm.staff.service.StaffInfoService;
import cn.grgbanking.feeltm.um.service.SysUserGroupService;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.DateUtil;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;

import com.opensymphony.xwork2.ActionContext;

public class FixAssetAction extends BaseAction{
	private FixAssetService fixAssetService;
	private FixAsset fixAsset;
	private FixAssetUseService fixAssetUseService;
	private StaffInfoService staffInfoService;
	private SysUserGroupService sysUserGroupService;
	
	
	public void refresh(){
		try {
			query();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public String query() throws Exception{
		try {
			UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
			String from = request.getParameter("from");
			String no=request.getParameter("no");
			String type=request.getParameter("type");
			String name=request.getParameter("name");
			String status=request.getParameter("status");
			String inman=request.getParameter("inman");
			String useman=request.getParameter("useman");
			String pageNums = request.getParameter("pageNum");
			String pageSizes = request.getParameter("pageSize");
			
			int pageNum = 1;
			int pageSize = 20;
			if (pageNums != null && pageNums.length() > 0)
				pageNum = Integer.parseInt(pageNums);
			if ( pageSizes!= null && pageSizes.length() > 0)
				pageSize = Integer.parseInt(pageSizes);
			Page page = fixAssetService.getPage(pageNum, pageSize,type,no,name,status,inman,useman,userModel);
			request.setAttribute("currPage", page);
			List<Object> list = (List<Object>)page.getQueryResult();
			if (from != null && from.equals("refresh")) {
				for (int i = 0; i < list.size(); i++) {
					FixAsset overtime=(FixAsset)list.get(i);
				}
				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf(page.getPageCount()));
				map.put("recordCount", String.valueOf(page.getRecordCount()));
				JSONUtil jsonUtil = new JSONUtil("cn.grgbanking.feeltm.fixasset.domain.FixAsset");
				JSONArray jsonObj = jsonUtil.toJSON(list, map);
				JSONObject input = new JSONObject();
				input.put("pageCount", String.valueOf(page.getPageCount()));
				input.put("recordCount", String.valueOf(page.getRecordCount()));
				input.put("jsonObj", jsonObj);								
				PrintWriter out = response.getWriter();
				out.print(input);
				return "";
			} else {
				ActionContext.getContext().put("fixassetList", list);
				return "query";
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "query";
	}
	public String add(){
		return "add";
	}
	public String edit(){
		String ids = request.getParameter("ids");
		fixAsset = fixAssetService.getFixAssetById(ids);
		List<FixAssetUse> records = fixAssetUseService.getRecordById(ids);
		SimpleDateFormat fo=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String record = "";
		for(FixAssetUse r:records){
			if("领用".equals(r.getType())){
				record += fo.format(r.getDate())+"  "+r.getUsername()+"  领用    "+"领用日期："+r.getUsedate()+"   领用原因："+r.getUsereson();
				record += "\n";
			}else{
				record += fo.format(r.getDate())+"  "+r.getUsername()+"  归还    "+"归还日期："+r.getFactdate();
				record += "\n";
			}
		}
		request.setAttribute("record", record);
		return "edit";
	}
	public String save() throws Exception{
		try{
			int count = fixAssetService.checkFixAssetNo(fixAsset.getNo(), "");
			if(count>0){
				MsgBox msgBox = new MsgBox(request, "已经存在该固定资产编号");
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}
			fixAssetService.add(fixAsset);
			if(!"".equals(fixAsset.getId()) && "1".equals(fixAsset.getStatus())){
				FixAssetUse fixAssetUse = new FixAssetUse();
				fixAssetUse.setDate(new Date());
				fixAssetUse.setExpactdate(fixAsset.getExpectdate());
				fixAssetUse.setFactdate(fixAsset.getFactdate());
				fixAssetUse.setFixid(fixAsset.getId());
				fixAssetUse.setType("领用");
				fixAssetUse.setUsereson(fixAsset.getUsereason());
				fixAssetUse.setUsername(fixAsset.getUseman());
				fixAssetUse.setUsedate(fixAsset.getUsedate());
				fixAssetUseService.add(fixAssetUse);
			}
			MsgBox msgBox = new MsgBox(request, "新增成功");
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}catch(Exception e){
			e.printStackTrace();
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request, "新增失败");
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}
	}
	public String update() throws Exception{
		try{
			int count = fixAssetService.checkFixAssetNo(fixAsset.getNo(), fixAsset.getId());
			if(count>0){
				MsgBox msgBox = new MsgBox(request, "已经存在该固定资产编号");
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}
			FixAsset fix = fixAssetService.getFixAssetById(fixAsset.getId());
			String type="";
			if("1".equals(fix.getStatus()) && "0".equals(fixAsset.getStatus())){
				type="归还";
			}else if("0".equals(fix.getStatus()) && "1".equals(fixAsset.getStatus())){
				type="领用";
			}else{
				type="";
			}
			fixAssetService.update(fixAsset);
			if(!"".equals(type)){
				FixAssetUse fixAssetUse = new FixAssetUse();
				fixAssetUse.setDate(new Date());
				fixAssetUse.setExpactdate(fixAsset.getExpectdate());
				fixAssetUse.setFactdate(fixAsset.getFactdate());
				fixAssetUse.setFixid(fixAsset.getId());
				fixAssetUse.setType(type);
				fixAssetUse.setUsereson(fixAsset.getUsereason());
				if(fix.getUseman()==null){
					fixAssetUse.setUsername(fixAsset.getUseman());
				}else{
					fixAssetUse.setUsername(fix.getUseman());
				}
				fixAssetUse.setUsedate(fixAsset.getUsedate());
				fixAssetUseService.add(fixAssetUse);
			}
			
			MsgBox msgBox = new MsgBox(request, "修改成功");
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}catch(Exception e){
			e.printStackTrace();
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request, "修改失败");
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}
	}
	public String delete() throws Exception{
		try{
			String[] ids = request.getParameter("ids").split(",");
			fixAssetService.delete(ids);
			List<FixAssetUse> list = fixAssetUseService.getRecordById(request.getParameter("ids").split(",")[0]);
			String id="";
			for(FixAssetUse record:list){
				id+=record.getId()+",";
			}
			fixAssetUseService.delete(id.split(","));
			MsgBox msgBox = new MsgBox(request, "删除成功");
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}catch(Exception e){
			e.printStackTrace();
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request, "删除失败");
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}
	}
	public String show() throws Exception {
		try {
			String ids = request.getParameter("ids");
			fixAsset = fixAssetService.getFixAssetById(ids);
			List<FixAssetUse> records = fixAssetUseService.getRecordById(ids);
			SimpleDateFormat fo=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String record = "";
			for(FixAssetUse r:records){
				if("领用".equals(r.getType())){
					record += fo.format(r.getDate())+"  "+r.getUsername()+"  领用    "+"领用日期："+r.getUsedate()+"   领用原因："+r.getUsereson();
					record += "\n";
				}else{
					record += fo.format(r.getDate())+"  "+r.getUsername()+"  归还    "+"归还日期："+r.getFactdate();
					record += "\n";
				}
			}
			request.setAttribute("record", record);
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "show";
	}
	public String select(){
		//部门与员工
		String id=request.getParameter("id");
		String type=request.getParameter("type");
		String see=request.getParameter("see");
		String hidden=request.getParameter("hidden");
		id=id==null?"":id;
		if(!"".equals(id)){
			fixAsset=fixAssetService.getFixAssetById(id);
			String inman=fixAsset.getInman();//资产管理员
			String useman=fixAsset.getUseman();//领用人
			request.setAttribute("inman", inman);
			request.setAttribute("useman", useman);
			request.setAttribute("type", type);
		}
		//部门与员工
		Map deptmaMap = BusnDataDir.getMapKeyValue("staffManager.department");
		Set<String> keySet = deptmaMap.keySet();
		Map<String,List<SysUser>> deptUser = new HashMap<String, List<SysUser>>();
		for(Iterator it = keySet.iterator();it.hasNext();){
			String key = (String) it.next();
			List<SysUser> users = staffInfoService.getStaffByDeptKey(key);
			deptUser.put(key, users);
		}
		request.setAttribute("deptUser", deptUser);
		request.setAttribute("deptmaMap", deptmaMap);
		//组别与员工
		List<Project> usrGroups = sysUserGroupService.getAllProjectList();
		Map<String, List<SysUser>> grpUser = new HashMap<String, List<SysUser>>();
		for(Project u:usrGroups){
			List<SysUser> users = sysUserGroupService.findUserByProject(u.getId());
			grpUser.put(u.getId(), users);
		}
		request.setAttribute("grpUser", grpUser);
		request.setAttribute("usrGroups", usrGroups);
		request.setAttribute("seename", see);
		request.setAttribute("seeid", hidden);
		return "select";
	}
	
	public String isValid(){
		String no = request.getParameter("no");
		String id = request.getParameter("id");
		String returnMsg = "false";
		int count = fixAssetService.checkFixAssetNo(no, id);
		if(count>0)
			returnMsg = "true";
		ajaxPrint("{\"returnMsg\":\"" + returnMsg + "\"}");
		return null;	
	}
	
	public String flagDateCompare(){
		String returnMsg = "";
		String status = request.getParameter("status");
		String id = request.getParameter("id");
		if(id.equals("")){
			if(status.equals("1")){
				Date indate = DateUtil.stringToDate(request.getParameter("indate").toString(),"yyyy-MM-dd");
				Date usedate = DateUtil.stringToDate(request.getParameter("usedate").toString(),"yyyy-MM-dd");
				Date expectdate = DateUtil.stringToDate(request.getParameter("expectdate").toString(),"yyyy-MM-dd");
				if(indate.after(usedate) || indate.after(expectdate)){
					returnMsg = "预计归还时间>=领用时间>=入库时间";
				}else if(usedate.after(expectdate)){
					returnMsg = "预计归还时间>=领用时间>=入库时间";
				}
			}
		}else{
			if(status.equals("1")){
				Date indate = DateUtil.stringToDate(request.getParameter("indate").toString(),"yyyy-MM-dd");
				Date usedate = DateUtil.stringToDate(request.getParameter("usedate").toString(),"yyyy-MM-dd");
				Date expectdate = DateUtil.stringToDate(request.getParameter("expectdate").toString(),"yyyy-MM-dd");
				if(indate.after(usedate) || indate.after(expectdate)){
					returnMsg = "预计归还时间>=领用时间>=入库时间";
				}else if(usedate.after(expectdate)){
					returnMsg = "预计归还时间>=领用时间>=入库时间";
				}
			}else if(status.equals("0")){
				Date indate = DateUtil.stringToDate(request.getParameter("indate").toString(),"yyyy-MM-dd");
				if("".equals(request.getParameter("factdate").toString())){
					returnMsg = "";
				}else{
					Date factdate = DateUtil.stringToDate(request.getParameter("factdate").toString(),"yyyy-MM-dd");
					if(indate.after(factdate)){
						returnMsg = "归还日期>=入库日期";
					}
				}
			}
		}
		ajaxPrint("{\"returnMsg\":\"" + returnMsg + "\"}");
		return null;
	}
	
	/**
	 * 前台ajax请求后台数据后，后台返回数据到前台
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
	
	public FixAssetService getFixAssetService() {
		return fixAssetService;
	}
	public void setFixAssetService(FixAssetService fixAssetService) {
		this.fixAssetService = fixAssetService;
	}
	public FixAsset getFixAsset() {
		return fixAsset;
	}
	public void setFixAsset(FixAsset fixAsset) {
		this.fixAsset = fixAsset;
	}
	public FixAssetUseService getFixAssetUseService() {
		return fixAssetUseService;
	}
	public void setFixAssetUseService(FixAssetUseService fixAssetUseService) {
		this.fixAssetUseService = fixAssetUseService;
	}
	public StaffInfoService getStaffInfoService() {
		return staffInfoService;
	}
	public void setStaffInfoService(StaffInfoService staffInfoService) {
		this.staffInfoService = staffInfoService;
	}
	public SysUserGroupService getSysUserGroupService() {
		return sysUserGroupService;
	}
	public void setSysUserGroupService(SysUserGroupService sysUserGroupService) {
		this.sysUserGroupService = sysUserGroupService;
	}
	
	
}
