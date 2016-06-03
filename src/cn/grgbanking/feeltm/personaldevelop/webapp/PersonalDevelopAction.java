package cn.grgbanking.feeltm.personaldevelop.webapp;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import cn.grgbanking.feeltm.domain.testsys.AbilityAnalyse;
import cn.grgbanking.feeltm.domain.testsys.AbilityDevelopPlan;
import cn.grgbanking.feeltm.domain.testsys.AbilityLog;
import cn.grgbanking.feeltm.domain.testsys.MediaUpdateLog;
import cn.grgbanking.feeltm.domain.testsys.WaitupDevelop;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.personaldevelop.domain.AbilityDevelopPlanRecord;
import cn.grgbanking.feeltm.personaldevelop.domain.WaitupDevelopRecord;
import cn.grgbanking.feeltm.personaldevelop.service.PersonalDevelopService;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.feeltm.util.OaOrEmail;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;

import com.opensymphony.xwork2.ActionContext;

public class PersonalDevelopAction extends BaseAction{
	@Autowired
	private PersonalDevelopService personaldevelopService;

	private String seachname;
	private String seachyear;
	private String seachstatus;
	private String addname;
	private String addgroupname;
	private String id;  //此id为公用的id
	private AbilityDevelopPlan personaldevelop;  //个人发展计划
	private WaitupDevelop waitupdevelop;   //待提升能力
	private AbilityAnalyse abilityanalyse;   //个人能力分析
	private AbilityLog abilitylog;
	private int level;
	private int finishTag;
	private String editwirte;
	private List<WaitupDevelopRecord> records;
	private List<AbilityDevelopPlanRecord> adpRecords;
	// 显示信息跳转标示 0为不显示 1为显示
	private String showFlag;
	
	private String mediaValue;   //传入的介质的实际值 a1,b2,c3等
	
	private List<MediaUpdateLog>   logs;
	
	public String add(){
		// 如果id不为空则为修改信息，显示修改信息内容
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		addgroupname = userModel.getGroupName();
		addname = userModel.getUsername();
		if( StringUtils.isNotBlank( id ) )
		{
			setId( id );
			abilityanalyse=personaldevelopService.getpersonaldevelopById(id);
			abilitylog=personaldevelopService.getAbilityLogById(id);
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
	
	public String edit(){
		// 如果id不为空则为修改信息，显示修改信息内容
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		if( StringUtils.isNotBlank( id ) )
		{
			setId( id );
			abilityanalyse=personaldevelopService.getpersonaldevelopById(id);
			abilitylog=personaldevelopService.getAbilityLogById(id);
			level = userModel.getLevel();
			editwirte = userModel.getUsername();
		}
		return "edit";
	}

	public String addWaitupDevelop(){
		// 如果id不为空则为修改信息，显示修改信息内容
		if( StringUtils.isNotBlank( id ) )
		{
			setId( id );
			try{
				waitupdevelop=personaldevelopService.getWaitupDevelopById(id);
				if(waitupdevelop!=null && !"1".equals(showFlag ) )
				{
					String taskDesc=waitupdevelop.getCapabilityname();
					parseToRecords(taskDesc);
					return "editWaitupDevelop";
				}
			}catch(Exception e){
				waitupdevelop=new WaitupDevelop(id);
			}
		}
		if( "1".equals( showFlag ) )
		{
			if(waitupdevelop!=null)
			{
			String taskDesc=waitupdevelop.getCapabilityname();
			parseToRecords(taskDesc);
			}
			return "showWaitupDevelop";
		}
		else
		{
			return "addWaitupDevelop";
		}
	}
	
	public String editWaitupDevelop(){
		// 如果id不为空则为修改信息，显示修改信息内容
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		if( StringUtils.isNotBlank( id ) )
		{
			setId( id );
			try{
				waitupdevelop=personaldevelopService.getWaitupDevelopById(id);
				abilityanalyse=personaldevelopService.getpersonaldevelopById(id);
				level = userModel.getLevel();
				editwirte = userModel.getUsername();
				if(waitupdevelop!=null)
				{
					String taskDesc=waitupdevelop.getCapabilityname();
					parseToRecords(taskDesc);
					if(!abilityanalyse.getStatus().equals("未经审核通过")){
						return "editWaitupDevelop2";
					}
					else{
						return "editWaitupDevelop";
					}			
				}
			}catch(Exception e){
				waitupdevelop=new WaitupDevelop(id);
			}
		}
		if( "1".equals( showFlag ) )
		{
			return "showWaitupDevelop";
		}
		else
		{
			return "addWaitupDevelop";
		}
	}
	
	public String addAbilityDevelopPlan() throws Exception{
		// 如果id不为空则为修改信息，显示修改信息内容
		if( StringUtils.isNotBlank( id ) )
		{
			setId( id );
			try{
			personaldevelop=personaldevelopService.getAbilityDevelopPlanById(id);
			if(personaldevelop!=null && !"1".equals( showFlag ))
			{
				String taskDesc=personaldevelop.getWishcapability();
				parseToPlanRecords(taskDesc);
				return "editAbilityDevelopPlan";
			}
			}
			catch(Exception e){
				personaldevelop=new AbilityDevelopPlan(id);
			}
		}
		if( "1".equals( showFlag ) )
		{
			if(personaldevelop!=null)
			{
			String taskDesc=personaldevelop.getWishcapability();
			parseToPlanRecords(taskDesc);
			}
			return "showAbilityDevelopPlan";
		}
		else
		{
			return "addAbilityDevelopPlan";
		}
	}
	
	public String editAbilityDevelopPlan(){
		// 如果id不为空则为修改信息，显示修改信息内容
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		if( StringUtils.isNotBlank( id ) )
		{
			setId( id );
			try{
			personaldevelop=personaldevelopService.getAbilityDevelopPlanById(id);
			abilityanalyse=personaldevelopService.getpersonaldevelopById(id);
			level = userModel.getLevel();
			editwirte = userModel.getUsername();
			if(personaldevelop!=null)
			{
				String taskDesc=personaldevelop.getWishcapability();
				parseToPlanRecords(taskDesc);
				if(!abilityanalyse.getStatus().equals("未经审核通过")){
					return "editAbilityDevelopPlan2";
				}
				else
				{
					return "editAbilityDevelopPlan";
				}
				
			}
			}
			catch(Exception e){
				personaldevelop=new AbilityDevelopPlan(id);
			}
		}
		if( "1".equals( showFlag ) )
		{
			return "showAbilityDevelopPlan";
		}
		else
		{
			return "addAbilityDevelopPlan";
		}
	}
	
	public String showAbilityLog(){
		// 如果id不为空则为修改信息，显示修改信息内容
		if( StringUtils.isNotBlank( id ) )
		{
			setId( id );
			abilitylog=personaldevelopService.getAbilityLogById(id);		
		}
		return "showAbilityLog";
	}
	
	public String save() throws Exception {    //保存ATM概况的数据
		try {
			UserModel userModel = (UserModel) request.getSession().getAttribute(
					Constants.LOGIN_USER_KEY);
			boolean flag = false;
			AbilityAnalyse oldAtm=null;
			if(id!=null&&!id.equals(""))
			oldAtm=personaldevelopService.getpersonaldevelopById(id);
			
			int addOrModify=0;   //0代表新增，1代表修改
			SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			abilityanalyse.setUpdatedate(f.format(new Date()));
			abilityanalyse.setUpdateman(userModel.getUsername());
			abilityanalyse.setStatus("未经审核通过");
			abilityanalyse.setHeadmanupditing("未审核");
			abilityanalyse.setManageupauditing("未审核");
			if(oldAtm==null){

				flag=personaldevelopService.addPersonalDevelop(abilityanalyse);
				setId(abilityanalyse.getId());
				abilitylog.setId(abilityanalyse.getId());
				abilitylog.setLog(f.format(new Date()) + " 新增记录\r\n");
				personaldevelopService.addAbilityLog(abilitylog);
			}else{
				//修改				
				abilityanalyse.setId(id);
				flag=personaldevelopService.updatePersonalDevelop(abilityanalyse);
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
						+ "/pages/personaldevelop/personaldevelopinfo!add.action" );
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
	
	public String update() throws Exception {//保存ATM概况的数据
		try {
			UserModel userModel = (UserModel) request.getSession().getAttribute(
					Constants.LOGIN_USER_KEY);
			boolean flag = false;
			AbilityAnalyse oldAtm=null;
			if(id!=null&&!id.equals(""))
			oldAtm=personaldevelopService.getpersonaldevelopById(id);		
			int addOrModify=0;   //0代表新增，1代表修改
					//修改				
			abilityanalyse.setId(id);
			Date now = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //显示日期，周，时间（精确到秒）
			String str5 = format.format(now);
			level = userModel.getLevel();
			editwirte = userModel.getUsername();
			OaOrEmail oa=new OaOrEmail();
			abilitylog=personaldevelopService.getAbilityLogById(id);
			String logCopytmp;
				if(level == 1)
				{
					if(!oldAtm.getHeadmanupditing().equals(abilityanalyse.getHeadmanupditing()))
					{
						if(abilityanalyse.getHeadmanupditing().equals("审核通过"))
						{
							abilityanalyse.setStatus("待上上级审核");
							logCopytmp = abilitylog.getLog() + str5 + " " + abilityanalyse.getName() + abilityanalyse.getCreateyear() + "个人发展计划被上级审核通过\r\n";
							abilitylog.setLog(logCopytmp);
							oa.oaContent("tfei", "请审核" + abilityanalyse.getName()+ abilityanalyse.getCreateyear() + "个人发展计划");
						}		
						if(abilityanalyse.getHeadmanupditing().equals("审核不通过"))
						{
							abilityanalyse.setStatus("未经审核通过");
							abilityanalyse.setHeadmanupditing("未审核");
							logCopytmp = abilitylog.getLog() + str5 + " " + abilityanalyse.getName() + abilityanalyse.getCreateyear() + "个人发展计划审核不通过,被上级回退\r\n";
							abilitylog.setLog(logCopytmp);
							oa.oaContent(personaldevelopService.getUserid(abilityanalyse.getName()), "你的" + abilityanalyse.getCreateyear() + "个人发展计划被打回,请修改后再次提交");
						}
					}
				}
				else if(level == 0)
				{
					if(!oldAtm.getManageupauditing().equals(abilityanalyse.getManageupauditing()))
					{
						if(abilityanalyse.getManageupauditing().equals("审核通过"))
						{
							abilityanalyse.setStatus("执行中");		
							logCopytmp = abilitylog.getLog() + str5 + " " + abilityanalyse.getName() + abilityanalyse.getCreateyear() + "个人发展计划被上 上级审核通过\r\n";
							abilitylog.setLog(logCopytmp);
							oa.oaContent(personaldevelopService.getUserid(abilityanalyse.getName()), abilityanalyse.getCreateyear() + "个人发展计划审核通过");
						}		
						if(abilityanalyse.getManageupauditing().equals("审核不通过"))
						{
							abilityanalyse.setStatus("未经审核通过");
							abilityanalyse.setHeadmanupditing("未审核");
							abilityanalyse.setManageupauditing("未审核");
							logCopytmp = abilitylog.getLog() + str5 + " " + abilityanalyse.getName() + abilityanalyse.getCreateyear() + "个人发展计划审核不通过,被上上级回退\r\n";
							abilitylog.setLog(logCopytmp);
							oa.oaContent(personaldevelopService.getUserid(abilityanalyse.getName()), "你的" + abilityanalyse.getCreateyear() + "个人发展计划被打回,请修改后再次提交");
						}
					}
				}
				abilityanalyse.setUpdatedate(format.format(new Date()));
				abilityanalyse.setUpdateman(userModel.getUsername());
				flag=personaldevelopService.updatePersonalDevelop(abilityanalyse);
				personaldevelopService.updateAbilityLog(abilitylog);

			if (flag == true) {
				MsgBox msgBox = null;
				msgBox=new MsgBox(request, getText("operInfoform.updateok"));
				this.addActionMessage(getText("operInfoform.updateok"));
				msgBox.setButtonType(MsgBox.BUTTON_OK);
				msgBox.setBackUrl( request.getContextPath()
						+ "/pages/personaldevelop/personaldevelopinfo!edit.action" );
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
	
	public String saveWaitupDevelop() throws Exception {
		try {
			boolean flag = false;
			WaitupDevelop oldObj=personaldevelopService.getWaitupDevelopById(id);
			int addOrModify=0;   //0代表新增，1代表修改
			waitupdevelop.setId(id);
			if(oldObj==null){
				String taskDesc=getTaskDescDetail(request);
				waitupdevelop.setCapabilityname(taskDesc);
				flag=personaldevelopService.addWaitupDevelop(waitupdevelop);
				setId(waitupdevelop.getId());
			}else{
				//修改
				waitupdevelop.setId(id);
				String taskDesc=getTaskDescDetail(request);
				waitupdevelop.setCapabilityname(taskDesc);
				flag=personaldevelopService.updateWaitupDevelop(waitupdevelop);
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
						+ "/pages/personaldevelop/personaldevelopinfo!editWaitupDevelop.action" );
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
	
	public String saveAbilityDevelopPlan() throws Exception {
		try {
			boolean flag = false;
			AbilityDevelopPlan oldObj=personaldevelopService.getAbilityDevelopPlanById(id);
			int addOrModify=0;   //0代表新增，1代表修改
			personaldevelop.setId(id);

			if(oldObj==null){
				String taskDesc=getPlanTaskDescDetail(request);
				personaldevelop.setWishcapability(taskDesc);
				flag=personaldevelopService.addAbilityDevelopPlan(personaldevelop);
				setId(personaldevelop.getId());
			}else{
				//修改
				String taskDesc=getPlanTaskDescDetail(request);
				personaldevelop.setWishcapability(taskDesc);
				personaldevelop.setId(id);
				flag=personaldevelopService.updateAbilityDevelopPlan(personaldevelop);
				addOrModify=1;
			}
			if (flag == true) {
				UserModel userModel = (UserModel) request.getSession().getAttribute(
						Constants.LOGIN_USER_KEY);
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
						+ "/pages/personaldevelop/personaldevelopinfo!editAbilityDevelopPlan.action" );
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
	
	public String UpAuditing() throws Exception {
		try {
			UserModel userModel = (UserModel) request.getSession().getAttribute(
					Constants.LOGIN_USER_KEY);
			boolean flag = false;		
			String ids = request.getParameter("ids");
			abilityanalyse=personaldevelopService.getpersonaldevelopById(ids);
			Date now = new Date();
	    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //显示日期，周，时间（精确到秒）
	    	String str5 = format.format(now);
			if(!abilityanalyse.getStatus().equals("未经审核通过"))
			{
				MsgBox msgBox1 = new MsgBox(request,getText("提交失败：已经在走审核流程"));
				msgBox1.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}
			if(!abilityanalyse.getName().equals(userModel.getUsername()))
			{
				MsgBox msgBox1 = new MsgBox(request,getText("提交失败：你没有提交审核该个人发展计划的权限"));
				msgBox1.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}	    	
				abilitylog=personaldevelopService.getAbilityLogById(ids);
				String logCopytmp = abilitylog.getLog() + str5 + " " + userModel.getUsername() + "提交" + abilityanalyse.getCreateyear() + "个人发展计划\r\n";
				abilitylog.setLog(logCopytmp);
				personaldevelopService.updateAbilityLog(abilitylog);
				OaOrEmail oa=new OaOrEmail();
				if(userModel.getLevel() == 1){
					abilityanalyse.setStatus("待上上级审核");
					abilityanalyse.setManageupauditing("未审核");
					abilityanalyse.setHeadmanupditing("审核通过");
					oa.oaContent("tfei", "请审核" + abilityanalyse.getName()+ abilityanalyse.getCreateyear() + "个人发展计划：");             
				}
				else
				{
					abilityanalyse.setStatus("待上级审核");
					abilityanalyse.setManageupauditing("未审核");
					abilityanalyse.setHeadmanupditing("未审核");	
					String uid=personaldevelopService.getHeadman(abilityanalyse.getGroupname());
					oa.oaContent(uid, "请审核" + abilityanalyse.getName()+ abilityanalyse.getCreateyear() + "个人发展计划：");             
				}	
				abilityanalyse.setUpdatedate(format.format(new Date()));
				abilityanalyse.setUpdateman(userModel.getUsername());
				personaldevelopService.updatePersonalDevelop(abilityanalyse);
				MsgBox msgBox = new MsgBox(request, getText("提交成功"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				this.addActionMessage(getText("提交成功"));	
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
	
	public String updateWaitupDevelop() throws Exception {
		try {
			UserModel userModel = (UserModel) request.getSession().getAttribute(
					Constants.LOGIN_USER_KEY);
			boolean flag = false;
			WaitupDevelop oldObj=personaldevelopService.getWaitupDevelopById(id);
			int addOrModify=0;   //0代表新增，1代表修改
			waitupdevelop.setId(id);
			abilityanalyse=personaldevelopService.getpersonaldevelopById(id);	
			if(oldObj==null){
				String taskDesc=getTaskDescDetail(request);
				waitupdevelop.setCapabilityname(taskDesc);
				flag=personaldevelopService.addWaitupDevelop(waitupdevelop);
				setId(waitupdevelop.getId());
			}else{
				//修改
				waitupdevelop.setId(id);
				String taskDesc=getTaskDescDetail(request);
				waitupdevelop.setCapabilityname(taskDesc);
				flag=personaldevelopService.updateWaitupDevelop(waitupdevelop);
				addOrModify=1;
			}
			SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //显示日期，周，时间（精确到秒）
			abilityanalyse.setUpdatedate(f.format(new Date()));
			abilityanalyse.setUpdateman(userModel.getUsername());
			personaldevelopService.updatePersonalDevelop(abilityanalyse);
			if (flag == true) {
				MsgBox msgBox = null;
				if(addOrModify==0){
					msgBox=new MsgBox(request, getText("update.ok"));
					this.addActionMessage(getText("updateok.ok"));
				}
				else{
					msgBox=new MsgBox(request, getText("operInfoform.updateok"));
					this.addActionMessage(getText("operInfoform.updateok"));
				}
				msgBox.setButtonType(MsgBox.BUTTON_OK);			
				if(!abilityanalyse.getStatus().equals("未经审核通过")){
				msgBox.setBackUrl( request.getContextPath()
							+ "/pages/personaldevelop/personaldevelopinfo!editWaitupDevelop.action" );
				}else
				{
					msgBox.setBackUrl( request.getContextPath()
							+ "/pages/personaldevelop/personaldevelopinfo!editWaitupDevelop.action" );
				}
				
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
	
	public String updateAbilityDevelopPlan() throws Exception {
		try {
			UserModel userModel = (UserModel) request.getSession().getAttribute(
					Constants.LOGIN_USER_KEY);
			boolean flag = false;
			AbilityDevelopPlan oldObj=personaldevelopService.getAbilityDevelopPlanById(id);
			int addOrModify=0;   //0代表新增，1代表修改
			personaldevelop.setId(id);
			abilityanalyse=personaldevelopService.getpersonaldevelopById(id);
			if(oldObj==null){
				String taskDesc=getPlanTaskDescDetail(request);
				personaldevelop.setWishcapability(taskDesc);
				flag=personaldevelopService.addAbilityDevelopPlan(personaldevelop);
				setId(personaldevelop.getId());
			}else{
				//修改
				String taskDesc=getPlanTaskDescDetail(request);
				personaldevelop.setWishcapability(taskDesc);
				if(!abilityanalyse.getStatus().equals("未经审核通过")){
					String logtmp = balancePlanRecords(oldObj.getWishcapability(),taskDesc);
					abilitylog=personaldevelopService.getAbilityLogById(id);
					if(!logtmp.equals(""))
					{	
						if(finishTag == 0 && abilityanalyse.getStatus().equals("执行中")){
							Date now = new Date();
					    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //显示日期，周，时间（精确到秒）
					    	String str5 = format.format(now);
					    	logtmp  = logtmp + str5 + " " + abilityanalyse.getName() + abilityanalyse.getCreateyear() + "个人发展计划执行完成\r\n";
					    	abilityanalyse.setStatus("执行完成");
					    	personaldevelopService.updatePersonalDevelop(abilityanalyse);
					    	OaOrEmail oa=new OaOrEmail();
					    	oa.oaContent("tfei", abilityanalyse.getName()+ abilityanalyse.getCreateyear() + "个人发展计划已经执行完成,请确认");
						}
						if(abilityanalyse.getStatus().equals("执行完成") && finishTag == 1)
						{
							Date now = new Date();
					    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //显示日期，周，时间（精确到秒）
					    	String str5 = format.format(now);
					    	logtmp  = logtmp + str5 + " " + abilityanalyse.getName() + abilityanalyse.getCreateyear() + "个人发展计划重新执行\r\n";
							abilityanalyse.setStatus("执行中");
							personaldevelopService.updatePersonalDevelop(abilityanalyse);
						}
						String logCopytmp = abilitylog.getLog() + logtmp;
						abilitylog.setLog(logCopytmp);
						personaldevelopService.updateAbilityLog(abilitylog);
					}	
				}
				personaldevelop.setId(id);
				SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //显示日期，周，时间（精确到秒）
				abilityanalyse.setUpdatedate(f.format(new Date()));
				abilityanalyse.setUpdateman(userModel.getUsername());
				personaldevelopService.updatePersonalDevelop(abilityanalyse);
				flag=personaldevelopService.updateAbilityDevelopPlan(personaldevelop);
				addOrModify=1;
			}			
			if (flag == true){
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
				if(!abilityanalyse.getStatus().equals("未经审核通过")){
					msgBox.setBackUrl( request.getContextPath()
							+ "/pages/personaldevelop/personaldevelopinfo!editAbilityDevelopPlan.action" );
					}else
					{
						msgBox.setBackUrl( request.getContextPath()
								+ "/pages/personaldevelop/personaldevelopinfo!editAbilityDevelopPlan.action" );
					}
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
	
	private String getTaskDescDetail(HttpServletRequest request) {
		StringBuffer sb=new StringBuffer();
		String[] capabilityExplain=request.getParameterValues("capabilityExplain");
		String[] taskDesc=request.getParameterValues("taskDesc");

		int size=taskDesc.length;
		
		for(int i=0;i<size;i++){
			if(taskDesc==null){
				sb.append("  ").append("$$");
			}else{
				sb.append(taskDesc[i]).append("$$");
			}			
			if(capabilityExplain.equals("")){
				sb.append("  ").append("$$1@@@");
			}else{
				sb.append(capabilityExplain[i]).append("$$1@@@");
			}		
		}
		return sb.toString();
	}
	
	private void parseToRecords(String taskDesc) {
		records=new ArrayList<WaitupDevelopRecord>();
		String[] record=taskDesc.split("@@@");
		for(int i=0;i<record.length;i++){
			String[] recordField=record[i].split("\\$\\$");  //一行的所有字段
			String taskDescTmp=recordField[0];   //unuse
			String capabilityExplainTmp=recordField[1];			
			WaitupDevelopRecord recordTmp=new WaitupDevelopRecord();		
			recordTmp.setTaskDesc(taskDescTmp);
			recordTmp.setCapabilityExplain(capabilityExplainTmp);
			records.add(recordTmp);
		}
	}
	
	private String balanceWaitupRecords(String taskDescOld,String taskDescNew) {
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		String[] record0ld=taskDescOld.split("@@@");
		String[] recordNew=taskDescNew.split("@@@");
		String tmp="";
		Date now = new Date();
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //显示日期，周，时间（精确到秒）
    	String str5 = format.format(now);
    	StringBuffer sb1=new StringBuffer();
    	sb1.append("");
		for(int i=0;i<record0ld.length;i++){
			String[] recordFieldOld=record0ld[i].split("\\$\\$");  //一行的所有字段
			String[] recordFieldNew=recordNew[i].split("\\$\\$");  //一行的所有字段
			String taskDescOldTmp=recordFieldOld[0];   //unuse
			String capabilityExplainOldTmp=recordFieldOld[1];
			String taskDescNewTmp=recordFieldNew[0];   //unuse
			String capabilityExplainNewTmp=recordFieldNew[1];
			
            if(!taskDescOldTmp.equals(taskDescNewTmp))
            {
		    	tmp = str5 + "  " + userModel.getUsername() + "  修改能力名称" + taskDescOldTmp+ "为" + taskDescNewTmp;
		    	sb1.append(tmp).append("\r\n");
            }
            
            if(!capabilityExplainOldTmp.equals(capabilityExplainNewTmp))
            {
		    	tmp = str5 + "  " + userModel.getUsername() + "  修改能力说明"  + capabilityExplainOldTmp+ "为" + capabilityExplainNewTmp;
		    	sb1.append(tmp).append("\r\n");
            }          
		}
		return sb1.toString();
	}
	
	private String balancePlanRecords(String taskDescOld,String taskDescNew) {
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		String[] record0ld=taskDescOld.split("@@@");
		String[] recordNew=taskDescNew.split("@@@");
		String tmp="";
		Date now = new Date();
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //显示日期，周，时间（精确到秒）
    	String str5 = format.format(now);
    	StringBuffer sb1=new StringBuffer();
    	sb1.append("");
    	finishTag = 0;
		for(int i=0;i<record0ld.length;i++){
			String[] recordFieldOld=record0ld[i].split("\\$\\$");  //一行的所有字段
			String[] recordFieldNew=recordNew[i].split("\\$\\$");  //一行的所有字段
			String taskDescOldTmp=recordFieldOld[0];   //unuse
			String planDateOldTmp=recordFieldOld[1];
			String suggestModeOldTmp=recordFieldOld[2];
			String wishInstanceOldTmp=recordFieldOld[3];
			String finishInstanceOldTmp=recordFieldOld[4];
			String finishDateOldTmp=recordFieldOld[5];
			String headmanScoreOldTmp=recordFieldOld[6];
			String manageScoreOldTmp=recordFieldOld[7];
			
			String taskDescNewTmp=recordFieldNew[0];   //unuse
			String planDateNewTmp=recordFieldNew[1];
			String suggestModeNewTmp=recordFieldNew[2];
			String wishInstanceNewTmp=recordFieldNew[3];
			String finishInstanceNewTmp=recordFieldNew[4];
			String finishDateNewTmp=recordFieldNew[5];
			String headmanScoreNewTmp=recordFieldNew[6];
			String manageScoreNewTmp=recordFieldNew[7];
			
			
            if(finishDateNewTmp.equals(""))
            {
            	finishTag = 1;
            }
            if(!finishDateOldTmp.equals(finishDateNewTmp))
            {
		    	tmp = str5 + " " + userModel.getUsername() + "对记录 " + taskDescNewTmp + "修改完成日期, " + finishDateOldTmp+ "改为" + finishDateNewTmp;
		    	sb1.append(tmp).append("\r\n");
            }
		}
		return sb1.toString();
	}
	
	private String getPlanTaskDescDetail(HttpServletRequest request) {
		StringBuffer sb=new StringBuffer();
		String[] planDate=request.getParameterValues("planDate");
		String[] taskDesc=request.getParameterValues("taskDesc");
		String[] suggestMode=request.getParameterValues("suggestMode");
		String[] wishInstance=request.getParameterValues("wishInstance");
		String[] finishInstance=request.getParameterValues("finishInstance");
		String[] finishDate=request.getParameterValues("finishDate");
		String[] headmanScore=request.getParameterValues("headmanScore");
		String[] manageScore=request.getParameterValues("manageScore");
		int size=taskDesc.length;
		
		for(int i=0;i<size;i++){
			sb.append(taskDesc[i]).append("$$");
			sb.append(planDate[i]).append("$$");
			sb.append(suggestMode[i]).append("$$");
			sb.append(wishInstance[i]).append("$$");
			sb.append(finishInstance[i]).append("$$");
			sb.append(finishDate[i]).append("$$");
			sb.append(headmanScore[i]).append("$$");
			sb.append(manageScore[i]).append("$$1@@@");
	
		}
		return sb.toString();
	}
	
	private void parseToPlanRecords(String taskDesc) throws Exception {
		adpRecords=new ArrayList<AbilityDevelopPlanRecord>();
		String[] record=taskDesc.split("@@@");
		for(int i=0;i<record.length;i++){
			String[] recordField=record[i].split("\\$\\$");  //一行的所有字段
			String taskDescTmp=recordField[0];   //unuse
			String planDateTmp=recordField[1];
			String suggestModeTmp=recordField[2];
			String wishInstanceTmp=recordField[3];
			String finishInstanceTmp=recordField[4];
			String finishDateTmp=recordField[5];
			String headmanScoreTmp=recordField[6];
			String manageScoreTmp=recordField[7];		
			//SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd");		
		//	Date planTmp=f.parse(planDateTmp);
		//	Date finishTmp=f.parse(finishDateTmp);
			AbilityDevelopPlanRecord recordTmp=new AbilityDevelopPlanRecord();		
			recordTmp.setTaskDesc(taskDescTmp);
			recordTmp.setPlanDate(planDateTmp);
			recordTmp.setSuggestMode(suggestModeTmp);
			recordTmp.setWishInstance(wishInstanceTmp);
			recordTmp.setFinishInstance(finishInstanceTmp);
			recordTmp.setFinishDate(finishDateTmp);
			recordTmp.setHeadmanScore(headmanScoreTmp);
			recordTmp.setManageScore(manageScoreTmp);
			adpRecords.add(recordTmp);
		}
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
					AbilityAnalyse temp=personaldevelopService.getpersonaldevelopById(sids[i]);
					//AbilityDevelopPlan temp = personaldevelopService.getAbilityDevelopPlanById(sids[i]);
					personaldevelopService.delete(temp);
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
			UserModel userModel = (UserModel) request.getSession().getAttribute(
					Constants.LOGIN_USER_KEY);
			String queryGroupname = userModel.getGroupName();
			String queryName = userModel.getUsername();
			int queryLevel = userModel.getLevel();
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
			Page page = personaldevelopService.getPage(seachname,seachyear,seachstatus,queryGroupname,queryName,queryLevel, pageNum, pageSize);
			request.setAttribute("currPage", page);
			List<Object> list = (List<Object>)page.getQueryResult();
			if (from != null && from.equals("refresh")) {
				for (int i = 0; i < list.size(); i++) {
					//AbilityDevelopPlan a1=(AbilityDevelopPlan)list.get(i);
	//				a1.setBrowerDateString(fo.format(a1.getBrowerDate()));
				}
				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf(page.getPageCount()));
				map.put("recordCount", String.valueOf(page.getRecordCount()));
				JSONUtil jsonUtil = new JSONUtil(
						"cn.grgbanking.feeltm.domain.testsys.AbilityAnalyse");
				JSONArray jsonObj = jsonUtil.toJSON(list, map);
				JSONObject input = new JSONObject();
				input.put("pageCount", String.valueOf(page.getPageCount()));
				input.put("recordCount", String.valueOf(page.getRecordCount()));
				input.put("jsonObj", jsonObj);								
				PrintWriter out = response.getWriter();
				out.print(input);
				return "";
			} else {
				ActionContext.getContext().put("personaldevelopList", list);
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
		boolean flag=personaldevelopService.checkExist(deviceNo);
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

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public AbilityDevelopPlan getAbilityDevelopPlan() {
		return personaldevelop;
	}
	public void setAbilityDevelopPlan(AbilityDevelopPlan personaldevelop) {
		this.personaldevelop = personaldevelop;
	}
	public WaitupDevelop getWaitupDevelop() {
		return waitupdevelop;
	}
	public void setWaitupDevelop(WaitupDevelop waitupdevelop) {
		this.waitupdevelop = waitupdevelop;
	}
	public AbilityAnalyse getAbilityAnalyse() {
		return abilityanalyse;
	}
	public void setAbilityAnalyse(AbilityAnalyse abilityanalyse) {
		this.abilityanalyse = abilityanalyse;
	}
	public PersonalDevelopService getPersonalDevelopService() {
		return personaldevelopService;
	}
	public void setPersonalDevelopService(PersonalDevelopService personaldevelopService) {
		this.personaldevelopService = personaldevelopService;
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

	public PersonalDevelopService getPersonaldevelopService() {
		return personaldevelopService;
	}

	public void setPersonaldevelopService(
			PersonalDevelopService personaldevelopService) {
		this.personaldevelopService = personaldevelopService;
	}

	public AbilityDevelopPlan getPersonaldevelop() {
		return personaldevelop;
	}

	public void setPersonaldevelop(AbilityDevelopPlan personaldevelop) {
		this.personaldevelop = personaldevelop;
	}

	public WaitupDevelop getWaitupdevelop() {
		return waitupdevelop;
	}

	public void setWaitupdevelop(WaitupDevelop waitupdevelop) {
		this.waitupdevelop = waitupdevelop;
	}

	public AbilityAnalyse getAbilityanalyse() {
		return abilityanalyse;
	}

	public void setAbilityanalyse(AbilityAnalyse abilityanalyse) {
		this.abilityanalyse = abilityanalyse;
	}

	public String getSeachname() {
		return seachname;
	}

	public void setSeachname(String seachname) {
		this.seachname = seachname;
	}

	public String getSeachyear() {
		return seachyear;
	}

	public void setSeachyear(String seachyear) {
		this.seachyear = seachyear;
	}

	public String getSeachstatus(){
		return seachstatus;
	}

	public void setSeachstatus(String seachstatus) {
		this.seachstatus = seachstatus;
	}

	public String getAddname() {
		return addname;
	}

	public void setAddname(String addname) {
		this.addname = addname;
	}

	public String getAddgroupname() {
		return addgroupname;
	}

	public void setAddgroupname(String addgroupname) {
		this.addgroupname = addgroupname;
	}

	public List<WaitupDevelopRecord> getRecords() {
		return records;
	}

	public void setRecords(List<WaitupDevelopRecord> records) {
		this.records = records;
	}

	public List<AbilityDevelopPlanRecord> getAdpRecords() {
		return adpRecords;
	}

	public void setAdpRecords(List<AbilityDevelopPlanRecord> adpRecords) {
		this.adpRecords = adpRecords;
	}

	public AbilityLog getAbilitylog() {
		return abilitylog;
	}

	public void setAbilitylog(AbilityLog abilitylog) {
		this.abilitylog = abilitylog;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getFinishTag() {
		return finishTag;
	}

	public void setFinishTag(int finishTag) {
		this.finishTag = finishTag;
	}

	public String getEditwirte() {
		return editwirte;
	}

	public void setEditwirte(String editwirte) {
		this.editwirte = editwirte;
	}	
	
	
}
