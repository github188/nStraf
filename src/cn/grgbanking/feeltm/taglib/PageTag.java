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
 * @author ��ɷ�ҳ��Tag Parameters1 : action ִ�е�.do�ļ�����ѡ Parameters2 :
 *         formName form����ƣ�Ĭ��ΪformList,���� Parameters3 : pageName
 *         page����ƣ���Hibernate��ɵ�page������ Parameters4 : submitFunction
 *         �ύ���ĺ����ѡ
 *         �����Ǻ���������parent.Query()��������javascript�ű������磺alert
 *         ('hello')�� ���submitFunctionΪ�գ�ͨ��
 *         formName.submit()�ύ�?���������submitFunction��Ľű�
 * 
 */
@SuppressWarnings("serial")
public class PageTag extends TagSupport {
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
		renderTable(sb, page);
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
				.append("<table border=\"0\" cellspacing=\"1\" cellpadding=\"4\" class=\"Pagination_table\">");
		renderElement(sb, page, request);
		
	}

	private void renderElement(StringBuffer sb, Page query,
			HttpServletRequest request) {

		String msgTotal = "";
		String msgSize = "";
		String msgGo = "";
		String msgPage = "";
		String forward = "";
		String backward = "";

		try {
			msgTotal = TagResources.message(pageContext, "page.nav.total");
			msgSize = TagResources.message(pageContext, "page.nav.size");
			msgGo = TagResources.message(pageContext, "page.nav.go");
			msgPage = TagResources.message(pageContext, "page.nav.page");
			forward = TagResources.message(pageContext, "page.nav.forward");
			backward = TagResources.message(pageContext, "page.nav.backward");
		} catch (JspException e) {
			e.printStackTrace();
		}

		// ��ǰҳ
		long currentPage = query.getCurrentPageNo();
		int pageSum = query.getPageCount();

		// �ܼƼ�¼
		int recordCount = query.getRecordCount();

		sb.append("<input type=\"hidden\" name=\"pageSize\" id=\"pageSize\" value=\"20\">");
		sb.append("<input type=\"hidden\" name=\"pageNum\" id=\"pageNum\" value=\"1\">");

		// ��һҳ
		sb.append("<tr><td nowrap class=\"Pagination\">");
		if (currentPage > 1)
			sb
					.append("<INPUT class=Btn type=button value=\"")
					.append(forward)
					.append("\" name=lastpage onClick=\"MovePrevious()\"></td>");
		else
			sb.append("<INPUT class=BtnDisable type=button value=\"").append(
					forward).append(
					"\" name=lastpage  disabled=\"disabled\"></td>");

		// ��һҳ
		sb.append("<td nowrap class=\"Pagination\">");
		if (pageSum > 0 && currentPage < pageSum)
			sb.append("<INPUT class=Btn type=button value=\"").append(backward)
					.append("\" name=nextpage onClick=\"MoveNext()\"></td>");
		else
			sb.append("<INPUT class=BtnDisable type=button value=\"").append(
					backward).append(
					"\" name=nextpage  disabled=\"disabled\"></td>");
	//	sb.append("</td>");

		// ��xҳ
		sb.append("<td nowrap class=\"Pagination\">").append(msgTotal).append(
				recordCount).append(msgSize).append(pageSum).append(msgPage)
				.append("</td>");

		// ��nҳ GO
		sb
				.append("<td nowrap class=\"Pagination\">")
				.append(msgGo)
				.append(
						"<input type=\"text\" name=\"gotoPageNo\" id=\"gotoPageNo\" maxlength=\"8\" class=textbox size=\"2\" value=\""
								+ currentPage);
		sb
				.append(
						"\" onfocus=\"this.select()\" onkeyup=\"this.value=this.value.replace(/[^\\d+]/g,'')\" onKeyPress=\"if(window.event.keyCode==13) ")
				.append("GotoPage()\">")
				.append(msgPage)
				.append(
						"<INPUT class=Btn type=button value=GO name=GO onclick=\"")
				.append("GotoPage()\"></td></tr></table>");

		if (submitFunction == null)
			// submitFunction = formName + ".submit();";
			submitFunction = "query();";

		String strContextPath = request.getContextPath();

		// TODO �Զ���ɷ������
		sb
				.append("\n <script language=\"javascript\" type=\"text/javascript\">");
		sb.append("\n  function MovePrevious()");
		sb.append("\n {");
		// sb.append("\n if (" + formName + ".pageNum.value >= 2) {");
		// sb.append("\n " + formName + ".pageNum.value = "
		// + (formName + ".pageNum.value*1 - 1"));
		// sb.append("\n " + formName + ".pageSize.value = "
		// + query.getPageSize());
		sb.append("\n if(document.getElementById('pageNum').value>=2){");
		sb
				.append("\n document.getElementById('pageNum').value=document.getElementById('pageNum').value*1-1");
		sb.append("\n document.getElementById('pageSize').value="
				+ query.getPageSize());

		if (action != null)
			sb.append("\n " + formName + ".action = \"" + strContextPath
					+ action + "\"");
		sb.append("\n " + submitFunction + " }");
		sb.append("\n }");

		sb.append("\n  function MoveNext()");
		sb.append("\n {");
		// sb.append("\n " + formName + ".pageNum.value = "
		// + (formName + ".pageNum.value*1 + 1 "));
		// sb.append("\n " + formName + ".pageSize.value = "
		// + query.getPageSize());
		sb
				.append("\n document.getElementById('pageNum').value=document.getElementById('pageNum').value*1+1");
		sb.append("\n document.getElementById('pageSize').value="
				+ query.getPageSize());

		if (action != null)
			sb.append("\n " + formName + ".action = \"" + strContextPath
					+ action + "\"");
		sb.append("\n " + submitFunction);
		sb.append("\n }");

		sb.append("\n  function MoveFirst()");
		sb.append("\n {");
		// sb.append("\n if (" + formName + ".pageNum.value > 1) {");
		// sb.append("\n " + formName + ".pageNum.value = 1");
		// sb.append("\n " + formName + ".pageSize.value = "
		// + query.getPageSize());
		sb.append("\n if(document.getElementById('pageNum').value>1){");
		sb.append("\n document.getElementById('pageNum').value=1");
		sb.append("\n document.getElementById('pageSize').value="
				+ query.getPageSize());
		if (action != null)
			sb.append("\n " + formName + ".action = \"" + strContextPath
					+ action + "\"");
		sb.append("\n " + submitFunction + " }");
		sb.append("\n }");

		sb.append("\n  function MoveLast()");
		sb.append("\n {");
		sb.append("\n if (" + currentPage + " < " + pageSum + ") {");
		// sb.append("\n " + formName + ".pageNum.value = " + pageSum);
		sb.append("\n document.getElementById('pageNum').value=" + pageSum);
		if (action != null)
			sb.append("\n " + formName + ".action = \"" + strContextPath
					+ action + "\"");
		sb.append("\n " + submitFunction + " }");
		sb.append("\n }");

		sb.append("\n  function GotoPage()");
		sb.append("\n {");
		// sb.append("\n if( isNumber(" + formName +
		// ".pageNum.value) == false){ ");
		// sb.append("\n " + formName + ".pageNum.value =1;");
		// sb.append("\n } else if( " + formName +
		// ".pageNum.value>"+pageSum+"){");
		// sb.append("\n " + formName + ".pageNum.value ="+pageSum);
		// sb.append("\n } else if(" + formName + ".pageNum.value<=0) {");
		// sb.append("\n "+ formName + ".pageNum.value =1");

		sb
				.append("\n document.getElementById('pageNum').value=document.getElementById('gotoPageNo').value;");

		sb.append("\n if(isNumber(document.getElementById('pageNum').value)){");

		sb.append("\n if(document.getElementById('pageNum').value>" + pageSum
				+ "){");
		sb.append("\n document.getElementById('pageNum').value='" + pageSum
				+ "';");
		sb.append("\n }");

		sb.append("\n else if(document.getElementById('pageNum').value<=0){");
		sb.append("\n document.getElementById('pageNum').value='1';");
		sb.append("\n }");

		sb.append("\n }");
		sb.append("\n else{");
		sb.append("\n document.getElementById('pageNum').value='1';");
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
		// sb.append("\n " + formName + ".pageNum.value = 1");
		sb.append("\n document.getElementById('pageNum').value=1");
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
