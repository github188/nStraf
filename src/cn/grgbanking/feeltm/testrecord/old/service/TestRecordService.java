package cn.grgbanking.feeltm.testrecord.old.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFHeader;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.domain.testsys.KpiPoint;
import cn.grgbanking.feeltm.domain.testsys.TestRecord;
import cn.grgbanking.feeltm.domain.testsys.DepartFinance;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.testrecord.dao.TestRecordDao;
import cn.grgbanking.framework.service.BaseService;
import cn.grgbanking.framework.util.Page;

@Service("testRecordService2")
@Transactional
public class TestRecordService extends BaseService{
	@Autowired
	public TestRecordDao testRecordDao;
	
	public boolean add(TestRecord testRecord){
		boolean flag=false;
		try{
			testRecordDao.addObject(testRecord);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	public TestRecord getCaseById(String id){
		return (TestRecord)testRecordDao.getObject(TestRecord.class, id);
	}
	
	@Transactional(readOnly = true)
	public List<String> getAllNames(){
		
		return testRecordDao.getAllNames();
	}
	

	public void export(HSSFWorkbook wb,HttpServletResponse response){
		//HttpServletResponse response = null;//创建一个HttpServletResponse对象
        OutputStream out = null;//创建一个输出流对象

        try {

            response = ServletActionContext.getResponse();//初始化HttpServletResponse对象

            out = response.getOutputStream();//

          

           	String headerStr ="student";

  //  headerStr =new String(headerStr.getBytes("gb2312"), "ISO8859-1");//headerString为中文时转码

    		response.setHeader("Content-disposition","attachment; filename="+headerStr+".xls");//filename是下载的xls的名，建议最好用英文

            response.setContentType("application/msexcel;charset=UTF-8");//设置类型

            response.setHeader("Pragma","No-cache");//设置头

            response.setHeader("Cache-Control","no-cache");//设置头

            response.setDateHeader("Expires", 0);//设置日期头
            //out = response.getOutputStream();
            wb.write(out);

            out.flush();
            wb.write(out);
        //  out.close();
        //  wb.write(out);

        } catch (IOException e) {

            e.printStackTrace();

        }finally{

            try{
                if(out!=null){
                    out.close();
                }        
            }catch(IOException e){
                e.printStackTrace();
            }
        }		
	}
	
	public boolean delete(TestRecord testRecord){
		boolean flag = false;
		try {
			testRecordDao.removeObject(TestRecord.class, testRecord.getId());
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	public boolean update(TestRecord testRecord){
		boolean flag=false;
		try{
			testRecordDao.updateObject(testRecord);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	@Transactional(readOnly = true)
	public TestRecord getDetailById(String id){
		return (TestRecord)testRecordDao.getObject(TestRecord.class, id);
	}
	
	@Transactional(readOnly = true)
	public Page getPage(String upMonth,String StartMonth,String EndMonth,String projectTypeQuery,String buildType,String prjName,String testStatusQuery,String sumbitProcessQuery,String testMan,int pageNum,int pageSize){
		//return testRecordDao.getPage(upMonth,StartMonth,EndMonth, projectTypeQuery, buildType, prjName,testStatusQuery,sumbitProcessQuery,testMan, pageNum, pageSize);
		return null;
	}
	
	@Transactional(readOnly = true)
	public Page getPageExcel(String upMonth,String StartMonth,String EndMonth,String projectTypeQuery,String buildType,String prjName,String testStatusQuery,String sumbitProcessQuery,String testMan,int pageNum,int pageSize){
		//return testRecordDao.getPageExcel(upMonth,StartMonth,EndMonth, projectTypeQuery, buildType, prjName,testStatusQuery,sumbitProcessQuery,testMan, pageNum, pageSize);
		return null;
	}
	
	@Transactional(readOnly = true)
	public Page getSum(String start,String end,String prjName,String tripMan,int pageNum,int pageSize){
		return testRecordDao.getSum(start, end, prjName, tripMan, pageNum, pageSize);
	}
	
}
