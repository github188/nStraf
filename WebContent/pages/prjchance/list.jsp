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
<link href="../../css/css_v2.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../calendar/fixDate.js"></script>
<script type="text/javascript" src="../../js/aa.js"></script>
<%@ include file="/inc/deptSelect.inc"%>
<%@ include file="/inc/pagination.inc"%>

<script type="text/javascript"> 
 var num=1;
 
 function toExport(){ 
		var actionUrl = "<%=request.getContextPath()%>/pages/prjchance/prjChanceInfo!exportData.action?";
		var prjName = document.getElementById("prjName").value;
		var followMan = document.getElementById("followMan").value;
		var client = document.getElementById("client").value;
		var area = document.getElementById("area").value;
		var province = document.getElementById("province").value;
		var clientManager = document.getElementById("clientManager").value;
		var clientType = document.getElementById("clientType").value;
		var prjStage = document.getElementById("prjStage").value;
		var prjResult = document.getElementById("prjResult").value;
		var pageNum = document.getElementById("pageNum").value;
		if(prjName!=''){
			actionUrl += "&prjName="+prjName;
		}
		if(followMan!=''){
			actionUrl += "&followMan="+followMan;
		}
		if(client!=''){
			actionUrl += "&client="+client;
		}
		if(area!=''){
			actionUrl += "&area="+area;
		}
		if(province!=''){
			actionUrl += "&province="+province;
		}
		if(clientManager!=''){
			actionUrl += "&clientManager="+clientManager;
		}
		if(clientType!=''){
			actionUrl += "&clientType="+clientType;
		}
		if(prjStage!=''){
			actionUrl += "&prjStage="+prjStage;
		}
		if(prjResult!=''){
			actionUrl += "&prjResult="+prjResult;
		}
		window.open(actionUrl,'newwindow',"toolbar=no,menubar=no ,location=no, width=700, height=400, top=100, left=100");	
		window.close();
	}
 
	function add(){
		var resultvalue = OpenModal('/pages/prjchance/prjChanceInfo!add.action','1000,900,tmlInfo.addTmlTitle,tmlInfo');
		//location.href="<%=request.getContextPath()%>/pages/prjchance/prjChanceInfo!add.action";
		refreshList();

	}
	function query() {
		var prjName = document.getElementById("prjName").value;
		var followMan = document.getElementById("followMan").value;
		var client = document.getElementById("client").value;
		var area = document.getElementById("area").value;
		var province = document.getElementById("province").value;
		var clientManager = document.getElementById("clientManager").value;
		var clientType = document.getElementById("clientType").value;
		var prjStage = document.getElementById("prjStage").value;
		var prjResult = document.getElementById("prjResult").value;
		var pageNum = document.getElementById("pageNum").value;
		
		
		if(pageNum==1)
			num=1;
		var actionUrl = "<%=request.getContextPath()%>/pages/prjchance/prjChanceInfo!refresh.action?from=refresh&pageNum="+pageNum;
		actionUrl += "&prjChance.prjName="+prjName;
		actionUrl += "&prjChance.followMan="+followMan;
		actionUrl += "&prjChance.client="+client;
		actionUrl += "&prjChance.area="+area;
		actionUrl += "&prjChance.province="+province;
		actionUrl += "&prjChance.clientManager="+clientManager;
		actionUrl += "&prjChance.clientType="+clientType;
		actionUrl += "&prjChance.prjStage="+prjStage;
		actionUrl += "&prjChance.prjResult="+prjResult;
		
		actionUrl = encodeURI(actionUrl,"UTF-8");
		var method = "setHTML";
		<%int k = 0; %>
		sendAjaxRequest(actionUrl,method,pageNum,true);
	}

	function setHTML(entry,entryInfo){
		var html  = '';
		var str = "javascript:show(\""+entryInfo["id"]+"\")";
		html += '<tr class="trClass<%=k%2 %>" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>';
		html += '<td>';
		html += '<input type="checkbox" name="chkList" value="' + entryInfo["id"] + '" />';
		html += '</td>';
		html += '<td align="center">';
		html += '	<div align="center" id="prjChanceId">'; 
    	html += '		<a href="javascript:showPrjChanceInfo('+"'"+entryInfo['id']+"'"+')"><font color="#3366FF">'+entryInfo['prjName']+'</font></a>';
   		html += '	</div>'; 
		html += '</td>';
		html += '<td align="center">' +entryInfo["client"] + '</td>';
		html += '<td align="center">'+entryInfo["prjResult"]+'</td>';
		html += '<td align="center">'+entryInfo["prjStage"]+'</td>';
		html += '<td align="center">' +entryInfo["budget"] + '</td>';
		html += '<td align="center">' +entryInfo["followMan"] + '</td>';
		html += '<td align="center">' +entryInfo["lastFollowDate"].substring(0,10) + '</td>';
  		html += '</tr>';
        num++;
 		<% k++;%>;     
		return html;
	}
	
	function showPrjChanceInfo(id){
		var strUrl = "/pages/prjchance/prjChanceInfo!edit.action?id="+id;
		var features="1000,900,prjChance.list.detail,customManager";
		var resultvalue = OpenModal(strUrl,features);

		refreshList();

	}
	
	function modify(){
		var aa=document.getElementsByName("chkList");
		var itemId=GetSelIds();
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
		  	var strUrl="/pages/prjchance/prjChanceInfo!edit.action?id="+itemId;
		  	var features="1000,900,tmlInfo.updateTitle,tmlInfo";
			var resultvalue = OpenModal(strUrl,features);
			refreshList();
		}
	}
	function  GetSelIds(){
		var idList="";
		//var  em= document.all.tags("input");
		var em= document.getElementsByName("chkList");
		for(var  i=0;i<em.length;i++){
		  if(em[i].type=="checkbox"){
		      if(em[i].checked){
		        idList+=","+em[i].value.split(",")[0];
		  		}
		  } 
	 	} 
		if(idList=="") 
		   return "";
		return idList.substring(1);
	}
	function del() {
		var idList=GetSelIds();
	  	if(idList=="") {
		  	alert('<s:text name="errorMsg.notInputDelete" />');
			return false;
	  	}
		if(confirm('<s:text name="确认删除该记录吗？" />')) {
		 	var strUrl="/pages/prjchance/prjChanceInfo!delete.action?ids="+idList;
		 	var returnValue=OpenModal(strUrl,"600,380,operInfo.delete,um")
		 	refreshList();
		}
	}
	</script>
</head>
<body id="bodyid" leftmargin="0" topmargin="0">
<s:form name="prjChanceInfoForm" action="prjChanceInfo!listAll.action"  namespace="/pages/prjchance" method="post" >
    
    <!-- 如果页面中用到了分页标签，请记得加上下面这两个隐藏标签 -->
	<input type="hidden" name="pageNum" id="pageNum"  value="1"/>
  	<input type="hidden" name="pageSize" id="pageSize" value="20" />
	<input type="hidden" name="menuid" value="<%=request.getParameter("menuid")%>">
	
	<%@include file="/inc/navigationBar.inc"%>
	
	<table width="100%" cellSpacing="0" cellPadding="0"> 
 		<tr>
 			<td >
					<table width="100%" class="select_area">
              			<tr>
							<td align="left" class="input_label">区域:</td>
							<td align="left">
								<input name="area" type="text" id="area"  />
							</td>
                			<td  class="input_label">省份:</td>
                			<td align="left">
                				<input name="province" type="text" id="province"  />
                			</td>
                			<td  class="input_label">客户经理:</td>
                			<td align="left">
                				<input name="clientManager" type="text" id="clientManager"  />
                			</td>  
                			           			 	
						</tr>
						<tr>
							<td align="left" class="input_label">客户类别:</td>
							<td align="left">
								<input name="clientType" type="text" id="clientType"  />
							</td>
                			<td  class="input_label">项目阶段:</td>
                			<td align="left">
                				<select name="prjStage" id="prjStage" style="width: 72%">
								<option value="" selected="selected"></option>
								<s:iterator value="#request.prjStage" id="ele">
								<option value="<s:property value='#ele.key'/>">
									<s:property value="#ele.value" />
								</option>
								</s:iterator>
					</select>
                			</td>
                			<td  class="input_label">项目结果:</td>
                			<td align="left">
                				<select name="prjResult" id="prjResult" style="width: 72%">
								<option value="" selected="selected"></option>
								<s:iterator value="#request.prjResult" id="ele">
								<option value="<s:property value='#ele.key'/>">
									<s:property value="#ele.value" />
								</option>
								</s:iterator>
                			</td>  
                			            			 	
						</tr>
						<tr>
							<td align="left" class="input_label">项目名称:</td>
							<td align="left">
								<input name="prjName" type="text" id="prjName"  />
							</td>
                			<td  class="input_label">客户名称:</td>
                			<td align="left">
                				<input name="client" type="text" id="client"  />
                			</td>
                			<td  class="input_label">跟进人:</td>
                			<td align="left">
                				<input name="followMan" type="text" id="followMan"  />
                			</td>  
                			<td align="right" colspan=2>
                				<tm:button site="1"></tm:button>
                			</td>            			 	
						</tr>
					</table>
			</td> 
		</tr>
	</table>
	<br/>
	
	<table width="100%" height="23" border="0" cellspacing="0" cellpadding="0" class="bgbuttonselect">
		<tr>
			<td width="25"  height="23" valign="middle"></td>
			<td class="orarowhead"><s:text name="operInfo.title" /></td>
			<td align="right" width="75%"><tm:button site="2"></tm:button></td>
		</tr>
	</table>
	
	<div id="showdiv" style='display:none;z-index:999; background:white;  height:39px; line-height:50px; width:42px;  position:absolute; top:236px; left:670px;'><img src="../../images/loading.gif"></div>
	
	
	<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066" id=downloadList>
		<tr> 
			<td nowrap class="oracolumncenterheader" width="2%"></td>
		  	<td nowrap width="20%" class="oracolumncenterheader"><div align="center">项目名称</div></td>
		  	<td nowrap width="15%" class="oracolumncenterheader"><div align="center">客户名称</div></td>
		  	<td nowrap width="15%" class="oracolumncenterheader"><div align="center">项目结果</div></td>
		  	<td nowrap width="15%" class="oracolumncenterheader"><div align="center">项目阶段</div></td>
		  	<td nowrap width="10%" class="oracolumncenterheader"><div align="center">项目预算</div></td>
		  	<td nowrap width="10%" class="oracolumncenterheader"><div align="center">跟进人</div></td>
		  	<td nowrap width="15%" class="oracolumncenterheader"><div align="center">最后跟进时间</div></td>
	  	</tr>
	  	<tbody name="formlist" id="formlist" align="center">
  			<s:iterator  value="prjChancelist" id="prjContractInfo" status="row">
	  		<s:if test="#row.odd == true"> 
	 		<tr id="tr" class="trClass0" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
	 		</s:if>
	 		<s:else>
	 		<tr id="tr" class="trClass1" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
	 		</s:else> 
				<td nowrap align="center">
					<input type="checkbox" name="chkList" value="<s:property value='Id'/>"/>
				</td>
				<td align="center">
				<div align="center">
			    <a href='javascript:showPrjChanceInfo("<s:property value="id"/>")'><font color="#3366FF">
			    <s:property value="prjName"/></font></a></div>
			    </td>
			 	<td align="center"><s:property value="client"/></td>
			 	<td align="center"><s:property value="prjResult" /></td>
			 	<td align="center"><s:property value="prjStage" /></td>
	            <td align="center"><s:property value="budget"/></td>
			 	<td align="center"><s:property value="followMan"/></td>
	            <td align="center"><s:date name="lastFollowDate" format="yyyy-MM-dd hh:mm:ss"/></td>
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
						<div id="pagetag"><tm:pagetag pageName="currPage" formName="prjChancefoForm" /></div>
					</td>
				</tr>
			</table>
		</td>
		</tr>
	</table>
</s:form>
</body>
</html>