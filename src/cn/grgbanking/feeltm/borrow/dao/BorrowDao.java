package cn.grgbanking.feeltm.borrow.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.borrow.domain.Borrow;
import cn.grgbanking.feeltm.borrow.domain.BorrowDetail;
import cn.grgbanking.feeltm.borrow.domain.BorrowRemind;
import cn.grgbanking.feeltm.config.Configure;
import cn.grgbanking.feeltm.config.UserDataRoleConfig;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.framework.dao.BaseDao;
import cn.grgbanking.framework.dao.hibernate3.BaseHibernateTemplate;
import cn.grgbanking.framework.util.Page;

@Repository("borrowDao")
public class BorrowDao extends BaseDao<Object>{

	/**
	 * 根据查询条件 获取借款信息
	 */
	public Page getPage(Borrow borrow, int pageNum, int pageSize, String borrowTime1, String borrowTime2, UserModel userModel){
		try{
			String hql="from Borrow b where 1=1";
			if(UserDataRoleConfig.viewAllData(userModel,Configure.getProperty("borrow.userdata.all"))){//查看全部数据
				
			}else if(UserDataRoleConfig.viewDeptData(userModel,Configure.getProperty("borrow.userdata.dept"))){//查看本部门数据
				hql += " and b.userid in (select u.userid from SysUser u where u.deptName='"+userModel.getDeptName()+"')";
			}else if(UserDataRoleConfig.viewAllDataByUserid(userModel.getUserid(),"borrow.userdata.project")){//项目经理查看本项目组下的数据
				String grp = " select g.userid from UserProject g where g.project.id in (select p.project.id from UserProject p where p.userid ='"+userModel.getUserid()+"')";
				hql += " and b.userId in (select s.userid from SysUser s where s.userid in ("+grp+"))";
			}else{
				hql +=" and b.userid='"+userModel.getUserid()+"'";
			}
			//查询条件
			if(borrow!=null && StringUtils.isNotBlank(borrow.getGroupname())){
				hql+=" and b.groupname='"+(String)borrow.getGroupname()+"'";
			}
			if(borrow!=null && StringUtils.isNotBlank(borrow.getDetname())){
				hql+=" and b.detname like '%"+(String)borrow.getDetname()+"%'";
			}
			if(borrow!=null && StringUtils.isNotBlank(borrow.getUserman())){
				hql+=" and (b.userid like '%"+(String)borrow.getUserman()+"%' or b.userman like '%"+(String)borrow.getUserman()+"%')";
			}
			if(StringUtils.isNotBlank(borrowTime1)){
				hql += " and b.borrowdate >=to_date('"+borrowTime1+"','yyyy-MM-dd') ";
			}
			if(StringUtils.isNotBlank(borrowTime2)){
				hql += " and b.borrowdate <=to_date('"+borrowTime2+"','yyyy-MM-dd') ";
			}
			if(borrow!=null && StringUtils.isNotBlank(borrow.getStatus())){
				hql+=" and b.status='"+(String)borrow.getStatus()+"'";
			}
			hql+=" order by status,userman,updateDate desc";
			//TODO  根据借款状态进行排序   （还款超时优先）   用数字表示优先级  进行排序
			Page page=((BaseHibernateTemplate) getHibernateTemplate()).getPage(hql,pageNum, pageSize);
			
			return page;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	} 
	
	public Borrow getById(String id){
		
		String hql = "from Borrow b where b.id='"+id+"'";
		List<Borrow> list = this.getHibernateTemplate().find(hql);
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	public List<BorrowDetail> getBorrowDetailByBorrowId(String id) {
		String hql = " from BorrowDetail c where c.borrowid =? order by c.replydate desc";
		Object[] objects = new Object[]{id};
		List<BorrowDetail> details = this.getHibernateTemplate().find(hql, objects);
		return details;
	}
	
	public BorrowRemind getRemindById(){
		String hql = "from BorrowRemind b ";
		List<BorrowRemind> list = this.getHibernateTemplate().find(hql);
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	public List getRepayInfoById(String currendate){
		String hql = "from Borrow b where status='0' and to_char(b.expectedRepaydate,'yyyy-mm-dd')<='"+currendate+"'";
		List<Borrow> list = this.getHibernateTemplate().find(hql);
		return list;
	}
}
