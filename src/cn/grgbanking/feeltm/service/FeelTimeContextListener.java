package cn.grgbanking.feeltm.service;

import java.util.Date;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import cn.grgbanking.feeltm.config.BusnDataDir;
import cn.grgbanking.feeltm.config.Configure;
import cn.grgbanking.feeltm.util.DateUtil;

@SuppressWarnings({"unchecked", "unused"})
public class FeelTimeContextListener implements ServletContextListener {
	private java.util.Timer timer = null;

	public FeelTimeContextListener() {
	}

	public void contextInitialized(ServletContextEvent event) {
		timer = new java.util.Timer(true);
		event.getServletContext().log("time clock start!");

		String systemDate = DateUtil.getTimeYYYYMMDDHHMMSSString(new Date());
		long delaytime = 60 * 1000;
		long minute = 5;
		/*    
		 * Map dataDirMap = BusnDataDir
				.getMap("systemConfig.systemParamConfig");
		if(dataDirMap != null) {
			String str_delaytime = (String)dataDirMap.get("delaytime");
			String str_minute = (String)dataDirMap.get("minute");
			if(str_delaytime != null && !str_delaytime.equals("")) {
				delaytime = Long.parseLong(str_delaytime) * 1000;
			}
			if(str_minute != null && !str_minute.equals("")) {
				minute = Long.parseLong(str_minute);
				System.out.println("=========== " + minute + " ===========");
			}
		}
		 */
		String str_delaytime = Configure.getProperty("analysedelaytime");
		String str_minute = Configure.getProperty("analysetimedistance");
		if(str_delaytime != null && !str_delaytime.equals("")) {
			delaytime = Long.parseLong(str_delaytime) * 1000;
		}
		if(str_minute != null && !str_minute.equals("")) {
			minute = Long.parseLong(str_minute);
			
		}
		timer.schedule(new AbnormityAnalyseService(event.getServletContext()),
				delaytime, minute * 60 * 1000);

		event.getServletContext().log("add schedule!");

	}

	public void contextDestroyed(ServletContextEvent event) {
		timer.cancel();
		event.getServletContext().log("time clock close!");
	}

}
