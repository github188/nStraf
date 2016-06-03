package cn.grgbanking.feeltm.common.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.stereotype.Service;

import cn.grgbanking.feeltm.config.BusnDataDir;
import cn.grgbanking.feeltm.context.BaseApplicationContext;

/**
 * @author wtjiao
 * 系统 定时器执行服务（部分不属于任何功能模块的定时器服务，放到这里统一管理）
 */
@Service(value="systemQuartzExecuteService")
public class SystemQuartzExecuteService {
	
	/**
	 * 刷新数据字典
	 */
	public void refreshBusData(){
		try {
			BusnDataDir bdd=(BusnDataDir)BaseApplicationContext.getAppContext().getBean("busnDataDir");
			bdd.loadData();
		} catch (Exception e) {
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			System.out.println("================="+sdf.format(Calendar.getInstance().getTime())+" 刷新数据字典出现异常 ===============");
			e.printStackTrace();
		}
	}
}
