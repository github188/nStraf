package cn.grgbanking.commonPlatform.monthlyManage.constants;

/**
 * 月度管理报告常量类
 * @author zzhui1
 *
 */
public class MonthlyManagerConstants {
	/** 考勤:不识别 0 */
	public static final int ATTENDACE_UNDEFINED_KEY = 0;

	/** 考勤:出勤 1 */
	public static final int ATTENDACE_ENTRY_KEY = 1;

	/** 考勤:退勤 2 */
	public static final int ATTENDACE_EXIT_KEY = 2;

	/** 考勤:迟到 3 */
	public static final int ATTENDACE_LATE_KEY = 3;

	/** 考勤:早退 4 */
	public static final int ATTENDACE_EARLY_KEY = 4;

	/** 部门ID */
	public static final String DEPT_ID = "deptId";

	/** 部门名 */
	public static final String DEPT_NAME = "deptName";
	
	/** 部门设定颜色 */
	public static final String DEPT_COLOR = "deptColor";
	
	/** 上月部门早退人数 */
	public static final String ABSENT_TOTAL = "absentTotal";

	/** 上月部门迟到人数 */
	public static final String LATE_TOTAL = "lateTotal";
	
	/** 各部门的总人数 */
	public static final String DEPT_STAFF_TOTAL = "staffTotal";
	
	/** 培训次数 */
	public static final String TRAIN_STAFF_TOTAL = "trainTotal";

	/** 早退人数 */
	public static final String ABSENT_NUM = "absentNum";

	/** 迟到人数 */
	public static final String LATE_NUM = "lateNum";
	
	/** 日期(天) */
	public static final String DAY = "day";
	
	/** 月份  */
	public static final String MONTH = "month";
	
	/** 月底总人数 */
	public static final String TOTAL_STAFF_NUM = "totalNum";
	
	/** 月新增人数  */
	public static final String NEW_STAFF = "add";
	
	/** 月离职人数  */
	public static final String LEAVE_STAFF = "leave";
	
	/** 当月每日详情 */
	public static final String DEPT_DETAIL = "deptDetail";
	
	/** 每月人日统计 */
	public static final String PERSONDAY = "personDay";
	
	/** 项目ID */
	public static final String PRJ_ID = "prjId";
	
	/** 项目名称 */
	public static final String PRJ_NAME = "prjName";
	
	/** 客户名称 */
	public static final String CUSTNAME = "custName";
	
	/** 未知客户 */
	public static final String UNKOWNCUST = "未知客户";
	
	/** 风险个数 */
	public static final String RISKNUM = "riskNum";
	
	/** 月度合同金额*/
	public static final String AMOUNT = "amount";
	
	/** 年度合同总金额*/
	public static final String SUMAMOUNT = "contractTotal";
	
	/** 合同完成率*/
	public static final String FINISHRATE = "contractRate";
	
	/** 员工总数*/
	public static final String STAFF_TOTAL = "staffTotal";
	
	/** 项目总数 */
	public static final String PROJECT_TOTAL = "projectTotal";
	
	/** 培训总人数 */
	public static final String TRAIN_TOTAL = "trainTotal";
	
	/** 广发考勤时间 */
	public static final String GF_ENTRY_TIME = "8:30:00";
	
	/** 广发考勤时间 */
	public static final String GF_EXIT_TIME = "17:30:00";
	
	/** 产业园考勤时间 */
	public static final String GRG_ENTRY_TIME = "8:00:00";
	
	/** 产业园考勤时间 */
	public static final String GRG_EXIT_TIME = "17:00:00";
	
	/** 广发上班考勤时间 */
	public static final String GF_ENTRY_TIME_KEY = "GFEntryTime";
	
	/** 广发下班考勤时间 */
	public static final String GF_EXIT_TIME_KEY = "GFExitTime";
	
	/** 产业园上班考勤时间 */
	public static final String GRG_ENTRY_TIME_KEY = "GRGEntryTime";
	
	/** 产业园下班考勤时间 */
	public static final String GRG_EXIT_TIME_KEY = "GRGExitTime";
	
	/** 允许时间误差 */
	public static final String DEVIATION_TIME = "0";
	
	/** 考勤分割点（分割是上班还是下班时间） */
	public static final String SAPARATION_TIME = "13:00:00";
	
	/** 产业园考勤类型(EHR) */
	public static final String GRG_ATTENDANCE = "0";
	
	/** 广发考勤类型 */
	public static final String GF_ATTENDANCE = "1";
	
	/** 移动考勤类型 */
	public static final String MOBILE_ATTENDANCE = "2";
	
}
