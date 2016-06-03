package cn.grgbanking.feeltm.myadvice.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.domain.SysUser;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.myadvice.dao.AdviceAndResponseDao;
import cn.grgbanking.feeltm.myadvice.domain.AdviceAndResponse;
import cn.grgbanking.framework.service.BaseService;
import cn.grgbanking.framework.util.Page;
/*
 * Author:ljlian
 * tel:13544347106
 * 
 * */
/**
 * @author ljlian2
 * 2014-12-5
 ×
 */
@Service("adviceAndResponseService")
@Transactional
public class AdviceAndResponseService extends BaseService{
	@Autowired
	private AdviceAndResponseDao adviceAndResponseDao;
	
	//添加数据
	public boolean addAdviceandResponse( AdviceAndResponse adviceandresponse ){
		boolean flag = false;
		try {
				if(adviceandresponse.getAdviceMan()!=null&&adviceandresponse.getContent()!=null){
					adviceAndResponseDao.addObject(adviceandresponse);
					flag = true;
				}
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			SysLog.error(e);
			throw new NullPointerException();
		}
		return flag;
		
	}
	
	//查询所有的数据
	public List<AdviceAndResponse> findAllAdvice( String sql ){
		
		return adviceAndResponseDao.getObjectList(sql);
		
	}
	//根据建议的id查询对应建议的详细信息
	public AdviceAndResponse getAdviseResponseById(String adviceid) {
		return adviceAndResponseDao.findAdviceById(adviceid);
	}
	
	///修改数据
	public boolean updateAdvicecInfo(AdviceAndResponse adviceandresponse) {
		boolean flag = false;
		try {
			adviceAndResponseDao.updateObject(adviceandresponse);
			flag = true;
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			SysLog.error(e);
			throw new NullPointerException();
		}
		return flag;
	}
	
	public boolean deleteAdviceInfo(String Id){
		boolean flag = false;
		try {
			adviceAndResponseDao.removeObject(AdviceAndResponse.class, Id);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	
	/**
	 * @param adviceid 用户的id
	 * @param content  建议内容
	 * @param status  建议被处理的状态
	 * @param tel  联系电话
	 * @param ptime   计划执行的时间
	 * @param reply 
	 * @return
	 */
	public boolean updateAll(String adviceid,String content,String status, String tel, String ptime, String reply,String email) {
		return adviceAndResponseDao.updateAll(adviceid, content,status,tel,ptime,reply, email);
	}

	public Page getPage(int pageNum, int pageSize) {
		String  sql="  from  AdviceAndResponse  a order by a.time desc";
		return adviceAndResponseDao.getPage(sql, pageNum,pageSize);
	}
	
	
	public SysUser findUserByUsername(String username) {
		return adviceAndResponseDao.findUserByUsername(username);
	}
	/**
	 * @param pageNum 
	 * @param pageSize
	 * @param selectusername 要查询的意见人
	 * @param selectStatus   查询的状态
	 * @param selectStarTime   查询的开始时间
	 * @param selectEndTime   查询的结束时间
	 * @return
	 */
	public Page getPage(int pageNum, int pageSize, String selectusername,
			String selectStatus, String selectStarTime, String selectEndTime) {
		
		
		String hql=" FROM   AdviceAndResponse a where 1=1 ";
		
		if(selectusername!=null&&!"".equals(selectusername)){
			hql+=" and a.userId like '%"+selectusername+"%'  ";
		}
		if(!"All".equals(selectStatus)&&selectStatus!=null){
			hql+="  and a.status='"+selectStatus+"'";
		}
		if(selectStarTime!=null &&!"".equals(selectStarTime) && selectEndTime!=null&&!"".equals(selectEndTime)){
			hql+=" and a.time  between  to_date('"+selectStarTime+"','yyyy-MM-dd')  and   to_date('"+selectEndTime+"','yyyy-MM-dd')";
		}
		if(selectStarTime!=null &&!"".equals(selectStarTime) &&"".equals(selectEndTime)){
			hql+=" and a.time  >=to_date('"+selectStarTime+"','yyyy-MM-dd')";
		}
		if(selectEndTime!=null &&!"".equals(selectEndTime) &&"".equals(selectStarTime)){
			hql+=" and a.time  <=to_date('"+selectEndTime+"','yyyy-MM-dd')";
		}
		hql+="   order by a.time desc";
		System.out.println(hql);
		return adviceAndResponseDao.getPage(hql, pageNum,pageSize);

	}
	
	
	
}
