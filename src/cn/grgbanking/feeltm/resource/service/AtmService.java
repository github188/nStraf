package cn.grgbanking.feeltm.resource.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.domain.testsys.Atm;
import cn.grgbanking.feeltm.domain.testsys.DeviceConfig;
import cn.grgbanking.feeltm.domain.testsys.MediaUpdateLog;
import cn.grgbanking.feeltm.domain.testsys.MediaVersion;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.resource.dao.AtmDao;
import cn.grgbanking.feeltm.resource.dao.DeviceConfigDao;
import cn.grgbanking.feeltm.resource.dao.MediaVersionDao;
import cn.grgbanking.feeltm.resource.dao.MediaVersionLogDao;
import cn.grgbanking.framework.service.BaseService;
import cn.grgbanking.framework.util.Page;

@Service("atmService")
@Transactional
public class AtmService extends BaseService{
	@Autowired
	private AtmDao atmDao;
	@Autowired
	private DeviceConfigDao  deviceConfigDao;
	@Autowired
	private MediaVersionDao  mediaVersionDao;
	@Autowired
	private MediaVersionLogDao mediaLogDao;
	
	
	public boolean addAtm(Atm atm){
		boolean flag=false;
		try{
			atmDao.addObject(atm);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	public boolean addDeviceConfig(DeviceConfig config){
		boolean flag=false;
		try{
			deviceConfigDao.addObject(config);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	public boolean addMediaVersion(MediaVersion version){
		boolean flag=false;
		try{
			mediaVersionDao.addObject(version);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	public boolean delete(String[] ids){
		boolean success=true;
		try{
			atmDao.remove(ids);
			deviceConfigDao.remove(ids);
			mediaVersionDao.remove(ids);
		}catch(Exception e){
			success=false;
		}
		return success;
	}
	
	public boolean delete(Atm atm){
		boolean flag = false;
		try {
			atmDao.removeObject(Atm.class, atm.getId());
			deviceConfigDao.removeObject(DeviceConfig.class, atm.getId());
			mediaVersionDao.removeObject(MediaVersion.class, atm.getId());
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	public boolean updateAtm(Atm atm){
		boolean flag=false;
		try{
			atmDao.updateObject(atm);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	public boolean updateDeviceConfig(DeviceConfig config){
		boolean flag=false;
		try{
			deviceConfigDao.updateObject(config);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	public boolean updateMediaVersion(MediaVersion version){
		boolean flag=false;
		try{
			mediaVersionDao.updateObject(version);
			flag=true;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			SysLog.error(e);
		}
		return flag;
	}
	
	
	@Transactional(readOnly = true)
	public Atm getAtmById(String id){
		return (Atm)atmDao.getObject(Atm.class, id);
	}
	
	@Transactional(readOnly = true)
	public DeviceConfig getDeviceConfigById(String id){
		DeviceConfig config=null;
		try{
		config=(DeviceConfig)deviceConfigDao.getObject(DeviceConfig.class, id);
		}catch(Exception e){
			e.printStackTrace();
		}
		return config;
	}
	
	@Transactional(readOnly = true)
	public MediaVersion getMediaVersionById(String id){
		MediaVersion version=null;
		
		try{
			version=(MediaVersion)mediaVersionDao.getObject(MediaVersion.class, id);
		}catch(Exception e){
			e.printStackTrace();
		}
		return version;
	}
	
	public void addLogs(MediaVersion mediaVersion,String updateMan){
		Map<String , String> media=new HashMap<String,String>();
		media.put(mediaVersion.getBackDoor(), mediaVersion.getBackDoorVersion());
		media.put(mediaVersion.getCore(), mediaVersion.getCoreVersion());
		media.put(mediaVersion.getDoor(), mediaVersion.getDoorVersion());
		media.put(mediaVersion.getKey(), mediaVersion.getKeyVersion());
		media.put(mediaVersion.getOther(), mediaVersion.getOtherVersion());
		media.put(mediaVersion.getProofPrinter(), mediaVersion.getProofPrinterVersion());
		media.put(mediaVersion.getReadCard(), mediaVersion.getReadCardVersion());
		media.put(mediaVersion.getRunningPrinter(), mediaVersion.getRunningPrinterVersion());
		for(String key:media.keySet()){  //key为介质名a1,a2,b1,b2,c1,c2等，value为相应的版本
			String value=media.get(key);
			MediaUpdateLog log=new MediaUpdateLog();
			log.setMedia(key);
			log.setUpdate_version(value);
			log.setUpdate_date(new Date());
			log.setUpdate_man(updateMan);
			mediaLogDao.addLog(log);
		}
	}
	
	@Transactional(readOnly = true)
	public List<MediaUpdateLog> getLogs(String media){
		List<MediaUpdateLog> list=mediaLogDao.getLogs(media);
		if(media.equals("h4")){   //hcm
			for(MediaUpdateLog log:list){
				log.setUpdate_version(log.getUpdate_version().replace("@@", "<br/>"));
				System.out.println(log.getUpdate_version());
			}
		}
		return list;
	}
	
	@Transactional(readOnly = true)
	public Page getPage(String type,String seriaNum, int pageNum, int pageSize) {
		return atmDao.getPage(type, seriaNum, pageNum, pageSize);
	}

	@Transactional(readOnly = true)
	public boolean checkExist(String deviceNo) {
		return atmDao.check(deviceNo);
	}
}
