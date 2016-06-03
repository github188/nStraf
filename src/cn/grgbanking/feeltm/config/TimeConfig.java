package cn.grgbanking.feeltm.config;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

import cn.grgbanking.feeltm.util.DateUtil;

public class TimeConfig {
	
	public static String[] compares = new String[]{"08:00:00","12:00:00","13:00:00","17:00:00"};
	
	public static int dayOfWork = 8;
	
	public static void main(String[] args)throws Exception {
		String s1 = "2014-06-04 09:36:04";
		String s2 = "2014-06-04 09:37:06";
		double i = getSumTime(DateUtil.stringToDate(s1, "yyyy-MM-dd HH:mm:ss"), DateUtil.stringToDate(s2, "yyyy-MM-dd HH:mm:ss"));
		System.out.println(i);
	}

	
	public static boolean isWeekend(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int week = calendar.get(Calendar.DAY_OF_WEEK);
		if(week==Calendar.SUNDAY || week==Calendar.SATURDAY){
			return true;
		}
		return false;
	}
	
	/**
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static double getSumTime(Date startTime, Date endTime) {
		double hours = 0;
		String startYMD = DateUtil.to_char(startTime, "yyyy-MM-dd");
		int i = 0;
		Date compareStart = DateUtil.stringToDate(startYMD+" "+compares[0], "yyyy-MM-dd HH:mm:ss");
		Calendar compareCalendar = Calendar.getInstance();
		compareCalendar.setTime(compareStart);
		while(compareStart.before(startTime) && (i++)<3){
			compareStart = DateUtil.stringToDate(startYMD+" "+compares[i], "yyyy-MM-dd HH:mm:ss");
		}
		
		// 设置startTime开始时间（必须在正常工作时间内8~12点，13~17点）
		//0表示原开始时间在八点之前，默认设置为八点，1表示原本在8~12点，不改动
		//2表示原开始时间在12点~13点，默认设置为13点，3表示原本在13~17点，不改动
		//3表示原开始时间在17点之后，则设置为第二天的八点；
		switch(i){
		case 0:
		case 2: startTime = DateUtil.stringToDate(startYMD+" "+compares[i], "yyyy-MM-dd HH:mm:ss");break;
		case 4:compareCalendar.add(Calendar.DAY_OF_WEEK, 1);
				startTime = compareCalendar.getTime();
				compareStart = startTime;break;
				default:break;
		}
		compareStart = DateUtil.stringToDate(startYMD+" "+compares[3], "yyyy-MM-dd HH:mm:ss");
		//计算开始时间当天的工作时间小时数当在12点之前需要减去12~13点之间的休息时间1个小时
		if(!isWeekend(startTime)){
			hours += (compareStart.getTime()-startTime.getTime())/(1000.0*3600.0);
			if(i<2){
				hours -= 1.0;
			}
		}

		String endYMD = DateUtil.to_char(endTime, "yyyy-MM-dd");
		compareStart = DateUtil.stringToDate(endYMD+" "+compares[3], "yyyy-MM-dd HH:mm:ss");
		i = 4;
		compareCalendar.setTime(compareStart);
		while(compareStart.after(endTime) && (i--)>0){
			compareStart = DateUtil.stringToDate(endYMD+" "+compares[i-1], "yyyy-MM-dd HH:mm:ss");
		}
		// 设置endTime结束时间（必须在正常工作时间内8~12点，13~17点）
		//4表示原开始时间在17点之后，默认设置为17点，2表示原本在13~17点，不改动
		//2表示原开始时间在12点~13点，默认设置为12点，3表示原本在8~12点，不改动
		//3表示原开始时间在8点之前，则设置为前一天的17点；
		switch(i){
		case 4: 
		case 2: endTime = DateUtil.stringToDate(endYMD+" "+compares[i-1], "yyyy-MM-dd HH:mm:ss");break;
		case 0:compareCalendar.add(Calendar.DAY_OF_WEEK, -1);
				endTime = compareCalendar.getTime();
				compareStart = endTime;break;
			default:break;
		}
		compareStart = DateUtil.stringToDate(endYMD+" "+compares[0], "yyyy-MM-dd HH:mm:ss");
		//计算结束时间当天的工作时间小时数当在13点之后需要减去12~13点之间的休息时间1个小时
		if(!isWeekend(endTime)){
			hours += (endTime.getTime()-compareStart.getTime())/(1000.0*3600.0);
			if(i>2){
				hours += -1.0;
			}
		}
		//计算除开始当天结束当天，之间的天数
		Calendar startCalendar = Calendar.getInstance();
		startCalendar.setTime(startTime);
		int day = 0;
		//如果，开始与结束在同一天，则重新计算
		if((endTime.getTime()-startTime.getTime())/(60*60*1000)<24){
			hours = (hours>=8)?(hours-8):(8-hours);
		}else{
			int betweenday = (int) ((endTime.getTime()-startTime.getTime())/(24*60*60*1000))-1;
			day = (betweenday/7)*5;
			startCalendar.add(Calendar.DAY_OF_YEAR, 1);
			for(int j=0;j<betweenday%7;j++){
				if(!isWeekend(startCalendar.getTime())){
					day++;
				}
			startCalendar.add(Calendar.DAY_OF_YEAR, 1);
			}
		}
		double times = dayOfWork*day + hours;
		DecimalFormat format = new DecimalFormat("#.00");
		times = Double.valueOf(format.format(times));
		//Integer time = (int) Math.round(times);
		return times;
	}
	
	
	
	
	/*
	public static int getSumTime(Date startTime, Date endTime) {
		//计算上班的开始时间
		double hours = 0;
		String startYMD = DateUtil.to_char(startTime, "yyyy-MM-dd");
		int i = 0;
		Date compareStart = DateUtil.stringToDate(startYMD+" "+compares[i], "yyyy-MM-dd HH:mm:ss");
		Calendar compareCalendar = Calendar.getInstance();
		compareCalendar.setTime(compareStart);
		boolean flag = true;
		while(compareStart.before(startTime) && i<3){
			i++;
			compareStart = DateUtil.stringToDate(startYMD+" "+compares[i], "yyyy-MM-dd HH:mm:ss");
			if((i-1)==1 && startTime.before(compareStart)){
				startTime = compareStart;
				flag = false;
			}
		}
		if(i==0){
			startTime = compareStart;
		}
		compareStart = DateUtil.stringToDate(startYMD+" "+compares[3], "yyyy-MM-dd HH:mm:ss");
		//如果是在下班之后 ，则从第二天开始的上班时间开始算起
		if(i==3 && startTime.after(compareStart)){
			compareCalendar.add(Calendar.DAY_OF_WEEK, 1);
			startTime = compareCalendar.getTime();
		}
		if(startTime.before(compareStart) && !isWeekend(startTime)){
			hours += (compareStart.getTime()-startTime.getTime())/(1000.0*3600.0);
			if(flag){
				hours += -1.0;
			}
		}
		//计算下班的结束时间
		String endYMD = DateUtil.to_char(endTime, "yyyy-MM-dd");
		i = compares.length-1;
		compareStart = DateUtil.stringToDate(endYMD+" "+compares[i], "yyyy-MM-dd HH:mm:ss");
		compareCalendar.setTime(compareStart);
		flag = false;
		while(compareStart.after(endTime) && i>0){
			i--;
			compareStart = DateUtil.stringToDate(endYMD+" "+compares[i], "yyyy-MM-dd HH:mm:ss");
			if((i+1)==2 && compareStart.before(endTime)){
				endTime = compareStart;
				flag = true;
			}
		}
		if(i==compares.length-1){
			endTime = compareStart;
		}
		compareStart = DateUtil.stringToDate(endYMD+" "+compares[0], "yyyy-MM-dd HH:mm:ss");
		if(i==0 && endTime.before(compareStart)){
			endTime = compareStart;
		}
		if(endTime.after(compareStart) && !isWeekend(endTime)){
			hours += (endTime.getTime()-compareStart.getTime())/(1000.0*3600.0);
			if(flag){
				hours += -1.0;
			}
		}
		Calendar startCalendar = Calendar.getInstance();
		startCalendar.setTime(startTime);
		Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(endTime);
		if(startCalendar.get(Calendar.DAY_OF_YEAR)==endCalendar.get(Calendar.DAY_OF_YEAR)){
			hours = (endTime.getTime()-startTime.getTime())/(1000.0*3600.0);
			if(flag){
				hours = -1.0;
			}
		}
		//开始算工作日
		startTime = DateUtil.stringToDate(startYMD, "yyyy-MM-dd");
		endTime = DateUtil.stringToDate(endYMD, "yyyy-MM-dd");
		startCalendar.add(Calendar.DAY_OF_YEAR, 1);
		//endCalendar.add(Calendar.DAY_OF_YEAR, -1);
		//先计算开始与结束之间的工作日(仅减掉周六周日)
		int day = 0;
		while(startCalendar.get(Calendar.DAY_OF_YEAR)<(endCalendar.get(Calendar.DAY_OF_YEAR))){
			if(!isWeekend(startCalendar.getTime())){
				day++;
			}
			startCalendar.add(Calendar.DAY_OF_YEAR, 1);
		}
		double times = dayOfWork*day + hours;
		//DecimalFormat df =new DecimalFormat("#####0");
		//Integer time = Integer.parseInt(df.format(times));
		
		Integer time = (int) Math.round(times);
		return time;
	}
	
	*/
	
}
