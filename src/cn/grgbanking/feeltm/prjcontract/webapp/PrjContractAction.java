package cn.grgbanking.feeltm.prjcontract.webapp;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import cn.grgbanking.feeltm.config.UserRoleConfig;
import cn.grgbanking.feeltm.domain.PrjContract;
import cn.grgbanking.feeltm.domain.PrjContractPayment;
import cn.grgbanking.feeltm.domain.SysUser;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.prjcontract.service.PrjContractService;
import cn.grgbanking.feeltm.project.domain.Project;
import cn.grgbanking.feeltm.project.service.ProjectService;
import cn.grgbanking.feeltm.staff.service.StaffInfoService;
import cn.grgbanking.feeltm.um.service.SysUserInfoService;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;

@SuppressWarnings({ "serial", "unchecked" })
public class PrjContractAction extends BaseAction implements
		ModelDriven<PrjContract> {
	private PrjContractService prjContractService;
	@Autowired
	private StaffInfoService staffInfoService;
	@Autowired
	private ProjectService projectService;
	
	private PrjContract prjContract = new PrjContract();
	private PrjContractPayment prjContractPayment = new PrjContractPayment();
	private List<PrjContract> list = new ArrayList<PrjContract>();
	private List<PrjContractPayment> paymentList = new ArrayList<PrjContractPayment>();
	private List<String> groupNameList = new ArrayList<String>();

	public String pageNum;
	public String pageSize;
	private SysUserInfoService sysUserInfoService;

	/**
	 * 查询所有项目合同信息，带有分页
	 * 
	 * @return
	 * @throws Exception
	 */
	public String listAll() throws Exception {
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		//初始化下拉列表
		initSelectData();
		int pagenum = 1;
		int pagesize = 20;
		if (pageNum != null && !pageNum.equals("")) {
			pagenum = Integer.parseInt(pageNum);
		}
		if (pageSize != null && !pageSize.equals("")) {
			pagesize = Integer.parseInt(pageSize);
		}
		boolean hasRight = isManagerOrNot(userModel.getUserid());
		Page page = prjContractService.getPage(pagenum, pagesize, hasRight,
				userModel.getUsername());
		List<SysUser> groupManager = staffInfoService.getGroupManager();
		request.setAttribute("groupManager", groupManager);
		request.setAttribute("currPage", page);
		list = page.getQueryResult();
		ActionContext.getContext().put("prjContractlist", list);
		request.getSession().setAttribute("prjContractinfo.menuid",
				request.getParameter("menuid"));
		return "listsuccess";
	}

	/**
	 * 根据查询条件查询
	 * 
	 * @return
	 * @throws Exception
	 */
	public String query() throws Exception {
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		try {
			String from = request.getParameter("from");
			int pagenum = 1;
			int pagesize = 20;
			if (pageNum != null && pageNum.length() > 0)
				pagenum = Integer.parseInt(pageNum);
			if (pageSize != null && pageSize.length() > 0)
				pagesize = Integer.parseInt(pageSize);
			// 根据查询条件查询数据
			boolean hasRight = isManagerOrNot(userModel.getUserid());
			//prjContract.setPrjName(new String(request.getParameter("prjName").getBytes("utf-8"),"utf-8"));
			Page page = prjContractService.getPrjContractByCondition(
					prjContract, pagenum, pagesize, hasRight,
					userModel.getUsername());
			request.setAttribute("currPage", page);
			List list = (List<Object>) page.getQueryResult();

			if (from != null && from.equals("refresh")) {
				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf(page.getPageCount()));
				map.put("recordCount", String.valueOf(page.getRecordCount()));
				JSONUtil jsonUtil = new JSONUtil(
						"cn.grgbanking.feeltm.domain.PrjContract");
				JSONArray jsonObj = jsonUtil.toJSON(list, map);// 将查询数据转换为jsonArray，
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
				input.put("jsonObj", jsonObj); // 将数据以json的方式传入前台
				PrintWriter out = response.getWriter();
				out.print(input);
				return "";
			} else {
				ActionContext.getContext().put("prjContractlist", list);
				return "query";
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "query";
	}

	/**
	 * 刷新页面
	 */
	public void refresh() {
		try {
			query();
		} catch (Exception e) {
			e.printStackTrace();
		}
		;
	}

	/**
	 * 判断是否为部门经理以上
	 * 
	 * @param userId
	 * @return
	 */
	private boolean isManagerOrNot(String userId) {
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		return UserRoleConfig.upDeptManager(userModel);

	}
	/**
	 * 初始化下拉列表
	 * */
	private void initSelectData(){
		//获取项目名称
		List<Project> prjGroupList = projectService.listAllGroup();
		for(Iterator<Project> it = prjGroupList.iterator();it.hasNext();){
			groupNameList.add(it.next().getName());
		}
		List<SysUser> groupManager = staffInfoService.getGroupManager();
		request.setAttribute("groupManager", groupManager);
	}

	/**
	 * 跳转到添加通讯录信息页面
	 * 
	 * @return
	 */
	public String add() {
		initSelectData();
		return "add";
	}

	/**
	 * 保存添加的信息
	 * 
	 * @return
	 */
	public String save() {
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		/* PrjContract prjcontract = new PrjContract(); */
		MsgBox msgBox;
		try {

			Date date = new Date();
			/* BeanUtils.copyProperties(prjcontract, prjContract); */
			prjContract.setUpDate(date);
			prjContract.setStatus("新增");
			prjContract.setUpdateMan(userModel.getUsername());
			boolean flag = prjContractService.addPrjContractInfo(prjContract);
			if (flag == true) { // 添加是否成功
				SysLog.operLog(request, Constants.OPER_ADD_VALUE,
						prjContract.getUpdateMan());// 记录日志
				SysLog.info("User:" + userModel.getUserid()
						+ " add a UserContact : " + prjContract.getUpdateMan());
				msgBox = new MsgBox(request, getText("add.ok"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				this.addActionMessage(getText("add.ok"));
			} else {
				msgBox = new MsgBox(request, "添加合同信息失败");
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				this.addActionMessage("添加合同信息失败");
			}

		} catch (Exception e) {
			// e.printStackTrace();
			SysLog.error(request, "error in (ContactAction.java-save())");
			SysLog.error("User:" + userModel.getUserid()
					+ "failed to add a UserContact : "
					+ prjContract.getUpdateMan());
			SysLog.error(e);

			msgBox = new MsgBox(request, "添加合同信息失败");
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			this.addActionMessage("添加合同信息失败");
		}

		boolean hasActionMessage = this.hasActionMessages();
		request.setAttribute("hasActionMessage", hasActionMessage);

		return "msgBox";
	}

	/**
	 * 删除信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String delete() throws Exception {
		System.out.println("删除报销信息... ");
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		try {
			String[] delItem = StringUtils.split(request.getParameter("ids"),
					",");
			int iCount = 0;
			for (int i = 0; i < delItem.length; i++) {
				PrjContract prj = new PrjContract();
				prj = prjContractService.getPrjContractById(delItem[i]);
				if (prj.getStatus() != null && "新增".equals(prj.getStatus())) {
					MsgBox msgBox = new MsgBox(request,
							getText("不能删除状态为新增的合同信息！"));
					msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
					return "msgBox";
				}
				if (!prj.getUpdateMan().equals(userModel.getUsername())) {
					MsgBox msgBox = new MsgBox(request, getText(
							"operInfoform.updatefaile",
							new String[] { "没有权限删除此记录！" }));
					msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
					return "msgBox";
				} else {
					prjContractService.deletePrjContractInfo(delItem[i]);
					iCount++;
				}
			}
			MsgBox msgBox = new MsgBox(request, getText("rule.del.ok"), "rule",
					new String[] { String.valueOf(iCount) });
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request, "删除合同信息失败");
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		}
		return "msgBox";
	}

	/**
	 * 查看详情页面
	 * 
	 * @return
	 */
	public String view() {
		try {
			String prjContractId = request.getParameter("prjContractId");
			list = prjContractService.getListPrjContractById(prjContractId);
			ActionContext.getContext().put("prjContractList", list);

		} catch (Exception e) {
			MsgBox msgBox = new MsgBox(request, "查看详情失败！");
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}
		return "view";
	}

	/**
	 * 回款明细页面
	 * 
	 * @return
	 */
	public String payment() {
		try {
			String prjContractId = request.getParameter("prjContractId");
			paymentList = prjContractService
					.getListPrjPaymentById(prjContractId);
			prjContract = prjContractService.getPrjContractById(prjContractId);
			/*
			 * ActionContext.getContext().put("prjContractList", list);
			 * ActionContext.getContext().put("prjPaymentList", list);
			 */
		} catch (Exception e) {
			MsgBox msgBox = new MsgBox(request, "查看回款明细失败！");
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}
		return "payment";
	}

	/**
	 * 添加回款记录
	 */
	public String addPayment() {
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		MsgBox msgBox;
		try {
			Date date = new Date();
			boolean flag = false;
			boolean flag1 = false;
			//同时添加多条汇款记录
/*			Iterator<PrjContractPayment> it = paymentList.iterator();
			while (it.hasNext()) {
				PrjContractPayment payment = it.next();
				if (payment != null) {
					payment.setContractId(prjContract.getId());
					payment.setUpDateTime(date);
					prjContract.setUpdateMan(userModel.getUsername());
					prjContract.setUpDate(Calendar.getInstance().getTime());
					flag1 = prjContractService.updateContactInfo(prjContract);
					if(flag1==true){						
						flag = prjContractService.addPrjPaymentInfo(payment);
					}
				}
			}*/
			prjContractPayment.setContractId(prjContract.getId());
			prjContractPayment.setUpDateTime(date);
			prjContractPayment.setPrjName(prjContract.getPrjName());
			prjContractPayment.setClient(prjContract.getClient());
			prjContract.setUpdateMan(userModel.getUsername());
			prjContract.setUpDate(Calendar.getInstance().getTime());
			flag1 = prjContractService.updateContactInfo(prjContract);
			if(flag1==true){						
				flag = prjContractService.addPrjPaymentInfo(prjContractPayment);
			}
			if (flag == true && flag1==true) { // 添加是否成功
				SysLog.operLog(request, Constants.OPER_ADD_VALUE,
						prjContract.getUpdateMan());// 记录日志
				SysLog.info("User:" + userModel.getUserid()
						+ " add a UserContact : " + prjContract.getUpdateMan());
				msgBox = new MsgBox(request, getText("add.ok"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				this.addActionMessage(getText("add.ok"));
			} else {
				msgBox = new MsgBox(request, "添加回款记录失败");
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				this.addActionMessage("添加回款记录失败");
			}

		} catch (Exception e) {
			// e.printStackTrace();
			SysLog.error(request, "error in (ContactAction.java-save())");
			SysLog.error("User:" + userModel.getUserid()
					+ "failed to add a UserContact : "
					+ prjContract.getUpdateMan());
			SysLog.error(e);

			msgBox = new MsgBox(request, "添加回款记录失败");
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			this.addActionMessage("添加回款记录失败");
		}

		boolean hasActionMessage = this.hasActionMessages();
		request.setAttribute("hasActionMessage", hasActionMessage);

		return "msgBox";
	}

	/**
	 * 修改报销单
	 * 
	 * @return
	 * @throws Exception
	 */
	public String update() throws Exception {
		System.out.println("update()方法修改保存中....");
		try {
			UserModel userModel = (UserModel) request.getSession()
					.getAttribute(Constants.LOGIN_USER_KEY);
			prjContract.setUpdateMan(userModel.getUsername());
			prjContract.setUpDate(Calendar.getInstance().getTime());
			boolean flag = prjContractService.updateContactInfo(prjContract);
			if (flag == true) {
				MsgBox msgBox = new MsgBox(request,
						getText("operInfoform.updateok"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request, "修改合同信息失败");
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		}

		return "msgBox";
	}

	/**
	 * 进入编辑数据页面
	 * 
	 * @return
	 * @throws Exception
	 */
	public String edit() throws Exception {
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		//初始化下拉列表
		initSelectData();
		try {
			String Id = request.getParameter("Id");
			prjContract = prjContractService.getPrjContractById(Id);
			if (prjContract.getUpdateMan().equals(userModel.getUsername())) {
				request.setAttribute("Id", Id);
				return "edit";
			} else {
				MsgBox msgBox = new MsgBox(request, getText(
						"operInfoform.updatefaile",
						new String[] { "没有权限修改此记录！" }));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "edit";
	}

	public SysUserInfoService getSysUserInfoService() {
		return sysUserInfoService;
	}

	public void setSysUserInfoService(SysUserInfoService sysUserInfoService) {
		this.sysUserInfoService = sysUserInfoService;
	}

	public PrjContractService getPrjContractService() {
		return prjContractService;
	}

	public void setPrjContractService(PrjContractService prjContractService) {
		this.prjContractService = prjContractService;
	}

	public PrjContract getPrjContract() {
		return prjContract;
	}

	public void setPrjContract(PrjContract prjContract) {
		this.prjContract = prjContract;
	}

	public String getPageNum() {
		return pageNum;
	}

	public void setPageNum(String pageNum) {
		this.pageNum = pageNum;
	}

	public String getPageSize() {
		return pageSize;
	}

	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}

	public PrjContract getModel() {
		return prjContract;
	}

	public List<PrjContract> getList() {
		return list;
	}

	public void setList(List<PrjContract> list) {
		this.list = list;
	}

	public PrjContractPayment getPrjContractPayment() {
		return prjContractPayment;
	}

	public void setPrjContractPayment(PrjContractPayment prjContractPayment) {
		this.prjContractPayment = prjContractPayment;
	}

	public List<PrjContractPayment> getPaymentList() {
		return paymentList;
	}

	public void setPaymentList(List<PrjContractPayment> paymentList) {
		this.paymentList = paymentList;
	}
	public List<String> getGroupNameList() {
		return groupNameList;
	}

	public void setGroupNameList(List<String> groupNameList) {
		this.groupNameList = groupNameList;
	}

}
