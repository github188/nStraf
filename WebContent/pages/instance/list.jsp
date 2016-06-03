<!--20110107 11:59-->
<%@ page contentType="text/html; charset=UTF-8"%>
<%@page import="cn.grgbanking.feeltm.login.domain.UserModel"%>
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

<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../calendar/fixDate.js"></script>
<script type="text/javascript" src="../../js/aa.js"></script>
<%@ include file="/inc/pagination.inc"%>
<!-- 行内文字过长自动省略显示 -->
<style type="text/css">
	#downloadList{table-layout:fixed;}
	#downloadList td {
		/* word-wrap: break-word; 
		word-break: break-all; */
		overflow:hidden;
		text-overflow:ellipsis;
		white-space:nowrap;
	}
</style>
<script type="text/javascript"> 
		   //IE10有问题，该功能屏蔽掉
		   /* function document.onkeydown()
		    {	
			

			var e=event.srcElement;        	
			if(event.keyCode==13) 
			{
				document.getElementById("bodyid").focus();
				query();
			}
	  	 } */
	 	function query(){
			var createDate = document.getElementById("createDate").value;
			var createMan = document.getElementById("createMan").value;
			var category = document.getElementById("category").value;
			var pageNum = document.getElementById("pageNum").value;
			var summary=document.getElementById("summary").value;
			//var planFinishDate=document.getElementById("planFinishDate").value;
			var raiseEndDate=document.getElementById("raiseEndDate").value;
			//var pn=document.getElementById("pn").value;
			var showdiv = document.getElementById("showdiv");
			var actionUrl = "<%=request.getContextPath()%>/pages/instance/instanceinfo!refresh.action?from=refresh&pageNum="+pageNum;
            actionUrl += "&createMan="+createMan;
            actionUrl += "&category="+category;
            actionUrl += "&createDate="+createDate;
			 actionUrl += "&summary="+summary;
			// actionUrl += "&planFinishDate="+planFinishDate;
			 //actionUrl += "&pn="+pn;
			  actionUrl += "&raiseEndDate="+raiseEndDate;
			actionUrl=encodeURI(actionUrl);
			var method="setHTML";
			<%int k = 0;%>
	   		sendAjaxRequest(actionUrl,method,pageNum,true);
		}
		
		function setHTML(entry,entryInfo){
			var html = '';
			var str = "javascript:show(\""+entryInfo["id"]+"\")";
			//var status=entryInfo["status"];
			var color='';

			html += '<tr class="trClass<%=k % 2%>" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>';
			html += '<td>';
			html += '<input type="checkbox" name="chkList" value="' + entryInfo["id"] + '" />';
			html += '</td>';
			html += '<td align="center"><a href='+str+'><font color="#3366FF">' +entryInfo["ino"] + '</font></a></td>';
			html += '<td>' +entryInfo["create_date"] + '</td>';
			html += '<td>' +entryInfo["create_man"] + '</td>';
			var summary=entryInfo["summary"];
			html += '<td align="left" title='+summary+'><span>' + summary +'</span></td>';
			html += '<td>' +entryInfo["category"] + '</td>';
			var embracer_man=entryInfo["embracer_man"];
			html += '<td align="left" title='+embracer_man+'><span>' + embracer_man +'</span></td>';
			if(entryInfo["confirmStatus"]==1){
				html += '<td align="center">已经确认</td>';		
			}
			else{
				html += '<td align="center">未经确认</td>';	
			}
			html += '<td align="center">' +entryInfo["confirmMan"] + '</td>'
			html += '<td align="center">' +entryInfo["updateDateString"] + '</td>';		
	  		html += '</tr>';
	 		<%k++;%>;
			showdiv.style.display = "none";
			return html;
		}
		
		function  GetSelIds(){
			var idList="";
			//var  em= document.all.tags("input");
			var em = document.getElementsByName("chkList");
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
			var em = document.getElementsByName("chkList");
			for(var  i=0;i<em.length;i++){
				if(em[i].type=="checkbox")
			    	em[i].checked=chkAll.checked
			}
		}
		
		function del() {

			var idList=GetSelIds();
		  	if(idList=="") {
		  		//alert("de");
			  	alert('<s:text name="errorMsg.notInputDelete" />');
				return false;
		  	}
			if(confirm('<s:text name="确认删除该记录吗？" />')) {
				//alert("dddddd");
			 	var strUrl="/pages/instance/instanceinfo!delete.action?ids="+idList;
			 	var returnValue=OpenModal(strUrl,"600,380,operInfo.delete,um")
			   	refreshList();
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
			  	var strUrl="/pages/instance/instanceinfo!edit.action?ids="+itemId;
			  	var features="900,500,tmlInfo.updateTitle,tmlInfo";
				var resultvalue = OpenModal(strUrl,features);
				refreshList();
			}
		}
	function add(){
      
    	var resultvalue = OpenModal('/pages/instance/instanceinfo!add.action','900,500,tmlInfo.addTmlTitle,tmlInfo');
    	refreshList();
	}
		
				
		function show(id) {
			var strUrl="/pages/instance/instanceinfo!show.action?ids="+id;
			var features="900,500,transmgr.traninfo,watch";
			var resultvalue = OpenModal(strUrl,features);
		}
		function openURL(url){
			window.open(url);
	}
	</script>
</head>
<body id="bodyid" leftmargin="0" topmargin="0">
	<s:form name="reportInfoForm" namespace="/pages/instance"
		action="instanceinfo!list.action" method="post">
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
							<td align="center" class="input_label">类别:</td>

							<td align="left"><tm:tmSelect name="instance.category"
									id="category" selType="dataDir"
									path="systemConfig.instancesource" style="width:75%" /></td>
							<script language="javascript">
								$("#category").prepend("<option value=''></option>");
							 	document.getElementById("category").value = "";
							</script>
							<td width="10%" class="input_label" align="left">接受者:</td>
							<td><input name="createMan" type="text" id="createMan"
								size="23" maxlength="23"></td>
							<td align="center" class="input_label">概要:</td>
							<td align="left"><input name="summary" type="text"
								id="summary" size="60" maxlength="60"></td>

						</tr>
						<tr>
							<td width="8%" align="center" class="input_label">开始日期:</td>
							<td width="20%" align="left"><input name="createDate" size="20"
								type="text" id="createDate"  class="MyInput" />
							</td>

							<td width="8%" align="center" class="input_label">结束日期:</td>
							<td width="20%" align="left"><input name="raiseEndDate" size="20"
								type="text" id="raiseEndDate" class="MyInput" />
							<td  colspan="2" align="right">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <tm:button
									site="1"></tm:button></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<br />
		<table width="100%" height="23" border="0" cellspacing="0"
			cellpadding="0" class="bgbuttonselect">
			<tr>
				<td width="25" height="23" valign="middle"></td>
				<td class="orarowhead"><s:text name="operInfo.title" /></td>
				<td align="right" width="75%"><tm:button site="2"></tm:button>
				</td>
			</tr>
		</table>
		<div id="showdiv"
			style='display: none; z-index: 999; background: white; height: 39px; line-height: 50px; width: 42px; position: absolute; top: 236px; left: 670px;'>
			<img src="../../images/loading.gif">
		</div>


		<table width="100%" border="0" cellpadding="1" cellspacing="1"
			bgcolor="#000066" id=downloadList>
			<tr>
				<td nowrap class="oracolumncenterheader" width="2%"></td>
				<td nowrap width="8%" class="oracolumncenterheader"><div
						align="center">编号</div></td>
				<td nowrap width="8%" class="oracolumncenterheader"><div
						align="center">提出日期</div></td>
				<td nowrap width="7%" class="oracolumncenterheader"><div
						align="center">提出者</div></td>
				<td nowrap class="oracolumncenterheader" style="word-wrap: break-word; word-break: break-all;"><div
						align="center">概要</div></td>
				<td nowrap width="7%" class="oracolumncenterheader"><div
						align="center">类别</div></td>
				<td nowrap class="oracolumncenterheader"><div
						align="center">接受者</div></td>
				<td nowrap width="7%" class="oracolumncenterheader"><div
						align="center">状态</div></td>
				<td nowrap width="7%" class="oracolumncenterheader"><div
						align="center">确认人</div></td>
				<td nowrap width="12%" class="oracolumncenterheader"><div
						align="center">最近更新日期</div></td>
			</tr>
			<tbody name="formlist" id="formlist" align="center">
				<s:iterator value="behaviorList" id="tranInfo" status="row">
					<s:if test="#row.odd == true">
						<tr id="tr" class="trClass0" oriClass="" onMouseOut=TrMove(this)
							onMouseOver=TrMove(this)>
					</s:if>
					<s:else>
						<tr id="tr" class="trClass1" oriClass="" onMouseOut=TrMove(this)
							onMouseOver=TrMove(this)>
					</s:else>
					<td nowrap align="center"><input type="checkbox"
						name="chkList" value="<s:property value='id'/>" /></td>
					<td align="center">
						<div align="center">
							<a href='javascript:show("<s:property value="id"/>")'><font
								color="#3366FF"> <s:property value="ino" /> </font> </a>
						</div>
					</td>
					<td align="center"><s:property value="createDateString" /></td>
					<td><s:property value="create_man" /></td>
					<td align="left"><s:property value="summary" /></td>
					<td align="center"><s:property value="category" /></td>
					<td align="left"><s:property value="embracer_man" /></td>
					<s:if test="confirmStatus==1">
					<td align="center">已经确认</td>
					</s:if>
					<s:else>
					<td align="center">未经确认</td>
					</s:else>
					<td align="center"><s:property value="confirmMan" /></td>
					<td align="center"><s:property value="updateDateString" /></td>
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
							<!--<td width="6%"> 
				<div align="center"><input type="checkbox" name="chkList"  id="chkAll"   value="all"  onClick="SelAll(this)"></div> 
			</td>
			<td width="11%">
				<div align="left"><label for=chkAll><s:text name="operInfo.checkall" /></label></div>
				</td>-->
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

