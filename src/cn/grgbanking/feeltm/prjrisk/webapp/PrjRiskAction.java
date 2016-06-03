package cn.grgbanking.feeltm.prjrisk.webapp;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

import cn.grgbanking.feeltm.config.UserRoleConfig;
import cn.grgbanking.feeltm.domain.testsys.PrjRisk;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.prj.dao.TesterDetailDao;
import cn.grgbanking.feeltm.prjrisk.service.PrjRiskService;
import cn.grgbanking.feeltm.staff.service.StaffInfoService;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;
public class PrjRiskAction extends BaseAction{
	private static final long serialVersionUID = 1L;
	private PrjRiskService prjRiskService;
	private StaffInfoService staffInfoService;
	
	private String prjname;
	private String summary;
	private String type;
	private String status;
	private String urgent;
	private String pond;   //问题编号
	private Date startDate;
	private Date endDate;
    private PrjRisk prjRisk;
    private String update_man;
    private String nowdate;  
    private String raiseMan;
    
    private List<PrjRisk> prjRisks;
	private String flag;
	
//	private Map<String,String> map;
	
	private TesterDetailDao testerDao;
  
	public String add(){
	//	map=testerDao.getNameListBySuggestion();
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		update_man=userModel.getUsername();
		raiseMan=update_man;
		 /* Integer	 integer= userModel.getLevel();  //用level来判断  0 经理 1 主任 2 普通用户
	    if (integer==0){  
	    	 flag="1";
	     }*/
		 //Map  role=userModel.getRole();     //用角色来判，是经理的可以开放
		if(UserRoleConfig.ifAdministratorOrHr(userModel))
		{
			return "add_manage";
		}
		
		return "add";
	}
	
	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	/**
	 * 发送oa短信，及邮件
	 * 标题：测试管理平台编号为[P0001]的问题建议(状态：打开)
	 * 正文：
	 * oa短信内容：测试管理平台编号为[P0001]的问题建议(状态：打开)
	 * @return
	 * @throws Exception
	 */
	public String save() throws Exception {     //保存suggestion概况的数据
		
		try {
			boolean flag = false;
			UserModel userModel = (UserModel) request.getSession().getAttribute(
					Constants.LOGIN_USER_KEY);
			prjRisk.setUpdateman(userModel.getUsername());
			prjRisk.setUserid(userModel.getUserid());
			prjRisk.setPrjname(prjRisk.getPrjname().trim());
			String deptname = staffInfoService.getDeptNameValueByUserId(userModel.getUserid());
		//	String groupname = staffInfoService.getGroupNameByUserId(userModel.getUserid());
			prjRisk.setDeptname(deptname);
			prjRisk.setGroupname(prjRisk.getPrjname());//修改项目组为项目名称
			String no=prjRiskService.getNextNo();
			prjRisk.setRno(no);
			prjRisk.setUpdate(new Date());
			flag=prjRiskService.add(prjRisk);
			if (flag == true) {
				MsgBox msgBox = new MsgBox(request, getText("add.ok"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				this.addActionMessage(getText("add.ok"));
				//采用多线程
		/*		Thread aa=new Thread(new Runnable() {
					@Override
					public void run() {
						OaOrEmail oe=new OaOrEmail();
						oe.sendMailOaByNew(suggestion);						
					}
				});
				aa.start();
				*/
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
				 
	public String delete() throws Exception {
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		String loginName=userModel.getUsername();
		try {
			String ids = request.getParameter("ids");
			System.out.println("ids: "+ids);
			int iCount = 0;
			int ncount = 0;
			String[] sids = ids.split(",");
			if("all".equals(sids[0])){
				String[] arr = new String[sids.length-1];
				System.arraycopy(sids, 1, arr, 0, arr.length);
				sids = arr;
			}
			if (sids != null) {
				for (int i = 0; i < sids.length; i++) {
					PrjRisk temp = prjRiskService.getprjRiskById(sids[i]);
					if(!loginName.equals(temp.getCreateman())){
						ncount++;
						continue;
					}else{
						prjRiskService.delete(temp);
						iCount++;
					}
				}
			}
			if(ncount>0){
				MsgBox msgBox = new MsgBox(request, "非记录创建者的数据不能被删除！");
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}else{
				MsgBox msgBox = new MsgBox(request, "删除成功！");
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request, getText("operator.addfaile"));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		}
		return "msgBox";
	} 

	public String edit() throws Exception {
	//	map=testerDao.getNameListBySuggestion();
		String forwardPage="edit";
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		
	
		try {
			String ids = request.getParameter("ids");
			prjRisk=prjRiskService.getprjRiskById(ids);
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		String Createname=prjRisk.getCreateman();
		String HandleMan=prjRisk.getHandleman();
		if(UserRoleConfig.upGrpManager(userModel)==false && !userModel.getUsername().equals(Createname)&& !userModel.getUsername().equals(HandleMan))
		{
			MsgBox msgBox1 = new MsgBox(request,getText("operInfoform.updatefaile",new String[]{"您没有修改该项目风险记录的权限 "}));
			msgBox1.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}
		else if(userModel.getUsername().equals(HandleMan))
		{
			forwardPage="edit_handleman";
		}
		else if(userModel.getUsername().equals(Createname))
		{
			if(UserRoleConfig.upGrpManager(userModel)==true){
				forwardPage="edit_hrmanage";
			}else{
				forwardPage="edit";
			}
		}
		else if(UserRoleConfig.upGrpManager(userModel)==true){
			forwardPage="edit_manage";
		}
		return forwardPage;
	 }
	
	
	public String update() throws Exception {
		System.out.println("update");
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		update_man=userModel.getUsername();
		try {
			PrjRisk oldPrjRisk=prjRiskService.getprjRiskById(prjRisk.getId());
			String rno=oldPrjRisk.getRno();
			
			prjRisk.setPrjname(prjRisk.getPrjname().trim());
			prjRisk.setUpdate(new Date());
			prjRisk.setUpdateman(update_man);
			prjRisk.setRno(rno);
			prjRisk.setGroupname(prjRisk.getGroupname());
			boolean flag=prjRiskService.update(prjRisk);
			if (flag == true) {
				MsgBox msgBox = new MsgBox(request,
						getText("operInfoform.updateok"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				
				
				//发送邮件并oa
		/*		if(!oldStatus.equals(newStatus)){					
					//多线程发
					Thread aa=new Thread(new Runnable(){
						@Override
						public void run() {
							// TODO Auto-generated method stub
							OaOrEmail oe=new OaOrEmail();
							if(!newStatus.equals("新建")){
								oe.sendMailOaByNewToOpen(suggestion);
							}							
						}
					});
					aa.start();
				}
				
				*/
				//添加发送邮件的程序，如状态为‘打开’，且为相关的处理人员时发送
//				SendMail sm=new SendMail();
//				StringBuffer sb=new StringBuffer();
//				
//				sm.sendMail("精细化管理："+suggestion.getSummary(), content, toAddress)
//				
//			flag=suggestionService.update(suggestion);
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
		System.out.println("refesh");
		try {
			query();
		} catch (Exception e) {
			e.printStackTrace();
		}
		;
	}
    
	
	public String query() throws Exception {
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		try {
			String createman = request.getParameter("createman");
			String from = request.getParameter("from");
			if (from != null && from.equals("refresh")) {
				SimpleDateFormat fo = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat fo1 = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				int pageNum = 1;
				int pageSize = 20;
				if (request.getParameter("pageNum") != null
						&& request.getParameter("pageNum").length() > 0)
					pageNum = Integer.parseInt(request.getParameter("pageNum"));
				if (request.getParameter("pageSize") != null
						&& request.getParameter("pageSize").length() > 0)
					pageSize = Integer.parseInt(request
							.getParameter("pageSize"));
				Page page = prjRiskService.getPage(prjname, summary, type,
						status, urgent, pond, startDate, endDate, pageNum,
						pageSize, createman, userModel);
				request.setAttribute("currPage", page);
				SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
				nowdate = f.format(new Date());
				List<Object> list = (List<Object>) page.getQueryResult();
				for (int i = 0; i < list.size(); i++) {
					PrjRisk tmp = (PrjRisk) list.get(i);
					// tmp.setRaiseDateString(tmp.getRaise_date()==null?"":fo.format(tmp.getRaise_date()));
					// tmp.setFinishing_dateString(tmp.getFinishing_date()==null?"":fo.format(tmp.getFinishing_date()));
					// tmp.setPratical_dateString(tmp.getPratical_date()==null?"":fo.format(tmp.getPratical_date()));
					// tmp.setUpdateDateString(tmp.getUpdate_date()==null?"":fo1.format(tmp.getUpdate_date()));
				}

				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf(page.getPageCount()));
				map.put("recordCount", String.valueOf(page.getRecordCount()));
				JSONUtil jsonUtil = new JSONUtil(
						"cn.grgbanking.feeltm.domain.testsys.PrjRisk");
				JSONArray jsonObj = jsonUtil.toJSON(list, map);
				JSONObject input = new JSONObject();
				if (page.getRecordCount() == 0) {
					input.put("pageCount",
							String.valueOf(page.getPageCount() + 1));
					input.put("recordCount",
							String.valueOf(page.getRecordCount()));
					input.put("jsonObj", jsonObj);
				} else {
					input.put("pageCount", String.valueOf(page.getPageCount()));
					input.put("recordCount",
							String.valueOf(page.getRecordCount()));
					input.put("jsonObj", jsonObj);
				}
				input.put("jsonObj", jsonObj);
				PrintWriter out = response.getWriter();
				out.print(input);
				return "";
			} else {
				//ActionContext.getContext().put("behaviorList", list);
				request.setAttribute("currPage", new Page());
				return "query";
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "query";
	}
	
	public String getNextNo(){
		String no=prjRiskService.getNextNo();
		ajaxPrint("{\"no\":\"" + no + "\"}");
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

	public TesterDetailDao getTesterDao() {
		return testerDao;
	}

	public void setTesterDao(TesterDetailDao testerDao) {
		this.testerDao = testerDao;
	}

	public String show() throws Exception {
		try {
			String ids = request.getParameter("ids");
			prjRisk=prjRiskService.getprjRiskById(ids);
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "show";
	}

	public PrjRiskService getPrjRiskService() {
		return prjRiskService;
	}

	public void setPrjRiskService(PrjRiskService prjRiskService) {
		this.prjRiskService = prjRiskService;
	}

	public String getPrjname() {
		return prjname;
	}

	public void setPrjname(String prjname) {
		this.prjname = prjname;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUrgent() {
		return urgent;
	}

	public void setUrgent(String urgent) {
		this.urgent = urgent;
	}

	public String getPond() {
		return pond;
	}

	public void setPond(String pond) {
		this.pond = pond;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public PrjRisk getPrjRisk() {
		return prjRisk;
	}

	public void setPrjRisk(PrjRisk prjRisk) {
		this.prjRisk = prjRisk;
	}

	public String getUpdate_man() {
		return update_man;
	}

	public void setUpdate_man(String update_man) {
		this.update_man = update_man;
	}

	public String getNowdate() {
		return nowdate;
	}

	public void setNowdate(String nowdate) {
		this.nowdate = nowdate;
	}

	public String getRaiseMan() {
		return raiseMan;
	}

	public void setRaiseMan(String raiseMan) {
		this.raiseMan = raiseMan;
	}

	public List<PrjRisk> getPrjRisks() {
		return prjRisks;
	}

	public void setPrjRisks(List<PrjRisk> prjRisks) {
		this.prjRisks = prjRisks;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
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
	


}
