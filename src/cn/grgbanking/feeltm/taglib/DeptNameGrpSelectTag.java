package cn.grgbanking.feeltm.taglib;

import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import cn.grgbanking.feeltm.config.BusnDataDir;
import cn.grgbanking.feeltm.context.BaseApplicationContext;
import cn.grgbanking.feeltm.domain.SysDatadir;
import cn.grgbanking.feeltm.project.dao.ProjectDao;
import cn.grgbanking.feeltm.project.domain.Project;
import cn.grgbanking.feeltm.um.dao.SysUserInfoDao;
import cn.grgbanking.feeltm.util.TagResources;

public class DeptNameGrpSelectTag extends TagSupport {
	
	private String deptId;		//部门Id
	private String userId;		//用户名Id
	private String groupId;		//组别Id
	private String deptName;		//部门name 
	private String userName;		//用户名name
	private String groupName;		//组别name
	
	private String deptHeadKey;		//部门默认显示项
	private String deptHeadValue;	//部门默认值
	private String userHeadKey;		//用户名默认显示项
	private String userHeadValue;	//部门默认值
	private String groupHeadKey;	//组别默认显示项
	private String groupHeadValue;	//组别默认值
	
	private String isloadName;		//是否首次加载全部人员名单
	private String type;			//查询或是增加等，当是查询时小组选择是可选，其他是只读.默认是query
	
	private String lableStyle;		//标签样式
	private String selectStyle;		//select选择框样式
	
	private String labelClass;		//标签class
	private String selectClass;		//select选择框class
	
	private String labelDept;		//部门标签，默认为"部门"
	private String labelGroup;		//组别标签，默认为"组别"
	private String labelUser;		//用户标签，默认为"用户"
	
	private String deptLabelClass;	//部门标签列属性集合
	private String deptClass;		//部门选框列属性集合
	private String groupLabelClass;	//组别标签列属性集合
	private String groupClass;		//组别选框列属性集合
	private String userLabelClass;	//用户标签列属性集合
	private String userClass;		//用户选框列属性集合
	
	private String trClass;			//行属性集合
	private String tableClass;		//表属性集合
	
	
	public int doEndTag() throws JspException {
		StringBuffer str_html = null;
		try {
			this.setDefaultValue();
			JspWriter out = pageContext.getOut();
//			String label_colon  = TagResources.message(pageContext, "label.colon");
			String label_colon  = "";
			List<SysDatadir> deptOrder = BusnDataDir.getObjectListInOrder("staffManager.department");
			SysUserInfoDao userDao =  (SysUserInfoDao) BaseApplicationContext.getAppContext().getBean("sysUserInfoDao");
			//SysUserGroupDao grouDao = (SysUserGroupDao)BaseApplicationContext.getAppContext().getBean("sysUserGroupDao");
			ProjectDao groupDao = (ProjectDao)BaseApplicationContext.getAppContext().getBean("projectDao");
			
			
		/*	str_html = new StringBuffer();
			str_html.append("<table ");
			this.setClassProperties(tableClass, str_html);
			str_html.append("><tr ");
			this.setClassProperties(trClass, str_html);
			str_html.append(">");
			out.print(str_html.toString());*/
			
			/**	** ** ** **部门选择项** ** ** ** **/
			str_html = new StringBuffer();
			str_html.append("<td ");
			this.setStyleClass(lableStyle, labelClass, str_html);
			this.setClassProperties(deptLabelClass, str_html);
			str_html.append(">");
			str_html.append(labelDept+label_colon+"</td><td ");
			this.setStyleClass(selectStyle, selectClass, str_html);
			this.setClassProperties(deptClass, str_html);
			str_html.append(">");
			str_html.append("<select id =\""+deptId+"\" name =\""+deptName+"\" ");
			if(isloadName!=null&&isloadName.equals("true")){
				str_html.append("onchange=\"searchByDept('"+deptId+"','"+userId+"','"+userHeadKey+"','"+userHeadValue+"','"+deptHeadValue+"','"+groupId+"','"+isloadName+"')\">");
			}else{
				str_html.append("onchange=\"searchByDept('"+deptId+"','"+userId+"','"+userHeadKey+"','"+userHeadValue+"','"+deptHeadValue+"','"+groupId+"','"+isloadName+"')\" >");
			}
			if(deptHeadKey!=null){
				str_html.append("<option value=\""+deptHeadValue+"\" >"+deptHeadKey+"</option>");
			}
			for(SysDatadir sd : deptOrder){
				String key = sd.getKey();
				String value = sd.getValue();
				str_html.append("<option value=\""+key+"\"");
				if(value.length()>10){
					str_html.append(" title=\""+value+"\">"+value.substring(0,8)+"...</option>");
				}else{
					str_html.append(">"+value+"</option>");
				}				
			}
			str_html.append("</select>");
			out.print(str_html.toString()+"</td>");
			
			
			/**	** ** ** **小组选择项（当为查询时）** ** ** ** **/
			if(type.equals("query")){
				List<Project> groups = groupDao.listUnFinishedGroup();
				//List<UsrGroup> usrGroups = grouDao.listALlGroup();
				str_html = new StringBuffer();
				str_html.append("<td ");
				this.setStyleClass(lableStyle, labelClass, str_html);
				this.setClassProperties(groupLabelClass, str_html);
				str_html.append(">");
				str_html.append(labelGroup+label_colon+"</td><td ");
				this.setStyleClass(selectStyle, selectClass, str_html);
				this.setClassProperties(groupClass, str_html);
				str_html.append(">");
				str_html.append("<select id =\""+groupId+"\" name =\""+groupName+"\" ");
				str_html.append("onchange=\"searchByGroup('"+groupId+"','"+deptId+"','"+userId+"','"+userHeadKey+"','"+userHeadValue+"','"+deptHeadValue+"','"+groupHeadValue+"')\" >");
				if(groupHeadKey!=null){
					str_html.append("<option value=\""+groupHeadValue+"\">"+groupHeadKey+"</option>");
				}
				for(Project group:groups){
//					str_html.append("<option value=\""+group.getCode()+"\">"+group.getName()+"</option>");
					str_html.append("<option value=\""+group.getId()+"\"");
					if(group.getName().length()>10){
						str_html.append(" title=\""+group.getName()+"\">"+group.getName().substring(0,8)+"...</option>");
					}else{
						str_html.append(">"+group.getName()+"</option>");
					}
				}
				str_html.append("</select>");
				out.print(str_html.toString()+"</td>");
			}
			
			/**	** ** ** **姓名选择项**	** ** ** **/
			str_html = new StringBuffer();
			str_html.append("<td ");
			this.setStyleClass(lableStyle, labelClass, str_html);
			this.setClassProperties(userLabelClass, str_html);
			str_html.append(">");
			str_html.append(labelUser+label_colon+"</td><td ");
			this.setStyleClass(selectStyle, selectClass, str_html);
			this.setClassProperties(userClass, str_html);
			str_html.append(">");
			str_html.append("<select style=\"width:124px;height:22px;display:none\" id =\"id"+userId+"\" name =\"name"+userName+"\"");
			if(!type.equals("query")){
				str_html.append("onchange=\"searchByName('"+userId+"','"+groupId+"')\"");
			}else{
				str_html.append("onchange=\"javascript:document.getElementById('"+userId+"').value = this.value;\"");
			}
			str_html.append(">");
			if(userHeadKey!=null){
				str_html.append("<option value=\""+userHeadValue+"\">"+userHeadKey+"</option>");
			}
			List<Object[]> list = null;
			if(isloadName!=null&&isloadName.equals("true")){
				list= userDao.getNamesByDept("全选","全选");
				for(Object[] sysusr:list){
					str_html.append("<option value=\""+sysusr[1].toString()+"\" title=\""+sysusr[1].toString()+"\">"+sysusr[0].toString()+"</option>");
				}
			}
			str_html.append("</select>");
			
			str_html.append("<input style=\"width:124px;height:22px\" id =\""+userId+"\" name =\""+userName+"\"/>");
			out.print(str_html.toString()+"</td>");
			
			if(!type.equals("query")){
				/**	** ** ** ** 小组只读**	** ** ** **/
				str_html = new StringBuffer();
				str_html.append("<td ");
				this.setStyleClass(lableStyle, labelClass, str_html);
				this.setClassProperties(groupLabelClass, str_html);
				str_html.append(">");
				str_html.append(labelGroup+label_colon+"</td><td ");
				this.setStyleClass(selectStyle, selectClass, str_html);
				this.setClassProperties(groupClass, str_html);
				System.out.println(str_html);
				str_html.append(">");
				str_html.append("<label  id=\""+groupId+"label\" name=\""+groupName+"label\" value=\"\"></label>");
				str_html.append("<input type=\"hidden\" id=\""+groupId+"\" name=\""+groupName+"\" value=\"\">");
				out.print(str_html.toString()+"</td>");
			}
			
			/*
			str_html = new StringBuffer();
			str_html.append("<script type=\"text/javascript\"> ");
			str_html.append("function replacetoggle(id,name){");
			//str_html.append("if(document.getElementById(id).tagName.toUpperCase()=='SELECT'){");
			
			str_html.append("alert(document.getElementsByName(name).length+','+document.getElementsByName('name'+name).length);");
			str_html.append("var selecttag = document.getElementById(id);");
			str_html.append("var inputtag = document.getElementById('id'+id);");
			
			str_html.append("alert(selecttag.name+','+selecttag.tagName+',,'+inputtag.name+','+inputtag.tagName);");
			
			str_html.append("selecttag.setAttribute('name',name+'select');selecttag.setAttribute('id',id+'select');");
			
			str_html.append("alert(selecttag.name+','+selecttag.tagName+',,'+inputtag.name+','+inputtag.tagName);");
			
			str_html.append("inputtag.setAttribute('name',name);inputtag.setAttribute('id',id);");
			
			str_html.append("alert(selecttag.name+','+selecttag.tagName+',,'+inputtag.name+','+inputtag.tagName);");
			
			str_html.append("selecttag.setAttribute('name','name'+name);selecttag.setAttribute('id','id'+id);");
			
			str_html.append("alert(selecttag.name+','+selecttag.tagName+',,'+inputtag.name+','+inputtag.tagName);");
			
			//str_html.append("}");
			str_html.append("document.getElementById(id).style.display=\"block\";");
			str_html.append("document.getElementById('id'+id).style.display=\"none\";");
			str_html.append("}");
			str_html.append("</script>");
			out.print(str_html.toString()+"&nbsp;&nbsp;&nbsp;&nbsp;");
			*/
			/*str_html = new StringBuffer();
			str_html.append("</tr> ");
			str_html.append("</table>");
			out.print(str_html.toString());*/
			
		/*	str_html = new StringBuffer();
			str_html.append("<script type=\"text/javascript\"> ");
			str_html.append("alert(\"kaishi\");");
			str_html.append("function searchName(){");
			str_html.append("alert(\"fangfa\");");
			str_html.append("var url=\"<%=request.getContextPath()%>/pages/um/sysUserInfo!queryNames.action\";");
			str_html.append("var params={deptName:$(\"#"+deptId+"\").val().trim()};");
			str_html.append("jQuery.post(url, params, $(document).call, \"json\");");
			str_html.append("}");
			str_html.append("$.fn.call=function (json){");
			str_html.append("alert(json);");
			str_html.append("$(\"#"+userId+" option\").remove();");
			if(userHeadKey!=null){
				str_html.append("$(\"#"+userId+"\").append(\"<option value=\""+userHeadValue+"\">"+userHeadKey+"</option>\");");
			}
			str_html.append("if(json!=null&&json.length>0){");
			str_html.append("for(var i=0;i<json.length;i++){");
			str_html.append("$(\"#"+userId+"\").append(\"<option value=\"\"+json[i][1]+\"\">\"+json[i][0]+\"(\"+json[i][1]+\")</option>\");");
			str_html.append("}");
			str_html.append("}");
			str_html.append("document.getElementById(\""+userId+"\").focus();");
			str_html.append("document.getElementById(\""+userId+"\").value = \""+userHeadKey+"\";");
			str_html.append("}");
			str_html.append("</script>");
			out.print(str_html.toString()+"&nbsp;&nbsp;&nbsp;&nbsp;");*/
		} catch (Exception e) {
			throw new JspTagException("IOException" + e.toString());
		}
		return super.doEndTag();
	}
	
	//设置属性默认值
	private void setDefaultValue() throws JspException{
		if(labelDept==null||labelDept.equals("null")||labelDept.equals("")){
			labelDept  = TagResources.message(pageContext, "label.deptName");
		}
		if(labelUser==null||labelUser.equals("null")||labelUser.equals("")){
			labelUser  = TagResources.message(pageContext, "label.userName");
		}
		if(labelGroup==null||labelGroup.equals("null")||labelGroup.equals("")){
			labelGroup  = TagResources.message(pageContext, "lable.projectName");
		}
		if(type==null||labelDept.equals("null")||labelDept.equals("")){
			type = "query";
		}
		if(deptHeadValue==null||deptHeadValue.equals("null")){
			deptHeadValue = "全选";
		}
		if(groupHeadValue==null||groupHeadValue.equals("null")){
			groupHeadValue = "全选";
		}
		if(deptName==null||deptName.equals("null")||deptName.equals("")){
			deptName = deptId;
		}
		if(groupName==null||groupName.equals("null")||groupName.equals("")){
			groupName = groupId;
		}
		if(userName==null||userName.equals("null")||userName.equals("")){
			userName = userId;
		}
	}
	
	private void setStyleClass(String style,String tldclass,StringBuffer str){
		if(style!=null){
			str.append(" style=\""+style+"\" ");
		}
		if(tldclass!=null){
			str.append(" class=\""+tldclass+"\" ");
		}
	}
	
	private void setClassProperties(String tldclass,StringBuffer str){
		if(tldclass!=null){
			String[] classProperties = tldclass.trim().split(";");
			for(String property :classProperties){
				String[] arrt = property.trim().split(":");
				str.append(" "+arrt[0]+"=\""+arrt[1]+"\" ");
			}
		}
	}
	
	
	public int doStartTag() throws JspTagException {
		return EVAL_BODY_INCLUDE;

	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getDeptHeadKey() {
		return deptHeadKey;
	}

	public void setDeptHeadKey(String deptHeadKey) {
		this.deptHeadKey = deptHeadKey;
	}

	public String getUserHeadKey() {
		return userHeadKey;
	}

	public void setUserHeadKey(String userHeadKey) {
		this.userHeadKey = userHeadKey;
	}

	public String getDeptHeadValue() {
		return deptHeadValue;
	}

	public void setDeptHeadValue(String deptHeadValue) {
		this.deptHeadValue = deptHeadValue;
	}

	public String getUserHeadValue() {
		return userHeadValue;
	}

	public void setUserHeadValue(String userHeadValue) {
		this.userHeadValue = userHeadValue;
	}

	public String getIsloadName() {
		return isloadName;
	}

	public void setIsloadName(String isloadName) {
		this.isloadName = isloadName;
	}

	public String getLableStyle() {
		return lableStyle;
	}

	public void setLableStyle(String lableStyle) {
		this.lableStyle = lableStyle;
	}

	public String getSelectStyle() {
		return selectStyle;
	}

	public void setSelectStyle(String selectStyle) {
		this.selectStyle = selectStyle;
	}

	public String getLabelClass() {
		return labelClass;
	}

	public void setLabelClass(String labelClass) {
		this.labelClass = labelClass;
	}

	public String getSelectClass() {
		return selectClass;
	}

	public void setSelectClass(String selectClass) {
		this.selectClass = selectClass;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getGroupHeadKey() {
		return groupHeadKey;
	}

	public void setGroupHeadKey(String groupHeadKey) {
		this.groupHeadKey = groupHeadKey;
	}

	public String getGroupHeadValue() {
		return groupHeadValue;
	}

	public void setGroupHeadValue(String groupHeadValue) {
		this.groupHeadValue = groupHeadValue;
	}

	public String getLabelDept() {
		return labelDept;
	}

	public void setLabelDept(String labelDept) {
		this.labelDept = labelDept;
	}

	public String getLabelGroup() {
		return labelGroup;
	}

	public void setLabelGroup(String labelGroup) {
		this.labelGroup = labelGroup;
	}

	public String getLabelUser() {
		return labelUser;
	}

	public void setLabelUser(String labelUser) {
		this.labelUser = labelUser;
	}

	public String getDeptLabelClass() {
		return deptLabelClass;
	}

	public void setDeptLabelClass(String deptLabelClass) {
		this.deptLabelClass = deptLabelClass;
	}

	public String getDeptClass() {
		return deptClass;
	}

	public void setDeptClass(String deptClass) {
		this.deptClass = deptClass;
	}

	public String getGroupLabelClass() {
		return groupLabelClass;
	}

	public void setGroupLabelClass(String groupLabelClass) {
		this.groupLabelClass = groupLabelClass;
	}

	public String getGroupClass() {
		return groupClass;
	}

	public void setGroupClass(String groupClass) {
		this.groupClass = groupClass;
	}

	public String getUserLabelClass() {
		return userLabelClass;
	}

	public void setUserLabelClass(String userLabelClass) {
		this.userLabelClass = userLabelClass;
	}

	public String getUserClass() {
		return userClass;
	}

	public void setUserClass(String userClass) {
		this.userClass = userClass;
	}

	public String getTrClass() {
		return trClass;
	}

	public void setTrClass(String trClass) {
		this.trClass = trClass;
	}

	public String getTableClass() {
		return tableClass;
	}

	public void setTableClass(String tableClass) {
		this.tableClass = tableClass;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
}
