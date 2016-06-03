package cn.grgbanking.feeltm.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.taglib.TagUtils;

import cn.grgbanking.feeltm.log.SysLog;

@SuppressWarnings("serial")
public class TmOrgId extends TagSupport {
	protected String beanName;

	private String property;

	protected String scope = null;

	protected Integer length;

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	/**
	 * @return
	 * @throws javax.servlet.jsp.JspException
	 */
	public int doStartTag() throws javax.servlet.jsp.JspException {
		JspWriter writer = pageContext.getOut();
		if (beanName == null && property != null) {
			try {
				writer.print(property);
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
			if (length != null) {
				if (codeValue.toString().length() > length) {
					writer.print("");
				} else {
					writer.print(codeValue.toString());
				}

			} else {
				writer.print(codeValue.toString());
			}

		} catch (IOException ie) {
			SysLog.error(ie);
		}

		return EVAL_BODY_INCLUDE;
	}

	// ------------------------------------------------------------------------------

	/**
	 * @return
	 * @throws javax.servlet.jsp.JspException
	 */
	public int doEndTag() throws javax.servlet.jsp.JspException {
		return EVAL_PAGE;
	}
}
