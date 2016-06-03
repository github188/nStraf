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
<% 
java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
java.util.Date currentTime = new java.util.Date();//得到当前系统时间 
String str_date1 = formatter.format(currentTime); //将日期时间格式化 
request.setAttribute("current_date", str_date1);
%>


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
		overflow:hidden;
		text-overflow:ellipsis;
		white-space:nowrap;
	}
</style>
<script type="text/javascript"> 
var pro_all=0;//生产问题总数
var pro_no=0;//为处理的生产问题
var pro_yes=0;//已处理的生产问题
var item_all=0;//项目风险总数
var item_no=0;//项目风险的生产问题
var item_yes=0;//项目风险的生产问题
		//IE10不兼容
		/* function document.onkeydown(){
				var e=event.srcElement;        	
				if(event.keyCode==13){
					document.getElementById("bodyid").focus();
					query();
				}
		} */
		function query(){
			pro_all=0;//生产问题总数
			pro_no=0;//为处理的生产问题
			pro_yes=0;//已处理的生产问题
			item_all=0;//项目风险总数
			item_no=0;//项目风险的生产问题
			item_yes=0;//项目风险的生产问题
			var createman = document.getElementById("createman").value;
			var status = document.getElementById("status").value;
			var prjname = document.getElementById("prjname").value;
			var summary = document.getElementById("summary").value;
			var pageNum = document.getElementById("pageNum").value;
			var type=document.getElementById("type").value;
			var urgent=document.getElementById("urgent").value;
			var pond=document.getElementById("pond").value;
			var startDate=document.getElementById("startDate").value;
			var endDate=document.getElementById("endDate").value;
          
			var actionUrl = "<%=request.getContextPath()%>/pages/prjrisk/prjriskinfo!refresh.action?from=refresh&pageNum="+pageNum;
            actionUrl += "&createman="+createman;
            actionUrl += "&status="+status;
            actionUrl += "&prjname="+prjname;
			actionUrl += "&summary="+summary;
			 actionUrl += "&type="+type;
			 actionUrl += "&urgent="+urgent;
			 actionUrl += "&pond="+pond;
			 actionUrl += "&startDate="+startDate;
			 actionUrl += "&endDate="+endDate;
			actionUrl=encodeURI(actionUrl);
			var method="setHTML";
			<%int k = 0 ; %>
	   		sendAjaxRequest(actionUrl,method,pageNum,true);
		}
		
		function setHTML(entry,entryInfo){
			var html = '';
			var str = "javascript:show(\""+entryInfo["id"]+"\")";
			var status=entryInfo["status"];
			var handleterm=entryInfo["handleterm"];
			var color='';
			var colortmp = '';
			var currentdate = "<%=str_date1%>";
			if(status=='处理中' || status=="新建"){
			    if(currentdate > handleterm){
					colortmp='red';
					color='red';
				}else{
					colortmp='blue';
					color='blue';
				}
			}else{
				colortmp="#000000";
				color='#000000';
			}
			if(status=="已解决"){
				colortmp='green';
				color='green';
			}
			
			html += '<tr class="trClass<%=k%2 %>" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>';
			html += '<td>';
			html += '<input type="checkbox" name="chkList" value="' + entryInfo["id"] + '" />';
			html += '</td>';
			html += '<td align="center"><a href='+str+'>' + '<font color=\''+colortmp+'\'>'+entryInfo["rno"] +'</font>' + '</a></td>';
			html += '<td>' +entryInfo["createdate"] + '</td>';
			html += '<td>' +entryInfo["createman"] + '</td>';
			html += '<td align="left">' +entryInfo["type"] + '</td>';
			html += '<td>' +entryInfo["prjname"] + '</td>';
			var summary=entryInfo["summary"];
			var tsummary = entryInfo["summary"];
			if(summary.length>25){
				summary=summary.substr(0,25)+"...";
			}
			html += '<td title="'+tsummary+'" align="left">' +summary + '</td>';
			html += '<td>' +entryInfo["urgent"] + '</td>';
			html += '<td>' +entryInfo["pond"] + '</td>';
			html += '<td>' +'<font color=\''+color+'\'>'+entryInfo["status"] +'</font>' +'</td>';
			html += '<td>' +entryInfo["handleman"] + '</td>';
			html += '<td align="center">' +entryInfo["handleterm"] + '</td>';
			html += '<td>' +entryInfo["factdate"] + '</td>';
			html += '<td align="center">' +entryInfo["updateman"] + '</td>';
			html += '<td align="center">' +entryInfo["update"] + '</td>';
	  		html += '</tr>';
	  		
	  		var type=entryInfo["type"];
	  		var status=entryInfo["status"];
	  		if(type=="生产问题"){
	  			pro_all+=1;
	  			if(status=="已解决"){
	  				pro_yes+=1;
	  			}else{
	  				pro_no+=1;
	  			}
	  		}
	  		if(type=="项目风险"){
	  			item_all+=1;
	  			if(status=="已解决"){
	  				item_yes+=1;
	  			}else{
	  				item_no+=1;
	  			}
	  		}
	  		var info="<span style='color:red;'>生产问题共:"+pro_all+"个，已解决:"+pro_yes+"个，未解决:"+pro_no+"个;"+"项目风险共:"+item_all+"个，已解决:"+item_yes+"个，未解决:"+item_no+"个。</span>";
	  		document.getElementById("remindInfo").innerHTML=info;
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
			 	var strUrl="/pages/prjrisk/prjriskinfo!delete.action?ids="+idList;
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
			  	var strUrl="/pages/prjrisk/prjriskinfo!edit.action?ids="+itemId;
			  	var features="900,600,tmlInfo.updateTitle,tmlInfo";
				var resultvalue = OpenModal(strUrl,features);
				refreshList();
			}
		}
	function add(){
      
    	var resultvalue = OpenModal('/pages/prjrisk/prjriskinfo!add.action','900,600,tmlInfo.addTmlTitle,tmlInfo');
    	refreshList();
	}
		
				
		function show(id) {
			var strUrl="/pages/prjrisk/prjriskinfo!show.action?ids="+id;
			var features="900,600,transmgr.traninfo,watch";
			var resultvalue = OpenModal(strUrl,features);
		}
		function openURL(url){
			window.open(url);
	}
	//初始进入页面即开始查询
	$(function(){
		query();
	})
	</script>
</head>

<body id="bodyid" leftmargin="0" topmargin="0">
	<s:form name="reportInfoForm" namespace="/pages/prjrisk"
		action="prjriskinfo!list.action" method="post">
		<input type="hidden" name="pageNum" id="pageNum" value="1" />
  		<input type="hidden" name="pageSize" id="pageSize" value="20" />
		<%@include file="/inc/navigationBar.inc"%>
		<input type="hidden" name="menuid"
			value="<%=request.getParameter("menuid")%>">
		<input type="hidden" name="nowdate" id="nowdate"
			value='<s:property value="nowdate"/>'>
		<table width="100%" cellSpacing="0" cellPadding="0">
			<tr>
				<td >
						<table width="100%" class="select_area">
							<tr>
								<td width="8%" align="right" class="input_label">项目名称:</td>
								<td width="20%"><input name="prjname" type="text"
									id="prjname" size="40" maxlength="40" ></td>
								<td width="8%" align="right" class="input_label">提出者:</td>
								<td width="10%"><input name="createman" type="text"
									id="createman" size="23" maxlength="23"></td>
								<td width="8%" align="right" class="input_label">风险类型:</td>
								<td width="8%" align="left"><select name="type" id="type"
									align="left" style="width: 100px">
										<option value='' selected="true">全部</option>
										<option value='资源'>资源</option>
										<option value='人力'>人力</option>
										<option value='进度'>进度</option>
										<option value='质量'>质量</option>
										<option value='设计'>设计</option>
										<option value='制度'>制度</option>
										<option value='生产问题'>生产问题</option>
										<option value='项目风险'>项目风险</option>
										<option value='其他'>其他</option>
								</select></td>
								<td align="right" class="input_label">状态:</td>
								<td><select name="status" id="status" align="left"
									style="width: 100px">
										<option value='' selected="true">全部</option>
										<option value='新建'>新建</option>
										<option value='处理中'>处理中</option>
										<option value='已解决'>已解决</option>
										<option value='不解决'>不解决</option>
								</select></td>
							</tr>
							<tr>
								<td align="right" class="input_label">提出日期:</td>
								<td><input name="startDate" type="text" id="startDate"
									class="MyInput" />&nbsp;&nbsp;至&nbsp;&nbsp;
									<input name="endDate" type="text" id="endDate"
									class="MyInput" /></td>
								<td align="right" class="input_label">概要:</td>
								<td><input name="summary" type="text"
									id="summary" size="23" maxlength="23"></td>
								<td align="right" class="input_label"> 紧急程度:</td>
								<td><select name="urgent" id="urgent" align="left"
									style="width: 100px">
										<option value='' selected="true">全部</option>
										<option value='非常紧急'>非常紧急</option>
										<option value='紧急'>紧急</option>
										<option value='一般'>一般</option>
										<option value='不紧急'>不紧急</option>
								</select></td>
								<td align="right" class="input_label">严重性:</td>
								<td><select name="pond" id="pond" align="left"
									style="width: 100px">
										<option value='' selected="true">全部</option>
										<option value='高'>高</option>
										<option value='中'>中</option>
										<option value='低'>低</option>
								</select></td>
								<td><tm:button
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
				<td id="remindInfo" width="50%"></td>
				<td align="right" width="25%"><tm:button site="2"></tm:button></td>
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
				<td nowrap width="6%" class="oracolumncenterheader"><div
						align="center">风险编号</div></td>
				<td nowrap width="8%" class="oracolumncenterheader"><div
						align="center">提出日期</div></td>
				<td nowrap width="6%" class="oracolumncenterheader"><div
						align="center">提出者</div></td>
				<td nowrap width="5%" class="oracolumncenterheader"><div
						align="center">类别</div></td>
				<td nowrap width="10%" class="oracolumncenterheader"><div
						align="center">项目名称</div></td>
				<td nowrap width="10%" class="oracolumncenterheader"><div
						align="center">风险概要</div></td>
				<td nowrap width="5%" class="oracolumncenterheader"><div
						align="center">紧急程度</div></td>
				<td nowrap width="5%" class="oracolumncenterheader"><div
						align="center">严重性</div></td>
				<td nowrap width="4%" class="oracolumncenterheader"><div
						align="center">状态</div></td>
				<td nowrap width="7%" class="oracolumncenterheader"><div
						align="center">负责处理者</div></td>
				<td nowrap width="8%" class="oracolumncenterheader"><div
						align="center">计划完成日期</div></td>
				<td nowrap width="8%" class="oracolumncenterheader"><div
						align="center">实际完成日期</div></td>
				<td nowrap class="oracolumncenterheader"><div
						align="center">最近更新者</div></td>
				<td nowrap width="8%" class="oracolumncenterheader"><div
						align="center">最近更新日期</div></td>
			</tr>
			<tbody name="formlist" id="formlist" align="center">
				<%-- <s:iterator value="behaviorList" id="tranInfo" status="row">
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
						<a href='javascript:show("<s:property value="id"/>")'> 
							<s:if
								test="status.equals('新建') || status.equals('处理中')">
								<s:if test="createdate > handleterm">
									<font color='red'>
								</s:if>
								<s:else>
									<font color="blue">
								</s:else>
							</s:if>
							<s:else>
								<s:if test="status.equals('已解决')">
									<font color="green">
								</s:if>
								<s:else>
									<font color="#000000">
								</s:else>
							</s:else> <s:property value="rno" /> </font>
						</a>
					</td>
					<td align="center"><s:date name="createdate" format="yyyy-MM-dd"/></td>
					<td><s:property value="createman" /></td>
					<td align="left"><s:property value="type" /></td>
					<td align="center"><s:property value="prjname" /></td>
					<td align="left"><s:property value="summary" /></td>
					<td align="left"><s:property value="urgent" /></td>
					<td align="left"><s:property value="pond" /></td>
					<td><s:if test="status.equals('不解决')">
							<font color='#666666'>
						</s:if> <s:elseif test="status.equals('已解决')">
							<font color='blue'>
						</s:elseif> <s:elseif test="status.equals('新建')">
							<font color='red'>
						</s:elseif> <s:else>
							<font color=''>
						</s:else> <s:property value="status" /> </font></td>
					<td align="center"><s:property value="handleman" /></td>
					<td align="center"><s:date name="handleterm" format="yyyy-MM-dd" /></td>
					<td align="center"><s:date name="factdate" format="yyyy-MM-dd" /></td>
					<td align="center"><s:property value="updateman" /></td>
					<td align="center"><s:date name="update" format="yyyy-MM-dd" /></td>
					</tr>
				</s:iterator> --%>
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

