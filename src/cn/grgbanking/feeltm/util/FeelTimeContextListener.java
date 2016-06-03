package cn.grgbanking.feeltm.util;

import java.util.Date;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

@SuppressWarnings("deprecation")
public class FeelTimeContextListener implements ServletContextListener {
	private java.util.Timer timer = null;

	public FeelTimeContextListener() {
	}

	public void contextInitialized(ServletContextEvent event) {
		timer = new java.util.Timer(true);
		Date date = new Date();
		// date.setDate(date.getDate()+1);
		date.setDate(date.getDate());
		date.setHours(date.getHours());
		date.setSeconds(date.getSeconds() + 100);

		// timer.schedule(new
		// BankmaScheduleCheckTask(event.getServletContext()), date,
		// 60*60*1000);
		// if(Configure.getProperty("dataMaintenanceTime") != null)
		// {
		// timer.schedule(new DataExportTask(event.getServletContext()), 0,
		// 60*60*1000);
		// event.getServletContext().log("原始表->备份表数据维护已经添加到任务调度表");
		// }
	}

	public void contextDestroyed(ServletContextEvent event) {
		timer.cancel();
		event.getServletContext().log("定时器销毁");
	}

}
