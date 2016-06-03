package cn.grgbanking.feeltm.common4Wechat.util;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

public class Common4Wechat {
	/** 返回码 */
	public static final String RETCODE_KEY = "retcode";
	/** 返回信息 */
	public static final String RETMSG_KEY = "retmsg";
	/** oa账号 */
	public static final String NO_KEY = "accountno";
	
	// 账户绑定接口用
	/** 姓名 */
	public static final String NAME_KEY = "username";
	/** 密码 */
	public static final String PWD_KEY = "password";
	/** 工号 */
	public static final String JOBNO_KEY = "jobno";
	/** 邮箱 */
	public static final String EMAIL = "email";
	/** 密码错误次数 */
	public static final String PWD_ERROR_TIMES = "pwderrortime";
	/** 返回码 */
	public static enum CHECK_RESULT {
		/** 默认值 */
		DEFAULT,
		/** 请求参数存在空值 */
		BLANK_PARAM,
		/** 用户不存在 */
		NO_EXIST,
		/** 用户无效被禁用 */
		USER_INVALID,
		/** 密码不匹配 */
		PWD_INVALID,
	}

	// 模板提醒接口用
	/** 用户ID分隔符 */
	public static final String SOCKET_SEPARATE = "|";
	/** 消息类型 */
	public static final String SOCKET_MAG_TYPE = "msgType";
	/** 通知时间 */
	public static final String SOCKET_NOTICE_DATE = "noticeDate";
	/** 账号集合 */
	public static final String SOCKET_ACCOUNTS = "accounts";
	/** 接收成功返回的信息 */
	public static final String SOCKET_RETURN_MSG = "SUCCESS";
	/** 重复发送次数 3次 */
	public static final int SEND_TIMES = 3;
	/** 端口号 */
	//public static final int SOCKET_PORT_NO = 8888;
	/** 一个输出流的最大长度 */
	public static final int OUTPUT_SCREAM_MAX_LEANGTH = 256 * 100;
	/** 编码格式 */
	public static final String CHARSET_NAME = "UTF-8";
	/** 占位符 ? */
	public static final String REPLACE_STR = "?";
	/** 套接字超时时间(30秒) */
	public static final int TIMEOUT_SECOND = 30 * 1000;
	
	//获取个人日志任务接口用的常量
	/** 任务日期 */
	public static final String DAYLOG_TASK_DATE = "taskdate";
	/** 项目情况 */
	public static final String DAYLOG_PROJECT_LIST = "projects";
	/** 项目ID */
	public static final String DAYLOG_PROJECT_ID = "projectid";
	/** 项目名 */
	public static final String DAYLOG_PROJECT_NAME = "projectname";
	/** 项目任务列表 */
	public static final String DAYLOG_PROJECT_TASKS = "tasks";
	/** 计划任务ID */
	public static final String DAYLOG_PROJECT_TASK_ID= "taskid";
	/** 计划任务 */
	public static final String DAYLOG_PROJECT_TASK_NAME = "taskname";
	/** 任务描述 */
	public static final String DAYLOG_PROJECT_TASK_DESCR = "taskdesc";
	/** 日期 */
	public static final String DAYLOG_FILL_DATE = "filldate";
	/** 工时 */
	public static final String DAYLOG_WORK_TIME = "worktime";
	/** 工作类别 */
	public static final String DAYLOG_WORK_TYPE = "worktype";
	/** 状态 */
	public static final String DAYLOG_STATUS = "status";
	/** 完成进度 */
	public static final String DAYLOG_PROCESS = "process";
	/** 日志类型 */
	public static final String DAYLOG_LOG_TYPE = "logtype";
	/** 工作类别 */
	public static final String[] WORK_TYPES = {"日常工作","需求","设计","编码","测试","管理","文档","会议","培训","其他"};
	/** 工作状态 */
	public static final String[] WORK_STATUS = {"按时","延时","提前","取消"};
	/** 日志类型 */
	public static final String[] LOG_TYPE = {"计划","新增"};
	
	//考勤统计
	/** 考勤统计信息 */
	public static final String MONTHLY_ATTANDANCE_INFO = "attenceInfo";
	/** 考勤统计信息的服务器时间的年份 */
	public static final String MONTHLY_ATTANDANCE_INFO_YEAR = "year";
	/** 考勤统计信息 的服务器时间的月份*/
	public static final String MONTHLY_ATTANDANCE_INFO_MONTH = "month";
	/** 部门月度信息 */
	public static final String MONTHLY_DEPT_INFO = "monthlyDeptInfo";
	/** 部门参加培训人数 */
	public static final String MONTHLY_TRAIN_TOTAL = "trainTotal";
	/** 人员变动信息 */
	public static final String MONTHLY_STAFFCHANGE_INFO = "staffChangeInfo";
	/** 总数统计信息 */
	public static final String MONTHLY_TOTAL_INFO = "totalInfo";
	/** 项目情况信息 */
	public static final String MONTHLY_PROJECT_INFO = "projectInfo";
	/** 合同统计信息 */
	public static final String MONTHLY_CONTRACT_INFO = "contractInfo";
	/**优秀员工信息 */
	public static final String MONTHLY_GOODEMPLOYEE_INFO = "goodEmployee";
	
	/** 考勤统计 年 */
	public static final String MONTHLY_ATTANDANCE_YEAR = "year";
	/** 考勤统计 月 */
	public static final String MONTHLY_ATTANDANCE_MONTH = "month";
	/** 当月每日详情，只记录不为0的日期 */
	public static final String MONTHLY_ATTANDANCE_DETAIL = "detail";
	
	/**返回码 0000*/
	public static final String DAYLOG_RETCODE_OK = "0000";
	/**返回码 9999*/
	public static final String DAYLOG_RETCODE_NG = "9999";
	/**返回信息 0000*/
	public static final String DAYLOG_RETMSG_OK = "成功";
	/**返回信息 9999*/
	public static final String DAYLOG_RETMSG_NG = "失败";
	/**返回信息 有查看权限*/
	public static final String MONTHLY_MNGR_RETMSG_OK = "有查看权限";
	/**返回信息无查看权限*/
	public static final String MONTHLY_MNGR_RETMSG_NG = "无查看权限";
	

	/**
	 * 输出json字符串
	 * 
	 * @param str
	 *            json字符串
	 */
	public static void ajaxPrint(String str) {
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			PrintWriter writer = response.getWriter();
			writer.print(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
