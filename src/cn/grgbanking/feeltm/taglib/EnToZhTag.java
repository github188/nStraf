package cn.grgbanking.feeltm.taglib;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;

import cn.grgbanking.feeltm.util.CacheUtil;

@SuppressWarnings({"serial", "unchecked"})
public class EnToZhTag extends TagSupport {

	protected String property;

	protected String name;

	protected String hashMap;

	protected String type;

	public int doEndTag() throws JspException {
		
		try {
			
			JspWriter out = pageContext.getOut();
			HashMap map = null;

			Object bean = pageContext.findAttribute(name);

			if (type.equals("session"))
				map = (HashMap) ((HttpServletRequest) pageContext.getRequest())
						.getSession().getAttribute(hashMap);
			else if (type.equals("oscache"))
				map = (HashMap) CacheUtil.getFromCache(hashMap);
			else
				map = (HashMap) pageContext.getAttribute(hashMap);

			StringBuffer key =new StringBuffer();
			String [] str=StringUtils.split(property,",");
			for (int i=0;i<str.length;i++)
			 key.append(
			 (String) PropertyUtils.getProperty(bean, str[i]));
			
			if (map==null)
				out.print(key.toString());
			else if(map.get(key.toString())==null)
				out.print(key.toString());
			else
				out.print(map.get(key.toString()));

		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}catch (Exception e) {
			throw new JspTagException("IOException" + e.toString());
		}

		return super.doEndTag();
	}

	public int doStartTag() throws JspTagException {

		return EVAL_BODY_INCLUDE;

	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getHashMap() {
		return hashMap;
	}

	public void setHashMap(String hashMap) {
		this.hashMap = hashMap;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}