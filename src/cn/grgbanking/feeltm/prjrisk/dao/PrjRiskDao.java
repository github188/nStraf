package cn.grgbanking.feeltm.prjrisk.dao;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.config.UserRoleConfig;
import cn.grgbanking.feeltm.domain.testsys.PrjRisk;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.framework.dao.BaseDao;
import cn.grgbanking.framework.dao.hibernate3.BaseHibernateTemplate;
import cn.grgbanking.framework.util.Page;

@Repository("prjRiskDao")
public class PrjRiskDao extends BaseDao<PrjRisk> {

	public Page getPage(String prjname,String summary, String type,String status,String urgent,String pond, Date startDate,Date endDate,
			int pageNum, int pageSize,String createman, UserModel userModel) {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		String hql = " from PrjRisk prj where 1=1 ";
		if (prjname != null && !prjname.equals("")) {
			hql += " and prj.prjname like '%" + prjname.trim() + "%' ";
		}
		if (type != null && !type.equals("")) {
			hql += " and prj.type like '%" + type.trim() + "%' ";
		}
		if (status != null && !status.equals("")) {
			hql += " and prj.status like '%" + status.trim() + "%' ";
		}
		if (summary != null && !summary.equals("")) {
			hql += " and prj.summary like '%" + summary.trim() + "%' ";
		}

		if (urgent != null && !urgent.equals("")) {
			hql += " and prj.urgent like '%" + urgent.trim() + "%' ";
		}
		if (pond != null && !pond.equals("")) {
			hql += " and prj.pond like '%" + pond.trim() + "%' ";
		}
		if(startDate!=null){
			hql += " and to_date('"+ sdf.format(startDate)+"','yyyy-MM-dd')<=prj.createdate";
		}
		if(endDate!=null){
			hql += " and to_date('"+ sdf.format(endDate)+"','yyyy-MM-dd')>=prj.createdate";
		}
		if(createman!=null && !"".equals(createman)){
			hql += " and createman like '%"+createman.trim()+"%'";
		}
		//如果不是行政人员，只能看到自己组别的数据
		if(!UserRoleConfig.upDeptManager(userModel)){
			//如果处理者是其他组别的
			//hql += " and (handleman='"+userModel.getUsername()+"'";
			//非部门经理以上查看自己项目的数据
			//部门经理以下 查看自己项目信息
			String grp = " select g.projectname from UserProject g where g.userid ='"+userModel.getUserid()+"')";
			//项目经理
			hql += " and prjname in ("+grp+")";
		}
		hql += " order by rno desc";
		List list = getHibernateTemplate().find(hql);
		Page page=((BaseHibernateTemplate) getHibernateTemplate()).getPage(hql,pageNum,pageSize);
		
		return page;
	}
	
	public  String  getNextNo(){
		String str=null;
		List list=this.getHibernateTemplate().find("select max(p.rno) from PrjRisk p");
		if(list!=null&&list.size()>=0){
		      String p=(String)list.get(0);
		      if(p!=null){
			      if(p.contains("R")){
			    	  p=p.substring(1);
			    	  long d=Long.parseLong(p)+1;
			    	  DecimalFormat format=new DecimalFormat("R00000");
			  		  str=format.format(d);
			      }
		      }
		      else
		      {
					str = "R00001";
		      }
		}
		return str;
		
	}
	
	
	
	
}
