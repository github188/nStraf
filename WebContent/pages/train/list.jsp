<!--20100107 14:00-->
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
<title>tool query</title>
</head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../calendar/fixDate.js"></script>
<script type="text/javascript" src="../../js/aa.js"></script>
<%@ include file="/inc/pagination.inc"%>
<script type="text/javascript">
			
            function query(){
				var student = document.getElementById("student").value;
				var category = document.getElementById("category").value;
				var courseName = document.getElementById("courseName").value;
				var teacher = document.getElementById("teacher").value;
				var start = document.getElementById("start").value;
				var end = document.getElementById("end").value;
				var showdiv = document.getElementById("showdiv");
				var pageNum = document.getElementById("pageNum").value;
				if(!compareDate(start,end)){
					return;
				}
				var actionUrl = "<%=request.getContextPath()%>/pages/train/traininfo!refresh.action?from=refresh&pageNum="+pageNum;
				actionUrl += "&student="+student;
				actionUrl += "&category="+category;
				actionUrl += "&courseName="+courseName;
				actionUrl += "&teacher="+teacher;
				actionUrl += "&start="+start;
				actionUrl += "&end="+end;
				actionUrl=encodeURI(actionUrl);
				var method="setHTML";
				<%int k = 0 ; %>
		   		sendAjaxRequest(actionUrl,method,pageNum,true);
            }
         function setHTML(entry,entryInfo){
				var html = '';
				var str = "javascript:show(\""+entryInfo["id"]+"\")";
				html += '<tr class="trClass<%=k%2 %>" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>';
				html += '<td>';
				html += '<input type="checkbox" name="chkList" value="' + entryInfo["id"] + '" />';
				html += '</td>';
				html += '<td align="center"><a href='+str+'><font color="#3366FF">' +entryInfo["trainDate"] + '</font></a></td>';
				var courseName=entryInfo["courseName"];
				html += '<td align="left" title='+courseName+'>' + courseName +'</td>';
				html += '<td align="center">' +entryInfo["category"] + '</td>';
				var teacher=entryInfo["teacher"];
				html += '<td align="left" title='+teacher+'>' + teacher +'</td>';
		        html += '<td>' +entryInfo["trainHour"] + '</td>';
			    html += '<td align="left">' +entryInfo["start"]+'-'+entryInfo["end"]+ '</td>';
			    var addr=entryInfo["addr"];
				html += '<td align="left" title='+addr+'>' + addr +'</td>';
				var student=entryInfo["student"];
				html += '<td align="left" title='+student+'>' + student +'</td>';
				 
				html += '<td align="center">' +entryInfo["updateMan"] + '</td>';
				html += '<td align="center">' +entryInfo["updateDate"].substr(0,11) + '</td>';
		  		html += '</tr>';
		 		<%k++;%>;    
				showdiv.style.display = "none"; 
				return html;
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
		
		function  SelAll(chkAll){
			//var   em=document.all.tags("input");
			var em= document.getElementsByName("chkList");
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
			 	var strUrl="/pages/train/traininfo!delete.action?ids="+idList;
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
			  	var strUrl="/pages/train/traininfo!edit.action?ids="+itemId;
			  	var features="1000,350,tmlInfo.updateTitle,tmlInfo";
				var resultvalue = OpenModal(strUrl,features);
				refreshList();
			}
		}
		
	function add(){
		var resultvalue = OpenModal('/pages/train/traininfo!add.action','1000,350,tmlInfo.addTmlTitle,tmlInfo');
	 	 if(resultvalue!=null)
	 		refreshList();
	}
	
	function show(id) {
			var strUrl="/pages/train/traininfo!show.action?ids="+id;
			var features="1000,350,transmgr.traninfo,watch";
             var resultvalue = OpenModal(strUrl,features); 
		}  
		  
		function openURL(url){
			window.open(url);
	      }
		function compareDate(startDate,endDate){
			var re = /^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$/g;
			var re1 = /^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$/g;
			if (startDate.length > 0 && endDate.length > 0) {
				var v = re.test(endDate);
				var a = re1.test(startDate);
				if (!v || !a) {
					alert('日期格式不正确,请使用日期选择!');
					return false;
				}
				if (!DateValidate(startDate,endDate)) {
					alert('培训开始日期不能小于培训结束日期，请重新输入！');
					return false;
				}
			} else if (startDate.length > 0 && endDate.length == 0) {
				var a = re1.test(startDate);
				if (!a) {
					alert('日期格式不正确,请使用日期选择!');
					return false;
				}
			} else if (startDate.length == 0 && endDate.length > 0) {
				var v = re.test(endDate);
				if (!v) {
					alert('日期格式不正确,请使用日期选择!');
					return false;
				}
			}
			return true;
		}

		function DateValidate(begin, end) {
			var Require = /.+/;
			var flag = true;
			if (Require.test(begin) && Require.test(end)) {
				var beginStr = begin.split("-");
				var endStr = end.split("-");
				if (parseInt(beginStr[0], 10) > parseInt(endStr[0], 10)) {
					flag = false;
				} else if (parseInt(beginStr[0], 10) == parseInt(endStr[0], 10)) {
					if (parseInt(beginStr[1], 10) > parseInt(endStr[1], 10)) {
						flag = false;
					} else if (parseInt(beginStr[1], 10) == parseInt(endStr[1], 10)) {
						if (parseInt(beginStr[2], 10) > parseInt(endStr[2], 10)) {
							flag = false;
						}
					}
				}
			}
			return flag;
		}
		function init(){
			//query();
		}
	</script>
<body id="bodyid" style="margin: 0" onload="init();">
	<s:form name="certificationInfoForm" namespace="/pages/train"
		action="traininfo!list.action" method="post">
		<input type="hidden" name="pageNum" id="pageNum" value="1" />
  		<input type="hidden" name="pageSize" id="pageSize" value="20" />
		<%@include file="/inc/navigationBar.inc"%>
		<input type="hidden" name="menuid"
			value="<%=request.getParameter("menuid")%>">
		<table width="100%" cellSpacing="0" cellPadding="0">
			<tr>
				<td>
					<table width="99%" class="select_area">
						<tr>
							<td class="input_label" height="25" width="5%" >课程名称:</td>
							<td><input name="courseName" type="text" id="courseName"
								/></td>
							<td class="input_label" >学员:</td>
							<td><input name="student"  type="text" id="student" /> <!--<input name="student" type="text" id="student"  class="MyInput">--></td>
							<td class="input_label" width="5%" >分类:</td>
							<td align="left" width="5%"><s:select list="{'内训','外训','外派'}"
									name="category" theme="simple" cssStyle="width:90px;"
									headerKey="" headerValue="--全部--" id="category"></s:select></td>
							<td class="input_label">讲师:</td>
							<td ><input name="teacher" type="text"
								id="teacher" /></td>

						</tr>
						<tr>
							<td class="input_label"  height="25" width="5%" >培训开始日期:</td>
							<td width="12%" nowrap="nowrap"><input name="start" type="text" id="start"
								size="18" class="MyInput" /></td>
							<td class="input_label"  height="25" width="5%" >培训结束日期:</td>
							<td width="8%" nowrap="nowrap"><input name="end" type="text" id="end"
								size="18" class="MyInput" /></td>
							<td width="5%" align="center">&nbsp;</td>
							<td width="5%" align="right">&nbsp;</td>
							<td width="5%" align="center">&nbsp;</td>
							<td width="14%" align="right"><tm:button site="1" /></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<br />

		<table width="100%" height="23" border="0" cellspacing="0"
			cellpadding="0" class="bgbuttonselect">
			<tr>
				<td width="25" height="23" valign="middle">&nbsp;</td>
				<td class="orarowhead"><s:text name="operInfo.title" /></td>
				<td align="right" width="75%"><tm:button site="2"></tm:button></td>
			</tr>
		</table>
		<div id="showdiv"
			style='display: none; z-index: 999; background: white; height: 39px; line-height: 50px; width: 42px; position: absolute; top: 236px; left: 670px;'>
			<img src="../../images/loading.gif">
		</div>

		<table width="100%" border="0" cellpadding="1" cellspacing="1"
			bgcolor="#000066" id=downloadList>
			<tr>
				<td nowrap width="2%" class="oracolumncenterheader"></td>
				<td width="8%" nowrap class="oracolumncenterheader">培训日期</td>
				<td nowrap width="14%" class="oracolumncenterheader">课程名称</td>
				<td nowrap width="4%" class="oracolumncenterheader">分类</td>
				<td nowrap width="6%" class="oracolumncenterheader"><div align="center">讲师</div></td>
				<td width="4%" nowrap class="oracolumncenterheader"><div align="center">时长</div></td>
				<td width="8%" nowrap class="oracolumncenterheader">时间</td>
				<td width="15%" nowrap class="oracolumncenterheader">地点</td>
				<td width="15%" nowrap class="oracolumncenterheader">参训人员</td>
				<td nowrap width="8%" class="oracolumncenterheader"><div align="center">最近更新者</div></td>
				<td nowrap width="" class="oracolumncenterheader"><div align="center">最近更新日期</div></td>
			</tr>
			<tbody name="formlist" id="formlist" align="center">
				<s:iterator value="list" id="tranInfo" status="row">
					<s:if test="#row.odd == true">
						<tr id="tr" class="trClass0" oriClass="" onMouseOut=TrMove(this)
							onMouseOver=TrMove(this)>
					</s:if>
					<s:else>
						<tr id="tr" class="trClass1" oriClass="" onMouseOut=TrMove(this)
							onMouseOver=TrMove(this)>
					</s:else>
					<td nowrap align="center"><input type="checkbox"
						name="chkList" value='<s:property value="id"/>' /></td>
					<td align="center">
						<div align="center">
							<a href='javascript:show("<s:property value="id"/>")'><font
								color="#3366FF"> <s:date name="trainDate"
										format="yyyy-MM-dd" /></font> </a>
						</div>
					</td>
					<td align="left"><s:property value="courseName" /></td>
					<td align="center"><s:property value="category" /></td>
					<td align="center"><s:property value="teacher" /></td>
					<td align="center"><s:property value="trainHour" /></td>
					<td align="left"><s:property value="start" />-<s:property
							value="end" /></td>
					<td align="left"><s:property value="addr" /></td>
					<td align="left"><s:property value="student" /></td>
					<td align="center"><s:property value="updateMan" /></td>
					<td align="center"><s:property value="updateDate" /></td>
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
	</s:form>
</body>
</html>
