package cn.grgbanking.feeltm.holiday.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.grgbanking.feeltm.domain.testsys.Holiday;
import cn.grgbanking.feeltm.holiday.dao.HolidayDao;
import cn.grgbanking.feeltm.holiday.service.impl.EasybotsHolidayResolver;
import cn.grgbanking.framework.service.BaseService;

/**
 * 节假日配置
 */
@Service
public class HolidayService extends BaseService{
	
	@Autowired
	private HolidayDao dao;
	
	/**检查某日期是否是节假日
	 * wtjiao 2014年11月11日 上午9:56:52
	 * @param date 查询的日期
	 * @return
	 */
	public boolean isHoliday(Date date){
		String dateStr=new SimpleDateFormat("yyyyMMdd").format(date);
		String hql="from Holiday h where h.checkDate ='"+dateStr+"'";
		List<Holiday> list=dao.findByHql(hql);
		if(list!=null && list.size()>0){
			Holiday hol=list.get(0);
			if("0".equals(hol.getType())){//工作日
				return false;
			}else{//休息日或者法定假日
				return true;
			}
		}else{
			//数据库中没有找到，说明请求第三方的时候出错了，那么这里只做是否是周末判断
			try{
				Calendar calendar=Calendar.getInstance();
				calendar.setTime(date);
				if(calendar.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY || calendar.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY){
					return true;
				}else{
					return false;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			return false;
		}
	}
	
	/**获取指定时间段内的假日
	 * @param startDate 开始日期
	 * @param endDate 截至日期
	 * @return
	 */
	public List<String> getHoliday(Date startDate,Date endDate) throws Exception{
		SimpleDateFormat sdf1=new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat sdf2=new SimpleDateFormat("yyyy-MM-dd");
		String startDateStr=sdf1.format(startDate);
		String endDateStr=sdf1.format(endDate);
		String hql="from Holiday h where h.type<>0 and to_date(h.checkDate,'yyyymmdd')>=to_date('"+startDateStr+"','yyyymmdd') and to_date(h.checkDate,'yyyymmdd')<=to_date('"+endDateStr+"','yyyymmdd')";
		List<Holiday> list=dao.findByHql(hql);
		List<String> holidays=new ArrayList<String>();
		if(list!=null && list.size()>0){
			for(Holiday hol:list){
				holidays.add(sdf2.format(sdf1.parse(hol.getCheckDate())));
			}
		}
		return holidays;
	}
	
	/**同步下一年的节假日数据
	 * wtjiao 2014年11月11日 上午11:45:55
	 */
	public void syschronizedHolidayData(){
		//获取当前年
		Calendar cal=Calendar.getInstance();
		//下一年
		String year=cal.get(Calendar.YEAR)+"";
		//String lastyear=cal.get(Calendar.YEAR)-1+"";
		//获取假期数据
		List<Holiday> holidays=getHolidayData(year);//获取当前年份的假期数据
		
		//删除下一年的数据，并更新成最新的年份数据
		if(holidays.size()>0){
			dao.deleteByYear(year);//删除本年的假期数据,再新增本年的假期数据
			for(int i=0;i<holidays.size();i++){
				dao.addObject(holidays.get(i));
			}
		}
	}

	/**获取假期数据
	 * wtjiao 2014年11月11日 上午11:48:44
	 * @param year
	 * @return
	 */
	private List<Holiday> getHolidayData(String year) {
		ResolveHolidyInterface holidyInterface=new EasybotsHolidayResolver();
		try{
			//先从Easybots中查询节假日
			return holidyInterface.resolve(year);
		}catch(Exception e){
			//从其他方式查询节假日
		}
		return null;
	}
}
