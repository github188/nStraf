package cn.grgbanking.feeltm.client.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.client.dao.ClientUploadDao;
import cn.grgbanking.feeltm.client.domain.ClientUpload;
import cn.grgbanking.framework.service.BaseService;
import cn.grgbanking.framework.util.Page;

@Service
@Transactional
public class ClientUploadService extends BaseService{
	
	@Autowired
	private ClientUploadDao dao;
	
	/**
	 * 获取最新版本号
	 * @return
	 * lhyan3
	 * 2014年7月22日
	 */
	public String getNewVersion(){
		String hql = "select c1.version from ClientUpload c1 where c1.versionNum in( select max(c.versionNum) from ClientUpload c)";
		Object object = dao.getNewVersion(hql);
		String newversion = "1.0.0.0";
		if(object!=null && !"".equals(object)){
			newversion = object.toString();
		}
		return newversion;
	}

	/**
	 * 获取列表
	 * @param pageNum
	 * @param pageSize
	 * @param startTime 上传开始时间
	 * @param endTime 上传结束时间
	 * @return
	 * lhyan3
	 * 2014年7月22日
	 */
	public Page getPage(int pageNum, int pageSize, String startTime,
			String endTime) {
		String hql = "from ClientUpload c where 1=1";
		if(startTime!=null && !"".equals(startTime)){
			startTime += " 00:00:00";
			hql += " and c.uploadTime>=to_date('"+startTime+"','yyyy-MM-dd HH24:mi:ss')";
		}
		if(endTime!=null && !"".equals(endTime)){
			endTime += " 23:59:59";
			hql += " and c.uploadTime<=to_date('"+endTime+"','yyyy-MM-dd HH24:mi:ss')";
		}
		hql+=" order by c.version desc";
		return dao.getpage(hql,pageNum,pageSize);
	}

	/**
	 * 保存
	 * @param clientUpload
	 * lhyan3
	 * 2014年7月22日
	 */
	public void save(ClientUpload clientUpload) {
		dao.addObject(clientUpload);
		
	}

	public ClientUpload getObject(String id) {
		return (ClientUpload)dao.getObject(ClientUpload.class, id);
	}

}
