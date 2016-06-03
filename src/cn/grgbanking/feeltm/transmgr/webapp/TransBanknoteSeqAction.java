package cn.grgbanking.feeltm.transmgr.webapp;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.grgbanking.feeltm.config.BusnDataDir;
import cn.grgbanking.feeltm.domain.TranInfo;
import cn.grgbanking.feeltm.domain.TransBanknoteSeq;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.parseMsg.Transfer;
import cn.grgbanking.feeltm.parseMsg.appMessage.R2002Msg;
import cn.grgbanking.feeltm.parseMsg.appMessage.S2002Msg;
import cn.grgbanking.feeltm.transmgr.service.TranInfoService;
import cn.grgbanking.feeltm.transmgr.service.TransBanknoteSeqService;
import cn.grgbanking.feeltm.util.DateUtil;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;

import com.opensymphony.xwork2.ActionContext;

@SuppressWarnings( { "serial", "unchecked" })
public class TransBanknoteSeqAction extends BaseAction {
	private TransBanknoteSeqService transBanknoteSeqService;
	private TranInfoService tranInfoService;

	public TranInfoService getTranInfoService() {
		return tranInfoService;
	}

	public void setTranInfoService(TranInfoService tranInfoService) {
		this.tranInfoService = tranInfoService;
	}

	public TransBanknoteSeqService getTransBanknoteSeqService() {
		return transBanknoteSeqService;
	}

	public void setTransBanknoteSeqService(
			TransBanknoteSeqService transBanknoteSeqService) {
		this.transBanknoteSeqService = transBanknoteSeqService;
	}

	private String checkResult;
	private String tranId;
	private String noteFlag;
	private String noteType;
	private String createDate;
	private String id;
	private String termid;
	private String journalNo;
	private Integer sequence;
	private String currency;
	private String denomination;
	private String cashBoxId;
	private String seriaNo;
	private String pictureName;
	private String verifyNo;
	private String urlName;
	private String transDate;
	private String tranTime;

	public String query() {
		try {
			String from = request.getParameter("from");
			String seriaNo = request.getParameter("seriaNo");
			// Object[] obj=new Object[1];
			TransBanknoteSeq obj = new TransBanknoteSeq();
			if (seriaNo != null && !seriaNo.equals("")) {
				obj.setSeriaNo(seriaNo);
			}

			String beginDate = request.getParameter("beginDate");
			String endDate = request.getParameter("endDate");

			int pageNum = 1;
			int pageSize = 20;
			if (request.getParameter("pageNum") != null
					&& request.getParameter("pageNum").length() > 0)
				pageNum = Integer.parseInt(request.getParameter("pageNum"));
			if (request.getParameter("pageSize") != null
					&& request.getParameter("pageSize").length() > 0)
				pageSize = Integer.parseInt(request.getParameter("pageSize"));

			Page page = transBanknoteSeqService.getTransBanknoteSeqPage(obj,
					pageNum, pageSize, beginDate, endDate);

			request.setAttribute("currPage", page);
			List<TransBanknoteSeq> list = page.getQueryResult();
			
			for (int i = 0; i < list.size(); i++) {
				TransBanknoteSeq temp = list.get(i);
				if(temp.getTransDate() == null || temp.getTranTime() == null) {
					temp.setTranTime(getText("audit.noFormatTime"));
				} else {
					temp.setTranTime(DateUtil.stringYYYYMMDDHHMMSSTo(temp
							.getTransDate()
							+ temp.getTranTime()));
				}
			}

			if (from != null && from.equals("refresh")) {
				List<Object> l = new ArrayList<Object>();
				for (int i = 0; i < list.size(); i++) {
					TransBanknoteSeq tmp = list.get(i);
					Map<String, String> noteFlagMap = BusnDataDir
							.getMap("transMgr.noteFlag"); // 钞票标识数据字典Map
					Map<String, String> noteTypeMap = BusnDataDir
							.getMap("transMgr.noteType"); // 钞票类别数据字典Map
					Map<String, String> denominationMap = BusnDataDir
							.getMap("ruleMgr.moneyDenomination"); // 钞票面额数据字典Map
					Object flagObj = noteFlagMap.get(tmp.getNoteFlag());
					Object typeObj = noteTypeMap.get(tmp.getNoteType());
					Object denominationObj = denominationMap.get(tmp
							.getDenomination());
					if (flagObj != null && !flagObj.toString().equals(""))
						tmp.setNoteFlag(flagObj.toString());
					if (typeObj != null && !typeObj.toString().equals("")) {
						tmp.setNoteType(typeObj.toString());
					} else {
						tmp.setNoteType(getText("audit.dontKnow"));
					}
					if (denominationObj != null
							&& !denominationObj.toString().equals("")){
						tmp.setDenomination(denominationObj.toString());
					}else{
						tmp.setDenomination(getText("audit.dontKnow"));
					}
					l.add(tmp);
				}
				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf(page.getPageCount()));
				map.put("recordCount", String.valueOf(page.getRecordCount()));
				JSONUtil jsonUtil = new JSONUtil(
						"cn.grgbanking.feeltm.domain.TransBanknoteSeq");
				JSONArray jsonObj = jsonUtil.toJSON(l, map);
				JSONObject input = new JSONObject();
				input.put("pageCount", String.valueOf(page.getPageCount()));
				input.put("recordCount", String.valueOf(page.getRecordCount()));
				input.put("jsonObj", jsonObj);								
				PrintWriter out = response.getWriter();
				out.print(input);
				return "";
			} else {
				ActionContext.getContext().put("transBanknoteSeqList", list);
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

	public String show() throws Exception {
		try {
			TransBanknoteSeq transBanknoteSeq = new TransBanknoteSeq();
			String ids = request.getParameter("id");
			transBanknoteSeq = transBanknoteSeqService
					.getTransBanknoteSeqObject(ids);
			TranInfo tranInfo = new TranInfo();
			tranInfo = tranInfoService.getTranInfoObject(transBanknoteSeq
					.getTranId());
			tranInfo.setTransTime(DateUtil.stringYYYYMMDDHHMMSSTo(tranInfo
					.getTransDate()
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
					transBanknoteSeqList.size()); // 列表的大小
			ActionContext.getContext().put("tranInfo", tranInfo);
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "show";
	}

	public void refresh() {
		try {
			query();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 查看图片
	public String checkPic() {
		try {
			List<TransBanknoteSeq> objList = new ArrayList<TransBanknoteSeq>();
			String[] ids = request.getParameter("ids").split(",");

			boolean flag = false;
			for (int i = 0; i < ids.length; i++) {
				String id = ids[i];
				TransBanknoteSeq obj = transBanknoteSeqService
						.getTransBanknoteSeqObject(id);

				if (obj != null) {
					String picPath = obj.getUrlName();

					if (picPath == null || picPath.equals(""))// 数据库中没有图片路径，向TST发送报文
					{
						String messageNo = String.valueOf(new Date().getTime())
								.substring(1, 13); // 截取后12位作为message的编号
						R2002Msg rmsg = new R2002Msg();

						rmsg = getReturnPack(obj, messageNo);

						if (rmsg != null)// 请求成功
						{
							if (rmsg.getMessageno().equals(messageNo))// 判断接收的message标号与发送的是否一致
							{
								String returnPicPath = rmsg.getFilepath();

								String checkResult = rmsg.getCheckresult()
										.trim();

								// checkResult = "0";

								if (returnPicPath != null
										&& !returnPicPath.equals("")) {
									obj.setUrlName(returnPicPath);
									obj.setCheckResult(checkResult);
									objList.add(obj);
									flag = transBanknoteSeqService
											.updateTransBanknoteSeq(obj);// 更新数据库记录

									// 记录日志
									if (flag)
										SysLog.info(obj.getTermid()
												+ "图片路径更新成功");
									else
										SysLog.info(obj.getTermid()
												+ "图片路径更新失败");
								}
							}
						} else// 响应超时
						{
							SysLog.info("服务器响应超时");
							MsgBox msgBox = new MsgBox(request, getText("audit.serviceOutTime"));
							msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
							return "msgBox";
						}
					} else// 数据库中有图片路径
					{
						flag = true;
						objList.add(obj);
					}
				}// end if(obj != null)
			}// end for

			if (flag == true) {
				ActionContext.getContext().put("objList", objList);
				ActionContext.getContext().put("listSize", objList.size());
				return "showPic";
			} else {
				SysLog.info("图片路径更新失败");
				MsgBox msgBox = new MsgBox(request, getText("audit.ActionFailed"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}
		} catch (Exception e) {
			SysLog.error(e);
			e.printStackTrace();
			MsgBox msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}
	}

	public R2002Msg getReturnPack(TransBanknoteSeq obj, String messageNo)
			throws Exception {
		S2002Msg smsg = new S2002Msg();

		smsg.setCheckcode(obj.getVerifyNo());
		smsg.setDevid(obj.getTermid());
		smsg.setMessageno(messageNo);
		String picName = obj.getPictureName();
		// smsg.setOgcd("0000000");
		// smsg.setPicturename(obj.getPictureName());
		smsg.setPicturename(obj.getTransDate() + "/" + picName);

		byte[] returnpack = null;
		byte[] sendpack = smsg.packMessage();
		// 报文发送，并等待获取返回报文
		Transfer trans = new Transfer();

		returnpack = trans.sendPack(sendpack);

		if (returnpack == null) {
			return null;
		} else {
			R2002Msg rmsg = new R2002Msg();
			rmsg.unpackMessage(returnpack);
			return rmsg;
		}
	}

	public String getCheckResult() {
		return checkResult;
	}

	public void setCheckResult(String checkResult) {
		this.checkResult = checkResult;
	}

	public String getTranId() {
		return tranId;
	}

	public void setTranId(String tranId) {
		this.tranId = tranId;
	}

	public String getNoteFlag() {
		return noteFlag;
	}

	public void setNoteFlag(String noteFlag) {
		this.noteFlag = noteFlag;
	}

	public String getNoteType() {
		return noteType;
	}

	public void setNoteType(String noteType) {
		this.noteType = noteType;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTermid() {
		return termid;
	}

	public void setTermid(String termid) {
		this.termid = termid;
	}

	public String getJournalNo() {
		return journalNo;
	}

	public void setJournalNo(String journalNo) {
		this.journalNo = journalNo;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getDenomination() {
		return denomination;
	}

	public void setDenomination(String denomination) {
		this.denomination = denomination;
	}

	public String getCashBoxId() {
		return cashBoxId;
	}

	public void setCashBoxId(String cashBoxId) {
		this.cashBoxId = cashBoxId;
	}

	public String getSeriaNo() {
		return seriaNo;
	}

	public void setSeriaNo(String seriaNo) {
		this.seriaNo = seriaNo;
	}

	public String getPictureName() {
		return pictureName;
	}

	public void setPictureName(String pictureName) {
		this.pictureName = pictureName;
	}

	public String getVerifyNo() {
		return verifyNo;
	}

	public void setVerifyNo(String verifyNo) {
		this.verifyNo = verifyNo;
	}

	public String getUrlName() {
		return urlName;
	}

	public void setUrlName(String urlName) {
		this.urlName = urlName;
	}

	public String getTransDate() {
		return transDate;
	}

	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}

	public String getTranTime() {
		return tranTime;
	}

	public void setTranTime(String tranTime) {
		this.tranTime = tranTime;
	}

}
