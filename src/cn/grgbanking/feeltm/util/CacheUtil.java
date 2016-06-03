package cn.grgbanking.feeltm.util;

import cn.grgbanking.feeltm.log.SysLog;

import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;



public class CacheUtil {

	private static final GeneralCacheAdministrator admin = new GeneralCacheAdministrator();



public static void putCache(String key, Object value) {
		admin.putInCache(key, value);
	}


	public static Object getFromCache(String key) {
		Object myValue = null;
		try {
			myValue = admin.getFromCache(key, Constants.myRefreshPeriod);
		} catch (NeedsRefreshException e) {
			e.printStackTrace();
			SysLog.error("error in (CacheUtil.java-getFromCache())");
			System.out.println("�������" + key + "�ѳ�ʱ��");
		}
		return myValue;
	}

	public static void removeCacheObject(String key) {
		admin.flushEntry(key);
	}

	
	public static void removeAllCache() {
		admin.flushAll();
	}
	public static void updateCache(String key, Object value) {
		boolean updated=false;
		Object obj;
		try {
			obj=admin.getFromCache(key,Constants.myRefreshPeriod);
		} catch (NeedsRefreshException e) {
			SysLog.error("error in (CacheUtil.java-updateCache())");
			try {
			//
			obj=value;
			admin.putInCache(key, obj);
			updated=true;
		}finally {
	         if (!updated) {
	             admin.cancelUpdate(key);
	         }
		}
		}
		admin.putInCache(key,value);
	}
}

