package cn.grgbanking.feeltm.prjchance.webapp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import cn.grgbanking.feeltm.common.util.SystemHelper;
import cn.grgbanking.feeltm.config.BusnDataDir;
import cn.grgbanking.feeltm.domain.PrjChance;
import cn.grgbanking.feeltm.loglistener.domain.WarnInfo;
import cn.grgbanking.feeltm.loglistener.service.TemplateEmailService;
import cn.grgbanking.feeltm.loglistener.service.WarnInfoService;
import cn.grgbanking.feeltm.prjchance.service.PrjChanceFollowService;
import cn.grgbanking.feeltm.prjchance.service.PrjChanceService;
import cn.grgbanking.feeltm.staff.service.StaffInfoService;
import cn.grgbanking.feeltm.util.DateUtil;

public class SendEmailToFollowPeopleAction {
	
	@Autowired
	private StaffInfoService staffInfoService;
	@Autowired
	private PrjChanceService prjChanceService;
	@Autowired
	private WarnInfoService warnInfoService;
	@Autowired
	private PrjChanceFollowService prjChanceFollowService;
	
	
	private PrjChance prjChance=new PrjChance();
	
	/**
	 * 发送邮件给跟进人和抄送人
	 * author:ljlian
	 * 2015-03-31
	 */
	public void sendEmailtofollowpeople(){
		
		if(!SystemHelper.isServerMachine()){//非服务器，不进行任何提示
			return;
		}
		
		try {
			//获取数据字典中的 项目阶段值
			Map  prjStage=BusnDataDir.getMapKeyValue("chanceManage.prjStage");
			//获取所有的项目
			List<PrjChance>  prjChancelist=prjChanceService.findAllPrjChance();
			Date d=new Date();
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			String  currentday=sdf.format(d);
			String title="商机跟进提醒(  "+currentday+"  )";
			String contents="";//发送给抄送用户的  邮件内容
			//遍历所有的商机项目
			if(prjChancelist!=null&&prjChancelist.size()>0){

				//判断  
				for(int i = 0;i<prjChancelist.size();i++){
					//获取抄送用户
					String copyuser=prjChancelist.get(i).getCopyEmail();
					//获取数据字典中的 项目结果值
					Map  prjResult=BusnDataDir.getMapKeyValue("chanceManage.prjResult");
					String  continues=(String) prjResult.get("continue");
					String projectresult=prjChancelist.get(i).getPrjResult();//数据库中读取continuekey
					String  continuevalue=(String) prjResult.get(projectresult);
					//如果项目的结果是持续  
					if(projectresult!=null&&continues.equals(continuevalue)){
						//读取项目的最后跟进时间
						Date   followday=prjChancelist.get(i).getLastFollowDate();
						Date lastEmailtime=prjChancelist.get(i).getLastSendEmailTime();
						SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String lastEmailtimestr=sdf1.format(lastEmailtime);
						//判断该项目的项目阶段有没有变化
						
						String  nowStage=prjChancelist.get(i).getPrjStage();//当前项目的阶段
						Date lastFollowDate=prjChancelist.get(i).getLastFollowDate();
						
						String stage=(String) prjStage.get(nowStage);
						List<String>  lastStagelist=prjChanceFollowService.findLastEmailStage(lastEmailtimestr);//当前项目的阶段
						String  lastStag=lastStagelist.get(0);
						
						if(nowStage!=null&&lastStag!=null&&nowStage.equals(lastStag)){
							String stagekey=prjChancelist.get(i).getPrjStage();
							Map  sendEailperiod=BusnDataDir.getMapKeyValue("chanceManage.sendEailperiod");
							int   period=Integer.parseInt(sendEailperiod.get(stagekey).toString().trim());
							//判断是否需要发送邮件
							boolean isOver=prjChanceFollowService.isOverPrjPeriod(lastFollowDate,lastEmailtime,period);
							
							String followman=prjChancelist.get(i).getFollowMan();
							if(isOver){//超过了时间
								String followmanid=staffInfoService.getUserIdByUsername(followman);
								String email=staffInfoService.getEmailForUserid(followmanid);
								contents+="<br/>跟进人："+followman+"，当前《"+prjChancelist.get(i).getPrjName()+"》项目的项目阶段为"+stage+"进展稍慢，需提醒跟进,请知悉！";
								sendtofollowpeopleEmail(email,copyuser,title,contents);
								System.out.println("邮件提醒人"+copyuser);
								Date date=Calendar.getInstance().getTime();
								System.out.println(date);
								PrjChance p=prjChancelist.get(i);
								p.setLastSendEmailTime(date);
								prjChanceService.updatePrjChanceInfo(p);
							}
						}
						
					}
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		
		
	}
	
	
/**
 * 邮件发送
 * @param sendPersonEmailList  发送人
 * @param copys   抄送人
 * @param title   标题
 * @param content  邮件内容
 */

private void sendtofollowpeopleEmail(String sendPersonEmailList,String copys,String title, String content) {
	
	if(!SystemHelper.isServerMachine()){//非服务器，不进行任何提示
		return;
	}
	TemplateEmailService emailService=new TemplateEmailService();
			
				//填充模版中的指定字段
				Map<String,String> templateTagMap=new HashMap<String,String>();
				String currentDay = DateUtil.getDateString(new Date());
				templateTagMap.put("remandday",currentDay);
				templateTagMap.put("warnInfo",content);
				String sendContent =emailService.sendTemplateMailCopys(sendPersonEmailList,copys, title, "prjchanceSendToFollow.ftl", templateTagMap);
				
				
				//保存发送记录
				if(!"0".equals(sendContent)){
					WarnInfo warn=new WarnInfo();
					warn.setToUserName(sendPersonEmailList);
					warn.setWarnWay("email");
					warn.setWarnType("remind");
					warn.setWarnTime(Calendar.getInstance().getTime());
					warn.setWarnContent(sendContent);
					warnInfoService.save(warn);
				}
			}

	
}


