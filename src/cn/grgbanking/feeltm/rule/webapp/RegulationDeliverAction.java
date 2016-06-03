package cn.grgbanking.feeltm.rule.webapp;

import java.io.PrintWriter;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.grgbanking.feeltm.config.BusnDataDir;
import cn.grgbanking.feeltm.domain.BlackRegulation;
import cn.grgbanking.feeltm.domain.RegulationDeliver;
import cn.grgbanking.feeltm.domain.RepeatRegulation;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.parseMsg.Transfer;
import cn.grgbanking.feeltm.parseMsg.appMessage.R2001Msg;
import cn.grgbanking.feeltm.parseMsg.appMessage.S2001Msg;
import cn.grgbanking.feeltm.rule.service.BlackRegulationService;
import cn.grgbanking.feeltm.rule.service.RegulationDeliverService;
import cn.grgbanking.feeltm.rule.service.RepeatRegulationService;
import cn.grgbanking.feeltm.util.DateUtil;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;

import com.opensymphony.xwork2.ActionContext;

@SuppressWarnings( { "serial", "unchecked" })
public class RegulationDeliverAction extends BaseAction {
	private RepeatRegulationService repeatRegulationService;

	public RepeatRegulationService getRepeatRegulationService() {
		return repeatRegulationService;
	}

	public void setRepeatRegulationService(
			RepeatRegulationService repeatRegulationService) {
		this.repeatRegulationService = repeatRegulationService;
	}

	private BlackRegulationService blackRegulationService;

	public BlackRegulationService getBlackRegulationService() {
		return blackRegulationService;
	}

	public void setBlackRegulationService(
			BlackRegulationService blackRegulationService) {
		this.blackRegulationService = blackRegulationService;
	}

	private RegulationDeliverService regulationDeliverService;

	public RegulationDeliverService getRegulationDeliverService() {
		return regulationDeliverService;
	}

	public void setRegulationDeliverService(
			RegulationDeliverService regulationDeliverService) {
		this.regulationDeliverService = regulationDeliverService;
	}

	private String id;
	private String termid;
	private String role;
	private String version;
	private String date;
	private String reguStatus;
	private String applyId;
	private String regType;
	private String deliverDate;
	// 备份撤销前的规则
	private String note;

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public void refresh() {
		try {
			query();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String query() throws Exception {
		try {
			String from = request.getParameter("from");
			RegulationDeliver regulationDeliver = new RegulationDeliver();
			int pageNum = 1;
			int pageSize = 20;
			if (request.getParameter("pageNum") != null
					&& request.getParameter("pageNum").length() > 0)
				pageNum = Integer.parseInt(request.getParameter("pageNum"));
			if (request.getParameter("pageSize") != null
					&& request.getParameter("pageSize").length() > 0)
				pageSize = Integer.parseInt(request.getParameter("pageSize"));
			String termid = request.getParameter("termid");
			String type = request.getParameter("type");
			String status = request.getParameter("status");
			if (termid != null && !"".equals(termid)) {
				regulationDeliver.setTermid(termid);
			}
			if (type != null && !"".equals(type)) {
				regulationDeliver.setType(type);
			}
			if (status != null && !"".equals(status)) {
				regulationDeliver.setStatus(status);
			}
			Page page = regulationDeliverService.getRegulationDeliverPage(
					regulationDeliver, pageNum, pageSize);
			request.setAttribute("currPage", page);
			List<RegulationDeliver> list = page.getQueryResult();
			int size = list.size();
			for (int i = 0; i < size; i++) {
				RegulationDeliver temp = (RegulationDeliver) list.get(i);
				if (temp.getDate() != null && !"".equals(temp.getDate()))
					temp.setDate(DateUtil
							.stringYYYYMMDDHHMMSSTo(temp.getDate()));
			}
			if (from != null && from.equals("refresh")) {
				List<Object> l = new ArrayList<Object>();
				Map dataDirMap1 = BusnDataDir.getMap("ruleMgr.applyType");
				Map dataDirMap2 = BusnDataDir.getMap("ruleMgr.reguStatus");
				for (int i = 0; i < list.size(); i++) {
					RegulationDeliver regulationOb = list.get(i);
					Object otype = dataDirMap1.get(regulationOb.getType());
					Object ostatus = dataDirMap2.get(regulationOb.getStatus());
					if (otype != null && !"".equals(otype.toString())) {
						regulationOb.setType(otype.toString());

					}
					if (ostatus != null && !"".equals(ostatus.toString())) {
						regulationOb.setStatus(ostatus.toString());
					}
					l.add(regulationOb);
				}
				// System.out.println(l.size()+"---------------------"+"\n"+"refresh");
				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf(page.getPageCount()));
				map.put("recordCount", String.valueOf(page.getRecordCount()));
				JSONUtil jsonUtil = new JSONUtil(
						"cn.grgbanking.feeltm.domain.RegulationDeliver");
				JSONArray jsonObj = jsonUtil.toJSON(l, map);
				JSONObject input = new JSONObject();
				input.put("pageCount", String.valueOf(page.getPageCount()));
				input.put("recordCount", String.valueOf(page.getRecordCount()));
				input.put("jsonObj", jsonObj);
				PrintWriter out = response.getWriter();
				out.print(input);
				return "";
			} else {
				// System.out.println(list.size()+"-------------------"+"\n"+"query");
				ActionContext.getContext().put("regulationDeliverList", list);
				return "query";
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_RETURN);
			return "msgBox";
		}
		// return "query";
	}

	public String show() throws Exception {
		try {
			RegulationDeliver regulationDeliver = new RegulationDeliver();
			String ids = request.getParameter("ids");
			regulationDeliver = regulationDeliverService
					.getRegulationDeliverObject(ids);
			ActionContext.getContext().put("regulationDeliver",
					regulationDeliver);
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "show";
	}

	/**
	 * 对指定ApplyId的规则进行撤销
	 * 
	 * @author Yondy Chow
	 * @return String
	 */
	public String cancleRegu() {
		RegulationDeliver regulationDeliver = new RegulationDeliver();
		String str_applyId = request.getParameter("applyId");
		regulationDeliver.setApplyId(str_applyId);
		MsgBox msgBox = null;
		String str_type = "1"; // 默认为黑名单规则
		if (str_applyId != null && !str_applyId.equals("")) {

			List<RegulationDeliver> rdList = regulationDeliverService
					.getRegulationDeliverListByApplyId(str_applyId);
			String str_date = DateUtil.getTimeYYYYMMDDHHMMSSString(new Date());
			String str_version = "v_"
					+ Long.toString(System.currentTimeMillis());
			if (rdList != null && rdList.size() != 0) {
				RegulationDeliver first = rdList.get(0);
				// if (first.getStatus() != null &&
				// !first.getStatus().equals("2")) {
				// msgBox = new MsgBox(request, getText("rule.selectRight"));
				// msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				// return "msgBox";
				// }
				String str_status = null;
				String str_rule = null;
				str_type = first.getType();
				if (str_type.equals("1")) {
					BlackRegulation blObj = blackRegulationService
							.getBlackRegulationByApplyId(str_applyId).get(0);
					str_status = blObj.getReguStatus();
					str_rule = blObj.getMoneyType()
							+ blObj.getMoneyDenomination()
							+ blObj.getRegulation();
				} else {
					RepeatRegulation reObj = repeatRegulationService
							.getRepeatRegulationByApplyId(str_applyId).get(0);
					str_status = reObj.getReguStatus();
				}
				if (str_status != null && !str_status.equals("2")) {
					msgBox = new MsgBox(request, getText("rule.selectRight"));
					msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
					return "msgBox";
				}
				for (int j = 0; j < rdList.size(); j++) {
					RegulationDeliver temp = rdList.get(j);
					temp.setStatus("1");
					temp.setNote(temp.getRole());

					RegulationDeliver last = regulationDeliverService
							.getRegulationDeliverListByTermid(temp.getTermid())
							.get(0); // 得到最新下发的
					StringBuffer strBuffer = new StringBuffer();
					String str_role = last.getRole();
					int index = str_role.indexOf("|");
					String str_bl = str_role.substring(0, index);
					if (str_type.equals("1")) {
						String[] arr_bl = str_bl.split(",");
						if (arr_bl.length == 1) {
							strBuffer.append("0000");
						} else {
							if (str_bl.indexOf(str_rule + ",") != -1) {
								strBuffer.append(str_bl.replaceAll(str_rule
										+ ",", ""));
							} else {
								strBuffer.append(str_bl.replaceAll(","
										+ str_rule, ""));
							}
						}
						strBuffer.append(str_role.substring(index));
					} else {
						strBuffer.append(str_bl + "|0000"); // 撤销重号规则
					}

					temp.setRole(strBuffer.toString());

					temp.setDate(str_date);
					temp.setVersion(str_version);
					regulationDeliverService.updateRegulationDeliver(temp);
				}
			} else {
				msgBox = new MsgBox(request, getText("rule.selectRight"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}
		} else {
			msgBox = new MsgBox(request, getText("audit.ActionFailed"));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}
		
		

		// 将对应的规则删除
		if (str_type.equals("1")) {
			BlackRegulation blRegu = blackRegulationService
					.getBlackRegulationByApplyId(str_applyId).get(0);
			blackRegulationService.deleteBlackRegulation(blRegu);
		} else {
			RepeatRegulation reRegu = repeatRegulationService
					.getRepeatRegulationByApplyId(str_applyId).get(0);
			repeatRegulationService.deleteRepeatRegulation(reRegu);
		}
		
		// 发送报文到STS端，通知规则下发
		String messageNo = (System.currentTimeMillis() + "").substring(1,
				13); // 当前时间精确到毫秒数，并得到后12位做为消息序列号
		try {
			R2001Msg rmsg = new R2001Msg();
			rmsg = getReturnPack(messageNo);
			if (rmsg != null) {
				;
			} else {
				SysLog.info(getText("deliver.error"));
				msgBox = new MsgBox(request,
						getText("deliver.error"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}

		} catch (ConnectException e) {
			SysLog.info(getText("deliver.error"));
			msgBox = new MsgBox(request, getText("deliver.error"));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";

		} catch (Exception e) {
			e.printStackTrace();
		}

		msgBox = new MsgBox(request, getText("audit.revokeRegulation"));
		msgBox.setButtonType(MsgBox.BUTTON_CLOSE);

		return "msgBox";
	}

	/**
	 * 对指定ApplyId的规则进行下发
	 * 
	 * @author Yondy Chow
	 * @return String
	 */
	public String deliver() {
		RegulationDeliver regulationDeliver = new RegulationDeliver();
		String applyIds = request.getParameter("applyIds");
		regulationDeliver.setApplyId(applyIds);
		int iCount = 0;
		// boolean flag = false;
		String[] rdids = regulationDeliver.getApplyId().split(",");
		String[] ids = new String[rdids.length];
		if (rdids != null) {
			for (int i = 0; i < rdids.length; i++) {

				List<RegulationDeliver> rdList = regulationDeliverService
						.getRegulationDeliverListByApplyId(rdids[i]);
				if (rdList != null && rdList.size() != 0) {
					String reguType = rdList.get(0).getType();
					if (reguType.equals("1")) {
						BlackRegulation blRegu = blackRegulationService
								.getBlackRegulationByApplyId(rdids[i]).get(0);
						if (blRegu.getReguStatus() != null
								&& !blRegu.getReguStatus().equals("1")) {
							MsgBox msgBox = new MsgBox(request,
									getText("audit.pleaseChooseRegulation"),
									"rule", new String[] { String
											.valueOf(iCount) });
							msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
							return "msgBox";
						}
					} else {
						RepeatRegulation reRegu = repeatRegulationService
								.getRepeatRegulationByApplyId(rdids[i]).get(0);
						if (reRegu.getReguStatus() != null
								&& !reRegu.getReguStatus().equals("1")) {
							MsgBox msgBox = new MsgBox(request,
									getText("audit.pleaseChooseRegulation"),
									"rule", new String[] { String
											.valueOf(iCount) });
							msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
							return "msgBox";
						}
					}
				} else {
					MsgBox msgBox = new MsgBox(request,
							getText("audit.pleaseChooseRegulation"), "rule",
							new String[] { String.valueOf(iCount) });
					msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
					return "msgBox";
				}
			}
			for (int i = 0; i < rdids.length; i++) {
				List<RegulationDeliver> rdList = regulationDeliverService
						.getRegulationDeliverListByApplyId(rdids[i]);
				if (rdList != null && rdList.size() != 0) {
					String reguType = rdList.get(0).getType();
					for (int j = 0; j < rdList.size(); j++) {
						RegulationDeliver temp = rdList.get(j);
						temp.setStatus("1");
						regulationDeliverService.updateRegulationDeliver(temp);
					}
					// if (rdList.size() != 0) {
					ids[iCount++] = rdids[i] + "|" + reguType; // 记录规则类型
					// System.out.println("========================= " +
					// ids[iCount]);
					// }
				}
			}
			// flag = true;
		}

		// 更新规则下发状态
		for (int i = 0; i < ids.length; i++) {
			if (ids[i] != null && !ids[i].equals("")) {
				int index = ids[i].indexOf("|");
				if ((ids[i].substring(index + 1)).equals("1")) {
					BlackRegulation blRegu = blackRegulationService
							.getBlackRegulationByApplyId(
									ids[i].substring(0, index)).get(0); // 得到黑名单规则
					blRegu.setReguStatus("2"); // 已下发
					blackRegulationService.updateBlackRegulation(blRegu);
				} else if ((ids[i].substring(index + 1)).equals("2")) {
					RepeatRegulation reRegu = repeatRegulationService
							.getRepeatRegulationByApplyId(
									ids[i].substring(0, index)).get(0); // 得到重号规则
					reRegu.setReguStatus("2"); // 已下发
					repeatRegulationService.updateRepeatRegulation(reRegu);
				}
			} else {
				return "msgBox";
			}
		}
		
		// 发送报文到STS端，通知规则下发
		String messageNo = (System.currentTimeMillis() + "").substring(1,
				13); // 当前时间精确到毫秒数，并得到后12位做为消息序列号
		try {
			R2001Msg rmsg = new R2001Msg();
			rmsg = getReturnPack(messageNo);
			if (rmsg != null) {
				;
			} else {
				SysLog.info(getText("deliver.error"));
				MsgBox msgBox = new MsgBox(request,
						getText("deliver.error"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}

		} catch (ConnectException e) {
			SysLog.info(getText("deliver.error"));
			MsgBox msgBox = new MsgBox(request,
					getText("deliver.error"));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		MsgBox msgBox = new MsgBox(request, getText("rule.deliver.ok"), "rule",
				new String[] { String.valueOf(iCount) });
		msgBox.setButtonType(MsgBox.BUTTON_CLOSE);

		return "msgBox";
	}

	private R2001Msg getReturnPack(String messageNo) throws Exception {
		S2001Msg smsg = new S2001Msg();
		smsg.setMessageno(messageNo);
		// String time = DateUtil.sdf_yyyyMMddHHmmss.format(new Date());
		// smsg.setTxdate(time.substring(0, 8));
		// smsg.setTxtime(time.substring(8));

		byte[] returnpack = null;
		byte[] sendpack = smsg.packMessage();

		// 发送报文，并等待获取返回报文
		Transfer trans = new Transfer();
		returnpack = trans.sendPack(sendpack);

		if (returnpack == null) {
			return null;
		} else {
			R2001Msg rmsg = new R2001Msg();
			rmsg.unpackMessage(returnpack);
			return rmsg;
		}
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

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getReguStatus() {
		return reguStatus;
	}

	public void setReguStatus(String reguStatus) {
		this.reguStatus = reguStatus;
	}

	public String getApplyId() {
		return applyId;
	}

	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}

	public String getRegType() {
		return regType;
	}

	public void setRegType(String regType) {
		this.regType = regType;
	}

	public String getDeliverDate() {
		return deliverDate;
	}

	public void setDeliverDate(String deliverDate) {
		this.deliverDate = deliverDate;
	}

}
