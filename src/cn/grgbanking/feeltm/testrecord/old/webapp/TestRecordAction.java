package cn.grgbanking.feeltm.testrecord.old.webapp;

import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.io.FileOutputStream;
import java.util.ArrayList;

import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.ModuleException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.faces.context.FacesContext; 
import javax.servlet.ServletContext; 
import javax.servlet.ServletOutputStream; 

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;

import javax.servlet.ServletException;    
import javax.servlet.http.HttpServlet;    
import javax.servlet.http.HttpServletRequest;    

import jxl.Sheet;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFHeader;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.struts2.ServletActionContext;


import org.json.JSONArray;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionContext;

import cn.grgbanking.feeltm.domain.testsys.KpiPoint;
import cn.grgbanking.feeltm.domain.testsys.TestRecord;
import cn.grgbanking.feeltm.domain.testsys.DepartFinance;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.testrecord.service.TestRecordService;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;


public class TestRecordAction extends BaseAction{
	private static final long serialVersionUID = 1L;
	private String TestRecordMan;
	private String absentPersons;
	private Map<String,String> umap=new LinkedHashMap<String,String>();
	private String upMonth;
	private String StartMonth;
	private String EndMonth;
	private String projectTypeQuery;
	private String buildType;
	private String prjName;
	private String testStatusQuery;
	private String sumbitProcessQuery;
	private String testMan;

	private String fileName;
	
	private String FindBugSumTotal;
	private String TesterSumTotal;
	private String TestTimeSumTotal;
	private String WorkLoadTotal;

	private List<String> unames;
	
	private TestRecord testRecord;
	
	private TestRecordService testRecordService;
	
	public String add(){
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		TestRecordMan=userModel.getUsername();
    	unames=testRecordService.getAllNames();
		umap.put("汤飞", "汤飞");
		for(String u:unames){
				umap.put(u, u);
		}	
		return "add";
	}
	public void refresh() {
		try {
			query();
		} catch (Exception e) {
			e.printStackTrace();
		}
		;
	}
	public String save() throws Exception {
		try {
			boolean flag = false;
			// SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			// behavior.setModifyDate(f.format(new Date()));
			UserModel userModel = (UserModel) request.getSession().getAttribute(
					Constants.LOGIN_USER_KEY);
			testRecord.setUpdateMan(userModel.getUsername());
			Date date=new Date();
			SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat f1=new SimpleDateFormat("yyyy-MM-dd");
			String modifyDate=f.format(date);
			String createDate=f1.format(date);
			testRecord.setUpdateDate(modifyDate);
			testRecord.setCreateDate(createDate);
			testRecord.setTester(absentPersons.replace(",","、"));
			//flag = testRecordService.add(testRecord);
			flag=false;
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

	public String edit() throws Exception {
		try {
			String ids = request.getParameter("ids");
			testRecord =testRecordService.getDetailById(ids);
			unames=testRecordService.getAllNames();
			umap.put("汤飞", "汤飞");
			for(String u:unames){
					umap.put(u, u);
			}	
			absentPersons=testRecord.getTester();
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "edit";
	}

	public String update() throws Exception {
		/*try {
			// SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			// behavior.setModifyDate(f.format(new Date()));
			UserModel userModel = (UserModel) request.getSession().getAttribute(
					Constants.LOGIN_USER_KEY);
			testRecord.setUpdateMan(userModel.getUsername());
			Date date=new Date();
			SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String modifyDate=f.format(date);
			testRecord.setUpdateDate(modifyDate);
			testRecord.setTester(absentPersons.replace(",","、"));
			boolean flag = testRecordService.update(testRecord);
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
		}*/
		return "msgBox";
	}
	
	public String delete() throws Exception {
		
		/*try {
			String ids = request.getParameter("ids");
			System.out.println("ids: "+ids);
			int iCount = 0;
			String[] sids = ids.split(",");
			if("all".equals(sids[0])){
				String[] arr = new String[sids.length-1];
				System.arraycopy(sids, 1, arr, 0, arr.length);
				sids = arr;
			}
			if (sids != null) {
				for (int i = 0; i < sids.length; i++) {
					TestRecord temp = testRecordService.getCaseById(sids[i]);
					testRecordService.delete(temp);
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
		}*/
		return "msgBox";
	} 
	


	public  String toExport() throws Exception
	{
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("TestRecord");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFFont font = wb.createFont();
		HSSFCellStyle style = wb.createCellStyle();
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBottomBorderColor(HSSFColor.BLACK.index);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setLeftBorderColor(HSSFColor.BLACK.index);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setRightBorderColor(HSSFColor.BLACK.index);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setTopBorderColor(HSSFColor.BLACK.index);
		
		HSSFCellStyle styleNomnal = wb.createCellStyle();
		styleNomnal.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		styleNomnal.setBottomBorderColor(HSSFColor.BLACK.index);
		styleNomnal.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		styleNomnal.setLeftBorderColor(HSSFColor.BLACK.index);
		styleNomnal.setBorderRight(HSSFCellStyle.BORDER_THIN);
		styleNomnal.setRightBorderColor(HSSFColor.BLACK.index);
		styleNomnal.setBorderTop(HSSFCellStyle.BORDER_THIN);
		styleNomnal.setTopBorderColor(HSSFColor.BLACK.index);
		
		HSSFCellStyle styleTestStatusFinish = wb.createCellStyle();
		styleTestStatusFinish.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		styleTestStatusFinish.setBottomBorderColor(HSSFColor.BLACK.index);
		styleTestStatusFinish.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		styleTestStatusFinish.setLeftBorderColor(HSSFColor.BLACK.index);
		styleTestStatusFinish.setBorderRight(HSSFCellStyle.BORDER_THIN);
		styleTestStatusFinish.setRightBorderColor(HSSFColor.BLACK.index);
		styleTestStatusFinish.setBorderTop(HSSFCellStyle.BORDER_THIN);
		styleTestStatusFinish.setTopBorderColor(HSSFColor.BLACK.index);
		HSSFFont fontBlue = wb.createFont();
		fontBlue.setColor(HSSFColor.BLUE.index); 
		styleTestStatusFinish.setFont(fontBlue);
		
		HSSFCellStyle styleStatusStopNLateOneDay = wb.createCellStyle();
		styleStatusStopNLateOneDay.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		styleStatusStopNLateOneDay.setBottomBorderColor(HSSFColor.BLACK.index);
		styleStatusStopNLateOneDay.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		styleStatusStopNLateOneDay.setLeftBorderColor(HSSFColor.BLACK.index);
		styleStatusStopNLateOneDay.setBorderRight(HSSFCellStyle.BORDER_THIN);
		styleStatusStopNLateOneDay.setRightBorderColor(HSSFColor.BLACK.index);
		styleStatusStopNLateOneDay.setBorderTop(HSSFCellStyle.BORDER_THIN);
		styleStatusStopNLateOneDay.setTopBorderColor(HSSFColor.BLACK.index);
		HSSFFont fontRed = wb.createFont();
		fontRed.setColor(HSSFColor.RED.index); 
		styleStatusStopNLateOneDay.setFont(fontRed);
		
		
		HSSFCellStyle styleTestStatusNotUp = wb.createCellStyle();
		styleTestStatusNotUp.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		styleTestStatusNotUp.setBottomBorderColor(HSSFColor.BLACK.index);
		styleTestStatusNotUp.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		styleTestStatusNotUp.setLeftBorderColor(HSSFColor.BLACK.index);
		styleTestStatusNotUp.setBorderRight(HSSFCellStyle.BORDER_THIN);
		styleTestStatusNotUp.setRightBorderColor(HSSFColor.BLACK.index);
		styleTestStatusNotUp.setBorderTop(HSSFCellStyle.BORDER_THIN);
		styleTestStatusNotUp.setTopBorderColor(HSSFColor.BLACK.index);
		styleTestStatusNotUp.setFillForegroundColor(HSSFColor.RED.index);
		styleTestStatusNotUp.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		
		HSSFCellStyle styleTestUpAhead = wb.createCellStyle();
		styleTestUpAhead.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		styleTestUpAhead.setBottomBorderColor(HSSFColor.BLACK.index);
		styleTestUpAhead.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		styleTestUpAhead.setLeftBorderColor(HSSFColor.BLACK.index);
		styleTestUpAhead.setBorderRight(HSSFCellStyle.BORDER_THIN);
		styleTestUpAhead.setRightBorderColor(HSSFColor.BLACK.index);
		styleTestUpAhead.setBorderTop(HSSFCellStyle.BORDER_THIN);
		styleTestUpAhead.setTopBorderColor(HSSFColor.BLACK.index);
		styleTestUpAhead.setFillForegroundColor(HSSFColor.GREEN.index);
		styleTestUpAhead.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		
		HSSFCellStyle styleTestUpLate = wb.createCellStyle();
		styleTestUpLate.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		styleTestUpLate.setBottomBorderColor(HSSFColor.BLACK.index);
		styleTestUpLate.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		styleTestUpLate.setLeftBorderColor(HSSFColor.BLACK.index);
		styleTestUpLate.setBorderRight(HSSFCellStyle.BORDER_THIN);
		styleTestUpLate.setRightBorderColor(HSSFColor.BLACK.index);
		styleTestUpLate.setBorderTop(HSSFCellStyle.BORDER_THIN);
		styleTestUpLate.setTopBorderColor(HSSFColor.BLACK.index);
		styleTestUpLate.setFillForegroundColor(HSSFColor.RED.index);
		styleTestUpLate.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		
		HSSFPalette palette = wb.getCustomPalette();
		//替换标准红色为freebsd.org 上
		palette.setColorAtIndex(HSSFColor.LIME.index,(byte) 150,(byte) 150,(byte) 150);

		//style.setFillBackgroundColor(HSSFColor.LIME.index);
		style.setFillForegroundColor(HSSFColor.LIME.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		
		//font.setColor(HSSFFont.COLOR_NORMAL);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); 
		style.setFont(font);

        HSSFCell cell = row.createCell((short) 0);
//		//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell.setCellValue("项目类型");
		cell.setCellStyle(style);
		cell = row.createCell((short) 1);
//		//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell.setCellValue("项目名称");
		cell.setCellStyle(style);
		cell = row.createCell((short) 2);
//		//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell.setCellValue("测试版本");
		cell.setCellStyle(style);
		cell = row.createCell((short) 3);
//		//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell.setCellValue("项目经理");
		cell.setCellStyle(style);
		cell = row.createCell((short) 4);
//		//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell.setCellValue("测试状态");
		cell.setCellStyle(style);
		cell = row.createCell((short) 5);
//		//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell.setCellValue("计划提交日期");
		cell.setCellStyle(style);
		cell = row.createCell((short) 6);
//		//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell.setCellValue("实际提交日期");
		cell.setCellStyle(style);
		cell = row.createCell((short) 7);
//		//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell.setCellValue("提交进度");
		cell.setCellStyle(style);
		cell = row.createCell((short) 8);
//		//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell.setCellValue("测试开始日期");
		cell.setCellStyle(style);
		cell = row.createCell((short) 9);
//		//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell.setCellValue("测试完成日期");
		cell.setCellStyle(style);
		cell = row.createCell((short) 10);
//		//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell.setCellValue("发现缺陷数");
		cell.setCellStyle(style);
		cell = row.createCell((short) 11);
//		//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell.setCellValue("测试人员数");
		cell.setCellStyle(style);
		cell = row.createCell((short) 12);
//		//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell.setCellValue("测试人均工时(单位:时/人)");
		cell.setCellStyle(style);
		cell = row.createCell((short) 13);
//		//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell.setCellValue("测试总工作量(单位:人天)");
		cell.setCellStyle(style);
		cell = row.createCell((short) 14);
//		//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell.setCellValue("OA流水号");
		cell.setCellStyle(style);
		cell = row.createCell((short) 15);
//		//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell.setCellValue("版本类型");
		cell.setCellStyle(style);
		cell = row.createCell((short) 16);
//		//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell.setCellValue("测试人员");
		cell.setCellStyle(style);
		cell = row.createCell((short) 17);
//		//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell.setCellValue("测试范围与重点");
		cell.setCellStyle(style);
		cell = row.createCell((short) 18);
//		//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell.setCellValue("备注");
		cell.setCellStyle(style);
		
		int pageNum = 1;
		int pageSize = 100;
		//upMonth = request.getParameter("upMonth");
		//Page page = testRecordService.getPageExcel(upMonth,StartMonth,EndMonth, projectTypeQuery, buildType, prjName,testStatusQuery,sumbitProcessQuery,testMan, pageNum, pageSize);
		Page page=null;
		request.setAttribute("currPage", page);
		List<Object> list = (List<Object>) page.getQueryResult();
		int FindBugSum=0;
		int TesterSum=0;
		double WorkLoad=0;
		double TestTimeSum=0;
		for (int i = 0; i < list.size(); i++) {
			TestRecord tmp=(TestRecord)list.get(i);
			if(tmp.getFindBugSum()!=null)
			{
				FindBugSum+=tmp.getFindBugSum();
			}
			if(tmp.getTesterSum()!=null)
			{
				TesterSum+=tmp.getTesterSum();
			}
			if(tmp.getTestTimeSum()!=null)
			{
				TestTimeSum+=tmp.getTestTimeSum();
			}
			if(tmp.getWorkLoad()!=null)
			{
				WorkLoad+=tmp.getWorkLoad()/8;
			}
		}
		
		DecimalFormat forma=(DecimalFormat)DecimalFormat.getInstance();
		forma.applyPattern("0.0");
	//	FindBugSum=Double.parseDouble(forma.format(FindBugSum));
	//	TesterSum=Double.parseDouble(forma.format(TesterSum));
		WorkLoad=Double.parseDouble(forma.format(WorkLoad));
		TestTimeSum=Double.parseDouble(forma.format(TestTimeSum));
		TestRecord f=new TestRecord();
		f.setTestVer("合计");
		f.setFindBugSum(FindBugSum);
		f.setTesterSum(TesterSum);
		f.setTestTimeSum(TestTimeSum);
		f.setWorkLoad(WorkLoad);
		
		list.add(f);

		for (int i = 0; i < list.size(); i++)
		{
			row = sheet.createRow((int) i + 1);
			TestRecord stu = (TestRecord) list.get(i);
			// 第四步，创建单元格，并设置值
			if(!stu.getTestVer().equals("合计"))
			{
				if(stu.getProjectType()!=null)
				{
					cell = row.createCell((short) 0);
//					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(stu.getProjectType());
			   	    cell.setCellStyle(style);//设置风格
			   	    sheet.setColumnWidth((short) 0,(short)(5*512));
				}
				else
				{
					cell = row.createCell((short) 0);
					cell.setCellStyle(style);
				}
				if(stu.getProjectName()!=null)
				{
					cell = row.createCell((short) 1);
//					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(stu.getProjectName());
				    cell.setCellStyle(styleNomnal);//设置风格
				    sheet.setColumnWidth((short) 1,(short)(8*512));
					//sheet.setColumnWidth((short) 0,(short)stu.getProjectName().length()*512);
				}
				else
				{
					cell = row.createCell((short) 1);
					cell.setCellStyle(styleNomnal);
				}

				if(stu.getProjectManager()!=null)
				{
					cell = row.createCell((short) 3);
//					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(stu.getProjectManager());
				    cell.setCellStyle(styleNomnal);//设置风格
				    sheet.setColumnWidth((short) 3,(short)(5*512));
				}
				else
				{
					cell = row.createCell((short) 3);
					cell.setCellStyle(styleNomnal);
				}
				if(stu.getTestVer()!=null)
				{
					cell = row.createCell((short) 2);
//					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(stu.getTestVer());
					cell.setCellStyle(styleNomnal);//设置风格
					sheet.setColumnWidth((short) 2,(short)(7*512));
				}
				else
				{
					cell = row.createCell((short) 2);
					cell.setCellStyle(styleNomnal);
				}
				if(stu.getVerType()!=null)
				{
					cell = row.createCell((short) 15);
//					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(stu.getVerType());
					cell.setCellStyle(styleNomnal);//设置风格
					sheet.setColumnWidth((short) 15,(short)(5*512));
				}
				else
				{
					cell = row.createCell((short) 15);
					cell.setCellStyle(styleNomnal);
				}
				if(stu.getTestStatus()!=null)
				{
					cell = row.createCell((short) 4);
//					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(stu.getTestStatus());
					if(stu.getTestStatus().equals("未提交"))
					{
						cell.setCellStyle(styleTestStatusNotUp);//设置风格

					}
					else if(stu.getTestStatus().equals("已完成"))
					{
						cell.setCellStyle(styleTestStatusFinish);
					}
					else if(stu.getTestStatus().equals("测试中止"))
					{
						cell.setCellStyle(styleStatusStopNLateOneDay);
					}
					else
					{
						cell.setCellStyle(styleNomnal);
					}		
					sheet.setColumnWidth((short) 4,(short)(5*512));
				}
				else
				{
					cell = row.createCell((short) 4);
					cell.setCellStyle(styleNomnal);
				}
				if(stu.getSumbitPlanDate()!=null)
				{
					cell = row.createCell((short) 5);
//					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(stu.getSumbitPlanDate().toString());
					cell.setCellStyle(styleNomnal);//设置风格
					sheet.setColumnWidth((short) 5,(short)(7*512));
				}
				else
				{
					cell = row.createCell((short) 5);
					cell.setCellStyle(styleNomnal);
				}
				if(stu.getActualSumbitDate()!=null)
				{
					cell = row.createCell((short) 6);
//					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(stu.getActualSumbitDate().toString());
					cell.setCellStyle(styleNomnal);//设置风格
					sheet.setColumnWidth((short) 6,(short)(7*512));
				}
				else
				{
					cell = row.createCell((short) 6);
					cell.setCellStyle(styleNomnal);
				}
				if(stu.getSumbitProcess()!=null)
				{
					cell = row.createCell((short) 7);
//					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(stu.getSumbitProcess());
					
					
					if(stu.getSumbitProcess().equals("提前"))
					{
						cell.setCellStyle(styleTestUpAhead);//设置风格
					}
					else if(stu.getSumbitProcess().equals("延迟>=1天"))
					{
						cell.setCellStyle(styleStatusStopNLateOneDay);//设置风格
					}
					else if(stu.getSumbitProcess().equals("按时"))
					{
						cell.setCellStyle(styleNomnal);//设置风格
					}
					else
					{
						cell.setCellStyle(styleTestUpLate);//设置风格
					}
					sheet.setColumnWidth((short) 7,(short)(5*512));
				}
				else
				{
					cell = row.createCell((short) 7);
					cell.setCellStyle(styleNomnal);
				}
				if(stu.getTestStartDate()!=null)
				{
					cell = row.createCell((short) 8);
//					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(stu.getTestStartDate().toString());
					cell.setCellStyle(styleNomnal);//设置风格
					sheet.setColumnWidth((short) 8,(short)(7*512));
				}
				else
				{
					cell = row.createCell((short) 8);
					cell.setCellStyle(styleNomnal);
				}
				if(stu.getTestFinishDate()!=null)
				{
					cell = row.createCell((short) 9);
//					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(stu.getTestFinishDate().toString());
					cell.setCellStyle(styleNomnal);//设置风格
					sheet.setColumnWidth((short) 9,(short)(7*512));
				}
				else
				{
					cell = row.createCell((short) 9);
					cell.setCellStyle(styleNomnal);
				}
				if(stu.getFindBugSum()!=null)
				{
					cell = row.createCell((short) 10);
//					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(stu.getFindBugSum().toString());
					cell.setCellStyle(styleNomnal);//设置风格
					sheet.setColumnWidth((short) 10,(short)(6*512));
				}
				else
				{
					cell = row.createCell((short) 10);
					cell.setCellStyle(styleNomnal);
				}
				if(stu.getTesterSum()!=null)
				{
					cell = row.createCell((short) 11);
//					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(stu.getTesterSum().toString());
					cell.setCellStyle(styleNomnal);//设置风格
					sheet.setColumnWidth((short) 11,(short)(6*512));
				}
				else
				{
					cell = row.createCell((short) 11);
					cell.setCellStyle(styleNomnal);
				}
				if(stu.getTestTimeSum()!=null)
				{
					cell = row.createCell((short) 12);
//					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(stu.getTestTimeSum().toString());
					cell.setCellStyle(styleNomnal);//设置风格
					sheet.setColumnWidth((short) 12,(short)(13*512));
				}
				else
				{
					cell = row.createCell((short) 12);
					cell.setCellStyle(styleNomnal);
				}
				if(stu.getWorkLoad()!=null)
				{
					double Workloadtmp = 0;
					Workloadtmp = Double.parseDouble(forma.format(stu.getWorkLoad()/8));
					cell = row.createCell((short) 13);
//					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(Workloadtmp);
					cell.setCellStyle(styleNomnal);//设置风格
					sheet.setColumnWidth((short) 13,(short)(13*512));
				}
				else
				{
					cell = row.createCell((short) 13);
					cell.setCellStyle(styleNomnal);
				}
				if(stu.getOaNum()!=null)
				{
					cell = row.createCell((short) 14);
//					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(stu.getOaNum());
					cell.setCellStyle(styleNomnal);//设置风格
					sheet.setColumnWidth((short) 14,(short)(5*512));
				}
				else
				{
					cell = row.createCell((short) 14);
					cell.setCellStyle(styleNomnal);
				}
				if(stu.getTester()!=null)
				{
					cell = row.createCell((short) 16);
//					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(stu.getTester());
					cell.setCellStyle(styleNomnal);//设置风格
					sheet.setColumnWidth((short) 16,(short)(15*512));
				}
				else
				{
					cell = row.createCell((short) 16);
					cell.setCellStyle(styleNomnal);
				}
				if(stu.getRemark()!=null)
				{
					cell = row.createCell((short) 17);
//					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(stu.getRemark());
					cell.setCellStyle(styleNomnal);//设置风格
					sheet.setColumnWidth((short) 17,(short)(10*512));
				}
				else
				{
					cell = row.createCell((short) 17);
					cell.setCellStyle(styleNomnal);
				}
				if(stu.getRemark2()!=null)
				{
					cell = row.createCell((short) 18);
//					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(stu.getRemark2());
					cell.setCellStyle(styleNomnal);//设置风格
					sheet.setColumnWidth((short) 18,(short)(10*512));
				}
				else
				{
					cell = row.createCell((short) 18);
					cell.setCellStyle(styleNomnal);
				}
			}
			else
			{	
				cell = row.createCell((short) 0);
				cell.setCellStyle(styleNomnal);
				cell = row.createCell((short) 1);
				cell.setCellStyle(styleNomnal);
				cell = row.createCell((short) 2);
				cell.setCellStyle(styleNomnal);
				cell = row.createCell((short) 3);
//				//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue(stu.getTestVer());
				cell.setCellStyle(styleNomnal);//设置风格	
				cell = row.createCell((short) 4);
				cell.setCellStyle(styleNomnal);
				cell = row.createCell((short) 5);
				cell.setCellStyle(styleNomnal);
				cell = row.createCell((short) 6);
				cell.setCellStyle(styleNomnal);
				cell = row.createCell((short) 7);
				cell.setCellStyle(styleNomnal);
				cell = row.createCell((short) 8);
				cell.setCellStyle(styleNomnal);
				cell = row.createCell((short) 9);
				cell.setCellStyle(styleNomnal);
				cell = row.createCell((short) 10);
//				//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue(stu.getFindBugSum().toString());
				cell.setCellStyle(styleNomnal);//设置风格
				cell = row.createCell((short) 11);
//				//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue(stu.getTesterSum().toString());
				cell.setCellStyle(styleNomnal);//设置风格
				cell = row.createCell((short) 12);
//				//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue(stu.getTestTimeSum().toString());
				cell.setCellStyle(styleNomnal);//设置风格
				cell = row.createCell((short) 13);
//				//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue(stu.getWorkLoad().toString());
				cell.setCellStyle(styleNomnal);//设置风格
				cell = row.createCell((short) 14);
				cell.setCellStyle(styleNomnal);
				cell = row.createCell((short) 15);
				cell.setCellStyle(styleNomnal);
				cell = row.createCell((short) 16);
				cell.setCellStyle(styleNomnal);
				cell = row.createCell((short) 17);
				cell.setCellStyle(styleNomnal);
				cell = row.createCell((short) 18);
				cell.setCellStyle(styleNomnal);
			
			}
		}
		Calendar NowDate= Calendar.getInstance();
		int NowYear = NowDate.get(Calendar.YEAR);
		int NowDay = NowDate.get(Calendar.DAY_OF_MONTH);
		int NowMonth = NowDate.get(Calendar.MONTH)+1;
		
		StringBuffer sb=new StringBuffer();
		sb.append("C:/").append(NowYear).append("年").append(NowMonth).append("月软件产品测试情况记录表-").append(NowYear).append(NowMonth).append(NowDay).append(".xls");
		fileName = sb.toString();	
		FileOutputStream fout = new FileOutputStream("C:/TestRecord.xls");
		wb.write(fout);
		fout.close();
		return "xia";
	}
	
	
	public String query() throws Exception {
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
			Page page=null;
			//Page page = testRecordService.getPage(upMonth,StartMonth,EndMonth, projectTypeQuery, buildType, prjName,testStatusQuery,sumbitProcessQuery,testMan, pageNum, pageSize);
			request.setAttribute("currPage", page);
			List<Object> list = (List<Object>) page.getQueryResult();
			int FindBugSum=0;
			int TesterSum=0;
			double WorkLoad=0;
			double TestTimeSum=0;
			for (int i = 0; i < list.size(); i++) {
				TestRecord tmp=(TestRecord)list.get(i);
				if(tmp.getFindBugSum()!=null)
				{
					FindBugSum+=tmp.getFindBugSum();
				}
				if(tmp.getTesterSum()!=null)
				{
					TesterSum+=tmp.getTesterSum();
				}
				if(tmp.getTestTimeSum()!=null)
				{
					TestTimeSum+=tmp.getTestTimeSum();
				}
				if(tmp.getWorkLoad()!=null)
				{
					WorkLoad+=tmp.getWorkLoad();
				}
			}
			DecimalFormat forma=(DecimalFormat)DecimalFormat.getInstance();
			forma.applyPattern("0.00");
	//		FindBugSum=Double.parseDouble(forma.format(FindBugSum));
	//		TesterSum=Double.parseDouble(forma.format(TesterSum));
			WorkLoad=Double.parseDouble(forma.format(WorkLoad));
			TestTimeSum=Double.parseDouble(forma.format(TestTimeSum));
			TestRecord f=new TestRecord();
			f.setTestVer("合计");
			f.setFindBugSum(FindBugSum);
			f.setTesterSum(TesterSum);
			f.setTestTimeSum(TestTimeSum);
			f.setWorkLoad(WorkLoad);
			list.add(f);
			if (from != null && from.equals("refresh")) {
				 
				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf(page.getPageCount()));
				map.put("recordCount", String.valueOf(page.getRecordCount()));
				JSONUtil jsonUtil = new JSONUtil(
						"cn.grgbanking.feeltm.domain.testsys.TestRecord");
				JSONArray jsonObj = jsonUtil.toJSON(list, map);
				JSONObject input = new JSONObject();
				input.put("pageCount", String.valueOf(page.getPageCount()));
				input.put("recordCount", String.valueOf(page.getRecordCount()));
				input.put("jsonObj", jsonObj);
				PrintWriter out = response.getWriter();
				out.print(input);
				return "";
			} else {
				ActionContext.getContext().put("TestRecordList", list);
				upMonth = request.getParameter("upMonth");
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
			testRecord=testRecordService.getDetailById(ids);
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "show";
	}
	public double formatDouble(String s){
		 BigDecimal b = new BigDecimal(s);
		 double f1= b.setScale(1,BigDecimal.ROUND_HALF_UP).doubleValue();
			//performance.setSubtotal_s(meg(sum_s));
		   // performance.setSubtotal_s(f1);
			//s=s;
			return f1;
		}
	
	public String getPrjName() {
		return prjName;
	}

	public void setPrjName(String prjName) {
		this.prjName = prjName;
	}

	public TestRecord getTestRecord() {
		return testRecord;
	}
	public void setTestRecord(TestRecord testRecord) {
		this.testRecord = testRecord;
	}
	public TestRecordService getTestRecordService() {
		return testRecordService;
	}
	public void setTestRecordService(TestRecordService testRecordService) {
		this.testRecordService = testRecordService;
	}
	public String getTestRecordMan() {
		return TestRecordMan;
	}
	public void setTestRecordMan(String testRecordMan) {
		TestRecordMan = testRecordMan;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Map<String, String> getUmap() {
		return umap;
	}
	public void setUmap(Map<String, String> umap) {
		this.umap = umap;
	}
	public String getAbsentPersons() {
		return absentPersons;
	}
	public void setAbsentPersons(String absentPersons) {
		this.absentPersons = absentPersons;
	}
	public List<String> getUnames() {
		return unames;
	}
	public void setUnames(List<String> unames) {
		this.unames = unames;
	}
	public String getFindBugSumTotal() {
		return FindBugSumTotal;
	}
	public void setFindBugSumTotal(String findBugSumTotal) {
		FindBugSumTotal = findBugSumTotal;
	}
	public String getTesterSumTotal() {
		return TesterSumTotal;
	}
	public void setTesterSumTotal(String testerSumTotal) {
		TesterSumTotal = testerSumTotal;
	}
	public String getTestTimeSumTotal() {
		return TestTimeSumTotal;
	}
	public void setTestTimeSumTotal(String testTimeSumTotal) {
		TestTimeSumTotal = testTimeSumTotal;
	}
	public String getWorkLoadTotal() {
		return WorkLoadTotal;
	}
	public void setWorkLoadTotal(String workLoadTotal) {
		WorkLoadTotal = workLoadTotal;
	}
	public String getUpMonth() {
		return upMonth;
	}
	public void setUpMonth(String upMonth) {
		this.upMonth = upMonth;
	}
	public String getProjectTypeQuery() {
		return projectTypeQuery;
	}
	public void setProjectTypeQuery(String projectTypeQuery) {
		this.projectTypeQuery = projectTypeQuery;
	}
	public String getBuildType() {
		return buildType;
	}
	public void setBuildType(String buildType) {
		this.buildType = buildType;
	}
	public String getTestStatusQuery() {
		return testStatusQuery;
	}
	public void setTestStatusQuery(String testStatusQuery) {
		this.testStatusQuery = testStatusQuery;
	}
	public String getSumbitProcessQuery() {
		return sumbitProcessQuery;
	}
	public void setSumbitProcessQuery(String sumbitProcessQuery) {
		this.sumbitProcessQuery = sumbitProcessQuery;
	}
	public String getTestMan() {
		return testMan;
	}
	public void setTestMan(String testMan) {
		this.testMan = testMan;
	}
	public String getStartMonth() {
		return StartMonth;
	}
	public void setStartMonth(String startMonth) {
		StartMonth = startMonth;
	}
	public String getEndMonth() {
		return EndMonth;
	}
	public void setEndMonth(String endMonth) {
		EndMonth = endMonth;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	


	
}
