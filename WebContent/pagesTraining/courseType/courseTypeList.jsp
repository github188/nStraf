<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<html>
<head>
<title>dataDirList</title>
</head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script language="javascript" src="../../js/tablesort.js"></script>
<script language="javascript" src="../../calendar/fixDate.js"></script>
<script language="javascript" src="../../js/aa.js"></script>
<%@ include file="/inc/pagination.inc"%>
<script type="text/javascript">

function query(){
	var parentid = document.getElementById("parentid").value;
	var actionUrl = "<%=request.getContextPath()%>/pages/courseType/coursetTypeInfo!refresh?from=refresh&parentid="+parentid;
	var method="setHTML";
	<%int k = 0 ; %>
	//执行ajax请求,其中method是产生显示list信息的html的方法的名字，该方法是需要在自己的页面中单独实现的,且方法的签名必须是methodName(entry,entryInfo)
	sendAjaxRequest(actionUrl,method,"",false, true);
}

//构建显示dataList的HTML
function setHTML(entry,entryInfo){
	var html = "";
	<%if(k%2 == 0) {%>
		html += '<tr class="trClass0"  oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>';
	<%}else {%>
		html += '<tr class="trClass1"  oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>';
	<%}%>  
	html += '<td nowrap width="5%">'; 
	html += '<div align="center">'; 
	html += '<input type="checkbox" name="chkList" value="' + entryInfo["id"] + '" />';
	html += '</div></td>';
 	html += '<td nowrap width="10%">'; 
  	html += '<div align="center">';
  	html += '<img src="../../images/share/foldericon.gif" style="CURSOR: hand" onclick="ModifyItem(\'' + entryInfo["id"] + '\');"/>';
	html += '</div></td>';
	html += '<td nowrap width="15%">'; 
	html += '<div align="left"><a href=javascript:ComeIn("' + entryInfo["id"] + '")><font color="#3366FF">';
	html += entryInfo["courseType"] + '</font></a></div></td>';
	var courseDesc = entryInfo["courseDesc"];
	html += '<td nowrap width="70%" title="'+courseDesc+'">'; 
	if(courseDesc.length>50){
		courseDesc=courseDesc.substring(0,50)+"...";
	}
	html += '<div align="left">' + courseDesc + '</div></td>';
	html += '</tr>';
	<% k++;%>
	return html;
}
    
function SelAll(chkAll){
	var chkSet = document.getElementsByName("chkList");
	for(var i=0;i<chkSet.length;i++){
		if(chkSet[i].name=="chkList"){
			chkSet[i].checked=chkAll.checked
		}   
	}
}
function  ComeIn(itemId){
	var strUrl="<%=request.getContextPath()%>/pages/courseType/coursetTypeInfo!list.action?parentid="+itemId+"&menuid=<%=request.getParameter("menuid")%>";
	window.location.href=strUrl
}
function  ComeBack(itemId){
	var strUrl="<%=request.getContextPath()%>/pages/courseType/coursetTypeInfo!list?action=back&id="+itemId+"&menuid=<%=request.getParameter("menuid")%>";
	window.location.href=strUrl
}

function  GetSelIds(){
	var idList=""
	var chkSet = document.getElementsByName("chkList");
	for(var i=0;i<chkSet.length;i++){
		if(chkSet[i].name=="chkList"){
			if(chkSet[i].checked){
				idList+=","+chkSet[i].value
			}    
		}   
	}
	if(idList=="")
		return ""
	return idList.substring(1)
}
function  add(){
  	var parentid = document.getElementById("parentid").value;
  	var strUrl="/pages/courseType/coursetTypeInfo!add.action?parentid="+parentid;
  	var returnValue=OpenModal(strUrl,"600,400,dataDir.title.add,datadir")
  	refreshList();  
}

function modify(){
	var aa=document.getElementsByName("chkList");
	   var itemId
	   var j=0;
	   for (var i=0; i<aa.length; i++){
	   		if (aa[i].checked){
				itemId=aa[i].value;
				j=j+1;
			}
	   }
		if (j==0)
	 		alert('<s:text name="operator.update" />')
	 	else if (j>1)
	 		alert('<s:text name="operator.updateone" />')
		else{
	  		var strUrl="/pages/courseType/coursetTypeInfo!modify.action?id="+itemId;
	  		var returnValue=OpenModal(strUrl,"600,380,dataDir.title.modify,datadir")
      		refreshList();
		}
}

function ModifyItem(itemId){
  	var strUrl="/pages/courseType/coursetTypeInfo!modify.action?id="+itemId;
  	var returnValue=OpenModal(strUrl,"600,400,dataDir.title.modify,datadir");
  	refreshList();
}
function del(){
   	var idList=GetSelIds();
   	if(idList==""){
       	alert('<s:text name="del.selectNone"/>')
	   	return false
   	}
   	if(!confirm('<s:text name="dataDir.del.confirm"/>')){
      	return false
   	}
  	var strUrl="/pages/courseType/coursetTypeInfo!delselected.action?id="+idList
  	var returnValue=OpenModal(strUrl,"520,380,dataDir.title.del,datadir")
  	refreshList();
}
</script>
<body id="bodyid" leftmargin="0" topmargin="0">
	<s:iterator value="sysDataDir">
		<form name="dataDirListForm" method="post"
			action="<%=request.getContextPath()%>/pages/courseType/coursetTypeInfo!list.action">
			<!-- 将dataDirListAction中传过来的menuid和parentid值存起来 -->
			<input type="hidden" name="menuid" id="menuid"
				value="<%=request.getParameter("menuid")%>"> <input
				type="hidden" name="parentid" id="parentid"
				value="<s:property value="parentid"/>">
			<aa:zone name="formlist1">
				<table width="100%" height="20" border="0" cellspacing="0"
					cellpadding="0" background="../../images/main/bgtop.gif">
					<tr>
						<td width="25" valign="middle">&nbsp;<img
							src="../../images/share/list.gif" width="14" height="16"></td>
						<td class="orarowhead">
							<!--当前位置--> <s:text name="dataDir.navigation" />
							<s:text name="label.colon" /> <s:property value="dirNavigation" />
						</td>
						<td width="50%" align="right"><tm:button site="2"></tm:button>
						</td>
					</tr>
				</table>
				<table width="100%" border="0" cellpadding="1" cellspacing="1"
					bgcolor="#000066">
					<tr bgcolor="#FFFFFF">
						<td>
							<table width="100%" cellSpacing="0" cellPadding="0" border="1">
								<tr>
									<td width="100%">
										<table width="100%" border="0" cellspacing="0" cellpadding="0">
											<tbody id="recordCount">
												<tr>
													<!--共有*条记录-->
													<td height="22"><s:text name="page.nav.total" />
														<bean:size id="iTotal" name="dirList" /><%=iTotal%></td>
													<td width="21%" height="22" valign="middle">
														<div align="center"></div>
													</td>
													<td height="22">&nbsp;&nbsp;</td>
												</tr>
											</tbody>
										</table>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>

				<table width="100%" border="0" cellpadding="1" cellspacing="1"
					bgcolor="#000066" id=tabDirList border="1">
					<tr>
						<td width="5%" class="oracolumncenterheader">
							<div align="center"><input type="checkbox" name="chkAll" id="chkAll" value="all"
												onClick="SelAll(this)"></div>
						</td>
						<td width="10%" class="oracolumncenterheader">修改</td>
						<td width="15%" class="oracolumncenterheader">课程类别</td>
						<td width="70%" class="oracolumncenterheader">类别描述</td>
					</tr>
					<s:if test='parentid != "0"'>
						<tr class="trClass0" oriClass="" onMouseOut=TrMove(this)
							onMouseOver=TrMove(this)>
							<td width="5%">&nbsp;</td>
							<td width="10%">&nbsp;</td>
							<td width="15%"><a
								href='javascript:ComeBack("<s:property value="parentid"/>")'>
									<!--返回上一级 --> <s:text name="dataDir.list.back" />
							</a></td>
							<td width="70%">&nbsp;</td>
						</tr>
					</s:if>
					<tbody id="formlist">
						<s:iterator value="dirList" status="rowstatus">
							<s:if test="#rowstatus.odd == true">
								<tr class="trClass0" oriClass="" onMouseOut=TrMove(this)
									onMouseOver=TrMove(this)>
							</s:if>
							<s:else>
								<tr class="trClass1" oriClass="" onMouseOut=TrMove(this)
									onMouseOver=TrMove(this)>
							</s:else>
							<td nowrap width="5%">
								<div align="center">
									<input type="checkbox" name="chkList"
										value='<s:property value="id"/>'>
								</div>
							</td>
							<td nowrap width="10%">
								<div align="center">
									<img src="../../images/share/foldericon.gif"
										style="CURSOR: hand"
										onClick="ModifyItem('<s:property value="id"/>')">
								</div>
							</td>
							<td nowrap width="15%">
								<div align="left">
									<a href='javascript:ComeIn("<s:property value="id"/>")'><font
										color="#3366FF"> <s:property value="courseType" />
									</font></a>
								</div>
							</td>
							<td nowrap width="70%" title='<s:property value="courseDesc" />'>
								<s:if test='courseDesc.length()>60'><s:property value="courseDesc.substring(0,60)" />...</s:if>
								<s:else><s:property value="courseDesc" /></s:else>
							</td>
							</tr>
						</s:iterator>
					</tbody>
				</table>

			</aa:zone>
		</form>
	</s:iterator>
</body>
</html>