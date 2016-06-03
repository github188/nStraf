package cn.grgbanking.feeltm.rule.webapp;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.grgbanking.feeltm.config.BusnDataDir;
import cn.grgbanking.feeltm.domain.SpecialRegulation;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.rule.service.SpecialRegulationService;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.DateUtil;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;

import com.opensymphony.xwork2.ActionContext;

@SuppressWarnings({"serial", "unchecked"})
public class SpecialRegulationAction extends BaseAction {
	private SpecialRegulationService specialRegulationService;

	public SpecialRegulationService getSpecialRegulationService() {
		return specialRegulationService;
	}

	public void setSpecialRegulationService(
			SpecialRegulationService specialRegulationService) {
		this.specialRegulationService = specialRegulationService;
	}

	private String id;
	private String applyId;
	private String orgid;
	private String moneyType;
	private String moneyDenomination;
	private String regulation;
	private String source;
	private String result;
	private String createDate;
	private String createName;
	private String specialType;

	public String add() throws Exception {
		return "add";
	}

	public String save() throws Exception {
//		UserModel userModel = (UserModel) request.getSession().getAttribute(
//				Constants.LOGIN_USER_KEY);
		String newAnOther = request.getParameter("newAnOther");
		try {
			SpecialRegulation specialRegulation = new SpecialRegulation();
			// specialRegulation.setId(getId());
			specialRegulation.setApplyId(getApplyId());
			specialRegulation.setOrgid(getOrgid());
			specialRegulation.setMoneyType(getMoneyType());
			specialRegulation.setMoneyDenomination(getMoneyDenomination());
			specialRegulation.setRegulation(getRegulation());
			specialRegulation.setSource(getSource());
			specialRegulation.setResult(getResult());

			specialRegulation.setCreateDate(DateUtil
					.getTimeYYYYMMDDHHMMSSString(new Date()));
			specialRegulation.setCreateName(getCreateName());
			specialRegulation.setSpecialType(getSpecialType());
			boolean flag = false;
			flag = specialRegulationService
					.addSpecialRegulation(specialRegulation);
			if (flag == true) {
				MsgBox msgBox = new MsgBox(request, getText("add.ok"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				this.addActionMessage(getText("add.ok"));
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
			if (newAnOther.equals("true")) {
				return "add";
			}
			return "msgBox";
		}
		boolean hasActionMessage = this.hasActionMessages();
		request.setAttribute("hasActionMessage", hasActionMessage);
		if (newAnOther.equals("true")) {
			// 得到业务主管用户组中用户列表，并保存
			return "add";
		}
		return "msgBox";
	}

	public String delete() throws Exception {
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		try {
			SpecialRegulation specialRegulation = new SpecialRegulation();
			String ids = request.getParameter("ids");
			//System.out.println(ids);
			specialRegulation.setId(ids);
			int iCount = 0;
			String[] sids = specialRegulation.getId().split(",");
			if("all".equals(sids[0])){
				String[] arr = new String[sids.length-1];
				System.arraycopy(sids, 1, arr, 0, arr.length);
				sids = arr;
			}
			if (sids != null) {
				for (int i = 0; i < sids.length; i++) {
					SpecialRegulation temp = specialRegulationService
							.getSpecialRegulationObject(sids[i]);
					specialRegulationService.deleteSpecialRegulation(temp);
					SysLog.operLog(request, Constants.OPER_MODIFY_VALUE, temp
							.getRegulation());
					SysLog.info("User:" + userModel.getUserid()
							+ " to delete a SpecialRegulation: "
							+ temp.getRegulation());

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

	public String edit() throws Exception {
		try {
			SpecialRegulation specialRegulation = new SpecialRegulation();
			String ids = request.getParameter("ids");
			specialRegulation = specialRegulationService
					.getSpecialRegulationObject(ids);
			List<SpecialRegulation> list = new ArrayList<SpecialRegulation>();
			list.add(specialRegulation);
			ActionContext.getContext().put("specialRegulation",
					list);
			String specialType = specialRegulation.getSpecialType();
			String regulation = specialRegulation.getRegulation();
			request.setAttribute("specialType", specialType);
		request.setAttribute("regulation", regulation);
			ActionContext.getContext().put("specialType", specialType);
		ActionContext.getContext().put("regulation", regulation);
			//System.out.println(specialType+"-"+regulation);
			System.out.println(specialType.length());
			System.out.println(specialType);
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "edit";
	}

	public String update() throws Exception {
		try {
			SpecialRegulation specialRegulation = new SpecialRegulation();
			specialRegulation = specialRegulationService
					.getSpecialRegulationObject(getId());
			//specialRegulation.setId(getId());
			specialRegulation.setApplyId(getApplyId());
			specialRegulation.setOrgid(getOrgid());
			specialRegulation.setMoneyType(getMoneyType());
			specialRegulation.setMoneyDenomination(getMoneyDenomination());
			specialRegulation.setRegulation(getRegulation());
			specialRegulation.setSource(getSource());
			specialRegulation.setResult(getResult());
			specialRegulation.setCreateDate(DateUtil.getTimeYYYYMMDDHHMMSSString(new Date()));
			specialRegulation.setCreateName(getCreateName());
			//specialRegulation.setSpecialType(getSpecialType());
			boolean flag = specialRegulationService
					.updateSpecialRegulation(specialRegulation);
			if (flag == true) {
				MsgBox msgBox = new MsgBox(request,
						getText("operInfoform.updateok"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
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
		try {
			query();
		} catch (Exception e) {
			e.printStackTrace();
		}
		;
	}

	public String query() throws Exception {
//		UserModel userModel = (UserModel) request.getSession().getAttribute(
//				Constants.LOGIN_USER_KEY);
		try {

			String from = request.getParameter("from");
			SpecialRegulation specialRegulation = new SpecialRegulation();
			int pageNum = 1;
			int pageSize = 20;
			if (request.getParameter("pageNum") != null
					&& request.getParameter("pageNum").length() > 0)
				pageNum = Integer.parseInt(request.getParameter("pageNum"));
			if (request.getParameter("pageSize") != null
					&& request.getParameter("pageSize").length() > 0)
				pageSize = Integer.parseInt(request.getParameter("pageSize"));
			String source = request.getParameter("source");

			if (source != null && !"".equals(source)) {
				specialRegulation.setSource(source);
			}
			Page page = specialRegulationService.getSpecialRegulationPage(
					specialRegulation, pageNum, pageSize);
			request.setAttribute("currPage", page);

			List<SpecialRegulation> list = page.getQueryResult();

			for (SpecialRegulation li : list) {
				String createDate = li.getCreateDate();
				if (createDate != null && !"".equals(createDate)) {
					li.setCreateDate(DateUtil
							.stringYYYYMMDDHHMMSSTo(createDate));
				}
			}
			if (from != null && from.equals("refresh")) {
				List<Object> l = new ArrayList<Object>();
				Map dataDirMap = BusnDataDir.getMap("transMgr.cometype");
				for (int i = 0; i < list.size(); i++) {
					SpecialRegulation specialOb = list.get(i);
					Object o = dataDirMap.get(specialOb.getSource());
					if (o != null && !"".equals(o.toString())) {
						specialOb.setSource(o.toString());
					}
					l.add((Object) specialOb);
				}
				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf(page.getPageCount()));
				map.put("recordCount", String.valueOf(page.getRecordCount()));
				JSONUtil jsonUtil = new JSONUtil(
						"cn.grgbanking.feeltm.domain.SpecialRegulation");
				JSONArray jsonObj = jsonUtil.toJSON(l, map);
				JSONObject input = new JSONObject();
				input.put("pageCount", String.valueOf(page.getPageCount()));
				input.put("recordCount", String.valueOf(page.getRecordCount()));
				input.put("jsonObj", jsonObj);								
				PrintWriter out = response.getWriter();
				out.print(input);
				return "";
			} else {
				ActionContext.getContext().put("specialRegulationList", list);

				return "query";
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "query";
	}

	public String show() throws Exception {
		try {
			SpecialRegulation specialRegulation = new SpecialRegulation();
			String ids = request.getParameter("ids");
			specialRegulation = specialRegulationService
					.getSpecialRegulationObject(ids);
			ActionContext.getContext().put("specialRegulation",
					specialRegulation);
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "show";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getApplyId() {
		return applyId;
	}

	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}

	public String getOrgid() {
		return orgid;
	}

	public void setOrgid(String orgid) {
		this.orgid = orgid;
	}

	public String getMoneyType() {
		return moneyType;
	}

	public void setMoneyType(String moneyType) {
		this.moneyType = moneyType;
	}

	public String getMoneyDenomination() {
		return moneyDenomination;
	}

	public void setMoneyDenomination(String moneyDenomination) {
		this.moneyDenomination = moneyDenomination;
	}

	public String getRegulation() {
		return regulation;
	}

	public void setRegulation(String regulation) {
		this.regulation = regulation;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	public String getSpecialType() {
		return specialType;
	}

	public void setSpecialType(String specialType) {
		this.specialType = specialType;
	}

}
