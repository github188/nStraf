package cn.grgbanking.feeltm.taglib;

import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.taglib.TagUtils;

import cn.grgbanking.feeltm.context.BaseApplicationContext;
import cn.grgbanking.feeltm.datadir.dao.DataDirDao;
import cn.grgbanking.feeltm.project.dao.ProjectDao;
import cn.grgbanking.feeltm.project.domain.Project;
import cn.grgbanking.feeltm.util.TagResources;

@SuppressWarnings("serial")
public class TmSelectTag extends TagSupport {

	private String id;

	private String name;

	private String selType;

	private String path;

	private String onchange;

	private String style;

	private String defaultValue;

	private String beforeOption;

	private String beanName;

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public String getBeforeOption() {
		return beforeOption;
	}

	public void setBeforeOption(String beforeOption) {
		this.beforeOption = beforeOption;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

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

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

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
		String smp = "";
		if (selType.equals("dataDir")) {
			DataDirDao dataDirDao = (DataDirDao) BaseApplicationContext
					.getAppContext().getBean("dataDirDao");
			targetObject = dataDirDao.getOptionByPath(path);

		} else if (selType.equals("dataDirCash")) {
			DataDirDao dataDirDao = (DataDirDao) BaseApplicationContext
					.getAppContext().getBean("dataDirDao");
			targetObject = dataDirDao.getOptionByPathCash(path);

		}else if(selType.equals("projectName")){
			ProjectDao groupDao = (ProjectDao)BaseApplicationContext.getAppContext().getBean("projectDao");
			List<Project> groups = groupDao.listAllGroup();
			StringBuffer sbfXml = new StringBuffer();
			for(Project group:groups){
				sbfXml.append("<option value=\""+group.getName()+"\">"+group.getName()+"</option>");
			}
			targetObject = sbfXml.toString();
			List<Project> groupList = groupDao.getProjectByUserId(defaultValue);
			if(groupList!=null&&groupList.size()>0){
				defaultValue = groupList.get(0).getName();
			}else{
				defaultValue = null;
			}
		}
		try {
			if (beanName != null) {
				Object codeValue1 = TagUtils.getInstance().lookup(pageContext,
						beanName, name, null);

				defaultValue = codeValue1.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (targetObject == null) {
			throw new JspTagException("TmSelectTag: selType " + selType
					+ " not found in scope.");
		} else if (targetObject instanceof java.lang.String) {
			smp = (String) targetObject;
		} else {
			throw new JspTagException("TmSelectTag: selType " + selType
					+ " is not an instance of java.lang.String.");
		}

		str_html = new StringBuffer();
		str_html.append("<select ");
		str_html.append(" id='").append(id).append("' ");
		str_html.append(" name='").append(name).append("' ");
		if (onchange != null) {
			str_html.append(" onchange='").append(onchange).append("' ");
		}
		if (style != null) {
			str_html.append(" style='").append(style).append("' ");
		}
		str_html.append("> ");

		if (beforeOption != null && beforeOption.equalsIgnoreCase("all")) {
			str_html.append("<option value=''>");

			String msgInfo = "";
			try {
				msgInfo = TagResources.message(pageContext, "label.select.all");
			} catch (JspException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			str_html.append(msgInfo);
			str_html.append("</option>");
		} else if (beforeOption != null
				&& beforeOption.equalsIgnoreCase("select")) {
			str_html.append("<option value=''>");

			String msgInfo = "";
			try {
				msgInfo = TagResources.message(pageContext, "label.select.one");
			} catch (JspException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			str_html.append(msgInfo);
			str_html.append("</option>");
		}

		if (defaultValue != null) {
			int i = smp.indexOf("value='" + defaultValue + "'");
			if (i < 0)
				i = smp.indexOf("value=\"" + defaultValue + "\"");

			if (i > -1) {
				str_html.append(smp.substring(0, i));
				str_html.append("value='").append(defaultValue);
				str_html.append("' selected ");
				i = i + 8 + defaultValue.length();
				str_html.append(smp.substring(i));
			}

		} else {
			str_html.append(smp);
		}

		str_html.append("</select>");

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
			throw new JspTagException("ViewSelectTag:doEndTag caught: " + ex);
		}
		return EVAL_PAGE;
	}

	public String getSelType() {
		return selType;
	}

	public void setSelType(String selType) {
		this.selType = selType;
	}

	public String getOnchange() {
		return onchange;
	}

	public void setOnchange(String onchange) {
		this.onchange = onchange;
	}

}
