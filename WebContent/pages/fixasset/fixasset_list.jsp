<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="cn.grgbanking.feeltm.login.domain.UserModel"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Date"%>
<%@ page contentType="text/html; charset=UTF-8"%>
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
			var no = document.getElementById("no").value.trim();
			var type = document.getElementById("type").value.trim();
			var name = document.getElementById("name").value.trim();
			var status = document.getElementById("status").value.trim();
			var inman = document.getElementById("inman").value.trim();
			var useman = document.getElementById("useman").value.trim();
			var pageNum = document.getElementById("pageNum").value;
			var showdiv = document.getElementById("showdiv");
			var actionUrl = "<%=request.getContextPath()%>/pages/fixasset/fixassetinfo!refresh.action?from=refresh&pageNum="+pageNum;
			actionUrl += "&no="+no+"&type="+type+"&name="+name+"&status="+status+"&inman="+inman;
			actionUrl += "&useman="+useman;
			actionUrl=encodeURI(actionUrl);
			var method="setHTML";
			<%int k = 0;%>
	   		sendAjaxRequest(actionUrl,method,pageNum,true);
		}
		
		function setHTML(entry,entryInfo){
			var html = '';
			var str = "javascript:show(\""+entryInfo["id"]+"\")";
			html += '<tr class="trClass<%=k % 2%>" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>';
			html += '<td>';
			html += '<input type="checkbox" name="chkList" value="' + entryInfo["id"] + '" />';
			html += '</td>';
			var type = "";
			if(entryInfo["type"]=="0"){
				type="PC";
			}else if(entryInfo["type"]=="1"){
				type="笔记本";
			}else if(entryInfo["type"]=="2"){
				type="服务器";
			}else if(entryInfo["type"]=="3"){
				type="路由";
			}else if(entryInfo["type"]=="4"){
				type="键盘";
			}else if(entryInfo["type"]=="5"){
				type="鼠标";
			}
			html += '<td align="center"><a href='+str+'><font color="#3366FF">' +entryInfo["no"] + '</font></a></td>';
			html += '<td >'+type + '</td>';
			html += '<td >'+entryInfo["name"] + '</td>';
			html += '<td>' +entryInfo["inman"] + '</td>';
			var status = "";
			var color='';
			if(entryInfo["status"]=="0"){
				type="闲置";
				color='blue';
			}else if(entryInfo["status"]=="1"){
				type="领用";
				color='red';
			}else if(entryInfo["status"]=="2"){
				type="无效";
				color='gray';
			}
			html += '<td><font color="'+color+'">' +type + '</font></td>';
			html += '<td>' +entryInfo["useman"] + '</td>';
			html += '<td>' +entryInfo["usedate"] + '</td>';
			html += '<td>' + entryInfo["expectdate"] + '</td>';
			html += '<td>' + entryInfo["factdate"] + '</td>';
			/* var str = entryInfo["use"];
			if(str.length > 14){
				str = str.substr(0,14) + "...";
			} */
			html += '<td align="left" title='+entryInfo["use"]+'>' + entryInfo["use"] + '</td>';
	  		html += '</tr>';
	 		<%k++;%>;
			showdiv.style.display = "none";
			return html;
		}
		
		function  GetSelIds(){
			var idList="";
		    //var em= document.all.tags("input");
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
		
		function  SelAll(chkAll){
			//var   em=document.all.tags("input");
			var em= document.getElementsByName("chkList");
			for(var  i=0;i<em.length;i++){
				if(em[i].type=="checkbox")
			    	em[i].checked=chkAll.checked
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
					var strUrl="/pages/fixasset/fixassetinfo!delete.action?ids="+idList;
					var returnValue=OpenModal(strUrl,"600,380,operInfo.delete,um")
					refreshList();
				}
			}
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
			  	var strUrl="/pages/fixasset/fixassetinfo!edit.action?ids="+itemId;
			  	var features="790,520,tmlInfo.updateTitle,tmlInfo";
				var resultvalue = OpenModal(strUrl,features);
				refreshList();
			}
		}
		
		function add(){
			var resultvalue = OpenModal('/pages/fixasset/fixassetinfo!add.action','790,520,tmlInfo.addTmlTitle,tmlInfo');
			refreshList();
		}
		
				
		function show(id) {
			var strUrl="/pages/fixasset/fixassetinfo!show.action?ids="+id;
			var features="790,520,transmgr.traninfo,watch";
			var resultvalue = OpenModal(strUrl,features);
		}
		function showAllInMonth(id) {
			//alert("该功能正在实现中....");
			//return;
			var strUrl="/pages/week/weekinfo!show.action?ids="+id;
			var features="820,700,transmgr.traninfo,watch";
			var resultvalue = OpenModal(strUrl,features);
		}
		function openURL(url){
			window.open(url);
		}
		function crlfLine(str, trunNum, cr){
			var testStr = str;
			var allStr = "";
			var start = 0;
			for(start = 0; start<= testStr.length; start+=trunNum){
				allStr = allStr + testStr.substr(start, trunNum) + cr;
			}
			return allStr;
		}
		
		function getKey(e){ 
			e = e || window.event; 
			var keycode = e.which ? e.which : e.keyCode; 
			if(keycode == 13 || keycode == 108){ 
				document.getElementById("bodyid").focus();
				query();
			} 
		}
		
		function listenKey(){ 
			if (document.addEventListener) { 
				document.addEventListener("keyup",getKey,false); 
			} else if (document.attachEvent) { 
				document.attachEvent("onkeyup",getKey); 
			} else { 
				document.onkeyup = getKey; 
			} 
		} 
		listenKey();
	</script>
<body id="bodyid" leftmargin="0" topmargin="0">
	<s:form name="reportInfoForm" namespace="/pages/overtime"
		action="overtimeinfo!list.action" method="post">
		<input type="hidden" name="pageNum" id="pageNum" value="1" />
  		<input type="hidden" name="pageSize" id="pageSize" value="20" />
		<%@include file="/inc/navigationBar.inc"%>
		<input type="hidden" name="menuid"
			value="<%=request.getParameter("menuid")%>">
		<table width="100%" cellSpacing="0" cellPadding="0">
			<tr>
				<td>
					<table width="100%" class="select_area">
						<tr>
							<td class="input_label">资产类型：</td>
							<td><select name="fixAsset.type" id="type">
									<option value="">全部</option>
									<option value="0">PC</option>
									<option value="1">笔记本</option>
									<option value="2">服务器</option>
									<option value="3">路由</option>
									<option value="4">键盘</option>
									<option value="5">鼠标</option>
							</select></td>
							<td class="input_label">资产编号：</td>
							<td><input type="text" name="fixAsset.no" id="no" /></td>
							<td class="input_label">资产名称：</td>
							<td><input type="text" name="fixAsset.name" id="name" /></td>
						</tr>
						<tr>
							<td class="input_label">状态：</td>
							<td><select name="fixAsset.status" id="status">
									<option value="">全部</option>
									<option value="0">闲置</option>
									<option value="1">领用</option>
									<option value="2">无效</option>
							</select></td>
							<td class="input_label">资产管理员：</td>
							<td><input type="text" name="fixAsset.inman" id="inman" />
							</td>
							<td class="input_label">领用人：</td>
							<td><input type="text" name="fixAsset.useman" id="useman" >
							</td>
							<td colspan="6" align="right"><tm:button site="1" />
							</td>
						</tr>
					</table></td>
			</tr>
		</table>
		<br />
		<table width="100%" height="23" border="0" cellspacing="0"
			cellpadding="0" class="bgbuttonselect">
			<tr>
				<td width="25" height="23" valign="middle"></td>
				<td class="orarowhead"><s:text name="operInfo.title" />
				</td>
				<td align="right" width="75%"><tm:button site="2"></tm:button>
				</td>
			</tr>
		</table>
		<div id="showdiv"
			style='display: none; z-index: 999; background: white; height: 39px; line-height: 50px; width: 42px; position: absolute; top: 236px; left: 670px;'>
			<img src="../../images/loading.gif">
		</div>

		<table width="100%" border="0" cellpadding="1" cellspacing="1"
			bgcolor="#000066" id="downloadList" >
			<tr>
				<td nowrap width="2%" class="oracolumncenterheader"></td>
				<td nowrap width="10%" class="oracolumncenterheader"><div
						align="center">资产编号</div>
				</td>
				<td nowrap width="10%" class="oracolumncenterheader"><div
						align="center">资产类型</div>
				</td>
				<td nowrap width="10%" class="oracolumncenterheader"><div
						align="center">资产名称</div>
				</td>
				<td nowrap width="10%" class="oracolumncenterheader"><div
						align="center">资产管理员</div>
				</td>
				<td nowrap width="6%" class="oracolumncenterheader"><div
						align="center">状态</div>
				</td>
				<td nowrap width="8%" class="oracolumncenterheader"><div
						align="center">领用人</div>
				</td>
				<td nowrap width="8%" class="oracolumncenterheader"><div
						align="center">领用日期</div>
				</td>
				<td nowrap width="10%" class="oracolumncenterheader"><div
						align="center">预计归还日期</div>
				</td>
				<td nowrap width="8%" class="oracolumncenterheader"><div
						align="center">归还日期</div>
				</td>
				<td nowrap width="18%" class="oracolumncenterheader"><div
						align="center">用途</div>
				</td>
			</tr>
			<tbody name="formlist" id="formlist" align="center">
				<s:iterator value="fixassetList" id="tranInfo" status="row">
					<s:if test="#row.odd == true">
						<tr id="tr" class="trClass0" oriClass="" onMouseOut=TrMove(this)
							onMouseOver=TrMove(this)>
					</s:if>
					<s:else>
						<tr id="tr" class="trClass1" oriClass="" onMouseOut=TrMove(this)
							onMouseOver=TrMove(this)>
					</s:else>
					<td nowrap align="center"><input type="checkbox"
						name="chkList" value='<s:property value="id"/>' />
					</td>
					<!-- 资产编号 -->					
					<td><a style="color:#3366FF" href="javascript:show('<s:property value="id" />')"><s:property value="no" /></a>
					</td>
					<td>
						<s:if test="type ==0 ">PC</s:if>
						<s:elseif test="type == 1">笔记本</s:elseif>
						<s:elseif test="type == 2">服务器</s:elseif>
						<s:elseif test="type == 3">路由</s:elseif>
						<s:elseif test="type == 4">键盘</s:elseif>
						<s:elseif test="type == 5">鼠标</s:elseif>
						<s:else>其他</s:else>
					</td>
					<td><s:property value="name" />
					</td>
					<td><s:property value="inman" />
					</td>
					<td>
						<s:if test="status == 0"><font color="blue">闲置</font></s:if>
						<s:elseif test="status == 1"><font color="red">领用</font></s:elseif>
						<s:elseif test="status == 2"><font color="gray">无效</font></s:elseif>						
					</td>
					<td><s:property value="useman" />
					</td>
					<td><s:date name="usedate" format="yyyy-MM-dd" />
					</td>
					<td><s:date name="expectdate" format="yyyy-MM-dd" />
					</td>
					<td ><s:date name="factdate" format="yyyy-MM-dd" />
					</td>
					<td align="left" title="<s:property value="use" />"><s:property value="use" />
					</td>
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
								</div></td>
						</tr>
					</table></td>
			</tr>
		</table>
	</s:form>
</body>
</html>

