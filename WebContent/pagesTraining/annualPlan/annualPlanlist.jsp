<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="cn.grgbanking.feeltm.login.domain.UserModel"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa"%>
<html>
<head>
<title></title>
</head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../calendar/fixDate.js"></script>
<script type="text/javascript" src="../../js/aa.js"></script>
<%@ include file="/inc/pagination.inc"%>
<%@ include file="/inc/deptSelect.inc"%>

<script type="text/javascript"> 
		String.prototype.trim = function(){
			return this.replace(/(^\s*)|(\s*$)/g, "");
		}

		function query(){
			var actionUrl = "<%=request.getContextPath()%>/pages/annualPlan/annualPlanInfo!refresh?from=refresh";
			var method="setHTML";
			<%int k = 0 ; %>
			//执行ajax请求,其中method是产生显示list信息的html的方法的名字，该方法是需要在自己的页面中单独实现的,且方法的签名必须是methodName(entry,entryInfo)
			sendAjaxRequest(actionUrl,method,"",false, true);
		}
		
		function setHTML(entry,entryInfo){
			var html = '';
			html += '<tr class="trClass<%=k%2 %>" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>';
			html += '<td>';
			html += '<input type="checkbox" name="chkList" value="' + entryInfo["id"] + '" />';
			html += '</td>';
			html += '<td>' +entryInfo["planYear"] + '</td>';
			html += '<td >';
			html += '<div align="left"><a href="/nStraf/pages/annualPlan/annualPlanInfo!downloadFile.action?id='+entryInfo['id']+'" ><font style="color: #3366FF">' + entryInfo['planFile'] + '</font></a></div>';
			html += '</td>';
			html += '<td>' + entryInfo["username"] + '</td>';
			html += '<td>' +entryInfo["uploadDate"].substr(0,entryInfo["uploadDate"].length-2) + '</td>';
	  		html += '</tr>';
	 		<%k++;%>;
			showdiv.style.display = "none";
			return html;
		}
		
		function add(){
			var resultvalue = OpenModal('/pages/annualPlan/annualPlanInfo!add.action','600,320,tmlInfo.addTmlTitle,tmlInfo');
		  	refreshList();
		}
		
		function modify(){
			var aa=document.getElementsByName("chkList");
			var itemId;
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
			  	var strUrl="/pages/annualPlan/annualPlanInfo!edit.action?ids="+itemId;
			  	var features="790,520,tmlInfo.updateTitle,tmlInfo";
				var resultvalue = OpenModal(strUrl,features);
				refreshList();
			}
		}
		
		function del() {
			var aa=document.getElementsByName("chkList");
			var itemId;
			var j=0;
			for (var i=0; i<aa.length; i++){
			   if (aa[i].checked){
					itemId=aa[i].value;
					j=j+1;
				}
			}

			if (j==0)
				alert('请选择一条记录');
			 else if (j>1)
				alert('你只能一次删除一条的记录');
			else{
				var idList=GetSelIds();
				if(confirm('<s:text name="确认删除该记录吗？" />')) {
					var strUrl="/pages/annualPlan/annualPlanInfo!delete.action?id="+idList;
					var returnValue=OpenModal(strUrl,"600,380,operInfo.delete,um")
					refreshList();
				}
			}
		}

		function show(id) {
			var strUrl="/pages/annualPlan/annualPlanInfo!show.action?ids="+id;
			var features="790,520,transmgr.traninfo,watch";
			var resultvalue = OpenModal(strUrl,features);
		}
		
		function  GetSelIds(){
			var idList="";
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
	</script>
	<body id="bodyid" leftmargin="0" topmargin="0" onload="init();">
	<s:form name="reportInfoForm" namespace="/pages/annualPlan"
		action="annualPlanInfo!query.action" method="post">
		<input type="hidden" name="pageNum" id="pageNum" value="1" />
  		<input type="hidden" name="pageSize" id="pageSize" value="20" />
		<%@include file="/inc/navigationBar.inc"%>
		<input type="hidden" name="menuid" value="<%=request.getParameter("menuid")%>">
		<table width="100%" height="23" border="0" cellspacing="0"
			cellpadding="0" class="bgbuttonselect">
			<tr>
				<td class="orarowhead"><span style="margin-left:10px;">信息列表</span></td>
				<td align="right" width="75%"><tm:button site="2"></tm:button></td>
			</tr>
		</table>
		<div id="showdiv" style='display: none; z-index: 999; background: white; height: 39px; line-height: 50px; width: 42px; position: absolute; top: 236px; left: 670px;'>
			<img src="../../images/loading.gif">
		</div>
		<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066" id=downloadList>
			<tr>
				<td nowrap width="5%" class="oracolumncenterheader"></td>
				<td nowrap width="10%" class="oracolumncenterheader"><div align="center">计划年度</div></td>
				<td nowrap width="50%" class="oracolumncenterheader"><div align="center">计划文件</div></td>
				<td nowrap width="10%" class="oracolumncenterheader"><div align="center">上传者</div></td>
				<td nowrap width="25%" class="oracolumncenterheader"><div align="center">上传日期</div></td>
			</tr>
			<tbody name="formlist" id="formlist" align="center">
				<s:iterator value="annualPlanList" id="tranInfo" status="row">
					<s:if test="#row.odd == true">
						<tr id="tr" class="trClass0" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
					</s:if>
					<s:else>
						<tr id="tr" class="trClass1" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
					</s:else>
						<td nowrap align="center"><input type="checkbox" name="chkList" value='<s:property value="id"/>' /></td>
						<td><s:property value="planYear" /></td>
						<td>
							<a href="/nStraf/pages/annualPlan/annualPlanInfo!downloadFile.action?id=<s:property value="id" />"><font style="color: #3366FF"><s:property value="planFile" /></font></a>
						</td>
						<td><s:property value="username" /></td>
						<td><s:date name="uploadDate" format="yyyy-MM-dd HH:mm:ss" /></td>
					</tr>
				</s:iterator>
			</tbody>
		</table>
		<table width="100%" border="0" cellpadding="1" cellspacing="1"
			bgcolor="#FFFFFF">
			<tr bgcolor="#FFFFFF">
				<td>
					<table width="100%" cellSpacing="0" cellPadding="0">
						<tr>
							<td width="83%" align="right">
								<div id="pagetag">
									<tm:pagetag pageName="currPage" formName="reportInfoForm" />
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

