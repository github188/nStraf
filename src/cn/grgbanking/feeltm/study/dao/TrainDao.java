package cn.grgbanking.feeltm.study.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.domain.testsys.TrainingRecord;
import cn.grgbanking.framework.dao.BaseDao;
import cn.grgbanking.framework.dao.hibernate3.BaseHibernateTemplate;
import cn.grgbanking.framework.util.Page;


@Repository("trainDao")
public class TrainDao extends BaseDao<TrainingRecord>{
	
	
	public List<String> getAllStudents(){
		List<String> list=null;
		String hql="select username from SysUser where groupName is not null and level != 3  and lower(trim(username)) not in('汤飞','开发员') order by groupName desc,level asc";
		System.out.println(hql);
		list=this.getHibernateTemplate().find(hql);
		return list;
	}
	
	public Page getPage(String courseName,String teacher,String category, String student,String start,String end,int pageNum, int pageSize) {
		String hql = " from TrainingRecord  where 1=1 ";
		if (courseName != null && !courseName.equals("")) {
			hql += " and courseName like '%" + courseName.trim() + "%' ";
		}
		if (teacher != null && !teacher.equals("")) {
			hql += " and teacher like '%" + teacher.trim() + "%' ";
		}
		if (category != null && !category.equals("")) {
			hql += " and category = '"+category+"'";
		}
		if (student != null && !student.equals("")) {//student的示例为  熊磊,史国辉,冯天桂
			String[] stu=student.split(",");
			for(String ss:stu){
				hql += " and student like '%" + ss.trim() + "%' ";
			}
		}
		
		if (start != null && !start.equals("")) {
			hql += " and trainDate >= to_date('" + start.trim()
					+ "','yyyy-MM-dd') ";
		}
		if (end != null && !end.equals("")) {
			hql += " and trainDate <= to_date('" + end.trim()
					+ "','yyyy-MM-dd') ";
		}
		
		hql += " order by trainDate desc";
		System.out.println(hql);
		List list = getHibernateTemplate().find(hql);
		List<TrainingRecord> trs=list;
		for(TrainingRecord rs:trs){
			rs.setStart(rs.getStart().substring(0,5));
			rs.setEnd(rs.getEnd().substring(0,5));
		}
		Page page=((BaseHibernateTemplate) getHibernateTemplate()).getPage(hql,pageNum,pageSize);
		
		return page;
	}
	
	/**
	 * 月参加培训人信息
	 * @param yyyyMM 当前日期的上一月
	 * @return
	 */
	public List<TrainingRecord> getTrainInfo(String yyyyMM){
		StringBuilder sbdHql = new StringBuilder();
		sbdHql.append(" FROM TrainingRecord t ");
		sbdHql.append(" WHERE TO_CHAR(t.trainDate, 'yyyyMM') = ? ");
		List<TrainingRecord> result = this.getHibernateTemplate().find(sbdHql.toString(),new Object[]{yyyyMM});
		if (result != null && result.size() > 0) {
			return result;
		} else {
			return new ArrayList<TrainingRecord>();
		}
	}
}
