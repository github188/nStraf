package cn.grgbanking.feeltm.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.Globals;
import org.apache.struts.util.MessageResources;

import cn.grgbanking.framework.util.Page;


@SuppressWarnings("serial")
public class PageiframeActionTag extends TagSupport {

	protected String action = null;

	protected String formName = "";

	protected String pageName = null;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getFormName() {
		return formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	/**
	 * @return
	 * @throws javax.servlet.jsp.JspException
	 */
	public int doStartTag() throws javax.servlet.jsp.JspException {
		Page query = (Page) (pageContext.getAttribute(pageName,	PageContext.REQUEST_SCOPE));

		StringBuffer sb = new StringBuffer();
		renderTable(sb, query);
		JspWriter writer = pageContext.getOut();
		
		try {
			writer.print(sb);
		} catch (IOException ie) {
			throw new JspException(ie.toString());
		}
		return EVAL_BODY_INCLUDE;
	}

	private void renderTable(StringBuffer sb, Page query) {
		// TODO �Զ���ɷ������
		sb.append("\n <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");
		renderElement(sb, query);
		renderMethod(sb, query);
	}

	private void renderElement(StringBuffer sb, Page query) {
		// TODO �Զ���ɷ������
		MessageResources resources = (MessageResources) pageContext
				.getRequest().getAttribute(Globals.MESSAGES_KEY);
		sb.append("\n <tr><td height=\"22\" >"
				+ resources.getMessage("page.nav.total")
				+ query.getRecordCount() + "&nbsp;&nbsp;&nbsp;"
				+ resources.getMessage("page.nav.sizeOfPage"));
		sb.append("\n <select id=\"pageSize\" name=\"pageSize\" class=\"MySelect\">");
		sb.append("\n <option value=\"" + query.getPageSize() + "\" selected>"
				+ query.getPageSize() + "</option>");
		sb.append("\n <option value=\"10\" >10</option>");
		sb.append("\n <option value=\"20\" >20</option>");
		sb.append("\n <option value=\"30\" >30</option>");
		sb.append("\n <option value=\"40\" >40</option>");
		sb.append("\n <option value=\"50\" >50</option>");
		sb.append("\n <option value=\"60\" >60</option>");
		sb.append("\n <option value=\"70\" >70</option>");
		sb.append("\n <option value=\"80\" >80</option>");
		sb.append("\n <option value=\"90\" >90</option>");
		sb.append("\n <option value=\"100\" >100</option>");
		sb.append("\n <option value=\"110\" >110</option>");
		sb.append("\n <option value=\"120\" >120</option>");
		sb.append("\n <option value=\"130\" >130</option>");
		sb.append("\n <option value=\"140\" >140</option>");
		sb.append("\n <option value=\"150\" >150</option>");
		sb.append("\n <option value=\"160\" >160</option>");
		sb.append("\n <option value=\"170\" >170</option>");
		sb.append("\n <option value=\"180\" >180</option>");
		sb.append("\n <option value=\"190\" >190</option>");
		sb.append("\n <option value=\"200\" >200</option>");
		sb.append("\n </select>&nbsp;" + resources.getMessage("page.nav.size")
				+ "&nbsp;&nbsp;</td>");
		sb.append("\n <td width=\"21%\" height=\"22\" valign=\"middle\">");
		sb.append("\n <div align=\"center\">");
		sb.append("<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");
		sb.append("\n <tr><td width=\"20\"><img src=\"../../images/page/unfirst.gif\" onClick=\"MoveFirst()\"></td>");
		sb.append("\n <td width=\"14\"><img src=\"../../images/page/unforward.gif\" onClick=\"MovePrevious()\"></td>");
		sb.append("\n <td width=\"46\" valign=\"bottom\">");
		sb.append("\n <table  class=\"nav_outline\" width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");
		sb.append("\n <tr><td><div align=\"center\">"
				+ query.getCurrentPageNo() + "/" + query.getPageCount()
				+ "</div></td></tr></table></td>");
		sb.append("\n <td width=\"12\"><div align=\"right\"><img src=\"../../images/page/unbackward.gif\" onClick=\"MoveNext()\"></div></td>");
		sb.append("\n <td width=\"19\"><div align=\"right\"><img src=\"../../images/page/unlast.gif\" onClick=\"MoveLast()\"></div></td>");
		sb.append("\n </tr></table></div></td>");
		sb.append("\n <td height=\"22\">&nbsp;&nbsp;"
				+ resources.getMessage("page.nav.go"));
		sb.append("\n <input type=\"text\" name=\"toPage\" maxlength=\"4\" class=\"nav_input\" value=\""
						+ query.getCurrentPageNo());
		sb.append("\" onfocus=\"this.select()\" onKeyPress=\"if(window.event.keyCode==13) GotoPage()\">"
						+ resources.getMessage("page.nav.page") + "&nbsp;&nbsp;");

		sb.append("\n <img src=\"../../images/page/view_search.gif\" onclick=\"GotoPage()\"></td></tr>");
	}

	private void renderMethod(StringBuffer sb, Page query) {
		// TODO �Զ���ɷ������
		sb.append("\n <script language=\"javascript\">");
		sb.append("\n  function MovePrevious()");
		sb.append("\n {");
		sb.append("\n if (" + query.getCurrentPageNo() + " - 1 >= 1) {");
		sb.append("\n " + formName + ".toPage.value = " + (query.getCurrentPageNo() - 1));
		sb.append("\n " + formName + ".pageSize.value = " + query.getPageSize());
		if (action != null)
			sb.append("\n " + formName + ".action = \"<%=request.getContextPath()%>" + action + "\"");
		sb.append("\n parent.query()}");
		//sb.append("\n " + formName + ".submit() }");
		sb.append("\n }");
		
		sb.append("\n  function MoveNext()");
		sb.append("\n {");
		sb.append("\n if (" + query.getCurrentPageNo() + " + 1 <= " + query.getPageCount() + ") {");
		sb.append("\n " + formName + ".toPage.value = " + (query.getCurrentPageNo() + 1));
		sb.append("\n " + formName + ".pageSize.value = " + query.getPageSize());
		if (action != null)
			sb.append("\n " + formName + ".action = \"<%=request.getContextPath()%>" + action + "\"");
		sb.append("\n parent.query()}");
//		sb.append("\n " + formName + ".submit() }");
		sb.append("\n }");

		sb.append("\n  function MoveFirst()");
		sb.append("\n {");
		sb.append("\n if (" + query.getCurrentPageNo() + " > 1) {");
		sb.append("\n " + formName + ".toPage.value = 1");
		sb.append("\n " + formName + ".pageSize.value = " + query.getPageSize());
		if (action != null)
			sb.append("\n " + formName + ".action = \"<%=request.getContextPath()%>" + action + "\"");
		sb.append("\n parent.query()}");
//		sb.append("\n " + formName + ".submit() }");
		sb.append("\n }");

		sb.append("\n  function MoveLast()");
		sb.append("\n {");
		sb.append("\n if (" + query.getCurrentPageNo() + " < " + query.getPageCount() + ") {");
		sb.append("\n " + formName + ".toPage.value = " + query.getPageCount());
		sb.append("\n " + formName + ".pageSize.value = " + query.getPageSize());
		if (action != null)
			sb.append("\n " + formName + ".action = \"<%=request.getContextPath()%>" + action + "\"");
		sb.append("\n parent.query()}");
		//		sb.append("\n " + formName + ".submit() }");
		sb.append("\n }");
		
		sb.append("\n  function GotoPage()");
		sb.append("\n {");
		sb.append("\n " + formName + ".toPage.value = formList.toPage.value");
		sb.append("\n " + formName + ".pageSize.value = formList.pageSize.value");
		if (action != null)
			sb.append("\n " + formName + ".action = \"<%=request.getContextPath()%>" + action + "\"");
		sb.append("\n parent.query()");
//		sb.append("\n " + formName + ".submit()");
		sb.append("\n }");
		sb.append("\n </script>");
	}

	/**
	 * @return
	 * @throws javax.servlet.jsp.JspException
	 */
	public int doEndTag() throws javax.servlet.jsp.JspException {
		JspWriter writer = pageContext.getOut();
		try {
			writer.print("</table>");

		} catch (IOException ie) {
			throw new JspException(ie.toString());
		}
		return EVAL_PAGE;
	}
}
