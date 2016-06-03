package cn.grgbanking.feeltm.config;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Properties;

/**
 * <p>Description: ϵͳ�����ļ���ȡ</p>
 * <p>�����ļ����������·����Ŀ¼���õģ���������·��
 * <p>�������WEB-INF/class/����
 * <p>ȱʡ�����ļ�ΪviewConfig.properties,���ڵ�ǰ��·��<p> 
 * @author ljming
 *
 */
import cn.grgbanking.feeltm.log.SysLog;

@SuppressWarnings("unchecked")
public class JdbcConnect {
	public final static String USER_DIR_PATH = System.getProperty("user.dir") + File.separator;
	public static String ROOT_PATH = USER_DIR_PATH;
	public static final String DEFAULT_CONFIGNAME = "jdbc.properties";
	private static Hashtable property = new Hashtable();
	private static Properties defPro = loadProperty(DEFAULT_CONFIGNAME);
	static {
		if (defPro == null) {
			SysLog.error("Can't Load " + DEFAULT_CONFIGNAME);
			throw new RuntimeException("Can't Load " + DEFAULT_CONFIGNAME);
		}
	}

	// ------------------------------------------------------------------------------
	/**
	 * @param key
	 *            String key
	 * @return String value
	 */
	public static String getProperty(String key) {
		String str = defPro.getProperty(key);
		if (str != null) {
			return str.trim();
		} else {
			SysLog.error("Can't  Find Value By Key[" + key + "]");
			return null;
		}
	}

	// ------------------------------------------------------------------------------
	/**
	 * @param filename
	 *            String �ļ����ļ������configĿ¼��
	 * @param key
	 *            String keyֵ
	 * @return String value
	 */
	public static String getProperty(String filename, String key) {
		Properties filePro = (Properties) property.get(filename);
		if (filePro == null) {
			filePro = loadProperty(filename);
			if (filePro == null)
				return null;
		}

		String str = filePro.getProperty(key);
		if (str != null) {
			return str.trim();
		} else {
			SysLog.error("Can't Find Value By Key[" + key + "]");
			return null;
		}
	}

	// ------------------------------------------------------------------------------
	/**
	 * @param filename
	 *            String �ļ���
	 * @return
	 * */
	private static Properties loadProperty(String filename) {
		try {
			Properties filePro = new Properties();
			filePro.load(new JdbcConnect().getClass().getClassLoader()
					.getResourceAsStream(filename));
			property.put(filename, filePro);
			return filePro;
		} catch (Exception ex) {
			SysLog.error(ex.getMessage());
			SysLog.error("Can't Read File(" + filename + ") !");
			return null;
		}
	}
	
	public static Connection getConnection() throws ClassNotFoundException,SQLException {
		Connection conn = null;
		String url = JdbcConnect.getProperty("jdbc.url");
		Class.forName(JdbcConnect.getProperty("jdbc.forname"));
		conn = DriverManager.getConnection(url,
				JdbcConnect.getProperty("jdbc.username"),
				JdbcConnect.getProperty("jdbc.password"));
		return conn;
	}
}