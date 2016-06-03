package cn.grgbanking.feeltm.staffEntry.service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.staffEntry.dao.StaffEntryDao;
import cn.grgbanking.feeltm.staffEntry.domain.OnBoardFlow;
import cn.grgbanking.feeltm.staffEntry.domain.OnBoardFlowCheckCondition;
import cn.grgbanking.framework.service.BaseService;
import cn.grgbanking.framework.util.Page;

@Service("staffEntryService")
@Transactional
public class StaffEntryService extends BaseService{
	@Autowired
	public StaffEntryDao staffEntryDao;
	
	
	public void export(HSSFWorkbook wb,HttpServletResponse response){
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
	
	
//	public boolean update(TestRecord testRecord){
//		boolean flag=false;
//		try{
//			staffEntryDao.updateObject(testRecord);
//			flag=true;
//		}catch(Exception e){
//			flag=false;
//			e.printStackTrace();
//			SysLog.error(e);
//		}
//		return flag;
//	}
	
	@Transactional(readOnly = true)
	public Page getPage(boolean hasRight, String curUserId, OnBoardFlow entryInfo, String searchContent, Date queryEntryStartTime, Date queryEntryEndTime, int pageNum,int pageSize){
		return staffEntryDao.getPage(hasRight,curUserId,entryInfo,searchContent,queryEntryStartTime,queryEntryEndTime,pageNum, pageSize);
	}

	/** 根据部门名称获取这个部门下所有的用户
	 */
	public List getUsersByDept(String dept) {
		return staffEntryDao.getUsersByDept(dept);
	}

	/** 获取指定模版的检查条件
	 */
	public List getFolderCheckConditions(String folder) {
		List list=staffEntryDao.getFolderCheckConditions(folder);
		return list;
	}

	/** 保存检查条件
	 */
	public void saveCheckCondition(OnBoardFlowCheckCondition checkCondition) {
		staffEntryDao.saveCheckCondition(checkCondition);
	}

	/**
	 * 查询指定模版的检查条件
	 */
	public List queryCheckCondtion(String folder) {
		return staffEntryDao.queryCheckCondtion(folder);
	}
	
	/**
	 * 查询指定ID的条件
	 */
	public List queryCheckCondtionById(String conId) {
		return staffEntryDao.queryCheckCondtionById(conId);
	}

	
	/**
	 * 删除指定的检查条件
	 */
	public void delCheckConditions(OnBoardFlowCheckCondition checkCondition) {
		String idStr=checkCondition.getId();
		String[] ids=idStr.split(",");
		for(int i=0;i<ids.length;i++){
			staffEntryDao.delCheckCondition(ids[i]);
		}
	}

	/**
	 * 保存或者修改入职信息
	 */
	public boolean saveOrUpdateStaffEntryInfo(OnBoardFlow entryInfo) {
		if(StringUtils.isEmpty(entryInfo.getId())){
			long maxSerialNumber=staffEntryDao.getMaxSerialNumber();
			entryInfo.setSerialNumber(maxSerialNumber+1);
		}
		staffEntryDao.saveOrUpdateStaffEntryInfo(entryInfo);
		return true;
	}

	/**获取指定id的入职信息
	 * @param id
	 * @return
	 */
	public OnBoardFlow getCaseById(String id){
		return (OnBoardFlow)staffEntryDao.getObject(OnBoardFlow.class, id);
	}
	
	/**获取指定人员的入职信息
	 * @param id
	 * @return
	 */
	public OnBoardFlow getCaseByUserId(String userId){
		return (OnBoardFlow)staffEntryDao.getCaseByUserId(userId);
	}
	
	/** 删除
	 * @return
	 */
	public boolean delete(OnBoardFlow obj){
		boolean flag = false;
		try {
			staffEntryDao.removeObject(OnBoardFlow.class, obj.getId());
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
}
