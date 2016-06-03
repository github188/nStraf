<%@page import="java.util.Date"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@page import="cn.grgbanking.feeltm.login.domain.UserModel"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<html>
<head>
	<title>prjContract info</title>
	<meta http-equiv="Cache-Control" content="no-store"/>   
</head>
<link href="../../css/css_v2.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../calendar/fixDate.js"></script>
<script type="text/javascript" src="../../js/aa.js"></script>
<%@ include file="/inc/deptSelect.inc"%>
<%@ include file="/inc/pagination.inc"%>

<script type="text/javascript"> 
	function query() {
		var pageNum = document.getElementById("pageNum").value;
		if(pageNum.indexOf(".")!=-1){
			pageNum = 1;
		}
		var userid =  document.getElementById("username").value;
		var deptNo =  document.getElementById("deptNo").value;
		var grpCode =  document.getElementById("grpCode").value;
		var status =  document.getElementById("status").value;
		var actionUrl = "<%=request.getContextPath()%>/pages/signrecord/signRecord!bindRefresh.action?form=refresh&pageNum="+pageNum;
		if(deptNo!=''){
		  actionUrl += "&deptname="+deptNo;
		}
		if(grpCode!=''){
			actionUrl += "&grpname="+grpCode;
		}
		if(userid!=''){
			actionUrl += "&userid="+userid;
		}
		if(status!=''){
			actionUrl += "&status="+status;
		}
		actionUrl = encodeURI(actionUrl,"UTF-8");
		var method = "setHTML";
		<%int k = 0; %>
		sendAjaxRequest(actionUrl,method,pageNum,true);
	}

	function setHTML(entry,entryInfo){
		
		var html  = '';
		html += '<tr class="trClass<%=k%2 %>" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>';
		html += '<td>';
		html += '<input type="checkbox" name="chkList" value="' + entryInfo["userid"] + '" />';
		html += '</td>';
		html += '<td align="center" width="10%">'+entryInfo["userid"] + '</td>';
		html += '<td align="center" width="10%">'+entryInfo["username"] + '</td>';
		html += '<td align="center" width="10%">'+entryInfo["deptname"] + '</td>';
		html += '<td align="center" width="20%">'+entryInfo["groupname"] + '</td>';
		html += '<td align="center" width="5%">' +entryInfo["phoneid"] + '</td>';
		html += '<td align="center" width="5%">'+entryInfo["status"] + '</td>';
		html += '<td align="center" width="20%">' +entryInfo["bindTime"].substring(0,19) + '</td>';
		html += '<td align="center" width="20%">' +entryInfo["releaseTime"].substring(0,19) + '</td>';
  		html += '</tr>'; 
 		<% k++;%>;     
		return html;
	}
	
	//获取所选复选框
	function  GetSelIds()
	{
	    var idList="";
		//var  em= document.all.tags("input");
		var  em= document.getElementsByName("chkList");
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
	     var  em= document.getElementsByName("chkList");
		   for(var  i=0;i<em.length;i++)
		   {
		       if(em[i].type=="checkbox")
			   {
			       em[i].checked=chkAll.checked;
			   }
		   }
	}

	
	function release(){
		var idList = GetSelIds();
		 if(idList==""){
			 alert('请选择需要解除绑定的用户！');
			 return false;
		 }
		 if (confirm('确定要解除所选用户的手机绑定吗？'))
		   {
			 var strUrl="/pages/signrecord/signRecord!releaseBind.action?userids="+idList;
			var features="600,380,staff.list.modify,staffManager";
			var resultvalue = OpenModal(strUrl,features);
			refreshList();
		    }
	}

</script>

<body id="bodyid" leftmargin="0" topmargin="0">
  <s:form name="signbindForm" action="signRecord!recordBind.action"  namespace="/pages/signrecord" method="post" >  
    <!-- 如果页面中用到了分页标签，请记得加上下面这两个隐藏标签 -->
	<input type="hidden" name="pageNum" id="pageNum"  value="1"/>
  	<input type="hidden" name="pageSize" id="pageSize" value="20" />
	
	<%@include file="/inc/navigationBar.inc"%>
	
	<table width="100%" cellSpacing="0" cellPadding="0" > 
 		<tr>
 			<td >
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
								deptLabelClass="align:right; width:5%;class:input_label"
								deptClass="align:left;width:12%;" 
								groupLabelClass="align:right; width:5%;class:input_label"
								groupClass="align:left;width:12%;" 
								userLabelClass="align:right; width:5%;class:input_label"
								userClass="align:left;width:15%;" 
								>
							</tm:deptSelect>
								<td>
								<b>状态：</b>
								<select id="status" name="status">
									<option value="">全部</option>
									<option value="绑定">绑定</option>
									<option value="已解绑">已解绑</option>
								</select>
								</td>
							 <td width="15%" align="right"><tm:button site="1"/></td>
							</tr>
					</table>
			</td> 
		</tr>
	</table>
	<br/>
	
	<table width="100%" height="23" border="0" cellspacing="0" cellpadding="0" class="bgbuttonselect">
		<tr>
			<td width="25"  height="23" valign="middle">&nbsp;</td>
			<td class="orarowhead"><s:text name="operInfo.title" /></td>
			<td align="right" width="75%"><tm:button site="2"></tm:button></td>
		</tr>
	</table>
	
	<div id="showdiv" style='display:none;z-index:999; background:white;  height:39px; line-height:50px; width:42px;  position:absolute; top:236px; left:670px;'><img src="../../images/loading.gif"></div>

	
	<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066" id=downloadList>
		<tr> 
			<td nowrap class="oracolumncenterheader" width="2%"></td>
		  	<td nowrap width="8%" class="oracolumncenterheader"><div align="center">用户标识</div></td>
		  	<td nowrap width="8%" class="oracolumncenterheader"><div align="center">姓名</div></td>
		  	<td nowrap width="12%" class="oracolumncenterheader"><div align="center">部门</div></td>
		  	<td nowrap width="18%" class="oracolumncenterheader"><div align="center">项目名称</div></td>
		  	<td nowrap width="15%" class="oracolumncenterheader"><div align="center">手机标识</div></td>
		  	<td nowrap class="oracolumncenterheader"><div align="center">解绑状态</div></td>
		  	<td nowrap width="15%" class="oracolumncenterheader"><div align="center">绑定时间</div></td>
		  	<td nowrap width="15%" class="oracolumncenterheader"><div align="center">解绑时间</div></td>
	  	</tr>
	  	<tbody name="formlist" id="formlist" align="center">
  		<s:iterator  value="bind" status="row">
  		<s:if test="#row.odd == true"> 
 		<tr id="tr" class="trClass0" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
 		</s:if>
 		<s:else>
 		<tr id="tr" class="trClass1" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
 		</s:else> 
			<td nowrap align="center">
				<input type="checkbox" name="chkList" value="<s:property value='userid'/>"/>
			</td>
			<td align="center" width="10%"><s:property value="userid"/></td>
		 	<td align="center" width="10%"><s:property value="username"/></td>
		 	<td align="center" width="10%"><s:property value="deptname"/></td>
		 	<td align="center" width="20%"><s:property value="groupname"/></td>
		 	<td align="center" width="5%"><s:property value="phoneid"/></td>
		 	<td align="center" width="5%"><s:property value="status"/></td>
		 	<td align="center" width="20%"><s:date name="bindTime" format="yyyy-MM-dd HH:mm:ss"/></td>
		 	<td align="center" width="20%"><s:date name="releaseTime" format="yyyy-MM-dd HH:mm:ss"/></td>
	    </tr>
		</s:iterator>  
 		</tbody> 
 	</table>
 		<table width="100%" border="0" cellpadding="1" cellspacing="1"  bgcolor="#FFFFFF">  
	<tr bgcolor="#FFFFFF">
		<td> 
			<table width="100%" cellSpacing="0" cellPadding="0">
				<tr>
					<%-- <td width="6%"> 
						<div align="center"><input type="checkbox" name="chkList"  id="chkAll"   value="all"  onClick="SelAll(this)"></div> 
					</td>
					<td width="11%">
					<div align="left"><label for=chkAll><s:text name="operInfo.checkall" /></label></div>
					</td> --%>
					<td width="83%" align="right">
						<div id="pagetag"><tm:pagetag pageName="currPage" formName="signbindForm" /></div>
					</td>
				</tr>
			</table>
		</td>
		</tr>
	</table>
 	</s:form> 
</body>
</html>