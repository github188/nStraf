package cn.grgbanking.feeltm.transmgr.webapp;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.grgbanking.feeltm.config.BusnDataDir;
import cn.grgbanking.feeltm.domain.TranInfo;
import cn.grgbanking.feeltm.domain.TransBanknoteSeq;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.transmgr.service.TranInfoService;
import cn.grgbanking.feeltm.transmgr.service.TransBanknoteSeqService;
import cn.grgbanking.feeltm.util.DateUtil;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;

import com.opensymphony.xwork2.ActionContext;

@SuppressWarnings( { "unchecked", "serial" })
public class TransMgrAction extends BaseAction {
	private TranInfoService tranInfoService;
	private TransBanknoteSeqService transBanknoteSeqService;

	public TransBanknoteSeqService getTransBanknoteSeqService() {
		return transBanknoteSeqService;
	}

	public void setTransBanknoteSeqService(TransBanknoteSeqService transBanknoteSeqService) {
		this.transBanknoteSeqService = transBanknoteSeqService;
	}

	private String id;
	private String transCode;
	private String transOrgid;
	private String termid;
	private String transDate;
	private String transTime;
	private String accountNo;
	private String journalNo;
	private Integer transAmt;
	private Integer transResult;

	/** 新增字段 */
	private Integer noteNum; // 交易张数
	private Integer blNum; // 黑名单张数
	private Integer repeatNum; // 重号张数
	private Integer callBackNum;// 回收张数
	private String reserve1;
	private String reserve2;
	private String reserve3;

	// 新增字段，保存时间
	private String createDate;

	public String query() throws Exception {
		try {
			String from = request.getParameter("from");

			TranInfo trans = new TranInfo();
			String str_result = request.getParameter("transResult");
			if (str_result != null && !str_result.equals("")) {
				trans.setTransResult(str_result);
			}
			String accountNo = request.getParameter("accountNo");
			String beginDate = request.getParameter("beginDate");
			String endDate = request.getParameter("endDate");

			String journalNo = request.getParameter("journalNo");

			// Object[] obj=new Object[1];

			trans.setAccountNo(accountNo);
			trans.setJournalNo(journalNo);

			int pageNum = 1;
			int pageSize = 20;
			if (request.getParameter("pageNum") != null
					&& request.getParameter("pageNum").length() > 0)
				pageNum = Integer.parseInt(request.getParameter("pageNum"));
			if (request.getParameter("pageSize") != null
					&& request.getParameter("pageSize").length() > 0)
				pageSize = Integer.parseInt(request.getParameter("pageSize"));

			Page page = tranInfoService.getTranInfoPage(trans,
					pageNum, pageSize, beginDate, endDate);
			request.setAttribute("currPage", page);
			List<TranInfo> list = page.getQueryResult();
			for (int i = 0; i < list.size(); i++) {
				TranInfo temp = list.get(i);
				if(temp.getTransDate() == null || temp.getTransTime() == null) {
					temp.setTransTime(getText("audit.noFormatTime"));
				} else {
					temp.setTransTime(DateUtil.stringYYYYMMDDHHMMSSTo(temp
							.getTransDate()
							+ temp.getTransTime()));
				}
			}
			if (from != null && from.equals("refresh")) {
				List<Object> l = new ArrayList<Object>();
				for (int i = 0; i < list.size(); i++) {
					Map dataDirMap = BusnDataDir.getMap("transMgr.transResult");
					TranInfo transObj = (TranInfo) list.get(i);
					Object o = dataDirMap.get(String.valueOf(transObj
							.getTransResult()));
					if (o != null && !o.toString().equals(""))
						transObj.setTransResult(o.toString());
					l.add((Object) transObj);
				}
				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf(page.getPageCount()));
				map.put("recordCount", String.valueOf(page.getRecordCount()));
				
				// 不能直接将dataDirMap中的字符串值存入TransHourInfo对象的transResult中，只能另外添加一个字段存储
				// map.put("transResultString", transResultString);
				JSONUtil jsonUtil = new JSONUtil(
						"cn.grgbanking.feeltm.domain.TranInfo");
				
				
				JSONArray jsonObj = jsonUtil.toJSON(l, map);
				JSONObject input = new JSONObject();
				input.put("pageCount", String.valueOf(page.getPageCount()));
				input.put("recordCount", String.valueOf(page.getRecordCount()));
				input.put("jsonObj", jsonObj);								
				PrintWriter out = response.getWriter();
				out.print(input);
				return "";
			} else {
				ActionContext.getContext().put("tranInfoList", list);
				return "query";
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			return "msgBox";
		}
		//return "query";
	}

	public void refresh() {
		try {
			query();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String show() {
		try {
			TranInfo tranInfo = new TranInfo();
			String ids = request.getParameter("ids");
			tranInfo = tranInfoService.getTranInfoObject(ids);
			tranInfo.setTransTime(DateUtil
					.stringYYYYMMDDHHMMSSTo(tranInfo.getTransDate()
							+ tranInfo.getTransTime()));
			List<TransBanknoteSeq> transBanknoteSeqList = transBanknoteSeqService
					.getTransBanknoteSeqByTranId(tranInfo.getId());
			List<TransBanknoteSeq> transBanknoteSeqList1 = new ArrayList<TransBanknoteSeq>();
			List<TransBanknoteSeq> transBanknoteSeqList2 = new ArrayList<TransBanknoteSeq>();
			for(TransBanknoteSeq t:transBanknoteSeqList){
				if("0".equals(t.getNoteFlag())){
					transBanknoteSeqList1.add(t);
				}
				if("1".equals(t.getNoteFlag())){
					transBanknoteSeqList2.add(t);
				}
			}
			ActionContext.getContext().put("transBanknoteSeqList1", transBanknoteSeqList1);
			ActionContext.getContext().put("transBanknoteSeqList2", transBanknoteSeqList2);
			ActionContext.getContext().put("listSize1", transBanknoteSeqList1.size());
			ActionContext.getContext().put("listSize2", transBanknoteSeqList2.size());
			ActionContext.getContext().put("transBanknoteSeqList",
					transBanknoteSeqList);
			ActionContext.getContext().put("listSize",
					transBanknoteSeqList.size()); //列表大小
			ActionContext.getContext().put("tranInfo", tranInfo);
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request, e.toString());
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}
		return "show";

	}

	public TranInfoService getTranInfoService() {
		return tranInfoService;
	}

	public void setTranInfoService(TranInfoService tranInfoService) {
		this.tranInfoService = tranInfoService;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTransCode() {
		return transCode;
	}

	public void setTransCode(String transCode) {
		this.transCode = transCode;
	}

	public String getTransOrgid() {
		return transOrgid;
	}

	public void setTransOrgid(String transOrgid) {
		this.transOrgid = transOrgid;
	}

	public String getTermid() {
		return termid;
	}

	public void setTermid(String termid) {
		this.termid = termid;
	}

	public String getTransDate() {
		return transDate;
	}

	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}

	public String getTransTime() {
		return transTime;
	}

	public void setTransTime(String transTime) {
		this.transTime = transTime;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getJournalNo() {
		return journalNo;
	}

	public void setJournalNo(String journalNo) {
		this.journalNo = journalNo;
	}

	public Integer getTransAmt() {
		return transAmt;
	}

	public void setTransAmt(Integer transAmt) {
		this.transAmt = transAmt;
	}

	public Integer getTransResult() {
		return transResult;
	}

	public void setTransResult(Integer transResult) {
		this.transResult = transResult;
	}

	public Integer getNoteNum() {
		return noteNum;
	}

	public void setNoteNum(Integer noteNum) {
		this.noteNum = noteNum;
	}

	public Integer getBlNum() {
		return blNum;
	}

	public void setBlNum(Integer blNum) {
		this.blNum = blNum;
	}

	public Integer getRepeatNum() {
		return repeatNum;
	}

	public void setRepeatNum(Integer repeatNum) {
		this.repeatNum = repeatNum;
	}

	public Integer getCallBackNum() {
		return callBackNum;
	}

	public void setCallBackNum(Integer callBackNum) {
		this.callBackNum = callBackNum;
	}

	public String getReserve1() {
		return reserve1;
	}

	public void setReserve1(String reserve1) {
		this.reserve1 = reserve1;
	}

	public String getReserve2() {
		return reserve2;
	}

	public void setReserve2(String reserve2) {
		this.reserve2 = reserve2;
	}

	public String getReserve3() {
		return reserve3;
	}

	public void setReserve3(String reserve3) {
		this.reserve3 = reserve3;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
}
