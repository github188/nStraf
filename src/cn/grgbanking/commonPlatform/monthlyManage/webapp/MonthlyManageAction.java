package cn.grgbanking.commonPlatform.monthlyManage.webapp;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import cn.grgbanking.commonPlatform.monthlyManage.bean.AtdDetail;
import cn.grgbanking.commonPlatform.monthlyManage.bean.AttendanceInfoBean;
import cn.grgbanking.commonPlatform.monthlyManage.domain.MonthlyManager;
import cn.grgbanking.commonPlatform.monthlyManage.service.MonthlyManageService;
import cn.grgbanking.feeltm.common.service.RightService;
import cn.grgbanking.feeltm.common4Wechat.util.Common4Wechat;
import cn.grgbanking.feeltm.staff.service.StaffInfoService;
import cn.grgbanking.feeltm.util.DateUtil;
import cn.grgbanking.framework.webapp.BaseAction;

/**
 * 月度管理报告
 * 
 * @author zzhui1
 *
 */
public class MonthlyManageAction extends BaseAction{
	@Autowired
	private MonthlyManageService monthlyManageService;
	@Autowired
	private StaffInfoService staffInfoService;
	@Autowired
	private RightService rightService;
	
	private JSONObject globalData = new JSONObject();
	/**
	 * 查看是否拥有权限
	 * 
	 * @param userId
	 *            用户ID
	 * @return 有无权限
	 */
	private boolean getAccess(String userId){
		//获取部门经理及以上人员
		boolean access = rightService.hasModuleRights_MonthManageReport(userId);
		if(userId==null){
			access = true;
		}
		return access;
	}
	/**
	 * 获取月度统计-网页端
	 * @author lping1 
	 * @since 2014-11-17 上午10:24:03
	 */
	public String getMonthlyInfo4Web() {
		String forward = "getMonthlyInfo";//默认跳转到月度报告页面
		getMonthlyDeptInfo4Web();
		getTotalInfo4Web();
		getMonthlyAttenceInfo4Web();
		getMonthlyChangeInfo4Web();
		getMonthlyContractInfo4Web();
		getMonthlyGoodEmployeeInfo4Web();
		getMonthlyProjectInfo4Web();
		
		Calendar calendar = Calendar.getInstance();
//        /**
//         * 获取年份
//         */
       int attendanceYear = calendar.get(Calendar.YEAR);
//        /**
//         * 获取月份
//         */
       int attendanceMonth = calendar.get(Calendar.MONTH);
       globalData.put(Common4Wechat.MONTHLY_ATTANDANCE_INFO_YEAR, attendanceYear);//服务器当前年份
       globalData.put(Common4Wechat.MONTHLY_ATTANDANCE_INFO_MONTH, attendanceMonth);//服务器当前月份
	   request.setAttribute("globalData", globalData.toString());
	   return forward;
	}

	/**
	 * 考勤统计信息
	 * 
	 * @return 月度管理报告信息
	 */
	private MonthlyManager getMonthDataByYearMonth() {
		//取上一个月的年份和月份
		String date = DateUtil.rollDownDate();
		String year = date.substring(0, 4);
		String month = date.substring(4, 6);
		MonthlyManager obj = monthlyManageService.getMonthDataByYearMonth(year, month);
		return obj;
	}
	
	/**
	 * 考勤统计信息
	 * 
	 * @return 月度管理报告信息
	 */
	private MonthlyManager getMonthDataByYearMonth(String year, String month) {
		MonthlyManager obj = monthlyManageService.getMonthDataByYearMonth(year, month);
		return obj;
	}
	/**
	 * 获取月度报告访问权限接口
	 * add by whxing 
	 * 2014-12-5
	 */
	public void getMonthlyInfoAuth(){
		// 返回的json对象
		JSONObject responseObj = new JSONObject();
		String userId = request.getParameter(Common4Wechat.NO_KEY);
		// 是否拥有查看权限
		boolean access = getAccess(userId);
		try {
			if (access) {
				responseObj.put(Common4Wechat.RETCODE_KEY, Common4Wechat.DAYLOG_RETCODE_OK);
				responseObj.put(Common4Wechat.RETMSG_KEY, Common4Wechat.MONTHLY_MNGR_RETMSG_OK);
			}else{
				responseObj.put(Common4Wechat.RETCODE_KEY, Common4Wechat.DAYLOG_RETCODE_NG);
				responseObj.put(Common4Wechat.RETMSG_KEY, Common4Wechat.MONTHLY_MNGR_RETMSG_NG);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 返回的json对象
			try {
				responseObj.put(Common4Wechat.RETCODE_KEY, Common4Wechat.DAYLOG_RETCODE_NG);
				responseObj.put(Common4Wechat.RETMSG_KEY, Common4Wechat.MONTHLY_MNGR_RETMSG_NG);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}finally {
			String jsonStr = responseObj.toString();
			//转码
			try {
				jsonStr = URLEncoder.encode(jsonStr,"UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			// 返回json对象给请求方
			Common4Wechat.ajaxPrint(jsonStr);
		}
	}
	/**
	 * 获取考勤统计接口-微信端
	 */
	public void getMonthlyAttenceInfo() {
		// 返回的json对象
		JSONObject responseObj = new JSONObject();
		String retBlank = "";
		try {
			// 获取传入的参数OA账号
			String userId = request.getParameter(Common4Wechat.NO_KEY);
			// 是否拥有查看权限
			boolean access = getAccess(userId);
			// 考勤统计信息
			if (access) {
				String lastYearMonth = DateUtil.rollDownDate();
				String year = lastYearMonth.substring(0,4);
				String month = lastYearMonth.substring(4,6);
				int monthCnt = Integer.parseInt(month);
				//List<Object> monthlyManagerList = new ArrayList<Object>();
				List<Object> list = new ArrayList<Object>();
				for (int i = 0; i < monthCnt; i++) {
					String monthTemp = String.valueOf((100 + (i+1))).substring(1);
					MonthlyManager monthlyManager = getMonthDataByYearMonth(year, monthTemp);
					String attenceInfo = "";
					//JSONObject attenceInfoJson = new JSONObject();
					if (monthlyManager != null) {
						attenceInfo = monthlyManager.getAttendanceInfo();
						if (attenceInfo != null) {
							ArrayList<AtdDetail> resultList = (ArrayList<AtdDetail>) 
									JSONArray.toList(JSONArray.fromObject(attenceInfo),AtdDetail.class); 
							
							AttendanceInfoBean bean=new AttendanceInfoBean();
							bean.setYear(Integer.valueOf(monthlyManager.getYear()));
							bean.setMonth(Integer.valueOf(monthlyManager.getMonth()));
							bean.setDetail(resultList);
							String res = JSONObject.fromObject(bean).toString(); 
							list.add(res);
						}
					}
				}
				responseObj.put(Common4Wechat.RETCODE_KEY, Common4Wechat.DAYLOG_RETCODE_OK);
				responseObj.put(Common4Wechat.RETMSG_KEY, Common4Wechat.MONTHLY_MNGR_RETMSG_OK);
				responseObj.put(Common4Wechat.MONTHLY_ATTANDANCE_INFO, list);
				Calendar calendar = Calendar.getInstance();
//		        /**
//		         * 获取年份
//		         */
		       int attendanceYear = calendar.get(Calendar.YEAR);
//		        /**
//		         * 获取月份
//		         */
		       int attendanceMonth = calendar.get(Calendar.MONTH);
		       responseObj.put(Common4Wechat.MONTHLY_ATTANDANCE_INFO_YEAR, attendanceYear);//服务器当前年份
		       responseObj.put(Common4Wechat.MONTHLY_ATTANDANCE_INFO_MONTH, attendanceMonth);//服务器当前月份
		       
			}else{
				responseObj.put(Common4Wechat.RETCODE_KEY, Common4Wechat.DAYLOG_RETCODE_NG);
				responseObj.put(Common4Wechat.RETMSG_KEY, Common4Wechat.MONTHLY_MNGR_RETMSG_NG);
				responseObj.put(Common4Wechat.MONTHLY_ATTANDANCE_INFO, new ArrayList<Object>());
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 返回的json对象
			try {
				responseObj.put(Common4Wechat.RETCODE_KEY, Common4Wechat.DAYLOG_RETCODE_NG);
				responseObj.put(Common4Wechat.RETMSG_KEY, Common4Wechat.MONTHLY_MNGR_RETMSG_NG);
				responseObj.put(Common4Wechat.MONTHLY_ATTANDANCE_INFO, retBlank);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		} finally {
			String jsonStr = responseObj.toString();
			// 返回json对象给请求方
			//转码
			try {
				jsonStr = URLEncoder.encode(jsonStr,"UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			globalData.put(Common4Wechat.MONTHLY_ATTANDANCE_INFO, responseObj.get(Common4Wechat.MONTHLY_ATTANDANCE_INFO));
			Common4Wechat.ajaxPrint(jsonStr);
		}
	}

	/**
	 * 获取考勤统计接口-网页端
	 * @author lping1 
	 * @since 2014-11-18 上午09:54:42
	 */
	public void getMonthlyAttenceInfo4Web() {
		// 返回的json对象
		JSONObject responseObj = new JSONObject();
		String retBlank = "";
		try {
			// 获取传入的参数OA账号
			String userId = request.getParameter(Common4Wechat.NO_KEY);
			// 是否拥有查看权限
			boolean access = getAccess(userId);
			// 考勤统计信息
			if (access) {
				String lastYearMonth = DateUtil.rollDownDate();
				String year = lastYearMonth.substring(0,4);
				String month = lastYearMonth.substring(4,6);
				int monthCnt = Integer.parseInt(month);
				//List<Object> monthlyManagerList = new ArrayList<Object>();
				List<Object> list = new ArrayList<Object>();
				for (int i = 0; i < monthCnt; i++) {
					String monthTemp = String.valueOf((100 + (i+1))).substring(1);
					MonthlyManager monthlyManager = getMonthDataByYearMonth(year, monthTemp);
					String attenceInfo = "";
					//JSONObject attenceInfoJson = new JSONObject();
					if (monthlyManager != null) {
						attenceInfo = monthlyManager.getAttendanceInfo();
						if (attenceInfo != null) {
							ArrayList<AtdDetail> resultList = (ArrayList<AtdDetail>) 
									JSONArray.toList(JSONArray.fromObject(attenceInfo),AtdDetail.class); 
							
							AttendanceInfoBean bean=new AttendanceInfoBean();
							bean.setYear(Integer.valueOf(monthlyManager.getYear()));
							bean.setMonth(Integer.valueOf(monthlyManager.getMonth()));
							bean.setDetail(resultList);
							String res = JSONObject.fromObject(bean).toString(); 
							list.add(res);
						}
					}
				}
				responseObj.put(Common4Wechat.RETCODE_KEY, Common4Wechat.DAYLOG_RETCODE_OK);
				responseObj.put(Common4Wechat.RETMSG_KEY, Common4Wechat.MONTHLY_MNGR_RETMSG_OK);
				responseObj.put(Common4Wechat.MONTHLY_ATTANDANCE_INFO, list);
				
				//移动APP端调用时需要用
				Calendar calendar = Calendar.getInstance();
//		        /**
//		         * 获取年份
//		         */
		       int attendanceYear = calendar.get(Calendar.YEAR);
//		        /**
//		         * 获取月份
//		         */
		       int attendanceMonth = calendar.get(Calendar.MONTH);
		       responseObj.put(Common4Wechat.MONTHLY_ATTANDANCE_INFO_YEAR, attendanceYear);//服务器当前年份
		       responseObj.put(Common4Wechat.MONTHLY_ATTANDANCE_INFO_MONTH, attendanceMonth);//服务器当前月份
			}else{
				responseObj.put(Common4Wechat.RETCODE_KEY, Common4Wechat.DAYLOG_RETCODE_NG);
				responseObj.put(Common4Wechat.RETMSG_KEY, Common4Wechat.MONTHLY_MNGR_RETMSG_NG);
				responseObj.put(Common4Wechat.MONTHLY_ATTANDANCE_INFO, new ArrayList<Object>());
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 返回的json对象
			try {
				responseObj.put(Common4Wechat.RETCODE_KEY, Common4Wechat.DAYLOG_RETCODE_NG);
				responseObj.put(Common4Wechat.RETMSG_KEY, Common4Wechat.MONTHLY_MNGR_RETMSG_NG);
				responseObj.put(Common4Wechat.MONTHLY_ATTANDANCE_INFO, retBlank);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		} finally {
			String jsonStr = responseObj.toString();
			// 返回json对象给请求方
			//转码
			try {
				jsonStr = URLEncoder.encode(jsonStr,"UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			globalData.put(Common4Wechat.MONTHLY_ATTANDANCE_INFO, responseObj.get(Common4Wechat.MONTHLY_ATTANDANCE_INFO));
		}
	}

	
	
	/**
	 * 获取月度人员变动信息-微信端
	 */
	public void getMonthlyChangeInfo() {
		// 返回的json对象
		JSONObject responseObj = new JSONObject();
		String retBlank = "";
		try {
			// 获取传入的参数OA账号
			String userId = request.getParameter(Common4Wechat.NO_KEY);
			// 是否拥有查看权限
			boolean access = getAccess(userId);
			// 考勤统计信息
			if (access) {
				String lastYearMonth = DateUtil.rollDownDate();
				String year = lastYearMonth.substring(0,4);
				String month = lastYearMonth.substring(4,6);
				int monthCnt = Integer.parseInt(month);
				List<String> list = new ArrayList<String>();
				for (int i = 0; i < monthCnt; i++) {
					String monthTemp = String.valueOf((100 + (i+1))).substring(1);
					MonthlyManager monthlyManager = getMonthDataByYearMonth(year, monthTemp);
					String info = retBlank;
					if (monthlyManager != null) {
						info = monthlyManager.getStaffChangeInfo();
					}
					if (StringUtils.isNotBlank(info)) {
						list.add(info);
					}
				}
				responseObj.put(Common4Wechat.RETCODE_KEY,Common4Wechat.DAYLOG_RETCODE_OK);
				responseObj.put(Common4Wechat.RETMSG_KEY,Common4Wechat.MONTHLY_MNGR_RETMSG_OK);
				responseObj.put(Common4Wechat.MONTHLY_STAFFCHANGE_INFO,list);
			} else {
				responseObj.put(Common4Wechat.RETCODE_KEY,Common4Wechat.DAYLOG_RETCODE_NG);
				responseObj.put(Common4Wechat.RETMSG_KEY,Common4Wechat.MONTHLY_MNGR_RETMSG_NG);
				responseObj.put(Common4Wechat.MONTHLY_STAFFCHANGE_INFO, new ArrayList<String>());
			}
		} catch (JSONException e) {
			e.printStackTrace();
			try {
				// 返回的json对象
				responseObj.put(Common4Wechat.RETCODE_KEY,Common4Wechat.DAYLOG_RETCODE_NG);
				responseObj.put(Common4Wechat.RETMSG_KEY,Common4Wechat.MONTHLY_MNGR_RETMSG_NG);
				responseObj.put(Common4Wechat.MONTHLY_STAFFCHANGE_INFO, new ArrayList<String>());
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		} finally {
			String jsonStr = responseObj.toString();
			//转码
			try {
				jsonStr = URLEncoder.encode(jsonStr,"UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			globalData.put(Common4Wechat.MONTHLY_DEPT_INFO, responseObj.get(Common4Wechat.MONTHLY_STAFFCHANGE_INFO));
			// 返回json对象给请求方
			Common4Wechat.ajaxPrint(jsonStr);
		}
	}
	
	/**
	 * 获取月度人员变动信息-网页端
	 * @param   
	 * @return void
	 * @author lping1 
	 * @since 2014-11-18 上午09:55:33
	 */
	public void getMonthlyChangeInfo4Web() {
		// 返回的json对象
		JSONObject responseObj = new JSONObject();
		String retBlank = "";
		try {
			// 获取传入的参数OA账号
			String userId = request.getParameter(Common4Wechat.NO_KEY);
			// 是否拥有查看权限
			boolean access = getAccess(userId);
			// 考勤统计信息
			if (access) {
				String lastYearMonth = DateUtil.rollDownDate();
				String year = lastYearMonth.substring(0,4);
				String month = lastYearMonth.substring(4,6);
				int monthCnt = Integer.parseInt(month);
				List<String> list = new ArrayList<String>();
				for (int i = 0; i < monthCnt; i++) {
					String monthTemp = String.valueOf((100 + (i+1))).substring(1);
					MonthlyManager monthlyManager = getMonthDataByYearMonth(year, monthTemp);
					String info = retBlank;
					if (monthlyManager != null) {
						info = monthlyManager.getStaffChangeInfo();
					}
					if (StringUtils.isNotBlank(info)) {
						list.add(info);
					}
				}
				responseObj.put(Common4Wechat.RETCODE_KEY,Common4Wechat.DAYLOG_RETCODE_OK);
				responseObj.put(Common4Wechat.RETMSG_KEY,Common4Wechat.MONTHLY_MNGR_RETMSG_OK);
				responseObj.put(Common4Wechat.MONTHLY_STAFFCHANGE_INFO,list);
			} else {
				responseObj.put(Common4Wechat.RETCODE_KEY,Common4Wechat.DAYLOG_RETCODE_NG);
				responseObj.put(Common4Wechat.RETMSG_KEY,Common4Wechat.MONTHLY_MNGR_RETMSG_NG);
				responseObj.put(Common4Wechat.MONTHLY_STAFFCHANGE_INFO, new ArrayList<String>());
			}
		} catch (JSONException e) {
			e.printStackTrace();
			try {
				// 返回的json对象
				responseObj.put(Common4Wechat.RETCODE_KEY,Common4Wechat.DAYLOG_RETCODE_NG);
				responseObj.put(Common4Wechat.RETMSG_KEY,Common4Wechat.MONTHLY_MNGR_RETMSG_NG);
				responseObj.put(Common4Wechat.MONTHLY_STAFFCHANGE_INFO, new ArrayList<String>());
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		} finally {
			String jsonStr = responseObj.toString();
			//转码
			try {
				jsonStr = URLEncoder.encode(jsonStr,"UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			globalData.put(Common4Wechat.MONTHLY_STAFFCHANGE_INFO, responseObj.get(Common4Wechat.MONTHLY_STAFFCHANGE_INFO));
		}
	}
	
	/**
	 * 获取月度部门人员信息-微信端
	 * @return
	 */
	public void getMonthlyDeptInfo() {
		// 返回的json对象
		JSONObject responseObj = new JSONObject();
		String retBlank = "";
		try {
			// 获取传入的参数OA账号
			String userId = request.getParameter(Common4Wechat.NO_KEY);
			// 是否拥有查看权限
			boolean access = getAccess(userId);
			// 考勤统计信息
			if (access) {
				MonthlyManager monthlyManager  = getMonthDataByYearMonth();
				String info = retBlank;
				if (monthlyManager != null) {
					info = monthlyManager.getDeptInfo();
				}
				responseObj.put(Common4Wechat.RETCODE_KEY,Common4Wechat.DAYLOG_RETCODE_OK);
				responseObj.put(Common4Wechat.RETMSG_KEY,Common4Wechat.MONTHLY_MNGR_RETMSG_OK);
				responseObj.put(Common4Wechat.MONTHLY_DEPT_INFO, info);
			} else {
				responseObj.put(Common4Wechat.RETCODE_KEY,Common4Wechat.DAYLOG_RETCODE_NG);
				responseObj.put(Common4Wechat.RETMSG_KEY,Common4Wechat.MONTHLY_MNGR_RETMSG_NG);
				responseObj.put(Common4Wechat.MONTHLY_DEPT_INFO, retBlank);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 返回的json对象
			try {
				responseObj.put(Common4Wechat.RETCODE_KEY,Common4Wechat.DAYLOG_RETCODE_NG);
				responseObj.put(Common4Wechat.RETMSG_KEY,Common4Wechat.MONTHLY_MNGR_RETMSG_NG);
				responseObj.put(Common4Wechat.MONTHLY_DEPT_INFO, retBlank);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		} finally {
			String jsonStr = responseObj.toString();
			//转码
			try {
				jsonStr = URLEncoder.encode(jsonStr,"UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			globalData.put(Common4Wechat.MONTHLY_DEPT_INFO, responseObj.get(Common4Wechat.MONTHLY_DEPT_INFO));
			// 返回json对象给请求方
			Common4Wechat.ajaxPrint(jsonStr);
		}
	}
	
	/**
	 * 获取月度部门人员信息
	 * @return
	 */
	/**
	 * 获取月度部门人员信息-网页端
	 * @author lping1 
	 * @since 2014-11-18 上午09:56:26
	 */
	public void getMonthlyDeptInfo4Web() {
		// 返回的json对象
		JSONObject responseObj = new JSONObject();
		String retBlank = "";
		try {
			// 获取传入的参数OA账号
			String userId = request.getParameter(Common4Wechat.NO_KEY);
			// 是否拥有查看权限
			boolean access = getAccess(userId);
			// 考勤统计信息
			if (access) {
				MonthlyManager monthlyManager  = getMonthDataByYearMonth();
				String info = retBlank;
				if (monthlyManager != null) {
					info = monthlyManager.getDeptInfo();
				}
				responseObj.put(Common4Wechat.RETCODE_KEY,Common4Wechat.DAYLOG_RETCODE_OK);
				responseObj.put(Common4Wechat.RETMSG_KEY,Common4Wechat.MONTHLY_MNGR_RETMSG_OK);
				responseObj.put(Common4Wechat.MONTHLY_DEPT_INFO, info);
			} else {
				responseObj.put(Common4Wechat.RETCODE_KEY,Common4Wechat.DAYLOG_RETCODE_NG);
				responseObj.put(Common4Wechat.RETMSG_KEY,Common4Wechat.MONTHLY_MNGR_RETMSG_NG);
				responseObj.put(Common4Wechat.MONTHLY_DEPT_INFO, retBlank);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 返回的json对象
			try {
				responseObj.put(Common4Wechat.RETCODE_KEY,Common4Wechat.DAYLOG_RETCODE_NG);
				responseObj.put(Common4Wechat.RETMSG_KEY,Common4Wechat.MONTHLY_MNGR_RETMSG_NG);
				responseObj.put(Common4Wechat.MONTHLY_DEPT_INFO, retBlank);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		} finally {
			String jsonStr = responseObj.toString();
			//转码
			try {
				jsonStr = URLEncoder.encode(jsonStr,"UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			globalData.put(Common4Wechat.MONTHLY_DEPT_INFO, responseObj.get(Common4Wechat.MONTHLY_DEPT_INFO));
		}
	}
	
	/**
	 * 获取月度统计总数(合同金额.合同完成率.员工总数.项目总数.培训总人数)-微信端
	 */
	public void getTotalInfo(){
		// 返回的json对象
		JSONObject responseObj = new JSONObject();
		String retBlank = "";
		try {
			// 获取传入的参数OA账号
			String userId = request.getParameter(Common4Wechat.NO_KEY);
			// 是否拥有查看权限
			boolean access = getAccess(userId);
			// 考勤统计信息
			if (access) {
				MonthlyManager monthlyManager  = getMonthDataByYearMonth();
				String info = retBlank;
				if (monthlyManager != null) {
					info = monthlyManager.getTotalInfo();
				}
				responseObj.put(Common4Wechat.RETCODE_KEY,Common4Wechat.DAYLOG_RETCODE_OK);
				responseObj.put(Common4Wechat.RETMSG_KEY,Common4Wechat.MONTHLY_MNGR_RETMSG_OK);
				responseObj.put(Common4Wechat.MONTHLY_TOTAL_INFO,info);
			} else {
				responseObj.put(Common4Wechat.RETCODE_KEY,Common4Wechat.DAYLOG_RETCODE_NG);
				responseObj.put(Common4Wechat.RETMSG_KEY,Common4Wechat.MONTHLY_MNGR_RETMSG_NG);
				responseObj.put(Common4Wechat.MONTHLY_TOTAL_INFO,retBlank);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 返回的json对象
			try {
				responseObj.put(Common4Wechat.RETCODE_KEY,Common4Wechat.DAYLOG_RETCODE_NG);
				responseObj.put(Common4Wechat.RETMSG_KEY,Common4Wechat.MONTHLY_MNGR_RETMSG_NG);
				responseObj.put(Common4Wechat.MONTHLY_TOTAL_INFO,retBlank);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		} finally {
			String jsonStr = responseObj.toString();
			//转码
			try {
				jsonStr = URLEncoder.encode(jsonStr,"UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			globalData.put(Common4Wechat.MONTHLY_TOTAL_INFO, responseObj.get(Common4Wechat.MONTHLY_TOTAL_INFO));
			// 返回json对象给请求方
			Common4Wechat.ajaxPrint(jsonStr);
		}
	}

	/**
	 * 获取月度统计总数(合同金额.合同完成率.员工总数.项目总数.培训总人数)-网页端
	 * @author lping1 
	 * @since 2014-11-18 上午09:57:14
	 */
	public void getTotalInfo4Web(){
		// 返回的json对象
		JSONObject responseObj = new JSONObject();
		String retBlank = "";
		try {
			// 获取传入的参数OA账号
			String userId = request.getParameter(Common4Wechat.NO_KEY);
			// 是否拥有查看权限
			boolean access = getAccess(userId);
			// 考勤统计信息
			if (access) {
				MonthlyManager monthlyManager  = getMonthDataByYearMonth();
				String info = retBlank;
				if (monthlyManager != null) {
					info = monthlyManager.getTotalInfo();
				}
				responseObj.put(Common4Wechat.RETCODE_KEY,Common4Wechat.DAYLOG_RETCODE_OK);
				responseObj.put(Common4Wechat.RETMSG_KEY,Common4Wechat.MONTHLY_MNGR_RETMSG_OK);
				responseObj.put(Common4Wechat.MONTHLY_TOTAL_INFO,info);
			} else {
				responseObj.put(Common4Wechat.RETCODE_KEY,Common4Wechat.DAYLOG_RETCODE_NG);
				responseObj.put(Common4Wechat.RETMSG_KEY,Common4Wechat.MONTHLY_MNGR_RETMSG_NG);
				responseObj.put(Common4Wechat.MONTHLY_TOTAL_INFO,retBlank);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 返回的json对象
			try {
				responseObj.put(Common4Wechat.RETCODE_KEY,Common4Wechat.DAYLOG_RETCODE_NG);
				responseObj.put(Common4Wechat.RETMSG_KEY,Common4Wechat.MONTHLY_MNGR_RETMSG_NG);
				responseObj.put(Common4Wechat.MONTHLY_TOTAL_INFO,retBlank);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		} finally {
			String jsonStr = responseObj.toString();
			//转码
			try {
				jsonStr = URLEncoder.encode(jsonStr,"UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			globalData.put(Common4Wechat.MONTHLY_TOTAL_INFO, responseObj.get(Common4Wechat.MONTHLY_TOTAL_INFO));
		}
	}
	/**
	 * 获取项目情况-微信端
	 */
	public void getMonthlyProjectInfo(){
		// 返回的json对象
		JSONObject responseObj = new JSONObject();
		String retBlank = "";
		try {
			// 获取传入的参数OA账号
			String userId = request.getParameter(Common4Wechat.NO_KEY);
			// 是否拥有查看权限
			boolean access = getAccess(userId);
			// 考勤统计信息
			if (access) {
				MonthlyManager monthlyManager  = getMonthDataByYearMonth();
				String info = retBlank;
				if (monthlyManager != null) {
					info = monthlyManager.getProjectInfo();
				}
				responseObj.put(Common4Wechat.RETCODE_KEY,Common4Wechat.DAYLOG_RETCODE_OK);
				responseObj.put(Common4Wechat.RETMSG_KEY,Common4Wechat.MONTHLY_MNGR_RETMSG_OK);
				responseObj.put(Common4Wechat.MONTHLY_PROJECT_INFO,info);
			} else {
				responseObj.put(Common4Wechat.RETCODE_KEY,Common4Wechat.DAYLOG_RETCODE_NG);
				responseObj.put(Common4Wechat.RETMSG_KEY,Common4Wechat.MONTHLY_MNGR_RETMSG_NG);
				responseObj.put(Common4Wechat.MONTHLY_PROJECT_INFO,retBlank);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 返回的json对象
			try {
				responseObj.put(Common4Wechat.RETCODE_KEY,Common4Wechat.DAYLOG_RETCODE_NG);
				responseObj.put(Common4Wechat.RETMSG_KEY,Common4Wechat.MONTHLY_MNGR_RETMSG_NG);
				responseObj.put(Common4Wechat.MONTHLY_PROJECT_INFO,retBlank);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		} finally {
			String jsonStr = responseObj.toString();
			//转码
			try {
				jsonStr = URLEncoder.encode(jsonStr,"UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			globalData.put(Common4Wechat.MONTHLY_PROJECT_INFO, responseObj.get(Common4Wechat.MONTHLY_PROJECT_INFO));
			// 返回json对象给请求方
			Common4Wechat.ajaxPrint(jsonStr);
		}
	}
	
	/**
	 * 获取项目情况-网页端
	 * @param   
	 * @return void
	 * @author lping1 
	 * @since 2014-11-18 上午09:58:02
	 */
	public void getMonthlyProjectInfo4Web(){
		// 返回的json对象
		JSONObject responseObj = new JSONObject();
		String retBlank = "";
		try {
			// 获取传入的参数OA账号
			String userId = request.getParameter(Common4Wechat.NO_KEY);
			// 是否拥有查看权限
			boolean access = getAccess(userId);
			// 考勤统计信息
			if (access) {
				MonthlyManager monthlyManager  = getMonthDataByYearMonth();
				String info = retBlank;
				if (monthlyManager != null) {
					info = monthlyManager.getProjectInfo();
				}
				responseObj.put(Common4Wechat.RETCODE_KEY,Common4Wechat.DAYLOG_RETCODE_OK);
				responseObj.put(Common4Wechat.RETMSG_KEY,Common4Wechat.MONTHLY_MNGR_RETMSG_OK);
				responseObj.put(Common4Wechat.MONTHLY_PROJECT_INFO,info);
			} else {
				responseObj.put(Common4Wechat.RETCODE_KEY,Common4Wechat.DAYLOG_RETCODE_NG);
				responseObj.put(Common4Wechat.RETMSG_KEY,Common4Wechat.MONTHLY_MNGR_RETMSG_NG);
				responseObj.put(Common4Wechat.MONTHLY_PROJECT_INFO,retBlank);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 返回的json对象
			try {
				responseObj.put(Common4Wechat.RETCODE_KEY,Common4Wechat.DAYLOG_RETCODE_NG);
				responseObj.put(Common4Wechat.RETMSG_KEY,Common4Wechat.MONTHLY_MNGR_RETMSG_NG);
				responseObj.put(Common4Wechat.MONTHLY_PROJECT_INFO,retBlank);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		} finally {
			String jsonStr = responseObj.toString();
			//转码
			try {
				jsonStr = URLEncoder.encode(jsonStr,"UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			globalData.put(Common4Wechat.MONTHLY_PROJECT_INFO, responseObj.get(Common4Wechat.MONTHLY_PROJECT_INFO));
		}
	}
	
	/**
	 * 合同统计折现图-微信端
	 */
	public void getMonthlyContractInfo(){
		// 返回的json对象
		JSONObject responseObj = new JSONObject();
		String retBlank = "";
		try {
			// 获取传入的参数OA账号
			String userId = request.getParameter(Common4Wechat.NO_KEY);
			// 是否拥有查看权限
			boolean access = getAccess(userId);
			// 考勤统计信息
			if (access) {
				MonthlyManager monthlyManager  = getMonthDataByYearMonth();
				String info = retBlank;
				if (monthlyManager != null) {
					info = monthlyManager.getContractInfo();
				}
				responseObj.put(Common4Wechat.RETCODE_KEY,Common4Wechat.DAYLOG_RETCODE_OK);
				responseObj.put(Common4Wechat.RETMSG_KEY,Common4Wechat.MONTHLY_MNGR_RETMSG_OK);
				responseObj.put(Common4Wechat.MONTHLY_CONTRACT_INFO,info);
			} else {
				responseObj.put(Common4Wechat.RETCODE_KEY,Common4Wechat.DAYLOG_RETCODE_NG);
				responseObj.put(Common4Wechat.RETMSG_KEY,Common4Wechat.MONTHLY_MNGR_RETMSG_NG);
				responseObj.put(Common4Wechat.MONTHLY_CONTRACT_INFO,retBlank);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 返回的json对象
			try {
				responseObj.put(Common4Wechat.RETCODE_KEY,Common4Wechat.DAYLOG_RETCODE_NG);
				responseObj.put(Common4Wechat.RETMSG_KEY,Common4Wechat.MONTHLY_MNGR_RETMSG_NG);
				responseObj.put(Common4Wechat.MONTHLY_CONTRACT_INFO,retBlank);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		} finally {
			String jsonStr = responseObj.toString();
			//转码
			try {
				jsonStr = URLEncoder.encode(jsonStr,"UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			globalData.put(Common4Wechat.MONTHLY_CONTRACT_INFO, responseObj.get(Common4Wechat.MONTHLY_CONTRACT_INFO));
			// 返回json对象给请求方
			Common4Wechat.ajaxPrint(jsonStr);
		}
	}
	
	/**
	 * 合同统计折现图-网页端
	 * @author lping1 
	 * @since 2014-11-18 上午09:58:30
	 */
	public void getMonthlyContractInfo4Web(){
		// 返回的json对象
		JSONObject responseObj = new JSONObject();
		String retBlank = "";
		try {
			// 获取传入的参数OA账号
			String userId = request.getParameter(Common4Wechat.NO_KEY);
			// 是否拥有查看权限
			boolean access = getAccess(userId);
			// 考勤统计信息
			if (access) {
				MonthlyManager monthlyManager  = getMonthDataByYearMonth();
				String info = retBlank;
				if (monthlyManager != null) {
					info = monthlyManager.getContractInfo();
				}
				responseObj.put(Common4Wechat.RETCODE_KEY,Common4Wechat.DAYLOG_RETCODE_OK);
				responseObj.put(Common4Wechat.RETMSG_KEY,Common4Wechat.MONTHLY_MNGR_RETMSG_OK);
				responseObj.put(Common4Wechat.MONTHLY_CONTRACT_INFO,info);
			} else {
				responseObj.put(Common4Wechat.RETCODE_KEY,Common4Wechat.DAYLOG_RETCODE_NG);
				responseObj.put(Common4Wechat.RETMSG_KEY,Common4Wechat.MONTHLY_MNGR_RETMSG_NG);
				responseObj.put(Common4Wechat.MONTHLY_CONTRACT_INFO,retBlank);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 返回的json对象
			try {
				responseObj.put(Common4Wechat.RETCODE_KEY,Common4Wechat.DAYLOG_RETCODE_NG);
				responseObj.put(Common4Wechat.RETMSG_KEY,Common4Wechat.MONTHLY_MNGR_RETMSG_NG);
				responseObj.put(Common4Wechat.MONTHLY_CONTRACT_INFO,retBlank);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		} finally {
			String jsonStr = responseObj.toString();
			//转码
			try {
				jsonStr = URLEncoder.encode(jsonStr,"UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			globalData.put(Common4Wechat.MONTHLY_CONTRACT_INFO, responseObj.get(Common4Wechat.MONTHLY_CONTRACT_INFO));
		}
	}
	
	/**
	 * 优秀员工-微信端
	 */
	@SuppressWarnings("deprecation")
	public void getMonthlyGoodEmployeeInfo(){
		// 返回的json对象
		JSONObject responseObj = new JSONObject();
		String retBlank = "";
//		StringBuffer info = new StringBuffer();   
		try {
			// 获取传入的参数OA账号
			String userId = request.getParameter(Common4Wechat.NO_KEY);
			// 是否拥有查看权限
			boolean access = getAccess(userId);
			// 考勤统计信息
			if (access) {
				MonthlyManager monthlyManager  = getMonthDataByYearMonth();
				String info = retBlank;
				if (monthlyManager != null) {
					info = monthlyManager.getGoodUserInfo();
				}
				responseObj.put(Common4Wechat.RETCODE_KEY,Common4Wechat.DAYLOG_RETCODE_OK);
				responseObj.put(Common4Wechat.RETMSG_KEY,Common4Wechat.MONTHLY_MNGR_RETMSG_OK);
				responseObj.put(Common4Wechat.MONTHLY_GOODEMPLOYEE_INFO,info);
			} else {
				responseObj.put(Common4Wechat.RETCODE_KEY,Common4Wechat.DAYLOG_RETCODE_NG);
				responseObj.put(Common4Wechat.RETMSG_KEY,Common4Wechat.MONTHLY_MNGR_RETMSG_NG);
				responseObj.put(Common4Wechat.MONTHLY_GOODEMPLOYEE_INFO,retBlank);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 返回的json对象
			try {
				responseObj.put(Common4Wechat.RETCODE_KEY,Common4Wechat.DAYLOG_RETCODE_NG);
				responseObj.put(Common4Wechat.RETMSG_KEY,Common4Wechat.MONTHLY_MNGR_RETMSG_NG);
				responseObj.put(Common4Wechat.MONTHLY_GOODEMPLOYEE_INFO,retBlank);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		} finally {
			String jsonStr = responseObj.toString();
			//转码
			try {
				jsonStr = URLEncoder.encode(jsonStr,"UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			globalData.put(Common4Wechat.MONTHLY_GOODEMPLOYEE_INFO, responseObj.get(Common4Wechat.MONTHLY_GOODEMPLOYEE_INFO));
			Common4Wechat.ajaxPrint(jsonStr);
		}
	}
	
	/**
	 * 优秀员工-网页端
	 * @author lping1 
	 * @since 2014-11-18 上午09:59:14
	 */
	@SuppressWarnings("deprecation")
	public void getMonthlyGoodEmployeeInfo4Web(){
		// 返回的json对象
		JSONObject responseObj = new JSONObject();
		String retBlank = "";
//		StringBuffer info = new StringBuffer();   
		try {
			// 获取传入的参数OA账号
			String userId = request.getParameter(Common4Wechat.NO_KEY);
			// 是否拥有查看权限
			boolean access = getAccess(userId);
			// 考勤统计信息
			if (access) {
				MonthlyManager monthlyManager  = getMonthDataByYearMonth();
				String info = retBlank;
				if (monthlyManager != null) {
					info = monthlyManager.getGoodUserInfo();
				}
				responseObj.put(Common4Wechat.RETCODE_KEY,Common4Wechat.DAYLOG_RETCODE_OK);
				responseObj.put(Common4Wechat.RETMSG_KEY,Common4Wechat.MONTHLY_MNGR_RETMSG_OK);
				responseObj.put(Common4Wechat.MONTHLY_GOODEMPLOYEE_INFO,info);
			} else {
				responseObj.put(Common4Wechat.RETCODE_KEY,Common4Wechat.DAYLOG_RETCODE_NG);
				responseObj.put(Common4Wechat.RETMSG_KEY,Common4Wechat.MONTHLY_MNGR_RETMSG_NG);
				responseObj.put(Common4Wechat.MONTHLY_GOODEMPLOYEE_INFO,retBlank);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 返回的json对象
			try {
				responseObj.put(Common4Wechat.RETCODE_KEY,Common4Wechat.DAYLOG_RETCODE_NG);
				responseObj.put(Common4Wechat.RETMSG_KEY,Common4Wechat.MONTHLY_MNGR_RETMSG_NG);
				responseObj.put(Common4Wechat.MONTHLY_GOODEMPLOYEE_INFO,retBlank);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		} finally {
			String jsonStr = responseObj.toString();
			//转码
			try {
				jsonStr = URLEncoder.encode(jsonStr,"UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			globalData.put(Common4Wechat.MONTHLY_GOODEMPLOYEE_INFO, responseObj.get(Common4Wechat.MONTHLY_GOODEMPLOYEE_INFO));
		}
	}
}
