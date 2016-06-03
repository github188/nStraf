package cn.grgbanking.feeltm.costControl.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.grgbanking.feeltm.config.BusnDataDir;
import cn.grgbanking.feeltm.costControl.dao.CostControlDao;
import cn.grgbanking.feeltm.costControl.domain.DeptDetailCost;
import cn.grgbanking.feeltm.costControl.domain.DeptGeneralCost;
import cn.grgbanking.feeltm.dayLog.dao.DayLogDao;
import cn.grgbanking.feeltm.dayLog.domain.DayLog;
import cn.grgbanking.feeltm.domain.SysDatadir;
import cn.grgbanking.feeltm.domain.SysUser;
import cn.grgbanking.feeltm.holiday.service.HolidayService;
import cn.grgbanking.feeltm.staff.service.StaffInfoService;
import cn.grgbanking.feeltm.um.dao.SysUserGroupDao;
import cn.grgbanking.feeltm.um.dao.SysUserInfoDao;
import cn.grgbanking.feeltm.util.DateUtil;

/**
 * @author wtjiao
 * 成本统计服务
 */
@Service(value="costStatisticService")
public class CostStatisticService {
	@Autowired
	private CostControlDao costControlDao;
	@Autowired
	private DayLogDao dayLogDao;
	@Autowired
	private StaffInfoService staffInfoService;
	@Autowired
	private HolidayService holidayService;
	@Autowired
	private CostControlService costControlService;
	
	/** 统计昨天部门人日投入数据
	 * @param date 指定日期
	 * @return
	 */
	public boolean statisticYestodayDeptCost(){
		try {
			String yesterday = DateUtil.getPreviousWorkDate();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			//记录日志
			System.out.println("当前时间"+sdf.format(Calendar.getInstance().getTime())+",准备统计"+yesterday+"部门确认人日投入概况及明细");
			Date yt = sdf.parse(yesterday);
			boolean sucess= statisticDeptCost(yt,yt);
			System.out.println(yesterday+"日投入概况及明细统计完毕");
			return sucess;
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**统计某个时间段部门总体人日投入数据
	 * @param fromDate 开始日期
	 * @param toDate 结束日期
	 * @return
	 */
	public boolean statisticDeptCost(Date fromDate,Date toDate){
		try{
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			
			//统计上一个工作日的日志，考虑到部分人员会补填日志，这里把上一个工作日2天前的日志全部重新统计一遍
			Date beginDate=getStatisticBeginDate(fromDate);
			Date endDate=toDate;
			Calendar beginCal=Calendar.getInstance();
			Calendar endCal=Calendar.getInstance();
			beginCal.setTime(beginDate);
			endCal.setTime(endDate);
			
			do{
				//得到指定日期的日志列表
				List daylogList=dayLogDao.queryList(null, sdf.format(beginCal.getTime()), sdf.format(beginCal.getTime()));
				
				//把日志组装成Map<用户id,用户日志列表>对象，方便后续根据用户id找到这个用户的日志
				Map<String,List<DayLog>> userLogMap=generateUserLogMap(daylogList);
				
				//对日志列表进行分析，获取分析后的部门详情统计结果
				List<DeptDetailCost> detailCostList=analysisLogToDetailCost(userLogMap,beginCal.getTime());
				//根据详情再统计部门总体情况
				List<DeptGeneralCost> generalCostList=analysisToDeptGeneralCost(detailCostList);
				
				
				//删除指定日期内的统计数据
				costControlDao.deleteDeptGeneralByDate(beginCal.getTime());//删除总体数据
				costControlDao.deleteDeptDetailByDate(beginCal.getTime());//删除详情数据
				
				//保存新数据
				costControlService.saveDetailCost(detailCostList);
				costControlService.saveGeneralCost(generalCostList);
				
				//往后增加一天
				beginCal.add(Calendar.DAY_OF_YEAR, 1);
			}while(beginCal.before(endCal) || beginCal.compareTo(endCal) == 0);
			
			
			
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	/**分析日志数据，得到部门维度的详细成本情况
	 * @param userLogMap
	 * @param date 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<DeptDetailCost> analysisLogToDetailCost(Map<String, List<DayLog>> userLogMap, Date date) throws Exception{
		//当前日是不是节假日
		boolean isHoliday=holidayService.isHoliday(date);
		//获取要统计的部门
		List<SysDatadir> deptSysDataList=BusnDataDir.getObjectListInOrder("costControl.statisticDept");
		//获取要统计部门中例外的人员
		String expectUserIds=BusnDataDir.getObjectListInOrder("costControl.statisticDeptExpectUserId").get(0).getValue();
		
		//用来存储最后计算出来所有监控部门的人日成本详情
		List<DeptDetailCost> deptDetailCostList=new ArrayList<DeptDetailCost>();
		
		for(int i=0;i<deptSysDataList.size();i++){
			//部门id
			String deptKey=deptSysDataList.get(i).getKey();
			//部门名称
			String deptName=deptSysDataList.get(i).getValue().trim();
			List<SysUser> statisticDeptUsers=staffInfoService.getUserByDeptExpecetTheseUsers(deptKey, expectUserIds);
			//将该部门的人日详情加入总列表中
			deptDetailCostList.addAll(calculateDetailCost(statisticDeptUsers,userLogMap,deptName,date,isHoliday));
		}
		
		
		return deptDetailCostList;
	}


	/**根据部门人员的详情，统计部门总体情况
	 * @param detailCostList
	 * @return
	 */
	private List<DeptGeneralCost> analysisToDeptGeneralCost(List<DeptDetailCost> detailCostList) {
		List<DeptGeneralCost> generalCostList=new ArrayList<DeptGeneralCost>();
		
		//获取要统计的部门
		List<SysDatadir> deptSysDataList=BusnDataDir.getObjectListInOrder("costControl.statisticDept");
		//获取要统计部门中例外的人员
		String[] expectUserIds=(BusnDataDir.getObjectListInOrder("costControl.statisticDeptExpectUserId").get(0).getValue()).split(",");
		if(detailCostList!=null){
			//遍历要统计的部门
			for(int i=0;i<deptSysDataList.size();i++){
				//四组要统计的数
				double totalDeptManager=0D;
				double totalProjectManager=0D;
				double totalNotConfirm=0D;
				double totalNotRegist=0D;
				double totalTraineeCost=0D;
				
				SysDatadir deptSysData=deptSysDataList.get(i);
				//部门总体情况
				DeptGeneralCost generalCost=new DeptGeneralCost();
				String deptName=deptSysData.getValue().trim();
				generalCost.setDeptName(deptName);
				//部门人员
				List<SysUser> sysusers=staffInfoService.getNotLeaveStaffByDeptKey(deptSysData.getKey());
				//部门人数
				generalCost.setDeptMembersNo(sysusers==null?"0":""+sysusers.size());
				int deptExpectUserSize=0;
				//统计各部门不进行统计的人员数
				if(expectUserIds!=null){
					for(int j=0;j<expectUserIds.length;j++){
						for(int k=0;k<sysusers.size();k++){
							if(expectUserIds[j].equals(sysusers.get(k).getUserid())){//在本部门中找到了该人员
								deptExpectUserSize++;
								break;
							}
						}
					}
				}
				//部门统计人数
				try{
					generalCost.setDeptMembersNoStatistic(Integer.parseInt(generalCost.getDeptMembersNo())-deptExpectUserSize+"");
				}catch(Exception e){
					e.printStackTrace();
					generalCost.setDeptMembersNoStatistic(generalCost.getDeptMembersNo());
				}
				
				//遍历人日详情
				for(int k=0;k<detailCostList.size();k++){
					DeptDetailCost detail=detailCostList.get(k);
					if(deptName.equals(detail.getDeptName())){//找到要统计的部门
						generalCost.setStatisticDate(detail.getStatisticDate());//设置统计日期
						//进行统计
						totalDeptManager+=detail.getDeptManagerConfrim();
						totalProjectManager+=detail.getProjectManagerConfirm();
						totalNotConfirm+=detail.getNotConfirm();
						totalNotRegist+=detail.getNotRegist();
						totalTraineeCost+=detail.getTraineeCost();
					}
				}
				
				//将四组统计数据放到部门统计对象中,并四舍五入
				generalCost.setDeptManagerConfirm(Integer.parseInt(Math.round(totalDeptManager)+""));
				generalCost.setProjectManagerConfirm(Integer.parseInt(Math.round(totalProjectManager)+""));
				generalCost.setNotConfirm(Integer.parseInt(Math.round(totalNotConfirm)+""));
				generalCost.setNotRegist(Integer.parseInt(Math.round(totalNotRegist)+""));
				generalCost.setTraineeCost(Integer.parseInt(Math.round(totalTraineeCost)+""));
				
				//非项目比例
				double part1=generalCost.getDeptManagerConfirm()+generalCost.getNotConfirm()+generalCost.getNotRegist();
				double part2=generalCost.getDeptManagerConfirm()+generalCost.getNotConfirm()+generalCost.getNotRegist()+generalCost.getProjectManagerConfirm();
				if(part2<=0){
					generalCost.setNotProjectPercent(0D);
				}else{
					double val=(part1*1.0)/(part2*1.0);
					generalCost.setNotProjectPercent(Math.round(val*10000)/100.0);
				}
				
				//将部门对象放到返回的部门对象列表中
				generalCostList.add(generalCost);
			}
		}
		
		return generalCostList;
	}

	/**把日志组装成Map<用户id,用户日志列表>对象，方便后续根据用户id找到这个用户的日志
	 * @param daylogList
	 * @return
	 */
	private Map<String, List<DayLog>> generateUserLogMap(List daylogList) {
		Map<String, List<DayLog>> userMap=new HashMap<String, List<DayLog>>();
		if(daylogList!=null){
			//遍历日志列表
			for(int i=0;i<daylogList.size();i++){
				DayLog log=(DayLog)daylogList.get(i);
				//用户id
				String userid=log.getUserId();
				//若map中没有该用户的id，就new一个ArrayList，然后把用户和他的日志列表加入进去 
				if(userMap.get(userid)==null){
					List<DayLog> userLogList=new ArrayList<DayLog>();
					userLogList.add(log);
					userMap.put(userid, userLogList);
				}else{
					//若map中存在该用户id，就取出原来的ArrayList,然后在该list中追加上当前遍历的日志
					List<DayLog> userLogList=userMap.get(userid);
					userLogList.add(log);
					userMap.put(userid, userLogList);
				}
			}
		}
		return userMap;
	}


	/**根据部门人员 以及 日期填写情况，确定部门本日的人日成本详情
	 */
	private List<DeptDetailCost> calculateDetailCost(List<SysUser> deptUsers, Map<String, List<DayLog>> userLogMap, String deptName, Date date, boolean isHoliday) throws Exception {
		List<DeptDetailCost> detailCostList=new ArrayList<DeptDetailCost>();
		
		for(int i=0;i<deptUsers.size();i++){
			//用户id
			String userid=deptUsers.get(i).getUserid();
			SysUser usr = staffInfoService.findUserByUserid(userid);
			Date begintime = usr.getGrgBegindate();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String staticstr = sdf.format(date);
			String beginstr = sdf.format(begintime);
			//获取用户的日志
			List<DayLog> userloglist=userLogMap.get(userid);

			//初始化成本详情对象
			DeptDetailCost detailCost=new DeptDetailCost();
			detailCost.setUserId(userid);
			detailCost.setUserName(deptUsers.get(i).getUsername());
			detailCost.setDeptName(deptName);
			detailCost.setStatisticDate(date);
			
			if(userloglist!=null && userloglist.size()>0){//进行日志填写判断，填写了日志,就做统计，不论是否是节假日
				//计算这个员工各个日志项中项目经理确认的总数以及部门经理确认的总数
				double totalDeptManagerConfirm=0D;
				double totalProjectManagerConfirm=0D;
				double totalTraineeCost=0D;
				double totalNotConfrim=0D;
				for(int j=0;j<userloglist.size();j++){
					DayLog log=userloglist.get(j);
					boolean inTraineeUser=inTraineeUsers (log);
					if(log.getProjectDay()==null){//没有确认人日
						System.out.println(log.getUserName()+"("+log.getUserId()+")的"+new SimpleDateFormat("yyyy-MM-dd").format(log.getLogDate())+"日日志确认人日为空");
						//容错处理：一般情况下，用户填写日志后，对应的确认人日默认填入。
						//进入到这个if，说明没有确认人日，那么统计时就根据项目名称来确认，如果是其他项目，则算入部门经理确认，否则算入项目经理确认。并且项目人日和确认人日都按1计算
						if("其他项目".equals(log.getGroupName())){
							log.setEnterRole("deptManager");
							log.setProjectDay(1D);//默认填写人日：1
							log.setEnterDay(1D);//默认确认人日：1
						}else{
							log.setEnterRole("groupManager");
							log.setProjectDay(1D);//默认填写人日：1
							log.setEnterDay(1D);//默认确认人日：1
						}
					}
					if("groupManager".equals(log.getEnterRole())){//项目经理确认的日志项
						//boolean inTraineeUser=inTraineeUsers(getTraineeUsers(), log.getUserId());
						
						if(inTraineeUser){//是实习人员
							totalTraineeCost+=log.getEnterDay();//实习消耗值
						}else{//不是实习人员
							totalProjectManagerConfirm+=log.getEnterDay();//项目经理确认值
						}
						
						//没被项目经理认可的人日=自己填写的人日-项目经理确认的人日
						totalNotConfrim+=log.getProjectDay()-log.getEnterDay();
						
					}else if("deptManager".equals(log.getEnterRole())){//部门经理确认的日志项
						
						if(inTraineeUser){//是实习人员
							totalTraineeCost+=log.getEnterDay();//实习消耗值
						}else{//不是实习人员
							totalDeptManagerConfirm+=log.getEnterDay();//部门经理确认值
						}
						//没被部门经理认可的人日=自己填写的人日-部门经理确认的人日
						totalNotConfrim+=log.getProjectDay()-log.getEnterDay();
					}
					
				}
				//项目经理确人的人日
				detailCost.setProjectManagerConfirm(totalProjectManagerConfirm);
				//部门经理确认的人日
				detailCost.setDeptManagerConfrim(totalDeptManagerConfirm);
				//未被领导认可的人日
				detailCost.setNotConfirm(totalNotConfrim);
				//实习生消耗人日
				detailCost.setTraineeCost(totalTraineeCost);
				//非项目比例
				double part1=detailCost.getDeptManagerConfrim()+detailCost.getNotConfirm()+detailCost.getNotRegist();
				double part2=detailCost.getDeptManagerConfrim()+detailCost.getNotConfirm()+detailCost.getNotRegist()+detailCost.getProjectManagerConfirm();
				
				if(part2<=0){
					detailCost.setNotProjectPercent(0D);
				}else{
					double val=(part1*1.0)/(part2*1.0);
					detailCost.setNotProjectPercent(Math.round(val*10000)/100.0);
				}
			}else if(!isHoliday && (userloglist==null || userloglist.size()==0)){//不是节假日，且没有日志，说明该日的日志没有填写
				//入职日期大于统计日期 date 统计时间
				int isEntry = beginstr.compareTo(staticstr);
				if( isEntry<0){
					detailCost.setNotRegist(1D);//设置未登记工时为1
				}
				
			}
			
			//将生成的人日详情加入列表返回
			detailCostList.add(detailCost);
		}
		
		return detailCostList;
	}
	/**
	 * 判断是否是实习生
	 * @param log 日志
	 * @return
	 */
	private boolean inTraineeUsers(DayLog log) {
		boolean flag = false;
		try {
			String employStatus = log.getEmploystatus();//员工的状态  实习 试用、正式 注意要去掉前后的空格
			//如果员工的状态为实习生  study  返回true
			if( employStatus != null && "study".equals(employStatus)){
				flag =  true;
			}else{
				flag =  false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		return flag;
	}

	/**获取不监控人员列表
	 * @return
	 */
	private String getTraineeUsers() {
		List<SysDatadir> dirs=BusnDataDir.getObjectListInOrder("costControl.traineeNameList");
		if(dirs!=null){
			for(int i=0;i<dirs.size();i++){
				if(dirs.get(i).getKey().equals("trainess")){
					return dirs.get(i).getValue();
				}
			}
		}
		return "";
	}
	
	/**用户是否在实习生名单中
	 * @param traineeUsers 不监控人员列表
	 * @param userid 指定人员
	 * @return
	 */
	private boolean inTraineeUsers(String traineeUsers, String userid) {
		if(StringUtils.isNotBlank(traineeUsers)){
			String[] tus=traineeUsers.split(",");
			for(String trainee:tus){
				if(trainee.equals(userid)){
					return true;
				}
			}
		}
		return false;
	}
	
	private Date getStatisticBeginDate(Date date) {
		 Calendar cal=Calendar.getInstance();
		 cal.setTime(date);
		 cal.add(Calendar.DAY_OF_YEAR, -2);
		return cal.getTime();
	}
}
