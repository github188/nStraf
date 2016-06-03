package cn.grgbanking.feeltm.taglib;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import cn.grgbanking.feeltm.util.TagResources;
import cn.grgbanking.framework.util.Page;

/**
 * 
 * @author wzhuo ��ɷ�ҳ��Tag Parameters1 : action ִ�е�.do�ļ�����ѡ Parameters2 :
 *         formName form����ƣ�Ĭ��ΪformList,���� Parameters3 : pageName
 *         page����ƣ���Hibernate��ɵ�page������ Parameters4 : submitFunction
 *         �ύ���ĺ����ѡ
 *         �����Ǻ���������parent.Query()��������javascript�ű������磺alert
 *         ('hello')�� ���submitFunctionΪ�գ�ͨ��
 *         formName.submit()�ύ�?���������submitFunction��Ľű�
 * 
 */
@SuppressWarnings("serial")
public class PaginationTag extends TagSupport {
	protected String action = null;

	protected String formName = "";

	protected String pageName = null;

	protected String submitFunction = null;

	protected String jsName = null;

	protected String thisForm = null;

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

	public String getJsName() {
		return jsName;
	}

	public void setJsName(String jsName) {
		this.jsName = jsName;
	}

	/**
	 * @return
	 * @throws javax.servlet.jsp.JspException
	 */
	public int doStartTag() throws javax.servlet.jsp.JspException {
		Page page = (Page) (pageContext.getAttribute(pageName,
				PageContext.REQUEST_SCOPE));

		StringBuffer sb = new StringBuffer();
		//renderTable(sb, page);

		JspWriter writer = pageContext.getOut();
		try {
			writer.print(sb);
		} catch (IOException ie) {
			throw new JspException(ie.toString());
		}
		return EVAL_BODY_INCLUDE;
	}

	private void renderTable(StringBuffer sb, Page page) {
		HttpServletRequest request = (HttpServletRequest) pageContext
				.getRequest();
		// TODO �Զ���ɷ������
		sb
				.append("\n  <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");
		renderElement(sb, page, request);
		renderMethod(sb, page, request);
	}

	private void renderElement(StringBuffer sb, Page query,
			HttpServletRequest request) {

		String strContextPath = request.getContextPath();
		String imgBasePath = strContextPath + "/images/page/";

		String msgTotal = null;
		String msgSizeOfPage = null;
		String msgSize = null;
		String msgGo = null;
		String msgPage = null;

		try {
			msgTotal = TagResources.message(pageContext, "page.nav.total");
			msgSizeOfPage = TagResources.message(pageContext,
					"page.nav.sizeOfPage");
			msgSize = TagResources.message(pageContext, "page.nav.size");
			msgGo = TagResources.message(pageContext, "page.nav.go");
			msgPage = TagResources.message(pageContext, "page.nav.page");
		} catch (JspException e) {
			e.printStackTrace();
		}

		sb
				.append("\n <tr><td height=\"22\" >" + msgTotal
						+ query.getRecordCount() + "&nbsp;&nbsp;&nbsp;"
						+ msgSizeOfPage);
		sb
				.append("\n <select id=\"pageSize\" name=\"pageSize\" onChange='onChangeSize()'>");

		int iPageSize = query.getPageSize();

		for (int i = 10; i < 201; i = i + 10) {
			sb.append("\n <option value=\"" + i + "\"");
			if (i == iPageSize)
				sb.append(" selected");

			sb.append(" >");
			sb.append(i);
			sb.append("</option>");
		}

		sb.append("\n </select>&nbsp;" + msgSize + "&nbsp;&nbsp;</td>");
		sb.append("\n <td width=\"21%\" height=\"22\" valign=\"middle\">");
		sb.append("\n <div align=\"center\">");
		sb.append("<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");

		sb.append("\n <tr><td width=\"20\">");

		// ��ǰҳ
		long currentPage = query.getCurrentPageNo();
		int pageSum = query.getPageCount();

		// ��ҳ
		if (currentPage > 1)
			sb
					.append("<img src=\""
							+ imgBasePath
							+ "first.gif\" style=\"CURSOR:hand\" onClick=\"MoveFirst()\"></td>");
		else
			sb.append("<img src=\"" + imgBasePath + "unfirst.gif\"></td>");

		// ��һҳ
		sb.append("\n <td width=\"14\">");
		if (currentPage > 1)
			sb
					.append("<img src=\""
							+ imgBasePath
							+ "forward.gif\" style=\"CURSOR:hand\" onClick=\"MovePrevious()\"></td>");
		else
			sb.append("<img src=\"" + imgBasePath + "unforward.gif\"></td>");

		sb.append("\n <td width=\"46\" valign=\"bottom\">");
		sb
				.append("\n <table  class=\"nav_outline\" width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");
		sb.append("\n <tr><td><div align=\"center\">" + currentPage + "/"
				+ pageSum + "</div></td></tr></table></td>");
		sb.append("\n <td width=\"12\"><div align=\"right\">");

		// ��һҳ
		if (pageSum > 0 && currentPage < pageSum)
			sb
					.append("<img src=\""
							+ imgBasePath
							+ "backward.gif\" style=\"CURSOR:hand\" onClick=\"MoveNext()\">");
		else
			sb.append("<img src=\"" + imgBasePath + "unbackward.gif\">");

		sb.append("</div></td>");

		sb.append("\n <td width=\"19\"><div align=\"right\">");
		// ĩҳ
		if (pageSum > 0 && currentPage < pageSum) {
			sb
					.append("<img src=\""
							+ imgBasePath
							+ "last.gif\" style=\"CURSOR:hand\" onClick=\"MoveLast()\">");
		} else {
			sb.append("<img src=\"" + imgBasePath + "unlast.gif\">");
		}

		sb.append("</div></td>");
		sb.append("\n </tr></table></div></td>");
		sb.append("\n <td height=\"22\">&nbsp;&nbsp;" + msgGo);
		sb
				.append("\n <input type=\"text\" name=\"toPage\" id=\"toPage\" maxlength=\"4\" class=\"nav_input\" value=\""
						+ currentPage);
		sb
				.append("\" onfocus=\"this.select()\" onkeyup=\"this.value=this.value.replace(/[^\\d+]/g,'')\" onKeyPress=\"if(window.event.keyCode==13) ");
		sb.append("GotoPage()\">");

		sb.append(msgPage + "&nbsp;&nbsp;");

		sb.append("\n <img src=\"" + imgBasePath
				+ "view_search.gif\" style=\"CURSOR:hand\" onclick=\"");
		sb.append("GotoPage()\"></td></tr>");
		// System.out.println(sb.toString());

	}

	private void renderMethod(StringBuffer sb, Page query,
			HttpServletRequest request) {

		if (submitFunction == null)
			submitFunction = formName + ".submit();";

		String strContextPath = request.getContextPath();

		// TODO �Զ���ɷ������
		sb.append("\n <script language=\"javascript\">");
		sb.append("\n  function MovePrevious()");
		sb.append("\n {");
		sb.append("\n if (" + query.getCurrentPageNo() + " - 1 >= 1) {");
		sb.append("\n " + formName + ".pageNum.value = "
				+ (query.getCurrentPageNo() - 1));
		sb
				.append("\n " + formName + ".pageSize.value = "
						+ query.getPageSize());
		if (action != null)
			sb.append("\n " + formName + ".action = \"" + strContextPath
					+ action + "\"");
		sb.append("\n " + submitFunction + " }");
		sb.append("\n }");

		sb.append("\n  function MoveNext()");
		sb.append("\n {");
		sb.append("\n if (" + query.getCurrentPageNo() + " + 1 <= "
				+ query.getPageCount() + ") {");
		sb.append("\n " + formName + ".pageNum.value = "
				+ (query.getCurrentPageNo() + 1));
		sb
				.append("\n " + formName + ".pageSize.value = "
						+ query.getPageSize());
		if (action != null)
			sb.append("\n " + formName + ".action = \"" + strContextPath
					+ action + "\"");
		sb.append("\n " + submitFunction + " }");
		sb.append("\n }");

		sb.append("\n  function MoveFirst()");
		sb.append("\n {");
		sb.append("\n if (" + query.getCurrentPageNo() + " > 1) {");
		sb.append("\n " + formName + ".pageNum.value = 1");
		sb
				.append("\n " + formName + ".pageSize.value = "
						+ query.getPageSize());
		if (action != null)
			sb.append("\n " + formName + ".action = \"" + strContextPath
					+ action + "\"");
		sb.append("\n " + submitFunction + " }");
		sb.append("\n }");

		sb.append("\n  function MoveLast()");
		sb.append("\n {");
		sb.append("\n if (" + query.getCurrentPageNo() + " < "
				+ query.getPageCount() + ") {");
		sb
				.append("\n " + formName + ".pageNum.value = "
						+ query.getPageCount());
		sb
				.append("\n " + formName + ".pageSize.value = "
						+ query.getPageSize());
		if (action != null)
			sb.append("\n " + formName + ".action = \"" + strContextPath
					+ action + "\"");
		sb.append("\n " + submitFunction + " }");
		sb.append("\n }");

		sb.append("\n  function GotoPage()");
		sb.append("\n {");
		sb.append("\n if( isNumber(formList.toPage.value) == false){ ");
		sb.append("\n " + formName + ".pageNum.value =1;");
		sb.append("\n " + formName
				+ ".pageSize.value = formList.pageSize.value");
		sb.append("\n } else if( formList.toPage.value>" + query.getPageCount()
				+ "){");
		sb.append("\n " + formName + ".pageNum.value =" + query.getPageCount());
		sb.append("\n " + formName
				+ ".pageSize.value = formList.pageSize.value");
		sb.append("\n } else if(formList.toPage.value<=0) {");
		sb.append("\n " + formName + ".pageNum.value =1");
		sb.append("\n " + formName
				+ ".pageSize.value = formList.pageSize.value");

		sb.append("\n }else {");
		sb.append("\n " + formName + ".pageNum.value = formList.toPage.value");
		sb.append("\n " + formName
				+ ".pageSize.value = formList.pageSize.value");
		sb.append("\n }");

		if (action != null)
			sb.append("\n " + formName + ".action = \"" + strContextPath
					+ action + "\"");
		sb.append("\n " + submitFunction);
		sb.append("\n }");

		// �ж��Ƿ�������
		sb.append("\n  function isNumber(oNum) {");
		sb.append("\n  if(!oNum) return false ;");
		sb.append("\n  var strP=/^\\d+(\\.\\d+)?$/;");
		sb.append("\n  if(!strP.test(oNum)) return false;");
		sb.append("\n  try{");
		sb.append("\n  if(parseFloat(oNum)!=oNum) return false;");
		sb.append("\n  }");
		sb.append("\n  catch(ex)");
		sb.append("\n  {");
		sb.append("\n  return false;");
		sb.append("\n  }");
		sb.append("\n  return true;");
		sb.append("\n  }");

		sb.append("\n  function onChangeSize()");
		sb.append("\n {");
		sb.append("\n " + formName + ".pageNum.value = 1");
		sb.append("\n " + formName
				+ ".pageSize.value = formList.pageSize.value");
		if (action != null)
			sb.append("\n " + formName + ".action = \"" + strContextPath
					+ action + "\"");
		sb.append("\n " + submitFunction);
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

	public String getSubmitFunction() {
		return submitFunction;
	}

	public void setSubmitFunction(String submitFunction) {
		this.submitFunction = submitFunction;
	}

	public String getThisForm() {
		return thisForm;
	}

	public void setThisForm(String thisForm) {
		this.thisForm = thisForm;
	}

}
