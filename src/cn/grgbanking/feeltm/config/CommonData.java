package cn.grgbanking.feeltm.config;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.commonPlatform.monthlyManage.service.MonthlyManageService;
import cn.grgbanking.feeltm.cardRecord.service.CardRecordService;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.personDay.bean.ProjectPersonDayBean;
import cn.grgbanking.feeltm.personDay.domain.PersonDay;
import cn.grgbanking.feeltm.personDay.service.PersonDayService;
import cn.grgbanking.feeltm.personDay.service.ProjectPersonDayStatisticService;
import cn.grgbanking.feeltm.project.domain.Project;
import cn.grgbanking.feeltm.project.service.ProjectService;

@Service
@Transactional
public class CommonData {
	@Autowired
	private ProjectPersonDayStatisticService personDayStatisticService;
	@Autowired
	private PersonDayService personDayService;
	@Autowired
	private MonthlyManageService monthlyManageService;
	@Autowired
	private CardRecordService cardRecordService;
	@Autowired
	private ProjectService projectService;

	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	
	//人日统计数据（key为年，如"2014"）
	public static Map<String,List<ProjectPersonDayBean>> personDayByYear=new HashMap<String,List<ProjectPersonDayBean>>();
	
	public void loadDataInServerStart(){
		Timer timer = new Timer();
        timer.schedule(new TimerTask() {
			@Override
			public void run() {
				try{
					//initialToTablePersonDay();//初始化10年的空白数据
				}catch(Exception e){
					e.printStackTrace();
				}
			}
        }, 100);
	}

	/**初始化进人日统计表，初始化10年的空白数据
	 * wtjiao 2014年10月30日 上午9:48:57
	 */
	private void initialToTablePersonDay() {
		int beginYear=Calendar.getInstance().get(Calendar.YEAR);
		for(int i=0;i<10;i++){
			try{
				int calYear=beginYear+i*(-1);
				List<Project> projects=projectService.listUnFinishedGroup();
				for(int j=0;j<projects.size();j++){
					Project p=projects.get(j);
					for(int k=1;k<=12;k++){
						PersonDay pd=new PersonDay();
						pd.setYear(calYear+"");
						pd.setError("0");
						pd.setEstimateDetail("");
						pd.setIsEdit("false");
						pd.setNote("");
						pd.setPersonDayConfirm("0");
						pd.setPersonDayEdit("0");
						pd.setProjectId(p.getId());
						pd.setProjectName(p.getName());
						pd.setType("month");
						if(k<=9){
							pd.setMonth("0"+k);
						}else{
							pd.setMonth(k+"");
						}
						pd.setExt1(pd.getYear()+"-"+pd.getMonth());
						personDayService.save(pd);
					}
				}
				
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
	}
	
	/**删除项目时，同时删除对应项目的人日统计数据
	 * @param projectId
	 */
	public void removePersonDayByProjectId(String projectId){
		personDayService.removeByProjectId(projectId);
	}
	
	/**
	 * 人日数据统计
	 */
	public void countPersonDay(){
		SysLog.info("人日数据统计开始:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		boolean b1=personDayService.projectPersonDayStatisticByMonth(null);
		//boolean b2= personDayService.projectPersonDayErrorStatisticByMonth(null);
		boolean ret = true;
		if (ret) {
			SysLog.info("人日数据统计结束:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		}else {
			SysLog.error("人日数据统计报错:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		}
	}
	/**
	 * 月度管理统计
	 */
	public void executeMonthlyManage(){
		SysLog.info("月度管理统计开始:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		boolean ret = monthlyManageService.executeMonthlyManage();
		if (ret) {
			SysLog.info("月度管理统计结束:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		}else {
			SysLog.error("月度管理统计报错:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		}
	}
	
	/**
	 * 每天凌晨统计更新考勤表的来自EHR和广发的考勤状态
	 */
	public void updateEHRGFAttendanceStatus(){
		SysLog.info("EHR和广发的考勤状态更新开始:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		boolean ret = cardRecordService.updateEHRGFAttendanceStatus(1);
		if (ret) {
			SysLog.info("EHR和广发的考勤状态更新结束:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		}else {
			SysLog.error("EHR和广发的考勤状态更新报错:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		}
	}
	/**
	 * 每日在定时器中更新当月部门人日消耗。
	 */
	public void countDeptMonthPersonDay(){
		//原来的每天统计部门人日作废，统一从成本控制--部门人日明细中统计，参考LeaderHomePageService中的getDeptMonthPersonDayInfos方法
		/*
		SysLog.info("每日在定时器中更新当月部门人日消耗开始:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		boolean ret = personDayService.savePersonDayByDept();
		if (ret) {
			SysLog.info("每日在定时器中更新当月部门人日消耗结束:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		}else {
			SysLog.error("每日在定时器中更新当月部门人日消耗报错:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		}
		 */
		
	}
}
