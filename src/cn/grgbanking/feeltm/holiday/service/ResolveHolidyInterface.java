package cn.grgbanking.feeltm.holiday.service;

import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.stereotype.Service;

import cn.grgbanking.feeltm.domain.testsys.Holiday;

/**通过第三方提供的方法获取法定节假日
 * wtjiao 2014年11月11日 上午10:07:49
 */
@Service
public interface ResolveHolidyInterface {
	public static final SimpleDateFormat sdf=new SimpleDateFormat("yyyy");
	
	/**远程请求节假日数据并解析
	 * wtjiao 2014年11月11日 上午10:15:43
	 * @param queryYear 要查询节假日的年份 如2014
	 * @return
	 * @throws Exception
	 */
	public List<Holiday> resolve(String queryYear) throws Exception;
}
