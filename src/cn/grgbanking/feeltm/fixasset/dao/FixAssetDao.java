package cn.grgbanking.feeltm.fixasset.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.config.UserDataRoleConfig;
import cn.grgbanking.feeltm.fixasset.domain.FixAsset;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.framework.dao.BaseDao;
import cn.grgbanking.framework.dao.hibernate3.BaseHibernateTemplate;
import cn.grgbanking.framework.util.Page;

@Repository("fixAssetDao")
public class FixAssetDao extends BaseDao<FixAsset>{
	public Page getPage(int pageNum,int pageSize,String type,String no,String name,String status,String inman,String useman,UserModel userModel){
		String hql = " from FixAsset where 1=1";
		if(UserDataRoleConfig.viewAllDataByUserid(userModel.getUserid(), "fixasset.userdata.all")){//查看全部数据
				
		}else if(UserDataRoleConfig.viewAllDataByUserid(userModel.getUserid(), "fixasset.userdata.dept")){
			hql += " and usemanid in (select u.userid from SysUser u where u.deptName='"+userModel.getDeptName()+"')";
		}else if(UserDataRoleConfig.viewAllDataByUserid(userModel.getUserid(), "fixasset.userdata.project")){
			String grp = " select g.userid from UserProject g where g.project in (select p.project from UserProject p where p.userid ='"+userModel.getUserid()+"')";
			hql += " and usemanid in (select s.userid from SysUser s where s.userid in ("+grp+"))";
		}else{
			hql +=" and usemanid='"+userModel.getUserid()+"'";
		}
		//查询条件
		if(!"".equals(type) && type!=null){
			hql+=" and type="+type;
		}
		if(!"".equals(no) && no!=null){
			hql+=" and no like '%"+no+"%'";
		}
		if(!"".equals(name) && name!=null){
			hql+=" and name like '%"+name+"%'";
		}
		if(!"".equals(status) && status!=null){
			hql+=" and status="+status;
		}
		if(!"".equals(inman) && inman!=null){
			hql+=" and inman like '%"+inman+"%'";
		}
		if(!"".equals(useman) && useman!=null){
			hql+=" and useman like '%"+useman+"%'";
		}
		hql+=" order by id desc";
		
		List list = getHibernateTemplate().find(hql);
		Page page = ((BaseHibernateTemplate)getHibernateTemplate()).getPageByList(list, pageNum, pageSize);
		return page;
	}
	public boolean remove(String[] ids){
		List<FixAsset> delList = new ArrayList<FixAsset>(); 
		for(int i=0;i<ids.length;i++){
			delList.add(new FixAsset(ids[i]));
		}
		try{
			this.getHibernateTemplate().deleteAll(delList);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			SysLog.error(e);
			return false;
		}
	}
	
	/**
	 * 判断资产编号是否重复
	 * @param no
	 * @param id
	 * @return
	 */
	public int checkFixAssetNo(String no,String id){
		String hql = "select count(*) from FixAsset where 1=1 and no='"+no+"'";
		if(!"".equals(id)){
			hql+=" and id!='"+id+"'";
		}
		List list = this.getHibernateTemplate().find(hql);
		if (list.isEmpty())
			return 0;
		if (list.get(0) == null)
			return 0;
		int i = Integer.parseInt(list.get(0).toString());
		return i;
	}
}
