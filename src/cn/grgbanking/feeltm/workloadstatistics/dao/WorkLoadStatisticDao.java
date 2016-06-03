package cn.grgbanking.feeltm.workloadstatistics.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.domain.SysUser;
import cn.grgbanking.feeltm.project.domain.Project;
import cn.grgbanking.framework.dao.BaseDao;
import cn.grgbanking.framework.dao.hibernate3.BaseHibernateTemplate;

@Repository("workLoadStatisticDao")
public class WorkLoadStatisticDao  extends  BaseDao<SysUser> {

	
	/**
	 * 按部门统计需要 填写日志的人
	 * @param exceptUser   不需要填写日志的用户
	 * @param deptSysDataList   需要统计的部门
	 * @return
	 */
	
	public List<SysUser> getNeededUser(  String[] exceptUserArray,String   dept) {
		String	hql="   from  SysUser u   where u.status!='leave'   and  u.deptName='"+dept+"'";	
		
		if (exceptUserArray != null  &&  exceptUserArray.length>0 ) {
			hql += "   and u.userid not in ( ";
			for (int i = 0; i < exceptUserArray.length; i++) {
				if (i == (exceptUserArray.length - 1)) {
					hql += "'"+exceptUserArray[i]+"'" ;
				} else {
					hql += "'"+exceptUserArray[i]+"',";
				}
			}
			
			hql += " )";
		}
		return  ((BaseHibernateTemplate) getHibernateTemplate()).find(hql);
		
		
	}

	
	/**
	 * 获取没有写日志的名单
	 * @param exceptUserArray   不用统计的用户id
	 * @param deptSysDataList   要统计的部门
	 * @param date      
	 * @return
	 */
	public  List<SysUser>  getDontWriteDayLogUserNameList( String[] exceptUserArray,
			String  dept, String date){
		
		//  没有填写日志   在职状态  入职日期小于等于统计日期
		String hql = "from SysUser u where   u.status!='leave'  and  u.deptName='"+ dept+ "' and u.grgBegindate<= to_date('"+ date+ "','yyyy-MM-dd')"
				+ " and u.userid not in (select d.userId from DayLog d where d.logDate=to_date('"+ date+ "','yyyy-MM-dd'))";
		
		if (exceptUserArray != null  &&  exceptUserArray.length>0 ) {
			hql += "   and u.userid not in ( ";
			for (int i = 0; i < exceptUserArray.length; i++) {
				if (i == (exceptUserArray.length - 1)) {
					hql += "'"+exceptUserArray[i]+"'" ;
				} else {
					hql += "'"+exceptUserArray[i]+"',";
				}
			}
			hql += "  )";
		}

		List<SysUser> list =  ((BaseHibernateTemplate) getHibernateTemplate()).find(hql);
		return list;
	}
	
	
	
	/**
	 * 获取昨天未确认的项目及项目经理信息，除“其他项目”外
	 * @param yesterday
	 * @return
	 */
	public List<Project> getNoConfirmPrjname(String start,String  end){
		String hql = "from Project t ";
		hql+=" where t.name in (select distinct groupName from DayLog t where to_char(logDate,'yyyy-mm-dd')>='"+start+"'";
		hql+=" and to_char(logDate,'yyyy-mm-dd')<='"+end+"' and confirmStatus='0')";
		hql+=" and name!='其他项目'";
		List<Project> list = new ArrayList<Project>();
		list = this.getHibernateTemplate().find(hql);
		return list;
	}
	
	/**
	 * 获取昨天未确认的部门经理信息
	 * @param yesterday
	 * @return
	 */
	public List<SysUser> getNoConfirmDept(String start,String  end ){
		String hql = "from SysUser where userid in (select userid from UsrUsrgrp where grpcode='deptManager')";
		hql+=" and deptName in (select key from SysDatadir where parentid=(select id from SysDatadir where key='department')";
		hql+=" and value in (select distinct detName from DayLog where to_char(logDate,'yyyy-mm-dd')>='"+start+"'";
		hql+=" and to_char(logDate,'yyyy-mm-dd')<='"+end+"' and confirmStatus='0'))";
		List<SysUser> list = new ArrayList<SysUser>();
		list = this.getHibernateTemplate().find(hql);
		return list;
	}

	

}
