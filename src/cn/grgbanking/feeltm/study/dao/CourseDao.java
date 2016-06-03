package cn.grgbanking.feeltm.study.dao;

import java.text.DecimalFormat;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.domain.testsys.Course;
import cn.grgbanking.framework.dao.BaseDao;
import cn.grgbanking.framework.dao.hibernate3.BaseHibernateTemplate;
import cn.grgbanking.framework.util.Page;

@Repository("courseDao")
public class CourseDao extends BaseDao<Course>{
	public Page getPage(String id, String courseName,String category, String teacher,int pageNum, int pageSize) {
		String hql = " from Course  where 1=1 ";
		if (id != null && !id.equals("")) {
			hql += " and id like '%" + id.trim() + "%' ";
		}
		if (courseName != null && !courseName.equals("")) {
			hql += " and courseName like '%" + courseName.trim() + "%' ";
		}
		if (category != null && !category.equals("")) {
			hql += " and category = '"+category.trim()+"'";
		}
		if (teacher != null && !teacher.equals("")) {
			hql += " and teacher like '%" + teacher.trim() + "%' ";
		}
		hql += " order by id desc";
		System.out.println(hql);
		List list = getHibernateTemplate().find(hql);
		Page page=((BaseHibernateTemplate) getHibernateTemplate()).getPage(hql,pageNum,pageSize);
		
		return page;
	}
	
	public List<Course> getAllCourses(){
		return this.getHibernateTemplate().loadAll(Course.class);
	}
	
	public  String  getNextNo(){
		String str=null;
		List list=this.getHibernateTemplate().find("select max(id) from Course c");
		System.out.println(list.size());
		if(list!=null&&list.size()>0){
		      String p=(String)list.get(0);
		      if(p!=null&&p.contains("C")){
		    	  p=p.substring(1);
		    	  long d=Long.parseLong(p)+1;
		    	  DecimalFormat format=new DecimalFormat("C0000");
		  		  str=format.format(d);
		      }else{
		    	str="C0001";
		      }
		}
		return str;
		
	}
}
