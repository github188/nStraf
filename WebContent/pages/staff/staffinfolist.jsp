<%@page import="java.text.SimpleDateFormat"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa" %>
<%@ page  import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<html>
<head>
<title>operation info</title>
<meta http-equiv="Cache-Control" content="no-store"/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">   
<link href="../../css/css_v2.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../js/customTableSort.js"></script>
<script type="text/javascript" src="../../calendar/fixDate.js"></script>
<script type="text/javascript" src="../../js/aa.js"></script>
<%-- <script type="text/javascript" src="../../js/jquery.js"></script> --%>
<%@ include file="/inc/pagination.inc"%>
<%@ include file="/inc/deptSelect.inc"%>
<script type="text/javascript"> 
function query()
{
	var pageNum = document.getElementById("pageNum").value;
	if(pageNum.indexOf(".")!=-1){
		pageNum = 1;
	}
	var userid =  document.getElementById("username").value;
	var deptNo =  document.getElementById("deptNo").value;
	var grpCode =  document.getElementById("grpCode").value;
	var status =  document.getElementById("status").value;
	var regulation =  $($activatedCSBtn).attr("name");
	var orderField =  $($activatedCSBtn).attr("orderField");
	var actionUrl = "<%=request.getContextPath()%>/pages/staff/staffInfo!refresh.action?from=refresh&pageNum="+pageNum+"&userid="+userid+"&deptNo="+deptNo+"&grpCode="+grpCode+"&status="+status+"&regulation="+ regulation +"&orderField="+orderField;
	actionUrl = encodeURI(actionUrl);
	var method="setHTML";
	k=0;
	<%int k = 0 ; %>
	//执行ajax请求,其中method是产生显示list信息的html的方法的名字，该方法是需要在自己的页面中单独实现的,且方法的签名必须是methodName(entry,entryInfo)
	sendAjaxRequest(actionUrl,method,pageNum,true);
}


//构建显示dataList的HTML
var k=0;
function setHTML(entry,entryInfo)
{	
	var html = "";
	    html+= '<tr id="'+entryInfo['userid']+'" class="trClass<%=k%2%>" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)> ';
		html+= '<td align="center"><input type="checkbox" name="userid" value="'+entryInfo['userid']+'"/></td>';
		html+= '<td width="10%" title='+entryInfo['username']+'('+entryInfo['userid']+')>';
   		html+= '<div align="left" id="userid">';
   		if(entryInfo['userid']=='<%=(String)request.getAttribute("loginUserId")%>'){
    		html+= '<a href="javascript:showSysUserInfo('+"'"+entryInfo['userid']+"'"+')"><font color="#3366FF">'+entryInfo['username']+'('+entryInfo['userid']+')</font></a>';
   		}else{
    		html+= (entryInfo['username']+'('+entryInfo['userid']+')');   			
   		}
   		html+= '</div>';
 		html+= '</td>';
		html+= '<td width="20%" title='+entryInfo["deptName"]+'>';
 		html+= '<div align="left">'+entryInfo["deptName"]+'</div>';
		html+= '</td>';
 		html+= '<td width="40%" align="left" title=' + entryInfo['groupName'] + '>' +entryInfo['groupName']+'</td>' ; 
 		html+= '<td width="40%" align="left" title=' + entryInfo['userGroupName'][k] + '>' +entryInfo['userGroupName'][k]+'</td>' ; 
 		html+= '<td width="40%" align="center" title=' + entryInfo['postLevel'] + '>' +entryInfo['postLevel']+'</td>' ; 
  		html+= '<td width="10%"><div align="left">'+entryInfo['grgBegindate'].substring(0,10)+'</div></td>';
 		html+= '<td width="40%" align="center" title=' + entryInfo['workYear'][k] + '>' +entryInfo['workYear'][k]+'</td>' ; 
 		html+= '<td width="40%" align="left" title=' + entryInfo['birthDate'] + '>' +entryInfo['birthDate']+'</td>' ; 
 		html+= '<td width="40%" align="left" title=' + entryInfo['graduateDate'] + '>' +entryInfo['graduateDate']+'</td>' ; 
 		html+= '<td width="40%" align="center" title=' + entryInfo['education'] + '>' +entryInfo['education']+'</td>' ; 
 		html+= '<td width="10%">';
		html+= '<div align="center">';
		
		if(entryInfo['isvalid']=='Y')   
   			html+= '<img src="../../images/share/button_ok.gif">';	
   		else
   		    html+= '<img src="../../images/share/mood9.gif">';    
   		     	        
  		html+= '</div>';
  		html+= '</td>';
 		//html+= '<td width="40%" align="left" title=' + entryInfo['email'] + '>' +entryInfo['email']+'</td>' ; 
 		//html+= '<td width="40%" align="left" title=' + entryInfo['mobile'] + '>' +entryInfo['mobile']+'</td>' ; 
 		html+= '<td width="40%" align="left" title=' + entryInfo['statusList'][k] + '>' +entryInfo['statusList'][k]+'</td>' ;
  		html+= '<td width="10%" title='+entryInfo['updateTime'] + '><div align="left">'+entryInfo['updateTime']+'</div></td>';
		html+= '</tr>' ;
 		<% k++;%>
 		k++;
	return html;
}

//添加
function add(){
	 var strUrl="/pages/staff/staffInfo!addpage.action";
	 var features="1000,500,staff.list.add,staffManager";
	 var resultvalue =OpenModal(strUrl,features);	
	 refreshList();
}

//获取所选复选框
function  GetSelIds()
{
    var idList="";
	//var  em= document.all.tags("input");
	var  em= document.getElementsByName("userid");
	for(var  i=0;i<em.length;i++)
	{
	   if(em[i].type=="checkbox")
	   {
		   if(em[i].checked){
			   idList+=","+em[i].value.split(",")[0];
		   }
	   }
	}
	
	 if(idList=="")
	    return "";
	 return idList.substring(1);
}
//全选
function  SelAll(chkAll)
{
     //var   em=document.all.tags("input");
      var  em= document.getElementsByName("userid");
	   for(var  i=0;i<em.length;i++)
	   {
	       if(em[i].type=="checkbox")
		   {
		       em[i].checked=chkAll.checked;
		   }
	   }
}

//删除
function del()
{	
   var idList=GetSelIds();  
   if(idList=="")
   {   
	   alert("请选择要删除的员工");
	   return false;
   }
 	if (confirm('确定要删除所选用户吗？'))
    {
	   // window.open(strUrl);
	 var strUrl="/pages/staff/staffInfo!delete.action?userids="+idList;
       var returnValue=OpenModal(strUrl,"600,380,staff.list.delete,staffManager");
   
       refreshList();
    }
}

//用户所属组
function refreshUsrGrp(){
	 var idList = GetSelIds();
	 var ids = idList.split(',');
	 if(idList==""){
		 alert('请选择需要设置的用户组的用户');
		 return false;
	 }
	 if(ids.length>1){
		 alert('每次只能对一个用户进行用户组的设置');
		 return false;
	 }
	 var strUrl="/pages/um/sysUserGroup!findStaffGroupList.action?userid="+idList;	
	 var features="600,450,staff.list.grpname,staffManager";
	 var resultvalue = OpenModal(strUrl,features);
	 refreshList();
}

//修改
function modify(){
	var idList = GetSelIds();
	 var ids = idList.split(',');
	 if(idList==""){
		 alert('请选择需要修改的用户！');
		 return false;
	 }
	 if(ids.length>1){
		 alert('一次只能修改一个用户的信息');
		 return false;
	 }
	 var strUrl="/pages/staff/staffInfo!modifyPage.action?userid="+idList;
		var features="1000,500,staff.list.modify,staffManager";
		var resultvalue = OpenModal(strUrl,features);

		refreshList();

}
//明细按钮响应函数
function showDetail(){
	var idList = GetSelIds();
	 var ids = idList.split(',');
	 if(idList==""){
		 alert('请选择需要查看的用户！');
		 return false;
	 }
	 if(ids.length>1){
		 alert('一次只能查看一个用户的信息');
		 return false;
	 }
	var strUrl="/pages/staff/staffInfo!view.action?userid="+ids;
	var features="1000,500,staff.list.detail,staffManager";
	var resultvalue = OpenModal(strUrl,features);
}
	
//点击超链接查看详情
function showSysUserInfo(userid){
	var strUrl="/pages/staff/staffInfo!view.action?userid="+userid;
	var features="1000,500,staff.list.detail,staffManager";
	var resultvalue = OpenModal(strUrl,features);
}

//密码重置
function resetPassWd(){
	var idList = GetSelIds();
	 var ids = idList.split(',');
	 if(idList==""){
		 alert('请选择一个需要重置密码的用户');
		 return false;
	 }
	 if(ids.length>1){
		 alert('一次只能重置一个用户的密码');
		 return false;
	 }
	 if (confirm("确定要重置该用户的密码吗？")){ 		
			var strUrl="/pages/staff/staffInfo!resetPW.action?userid="+idList;
			var features="500,350,staff.list.resetpwd,staffManager";
			var resultvalue = OpenModal(strUrl,features);
			refreshList();
		}
}

//解锁
function resetLock()
{
	var idList = GetSelIds();
	 var ids = idList.split(',');
	 if(idList==""){
		 alert('请选择一个需要解锁的用户！');
		 return false;
	 }
	 if(ids.length>1){
		 alert('一次只能为一个用户解锁！');
		 return false;
	 }
	 if (confirm("确定要为该用户解锁吗?")){ 		
			var strUrl="/pages/staff/staffInfo!resetUser.action?userid="+idList;
			var features="500,350,staff.list.locked,staffManager";
			var resultvalue = OpenModal(strUrl,features);
			refreshList();
	 }
}
//加载时限制下拉列表宽度,主要是针对名称过长时出现的问题
$(function(){
	$("#grpCode").attr("style","width:100%");	
})
</script>
</head>
<body id="bodyid" leftmargin="0" topmargin="0">
<s:form name="sysUserForm" action="staffInfo!list.action"  namespace="/pages/staff" method="post" >
    
    <!-- 如果页面中用到了分页标签，请记得加上下面这两个隐藏标签 -->
	<input type="hidden" name="pageNum" id="pageNum"  value="1"/>
	<input type="hidden" name="pageSize" id="pageSize" value="20" />
  
  <%@include file="/inc/navigationBar.inc"%>
		<input type="hidden" name="menuid" value="<%=request.getParameter("menuid")%>">
        	<table width="100%" cellSpacing="0" cellPadding="0"> 
                   <tr>
                       <td width="55%">
						 <table width="100%" class="select_area">
							  <tr>
							    <tm:deptSelect 
								deptId="deptNo" 
								deptName="deptNo"
								groupId="grpCode"
								groupName="grpCode"
								userId="username"
								userName="username" 
								isloadName="false" 
								deptHeadKey="---请选择部门---" 
								deptHeadValue="" 
								userHeadKey="----请选择人员----" 
								userHeadValue=""  
								groupHeadKey="---请选择项目名称---"
								groupHeadValue=""
								labelDept="部门 :" 
								labelGroup="项目名称:" 
								labelUser="姓名 :" 
								deptLabelClass=" width:5%;class:input_label;"
								deptClass="align:left;width:12%;" 
								groupLabelClass=" width:5%;class:input_label;"
								groupClass="align:left;width:15%;" 
								userLabelClass=" width:5%;class:input_label;"
								userClass="align:left;width:15%;" 
								>
							</tm:deptSelect>
							</tr>
							</table>
							</td>
                     		 <td class="input_label"><s:text
										name="staff.add.status" />：</td>
	                     	<td>
	                     		<select  style="width: 90px" id="status">
								<option value=""><s:text name="staff.add.option" /></option>
								<s:iterator value="#request.statusMap" >
									<option value="<s:property value='key'/>"><s:property
											value="value" /></option>
								</s:iterator>
								</select>
							</td>
							<td width="13%" >&nbsp;&nbsp;<tm:button site="1"/></td>
                    </tr>
			</table>
			
			<br>

			<table width="100%" border="0" cellspacing="0" cellpadding="0" class="bgbuttonselect">
			  <tr>
			    <td width="25" height="23" vAlign="middle"><td class="orarowhead"><s:text name="operInfo.title"/></td>
			    <td style="margin-left: 10px;"><input class="customSortBtn"  type="button" name="asc" value="部门" orderField="deptName" actionUrl="<%=request.getContextPath()%>/pages/staff/staffInfo!refresh.action" onclick="k=0" /></td>
			    <td style="margin-left: 10px;"><input class="customSortBtn" type="button"  value="更新时间" orderField="updateTime" actionUrl="<%=request.getContextPath()%>/pages/staff/staffInfo!refresh.action" onclick="k=0" /></td>
			    <td	align="right" width="75%">
			       <tm:button site="2"></tm:button>
			    </td>
			  </tr>
			</table>

			

		  <table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066" id="downloadList">
		    <tr> 
		      <td width="2%" nowrap class="oracolumncenterheader"></td>
		      <td nowrap  class="oracolumncenterheader"><div align="center"><s:text name="staff.add.username"/></div></td>
		      <td nowrap class="oracolumncenterheader"><div align="center"><s:text name="staff.add.deptName"/></div></td>
		      <td nowrap class="oracolumncenterheader"><s:text name="lable.projectName" /></td>
		      <td nowrap class="oracolumncenterheader">权限组</td>
		      <td nowrap  class="oracolumncenterheader"><s:text name="staff.add.postLevel" /></td>
		      <td nowrap  class="oracolumncenterheader"><div align="center"><s:text name="staff.add.grgBegindate"/></div></td>
		      <td nowrap  class="oracolumncenterheader"><div align="center">运通/<s:text name="staff.sumworkage"/></div></td>
		      <td nowrap class="oracolumncenterheader"><div align="center"><s:text name="staff.add.birthDate"/></div></td>
		      <td nowrap class="oracolumncenterheader"><div align="center"><s:text name="staff.add.graduateDate"/></div></td>
		      <td nowrap  class="oracolumncenterheader"><div align="center"><s:text name="staff.add.education"/></div></td>
		      <td nowrap class="oracolumncenterheader"><div align="center">登录许可</div></td>
		      <!-- <td nowrap class="oracolumncenterheader"><div align="center"><s:text name="staff.add.email"/></div></td>
		      <td nowrap  class="oracolumncenterheader"><div align="center"><s:text name="form.Tel"/></div></td>
		       -->
		       <td nowrap  class="oracolumncenterheader"><div align="center"><s:text name="staff.add.status"/></div></td>
		       <td nowrap  class="oracolumncenterheader"><div align="center">更新时间</div></td>
		    </tr>
		    <%
		    	List groupNameList=(ArrayList)request.getAttribute("groupNameList");
		    	List userGroupNameList=(ArrayList)request.getAttribute("userGroupNameList");
		    	List workYearList=(ArrayList)request.getAttribute("workYearList");
		    	List statusList=(ArrayList)request.getAttribute("statusList");
		    	int i=0 ;
		    	int index = 0;
		     %>
		     
		    <tbody name="formlist" id="formlist">   
			    <s:iterator value="userList" id="user">      
			    <tr id='tr<s:property value="userid"/>' class="trClass<%=index%2 %>"  oriClass="" onMouseOut="TrMove(this)" onMouseOver="TrMove(this)"> 
			    <td align="left"><input type="checkbox" name="userid" value='<s:property value="userid"/>,<s:property value="orgLevel"/>'/></td>
			    <td width="10%" title='<s:property value="username"/>(<s:property value="userid"/>)'>
				    <div align="left" style="white-space: nowrap;word-break: keep-all" >
				    	<s:if test="userid==#request.loginUserId">
			    			 <div  id="username">
		    					<s:property value="username"/>
					    	(<a href='javascript:showSysUserInfo("<s:property value="userid"/>")'>
					    		<font color="#3366FF" style="font-size: 10px"><s:property value="userid"/></font>
					     	</a>)
		    				 </div>
				    	</s:if>
				    	<s:else>
				    		<div id="username">
		    					<s:property value="username"/>
				    		(<font  style="font-size: 10px"><s:property value="userid"/></font>)
		    				 </div>
			    		</s:else>
				    </div>
			    </td>
			    <td title='<tm:dataKeyValue beanName="user" property="deptName"  path="staffManager.department"/>'>
				    <div align="left" id="deptName">
				    	<tm:dataKeyValue beanName="user" property="deptName"  path="staffManager.department"/> 
				    </div>
			    </td>
			     <td width="40%" align="left" title="<%=(String)groupNameList.get(i) %>"><%=(String)groupNameList.get(i)%></td>		  
			     <td width="40%" align="left" title="<%=(String)userGroupNameList.get(i)%>" ><%=(String)userGroupNameList.get(i)%></td>		  
			    <td title='<s:property value="postLevel"/>'>
				    <div align="center" id="postLevel">
				    	<s:property value="postLevel"/>
				    </div>
			    </td>
			     <td title='<s:date name="grgBegindate"  format="yyyy-MM-dd"/>'><div align="left"><s:date name="grgBegindate"  format="yyyy-MM-dd"/></div></td>
			    <!-- 运通工龄 -->
			    <td width="40%" align="center" title="<%=(String)workYearList.get(i)%>" ><%=(String)workYearList.get(i)%></td>		  
			     <td title='<s:date name="birthDate"  format="yyyy-MM-dd"/>'><div align="left"><s:date name="birthDate"  format="yyyy-MM-dd"/></div></td>
			     <td title='<s:date name="graduateDate"  format="yyyy-MM-dd"/>'><div align="left"><s:date name="graduateDate"  format="yyyy-MM-dd"/></div></td>
			     <td title='<s:property value="education"/> '>
				    <s:property value="education"/> 
			     </td>
			     <td width="10%">
				     <div align="center">     
					     <s:if test='isvalid=="Y"'>
					           <img src="../../images/share/button_ok.gif">
					     </s:if>     
					     <s:else>
					           <img src="../../images/share/mood9.gif">
					     </s:else>  
				     </div>
			     </td>
			     <!-- <td title='<s:property value="email"/>'>
				    <div align="left" id="email">
				    	<s:property value="email"/>
				    </div>
			    </td>
			     <td title=' <s:property value="mobile"/>'>
				    <div align="left" id="mobile">
					    <s:property value="mobile"/>
				    </div>
			    </td> -->
			    <td title="<%=(String)statusList.get(i) %>"><%=(String)statusList.get(i)%></td>
			    <td title='<s:date name="updateTime"  format="yyyy-MM-dd"/>'><div align="left"><s:date name="updateTime"  format="yyyy-MM-dd"/></div></td>
			    </tr>
			    <%index++;i++; %>
			 </s:iterator> 
		    </tbody>
		  </table>
		<table width="100%" border="0" cellpadding="1" cellspacing="1"
			bgcolor="#FFFFFF">
			<tr>
				<td>
					<table width="100%" cellSpacing="0" border="0" cellPadding="0">
						<tr>
							<td width="12%">
								<div align="center">
									<input type="checkbox" name="all" id=chkAll value="all"
										onClick="SelAll(this)">
									<s:text name="operInfo.checkall" />
								</div>

							</td>
							<td width="11%">
								<div align="left">
									<label for="chkAll"></label>
								</div>
							</td>
							<td width="83%" align="right">
								<div id="pagetag">
									<tm:pagetag pageName="currPage" formName="sysUserForm" />
								</div>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</s:form>
</body>
</html>