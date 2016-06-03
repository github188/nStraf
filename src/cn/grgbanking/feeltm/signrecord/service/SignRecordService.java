package cn.grgbanking.feeltm.signrecord.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.approval.service.ApprovalService;
import cn.grgbanking.feeltm.config.BusnDataDir;
import cn.grgbanking.feeltm.config.UserRoleConfig;
import cn.grgbanking.feeltm.context.BaseApplicationContext;
import cn.grgbanking.feeltm.domain.SignRecord;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.signrecord.dao.SignRecordDao;
import cn.grgbanking.feeltm.signrecord.domain.SignBind;
import cn.grgbanking.feeltm.staff.dao.StaffInfoDao;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.DateUtil;
import cn.grgbanking.framework.service.BaseService;
import cn.grgbanking.framework.util.Page;

@Service("signRecordService")
@Transactional
public class SignRecordService extends BaseService {
	@Autowired
	private SignRecordDao signRecordDao;
	
	@Transactional(readOnly = true)
	public Page getPage(int pageNum,int pageSize,UserModel userModel){
		return signRecordDao.getPage(pageNum, pageSize, userModel);
	}
	
	@Transactional(readOnly = true)
	public Page getPageByCondition(SignRecord signRecord,int pageNum, int pageSize, String signTime, String signEndTime, UserModel userModel){
		return signRecordDao.getPageByCondition(signRecord,pageNum, pageSize, signTime, signEndTime, userModel);
	}
	
	@Transactional(readOnly = true)
	public Page getPageByDate(int pageNum,int pageSize,String signTime,String signEndTime){
		return signRecordDao.getPageByDate(pageNum, pageSize, signTime, signEndTime);
	}
	
	public List getMobileExportData(String sdate,String edate,String prjname) {
		return signRecordDao.getMobileExportData(sdate,edate,prjname);
	}
	
	

	/**
	 * 获取所有绑定记录
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * lhyan3
	 * 2014年6月9日
	 */
	public Page findAllBind(String deptname,String grpname,String userid,String status,int pageNum, int pageSize) {
		return signRecordDao.findAllBind(deptname,grpname,userid,status,pageNum,pageSize);
	}

	/**
	 * 解除绑定
	 * @param userids
	 * lhyan3
	 * 2014年6月9日
	 */
	public void releaseBind(String userids) {
		String[] users = userids.split(",");
		List<SignBind> binds = new ArrayList<SignBind>();
		Date now = new Date();
		if(users.length>0){
			for(String userid:users){
				SignBind bind = signRecordDao.getBindByUserid(userid);
				if(bind!=null){
					bind.setPhoneid("");
					bind.setReleaseTime(now);
					bind.setStatus("已解绑");
					binds.add(bind);
				}
			}
		}
		signRecordDao.releaseBind(binds);
	}

	/**
	 * 查询
	 * @param deptName
	 * @param grpName
	 * @param userId
	 * @param pagenum
	 * @param pagesize
	 * @param signTime
	 * @param signEndTime
	 * @param userModel
	 * @param approveStatus 
	 * @param approvePerson 
	 * @return
	 * lhyan3
	 * 2014年7月8日
	 */
	public Page getPageByCondition(String deptName, String grpName,
			String userId, int pagenum, int pagesize, String signTime,
			String signEndTime, UserModel userModel, String approvePerson, String approveStatus) {
		Page page = signRecordDao.getPageByCondition(deptName,grpName,userId,pagenum, pagesize, signTime, signEndTime, userModel,approvePerson,approveStatus);
		return page;
	}
	/**
	 * 导出移动签到数据
	 * @param deptName
	 * @param grpName
	 * @param userId
	 * @param signTime
	 * @param signEndTime
	 * @param userModel
	 * @param approvePerson
	 * @param approveStatus
	 * @return
	 */
	public List getListByCondition(String deptName, String grpName,
			String userId, String signTime,
			String signEndTime, UserModel userModel, String approvePerson, String approveStatus) {
		List signRecordList = signRecordDao.getListByCondition(deptName,grpName,userId,signTime, signEndTime, userModel,approvePerson,approveStatus);
		return signRecordList;
	}

	/**
	 * @return
	 * lhyan3
	 * 2014年6月17日
	 */
	public List<SignRecord> getRecordAddrisNull() {
		return signRecordDao.getRecordAddrisNull();
	}

	/**
	 * @param record
	 * lhyan3
	 * 2014年6月17日
	 */
	public void update(SignRecord record) {
		signRecordDao.updateObject(record);
	}
	
	public void add(SignRecord record) {
		signRecordDao.addObject(record);
	}
	
	public boolean addSignRecordListInfo(List<SignRecord> overSingRecordList) {
		boolean flag = false;
		try {
			for(SignRecord signRecord:overSingRecordList)
				add(signRecord);
			flag = true;
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			SysLog.error(e);
			throw new NullPointerException();
		}
		return flag;
	}
	
	/**
	 * 判断用户在该时间段是否存在记录，有return false，无return true
	 * @param userid
	 * @param signTime
	 * @return
	 */
	public boolean checkOt(String userid, Date signTime){
		boolean flag = true;
		try {
			flag = signRecordDao.checkExist(userid, signTime);
		} catch (Exception e1) {
			flag = false;
			e1.printStackTrace();
			SysLog.error(e1);
		}
		return flag;
	}
	
	public boolean updateStatus(){
		boolean flag = signRecordDao.updateStatus();
		return flag;
	}

	/**
	 * 获得该用户近期常用签到地址
	 * @param userid
	 * @return
	 */
	public List<String> getLastAreaByUserId(String userid) {
		List arryList =  signRecordDao.getLastAreaByUserId(userid);
		List<String> stringList = new ArrayList<String>();
		for(int i=0;i<arryList.size();i++){
			if(arryList.get(i)!=null&&!arryList.get(i).toString().equals("")&&!"null".equals(arryList.get(i).toString())){
				stringList.add(arryList.get(i).toString());
			}
		}
		return stringList;
	}

	public SignRecord findById(String id) {
		return (SignRecord) signRecordDao.getObject(SignRecord.class,id);
	}

	/**
	 * 根据 id字符串，查询记录集合
	 * @param ids
	 * @return
	 */
	public List<SignRecord> getRecordsByIds(String ids) {
		String id = "('"+ids.replace(",", "','")+"')";
		return signRecordDao.getRecordsByIds(id);
	}

	/**
	 * 根据id数组，，提交审核信息。
	 * @param ids
	 * @param userModel
	 * @return
	 */
	public String updateSignList(String ids, UserModel userModel) {
		int count = 0;
		String resultMsg ="";
		int nodealCount = 0;
		try {
			String[] idarry = ids.split(","); 
			for(int i=0;i<idarry.length;i++){
				SignRecord sign = this.findById(idarry[i]);
				if(!sign.getUserId().equals(userModel.getUserid())){
					count++;
					continue;
				}
//				if(UserRoleConfig.getJob(sign.getApprovePerson()).contains(UserRoleConfig.hr)){
//					//如果申请人是行政人员则做如下处理
//					if(UserRoleConfig.getJob(sign.getUserId()).contains(UserRoleConfig.hr)){
//						sign.setApproveStatus("3");//部门经理审核
//					}else{
//						sign.setApproveStatus("4");//行政审核
//					}
//				}else if(UserRoleConfig.getJob(sign.getApprovePerson()).contains(UserRoleConfig.deptManagerGroup)){
//					sign.setApproveStatus("3");//部门经理审核
//				}else if(UserRoleConfig.isProjectManager(sign.getApprovePerson())){
//					sign.setApproveStatus("2");//项目经理审核
//				}else {
//					erro =",部分签到信息审核人 错误，不能提交该信息审核；";
//					continue;
//				}
				
				
				//判断签到日期是否超过处理时间
				String str = DateUtil.getDateString(sign.getSignTime());
				boolean flag = DateUtil.flagSigntimeTimeOut(DateUtil.stringToDate(str, "yyyy-MM-dd"));
				if(!flag){
					nodealCount++;
					continue;
				}
				//前面实现逻辑有误，现修改为如下逻辑2015-02-04 ling.tu
				//提交审核逻辑修改为：1、项目经理——部门经理审核；2、部门经理,不用提交审核；3、其他人员——项目经理审核——部门经理审核
				if(UserRoleConfig.isProjectManager(sign.getUserId())){//项目经理审核
					sign.setApproveStatus("3");//部门经理审核
				}else{
					String status = sign.getApproveStatus();
					if("0".equals(status) || "1".equals(status)){//01表示状态为新增、修改中
						sign.setApproveStatus("2");//项目经理审核
					}
				}
				signRecordDao.updateObject(sign);
			}
			resultMsg = "，共处理"+idarry.length+"条记录";
			if(count>0){
				resultMsg += "，其中有"+count+"条他人签到信息不能提交";
			}
			if(nodealCount>0){
				resultMsg += "，有"+nodealCount+"条签到信息超过处理时间";
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			throw new NullPointerException();
		}
		return resultMsg;
	}

	/**
	 * 审核签到信息，
	 * @param userModel
	 * @param signRecord
	 * @param option 
	 * @param approvalService 
	 * @return
	 */
	public void approvalsign(UserModel userModel, SignRecord signRecord, String option, ApprovalService approvalService)throws Exception {
		StaffInfoDao userDao =  (StaffInfoDao) BaseApplicationContext.getAppContext().getBean("staffInfoDao");
			SignRecord resign = this.findById(signRecord.getId());
			//resign.setApproveStatus(signRecord.getApproveStatus());
			String approvePerson = resign.getApprovePerson();
			if(signRecord.getApproveStatus().equals("5")){
				resign.setApproveStatus(signRecord.getApproveStatus());
				signRecord.setApprovePerson(approvePerson);
			}else if(signRecord.getApproveStatus().equals("6")){
				resign.setApprovePerson(signRecord.getApprovePerson());
				signRecord.setApprovePerson(approvePerson);
				//if(resign.getApproveStatus().equals("4")){//行政审核
				if(resign.getApproveStatus().equals("3")){
					resign.setApproveStatus("6");//审核通过
					resign.setApprovePerson(approvePerson);
					resign.setVilid("1");
					if(option==null||option.trim().equals(""))
						option = "通过";			//审核通过情况下，默认的审核结论
					//根据考勤时间得出考勤状态
					String s_signtime = cn.grgbanking.feeltm.util.DateUtil.getTimeString(resign.getSignTime()).split(" ")[1];
					int status = getAttendanceStatus(s_signtime);
					resign.setAttendanceStatus(status);
				}
				//2014.11.11 tling1 需求改动，不需要行政审核，部门经理审核通过后，即可
				/*else if(UserRoleConfig.getJob(resign.getApprovePerson()).contains(UserRoleConfig.hr)){
					resign.setApproveStatus("4");//行政审核
				}*/
				else  if(UserRoleConfig.getJob(resign.getApprovePerson()).contains(UserRoleConfig.deptManagerGroup)){
					resign.setApproveStatus("3");//部门经理审核
				}else if(UserRoleConfig.isProjectManager(resign.getApprovePerson())){
					resign.setApproveStatus("2");//项目经理审核
				}
			}
			String name = "备注签到";
			if(resign.getType().equals("3"))
				name = "补签到";
			
			approvalService.makeRecored(resign.getId(), name+"信息审核",
					userDao.getUsernameById(signRecord.getApprovePerson()),option ,resign.getApproveStatus());
			
				
			this.update(resign);
	}
	
	public static int getAttendanceStatus(String signtime){
		//判断签到时间是上午还是下午，填写考勤状态
		Map<String,String> attendanceConfigDir = BusnDataDir.getMapKeyValue("systemConfig.systemParamConfig");
		String saparationTime = (String)attendanceConfigDir.get(Constants.SEPARATION_TIME_KEY);
		String deviationTime = (String)attendanceConfigDir.get(Constants.DEVIATION_TIME_KEY);
		if (StringUtils.isBlank(saparationTime)) {
			saparationTime = "13:00:00";
		}
		if (StringUtils.isBlank(deviationTime)) {
			deviationTime = "180";
		}
		int timem = Integer.parseInt(deviationTime)/60;
		String times[] = saparationTime.split(":");
		int middleM = Integer.parseInt(times[1])+timem;
		String middleS = middleM+"";
		if(middleM<10){
			middleS = "0"+middleM;
		}
		saparationTime = times[0]+":"+middleS+":"+times[2];
		int statusKey = 0;
		if(saparationTime.compareTo(signtime) >= 0){
			statusKey = Constants.ATTENDACE_ENTRY_KEY;
		}else{
			statusKey = Constants.ATTENDACE_EXIT_KEY;
		}
		return statusKey;
	}
	
	public String saveUserIdForOutCard(String[] userid,String[] outcard,String[] usernames){
		String username = signRecordDao.saveUserIdForOutCard(userid, outcard,usernames);
		return username;
	}
	
	public boolean flagHasUserid(String userid){
		boolean result = signRecordDao.flagHasUserid(userid);
		return result;
	}
	
	/**
	 * 将有效移动签到数据同步到考勤表中
	 */
	public void synchronizateSignToCard(){
		signRecordDao.synchronizateSignToCard();
		System.out.println("执行移动签到同步到考勤表中");
	}
	
	/**
	 * 获取用户所在项目组
	 * @param userid
	 * @param signTime
	 * @return
	 */
	public String getGroupnameByuserid(String userid, String signTime){
		try{
			return signRecordDao.getGroupnameByuserid(userid, signTime);
		}catch(Exception e){
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * 根据当前用户角色列出需要自己进行批量审核的移动签到数据
	 * 数据记录为当前日期前一个月至当天
	 * @param pageNum
	 * @param pageSize
	 * @param userModel
	 * @return
	 */
	public Page getApprovalMoreSign(int pageNum,int pageSize,UserModel userModel,String status){
		return signRecordDao.getApprovalMoreSign(pageNum, pageSize, userModel, status);
	}
	
	/**
	 * 获取移动签到数据
	 * @param userid
	 * @param status
	 * @return
	 */
	public List<SignRecord> getApprovalRecord(String userid,String status){
		return signRecordDao.getApprovalRecord(userid, status);
	}
	
	/**
	 * 根据部门名称得出部门审核人
	 * @param deptid
	 * @return
	 */
	public String getDeptApprovePeople(String deptid){
		return signRecordDao.getDeptApprovePeople(deptid);
	}
	
	/**
	 * 批量审核时，处理数据
	 * @param userModel
	 * @param signRecord
	 * @param option
	 * @param approvalService
	 * @param approvalDate
	 * @throws Exception
	 */
	public void approvalmoresign(UserModel userModel, SignRecord signRecord, String option, ApprovalService approvalService,String approvalDate)throws Exception {
		StaffInfoDao userDao =  (StaffInfoDao) BaseApplicationContext.getAppContext().getBean("staffInfoDao");
		SignRecord resign = this.findById(signRecord.getId());
		String approvePerson = resign.getApprovePerson();
		if(signRecord.getApproveStatus().equals("5")){
			resign.setApproveStatus(signRecord.getApproveStatus());
			signRecord.setApprovePerson(approvePerson);
		}else if(signRecord.getApproveStatus().equals("6")){
			resign.setApprovePerson(signRecord.getApprovePerson());
			signRecord.setApprovePerson(approvePerson);
			if(resign.getApproveStatus().equals("3")){
				resign.setApproveStatus("6");//审核通过
				resign.setApprovePerson(approvePerson);
				resign.setVilid("1");
				if(option==null||option.trim().equals(""))
					option = "通过";			//审核通过情况下，默认的审核结论
				//根据考勤时间得出考勤状态
				String s_signtime = cn.grgbanking.feeltm.util.DateUtil.getTimeString(resign.getSignTime()).split(" ")[1];
				int status = getAttendanceStatus(s_signtime);
				resign.setAttendanceStatus(status);
			}else  if(UserRoleConfig.getJob(resign.getApprovePerson()).contains(UserRoleConfig.deptManagerGroup)){
				resign.setApproveStatus("3");//部门经理审核
			}else if(UserRoleConfig.isProjectManager(resign.getApprovePerson())){
				resign.setApproveStatus("2");//项目经理审核
			}
		}
		String name = "备注签到";
		if(resign.getType().equals("3"))
			name = "补签到";
		approvalService.makeMoreRecored(resign.getId(), name+"信息审核",userDao.getUsernameById(signRecord.getApprovePerson()),option ,resign.getApproveStatus(),approvalDate);
		this.update(resign);
	}
}
