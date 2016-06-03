package cn.grgbanking.feeltm.prjMonitor.dao;

import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.prjMonitor.domain.DeptColor;
import cn.grgbanking.feeltm.project.domain.Project;
import cn.grgbanking.framework.dao.BaseDao;

@Repository
public class ColorSettingDao extends BaseDao<Project> {


	/**
	 * 获取部门颜色配置
	 */
	public List<DeptColor> getDeptColorList(){
		String hql = "from DeptColor d ";
		return  this.getHibernateTemplate().find(hql);
	}
	/**
	 * 根据部门ID获取部门颜色
	 * @param id 部门ID
	 */
	public String getDeptColorById(String id){
		String hql = "select deptColorVal from DeptColor where 1=1";
		String color = "FFFFFF";
		if(id!=null && !"".equals(id)){
			hql += " and deptId='" + id + "'";
		}else{
			hql += " and 1=2";
		}
		List<String> list =  this.getHibernateTemplate().find(hql);
		for(Iterator<String> it = list.iterator();it.hasNext();){
			color = (String)it.next();//理论上只有一个
		}
		return color;
	}
}
