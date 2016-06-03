package cn.grgbanking.feeltm.admintool.service;
/**
 * 管理工具
 * @author xing 
 */
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.commonPlatform.monthlyManage.constants.MonthlyManagerConstants;
import cn.grgbanking.commonPlatform.monthlyManage.dao.MonthlyManagerDao;
import cn.grgbanking.commonPlatform.monthlyManage.domain.MonthlyManager;
import cn.grgbanking.feeltm.admintool.dao.AdminToolDao;
import cn.grgbanking.feeltm.cardRecord.dao.CardRecordDao;
import cn.grgbanking.feeltm.config.BusnDataDir;
import cn.grgbanking.feeltm.dayLog.dao.DayLogDao;
import cn.grgbanking.feeltm.dayLog.domain.DayLog;
import cn.grgbanking.feeltm.domain.SysUser;
import cn.grgbanking.feeltm.domain.testsys.TrainingRecord;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.personDay.dao.PersonDayDao;
import cn.grgbanking.feeltm.personDay.domain.DeptMonthPersonDay;
import cn.grgbanking.feeltm.personDay.domain.PersonDay;
import cn.grgbanking.feeltm.personDay.service.PersonDayService;
import cn.grgbanking.feeltm.prjMonitor.dao.ColorSettingDao;
import cn.grgbanking.feeltm.project.dao.ProjectDao;
import cn.grgbanking.feeltm.project.dao.ProjectResourcePlanDao;
import cn.grgbanking.feeltm.project.domain.Project;
import cn.grgbanking.feeltm.staff.dao.StaffInfoDao;
import cn.grgbanking.feeltm.study.dao.TrainDao;
import cn.grgbanking.feeltm.util.DateUtil;
import cn.grgbanking.framework.service.BaseService;

@Service
@Transactional
public class AdminToolService extends BaseService{
	@Autowired
	private StaffInfoDao staffInfoDao;
	@Autowired
	private CardRecordDao cardRecordDao;
	@Autowired
	private MonthlyManagerDao monthlyManagerDao;
	@Autowired
	private ColorSettingDao colorSettingDao;
	@Autowired
	private TrainDao trainDao;
	
	@Autowired
	private PersonDayService personDayService;
	/** 在职总人数 */
	private int staffCount = 0;
	/** 项目总数 */
	private int projectCount = 0;
	/**合同总金额 */
	private BigDecimal sumAmount = new BigDecimal(0);
	/**合同完成率 */
	private BigDecimal finishRate = new BigDecimal(0);
	/** 执行时间  **/
	private String operateTime;
	
	
	@Autowired
	PersonDayDao personDayDao;
	@Autowired
	private DayLogDao daylogDao;
	@Autowired
	private AdminToolDao adminToolDao;
	@Autowired
	private ProjectDao projectDao;
	@Autowired
	private ProjectResourcePlanDao projectResourcePlanDao;
	/** 用户ID */
	private static final String ERROR_RESULT = "result";
	/** 用户ID */
	private static final String ERROR_COUNT = "count";
	/** 用户ID */
	private static final String ERROR_USER_ID = "userId";
	/** 用户名 */
	private static final String ERROR_USER_NAME = "userName";
	/** 未填写日志的日期 */
	private static final String ERROR_NO_LOG_DATE = "noLogDate";
	//***************************************************start**********************************************//
	//******************************************start***月度管理统计****start********************************//
	//***************************************************start*********************************************//
	/**
	 * 月度管理统计
	 */
	public void executeMonthlyManage(String monthlyTime){
		SysLog.info("月度管理统计开始:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		operateTime = monthlyTime;
		boolean ret = handleMonthlyManage();
		if (ret) {
			SysLog.info("月度管理统计结束:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		}else {
			SysLog.error("月度管理统计报错:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		}
	}
	
	/**
	 * 月度管理统计分析
	 * 
	 * @return
	 */
	public boolean  handleMonthlyManage(){
		try {
			// 上一个月的年月份
			String year = operateTime.substring(0, 4);
			String month = operateTime.substring(4, 6);
			//月度考勤状态的数据
			List<Object> list = cardRecordDao.getMonthlyAttendance(operateTime);
			//月度管理报告信息表
			MonthlyManager monthlyManager = new MonthlyManager();
			String jsonObj = "";
			//判断是否存在当月的记录
			MonthlyManager result = monthlyManagerDao.getMonthDataByYearMonth(year,month);
			boolean insert = true;
			if (result != null) {
				monthlyManager = result;
				insert = false;
			}
			//设值更新
			monthlyManager.setYear(year);
			monthlyManager.setMonth(month);
			//按天数，部门，考勤状态分组统计每天各部门的迟到.早退人数
			jsonObj = summaryDetailData(list);
			monthlyManager.setAttendanceInfo(jsonObj);
			//部门月度信息(部门总人数.参加培训人数.异常考勤人数)
			jsonObj = summaryDeptInfo();
			monthlyManager.setDeptInfo(jsonObj);
			//项目情况
			jsonObj = prjSituation();
			monthlyManager.setProjectInfo(jsonObj);
			//合同统计情况
			jsonObj = prjContractSumByMonth();
			monthlyManager.setContractInfo(jsonObj);
			//优秀员工
			jsonObj = getGoodEmployee();
			monthlyManager.setGoodUserInfo(jsonObj);
			jsonObj = getLeaveEntryInfoByMonth();
			monthlyManager.setStaffChangeInfo(jsonObj);
			//月度管理统计综合数
			JSONObject totalInfoJson = new JSONObject();
			totalInfoJson.put(MonthlyManagerConstants.SUMAMOUNT, sumAmount);
			totalInfoJson.put(MonthlyManagerConstants.FINISHRATE, finishRate);
			totalInfoJson.put(MonthlyManagerConstants.STAFF_TOTAL, staffCount);
			totalInfoJson.put(MonthlyManagerConstants.PROJECT_TOTAL, projectCount);
			int trainTotal = 0;
			trainTotal = this.getMonthTrainInfo();
			totalInfoJson.put(MonthlyManagerConstants.TRAIN_STAFF_TOTAL, trainTotal);
			monthlyManager.setTotalInfo(totalInfoJson.toString());
			if (insert) {
				monthlyManagerDao.addObject(monthlyManager);
			} else {
				monthlyManagerDao.updateObject(monthlyManager);
			}
		} catch (Exception e) {
			SysLog.error("执行中有异常：" + e.toString());
			return false;
		}
		return true;
	}
	
	/**
	 * 按天数，部门，考勤状态分组统计每天各部门的迟到.早退人数
	 * @param list SQL查询结果
	 * @return  分组统计结果（JSON）
	 */
	private String summaryDetailData(List<Object> list){
		List<Object> groupInfoJsonList = new ArrayList<Object>();
		try {
			//数据为空时直接返回
			if (list == null || list.size() == 0) {
				return "";
			}
			//取第一个元素作为基准
			Object[] tempObj = (Object[])list.get(0);
			String tempAttendanceDate = "";
			String tempDeptName = "";
			tempAttendanceDate = (String)tempObj[0];
			tempDeptName = ((String)tempObj[1]).trim();
			//初始化变量
			JSONObject infoGroupbyDeptJson = new JSONObject();
			List<Object> infoGrpbyDeptJsonList = new ArrayList<Object>();
			JSONObject infoGroupbyDayJson = new JSONObject();
			for (int i = 0; i < list.size(); i++) {
				//遍历逐个取数据
				Object[] record = (Object[])list.get(i);
				String attendanceDate = (String)record[0];
				String deptName = ((String)record[1]).trim();
				//考勤状态（迟到.早退）
				String statusStr = record[2].toString();
				int status =  Integer.valueOf(statusStr);
				//迟到.早退人数
				int cntStatus = Integer.valueOf(record[3].toString());
				//按考勤日期分组统计
				if (tempAttendanceDate.compareTo(attendanceDate) == 0) {
					//按考勤部门分组统计
					if (tempDeptName.equals(deptName)) {
						//判断考勤状态（迟到.早退），取值
						switch (status) {
						case MonthlyManagerConstants.ATTENDACE_LATE_KEY:
							infoGroupbyDeptJson.put(MonthlyManagerConstants.LATE_NUM, cntStatus);
							break;
						case MonthlyManagerConstants.ATTENDACE_EARLY_KEY:
							infoGroupbyDeptJson.put(MonthlyManagerConstants.ABSENT_NUM, cntStatus);
							break;
						default:
							break;
						}
					}else{
						//保存上一次的部门名
						infoGroupbyDeptJson.put(MonthlyManagerConstants.DEPT_NAME, tempDeptName);
						//保存内容：部门名迟到.早退人数
						infoGrpbyDeptJsonList.add(infoGroupbyDeptJson);
						//将本次的部门覆盖上一次的部门
						tempDeptName = deptName;
						//再次初始化
						infoGroupbyDeptJson = new JSONObject();
						//判断考勤状态（迟到.早退），取值
						switch (status) {
						case MonthlyManagerConstants.ATTENDACE_LATE_KEY:
							infoGroupbyDeptJson.put(MonthlyManagerConstants.LATE_NUM, cntStatus);
							break;
						case MonthlyManagerConstants.ATTENDACE_EARLY_KEY:
							infoGroupbyDeptJson.put(MonthlyManagerConstants.ABSENT_NUM, cntStatus);
							break;
						default:
							break;
						}
					}
				}else{
					//取天
					int day = Integer.valueOf(DateUtil.stringyyy_mm_ddToyyyyMMdd(tempAttendanceDate).substring(6));
					infoGroupbyDayJson.put(MonthlyManagerConstants.DAY, day);
					//保存上一次的部门名
					infoGroupbyDeptJson.put(MonthlyManagerConstants.DEPT_NAME, tempDeptName);
					//保存内容：部门名，迟到.早退人数
					infoGrpbyDeptJsonList.add(infoGroupbyDeptJson);
					//将当天的各部门的迟到早退的人数归结起来
					infoGroupbyDayJson.put(MonthlyManagerConstants.DEPT_DETAIL, infoGrpbyDeptJsonList);
					//将所有日期的迟到早退情况归结起来
					groupInfoJsonList.add(infoGroupbyDayJson);
					//再次初始化
					infoGroupbyDayJson = new JSONObject();
					infoGrpbyDeptJsonList = new ArrayList<Object>();
					tempAttendanceDate = attendanceDate;
					tempDeptName = deptName;
					infoGroupbyDeptJson = new JSONObject();
					//判断考勤状态（迟到.早退），取值
					switch (status) {
					case MonthlyManagerConstants.ATTENDACE_LATE_KEY:
						infoGroupbyDeptJson.put(MonthlyManagerConstants.LATE_NUM, cntStatus);
						break;
					case MonthlyManagerConstants.ATTENDACE_EARLY_KEY:
						infoGroupbyDeptJson.put(MonthlyManagerConstants.ABSENT_NUM, cntStatus);
						break;
					default:
						break;
					}
				}
				//最后一条记录的处理
				if (i == (list.size() - 1)) {
					String day = DateUtil.stringyyy_mm_ddToyyyyMMdd(tempAttendanceDate).substring(6);
					infoGroupbyDayJson.put(MonthlyManagerConstants.DAY, day);
					infoGroupbyDeptJson.put(MonthlyManagerConstants.DEPT_NAME, tempDeptName);
					infoGrpbyDeptJsonList.add(infoGroupbyDeptJson);
					infoGroupbyDayJson.put(MonthlyManagerConstants.DEPT_DETAIL, infoGrpbyDeptJsonList);
					groupInfoJsonList.add(infoGroupbyDayJson);
					infoGroupbyDeptJson = new JSONObject();
					infoGrpbyDeptJsonList = new ArrayList<Object>();
				}
			}
			return groupInfoJsonList.toString();
		} catch (JSONException e) {
			SysLog.error("JSON异常:" + e.toString());
			e.printStackTrace();
		}
		return groupInfoJsonList.toString();
	}
	
	/**
	 * 部门月度信息(部门总人数.参加培训人数.异常考勤人数)
	 * @return 人数
	 */
	private String summaryDeptInfo() {
		staffCount = 0;
		List<Object> rslt = staffInfoDao.getStaffsGroupbyDept();
		// 初始化变量
		JSONObject infoGroupbyDeptJson = new JSONObject();
		List<Object> infoGrpbyDeptJsonList = new ArrayList<Object>();
		//获取本月离职人数
		String yyyyMMString = DateUtil.to_char(new Date(), "yyyyMM");
		List<SysUser> leaveUserList = staffInfoDao.getLeaveUserInfo(yyyyMMString);
		for (int i = 0; i < rslt.size(); i++) {
			Object[] record = (Object[]) rslt.get(i);
			String deptKey = record[0].toString().trim();
			// 部门总人数
			int deptCounts = Integer.valueOf(record[1].toString());
			// 取部门名称
			String deptValue = staffInfoDao.getDeptNameValueByKey(deptKey);
			// 取部门设置的颜色
			String deptColor = getDeptColor(deptKey);
			// 在职总人数
			staffCount = staffCount + deptCounts;
			//获取上月yyyyMM
			String lastYearMonth = operateTime;
			//上月迟到总数
			long lateTotal = cardRecordDao.getCountAttendanceStatus(lastYearMonth, MonthlyManagerConstants.ATTENDACE_LATE_KEY, deptValue.trim());
			//上月早退人数
			long absentTotal = cardRecordDao.getCountAttendanceStatus(lastYearMonth, MonthlyManagerConstants.ATTENDACE_EARLY_KEY, deptValue.trim());
			try {
				// 早退总人数
				infoGroupbyDeptJson.put(MonthlyManagerConstants.ABSENT_TOTAL,absentTotal);
				// 迟到总人数
				infoGroupbyDeptJson.put(MonthlyManagerConstants.LATE_TOTAL,lateTotal);
				infoGroupbyDeptJson.put(MonthlyManagerConstants.DEPT_ID,deptKey);
				infoGroupbyDeptJson.put(MonthlyManagerConstants.DEPT_NAME,deptValue);
				infoGroupbyDeptJson.put(MonthlyManagerConstants.DEPT_COLOR,"#" + deptColor);
				infoGroupbyDeptJson.put(MonthlyManagerConstants.DEPT_STAFF_TOTAL, deptCounts);
				infoGrpbyDeptJsonList.add(infoGroupbyDeptJson);
				infoGroupbyDeptJson = new JSONObject();
			} catch (JSONException e) {
				SysLog.error("JSON异常:" + e.toString());
				e.printStackTrace();
			}
		}
		//统计的是上月人数，所有加上本月离职人数
		staffCount = staffCount + leaveUserList.size();
		return infoGrpbyDeptJsonList.toString();
	}
	
	/**
	 * 根据部门ID获得部门配置的颜色
	 * @param deptId 部门ID
	 * @return 颜色
	 */
	private String getDeptColor(String deptId){
		String color = colorSettingDao.getDeptColorById(deptId);
		return color;
	}
	
	/**
	 * 项目情况统计
	 * @return
	 */
	public String prjSituation(){
		String year = operateTime.substring(0, 4);
		String month = operateTime.substring(4, 6);
		List<Project> proList = monthlyManagerDao.getAllProjectByYear(year);
		List<Object> infoGrpbyDeptJsonList = new ArrayList<Object>();
		projectCount = proList.size();
		try 
		{
			for (int i = 0; i < proList.size(); i++) 
			{
				Project project = proList.get(i);
				//根据项目ID和年份上一月份获取上月的记录
				JSONObject infoGroupbyDeptJson = new JSONObject();
				PersonDay personDay = monthlyManagerDao.getLastPersonDay(year, project.getId(),month);
				//人日数据是否修改过
				if (personDay == null) {
					infoGroupbyDeptJson.put(MonthlyManagerConstants.PERSONDAY,0);
				}
				else 
				{
					if ("true".equals(personDay.getIsEdit())) 
					{
						infoGroupbyDeptJson.put(MonthlyManagerConstants.PERSONDAY,Integer.parseInt(personDay.getPersonDayEdit()));
					}
					else
					{
						infoGroupbyDeptJson.put(MonthlyManagerConstants.PERSONDAY,Integer.parseInt(personDay.getPersonDayConfirm()));
					}
				}
				//项目ID
				infoGroupbyDeptJson.put(MonthlyManagerConstants.PRJ_ID, project.getId());
				//项目名称
				infoGroupbyDeptJson.put(MonthlyManagerConstants.PRJ_NAME,project.getName());
				//获取客户名称
				if (StringUtils.isNotBlank(project.getCustomer())) 
				{
					Map customerMap=BusnDataDir.getMapKeyValue("projectManage.customer");
					String customerVal = BusnDataDir.getValue(customerMap, project.getCustomer());
					infoGroupbyDeptJson.put(MonthlyManagerConstants.CUSTNAME,customerVal);
				}
				else
				{
					infoGroupbyDeptJson.put(MonthlyManagerConstants.CUSTNAME,MonthlyManagerConstants.UNKOWNCUST);
				}
				//风险个数
				//上月的最后一天
				String lastDayStr = ""; 
				int lastDay = DateUtil.getLastDayForYearAndMonth(Integer.parseInt(year), Integer.parseInt(month));
				if (lastDay>=10) {
					lastDayStr = String.valueOf(lastDay);
				}
				else{
					lastDayStr = "0"+String.valueOf(lastDay);
				}
				long riskCount = monthlyManagerDao.getRiskCountByPrjName(year, project.getName(),month,lastDayStr);
				infoGroupbyDeptJson.put(MonthlyManagerConstants.RISKNUM,riskCount);
				//总人日
				infoGrpbyDeptJsonList.add(infoGroupbyDeptJson);
				}
		} catch (JSONException e) 
		{
			e.printStackTrace();
		}
			return infoGrpbyDeptJsonList.toString();
	}
	/**
	 * 获取合同统计
	 * @return
	 */
	public String prjContractSumByMonth(){
		//获取年月
		String yearStr = operateTime.substring(0, 4);
		String monthStr = operateTime.substring(4, 6);
		int month = Integer.parseInt(monthStr);
		List<Object> infoGrpbyDeptJsonList = new ArrayList<Object>();
		sumAmount = new BigDecimal(0);
		finishRate = new BigDecimal(0);
		//统计当月之前的总金额
		try {
		//当前月之前的所有月
			for (int i = 1; i <=month; i++) {
				JSONObject infoGroupbyDeptJson = new JSONObject();
				//上月的最后一天
				String lastDayStr = ""; 
				//获取一个月的最后一天
				int lastDay = DateUtil.getLastDayForYearAndMonth(Integer.parseInt(yearStr), i);
				if (lastDay>=10) {
					lastDayStr = String.valueOf(lastDay);
				}
				else{
					lastDayStr = "0"+String.valueOf(lastDay);
				}
				if (i>=10){
					monthStr = String.valueOf(i);
				}
				else{
					monthStr = "0"+String.valueOf(i);
				}
				//月份
				 infoGroupbyDeptJson.put(MonthlyManagerConstants.MONTH,i);
				//统计一个月的合同金额
				Object obj = monthlyManagerDao.prjContractSum(yearStr,monthStr,lastDayStr);
				if (obj==null){
					 infoGroupbyDeptJson.put(MonthlyManagerConstants.AMOUNT, 0);
				}
				else{
					BigDecimal amount = (BigDecimal) obj;
					infoGroupbyDeptJson.put(MonthlyManagerConstants.AMOUNT, amount);
					sumAmount = sumAmount.add(amount);
				}
			    //组装json
			    infoGrpbyDeptJsonList.add(infoGroupbyDeptJson);
			}
			//合同总额
			if (BusnDataDir.getMapKeyValue("projectManage.contractPlan").get(yearStr)!=null) {
				//从数字字典中读取年度合同计划金额
				String annualContractString =  StringUtils.trim((String)BusnDataDir.getMapKeyValue("projectManage.contractPlan").get(yearStr));
				BigDecimal annualContract = new BigDecimal(annualContractString);
				//合同完成率
				if(!"0".equals(annualContractString.trim())){//加一个判断条件，如果字典中的年度完成金额为0，则完成率默认为0，不做处理，否则下面语句会抛商为0的异常 ling.tu2015.03.05(这里的完成率是0还是100由项目经理确认需求，暂时给0)
					finishRate = sumAmount.divide(annualContract, 2, BigDecimal.ROUND_HALF_UP);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return infoGrpbyDeptJsonList.toString();
	}
	
	 public String getGoodEmployee(){
	    	
	    	/*运通信息/北京开发部   闫小田 
	    	运通信息/测试部   邓海凤 
	    	运通信息/测试部   李萍 
	    	运通信息/测试部   涂梦坤 
	    	运通信息/测试部   廖春旭 
	    	运通信息/开发二部   杨凯（小） 
	    	运通信息/开发二部   刘学 
	    	运通信息/开发二部   曹纪梅 
	    	运通信息/开发二部   颜志军 
	    	运通信息/开发二部   周潮海 
	    	运通信息/开发二部   姚炜 
	    	运通信息/开发二部   吴杰 
	    	运通信息/开发二部   符丰功 
	    	运通信息/人事行政部   陈妙苏 
	    	运通信息/开发三部   姚立军 
	    	运通信息/开发三部   梁学良 
	    	运通信息/开发三部   谈日生 
	    	运通信息/开发三部   李超繁 
	    	运通信息/开发三部   肖绍志 
	    	运通信息/开发三部   韦云超 
	    	运通信息/开发三部   郑彪麟 
	    	运通信息/开发一部   张方晴 
	    	运通信息/开发一部   李燕南 
	    	运通信息/开发一部   柳军庆 
	    	运通信息/开发一部   黄旭林 
	    	运通信息/项目管理部   林俊 
	    	运通信息/项目管理部   唐振华 
	    	运通信息/项目管理部   詹齐亮 
	    	运通信息/项目管理部   浣康 
	    	运通信息/项目管理部   陈巧圆 
	    	运通信息/项目管理部   范恒伟*/
	    	
	    	String GoodEmployeeList= "[{\"userId\":\"lping1\",\"userName\":\"闫小田\",\"deptId\":\"kaifatwo\",\"deptName\":\"北京开发部\",\"duty\":\"软件工程师\",\"imgUrl\":\"\"}"
	    			+",{\"userId\":\"lping1\",\"userName\":\"邓海凤 \",\"deptId\":\"kaifatwo\",\"deptName\":\"测试部\",\"duty\":\"测试经理\",\"imgUrl\":\"\"}"
	    			+",{\"userId\":\"lping1\",\"userName\":\"李萍 \",\"deptId\":\"kaifatwo\",\"deptName\":\"测试部\",\"duty\":\"软件测试工程师\",\"imgUrl\":\"\"}"
	    			+",{\"userId\":\"lping1\",\"userName\":\"涂梦坤 \",\"deptId\":\"kaifatwo\",\"deptName\":\"测试部\",\"duty\":\"软件测试工程师\",\"imgUrl\":\"\"}"
	    			+",{\"userId\":\"lping1\",\"userName\":\"廖春旭 \",\"deptId\":\"kaifatwo\",\"deptName\":\"测试部\",\"duty\":\"软件测试工程师\",\"imgUrl\":\"\"}"
	    			+",{\"userId\":\"lping1\",\"userName\":\"杨凯（小） \",\"deptId\":\"kaifatwo\",\"deptName\":\"开发二部\",\"duty\":\"软件工程师\",\"imgUrl\":\"\"}"
	    			+",{\"userId\":\"lping1\",\"userName\":\"刘学 \",\"deptId\":\"kaifatwo\",\"deptName\":\"开发二部\",\"duty\":\"软件工程师\",\"imgUrl\":\"\"}"
	    			+",{\"userId\":\"lping1\",\"userName\":\"曹纪梅 \",\"deptId\":\"kaifatwo\",\"deptName\":\"开发二部\",\"duty\":\"软件工程师\",\"imgUrl\":\"\"}"
	    			+",{\"userId\":\"lping1\",\"userName\":\"颜志军 \",\"deptId\":\"kaifatwo\",\"deptName\":\"开发二部\",\"duty\":\"软件工程师\",\"imgUrl\":\"\"}"
	    			+",{\"userId\":\"lping1\",\"userName\":\"周潮海 \",\"deptId\":\"kaifatwo\",\"deptName\":\"开发二部\",\"duty\":\"软件工程师\",\"imgUrl\":\"\"}"
	    			+",{\"userId\":\"lping1\",\"userName\":\"姚炜 \",\"deptId\":\"kaifatwo\",\"deptName\":\"开发二部\",\"duty\":\"软件工程师\",\"imgUrl\":\"\"}"
	    			+",{\"userId\":\"lping1\",\"userName\":\"吴杰 \",\"deptId\":\"kaifatwo\",\"deptName\":\"开发二部\",\"duty\":\"软件工程师\",\"imgUrl\":\"\"}"
	    			+",{\"userId\":\"lping1\",\"userName\":\"符丰功 \",\"deptId\":\"kaifatwo\",\"deptName\":\"开发二部\",\"duty\":\"主任\",\"imgUrl\":\"\"}"
	    			+",{\"userId\":\"lping1\",\"userName\":\"陈妙苏 \",\"deptId\":\"kaifatwo\",\"deptName\":\"人事行政部\",\"duty\":\"人力资源主管\",\"imgUrl\":\"\"}"
	    			+",{\"userId\":\"lping1\",\"userName\":\"姚立军 \",\"deptId\":\"kaifatwo\",\"deptName\":\"开发三部\",\"duty\":\"软件工程师\",\"imgUrl\":\"\"}"
	    			+",{\"userId\":\"lping1\",\"userName\":\"梁学良 \",\"deptId\":\"kaifatwo\",\"deptName\":\"开发三部\",\"duty\":\"软件工程师\",\"imgUrl\":\"\"}"
	    			+",{\"userId\":\"lping1\",\"userName\":\"谈日生 \",\"deptId\":\"kaifatwo\",\"deptName\":\"开发三部\",\"duty\":\"软件工程师\",\"imgUrl\":\"\"}"
	    			+",{\"userId\":\"lping1\",\"userName\":\"李超繁 \",\"deptId\":\"kaifatwo\",\"deptName\":\"开发三部\",\"duty\":\"软件工程师\",\"imgUrl\":\"\"}"
	    			+",{\"userId\":\"lping1\",\"userName\":\"肖绍志 \",\"deptId\":\"kaifatwo\",\"deptName\":\"开发三部\",\"duty\":\"软件工程师\",\"imgUrl\":\"\"}"
	    			+",{\"userId\":\"lping1\",\"userName\":\"韦云超 \",\"deptId\":\"kaifatwo\",\"deptName\":\"开发三部\",\"duty\":\"软件工程师\",\"imgUrl\":\"\"}"
	    			+",{\"userId\":\"lping1\",\"userName\":\"郑彪麟 \",\"deptId\":\"kaifatwo\",\"deptName\":\"开发三部\",\"duty\":\"软件工程师\",\"imgUrl\":\"\"}"
	    			+",{\"userId\":\"lping1\",\"userName\":\"张方晴 \",\"deptId\":\"kaifatwo\",\"deptName\":\"开发一部\",\"duty\":\"软件工程师\",\"imgUrl\":\"\"}"
	    			+",{\"userId\":\"lping1\",\"userName\":\"李燕南 \",\"deptId\":\"kaifatwo\",\"deptName\":\"开发一部\",\"duty\":\"软件工程师\",\"imgUrl\":\"\"}"
	    			+",{\"userId\":\"lping1\",\"userName\":\"柳军庆 \",\"deptId\":\"kaifatwo\",\"deptName\":\"开发一部\",\"duty\":\"软件工程师\",\"imgUrl\":\"\"}"
	    			+",{\"userId\":\"lping1\",\"userName\":\"黄旭林 \",\"deptId\":\"kaifatwo\",\"deptName\":\"开发一部\",\"duty\":\"软件工程师\",\"imgUrl\":\"\"}"
	    			+",{\"userId\":\"lping1\",\"userName\":\"林俊 \",\"deptId\":\"kaifatwo\",\"deptName\":\"项目管理部\",\"duty\":\"部门经理\",\"imgUrl\":\"\"}"
	    			+",{\"userId\":\"lping1\",\"userName\":\"唐振华 \",\"deptId\":\"kaifatwo\",\"deptName\":\"项目管理部\",\"duty\":\"项目经理\",\"imgUrl\":\"\"}"
	    			+",{\"userId\":\"lping1\",\"userName\":\"詹齐亮 \",\"deptId\":\"kaifatwo\",\"deptName\":\"项目管理部\",\"duty\":\"项目经理\",\"imgUrl\":\"\"}"
	    			+",{\"userId\":\"lping1\",\"userName\":\"浣康 \",\"deptId\":\"kaifatwo\",\"deptName\":\"项目管理部\",\"duty\":\"项目经理\",\"imgUrl\":\"\"}"
	    			+",{\"userId\":\"lping1\",\"userName\":\"陈巧圆 \",\"deptId\":\"kaifatwo\",\"deptName\":\"项目管理部\",\"duty\":\"项目经理\",\"imgUrl\":\"\"}"
	    			+",{\"userId\":\"lping1\",\"userName\":\"范恒伟\",\"deptId\":\"kaifatwo\",\"deptName\":\"项目管理部\",\"duty\":\"项目经理\",\"imgUrl\":\"\"}]";
	    	return GoodEmployeeList;
	    	
	    }
	 /**
		 * 获取月度人员变动信息
		 * @return JSON信息
		 */
		private String getLeaveEntryInfoByMonth(){
			String preYearMonth = operateTime;
			String month = preYearMonth.substring(4,6);
			int monthCnt = Integer.parseInt(month);
			int leaveConut = 0;
			int entryConut = 0;
			// 初始化变量
			JSONObject infoGroupbyMonthJson = new JSONObject();
			List<SysUser> leaveList = staffInfoDao.getLeaveUserInfo(preYearMonth);
			List<SysUser> entryList = staffInfoDao.getEntryUserInfo(preYearMonth);
			leaveConut = leaveList.size();
			entryConut = entryList.size();
			try {
				infoGroupbyMonthJson.put(MonthlyManagerConstants.MONTH, monthCnt);
				infoGroupbyMonthJson.put(MonthlyManagerConstants.TOTAL_STAFF_NUM, 0);
				infoGroupbyMonthJson.put(MonthlyManagerConstants.NEW_STAFF, entryConut);
				infoGroupbyMonthJson.put(MonthlyManagerConstants.LEAVE_STAFF, leaveConut);
				//infoGroupbyMonthJson = new JSONObject();
			} catch (JSONException e) {
				SysLog.error("JSON异常:" + e.toString());
				e.printStackTrace();
			}
			return infoGroupbyMonthJson.toString();
		}
		/**
		 * 月参加培训人信息
		 * @return 培训次数
		 */
		private int getMonthTrainInfo(){
			List<TrainingRecord> list = trainDao.getTrainInfo(operateTime);
			int count = 0 ;
	/* //	           培训人次
	  		List<String> listTemp = new ArrayList<String>();
			for (TrainingRecord trainingRecord : list) {
				String[] trains = trainingRecord.getStudent().split(",");
				listTemp.addAll(Arrays.asList(trains));
			}
			Set<String> set  = new HashSet<String>();
			for (String temp : listTemp) {
				set.add(temp);
			}
			count = set.size();*/
			count = list.size();
			return count;
		}
		
		/**
		 * 人日数据统计
		 */
		public void countPersonDay(String month){
			SysLog.info("人日数据统计开始:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			boolean b1=personDayService.projectPersonDayStatisticByMonth(month);
			//boolean b2= personDayService.projectPersonDayErrorStatisticByMonth(month);
			
			boolean ret = true;
			if (ret) {
				SysLog.info("人日数据统计结束:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			}else {
				SysLog.error("人日数据统计报错:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			}
		}
		
		/**
		 * 在数据库中初始录入近10年所有项目的人日数据
		 */
		public void initialPersonDay(){
			
		}
	
		/**
		 * 初始化人日表实体
		 * @param dayLog 日志表实体
		 * @return 人日表实体
		 */
		private PersonDay initPersonDay(DayLog dayLog){
			PersonDay personDay = new PersonDay();
			personDay.setProjectId(dayLog.getPrjName());
			personDay.setProjectName(dayLog.getGroupName());
			personDay.setIsEdit("false");
			personDay.setType("month");
			String logDate = DateUtil.getTimeYYYYMMDDHHMMString(dayLog.getLogDate());
			String year = logDate.substring(0, 4);
			String month = logDate.substring(4, 6);
			personDay.setYear(year);
			personDay.setMonth(month);
			return personDay;
		}
		/**
		 * 保存或者更新表内容
		 * @param personDay 人日表
		 */
		private void saveOrUpdateData(PersonDay personDay){
			String projectId = personDay.getProjectId();
			String year = personDay.getYear();
			String month = personDay.getMonth();
			PersonDay existPersonDay = personDayDao.existPersonDay(projectId, year, month);
			if (existPersonDay != null) {
				existPersonDay.setPersonDayConfirm(personDay.getPersonDayConfirm());
				//人日误差
				existPersonDay.setError(personDay.getError());
				//人日误差明细
				existPersonDay.setEstimateDetail(personDay.getEstimateDetail());
				existPersonDay.setUpdateTime(new Date());
				existPersonDay.setUpdateuserId("system-timer");
				existPersonDay.setUpdateUsername("system-timer");
				personDayDao.saveOrUpdateData(existPersonDay);
			} else {
				personDay.setUpdateTime(new Date());
				personDay.setUpdateuserId("system-timer");
				personDay.setUpdateUsername("system-timer");
				personDayDao.saveOrUpdateData(personDay);
			}
		}
		//***************************************************end**********************************************//
		//******************************************end***人日数据统计****end********************************//
		//***************************************************end*********************************************//
		
		
		
		//***************************************************start**********************************************//
		//******************************************start***部门人日消耗****start********************************//
		//***************************************************start*********************************************//
		/**
		 * 每日在定时器中更新当月部门人日消耗。
		 */
		public void countDeptMonthPersonDay(String opreateTime){
			SysLog.info("每日在定时器中更新当月部门人日消耗开始:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			boolean ret = savePersonDayByDept(opreateTime);
			if (ret) {
				SysLog.info("每日在定时器中更新当月部门人日消耗结束:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			}else {
				SysLog.error("每日在定时器中更新当月部门人日消耗报错:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			}
		}
		
		
		/**
		 * 根据部门划分统计人日<br>
		 * 对日志表的当月数据进行统计，统计的分组条件：部门名<br>
		 * 统计结果保存到oa_personday_dept<br>
		 * @return 统计及结果 true:统计成功  false：统计失败
		 */
		public boolean savePersonDayByDept(String opreateTime) {
			//从日志表中查询当月的数据
			List<DayLog> dayLogStatisticList = adminToolDao.queryDayLogOrderBydept(opreateTime);
			//List<PersonDay> pdlist = new ArrayList<PersonDay>();
			if (dayLogStatisticList != null && dayLogStatisticList.size() > 0) {
				String logDate = "";
				Double workhours = 0d;
				Double personday = 0d;
				Double persondaySum= 0d;
				double standardWorkhour = 8d;
				try {
					//创建一个初始的人日实体
					DeptMonthPersonDay deptMonthPDay = new DeptMonthPersonDay();
					deptMonthPDay= initDeptMonthPersonDay(dayLogStatisticList.get(0));
					for(int i = 0; i < dayLogStatisticList.size(); i++){
						DayLog dayLog = dayLogStatisticList.get(i);
						logDate = DateUtil.getDateString(dayLog.getLogDate());
						//根据用户ID和日志日期查询该用户的当天总工时
						workhours = daylogDao.queryWorkhoursByUserIdDate(dayLog.getUserId(), logDate);
						//人日=当前项目工时/总工时(总工时<8时,当作8)
						personday = dayLog.getConfirmHour() / (workhours >= standardWorkhour ? workhours : standardWorkhour);
						if (StringUtils.isBlank(dayLog.getPrjName())) {
							continue;
						}
						if (dayLog.getDetName().equals(deptMonthPDay.getDeptName())) {
							//如果item的项目名存在则累加统计当前项目的人日
							persondaySum = persondaySum + personday;
						} else {
							//如果不存在则添加一条人日实体,同时将上一次的人日保存到实体类,将本次的统计也存起来
							deptMonthPDay.setPersonDay(String.valueOf(Math.round(persondaySum)));
							//保存到人日表中
							saveOrUpdateDeptMonthPersonDay(deptMonthPDay);
							//pdlist.add(personDayBean);
							persondaySum = personday;
							deptMonthPDay= initDeptMonthPersonDay(dayLog);
						}
						//最后一条的处理
						if (i == (dayLogStatisticList.size()-1)) {
							deptMonthPDay.setPersonDay(String.valueOf(Math.round(persondaySum)));
							//保存到人日表中
							saveOrUpdateDeptMonthPersonDay(deptMonthPDay);
							//pdlist.add(personDayBean);
							persondaySum = 0d;
						}
					}
				} catch (Exception e) {
					System.out.println("部门人日统计报错");
					return false;
				}
			}
			return true;
		}
		
		/**
		 * 初始化oa_personday_dept表实体
		 * @param dayLog 日志表实体
		 * @return mon
		 */
		private DeptMonthPersonDay initDeptMonthPersonDay(DayLog dayLog){
			DeptMonthPersonDay deptMonthPersonDay = new DeptMonthPersonDay();
			//根据部门名称获取部门Id
			Map<String,Object> deptMap=BusnDataDir.getMapKeyObject("staffManager.department");
			Set<String> key = deptMap.keySet();        
		   for (Iterator it = key.iterator(); it.hasNext();)
		   { 
			   	String keystr = (String) it.next(); 
			   String  keyValue = BusnDataDir.getMapKeyValue("staffManager.department").get(keystr).toString().trim();
			   if (keyValue.equals(dayLog.getDetName())) {
				   deptMonthPersonDay.setDeptId(keystr);
				   break;
			}
		   }
			deptMonthPersonDay.setDeptName(dayLog.getDetName());
			String logDate = DateUtil.getTimeYYYYMMDDHHMMString(dayLog.getLogDate());
			String year = logDate.substring(0, 4);
			String month = logDate.substring(4, 6);
			deptMonthPersonDay.setYear(year);
			deptMonthPersonDay.setMonth(month);
			deptMonthPersonDay.setCreateTime(new Date());
			return deptMonthPersonDay;
		}
		/**
		 * 保存或者更新表内容
		 * @param personDay 人日表
		 */
		private void saveOrUpdateDeptMonthPersonDay(DeptMonthPersonDay deptMonthPDay){
			String deptName = deptMonthPDay.getDeptName();
			String year = deptMonthPDay.getYear();
			String month = deptMonthPDay.getMonth();
			DeptMonthPersonDay existDeptMonthPersonDay = personDayDao.existDeptMonthPersonDay(deptName, year, month);
			if (existDeptMonthPersonDay != null) {
				existDeptMonthPersonDay.setPersonDay(deptMonthPDay.getPersonDay());
				existDeptMonthPersonDay.setCreateTime(new Date());
				personDayDao.saveOrUpdateDeptMonthPersonDay(existDeptMonthPersonDay);
			} else {
				personDayDao.saveOrUpdateDeptMonthPersonDay(deptMonthPDay);
			}
		}
}
