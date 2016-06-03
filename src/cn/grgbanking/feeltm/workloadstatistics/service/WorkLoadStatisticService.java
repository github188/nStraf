package cn.grgbanking.feeltm.workloadstatistics.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.domain.SysUser;
import cn.grgbanking.feeltm.project.domain.Project;
import cn.grgbanking.feeltm.workloadstatistics.dao.WorkLoadStatisticDao;

@Service("workLoadStatisticService")
@Transactional
public class WorkLoadStatisticService {
	@Autowired
	private  WorkLoadStatisticDao   workLoadStatisticDao;


	
	/**
	 * 按部门统计需要填写日志的人数
	 * @param exceptUser  不需要统计的用户id
	 * @param statisticdept 需要统计的部门
	 * @return
	 */
	public List<SysUser> getNeedUser(String[] exceptUserArray,String  statisticdept) {
		
		return workLoadStatisticDao.getNeededUser(exceptUserArray,statisticdept);
	}

/*	public List<SysUser> getNotWriteDaylogUserList(String[] exceptUserArray,
			List<SysDatadir> deptSysDataList,String  date) {
		
		return workLoadStatisticDao.getNotWriteDayLogUserList(exceptUserArray,deptSysDataList,date);
	}
	*/
	/**
	 * 获取没有填写日志的用户名单
	 * @param exceptUserArray   不用填写日志的用户
	 * @param deptSysDataList   需要统计的部门
	 * @param date
	 * @return
	 */
	public List<SysUser> getNotWriteDaylogUserNameList(String[] exceptUserArray,
			String  deptSysDataList,String  date) {
		return workLoadStatisticDao.getDontWriteDayLogUserNameList(exceptUserArray, deptSysDataList, date);
	}
	
	/**
	 * 获取昨天未确认的项目及项目经理信息，除“其他项目”外
	 * @param start  开始时间
	 * @param end    结束时间
	 * @return
	 */
	public List<Project> getNoConfirmPrjname(String start,String  end){
		return workLoadStatisticDao.getNoConfirmPrjname(start ,end);
	}
	
	/**
	 * 获取昨天未确认的部门经理信息
	 * @param start 开始时间
	 * @param endy  结束时间
	 * @return
	 */
	public List<SysUser> getNoConfirmDept(String start,String  end){
		return workLoadStatisticDao.getNoConfirmDept(start,end);
	}
	
	

}
