package cn.grgbanking.feeltm.expense.webapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import cn.grgbanking.feeltm.approval.domain.ApprovalRecord;
import cn.grgbanking.feeltm.approval.service.ApprovalService;
import cn.grgbanking.feeltm.common.util.NStrafFileUtils;
import cn.grgbanking.feeltm.config.BusnDataDir;
import cn.grgbanking.feeltm.config.Configure;
import cn.grgbanking.feeltm.config.UserDataRoleConfig;
import cn.grgbanking.feeltm.config.UserRoleConfig;
import cn.grgbanking.feeltm.domain.SysUser;
import cn.grgbanking.feeltm.expense.domain.OAExpenseAccount;
import cn.grgbanking.feeltm.expense.domain.OAExpenseAccountCheckCondition;
import cn.grgbanking.feeltm.expense.domain.OAExpenseAccountProject;
import cn.grgbanking.feeltm.expense.domain.OAExpenseAccountTravelDetail;
import cn.grgbanking.feeltm.expense.service.ExpenseInfoService;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.modify.domain.ModifyRecord;
import cn.grgbanking.feeltm.modify.service.ModifyRecordService;
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

@SuppressWarnings({"unchecked" })
public class ExpenseInfoAction extends BaseAction implements
		ModelDriven<OAExpenseAccount> {
	private static final long serialVersionUID = 1L;
	@Autowired
	private ExpenseInfoService expenseInfoService;
	@Autowired
	private SysUserInfoService sysUserInfoService;
	@Autowired
	private ApprovalService approvalService;
	@Autowired
	private StaffInfoService staffInfoService;
	
	@Autowired
	private ModifyRecordService modifyRecordService;

	@Autowired
	private ProjectService projectService;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	private OAExpenseAccount expenseAccount = new OAExpenseAccount();
	private List<OAExpenseAccountCheckCondition> expenseList = new ArrayList<OAExpenseAccountCheckCondition>();
	private List<OAExpenseAccountCheckCondition> expenselist = new ArrayList<OAExpenseAccountCheckCondition>();
	private List<OAExpenseAccountTravelDetail> travelDetailList = new ArrayList<OAExpenseAccountTravelDetail>();
	private List<OAExpenseAccountProject> expensePrjList = new ArrayList<OAExpenseAccountProject>();

	// 输入：查询条件
	private String beginTime;// 提交开始时间
	private String endTime;// 提交结束时间
	private String userName;// 报销人姓名
	private String detName; // 部门
	private String prjName; // 项目
	private String expenseNum; // 报销流水号
	private String status;//报销单状态
	private String expenseprjname;//报销项目名称
	
	DecimalFormat nf = new DecimalFormat("#.00");

	private File file;
	
	private FileInputStream inputStream;

	public InputStream getInputStream() throws Exception {
		return inputStream;
	}
	
	/**
	 * 修改出差明细
	 * @return
	 * lhyan3
	 * 2014年7月18日
	 */
	public String modifyCostDetail(){
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		String detailId = request.getParameter("detailId");
		OAExpenseAccountCheckCondition costDetail = expenseInfoService.getCostDetail(detailId);
		OAExpenseAccountCheckCondition costSum = expenseInfoService.getCostSum(costDetail.getExpenseId());
		OAExpenseAccount account = expenseInfoService.getExpenseInfoById(costDetail.getExpenseId());
		String subValue = request.getParameter("subValue");
		if(subValue!=null && !"".equals(subValue)){
			String values[] = subValue.split(",");
			if(values.length>0){
				costSum.setFly(String.valueOf(nf.format(Double.valueOf(costSum.getFly())-Double.valueOf(costDetail.getFly())+Double.valueOf(values[0]))));
				costSum.setTaxi(String.valueOf(nf.format(Double.valueOf(costSum.getTaxi())-Double.valueOf(costDetail.getTaxi())+Double.valueOf(values[1]))));
				costSum.setBus(String.valueOf(nf.format(Double.valueOf(costSum.getBus())-Double.valueOf(costDetail.getBus())+Double.valueOf(values[2]))));
				costSum.setLiving(String.valueOf(nf.format(Double.valueOf(costSum.getLiving())-Double.valueOf(costDetail.getLiving())+Double.valueOf(values[3]))));
				costSum.setContact(String.valueOf(nf.format(Double.valueOf(costSum.getContact())-Double.valueOf(costDetail.getContact())+Double.valueOf(values[4]))));
				costSum.setBusiness(String.valueOf(nf.format(Double.valueOf(costSum.getBusiness())-Double.valueOf(costDetail.getBusiness())+Double.valueOf(values[5]))));
				costSum.setOther(String.valueOf(nf.format(Double.valueOf(costSum.getOther())-Double.valueOf(costDetail.getOther())+Double.valueOf(values[6]))));
				costSum.setBuzhu(String.valueOf(nf.format(Double.valueOf(costSum.getBuzhu())-Double.valueOf(costDetail.getBuzhu())+Double.valueOf(values[7]))));
				costSum.setAccount(String.valueOf(nf.format(Double.valueOf(costSum.getAccount())-Double.valueOf(costDetail.getAccount())+Double.valueOf(values[8]))));
				//if(UserRoleConfig.ifHr(userModel)){
					//hr修改
					//记录修改前的记录
					String record = "往返车船飞机票:"+costDetail.getFly()
							+",出租车:"+costDetail.getTaxi()
							+",公共汽车:"+costDetail.getBus()
							+",住宿费:"+costDetail.getLiving()
							+",通讯费:"+costDetail.getContact()
							+",业务费用:"+costDetail.getBusiness()
							+",其它:"+costDetail.getOther()
							+",补助:"+costDetail.getBuzhu();
					modifyRecordService.saveRecord(account.getId(), userModel.getUsername(), "修改了:"+costDetail.getDateTime()+"的记录", new Date(), record);
				//}
				costDetail.setFly(values[0]);
				costDetail.setTaxi(values[1]);
				costDetail.setBus(values[2]);
				costDetail.setLiving(values[3]);
				costDetail.setContact(values[4]);
				costDetail.setBusiness(values[5]);
				costDetail.setOther(values[6]);
				costDetail.setBuzhu(values[7]);
				costDetail.setAccount(values[8]);
				expenseInfoService.updateCostDetail(costDetail);
				expenseInfoService.updateCostDetail(costSum);
			}
			String sum = request.getParameter("sum");
			account.setSum(Double.valueOf(sum));
			expenseInfoService.updateExpenseInfo(account);
		}
		
		return null;
	}
	
	/**
	 * 删除出差明细
	 * @return
	 * lhyan3
	 * 2014年7月18日
	 */
	public String deleteCostDetail(){
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		String detailId = request.getParameter("detailId");
		OAExpenseAccountCheckCondition costDetail = expenseInfoService.getCostDetail(detailId);
		OAExpenseAccountCheckCondition costSum = expenseInfoService.getCostSum(costDetail.getExpenseId());
		OAExpenseAccount account = expenseInfoService.getExpenseInfoById(costDetail.getExpenseId());
		//if(UserRoleConfig.ifHr(userModel)){
			String record = "往返车船飞机票:"+costDetail.getFly()
					+",出租车:"+costDetail.getTaxi()
					+",公共汽车:"+costDetail.getBus()
					+",住宿费:"+costDetail.getLiving()
					+",通讯费:"+costDetail.getContact()
					+",业务费用:"+costDetail.getBusiness()
					+",其它:"+costDetail.getOther()
					+",补助:"+costDetail.getBuzhu();
			//记录删除
			modifyRecordService.saveRecord(account.getId(), userModel.getUsername(), "删除了:"+costDetail.getDateTime()+"的记录", new Date(), record);
		//}
		//删除明细
		expenseInfoService.deleteCostDetail(detailId);
		//减掉合计
		costSum.setFly(String.valueOf(Double.valueOf(costSum.getFly())-Double.valueOf(costDetail.getFly())));
		costSum.setTaxi(String.valueOf(Double.valueOf(costSum.getTaxi())-Double.valueOf(costDetail.getTaxi())));
		costSum.setBus(String.valueOf(Double.valueOf(costSum.getBus())-Double.valueOf(costDetail.getBus())));
		costSum.setLiving(String.valueOf(Double.valueOf(costSum.getLiving())-Double.valueOf(costDetail.getLiving())));
		costSum.setContact(String.valueOf(Double.valueOf(costSum.getContact())-Double.valueOf(costDetail.getContact())));
		costSum.setBusiness(String.valueOf(Double.valueOf(costSum.getBusiness())-Double.valueOf(costDetail.getBusiness())));
		costSum.setOther(String.valueOf(Double.valueOf(costSum.getOther())-Double.valueOf(costDetail.getOther())));
		costSum.setBuzhu(String.valueOf(Double.valueOf(costSum.getBuzhu())-Double.valueOf(costDetail.getBuzhu())));
		costSum.setAccount(String.valueOf(Double.valueOf(costSum.getAccount())-Double.valueOf(costDetail.getAccount())));
		expenseInfoService.updateCostDetail(costSum);
		String sum = request.getParameter("sum");
		account.setSum(Double.valueOf(sum));
		expenseInfoService.updateExpenseInfo(account);
		return null;
	}
	
	/**
	 * 删除费用明细
	 * @return
	 * lhyan3
	 * 2014年7月21日
	 */
	public String deleteTravelDetail(){
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		String detailId = request.getParameter("detailId");
		OAExpenseAccountTravelDetail travelDetail = expenseInfoService.getTravelDetail(detailId);
		OAExpenseAccountTravelDetail travelSum = expenseInfoService.getTravelSum(travelDetail.getExpenseId());
		OAExpenseAccount account = expenseInfoService.getExpenseInfoById(travelDetail.getExpenseId());
		//if(UserRoleConfig.ifHr(userModel)){
			String record = "业务费用:"+travelDetail.getYewu()
					+",市内交通:"+travelDetail.getTraffic()
					+",其它费用:"+travelDetail.getAnother();
			modifyRecordService.saveRecord(account.getId(), userModel.getUsername(), "删除了:"+travelDetail.getDdTime()+"的记录", new Date(), record);
		//}
		expenseInfoService.deleteTravelDetail(detailId);
		travelSum.setAnother(nf.format(Double.valueOf(travelSum.getAnother())-Double.valueOf(travelDetail.getAnother())));
		travelSum.setTraffic(nf.format(Double.valueOf(travelSum.getTraffic())-Double.valueOf(travelDetail.getTraffic())));
		travelSum.setYewu(nf.format(Double.valueOf(travelSum.getYewu())-Double.valueOf(travelDetail.getYewu())));
		expenseInfoService.updateTravel(travelSum);
		//更新总和
		String sum = request.getParameter("sum");
		account.setSum(Double.valueOf(sum));
		expenseInfoService.updateExpenseInfo(account);
		return null;
	}
	
	/**
	 * 修改费用明细
	 * @return
	 * lhyan3
	 * 2014年7月21日
	 */
	public String modifyTravelDetail(){
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		String detailId = request.getParameter("detailId");
		OAExpenseAccountTravelDetail travelDetail = expenseInfoService.getTravelDetail(detailId);
		OAExpenseAccountTravelDetail travelSum = expenseInfoService.getTravelSum(travelDetail.getExpenseId());
		OAExpenseAccount account = expenseInfoService.getExpenseInfoById(travelDetail.getExpenseId());
		String subValue = request.getParameter("subValue");
		if(subValue!=null && !"".equals(subValue)){
			String values[] = subValue.split(",");
			if(values.length>0){
				travelSum.setYewu(nf.format(Double.valueOf(travelSum.getYewu())-Double.valueOf(travelDetail.getYewu())+Double.valueOf(values[0])));
				travelSum.setTraffic(nf.format(Double.valueOf(travelSum.getTraffic())-Double.valueOf(travelDetail.getTraffic())+Double.valueOf(values[1])));
				travelSum.setAnother(nf.format(Double.valueOf(travelSum.getAnother())-Double.valueOf(travelDetail.getAnother())+Double.valueOf(values[2])));
				//if(UserRoleConfig.ifHr(userModel)){
					String record = "业务费用:"+travelDetail.getYewu()
							+",市内交通:"+travelDetail.getTraffic()
							+",其它费用:"+travelDetail.getAnother();
					modifyRecordService.saveRecord(account.getId(), userModel.getUsername(), "修改了:"+travelDetail.getDdTime()+"的记录", new Date(), record);
				//}
				travelDetail.setYewu(values[0]);
				travelDetail.setTraffic(values[1]);
				travelDetail.setAnother(values[2]);
				expenseInfoService.updateTravel(travelDetail);
				expenseInfoService.updateTravel(travelSum);
			}
			String sum = request.getParameter("sum");
			account.setSum(Double.valueOf(sum));
			expenseInfoService.updateExpenseInfo(account);
		}
		
		return null;
	}

	/**
	 * 获取下载的文件名
	 * @return
	 * lhyan3
	 * 2014年7月17日
	 */
	public String getDownloadFileName() {
		String downFileName = Configure.getProperty("expenseTemplateRealName");
		try {
			downFileName = new String(downFileName.getBytes("gb2312"), "ISO8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return downFileName;
	}

	/**
	 * 下载文件
	 * 
	 * @return lhyan3 2014年6月19日
	 */
	public String download() {
		String downfileMessage = "";
		try {
			String dir = NStrafFileUtils.getExpenseTemplatePath();
			File file = new File(dir);
			inputStream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			downfileMessage = "服务器上不存在该文件,请联系管理员";
			e.printStackTrace();
			request.setAttribute("downfileMessage", downfileMessage);
			return "error";
		}catch (Exception e) {
			downfileMessage = "其它未知错误,请联系管理员";
			request.setAttribute("downfileMessage", downfileMessage);
			e.printStackTrace();
			return "error";
		}
		request.setAttribute("downfileMessage", downfileMessage);
		return "download";
	}

	/**
	 * 查看详情页面
	 * 
	 * @return
	 */
	public String view() {
		try {
			String expenseNum = request.getParameter("expenseNum");
			OAExpenseAccount account = new OAExpenseAccount();
			account = expenseInfoService.getExpenseInfoByexpenseNum(expenseNum);
			// 获取审批记录
			String record = "";
			List<ApprovalRecord> records = approvalService.getRecordByName(account.getId());
			for (int i =0;i<records.size();i++) {
				record +="["+records.get(i).getResult()+"] "+records.get(i).getApprovalUser()+
						"("+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(records.get(i).getApprovalTime())+
						"):"+records.get(i).getOpinion()+"<br/>";
			}
			request.setAttribute("record", record.trim());
			//出差报销明细
			expenselist = expenseInfoService.getExpenseDetailByExpenseId(account.getId());
			request.setAttribute("expenselist", expenselist);
			
			//合计
			OAExpenseAccountCheckCondition costSum = expenseInfoService.getCostSum(account.getId());
			request.getSession().setAttribute("costSum", costSum);
			//费用说明报销明细
			travelDetailList = expenseInfoService.getTravelDetailByExpenseId(account.getId());
			request.setAttribute("travelDetailList", travelDetailList);
			//合计
			OAExpenseAccountTravelDetail travelSum = expenseInfoService.getTravelSum(account.getId());
			request.getSession().setAttribute("travelSum", travelSum);
			request.setAttribute("expenseInfoList", account);
			
			//项目金额
			expensePrjList = expenseInfoService.getExpenseProjectSumByExpenseId(account.getId());
			request.getSession().setAttribute("savePrjSumList", expensePrjList);
			request.getSession().setAttribute("prjSumSize", expensePrjList.size());
			//明细金额中的项目名称
			List<Project> projects = projectService.listAllGroup();
			request.getSession().setAttribute("projectSelect", projects);
			
			List<ModifyRecord> modifyRecords = modifyRecordService.findRecordByModifyId(account.getId());
			String modifyRecord = "";
			if(modifyRecords!=null && modifyRecords.size()>0){
				for(ModifyRecord r:modifyRecords ){
					modifyRecord +=r.getUsername()+"("+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(r.getModifyDate())+")"+r.getOperate()+",操作前的数据为："+r.getRecorde();
					modifyRecord += "<br/>";
				}
				
			}
			request.setAttribute("modifyRecord", modifyRecord);
		} catch (Exception e) {
			MsgBox msgBox = new MsgBox(request, "查看详情失败！");
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}
		return "view";
	}

	/**
	 * 跳转到增加报销记录页面
	 * 
	 * @return
	 */
	public String toAddPage() throws Exception {
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		Map<String, String> deptkeyValue = BusnDataDir
				.getMapKeyValue("staffManager.department");
		String DetName = deptkeyValue.get(userModel.getDeptName());
		prjName = projectService.getProjectNameByUserid(userModel.getUserid());
		request.setAttribute("userName", userModel.getUsername());
		request.setAttribute("DetName", DetName);
		request.setAttribute("prjName", prjName);
		request.setAttribute("uuid", UUID.randomUUID());
		request.getSession().setAttribute("expenseList", null);
		request.getSession().setAttribute("costSum", "");
		request.getSession().setAttribute("travelDetailList", null);
		request.getSession().setAttribute("travelSum", "");
		request.getSession().setAttribute("savePrjSumList", null);
		request.getSession().setAttribute("prjSumSize", null);
		request.getSession().setAttribute("expenseSum", null);
		request.getSession().setAttribute("updatemanid", null);
		request.getSession().setAttribute("note", null);
		request.getSession().setAttribute("status", null);
		List<SysUser> approveList = UserRoleConfig.getHrOaFinaByUser(userModel);
		request.setAttribute("approvePerson", approveList);
		//是否为行政人员或财务人员
		if(UserDataRoleConfig.viewAllDataByUserid(userModel.getUserid(), "expense.userdata.isHr")){
			request.setAttribute("status", "1");
		}else if(UserDataRoleConfig.viewAllDataByUserid(userModel.getUserid(), "expense.userdata.isFinancial")){
			request.setAttribute("status", "2");
		}else{
			request.setAttribute("status", "0");
		}
		return "add";
	}

	/**
	 * 增加数据
	 * 
	 * @return
	 */
	public String add() throws Exception {
		try {
			UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
			Date date = new Date();
			expenselist = (List<OAExpenseAccountCheckCondition>) request.getSession().getAttribute("expenseList");
			travelDetailList = (List<OAExpenseAccountTravelDetail>) request.getSession().getAttribute("travelDetailList");
			OAExpenseAccountCheckCondition costSum = null;
			if(!"".equals(request.getSession().getAttribute("costSum"))){
				costSum = (OAExpenseAccountCheckCondition) request.getSession().getAttribute("costSum");
			}
			OAExpenseAccountTravelDetail travelSum = null;
			if(!"".equals(request.getSession().getAttribute("travelSum"))){
				travelSum = (OAExpenseAccountTravelDetail) request.getSession().getAttribute("travelSum");
			}
			
			String updateCostRow = request.getParameter("updateCostRow");
			String updateCostData = request.getParameter("updateCostData");
			String delCostRow = request.getParameter("delCostRow");
			String costSumData = request.getParameter("costSumData");
			
			//根据updateCostRow及delCostRow中的值重构，如果都为空，则无需重构
			if(!"".equals(updateCostRow)){
				//重构修改的数据
				String[] costDataArray = updateCostData.split(",");
				int count = (costDataArray.length)/9;
				String[] dataArray = new String[count];
				int len = 0;
				for(int i=0;i<costDataArray.length;i=i+9){
					String str = "";
					for(int j=i;j<i+9;j++){
						str+=","+costDataArray[j];
					}
					if(!"".equals(str)){
						str=str.substring(1);
					}
					dataArray[len++]=str;
				}
				String[] updateCostRows = updateCostRow.split(",");
				for(int i=0;i<updateCostRows.length;i++){
					int rowid = Integer.parseInt(updateCostRows[i]);
					String[] str = dataArray[i].split(",");
					expenselist.get(rowid).setFly(str[0]);
					expenselist.get(rowid).setTaxi(str[1]);
					expenselist.get(rowid).setBus(str[2]);
					expenselist.get(rowid).setLiving(str[3]);
					expenselist.get(rowid).setContact(str[4]);
					expenselist.get(rowid).setBusiness(str[5]);
					expenselist.get(rowid).setOther(str[6]);
					expenselist.get(rowid).setBuzhu(str[7]);
					expenselist.get(rowid).setAccount(str[8]);
				}
			}
			if(!"".equals(delCostRow)){
				String[] delCostRows = delCostRow.split(",");
				for(int i=0;i<delCostRows.length;i++){
					int rowid = Integer.parseInt(delCostRows[i]);
					expenselist.remove(rowid);//移除修改的数据
				}
			}
			if(!"".equals(costSumData)){
				String[] str = costSumData.split(",");
				costSum.setFly(str[0]);
				costSum.setTaxi(str[1]);
				costSum.setBus(str[2]);
				costSum.setLiving(str[3]);
				costSum.setContact(str[4]);
				costSum.setBusiness(str[5]);
				costSum.setOther(str[6]);
				costSum.setBuzhu(str[7]);
				costSum.setAccount(str[8]);
			}
			
			String updateTravelRow = request.getParameter("updateTravelRow");
			String updateTravelData = request.getParameter("updateTravelData");
			String delTravelRow = request.getParameter("delTravelRow");
			String travelSumData = request.getParameter("travelSumData");
			
			//根据updateCostRow及delCostRow中的值重构，如果都为空，则无需重构
			if(!"".equals(updateTravelRow)){
				//重构修改的数据
				String[] travelDataArray = updateTravelData.split(",");
				int count = (travelDataArray.length)/3;
				String[] dataArray = new String[count];
				int len = 0;
				for(int i=0;i<travelDataArray.length;i=i+3){
					String str = "";
					for(int j=i;j<i+3;j++){
						str+=","+travelDataArray[j];
					}
					if(!"".equals(str)){
						str=str.substring(1);
					}
					dataArray[len++]=str;
				}
				String[] updateTravelRows = updateTravelRow.split(",");
				for(int i=0;i<updateTravelRows.length;i++){
					int rowid = Integer.parseInt(updateTravelRows[i]);
					String[] str = dataArray[i].split(",");
					travelDetailList.get(rowid).setYewu(str[0]);
					travelDetailList.get(rowid).setTraffic(str[1]);
					travelDetailList.get(rowid).setAnother(str[2]);
				}
			}
			if(!"".equals(delTravelRow)){
				String[] delTravelRows = delTravelRow.split(",");
				for(int i=0;i<delTravelRows.length;i++){
					int rowid = Integer.parseInt(delTravelRows[i]);
					travelDetailList.remove(rowid);//移除修改的数据
				}
			}
			
			if(!"".equals(travelSumData)){
				String[] str = travelSumData.split(",");
				travelSum.setYewu(str[0]);
				travelSum.setTraffic(str[1]);
				travelSum.setAnother(str[2]);
			}
			
			expenseAccount.setExpenseNum(expenseInfoService.getNextEno());// 报销流水号
			expenseAccount.setSubmitDate(date);
			expenseAccount.setType("出差费用报销");
			//expenseAccount.setUserName(userModel.getUsername());
			//expenseAccount.setPrjName(prjName);
			//expenseAccount.setDetName(DetName);
//			expenseAccount.setStatus("0");
//			expenseAccount.setApproveSum(0.0);
			if("2".equals(expenseAccount.getStatus())){
				expenseAccount.setStatus("3");
			}
			expenseAccount.setUpdateMan(userModel.getUsername());
			expenseAccount.setUpdateDate(date);
			expenseAccount.setUserId(userModel.getUserid());
			
			//重构项目金额数据
			String expense_prjname = "";//报销项目名称
			String savePrjdata = request.getParameter("savePrjdata");
			String saveSumdata = request.getParameter("saveSumdata");
			savePrjdata=savePrjdata==null?"":savePrjdata;
			saveSumdata=saveSumdata==null?"":saveSumdata;
			List<OAExpenseAccountProject> prjsumList = new ArrayList<OAExpenseAccountProject>();
			for(int i=0;i<savePrjdata.split(",").length;i++){
				OAExpenseAccountProject prjsum = new OAExpenseAccountProject();
				prjsum.setPrjname(savePrjdata.split(",")[i]);
				prjsum.setSum(Double.parseDouble(saveSumdata.split(",")[i]));
				prjsumList.add(prjsum);
				String prjname = savePrjdata.split(",")[i];
				if(expense_prjname.indexOf(prjname)==-1){
					expense_prjname+=","+prjname;
				}
			}
			if(!"".equals(expense_prjname)){
				expense_prjname = expense_prjname.substring(1, expense_prjname.length());
			}
			expenseAccount.setExpenseprjname(expense_prjname);
			if(expenselist!=null && expenselist.size()>0){
				//存在明细
				boolean flag = false;
				flag = expenseInfoService.addExpenseInfo(expenseAccount);
				//保存明细
				for(OAExpenseAccountCheckCondition cost:expenselist){
					cost.setExpenseId(expenseAccount.getId());
				}
				expenseInfoService.saveList(expenselist);
				//保存合计
				if(costSum!=null){
					costSum.setExpenseId(expenseAccount.getId());
					expenseInfoService.saveCostDetail(costSum);
				}
				//保存明细
				if(travelDetailList!=null && travelDetailList.size()>0){
					for(OAExpenseAccountTravelDetail travel:travelDetailList){
						travel.setExpenseId(expenseAccount.getId());
					}
				}
				//保存合计
				expenseInfoService.saveList(travelDetailList);
				if(travelSum!=null){
					travelSum.setExpenseId(expenseAccount.getId());
					expenseInfoService.saveTravelDetail(travelSum);
				}
				//保存项目金额
				if(prjsumList!=null && prjsumList.size()>0){
					for(OAExpenseAccountProject prjsum:prjsumList){
						prjsum.setExpenseId(expenseAccount.getId());
					}
				}
				expenseInfoService.saveList(prjsumList);
				
				if (flag == true) {
					MsgBox msgBox = new MsgBox(request, getText("add.ok"));
					msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
					this.addActionMessage(getText("add.ok"));
				} else {
					MsgBox msgBox = new MsgBox(request, getText(
							"operInfoform.updatefaile", new String[] { "没有导入报销明细表 " }));
					msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
					addActionMessage(getText("operInfoform.addfaile"));
					msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				}
			}else{
				MsgBox msgBox = new MsgBox(request, getText("出现未知错误,请联系管理员!"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				addActionMessage(getText("添加失败"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			}
			boolean hasActionMessage = this.hasActionMessages();
			request.setAttribute("hasActionMessage", hasActionMessage);
		} catch (Exception e) {
			MsgBox msgBox = new MsgBox(request, getText("出现未知错误,请联系管理员!"));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			addActionMessage(getText("添加失败"));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		}
		return "msgBox";
	}

	/**
	 * 添加页面的导入并解析Excel表
	 * 解析后保存相关明细
	 * @return
	 * lhyan3
	 * 2014年7月18日
	 */
	public String addImportData() {
		request.getSession().setAttribute("expenseList", null);
		request.getSession().setAttribute("costSum", "");
		request.getSession().setAttribute("travelDetailList", null);
		request.getSession().setAttribute("travelSum", "");
		request.getSession().setAttribute("savePrjSumList", null);
		request.getSession().setAttribute("expenseSum", null);
		double expenseSum =0;
		String uuid = request.getParameter("uuid");
		String updatemanid = request.getParameter("updatemanid");
		String note = request.getParameter("note");
		if(!"".equals(updatemanid)){
			request.getSession().setAttribute("updatemanid", updatemanid);
		}
		if(!"".equals(note)){
			request.getSession().setAttribute("note", note);
		}
		try {
			if (file == null) {
				throw new Exception("文件为空");
			}
			// 获取文件流，并创建对应的excel对象
			POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(file));
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			int sheetNumber = wb.getNumberOfSheets();
			/** ** ** *循环每一个sheet，添加数据* ** ** **/
			for (int shNum = 0; shNum < sheetNumber; shNum++) {
				HSSFSheet sheet = wb.getSheetAt(shNum);
				HSSFRow headRow = sheet.getRow(0);

				if (headRow == null) {// 判断sheet中第一行是否有数据，没有数据则不进行下一步
					continue;
				}
				if (headRow.getCell((short) 0) != null) {
					headRow.getCell((short) 0).setCellType(
							HSSFCell.CELL_TYPE_STRING);
				}
				Double fly = 0.0;
				Double taxi = 0.0;
				Double bus = 0.0;
				Double living = 0.0;
				Double contact = 0.0;
				Double business = 0.0;
				Double other = 0.0;
				Double buzhu = 0.0;
				Double sum = 0.0;
				String path = "";
				boolean flag = false;// 判断excel中没有合计
				/** ** ** *获取表格标题，如标题不对，则格式不对* ** ** **/
				String titel = headRow.getCell((short) 0).getStringCellValue() == null ? ""
						: headRow.getCell((short) 0).getStringCellValue();
				if (titel.trim().equals("软件体系出差费用报销明细表")) {
					OAExpenseAccountCheckCondition checks = new OAExpenseAccountCheckCondition();
					int rows = 0;
					long rowNumber = sheet.getPhysicalNumberOfRows(); // sheet中的行数
					String first_prjname = "";//得出第一个项目名称，用于以后的比较
					String save_prjname = "";
					for (int rowNum = 5; rowNum < rowNumber; rowNum++) {
						OAExpenseAccountCheckCondition oaexpenseCondition = new OAExpenseAccountCheckCondition();
						OAExpenseAccountProject prjSum = new OAExpenseAccountProject();
						oaexpenseCondition.setUuid(uuid);
						//获取当前的所有列
						HSSFRow row = sheet.getRow(rowNum);
						SimpleDateFormat sdf = new SimpleDateFormat(
								"EEE MMM dd HH:mm:ss z yyyy", Locale.US);
						TimeZone tz = TimeZone.getTimeZone("GMT+8");
						sdf.setTimeZone(tz);
						String dateTime = "";
						if (row.getCell((short) 0).getCellType() == HSSFCell.CELL_TYPE_STRING) {
							dateTime = row.getCell((short) 0)
									.getStringCellValue();
						} else {
							dateTime = row.getCell((short) 0)
									.getDateCellValue() == null ? "" : String
									.valueOf(row.getCell((short) 0)
											.getDateCellValue());
							if (!dateTime.equals("")) {
								Date s = sdf.parse(dateTime);
								sdf = new SimpleDateFormat("yyyy/MM/dd");
								dateTime = sdf.format(s);
							}
						}
						if (dateTime != null && dateTime.equals("部门经理审核：")) {
							break;
						}
						if(dateTime==null || "".equals(dateTime)){
							break;
						}
						oaexpenseCondition.setDateTime(dateTime);
						String didian = "";
						if (row.getCell((short) 1) != null)
							row.getCell((short) 1).setCellType(
									HSSFCell.CELL_TYPE_STRING);
						didian = row.getCell((short) 1).getStringCellValue() == null ? ""
								: row.getCell((short) 1).getStringCellValue();
						if (didian.equals("") && !dateTime.equals("合计")) {
							continue;
						}
						oaexpenseCondition.setPlace(didian);
						
						String prjname = "";//项目名称
						if (row.getCell((short) 2) != null)
							row.getCell((short) 2).setCellType(
									HSSFCell.CELL_TYPE_STRING);
						prjname = row.getCell((short) 2).getStringCellValue() == null ? ""
								: row.getCell((short) 2).getStringCellValue();
						if("".equals(first_prjname)){
							first_prjname = prjname;
							//模糊查询出项目组名称，如果无法找到，则默认为报销表上的项目名称
							save_prjname = projectService.getProjectNameByInfo(first_prjname);
						}
						if(!first_prjname.equals(prjname)){
							first_prjname = prjname;
							//模糊查询出项目组名称，如果无法找到，则默认为报销表上的项目名称
							save_prjname = projectService.getProjectNameByInfo(first_prjname);
						}
						if("".equals(save_prjname)){
							save_prjname = prjname;
							oaexpenseCondition.setPrjname(prjname);
						}else{
							oaexpenseCondition.setPrjname(prjname);
						}
						String renwu = "";
						if (row.getCell((short) 3) != null)
							row.getCell((short) 3).setCellType(
									HSSFCell.CELL_TYPE_STRING);
						renwu = row.getCell((short) 3).getStringCellValue() == null ? ""
								: row.getCell((short) 3).getStringCellValue();
						oaexpenseCondition.setTask(renwu);
						String feiji = "0.0";
						if (row.getCell((short) 4) != null) {
							if (row.getCell((short) 4).getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
								feiji = row.getCell((short) 4)
										.getNumericCellValue() == 0.0 ? ""
										: String.valueOf(row.getCell((short) 4)
												.getNumericCellValue());
							else if (row.getCell((short) 4).getCellType() == HSSFCell.CELL_TYPE_STRING)
								feiji = row.getCell((short) 4)
										.getStringCellValue() == null ? ""
										: row.getCell((short) 4)
												.getStringCellValue();
						}
						oaexpenseCondition.setFly(feiji);
						String chuzuche = "0.0";
						if (row.getCell((short) 5) != null) {
							if (row.getCell((short) 5).getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
								chuzuche = row.getCell((short) 5)
										.getNumericCellValue() == 0.0 ? ""
										: String.valueOf(row.getCell((short) 5)
												.getNumericCellValue());
							else if (row.getCell((short) 5).getCellType() == HSSFCell.CELL_TYPE_STRING)
								chuzuche = row.getCell((short) 5)
										.getStringCellValue() == null ? ""
										: row.getCell((short) 5)
												.getStringCellValue();
						}
						oaexpenseCondition.setTaxi(chuzuche);
						String gongjiao = "0.0";
						if (row.getCell((short) 6) != null) {
							if (row.getCell((short) 6).getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
								gongjiao = row.getCell((short) 6)
										.getNumericCellValue() == 0.0 ? ""
										: String.valueOf(row.getCell((short) 6)
												.getNumericCellValue());
							else if (row.getCell((short) 6).getCellType() == HSSFCell.CELL_TYPE_STRING)
								gongjiao = row.getCell((short) 6)
										.getStringCellValue() == null ? ""
										: row.getCell((short) 6)
												.getStringCellValue();
						}
						oaexpenseCondition.setBus(gongjiao);
						String zhusu = "0.0";
						if (row.getCell((short) 7).getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
							zhusu = row.getCell((short) 7)
									.getNumericCellValue() == 0.0 ? "" : String
									.valueOf(row.getCell((short) 7)
											.getNumericCellValue());
						else if (row.getCell((short) 7).getCellType() == HSSFCell.CELL_TYPE_STRING)
							zhusu = row.getCell((short) 7).getStringCellValue() == null ? ""
									: row.getCell((short) 7)
											.getStringCellValue();
						oaexpenseCondition.setLiving(zhusu);
						String tongxun = "0.0";
						if (row.getCell((short) 8) != null) {
							if (row.getCell((short) 8).getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
								tongxun = row.getCell((short) 8)
										.getNumericCellValue() == 0.0 ? ""
										: String.valueOf(row.getCell((short) 8)
												.getNumericCellValue());
							else if (row.getCell((short) 8).getCellType() == HSSFCell.CELL_TYPE_STRING)
								tongxun = row.getCell((short) 8)
										.getStringCellValue() == null ? ""
										: row.getCell((short) 8)
												.getStringCellValue();
						}
						oaexpenseCondition.setContact(tongxun);
						String yw = "0.0";
						if (row.getCell((short) 9) != null) {
							if (row.getCell((short) 9).getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
								yw = row.getCell((short) 9)
										.getNumericCellValue() == 0.0 ? ""
										: String.valueOf(row.getCell((short) 9)
												.getNumericCellValue());
							else if (row.getCell((short) 9).getCellType() == HSSFCell.CELL_TYPE_STRING)
								yw = row.getCell((short) 9)
										.getStringCellValue() == null ? ""
										: row.getCell((short) 9)
												.getStringCellValue();
						}
						oaexpenseCondition.setBusiness(yw);
						String qita = "0.0";
						if (row.getCell((short) 10) != null) {
							if (row.getCell((short) 10).getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
								qita = row.getCell((short) 10)
										.getNumericCellValue() == 0.0 ? ""
										: String.valueOf(row.getCell((short) 10)
												.getNumericCellValue());
							else if (row.getCell((short) 10).getCellType() == HSSFCell.CELL_TYPE_STRING)
								qita = row.getCell((short) 10)
										.getStringCellValue() == null ? ""
										: row.getCell((short) 10)
												.getStringCellValue();
						}
						oaexpenseCondition.setOther(qita);
						String bz = "0.0";
						if (row.getCell((short) 11) != null) {
							if (row.getCell((short) 11).getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
								bz = row.getCell((short) 11)
										.getNumericCellValue() == 0.0 ? ""
										: String.valueOf(row
												.getCell((short) 11)
												.getNumericCellValue());
							else if (row.getCell((short) 11).getCellType() == HSSFCell.CELL_TYPE_STRING)
								bz = row.getCell((short) 11)
										.getStringCellValue() == null ? ""
										: row.getCell((short) 11)
												.getStringCellValue();
						}
						oaexpenseCondition.setBuzhu(bz);
						String xiaoji = "0.0";
						Double total = 0.0;
						total = Double.valueOf(bz) + Double.valueOf(qita)
								+ Double.valueOf(yw) + Double.valueOf(tongxun)
								+ Double.valueOf(zhusu)
								+ Double.valueOf(gongjiao)
								+ Double.valueOf(chuzuche)
								+ Double.valueOf(feiji);
						//单行的合计
						xiaoji = String.valueOf(total);
						oaexpenseCondition.setAccount(xiaoji);
						//将项目名称及项目金额放在prjSum——list中
						prjSum.setPrjname(save_prjname);
						prjSum.setSum(Double.parseDouble(xiaoji));
						expensePrjList.add(prjSum);
						String luxian = "";
						//如果当前的路线为空，则读取上一条路线
						if (row.getCell((short) 13).getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
							luxian = row.getCell((short) 13)
									.getNumericCellValue() == 0.0 ? ""
									: String.valueOf(row.getCell((short) 13)
											.getNumericCellValue());
						else  if (row.getCell((short) 13).getCellType() == HSSFCell.CELL_TYPE_STRING)
							luxian = row.getCell((short) 13).getStringCellValue() == null ? ""
									: row.getCell((short) 13).getStringCellValue();
						if (luxian.equals("")) {
							oaexpenseCondition.setPath(path);
						} else {
							path = luxian;
							oaexpenseCondition.setPath(path);
						}
						//列的合计
						fly = fly + Double.valueOf(feiji);
						taxi = taxi + Double.valueOf(chuzuche);
						bus = bus + Double.valueOf(gongjiao);
						living = living + Double.valueOf(zhusu);
						contact = contact + Double.valueOf(tongxun);
						business = business + Double.valueOf(yw);
						other = other + Double.valueOf(qita);
						buzhu = buzhu + Double.valueOf(bz);
						sum = sum + Double.valueOf(xiaoji);
						oaexpenseCondition.setNumber(rowNum);
						rows = rowNum + 1;
						if (dateTime != null && dateTime.equals("合计")) {
							//excel中有合计行，如果是合计行
							flag = true;
							BigDecimal bg = new BigDecimal(taxi);
							taxi = bg.setScale(1, BigDecimal.ROUND_HALF_UP)
									.doubleValue();
							oaexpenseCondition.setFly(String.valueOf(fly));
							oaexpenseCondition.setTaxi(String.valueOf(taxi));
							oaexpenseCondition.setBus(String.valueOf(bus));
							oaexpenseCondition
									.setLiving(String.valueOf(living));
							oaexpenseCondition.setContact(String
									.valueOf(contact));
							oaexpenseCondition.setBusiness(String
									.valueOf(business));
							oaexpenseCondition.setUuid(uuid);
							oaexpenseCondition.setOther(String.valueOf(other));
							oaexpenseCondition.setBuzhu(String.valueOf(buzhu));
							oaexpenseCondition.setAccount(String.valueOf(sum));
							oaexpenseCondition.setPath("");
							oaexpenseCondition.setDateTime("合计");
							oaexpenseCondition.setPrjname("");
							expenseSum += sum;
							request.getSession().setAttribute("expenseSum", expenseSum);
							//合计不放入list中，单独放进request，控制页面显示
							request.getSession().setAttribute("costSum", oaexpenseCondition);
						}else {
							expenseList.add(oaexpenseCondition);
						}
						//expenseInfoService.addOAExpenseCheckCondition(oaexpenseCondition);
						if(flag){
							break;
						}
					}
					if (!flag) {
						//如果excel中没有合计，那么系统中加合计列
						BigDecimal bg = new BigDecimal(taxi);
						taxi = bg.setScale(1, BigDecimal.ROUND_HALF_UP)
								.doubleValue();
						checks.setFly(String.valueOf(fly));
						checks.setUuid(uuid);
						checks.setDateTime("合计");
						checks.setPrjname("");
						checks.setTaxi(String.valueOf(taxi));
						checks.setBus(String.valueOf(bus));
						checks.setLiving(String.valueOf(living));
						checks.setContact(String.valueOf(contact));
						checks.setBusiness(String.valueOf(business));
						checks.setOther(String.valueOf(other));
						checks.setBuzhu(String.valueOf(buzhu));
						checks.setAccount(String.valueOf(sum));
						expenseSum += sum;
						checks.setPath("");
						checks.setNumber(rows);
						request.getSession().setAttribute("expenseSum", expenseSum);
						//合计不放入list中，单独放进request，控制页面显示
						//expenseInfoService.addOAExpenseCheckCondition(checks);
						if(expenseList!=null && expenseList.size()>0){
							request.getSession().setAttribute("costSum", checks);
						}
					}
					flag = false;
					request.getSession().setAttribute("expenseList", expenseList);
				} else if (titel.trim().equals("软件体系费用说明表")) {
					long rownumber = sheet.getPhysicalNumberOfRows();
					Double yewusum = 0.0;
					Double officesum = 0.0;
					Double qitasum = 0.0;
					int no = 0;
					OAExpenseAccountTravelDetail travel = new OAExpenseAccountTravelDetail();
					int rows = 0;
					String first_prjname = "";
					String save_prjname = "";
					for (int rownum = 5; rownum < rownumber; rownum++) {
						SimpleDateFormat sdf = new SimpleDateFormat(
								"EEE MMM dd HH:mm:ss z yyyy", Locale.US);
						TimeZone tz = TimeZone.getTimeZone("GMT+8");
						sdf.setTimeZone(tz);
						OAExpenseAccountTravelDetail travelDetail = new OAExpenseAccountTravelDetail();
						OAExpenseAccountProject prjSum = new OAExpenseAccountProject();
						HSSFRow row = sheet.getRow(rownum);
						if (no + 1 == rownum) {
							break;
						}
						String dtime = "";
						if (row.getCell((short) 0).getCellType() == HSSFCell.CELL_TYPE_STRING) {
							dtime = row.getCell((short) 0).getStringCellValue() == null ? ""
									: row.getCell((short) 0)
											.getStringCellValue();
						} else {
							dtime = row.getCell((short) 0).getDateCellValue() == null ? ""
									: String.valueOf(row.getCell((short) 0)
											.getDateCellValue());
							if (!dtime.equals("")) {
								Date s = sdf.parse(dtime);
								sdf = new SimpleDateFormat("yyyy/MM/dd");
								dtime = sdf.format(s);
							}
						}
						if (dtime != null && dtime.equals("总计")) {
							no = rownum;
						}
						if (dtime.equals("")) {
							flag = false;
							break;
						}
						travelDetail.setDdTime(dtime);
						String address = "";
						address = row.getCell((short) 1).getStringCellValue() == null ? ""
								: row.getCell((short) 1).getStringCellValue();
						if (address.equals("") && "".equals(dtime)) {
							continue;
						}
						travelDetail.setDidian(address);
						
						String prjname = "";//项目名称
						if (row.getCell((short) 2) != null)
							row.getCell((short) 2).setCellType(
									HSSFCell.CELL_TYPE_STRING);
						prjname = row.getCell((short) 2).getStringCellValue() == null ? ""
								: row.getCell((short) 2).getStringCellValue();
						if("".equals(first_prjname)){
							first_prjname = prjname;
							//模糊查询出项目组名称，如果无法找到，则默认为报销表上的项目名称
							save_prjname = projectService.getProjectNameByInfo(first_prjname);
						}
						if(!first_prjname.equals(prjname)){
							first_prjname = prjname;
							//模糊查询出项目组名称，如果无法找到，则默认为报销表上的项目名称
							save_prjname = projectService.getProjectNameByInfo(first_prjname);
						}
						if("".equals(save_prjname)){
							save_prjname = prjname;
							travelDetail.setPrjname(prjname);
						}else{
							travelDetail.setPrjname(prjname);
						}
						String feiyong = "0.0";
						if (row.getCell((short) 3) != null) {
							if (row.getCell((short) 3).getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
								feiyong = row.getCell((short) 3)
										.getNumericCellValue() == 0.0 ? ""
										: String.valueOf(row.getCell((short) 3)
												.getNumericCellValue());
							else if (row.getCell((short) 3).getCellType() == HSSFCell.CELL_TYPE_STRING)
								feiyong = row.getCell((short) 3)
										.getStringCellValue() == null ? ""
										: row.getCell((short) 3)
												.getStringCellValue();
						}
						travelDetail.setYewu(feiyong);
						String jiaotong = "0.0";
						if (row.getCell((short) 4) != null) {
							if (row.getCell((short) 4).getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
								jiaotong = row.getCell((short) 4)
										.getNumericCellValue() == 0.0 ? ""
										: String.valueOf(row.getCell((short) 4)
												.getNumericCellValue());
							else if (row.getCell((short) 4).getCellType() == HSSFCell.CELL_TYPE_STRING)
								jiaotong = row.getCell((short) 4)
										.getStringCellValue() == null ? ""
										: row.getCell((short) 4)
												.getStringCellValue();
						}
						travelDetail.setTraffic(jiaotong);
						String another = "0.0";
						if (row.getCell((short) 5) != null) {
							if (row.getCell((short) 5).getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
								another = row.getCell((short) 5)
										.getNumericCellValue() == 0.0 ? ""
										: String.valueOf(row.getCell((short) 5)
												.getNumericCellValue());
							else if (row.getCell((short) 5).getCellType() == HSSFCell.CELL_TYPE_STRING)
								another = row.getCell((short) 5)
										.getStringCellValue() == null ? ""
										: row.getCell((short) 5)
												.getStringCellValue();
						}
						travelDetail.setAnother(another);
						String notice = "";
						if (row.getCell((short) 6) != null) {
							if (row.getCell((short) 6).getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
								notice = row.getCell((short) 6)
										.getNumericCellValue() == 0.0 ? ""
										: String.valueOf(row.getCell((short) 6)
												.getNumericCellValue());
							else if (row.getCell((short) 6).getCellType() == HSSFCell.CELL_TYPE_STRING)
								notice = row.getCell((short) 6)
										.getStringCellValue() == null ? ""
										: row.getCell((short) 6)
												.getStringCellValue();
						}
						travelDetail.setNote(notice);
						travelDetail.setUuid(uuid);
						yewusum += Double.valueOf(feiyong);
						officesum += Double.valueOf(jiaotong);
						qitasum += Double.valueOf(another);
						travelDetail.setNumber(rownum);
						//将项目名称及项目金额放在prjSum——list中
						prjSum.setPrjname(save_prjname);
						prjSum.setSum(Double.valueOf(feiyong)+Double.valueOf(jiaotong)+Double.valueOf(another));
						expensePrjList.add(prjSum);
						rows = rownum;
						if (travelDetail.getDdTime() != null
								&& "合计".equals(dtime)) {
							flag = true;
							Double total = 0.0;
							total = yewusum + officesum + qitasum;
							travelDetail.setYewu(String.valueOf(yewusum));
							travelDetail.setTraffic(String.valueOf(officesum));
							travelDetail.setAnother(String.valueOf(qitasum));
							travelDetail.setNote(String.valueOf(total));
							travelDetail.setPrjname("");
							expenseSum += total;
							request.getSession().setAttribute("expenseSum", expenseSum);
							request.getSession().setAttribute("travelSum", travelDetail);
						}else{
							travelDetailList.add(travelDetail);
						}
						//expenseInfoService.addOAExpenseTravelDetail(travelDetail);
						if(flag){
							//到了合计就跳出循环
							break;
						}
						request.getSession().setAttribute("travelDetailList", travelDetailList);
					}
					if (!flag) {
						Double total = 0.0;
						total = yewusum + officesum + qitasum;
						travel.setDdTime("合计");
						travel.setDidian("");
						travel.setPrjname("");
						travel.setUuid(uuid);
						travel.setYewu(String.valueOf(yewusum));
						travel.setTraffic(String.valueOf(officesum));
						travel.setAnother(String.valueOf(qitasum));
						travel.setNote("");
						travel.setNumber(rows);
						expenseSum += total;
						request.getSession().setAttribute("expenseSum", expenseSum);
						//expenseInfoService.addOAExpenseTravelDetail(travel);
						if(travelDetailList!=null && travelDetailList.size()>0){
							request.getSession().setAttribute("travelSum", travel);
						}
					}
				} else {
					MsgBox msgBox = new MsgBox(request, getText("请导入正确的报销明细表，谢谢！"));
					msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
					return "msgBox";
				}
			}
			if (expenseList.size()<0 && travelDetailList.size()<0) {//将expenselist修改为expenseList
				MsgBox msgBox = new MsgBox(request, getText(
						"operInfoform.updatefaile",
						new String[] { "请导入正确的报销明细表，谢谢！" }));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}
			if (!(expenseList.size()==0 && travelDetailList.size()==0)) {//将expenselist修改为expenseList
				//按照项目名称排序
				Collections.sort(expensePrjList, new Comparator<OAExpenseAccountProject>() {
					public int compare(OAExpenseAccountProject log1, OAExpenseAccountProject log2) {
						if(log1.getPrjname().compareTo(log2.getPrjname())<0){
							return 1;
						}else{
							return -1;
						}
					}
				});
				String first_prjname = "";
				String prjname = "";
				Double prjsum = 0.0;
				List<OAExpenseAccountProject> savePrjSumList = new ArrayList<OAExpenseAccountProject>();
				for(OAExpenseAccountProject record:expensePrjList){
					OAExpenseAccountProject savePrjSum = new OAExpenseAccountProject();
					prjname = record.getPrjname();
					if("".equals(first_prjname)){
						first_prjname=prjname;
						prjsum+=record.getSum();
					}else if(first_prjname.equals(prjname)){
						prjsum+=record.getSum();
					}else{
						savePrjSum.setPrjname(first_prjname);
						savePrjSum.setSum(prjsum);
						first_prjname = prjname;
						prjsum = record.getSum();
						savePrjSumList.add(savePrjSum);
					}
				}
				OAExpenseAccountProject savePrjSum = new OAExpenseAccountProject();
				savePrjSum.setPrjname(first_prjname);
				savePrjSum.setSum(prjsum);
				savePrjSumList.add(savePrjSum);
				
				request.getSession().setAttribute("savePrjSumList", savePrjSumList);
				request.getSession().setAttribute("prjSumSize", savePrjSumList.size());
				List<Project> projects = projectService.listAllGroup();
				request.getSession().setAttribute("projectSelect", projects);
			}
			
			//是否为行政人员或财务人员
			UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
			if(UserDataRoleConfig.viewAllDataByUserid(userModel.getUserid(), "expense.userdata.isHr")){
				request.getSession().setAttribute("status", "1");
			}else if(UserDataRoleConfig.viewAllDataByUserid(userModel.getUserid(), "expense.userdata.isFinancial")){
				request.getSession().setAttribute("status", "2");
			}else{
				request.getSession().setAttribute("status", "0");
			}
		} catch (Exception e) {// 捕获异常处理如下
			e.printStackTrace();
			SysLog.error(e);
			MsgBox msgBox1 = new MsgBox(request, getText(
					"operInfoform.addfaile", new String[] { "没有导入报销明细表 " }));
			msgBox1.setButtonType(MsgBox.BUTTON_CLOSE);
			this.addActionMessage(getText("operInfoform.addfaile"));
			boolean hasActionMessage = this.hasActionMessages();
			request.setAttribute("hasActionMessage", hasActionMessage);
			return "msgBox";
		}
		if (expenseList.size()==0 && travelDetailList.size()==0) {//将expenselist修改为expenseList
			MsgBox msgBox = new MsgBox(request, "增加成功，但报销明细表中无任何数据");
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}else{
			MsgBox msgBox = new MsgBox(request, getText("add.ok"));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}
	}

	/**
	 * 行政人员审批
	 * 
	 * @return
	 * @throws Exception
	 */
	public String audit() throws Exception {
		try {
			UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
			String expenseId = request.getParameter("expenseId");
			OAExpenseAccount expense = expenseInfoService.getExpenseInfoById(expenseId);
			expense.setUpdateDate(Calendar.getInstance().getTime());
			expense.setUpdateMan(userModel.getUsername());
			expense.setUpdatemanid(expenseAccount.getUpdatemanid());
			String approveStatus = request.getParameter("approveStatus");
			String approveContent = request.getParameter("approveContent");
			if("1".equals(approveStatus)){//通过
				expense.setStatus("2");
				expense.setUpdatemanid(expenseAccount.getUpdatemanid());
				expense.setUpdateMan(userModel.getUsername());
				expense.setUpdateDate(new Date());
				approvalService.makeRecored(expense.getId(), "行政人员审批",userModel.getUsername(), approveContent, "行政审核通过");
			}else{
				expense.setStatus("4");
				expense.setUpdatemanid(userModel.getUserid());
				expense.setUpdateMan(userModel.getUsername());
				expense.setUpdateDate(new Date());
				approvalService.makeRecored(expense.getId(), "行政人员审批",userModel.getUsername(), approveContent, "行政审核不通过");
			}
			expense.setNote(expenseAccount.getNote());
			expense.setReason(expenseAccount.getReason());
			expenseInfoService.updateExpenseInfo(expense);
//			if (expense.getStatus().equals("3")) {
//				approvalService.makeRecored(expense.getExpenseNum(), "报销审批已发款",
//						userModel.getUsername(), "无", expense.getStatus());
//			} else if ("5".equals(expense.getStatus())) {
//				approvalService.makeRecored(expense.getExpenseNum(), "报销审批被打回",
//						userModel.getUsername(), expenseAccount.getReason(),
//						expenseAccount.getStatus());
//			}
			//检测项目金额是否有新增数据，如果有则保存
//			OAExpenseAccount account = expenseInfoService.getExpenseInfoById(expenseId);
			String savePrjdata = request.getParameter("noSavePrjname");
			String saveSumdata = request.getParameter("noSavePrjsum");
			savePrjdata=savePrjdata==null?"":savePrjdata;
			saveSumdata=saveSumdata==null?"":saveSumdata;
			if(!"".equals(savePrjdata)){
				List<OAExpenseAccountProject> prjsumList = new ArrayList<OAExpenseAccountProject>();
				for(int i=0;i<savePrjdata.split(",").length;i++){
					OAExpenseAccountProject prjsum = new OAExpenseAccountProject();
					prjsum.setPrjname(savePrjdata.split(",")[i]);
					prjsum.setSum(Double.parseDouble(saveSumdata.split(",")[i]));
					prjsum.setExpenseId(expenseId);
					prjsumList.add(prjsum);
				}
				expenseInfoService.saveList(prjsumList);
				for(OAExpenseAccountProject project:prjsumList){
					//if(UserRoleConfig.ifHr(userModel)){
//						String record = "项目名称:"+project.getPrjname() +",项目金额:"+project.getSum();
						modifyRecordService.saveRecord(expenseId, userModel.getUsername(), "新增了:"+project.getPrjname()+"的记录", new Date(), " ");
					//}
				}
			}
			//坚持项目金额是否有修改
			String uppPrjdata = request.getParameter("noUpdatePrjname");
			String uppSumdata = request.getParameter("noUpdatePrjsum");
			String ids = request.getParameter("noUpdateId");
			uppPrjdata=uppPrjdata==null?"":uppPrjdata;
			uppSumdata=uppSumdata==null?"":uppSumdata;
			ids=ids==null?"":ids;
			if(!"".equals(uppPrjdata)){
				for(int i=0;i<uppPrjdata.split(",").length;i++){
					OAExpenseAccountProject prjsum = expenseInfoService.getProjectSumDetail(ids.split(",")[i]);
					//if(UserRoleConfig.ifHr(userModel)){
						if(!(prjsum.getPrjname().equals(uppPrjdata.split(",")[i]) && prjsum.getSum().equals(Double.parseDouble(uppSumdata.split(",")[i])))){
							String record = "项目名称:"+prjsum.getPrjname() +",项目金额:"+prjsum.getSum();
							modifyRecordService.saveRecord(expenseId, userModel.getUsername(), "修改了:"+uppPrjdata.split(",")[i]+"的记录", new Date(), record);
						}
					//}
					prjsum.setPrjname(uppPrjdata.split(",")[i]);
					prjsum.setSum(Double.parseDouble(uppSumdata.split(",")[i]));
					expenseInfoService.updateExpenseProjectSumInfo(prjsum);
				}
			}
			
			//更新报销项目名称
			List<OAExpenseAccountProject> projectList = expenseInfoService.getExpenseProjectSumByExpenseId(expense.getId());
			String expense_prjname="";
			for(OAExpenseAccountProject project:projectList){
				String prjname = project.getPrjname();
				if(expense_prjname.indexOf(prjname)==-1){
					expense_prjname+=","+prjname;
				}
			}
			if(!"".equals(expense_prjname)){
				expense_prjname=expense_prjname.substring(1, expense_prjname.length());
			}
			expense.setExpenseprjname(expense_prjname);
			expenseInfoService.updateExpenseInfo(expense);
			
			MsgBox msgBox = new MsgBox(request, "审批成功！");
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			MsgBox msgBox1 = new MsgBox(request, getText(
					"operInfoform.addfaile", new String[] { "审批失败！ " }));
			msgBox1.setButtonType(MsgBox.BUTTON_CLOSE);
			this.addActionMessage(getText("operInfoform.addfaile"));
			boolean hasActionMessage = this.hasActionMessages();
			request.setAttribute("hasActionMessage", hasActionMessage);
			return "msgBox";
		}
		return "msgBox";
	}
	
	/**
	 * 财务人员审批
	 * 
	 * @return
	 * @throws Exception
	 */
	public String finAudit() throws Exception {
		try {
			UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
			String expenseId = request.getParameter("expenseId");
			OAExpenseAccount expense = expenseInfoService.getExpenseInfoById(expenseId);
			expense.setUpdateDate(Calendar.getInstance().getTime());
			expense.setUpdateMan(userModel.getUsername());
			expense.setUpdatemanid(expenseAccount.getUpdatemanid());
			String approveStatus = request.getParameter("approveStatus");
			String approveContent = request.getParameter("approveContent");
			if("1".equals(approveStatus)){//通过
				expense.setStatus("3");
				expense.setUpdateMan(userModel.getUsername());
				expense.setUpdateDate(new Date());
				approvalService.makeRecored(expense.getId(), "财务人员审批",userModel.getUsername(), approveContent, "财务审核通过，已发款");
			}else{
				expense.setStatus("5");
				expense.setUpdateMan(userModel.getUsername());
				expense.setUpdateDate(new Date());
				expense.setUpdatemanid(userModel.getUserid());
				approvalService.makeRecored(expense.getId(), "财务人员审批",userModel.getUsername(), approveContent, "财务审核不通过");
			}
			expense.setNote(expenseAccount.getNote());
			expense.setReason(expenseAccount.getReason());
			expense.setApproveSum(expenseAccount.getApproveSum());
			expenseInfoService.updateExpenseInfo(expense);
			
			//检测项目金额是否有新增数据，如果有则保存
//			OAExpenseAccount account = expenseInfoService.getExpenseInfoById(expenseId);
			String savePrjdata = request.getParameter("savePrjdata");
			String saveSumdata = request.getParameter("saveSumdata");
			savePrjdata=savePrjdata==null?"":savePrjdata;
			saveSumdata=saveSumdata==null?"":saveSumdata;
			if(!"".equals(savePrjdata)){
				List<OAExpenseAccountProject> prjsumList = new ArrayList<OAExpenseAccountProject>();
				for(int i=0;i<savePrjdata.split(",").length;i++){
					OAExpenseAccountProject prjsum = new OAExpenseAccountProject();
					prjsum.setPrjname(savePrjdata.split(",")[i]);
					prjsum.setSum(Double.parseDouble(saveSumdata.split(",")[i]));
					prjsum.setExpenseId(expenseId);
					prjsumList.add(prjsum);
				}
				expenseInfoService.saveList(prjsumList);
				for(OAExpenseAccountProject project:prjsumList){
					//if(UserRoleConfig.ifHr(userModel)){
//						String record = "项目名称:"+project.getPrjname() +",项目金额:"+project.getSum();
						modifyRecordService.saveRecord(expenseId, userModel.getUsername(), "新增了:"+project.getPrjname()+"的记录", new Date(), " ");
					//}
				}
			}
			//坚持项目金额是否有修改
			String uppPrjdata = request.getParameter("noUpdatePrjname");
			String uppSumdata = request.getParameter("noUpdatePrjsum");
			String ids = request.getParameter("noUpdateId");
			uppPrjdata=uppPrjdata==null?"":uppPrjdata;
			uppSumdata=uppSumdata==null?"":uppSumdata;
			ids=ids==null?"":ids;
			if(!"".equals(uppPrjdata)){
				for(int i=0;i<uppPrjdata.split(",").length;i++){
					OAExpenseAccountProject prjsum = expenseInfoService.getProjectSumDetail(ids.split(",")[i]);
					//if(UserRoleConfig.ifHr(userModel)){
						if(!(prjsum.getPrjname().equals(uppPrjdata.split(",")[i]) && prjsum.getSum().equals(Double.parseDouble(uppSumdata.split(",")[i])))){
							String record = "项目名称:"+prjsum.getPrjname() +",项目金额:"+prjsum.getSum();
							modifyRecordService.saveRecord(expenseId, userModel.getUsername(), "修改了:"+uppPrjdata.split(",")[i]+"的记录", new Date(), record);
						}
					//}
					prjsum.setPrjname(uppPrjdata.split(",")[i]);
					prjsum.setSum(Double.parseDouble(uppSumdata.split(",")[i]));
					expenseInfoService.updateExpenseProjectSumInfo(prjsum);
				}
			}
			
			//更新报销项目名称
			List<OAExpenseAccountProject> projectList = expenseInfoService.getExpenseProjectSumByExpenseId(expense.getId());
			String expense_prjname="";
			for(OAExpenseAccountProject project:projectList){
				String prjname = project.getPrjname();
				if(expense_prjname.indexOf(prjname)==-1){
					expense_prjname+=","+prjname;
				}
			}
			if(!"".equals(expense_prjname)){
				expense_prjname=expense_prjname.substring(1, expense_prjname.length());
			}
			expense.setExpenseprjname(expense_prjname);
			expenseInfoService.updateExpenseInfo(expense);
			
			MsgBox msgBox = new MsgBox(request, "审批成功！");
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			MsgBox msgBox1 = new MsgBox(request, getText(
					"operInfoform.addfaile", new String[] { "审批失败！ " }));
			msgBox1.setButtonType(MsgBox.BUTTON_CLOSE);
			this.addActionMessage(getText("operInfoform.addfaile"));
			boolean hasActionMessage = this.hasActionMessages();
			request.setAttribute("hasActionMessage", hasActionMessage);
			return "msgBox";
		}
		return "msgBox";
	}

	/**
	 * 跳转到审核页面
	 * 
	 * @return
	 * @throws Exception
	 */
	public String toAuditPage() throws Exception {
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		try {
			String expenseId = request.getParameter("expenseId");
			expenseAccount = expenseInfoService.getExpenseInfoByexpenseNum(expenseId);
			String record = "";
			if (expenseAccount.getStatus().equals("3")) {
				MsgBox msgBox = new MsgBox(request,
						getText("该报销单已经处理完毕,不能审核"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			} else {
				// 判断是否为人事行政人员
				if (UserRoleConfig.ifHr(userModel)) {
					if (expenseAccount.getStatus().equals("1")) {
						//出差明细
						expenselist = expenseInfoService.getExpenseDetailByExpenseId(expenseAccount.getId());
						request.getSession().setAttribute("expenselist", expenselist);
						//合计
						OAExpenseAccountCheckCondition costSum = expenseInfoService.getCostSum(expenseAccount.getId());
						request.getSession().setAttribute("costSum", costSum);
						//费用明细
						travelDetailList = expenseInfoService.getTravelDetailByExpenseId(expenseAccount.getId());
						request.getSession().setAttribute("travelDetailList", travelDetailList);
						//合计
						OAExpenseAccountTravelDetail travelSum = expenseInfoService.getTravelSum(expenseAccount.getId());
						request.getSession().setAttribute("travelSum", travelSum);
						//项目金额
						expensePrjList = expenseInfoService.getExpenseProjectSumByExpenseId(expenseAccount.getId());
						request.getSession().setAttribute("savePrjSumList", expensePrjList);
						request.getSession().setAttribute("prjSumSize", expensePrjList.size());
						//明细金额中的项目名称
						List<Project> projects = projectService.listAllGroup();
						request.getSession().setAttribute("projectSelect", projects);
						//审核人员
						List<SysUser> approveList = UserRoleConfig.getHrOaFinaByUser(userModel);
						request.getSession().setAttribute("approvePerson", approveList);
						List<ApprovalRecord> records = approvalService.getRecordByName(expenseAccount.getId());
						for (int i =0;i<records.size();i++) {
							record +="["+records.get(i).getResult()+"] "+records.get(i).getApprovalUser()+
									"("+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(records.get(i).getApprovalTime())+
									"):"+records.get(i).getOpinion()+"<br/>";
						}
						request.setAttribute("expenseAccount", expenseAccount);
						request.setAttribute("record", record.trim());
						List<ModifyRecord> modifyRecords = modifyRecordService.findRecordByModifyId(expenseAccount.getId());
						String modifyRecord = "";
						if(modifyRecords!=null && modifyRecords.size()>0){
							for(ModifyRecord r:modifyRecords ){
								modifyRecord +=r.getUsername()+"("+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(r.getModifyDate())+")"+r.getOperate()+",操作前的数据为："+r.getRecorde();
								modifyRecord += "<br/>";
							}
						}
						request.setAttribute("modifyRecord", modifyRecord);
						return "audit";
					} else {
						MsgBox msgBox = new MsgBox(request,
								getText("你不能审核当前状态下的报销单！"));
						msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
						return "msgBox";
					}
				}else if(UserDataRoleConfig.viewAllDataByUserid(userModel.getUserid(), "expense.userdata.isFinancial")){
					if (expenseAccount.getStatus().equals("2")) {
						//出差明细
						expenselist = expenseInfoService.getExpenseDetailByExpenseId(expenseAccount.getId());
						request.getSession().setAttribute("expenselist", expenselist);
						//合计
						OAExpenseAccountCheckCondition costSum = expenseInfoService.getCostSum(expenseAccount.getId());
						request.getSession().setAttribute("costSum", costSum);
						//费用明细
						travelDetailList = expenseInfoService.getTravelDetailByExpenseId(expenseAccount.getId());
						request.getSession().setAttribute("travelDetailList", travelDetailList);
						//合计
						OAExpenseAccountTravelDetail travelSum = expenseInfoService.getTravelSum(expenseAccount.getId());
						request.getSession().setAttribute("travelSum", travelSum);
						//项目金额
						expensePrjList = expenseInfoService.getExpenseProjectSumByExpenseId(expenseAccount.getId());
						request.getSession().setAttribute("savePrjSumList", expensePrjList);
						request.getSession().setAttribute("prjSumSize", expensePrjList.size());
						//明细金额中的项目名称
						List<Project> projects = projectService.listAllGroup();
						request.getSession().setAttribute("projectSelect", projects);
						//获取审核记录
						List<ApprovalRecord> records = approvalService.getRecordByName(expenseAccount.getId());
						for (int i =0;i<records.size();i++) {
							record +="["+records.get(i).getResult()+"] "+records.get(i).getApprovalUser()+
									"("+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(records.get(i).getApprovalTime())+
									"):"+records.get(i).getOpinion()+"<br/>";
						}
						request.setAttribute("expenseAccount", expenseAccount);
						request.setAttribute("record", record.trim());
						//获取修改记录
						List<ModifyRecord> modifyRecords = modifyRecordService.findRecordByModifyId(expenseAccount.getId());
						String modifyRecord = "";
						if(modifyRecords!=null && modifyRecords.size()>0){
							for(ModifyRecord r:modifyRecords ){
								modifyRecord +=r.getUsername()+"("+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(r.getModifyDate())+")"+r.getOperate()+",操作前的数据为："+r.getRecorde();
								modifyRecord += "<br/>";
							}
						}
						request.setAttribute("modifyRecord", modifyRecord);
						return "finAudit";
					} else {
						MsgBox msgBox = new MsgBox(request,
								getText("你不能审核当前状态下的报销单！"));
						msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
						return "msgBox";
					}
				}else {
					MsgBox msgBox = new MsgBox(request, getText("你没有权限修改此报销单！"));
					msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
					return "msgBox";
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "edit";
	}

	/**
	 * 跳转到修改页面
	 * 
	 * @return
	 * @throws Exception
	 */
	public String toUpdatePage() throws Exception {
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		try {
			String expenseId = request.getParameter("expenseId");
			expenseAccount = expenseInfoService.getExpenseInfoByexpenseNum(expenseId);
			request.setAttribute("expenseAccount", expenseAccount);
			// 判断是否为报销单创建者本人
			if (expenseAccount.getUserId().equals(userModel.getUserid())) {
				if("3".equals(expenseAccount.getStatus())){
					MsgBox msgBox = new MsgBox(request,
							getText("报销单已处理完毕,不能修改！"));
					msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
					return "msgBox";
				}
				if ("1".equals(expenseAccount.getStatus()) || "2".equals(expenseAccount.getStatus())) {
					MsgBox msgBox = new MsgBox(request,
							getText("报销单正在审批中,不能修改！"));
					msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
					return "msgBox";
				}else{
					//出差明细
					expenselist = expenseInfoService.getExpenseDetailByExpenseId(expenseAccount.getId());
					request.getSession().setAttribute("expenselist", expenselist);
					//合计
					OAExpenseAccountCheckCondition costSum = expenseInfoService.getCostSum(expenseAccount.getId());
					request.getSession().setAttribute("costSum", costSum);
					//费用明细
					travelDetailList = expenseInfoService.getTravelDetailByExpenseId(expenseAccount.getId());
					request.getSession().setAttribute("travelDetailList", travelDetailList);
					//合计
					OAExpenseAccountTravelDetail travelSum = expenseInfoService.getTravelSum(expenseAccount.getId());
					request.getSession().setAttribute("travelSum", travelSum);
					request.setAttribute("expenseId", expenseId);
					//项目金额
					expensePrjList = expenseInfoService.getExpenseProjectSumByExpenseId(expenseAccount.getId());
					request.getSession().setAttribute("savePrjSumList", expensePrjList);
					request.getSession().setAttribute("prjSumSize", expensePrjList.size());
					//明细金额中的项目名称
					List<Project> projects = projectService.listAllGroup();
					request.getSession().setAttribute("projectSelect", projects);
					//审核人员
					List<SysUser> approveList = UserRoleConfig.getHrOaFinaByUser(userModel);
					request.getSession().setAttribute("approvePerson", approveList);
					
					String record = "";
					//审核记录
					List<ApprovalRecord> records = approvalService.getRecordByName(expenseAccount.getId());
					for (int i =0;i<records.size();i++) {
						record +="["+records.get(i).getResult()+"] "+records.get(i).getApprovalUser()+
								"("+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(records.get(i).getApprovalTime())+
								"):"+records.get(i).getOpinion()+"<br/>";
					}
					request.setAttribute("record", record.trim());
					List<ModifyRecord> modifyRecords = modifyRecordService.findRecordByModifyId(expenseAccount.getId());
					String modifyRecord = "";
					if(modifyRecords!=null && modifyRecords.size()>0){
						for(ModifyRecord r:modifyRecords ){
							modifyRecord +=r.getUsername()+"("+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(r.getModifyDate())+")"+r.getOperate()+",操作前的数据为："+r.getRecorde();
							modifyRecord += "<br/>";
						}
					}
					request.setAttribute("modifyRecord", modifyRecord);
					//是否为行政人员或财务人员
					if(UserDataRoleConfig.viewAllDataByUserid(userModel.getUserid(), "expense.userdata.isHr")){
						request.setAttribute("status", "1");
					}else if(UserDataRoleConfig.viewAllDataByUserid(userModel.getUserid(), "expense.userdata.isFinancial")){
						request.setAttribute("status", "2");
					}else{
						request.setAttribute("status", "0");
					}
					return "edit";
				}
			} else {
				MsgBox msgBox = new MsgBox(request,
						getText("只能修改自己申请的报销单"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}

		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "edit";
	}

	/**
	 * 删除信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String delete() throws Exception {
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		try {
			String[] delItem = StringUtils.split(request.getParameter("ids"),
					",");
			int iCount = 0;
			for (int i = 0; i < delItem.length; i++) {
				OAExpenseAccount oa = new OAExpenseAccount();
				oa = expenseInfoService.getExpenseInfoByexpenseNum(delItem[i]);
				if (!oa.getUserName().equals(userModel.getUsername())) {
					MsgBox msgBox = new MsgBox(request, getText(
							"operInfoform.updatefaile",
							new String[] { "你没有权限删除此报销单！" }));
					msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
					return "msgBox";
				}
				if (!oa.getStatus().equals("0") &&  !oa.getStatus().equals("4") && !oa.getStatus().equals("5")) {
					MsgBox msgBox = new MsgBox(request, getText(
							"operInfoform.updatefaile",
							new String[] { "你不能删除当前状态下的报销单！" }));
					msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
					return "msgBox";
				}
				if (oa != null && !"".equals(oa.getId())) {
					expenseInfoService.deleteExpenseInfo(oa.getId());
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

	/**
	 * 提交报销单
	 * 
	 * @return
	 * @throws Exception
	 */
	public String submit() throws Exception {
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		try {
			String expenseId = request.getParameter("expenseId");
			expenseAccount = expenseInfoService
					.getExpenseInfoByexpenseNum(expenseId);
			if (!expenseAccount.getUserId().equals(userModel.getUserid())) {
				MsgBox msgBox = new MsgBox(request,"你没有权限提交此报销单！");
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}
			if (expenseAccount.getStatus().equals("1") || expenseAccount.getStatus().equals("2")) {
				MsgBox msgBox = new MsgBox(request, "该报销单已经送审!");
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}
			if("3".equals(expenseAccount.getStatus())){
				MsgBox msgBox = new MsgBox(request, "该报销单已经处理完毕,不能提交审核！");
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}
//			if (expenseAccount.getStatus().equals("4") || expenseAccount.getStatus().equals("5")) {
//				if (expenseAccount.getFeedBack() != null && !expenseAccount.getFeedBack().equals("")) {
//					approvalService.makeRecored(expenseAccount.getExpenseNum(), "提交报销审批", userModel.getUsername(), expenseAccount.getFeedBack(), "提交审核");
//				} else {
//					approvalService.makeRecored(expenseAccount.getExpenseNum(), "提交报销审批", userModel.getUsername(), "无", "提交审核");
//				}
//			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			expenseAccount.setSubmitDate(sdf.parse(sdf.format(new Date())));
			//是否为行政人员或财务人员
			if(UserDataRoleConfig.viewAllDataByUserid(userModel.getUserid(), "expense.userdata.isHr")){
				expenseAccount.setStatus("2");
				expenseAccount.setUpdateMan(userModel.getUsername());
				expenseAccount.setUpdateDate(new Date());
				//String username=staffInfoService.getUsernameById(expenseAccount.getUpdatemanid());
				//expenseAccount.setUpdateMan(username);
			}else{
				expenseAccount.setStatus("1");
				expenseAccount.setUpdateDate(new Date());
				expenseAccount.setUpdateMan(userModel.getUsername());
				//String username=staffInfoService.getUsernameById(expenseAccount.getUpdatemanid());
				//expenseAccount.setUpdateMan(username);
			}
			expenseInfoService.updateExpenseInfo(expenseAccount);
			MsgBox msgBox = new MsgBox(request, "提交审核成功！");
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			this.addActionMessage("提交审核成功！");
		}catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);

			MsgBox msgBox = new MsgBox(request,
					getText("提交审核失败"));
			addActionMessage(getText("提交审核失败"));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		}
		return "msgBox";
	}

	/**
	 * 修改报销单
	 * 
	 * @return
	 * @throws Exception
	 */
	public String update() throws Exception {
		try {
			UserModel userModel = (UserModel) request.getSession()
					.getAttribute(Constants.LOGIN_USER_KEY);
			String expenseId = request.getParameter("expenseId");
			OAExpenseAccount expense = expenseInfoService
					.getExpenseInfoById(expenseId);
			expense.setUpdateDate(Calendar.getInstance().getTime());
			expense.setUpdateMan(userModel.getUsername());
			expense.setSubmitDate(expense.getSubmitDate());
			expense.setNote(expenseAccount.getNote());
			expense.setStatus("0");
			expense.setUpdatemanid(expenseAccount.getUpdatemanid());
			//expense.setExpenseNum(expenseAccount.getExpenseNum());
			expense.setReason(expenseAccount.getReason());
			if(expenseAccount.getFeedBack()!=null&&!expenseAccount.getFeedBack().trim().equals("")){
				expense.setFeedBack(expenseAccount.getFeedBack());
			}else{
				expense.setFeedBack("无");
			}
			boolean flag = expenseInfoService.updateExpenseInfo(expense);
			
			//检测项目金额是否有新增数据，如果有则保存
			String savePrjdata = request.getParameter("noSavePrjname");
			String saveSumdata = request.getParameter("noSavePrjsum");
			savePrjdata=savePrjdata==null?"":savePrjdata;
			saveSumdata=saveSumdata==null?"":saveSumdata;
			if(!"".equals(savePrjdata)){
				List<OAExpenseAccountProject> prjsumList = new ArrayList<OAExpenseAccountProject>();
				for(int i=0;i<savePrjdata.split(",").length;i++){
					OAExpenseAccountProject prjsum = new OAExpenseAccountProject();
					prjsum.setPrjname(savePrjdata.split(",")[i]);
					prjsum.setSum(Double.parseDouble(saveSumdata.split(",")[i]));
					prjsum.setExpenseId(expenseId);
					prjsumList.add(prjsum);
				}
				expenseInfoService.saveList(prjsumList);
//				OAExpenseAccount account = expenseInfoService.getExpenseInfoById(expenseId);
				for(OAExpenseAccountProject project:prjsumList){
					//if(UserRoleConfig.ifHr(userModel)){
//						String record = "项目名称:"+project.getPrjname() +",项目金额:"+project.getSum();
						modifyRecordService.saveRecord(expenseId, userModel.getUsername(), "新增了:"+project.getPrjname()+"的记录", new Date(), " ");
					//}
				}
			}
			//坚持项目金额是否有修改
			String uppPrjdata = request.getParameter("noUpdatePrjname");
			String uppSumdata = request.getParameter("noUpdatePrjsum");
			String ids = request.getParameter("noUpdateId");
			uppPrjdata=uppPrjdata==null?"":uppPrjdata;
			uppSumdata=uppSumdata==null?"":uppSumdata;
			ids=ids==null?"":ids;
			if(!"".equals(uppPrjdata)){
				for(int i=0;i<uppPrjdata.split(",").length;i++){
					OAExpenseAccountProject prjsum = expenseInfoService.getProjectSumDetail(ids.split(",")[i]);
					//if(UserRoleConfig.ifHr(userModel)){
						if(!(prjsum.getPrjname().equals(uppPrjdata.split(",")[i]) && prjsum.getSum().equals(Double.parseDouble(uppSumdata.split(",")[i])))){
							String record = "项目名称:"+prjsum.getPrjname() +",项目金额:"+prjsum.getSum();
							modifyRecordService.saveRecord(expenseId, userModel.getUsername(), "修改了:"+uppPrjdata.split(",")[i]+"的记录", new Date(), record);
						}
					//}
					prjsum.setPrjname(uppPrjdata.split(",")[i]);
					prjsum.setSum(Double.parseDouble(uppSumdata.split(",")[i]));
					expenseInfoService.updateExpenseProjectSumInfo(prjsum);
				}
			}
			
			
			//更新报销项目名称
			List<OAExpenseAccountProject> projectList = expenseInfoService.getExpenseProjectSumByExpenseId(expense.getId());
			String expense_prjname="";
			for(OAExpenseAccountProject project:projectList){
				String prjname = project.getPrjname();
				if(expense_prjname.indexOf(prjname)==-1){
					expense_prjname+=","+prjname;
				}
			}
			if(!"".equals(expense_prjname)){
				expense_prjname=expense_prjname.substring(1, expense_prjname.length());
			}
			expense.setExpenseprjname(expense_prjname);
			expenseInfoService.updateExpenseInfo(expense);
			
			if (flag == true) {
				//expense.setStatus("新增");
				MsgBox msgBox = new MsgBox(request,
						getText("operInfoform.updateok"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request,
					getText("operInfoform.updatefaile"),
					new String[] { e.toString() });
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		}

		return "msgBox";
	}

	/**
	 * 更新页面的导入Excel表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String importData() throws Exception {
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		String expenseId = request.getParameter("expenseId");
		expenseAccount = expenseInfoService
				.getExpenseInfoByexpenseNum(expenseId);
		try {
			if (file == null) {
				throw new Exception("文件为空");
			}
			String json = "";
			String jsonStr = "";
			// 获取文件流，并创建对应的excel对象
			POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(file));
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			int sheetNumber = wb.getNumberOfSheets();
			/** ** ** *循环每一个sheet，添加数据* ** ** **/
			for (int shNum = 0; shNum < sheetNumber; shNum++) {
				HSSFSheet sheet = wb.getSheetAt(shNum);
				HSSFRow headRow = sheet.getRow(0);

				if (headRow == null) {// 判断sheet中第一行是否有数据，没有数据则不进行下一步
					continue;
				}
				if (headRow.getCell((short) 0) != null) {
					headRow.getCell((short) 0).setCellType(
							HSSFCell.CELL_TYPE_STRING);
				}

				/** ** ** *获取表格标题，如标题不对，则格式不对* ** ** **/
				boolean flag = false;// 是否有合计
				String titel = headRow.getCell((short) 0).getStringCellValue() == null ? ""
						: headRow.getCell((short) 0).getStringCellValue();
				if (titel.trim().equals("软件体系出差费用报销明细表")) {
					OAExpenseAccountCheckCondition checks = new OAExpenseAccountCheckCondition();
					int rows = 0;
					long rowNumber = sheet.getPhysicalNumberOfRows(); // sheet中的行数
					Double fly = 0.0;
					Double taxi = 0.0;
					Double bus = 0.0;
					Double living = 0.0;
					Double contact = 0.0;
					Double business = 0.0;
					Double other = 0.0;
					Double buzhu = 0.0;
					Double sum = 0.0;
					String path = "";
					// HSSFRow cNameRow = sheet.getRow(5);
					// //获取列标题行，并判断对应列标题是否正确，防止数据导入出错
					// listt = new ArrayList<Object>();
					/** ** ** *循环excel表格中的数据，并判断格式，存入或修改数据库相应值* ** ** **/
					for (int rowNum = 5; rowNum < rowNumber; rowNum++) {
						OAExpenseAccountCheckCondition oaexpenseCondition = new OAExpenseAccountCheckCondition();
						HSSFRow row = sheet.getRow(rowNum);
						SimpleDateFormat sdf = new SimpleDateFormat(
								"EEE MMM dd HH:mm:ss z yyyy", Locale.US);
						TimeZone tz = TimeZone.getTimeZone("GMT+8");
						sdf.setTimeZone(tz);
						String dateTime = "";
						if (row.getCell((short) 0).getCellType() == HSSFCell.CELL_TYPE_STRING) {
							dateTime = row.getCell((short) 0)
									.getStringCellValue();
						} else {
							dateTime = row.getCell((short) 0)
									.getDateCellValue() == null ? "" : String
									.valueOf(row.getCell((short) 0)
											.getDateCellValue());
							if (!dateTime.equals("")) {
								Date s = sdf.parse(dateTime);
								sdf = new SimpleDateFormat("yyyy/MM/dd");
								dateTime = sdf.format(s);
							}
						}
						if (dateTime != null && dateTime.equals("部门经理审核：")) {
							break;
						}

						oaexpenseCondition.setDateTime(dateTime);
						String didian = "";
						if (row.getCell((short) 1) != null)
							row.getCell((short) 1).setCellType(
									HSSFCell.CELL_TYPE_STRING);
						didian = row.getCell((short) 1).getStringCellValue() == null ? ""
								: row.getCell((short) 1).getStringCellValue();
						if (didian.equals("") && !dateTime.equals("合计")) {
							continue;
						}
						oaexpenseCondition.setPlace(didian);
						String renwu = "";
						if (row.getCell((short) 2) != null)
							row.getCell((short) 2).setCellType(
									HSSFCell.CELL_TYPE_STRING);
						renwu = row.getCell((short) 2).getStringCellValue() == null ? ""
								: row.getCell((short) 2).getStringCellValue();
						oaexpenseCondition.setTask(renwu);
						String feiji = "0.0";
						if (row.getCell((short) 3) != null) {
							if (row.getCell((short) 3).getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
								feiji = row.getCell((short) 3)
										.getNumericCellValue() == 0.0 ? ""
										: String.valueOf(row.getCell((short) 3)
												.getNumericCellValue());
							else if (row.getCell((short) 3).getCellType() == HSSFCell.CELL_TYPE_STRING)
								feiji = row.getCell((short) 3)
										.getStringCellValue() == null ? ""
										: row.getCell((short) 3)
												.getStringCellValue();
						}
						oaexpenseCondition.setFly(feiji);
						String chuzuche = "0.0";
						if (row.getCell((short) 4) != null) {
							if (row.getCell((short) 4).getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
								chuzuche = row.getCell((short) 4)
										.getNumericCellValue() == 0.0 ? ""
										: String.valueOf(row.getCell((short) 4)
												.getNumericCellValue());
							else if (row.getCell((short) 4).getCellType() == HSSFCell.CELL_TYPE_STRING)
								chuzuche = row.getCell((short) 4)
										.getStringCellValue() == null ? ""
										: row.getCell((short) 4)
												.getStringCellValue();
						}
						oaexpenseCondition.setTaxi(chuzuche);
						String gongjiao = "0.0";
						if (row.getCell((short) 5) != null) {
							if (row.getCell((short) 5).getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
								gongjiao = row.getCell((short) 5)
										.getNumericCellValue() == 0.0 ? ""
										: String.valueOf(row.getCell((short) 5)
												.getNumericCellValue());
							else if (row.getCell((short) 5).getCellType() == HSSFCell.CELL_TYPE_STRING)
								gongjiao = row.getCell((short) 5)
										.getStringCellValue() == null ? ""
										: row.getCell((short) 5)
												.getStringCellValue();
						}
						oaexpenseCondition.setBus(gongjiao);
						String zhusu = "0.0";
						if (row.getCell((short) 6).getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
							zhusu = row.getCell((short) 6)
									.getNumericCellValue() == 0.0 ? "" : String
									.valueOf(row.getCell((short) 6)
											.getNumericCellValue());
						else if (row.getCell((short) 6).getCellType() == HSSFCell.CELL_TYPE_STRING)
							zhusu = row.getCell((short) 6).getStringCellValue() == null ? ""
									: row.getCell((short) 6)
											.getStringCellValue();
						oaexpenseCondition.setLiving(zhusu);
						String tongxun = "0.0";
						if (row.getCell((short) 7) != null) {
							if (row.getCell((short) 7).getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
								tongxun = row.getCell((short) 7)
										.getNumericCellValue() == 0.0 ? ""
										: String.valueOf(row.getCell((short) 7)
												.getNumericCellValue());
							else if (row.getCell((short) 7).getCellType() == HSSFCell.CELL_TYPE_STRING)
								tongxun = row.getCell((short) 7)
										.getStringCellValue() == null ? ""
										: row.getCell((short) 7)
												.getStringCellValue();
						}
						oaexpenseCondition.setContact(tongxun);
						String yw = "0.0";
						if (row.getCell((short) 8) != null) {
							if (row.getCell((short) 8).getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
								yw = row.getCell((short) 8)
										.getNumericCellValue() == 0.0 ? ""
										: String.valueOf(row.getCell((short) 8)
												.getNumericCellValue());
							else if (row.getCell((short) 8).getCellType() == HSSFCell.CELL_TYPE_STRING)
								yw = row.getCell((short) 8)
										.getStringCellValue() == null ? ""
										: row.getCell((short) 8)
												.getStringCellValue();
						}
						oaexpenseCondition.setBusiness(yw);
						String qita = "0.0";
						if (row.getCell((short) 9) != null) {
							if (row.getCell((short) 9).getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
								qita = row.getCell((short) 9)
										.getNumericCellValue() == 0.0 ? ""
										: String.valueOf(row.getCell((short) 9)
												.getNumericCellValue());
							else if (row.getCell((short) 9).getCellType() == HSSFCell.CELL_TYPE_STRING)
								qita = row.getCell((short) 9)
										.getStringCellValue() == null ? ""
										: row.getCell((short) 9)
												.getStringCellValue();
						}
						oaexpenseCondition.setOther(qita);
						String bz = "0.0";
						if (row.getCell((short) 10) != null) {
							if (row.getCell((short) 10).getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
								bz = row.getCell((short) 10)
										.getNumericCellValue() == 0.0 ? ""
										: String.valueOf(row
												.getCell((short) 10)
												.getNumericCellValue());
							else if (row.getCell((short) 10).getCellType() == HSSFCell.CELL_TYPE_STRING)
								bz = row.getCell((short) 10)
										.getStringCellValue() == null ? ""
										: row.getCell((short) 10)
												.getStringCellValue();
						}
						oaexpenseCondition.setBuzhu(bz);
						String xiaoji = "0.0";
						Double total = 0.0;
						total = Double.valueOf(bz) + Double.valueOf(qita)
								+ Double.valueOf(yw) + Double.valueOf(tongxun)
								+ Double.valueOf(zhusu)
								+ Double.valueOf(gongjiao)
								+ Double.valueOf(chuzuche)
								+ Double.valueOf(feiji);
						xiaoji = String.valueOf(total);
						oaexpenseCondition.setAccount(xiaoji);
						String luxian = "";
						luxian = row.getCell((short) 12).getStringCellValue() == null ? ""
								: row.getCell((short) 12).getStringCellValue();
						if (luxian.equals("")) {
							oaexpenseCondition.setPath(path);
						} else {
							path = luxian;
							oaexpenseCondition.setPath(path);
						}
						/* oaexpenseCondition.setPath(luxian); */
						fly = fly + Double.valueOf(feiji);
						taxi = taxi + Double.valueOf(chuzuche);
						bus = bus + Double.valueOf(gongjiao);
						living = living + Double.valueOf(zhusu);
						contact = contact + Double.valueOf(tongxun);
						business = business + Double.valueOf(yw);
						other = other + Double.valueOf(qita);
						buzhu = buzhu + Double.valueOf(bz);
						sum = sum + Double.valueOf(xiaoji);
						if (dateTime != null && dateTime.equals("合计")) {
							flag = true;
							BigDecimal bg = new BigDecimal(taxi);
							taxi = bg.setScale(1, BigDecimal.ROUND_HALF_UP)
									.doubleValue();
							oaexpenseCondition.setFly(String.valueOf(fly));
							oaexpenseCondition.setTaxi(String.valueOf(taxi));
							oaexpenseCondition.setBus(String.valueOf(bus));
							oaexpenseCondition
									.setLiving(String.valueOf(living));
							oaexpenseCondition.setContact(String
									.valueOf(contact));
							oaexpenseCondition.setBusiness(String
									.valueOf(business));
							oaexpenseCondition.setOther(String.valueOf(other));
							oaexpenseCondition.setBuzhu(String.valueOf(buzhu));
							oaexpenseCondition.setAccount(String.valueOf(sum));
							oaexpenseCondition.setPath("");
						}
						oaexpenseCondition.setNumber(rowNum);
						expenseInfoService
								.addOAExpenseCheckCondition(oaexpenseCondition);
						expenseList.add(oaexpenseCondition);

					}
					if (!flag) {
						BigDecimal bg = new BigDecimal(taxi);
						taxi = bg.setScale(1, BigDecimal.ROUND_HALF_UP)
								.doubleValue();
						checks.setFly(String.valueOf(fly));
						//checks.setUuid(uuid);
						checks.setDateTime("合计");
						checks.setTaxi(String.valueOf(taxi));
						checks.setBus(String.valueOf(bus));
						checks.setLiving(String.valueOf(living));
						checks.setContact(String.valueOf(contact));
						checks.setBusiness(String.valueOf(business));
						checks.setOther(String.valueOf(other));
						checks.setBuzhu(String.valueOf(buzhu));
						checks.setAccount(String.valueOf(sum));
						checks.setPath("");
						checks.setNumber(rows);
						expenseInfoService.addOAExpenseCheckCondition(checks);
						expenseList.add(checks);
					}
					jsonStr = JSONArray.fromObject(expenseList).toString();
					expenseAccount.setCostDetail(jsonStr);
				} else if (titel.trim().equals("软件体系费用说明表")) {
					long rownumber = sheet.getPhysicalNumberOfRows();
					Double yewusum = 0.0;
					Double officesum = 0.0;
					Double qitasum = 0.0;
					int no = 0;
					flag = false;
					int rows = 0;
					// HSSFRow cnameRow = sheet.getRow(5);
					// li = new ArrayList<Object>();
					OAExpenseAccountTravelDetail travel = new OAExpenseAccountTravelDetail();
					for (int rownum = 5; rownum < rownumber; rownum++) {
						SimpleDateFormat sdf = new SimpleDateFormat(
								"EEE MMM dd HH:mm:ss z yyyy", Locale.US);
						TimeZone tz = TimeZone.getTimeZone("GMT+8");
						sdf.setTimeZone(tz);
						OAExpenseAccountTravelDetail travelDetail = new OAExpenseAccountTravelDetail();
						if (no + 1 == rownum) {
							break;
						}
						HSSFRow row = sheet.getRow(rownum);
						String dtime = "";
						if (row.getCell((short) 0).getCellType() == HSSFCell.CELL_TYPE_STRING) {
							dtime = row.getCell((short) 0).getStringCellValue() == null ? ""
									: row.getCell((short) 0)
											.getStringCellValue();
						} else {
							dtime = row.getCell((short) 0).getDateCellValue() == null ? ""
									: String.valueOf(row.getCell((short) 0)
											.getDateCellValue());
							if (!dtime.equals("")) {
								Date s = sdf.parse(dtime);
								sdf = new SimpleDateFormat("yyyy/MM/dd");
								dtime = sdf.format(s);
							}
						}
						if (dtime != null && dtime.equals("总计")) {
							no = rownum;
						}
						if (dtime.equals("")) {
							break;
						}
						travelDetail.setDdTime(dtime);
						String address = "";
						address = row.getCell((short) 1).getStringCellValue() == null ? ""
								: row.getCell((short) 1).getStringCellValue();
						if (address.equals("") && "".equals(dtime)) {
							continue;
						}
						travelDetail.setDidian(address);
						String feiyong = "0.0";
						if (row.getCell((short) 2).getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
							feiyong = row.getCell((short) 2)
									.getNumericCellValue() == 0.0 ? "" : String
									.valueOf(row.getCell((short) 2)
											.getNumericCellValue());
						else if (row.getCell((short) 2).getCellType() == HSSFCell.CELL_TYPE_STRING)
							feiyong = row.getCell((short) 2)
									.getStringCellValue() == null ? "" : row
									.getCell((short) 2).getStringCellValue();
						travelDetail.setYewu(feiyong);
						String jiaotong = "0.0";
						if (row.getCell((short) 3).getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
							jiaotong = row.getCell((short) 3)
									.getNumericCellValue() == 0.0 ? "" : String
									.valueOf(row.getCell((short) 3)
											.getNumericCellValue());
						else if (row.getCell((short) 3).getCellType() == HSSFCell.CELL_TYPE_STRING)
							jiaotong = row.getCell((short) 3)
									.getStringCellValue() == null ? "" : row
									.getCell((short) 3).getStringCellValue();
						travelDetail.setTraffic(jiaotong);
						String another = "0.0";
						if (row.getCell((short) 4).getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
							another = row.getCell((short) 4)
									.getNumericCellValue() == 0.0 ? "" : String
									.valueOf(row.getCell((short) 4)
											.getNumericCellValue());
						else if (row.getCell((short) 4).getCellType() == HSSFCell.CELL_TYPE_STRING)
							another = row.getCell((short) 4)
									.getStringCellValue() == null ? "" : row
									.getCell((short) 4).getStringCellValue();
						travelDetail.setAnother(another);
						String notice = "";
						if (row.getCell((short) 5).getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
							notice = row.getCell((short) 5)
									.getNumericCellValue() == 0.0 ? "" : String
									.valueOf(row.getCell((short) 5)
											.getNumericCellValue());
						else if (row.getCell((short) 5).getCellType() == HSSFCell.CELL_TYPE_STRING)
							notice = row.getCell((short) 5).getStringCellValue() == null ? ""
									: row.getCell((short) 5).getStringCellValue();
						travelDetail.setNote(notice);
						yewusum += Double.valueOf(feiyong);
						officesum += Double.valueOf(jiaotong);
						qitasum += Double.valueOf(another);
						if (travelDetail.getDdTime() != null
								&& "合计".equals(dtime)) {
							Double total = 0.0;
							// total = yewu + office + qita;
							travelDetail.setYewu(String.valueOf(yewusum));
							travelDetail.setTraffic(String.valueOf(officesum));
							travelDetail.setAnother(String.valueOf(qitasum));
							travelDetail.setNote(String.valueOf(total));
						}
						expenseInfoService
								.addOAExpenseTravelDetail(travelDetail);
						if (travelDetail.getDdTime() != null
								&& travelDetail.equals("合计"))
							break;
						travelDetail.setNumber(rownum);
						rows = rownum;
						travelDetailList.add(travelDetail);
					}
					if (!flag) {
						Double total = 0.0;
						total = yewusum + officesum + qitasum;
						travel.setDdTime("合计");
						travel.setDidian("");
						//travel.setUuid(uuid);
						travel.setYewu(String.valueOf(yewusum));
						travel.setTraffic(String.valueOf(officesum));
						travel.setAnother(String.valueOf(qitasum));
						travel.setNote("");
						travel.setNumber(rows);
						expenseInfoService.addOAExpenseTravelDetail(travel);
						travelDetailList.add(travel);
					}
					json = JSONArray.fromObject(travelDetailList).toString();
					expenseAccount.setTravelDetail(json);

				} else {
					MsgBox msgBox = new MsgBox(request, getText(
							"operInfoform.updatefaile",
							new String[] { "请导入正确的报销明细表，谢谢！" }));
					msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
					return "msgBox";
				}
			}
			if (json.equals("") && jsonStr.equals("")) {
				MsgBox msgBox = new MsgBox(request, getText(
						"operInfoform.updatefaile",
						new String[] { "请导入正确的报销明细表，谢谢！" }));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);

			MsgBox msgBox = new MsgBox(request, getText(
					"operInfoform.updatefaile",
					new String[] { "请导入正确的报销明细表，谢谢！" }));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}
		;
		expenseAccount.setUserId(userModel.getUserid());
		expenseInfoService.updateExpenseInfo(expenseAccount);
		MsgBox msgBox = new MsgBox(request, getText("operInfoform.updateok"));
		msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		return "msgBox";
	}

	// 修改页面显示报销明细表
	public String showCostDetail() {
		String expenseId = request.getParameter("expenseId");
		expenseAccount = expenseInfoService
				.getExpenseInfoByexpenseNum(expenseId);
		String jsonStr = expenseAccount.getCostDetail();
		expenselist = (List) JSONArray.toList(JSONArray.fromObject(jsonStr),
				OAExpenseAccountCheckCondition.class);
		request.setAttribute("expenselist", expenselist);
		String json = expenseAccount.getTravelDetail();
		travelDetailList = (List) JSONArray.toList(JSONArray.fromObject(json),
				OAExpenseAccountTravelDetail.class);
		request.setAttribute("travelDetailList", travelDetailList);
		ActionContext.getContext().getValueStack().pop(); // 清空页面堆栈中的顶层数据
		ActionContext.getContext().getValueStack().push(expenseAccount); // 将需修改的原数据放入顶层堆栈中，方便页面获取
		request.setAttribute("expenseId", expenseAccount.getId());
		return "showCostDetail";
	}

	// 添加页面显示报销明细表
	public String showAddDetail() {
		try {
			UserModel userModel = (UserModel) request.getSession().getAttribute(
					Constants.LOGIN_USER_KEY);
			Map<String, String> deptkeyValue = BusnDataDir
					.getMapKeyValue("staffManager.department");
			String DetName = deptkeyValue.get(userModel.getDeptName());
			prjName = projectService.getProjectNameByUserid(userModel.getUserid());
			request.setAttribute("userName", userModel.getUsername());
			request.setAttribute("DetName", DetName);
			request.setAttribute("prjName", prjName);
			String uuid = request.getParameter("uuid");
			/*expenselist = (List<OAExpenseAccountCheckCondition>) request.getSession().getAttribute("expenseList");
			travelDetailList = (List<OAExpenseAccountTravelDetail>) request.getSession().getAttribute("travelDetailList");
			OAExpenseAccountCheckCondition costSum = (OAExpenseAccountCheckCondition) request.getSession().getAttribute("costSum");
			OAExpenseAccountTravelDetail travelSum = (OAExpenseAccountTravelDetail) request.getSession().getAttribute("travelSum");*/
			/*request.setAttribute("expenselist", expenselist);
			request.setAttribute("travelDetailList", travelDetailList);*/
			request.setAttribute("uuid", uuid);
			List<SysUser> approveList = UserRoleConfig.getHrOaFinaByUser(userModel);
			request.setAttribute("approvePerson", approveList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "showAddDetail";
	}

	/**
	 * 刷新
	 */
	public void refresh() {
		try {
			list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		;
	}

	/**
	 * 获取列表
	 * @return
	 * @throws Exception
	 * lhyan3
	 * 2014年7月18日
	 */
	public String list() throws Exception {
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		try {
			String from = request.getParameter("from");
			int pageNum = 1;
			int pageSize = 20;
			String submitDate = null;
			String submitEndDate = null;
			if (request.getParameter("pageNum") != null
					&& request.getParameter("pageNum").length() > 0) {
				pageNum = Integer.parseInt(request.getParameter("pageNum"));
			}
			if (request.getParameter("pageSize") != null
					&& request.getParameter("pageSize").length() > 0) {
				pageSize = Integer.parseInt(request.getParameter("pageSize"));
			}
			if (request.getParameter("submitDate") != null
					&& request.getParameter("submitDate").length() > 0) {
				submitDate = request.getParameter("submitDate");
			}
			if (request.getParameter("submitEndDate") != null
					&& request.getParameter("submitEndDate").length() > 0) {
				submitEndDate = request.getParameter("submitEndDate");
			}
			//是否为行政人员或财务人员
			if(UserDataRoleConfig.viewAllDataByUserid(userModel.getUserid(), "expense.userdata.isHr")){
				request.setAttribute("status", "1");
			}else if(UserDataRoleConfig.viewAllDataByUserid(userModel.getUserid(), "expense.userdata.isFinancial")){
				request.setAttribute("status", "2");
			}
			Page page = expenseInfoService.getPageByCondition(expenseAccount,
					pageNum, pageSize, userModel, submitDate, submitEndDate);
			request.setAttribute("currPage", page);
			List<Project> projects = projectService.listAllGroup();
			request.setAttribute("projectSelect", projects);
			List list = (List<Object>) page.getQueryResult();
			if (from != null && from.equals("refresh")) {
				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf(page.getPageCount()));
				map.put("recordCount", String.valueOf(page.getRecordCount()));
				JSONUtil jsonUtil = new JSONUtil(
						"cn.grgbanking.feeltm.expense.domain.OAExpenseAccount");
				org.json.JSONArray jsonObj = jsonUtil.toJSON(list, map);
				JSONObject input = new JSONObject();
				input.put("pageCount", String.valueOf(page.getPageCount()));
				input.put("recordCount", String.valueOf(page.getRecordCount()));
				input.put("jsonObj", jsonObj);
				PrintWriter out = response.getWriter();
				out.print(input);
				return "";
			} else {
				ActionContext.getContext().put("behaviorList", list);
				return "list";
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return null;
	}
	
	/**
	 * 删除项目金额明细
	 * @return
	 * lhyan3
	 * 2014年7月21日
	 */
	public String deleteProjectSumDetail(){
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		String detailId = request.getParameter("detailId");
		OAExpenseAccountProject projectsum = expenseInfoService.getProjectSumDetail(detailId);
		OAExpenseAccount account = expenseInfoService.getExpenseInfoById(projectsum.getExpenseId());
		//if(UserRoleConfig.ifHr(userModel)){
			String record = "项目名称:"+projectsum.getPrjname()
					+",项目金额:"+projectsum.getSum();
			modifyRecordService.saveRecord(account.getId(), userModel.getUsername(), "删除了:"+projectsum.getPrjname()+"的记录", new Date(), record);
		//}
		expenseInfoService.deleteProjectSumDetail(detailId);
		return null;
	}
	
	/**
	 * 新增项目金额明细
	 * @return
	 * lhyan3
	 * 2014年7月21日
	 */
	public String addProjectSumDetail(){
		//保存项目明细金额是无法取得c_id，先用uuid代替c_id,方便修改
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		List<OAExpenseAccountProject> prjsumList = new ArrayList<OAExpenseAccountProject>();
		OAExpenseAccountProject expensePrjsum = new OAExpenseAccountProject();
		String prjname = request.getParameter("prjname");
		String prjsum = request.getParameter("prjsum");
		String expenseId = request.getParameter("expenseId");
		String uuid = UUID.randomUUID().toString().substring(0,32);
		expensePrjsum.setPrjname(prjname);
		expensePrjsum.setSum(Double.parseDouble(prjsum));
		expensePrjsum.setUuid(uuid);
		expensePrjsum.setExpenseId(expenseId);
		prjsumList.add(expensePrjsum);
//		String record = "项目名称:"+prjname +",项目金额:"+prjsum;
		modifyRecordService.saveRecord(expenseId, userModel.getUsername(), "新增了"+prjname+"的记录", new Date(), " ");
		expenseInfoService.saveList(prjsumList);
		ajaxPrint("{\"uuid\":\"" + uuid + "\"}");
		return null;
	}
	/**
	 * 修改项目金额明细
	 * @return
	 * lhyan3
	 * 2014年7月21日
	 */
	public String updateProjectSumDetail(){
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		String detailId = request.getParameter("detailId");
		String expenseId = request.getParameter("expenseId");
		String prjname = request.getParameter("prjname");
		String prjsum = request.getParameter("prjsum");
		OAExpenseAccountProject projectsum = expenseInfoService.getProjectSumDetail(detailId);
		if(projectsum==null){//根据detailId找项目金额明细，如果找到说明是原有数据，如果找不到，则修改的是新增的数据，根据uuid找数据
			projectsum = expenseInfoService.getProjectSumDetailByUuid(detailId);
		}
		String record = "项目名称:"+projectsum.getPrjname() +",项目金额:"+projectsum.getSum();
		modifyRecordService.saveRecord(expenseId, userModel.getUsername(), "修改了:"+projectsum.getPrjname()+"的记录", new Date(), record);
		projectsum.setPrjname(prjname);
		projectsum.setSum(Double.parseDouble(prjsum));
		expenseInfoService.updateExpenseProjectSumInfo(projectsum);
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
	
	/**
	 * 跳转到打印报销明细页面
	 * 
	 * @return
	 */
	public String toPrintDetailPage() throws Exception {
		String expenseId=request.getParameter("expenseId");
		OAExpenseAccount expense = expenseInfoService.getExpenseInfoById(expenseId);
		request.setAttribute("expenseAccount", expense);
		//出差明细
		expenselist = expenseInfoService.getExpenseDetailByExpenseId(expenseId);
		request.setAttribute("expenselist", expenselist);
		//合计
		OAExpenseAccountCheckCondition costSum = expenseInfoService.getCostSum(expenseId);
		request.setAttribute("costSum", costSum);
		return "printExpenseDetail";
	}
	
	/**
	 * 跳转到打印费用页面
	 * 
	 * @return
	 */
	public String toPrintCostPage() throws Exception {
		String expenseId=request.getParameter("expenseId");
		OAExpenseAccount expense = expenseInfoService.getExpenseInfoById(expenseId);
		request.setAttribute("expenseAccount", expense);
		//费用明细
		travelDetailList = expenseInfoService.getTravelDetailByExpenseId(expenseId);
		request.setAttribute("travelDetailList", travelDetailList);
		//合计
		OAExpenseAccountTravelDetail travelSum = expenseInfoService.getTravelSum(expenseId);
		request.setAttribute("travelSum", travelSum);
		return "printExpenseCost";
	}
	
	/**
	 * 保存报销单是否打印
	 * 
	 * @return
	 */
	public String saveExpensePrint() throws Exception {
		try{
			String expenseId=request.getParameter("expenseId");
			String printType=request.getParameter("printType");
			OAExpenseAccount expense = expenseInfoService.getExpenseInfoById(expenseId);
			if("cost".equals(printType)){
				expense.setPrintcost("1");
			}else if("detail".equals(printType)){
				expense.setPrintdetail("1");
			}
			expenseInfoService.updateExpenseInfo(expense);
			MsgBox msgBox = new MsgBox(request, getText("操作成功!"));
			msgBox.setButtonType(MsgBox.BUTTON_OK);
		}catch (Exception e) {
				MsgBox msgBox = new MsgBox(request, getText("出现未知错误,请联系管理员!"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				addActionMessage(getText("添加失败"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		}
		return "msgBox";
	}

	public OAExpenseAccount getExpenseAccount() {
		return expenseAccount;
	}

	public void setExpenseAccount(OAExpenseAccount expenseAccount) {
		this.expenseAccount = expenseAccount;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public ExpenseInfoService getExpenseInfoService() {
		return expenseInfoService;
	}

	public void setExpenseInfoService(ExpenseInfoService expenseInfoService) {
		this.expenseInfoService = expenseInfoService;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDetName() {
		return detName;
	}

	public void setDetName(String detName) {
		this.detName = detName;
	}

	public String getPrjName() {
		return prjName;
	}
	
	public void setPrjName(String prjName) {
		this.prjName = prjName;
	}

	public String getExpenseNum() {
		return expenseNum;
	}

	public void setExpenseNum(String expenseNum) {
		this.expenseNum = expenseNum;
	}

	@Override
	public OAExpenseAccount getModel() {

		return expenseAccount;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public List<OAExpenseAccountCheckCondition> getExpenseList() {
		return expenseList;
	}

	public void setExpenseList(List<OAExpenseAccountCheckCondition> expenseList) {
		this.expenseList = expenseList;
	}

	public List<OAExpenseAccountCheckCondition> getExpenselist() {
		return expenselist;
	}

	public void setExpenselist(List<OAExpenseAccountCheckCondition> expenselist) {
		this.expenselist = expenselist;
	}

	public List<OAExpenseAccountTravelDetail> getTravelDetailList() {
		return travelDetailList;
	}

	public void setTravelDetailList(
			List<OAExpenseAccountTravelDetail> travelDetailList) {
		this.travelDetailList = travelDetailList;
	}

	public SysUserInfoService getSysUserInfoService() {
		return sysUserInfoService;
	}

	public void setSysUserInfoService(SysUserInfoService sysUserInfoService) {
		this.sysUserInfoService = sysUserInfoService;
	}

	public ApprovalService getApprovalService() {
		return approvalService;
	}

	public void setApprovalService(ApprovalService approvalService) {
		this.approvalService = approvalService;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getExpenseprjname() {
		return expenseprjname;
	}

	public void setExpenseprjname(String expenseprjname) {
		this.expenseprjname = expenseprjname;
	}

}
