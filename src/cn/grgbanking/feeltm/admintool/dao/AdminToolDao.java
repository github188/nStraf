package cn.grgbanking.feeltm.admintool.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.dayLog.domain.DayLog;
import cn.grgbanking.feeltm.domain.testsys.WeekReport;
import cn.grgbanking.framework.dao.BaseDao;

@Repository("adminToolDao")
public class AdminToolDao extends BaseDao<WeekReport>{
	/**
	 * 从日志表中查询当月的数据
	 * @return 当月的日志数据
	 */
	public List<DayLog> queryDayLogStatistic(String operateTime){		
		//如果当前为月首日,则取上一个月的整月来进行人日统计
		//如果不是为首日,则取当前月来统计
		List<String> dateList = getWorkdayList(operateTime);
		String startDate ="";
		String endDate = "";
		if (dateList.size() == 1) {
			startDate = endDate = dateList.get(0);
		}else{
			startDate =  dateList.get(0);
			endDate = dateList.get(dateList.size() - 1);
		}
		StringBuilder sbdHql = new StringBuilder("");
		sbdHql.append(" FROM DayLog l ");
		sbdHql.append(" WHERE l.logDate >= to_date(?,'yyyy-MM-dd')");
		sbdHql.append(" AND l.logDate <= to_date(?,'yyyy-MM-dd')");
		sbdHql.append(" ORDER BY l.prjName, l.userId,l.logDate");
		List<DayLog> logs=(List<DayLog>)this.getHibernateTemplate().find(sbdHql.toString()
				,new Object[]{startDate,endDate});
		return logs;
	}

	
	
	/**
	 * 从日志表中查询当月的数据
	 * @return 当月的日志数据
	 */
	public List<DayLog> queryDayLogOrderBydept(String operateTime){		
		//如果当前为月首日,则取上一个月的整月来进行人日统计
		//如果不是为首日,则取当前月来统计
		List<String> dateList = getWorkdayList(operateTime);
		String startDate ="";
		String endDate = "";
		if (dateList.size() == 1) {
			startDate = endDate = dateList.get(0);
		}else{
			startDate =  dateList.get(0);
			endDate = dateList.get(dateList.size() - 1);
		}
		StringBuilder sbdHql = new StringBuilder("");
		sbdHql.append(" FROM DayLog l ");
		sbdHql.append(" WHERE l.logDate >= to_date(?,'yyyy-MM-dd')");
		sbdHql.append(" AND l.logDate <= to_date(?,'yyyy-MM-dd')");
		sbdHql.append(" ORDER BY l.detName, l.userId,l.logDate");
		List<DayLog> logs=(List<DayLog>)this.getHibernateTemplate().find(sbdHql.toString()
				,new Object[]{startDate,endDate});
		return logs;
	}
	
	/**
	 * 取当天以前到月初的日期，如果当天是月初则取上一个月的所有日期
	 * 
	 * @return 日期列表(yyyy-MM-dd)
	 */
	public static List<String> getWorkdayList(String operateTime){
		List<String> list = new ArrayList<String>();
		String endDate = operateTime;
		//得到当月开始日期
		String rollDate = operateTime.substring(0,8)+"01";
		//从开始日期到结束日期，将他们添加到列表中（yyyy-MM-dd）
		int i=1;
		while (rollDate.compareTo(endDate) < 0) {
			list.add(rollDate);
			i++;
			if(i<=9){
				rollDate = rollDate.substring(0,9)+String.valueOf(i);
			}
			else{
				rollDate = rollDate.substring(0,8)+String.valueOf(i);
			}
		}
		//截至日前的添加
		list.add(endDate);
		return list;
	}
}
