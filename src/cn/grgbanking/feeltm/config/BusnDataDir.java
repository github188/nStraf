package cn.grgbanking.feeltm.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.datadir.dao.DataDirDao;
import cn.grgbanking.feeltm.domain.SysDatadir;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.menu.dao.MenuInfoDao;
import cn.grgbanking.feeltm.util.CacheUtil;
import cn.grgbanking.feeltm.util.DateUtil;
import cn.grgbanking.framework.service.BaseService;

@SuppressWarnings("unchecked")
@Service
@Transactional
public class BusnDataDir extends BaseService {
	@Autowired
	private DataDirDao dataDirDao;
	@Autowired
	private MenuInfoDao menuInfoDao;

	public static HashMap<Object, Map> mapMgr = new HashMap<Object, Map>(30);
	public static HashMap<String, Map> mapkeyvalue = new HashMap<String, Map>();
	public static HashMap<String, Map> mapkeyObject = new HashMap<String, Map>();
	public static HashMap<String, List<SysDatadir>> mapSysDataDirList=new HashMap<String,List<SysDatadir>>();
	
	public static HashMap orgInfoMap = new HashMap();
	public static HashMap geogAreaMap = new HashMap();
	public static HashMap sysUserMap = new HashMap();
	public static HashMap<String, String> menuMap = new HashMap<String, String>();

	/**
	 * ��ȡ��ݿ⣬�������
	 */
	public void loadData() throws Exception {
		loadDataDir();// ����ֵ�
		loadDataKeyValue();
		//根据key得到这个key对应的子节点信息(子节点key:子节点对象)
		loadDataKeyObject();
		//在初始化mapkeyObject的基础上，按照数据字典的排序，返回排序后的字典列表
		loadObjectListInOrder();
		loadSysUserMap();
		loadMenu();
		loadOrgInfoMap();
	}

	/**
	 * lhy 2014-4-25
	 * 获取key value的数据字典
	 * @throws Exception
	 */
	public void loadDataKeyValue() throws Exception{
		try {

			List dataDirList = dataDirDao.queryChildList(DataDirDao.TOP_PARENT_ID);
			Iterator itr = dataDirList.iterator();
			while (itr.hasNext()) {
				SysDatadir dir = (SysDatadir) itr.next();
				List dataDirChildList = dataDirDao.queryChildList(dir.getId());
				Iterator itrChild = dataDirChildList.iterator();
				while (itrChild.hasNext()) {
					SysDatadir dirChild = (SysDatadir) itrChild.next();
					String dirChildKey = dir.getKey() + "." + dirChild.getKey();
					mapkeyvalue.put(dirChildKey,
							dataDirDao.getChildValueMap2(dirChildKey));
				}
			}

		} catch (Exception ee) {
			SysLog.error(ee);
			DateUtil.showLog("BusnDataDir 加载数据字典失败");
			throw ee;
		} finally {
		}
	}
	
	public void loadDataKeyObject() throws Exception{
		try {

			List dataDirList = dataDirDao.queryChildList(DataDirDao.TOP_PARENT_ID);
			Iterator itr = dataDirList.iterator();
			while (itr.hasNext()) {
				SysDatadir dir = (SysDatadir) itr.next();
				List dataDirChildList = dataDirDao.queryChildList(dir.getId());
				Iterator itrChild = dataDirChildList.iterator();
				while (itrChild.hasNext()) {
					SysDatadir dirChild = (SysDatadir) itrChild.next();
					String dirChildKey = dir.getKey() + "." + dirChild.getKey();
					mapkeyObject.put(dirChildKey,dataDirDao.getChildObjectMap(dirChildKey));
				}
			}

		} catch (Exception ee) {
			throw ee;
		} finally {
		}
	}
	
	/**对已经初始化的mapkeyObject按照字典排序进行排序，返回排序后的列表
	 * wtjiao 2014年9月9日 下午1:57:56
	 * @throws Exception
	 */
	public void loadObjectListInOrder() throws Exception{
		try {
			Iterator<String> ite=mapkeyObject.keySet().iterator();
			while (ite.hasNext()) {
				String key = (String) ite.next();
				Map value=(Map)mapkeyObject.get(key);
				Iterator<String> valueKeyIte=value.keySet().iterator();
				List<SysDatadir> tmp=new ArrayList<SysDatadir>();
				while(valueKeyIte.hasNext()){
					String key2=valueKeyIte.next();
					SysDatadir value2=(SysDatadir)value.get(key2);
					tmp.add(value2);
				}
				Collections.sort(tmp,new Comparator<SysDatadir>() {
					@Override
					public int compare(SysDatadir o1, SysDatadir o2) {
						SysDatadir dir1=(SysDatadir)o1;
						SysDatadir dir2=(SysDatadir)o2;
						return dir1.getOrder()-dir2.getOrder();
					}
				});
				mapSysDataDirList.put(key, tmp);
			}
			

		} catch (Exception ee) {
			throw ee;
		} finally {
		}
	}

	public void loadSysUserMap() {

		try {

			sysUserMap = dataDirDao.getSysUserMap();
			CacheUtil.putCache("sysUserMap", sysUserMap);
		} catch (Exception ee) {
			SysLog.error(ee);
			SysLog.info("BusnDataDir loading SysUserMap failed!");
		} finally {
			// dao = null;
		}
	}

	/**
	 * 加载机构map
	 */
	public void loadOrgInfoMap() {
		try {
			orgInfoMap = dataDirDao.getOrgInfoMap();
			CacheUtil.putCache("orgInfoMap", orgInfoMap);
			
			DateUtil.showLog("BusnDataDir 加载数据字典成功");
		} catch (Exception ee) {
			SysLog.error(ee);
			SysLog.info("BusnDataDir loading OrgInfoMap failed!");
		} finally {
		}
	}
	
	/**根据三级目录，直接过去字典中的值
	 * @param detailDir
	 * @return
	 */
	public static String getObjectDetail(String detailDir){
		String parentDir=detailDir.substring(0,detailDir.lastIndexOf("."));
		String lastDir=detailDir.substring(detailDir.lastIndexOf(".")+1,detailDir.length());
		List<SysDatadir> deptSysDataList=BusnDataDir.getObjectListInOrder(parentDir);
		if(deptSysDataList!=null && deptSysDataList.size()>=0){
			for(SysDatadir dir:deptSysDataList){
				if(lastDir.equals(dir.getKey())){//
					return dir.getValue();
				}
			}
		}
		return "";
	}
	

	/*
	 * 加载菜单转换map
	 */
	public void loadMenu() {

		try {

			menuMap = menuInfoDao.getMenuMap();
			CacheUtil.putCache("menuMap", menuMap);
		} catch (Exception ee) {
			SysLog.error(ee);
			SysLog.info("BusnDataDir loading menuMap false!");
		} finally {
			// menuInfoDao = null;
		}
	}

	public void loadDataDir() throws Exception {

		try {

			List dataDirList = dataDirDao
					.queryChildList(DataDirDao.TOP_PARENT_ID);
			Iterator itr = dataDirList.iterator();
			while (itr.hasNext()) {
				SysDatadir dir = (SysDatadir) itr.next();
				List dataDirChildList = dataDirDao.queryChildList(dir.getId());
				Iterator itrChild = dataDirChildList.iterator();
				while (itrChild.hasNext()) {
					SysDatadir dirChild = (SysDatadir) itrChild.next();
					String dirChildKey = dir.getKey() + "." + dirChild.getKey();
					mapMgr.put(dirChildKey,
							dataDirDao.getChildValueMap(dirChildKey));

				}
			}

		} catch (Exception ee) {
			SysLog.error(ee);
			SysLog.info("BusnDataDir loading DataDir false!");
			throw ee;
		} finally {
			// dao = null;
		}
	}

	/**
	 * Ϊ�˼�����̬�������loadData����������
	 */
	public void waken() throws Exception {
		loadData();
	}

	public static Map getMap(String path) {
		return (Map) mapMgr.get(path);
	}
	
	public static Map getMapKeyValue(String path){
		return (Map) mapkeyvalue.get(path);
	}
	
	/** 获取某路径下的key,SysDatadir对象组成的map
	 * wtjiao 2014年9月9日 下午1:47:01
	 * @param path
	 * @return
	 */
	public static Map getMapKeyObject(String path){
		return (Map) mapkeyObject.get(path);
	}
	
	/**按照数据字典的排序获取某个目录下面的字典列表
	 * wtjiao 2014年9月9日 下午1:45:33
	 * @param string
	 * @return
	 */
	public static List<SysDatadir> getObjectListInOrder(String path) {
		return mapSysDataDirList.get(path);
	}

	public static String getValue(Map map,String key) {
		String str = (String) map.get(key);
		if(str==null){
			Iterator<String> ite=map.keySet().iterator();
			Map map2=new HashMap();
			while(ite.hasNext()){
				String oriKey=ite.next();
				String trimKey=oriKey.trim();
				String trimValue=(String)map.get(oriKey);
				map2.put(trimKey, trimValue);
			}
			str=(String)map2.get(key);
			if(str==null){
				str = "";
			}
		}
		return str;
	}


}