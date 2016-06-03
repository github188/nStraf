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
<%-- <script type="text/javascript" src="../../js/jquery.js"></script> --%>
<%@ include file="/inc/pagination.inc"%>
<script type="text/javascript">
            function query(){
				var uname = document.getElementById("uname").value;
				var courseName = document.getElementById("courseName").value;
				var start = document.getElementById("start").value;
				var end = document.getElementById("end").value;
				var category = document.getElementById("category").value;
 				//var showdiv = document.getElementById("showdiv");
           		//showdiv.style.display = "block";
				var pageNum = document.getElementById("pageNum").value;
				if(!compareDate(start,end)){
					return ;
				}
				var actionUrl = "<%=request.getContextPath()%>/pages/ec/ecinfo!refresh.action?from=refresh&pageNum="+pageNum;
				actionUrl += "&uname="+uname;
				actionUrl += "&courseName="+courseName;
				actionUrl += "&category="+category;
				actionUrl += "&start="+start;
				actionUrl += "&end="+end;
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
				html += '<td align="center"><a href='+str+'><font color="#3366FF">' +entryInfo["uname"] + '</font></a></td>';
				html += '<td>' +entryInfo["cid"] + '</td>';
				html += '<td align="center">' +entryInfo["courseName"] + '</td>';
				html += '<td>' +entryInfo["category"] + '</td>';
				html += '<td>' +entryInfo["planFinishDate"] + '</td>';
							
				html += '<td>' +entryInfo["graspStandard"] + '</td>';
				html += '<td align="center">' +entryInfo["prioryLevel"] + '</td>';
				html += '<td align="center">' +entryInfo["finishPercent"] + '</td>';
				html += '<td align="center">' +entryInfo["actualFinishDate"] + '</td>';
				html += '<td align="center">' +entryInfo["finishEffect"] + '</td>';
				html += '</tr>';			
		 		<%k++;%>;  
				//showdiv.style.display = "none";   
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
			 	var strUrl="/pages/ec/ecinfo!delete.action?ids="+idList;
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
			  	var strUrl="/pages/ec/ecinfo!edit.action?ids="+itemId;
			  	var features="1000,350,tmlInfo.updateTitle,tmlInfo";
				var resultvalue = OpenModal(strUrl,features);
				refreshList();
			}
		}
		
		
		
	function add(){
		var resultvalue = OpenModal('/pages/ec/ecinfo!add.action','1000,350,tmlInfo.addTmlTitle,tmlInfo');

		refreshList();
	}
	
	function show(id) {
			var strUrl="/pages/ec/ecinfo!show.action?ids="+id;
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
					alert('计划完成开始日期大于计划完成结束日期，请重新输入！');
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
	
	</script>
<body id="bodyid" style="margin: 0">
	<s:form name="certificationInfoForm" namespace="/pages/trip"
		action="tripinfo!list.action" method="post">
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
							<td class="input_label" width="5%" height="21" align="center">姓名:</td>
							<td width="6%"><input name="uname" type="text"
								id="uname" size="10" maxlength="10"></td>
							<td class="input_label" width="6%" align="center">课程名称:</td>
							<td width="8%"><input name="courseName" type="text"
								id="courseName" size="15" maxlength="30">
							</td>
							<td class="input_label" width="5%" align="center">类别:</td>
							<td width="7%" align="center"><select name="category"
								id="category">
									<option value="">全选</option>
									<option value="必修">必修</option>
									<option value="选修">选修</option>
							</select>
							</td>
							<td class="input_label" width="7%" align="right">计划完成日期:</td>
							<td width="11%" align="left"><input name="start" type="text"
								id="start" size="15" class="MyInput" readonly="readonly" /></td>
							<td width="2%">至</td>
							<td width="11%" align="left"><input name="end" type="text"
								id="end" size="15" class="MyInput" readonly="readonly" /></td>
							<td width="10%" align="right"><tm:button site="1" /></td>
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
<!-- 		<div id="showdiv"
			style='display: none; z-index: 999; background: white; height: 39px; line-height: 50px; width: 42px; position: absolute; top: 236px; left: 670px;'>
			<img src="../../images/loading.gif">
		</div> -->

		<table width="100%" border="0" cellpadding="1" cellspacing="1"
			bgcolor="#000066" id=downloadList>
			<tr>
				<td nowrap width="2%" class="oracolumncenterheader"></td>
				<td width="6%" nowrap class="oracolumncenterheader">员工姓名</td>
				<td nowrap width="5%" class="oracolumncenterheader">课程编号</td>
				<td nowrap width="24%" class="oracolumncenterheader">课程名称</td>
				<td nowrap width="4%" class="oracolumncenterheader">类别</td>
				<td nowrap width="9%" class="oracolumncenterheader">计划完成日期</td>
				<td width="7%" nowrap class="oracolumncenterheader"><div
						align="center">掌握标准</div></td>
				<td width="6%" nowrap class="oracolumncenterheader">优先级</td>
				<td width="6%" nowrap class="oracolumncenterheader">完成百分比</td>
				<td nowrap width="10%" class="oracolumncenterheader"><div
						align="center">实际完成日期</div></td>
				<td nowrap width="13%" class="oracolumncenterheader"><div
						align="center">完成效果</div></td>
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
						name="chkList" value='<s:property value="id"/>' />
					</td>

					<td align="center">
						<div align="center">
							<a href='javascript:show("<s:property value="id"/>")'><font
								color="#3366FF"> <s:property value="uname" /> </font> </a>
						</div>
					</td>
					<td align="center"><s:property value="cid" /></td>
					<td align="center" style="white-space: nowrap;"><span
						title='<s:property value="courseName"/>'> <s:if
								test="courseName.length()>20">
								<s:property value="courseName.substring(0,20)+'...'" />
							</s:if>
							<s:else><s:property value="courseName"/></s:else>
							 <span>
					</td>
					<td align="center"><s:property value="category" /></td>

					<td align="center"><s:date name="planFinishDate"
							format="yyyy-MM-dd" /></td>
					<td align="center"><s:property value="graspStandard" /></td>
					<td align="center"><s:property value="prioryLevel" /></td>
					<td align="center"><s:property value="finishPercent" /></td>
					<td align="center"><s:date name="actualFinishDate"
							format="yyyy-MM-dd" /></td>
					<td align="center"><s:property value="finishEffect" /></td>

					</tr>
				</s:iterator>
			</tbody>
		</table>
		<table width="100%" border="0" bgcolor="#FFFFFF" cellpadding="1"
			cellspacing="1">
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
