package cn.grgbanking.feeltm.context;

import java.io.File;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.grgbanking.feeltm.config.BusnDataDir;
import cn.grgbanking.feeltm.config.CommonData;
import cn.grgbanking.feeltm.config.Configure;
import cn.grgbanking.feeltm.log.SysLog;

/**
 * 
 */
@SuppressWarnings("serial")
public class InitServlet extends HttpServlet {
	// Initialize global variables
	public void init() throws ServletException {
		// ȡ����վ������Ŀ¼����ʵ����·��
		Configure.ROOT_PATH = getServletContext().getRealPath("/");
		if (!File.separator.equals(Configure.ROOT_PATH
				.substring(Configure.ROOT_PATH.length() - 1)))
			Configure.ROOT_PATH = Configure.ROOT_PATH + File.separator;
		SysLog.info("[InitServlet:ROOT_PATH]" + Configure.ROOT_PATH);
		// ��ʼ��������
		BaseApplicationContext.initAppContext();

		SysLog.info("[InitServlet]initAppContext OK");

		try {
			BusnDataDir a = (BusnDataDir) WebApplicationContextUtils
					.getWebApplicationContext(this.getServletContext())
					.getBean("busnDataDir");
			a.waken();
			CommonData b=(CommonData)WebApplicationContextUtils.getWebApplicationContext(this.getServletContext()).getBean("commonData");
			b.loadDataInServerStart();
			SysLog.info("Start TMM Server OK!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			SysLog.info("BusnDataDir Init false!");
		}

	}

}