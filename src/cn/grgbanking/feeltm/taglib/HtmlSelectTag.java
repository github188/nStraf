package cn.grgbanking.feeltm.taglib;

import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import cn.grgbanking.feeltm.util.CacheUtil;
import cn.grgbanking.feeltm.util.HtmlUtil;

@SuppressWarnings({"serial", "unchecked"})
public class HtmlSelectTag extends TagSupport {

	protected String hashMap = null;

	public String getHashMap() {
		return hashMap;
	}

	public void setHashMap(String hashMap) {
		this.hashMap = hashMap;
	}

	protected String name = null;

	protected String selectKey = null;
	
	protected String jsEvent=null;
	
	protected String jsMothed=null;

	public int doStartTag() throws JspException {

		try {
			JspWriter out = pageContext.getOut();
//			System.out.println("cache="+(HashMap)CacheUtil.getFromCache(hashMap));
//			HashMap map = (HashMap) ((HttpServletRequest) pageContext
//					.getRequest()).getSession().getAttribute(hashMap);
			HashMap map=(HashMap)CacheUtil.getFromCache(hashMap);
			//��ѡ�е������б��ֵ
			String skey=(String)((HttpServletRequest) pageContext
					.getRequest()).getSession().getAttribute(selectKey);
			StringBuffer sbf = new StringBuffer();
			Iterator itr = map.keySet().iterator();
			String key;
			while (itr.hasNext()) {
				key = itr.next().toString();

					sbf.append("<option value=\"").append(key);
				sbf.append("\"").append(
						key.equals(skey) ? "selected >" : ">");
				sbf.append(HtmlUtil.htmlEncode(map.get(key))).append(
						"</option>\r\n");
			}
			out.print("<select name=\"" + name + "\" "+jsEvent+"=\""+jsMothed+"\">");
			out.print("<option value=\"");
			out.print("\">======");
			out.print("</option>\r\n");
			out.print(sbf.toString());
			out.print("</select>");

		} catch (Exception e) {
			throw new JspTagException("IOException" + e.toString());
		}
		return super.doStartTag();

	}

	public int doEndTag() throws JspException {
		return EVAL_BODY_INCLUDE;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSelectKey() {
		return selectKey;
	}

	public void setSelectKey(String selectKey) {
		this.selectKey = selectKey;
	}


	public String getJsMothed() {
		return jsMothed;
	}

	public void setJsMothed(String jsMothed) {
		this.jsMothed = jsMothed;
	}

	public String getJsEvent() {
		return jsEvent;
	}

	public void setJsEvent(String jsEvent) {
		this.jsEvent = jsEvent;
	}

}
