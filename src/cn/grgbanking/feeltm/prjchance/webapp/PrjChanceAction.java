package cn.grgbanking.feeltm.prjchance.webapp;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import cn.grgbanking.feeltm.config.BusnDataDir;
import cn.grgbanking.feeltm.config.UserRoleConfig;
import cn.grgbanking.feeltm.domain.PrjChance;
import cn.grgbanking.feeltm.domain.PrjChanceFollow;
import cn.grgbanking.feeltm.domain.PrjChanceOtherInfo;
import cn.grgbanking.feeltm.domain.SysDatadir;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.prjchance.service.PrjChanceFollowService;
import cn.grgbanking.feeltm.prjchance.service.PrjChanceService;
import cn.grgbanking.feeltm.project.service.ProjectService;
import cn.grgbanking.feeltm.um.service.SysUserInfoService;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.DateUtil;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;

@SuppressWarnings( { "serial", "unchecked" })
public class PrjChanceAction extends BaseAction implements ModelDriven<PrjChance> {
	@Autowired
	private ProjectService projectService;
	@Autowired
	private PrjChanceFollowService prjChanceFollowService;
	private PrjChanceService prjChanceService;
	
	private PrjChance prjChance = new PrjChance();
	private List<PrjChanceFollow > followList=new ArrayList<PrjChanceFollow>();
	private List<PrjChanceOtherInfo> otherInfoList=new ArrayList<PrjChanceOtherInfo>();
	private List<PrjChance> list = new ArrayList<PrjChance>();
	private List<String> groupNameList = new ArrayList<String>();
	private String pageNum;
	private String pageSize;
	private SysUserInfoService sysUserInfoService;
	
	private List<String> followList_followMan;//跟进人
	private List<String> followList_followDate;//跟进日期
	private List<String> followList_prjStage;//项目阶段
	private List<String> followList_followContent;//跟进内容
	
	private List<String> otherInfoList_relMan;//联系人
	private List<String> otherInfoList_relDept;//联系人部门
	private List<String> otherInfoList_relPosition;//联系人职位
	
	
	/**
	 * 查询所有商机信息，带有分页
	 * @return
	 * @throws Exception
	 */
	public String listAll() throws Exception{
		//初始化下拉列表
		initSelectData("list");
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		int pagenum = 1;
		int pagesize = 20;
		if(pageNum!=null && !pageNum.equals("")){
			pagenum = Integer.parseInt(pageNum);
		}
		if(pageSize!=null && !pageSize.equals("")){
			pagesize = Integer.parseInt(pageSize);
		}
		Page page = prjChanceService.getPage(pagenum, pagesize);
		List<PrjChance> chanceList=page.getQueryResult();
		if(chanceList!=null){
			for(int i=0;i<chanceList.size();i++){
				chanceList.get(i).setPrjResult(getPrjResultName(chanceList.get(i).getPrjResult()));
				chanceList.get(i).setPrjStage(getPrjStageName(chanceList.get(i).getPrjStage()));
			}
		}
		request.setAttribute("currPage", page);
		list = page.getQueryResult();
		ActionContext.getContext().put("prjChancelist", list);
		request.getSession().setAttribute("prjChanceinfo.menuid",  request.getParameter("menuid"));
		return "listsuccess";
	}
	/**
	 * 根据查询条件查询
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
			String creatEndDate = null;
			if (pageNum!= null&&pageNum.length() > 0)
				pagenum = Integer.parseInt(pageNum);
			if (pageSize!= null&&pageSize.length() > 0)
				pagesize = Integer.parseInt(pageSize);
			boolean hasRight = isManagerOrNot(userModel.getUserid());
			//根据查询条件查询数据
			Page page = prjChanceService.getPrjChanceByCondition(prjChance,pagenum, pagesize);
			
			request.setAttribute("currPage", page);
			List list = (List<Object>)page.getQueryResult();
			List<PrjChance> chanceList=page.getQueryResult();
			if(chanceList!=null){
				for(int i=0;i<chanceList.size();i++){
					chanceList.get(i).setPrjResult(getPrjResultName(chanceList.get(i).getPrjResult()));
					chanceList.get(i).setPrjStage(getPrjStageName(chanceList.get(i).getPrjStage()));
				}
			}
			if (from != null && from.equals("refresh")) {
				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf(page.getPageCount()));
				map.put("recordCount", String.valueOf(page.getRecordCount()));
				JSONUtil jsonUtil = new JSONUtil(
						"cn.grgbanking.feeltm.domain.PrjChance");
				JSONArray jsonObj = jsonUtil.toJSON(list, map);//将查询数据转换为jsonArray，
				JSONObject input = new JSONObject();
				if(page.getRecordCount()==0){
					input.put("pageCount", String.valueOf(page.getPageCount()+1));	
					input.put("recordCount", String.valueOf(page.getRecordCount()));
					input.put("jsonObj", jsonObj);	
				}
				else{
					input.put("pageCount", String.valueOf(page.getPageCount()));
					input.put("recordCount", String.valueOf(page.getRecordCount()));
					input.put("jsonObj", jsonObj);	
				}
				input.put("jsonObj", jsonObj);		//将数据以json的方式传入前台						
				PrintWriter out = response.getWriter();
				out.print(input);
				return "";
			} else {
				ActionContext.getContext().put("prjChancelist", list);
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
	 * 跳转到添加商机信息页面
	 * @return
	 */
	public String add(){
		//初始化下拉列表
		initSelectData("add");
		return "add";
	}
	
	/**
	 * 保存添加的信息
	 * @return
	 */
	public String save(){
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		MsgBox msgBox;
		try {
			Date date = Calendar.getInstance().getTime();
			prjChance.setCreatDate(date);//创建记录时间
			prjChance.setUpDate(date);//修改时间
			prjChance.setUpdateMan(userModel.getUsername());//修改人
			prjChance.setLastSendEmailTime(prjChance.getLastFollowDate());//默认上次发送邮件时间为最后的跟进时间
			String id = prjChanceService.addPrjChanceInfo(prjChance);
			//转为ChanceFollow对象
			followList=toPrjChangeFollowList(id,followList_followMan,followList_followDate,followList_followContent,followList_prjStage);
			//保存对象 
			for(int i=0;i<followList.size();i++){
				prjChanceService.addPrjChanceFollow(followList.get(i));
			}
			
			//转为ChanceOtherInf对象
			otherInfoList=toPrjChanceOtherInfoList(id,otherInfoList_relDept,otherInfoList_relMan,otherInfoList_relPosition);
			//保存对象 
			for(int i=0;i<otherInfoList.size();i++){
				prjChanceService.addPrjChanceOtherInfo(otherInfoList.get(i));
			}
			
			if (StringUtils.isNotBlank(id)) {		//添加是否成功
				DateUtil.showLog("User:" + userModel.getUserid()+ " add a UserContact : " + prjChance.getUpdateMan());
				msgBox = new MsgBox(request, getText("add.ok"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				this.addActionMessage(getText("add.ok"));
			} else {
				msgBox = new MsgBox(request,"添加商机信息失败！");
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				this.addActionMessage("添加商机信息失败！");
			}
			
		} catch (Exception e) {
			// e.printStackTrace();
			SysLog.error(request,
					"error in (ContactAction.java-save())");
			SysLog.error("User:" + userModel.getUserid()
					+ "failed to add a UserContact : " + prjChance.getUpdateMan());
			SysLog.error(e);

			msgBox = new MsgBox(request, getText("operInfoform.exit"));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			this.addActionMessage("添加商机信息失败！");
		}

		boolean hasActionMessage = this.hasActionMessages();
		request.setAttribute("hasActionMessage", hasActionMessage);

		return "msgBox";
	}
	
	/**其他信息列表
	 * @param id 商机id
	 * @param relDept 联系人部门
	 * @param relMan 联系人
	 * @param relPosition 联系人职位
	 * @return
	 */
	private List<PrjChanceOtherInfo> toPrjChanceOtherInfoList(String chanceId,List<String> relDept,List<String> relMan,List<String> relPosition) {
		List<PrjChanceOtherInfo> list=new ArrayList<PrjChanceOtherInfo>();
		if(relMan!=null && relMan.size()>=0){
			for(int i=0;i<relMan.size();i++){
				try{
					PrjChanceOtherInfo otherInfo=new PrjChanceOtherInfo();
					otherInfo.setChanceId(chanceId);
					otherInfo.setRelMan(relMan.get(i));
					otherInfo.setRelDept(relDept.get(i));
					otherInfo.setRelPosition(relPosition.get(i));
					list.add(otherInfo);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		return list;
	}
	/**转为项目跟进列表
	 * @param chanceId 商机id
	 * @param prjStage 项目阶段 
	 * @param followContent 跟进内容
	 * @param followDate 跟进日期
	 * @param followMan 跟进人
	 * @return
	 */
	private List<PrjChanceFollow> toPrjChangeFollowList(String chanceId, List<String> followMan, List<String> followDate, List<String> followContent, List<String> prjStage) {
		
		List<PrjChanceFollow> list=new ArrayList<PrjChanceFollow>();
		if(prjStage!=null && prjStage.size()>=0){
			for(int i=0;i<prjStage.size();i++){
					PrjChanceFollow follow=new PrjChanceFollow();
					follow.setChanceId(chanceId);
					follow.setPrjStage(prjStage.get(i));
					follow.setFollowContent(followContent.get(i));
					try{
						follow.setFollowDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(followDate.get(i)));
					}catch(Exception e){
						follow.setFollowDate(Calendar.getInstance().getTime());
					}
					follow.setFollowMan(followMan.get(i));
					list.add(follow);
			}
		}
		return list;
	}
	/**
	 * 查看详情页面
	 * @return
	 */
	public String view(){
		try{
			String prjChanceId = request.getParameter("prjChanceId");
			 list = prjChanceService.getListPrjChanceById(prjChanceId);
			ActionContext.getContext().put("prjChanceList", list);
			
		}catch (Exception e){
			MsgBox msgBox = new MsgBox(request,"查看详情失败！");
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			return "msgBox";
		}
		
		return "view";
	}
	
	/**
	 * 删除信息
	 * @return
	 * @throws Exception
	 */
	public String delete() throws Exception {
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		try {
			String[] delItem = StringUtils.split(request.getParameter("ids"), ",");
			int iCount = 0;
			for (int i = 0; i < delItem.length; i++) {
				PrjChance prj = new PrjChance();
				prj = prjChanceService.getPrjChanceById(delItem[i]);
				prjChanceService.deletePrjChanceInfo(delItem[i]);
				prjChanceService.deletePrjChanceFollowByChanceId(delItem[i]);
				prjChanceService.deletePrjChanceOtherInfoByChanceId(delItem[i]);
				iCount++;
			}	
			MsgBox msgBox = new MsgBox(request, getText("rule.del.ok"), "rule",new String[] { String.valueOf(iCount) });
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request, "删除商机信息失败！");
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		}
		return "msgBox";
	} 
	
	/**
	 * 修改商机信息
	 * @return
	 * @throws Exception
	 */
	public String update() throws Exception{ 
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		MsgBox msgBox;
		try {
			Date date = Calendar.getInstance().getTime();
			prjChance.setUpDate(date);
			prjChance.setUpdateMan(userModel.getUsername());
			//修改商机对象
			prjChanceService.updatePrjChanceInfo(prjChance);
			
			//转为ChanceFollow对象并修改对象
			followList=toPrjChangeFollowList(prjChance.getId(),followList_followMan,followList_followDate,followList_followContent,followList_prjStage);
			prjChanceService.updatePrjChanceFollowByChanceId(prjChance.getId(), followList);
			
			//转为ChanceOtherInf对象并修改对象
			otherInfoList=toPrjChanceOtherInfoList(prjChance.getId(),otherInfoList_relDept,otherInfoList_relMan,otherInfoList_relPosition);
			prjChanceService.updatePrjChanceOtherInfoByChanceId(prjChance.getId(),otherInfoList);
			
			msgBox = new MsgBox(request,"修改成功");
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			this.addActionMessage("修改成功");
			
		} catch (Exception e) {
			SysLog.error("User:" + userModel.getUserid()+ "failed to update a UserContact : " + prjChance.getUpdateMan());
			SysLog.error(e);

			msgBox = new MsgBox(request,"修改商机信息失败！");
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			this.addActionMessage("修改商机信息失败！");
		}
		return "msgBox";
	}
	
	/**
	 * 进入编辑数据页面
	 * @return
	 * @throws Exception
	 */
	public String edit() throws Exception {
		//初始化下拉列表
		initSelectData("edit");
		try {
			String Id = request.getParameter("id");
			prjChance =prjChanceService.getPrjChanceById(Id);
			followList=prjChanceService.getPrjChanceFollowByChanceId(Id);
			otherInfoList=prjChanceService.getPrjChanceOtherInfoByChanceId(Id);
			request.setAttribute("id", Id);
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "edit";
	 }

	/**
	 * 判断是否为部门经理以上
	 * @param userId
	 * @return
	 */
	private boolean isManagerOrNot(String userId){
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		return UserRoleConfig.upDeptManager(userModel);
		
	}
	
	/**
	 * 初始化下拉列表
	 * */
	private void initSelectData(String type){
		//获取项目结果
		List<SysDatadir> prjResultList=BusnDataDir.getObjectListInOrder("chanceManage.prjResult");
		request.setAttribute("prjResult", prjResultList);
		
		//获取项目阶段
		List<SysDatadir> prjStageList=BusnDataDir.getObjectListInOrder("chanceManage.prjStage");
		request.setAttribute("prjStage", prjStageList);
		
		if("add".equals(type)){
			//登记日期
			prjChance.setCreatDate(Calendar.getInstance().getTime());
			//最后修改日期
			prjChance.setLastFollowDate(Calendar.getInstance().getTime());
			//邮件抄送人
			String defaultCopyEmail=BusnDataDir.getObjectDetail("chanceManage.defaultCopyEmail.defaultEmail");
			if(StringUtils.isNotBlank(defaultCopyEmail)){
				prjChance.setCopyEmail(defaultCopyEmail);
			}
		}
		
	}
	
	
	/**
	 * @return 导出Excel表
	 */
	public String exportData() throws Exception{ 
		UserModel userModel = (UserModel) request.getSession().getAttribute( Constants.LOGIN_USER_KEY);
		SimpleDateFormat fo=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		request.setCharacterEncoding("utf-8");
		String prjname=request.getParameter("prjName");
		String followMan=request.getParameter("followMan");
		String client=request.getParameter("client");
		String area=request.getParameter("area");
		String province=request.getParameter("province");
		String clientManager=request.getParameter("clientManager");
		String clientType=request.getParameter("clientType");
		String prjStage=request.getParameter("prjStage");
		String prjResult=request.getParameter("prjResult");
		//获取需要导出的商机表的数据列表
		List beanList = prjChanceService.getPrjChanceListByCondition( prjname,
				 client, followMan,  area,  province,
				 clientManager, clientType, prjStage,
				 prjResult);
		List<PrjChance> chanceList=(List<PrjChance>)beanList;
		if(beanList!=null){
			for(int i=0;i<chanceList.size();i++){
				chanceList.get(i).setPrjResult(getPrjResultName(chanceList.get(i).getPrjResult()));
				chanceList.get(i).setPrjStage(getPrjStageName(chanceList.get(i).getPrjStage()));
			}
		}
		//文件名
		String filename = "商机报表";//设置文件名
		//获取需要导出的跟进表的数据列表
		List beanList1=prjChanceFollowService.getPrjChanceFollowListByCondition(followMan, prjStage, client,area,  province,
				 clientManager, clientType, prjStage,
				 prjResult);
		  String filename1="跟进表";
		//执行导出操作
		  //商机表和跟进表都有数据
		doExport1(beanList,filename,beanList1,filename1); 
		  
		/*doExport(beanList,filename);*/
		
		return null;
	}         
	
	
	private void doExport1(List chanceList, String filename,List chanceList1, String filename1) throws IOException {
		OutputStream os = response.getOutputStream();// 取得输出流 
		response.reset();// 清空输出流  
        response.setHeader("Content-disposition","attachment;filename="+new String(filename.getBytes("gb2312"), "iso8859-1")+".xls");// 设定输出文件头  
        response.setContentType("application/vnd.ms-excel");// 定义输出类型  
        int sheetNo = 0;
        //第一步，创建一个webbook，对应一个Excel文件
     	HSSFWorkbook wb1 = new HSSFWorkbook();
     	//第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
     	HSSFSheet sheet1 = wb1.createSheet();
     	wb1.setSheetName(sheetNo,filename1);
     	
     	/**	** ** *设置字体* ** ** **/
		HSSFFont defaultFont = wb1.createFont(); //默认字体
		defaultFont.setFontName("宋体");
		defaultFont.setFontHeightInPoints((short)10);// 字体大小
		
		HSSFFont headfont = wb1.createFont(); 
		headfont.setFontName("黑体");
		headfont.setFontHeightInPoints((short)30);
		headfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		
		HSSFFont cellfont = wb1.createFont();  
		cellfont.setFontName("黑体");
		cellfont.setFontHeightInPoints((short)12);
		cellfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		
		HSSFFont allFont = wb1.createFont();
		allFont.setFontName("黑体");
		allFont.setColor(HSSFColor.RED.index);
		allFont.setFontHeightInPoints((short)12);
		allFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		
		
		/**	** ** *设置样式* ** ** **/
		//================== 标题 =====================
		HSSFFont cellNamefont = wb1.createFont();  
		cellNamefont.setFontName("宋体");
		cellNamefont.setFontHeightInPoints((short)10);
		cellNamefont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		HSSFCellStyle cellNameStyle = this.getCellStyle(wb1,true,cellNamefont, HSSFColor.GREY_25_PERCENT.index, (short)-2, HSSFCellStyle.ALIGN_CENTER);
		cellNameStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		cellNameStyle.setFillBackgroundColor(HSSFColor.GREY_80_PERCENT.index);
		
		//================== 普通行 (左中)======================
		HSSFCellStyle defaultStyle = this.getCellStyle(wb1,true, defaultFont,(short)-2, (short)-2, HSSFCellStyle.ALIGN_LEFT);
		cellNameStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		
		//================== 普通行 (居中)======================
		HSSFCellStyle defaultCenterStyle = this.getCellStyle(wb1,true, defaultFont,(short)-2, (short)-2, HSSFCellStyle.ALIGN_CENTER);
		cellNameStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		
		//================== 日期(居中) ======================
		HSSFCellStyle dateStyle = this.getCellStyle(wb1, true,defaultFont,(short)-2, (short)-2, HSSFCellStyle.ALIGN_CENTER);
		cellNameStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		
		/**	** ** *设置文本格式* ** ** **/
		HSSFDataFormat format = wb1.createDataFormat();
		defaultStyle.setDataFormat(format.getFormat("@"));
		defaultCenterStyle.setDataFormat(format.getFormat("@"));
		dateStyle.setDataFormat(format.getFormat("yyyy-MM-dd HH:mm:ss"));
		
		/**		创建列名		*/
		HSSFRow cellNameRow1 = sheet1.createRow((int) 0);
		cellNameRow1.setHeight((short)350);
		wb1 = this.setExcelValue(wb1, sheetNo, "项目名称", 0, (short)0, cellNameStyle);
		wb1 = this.setExcelValue(wb1, sheetNo, "跟进人",0, (short)1, cellNameStyle);
		wb1 = this.setExcelValue(wb1, sheetNo, "跟进内容", 0, (short)2, cellNameStyle);
		wb1 = this.setExcelValue(wb1, sheetNo, "跟进日期", 0, (short)3, cellNameStyle);
		wb1 = this.setExcelValue(wb1, sheetNo, "项目状态", 0, (short)4, cellNameStyle);

		/**	** ** *循环数据，并将数据列入Excel表中* ** ** **/
		if(chanceList1!=null&&chanceList1.size()>0){
			int sumRow1=0;
			for(int i=0;i<chanceList1.size();i++){
				Object[] o=(Object[]) chanceList1.get(i);
				++sumRow1;
				HSSFRow row = sheet1.createRow(sumRow1);
				row.setHeight((short)256);//行高
				HSSFCell cell = row.createCell((short)0);
				
				/*项目名称*/
				cell = row.createCell((short)0);
				setBorderStyle(defaultCenterStyle);
				cell.setCellStyle(defaultCenterStyle);
				cell.setCellValue(o[0].toString());
				sheet1.setColumnWidth((short)0, (short) (10*512));
				
				/*跟进人*/
				cell = row.createCell((short)1);
				setBorderStyle(defaultCenterStyle);
				cell.setCellStyle(defaultCenterStyle);
				cell.setCellValue(o[1].toString());
				sheet1.setColumnWidth((short)1, (short) (7*512));
				
				/*跟进内容*/
				cell = row.createCell((short)2);
				setBorderStyle(defaultCenterStyle);
				cell.setCellStyle(defaultCenterStyle);
				cell.setCellValue(o[2].toString());
				sheet1.setColumnWidth((short)2, (short) (10*512));
				
				
				
				/*跟进日期*/
				cell = row.createCell((short)3);
				setBorderStyle(defaultCenterStyle);
				defaultStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
				cell.setCellStyle(defaultCenterStyle);
				cell.setCellValue(o[3].toString());
				sheet1.setColumnWidth((short)3, (short) (10*512));
				
				/*项目状态*/
				cell = row.createCell((short)4);
				setBorderStyle(defaultCenterStyle);
				cell.setCellStyle(defaultCenterStyle);
				Map  prjStage=BusnDataDir.getMapKeyValue("chanceManage.prjStage");
				String stage=(String) prjStage.get(o[4].toString());
				cell.setCellValue(stage);
				sheet1.setColumnWidth((short)4, (short) (7*512));
			}
			
		}
		
	    sheetNo = 1;
     	//第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
     	HSSFSheet sheet = wb1.createSheet();
     	wb1.setSheetName(sheetNo,filename);//poi3.7版本中只有两个参数
 
     	
     	/**		创建列名		*/
		HSSFRow cellNameRow = sheet.createRow((int) 0);
		cellNameRow.setHeight((short)350);
		wb1 = this.setExcelValue(wb1, sheetNo, "所属区域", 0, (short)0, cellNameStyle);
		wb1 = this.setExcelValue(wb1, sheetNo, "省份",0, (short)1, cellNameStyle);
		wb1 = this.setExcelValue(wb1, sheetNo, "客户名称", 0, (short)2, cellNameStyle);
		wb1 = this.setExcelValue(wb1, sheetNo, "客户类别", 0, (short)3, cellNameStyle);
		wb1 = this.setExcelValue(wb1, sheetNo, "客户经理", 0, (short)4, cellNameStyle);
		wb1 = this.setExcelValue(wb1, sheetNo, "项目名称", 0, (short)5, cellNameStyle);
		wb1 = this.setExcelValue(wb1, sheetNo, "项目预算", 0, (short)6, cellNameStyle);
		wb1 = this.setExcelValue(wb1, sheetNo, "客户联系人", 0, (short)7, cellNameStyle);
		wb1 = this.setExcelValue(wb1, sheetNo, "联系人方式", 0, (short)8, cellNameStyle);
		wb1 = this.setExcelValue(wb1, sheetNo, "联系人部门", 0, (short)9, cellNameStyle);
		wb1 = this.setExcelValue(wb1, sheetNo, "联系人职位", 0,(short) 10, cellNameStyle);
		wb1 = this.setExcelValue(wb1, sheetNo, "跟进人", 0, (short)11, cellNameStyle);
		wb1 = this.setExcelValue(wb1, sheetNo, "项目阶段", 0, (short)12, cellNameStyle);
		wb1 = this.setExcelValue(wb1, sheetNo, "项目结果", 0, (short)13, cellNameStyle);
		wb1 = this.setExcelValue(wb1, sheetNo, "最后跟进日期", 0, (short)14, cellNameStyle);
		wb1 = this.setExcelValue(wb1, sheetNo, "登记日期", 0, (short)15, cellNameStyle);
		wb1 = this.setExcelValue(wb1, sheetNo, "商机描述", 0, (short)16, cellNameStyle);
		wb1 = this.setExcelValue(wb1, sheetNo, "备注", 0, (short)17, cellNameStyle);
		
		
		/**	** ** *循环数据，并将数据列入Excel表中* ** ** **/
		if(chanceList!=null&&chanceList.size()>0){
			int sumRow=0;
			for(int i=0;i<chanceList.size();i++){
				PrjChance chance=(PrjChance)chanceList.get(i);
				++sumRow;
				HSSFRow row = sheet.createRow(sumRow);
				row.setHeight((short)256);//行高
				HSSFCell cell = row.createCell((short)0);
				
				/*所属区域*/
				cell = row.createCell((short)0);
				setBorderStyle(defaultCenterStyle);
				cell.setCellStyle(defaultCenterStyle);
				cell.setCellValue(chance.getArea());
				sheet.setColumnWidth((short)0, (short) (10*512));
				
				/*省份*/
				cell = row.createCell((short)1);
				setBorderStyle(defaultCenterStyle);
				cell.setCellStyle(defaultCenterStyle);
				cell.setCellValue(chance.getProvince());
				sheet.setColumnWidth((short)1, (short) (7*512));
				
				/*客户名称*/
				cell = row.createCell((short)2);
				setBorderStyle(defaultCenterStyle);
				cell.setCellStyle(defaultCenterStyle);
				cell.setCellValue(chance.getClient());
				sheet.setColumnWidth((short)2, (short) (10*512));
				
				/*客户类别*/
				cell = row.createCell((short)3);
				setBorderStyle(defaultCenterStyle);
				cell.setCellStyle(defaultCenterStyle);
				cell.setCellValue(chance.getClientType());
				sheet.setColumnWidth((short)3, (short) (10*512));
				
				/*客户经理*/
				cell = row.createCell((short)4);
				setBorderStyle(defaultCenterStyle);
				cell.setCellStyle(defaultCenterStyle);
				cell.setCellValue(chance.getClientManager());
				sheet.setColumnWidth((short)4, (short) (7*512));
				/*项目名称*/
				cell = sheet.getRow(sumRow).createCell((short)5);
				setBorderStyle(defaultCenterStyle);
				cell.setCellStyle(defaultCenterStyle);
				cell.setCellValue(chance.getPrjName());
				sheet.setColumnWidth((short)5, (short) (10*512));
				
				/*项目预算*/
				cell = row.createCell((short)6);
				setBorderStyle(defaultCenterStyle);
				cell.setCellStyle(defaultCenterStyle);
				cell.setCellValue(chance.getBudget());
				sheet.setColumnWidth((short)6, (short) (7*512));
				
	
				/*客户联系人*/
				cell = row.createCell((short)7);
				setBorderStyle(defaultCenterStyle);
				cell.setCellStyle(defaultCenterStyle);
				cell.setCellValue(chance.getClientLinkMan());
				sheet.setColumnWidth((short)7, (short) (7*512));
				/*联系方式*/
				cell = row.createCell((short)8);
				setBorderStyle(defaultCenterStyle);
				cell.setCellStyle(defaultCenterStyle);
				cell.setCellValue(chance.getContactortel());
				sheet.setColumnWidth((short)8, (short) (7*512));
				
				/*联系人部门*/
				cell = row.createCell((short)9);
				setBorderStyle(defaultCenterStyle);
				defaultStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
				cell.setCellStyle(defaultCenterStyle);
				cell.setCellValue(chance.getLinkManDept());
				sheet.setColumnWidth((short)9, (short) (7*512));
				
				/*联系人职位*/
				cell = row.createCell((short)10);
				setBorderStyle(defaultCenterStyle);
				defaultStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
				cell.setCellStyle(defaultCenterStyle);
				cell.setCellValue(chance.getLinkManPosition());
				sheet.setColumnWidth((short)10, (short) (7*512));
				
				/*跟进人*/
				cell = row.createCell((short)11);
				setBorderStyle(defaultCenterStyle);
				defaultStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
				cell.setCellStyle(defaultCenterStyle);
				cell.setCellValue(chance.getFollowMan());
				sheet.setColumnWidth((short)11, (short) (7*512));
				
				/*项目阶段*/
				cell = sheet.getRow(sumRow).createCell((short)12);
				setBorderStyle(defaultCenterStyle);
				cell.setCellStyle(defaultCenterStyle);
				cell.setCellValue(chance.getPrjStage());
				sheet.setColumnWidth((short)12, (short) (7*512));
				
				/*项目结果*/
				cell = sheet.getRow(sumRow).createCell((short)13);
				setBorderStyle(defaultCenterStyle);
				cell.setCellStyle(defaultCenterStyle);
				cell.setCellValue(chance.getPrjResult());
				sheet.setColumnWidth((short)13, (short) (7*512));
				
				
				
				/*最近跟进日期*/
				cell = row.createCell((short)14);
				setBorderStyle(defaultCenterStyle);
				defaultStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
				cell.setCellStyle(defaultCenterStyle);
				cell.setCellValue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(chance.getLastFollowDate()));
				sheet.setColumnWidth((short)14, (short) (10*512));
				
				/*登记日期*/
				cell = row.createCell((short)15);
				setBorderStyle(defaultCenterStyle);
				defaultStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
				cell.setCellStyle(defaultCenterStyle);
				cell.setCellValue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(chance.getCreatDate()));
				sheet.setColumnWidth((short)15, (short) (10*512));
				
				
				
				/*商机描述*/
				cell = row.createCell((short)16);
				setBorderStyle(defaultCenterStyle);
				defaultStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
				cell.setCellStyle(defaultCenterStyle);
				cell.setCellValue(chance.getDescription());
				sheet.setColumnWidth((short)16, (short) (15*512));
				
				
				/*备注*/
				cell = row.createCell((short)17);
				setBorderStyle(defaultCenterStyle);
				defaultStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
				cell.setCellStyle(defaultCenterStyle);
				cell.setCellValue(chance.getNote());
				sheet.setColumnWidth((short)17, (short) (15*512));
			}
		}
		
		wb1.write(os);
		os.close();
		
		//清理刷新缓冲区，将缓存中的数据将数据导出excel
		os.flush();
		//关闭os
		if(os!=null){
			os.close();
		}
	}
	private void doExport(List chanceList, String filename) throws IOException {
		OutputStream os = response.getOutputStream();// 取得输出流 
		response.reset();// 清空输出流  
        response.setHeader("Content-disposition","attachment;filename="+new String(filename.getBytes("gb2312"), "iso8859-1")+".xls");// 设定输出文件头  
        response.setContentType("application/vnd.ms-excel");// 定义输出类型  
        //第一步，创建一个webbook，对应一个Excel文件
     	HSSFWorkbook wb = new HSSFWorkbook();
     	//第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
     	HSSFSheet sheet = wb.createSheet();
     	wb.setSheetName(0,filename);//poi3.7版本中只有两个参数
     	
     	/**	** ** *设置字体* ** ** **/
		HSSFFont defaultFont = wb.createFont(); //默认字体
		defaultFont.setFontName("宋体");
		defaultFont.setFontHeightInPoints((short)10);// 字体大小
		
		HSSFFont headfont = wb.createFont(); 
		headfont.setFontName("黑体");
		headfont.setFontHeightInPoints((short)30);
		headfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		
		HSSFFont cellfont = wb.createFont();  
		cellfont.setFontName("黑体");
		cellfont.setFontHeightInPoints((short)12);
		cellfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		
		HSSFFont allFont = wb.createFont();
		allFont.setFontName("黑体");
		allFont.setColor(HSSFColor.RED.index);
		allFont.setFontHeightInPoints((short)12);
		allFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		
		
		/**	** ** *设置样式* ** ** **/
		//================== 标题 =====================
		HSSFFont cellNamefont = wb.createFont();  
		cellNamefont.setFontName("宋体");
		cellNamefont.setFontHeightInPoints((short)10);
		cellNamefont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		HSSFCellStyle cellNameStyle = this.getCellStyle(wb,true,cellNamefont, HSSFColor.GREY_25_PERCENT.index, (short)-2, HSSFCellStyle.ALIGN_CENTER);
		cellNameStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		cellNameStyle.setFillBackgroundColor(HSSFColor.GREY_80_PERCENT.index);
		
		//================== 普通行 (左中)======================
		HSSFCellStyle defaultStyle = this.getCellStyle(wb,true, defaultFont,(short)-2, (short)-2, HSSFCellStyle.ALIGN_LEFT);
		cellNameStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		
		//================== 普通行 (居中)======================
		HSSFCellStyle defaultCenterStyle = this.getCellStyle(wb,true, defaultFont,(short)-2, (short)-2, HSSFCellStyle.ALIGN_CENTER);
		cellNameStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		
		//================== 日期(居中) ======================
		HSSFCellStyle dateStyle = this.getCellStyle(wb, true,defaultFont,(short)-2, (short)-2, HSSFCellStyle.ALIGN_CENTER);
		cellNameStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		
		/**	** ** *设置文本格式* ** ** **/
		HSSFDataFormat format = wb.createDataFormat();
		defaultStyle.setDataFormat(format.getFormat("@"));
		defaultCenterStyle.setDataFormat(format.getFormat("@"));
		dateStyle.setDataFormat(format.getFormat("yyyy-MM-dd HH:mm:ss"));
		
		/**		创建列名		*/
		HSSFRow cellNameRow = sheet.createRow((int) 0);
		cellNameRow.setHeight((short)350);
		wb = this.setExcelValue(wb, 0, "所属区域", 0, (short)0, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "省份",0, (short)1, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "客户名称", 0, (short)2, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "客户类别", 0, (short)3, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "客户经理", 0, (short)4, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "项目名称", 0, (short)5, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "项目预算", 0, (short)6, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "客户联系人", 0, (short)7, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "联系人方式", 0, (short)8, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "联系人部门", 0, (short)9, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "联系人职位", 0,(short) 10, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "跟进人", 0, (short)11, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "项目阶段", 0, (short)12, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "项目结果", 0, (short)13, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "最后跟进日期", 0, (short)14, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "登记日期", 0, (short)15, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "商机描述", 0, (short)16, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "备注", 0, (short)17, cellNameStyle);
		
		
		/**	** ** *循环数据，并将数据列入Excel表中* ** ** **/
		int sumRow=0;
		for(int i=0;i<chanceList.size();i++){
			PrjChance chance=(PrjChance)chanceList.get(i);
			++sumRow;
			HSSFRow row = sheet.createRow(sumRow);
			row.setHeight((short)256);//行高
			HSSFCell cell = row.createCell((short)0);
			
			/*所属区域*/
			cell = row.createCell((short)0);
			setBorderStyle(defaultCenterStyle);
			cell.setCellStyle(defaultCenterStyle);
			cell.setCellValue(chance.getArea());
			sheet.setColumnWidth((short)0, (short) (10*512));
			
			/*省份*/
			cell = row.createCell((short)1);
			setBorderStyle(defaultCenterStyle);
			cell.setCellStyle(defaultCenterStyle);
			cell.setCellValue(chance.getProvince());
			sheet.setColumnWidth((short)1, (short) (7*512));
			
			/*客户名称*/
			cell = row.createCell((short)2);
			setBorderStyle(defaultCenterStyle);
			cell.setCellStyle(defaultCenterStyle);
			cell.setCellValue(chance.getClient());
			sheet.setColumnWidth((short)2, (short) (10*512));
			
			/*客户类别*/
			cell = row.createCell((short)3);
			setBorderStyle(defaultCenterStyle);
			cell.setCellStyle(defaultCenterStyle);
			cell.setCellValue(chance.getClientType());
			sheet.setColumnWidth((short)3, (short) (10*512));
			
			/*客户经理*/
			cell = row.createCell((short)4);
			setBorderStyle(defaultCenterStyle);
			cell.setCellStyle(defaultCenterStyle);
			cell.setCellValue(chance.getClientManager());
			sheet.setColumnWidth((short)4, (short) (7*512));
			/*项目名称*/
			cell = sheet.getRow(sumRow).createCell((short)5);
			setBorderStyle(defaultCenterStyle);
			cell.setCellStyle(defaultCenterStyle);
			cell.setCellValue(chance.getPrjName());
			sheet.setColumnWidth((short)5, (short) (10*512));
			
			/*项目预算*/
			cell = row.createCell((short)6);
			setBorderStyle(defaultCenterStyle);
			cell.setCellStyle(defaultCenterStyle);
			cell.setCellValue(chance.getBudget());
			sheet.setColumnWidth((short)6, (short) (7*512));
			

			/*客户联系人*/
			cell = row.createCell((short)7);
			setBorderStyle(defaultCenterStyle);
			cell.setCellStyle(defaultCenterStyle);
			cell.setCellValue(chance.getClientLinkMan());
			sheet.setColumnWidth((short)7, (short) (7*512));
			/*联系方式*/
			cell = row.createCell((short)8);
			setBorderStyle(defaultCenterStyle);
			cell.setCellStyle(defaultCenterStyle);
			cell.setCellValue(chance.getContactortel());
			sheet.setColumnWidth((short)8, (short) (7*512));
			
			/*联系人部门*/
			cell = row.createCell((short)9);
			setBorderStyle(defaultCenterStyle);
			defaultStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			cell.setCellStyle(defaultCenterStyle);
			cell.setCellValue(chance.getLinkManDept());
			sheet.setColumnWidth((short)9, (short) (7*512));
			
			/*联系人职位*/
			cell = row.createCell((short)10);
			setBorderStyle(defaultCenterStyle);
			defaultStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			cell.setCellStyle(defaultCenterStyle);
			cell.setCellValue(chance.getLinkManPosition());
			sheet.setColumnWidth((short)10, (short) (7*512));
			
			/*跟进人*/
			cell = row.createCell((short)11);
			setBorderStyle(defaultCenterStyle);
			defaultStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			cell.setCellStyle(defaultCenterStyle);
			cell.setCellValue(chance.getFollowMan());
			sheet.setColumnWidth((short)11, (short) (7*512));
			
			/*项目阶段*/
			cell = sheet.getRow(sumRow).createCell((short)12);
			setBorderStyle(defaultCenterStyle);
			cell.setCellStyle(defaultCenterStyle);
			cell.setCellValue(chance.getPrjStage());
			sheet.setColumnWidth((short)12, (short) (7*512));
			
			/*项目结果*/
			cell = sheet.getRow(sumRow).createCell((short)13);
			setBorderStyle(defaultCenterStyle);
			cell.setCellStyle(defaultCenterStyle);
			cell.setCellValue(chance.getPrjResult());
			sheet.setColumnWidth((short)13, (short) (7*512));
			
			
			
			/*最近跟进日期*/
			cell = row.createCell((short)14);
			setBorderStyle(defaultCenterStyle);
			defaultStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			cell.setCellStyle(defaultCenterStyle);
			cell.setCellValue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(chance.getLastFollowDate()));
			sheet.setColumnWidth((short)14, (short) (10*512));
			
			/*登记日期*/
			cell = row.createCell((short)15);
			setBorderStyle(defaultCenterStyle);
			defaultStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			cell.setCellStyle(defaultCenterStyle);
			cell.setCellValue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(chance.getCreatDate()));
			sheet.setColumnWidth((short)15, (short) (10*512));
			
			
			
			/*商机描述*/
			cell = row.createCell((short)16);
			setBorderStyle(defaultCenterStyle);
			defaultStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			cell.setCellStyle(defaultCenterStyle);
			cell.setCellValue(chance.getDescription());
			sheet.setColumnWidth((short)16, (short) (15*512));
			
			
			/*备注*/
			cell = row.createCell((short)17);
			setBorderStyle(defaultCenterStyle);
			defaultStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			cell.setCellStyle(defaultCenterStyle);
			cell.setCellValue(chance.getNote());
			sheet.setColumnWidth((short)17, (short) (15*512));
		}
		
		wb.write(os);
		os.close();
		
		//清理刷新缓冲区，将缓存中的数据将数据导出excel
		os.flush();
		//关闭os
		if(os!=null){
			os.close();
		}
	}

	private void setBorderStyle(HSSFCellStyle style){
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);	
		style.setBottomBorderColor(HSSFColor.BLACK.index);	
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);		
		style.setLeftBorderColor(HSSFColor.BLACK.index);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setRightBorderColor(HSSFColor.BLACK.index);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setTopBorderColor(HSSFColor.BLACK.index);
	}
	
	/**
	 * 设置样式方法，
	 * @param wb
	 * @param hasBorder 是否显示边框
	 * @param defaultFont
	 * @param FillForeColor
	 * @param FillbackColor
	 * @param local
	 * @return
	 */
	private HSSFCellStyle getCellStyle(HSSFWorkbook wb,boolean hasBorder,HSSFFont defaultFont,short FillForeColor,short FillbackColor,short local){
		HSSFCellStyle style = wb.createCellStyle();	
		
		/**  黑色粗体边框    */
		if(hasBorder){
			setBorderStyle(style);
		}
		/** 	fontColor字体		*/
		if(defaultFont!=null){
			style.setFont(defaultFont);
		}
		
		/**		FillColor前景填充		*/
		if(FillForeColor!=(short)-2){
			style.setFillForegroundColor(FillForeColor);
			style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		}
		if(FillForeColor!=(short)-2){
			style.setFillBackgroundColor(FillForeColor);
			style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		}
		/**		设置格式 		*/
		if(local!=-2){
			style.setAlignment(local);
		}
		style.setWrapText(true);//自动换行
		return style;
	}
	
	/**
	 * 设置每一个cell值以及其样式
	 * @param wb
	 * @param sheetNo
	 * @param value
	 * @param rowNo
	 * @param cellNo
	 * @param defaultStyle
	 * @return
	 */
	private HSSFWorkbook setExcelValue(HSSFWorkbook wb,int sheetNo,Object value,int rowNo,short cellNo,HSSFCellStyle defaultStyle){
		HSSFSheet sheet = wb.getSheetAt(sheetNo);
		HSSFRow row = sheet.getRow(rowNo);
		HSSFCell cell = row.createCell(cellNo);
		if(value instanceof Integer){
			cell.setCellValue(Integer.valueOf(value.toString()));
		}else if(value instanceof String){
			cell.setCellValue(value.toString());
		}else if(value instanceof Date){
			cell.setCellValue((Date)value);
		}else if(value instanceof Double){
			cell.setCellValue((Double)value);
		}else if(value instanceof Calendar){
			cell.setCellValue((Calendar)value);
		}else if(value instanceof Boolean){
			cell.setCellValue(value.toString().equals("true")?"是":"否");
		}else if(value == null){
			cell.setCellValue(value.toString());
		}else {
			cell.setCellValue(value.toString());
		}
		if(defaultStyle!=null){
			cell.setCellStyle(defaultStyle);
		}
		return wb;
	}
	
	
	/**获取项目结果的值
	 * @param key
	 * @return
	 */
	public String getPrjResultName(String key){
		return BusnDataDir.getObjectDetail("chanceManage.prjResult."+key);
	}
	
	
	/**获取项目阶段的值
	 * @param key
	 * @return
	 */
	public String getPrjStageName(String key){
		return BusnDataDir.getObjectDetail("chanceManage.prjStage."+key);
	}
	
	public SysUserInfoService getSysUserInfoService() {
		return sysUserInfoService;
	}
	public void setSysUserInfoService(SysUserInfoService sysUserInfoService) {
		this.sysUserInfoService = sysUserInfoService;
	}
	public PrjChanceService getPrjChanceService() {
		return prjChanceService;
	}



	public void setPrjChanceService(PrjChanceService prjChanceService) {
		this.prjChanceService = prjChanceService;
	}



	public PrjChance getPrjChance() {
		return prjChance;
	}



	public void setPrjChance(PrjChance prjChance) {
		this.prjChance = prjChance;
	}



	public List<PrjChance> getList() {
		return list;
	}



	public void setList(List<PrjChance> list) {
		this.list = list;
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



	public List<PrjChanceOtherInfo> getOtherInfoList() {
		return otherInfoList;
	}
	public void setOtherInfoList(List<PrjChanceOtherInfo> otherInfoList) {
		this.otherInfoList = otherInfoList;
	}
	public PrjChance getModel() {
		
		return prjChance;
	}
	public List<PrjChanceFollow> getFollowList() {
		return followList;
	}
	public void setFollowList(List<PrjChanceFollow> followList) {
		this.followList = followList;
	}
	public List<String> getFollowList_followMan() {
		return followList_followMan;
	}
	public void setFollowList_followMan(List<String> followList_followMan) {
		this.followList_followMan = followList_followMan;
	}
	public List<String> getFollowList_followDate() {
		return followList_followDate;
	}
	public void setFollowList_followDate(List<String> followList_followDate) {
		this.followList_followDate = followList_followDate;
	}
	public List<String> getFollowList_prjStage() {
		return followList_prjStage;
	}
	public void setFollowList_prjStage(List<String> followList_prjStage) {
		this.followList_prjStage = followList_prjStage;
	}
	public List<String> getFollowList_followContent() {
		return followList_followContent;
	}
	public void setFollowList_followContent(List<String> followList_followContent) {
		this.followList_followContent = followList_followContent;
	}
	public List<String> getOtherInfoList_relMan() {
		return otherInfoList_relMan;
	}
	public void setOtherInfoList_relMan(List<String> otherInfoList_relMan) {
		this.otherInfoList_relMan = otherInfoList_relMan;
	}
	public List<String> getOtherInfoList_relDept() {
		return otherInfoList_relDept;
	}
	public void setOtherInfoList_relDept(List<String> otherInfoList_relDept) {
		this.otherInfoList_relDept = otherInfoList_relDept;
	}
	public List<String> getOtherInfoList_relPosition() {
		return otherInfoList_relPosition;
	}
	public void setOtherInfoList_relPosition(List<String> otherInfoList_relPosition) {
		this.otherInfoList_relPosition = otherInfoList_relPosition;
	}
	public List<String> getGroupNameList() {
		return groupNameList;
	}
	public void setGroupNameList(List<String> groupNameList) {
		this.groupNameList = groupNameList;
	}
}
