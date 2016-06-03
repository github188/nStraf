package cn.grgbanking.feeltm.context;

import java.io.File;
import java.util.ArrayList;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import cn.grgbanking.feeltm.config.Configure;

/**
 * ͨ���ļ�ϵͳ��ʽ��ʼ�������ģ����ṩ��̬��������������ã���Bean ���datadirDao =
 * (DataDirDAO) ctx.getBean("dataDirDAO");
 * 
 * 
 */
@SuppressWarnings("unchecked")
public class BaseApplicationContext {
	private static ApplicationContext appContext;

	// ------------------------------------------------------------------------
	public final static ApplicationContext getAppContext() {
		if (appContext == null)
			initAppContext();
		return appContext;
	}

	// ---------------------------------------------------------------------------------
	public final static void initAppContext() {
		String[] xmlPaths = getXMLFilePath();

		appContext = new FileSystemXmlApplicationContext(xmlPaths);

	}

	// ---------------------------------------------------------------------------------
	private final static String[] getXMLFilePath() {
		String basePath = Configure.ROOT_PATH;
		if (Configure.ROOT_PATH.equals(Configure.USER_DIR_PATH)) {

			basePath = basePath + "phbank" + File.separator + "WEB-INF"
					+ File.separator;
		} else {

			basePath = basePath + "WEB-INF" + File.separator;
		}
		ArrayList aryXmlFiles = new ArrayList();
		// ����ȫ��XML�ļ�
		// aryXmlFiles.add(basePath+"dataAccessContext-local.xml");
		// ��������jdbc.properties��·��
		aryXmlFiles.add(basePath + "applicationContext.xml");

		// aryXmlFiles.add(basePath+"applicationContext-hibernate.xml");

		File modDir = new File(basePath + "modules");
		if (modDir.exists()) {
			// ȡ��ģ��Ŀ¼
			File[] aryModFiles = modDir.listFiles();
			for (int i = 0; i < aryModFiles.length; i++) {
				// File xmlFile=new
				// File(aryModFiles[i].getPath()+File.separator+"applicationContext-hibernate.xml");
				// if(xmlFile.exists()){
				// aryXmlFiles.add(xmlFile.getPath());
				// }
			}
		}// if
		String[] strPaths = new String[aryXmlFiles.size()];
		for (int i = 0; i < strPaths.length; i++) {
			strPaths[i] = aryXmlFiles.get(i).toString();
		}
		return strPaths;
	}

	// ----------------------------------------------------------------------
	public static void main(String[] args) {
		String[] xmlPaths = getXMLFilePath();
		for (int i = 0; i < xmlPaths.length; i++) {
			System.out.println(xmlPaths[i]);
		}
	}
	// ---------------------------------------------------------------------------------
}
