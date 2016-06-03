<!--20100107 14:00-->
<%@ page contentType="text/html; charset=UTF-8"%>
<%@page import="cn.grgbanking.feeltm.login.domain.UserModel"%>
<%@page import="java.math.BigDecimal"%>

<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa"%>
<html>
<head>
<title>tool query</title>
</head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../calendar/fixDate.js"></script>
<script type="text/javascript" src="../../js/aa.js"></script>
<%@ include file="/inc/pagination.inc"%>
<script type="text/javascript">

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
				var searchSoloveMan = document.getElementById("searchSoloveMan").value;
				var searchStatus = document.getElementById("searchStatus").value;
			//	var searchFinishRate = document.getElementById("searchFinishRate").value;
				var searchFindDate = document.getElementById("searchFindDate").value;
				var searchSoloveDate = document.getElementById("searchSoloveDate").value;
				var searchFindMan = document.getElementById("searchFindMan").value;
				var searchModualName = document.getElementById("searchModualName").value;
				var searchTitle = document.getElementById("searchTitle").value;
				var serialNo = document.getElementById("serialNo").value;
				var urge = document.getElementById("searchUrge").value;
				var buglevel = document.getElementById("buglevel").value;
				var searchUpdateMan = document.getElementById("searchUpdateMan").value;
				var searchDeployStatus = document.getElementById("searchDeployStatus").value;

				var pageNum = document.getElementById("pageNum").value;
				var actionUrl = "<%=request.getContextPath()%>/pages/TestRecord/TestRecordinfo!refresh.action?from=refresh&pageNum="+pageNum;
				if(searchSoloveMan!="")
					actionUrl += "&searchSoloveMan="+searchSoloveMan;
				if(buglevel!="")
					actionUrl += "&buglevel="+buglevel;
				
				if(searchStatus!="")
					actionUrl += "&searchStatus="+searchStatus;
				
				//if(searchFinishRate!="")
				//	actionUrl += "&searchFinishRate="+searchFinishRate;
				
				
				if(searchFindDate!='')
					actionUrl += "&searchFindDate="+searchFindDate;
				
				if(searchSoloveDate!='')
					actionUrl += "&searchSoloveDate="+searchSoloveDate;
				
				if(searchFindMan!='')
					actionUrl += "&searchFindMan="+searchFindMan;
				
				if(searchModualName!='')
					actionUrl += "&searchModualName="+searchModualName;
				
				if(searchTitle!='')
					actionUrl += "&searchTitle="+searchTitle;
				
				if(serialNo!='')
					actionUrl += "&serialNo="+serialNo;
				
				if(urge!='')
					actionUrl += "&searchUrge="+urge;
				
				if(searchUpdateMan!='')
					actionUrl += "&searchUpdateMan="+searchUpdateMan;
				
				if(searchDeployStatus!='')
					actionUrl += "&searchDeployStatus="+searchDeployStatus;
				
				actionUrl=encodeURI(actionUrl);
				var method="setHTML";
				<%int k = 0 ; %>
		   		sendAjaxRequest(actionUrl,method,pageNum,true);
            }
		
        function shortShow(title){
        	if(title.length>15){
        		title=title.substring(0,15)+"...";
        	}
        	return title;
        }
        
        function setHTML(entry,entryInfo){
        	var html = '';
    		var str = "javascript:modify(\""+entryInfo["id"]+"\")";
    	
    		html += '<tr class="trClass<%=k%2%>" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this) align="center">';
    		html += '<td>';
    		html += '<input type="checkbox" name="chkList" value="' + entryInfo["id"] + '" />';
    		html += '</td>';
    		html += '<td width="7%"><a href='+str+'><font style="color: #3366FF">' +entryInfo["serialNo"] + '</font></a></td>';
    		html += '<td width="10%" align="left">'+entryInfo["modualName"]+'</td>';
    		html += '<td width="20%" align="left">' +shortShow(entryInfo["title"]) + '</td>';
    		html += '<td width="8%" align="left">' +entryInfo["findMan"]+ '</td>';
    		html += '<td widht="8%">' + entryInfo["findDate"] + '</td>';
    		html += '<td width="8%" align="left">' +entryInfo["soloveMan"]+ '</td>';
    		html += '<td width="8%">' + entryInfo["soloveDate"] + '</td>';
    		html += '<td widht="8%">' + entryInfo["questionStatus"] + '</td>';
    		html += '<td widht="8%">' + entryInfo["buglevel"] + '</td>';
    		html += '<td widht="6%">' + entryInfo["finishRate"] + '</td>';
    		html += '<td width="5%">' + entryInfo["urge"] + '</td>';
    		html += '<td widht="10%">' + entryInfo["updateDate"] + '</td>';
    	  	html += '</tr>';
    	 	<% k++;%>;
    		return html;
	   }
	
         
     	function  GetSelIds(){
			var idList="";
			var  em= document.all.tags("input");
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
			var   em=document.all.tags("input");
			for(var  i=0;i<em.length;i++){
				if(em[i].type=="checkbox")
			    	em[i].checked=chkAll.checked
			}
		}

		function del() {
			var idList=GetSelIds();
		  	if(idList=="") {
			  	alert('<s:text name="errorMsg.notInputDelete" />');
				return false;
		  	}
			if(confirm('确认删除吗?')){
			 	var strUrl="/pages/TestRecord/TestRecordinfo!delete.action?ids="+idList;
			 	var returnValue=OpenModal(strUrl,"600,380,operInfo.delete,um")
			   	query();
			}
		}
	
		function modify(ids){
			if(ids!=undefined){
				var strUrl="/pages/TestRecord/TestRecordinfo!edit.action?ids="+ids;
			  	var features="1000,650,tmlInfo.updateTitle,tmlInfo";
				var resultvalue = OpenModal(strUrl,features);
			    query();
			}else{
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
				  	var strUrl="/pages/TestRecord/TestRecordinfo!edit.action?ids="+itemId;
				  	var features="1000,650,tmlInfo.updateTitle,tmlInfo";
					var resultvalue = OpenModal(strUrl,features);
				    query();
				}
			}
		}
	
	function add(){
		var resultvalue = OpenModal('/pages/TestRecord/TestRecordinfo!add.action','1000,750,tmlInfo.addTmlTitle,tmlInfo');
	  if(resultvalue!=null)
	  		query();
	
	}
		function show(id) {
			var strUrl="/pages/TestRecord/TestRecordinfo!show.action?ids="+id;
			var features="1000,450,transmgr.traninfo,watch";
             var resultvalue = OpenModal(strUrl,features); 
		}  
		function openURL(url){
			window.open(url);
	}
	function validateInputStart(monthVal){
	var re=/^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$/g; 
  	var thisDate = document.getElementById(monthVal).value.trim();
		if(thisDate.length>0){
		var a = re.test(thisDate);
		if(!a){
			//alert('日期格式不正确,请使用日期选择!');
			return false;
			}
		 }
		  return true;
		 }
	function validateInputStartMonth(monthVal){
	var re=/^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012]))|(((1[6-9]|[2-9]\d)\d{2})-0?2)|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2))$/g; 
  	var thisDate = document.getElementById(monthVal).value.trim();
		if(thisDate.length>0){
		var a = re.test(thisDate);
		if(!a){
			//alert('日期格式不正确,请使用日期选择!');
			return false;
			}
		 }
		  return true;
		 }
	
		function startMonthHdl()
		{
			
			var checkMonthStartFlag = true;
			var dayFlag = false;
			var monthFlag = false;
			if(document.getElementById("upMonth").value == "")
			{
		
				checkMonthStartFlag = true;
				return true;
			}
			else if(document.getElementById("upMonth").value != "")
			{
			
			/*	if(validateInputStart("upMonth"))
				{
		
					dayFlag = true;
				}
				else if(validateInputStartMonth("upMonth"))
				{

					monthFlag = true;
				}
				else
				{
		
					checkMonthStartFlag = false;
					return false;
				}*/
				dayFlag = true;
				monthFlag = true;
				if(dayFlag)
				{
				
					var str = document.getElementById("upMonth").value.split("-");
					if(str[1] != "10"&& str[1]!="11" && str[1]!="12")
					{
						document.getElementById("upMonth").value = parseInt(str[0],10) + "-" + "0" + parseInt(str[1],10);
					}
					else
					{
						document.getElementById("upMonth").value = parseInt(str[0],10) + "-" + parseInt(str[1],10);
					}
				}
				checkMonthStartFlag = true;
				
				return true;
			}
		}
        
		
	$(function(){
		$("td[name='titleTd']").each(function(){
			if($(this).html().length>15){
				$(this).html($(this).html().substring(0,15)+'...');
			}
		});
	});
	</script>
<body id="bodyid" centermargin="0" topmargin="10">
	<s:form name="certificationInfoForm" namespace="/pages/TestRecord"
		action="TestRecordinfo!list.action" method="post">
		<input type="hidden" name="pageNum" id="pageNum" value="1" />
		<%@include file="/inc/navigationBar.inc"%>
		<input type="hidden" name="menuid"
			value="<%=request.getParameter("menuid")%>">
		<table width="100%" height="80" cellSpacing="0" cellPadding="0"
			class='selectTableBackground'>
			<tr>
				<td height="83">
					<fieldset width="100%">
						<legend></legend>
						<table width="100%">
							<tr>
								<td align="right" width="10%">序号:</td>
								<td align="left" width="23%"><input name="serialNo" id="serialNo" size="40" type="text" class=""></td>
								<td align="right" width="10%">模块:</td>
								<td align="left" width="23%"><input name="searchModualName" id="searchModualName" size="40" type="text" class=""></td>
								<td align="right" width="10%">标题:</td>
								<td align="left" width="23%">
									<input name="searchTitle" id="searchTitle" size="40" type="text"  class="">
								</td>
							</tr>
							<tr>
								<td width="10%" height="27" align="right">指派给:</td>
								<td width="23%" align="left" >
									<select name="searchSoloveMan" id="searchSoloveMan" style="width:150px">
										<option value="">---- 请选择 ----</option>
										<option value="陈天水">陈天水</option>
										<option value="卢海燕">卢海燕</option>
										<option value="涂玲">涂玲</option>
										<option value="曾志慧">曾志慧</option>
										<option value="李平">李平</option>
										<option value="汪腾蛟">汪腾蛟</option>
										<option value="徐文山">徐文山</option>
										<option value="吴杰">吴杰</option>
										<option value="郑权盛">郑权盛</option>
										<option value="张王府">张王府</option>
									</select>
								</td>
								
								<td width="10%" align="right">状态:</td>
								<td width="23%" align="left">
									<select name="searchStatus" id="searchStatus" style="width:150px">
										<option value="">---- 请选择 ----</option>
										<option value="新建">新建</option>
										<option value="已指派">已指派</option>
										<option value="已解决">已解决</option>
										<option value="已拒绝">已拒绝</option>
										<option value="测试通过">测试通过</option>
										<option value="测试未通过">测试未通过</option>
										<option value="已关闭">已关闭</option>
									</select>
								</td>
								<td width="10%" align="right">缺陷等级:</td>
								<td width="23%" align="left">
									<select name="buglevel" id="buglevel" style='width: 150px'>
										<option value="" selected="true">---- 请选择 ----</option>
										<option value="致命">致命</option>
										<option value="严重">严重</option>
										<option value="一般">一般</option>
										<option value="警告">警告</option>
										<option value="建议">建议</option>
									</select>
								</td>
							</tr>
							<tr>	
								<td width="10%" height="27" align="right">提出日期:</td>
								<td width="23%" align="left">
									<input
										name="searchFindDate" id="searchFindDate" type="text" id="StartMonth" class="MyInput"
										isSel="true" isDate="true" onFocus="ShowDate(this)"
										dofun="ShowDate(this)" /></td>
								<td width="10%" height="27" align="right">解决日期:</td>
								<td width="23%" align="left">
									<input
									name="searchSoloveDate" id="searchSoloveDate"  type="text" id="EndMonth" class="MyInput"
									isSel="true" isDate="true" onFocus="ShowDate(this)"
									dofun="ShowDate(this)" /></td>
								<td width="10%" align="right">提出人:</td>
								<td width="23%" align="left">
									<select name="searchFindMan" id="searchFindMan" style="width:150px">
										<option value="">---- 请选择 ----</option>
										<option value="陈天水">陈天水</option>
										<option value="卢海燕">卢海燕</option>
										<option value="涂玲">涂玲</option>
										<option value="曾志慧">曾志慧</option>
										<option value="李平">李平</option>
										<option value="汪腾蛟">汪腾蛟</option>
										<option value="徐文山">徐文山</option>
										<option value="吴杰">吴杰</option>
										<option value="郑权盛">郑权盛</option>
										<option value="姚炜">姚炜</option>
										<option value="张王府">张王府</option>
									</select>
								</td>
							</tr>
							<tr>
								<td width="10%" align="right">紧急度:</td>
								<td width="23%" align="left">
									<select name="searchUrge" id="searchUrge" style="width:150px">
										<option value="">---- 请选择 ----</option>
										<option value="低">低</option>
										<option value="中">中</option>
										<option value="高">高</option>
										<option value="紧急">紧急</option>
									</select>
								</td>
								<td width="10%" align="right">部署状态:</td>
								<td width="23%" align="left">
									<select name="searchDeployStatus" id="searchDeployStatus" style="width:150px">
										<option value="">---- 请选择 ----</option>
										<option value="未部署">未部署</option>
											<option value="已部署">已部署</option>
									</select>
								</td>
								<td width="10%" align="right">更新人:</td>
								<td width="23%" align="left">
									<select name="searchUpdateMan" id="searchUpdateMan" style="width:150px">
										<option value="">---- 请选择 ----</option>
										<option value="陈天水">陈天水</option>
										<option value="卢海燕">卢海燕</option>
										<option value="涂玲">涂玲</option>
										<option value="曾志慧">曾志慧</option>
										<option value="李平">李平</option>
										<option value="汪腾蛟">汪腾蛟</option>
										<option value="徐文山">徐文山</option>
										<option value="吴杰">吴杰</option>
										<option value="郑权盛">郑权盛</option>
										<option value="张王府">张王府</option>
									</select>
								</td>
							</tr>
							<tr>
								<td colspan="6" style="text-align: right;">
									<tm:button site="1" />
								</td>
							</tr>
						</table>
					</fieldset>
				</td>
			</tr>
		</table>
		<br />

		<table width="100%" height="23" border="0" cellspacing="0"
			cellpadding="0" background="../../images/main/bgtop.gif">
			<tr>
				<td width="25" height="23" valign="middle">&nbsp;<img
					src="../../images/share/list.gif" width="14" height="16"></td>
				<td class="orarowhead"><s:text name="operInfo.title" /></td>
				<td align="right" width="75%"><tm:button site="2"></tm:button></td>
			</tr>
		</table>
		<table width="100%" border="0" cellpadding="1" cellspacing="1"
			bgcolor="#000066">
			<tr bgcolor="#FFFFFF">
				<td>
					<table width="100%" cellSpacing="0" cellPadding="0">
						<tr>
							<!--<td width="6%"> 
						<div align="center"><input type="checkbox" name="chkList"  id="chkAll"   value="all"  onClick="SelAll(this)"></div> 
					</td>
					<td width="11%">
						<div align="center"><label for=chkAll><s:text name="operInfo.checkall" /></label></div>
 					</td>-->
							<td width="83%" align="right">
								<div id="pagetag">
									<tm:pagetag pageName="currPage"
										formName="certificationInfoForm" />
								</div>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<table width="100%" border="0" cellpadding="1" cellspacing="1"
			bgcolor="#000066" id=downloadList>
			<tr>
				<td nowrap width="3%" class="oracolumncenterheader"></td>
				<td nowrap width="7%" class="oracolumncenterheader"><div
						align="center">编号</div></td>
				<td nowrap width="10%" class="oracolumncenterheader"><div
						align="center">模块</div></td>
				<td nowrap width="15%" class="oracolumncenterheader"><div
						align="center">标题</div></td>
				<td nowrap width="7%" class="oracolumncenterheader">提出人</td>
				<td nowrap width="8%" class="oracolumncenterheader">提出日期</td>
				<td nowrap width="7%" class="oracolumncenterheader">指派给</td>
				<td nowrap width="8%" class="oracolumncenterheader">解决日期</td>
				<td nowrap width="8%" class="oracolumncenterheader"><div
						align="center">状态</div></td>
				<td nowrap width="8%" class="oracolumncenterheader">缺陷等级</td>
				<td nowrap width="5%" class="oracolumncenterheader"><div
						align="center">完成度</div></td>
				<td nowrap width="5%" class="oracolumncenterheader"><div
						align="center">紧急度</div></td>
				<td nowrap width="10%" class="oracolumncenterheader"><div
						align="center">更新日期</div></td>
			</tr>
			<tbody name="formlist" id="formlist" align="center">
				<s:iterator value="questionList" id="tranInfo" status="row">
					<s:if test="#row.odd == true && id==null">
						<tr id="tr" oriClass="" onMouseOut=TrMove(this)
							onMouseOver=TrMove(this) bgcolor="#6699FF">
					</s:if>
					<s:else>
						<tr id="tr" class="trClass1" oriClass="" onMouseOut=TrMove(this)
							onMouseOver=TrMove(this)>
					</s:else>
					<td nowrap align="center"><s:if test="id!=null">
							<input type="checkbox" name="chkList"
								value='<s:property value="id"/>' />
						</s:if></td>
					<td align="center"><a
						href='javascript:modify("<s:property value="id"/>")'><font
							color="#3366FF"> <s:property value="serialNo" /></font> </a>
					<td align="center"><div align="center">
							<s:property value="modualName" /></td>
					<td align="center" name='titleTd'>
						<s:property value="title" />
					</td>
					<td align="center"><s:property value="findMan" /></td>
					<td align="center"><s:date name="findDate" format="yyyy-MM-dd" /></td>
					<td align="center"><s:property value="soloveMan" /></td>
					<td align="center"><s:date name="soloveDate"
							format="yyyy-MM-dd" /></td>
					<td align="center"><s:property value="questionStatus" /></td>
					<td align="center"><s:property value="buglevel" /></td>
					<td align="center"><s:property value="finishRate" /></td>
					<td align="center"><s:property value="urge" /></td>
					<td align="center"><s:property value="updateDate" /></td>
					</tr>
				</s:iterator>
			</tbody>
		</table>
	</s:form>
</body>
</html>
