package cn.grgbanking.feeltm.staff.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.staff.utils.HttpRequest;
import cn.grgbanking.feeltm.staff.utils.PropertiesUtil;

@Service
@Transactional
public class TempRequestEhrService {

	public void requestEhr() {
		String s = HttpRequest.sendGet(PropertiesUtil.get("synEMPById"), "");
		SysLog.info("请求EHR ==================================" + s);
	}

}
