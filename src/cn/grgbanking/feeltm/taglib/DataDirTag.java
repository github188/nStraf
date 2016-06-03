package cn.grgbanking.feeltm.taglib;

import java.io.IOException;
import java.util.Map;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.taglib.TagUtils;

import cn.grgbanking.feeltm.config.BusnDataDir;
import cn.grgbanking.feeltm.config.Configure;
import cn.grgbanking.feeltm.log.SysLog;



/**
 * <p>
 * Title: FEEL View4.0.0
 * </p>
 * <p>
 * Description: ��������ֵ�ҵ����BusnDataDir����bean�����Զ�Ӧ�Ĵ���ֵ
 * </p>
 * path: ����ֵ�·��,���� beanName:JavaBean ���浽request��session��page�е�ID��,����
 * property:��Ҫ�����JavaBean����������� scope:��javaBean����ķ�Χ��Ĭ��Ϊ��null,�ᵱ��page���� ��������:<view:dataDir
 * path="equipState.tml" beanName="event" property="type" scope="page"/>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: grgbanking
 * </p>
 * 
 * @author ljming
 * @version 1.0
 */

@SuppressWarnings({"serial", "unchecked"})
public class DataDirTag extends TagSupport {
	

	protected String beanName;

	private String property;

	protected String scope = null;

	protected String path;
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	/**
	 * @return
	 * @throws javax.servlet.jsp.JspException
	 */
	public int doStartTag() throws javax.servlet.jsp.JspException {
		JspWriter writer = pageContext.getOut();
		if(beanName==null && property!=null){
			try {
				writer.print(parseCode(property));
			} catch (IOException ie) {
				SysLog.error(ie);
			}
			return EVAL_BODY_INCLUDE;
		}
		Object codeValue = TagUtils.getInstance().lookup(pageContext, beanName,
				property, scope);
		if (codeValue == null) {
			return (SKIP_BODY); // Nothing to output
		}

		try {
			writer.print(parseCode(codeValue.toString()));
		} catch (IOException ie) {
			SysLog.error(ie);
		}
		return EVAL_BODY_INCLUDE;
	}

	// ------------------------------------------------------------------------------
	/**
	 * �������ֵ���������
	 * 
	 * @param codeValue
	 * @return
	 */
	private String parseCode(String codeValue) {
		Map dataDirMap = BusnDataDir.getMap(path);
		if (dataDirMap == null)
			return codeValue;

		Object obj = dataDirMap.get(codeValue);
		if (obj != null){
			return  obj.toString();
		}else{
			SysLog.info("DateDir: "+path + " have not velue: " + codeValue +" !");
			return  Configure.getProperty("dataDir.unknown");
		}
	}

	/**
	 * @return
	 * @throws javax.servlet.jsp.JspException
	 */
	public int doEndTag() throws javax.servlet.jsp.JspException {
		return EVAL_PAGE;
	}

}