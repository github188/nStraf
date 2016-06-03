/**
 * 
 */
package cn.grgbanking.feeltm.org.webapp;

import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import cn.grgbanking.feeltm.config.BusnDataDir;
import cn.grgbanking.feeltm.domain.OrgInfo;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.org.service.OrgInfoService;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.webapp.BaseAction;

import com.opensymphony.xwork2.ActionContext;

@SuppressWarnings( { "serial", "unchecked" })
public class OrgInfoAction extends BaseAction {
	private OrgInfoService orgInfoService;

	private String id;

	private String orgid;

	private String parentid;

	private String orgname;

	private String orgfullname;

	private String orgnameEn;

	private String contact;

	private String tel;

	private String address;

	private int order;

	private int level;

	private int childnum;

	private String nowstatus;

	private String remark;

	private String ifreckon;

	private String orgLevel;

	private String orgfloor;

	public void refresh() {
		list();
	}

	public String list() {
		String menuid = request.getParameter("menuid");
		if (menuid == null) {
			menuid = (String) request.getSession().getAttribute("org.menuid");
		}
		request.getSession().setAttribute("org.menuid", menuid);

		try {
			this.getOrgList();

			String from = request.getParameter("from");
			if (from != null && from.equals("refresh"))// 刷新
				return "";
			else
				return "list";
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(request, "failed to delete org");
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			return "msgBox";
		}
	}

	public void getOrgList() throws Exception {
		OrgInfo orgInfo = new OrgInfo();

		if (request.getParameter("parentid") == null
				|| request.getParameter("parentid").equals("")) {
			orgInfo.setParentid(OrgInfoService.TOP_PARENTID);
		} else
			orgInfo.setParentid(request.getParameter("parentid"));

		request.getSession().setAttribute("orgInfoForms", orgInfo);

		ActionContext.getContext().put("orgInfoForms", orgInfo);
		ActionContext.getContext()
				.put(
						"countAllNum",
						new Integer(orgInfoService.getCountChild(orgInfo
								.getParentid())));
		this.getOrgName(orgInfo);
		List list = orgInfoService.getOrgChildList(orgInfo.getParentid());

		ActionContext.getContext().put("orgInfoList", list);

		String from = request.getParameter("from");// 刷新
		if (from != null && from.equals("refresh")) {
			Map dataDirMap = BusnDataDir.getMap("tmlMgr.ifreckon");

			List<Object> l = new ArrayList<Object>();
			for (int i = 0; i < list.size(); i++) {
				OrgInfo orgObj = (OrgInfo) list.get(i);
				Object o = dataDirMap.get(orgObj.getIfreckon());
				if (o != null)
					orgObj.setIfreckon(o.toString());
				l.add((Object) orgObj);
			}

			JSONUtil jsonUtil = new JSONUtil(
					"cn.grgbanking.feeltm.domain.OrgInfo");
			JSONArray jsonObj = jsonUtil.toJSON(l, null);
			JSONObject input = new JSONObject();
			input.put("pageCount", 1);
			input.put("recordCount", l.size());
			input.put("jsonObj", jsonObj);								
			PrintWriter out = response.getWriter();
			out.print(input);
		}
	}

	public String back() {
		try {
			this.returnSuperior();
			return "list";
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(request, "返回上一级失败");
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			return "msgBox";
		}
	}

	public void returnSuperior() throws Exception {
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		//String parentid = null;

		OrgInfo orgInfo = new OrgInfo();

		String parentId;
		if (request.getParameter("parentid") != null
				&& request.getParameter("parentid").equals("Top_Parentid"))// 已经到了最顶层
			parentId = "Top_Parentid";
		else
			parentId = orgInfoService.findParentid(request
					.getParameter("parentid"));

		ActionContext.getContext().put("countAllNum",
				new Integer(orgInfoService.getCountChild(parentId)));

		orgInfo.setParentid(parentId);

		OrgInfo oi = orgInfoService.getOrgInfoByOrgId(userModel.getOrgid());
		parentid = oi.getParentid();

		orgInfo.setParentid(parentId);
		List orgInfoList = orgInfoService
				.getOrgChildList(orgInfo.getParentid());

		request.getSession().setAttribute("orgInfoForms", orgInfo);
		request.getSession().setAttribute("orgInfoList", orgInfoList);

		ActionContext.getContext().put("orgInfoForms", orgInfo);
		ActionContext.getContext().put("orgInfoList", orgInfoList);

		this.getOrgName(orgInfo);
	}

	public String query() {
		try {
			this.queryChildrenOrg();
			return "list";
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(request, "failed to query org info");
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			return "msgBox";
		}
	}

	/**
	 * query children orgInfo
	 * 
	 * @param orgInfoManager
	 */
	public void queryChildrenOrg() throws Exception {
		this.getOrgList();
	}

	public void getOrgName(OrgInfo info) {
		String parentName = "";
		parentName = orgInfoService.findParentName(info.getParentid());
		if (parentName != null) {
			request.getSession().setAttribute("parentName", parentName);
			ActionContext.getContext().put("parentName", parentName);
		}
	}

	public String openInsertPage() {
		return "insert";
	}

	public String insert() {
		return insertOrgInfo();
	}

	/**
	 * add orgInfo
	 * 
	 */
	public String insertOrgInfo() {
		String newAnOther = request.getParameter("newAnOther");

		MsgBox msg;

		try {
			OrgInfo orgInfo = new OrgInfo();
			orgInfo.setOrgLevel(this.getOrgLevel());
			orgInfo.setOrgid(this.getOrgid());
			orgInfo.setOrgname(this.getOrgname());
			orgInfo.setOrgfullname(this.getOrgfullname());
			orgInfo.setContact(this.getContact());
			orgInfo.setTel(this.getTel());
			orgInfo.setAddress(this.getAddress());
			orgInfo.setParentid(this.getParentid());

//			UserModel userModel = (UserModel) request.getSession()
//					.getAttribute(Constants.LOGIN_USER_KEY);
			//String username = userModel.getUsername();

			if (orgInfo.getParentid().equals("")
					|| orgInfo.getParentid() == null)
				orgInfo.setParentid(OrgInfoService.TOP_PARENTID);

			orgInfo.setLevel(orgInfoService.getChildLevel(orgInfo));
			orgInfo.setOrder(orgInfoService.getChildOrder(orgInfo));

			boolean flag = false;
			if (orgInfoService.isOnlyOrgid(orgInfo)) {
				orgInfo.setNowstatus("0");// 状态默认设置为正常营业状态
				// 最顶层，增加机构，没有父机构，只要修改同级的order
				if (orgInfo.getParentid().equals(OrgInfoService.TOP_PARENTID)) {
					orgInfoService.addOrgInfo(orgInfo, null);
				} else {
					OrgInfo parentOrgInfo = orgInfoService
							.getOrgInfoByOrgId(orgInfo.getParentid());
					flag = orgInfoService.addOrgInfo(orgInfo, parentOrgInfo);
				}

				if (flag == true) {
					SysLog.operLog(request, Constants.OPER_ADD_VALUE, "网点:"
							+ orgInfo.getOrgid());// 记录日志
				} else {
					// msg = new MsgBox(request,getText("orginfo.exist"));
					// this.addActionMessage(getText("orginfo.exist"));
				}

				this.addActionMessage(getText("add.ok"));
				msg = new MsgBox(request, getText("add.ok"));
				SysLog.info(request, "add new org:" + orgInfo.getOrgid()
						+ "successfully.");
			} else {
				this.addActionMessage(getText("orginfo.exist"));
				msg = new MsgBox(request, getText("orginfo.exist"));
			}

		} catch (Exception e) {
			this.addActionMessage(getText("orginfo.exist"));
			msg = new MsgBox(request, getText("orginfo.exist"));
			e.printStackTrace();
			SysLog.error(request, "failed to add new org");
		}

		boolean hasActionMessage = this.hasActionMessages();
		request.setAttribute("hasActionMessage", hasActionMessage);

		if (newAnOther.equals("false")) {
			msg.setBackUrl(request.getContextPath()
					+ "/pages/org/orgInfo_list.jsp");
			msg.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}
		return "insert";
	}

	public String modify() {
		try {
			String orgid = request.getParameter("orgid");
			
			OrgInfo orgInfo = orgInfoService.getOrgInfoByOrgId(orgid);
			String contact = orgInfo.getContact();
			String tel = orgInfo.getTel();
			String address = orgInfo.getAddress();
			if(contact==null||"".equals(contact)){
				orgInfo.setContact("");
			}
			if(tel==null||"".equals(tel)){
				orgInfo.setTel("");
			}
			if(address==null||"".equals(address)){
				orgInfo.setAddress("");
			}
			request.getSession().setAttribute("orgInfoForms", orgInfo);
			ActionContext.getContext().put("orgInfoForms", orgInfo);

			this.getOrgName(orgInfo);
			return "modify";
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(request, "error in (OrgInfoAction.java-modify())");
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}
	}

	public String update() {
		try {
			this.updateOrgInfo();
			return "msgBox";
		} catch (Exception e) {
			MsgBox msg = new MsgBox(request, getText("audit.ActionFailed"));
			msg.setButtonType(MsgBox.BUTTON_CLOSE);
			e.printStackTrace();
			SysLog.error(request, "error in (OrgInfoAction.java-update())");
			SysLog.error(e);

			return "msgBox";
		}
	}

	/**
	 * modify orginfo
	 * 
	 * @throws ParseException
	 */
	public void updateOrgInfo() throws ParseException {
		String oldOrgid = request.getParameter("oldorgid");
		OrgInfo orgInfo = orgInfoService.getOrgInfoByOrgId(oldOrgid);
		String cid = this.getOrgid();
		if(cid!=null&&!"".equals(cid))
		orgInfo.setOrgid(this.getOrgid());
		orgInfo.setOrgname(this.getOrgname());
		orgInfo.setOrgfullname(this.getOrgfullname());
		orgInfo.setContact(this.getContact());
		orgInfo.setTel(this.getTel());
		orgInfo.setAddress(this.getAddress());
		
		int i = 0;
		i = orgInfoService.updateOrgInfo(orgInfo);
		if (i == 1) {

			MsgBox msg = new MsgBox(request, getText("modify.ok"));
			msg.setButtonType(MsgBox.BUTTON_CLOSE);
		} else {
			MsgBox msg = new MsgBox(request, getText("operator.addfaile"));
			msg.setButtonType(MsgBox.BUTTON_CLOSE);
		}

		if (i == 1) {
			SysLog.info(request, "modify the bank's information successfully.");
		}

	}

	public String delete() {
		return this.removeOrgInfo();
	}

	/**
	 * remove orgInfo
	 * 
	 * @param orgInfoManager
	 */
	public String removeOrgInfo() {
		int j = 0;
		boolean flag = false;
//		UserModel userModel = (UserModel) request.getSession().getAttribute(
//				Constants.LOGIN_USER_KEY);
//		String username = userModel.getUsername();
		String[] ids = StringUtils.split(request.getParameter("deleteOrgid"),
				",");
//		for(String s:ids){
//			System.out.print(s+"\t");
//		}
		try {
			if (ids.length > 0) {
				if (this.isRemoveInfo(ids))// 如果删除的机构有子机构，则不能删除
				{
					// msgs.add("deleteOk",new ActionMessage("orginfo.delete"));
					MsgBox msgBox = new MsgBox(request, getText("org.hadSons"));
					msgBox.setButtonType(MsgBox.BUTTON_RETURN);
					return "msgBox";
				} else {
					for (int i = 0; i < ids.length; i++) {
						OrgInfo orgInfo = orgInfoService
								.getOrgInfoByOrgId(ids[i]);
						OrgInfo parentOrgInfo = orgInfoService
								.getOrgInfoByOrgId(orgInfo.getParentid());
						flag = orgInfoService.removeOrgInfo(orgInfo,
								parentOrgInfo);
						SysLog.operLog(request, Constants.OPER_DELETE_VALUE,
								orgInfo.getOrgname());// 记录日志
						SysLog.info(request, "delete org:"
								+ orgInfo.getOrgname() + " successfully.");

						if (ids[i] != null)
							j++;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(request, "failed to delete org");
			SysLog.error(e);
			flag = false;
		}

		if (flag == true) {
			MsgBox msgBox = new MsgBox(request, getText("back.del.ok"), "org",
					new String[] { String.valueOf(j) });
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		} else {
			MsgBox msgBox = new MsgBox(request, getText("operator.deletefaile"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
		}
		return "msgBox";

	}

	public boolean isRemoveInfo(String[] ids) {
		boolean result = false;
		for (int i = 0; i < ids.length; i++) {
			if (orgInfoService.getCountChild(ids[i]) > 0) {
				result = true;
				break;
			}

		}
		return result;
	}

	public OrgInfoService getorgInfoService() {
		return orgInfoService;
	}

	public void setOrgInfoService(OrgInfoService orgInfoService) {
		this.orgInfoService = orgInfoService;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrgid() {
		return orgid;
	}

	public void setOrgid(String orgid) {
		this.orgid = orgid;
	}

	public String getParentid() {
		return parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	public String getOrgname() {
		return orgname;
	}

	public void setOrgname(String orgname) {
		this.orgname = orgname;
	}

	public String getOrgfullname() {
		return orgfullname;
	}

	public void setOrgfullname(String orgfullname) {
		this.orgfullname = orgfullname;
	}

	public String getOrgnameEn() {
		return orgnameEn;
	}

	public void setOrgnameEn(String orgnameEn) {
		this.orgnameEn = orgnameEn;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = Integer.parseInt(level);
	}

	public int getChildnum() {
		return childnum;
	}

	public void setChildnum(int childnum) {
		this.childnum = childnum;
	}

	public String getNowstatus() {
		return nowstatus;
	}

	public void setNowstatus(String nowstatus) {
		this.nowstatus = nowstatus;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getIfreckon() {
		return ifreckon;
	}

	public void setIfreckon(String ifreckon) {
		this.ifreckon = ifreckon;
	}

	public String getOrgLevel() {
		return orgLevel;
	}

	public void setOrgLevel(String orgLevel) {
		this.orgLevel = orgLevel;
	}

	public String getOrgfloor() {
		return orgfloor;
	}

	public void setOrgfloor(String orgfloor) {
		this.orgfloor = orgfloor;
	}
}
