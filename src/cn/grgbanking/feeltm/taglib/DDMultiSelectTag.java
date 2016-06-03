package cn.grgbanking.feeltm.taglib;

import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspTagException;
import java.util.Map;
import java.util.Iterator;
import java.util.Set;

/**
 * <p>
 * Title: �����ն˼��ϵͳFEEL-View
 * </p>
 * <p>
 * Description: ��jsp�������ӣ�
 * </p>
 * <% request.setAttribute("txnId",BusnDataDir.tradeType.getDataMap()); String
 * strAllLabel=MsgResources.message(pageContext,"select.option.all"); %>
 * <view:dmselect name="txnId" dicId="txnId" scope="request" width="230"
 * height="150" separator="," allvalue="ALL_VALUE" allkey="<%=strAllLabel%>" />
 * �ñ�ǩ�����һ��hiddenField��hiddenField�ĳ�ʼֵΪallvalue, ��ʲô��ûѡΪ�մ���
 * ����Ϊ��separator�ָ��ѡ��ֵ���У������separator������1,2����ʽ����1,2,
 * 
 * 
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: GRGBanking Co.,Ltd
 * </p>
 * 
 * @author lvjm
 * @version 1.0
 */

@SuppressWarnings({"serial", "unchecked"})
public class DDMultiSelectTag extends TagSupport {
	private String name; // �ؼ���
	private String dicId; // ѡ����ֵ伯��
	private String txtStyle; // �ı�����ʽ
	private String sltStyle; // ѡ������ʽ
	private int width; // ѡ��������
	private int height; // ѡ������߶�
	private String separator; // ���ѡ��֮��ķָ��,Ϊ�ջ򳤶�С��1ʱĬ������Ϊ#
	private String allvalue; // ��ʾ"ȫѡ"��value,Ϊ�ջ򳤶�С��1ʱĬ������ΪALL_VALUE
	private String allkey; // ��ʾ"ȫѡ"��label,Ϊ�ջ򳤶�С��1ʱĬ������ΪALL
	private String scope; // ȡ���ϵĶ���(page,request,session)

	StringBuffer str_html;

	/**
	 * <p>
	 * ��ǿ�ʼʱ�Զ�ִ�еĺ��� ������Ҫ�Ǵ�scopeָ��������������collectionId�����ݼ���
	 * 
	 * @return int
	 * @throws JspTagException
	 */
	public int doStartTag() throws JspTagException {
		Object targetObject = null;
		Map mp = null;
		if (scope.equals("request")) {
			targetObject = pageContext.getRequest().getAttribute(dicId);
		} else if (scope.equals("session")) {
			targetObject = pageContext.getSession().getAttribute(dicId);
		} else if (scope.equals("page")) {
			targetObject = pageContext.getAttribute(dicId);
		}

		if (targetObject == null) {
			throw new JspTagException("DDMultiSelectTag: dicId " + dicId
					+ " not found in " + scope + " scope.");
		} else if (targetObject instanceof java.util.Map) {
			mp = (Map) targetObject;
		} else {
			throw new JspTagException("DDMultiSelectTag: dicId " + dicId
					+ " is not an instance of java.util.Map.");
		}

		// ����Ĭ��ֵ
		if (width <= 0) {
			width = 100;
		}
		if (height <= 0) {
			height = 100;
		}
		if (allkey == null || allkey.length() < 1) {
			this.allkey = "";
		}
		if (allvalue == null || allvalue.length() < 1) {
			this.allvalue = "ALL_VALUE";
		}
		if (separator == null || separator.length() < 1) {
			this.separator = "#";
		}

		str_html = new StringBuffer("");
		String txtName = ((new StringBuffer(name)).append("Txt")).toString();
		String layName = ((new StringBuffer(name)).append("Lay")).toString();
		String chkName = ((new StringBuffer(name)).append("Chk")).toString();

		str_html.append("<INPUT class=underLine readOnly value='");
		str_html.append(allkey);
		str_html.append("' name='");
		str_html.append(txtName);
		str_html.append("' id='");
		str_html.append(txtName);
		str_html.append("' style='width:");
		str_html.append(width);
		str_html.append("px'>");

		str_html.append("<INPUT type=hidden name='");
		str_html.append(name);
		str_html.append("' id='");
		str_html.append(name);
		str_html.append("' value='");
		str_html.append(allvalue);
		str_html.append("'>");
		str_html.append("<a style='CURSOR: hand' onclick=Expand");
		str_html.append(name);
		str_html.append("(this)>▼</a>");

		str_html.append("<DIV id='");
		str_html.append(layName);
		str_html
				.append("' style='Z-INDEX: 1; display:none; POSITION: absolute'>");

		str_html
				.append("<TABLE class=tboutline cellSpacing=0 cellPadding=0 bgColor=#ffffff><TBODY><TR><TD vAlign=top>");
		str_html.append("<DIV style='OVERFLOW: auto; WIDTH: ");
		str_html.append(width);
		str_html.append("px; HEIGHT: ");
		str_html.append(height);
		str_html.append("px'>");

		str_html.append("<SCRIPT language=javascript>");
		str_html.append("function Expand");
		str_html.append(name);
		str_html.append("(imgObj)");
		str_html.append("{var topX=window.event.x-10-");
		str_html.append(width);
		str_html.append(";var topY=window.event.y+10;");
		str_html.append("var whichLay=eval('");
		str_html.append(layName);
		str_html.append("');if (whichLay.style.display == 'none')");
		str_html
				.append("{whichLay.style.display = 'block';whichLay.style.left=topX;whichLay.style.top=topY;imgObj.innerText='▲';}");
		str_html
				.append("else{whichLay.style.display = 'none';imgObj.innerText='▼';}}");

		str_html.append("function  MultiSelect");
		str_html.append(name);
		str_html.append("(chkBox)");
		str_html.append("{var objTxt=document.getElementById('");
		str_html.append(txtName);
		str_html.append("');");
		str_html.append("var obj=document.getElementById('");
		str_html.append(name);
		str_html.append("');");
		str_html.append("var objChks=document.getElementsByName('");
		str_html.append(chkName);
		str_html.append("');");
		str_html
				.append("if(chkBox.value==''){for(var i=1;i<objChks.length;i++){objChks[i].checked=chkBox.checked;}}");
		str_html.append("else{if(!chkBox.checked){objChks[0].checked=false;}}");
		str_html.append("if(objChks[0].checked){");
		str_html.append("var  txtInfo='';var  valueInfo='';");
		
		str_html.append("for(var i=1;i<objChks.length;i++){");

		str_html.append("txtInfo+=objChks[i].des+',';");
		str_html.append("valueInfo+=objChks[i].value+',';}");
		str_html.append("objTxt.value=txtInfo;");
		str_html.append("obj.value=valueInfo==\"\"?\"\":valueInfo.substring(0,valueInfo.length-1);");
		
		//str_html.append("{objTxt.value='");
		//str_html.append(allkey);
		//str_html.append("';obj.value='");
		//str_html.append(allvalue);
		str_html.append("}else{var  txtInfo='';var  valueInfo='';");
		str_html.append("for(var i=1;i<objChks.length;i++)");
		str_html.append("{if(objChks[i].checked){");
		str_html.append("txtInfo+=objChks[i].des+',';");
		str_html.append("valueInfo+=objChks[i].value+'");
		str_html.append(separator);
		str_html.append("';}");
		str_html
		  .append("objTxt.value=txtInfo;obj.value=valueInfo==\"\"?\"\":valueInfo.substring(0,valueInfo.length-1);}}}</SCRIPT>");

		str_html.append("<INPUT onclick=MultiSelect");
		str_html.append(name);
		str_html.append("(this) type=checkbox  value='' name='");
		str_html.append(chkName);
		str_html.append("'> ");
		str_html.append(allkey);
		str_html.append("<BR>");

		Set st = mp.keySet();
		Iterator itetor = st.iterator();

		while (itetor.hasNext()) {
			Object obj = itetor.next();
			str_html.append("<INPUT onclick=MultiSelect");
			str_html.append(name);
			str_html.append("(this) type=checkbox  value='");
			str_html.append((String) obj);
			str_html.append("' name='");
			str_html.append(chkName);
			str_html.append("' des='");
			str_html.append((String) mp.get(obj));
			str_html.append("'> ");
			str_html.append((String) mp.get(obj));
			str_html.append("<BR>");
		}

		str_html.append("</DIV></TD></TR></TBODY></TABLE></DIV>");
		return SKIP_BODY;
	}

	/**
	 * ��ǽ���ʱ���ؼ�html
	 * 
	 * @return
	 * @throws JspTagException
	 */
	public int doEndTag() throws JspTagException {
		try {
			pageContext.getOut().print(str_html.toString());
		} catch (Exception ex) {
			throw new JspTagException("DDMultiSelectTag:doEndTag caught: " + ex);
		}
		return EVAL_PAGE;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getDicId() {
		return dicId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope.toLowerCase();
	}

	public String getSltStyle() {
		return sltStyle;
	}

	public void setSltStyle(String sltStyle) {
		this.sltStyle = sltStyle;
	}

	public String getTxtStyle() {
		return txtStyle;
	}

	public void setTxtStyle(String txtStyle) {
		this.txtStyle = txtStyle;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setDicId(String dicId) {
		this.dicId = dicId;
	}

	public String getAllkey() {
		return allkey;
	}

	public void setAllkey(String allkey) {
		this.allkey = allkey;
	}

	public String getAllvalue() {
		return allvalue;
	}

	public void setAllvalue(String allvalue) {
		this.allvalue = allvalue;
	}

	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}
}