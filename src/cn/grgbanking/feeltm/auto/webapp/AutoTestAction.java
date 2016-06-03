package cn.grgbanking.feeltm.auto.webapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionContext;

import cn.grgbanking.feeltm.auto.domain.AutoListInfo;
import cn.grgbanking.feeltm.auto.service.AutoService;
import cn.grgbanking.feeltm.auto.service.ExecInfoService;
import cn.grgbanking.feeltm.config.BusnDataDir;
import cn.grgbanking.feeltm.domain.testsys.Behavior;
import cn.grgbanking.feeltm.domain.testsys.ExecInfo;
import cn.grgbanking.feeltm.domain.testsys.Overtime;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.overtime.service.OvertimeService;
import cn.grgbanking.feeltm.report.domain.ReportDayInfo;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.feeltm.util.Random32;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;
import flex.messaging.io.ArrayList;

public class AutoTestAction extends BaseAction{
	private AutoService autoService;
	
	private String dailyBuildIP;
	private String execIP;
	private String prjName;
	private String standard;   //多选,用@区分  NotWosa Wosa
	private String testType;   //同上  1.2.3.4
	private String machineNo;  //同上
	private String versionNo;  
	private String qc_username;
	private String qc_password;
	private String allScripts;
	private String failScripts;
	private String rankScripts;
	private String file;
	private AutoListInfo info;
	
	private String start;
	private String end;
	private ExecInfo execInfo = new ExecInfo();
	private ExecInfoService execInfoService;
	
	
	/**
	 * 2 1,2 H22,H68 CATalyst3.0R4 NotWosa Test 123456 145
	     该报文表示执行以下类型的自动化测试：测试机型为H22和H68，测试项目为CATalyst3.0R4，测试的标准为非Wosa，QC用户名和密码分别为Test和123456，测试版本为145
	 * @return 
	 */
	

	public String execute() throws Exception{
		if(execIP!=null){
			StringBuffer sb=new StringBuffer();
			char[] first=init(64,"2");
			char[]	acType=init(64,testType.substring(0, testType.length()-1));		// 测试类型，大于4表示其他消息
			char[]	acMachine=init(64,machineNo.substring(0,machineNo.length()-1));	// 机器型号
			char[]	acName=init(64,qc_username);		// QC用户名
			char[]	acPassword=init(64,qc_password);	// QC密码
			char[] acVer =init(64,versionNo);		// 报文版本号
			SimpleDateFormat fo=new SimpleDateFormat("yyyyMMdd HH:mm:dd");
			String ds=fo.format(new Date());
			char[]	acDate=init(64,ds);		// 日期时间
			char[]	acProName=init(64,prjName);	// 项目名称
			char[]	acWosa=init(64,standard.substring(0, standard.length()-1));		// 是否Wosa
			char[]	acReserve=new char[128];	// 保留
			sb.append(String.valueOf(first)).append(String.valueOf(acType))
				.append(String.valueOf(acMachine)).append(String.valueOf(acName))
				.append(String.valueOf(acPassword)).append(String.valueOf(acVer))
				.append(String.valueOf(acDate)).append(String.valueOf(acProName))
				.append(String.valueOf(acWosa))
				.append(String.valueOf(acReserve));
			System.out.println(sb.toString());
			response.setHeader( "Cache-Control", "no-cache" );
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/javascript;charset=UTF-8");
			Socket socket=null;
			PrintWriter pw=null;
			BufferedReader reader=null;
			try {
				socket = new Socket(execIP, 9999);
				pw=new PrintWriter(socket.getOutputStream());
				reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
				pw.print(sb.toString());
				pw.flush();
				String status=reader.readLine().substring(0, 1);
				System.out.println("status:  ------------------------- "+status);
				PrintWriter pw1=response.getWriter();
				try {
					pw1.print(status);
					pw1.flush();
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					if(pw1!=null){
						pw1.close();
					}
				}
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				if(pw!=null){
					pw.close();
				}
				if(reader!=null){
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if(socket!=null){
					try {
						socket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}		
		
		return "";
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
			Page page = autoService.getPage(start,end, pageNum, pageSize, prjName);
			request.setAttribute("currPage", page);
			List<Object> list = (List<Object>)page.getQueryResult();
			if (from != null && from.equals("refresh")) {
				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf(page.getPageCount()));
				map.put("recordCount", String.valueOf(page.getRecordCount()));
				JSONUtil jsonUtil = new JSONUtil(
						"cn.grgbanking.feeltm.auto.domain.AutoListInfo");
				JSONArray jsonObj = jsonUtil.toJSON(list, map);
				JSONObject input = new JSONObject();
				input.put("pageCount", String.valueOf(page.getPageCount()));
				input.put("recordCount", String.valueOf(page.getRecordCount()));
				input.put("jsonObj", jsonObj);								
				PrintWriter out = response.getWriter();
				out.print(input);
				return "";
			} else {
				ActionContext.getContext().put("autoList", list);
				return "query";
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "query";
	}
	
	public String queryPro() throws Exception{
		response.setHeader( "Cache-Control", "no-cache" );
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/javascript;charset=UTF-8");
		String groupName=request.getParameter("groupName");
		PrintWriter out=response.getWriter();
		
		Map systemConfig = BusnDataDir.getMap("systemConfig.autoProject");
		Set<String> ks = systemConfig.keySet();
		String szTmp = "{\"rows\":[";
		for(String uid:ks)
		{
			String[] uids = uid.split("\\|");
			szTmp = szTmp + "{\"pn\":\"" + uids[0].trim() + "\",";
			szTmp = szTmp + "\"ip\":\"" + uids[1].trim()+ "\",";
			szTmp = szTmp + "\"port\":\"" + uids[2].trim()+ "\",";
			szTmp = szTmp + "\"fileformat\":\"" + uids[3].trim() + "\"},";
		}
		szTmp = szTmp.substring(0, szTmp.length()-1);
		
		szTmp += "]}";
	//	JSONUtil jsonUtil=new JSONUtil("java.lang.String");
		//JSONArray jsonArray=jsonUtil.toJSON(list, null);
		//JSONArray jsonArray=new JSONArray(szTmp);
		out.print(szTmp);
		out.flush();
		out.close();
		return null;
	}
	
	public String execTestQueue() throws Exception{
		String szIP = "";
		String szPort = "";
		String szVersion = "";
		String szProName = "";
		
		response.setHeader( "Cache-Control", "no-cache" );
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/javascript;charset=UTF-8");
		PrintWriter out=response.getWriter();
		
		
		if (request.getParameter("execIP") != null
				&& request.getParameter("execIP").length() > 0)
		{
			szIP = request.getParameter("execIP");
		}
		else
		{
			out.print(0);
			out.flush();
			out.close();
			return null;
		}
		if (request.getParameter("execPort") != null
				&& request.getParameter("execPort").length() > 0)
		{
			szPort = request.getParameter("execPort");
		}
		else
		{
			out.print(0);
			out.flush();
			out.close();
			return null;
		}
		if (request.getParameter("version") == null)
		{
			szVersion = "";
		}
		else
		{
			szVersion = request.getParameter("version");
		}
		
		if (request.getParameter("proName") == null)
		{
			szProName = "";
		}
		else
		{
			szProName = request.getParameter("proName");
		}
		
		// add to autotest list
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		execInfo.setId(uuid());
		execInfo.setNetIP(szIP);
		execInfo.setNetPort(szPort);
		execInfo.setVersionNo(szVersion);
		execInfo.setUsername(userModel.getUsername());
		execInfo.setMachineNo("");
		execInfo.setPrjName(szProName);
		execInfo.setTestType("自动化测试");
		SimpleDateFormat fo=new SimpleDateFormat("yyyy-MM-dd HH:mm:dd");
		//String ds=fo.format(new Date());
		execInfo.setStartTime(new Date());
		execInfo.setExecTime(new Date());
		execInfo.setStatus("排队等待");
		execInfo.setAllScripts("0");
		execInfo.setFailScripts("0");
		execInfo.setRankScripts("0%");
		execInfoService.add(execInfo);
		out.print(1);
		out.flush();
		out.close();
		
		return null;
	}
	
	public String execTest() throws Exception{
		String szIP = "";
		String szPort = "";
		String szVersion = "";
		String szProName = "";
		int iData = 10;
		
		
		response.setHeader( "Cache-Control", "no-cache" );
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/javascript;charset=UTF-8");
		PrintWriter out=response.getWriter();
		
		
		if (request.getParameter("execIP") != null
				&& request.getParameter("execIP").length() > 0)
		{
			szIP = request.getParameter("execIP");
		}
		else
		{
			iData = 10;
			out.print(iData);
			out.flush();
			out.close();
			return null;
		}
		if (request.getParameter("execPort") != null
				&& request.getParameter("execPort").length() > 0)
		{
			szPort = request.getParameter("execPort");
		}
		else
		{
			iData = 10;
			out.print(iData);
			out.flush();
			out.close();
			return null;
		}
		if (request.getParameter("version") == null)
		{
			szVersion = "";
		}
		else
		{
			szVersion = request.getParameter("version");
		}
		
		if (request.getParameter("proName") == null)
		{
			szProName = "";
		}
		else
		{
			szProName = request.getParameter("proName");
		}
		String uid = uuid();
		String szExecParam = "E:\\jdk\\AutoClt.exe  ";
		szExecParam = szExecParam + szIP + " " + szPort + " " + szVersion + " " + uid;
		
		// add to autotest list
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		execInfo.setId(uid);
		execInfo.setNetIP(szIP);
		execInfo.setNetPort(szPort);
		execInfo.setVersionNo(szVersion);
		execInfo.setUsername(userModel.getUsername());
		execInfo.setMachineNo("");
		execInfo.setPrjName(szProName);
		execInfo.setTestType("自动化测试");
		SimpleDateFormat fo=new SimpleDateFormat("yyyy-MM-dd HH:mm:dd");
		//String ds=fo.format(new Date());
		execInfo.setStartTime(new Date());
		execInfo.setExecTime(new Date());
		execInfo.setStatus("正在运行");
		execInfo.setAllScripts("0");
		execInfo.setFailScripts("0");
		execInfo.setRankScripts("0%");
		
		Runtime rt = Runtime.getRuntime();
		Process proc = rt.exec(szExecParam);
		InputStream stderr = proc.getErrorStream();
		InputStreamReader isr = new InputStreamReader(stderr);
		BufferedReader br = new BufferedReader(isr);
		String line = null;
		while ( (line = br.readLine()) != null)
		{
			System.out.println(line);
		}
		iData = proc.waitFor();
		System.out.println("Process exitValue: " + iData);
		if(iData == 0)
		{
			execInfoService.add(execInfo);
		}
		out.print(iData);
		out.flush();
		out.close();
		br.close();
		
		return null;
	}
	
	public String uuid()
	{
		Date dd = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
		String tt = sdf.format(dd);
		tt = tt + Math.random();
		String random32 = Random32.MD5(tt);
		return random32;
	}
	
	public String detail(){
		String id=request.getParameter("id");
		info=autoService.getInfo(id);
		return "detail";
	}
	
	public String download(){
		String id=request.getParameter("id");
		file=autoService.getFile(id);
		return "download";
	}
	
	public String delete() throws Exception {
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		String loginName=userModel.getUsername();
		try {
			String ids = request.getParameter("ids");
			System.out.println("ids: "+ids);
			int iCount = 0;
			String[] sids = ids.split(",");
			ExecInfo otObj = execInfoService.getExecInfoById(ids);
			String curName = otObj.getUsername();
			if(!loginName.equals(curName)){
				MsgBox msgBox = new MsgBox(request,
						getText("operInfoform.updatefaile",new String[]{"您不能操作其他用户的数据"}));
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
					ExecInfo temp = execInfoService.getExecInfoById(sids[i]);
					execInfoService.delete(temp);
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

	public String show(){
		return "show";
	}
	
	public String showNew(){
		return "showNew";
	}
	
	public String list(){
		return "list";
	}


	public String getDailyBuildIP() {
		return dailyBuildIP;
	}



	public void setDailyBuildIP(String dailyBuildIP) {
		this.dailyBuildIP = dailyBuildIP;
	}



	public String getExecIP() {
		return execIP;
	}



	public void setExecIP(String execIP) {
		this.execIP = execIP;
	}



	public String getPrjName() {
		return prjName;
	}



	public void setPrjName(String prjName) {
		this.prjName = prjName;
	}



	public String getStandard() {
		return standard;
	}



	public void setStandard(String standard) {
		this.standard = standard;
	}



	public String getTestType() {
		return testType;
	}



	public void setTestType(String testType) {
		this.testType = testType;
	}



	public String getMachineNo() {
		return machineNo;
	}



	public void setMachineNo(String machineNo) {
		this.machineNo = machineNo;
	}



	public String getVersionNo() {
		return versionNo;
	}



	public void setVersionNo(String versionNo) {
		this.versionNo = versionNo;
	}



	public String getQc_username() {
		return qc_username;
	}



	public void setQc_username(String qcUsername) {
		qc_username = qcUsername;
	}



	public String getQc_password() {
		return qc_password;
	}



	public void setQc_password(String qcPassword) {
		qc_password = qcPassword;
	}

	public AutoService getAutoService() {
		return autoService;
	}

	public ExecInfo getExecInfo() {
		return execInfo;
	}

	public void setExecInfo(ExecInfo execInfo) {
		this.execInfo = execInfo;
	}

	public void setAutoService(AutoService autoService) {
		this.autoService = autoService;
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
	
	

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public void setEnd(String end) {
		this.end = end;
	}
	
	
	public AutoListInfo getInfo() {
		return info;
	}

	public void setInfo(AutoListInfo info) {
		this.info = info;
	}

	//	private void process() throws Exception{
//		Socket socket = new Socket("10.1.89.27", 9999);
//		PrintWriter pw=new PrintWriter(socket.getOutputStream());
//		//String str="2 1 H22 CATalyst3.0R4 NotWosa ckai 1 123";
//		char[] first=init(64,"2");
//		char[]	acType=init(64,"1");		// 测试类型，大于4表示其他消息
//		char[]	acMachine=init(64,"H22");	// 机器型号
//		char[]	acName=init(64,"ckai");		// QC用户名
//		char[]	acPassword=init(64,"1");	// QC密码
//		char[] acVer =init(64,"123");		// 报文版本号
//		SimpleDateFormat fo=new SimpleDateFormat("yyyyMMdd HH:mm:dd");
//		String ds=fo.format(new Date());
//		System.out.print("date:"+ds);
//		char[]	acDate=init(64,"20110203 18:00:25");		// 日期时间
//		char[]	acProName=init(64,"CATalyst3.0R4");	// 项目名称
//		//char[]	acBuild=init(64,"10.1.3.195");	// build号
//		char[]	acWosa=init(64,"Wosa");		// 是否Wosa
//		char[]	acReserve=new char[128];	// 保留	 
//		StringBuffer sb=new StringBuffer();
//		sb.append(String.valueOf(first)).append(String.valueOf(acType))
//			.append(String.valueOf(acMachine)).append(String.valueOf(acName))
//			.append(String.valueOf(acPassword)).append(String.valueOf(acVer))
//			.append(String.valueOf(acDate)).append(String.valueOf(acProName))
//			.append(String.valueOf(acWosa))
//			.append(String.valueOf(acReserve));
//		System.out.println(sb.toString());
//		BufferedReader reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
//		pw.print(sb.toString());
//		pw.flush();
//		System.out.println("................");
//		// 为正常,1为服务器忙,2为参数无效,3为登陆失败,4为客户端版本低,9为无法连接
//		String status=reader.readLine();
//		System.out.print("status:"+status);
//	}
	private char[] init(int size,String col){
		int len=col.length();
		char[] c=new char[size];
		for(int i=0;i<len;i++){
			c[i]=col.charAt(i);
		}
		return c;
	}

	public ExecInfoService getExecInfoService() {
		return execInfoService;
	}

	public void setExecInfoService(ExecInfoService execInfoService) {
		this.execInfoService = execInfoService;
	}

	public String getAllScripts() {
		return allScripts;
	}

	public void setAllScripts(String allScripts) {
		this.allScripts = allScripts;
	}

	public String getFailScripts() {
		return failScripts;
	}

	public void setFailScripts(String failScripts) {
		this.failScripts = failScripts;
	}

	public String getRankScripts() {
		return rankScripts;
	}

	public void setRankScripts(String rankScripts) {
		this.rankScripts = rankScripts;
	}
	

	
	
}
