<!--20100107 14:00-->
<%@ page contentType="text/html; charset=UTF-8" %>
<%@page import="cn.grgbanking.feeltm.login.domain.UserModel"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa" %>
<html>
<head><title>tool query</title></head>
	<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
	<script type="text/javascript" src="../../js/tablesort.js"></script>
	<script type="text/javascript" src="../../calendar/fixDate.js"></script>
	<script type="text/javascript" src="../../js/aa.js"></script>
	<script type="text/javascript" src="../../js/jquery.js"></script>
	<%@ include file="/inc/pagination.inc"%>
	
	<!-- 	private String username;  //对应用户表中主键id
	private String start;
	private String end;
	private String prjName; -->
	<script type="text/javascript"> 
	var g_query = true;
	function OpenModal3(strHref,strCustom)
	{
	
	  var aryCustom=strCustom.split(",");
	  var width=aryCustom.length>0?aryCustom[0]:300
	  var height=aryCustom.length>1?aryCustom[1]:300
	  var title=aryCustom.length>2?aryCustom[2]:""
	  var bundle=aryCustom.length>3?aryCustom[3]:""
	  return OpenModal4(strHref,width,height,title,bundle)	
	}
	
	function OpenModal4(strHref,width,height,title,bundle)
	{
	   var strContextPath='<%=request.getContextPath()%>'	
	   if(strHref.indexOf('http')<0)
	   {
		  if(strHref.substr(0,1)=='/')	
			strHref=strContextPath+strHref
		  else  
			strHref=strContextPath+"/"+strHref
	   }
	   //alert(title);  
	   if(title==null)
		 title=""
		 
	   if(bundle==null)
		 bundle=""
		 
	   strHref=strContextPath+"/modalDialog.jsp?urlStr="+strHref+"&title="+title
	
	   if(bundle!="")
	   {
		strHref=strHref+"&bundle="+bundle	
	   } 	
	  strHref = encodeURI(strHref);
	  return window.showModalDialog(strHref,window,"dialogWidth:"+width+"px;dialogHeight:"+height+"px;resizable:no;scroll:0;help:0;status:0")
	}
	
		function document.onkeydown()
		{	
			var e=event.srcElement;        	
			if(event.keyCode==13) 
			{
				document.getElementById("bodyid").focus();
				query();
			}
		 }
		 
		 String.prototype.trim = function()
		{
			return this.replace(/(^\s*)|(\s*$)/g, "");
		}
		 function delay()
		 {
		 	g_query = true;
		 }
		 
		function query(){
			if(!validateInputInfo()){
				return ;
			}
			if(!g_query)
			{
				return ;
			}
			g_query = false;
			var start =  document.getElementById("start").value;
			var end =  document.getElementById("end").value;
			var pageNum = document.getElementById("pageNum").value;
			var prjName = document.getElementById("prjName1").value;
			var designerName = document.getElementById("designerName").value;
			var showdiv = document.getElementById("showdiv");
           		showdiv.style.display = "block";
			prjName = prjName.replace("+","$jia$");
			var actionUrl = "<%=request.getContextPath()%>/pages/designer/designerinfo!refresh.action?from=refresh&pageNum="+pageNum;
			actionUrl += "&prjName="+prjName;
			actionUrl += "&designerName="+designerName;
			actionUrl += "&start="+start+"&end="+end;
			actionUrl=encodeURI(actionUrl);
			var method="setHTML";
			<%int k = 0 ; %>
	   		sendAjaxRequest(actionUrl,method,pageNum,true);
			setTimeout("delay();",3000); 
		}
		function setHTML(entry,entryInfo){
				var html = '';
				//var id=entryInfo["id"];
				var prjName=entryInfo["prjName"];
				var designerName = entryInfo["name"];
			var start =  document.getElementById("start").value;
			var end =  document.getElementById("end").value;
				var tmpAllUrl =  designerName + "\",\"" + prjName +"\",\""+start+"\",\""+end ;
				var str = "javascript:show(\"" + tmpAllUrl + "\")";
			html += '<tr class="trClass<%=k%2 %>" oriClass="" align="center" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>';
			/*html += '<td align="center">';
			html += '<input type="checkbox" name="chkList" value="' + entryInfo["id"] + '" />';
			html += '</td>';*/
			//html += '<td align="center"><a href='+str+'><font color="#336699">' +entryInfo["prjName"] + '</font></a></td>';
			html += '<td align="center"><a href=\''+str+'\'><font color="#336699">' +entryInfo["prjName"] + '</font></a></td>';
			html += '<td>' +entryInfo["reqCoveredRate"] + '</td>';
			html += '<td>' +entryInfo["caseBugNum"] + '</td>';
			html += '<td>' +entryInfo["caseBugResolveRate"] + '</td>';
			html += '<td>' +entryInfo["caseLowRate"] + '</td>';
			html += '<td>' +entryInfo["caseValidRate"] + '</td>';
	  		html += '</tr>';
	 	<% k++;%>;
		showdiv.style.display = "none";
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
		
		
				
		function show(name,prjName,start,end) {
		if(start==null || start=="")
		{
			start = "fq";
		}
		if(end==null || end=="")
		{
			end = "fq";
		}
		prjName = prjName.replace("(","（").replace(")","）");
		prjName = prjName.replace("+","$jia$");
		var strUrl="/pages/designer/designerinfo!show.action?req="+start+"@"+end+"@"+ prjName +"@"+name+"@";
		var features="800,350,transmgr.traninfo,watch";
		var resultvalue = OpenModal3(strUrl,features);
		}
		function openURL(url){
			window.open(url);
		}
		function validateInputInfo(){
	var re=/^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$/g;   	  
    var re1=/^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$/g; 
  	var thisDate = reportInfoForm.start.value.trim();
	var endDate = reportInfoForm.end.value.trim();
		if(thisDate.length>0&&endDate.length>0){
		var v = re.test(endDate);
		var a = re1.test(thisDate);
		if(!v||!a){
			alert('日期格式不正确,请使用日期选择!');
			return false;
			}
		 
		 if(!DateValidate('start','end'))
		 {
		 	alert('开始日期大于结束日期，请重新输入！');
		 	return false;
		 }
	 }else if(thisDate.length>0&&endDate.length==0){
	     
		  var a = re1.test(thisDate);
		if(!a){
			alert('日期格式不正确,请使用日期选择!');
			return false;
			}
	  }else if(thisDate.length==0&&endDate.length>0){
	      var v = re.test(endDate);
		if(!v){
			alert('日期格式不正确,请使用日期选择!');
			return false;
			}
	  }
	  return true;
	 }
	 
	 function DateValidate(beginDate, endDate){
	
			var Require= /.+/;
		
			var begin=document.getElementsByName(beginDate)[0].value.trim();
			var end=document.getElementsByName(endDate)[0].value.trim();
		
			var flag=true;
		
			/*if(Require.test(begin) && Require.test(end))
				if( begin > end)
					flag = false;*/
			if(Require.test(begin) && Require.test(end))
			{
				var beginStr = begin.split("-");
				var endStr = end.split("-");
				if(parseInt(beginStr[0], 10) > parseInt(endStr[0], 10))
				{
					flag = false;
				}
				else if(parseInt(beginStr[0], 10) == parseInt(endStr[0], 10))
				{
					if(parseInt(beginStr[1], 10) > parseInt(endStr[1], 10))
					{
						flag = false;
					}
					else if(parseInt(beginStr[1], 10) == parseInt(endStr[1], 10))
					{
						if(parseInt(beginStr[2], 10) > parseInt(endStr[2], 10))
						{
							flag = false;
						}
					}
				}
			}
		   return flag;
		
		}

		 function searchPrj()
		{
			var url="../prjdetail/prjdetailinfo!queryPrjs.action";
			var params="";
			jQuery.post(url, params, $(document).callbackFun1, 'json');
			document.getElementById("prjName1").disabled = true;
			//document.getElementById("testerName1").disabled = true;
			//url="prjtestinfo!queryTesterNames.action";
			//jQuery.post(url, params, $(document).callbackFun2, 'json');
		}
		
		$.fn.callbackFun1=function (json)
		 {	
		 	document.getElementById("prjName1").disabled = false;
		  	$("#prjName1 option").remove();
		  	if(json!=null&&json.length>0)
			{	
				for(var i=0;i<json.length;i++)
				{
					 $("#prjName1").append("<option value='"+json[i]+"'>"+json[i]+"</option>");
				 }
				 document.getElementById("prjName1").focus();
				 document.getElementById("prjName1").value = "全选";
				 document.getElementById("bodyid").focus();
			}
			
		 }
		 
		 function searchType()
		{
			var url="../prjdetail/prjdetailinfo!selectDB.action";
			var params={prjType1:$("#prjType1").val()};;
			jQuery.post(url, params, $(document).callbackFun3, 'json');
			//url="prjtestinfo!queryTesterNames.action";
			//jQuery.post(url, params, $(document).callbackFun4, 'json');
			document.getElementById("prjName1").disabled = true;
		}
		$.fn.callbackFun3=function (json)
		 {
			var params={prjType1:$("#prjType1").val()};
		 	document.getElementById("prjName1").disabled = false;	
		  	$("#prjName1 option").remove();
		  	if(json!=null&&json.length>0)
			{	
				for(var i=0;i<json.length;i++)
				{
					 $("#prjName1").append("<option value='"+json[i]+"'>"+json[i]+"</option>");
				 }
			
				 document.getElementById("prjName1").focus();
				 document.getElementById("prjName1").value = "全选";
				 document.getElementById("bodyid").focus();
			}	
		 }
	
	</script>
 <body id="bodyid" leftmargin="0" topmargin="0" onLoad="searchPrj();"><br>
 	<s:form name="reportInfoForm"  namespace="/pages/designer" action="prjdetailinfo!list.action" method="post" >
  	<input type="hidden" name="pageNum" id="pageNum"  value="1"/>
	<%@include file="/inc/navigationBar.inc"%>
	<input type="hidden" name="menuid" value="<%=request.getParameter("menuid")%>">
 	<table width="100%" cellSpacing="0" cellPadding="0" class="selectTableBackground"> 
 		<tr>
 		<td>
			<fieldset  width="100%">
			<legend></legend>
		<table width="100%" >
              <tr>
                <td width="10%" align="center">开始时间：</td>
				<td width="25%"><input name="start"  type="text" id="start" size="22" class="MyInput"   isSel="true" isDate="true" onFocus="ShowDate(this)" dofun="ShowDate(this)" /> </td>
				<td width="10%" align="center">结束时间：</td>
				<td width="25%"><input name="end"  type="text" id="end" size="22" class="MyInput"   isSel="true" isDate="true" onFocus="ShowDate(this)" dofun="ShowDate(this)" /></td>
                <td width="10%" align="center">项目类型：</td>
                <td width="15%">
               <s:select name="prjType1" id="prjType1" theme="simple" list="prjTypes" cssStyle="width:100px;" onchange="searchType();"></s:select>
               <!-- <select name="prjType1" id="prjType1" onChange="searchType()" style="width:100px">
                <option value="ATMC" selected>ATMC</option>
                <option value="DevDll">DevDll</option>
                <option value="SP">SP</option>
                <option value="View">FEEL View</option>
                <option value="Sith">FEEL Switch</option>
                <option value="SECOne">SECOne</option>
                <option value="LiquidDate">F@ST LiquidDate</option>
                <option value="TellerMaster">TellerMaster</option>
                <option value="dongbao">东保押运综合业务系统</option>
                </select>--></td>
			  </tr>
			   <tr>
				<td align="center">项目名称：</td>
                <td >
                	<select name="prjName1" id="prjName1"  style="width:165px;"></select>
                </td>
                <td align="center">设计人员：</td>
                <td>
                	<s:select name="designerName" id="designerName" theme="simple" list="map" cssStyle="width:70px;"></s:select>
                </td>
                <td align="center" >&nbsp;</td>
               	<td >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<tm:button site="1"/></td>
			  </tr>
			</table> 
			</fieldset>  
		</td> 
		</tr>
	</table><br />
	<table width="100%" height="23" border="0" cellspacing="0" cellpadding="0" background="../../images/main/bgtop.gif">
			<tr>
				 <td width="25"  height="23" valign="middle">&nbsp;<img src="../../images/share/list.gif" width="14" height="16"></td>
				 <td class="orarowhead"><s:text name="operInfo.title" /></td>
				 <td align="right" width="75%"><tm:button site="2"></tm:button></td>
			</tr>
		</table>
		<div id="showdiv" style='display:none;z-index:999; background:white;  height:39px; line-height:50px; width:42px;  position:absolute; top:236px; left:670px;'><img src="../../images/loading.gif"></div>
		<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066">  
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
						<div id="pagetag"><tm:pagetag pageName="currPage" formName="reportInfoForm" /></div>
					</td>
				</tr>
				</table>
			</td>
			</tr>
		</table>
	<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066" id=downloadList>
<tr> 
			<td nowrap width="15%" class="oracolumncenterheader"><div align="center">项目名称</div></td>
            <td nowrap width="6%" class="oracolumncenterheader"><div align="center">需求覆盖率</div></td>
            <td nowrap width="6%" class="oracolumncenterheader"><div align="center">用例缺陷数</div></td>
            <td nowrap width="6%" class="oracolumncenterheader"><div align="center">用例缺陷解决率</div></td>
            <td nowrap width="6%" class="oracolumncenterheader"><div align="center">用例漏出率</div></td>
            <td nowrap width="6%" class="oracolumncenterheader"><div align="center">用例有效性</div></td>
  </tr>
		<tbody name="formlist" id="formlist"> 
  		<s:iterator  value="designerList" id="tranInfo" status="row">
  		<s:if test="#row.odd == true"> 
 		<tr id="tr" class="trClass0" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
 		</s:if><s:else>
 		<tr id="tr" class="trClass1" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
 		</s:else> 
			<td><div align="center">
				<a href='javascript:show("<s:property value="name"/>","<s:property value="prjName"/>","<s:property value="start"/>","<s:property value="end"/>")'>
					<font color="#336699"><s:property value="prjName"/></font>
		  		</a>
		  	</div></td>
            <td align="center"><s:property value="reqCoveredRate"/></a></td>
            <td align="center"><s:property value="caseBugNum"/></a></td>
            <td align="center"><s:property value="caseBugResolveRate"/></a></td>
            <td align="center"><s:property value="caseLowRate//"/></a></td>
            <td align="center"><s:property value="caseValidRate"/></a></td>
    </tr>
		</s:iterator>  
 		</tbody> 
 	</table> 
 </s:form>
 <script type="text/javascript">
 	document.getElementById("prjType1").value ='<%=((cn.grgbanking.feeltm.domain.testsys.ProjectDB)session.getAttribute("globalDB")).getPrjName()%>';
 </script>
</body>
</html>



