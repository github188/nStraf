package cn.grgbanking.feeltm.performance.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.domain.testsys.Performance;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.performance.dao.PerformanceDao;
import cn.grgbanking.feeltm.report.domain.DayReportStatic;
import cn.grgbanking.framework.service.BaseService;
import cn.grgbanking.framework.util.Page;

@Service("performanceService")
@Transactional
public class PerformanceService extends BaseService{
	@Autowired
	private PerformanceDao performanceDao;
	public boolean add(Performance performance){
		boolean flag=false;
		try{
			performanceDao.addObject(performance);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
/*	public boolean delete(String[] ids){
		return suggestionDao.remove(ids);
	}*/
	
	public boolean delete(Performance performance){
		boolean flag = false;
		try {
			performanceDao.removeObject(Performance.class, performance.getId());
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	

	public boolean update(Performance performance){
		boolean flag=false;
		try{
			performanceDao.updateObject(performance);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	public Performance getCaseById(String id){
		return (Performance)performanceDao.getObject(Performance.class, id);
	}
	
	public boolean upEdit_lock(String month_date){
		return performanceDao.upEdit_lock(month_date);
	}
	//String createDate, String createMan,String updateman,String category,String ano,int pageNum, int pageSize,String raiseEndDate
	@Transactional(readOnly = true)
	public Page getPage(String groupname,String username,String queryGroupname,int queryLevel,int pageNum, int pageSize,String month) {
		return performanceDao.getPage(groupname,username,queryGroupname,queryLevel,pageNum,pageSize,month);
	}
	
	
	@Transactional(readOnly = true)
	public List<Object> getNames(String groupName) {
		return performanceDao.getUsernamesByGroup(groupName);
	}
	
	
	@Transactional(readOnly=true)
	public String getNextNo(){
		return performanceDao.getNextNo();
	}
	
	@Transactional(readOnly=true)
	public String getUserGroup_name(String username){
		return performanceDao.getUserGroup_name(username);
	}
	
	@Transactional(readOnly=true)
	public String getUserEffect_score(String username,String month_date){
		return performanceDao.getUserEffect_score(username,month_date);
	}
	
	@Transactional(readOnly=true)
	public String getUserPrice_score(String username,String month_date){
		return performanceDao.getUserPrice_score(username,month_date);
	}
	
	@Transactional(readOnly=true)
	public String getUserEffect_num(String username,String month_date){
		return performanceDao.getUserEffect_num(username,month_date);
	}
	
	@Transactional(readOnly=true)
	public String getUserPrice_num(String username,String month_date){
		return performanceDao.getUserPrice_num(username,month_date);
	}
	
	@Transactional(readOnly=true)
	public String getUser_priseValue(String username,String month_date){
		return performanceDao.getUser_priseValue(username,month_date);
	}
	
	@Transactional(readOnly=true)
	public String getUser_punishValue(String username,String month_date){
		return performanceDao.getUser_punishValue(username,month_date);
	}
	
	@Transactional(readOnly=true)
	public String getUserMeetingUnAudit_num(String group_name,String month_date){
		return performanceDao.getUserMeetingUnAudit_num(group_name,month_date);
	}
	
	@Transactional(readOnly=true)
	public String getUserTrain_num(String username,String month_date){
		return performanceDao.getUserTrain_num(username,month_date);
	}
	
	@Transactional(readOnly = true)
	public int getReportStatics(String username,String month_date,Date date2) {
		List<DayReportStatic> list = performanceDao.dayStatic(month_date);// 获得前四项数据
		int i = 0;
		if (list != null && list.size() > 0) {
			for (DayReportStatic st : list) {
				Date date = st.getStartDate();
				String str="";
				

				long startT=date.getTime(); //定义上机时间  
				long endT=date2.getTime();  //定义下机时间

				 
				long ss=(endT-startT)/(1000); //共计秒数  
				int MM =(int)ss/60;   //共计分钟数  
				int hh=(int)ss/3600;  //共计小时数  
				int dd=(int)hh/24;   //共计天数 
				
				
				
				
				
				if(dd > 0){
					if(!st.getWeekDay().equals("星期六")&&!st.getWeekDay().equals("星期日")){
						str = performanceDao.getUnwritePersons(date); // 获得未填写的人员
						if(str!=null){
							if(!str.equals("") && str.indexOf(username)!=-1){
								i++;
							}
						}
					}
				}
				//判断是否有用户名字,有的话累加一
				
			}
		}
		return i;
	}
	
	@Transactional(readOnly=true)
	public String getUseredit_lock(String month_date){
		return performanceDao.getUseredit_lock(month_date);
	}
	
	@Transactional(readOnly=true)
	public boolean getUserMonth_date(String month_date){
		return performanceDao.getUserMonth_date(month_date);
	}
	
	@Transactional(readOnly=true)
	public String getUserlevel(String username){
		return performanceDao.getUserlevel(username);
	}
	
	@Transactional(readOnly = true)
	public List<String> getAllNames(){
		
		return performanceDao.getAllNames();
	}
	
	@Transactional(readOnly = true)
	public List<Performance> getPerformanceByPno(String pno) {
		return performanceDao.selectPerformance(pno);
	}
	
}
