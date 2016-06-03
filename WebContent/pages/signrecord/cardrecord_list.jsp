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
	<title>考勤数据列表</title>
	<meta http-equiv="Cache-Control" content="no-store"/>   
</head>
<link href="../../css/css_v2.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../calendar/fixDate.js"></script>
<script type="text/javascript" src="../../js/aa.js"></script>
<%@ include file="/inc/deptSelect.inc"%>
<%@ include file="/inc/pagination.inc"%>

<script type="text/javascript"> 
 	var num=1;
 	String.prototype.trim = function(){
		return this.replace(/(^\s*)|(\s*$)/g, "");
	}
 	
	function query() {
		var deptName = $("#queryUserDept option:selected").text();
		var grpName = $("#queryGroup option:selected").text();		
		if($("#queryGroup option:selected").attr("title")){
			grpName = $("#queryGroup option:selected").attr("title");
		}		
		var userName = document.getElementById("idqueryUserName").value;
		var signTime = document.getElementById("signTime").value;
		var signEndTime = document.getElementById("signEndTime").value;
		var pageNum = document.getElementById("pageNum").value;
		if(pageNum.indexOf(".")!=-1){
			pageNum = 1;
		}
		var actionUrl = "<%=request.getContextPath()%>/pages/signrecord/signRecord!listCardAll.action?from=refresh&pageNum="+pageNum;
		if(deptName!='---请选择部门---'){
		actionUrl += "&deptName="+deptName;
		}
		if(grpName!='---请选择项目名称---'){
			actionUrl += "&grpName="+grpName;
		}
		if($("#idqueryUserName").is(":visible")==true&&userName!='----请选择人员----'){
    		actionUrl += "&userId="+userName;
        }else if($("#idqueryUserName").is(":visible")==false){
			actionUrl += "&userId="+$("#queryUserName").val();
		}
		actionUrl += "&signTime="+signTime;
		actionUrl += "&signEndTime="+signEndTime;
		actionUrl = encodeURI(actionUrl,"UTF-8");
		var method = "setHTML";
		<%int k = 0; %>
		sendAjaxRequest(actionUrl,method,pageNum,true);
	}

	function setHTML(entry,entryInfo){
		var html  = '';
		var str = "javascript:show(\""+entryInfo["id"]+"\")";
		html += '<tr id="tr'+entryInfo['id']+'" class="trClass<%=k%2 %>" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>';
		html += '<td align="center"><a href="javascript:showInfo(\'' + entryInfo["id"] + '\')"><font color="#3366FF">'+entryInfo["username"] + '</font></a></td>';
		html += '<td align="center">' +entryInfo["deptname"] + '</td>';
		html += '<td align="center">' +entryInfo["grpname"] + '</td>';
		html += '<td align="center">' +entryInfo["signtime"].substring(0,19) + '</td>';
		html += '<td align="center">' ;
		if(entryInfo["type"]=='0'){
			html += '<font color="red">公司考勤</font>';
		}else if(entryInfo["type"]=='1'){
			html += '<font color="blue">外派导入</font>';
		}else if(entryInfo["type"]=='2'){
			html += '<font color="green">移动签到</font>';
		}else if(entryInfo["type"]=='3'){
			html += '<font color="yellow">补签到</font>';
		}
		html += '</td>';
		html += '<td align="center">' ;
		if(entryInfo["attendanceStatus"]=='0'){
			html += '<font color="blue">不识别</font>';
		}else if(entryInfo["attendanceStatus"]=='1'){
			html += '正常出勤';
		}else if(entryInfo["attendanceStatus"]=='2'){
			html += '正常退勤';
		}else if(entryInfo["attendanceStatus"]=='3'){
			html += '<font color="red">迟到</font>';
		}else if(entryInfo["attendanceStatus"]=='4'){
			html += '<font color="red">早退</font>';
		}
		html += '</td>';
  		html += '</tr>';
        num++;
 		<% k++;%>;     
		return html;
	}
	
	 function showInfo(id){
		 var strUrl="/pages/signrecord/signRecord!view.action?id="+id;
		var features="800,600,signrecord.title_view";	
		var resultvalue =OpenModal(strUrl,features);	
		refreshList();
	 }
</script>

<body id="bodyid" leftmargin="0" topmargin="0">
<s:form name="signRecordInfoForm" action="signRecord!listCardAll.action"  namespace="/pages/signrecord" method="post" >
    
    <!-- 如果页面中用到了分页标签，请记得加上下面这两个隐藏标签 -->
	<input type="hidden" name="pageNum" id="pageNum"  value="1"/>
  	<input type="hidden" name="pageSize" id="pageSize" value="20" />
	<input type="hidden" name="menuid" value="<%=request.getParameter("menuid")%>">
	
	<%@include file="/inc/navigationBar.inc"%>
	
	<table width="100%" cellSpacing="0" cellPadding="0"> 
 		<tr>
 			<td >
					<table width="100%"  class="select_area">
              			<tr>
							<tm:deptSelect 
								deptId="queryUserDept" 
								deptName="signRecord.deptName"
								groupId="queryGroup"
								groupName="signRecord.grpName"
								userId="queryUserName" 
								userName="signRecord.userName"
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
								deptClass="align:left;width:18%;" 
								groupLabelClass="align:right; width:5%;class:input_label"
								groupClass="align:left;width:18%;" 
								userLabelClass="align:right; width:5%;class:input_label"
								userClass="align:left;width:18%;" 
							>
							</tm:deptSelect>
						</tr>
              			<tr>
              			 	<td align="right" width="5%" class="input_label">开始日期:</td> 
                			<td>
                				<input name="signTime" type="text" id="signTime"  
                				class="MyInput" width="18%" size="13" />
                			</td>
                			<td align="right" width="5%" class="input_label">结束日期:</td>
                			<td>
                				<input name="signEndTime" type="text" id="signEndTime"  
                				class="MyInput" size="13" width="18%"/>
                			</td>
                			<td align="center" colspan="2">
                				<tm:button site="1" ></tm:button>
                			</td>
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
		  	<td nowrap width="10%" class="oracolumncenterheader"><div align="center">姓名</div></td>
		  	<td nowrap width="15%" class="oracolumncenterheader"><div align="center">所属部门</div></td>
		  	<td nowrap width="35%" class="oracolumncenterheader"><div align="center">所在项目名称</div></td>
		  	<td nowrap width="20%" class="oracolumncenterheader"><div align="center">签到日期</div></td>
		  	<td nowrap width="10%" class="oracolumncenterheader"><div align="center">考勤类型</div></td>
		  	<td nowrap width="10%" class="oracolumncenterheader"><div align="center">考勤状态</div></td>
	  	</tr>
	  	<tbody name="formlist" id="formlist" align="center">
  		<s:iterator  value="cardlist" id="signRecordInfo" status="row">
  			<s:if test="#row.odd == true"> 
 				<tr id='tr<s:property value="id"/>' class="trClass0" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
 			</s:if>
 			<s:else>
 				<tr id='tr<s:property value="id"/>' class="trClass1" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
 			</s:else> 
			<td align="center">
				<a href='javascript:showInfo("<s:property value="id"/>")'>
					<font color="#3366FF"><s:property value="username"/></font>
				</a>
			</td>
		 	<td align="center"><s:property value="deptname"/></td>
		 	<td align="center"><s:property value="grpname"/></td>
		 	<td align="center"><s:date name="signtime" format="yyyy-MM-dd HH:mm:ss"/></td>
		 	<td align="center">
            	<s:if test="type==0"><font color="red">公司考勤</font></s:if>
            	<s:elseif test="type==1"><font color="blue">外派导入</font></s:elseif>
            	<s:elseif test="type==2"><font color="green">移动签到</font></s:elseif>
            	<s:elseif test="type==3"><font color="yellow">补签到</font></s:elseif>
            </td>
            <td align="center">
            	<s:if test="attendanceStatus==0"><font color="blue">不识别</font></s:if>
            	<s:elseif test="attendanceStatus==1">正常出勤</s:elseif>
            	<s:elseif test="attendanceStatus==2">正常退勤</s:elseif>
            	<s:elseif test="attendanceStatus==3"><font color="red">迟到</font></s:elseif>
            	<s:elseif test="attendanceStatus==4"><font color="red">早退</font></s:elseif>
            </td>
	    </tr>
		</s:iterator>  
 		</tbody> 
 	</table> 
 		<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#FFFFFF">  
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
						<div id="pagetag"><tm:pagetag pageName="currPage" formName="prjContractInfoForm" /></div>
					</td>
				</tr>
			</table>
		</td>
		</tr>
	</table>
</s:form>
</body>
</html>