package cn.grgbanking.feeltm.holiday.service.impl;

import httpcall.HttpClentUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.json.JSONObject;
import cn.grgbanking.feeltm.domain.testsys.Holiday;
import cn.grgbanking.feeltm.holiday.service.ResolveHolidyInterface;
import cn.grgbanking.feeltm.util.DatetimeUtil;

/**用EasyBots提供的方法获取 ，详见 http://www.easybots.cn/holiday_api.net
 * wtjiao 2014年11月11日 上午10:07:31
 */
public class EasybotsHolidayResolver implements ResolveHolidyInterface{
	/** 请求地址 */
	private String url="http://www.easybots.cn/api/holiday.php";

	@Override
	public List<Holiday> resolve(String queryYear) throws Exception {
		String parmValue=getQueryParmValue(queryYear);
		url=url+"?m="+parmValue;
		//请求第三方
		String responseStr=HttpClentUtil.getHttpReq(url, "utf-8");
		//解析返回的字符串，转为假日对象
		List<Holiday> holidays=parse2Holidy(responseStr,queryYear);
		return holidays;
	}

	/**解析
	 * wtjiao 2014年11月11日 上午10:59:38
	 * @param responseStr
	 * @param queryYear 
	 * @return
	 */
	private List<Holiday> parse2Holidy(String responseStr, String queryYear) {
		JSONObject jsonObject=JSONObject.fromObject(responseStr);
		List<Holiday> holidays=new ArrayList<Holiday>();
		Set<String> holidayDateSet=new HashSet<String>();
		
		//将得到的假期加入列表中并记录
		for(int i=1;i<=12;i++){
			String month=i<10?queryYear+"0"+i:queryYear+i;
			if(jsonObject.has(month)){
				String monthData=jsonObject.getString(month);
				//每月的节假日数据
				JSONObject monthHolidayJson=JSONObject.fromObject(monthData);
				for(int j=1;j<=31;j++){
					String day=j<10?"0"+j:j+"";
					if(monthHolidayJson.has(day)){
						String dayData=monthHolidayJson.getString(day);
						holidayDateSet.add(month+day);
						Holiday hol=new Holiday();
						hol.setCheckYear(queryYear);
						hol.setCheckMonth(month);
						hol.setCheckDate(month+day);
						hol.setType(dayData);
						holidays.add(hol);
					}
				}
			}
		}
			
		//=========================== 将工作日也加入到列表中（从当年的第一天开始遍历，找到哪些日期不在上面的holiday表中，则表示是工作日） =======================================
		if(holidays.size()>0){
			DatetimeUtil.registDateFomart("yyyyMMdd");//注册格式
			SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
			SimpleDateFormat sdf2=new SimpleDateFormat("yyyyMM");
			
			int yearDateNum=DatetimeUtil.getMaxYear(queryYear);//一年多少天
			Date firstDay=DatetimeUtil.getFirstDayOfYear(queryYear);//当年的第一天
			Calendar cal=Calendar.getInstance();
			cal.setTime(firstDay);
			
			for(int i=0;i<yearDateNum;i++){
				String day=sdf.format(cal.getTime());
				//在假日列表中不存在
				if(!holidayDateSet.contains(day)){
					Holiday hol=new Holiday();
					hol.setCheckYear(queryYear);
					hol.setCheckMonth(sdf2.format(cal.getTime()));
					hol.setCheckDate(day);
					hol.setType("0");
					holidays.add(hol);
				}
				cal.add(Calendar.DAY_OF_YEAR, 1);
			}
		}
		
		return holidays;
	}

	/**查询指定年份的参数
	 * wtjiao 2014年11月11日 上午10:49:13
	 * @param queryYear
	 * @return
	 */
	private String getQueryParmValue(String queryYear) {
		String val="";
		for(int i=1;i<=12;i++){
			if(i<10){
				val+=","+queryYear+"0"+i;
			}else{
				val+=","+queryYear+i;
			}
		}
		if(val.startsWith(",")){
			val=val.substring(1);
		}
		return val;
	}

	
	public static void main(String[] args) {
		try {
			new EasybotsHolidayResolver().resolve("2015");
			//System.out.println(DatetimeUtil.getMaxYear("2014"));
		} catch (Exception e) {
		}
	}

}
