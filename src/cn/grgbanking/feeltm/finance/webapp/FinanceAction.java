package cn.grgbanking.feeltm.finance.webapp;

import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.grgbanking.feeltm.domain.testsys.DepartFinance;
import cn.grgbanking.feeltm.finance.domain.BalanceInfo;
import cn.grgbanking.feeltm.finance.service.FinanceService;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;

import com.opensymphony.xwork2.ActionContext;

public class FinanceAction extends BaseAction {
	private FinanceService financeService;

	private String start;
	private String end;
	/**
	 * type 0为全部，1为支出，2 为收入
	 */
	private String type;
	private String responsible;

	private List<DepartFinance> finances;
	private DepartFinance finance;
	private BalanceInfo info;

	public String add() {
		return "add";
	}

	public String save() throws Exception {
		try {
			boolean flag = false;
			// SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			// behavior.setModifyDate(f.format(new Date()));
			flag = financeService.add(finance);
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
			return "msgBox";
		}
		boolean hasActionMessage = this.hasActionMessages();
		request.setAttribute("hasActionMessage", hasActionMessage);
		return "msgBox";
	}

	public String delete() throws Exception {
		try {
			String ids = request.getParameter("ids");
			System.out.println("ids: " + ids);
			int iCount = 0;
			String[] sids = ids.split(",");
			if ("all".equals(sids[0])) {
				String[] arr = new String[sids.length - 1];
				System.arraycopy(sids, 1, arr, 0, arr.length);
				sids = arr;
			}
			if (sids != null) {
				for (int i = 0; i < sids.length; i++) {
					DepartFinance temp = financeService.getDetailById(sids[i]);
					financeService.delete(temp);
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
			String ids = request.getParameter("ids");
			finance = financeService.getDetailById(ids);
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "edit";
	}

	public String update() throws Exception {
		try {
			// SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			// behavior.setModifyDate(f.format(new Date()));
			boolean flag = financeService.update(finance);
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
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		try {
			String from = request.getParameter("from");
			// ReportDayInfo info=new ReportDayInfo()

			int pageNum = 1;
			int pageSize = 20;
			if (request.getParameter("pageNum") != null
					&& request.getParameter("pageNum").length() > 0)
				pageNum = Integer.parseInt(request.getParameter("pageNum"));
			if (request.getParameter("pageSize") != null
					&& request.getParameter("pageSize").length() > 0)
				pageSize = Integer.parseInt(request.getParameter("pageSize"));
			Page page = financeService.getPage(start, end, type, responsible,
					pageNum, pageSize);
			request.setAttribute("currPage", page);
			List<Object> list = (List<Object>) page.getQueryResult();
			
			double paySum=0;
			double incomeSum=0;
			for (int i = 0; i < list.size(); i++) {
				DepartFinance tmp=(DepartFinance)list.get(i);
				paySum+=tmp.getPay();
				incomeSum+=tmp.getIncome();
			}
			
			DecimalFormat forma=(DecimalFormat)DecimalFormat.getInstance();
			forma.applyPattern("0.00");
			paySum=Double.parseDouble(forma.format(paySum));
			incomeSum=Double.parseDouble(forma.format(incomeSum));
			
			DepartFinance f=new DepartFinance();
			f.setActivity("合计");
			f.setIncome(incomeSum);
			f.setPay(paySum);
			list.add(f);
			
			if (from != null && from.equals("refresh")) {
				 
				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf(page.getPageCount()));
				map.put("recordCount", String.valueOf(page.getRecordCount()));
				JSONUtil jsonUtil = new JSONUtil(
						"cn.grgbanking.feeltm.domain.testsys.DepartFinance");
				JSONArray jsonObj = jsonUtil.toJSON(list, map);
				JSONObject input = new JSONObject();
				input.put("pageCount", String.valueOf(page.getPageCount()));
				input.put("recordCount", String.valueOf(page.getRecordCount()));
				input.put("jsonObj", jsonObj);
				PrintWriter out = response.getWriter();
				out.print(input);
				return "";
			} else {
				ActionContext.getContext().put("financesList", list);
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
			String ids = request.getParameter("ids");
			finance = financeService.getDetailById(ids);
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "show";
	}
	
	public String getBalance(){
		info=financeService.getBalance(start, end);
		return "balance";
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

	public void setEnd(String end) {
		this.end = end;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getResponsible() {
		return responsible;
	}

	public void setResponsible(String responsible) {
		this.responsible = responsible;
	}

	public List<DepartFinance> getFinances() {
		return finances;
	}

	public void setFinances(List<DepartFinance> finances) {
		this.finances = finances;
	}

	public DepartFinance getFinance() {
		return finance;
	}

	public void setFinance(DepartFinance finance) {
		this.finance = finance;
	}
	
	public BalanceInfo getInfo() {
		return info;
	}

	public void setInfo(BalanceInfo info) {
		this.info = info;
	}

	public FinanceService getFinanceService() {
		return financeService;
	}

	public void setFinanceService(FinanceService financeService) {
		this.financeService = financeService;
	}

}
