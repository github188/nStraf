package cn.grgbanking.feeltm.common.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.grgbanking.feeltm.common.bean.DynamicWeek;

public class DynamicWeekUtils {
	private static SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日");
	private static SimpleDateFormat sdf2=new SimpleDateFormat("MM月dd日");
	
	
	
	/**获取指定日期所处的动态周
	 * wtjiao 2014年7月3日 下午1:57:24
	 * @param date
	 * @return
	 */
	public static DynamicWeek getDateDynamicWeek(Date date) {
		if(date==null){
			date=Calendar.getInstance().getTime();
		}
		//指定的日期calendar对象
		Calendar startCal=Calendar.getInstance();
		startCal.setTime(date);
		while(startCal.get(Calendar.DAY_OF_WEEK)!=Calendar.SUNDAY){
			startCal.add(Calendar.DAY_OF_YEAR,-1);//依次往前倒数，找到这周的起点（周日）
		}
		//结束时间，在找到的周起点上加6天
		Calendar endCal=Calendar.getInstance();
		endCal.setTime(startCal.getTime());
		endCal.add(Calendar.DAY_OF_YEAR,6);
		
		DynamicWeek week=generateDynamicWeek(startCal,endCal);
        return week;
    }
	
	/**产生动态周
	 * wtjiao 2014年7月3日 下午2:10:29
	 * @param startCal
	 * @param endCal
	 * @return
	 */
	private static DynamicWeek generateDynamicWeek(Calendar startCal,Calendar endCal) {
		//设置动态周
    	DynamicWeek week=new DynamicWeek();
    	String key=sdf.format(startCal.getTime())+"-"+sdf.format(endCal.getTime());
    	int weekInMonth=startCal.get(Calendar.WEEK_OF_MONTH);
    	String timePeriod=sdf2.format(startCal.getTime())+"-"+sdf2.format(endCal.getTime());
    	String desc=startCal.get(Calendar.YEAR)+"年"+(startCal.get(Calendar.MONTH)+1)+"月第"+weekInMonth+"周";
    	String info=timePeriod+"("+desc+")";
    	week.setKey(key);
    	week.setInfo(info);
    	week.setDesc(desc);
    	week.setTimePeriod(timePeriod);
    	week.setStartTime(startCal.getTime());
    	week.setEndTime(endCal.getTime());
		return week;
	}

	/**获取指定之间段中的动态周
	 * wtjiao 2014年6月26日 上午10:40:36
	 * @return
	 */
	public static List<DynamicWeek> getDynamicWeeks(Calendar startDate, Calendar endDate) {
        List<DynamicWeek> result = new ArrayList<DynamicWeek>();
        startDate.add(Calendar.DAY_OF_YEAR, 1);
        //在开始时间到结束时间内找
        while (startDate.before(endDate)) {
            //起始日期是星期日
        	if(startDate.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY){
            	//周的起始日期（星期日）
        		Calendar weekStartDate=startDate;
            	//周的结束日期（星期六）
            	Calendar weekEndDate=Calendar.getInstance();
            	weekEndDate.setTime(weekStartDate.getTime());
            	weekEndDate.add(Calendar.DAY_OF_YEAR, 6);
        		
            	result.add(generateDynamicWeek(weekStartDate, weekEndDate));
            }
        	startDate.add(Calendar.DAY_OF_YEAR, 1);//往后数一日，继续遍历
        }
        return result;
    }
	
	
	/**获取指定之间段中的动态周
	 * wtjiao 2014年6月26日 上午10:40:36
	 * @return
	 */
	public static List<DynamicWeek> getDynamicWeeks(Calendar startDate, Calendar endDate,Calendar dateInSelectedWeek) {
		//获取指定时间段里的动态周
		List<DynamicWeek> dWeeks=getDynamicWeeks(startDate,endDate);
		for(DynamicWeek week:dWeeks){
			//找到包含选定日期的周，设置此周的selected属性为true，其他周则为false
			if(week.getStartTime().getTime()<=dateInSelectedWeek.getTime().getTime() && week.getEndTime().getTime()>=dateInSelectedWeek.getTime().getTime()){
				week.setSelected(true);
			}else{
				week.setSelected(false);
			}
		}
		return dWeeks;
	}
}
