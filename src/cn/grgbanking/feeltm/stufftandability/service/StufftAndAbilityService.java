package cn.grgbanking.feeltm.stufftandability.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.domain.testsys.StufftAndAbility;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.report.domain.DayReportStatic;
import cn.grgbanking.feeltm.stufftandability.dao.StufftAndAbilityDao;
import cn.grgbanking.framework.service.BaseService;
import cn.grgbanking.framework.util.Page;

@Service("stufftAndAbilityService")
@Transactional
public class StufftAndAbilityService extends BaseService{
	@Autowired
	private StufftAndAbilityDao stufftAndAbilityDao;
	public boolean add(StufftAndAbility stufftAndAbility){
		boolean flag=false;
		try{
			stufftAndAbilityDao.addObject(stufftAndAbility);
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
	@Transactional(readOnly = true)
	public String getUidByLevel(String groupName){
		return stufftAndAbilityDao.getUidByLevel(groupName);
	}
	
	public boolean delete(StufftAndAbility stufftAndAbility){
		boolean flag = false;
		try {
			stufftAndAbilityDao.removeObject(StufftAndAbility.class, stufftAndAbility.getId());
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	

	public boolean update(StufftAndAbility stufftAndAbility){
		boolean flag=false;
		try{
			stufftAndAbilityDao.updateObject(stufftAndAbility);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	public StufftAndAbility getCaseById(String id){
		return (StufftAndAbility)stufftAndAbilityDao.getObject(StufftAndAbility.class, id);
	}
	
	public boolean upEdit_lock(String month_date){
		return stufftAndAbilityDao.upEdit_lock(month_date);
	}
	//String createDate, String createMan,String updateman,String category,String ano,int pageNum, int pageSize,String raiseEndDate
	@Transactional(readOnly = true)
	public Page getPage(String groupname,String username,int pageNum, int pageSize,String month) {
		return stufftAndAbilityDao.getPage(groupname,username,pageNum,pageSize,month);
	}
	
	
	@Transactional(readOnly = true)
	public List<Object> getNames(String groupName) {
		return stufftAndAbilityDao.getUsernamesByGroup(groupName);
	}
	
	
	@Transactional(readOnly=true)
	public String getNextNo(){
		return stufftAndAbilityDao.getNextNo();
	}
	
	@Transactional(readOnly=true)
	public String getUserGroup_name(String username){
		return stufftAndAbilityDao.getUserGroup_name(username);
	}
	
	@Transactional(readOnly=true)
	public String getUserEffect_score(String username,String month_date){
		return stufftAndAbilityDao.getUserEffect_score(username,month_date);
	}
	
	@Transactional(readOnly=true)
	public String getUserPrice_score(String username,String month_date){
		return stufftAndAbilityDao.getUserPrice_score(username,month_date);
	}
	
	@Transactional(readOnly=true)
	public String getUserEffect_num(String username,String month_date){
		return stufftAndAbilityDao.getUserEffect_num(username,month_date);
	}
	
	@Transactional(readOnly=true)
	public String getUserPrice_num(String username,String month_date){
		return stufftAndAbilityDao.getUserPrice_num(username,month_date);
	}
	
	@Transactional(readOnly=true)
	public String getHeadmanAddValue(String username,String month_date){
		return stufftAndAbilityDao.getHeadmanAddValue(username,month_date);
	}
	
	@Transactional(readOnly=true)
	public String getFellowValue(String username,String month_date){
		return stufftAndAbilityDao.getFellowValue(username,month_date);
	}
	
	@Transactional(readOnly=true)
	public String getUserMeetingUnAudit_num(String group_name,String month_date){
		return stufftAndAbilityDao.getUserMeetingUnAudit_num(group_name,month_date);
	}
	
	@Transactional(readOnly=true)
	public String getUserTrainStride_num(String username,String month_date){
		return stufftAndAbilityDao.getUserTrainStride_num(username,month_date);
	}
	
	@Transactional(readOnly=true)
	public String getUserTrainInner_num(String username,String month_date){
		return stufftAndAbilityDao.getUserTrainInner_num(username,month_date);
	}
	
	@Transactional(readOnly = true)
	public int getReportStatics(String username,String month_date,Date date2) {
		List<DayReportStatic> list = stufftAndAbilityDao.dayStatic(month_date);// 获得前四项数据
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
						str = stufftAndAbilityDao.getUnwritePersons(date); // 获得未填写的人员
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
		return stufftAndAbilityDao.getUseredit_lock(month_date);
	}
	
	@Transactional(readOnly=true)
	public boolean getUserMonth_date(String month_date){
		return stufftAndAbilityDao.getUserMonth_date(month_date);
	}
	
	@Transactional(readOnly=true)
	public String getUserlevel(String username){
		return stufftAndAbilityDao.getUserlevel(username);
	}
	
	@Transactional(readOnly = true)
	public List<String> getAllNames(){
		
		return stufftAndAbilityDao.getAllNames();
	}
	
	@Transactional(readOnly = true)
	public List<StufftAndAbility> getStufftAndAbilityByPno(String pno) {
		return stufftAndAbilityDao.selectStufftAndAbility(pno);
	}
	
}
