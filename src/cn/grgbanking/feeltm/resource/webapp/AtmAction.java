package cn.grgbanking.feeltm.resource.webapp;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import cn.grgbanking.feeltm.domain.testsys.Atm;
import cn.grgbanking.feeltm.domain.testsys.DeviceConfig;
import cn.grgbanking.feeltm.domain.testsys.MediaUpdateLog;
import cn.grgbanking.feeltm.domain.testsys.MediaVersion;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.resource.service.AtmService;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;

import com.opensymphony.xwork2.ActionContext;

public class AtmAction extends BaseAction{
	@Autowired
	private AtmService atmService;

	//两项查询项   机器型号	设备编号
	private String type;
	private String seriaNum;

	private String id;  //此id为公用的id
	private Atm atm;  //atm概况
	private DeviceConfig config;   //设备配置
	private MediaVersion media;   //介质版本
	
	// 显示信息跳转标示 0为不显示 1为显示
	private String showFlag;
	
	private String mediaValue;   //传入的介质的实际值 a1,b2,c3等
	
	private List<MediaUpdateLog>   logs;
	
	public String add(){
		// 如果id不为空则为修改信息，显示修改信息内容
		if( StringUtils.isNotBlank( id ) )
		{
			setId( id );
			atm=atmService.getAtmById(id);
			if(atm.getSpace()!=null)
			atm.setSpace(atm.getSpace().replace("@@", "\r\n"));
		}
		if( "1".equals( showFlag ) )
		{
			return "show";
		}
		else
		{
			return "add";
		}
	}
	
	public String addDeviceConfig(){
		// 如果id不为空则为修改信息，显示修改信息内容
		if( StringUtils.isNotBlank( id ) )
		{
			setId( id );
			try{
			config=atmService.getDeviceConfigById(id);
			}catch(Exception e){
				config=new DeviceConfig(id);
			}
		}
		if( "1".equals( showFlag ) )
		{
			return "showConfig";
		}
		else
		{
			return "addConfig";
		}
	}
	
	public String addMediaVersion(){
		// 如果id不为空则为修改信息，显示修改信息内容
		if( StringUtils.isNotBlank( id ) )
		{
			setId( id );
			media=atmService.getMediaVersionById(id);
			if(media!=null&&media.getCoreVersion()!=null)
			media.setCoreVersion(media.getCoreVersion().replace("@@", "\r\n"));
		}
		if( "1".equals( showFlag ) )
		{
			return "showVersion";
		}
		else
		{
			return "addVersion";
		}
	}
	
	
	
	public String save() throws Exception {    //保存ATM概况的数据
		try {
			boolean flag = false;
			Atm oldAtm=null;
			if(id!=null&&!id.equals(""))
			oldAtm=atmService.getAtmById(id);
			
			int addOrModify=0;   //0代表新增，1代表修改
			atm.setSpace(atm.getSpace().replace("\r\n", "@@"));
			if(oldAtm==null){
				flag=atmService.addAtm(atm);
				setId(atm.getId());
			}else{
				//修改
				atm.setId(id);
				atm.setSpace(atm.getSpace().trim());
				flag=atmService.updateAtm(atm);
				addOrModify=1;
			}
			
			if (flag == true) {
				MsgBox msgBox = null;
				if(addOrModify==0){
					msgBox=new MsgBox(request, getText("add.ok"));
					this.addActionMessage(getText("add.ok"));
				}
				else{
					msgBox=new MsgBox(request, getText("operInfoform.updateok"));
					this.addActionMessage(getText("operInfoform.updateok"));
				}
				msgBox.setButtonType(MsgBox.BUTTON_OK);
				msgBox.setBackUrl( request.getContextPath()
						+ "/pages/atm/atminfo!add.action" );
				msgBox.setId( getId() );
				msgBox.setUrlParameters( request );
				System.out.println(msgBox.getBackUrl());
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
			msgBox.setId(id);
			msgBox.setUrlParameters( request );
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
	
	public String saveDeviceConfig() throws Exception {
		try {
			boolean flag = false;
			DeviceConfig oldObj=atmService.getDeviceConfigById(id);
			int addOrModify=0;   //0代表新增，1代表修改
			config.setId(id);
				
			if(oldObj==null){
				flag=atmService.addDeviceConfig(config);
				setId(config.getId());
			}else{
				//修改
				config.setId(id);
				flag=atmService.updateDeviceConfig(config);
				addOrModify=1;
			}
			
			if (flag == true) {
				MsgBox msgBox = null;
				if(addOrModify==0){
					msgBox=new MsgBox(request, getText("add.ok"));
					this.addActionMessage(getText("add.ok"));
				}
				else{
					msgBox=new MsgBox(request, getText("operInfoform.updateok"));
					this.addActionMessage(getText("operInfoform.updateok"));
				}
				msgBox.setButtonType(MsgBox.BUTTON_OK);
				msgBox.setBackUrl( request.getContextPath()
						+ "/pages/atm/atminfo!addDeviceConfig.action" );
				msgBox.setId( getId() );
				msgBox.setUrlParameters( request );
				System.out.println(msgBox.getBackUrl());
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
	
	public String saveVersion() throws Exception {
		try {
			boolean flag = false;
			MediaVersion oldObj=atmService.getMediaVersionById(id);
			int addOrModify=0;   //0代表新增，1代表修改
			media.setId(id);
			if(media.getCore().equals("h4")){  //hcm
				media.setCoreVersion(media.getCoreVersion().replace("\r\n", "@@"));
			}
			if(oldObj==null){
				flag=atmService.addMediaVersion(media);
				setId(media.getId());
			}else{
				//修改
				media.setId(id);
				flag=atmService.updateMediaVersion(media);
				addOrModify=1;
			}
			
			if (flag == true) {
				UserModel userModel = (UserModel) request.getSession().getAttribute(
						Constants.LOGIN_USER_KEY);
				atmService.addLogs(media, userModel.getUsername());
				MsgBox msgBox = null;
				if(addOrModify==0){
					msgBox=new MsgBox(request, getText("add.ok"));
					this.addActionMessage(getText("add.ok"));
				}
				else{
					msgBox=new MsgBox(request, getText("operInfoform.updateok"));
					this.addActionMessage(getText("operInfoform.updateok"));
				}
				msgBox.setButtonType(MsgBox.BUTTON_OK);
				msgBox.setBackUrl( request.getContextPath()
						+ "/pages/atm/atminfo!addMediaVersion.action" );
				msgBox.setId( getId() );
				msgBox.setUrlParameters( request );
				System.out.println(msgBox.getBackUrl());
				//此处添加版本号的日志记录信息
				
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
	
	public String showLogs(){
		
		logs=atmService.getLogs(mediaValue);
		return "showLogs";
	}
	
	public String showLogs1(){
		
		logs=atmService.getLogs(mediaValue);
		return "showLogs1";
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
					Atm temp = atmService.getAtmById(sids[i]);
					atmService.delete(temp);
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
			SimpleDateFormat fo=new SimpleDateFormat("yyyy-MM-dd");
			int pageNum = 1;
			int pageSize = 20;
			if (request.getParameter("pageNum") != null
					&& request.getParameter("pageNum").length() > 0)
				pageNum = Integer.parseInt(request.getParameter("pageNum"));
			if (request.getParameter("pageSize") != null
					&& request.getParameter("pageSize").length() > 0)
				pageSize = Integer.parseInt(request.getParameter("pageSize"));
			Page page = atmService.getPage(type,seriaNum, pageNum, pageSize);
			request.setAttribute("currPage", page);
			List<Object> list = (List<Object>)page.getQueryResult();
			if (from != null && from.equals("refresh")) {
				for (int i = 0; i < list.size(); i++) {
					Atm a1=(Atm)list.get(i);
					a1.setBrowerDateString(fo.format(a1.getBrowerDate()));
				}
				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf(page.getPageCount()));
				map.put("recordCount", String.valueOf(page.getRecordCount()));
				JSONUtil jsonUtil = new JSONUtil(
						"cn.grgbanking.feeltm.domain.testsys.Atm");
				JSONArray jsonObj = jsonUtil.toJSON(list, map);
				JSONObject input = new JSONObject();
				input.put("pageCount", String.valueOf(page.getPageCount()));
				input.put("recordCount", String.valueOf(page.getRecordCount()));
				input.put("jsonObj", jsonObj);								
				PrintWriter out = response.getWriter();
				out.print(input);
				return "";
			} else {
				ActionContext.getContext().put("atmList", list);
				return "query";
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "query";
	}
	
	public String check() throws Exception{
		response.setHeader( "Cache-Control", "no-cache" );
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/javascript;charset=UTF-8");
		String deviceNo=request.getParameter("deviceNo");
		PrintWriter out=response.getWriter();
		boolean flag=atmService.checkExist(deviceNo);
		if(flag)
			out.print("");
		else
			out.print("false");
		out.flush();
		out.close();
		return null;
	}
	
	public String getShowFlag() {
		return showFlag;
	}
	public void setShowFlag(String showFlag) {
		this.showFlag = showFlag;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSeriaNum() {
		return seriaNum;
	}
	public void setSeriaNum(String seriaNum) {
		this.seriaNum = seriaNum;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Atm getAtm() {
		return atm;
	}
	public void setAtm(Atm atm) {
		this.atm = atm;
	}
	public DeviceConfig getConfig() {
		return config;
	}
	public void setConfig(DeviceConfig config) {
		this.config = config;
	}
	public MediaVersion getMedia() {
		return media;
	}
	public void setMedia(MediaVersion media) {
		this.media = media;
	}
	public AtmService getAtmService() {
		return atmService;
	}
	public void setAtmService(AtmService atmService) {
		this.atmService = atmService;
	}

	public String getMediaValue() {
		return mediaValue;
	}

	public void setMediaValue(String mediaValue) {
		this.mediaValue = mediaValue;
	}

	public List<MediaUpdateLog> getLogs() {
		return logs;
	}

	public void setLogs(List<MediaUpdateLog> logs) {
		this.logs = logs;
	}	
	
	
}
