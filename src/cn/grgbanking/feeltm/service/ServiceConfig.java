package cn.grgbanking.feeltm.service;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
import org.apache.commons.lang.NumberUtils;

import cn.grgbanking.feeltm.config.Configure;

@SuppressWarnings("deprecation")
public interface ServiceConfig {
	// ------------------------------------------------------------------------------
	/**
	 * ����������·��
	 */
	public static final String SOAP_SERVER_IP = Configure
			.getProperty("soapServer.url");
	/**
	 * ���ýӿ�ˢ�»���ʱ����
	 */
	public static final int SOAP_REFRESH_INTERVAL = NumberUtils.stringToInt(
			Configure.getProperty("soap.refresh.interval"), 10000);
	/**
	 * �豸״̬��ؽӿ������ļ�URL
	 */
	public static final String DEVICE_WSDL_URL = SOAP_SERVER_IP
			+ Configure.getProperty("device.wsdl");
	// ------------------------------------------------------------------------------
	/**
	 * Զ�̿��ƽӿ������ļ�URL
	 */
	public static final String REMOTE_WSDL_URL = SOAP_SERVER_IP
			+ Configure.getProperty("remote.wsdl");
	// ------------------------------------------------------------------------------
	/**
	 * �����ʼ��ӿ������ļ�URL
	 */
	public static final String MAIL_WSDL_URL = SOAP_SERVER_IP
			+ Configure.getProperty("sendmail.wsdl");

	/**
	 * ���Ͷ��Žӿ������ļ�URL
	 */
	public static final String SMS_WSDL_URL = SOAP_SERVER_IP
			+ Configure.getProperty("sendmessage.wsdl");
	// ------------------------------------------------------------------------------
}