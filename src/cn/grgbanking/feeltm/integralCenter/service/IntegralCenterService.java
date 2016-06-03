package cn.grgbanking.feeltm.integralCenter.service;
/**
 * @author whxing
 * 积分中心service
 */
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.config.BusnDataDir;
import cn.grgbanking.feeltm.dayLog.domain.DayLog;
import cn.grgbanking.feeltm.domain.SysUser;
import cn.grgbanking.feeltm.domain.testsys.Instance;
import cn.grgbanking.feeltm.instance.dao.InstanceDao;
import cn.grgbanking.feeltm.integralCenter.dao.IntegralCenterDao;
import cn.grgbanking.feeltm.integralCenter.domain.IntegralInfo;
import cn.grgbanking.feeltm.project.dao.ProjectDao;
import cn.grgbanking.feeltm.projectweekplan.service.ProjectWeekPlanService;
import cn.grgbanking.feeltm.staff.service.StaffInfoService;
import cn.grgbanking.feeltm.util.DateUtil;
import cn.grgbanking.framework.util.Page;

@Service("integralCenterService")
@Transactional
public class IntegralCenterService {
	@Autowired
	private IntegralCenterDao integralCenterDao;
	
	@Autowired
	private StaffInfoService staffInfoService;
	
	@Autowired
	public ProjectWeekPlanService projectWeekPlanService;
	@Autowired
	public ProjectDao projectDao;
	@Autowired
	public InstanceDao instanceDao;
	
	private IntegralInfo integralInfo;
	/**
	 * 通过 日志参数保存积分对象
	 * @param param
	 */
	public String saveIntegralInfoByLogDayParamMap(Map<String, Object> param){
		SysUser usr = (SysUser) param.get("usr");
		List<DayLog> daylogList = (List<DayLog>) param.get("daylogList");
		setIntegralInfo(usr);
			
			 java.util.Date logDate = (java.util.Date) param.get("logDate");
			 String logDataStr = DateUtil.getDateString(logDate);
			if (null!=BusnDataDir.getMapKeyValue("integralCenter.integralGategory")) {
				integralInfo.setGategory(StringUtils.trim((String)BusnDataDir.getMapKeyValue("integralCenter.integralGategory").get("rizhi")));//数字字典
			}
			
			integralInfo.setGategoryId("rizhi");//类别ID
			
			java.util.Date createDate = new java.util.Date();
			
			integralInfo.setCreateTime(createDate);
			integralInfo.setLogDate(logDate);
			
			
			
			int between = DateUtil.daysBetween(logDate,createDate);
			String msg = "";
			if(null!=BusnDataDir.getMapKeyValue("integralCenter.integralRule")){
				if(between<1){
				integralInfo.setReason(usr.getUsername()+"于"+DateUtil.to_char(createDate, "yyyy年MM月dd日 HH:mm:ss")
						+"按时新增"+logDataStr+"日志记录获得"+StringUtils.trim((String)BusnDataDir.getMapKeyValue("integralCenter.integralRule").get("dayLogGrade"))+"积分");
				integralInfo.setIntegral(Integer.parseInt(StringUtils.trim((String) BusnDataDir.getMapKeyValue("integralCenter.integralRule").get("dayLogGrade"))));//数字字典读积分
				msg = "按时新增"+logDataStr+"日志记录获得"+StringUtils.trim((String)BusnDataDir.getMapKeyValue("integralCenter.integralRule").get("dayLogGrade"))+"积分";
				}
				else if (1<=between&&between<=7) {
					integralInfo.setReason(usr.getUsername()+"于"+DateUtil.to_char(createDate, "yyyy年MM月dd日 HH:mm:ss")
							+"补填"+logDataStr+"日志记录获得"+StringUtils.trim((String)BusnDataDir.getMapKeyValue("integralCenter.integralRule").get("dayLogNotOnTimeGrade"))+"积分,"+"已经超时"+between+"天。");
					integralInfo.setIntegral(Integer.parseInt(StringUtils.trim((String) BusnDataDir.getMapKeyValue("integralCenter.integralRule").get("dayLogNotOnTimeGrade"))));//数字字典读积分
					msg = "补填"+logDataStr+"日志记录获得"+StringUtils.trim((String)BusnDataDir.getMapKeyValue("integralCenter.integralRule").get("dayLogNotOnTimeGrade"))+"积分,"+"已经超时"+between+"天。";
				}
				else{
					integralInfo.setReason(usr.getUsername()+"于"+DateUtil.to_char(createDate, "yyyy年MM月dd日 HH:mm:ss")
							+"超时补填写"+logDataStr+"日志记录获得"+((String)BusnDataDir.getMapKeyValue("integralCenter.integralRule").get("dayLogTimeoutGrade"))+"积分,"+"已经超时"+between+"天。");
					integralInfo.setIntegral(Integer.parseInt(StringUtils.trim((String) BusnDataDir.getMapKeyValue("integralCenter.integralRule").get("dayLogTimeoutGrade"))));//数字字典读积分
					msg = "超时补填写"+logDataStr+"日志记录获得"+((String)BusnDataDir.getMapKeyValue("integralCenter.integralRule").get("dayLogTimeoutGrade"))+"积分,"+"已经超时"+between+"天。";
				}
			}
		if (null!=param.get("submitOldDate")){
			java.util.Date submitOldDate = (java.util.Date) param.get("submitOldDate");
			integralCenterDao.deleteIntegralInfoByDayLogParam(integralInfo.getUserId(), submitOldDate);
		}
		integralCenterDao.saveByIntegralInfo(integralInfo);
		return msg;
	}
	
	
	/**
	 * 通过周报参数记录积分
	 */
	public String saveIntegralInfoByWeekDayParamMap(Map<String, Object> param){
		SysUser usr = (SysUser) param.get("usr");
		Date endDate = (Date) param.get("endDate");
		setIntegralInfo(usr);
//		personWeekLog.getGroupName();
//		List<Project> projectlist= projectDao.getProjectByUserId(usr.getUserid());
//		for (Object obj : projectlist) {
//			Project project = (Project) obj;
//			
//		}
		    if (null!= BusnDataDir.getMapKeyValue("integralCenter.integralGategory")){
		    String asdString = (String)BusnDataDir.getMapKeyValue("integralCenter.integralGategory").get("zhoubao");
			integralInfo.setGategory(StringUtils.trim(asdString));//数字字典
		    }
			integralInfo.setGategoryId("zhoubao");//类别ID
			
			java.util.Date createDate = new java.util.Date();
			
			integralInfo.setCreateTime(createDate);
			
			integralInfo.setLogDate(endDate);
			
			
			int between = DateUtil.daysBetween(createDate,endDate);
			String msg = "";
			if(null!=BusnDataDir.getMapKeyValue("integralCenter.integralRule")){
				if(between>=0&&between<=7){
				integralInfo.setReason(usr.getUsername()+"于"+DateUtil.to_char(createDate, "yyyy年MM月dd日 HH:mm:ss")
						+"按时新增个人周报获得"+StringUtils.trim((String)BusnDataDir.getMapKeyValue("integralCenter.integralRule").get("weekLogGrade"))+"积分。");
				integralInfo.setIntegral(Integer.parseInt(StringUtils.trim((String) BusnDataDir.getMapKeyValue("integralCenter.integralRule").get("weekLogGrade"))));//数字字典读积分
				msg = "按时新增个人周报获得"+StringUtils.trim((String)BusnDataDir.getMapKeyValue("integralCenter.integralRule").get("weekLogGrade"))+"积分。";
				}
				else{
					integralInfo.setReason(usr.getUsername()+"于"+DateUtil.to_char(createDate, "yyyy年MM月dd日 HH:mm:ss")
							+"超时未填写个人周报获得"+((String)BusnDataDir.getMapKeyValue("integralCenter.integralRule").get("weekLogTimeout"))+"积分,"+"已经超时"+between+"天。");
					integralInfo.setIntegral(Integer.parseInt(StringUtils.trim((String) BusnDataDir.getMapKeyValue("integralCenter.integralRule").get("weekLogTimeout"))));//数字字典读积分
					msg = "超时未填写个人周报获得"+((String)BusnDataDir.getMapKeyValue("integralCenter.integralRule").get("weekLogTimeout"))+"积分,"+"已经超时"+between+"天。";
				}
			}
		integralCenterDao.saveByIntegralInfo(integralInfo);
		return msg;
	}
	//新增发表赞扬，
	public String queryPraiseLimitByInstanceParamMap(Map<String, Object> param){
		SysUser usr = (SysUser) param.get("usr");
		java.util.Date createDate = new java.util.Date();
		  if (integralCenterDao.queryPraiseLimit(usr.getUserid(), createDate)) {
			  return "等待部门经理确认后,获得"+StringUtils.trim((String)BusnDataDir.getMapKeyValue("integralCenter.integralRule").get("praiseOrPraised"))+"积分。详情请到个人积分中心";
		  }
		  else{
			  return "今天已经超出发表赞扬获得积分的上限"; 
		  }
	}
	
	//经理确认后记录相应积分
	public String saveIntegralInfoByInstanceParamMap(Map<String, Object> param){
	    SysUser usr = (SysUser) param.get("usr");
	    Instance instance =  (Instance) param.get("instance");
	    String instanceId = (String) param.get("instanceId");
	    java.util.Date createDate = new java.util.Date();
	    //判断当天是否已经超出发表赞扬上限
	    if (integralCenterDao.queryPraiseLimit(usr.getUserid(), createDate)) {
	    setIntegralInfo(usr);
	    if (null!=BusnDataDir.getMapKeyValue("integralCenter.integralGategory")) {
			integralInfo.setGategory(StringUtils.trim((String) BusnDataDir.getMapKeyValue("integralCenter.integralGategory").get("gainPraise")));//数字字典
		}
		integralInfo.setGategoryId("praise");//类别ID
		integralInfo.setCreateTime(createDate);
		integralInfo.setLogDate(instance.getCreate_date());
		integralInfo.setExt4(instanceId);
		integralInfo.setNote("1");
		if(null!=BusnDataDir.getMapKeyValue("integralCenter.integralRule")&&StringUtils.isNotBlank(instance.getCategory())){
			integralInfo.setReason(instance.getCreate_man()+"于"+DateUtil.to_char(createDate, "yyyy年MM月dd日 HH:mm:ss")
					+instance.getCategory().trim()+"了"+instance.getEmbracer_man()+"获得了"+StringUtils.trim((String)BusnDataDir.getMapKeyValue("integralCenter.integralRule").get("praiseOrPraised"))+"积分。");
			integralInfo.setIntegral(Integer.parseInt(StringUtils.trim((String) BusnDataDir.getMapKeyValue("integralCenter.integralRule").get("praiseOrPraised"))));//数字字典读积分
		}
		else{
			integralInfo.setReason(instance.getCreate_man()+"于"+DateUtil.to_char(createDate, "yyyy年MM月dd日 HH:mm:ss")
					+"赞扬了"+instance.getEmbracer_man()+"获得了"+StringUtils.trim((String)BusnDataDir.getMapKeyValue("integralCenter.integralRule").get("praiseOrPraised"))+"积分。");
			integralInfo.setIntegral(Integer.parseInt(StringUtils.trim((String) BusnDataDir.getMapKeyValue("integralCenter.integralRule").get("praiseOrPraised"))));//数字字典读积分

		}
		integralCenterDao.saveByIntegralInfo(integralInfo);
		
	    }
	    else{
	    	 return instance.getCreate_man()+"今天已经超出发表赞扬获得积分的上限";
	    }
	    //忠告或建议不记录积分
	    if (StringUtils.trim((String) BusnDataDir.getMapKeyValue("systemConfig.instancesource").get("TX"))
	.equals(instance.getCategory().trim())||StringUtils.trim((String) BusnDataDir.getMapKeyValue("systemConfig.instancesource").get("ZS"))
	.equals(instance.getCategory().trim())) {
	    	 SaveBePraised(instance,instanceId);
		}
	    if("edit".equals(param.get("operat").toString())){
	    	return "修改记录对应积分是"+StringUtils.trim((String)BusnDataDir.getMapKeyValue("integralCenter.integralRule").get("praiseOrPraised"))+"积分。";
	    }
	    else{
	    	return instance.getCreate_man()+"获得了"+StringUtils.trim((String)BusnDataDir.getMapKeyValue("integralCenter.integralRule").get("praiseOrPraised"))+"积分。";
	    }
	    
	}
	
	/**
	 * 受到赞扬者分别记录积分表
	 * @param instance
	 */
	public  void SaveBePraised(Instance instance, String instanceId){
		String idString = instance.getEmbracerids();
		String[] idS = idString.split(",");
		for (String userid : idS) {
			if (StringUtils.isNotBlank(userid)) {
			SysUser usr= staffInfoService.findUserByUserid(userid);
			setIntegralInfo(usr);
			if (null!=BusnDataDir.getMapKeyValue("integralCenter.integralGategory")) {
				integralInfo.setGategory(StringUtils.trim((String) BusnDataDir.getMapKeyValue("integralCenter.integralGategory").get("gainPraise")));//数字字典
			}
			integralInfo.setGategoryId("gainPraise");//类别ID
			java.util.Date createDate = new java.util.Date();
			integralInfo.setCreateTime(createDate);
			integralInfo.setLogDate(instance.getCreate_date());
			integralInfo.setExt4(instanceId);
			integralInfo.setNote("0");
			if(null!=BusnDataDir.getMapKeyValue("integralCenter.integralRule")){
				integralInfo.setReason(usr.getUsername()+"于"+DateUtil.to_char(createDate, "yyyy年MM月dd日 HH:mm:ss")
						+"受到"+instance.getCreate_man()+"的赞扬，获得了"+StringUtils.trim((String)BusnDataDir.getMapKeyValue("integralCenter.integralRule").get("praiseOrPraised"))+"积分。");
				integralInfo.setIntegral(Integer.parseInt(StringUtils.trim((String) BusnDataDir.getMapKeyValue("integralCenter.integralRule").get("praiseOrPraised"))));//数字字典读积分
			}
			integralCenterDao.saveByIntegralInfo(integralInfo);
			}
		}
		
	}
	
	
	//赋值
	public void setIntegralInfo(SysUser usr){
		integralInfo = new IntegralInfo();
		integralInfo.setUserId(usr.getUserid());
		integralInfo.setUserName(usr.getUsername());
		integralInfo.setDetNameId(usr.getDeptName());
		if(StringUtils.isNotBlank(usr.getDeptName())){
			integralInfo.setDetName(staffInfoService.getDeptNameValueByKey(usr.getDeptName()));
		}
		integralInfo.setStatu("1");
	}
	
	/**
	 * 删除日志的积分
	 * @param param
	 */
	public void deleteIntegralInfoByParam(Map<String, Object> param){
		String userId = (String) param.get("userId");
		java.util.Date logDate = (Date) param.get("logDate");
		integralCenterDao.deleteIntegralInfoByDayLogParam(userId, logDate);
	}
	
	/**
	 * 删除周报的积分
	 * @param param
	 */
	public void deleteIntegralInfoByWeekLogParam(Map<String, Object> param){
		String userId = (String) param.get("userId");
		java.util.Date logDate = (Date) param.get("endDate");
		integralCenterDao.deleteIntegralInfoByWeekLogLogParam(userId, logDate);
	}
	
	/**
	 * 删除爱心小鱼的积分
	 * @param param
	 */
	public void deleteIntegralInfoByInstanceId(String instanceId){
		integralCenterDao.deleteIntegralInfoByInstanceId(instanceId);
	}
	
	/**
	 * 根据全公司员工查询
	 * @return
	 */
	public List orderByStaff(){
		return integralCenterDao.orderByStaff();
	}
	/**
	 * 根据部门查询
	 * @param deptId
	 * @return
	 */
	public List orderByDept(String deptId){
		return integralCenterDao.orderByDept(deptId);
	}
	/**
	 * 根据项目查询
	 * @param List
	 * @return
	 */
	public List orderByGroup(String userId){
		return integralCenterDao.orderByGroup(userId);
	}
	
	/**
	 * 个人总积分
	 * @param userId
	 * @return
	 */
	public List getPersonIntegral(String userId){
		return integralCenterDao.getPersonIntegral(userId);
	}
	
	//查询积分详情
	public Page getPage(String createDate,String gategoryId,String userId,
			int pageNum, int pageSize,String endDate) {
		return integralCenterDao.getPage(createDate, gategoryId, userId, pageNum, pageSize, endDate);
	}
//	/**
//	 * 个人积分公司总排名
//	 * @param userId
//	 * @return
//	 */
//	public List getPersonRankingOrderbyStaff(String userId){
//		return integralCenterDao.getPersonRankingOrderbyStaff(userId);
//	}
	//当修改员工信息部门时，对应员工积分部门修改
    public void updateIntegral(String detid,String detName,String userId){
    	integralCenterDao.updateIntegral(detid, detName, userId);
    }
}

