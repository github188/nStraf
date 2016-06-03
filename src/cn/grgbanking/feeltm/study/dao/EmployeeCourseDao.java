package cn.grgbanking.feeltm.study.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.domain.testsys.EmployeeCourse;
import cn.grgbanking.framework.dao.BaseDao;
import cn.grgbanking.framework.dao.hibernate3.BaseHibernateTemplate;
import cn.grgbanking.framework.util.Page;


@Repository("ecDao")
public class EmployeeCourseDao extends BaseDao<EmployeeCourse>{
	/**
	 * 课程分配的功能
	 * @param uid
	 */
	public void assignCourses(List<EmployeeCourse> ec){
		String employeeCourseId=ec.get(0).getNewEmployeeId();
		String hql="delete from EmployeeCourse ec where ec.newEmployeeId='"+employeeCourseId+"'";
		try{
			this.getHibernateTemplate().bulkUpdate(hql);
		}catch(Exception e){
			e.printStackTrace();
		}
		this.getHibernateTemplate().saveOrUpdateAll(ec);
	}
	
	/**
	 * 查找新员工对应的学习课程
	 * @param 新员工的uid
	 * @return
	 */
	public List<EmployeeCourse> getCoursesByUid(String uid){
		String queryString="from EmployeeCourse ec where ec.newEmployeeId=? order by ec.cid";
		List<EmployeeCourse> ec=this.getHibernateTemplate().find(queryString,uid);
		for(EmployeeCourse e:ec){
			e.setCid(e.getCid()+":"+e.getCourseName());
		}
		return ec;
	}
	
	/**
	 * 根据新员工ID的出已分配课程Id
	 * @param 新员工的uid
	 * @return
	 */
	public List<EmployeeCourse> getCoursesIdByUid(String uid){
		String queryString="from EmployeeCourse ec where ec.newEmployeeId=? order by ec.cid";
		List<EmployeeCourse> ec=this.getHibernateTemplate().find(queryString,uid);
		for(EmployeeCourse e:ec){
			e.setId(e.getId());
		}
		return ec;
	}
	
	
	public Page getPage(String courseName, String uname,String category,String start,String end,int pageNum, int pageSize) {
		String hql = " from EmployeeCourse  where 1=1 ";
		if (courseName != null && !courseName.equals("")) {
			hql += " and courseName like '%" + courseName.trim() + "%' ";
		}
		if (uname != null && !uname.equals("")) {
			hql += " and uname like '%" + uname.trim() + "%' ";
		}
		if (start != null && !start.equals("")) {
			hql += " and planFinishDate >= to_date('" + start.trim()
					+ "','yyyy-MM-dd') ";
		}
		if (end != null && !end.equals("")) {
			hql += " and planFinishDate <= to_date('" + end.trim()
					+ "','yyyy-MM-dd') ";
		}
		if (category != null && !category.equals("")) {
			hql += " and category = '"+category.trim()+"'";
		}
		
		hql += " order by newEmployeeId desc,courseName desc";
		System.out.println(hql);
		List list = getHibernateTemplate().find(hql);
		Page page=((BaseHibernateTemplate) getHibernateTemplate()).getPage(hql,pageNum,pageSize);
		
		return page;
	}
	
	
	
	
	
	
	
	
	
	
}
