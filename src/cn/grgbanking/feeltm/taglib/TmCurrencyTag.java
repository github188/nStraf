package cn.grgbanking.feeltm.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspWriter;
import org.apache.struts.taglib.TagUtils;

import cn.grgbanking.feeltm.util.Currency;

@SuppressWarnings("serial")
public class TmCurrencyTag extends TagSupport {

	private String id;

	private String name;

	private String beanName;

	private String property;

	protected String scope = null;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
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
	 * <p>
	 * ��ǿ�ʼʱ�Զ�ִ�еĺ��� ������Ҫ�Ǵ�scopeָ��������������collectionId�����ݼ���
	 * 
	 * @return int
	 * @throws JspException
	 */
	public int doStartTag() throws JspException {
		Object codeValue = TagUtils.getInstance().lookup(pageContext, beanName,
				property, scope);
		if (codeValue == null) {
			return (SKIP_BODY); // Nothing to output
		}

		JspWriter writer = pageContext.getOut();
		String temp = codeValue.toString();
		try {
			writer.print(Currency.format(temp));
		} catch (IOException ie) {
			throw new JspException(ie.toString());
		}

		return EVAL_BODY_INCLUDE;
	}

	/**
	 * ��ǽ���ʱ����ؼ�html
	 * 
	 * @return
	 * @throws JspTagException
	 */
	public int doEndTag() throws JspTagException {

		return EVAL_PAGE;
	}
}
