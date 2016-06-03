package cn.grgbanking.feeltm.tmlInfo.webapp;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import cn.grgbanking.feeltm.config.BusnDataDir;
import cn.grgbanking.feeltm.datadir.dao.DataDirDao;
import cn.grgbanking.feeltm.domain.TmlInfo;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.tmlInfo.service.TmlInfoService;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;

import com.opensymphony.xwork2.ActionContext;

@SuppressWarnings( { "serial", "unchecked" })
public class TmlInfoAction extends BaseAction {
	@Autowired
	private   DataDirDao dataDirDao;
	private TmlInfoService tmlInfoService;

	public TmlInfoService getTmlInfoService() {
		return tmlInfoService;
	}

	public void setTmlInfoService(TmlInfoService tmlInfoService) {
		this.tmlInfoService = tmlInfoService;
	}

	private String termid;
	private String termseq;
	private String counterid;
	private String termtype;
	private String brand;
	private Date opertermid;
	private String netaddr;
	private Integer netport;
	private String softversion;
	private Integer savebox;
	private Integer cwdbox;
	private String isinteratm;
	private String orgid;
	private String areaid;
	private String termaddress;
	private String mark;
	private String laytype;
	private String settype;
	private String selfbankid;
	private String insttype;
	private String instarea;
	private String snproperty;
	private String instproperty;
	private String runbegintime;
	private String runendtime;
	private Integer daytime;
	private String procurementyear;
	private String procurementbatch;
	private Date arrivaldate;
	private Date instdate;
	private Date activedate;
	private Integer warrantystatus;
	private Date warrantybegindate;
	private Date warrantyenddate;
	private Date warrantydays;
	private String maintainid;
	private String id;

	public String query() throws Exception {
		String from = request.getParameter("from");
		String termid = request.getParameter("termid");
		String termtype = request.getParameter("termtype");

		try {
			TmlInfo tmlInfo = new TmlInfo();
			int pageNum = 1;
			int pageSize = 20;
			if (request.getParameter("pageNum") != null
					&& request.getParameter("pageNum").length() > 0)
				pageNum = Integer.parseInt(request.getParameter("pageNum"));
			if (request.getParameter("pageSize") != null
					&& request.getParameter("pageSize").length() > 0)
				pageSize = Integer.parseInt(request.getParameter("pageSize"));

			if (termid != null)
				tmlInfo.setTermid(termid);
			if (termtype != null && !termtype.equals(""))
				tmlInfo.setTermtype(termtype);

			Page page = tmlInfoService.findTmlInfoPage(tmlInfo, pageNum,
					pageSize);
			request.setAttribute("currPage", page);

			List<TmlInfo> list = page.getQueryResult();

			if (from != null && from.equals("refresh")) {
				List<Object> l = new ArrayList<Object>();

				Map dataDirMap = BusnDataDir.getMap("tmlMgr.termType");
				Map brandMap = BusnDataDir.getMap("tmlMgr.brand");
				Map orgInfoMap = dataDirDao.getOrgInfoMap();
				for (int i = 0; i < list.size(); i++) {

					TmlInfo tempObj = (TmlInfo) list.get(i);
					Object o1 = dataDirMap.get(tempObj.getTermtype());
					Object o2 = brandMap.get(tempObj.getBrand());
					Object o3 = orgInfoMap.get(tempObj.getOrgid());
					if (o1 != null)
						tempObj.setTermtype(o1.toString());
					if (o2 != null)
						tempObj.setBrand(o2.toString());
					if(o3!=null){
						tempObj.setOrgid(o3.toString());
					}
					l.add((Object) tempObj);
				}
				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf(page.getPageCount()));
				map.put("recordCount", String.valueOf(page.getRecordCount()));
				JSONUtil jsonUtil = new JSONUtil(
						"cn.grgbanking.feeltm.domain.TmlInfo");
				JSONArray jsonObj = jsonUtil.toJSON(l, map);
				JSONObject input = new JSONObject();
				input.put("pageCount", String.valueOf(page.getPageCount()));
				input.put("recordCount", String.valueOf(page.getRecordCount()));
				input.put("jsonObj", jsonObj);								
				PrintWriter out = response.getWriter();
				out.print(input);
				return "";
			} else {
				ActionContext.getContext().put("tmlInfoList", list);
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

	//选择终端
	public String selectTml() {
		List<TmlInfo> tmlInfoList = new ArrayList<TmlInfo>();
		tmlInfoList = tmlInfoService.getTmlInfoList(null);
		ActionContext.getContext().put("tmlInfoList", tmlInfoList);
		ActionContext.getContext().put("listSize", tmlInfoList.size());
		return "selectTml";
	}

	public String add() throws Exception {
		return "add";
	}

	public String save() throws Exception {
		try {
			TmlInfo tmlInfo = new TmlInfo();
			tmlInfo.setTermid(getTermid());
			tmlInfo.setTermseq(getTermseq());
			tmlInfo.setCounterid(getCounterid());
			tmlInfo.setTermtype(getTermtype());
			tmlInfo.setBrand(getBrand());
			tmlInfo.setOpertermid(getOpertermid());
			tmlInfo.setNetaddr(getNetaddr());
			tmlInfo.setNetport(getNetport());
			tmlInfo.setSoftversion(getSoftversion());
			tmlInfo.setSavebox(getSavebox());
			tmlInfo.setCwdbox(getCwdbox());
			tmlInfo.setIsinteratm(getIsinteratm());
			tmlInfo.setOrgid(getOrgid());
			tmlInfo.setAreaid(getAreaid());
			tmlInfo.setTermaddress(getTermaddress());
			tmlInfo.setMark(getMark());
			tmlInfo.setLaytype(getLaytype());
			tmlInfo.setSettype(getSettype());
			tmlInfo.setSelfbankid(getSelfbankid());
			tmlInfo.setInsttype(getInsttype());
			tmlInfo.setInstarea(getInstarea());
			tmlInfo.setSnproperty(getSnproperty());
			tmlInfo.setInstproperty(getInstproperty());
			tmlInfo.setRunbegintime(getRunbegintime());
			tmlInfo.setRunendtime(getRunendtime());
			tmlInfo.setDaytime(getDaytime());
			tmlInfo.setProcurementyear(getProcurementyear());
			tmlInfo.setProcurementbatch(getProcurementbatch());
			tmlInfo.setArrivaldate(getArrivaldate());
			tmlInfo.setInstdate(getInstdate());
			tmlInfo.setActivedate(getActivedate());
			tmlInfo.setWarrantystatus(getWarrantystatus());
			tmlInfo.setWarrantybegindate(getWarrantybegindate());
			tmlInfo.setWarrantyenddate(getWarrantyenddate());
			tmlInfo.setWarrantydays(getWarrantydays());
			tmlInfo.setMaintainid(getMaintainid());
			tmlInfo.setId(getId());
			boolean flag = tmlInfoService.addTmlInfo(tmlInfo);
			if (flag == true) {
				MsgBox msgBox = new MsgBox(request, getText("add.ok"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				;
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
			return "msgBox";
		}
		return "msgBox";
	}

	public String delete() throws Exception {
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);

		try {
			TmlInfo tmlInfo = new TmlInfo();
			String ids = request.getParameter("ids");
			tmlInfo.setId(ids);
			int iCount = 0;
			String[] tmlInfoids = tmlInfo.getId().split(",");
			if (tmlInfoids != null) {
				for (int i = 0; i < tmlInfoids.length; i++) {
					TmlInfo tmp = tmlInfoService
							.getTmlInfoObject(tmlInfoids[i]);
					tmlInfoService.deleteTmlInfo(tmp);
					SysLog.operLog(request, Constants.OPER_MODIFY_VALUE, tmp
							.getTermid());// 记录日志
					SysLog.info("User:" + userModel.getUserid()
							+ " to delete a termInfo: " + tmp.getTermid());
					iCount++;
				}
			}
			MsgBox msgBox = new MsgBox(request, getText("tmlInfo.del.ok"),
					"menu", new String[] { String.valueOf(iCount) });
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
			request.getSession().setAttribute("a", "aaaaa");
			TmlInfo tmlInfo = new TmlInfo();
			String ids = request.getParameter("ids");
			tmlInfo = tmlInfoService.getTmlInfoObject(ids);
			ActionContext.getContext().put("tmlInfo", tmlInfo);
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "edit";
	}

	public String update() throws Exception {
		try {
			TmlInfo tmlInfo = new TmlInfo();
			tmlInfo = tmlInfoService.getTmlInfoObject(getId());
			tmlInfo.setTermid(getTermid());
			tmlInfo.setTermseq(getTermseq());
			tmlInfo.setCounterid(getCounterid());
			tmlInfo.setTermtype(getTermtype());
			tmlInfo.setBrand(getBrand());
			tmlInfo.setOpertermid(getOpertermid());
			tmlInfo.setNetaddr(getNetaddr());
			tmlInfo.setNetport(getNetport());
			tmlInfo.setSoftversion(getSoftversion());
			tmlInfo.setSavebox(getSavebox());
			tmlInfo.setCwdbox(getCwdbox());
			tmlInfo.setIsinteratm(getIsinteratm());
			tmlInfo.setOrgid(getOrgid());
			tmlInfo.setAreaid(getAreaid());
			tmlInfo.setTermaddress(getTermaddress());
			tmlInfo.setMark(getMark());
			tmlInfo.setLaytype(getLaytype());
			tmlInfo.setSettype(getSettype());
			tmlInfo.setSelfbankid(getSelfbankid());
			tmlInfo.setInsttype(getInsttype());
			tmlInfo.setInstarea(getInstarea());
			tmlInfo.setSnproperty(getSnproperty());
			tmlInfo.setInstproperty(getInstproperty());
			tmlInfo.setRunbegintime(getRunbegintime());
			tmlInfo.setRunendtime(getRunendtime());
			tmlInfo.setDaytime(getDaytime());
			tmlInfo.setProcurementyear(getProcurementyear());
			tmlInfo.setProcurementbatch(getProcurementbatch());
			tmlInfo.setArrivaldate(getArrivaldate());
			tmlInfo.setInstdate(getInstdate());
			tmlInfo.setActivedate(getActivedate());
			tmlInfo.setWarrantystatus(getWarrantystatus());
			tmlInfo.setWarrantybegindate(getWarrantybegindate());
			tmlInfo.setWarrantyenddate(getWarrantyenddate());
			tmlInfo.setWarrantydays(getWarrantydays());
			tmlInfo.setMaintainid(getMaintainid());
			tmlInfo.setId(getId());
			boolean flag = tmlInfoService.updateTmlInfo(tmlInfo);
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

	public String show() throws Exception {
		try {
			TmlInfo tmlInfo = new TmlInfo();
			String ids = request.getParameter("id");
			tmlInfo = tmlInfoService.getTmlInfoObject(ids);
			ActionContext.getContext().put("tmlInfo", tmlInfo);
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
		;
	}

	public String getTermid() {
		return termid;
	}

	public void setTermid(String termid) {
		this.termid = termid;
	}

	public String getTermseq() {
		return termseq;
	}

	public void setTermseq(String termseq) {
		this.termseq = termseq;
	}

	public String getCounterid() {
		return counterid;
	}

	public void setCounterid(String counterid) {
		this.counterid = counterid;
	}

	public String getTermtype() {
		return termtype;
	}

	public void setTermtype(String termtype) {
		this.termtype = termtype;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public Date getOpertermid() {
		return opertermid;
	}

	public void setOpertermid(Date opertermid) {
		this.opertermid = opertermid;
	}

	public String getNetaddr() {
		return netaddr;
	}

	public void setNetaddr(String netaddr) {
		this.netaddr = netaddr;
	}

	public Integer getNetport() {
		return netport;
	}

	public void setNetport(Integer netport) {
		this.netport = netport;
	}

	public String getSoftversion() {
		return softversion;
	}

	public void setSoftversion(String softversion) {
		this.softversion = softversion;
	}

	public Integer getSavebox() {
		return savebox;
	}

	public void setSavebox(Integer savebox) {
		this.savebox = savebox;
	}

	public Integer getCwdbox() {
		return cwdbox;
	}

	public void setCwdbox(Integer cwdbox) {
		this.cwdbox = cwdbox;
	}

	public String getIsinteratm() {
		return isinteratm;
	}

	public void setIsinteratm(String isinteratm) {
		this.isinteratm = isinteratm;
	}

	public String getOrgid() {
		return orgid;
	}

	public void setOrgid(String orgid) {
		this.orgid = orgid;
	}

	public String getAreaid() {
		return areaid;
	}

	public void setAreaid(String areaid) {
		this.areaid = areaid;
	}

	public String getTermaddress() {
		return termaddress;
	}

	public void setTermaddress(String termaddress) {
		this.termaddress = termaddress;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getLaytype() {
		return laytype;
	}

	public void setLaytype(String laytype) {
		this.laytype = laytype;
	}

	public String getSettype() {
		return settype;
	}

	public void setSettype(String settype) {
		this.settype = settype;
	}

	public String getSelfbankid() {
		return selfbankid;
	}

	public void setSelfbankid(String selfbankid) {
		this.selfbankid = selfbankid;
	}

	public String getInsttype() {
		return insttype;
	}

	public void setInsttype(String insttype) {
		this.insttype = insttype;
	}

	public String getInstarea() {
		return instarea;
	}

	public void setInstarea(String instarea) {
		this.instarea = instarea;
	}

	public String getSnproperty() {
		return snproperty;
	}

	public void setSnproperty(String snproperty) {
		this.snproperty = snproperty;
	}

	public String getInstproperty() {
		return instproperty;
	}

	public void setInstproperty(String instproperty) {
		this.instproperty = instproperty;
	}

	public String getRunbegintime() {
		return runbegintime;
	}

	public void setRunbegintime(String runbegintime) {
		this.runbegintime = runbegintime;
	}

	public String getRunendtime() {
		return runendtime;
	}

	public void setRunendtime(String runendtime) {
		this.runendtime = runendtime;
	}

	public Integer getDaytime() {
		return daytime;
	}

	public void setDaytime(Integer daytime) {
		this.daytime = daytime;
	}

	public String getProcurementyear() {
		return procurementyear;
	}

	public void setProcurementyear(String procurementyear) {
		this.procurementyear = procurementyear;
	}

	public String getProcurementbatch() {
		return procurementbatch;
	}

	public void setProcurementbatch(String procurementbatch) {
		this.procurementbatch = procurementbatch;
	}

	public Date getArrivaldate() {
		return arrivaldate;
	}

	public void setArrivaldate(Date arrivaldate) {
		this.arrivaldate = arrivaldate;
	}

	public Date getInstdate() {
		return instdate;
	}

	public void setInstdate(Date instdate) {
		this.instdate = instdate;
	}

	public Date getActivedate() {
		return activedate;
	}

	public void setActivedate(Date activedate) {
		this.activedate = activedate;
	}

	public Integer getWarrantystatus() {
		return warrantystatus;
	}

	public void setWarrantystatus(Integer warrantystatus) {
		this.warrantystatus = warrantystatus;
	}

	public Date getWarrantybegindate() {
		return warrantybegindate;
	}

	public void setWarrantybegindate(Date warrantybegindate) {
		this.warrantybegindate = warrantybegindate;
	}

	public Date getWarrantyenddate() {
		return warrantyenddate;
	}

	public void setWarrantyenddate(Date warrantyenddate) {
		this.warrantyenddate = warrantyenddate;
	}

	public Date getWarrantydays() {
		return warrantydays;
	}

	public void setWarrantydays(Date warrantydays) {
		this.warrantydays = warrantydays;
	}

	public String getMaintainid() {
		return maintainid;
	}

	public void setMaintainid(String maintainid) {
		this.maintainid = maintainid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
