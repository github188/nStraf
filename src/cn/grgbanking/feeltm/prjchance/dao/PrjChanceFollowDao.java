package cn.grgbanking.feeltm.prjchance.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.grgbanking.feeltm.domain.PrjChanceFollow;
import cn.grgbanking.framework.dao.BaseDao;

@Repository("prjChanceFollowDao")
public class PrjChanceFollowDao  extends BaseDao<PrjChanceFollow> {

	public List<String> findLastEmailStage(String lastEmailtimestr) {
		String hql=" select p.prjStage from PrjChance p  where p.lastSendEmailTime=to_date('"+lastEmailtimestr+"','yyyy-MM-dd HH24:mi:ss')";
		System.out.println(hql);
		return getHibernateTemplate().find(hql);
	}

	
	/**
	 * 获取用户需要导出的跟进表信息
	 * @param followMan  跟进人  
	 * @param prjStage  项目的阶段
	 * @param client  客户
	 * @param area  区域
	 * @param province 省市
	 * @param clientManager  客户经理
	 * @param clientType  客户类型
	 * @param prjResult  项目的结果
	 * @return
	 */
	public List getPrjChanceListByCondition(String followMan,
			String prjStage, String client, String area, String province,
			String clientManager, String clientType,
			String prjResult) {

		String sql="select   C_PRJNAME ,pf.C_FOLLOWMAN,pf.C_FOLLOWCONTENT,TO_CHAR(pf.D_FOLLOWDATE,'yyyy-mm-dd hh24:mi:ss'),pf.C_PRJSTAGE from  OA_PRJCHANCE   pc,OA_PRJCHANCE_FOLLOW  pf  where  pc.C_ID=pf.C_CHANCEID  ";
		if (prjStage != null && !prjStage.equals("")) {
			sql += " and pc.C_PRJSTAGE like '%" + prjStage + "%'";
		}
		if (followMan != null && !followMan.equals("")) {
			sql += " and pc.C_FOLLOWMAN like '%" + followMan + "%'";
		}
		if (client != null && !client.equals("")) {
			sql += " and pc.C_CLIENT like '%" + client + "%'";
		}
		
		if (area != null && !area.equals("")) {
			sql += " and pc.C_AREA like '%" + area + "%'";
		}
		if (province != null && !province.equals("")) {
			sql += " and pc.C_PROVINCE like '%" + province + "%'";
		}
		if (clientManager != null && !clientManager.equals("")) {
			sql += " and pc.C_CLIENTMANAGER like '%" + clientManager + "%'";
		}
		if (clientType != null && !clientType.equals("")) {
			sql += " and pc.C_CLIENTTYPE like '%" + clientType + "%'";
		}
		
		if (prjResult != null && !prjResult.equals("")) {
			sql += " and pc.C_PRJRESULT like '%" + prjResult + "%'";
		}
		
		
		sql += " order by   pf.C_CHANCEID";
		List list = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql).list();
		if(list!=null&&list.size()>0){
			return list;
		}else{
			return null;
		}

	}

}
