package cn.grgbanking.feeltm.costControl.action;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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

import cn.grgbanking.feeltm.costControl.bean.ProjectProfitBean;
import cn.grgbanking.feeltm.costControl.services.CostControlService;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;

import com.opensymphony.xwork2.ActionContext;

public class ProjectProfitAction extends BaseAction{
	@Autowired
	private CostControlService costControlService;
	
	/** 查询部门基础信息
	 * @return
	 * @throws Exception
	 */
	public String list() throws Exception {

		try {
			UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
			String from = request.getParameter("from");
			int pageNum = 1;
			int pageSize = 20;
			String queryStartTime=null;
			String queryEndTime=null;
			String queryProject=null;
			if (request.getParameter("pageNum") != null && request.getParameter("pageNum").length() > 0)
				pageNum = Integer.parseInt(request.getParameter("pageNum"));
			if (request.getParameter("pageSize") != null && request.getParameter("pageSize").length() > 0)
				pageSize = Integer.parseInt(request.getParameter("pageSize"));
			if (request.getParameter("queryStartTime") != null && request.getParameter("queryStartTime").length() > 0)
				queryStartTime = request.getParameter("queryStartTime");
			if (request.getParameter("queryEndTime") != null && request.getParameter("queryEndTime").length() > 0)
				queryEndTime = request.getParameter("queryEndTime");
			if (request.getParameter("queryDept") != null && request.getParameter("queryDept").length() > 0)
				queryProject = request.getParameter("queryDept");
			
			if(from==null){
				Calendar cal=Calendar.getInstance();
				int year=cal.get(Calendar.YEAR);
				int month=cal.get(Calendar.MONTH)+1;
				queryStartTime=year+"-"+(month<10?("0"+month):month);
				queryEndTime=year+"-"+(month<10?("0"+month):month);
				request.setAttribute("queryStartTime", queryStartTime);
				request.setAttribute("queryEndTime", queryEndTime);
			}
			
			
			Page page = costControlService.getProjectProfitPage(queryProject,queryStartTime,queryEndTime,pageNum,pageSize);
			request.setAttribute("currPage", page);
			List<Object> list = (List<Object>)page.getQueryResult();
			
			if (from != null && from.equals("refresh")) {
				
				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf(page.getPageCount()));
				map.put("recordCount", String.valueOf(page.getRecordCount()));
				//公司的json转换
				JSONUtil jsonUtil = new JSONUtil("cn.grgbanking.feeltm.costControl.bean.ProjectProfitBean");
				JSONArray jsonObj = jsonUtil.toJSON(list, map);

				JSONObject input = new JSONObject();
				input.put("pageCount", String.valueOf(page.getPageCount()));
				input.put("recordCount", String.valueOf(page.getRecordCount()));
				input.put("jsonObj", jsonObj);								
				PrintWriter out = response.getWriter();
				out.print(input);
				return null;
			} else {
				ActionContext.getContext().put("profitlist", list);
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
		String queryProject=null;
		if (request.getParameter("queryStartTime") != null && request.getParameter("queryStartTime").length() > 0)
			queryStartTime = request.getParameter("queryStartTime");
		if (request.getParameter("queryEndTime") != null && request.getParameter("queryEndTime").length() > 0)
			queryEndTime = request.getParameter("queryEndTime");
		if (request.getParameter("queryDept") != null && request.getParameter("queryDept").length() > 0)
			queryProject = request.getParameter("queryDept");
		
		//获取数据列表
		List profitList = costControlService.getProjectProfitList(queryProject,queryStartTime,queryEndTime);
		//文件名
		String filename = getWorkSheetName(queryProject,queryStartTime,queryEndTime);//设置文件名
		
		//执行导出操作
		doExport(profitList,filename);
		
		
		MsgBox msgBox = new MsgBox(request, getText("add.ok"));
		msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		this.addActionMessage(getText("add.ok"));
		return "msgBox";
	}         
	
	private void doExport(List profitList, String filename) throws IOException {
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
		wb = this.setExcelValue(wb, 0, "项目名称", 0, (short)0, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "统计月份", 0, (short)1, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "人日收入", 0,(short) 2, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "人日成本", 0, (short)3, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "人日收益",0, (short)4, cellNameStyle);
		
		
		
		/**	** ** *循环数据，并将数据列入Excel表中* ** ** **/
		int sumRow=0;
		for(int i=0;i<profitList.size();i++){
			ProjectProfitBean bean=(ProjectProfitBean)profitList.get(i);
			++sumRow;
			HSSFRow row = sheet.createRow(sumRow);
			row.setHeight((short)256);//行高
			HSSFCell cell = row.createCell((short)0);
			
			/*项目名称*/
			cell = sheet.getRow(sumRow).createCell((short)0);
			setBorderStyle(defaultCenterStyle);
			cell.setCellStyle(defaultCenterStyle);
			cell.setCellValue(bean.getProjectName());
			sheet.setColumnWidth((short)0, (short) (20*512));
			
			/*统计月份*/
			cell = row.createCell((short)1);
			setBorderStyle(defaultCenterStyle);
			cell.setCellStyle(defaultCenterStyle);
			cell.setCellValue(bean.getMonth());
			sheet.setColumnWidth((short)1, (short) (12*512));
			
			/*收入*/
			cell = row.createCell((short)2);
			setBorderStyle(defaultCenterStyle);
			cell.setCellStyle(defaultCenterStyle);
			cell.setCellValue(bean.getIncome());
			sheet.setColumnWidth((short)2, (short) (12*512));
			
			/*成本*/
			cell = row.createCell((short)3);
			setBorderStyle(defaultCenterStyle);
			cell.setCellStyle(defaultCenterStyle);
			cell.setCellValue(bean.getCost());
			sheet.setColumnWidth((short)3, (short) (12*512));
			
			/*收益*/
			cell = row.createCell((short)4);
			setBorderStyle(defaultCenterStyle);
			cell.setCellStyle(defaultCenterStyle);
			cell.setCellValue(bean.getIncome()-bean.getCost());
			sheet.setColumnWidth((short)4, (short) (12*512));
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
	 * @param queryProject
	 * @param queryStartTime
	 * @param queryEndTime
	 * @return
	 */
	private String getWorkSheetName(String queryProject, String queryStartTime,String queryEndTime) {
		try{
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
			String name="";
			if(StringUtils.isNotBlank(queryProject)){
				name+=queryProject+" ";
			}
			if(StringUtils.isNotBlank(queryStartTime)&&StringUtils.isNotBlank(queryEndTime)){
				Date st=sdf.parse(queryStartTime);
				Date end=sdf.parse(queryEndTime);
				name+=new SimpleDateFormat("yyyy年MM月").format(st)+"-"+new SimpleDateFormat("yyyy年MM月").format(end);
			}
			name+=" 人日收益统计";
			return name;
		}catch(Exception e){
			e.printStackTrace();
		}
		return "项目人日收益统计";
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
}
