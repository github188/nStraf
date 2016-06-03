package cn.grgbanking.feeltm.taglib;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import cn.grgbanking.feeltm.util.TagResources;

@SuppressWarnings("serial")
public class StepStatusTag extends TagSupport {

	private String currstep;

	private String steptotal;

	// �����������á�;�����
	private String stepnames;

	private String bundle;

	public String getBundle() {
		return bundle;
	}

	public void setBundle(String bundle) {
		this.bundle = bundle;
	}

	public String getCurrstep() {
		return currstep;
	}

	public void setCurrstep(String currstep) {
		this.currstep = currstep;
	}

	public String getStepnames() {
		return stepnames;
	}

	public void setStepnames(String stepnames) {
		this.stepnames = stepnames;
	}

	public String getSteptotal() {
		return steptotal;
	}

	public void setSteptotal(String steptotal) {
		this.steptotal = steptotal;
	}

	StringBuffer str_html;

	public int doStartTag() throws JspTagException {
		str_html = new StringBuffer();

		// ��ǰ����
		int icurrstep = 0;
		if (currstep != null && !currstep.equals("")) {
			try {
				icurrstep = Integer.parseInt(currstep);
			} catch (Exception e) {
				// TODO: handle exception
				return SKIP_BODY;
			}
		} else {
			return SKIP_BODY;
		}

		// �ܹ�����
		int isteptotal = 0;
		if (steptotal != null && !steptotal.equals("")) {
			try {
				isteptotal = Integer.parseInt(steptotal);
			} catch (Exception e) {
				// TODO: handle exception
				return SKIP_BODY;
			}
		} else {
			return SKIP_BODY;
		}

		String[] stepname = stepnames.split(";");
		if (stepname == null || stepname.length < isteptotal) {
			return SKIP_BODY;
		}

		if (bundle != null) {
			try {
				for (int i = 0; i < stepname.length; i++) {
					if (bundle.equals("")) {
						stepname[i] = TagResources.message(pageContext,
								stepname[i]);
					} else {
						stepname[i] = TagResources.message(pageContext,
								stepname[i], bundle);
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
				return SKIP_BODY;
			}
		}

		HttpServletRequest request = (HttpServletRequest) pageContext
				.getRequest();
		String strContextPath = request.getContextPath();
		String imgBasePath = strContextPath + "/images/step/";

		StringBuffer src_01 = new StringBuffer(" src=\"" + imgBasePath
				+ "run.gif\"");
		StringBuffer src_02 = new StringBuffer(" src=\"" + imgBasePath
				+ "pause.gif\"");

		StringBuffer bg_01 = new StringBuffer(" src=\"" + imgBasePath
				+ "to.gif\"");

		str_html.append("<table width=\"100%\"><tr><td align=\"center\">");
		str_html
				.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">");
		str_html.append("<tr><td width=\"20\"></td>");

		// ��һ������ڵ�
		str_html.append("<td align=\"right\"><img ");
		if (icurrstep == 1) {
			str_html.append(src_01);
		} else {
			str_html.append(src_02);
		}
		str_html.append(" width=\"12\" height=\"23\"></td><td><img ");

		str_html.append(bg_01);

		str_html.append(" width=\"12\" height=\"23\" align=\"left\"></td> ");

		// �м䲽����
		for (int step = 2; step < isteptotal; step++) {
			str_html.append("<td height=\"23\" align=\"right\"><img ");
			if (step == icurrstep) {
				str_html.append(src_01);
			} else {
				str_html.append(src_02);
			}
			str_html.append(" width=\"12\" height=\"23\"></td> <td><img ");
			str_html.append(bg_01);

			str_html
					.append(" width=\"12\" height=\"23\" align=\"left\"></td> ");

		}

		// ���һ������ڵ�

		str_html.append("<td align=\"left\"><img ");
		if (icurrstep < isteptotal) {
			str_html.append(src_02);
		} else {
			str_html.append(src_01);
		}
		str_html
				.append(" width=\"11\" height=\"23\"></td><td width=\"20\"></td></tr>");

		// ÿһ�������˵��
		str_html.append("<tr><td></td>");
		for (int step = 1; step < isteptotal + 1; step++) {
			str_html.append("<td colspan=\"2\"");
			// ͻ����ʾ��ǰ����
			if (icurrstep == step) {
				str_html.append(" class=\"boldblue\"");
			}
			str_html.append(" >");
			str_html.append(stepname[step - 1]);
			str_html.append("</td><td></td>");
		}
		str_html.append("</tr></table></td></tr>");

		String step1note = "";
		String step2note = "";
		String step3note = "";
		try {
			step1note = TagResources.message(pageContext, "step.step1note");
			step2note = TagResources.message(pageContext, "step.step2note");
			step3note = TagResources.message(pageContext, "step.step3note");
		} catch (Exception e) {
			// TODO: handle exception
		}
		// ��ǰ����˵��
		str_html.append("<tr><td align=\"center\" class=\"boldblue\" >");
		str_html.append(step1note);
		str_html.append(currstep);
		str_html.append(step2note);
		str_html.append(steptotal);
		str_html.append(step3note);
		str_html.append("</td></tr></table>");

		return SKIP_BODY;
	}

	/**
	 * ��ǽ���ʱ����ؼ�html
	 * 
	 * @return
	 * @throws JspTagException
	 */
	public int doEndTag() throws JspTagException {
		try {
			pageContext.getOut().print(str_html.toString());
		} catch (Exception ex) {
			throw new JspTagException("StepStatusTag:doEndTag caught: " + ex);
		}
		return EVAL_PAGE;
	}
}
