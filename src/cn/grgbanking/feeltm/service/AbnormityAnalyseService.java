package cn.grgbanking.feeltm.service;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TimerTask;

import javax.servlet.ServletContext;

import cn.grgbanking.feeltm.config.BusnDataDir;
import cn.grgbanking.feeltm.context.BaseApplicationContext;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.transmgr.service.TranInfoService;
import cn.grgbanking.feeltm.util.DateUtil;

public class AbnormityAnalyseService extends TimerTask {

	private static boolean isRunning = false;
	private ServletContext context = null;

	public AbnormityAnalyseService(ServletContext context) {
		this.context = context;
	}

	public void run() {

		if (!isRunning) {
			isRunning = true;
			System.out.println("AbnormitAnalyseService start  " + new Date());
			Calendar cal = Calendar.getInstance();
			int timebound = 30;
			Map dataDirMap = BusnDataDir
					.getMap("systemConfig.systemParamConfig");
			if (dataDirMap != null) {
				String str_timebound = (String) dataDirMap.get("timebound");
				if (str_timebound != null && !str_timebound.equals("")) {
					timebound = Integer.parseInt(str_timebound);
				}
			}
			cal.add(Calendar.MINUTE, -timebound);
			context.log("开始执行指定任务" + new Date());
			String anyDate = DateUtil
					.getTimeYYYYMMDDHHMMSSString(cal.getTime());
			// TODO 添加自定义的每日处理

			@SuppressWarnings("unused")
			boolean flag = false;
			flag = backNatureTranData(anyDate);
			// if(flag)
			isRunning = false;
			context.log("指定任务执行结束" + new Date());
			System.out.println("AbnormitAnalyseService end   " + new Date());

		} else {
			context.log("上一次任务执行还未结束");
		}
	}

	public boolean backNatureTranData(String date) {
		boolean flag = false;
		try {
			// TranInfoDao tranInfoDao = (TranInfoDao)
			// BaseApplicationContext.getAppContext().getBean("tranInfoDao");
			// flag=tranInfoDao.removeTranHourDate(date);
			// flag=tranInfoDao.unatureAny(date);
			TranInfoService tranInfoService = (TranInfoService) BaseApplicationContext
					.getAppContext().getBean("tranInfoService");
			flag = tranInfoService.analyseTran(date);
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);

		}
		return flag;
	}

}
