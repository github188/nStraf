package cn.grgbanking.feeltm.testrecord.service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.domain.testsys.TestRecord;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.testrecord.dao.TestRecordDao;
import cn.grgbanking.feeltm.testrecord.domain.TestQuestion;
import cn.grgbanking.framework.service.BaseService;
import cn.grgbanking.framework.util.Page;

@Service("testRecordService")
@Transactional
public class TestRecordService extends BaseService{
	@Autowired
	public TestRecordDao testRecordDao;
	
	public boolean add(TestQuestion ques){
		boolean flag=false;
		try{
			testRecordDao.addObject(ques);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	public TestQuestion getCaseById(String id){
		return (TestQuestion)testRecordDao.getObject(TestQuestion.class, id);
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
	
	public boolean delete(TestQuestion testRecord){
		boolean flag = false;
		try {
			testRecordDao.removeObject(TestQuestion.class, testRecord.getId());
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	public boolean update(TestQuestion question){
		boolean flag=false;
		try{
			testRecordDao.updateObject(question);
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
	public Page getPage(String modualName2,String serialNo,String findMan,String findDate,String soloveMan,String soloveDate,String questionStaus,String finishRate,String modualName, int pageNum,int pageSize, String title, String urge, String updateMan, String deployStatus){
		return testRecordDao.getPage(modualName2,serialNo,findMan,findDate,soloveMan,soloveDate,questionStaus,finishRate,modualName, pageNum, pageSize,title,urge,updateMan,deployStatus);
	}
	
	
	@Transactional(readOnly = true)
	public Page getSum(String start,String end,String prjName,String tripMan,int pageNum,int pageSize){
		return testRecordDao.getSum(start, end, prjName, tripMan, pageNum, pageSize);
	}

	public long getMaxSerialNo() {
		return testRecordDao.getMaxSerialNo();
	}
	
}
