package cn.grgbanking.feeltm.attendance.webapp;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import cn.grgbanking.feeltm.attendance.domain.WorkSummaryList;
import cn.grgbanking.feeltm.attendance.service.AttendanceAnalysisService;
import cn.grgbanking.feeltm.attendance.service.WorkSummaryService;
import cn.grgbanking.feeltm.leave.service.LeaveRecordService;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.overtime.service.OvertimeRecordService;
import cn.grgbanking.feeltm.trip.service.TripRecordService;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;

import com.opensymphony.xwork2.ActionContext;

public class AttendanceAction extends BaseAction{
	@Autowired
	private AttendanceAnalysisService attendanceAnalysisService;
	@Autowired
	private TripRecordService tripRecordService;
	@Autowired
	private LeaveRecordService leaveRecordService;
	@Autowired
	private OvertimeRecordService overtimeRecordService;
	@Autowired
	private WorkSummaryService workSummaryService;
	
	public String queryWorkSummary() throws Exception {
		try {
			String from = request.getParameter("from");
			String startdate = request.getParameter("startdate");
			String enddate = request.getParameter("enddate");
			
			int pageNum = 1;
			int pageSize = 20;
			if (request.getParameter("pageNum") != null && request.getParameter("pageNum").length() > 0)
				pageNum = Integer.parseInt(request.getParameter("pageNum"));
			if (request.getParameter("pageSize") != null && request.getParameter("pageSize").length() > 0)
				pageSize = Integer.parseInt(request.getParameter("pageSize"));
			Page page = workSummaryService.getPage(startdate,enddate, pageNum, pageSize);
			request.setAttribute("currPage", page);
			List<Object> list = (List<Object>)page.getQueryResult();
			if (from != null && from.equals("refresh")) {
				for (int i = 0; i < list.size(); i++) {
					WorkSummaryList worklist=(WorkSummaryList)list.get(i);
				}
				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf(page.getPageCount()));
				map.put("recordCount", String.valueOf(page.getRecordCount()));
				JSONUtil jsonUtil = new JSONUtil("cn.grgbanking.feeltm.attendance.domain.WorkSummaryList");
				JSONArray jsonObj = jsonUtil.toJSON(list, map);
				JSONObject input = new JSONObject();
				input.put("pageCount", String.valueOf(page.getPageCount()));
				input.put("recordCount", String.valueOf(page.getRecordCount()));
				input.put("jsonObj", jsonObj);								
				PrintWriter out = response.getWriter();
				out.print(input);
				return null;
			} else {
				ActionContext.getContext().put("overtimeList", list);
				return "worksummary";
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "worksummary";
	}
	/**
	 * 考勤统计
	 * @return
	 * @throws Exception
	 */
	public String querySummary() throws Exception {
		try {
			String from = request.getParameter("from");
			String startdate = request.getParameter("startdate");
			String enddate = request.getParameter("enddate");
			if(startdate==null){
				startdate="2014-07-01";
			}
			if(enddate==null){
				enddate="2014-07-31";
			}
			int pageNum = 1;
			int pageSize = 35;
			if (request.getParameter("pageNum") != null && request.getParameter("pageNum").length() > 0)
				pageNum = Integer.parseInt(request.getParameter("pageNum"));
			if (request.getParameter("pageSize") != null && request.getParameter("pageSize").length() > 0)
				pageSize = Integer.parseInt(request.getParameter("pageSize"));
			List list = attendanceAnalysisService.getAttendanceOut(startdate,enddate);
			Page page = new Page();
			page.setQueryResult(list);
			if(list!=null && list.size()>0){
				page.setPageCount(1);
			}else{
				page.setPageCount(0);
			}
			page.setCurrentPageNo(1);
			page.setPageSize(25);
			page.setRecordCount(list.size());
			request.setAttribute("currPage", page);
			if (from != null && from.equals("refresh")) {
				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf(page.getPageCount()));
				map.put("recordCount", String.valueOf(page.getRecordCount()));
				JSONUtil jsonUtil = new JSONUtil("cn.grgbanking.feeltm.attendance.domain.AttendanceListOut");
				JSONArray jsonObj = jsonUtil.toJSON(list, map);
				JSONObject input = new JSONObject();
				input.put("pageCount", String.valueOf(page.getPageCount()));
				input.put("recordCount", String.valueOf(page.getRecordCount()));
				input.put("jsonObj", jsonObj);
				PrintWriter out = response.getWriter();
				out.print(input);
				return null;
			}  else {
				ActionContext.getContext().put("attendanceList", list);
				return "input";
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "input";
	}
	/**
	 * 公司月底汇总
	 * @return
	 * @throws Exception
	 */
	public String query() throws Exception {
		try {
			String from = request.getParameter("from");
			String startdate = request.getParameter("startdate");
			String enddate = request.getParameter("enddate");
			if(startdate==null){
				startdate="2014-07-01";
			}
			if(enddate==null){
				enddate="2014-07-31";
			}
			int pageNum = 1;
			int pageSize = 35;
			if (request.getParameter("pageNum") != null && request.getParameter("pageNum").length() > 0)
				pageNum = Integer.parseInt(request.getParameter("pageNum"));
			if (request.getParameter("pageSize") != null && request.getParameter("pageSize").length() > 0)
				pageSize = Integer.parseInt(request.getParameter("pageSize"));
			List list = attendanceAnalysisService.getAttendanceInner(startdate,enddate);
			Page page = new Page();
			page.setQueryResult(list);
			if(list!=null && list.size()>0){
				page.setPageCount(1);
			}else{
				page.setPageCount(0);
			}
			page.setCurrentPageNo(1);
			page.setPageSize(25);
			page.setRecordCount(list.size());
			request.setAttribute("currPage", page);
			if (from != null && from.equals("refresh")) {
				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf(page.getPageCount()));
				map.put("recordCount", String.valueOf(page.getRecordCount()));
				JSONUtil jsonUtil = new JSONUtil("cn.grgbanking.feeltm.attendance.domain.AttendanceListInner");
				JSONArray jsonObj = jsonUtil.toJSON(list, map);
				JSONObject input = new JSONObject();
				input.put("pageCount", String.valueOf(page.getPageCount()));
				input.put("recordCount", String.valueOf(page.getRecordCount()));
				input.put("jsonObj", jsonObj);
				PrintWriter out = response.getWriter();
				out.print(input);
				return null;
			} else {
				ActionContext.getContext().put("attendanceList", list);
				return "allinput";
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "allinput";
	}
	
	public String expenseQuery() throws Exception {
		try {
			String from = request.getParameter("from");
			String username = request.getParameter("username")==null?"":request.getParameter("username");
			String groupname = request.getParameter("groupName")==null?"":request.getParameter("groupName");
			String deptname = request.getParameter("deptName")==null?"":request.getParameter("deptName");
			if (username != null) {
				if (username.equals("全选"))
					username = "";
			}
			if (deptname != null) {
				if (deptname.equals("全选"))
					deptname = "";
			}
			if(!"".equals(groupname)){
				if(groupname.equals("---请选择项目组---")){
					groupname="";
				}
			}
			int pageNum = 1;
			int pageSize = 35;
			if (request.getParameter("pageNum") != null && request.getParameter("pageNum").length() > 0)
				pageNum = Integer.parseInt(request.getParameter("pageNum"));
			if (request.getParameter("pageSize") != null && request.getParameter("pageSize").length() > 0)
				pageSize = Integer.parseInt(request.getParameter("pageSize"));
			List list = attendanceAnalysisService.getExpenseData("2014-06-01","2014-06-30");
			Page page = new Page();
			page.setQueryResult(list);
			page.setPageCount(1);
			page.setCurrentPageNo(1);
			page.setPageSize(25);
			page.setRecordCount(list.size());
			request.setAttribute("currPage", page);
			if (from != null && from.equals("refresh")) {
				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf(page.getPageCount()));
				map.put("recordCount", String.valueOf(page.getRecordCount()));
				JSONUtil jsonUtil = new JSONUtil("cn.grgbanking.feeltm.attendance.domain.ExpenseList");
				JSONArray jsonObj = jsonUtil.toJSON(list, map);
				JSONObject input = new JSONObject();
				input.put("pageCount", String.valueOf(page.getPageCount()));
				input.put("recordCount", String.valueOf(page.getRecordCount()));
				input.put("jsonObj", jsonObj);
				PrintWriter out = response.getWriter();
				out.print(input);
				return null;
			} else {
				ActionContext.getContext().put("attendanceList", list);
				return "expenseInput";
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "expenseInput";
	}
	
	public String exportAttendanceData() throws Exception{
		String sdate = request.getParameter("startdate");
		String edate = request.getParameter("enddate");
		
		List list = attendanceAnalysisService.exportOutAttendanceData(sdate,edate);
		String filename = "广发考勤数据";//设置文件名
		OutputStream  os = response.getOutputStream();// 取得输出流 
		response.reset();// 清空输出流  
        response.setHeader("Content-disposition","attachment;filename="+new String(filename.getBytes("gb2312"),"ISO8859-1")+".xls");// 设定输出文件头  
        response.setContentType("application/vnd.ms-excel");
        // 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("sheet1");
		
		/**	** ** *设置字体* ** ** **/
		HSSFFont defaultFont = wb.createFont(); 
		defaultFont.setFontName("宋体");
		defaultFont.setFontHeightInPoints((short)12);
		
		HSSFFont headfont = wb.createFont();
		headfont.setFontName("黑体");
		headfont.setFontHeightInPoints((short)30);// 字体大小   
		headfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		
		HSSFFont cellfont = wb.createFont();
		headfont.setFontName("黑体");
		headfont.setFontHeightInPoints((short)25);// 字体大小   
		headfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		
		HSSFFont allFont = wb.createFont();
		allFont.setFontName("宋体");
		//allFont.setColor(HSSFColor.RED.index);
		allFont.setFontHeightInPoints((short)10);// 字体大小   
//		allFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		
		/**	** ** *设置样式* ** ** **/
		HSSFCellStyle defaultStyle = this.getCellStyle(wb,true, defaultFont,(short)-2, (short)-2, HSSFCellStyle.ALIGN_CENTER);
		HSSFCellStyle tileStyle = this.getCellStyle(wb, false,headfont, (short)-2, (short)-2, HSSFCellStyle.ALIGN_CENTER);
		HSSFCellStyle dateStyle = this.getCellStyle(wb, false,defaultFont,(short)-2, (short)-2, HSSFCellStyle.ALIGN_CENTER);
		HSSFCellStyle upDateStyle = this.getCellStyle(wb, true,defaultFont,(short)-2, (short)-2, HSSFCellStyle.ALIGN_CENTER);
		HSSFCellStyle cellNameStyle = this.getCellStyle(wb,true,cellfont, HSSFColor.GREY_25_PERCENT.index, HSSFColor.LIME.index, HSSFCellStyle.ALIGN_CENTER); 
		HSSFCellStyle countStyle = this.getCellStyle(wb,false,  cellfont, (short)-2, (short)-2, HSSFCellStyle.ALIGN_RIGHT);
		HSSFCellStyle allStyle = this.getCellStyle(wb, false, allFont, (short)-2, (short)-2, HSSFCellStyle.ALIGN_CENTER);
		
		/**	** ** *设置文本格式* ** ** **/
		HSSFDataFormat format = wb.createDataFormat();
		defaultStyle.setDataFormat(format.getFormat("@"));
		dateStyle.setDataFormat(format.getFormat("m/d/yy h:mm"));
		upDateStyle.setDataFormat(format.getFormat("m/d/yy h:mm"));
		
		
		/**		创建标题		*/
		HSSFRow titleRow = sheet.createRow((int) 0);
		titleRow.setHeight((short)(2*512));
		wb = this.setExcelValue(wb, 0, "广发现场员工", 0, (short)0, tileStyle);
		sheet.addMergedRegion(new Region(0, (short)0, 0, (short)18));
		
		HSSFRow titleRow1 = sheet.createRow((int)1);
		titleRow1.setHeight((short)(2*512));
		wb = this.setExcelValue(wb, 0, "6月第一周考勤报表", 1, (short)0, tileStyle);
		sheet.addMergedRegion(new Region(1, (short)0, 1, (short)18));
		
		HSSFRow titleRow2 = sheet.createRow((int)2);
		titleRow2.setHeight((short)(1*512));
		wb = this.setExcelValue(wb, 0, "运通信息", 2, (short)0, defaultStyle);
		sheet.addMergedRegion(new Region(2, (short)0, 2, (short)18));
		
		/** 	创建表头 	*/
		HSSFRow titleRow3 = sheet.createRow((int)3);
		titleRow3.setHeight((short)(1*512));
//		wb = this.setExcelValue(wb, 0, "序号", 3, (short)0, cellNameStyle);
//		sheet.addMergedRegion(new Region(3, (short)0, 4, (short)0));
//		wb = this.setExcelValue(wb, 0, "姓名", 3, (short)1, cellNameStyle);
//		sheet.addMergedRegion(new Region(3, (short)1, 4, (short)1));
//		wb = this.setExcelValue(wb, 3, "考勤数据", 3, (short)2, cellNameStyle);
//		sheet.addMergedRegion(new Region(3, (short)2, 3, (short)18));
		wb = this.setExcelValue(wb, 0, "考勤数据", 3, (short)0, defaultStyle);
		sheet.addMergedRegion(new Region(3, (short)0, 3, (short)18));
		
		/**		创建列名		*/
		HSSFRow titleRow4 = sheet.createRow((int)4);
		titleRow4.setHeight((short)(1*512));
		wb = this.setExcelValue(wb, 0, "序号", 4, (short)0, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "姓名", 4, (short)1, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "出勤(天)", 4, (short)2, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "早上迟到(分)", 4, (short)3, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "中午迟到(分)", 4, (short)4, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "说明(打卡时间)", 4,(short) 5, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "早退(小时)", 4, (short)6, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "说明(打卡时间)", 4, (short)7, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "缺勤(天)", 4, (short)8, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "说明(打卡时间)", 4, (short)9, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "出勤异常次数", 4, (short)10, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "事假(天)", 4, (short)11, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "说明", 4, (short)12, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "病假(天)", 4, (short)13, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "说明", 4, (short)14, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "年假(天)", 4, (short)15, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "说明", 4, (short)16, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "其它假(天)", 4, (short)17, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "说明", 4, (short)18, cellNameStyle);
		/**	** ** *循环数据，并将数据列入Excel表中* ** ** **/
		int i=0;
		for(;i<list.size();i++){
			Object[] record = (Object[])list.get(i);
			HSSFRow row = sheet.createRow(i+5);
			row.setHeight((short)512);
			HSSFCell cell = row.createCell((short)0);
			
			int rownum = i+1;
			String username = record[0]==null?"":record[0].toString();
			String days = record[1]==null?"":record[1].toString();
			String morning = record[2]==null?"":record[2].toString();
			String str1 = "0";
			String str2 = "0";
			if(morning.split("\\.").length>1){
				str1 = morning.split("\\.")[0];
				str2=morning.split("\\.")[1];
				morning = str1 + "." + str2;
				double num = Double.parseDouble(morning);
				morning = (int)num+"";
			}
			String after = record[3]==null?"":record[3].toString();
			String desc1 = record[4]==null?"":record[4].toString();
			String overleave = record[5]==null?"":record[5].toString();
			String desc2 = record[6]==null?"":record[6].toString();
			String nowork = record[7]==null?"":record[7].toString();
			String desc3 = record[8]==null?"":record[8].toString();
			String iscatch = record[9]==null?"":record[9].toString();
			String sjia = record[10]==null?"":record[10].toString();
			String sjiadesc = record[11]==null?"":record[11].toString();
			String bjia = record[12]==null?"":record[12].toString();
			String bjiadesc = record[13]==null?"":record[13].toString();
			String njia = record[14]==null?"":record[14].toString();
			String njiadesc = record[15]==null?"":record[15].toString();
			String otherjia = record[16]==null?"":record[16].toString();
			String otherdesc = record[17]==null?"":record[17].toString();
			
			cell = row.createCell((short)0);
			cell.setCellStyle(allStyle);
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellValue(rownum);
			
			cell = row.createCell((short)1);
			cell.setCellStyle(allStyle);
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellValue(username);
			
			cell = row.createCell((short)2);
			cell.setCellStyle(allStyle);
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellValue(days);
			
			cell = row.createCell((short)3);
			cell.setCellStyle(allStyle);
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			if("0".equals(morning)){
				morning="";
			}
			cell.setCellValue(morning);
			
			cell = row.createCell((short)4);
			cell.setCellStyle(allStyle);
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellValue(after);
			sheet.setColumnWidth((short)4, (short) (8*512));
			
			cell = row.createCell((short)5);
			cell.setCellStyle(allStyle);
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			if(desc1.split("@").length>1){
				desc1 = desc1.replaceAll("@", "\n");
			}
			cell.setCellValue(desc1);
			sheet.setColumnWidth((short)5, (short) (8*512));
			
			cell = row.createCell((short)6);
			cell.setCellStyle(allStyle);
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellValue(overleave);
			
			cell = row.createCell((short)7);
			cell.setCellStyle(allStyle);
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellValue(desc2);
			sheet.setColumnWidth((short)7, (short) (8*512));
			
			cell = row.createCell((short)8);
			cell.setCellStyle(allStyle);
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellValue(nowork);
			
			cell = row.createCell((short)9);
			cell.setCellStyle(allStyle);
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellValue(desc3);
			sheet.setColumnWidth((short)9, (short) (8*512));
			
			cell = row.createCell((short)10);
			cell.setCellStyle(allStyle);
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellValue(iscatch);
			
			cell = row.createCell((short)11);
			cell.setCellStyle(allStyle);
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellValue(sjia.replaceAll("@","\n").replaceAll("\\|", "\n"));
			sheet.setColumnWidth((short)11, (short)(8*512));
			
			cell = row.createCell((short)12);
			cell.setCellStyle(allStyle);
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellValue(sjiadesc.replaceAll("@", "\n"));
			
			cell = row.createCell((short)13);
			cell.setCellStyle(allStyle);
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellValue(bjia.replaceAll("@","\n").replaceAll("\\|", "\n"));
			sheet.setColumnWidth((short)13, (short) (8*512));
			
			cell = row.createCell((short)14);
			cell.setCellStyle(allStyle);
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellValue(bjiadesc.replaceAll("@", "\n"));
			
			cell = row.createCell((short)15);
			cell.setCellStyle(allStyle);
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellValue(njia.replaceAll("@","\n").replaceAll("\\|", "\n"));
			sheet.setColumnWidth((short)15, (short) (8*512));
			
			cell = row.createCell((short)16);
			cell.setCellStyle(allStyle);
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellValue(njiadesc.replaceAll("@", "\n"));
			
			cell = row.createCell((short)17);
			cell.setCellStyle(allStyle);
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellValue(otherjia.replaceAll("@","\n").replaceAll("\\|", "\n"));
			sheet.setColumnWidth((short)17, (short) (8*512));
			
			cell = row.createCell((short)18);
			cell.setCellStyle(allStyle);
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellValue(otherdesc.replaceAll("@", "\n"));
			
		}
		
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet2 = wb.createSheet("sheet2");
		HSSFRow sheet2_title = sheet2.createRow((int)0);
		sheet2_title.setHeight((short)(1*512));
		wb = this.setExcelValue(wb, 1, "序号", 0, (short)0, cellNameStyle);
		wb = this.setExcelValue(wb, 1, "姓名", 0, (short)1, cellNameStyle);
		wb = this.setExcelValue(wb, 1, "日期", 0, (short)2, cellNameStyle);
		wb = this.setExcelValue(wb, 1, "上班", 0, (short)3, cellNameStyle);
		wb = this.setExcelValue(wb, 1, "上班打卡", 0, (short)4, cellNameStyle);
		wb = this.setExcelValue(wb, 1, "是否迟到", 0,(short) 5, cellNameStyle);
		wb = this.setExcelValue(wb, 1, "下班", 0, (short)6, cellNameStyle);
		wb = this.setExcelValue(wb, 1, "下班打卡", 0, (short)7, cellNameStyle);
		wb = this.setExcelValue(wb, 1, "是否早退", 0, (short)8, cellNameStyle);
		List list1 = attendanceAnalysisService.exportAttendanceDetailTableData(sdate,edate);
		int j=0;
		/**	** ** *循环数据，并将数据列入Excel表中* ** ** **/
		for(;j<list1.size();j++){
			Object[] record = (Object[])list1.get(j);
			HSSFRow row = sheet2.createRow(j+1);
			row.setHeight((short)512);
			HSSFCell cell = row.createCell((short)0);

			cell = row.createCell((short)0);
			cell.setCellStyle(allStyle);
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellValue(j+1);
			
			cell = row.createCell((short)1);
			cell.setCellStyle(allStyle);
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			if(record[0]==null){
				cell.setCellValue("");
			}else{
				cell.setCellValue(record[0].toString());
			}
			
			cell = row.createCell((short)2);
			cell.setCellStyle(allStyle);
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			if(record[1]==null){
				cell.setCellValue("");
			}else{
				cell.setCellValue(record[1].toString());
			}
			sheet2.setColumnWidth((short)2, (short) (8*512));
			
			cell = row.createCell((short)3);
			cell.setCellStyle(allStyle);
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			if(record[2]==null){
				cell.setCellValue("");
			}else{
				cell.setCellValue(record[2].toString());
			}
			
			cell = row.createCell((short)4);
			cell.setCellStyle(allStyle);
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			if(record[3]==null){
				cell.setCellValue("");
			}else{
				cell.setCellValue(record[3].toString());
			}
			
			cell = row.createCell((short)5);
			cell.setCellStyle(allStyle);
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			String str = "";
			if(record[4]==null){
				cell.setCellValue("");
			}else{
				cell.setCellValue(record[4].toString());
				str = record[4].toString();
			}
			
			cell = row.createCell((short)6);
			cell.setCellStyle(allStyle);
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			if(record[5]==null){
				cell.setCellValue("");
			}else{
				if(!"".equals(str)){
					cell.setCellValue("");
				}else{
					cell.setCellValue(record[5].toString());
				}
			}
			
			cell = row.createCell((short)7);
			cell.setCellStyle(allStyle);
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			if(record[6]==null){
				cell.setCellValue("");
			}else{
				if(!"".equals(str)){
					cell.setCellValue("");
				}else{
					cell.setCellValue(record[6].toString());
				}
			}
			
			cell = row.createCell((short)8);
			cell.setCellStyle(allStyle);
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			if(record[7]==null){
				cell.setCellValue("");
			}else{
				if(!"".equals(str)){
					cell.setCellValue("");
				}else{
					cell.setCellValue(record[7].toString());
				}
			}
			
		}
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet3 = wb.createSheet("sheet3");
		HSSFRow sheet3_title = sheet3.createRow((int)0);
		sheet3_title.setHeight((short)(1*512));
		wb = this.setExcelValue(wb, 2, "序号", 0, (short)0, cellNameStyle);
		wb = this.setExcelValue(wb, 2, "姓名", 0, (short)1, cellNameStyle);
		wb = this.setExcelValue(wb, 2, "考勤时间", 0, (short)2, cellNameStyle);
		List list2 = attendanceAnalysisService.exportAttendanceCatchData(sdate,edate);
		int k=0;
		/**	** ** *循环数据，并将数据列入Excel表中* ** ** **/
		for(;k<list2.size();k++){
			Object[] record = (Object[])list2.get(k);
			HSSFRow row = sheet3.createRow(k+1);
			row.setHeight((short)512);
			HSSFCell cell = row.createCell((short)0);

			cell = row.createCell((short)0);
			cell.setCellStyle(allStyle);
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellValue(k+1);
			
			cell = row.createCell((short)1);
			cell.setCellStyle(allStyle);
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			if(record[0]==null){
				cell.setCellValue("");
			}else{
				cell.setCellValue(record[0].toString());
			}
			
			cell = row.createCell((short)2);
			cell.setCellStyle(allStyle);
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			if(record[1]==null){
				cell.setCellValue("");
			}else{
				cell.setCellValue(record[1].toString());
			}
			sheet3.setColumnWidth((short)2, (short) (8*512));
			
		}
		wb.write(os);
		os.close();
		//清理刷新缓冲区，将缓存中的数据将数据导出excel
		os.flush();
		//关闭os
		if(os!=null){
			os.close();
		}
		MsgBox msgBox = new MsgBox(request, getText("add.ok"));
		msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		this.addActionMessage(getText("add.ok"));
		return "msgBox";
	}
	
	/**
	 * 设置样式方法，
	 * @param wb
	 * @param hasBorder
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
			style.setBorderBottom(HSSFCellStyle.BORDER_THIN);	
			style.setBottomBorderColor(HSSFColor.BLACK.index);	
			style.setBorderLeft(HSSFCellStyle.BORDER_THIN);		
			style.setLeftBorderColor(HSSFColor.BLACK.index);
			style.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style.setRightBorderColor(HSSFColor.BLACK.index);
			style.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style.setTopBorderColor(HSSFColor.BLACK.index);
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
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellValue(value.toString());
		}else if(value instanceof Date){
			cell.setCellValue((Date)value);
		}else if(value instanceof Double){
			cell.setCellValue((Double)value);
		}else if(value instanceof Calendar){
			cell.setCellValue((Calendar)value);
		}else if(value instanceof Boolean){
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellValue(value.toString().equals("true")?"是":"否");
		}else if(value == null){
			cell.setCellValue(value.toString());
		}else {
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellValue(value.toString());
		}
		if(defaultStyle!=null){
			cell.setCellStyle(defaultStyle);
		}
		return wb;
	}
	
	
	public String exportAllAttendanceData() throws Exception{
		String sdate = request.getParameter("startdate");
		String edate = request.getParameter("enddate");
		List list = attendanceAnalysisService.exportAllAttendanceData(sdate, edate);
		String filename = "公司月底汇总";//设置文件名
		OutputStream  os = response.getOutputStream();// 取得输出流 
		response.reset();// 清空输出流  
        response.setHeader("Content-disposition","attachment;filename="+new String((filename).getBytes("gb2312"),"ISO8859-1")+".xls");// 设定输出文件头  
        response.setContentType("application/vnd.ms-excel");
        // 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("sheet1");
		
		/**	** ** *设置字体* ** ** **/
		HSSFFont defaultFont = wb.createFont(); 
		defaultFont.setFontName("宋体");
		defaultFont.setFontHeightInPoints((short)12);
		
		HSSFFont headfont = wb.createFont();
		headfont.setFontName("黑体");
		headfont.setFontHeightInPoints((short)30);// 字体大小   
		headfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		
		HSSFFont cellfont = wb.createFont();
		headfont.setFontName("黑体");
		headfont.setFontHeightInPoints((short)25);// 字体大小   
		headfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		
		HSSFFont allFont = wb.createFont();
		allFont.setFontName("宋体");
		//allFont.setColor(HSSFColor.RED.index);
		allFont.setFontHeightInPoints((short)10);// 字体大小   
//		allFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		
		/**	** ** *设置样式* ** ** **/
		HSSFCellStyle defaultStyle = this.getCellStyle(wb,true, defaultFont,(short)-2, (short)-2, HSSFCellStyle.ALIGN_CENTER);
		HSSFCellStyle tileStyle = this.getCellStyle(wb, false,headfont, (short)-2, (short)-2, HSSFCellStyle.ALIGN_CENTER);
		HSSFCellStyle dateStyle = this.getCellStyle(wb, false,defaultFont,(short)-2, (short)-2, HSSFCellStyle.ALIGN_CENTER);
		HSSFCellStyle upDateStyle = this.getCellStyle(wb, true,defaultFont,(short)-2, (short)-2, HSSFCellStyle.ALIGN_CENTER);
		HSSFCellStyle cellNameStyle = this.getCellStyle(wb,true,cellfont, HSSFColor.GREY_25_PERCENT.index, HSSFColor.LIME.index, HSSFCellStyle.ALIGN_CENTER); 
		HSSFCellStyle countStyle = this.getCellStyle(wb,false,  cellfont, (short)-2, (short)-2, HSSFCellStyle.ALIGN_RIGHT);
		HSSFCellStyle allStyle = this.getCellStyle(wb, false, allFont, (short)-2, (short)-2, HSSFCellStyle.ALIGN_LEFT);
		
		/**	** ** *设置文本格式* ** ** **/
		HSSFDataFormat format = wb.createDataFormat();
		defaultStyle.setDataFormat(format.getFormat("@"));
		dateStyle.setDataFormat(format.getFormat("m/d/yy h:mm"));
		upDateStyle.setDataFormat(format.getFormat("m/d/yy h:mm"));
		
		
		/**		创建标题		*/
		HSSFRow titleRow = sheet.createRow((int) 0);
		titleRow.setHeight((short)(2*512));
		wb = this.setExcelValue(wb, 0, "广州广电运通金融电子股份有限公司", 0, (short)0, tileStyle);
		sheet.addMergedRegion(new Region(0, (short)0, 0, (short)22));
		
		HSSFRow titleRow1 = sheet.createRow((int)1);
		titleRow1.setHeight((short)(2*512));
		wb = this.setExcelValue(wb, 0, "2014年5月考勤报表", 1, (short)0, tileStyle);
		sheet.addMergedRegion(new Region(1, (short)0, 1, (short)22));
		
		HSSFRow titleRow2 = sheet.createRow((int)2);
		titleRow2.setHeight((short)(1*512));
		wb = this.setExcelValue(wb, 0, "部门：软件外包服务事业部", 2, (short)0, defaultStyle);
		sheet.addMergedRegion(new Region(2, (short)0, 2, (short)22));
		
		/** 	创建表头 	*/
		HSSFRow titleRow3 = sheet.createRow((int)3);
		titleRow3.setHeight((short)(1*512));
//		wb = this.setExcelValue(wb, 0, "序号", 3, (short)0, cellNameStyle);
//		sheet.addMergedRegion(new Region(3, (short)0, 4, (short)0));
//		wb = this.setExcelValue(wb, 0, "姓名", 3, (short)1, cellNameStyle);
//		sheet.addMergedRegion(new Region(3, (short)1, 4, (short)1));
//		wb = this.setExcelValue(wb, 3, "考勤数据", 3, (short)2, cellNameStyle);
//		sheet.addMergedRegion(new Region(3, (short)2, 3, (short)18));
		wb = this.setExcelValue(wb, 0, "考勤数据", 3, (short)0, defaultStyle);
		sheet.addMergedRegion(new Region(3, (short)0, 3, (short)18));
		
		/**		创建列名		*/
		HSSFRow titleRow4 = sheet.createRow((int)4);
		titleRow4.setHeight((short)(1*512));
		wb = this.setExcelValue(wb, 0, "序号", 4, (short)0, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "姓名", 4, (short)1, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "出勤(天)", 4, (short)2, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "迟到(分)", 4, (short)3, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "早退(分)", 4, (short)4, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "事假(天)", 4,(short) 5, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "说明", 4, (short)6, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "病假(天)", 4, (short)7, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "说明", 4, (short)8, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "年假(天)", 4, (short)9, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "说明", 4, (short)10, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "其它假(天)", 4, (short)11, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "说明", 4, (short)12, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "出差(天)", 4, (short)13, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "说明", 4, (short)14, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "上月剩余调休(天)", 4, (short)15, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "出差新增调休（天）", 4, (short)16, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "说明", 4, (short)17, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "加班(天)", 4, (short)18, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "说明", 4, (short)19, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "补休(天)", 4, (short)20, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "说明", 4, (short)21, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "本月剩余调休(天)", 4, (short)22, cellNameStyle);
		/**	** ** *循环数据，并将数据列入Excel表中* ** ** **/
		int i=0;
		for(;i<list.size();i++){
			Object[] record = (Object[])list.get(i);
			HSSFRow row = sheet.createRow(i+5);
			row.setHeight((short)512);
			HSSFCell cell = row.createCell((short)0);
			
			cell = row.createCell((short)0);
			cell.setCellStyle(allStyle);
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellValue(i+1);
			
			cell = row.createCell((short)1);
			cell.setCellStyle(allStyle);
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			if(record[0]==null){
				cell.setCellValue("");
			}else{
				cell.setCellValue(record[0].toString());
			}
			
			cell = row.createCell((short)2);
			cell.setCellStyle(allStyle);
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			if(record[1]==null){
				cell.setCellValue("");
			}else{
				cell.setCellValue(record[1].toString());
			}
			
			cell = row.createCell((short)3);
			cell.setCellStyle(allStyle);
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			if(record[2]==null){
				cell.setCellValue("");
			}else{
				cell.setCellValue(record[2].toString());
			}
			
			cell = row.createCell((short)4);
			cell.setCellStyle(allStyle);
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			if(record[3]==null){
				cell.setCellValue("");
			}else{
				cell.setCellValue(record[3].toString());
			}
			
			cell = row.createCell((short)5);
			cell.setCellStyle(allStyle);
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			if(record[4]==null){
				cell.setCellValue("");
			}else{
				cell.setCellValue("0".equals(record[4].toString())?"":record[4].toString());
			}
			
			cell = row.createCell((short)6);
			cell.setCellStyle(allStyle);
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			if(record[5]==null){
				cell.setCellValue("");
			}else{
				cell.setCellValue(record[5].toString().replaceAll("@", "\n"));
			}
			
			cell = row.createCell((short)7);
			cell.setCellStyle(allStyle);
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			if(record[6]==null){
				cell.setCellValue("");
			}else{
				cell.setCellValue("0".equals(record[6].toString())?"":record[6].toString());
			}
			
			cell = row.createCell((short)8);
			cell.setCellStyle(allStyle);
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			if(record[7]==null){
				cell.setCellValue("");
			}else{
				cell.setCellValue(record[7].toString().replaceAll("@", "\n"));
			}
			
			cell = row.createCell((short)9);
			cell.setCellStyle(allStyle);
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			if(record[8]==null){
				cell.setCellValue("");
			}else{
				cell.setCellValue("0".equals(record[8].toString())?"":record[8].toString());
			}
			
			cell = row.createCell((short)10);
			cell.setCellStyle(allStyle);
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			if(record[9]==null){
				cell.setCellValue("");
			}else{
				cell.setCellValue(record[9].toString().replaceAll("@", "\n").replaceAll("|", "\n"));
			}
			
			cell = row.createCell((short)11);
			cell.setCellStyle(allStyle);
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			if(record[10]==null){
				cell.setCellValue("");
			}else{
				cell.setCellValue("0".equals(record[10].toString())?"":record[10].toString());
			}
			
			cell = row.createCell((short)12);
			cell.setCellStyle(allStyle);
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			if(record[11]==null){
				cell.setCellValue("");
			}else{
				cell.setCellValue(record[11].toString().replaceAll("@", "\n"));
			}
			
			cell = row.createCell((short)13);
			cell.setCellStyle(allStyle);
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			if(record[12]==null){
				cell.setCellValue("");
			}else{
				cell.setCellValue(record[12].toString());
			}
			
			cell = row.createCell((short)14);
			cell.setCellStyle(allStyle);
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			if(record[13]==null){
				cell.setCellValue("");
			}else{
				cell.setCellValue(record[13].toString().replaceAll("@", "\n"));
			}
			
			cell = row.createCell((short)15);
			cell.setCellStyle(allStyle);
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			if(record[14]==null){
				cell.setCellValue("");
			}else{
				cell.setCellValue(record[14].toString());
			}
			
			cell = row.createCell((short)16);
			cell.setCellStyle(allStyle);
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			if(record[15]==null){
				cell.setCellValue("");
			}else{
				cell.setCellValue(record[15].toString());
			}
			
			cell = row.createCell((short)17);
			cell.setCellStyle(allStyle);
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			if(record[16]==null){
				cell.setCellValue("");
			}else{
				cell.setCellValue(record[16].toString().replaceAll("@", "\n"));
			}
			
			cell = row.createCell((short)18);
			cell.setCellStyle(allStyle);
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			if(record[17]==null){
				cell.setCellValue("");
			}else{
				cell.setCellValue("0".equals(record[17].toString())?"":record[17].toString());
			}
			
			cell = row.createCell((short)19);
			cell.setCellStyle(allStyle);
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			if(record[18]==null){
				cell.setCellValue("");
			}else{
				cell.setCellValue(record[18].toString().replaceAll("@", "\n"));
			}
			
			cell = row.createCell((short)20);
			cell.setCellStyle(allStyle);
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			if(record[19]==null){
				cell.setCellValue("");
			}else{
				cell.setCellValue("0".equals(record[19].toString())?"":record[19].toString());
			}
			
			cell = row.createCell((short)21);
			cell.setCellStyle(allStyle);
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			if(record[20]==null){
				cell.setCellValue("");
			}else{
				cell.setCellValue(record[20].toString().replaceAll("@", "\n"));
			}
			
			cell = row.createCell((short)22);
			cell.setCellStyle(allStyle);
//			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			if(record[21]==null){
				cell.setCellValue("");
			}else{
				cell.setCellValue(record[21].toString());
			}
			
		}
		wb.write(os);
		os.close();
		//清理刷新缓冲区，将缓存中的数据将数据导出excel
		os.flush();
		//关闭os
		if(os!=null){
			os.close();
		}
		MsgBox msgBox = new MsgBox(request, getText("add.ok"));
		msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		this.addActionMessage(getText("add.ok"));
		return "msgBox";
	}
	
	/**
	 * 分析广发数据
	 * 按给出的时间段进行分析，如果已经存在给出的时间段，则提醒数据已经分析过
	 * @return
	 */
	public String countLeaveToAttendance(){
		String sdate = request.getParameter("startdate");
		String edate = request.getParameter("enddate");
		//attendanceAnalysisService.execDataForEhr();
		/*attendanceAnalysisService.execMobilecard_Proc();//同步到time_proc
		attendanceAnalysisService.execProcedure();//同步到time_proc
		tripRecordService.getTripRecord();//修改为存储过程
		tripRecordService.execProcedure();//修改为存储过程
		leaveRecordService.getLeaveRecord();
		leaveRecordService.execProcedure();
		overtimeRecordService.getOvertimeRecord();//修改为存储过程
		overtimeRecordService.execProcedure();//修改为存储过程
		attendanceAnalysisService.getSignRecord(sdate, edate);
		attendanceAnalysisService.updateTimeForLeaveData(sdate, edate);
		attendanceAnalysisService.updateStatusForAttendanceTime(sdate, edate);
		attendanceAnalysisService.execAttendanceProc();
		attendanceAnalysisService.updateCardDealStatus(sdate, edate);*/
		
		leaveRecordService.getLeaveRecord();
		leaveRecordService.execProcedure();
		attendanceAnalysisService.execTripProc();
		attendanceAnalysisService.execOvertimeProc();
		attendanceAnalysisService.execTimeAttendanceProc(sdate, edate);
		
		System.out.println("ok");
		MsgBox msgBox = new MsgBox(request,"统计完成");
		msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		return "msgBox";
	}
	
	public static void main(String[] args) {
		AttendanceAction action = new AttendanceAction();
		action.countLeaveToAttendance();
	}
	
	
	public String updateUserRols(){
		String userid=request.getParameter("userid");
		String deferred=request.getParameter("deferred");
		attendanceAnalysisService.updateUserHols(userid, deferred);
		MsgBox msgBox = new MsgBox(request,"成功修改");
		msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		return "msgBox";
	}
}
