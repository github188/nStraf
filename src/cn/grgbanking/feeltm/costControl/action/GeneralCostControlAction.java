package cn.grgbanking.feeltm.costControl.action;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import cn.grgbanking.feeltm.common.util.SystemHelper;
import cn.grgbanking.feeltm.config.BusnDataDir;
import cn.grgbanking.feeltm.costControl.bean.DeptGeneralCostBean;
import cn.grgbanking.feeltm.costControl.domain.DeptDetailCost;
import cn.grgbanking.feeltm.costControl.domain.DeptGeneralCost;
import cn.grgbanking.feeltm.costControl.services.CostControlService;
import cn.grgbanking.feeltm.holiday.service.HolidayService;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.loglistener.domain.WarnInfo;
import cn.grgbanking.feeltm.loglistener.service.TemplateEmailService;
import cn.grgbanking.feeltm.loglistener.service.WarnInfoService;
import cn.grgbanking.feeltm.staff.service.StaffInfoService;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.DateUtil;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.webapp.BaseAction;

import com.opensymphony.xwork2.ActionContext;

public class GeneralCostControlAction extends BaseAction{
	@Autowired
	private CostControlService costControlService;
	@Autowired
	private StaffInfoService staffInfoService;
	@Autowired
	private WarnInfoService warnInfoService;
	@Autowired
	private HolidayService holidayService;
	
	/** 查询部门基础信息
	 * @return
	 * @throws Exception
	 */
	public String list() throws Exception {
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		try {
			String from = request.getParameter("from");
			String queryStartTime=null;
			String queryEndTime=null;
			String queryDept=null;
			if (request.getParameter("queryStartTime") != null && request.getParameter("queryStartTime").length() > 0)
				queryStartTime = request.getParameter("queryStartTime");
			if (request.getParameter("queryEndTime") != null && request.getParameter("queryEndTime").length() > 0)
				queryEndTime = request.getParameter("queryEndTime");
			if (request.getParameter("queryDept") != null && request.getParameter("queryDept").length() > 0)
				queryDept = request.getParameter("queryDept");
			
			//第一次查询，默认查询当月
			if(from == null||!from.equals("refresh")){
				Calendar cal=Calendar.getInstance();
				int year=cal.get(Calendar.YEAR);
				int month=cal.get(Calendar.MONTH)+1;
				queryStartTime=year+"-"+(month<10?("0"+month):month)+"-01";
				queryEndTime=year+"-"+(month<10?("0"+month):month)+"-"+DateUtil.getLastDayForYearAndMonth(year, month);
				request.setAttribute("queryStartTime", queryStartTime);
				request.setAttribute("queryEndTime", queryEndTime);
			}
			
			List list = costControlService.getDeptGeneralCostBeanList(queryDept,queryStartTime,queryEndTime);
			if (from != null && from.equals("refresh")) {
				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf("1"));
				map.put("recordCount", String.valueOf(list.size()));
				//公司的json转换
				JSONUtil jsonUtil = new JSONUtil("cn.grgbanking.feeltm.costControl.bean.DeptGeneralCostBean");
				JSONArray jsonObj = jsonUtil.toJSON(list, map);
				
				JSONObject input = new JSONObject();
				input.put("pageCount", String.valueOf(1));
				input.put("recordCount", String.valueOf(list.size()));
				input.put("jsonObj", jsonObj);								
				ajaxPrint(input.toString());
				return null;
			} else {
				ActionContext.getContext().put("generallist", list);
				return "list";
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "list";
	}
	
	/**
	 * @return 导出Excel表
	 */
	public String exportData() throws Exception{ 
		UserModel userModel = (UserModel) request.getSession().getAttribute( Constants.LOGIN_USER_KEY);
		SimpleDateFormat fo=new SimpleDateFormat("yyyy-MM-dd");
		request.setCharacterEncoding("utf-8");

		String queryStartTime=null;
		String queryEndTime=null;
		String queryDept=null;
		if (request.getParameter("queryStartTime") != null && request.getParameter("queryStartTime").length() > 0)
			queryStartTime = request.getParameter("queryStartTime");
		if (request.getParameter("queryEndTime") != null && request.getParameter("queryEndTime").length() > 0)
			queryEndTime = request.getParameter("queryEndTime");
		if (request.getParameter("queryDept") != null && request.getParameter("queryDept").length() > 0)
			queryDept = request.getParameter("queryDept");
		
		//获取数据列表
		List costBeanList = costControlService.getDeptGeneralCostBeanList(queryDept,queryStartTime,queryEndTime);
		//文件名
		String filename = getWorkSheetName(queryDept,queryStartTime,queryEndTime);//设置文件名
		
		//执行导出操作
		doExport(costBeanList,filename);
		
		
		MsgBox msgBox = new MsgBox(request, getText("add.ok"));
		msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		this.addActionMessage(getText("add.ok"));
		return "msgBox";
	}         
	
	private void doExport(List costBeanList, String filename) throws IOException {
		OutputStream os = response.getOutputStream();// 取得输出流 
		response.reset();// 清空输出流  
        response.setHeader("Content-disposition","attachment;filename="+new String(filename.getBytes("gb2312"), "iso8859-1")+".xls");// 设定输出文件头  
        response.setContentType("application/vnd.ms-excel");// 定义输出类型  
        //第一步，创建一个webbook，对应一个Excel文件
     	HSSFWorkbook wb = new HSSFWorkbook();
     	//第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
     	HSSFSheet sheet = wb.createSheet();
//     	wb.setSheetName(0,filename,HSSFWorkbook.ENCODING_UTF_16);
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
		dateStyle.setDataFormat(format.getFormat("yyyy-MM-dd "));
		
		/**		创建列名		*/
		HSSFRow cellNameRow = sheet.createRow((int) 0);
		cellNameRow.setHeight((short)350);
		wb = this.setExcelValue(wb, 0, "部门名称", 0, (short)0, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "部门人数", 0, (short)1, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "部门统计人数", 0, (short)2, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "开始时间", 0, (short)3, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "结束时间", 0,(short) 4, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "项目经理确认", 0, (short)5, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "部门经理确认",0, (short)6, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "未确认", 0, (short)7, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "未登记", 0, (short)8, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "实习人员消耗", 0, (short)9, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "非项目比例%", 0, (short)10, cellNameStyle);
		
		
		
		/**	** ** *循环数据，并将数据列入Excel表中* ** ** **/
		int sumRow=0;
		for(int i=0;i<costBeanList.size();i++){
			DeptGeneralCostBean costBean=(DeptGeneralCostBean)costBeanList.get(i);
			++sumRow;
			HSSFRow row = sheet.createRow(sumRow);
			row.setHeight((short)256);//行高
			HSSFCell cell = row.createCell((short)0);
			
			/*部门名称*/
			cell = sheet.getRow(sumRow).createCell((short)0);
			setBorderStyle(defaultCenterStyle);
			cell.setCellStyle(defaultCenterStyle);
			cell.setCellValue(costBean.getDeptName());
			sheet.setColumnWidth((short)0, (short) (10*512));
			
			/*部门人数*/
			cell = sheet.getRow(sumRow).createCell((short)1);
			setBorderStyle(defaultCenterStyle);
			cell.setCellStyle(defaultCenterStyle);
			cell.setCellValue(costBean.getDeptMembersNo());
			sheet.setColumnWidth((short)1, (short) (10*512));
			
			/*部门统计人数*/
			cell = sheet.getRow(sumRow).createCell((short)2);
			setBorderStyle(defaultCenterStyle);
			cell.setCellStyle(defaultCenterStyle);
			cell.setCellValue(costBean.getDeptMembersNoStatistic());
			sheet.setColumnWidth((short)2, (short) (10*512));
			
			/*开始时间*/
			cell = row.createCell((short)3);
			setBorderStyle(defaultCenterStyle);
			cell.setCellStyle(defaultCenterStyle);
			cell.setCellValue(costBean.getQueryStartTime());
			sheet.setColumnWidth((short)3, (short) (12*512));
			
			/*结束时间*/
			cell = row.createCell((short)4);
			setBorderStyle(defaultCenterStyle);
			cell.setCellStyle(defaultCenterStyle);
			cell.setCellValue(costBean.getQueryEndTime());
			sheet.setColumnWidth((short)4, (short) (12*512));
			
			/*项目经理确认*/
			cell = row.createCell((short)5);
			setBorderStyle(defaultCenterStyle);
			cell.setCellStyle(defaultCenterStyle);
			cell.setCellValue(costBean.getProjectManagerConfirm());
			sheet.setColumnWidth((short)5, (short) (10*512));
			
			/*部门经理确认*/
			cell = row.createCell((short)6);
			setBorderStyle(defaultCenterStyle);
			cell.setCellStyle(defaultCenterStyle);
			cell.setCellValue(costBean.getDeptManagerConfirm());
			sheet.setColumnWidth((short)6, (short) (10*512));
			
			/*未确认*/
			cell = row.createCell((short)7);
			setBorderStyle(defaultCenterStyle);
			cell.setCellStyle(defaultCenterStyle);
			cell.setCellValue(costBean.getNotConfirm());
			sheet.setColumnWidth((short)7, (short) (10*512));
			
			/*未登记*/
			cell = row.createCell((short)8);
			setBorderStyle(defaultCenterStyle);
			cell.setCellStyle(defaultCenterStyle);
			cell.setCellValue(costBean.getNotRegist());
			sheet.setColumnWidth((short)8, (short) (10*512));
			
			/*实习人员消耗*/
			cell = row.createCell((short)9);
			setBorderStyle(defaultCenterStyle);
			cell.setCellStyle(defaultCenterStyle);
			double traineeCost=costBean.getTraineeCost();
			cell.setCellValue(traineeCost>0?traineeCost+"":"-");
			sheet.setColumnWidth((short)9, (short) (10*512));
			
			/*非项目比例*/
			cell = row.createCell((short)10);
			setBorderStyle(defaultCenterStyle);
			defaultStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			cell.setCellStyle(defaultCenterStyle);
			cell.setCellValue(costBean.getNotProjectPercent());
			sheet.setColumnWidth((short)10, (short) (10*512));
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

	/**excel表的名称
	 * @param queryDept
	 * @param queryStartTime
	 * @param queryEndTime
	 * @return
	 */
	private String getWorkSheetName(String queryDept, String queryStartTime,String queryEndTime) {
		try{
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			String name="";
			if(StringUtils.isNotBlank(queryDept)){
				name+=queryDept+" ";
			}
			if(StringUtils.isNotBlank(queryStartTime)&&StringUtils.isNotBlank(queryEndTime)){
				Date st=sdf.parse(queryStartTime);
				Date end=sdf.parse(queryEndTime);
				name+=new SimpleDateFormat("MM月dd日").format(st)+"-"+new SimpleDateFormat("MM月dd日").format(end);
			}
			name+=" 人日成本统计";
			return name;
		}catch(Exception e){
			e.printStackTrace();
		}
		return "部门人日成本统计";
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

	private void ajaxPrint(String str){
		try{
			HttpServletResponse response = ServletActionContext.getResponse(); 
	        response.setContentType("application/json");  
	        response.setCharacterEncoding("UTF-8");  
	        PrintWriter writer = response.getWriter();
	        writer.print(str);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 给指定的人员发送部门统计数据
	 */
	public void sendDeptCountDateToUser(){
		if(!SystemHelper.isServerMachine()){//非服务器，不发送
			return;
		}
		String sendEmail = (String)BusnDataDir.getMapKeyValue("costControl.sendEmail").get("send");
		if("0".equals(sendEmail.trim())){
			return;
		}
		boolean isHoliday=holidayService.isHoliday(new Date());
		if(isHoliday){
			return;
		}
		try{
			String yesterday = DateUtil.getPreviousWorkDate();
			String title = yesterday+"部门人员统计及其明细数据";
			String content="<table style='background-color:#E6E6E6;border-right:1px solid #CCCCCC;border-left:1px solid #CCCCCC;border-top:none;border-bottom:2px solid #CCCCCC;'>";
			content+="<tr><td style='text-align:center;font-size:13px;width:9%;'>部门名称</td><td style='text-align:center;font-size:13px;width:9%;'>部门人数</td><td style='text-align:center;font-size:13px;width:9%;'>统计人数</td>";
			content+="<td style='text-align:center;font-size:13px;width:9%;'>开始时间</td><td style='text-align:center;font-size:13px;width:9%;'>结束时间</td>";
			content+="<td style='text-align:center;font-size:13px;width:9%;'>项目经理确认</td><td style='text-align:center;font-size:13px;width:9%;'>部门经理确认</td><td style='text-align:center;font-size:13px;width:9%;'>未确认</td>";
			content+="<td style='text-align:center;font-size:13px;width:9%;'>未登记</td><td style='text-align:center;font-size:13px;width:9%;'>实习生消耗</td><td style='text-align:center;font-size:13px;width:10%;'>非项目比例</td></tr>";
			List<DeptGeneralCost> deptList = costControlService.getDeptCostInfo(yesterday);
			for(int i=0;i<deptList.size();i++){
				DeptGeneralCost cost = deptList.get(i);
				String traineeCost=cost.getTraineeCost()>0?cost.getTraineeCost()+"":"-";
				content+="<tr><td style='text-align:center;font-size:13px;width:9%;'>"+cost.getDeptName()+"</td><td style='text-align:center;font-size:13px;width:9%;'>"+cost.getDeptMembersNo()+"</td><td style='text-align:center;font-size:13px;width:9%;'>"+cost.getDeptMembersNoStatistic()+"</td>";
				content+="<td style='text-align:center;font-size:13px;width:9%;'>"+cost.getStatisticDate()+"</td><td style='text-align:center;font-size:13px;width:9%;'>"+cost.getStatisticDate()+"</td>";
				content+="<td style='text-align:center;font-size:13px;width:9%;'>"+cost.getProjectManagerConfirm()+"</td><td style='text-align:center;font-size:13px;width:9%;'>"+cost.getDeptManagerConfirm()+"</td><td style='text-align:center;font-size:13px;width:9%;'>"+cost.getNotConfirm();
				content+="</td><td style='text-align:center;font-size:13px;width:9%;'>"+cost.getNotRegist()+"</td><td style='text-align:center;font-size:13px;width:9%;'>"+traineeCost+"</td><td style='text-align:center;font-size:13px;width:10%;'>"+cost.getNotProjectPercent()+"</td></tr>";
			}
			content+="</table>";
			content+="<br/><br/><font style='text-align:center;font-size:13px;width:10%;'>部门明细：</font><br/>";
			content+="<table style='background-color:#E6E6E6;border-right:1px solid #CCCCCC;border-left:1px solid #CCCCCC;border-top:none;border-bottom:2px solid #CCCCCC;'>";
			content+="<tr><td style='text-align:center;font-size:13px;width:10%;'>姓名</td><td style='text-align:center;font-size:13px;width:10%;'>所属部门</td><td style='text-align:center;font-size:13px;width:10%;'>开始时间</td><td style='text-align:center;font-size:13px;width:10%;'>结束时间</td>";
			content+="<td style='text-align:center;font-size:13px;width:10%;'>项目经理确认</td><td style='text-align:center;font-size:13px;width:10%;'>部门经理确认</td><td style='text-align:center;font-size:13px;width:10%;'>未确认</td>";
			content+="<td style='text-align:center;font-size:13px;width:10%;'>未登记</td><td style='text-align:center;font-size:13px;width:10%;'>实习生消耗</td><td style='text-align:center;font-size:13px;width:10%;'>非项目比例</td></tr>";
			List<DeptDetailCost> deptUserList = costControlService.getDeptUserCostInfo(yesterday);
			for(int i=0;i<deptUserList.size();i++){
				DeptDetailCost cost = deptUserList.get(i);
				String traineeCost=cost.getTraineeCost()>0?cost.getTraineeCost()+"":"-";
				content+="<tr><td style='text-align:center;font-size:13px;width:10%;'>"+cost.getUserName()+"</td><td style='text-align:center;font-size:13px;width:10%;'>"+cost.getDeptName()+"</td><td style='text-align:center;font-size:13px;width:10%;'>"+cost.getStatisticDate()+"</td><td style='text-align:center;font-size:13px;width:10%;'>"+cost.getStatisticDate()+"</td>";
				content+="<td style='text-align:center;font-size:13px;width:10%;'>"+cost.getProjectManagerConfirm()+"</td><td style='text-align:center;font-size:13px;width:10%;'>"+cost.getDeptManagerConfrim()+"</td><td style='text-align:center;font-size:13px;width:10%;'>"+cost.getNotConfirm();
				content+="</td><td style='text-align:center;font-size:13px;width:10%;'>"+cost.getNotRegist()+"</td><td style='text-align:center;font-size:13px;width:10%;'>"+traineeCost+"</td><td style='text-align:center;font-size:13px;width:10%;'>"+cost.getNotProjectPercent()+"</td></tr>";
			}
			content+="</table>";
			//String deptcost = Configure.getProperty("deptcost");
			String deptcost = (String)BusnDataDir.getMapKeyValue("costControl.sendEmailUserId").get("deptdetailuser");
			if("".equals(deptcost.trim())){
				return;
			}
			//指定发送邮件的用户
			List<String> sendPersonList = new ArrayList<String>();
			List<String> getUseridList=userStrToList(deptcost);
			sendPersonList.addAll(getUseridList);
			if(!((deptList==null || deptList.size()==0) && (deptList==null || deptList.size()==0))){
				//发送并保存发送结果
				sendNoConfirmInfoToUser(sendPersonList,title,content);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 给指定用户发送邮件
	 * @param sendPersonList
	 * @param title
	 * @param content
	 */
	private void sendNoConfirmInfoToUser(List<String> sendPersonList,String title, String content) {
		TemplateEmailService emailService=new TemplateEmailService();
		//发送邮件
		for(int i=0;i<sendPersonList.size();i++){
			String userid = sendPersonList.get(i);
			//先判断用户是否存在，如果存在则判断用户是否离职；否则不做判断。例如：pm@grgbanking.com不做处理
			if(staffInfoService.isExitStaffByUserid(userid.split("@")[0])){
				if(!staffInfoService.flagUserisLeave(userid.split("@")[0])){
					continue;
				}
			}
			//获取用户email
			String email="";
			if(userid.indexOf("@")!=-1){
				email=userid;
			}else{
				email=staffInfoService.getEmailForUserid(userid);
			}
			//填充模版中的指定字段
			Map<String,String> templateTagMap=new HashMap<String,String>();
			String currentDay = DateUtil.getDateString(new Date());
			templateTagMap.put("logdate",currentDay);
			templateTagMap.put("warnInfo",content);
			String sendContent =emailService.sendTemplateMail(email, title, "sendDetpCostAndDeptUserCost.ftl", templateTagMap);
			//保存发送记录
			if(!"0".equals(sendContent)){
				WarnInfo warn=new WarnInfo();
				warn.setToUserId(userid);
				warn.setToUserName(email);
				warn.setWarnWay("email");
				warn.setWarnType("remind");
				warn.setWarnTime(Calendar.getInstance().getTime());
				warn.setWarnContent(sendContent);
				warnInfoService.save(warn);
			}
		}
	}
	
	/**
	 * 将字符串转换为list
	 * @param str
	 * @return
	 */
	public List<String> userStrToList(String str) {
		List<String> list=new ArrayList();
		String[] strs = str.split(",");
		for(int i=0;i<strs.length;i++){
			if(!"".equals(strs[i])){
				list.add(strs[i].trim());
			}
		}
		return list;
	}
}
