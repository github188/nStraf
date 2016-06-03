package cn.grgbanking.feeltm.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TimeZone;

import cn.grgbanking.feeltm.config.Configure;
import cn.grgbanking.feeltm.context.BaseApplicationContext;
import cn.grgbanking.feeltm.dayLog.service.DayLogService;
import cn.grgbanking.feeltm.log.SysLog;

public class DateUtil {

	public static final SimpleDateFormat sdf_yyyyMMddHHmmss = new SimpleDateFormat(
			"yyyyMMddHHmmss");

	// ------------------------------------------------------------------------------
	/**
	 * ��������Ƿ�����"yyyy-MM-dd"�ĸ�ʽ����toDate��С��fromDate
	 * 
	 * @param fromDate
	 *            ��ʼ����
	 * @param toDate
	 *            ��������
	 * @param * @return ���ڸ�ʽ��ȷ������true������false
	 */
	public static boolean isValidDates(String fromDate, String toDate,
			String[] dates) {
		if (fromDate == null || fromDate.trim().length() == 0)
			return false;
		if (toDate == null || toDate.trim().length() == 0)
			return false;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date begin = null;
		Date end = null;
		try {
			begin = format.parse(fromDate);
			end = format.parse(toDate);
			if (begin.after(end))
				return false;
			dates[0] = format.format(begin);
			dates[1] = format.format(end);
		} catch (Exception e) {
			SysLog.error("error in (DateUtil.java-isValidDates())");
			return false;
		}
		return true;
	}
	
	public static void showLog(String log){
		try{
			String str=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime())+": "+log;
			SysLog.info(str);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	// ------------------------------------------------------------------------------
	public static String getTimeString(Timestamp tsp) {
		if (tsp == null)
			return "";
		return getTimeString(new Date(tsp.getTime()));
	}

	// ------------------------------------------------------------------------------
	public static String getTimeString(java.util.Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formatter.format(date).trim();
	}

	public static String stringYYYYMMDDHHMMSSTo(String date) {
		StringBuffer strBuffer = new StringBuffer();
		strBuffer.append(date.substring(0, 4));
		strBuffer.append("-");
		strBuffer.append(date.substring(4, 6));
		strBuffer.append("-");
		strBuffer.append(date.substring(6, 8));
		strBuffer.append(" ");
		strBuffer.append(date.substring(8, 10));
		strBuffer.append(":");
		strBuffer.append(date.substring(10, 12));
		strBuffer.append(":");
		strBuffer.append(date.substring(12, 14));

		return strBuffer.toString();
	}

	public static String getTimeYYYYMMDDHHMMSSString(java.util.Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		return formatter.format(date).trim();
	}

	public static String getTimeYYYYMMDDHHMMString(java.util.Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmm");
		return formatter.format(date).trim();
	}
	// ------------------------------------------------------------------------------
	public static String getDateString(java.util.Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(date).trim();
	}
	
	// ------------------------------------------------------------------------------
	public static String to_char(Timestamp tsp, String format) {
		if (tsp == null)
			return "";
		return to_char(new Date(tsp.getTime()), format);
	}

	// ------------------------------------------------------------------------------
	public static String to_char(Date date, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(date).trim();
	}

	// ------------------------------------------------------------------------------
	public static Timestamp to_timestamp(String date) {
		return new Timestamp(to_date(date).getTime());
	}

	// ------------------------------------------------------------------------------

	public static Date stringToDate(String dateText, String format) {
		if (dateText == null) {
			return null;
		}
		DateFormat df = null;
		try {
			if (format == null) {
				df = new SimpleDateFormat();
			} else {
				df = new SimpleDateFormat(format);
			}

			df.setLenient(false);
			return df.parse(dateText);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	// ------------------------------------------------------------------------------
	public static Date to_date(String date) {
		Calendar cd = Calendar.getInstance();
		StringTokenizer token = new StringTokenizer(date, "-/ :");
		if (token.hasMoreTokens()) {
			cd.set(Calendar.YEAR, Integer.parseInt(token.nextToken()));
		} else {
			cd.set(Calendar.YEAR, 1970);
		}
		if (token.hasMoreTokens()) {
			cd.set(Calendar.MONTH, Integer.parseInt(token.nextToken()) - 1);
		} else {
			cd.set(Calendar.MONTH, 0);
		}
		if (token.hasMoreTokens()) {
			cd.set(Calendar.DAY_OF_MONTH, Integer.parseInt(token.nextToken()));
		} else {
			cd.set(Calendar.DAY_OF_MONTH, 1);
		}
		if (token.hasMoreTokens()) {
			cd.set(Calendar.HOUR_OF_DAY, Integer.parseInt(token.nextToken()));
		} else {
			cd.set(Calendar.HOUR_OF_DAY, 0);
		}
		if (token.hasMoreTokens()) {
			cd.set(Calendar.MINUTE, Integer.parseInt(token.nextToken()));
		} else {
			cd.set(Calendar.MINUTE, 0);
		}
		if (token.hasMoreTokens()) {
			cd.set(Calendar.SECOND, Integer.parseInt(token.nextToken()));
		} else {
			cd.set(Calendar.SECOND, 0);
		}
		if (token.hasMoreTokens()) {
			cd.set(Calendar.MILLISECOND, Integer.parseInt(token.nextToken()));
		} else {
			cd.set(Calendar.MILLISECOND, 0);
		}
		return cd.getTime();
	}

	// ------------------------------------------------------------------------------

	public static final String[] getDateRange(int selType) {
		String startDate = null, endDate = null;

		Calendar cd = Calendar.getInstance();
		int year = cd.get(Calendar.YEAR);
		int month = cd.get(Calendar.MONTH) + 1;
		Calendar cdTmp = Calendar.getInstance();
		int i;
		switch (selType) {
		case 1: // ����
			i = cd.get(Calendar.DAY_OF_WEEK) - 1;
			cdTmp.setTime(new Date(cd.getTime().getTime() - i * 3600 * 24
					* 1000));
			startDate = cdTmp.get(Calendar.YEAR) + "-"
					+ (cdTmp.get(Calendar.MONTH) + 1) + "-"
					+ cdTmp.get(Calendar.DAY_OF_MONTH);
			i = 7 - cd.get(Calendar.DAY_OF_WEEK);
			cdTmp.setTime(new Date(cd.getTime().getTime() + i * 3600 * 24
					* 1000));
			endDate = cdTmp.get(Calendar.YEAR) + "-"
					+ (cdTmp.get(Calendar.MONTH) + 1) + "-"
					+ cdTmp.get(Calendar.DAY_OF_MONTH);
			break;
		case 2: // ����
			startDate = year + "-" + month + "-01";
			switch (month) {
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:
				endDate = year + "-" + month + "-31";
				break;
			case 2:
				if (isLeapYear(year))
					endDate = year + "-" + month + "-29";
				else
					endDate = year + "-" + month + "-28";
				break;
			default:
				endDate = year + "-" + month + "-30";
			}
			break;
		case 3: // ����
			startDate = year + "-01-01";
			endDate = year + "-12-31";
			break;
		default:
			startDate = "2000-01-01";
			endDate = "2100-01-01";
		} // switch
		return new String[] { startDate, endDate };
	}

	// ------------------------------------------------------------------------------
	/**
	 * �ж��Ƿ�����
	 * 
	 * @param y
	 *            ���
	 * @return true or false
	 */
	public static final boolean isLeapYear(int y) {
		if (y % 4 == 0) {
			if (y % 100 == 0) {
				if (y % 400 == 0)
					return true;
				else
					return false;
			} else {
				return true;
			} // else
		} // if
		return false;
	}

	// ------------------------------------------------------------------------------

	public static Date dateIncreaseByHour(Date date, int hours) {

		Calendar cal = GregorianCalendar.getInstance(TimeZone
				.getTimeZone("GMT"));
		cal.setTime(date);
		cal.add(Calendar.HOUR, hours);

		return cal.getTime();
	}

	/**
	 * �������-�������
	 * 
	 * @param date
	 * @param days
	 * @return java.util.Date
	 */
	public static Date dateIncreaseByDay(Date date, int days) {

		Calendar cal = GregorianCalendar.getInstance(TimeZone
				.getTimeZone("GMT"));
		cal.setTime(date);
		cal.add(Calendar.DATE, days);

		return cal.getTime();
	}

	/**
	 * �������-�������
	 * 
	 * @param date
	 * @param days
	 * @return java.util.Date
	 */
	public static Date dateIncreaseByMonth(Date date, int mnt) {

		Calendar cal = GregorianCalendar.getInstance(TimeZone
				.getTimeZone("GMT"));
		cal.setTime(date);
		cal.add(Calendar.MONTH, mnt);

		return cal.getTime();
	}

	/**
	 * �������-�������
	 * 
	 * @param date
	 * @param mnt
	 * @return java.util.Date
	 */
	public static Date dateIncreaseByYear(Date date, int mnt) {

		Calendar cal = GregorianCalendar.getInstance(TimeZone
				.getTimeZone("GMT"));
		cal.setTime(date);
		cal.add(Calendar.YEAR, mnt);

		return cal.getTime();
	}

	public static final Date getThisweekFirst(Date early) {
		Calendar c1 = Calendar.getInstance();
		c1.setTime(early);
		int week = c1.get(Calendar.DAY_OF_WEEK);
		int firstw = (-1) * (week - 1);
		Date weekFirst = dateIncreaseByDay(early, firstw);
		return weekFirst;
	}

	public static final Date firstdateIncreaseByWeek(Date early, int weeks) {
		Date firstDate = getThisweekFirst(early);
		firstDate = dateIncreaseByDay(firstDate, weeks * 7);
		return firstDate;
	}

	public static final int monthsIndays(Date early, Date late) {
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(early);
		c2.setTime(late);
		int earlyYear = c1.get(Calendar.YEAR);
		int earlyMonth = c1.get(Calendar.MONTH);
		int lateYear = c2.get(Calendar.YEAR);
		int lateMonth = c2.get(Calendar.MONTH);
		int months = (lateYear - earlyYear) * 12 + lateMonth - earlyMonth + 1;
		return months;
	}

	/**
	 * Returns the weeks between two dates.
	 * 
	 * @param early
	 *            the "first date"
	 * @param late
	 *            the "second date"
	 * @return the weeks between the two dates
	 */
	public static final int weeksIndays(Date early, Date late) {
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(early);
		c2.setTime(late);
		int days = daysBetween(c1, c2) + 1;
		int earlyweek = c1.get(Calendar.DAY_OF_WEEK);
		int lateweek = c2.get(Calendar.DAY_OF_WEEK);
		int weeks = days / 7;
		int weekst = days % 7;
		if (weekst == 0) {
			return weeks;
		} else if (lateweek >= earlyweek) {
			return weeks + 1;
		} else {
			return weeks + 2;
		}
	}

	/**
	 * Returns the days between two dates. Positive values indicate that the
	 * second date is after the first, and negative values indicate, well, the
	 * opposite. Relying on specific times is problematic.
	 * 
	 * @param early
	 *            the "first date"
	 * @param late
	 *            the "second date"
	 * @return the days between the two dates
	 */
	public static final int daysBetween(Date early, Date late) {

		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(early);
		c2.setTime(late);

		return daysBetween(c1, c2);
	}

	/**
	 * Returns the days between two dates. Positive values indicate that the
	 * second date is after the first, and negative values indicate, well, the
	 * opposite.
	 * 
	 * @param early
	 * @param late
	 * @return the days between two dates.
	 */
	public static final int daysBetween(Calendar early, Calendar late) {

		return (int) (toJulian(late) - toJulian(early));
	}

	/**
	 * Return a Julian date based on the input parameter. This is based from
	 * calculations found at <a
	 * href="http://quasar.as.utexas.edu/BillInfo/JulianDatesG.html">Julian Day
	 * Calculations (Gregorian Calendar)</a>, provided by Bill Jeffrys.
	 * 
	 * @param c
	 *            a calendar instance
	 * @return the julian day number
	 */
	public static final float toJulian(Calendar c) {

		int Y = c.get(Calendar.YEAR);
		int M = c.get(Calendar.MONTH);
		int D = c.get(Calendar.DATE);
		int A = Y / 100;
		int B = A / 4;
		int C = 2 - A + B;
		float E = (int) (365.25f * (Y + 4716));
		float F = (int) (30.6001f * (M + 1));
		float JD = C + D + E + F - 1524.5f;

		return JD;
	}

	// ------�鿴�Ƿ�����----------------
	public static final boolean checkLeapYear(int Year) {
		boolean isLeapYear = false;
		if (Year % 4 == 0 && Year % 100 != 0) {
			isLeapYear = true;
		}
		if (Year % 400 == 0)
			isLeapYear = true;
		else if (Year % 4 != 0) {
			isLeapYear = false;
		}
		return isLeapYear;
	}

	// ---------------------------------

	// --------���㵱������---------------
	public static final int checkMonth(int Month, int Year) {
		int Dates = 0;
		if (Month < 0 || Month > 12) {
			System.out.println("Month Error");
		}
		if (Month == 1 || Month == 3 || Month == 5 || Month == 7 || Month == 8
				|| Month == 10 || Month == 12) {
			Dates = 31;
		}
		if (Month == 2 && checkLeapYear(Year)) {
			Dates = 29;
		}
		if (Month == 2 && !checkLeapYear(Year)) {
			Dates = 28;
		}
		if (Month == 4 || Month == 6 || Month == 9 || Month == 11) {
			Dates = 30;
		}
		return Dates;
	}

	public static String to_TMdate(String date) {
		String returnDate;
		date = date + "-";
		String[] str = date.split("-");
		int len = str.length;
		returnDate = str[0];
		if (len > 1) {
			for (int i = 1; i < len; i++) {
				returnDate = returnDate + str[i];
			}
		}

		return returnDate;
	}

	public static String to_TMdate1(String date) {
		String returnDate;
		date = date + "-";
		String[] str = date.split("-");
		int len = str.length;
		returnDate = str[0];
		if (len > 1) {
			for (int i = 1; i < len; i++) {
				if (str[i].length() == 1) {
					str[i] = new String("0") + str[i];
				}
				returnDate = returnDate + str[i];
			}
		}

		return returnDate;
	}

	public static String to_TMNormalDate(String date) {
		String returnDate = "1111-11-11";
		if (date.length() >= 8) {
			returnDate = date.substring(0, 4) + "-" + date.substring(4, 6)
					+ "-" + date.subSequence(6, 8);
		}
		return returnDate;
	}

	public static String to_TMNormalTime(String time) {
		String returnDate = "00:00:00";
		if ((time != null) && (time.length() >= 6)) {
			returnDate = time.substring(0, 2) + ":" + time.substring(2, 4)
					+ ":" + time.subSequence(4, 6);
		}
		return returnDate;
	}

	public static String getyyyyMMddDate(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

		//String dates = DateUtil.getDateString(date);
		return dateFormat.format(date);
	}

	public static String stringyyy_mm_ddToyyyyMMdd(String dateText) {
		if (dateText == null)
			return null;
		try {
			String date = "";
			if (dateText.length() >= 10) {
				String year = dateText.substring(0, 4);
				String month = dateText.substring(5, 7);
				String day = dateText.substring(8, 10);
				date = year + month + day;
			}
			return date;

		} catch (Exception e) {
			SysLog.error(e);
			e.printStackTrace();
			return null;
		}
	}

	public static String stringyyyyMMddToyyyy_mm_dd(String dateText) {
		if (dateText == null)
			return null;
		try {
			String date = "";
			if (dateText.length() >= 8) {
				String year = dateText.substring(0, 4);
				String month = dateText.substring(4, 6);
				String day = dateText.substring(6, 8);
				date = year + "-" + month + "-" + day;
			}
			return date;

		} catch (Exception e) {
			SysLog.error(e);
			e.printStackTrace();
			return null;
		}
	}

	public static String stringhh_mm_ssTohhmmss(String timeText) {
		if (timeText == null)
			return null;
		try {
			String time = "";
			if (timeText.length() >= 8) {
				String hh = timeText.substring(0, 2);
				String mm = timeText.substring(3, 5);
				String ss = timeText.substring(6, 8);
				time = hh + mm + ss;
			}
			return time;

		} catch (Exception e) {
			SysLog.error(e);
			e.printStackTrace();
			return null;
		}
	}

	public static String stringyyyy_MM_ddHH_mm_ssToyyyyMMddHHmmss(
			String dateTimeText) {
		if (dateTimeText == null)
			return null;
		try {
			String datetime = "";
			if (dateTimeText.length() >= 19) {
				String year = dateTimeText.substring(0, 4);
				String month = dateTimeText.substring(5, 7);
				String day = dateTimeText.substring(8, 10);
				String hh = dateTimeText.substring(11, 13);
				String mm = dateTimeText.substring(14, 16);
				String ss = dateTimeText.substring(17, 19);
				datetime = year + month + day + hh + mm + ss;
			}
			return datetime;

		} catch (Exception e) {
			SysLog.error(e);
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * lhy 2014-4-29
	 * 两个年份差
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static double getYearMus(Date date1,Date date2){
		double year = 0;
		year = date1.getTime()-date2.getTime();
		year = year/(60*60*1000*24)/365;
		DecimalFormat forma=(DecimalFormat)DecimalFormat.getInstance();
		forma.applyPattern("0.0");
		year = Double.parseDouble(forma.format(year));
		return year;
	}
	/**
	 * 获取时间段的日期
	 * @param startdate
	 * @param enddate
	 * @return
	 * @throws Exception
	 */
	public static List<String> getDateLine(String startdate, String enddate) throws Exception{
		List<String> list = new ArrayList<String>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date begin = sdf.parse(startdate);
		Date end = sdf.parse(enddate);
		double between = (end.getTime() - begin.getTime()) / 1000;// 除以1000是为了转换成秒
		double day = between / (24 * 3600);
		for (int i = 0; i <= day; i++) {
			Calendar cd = Calendar.getInstance();
			cd.setTime(sdf.parse(startdate));
			cd.add(Calendar.DATE, i);// 增加一天
			//cd.add(Calendar.MONTH, n);// 增加一个月
			//System.out.println(sdf.format(cd.getTime()));
			list.add(sdf.format(cd.getTime()));
		}
		return list;
	}
	/**
	 * 获取时间段除周末的天数
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static long getworkDay(String s1, String s2){
		String[] str = s1.split("-");
		String[] str2 = s2.split("-");
		int startYear = Integer.parseInt(str[0]);
		int startMonth = Integer.parseInt(str[1])-1;
		int startDay = Integer.parseInt(str[2]);
		int endYear = Integer.parseInt(str2[0]);
		int endMonth = Integer.parseInt(str2[1])-1;
		int endDay = Integer.parseInt(str2[2]);
		//System.out.println("startdate=="+startYear+"  "+startMonth+"   "+startDay);
		//System.out.println("enddate=="+endYear+"  "+endMonth+"   "+endDay);
		Calendar start = new GregorianCalendar(startYear, startMonth, startDay, 0, 0);
		Calendar end = new GregorianCalendar(endYear, endMonth, endDay, 0, 0);
		long day = 86400000;// 一天的millis
		long mod = (end.getTimeInMillis() - start.getTimeInMillis()) / day+1;
		//System.out.println("一共是" + mod + "天");
		int n = start.get(Calendar.DAY_OF_WEEK);// 2009-1-1是周几
		// 周日到周六分别是1~7
		long cnt = mod / 7;
		// 几个整周
		cnt = cnt * 2;
		long yushu = mod % 7;
		if (n + yushu > 7)
			cnt = cnt + 2;// 过了周六
		if (n + yushu == 7)
			cnt++;// 正好是周六
		System.out.println("不算周六周日共" + (mod - cnt) + "天");
		return mod - cnt;
	}
	/**
	 * 判断指定日期是否为周末
	 * @param s
	 * @return
	 */
	public static String isWorkDay(String s){
		SimpleDateFormat sdfInput = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		Date date = new Date();
		try {
			date = sdfInput.parse(s);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		calendar.setTime(date);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)-1;
		if(dayOfWeek==0 || dayOfWeek==6){
			return "0";
		}
		return "1";
	}
	/**
	 * 
	 * @param startdate
	 * @param enddate
	 * @return
	 */
	public static double getHourMinute(Date startdate,Date enddate){
		long between = (enddate.getTime()-startdate.getTime())/1000;
		long day=between/(24*3600);
		long hour=between%(24*3600)/3600;
		long minute=between%3600/60; 
		long second=between%60/60;
//		System.out.println(""+day+"天"+hour+"小时"+minute+"分"+second+"秒");
		double hourmin=(double)Math.round(minute/(60*1.00)*100)/100;
		double sumtime = day*8+hour+hourmin;
		return sumtime;
	}
	
	/**
	 * 取得当前月份的首日和尾日
	 * @param type 0:为取首日  1:为取尾日
	 * @return 日期 Date
	 */
	public static Date getStartEndDate(int type) {
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, 0);
		if (type == 0) {
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			date = calendar.getTime();
		}else if (type == 1){
			calendar.set(Calendar.DAY_OF_MONTH, 
					calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
			date = calendar.getTime();
		}
		return date;
	}
	
	/**
	 * 取得指定月份的首日或尾日
	 * @param type 0:为取首日  1:为取尾日
	 * @return 日期 Date
	 */
	public static Date getStartEndDate(int year,int month,int type) {
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.add(Calendar.MONTH, 0);
		
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		if (type == 0) {
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			date = calendar.getTime();
		}else if (type == 1){
			calendar.set(Calendar.DAY_OF_MONTH, 
					calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
			date = calendar.getTime();
		}
		return date;
	}
	
	public static void main(String[] args) {
		
	}
	
	/**
	 * 取上一个月的年份和月份
	 * @return yyyyMM
	 */
	public static String rollDownDate() {
		Calendar calendar = Calendar.getInstance();
		int month = calendar.get(Calendar.MONTH) + 1;
		if (month == 1) {
			calendar.roll(Calendar.YEAR, -1);
		}
		calendar.roll(Calendar.MONTH, -1);
		Date date = calendar.getTime();
		return getyyyyMMddDate(date).substring(0,6);
	}
	
	/**
	 * 取当天以前到月初的日期，如果当天是月初则取上一个月的所有日期
	 * 
	 * @return 日期列表(yyyy-MM-dd)
	 */
	public static List<String> getWorkdayList(){
		List<String> list = new ArrayList<String>();
		Calendar c = Calendar.getInstance();
		//得到当月的第几天
		int date = c.get(Calendar.DATE);
		//得到当年的第几月
		int month = c.get(Calendar.MONTH) + 1;
		String endDate = "";
		if (date == 1) {
			//如果是第一天，回滚到上一个月
			c.roll(Calendar.MONTH, -1);
			//指至当月最后一天
			c.set(Calendar.DAY_OF_MONTH, 
					c.getActualMaximum(Calendar.DAY_OF_MONTH));
			if (month == 1) {
				//如果是第一个月，还需回滚至上一年
				c.roll(Calendar.YEAR, -1);
			}
			endDate = getDateString(c.getTime());
		} else {
			//回滚到上一个月
			c.roll(Calendar.DATE, false);
			endDate =  getDateString(c.getTime());
		}
		//得到当月开始日期
		c.set(Calendar.DAY_OF_MONTH, 1);
		Date dateOfMonth = c.getTime();
		String rollDate = getDateString(dateOfMonth);
		//从开始日期到结束日期，将他们添加到列表中（yyyy-MM-dd）
		while (rollDate.compareTo(endDate) < 0) {
			list.add(rollDate);
			c.roll(Calendar.DATE, true);
			rollDate = getDateString(c.getTime());
		}
		//截至日前的添加
		list.add(endDate);
		return list;
	}
	
	
	public static List<String> getWorkdayList(String month){
		String yearStr=month.split("-")[0];
		String monthStr=month.split("-")[1];
		Calendar c1=Calendar.getInstance();
		Calendar c2=Calendar.getInstance();
		
		c1.set(Calendar.YEAR, Integer.parseInt(yearStr));
		c2.set(Calendar.YEAR, Integer.parseInt(yearStr));
		
		c1.set(Calendar.MONTH, Integer.parseInt(monthStr)-1);
		c2.set(Calendar.MONTH, Integer.parseInt(monthStr)-1);
		
		c1.set(Calendar.DAY_OF_MONTH, 1);//当月1号
		c2.set(Calendar.DAY_OF_MONTH, c2.getActualMaximum(Calendar.DAY_OF_MONTH));//当月最后一天
		List<String> list=new ArrayList<String>();
		while(c1.compareTo(c2)<=0){
			String date=DateUtil.getDateString(c1.getTime());
			list.add(date);
			c1.add(Calendar.DAY_OF_MONTH, 1);
		}
		
		return list;
	}
	
	/**
	 * 判断是否为周六周日
	 * 
	 * @param checkDate
	 *            校验的日期
	 * @return true：是周六周日 false：不是
	 */
	public static boolean isWeekend(String checkDate){
		Date date = to_date(checkDate);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK)-1;
		if(dayOfWeek == 0 || dayOfWeek == 6){
			return true;
		}
		return false;
	}
	
	/**
	 * 获取某年某月的最后一天
	 * @param year
	 * @param month
	 * @return
	 */
	public static int getLastDayForYearAndMonth(int year,int month){
		/**
		 * 与其他语言环境敏感类一样，Calendar 提供了一个类方法 getInstance，
		 * 以获得此类型的一个通用的对象。Calendar 的 getInstance 方法返回一
		 * 个 Calendar 对象，其日历字段已由当前日期和时间初始化： 
		 */
        Calendar calendar = Calendar.getInstance();
        /**
         * 实例化日历各个字段,这里的day为实例化使用
         */
        calendar.set(year,month-1,1);
        /**
         * Calendar.Date:表示一个月中的某天
         * calendar.getActualMaximum(int field):返回指定日历字段可能拥有的最大值
         */
        return calendar.getActualMaximum(Calendar.DATE);
	}
	
	/**
	 * 获取周日和周六的日期
	 * @param getSunday
	 * @return
	 */
	public static String getDateByWeek(boolean getSunday){
		String date = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		// 星期的排列是，星期日，星期一.......星期六 (1-7)
		if (getSunday) {
			c.set(Calendar.DAY_OF_WEEK, 1);
		} else {
			c.set(Calendar.DAY_OF_WEEK, 7);
		}
		date = sdf.format(c.getTime());
		return date;
	}
	
	/**
	 * 自动获取周
	 * @return
	 */
	public static String[] getWeekDate(Date date){
		String[] week = new String[2];
		Calendar c = Calendar.getInstance();  
		c.setTime(date);
		int weekday = c.get(7)-1;  
		c.add(5,-weekday);  
		week[0]=getDateString(c.getTime());
		c.add(5,6);  
		week[1]=getDateString(c.getTime());
		return week;
	}
	
	/**
	 * 获取上周日期数据
	 * @return
	 */
	public static String[] getBeforeWeekDate(){
		String[] week = new String[2];
		Calendar c = Calendar.getInstance();
	    // 减去一个星期
	    c.add(Calendar.WEEK_OF_MONTH, -1);
	    // 上个星期的今天是第几天,星期天是1,所以要减去1
	    int d = c.get(Calendar.DAY_OF_WEEK) ;
	    // 添加余下的天数
	    c.add(Calendar.DAY_OF_WEEK, 7 - d);
	    week[0]=getDateString(c.getTime());
	    System.out.println(week[0]);
	    c.add(Calendar.DAY_OF_WEEK, c.getTime().getDay()-(7+c.getTime().getDay()-1));
	    week[1]=getDateString(c.getTime());
	    System.out.println(week[1]);
	    return week;
	}
	
	/**
	 * 根据日期，算出是该月的第几周
	 * @return
	 */
	public static String getWeekCountByDate(Date sdate){
		try{
			Calendar cal=Calendar.getInstance();
			cal.setTime(sdate);
			String weekDesc=cal.get(Calendar.YEAR)+"年"+(cal.get(Calendar.MONTH)+1)+"月第"+cal.get(Calendar.WEEK_OF_MONTH)+"周";
			return weekDesc;
		}catch(Exception e){
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * 日期相减得天数
	 * @param endDate
	 * @param beginDate
	 * @return
	 */
	public static String getDayByEdateToSdate(Date endDate,Date beginDate){
        Long day=(endDate.getTime()-beginDate.getTime())/(24*60*60*1000)-1;
        return day+"";
	}
	
	/**
	 * 判断日期是否在修改范围内
	 * @param signtime
	 * @return
	 */
	public static boolean flagSigntimeTimeOut(Date date){
		String day = Configure.getProperty("stopDateDay");
		int initDay = 0;
		if(!"".equals(day)){
			initDay = Integer.parseInt(day);
		}else{
			return true;
		}
		String signtime = DateUtil.getDateString(date);
		String[] signtimes = signtime.split("-");
		String currendate = DateUtil.getDateString(new Date());
		String[] currendates = currendate.split("-");
		int year = Integer.parseInt(signtimes[0])-Integer.parseInt(currendates[0]);
		int month = Integer.parseInt(signtimes[1])-Integer.parseInt(currendates[1]);
		int cday = Integer.parseInt(currendates[2]);
		if(year<-1){//小于一年以上
			return false;
		}else if(year>0){//大于一年后一年以上
			return true;
		}else if(year==-1){//小于一年
			if(month!=11){//2015-01-10与2014-12-20，相差11，不是12月数据
				return false;
			}else if(month==11){//是12月份数据
				if(cday>=initDay){//大于等于指定日期
					return false;
				}else{//小于指定日期
					return true;
				}
			}
		}else if(month==0){
			return true;
		}else if(month!=-1){//同一年，相差一个月以上
			return false;
		}else if(month==-1){//同一年，相差一个月
			if(cday>=initDay){
				return false;
			}else{
				return true;
			}
		}
		return true;
	}
	
	/**
	 * 根据oa_holiday,获取上一个工作日
	 * @return
	 */
	public static String getPreviousWorkDate(){
		String currentDate = DateUtil.getDateString(new Date());
		DayLogService dayLogService=(DayLogService)BaseApplicationContext.getAppContext().getBean("dayLogService"); 
		String yesterday = dayLogService.getYesterdayWorkDate(currentDate);
		return yesterday;
	}
	
	/**
	 * 获取当前日期的前一天
	 * @return
	 */
	public static String getYesterdayByCurrentDate(){
		Calendar cal=Calendar.getInstance();
		cal.add(Calendar.DATE,-1);
		Date d=cal.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String yesterday = sdf.format(d);//获取昨天日期
		return yesterday;
	}

	/**获取指定时间段内的月份列表
	 * @param startMonth
	 * @param endMonth
	 * @return
	 */
	public static List<String> getMonthListByBucket(String startMonth,String endMonth) {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
		Calendar startCal=Calendar.getInstance();
		Calendar endCal=Calendar.getInstance();
		List<String> monthList=new ArrayList<String>();
		try{
			Date sMonth=sdf.parse(startMonth);
			Date eMonth=sdf.parse(endMonth);
			startCal.setTime(sMonth);
			endCal.setTime(eMonth);
			
			while(startCal.before(endCal)||startCal.compareTo(endCal)==0){
				String month=sdf.format(startCal.getTime());
				monthList.add(month);
				startCal.add(Calendar.MONTH, 1);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return monthList;
	}
}
