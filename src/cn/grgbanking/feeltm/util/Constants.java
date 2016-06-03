package cn.grgbanking.feeltm.util;

import java.util.HashMap;

@SuppressWarnings("unchecked")
public final class Constants {

	public final static String LOGIN_USER_KEY = "tm.loginUser";
	public static HashMap MENU_OPERID_MAP = new HashMap();

	public static int LOGIN_UERR_COUNT = 0;

	/** 功能点各种操作 */
	public static String OPER_ADD_VALUE = "1"; // 新增
	public static String OPER_MODIFY_VALUE = "2"; // 修改
	public static String OPER_DELETE_VALUE = "3"; // 删除
	public static String OPER_PWRESET_VALUE = "32"; // 密码重置

	public static void setMENU_OPERID_MAP(String userid, HashMap menuOperMap) {
		MENU_OPERID_MAP.put(userid, menuOperMap);
	}

	// //////////////////////////
	public static final String CURRENT_MENU_ITEM = "currentMenuItem";
	public static final String CURRENT_MENU_ID = "currentMenuId";

	public static final int myRefreshPeriod = 315360000;

	public static String OPER_SEARCH_VALUE = "4"; // 查询

	public static String OPER_NEXTSTEP_VALUE = "50"; // 下一步

	public static String OPER_SYNCHOPARAM_VALUE = "17"; // 同步参数

	public static String OPER_GROUPROLE_VALUE = "18"; // 角色归属组

	public static String OPER_USERGROUP_VALUE = "19"; // 用户归属组

	public static String OPER_REDO_VALUE = "29"; // 重做

	public static String OPER_LOGOUT_VALUE = "31"; // 用户注销

	public static String OPER_VIEW_REPORT = "45"; // 查看报表

	public static String OPER_SHOWDETAIL = "46"; // 明细

	public static String OPER_SHORPRINT = "49"; // 打印

	public static String OPER_SORT = "57"; // 排序

	public static String OPER_STAT_VALUE = "6"; // 统计

	public static String OPER_SUBMIT = "7"; // 确定

	public static String OPER_REFLESH = "8"; // 刷新

	/** 用于分开是出勤时间还是退勤时间的标准分界时间的KEY */
	public static String SEPARATION_TIME_KEY = "saparationTime";
	
	/** 考勤误差秒数的KEY */
	public static String DEVIATION_TIME_KEY = "deviationTime";
	
	/** 考勤:不识别  0 (一般为该项目没有填写规定的上下班时间)*/
	public static int ATTENDACE_UNDEFINED_KEY = 0;
	
	/** 考勤:出勤   1 */
	public static int ATTENDACE_ENTRY_KEY = 1;
	
	/** 考勤:退勤  2 */
	public static int ATTENDACE_EXIT_KEY = 2;
	
	/** 考勤:迟到   3 */
	public static int ATTENDACE_LATE_KEY = 3;

	/** 考勤:早退 4 */
	public static final int ATTENDACE_EARLY_KEY = 4;
	
	/**
     * zzwen6
     * 2016年3月25日8:17:49
     * LEAVE:请假
     */
    public static final String LEAVE = "1";
    
    /**
     * NOT_LEAVE:不是请假
     */
    public static final String NOT_LEAVE = "";
    
    
    /**
     *
     * EIGHT_HOURS: 工时八小时为标准线
     * 2016年3月30日
     * zzwen6
     */
    public static final double EIGHT_HOURS = 8.0;
}
