package cn.grgbanking.feeltm.util;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class ExcelDeal {
	public static void exportExcel() {
		String exportFileName = "导出EXCEl";
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();
		HSSFRow row = sheet.createRow(0);
		HSSFCell cell = row.createCell((short) 0);
		cell.setCellValue("姓名");
		cell = row.createCell((short) 1);
		cell.setCellValue("性别");
		row = sheet.createRow(1);
		cell = row.createCell((short) 0);
		cell.setCellValue("涂林");
		cell = row.createCell((short) 1);
		cell.setCellValue("女");
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.reset();
			response.setHeader("Content-disposition", "attachment;filename="+exportFileName);
			response.setContentType("application/vnd.ms-excel");
			OutputStream os = response.getOutputStream();
			workbook.write(os);
			os.close();
			os.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void autonotionTest(){
		String[] xml = {"userbean.xml"};
		ApplicationContext ctx = new ClassPathXmlApplicationContext(xml);
		ctx.getBean("");
		
	}
	public static void main(String[] args) {
		exportExcel();
	}
	
}
