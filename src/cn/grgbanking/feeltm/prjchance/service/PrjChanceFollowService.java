package cn.grgbanking.feeltm.prjchance.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.domain.PrjChanceFollow;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.prjchance.dao.PrjChanceFollowDao;
@Service("prjChanceFollowService")
@Transactional
public class PrjChanceFollowService {
	@Autowired
	private PrjChanceFollowDao prjChanceFollowDao;

	public void saveprjChanceFollow(PrjChanceFollow prjChanceFollow) {
		try {
				if(prjChanceFollow!=null){
					prjChanceFollowDao.addObject(prjChanceFollow);
				}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			throw new NullPointerException();
		}
		
	}
	/**
	 * 判断  是否需要发送给跟进人和抄送人
	 * @param lastFollowDate  最后一次跟进时间
	 * @param lastEmailday  上一次发邮件的时间
	 * @param period    发送邮件的周期
	 * @return   true    超时,需要发送  邮件           false   没有超时   不需要发送邮件
	 */
	
	public boolean isOverPrjPeriod(Date lastFollowDate,Date lastEmailday,Integer  period) {
		boolean flag=false;
		
		//   如果当前的时间距离上一次发送邮件的时间超过了  发送邮件的周期(数据字典中配置)
		//  并且跟进人 没有跟进项目   则视为  项目进度慢   
		try {
			long  lastEmaildaytime=lastEmailday.getTime();
			Date d=new Date();
			long dl=d.getTime();//当前时间
			
			long result1=(long)((dl-lastEmaildaytime)/ (1000 * 60 * 60 *24) );
			long  overemailtime=period-result1;//是否超过了  发送邮件的周期
			if(overemailtime<=0){
				long lastFollowDate1=lastFollowDate.getTime();//
				long result=(long) ((dl-lastFollowDate1) / (1000 * 60 * 60 *24) );
				long s=period-result;//超过了发送邮件的周期     并且  没有跟进
				if(s<=0){
					flag=true;
					
				}else{
					flag=false;
					
				}
			}
			return flag;
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		
	}
	/**
	 * 查找上一次发送邮件的项目阶段
	 * @param lastEmailtimestr  上一次  发送邮件的事件
	 * @return
	 */
	public List<String> findLastEmailStage(String lastEmailtimestr) {
		
		return prjChanceFollowDao.findLastEmailStage(lastEmailtimestr);
	}
	
	/**
	 * 获取用户需要导出的跟进表信息
	 * @param followMan  跟进人  
	 * @param prjStage  项目的阶段
	 * @param client  客户
	 * @param area  区域
	 * @param province 省市
	 * @param clientManager  客户经理
	 * @param clientType  客户类型
	 * @param prjResult  项目的结果
	 * @return
	 */
	
	
	public List getPrjChanceFollowListByCondition(String followMan,
			String prjStage, String client, String area, String province,
			String clientManager, String clientType, String prjStage2,
			String prjResult) {
		return prjChanceFollowDao.getPrjChanceListByCondition(followMan, prjStage, client, area, province, clientManager, clientType, prjResult);
	}

}
