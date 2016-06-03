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
	  //alert(title);
	 
	  return OpenModal4(strHref,width,height,title,bundle)	
	}


	function showDateMonth1()
	{   
	   ShowDate(form1.start);
	   var yymmdd = form1.start.value;
	   var yymm = yymmdd.substring(0,10);
	   form1.start.value=yymm;
	 
	}

	function showDateMonth2()
	{   
	   ShowDate(form1.end);
	   var yymmdd = form1.end.value;
	   var yymm = yymmdd.substring(0,10);
	   form1.end.value=yymm;
	 
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
	
				/*if(!validateInputInfo()){
				return ;
			}
				if(!g_query)
			{
				return ;
			}
		*/
			g_query = false;
			var start =  document.getElementById("start").value;
			var end =  document.getElementById("end").value;
			var pageNum = document.getElementById("pageNum").value;
			var prjName = document.getElementById("prjName1").value;
			
		//	var versionNO = document.getElementById("versionNO1").value;
			prjName = prjName.replace("+","$jia$");
			//versionNO = versionNO.replace("+","$jia$");
			var actionUrl = "<%=request.getContextPath()%>/pages/prjdetail/prjdetailinfo!refresh.action?from=refresh&pageNum="+pageNum;
			actionUrl += "&prjName="+prjName;
		//	actionUrl += "&versionNO="+versionNO;
			actionUrl += "&start="+start+"&end="+end;
			actionUrl=encodeURI(actionUrl);
			var method="setHTML";
			<%int k = 0 ; %>
	   		sendAjaxRequest(actionUrl,method,pageNum,true);
			setTimeout("delay();",3000); 
		}
		function setHTML(entry,entryInfo){
				var html = '';
				var prjName=entryInfo["prjName"];
				var staticMonth=entryInfo["staticMonth"];
				var start11=entryInfo["start"];
				
			var start =  document.getElementById("start").value;
			var end =  document.getElementById("end").value;
				var tmpAllUrl =  prjName + "\",\"" +start+"\",\""+end  ;
				var str = "javascript:show(\"" + tmpAllUrl + "\")";
			html += '<tr class="trClass<%=k%2 %>" oriClass="" align="center" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>';
			html += '<td align="center">';
			html += '<input type="checkbox" name="chkList" value="'+start11+staticMonth+prjName + '" />';
			html += '</td>';
			//html += '<td align="center"><a href='+str+'><font color="#336699">' +entryInfo["prjName"] + '</font></a></td>';
			html += '<td align="center"><a href=\''+str+'\'><font color="#336699">' +entryInfo["prjName"] + '</font></a></td>';
			html += '<td>' +entryInfo["staticMonth"] + '</td>';
			html += '<td>' +entryInfo["PM"] + '</td>';
			html += '<td>' +(entryInfo["qualityPoint"]=='0'?'':entryInfo["qualityPoint"]) + '</td>';
			html += '<td>' +entryInfo["totalEvaluate"] + '</td>';
			html += '<td>' +entryInfo["processQualityStr"] + '</td>';
			html += '<td>' +entryInfo["progressStr"] + '</td>';
			html += '<td>' +entryInfo["projectQualityStr"]+"("+entryInfo["prjQualityAvgPoint"]+")" + '</td>';
			html += '<td>' +entryInfo["submitVersionNumer"] + '</td>';
			html += '<td>' +entryInfo["bugResolveRate"] + '</td>';
			html += '<td>' +entryInfo["unfitNumber"] + '</td>';
			html += '<td>' +entryInfo["totalValidBugs"] + '</td>';
			html += '<td>' +entryInfo["totalBugValue"] + '</td>';
			html += '<td>' +entryInfo["prjAvgBugbugDensity"] + '</td>';
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
			if(confirm('确认删除吗？')) {
			 	var strUrl="/pages/prjdetail/prjdetailinfo!delete.action?ids="+idList;
			 	strUrl = encodeURI(strUrl);
			 	var returnValue=OpenModal(strUrl,"600,380,operInfo.delete,um")
			   	query();
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
			  	var strUrl="/pages/prjdetail/prjdetailinfo!edit.action?ids="+itemId;
				strUrl = encodeURI(strUrl);
			  	
			  	var features="800,350,tmlInfo.updateTitle,tmlInfo";
				var resultvalue = OpenModal(strUrl,features);
				if(resultvalue==true){
					query();
				}
			}
		}
		
	function add(){
		var resultvalue = OpenModal('/pages/prjparams/prjparamsinfo!add.action','800,350,tmlInfo.addTmlTitle,tmlInfo');
		if(resultvalue!=null)
	  		query();
	}
		
				
		function show(prjName,start,end) {
		prjName = prjName.replace("（","lquot");
		prjName = prjName.replace("）","rquot");
		prjName = prjName.replace("+","$jia$");
		var strUrl="/pages/prjdetail/prjdetailinfo!show.action?req="+prjName+ "@" +start +"@"+end;
		
		//var strUrl="/pages/prjdetail/prjdetailinfo!show.action?req="+encodeURI(prjName);
	//	strUrl = encodeURI(strUrl);
		var features="670,700,transmgr.traninfo,watch";
		var resultvalue = OpenModal3(strUrl,features);
		}
		function openURL(url){
			window.open(url);
		}
		function validateInputInfo(){
	var re=/^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$/g;   	  
    var re1=/^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$/g; 
  	var thisDate = reportInfoForm.start.value.trim()+'-01';
	var endDate = reportInfoForm.end.value.trim()+'-02';
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
	 
	 
	 function getSummary(){
	 	if(!validateInputInfo()){
			return ;
		}
		var prjName =  document.getElementById("prjName1").value;
		//alert(prjName);
		if(prjName=="全选"){
			alert("必须选择某一项目！！");
			return;
		}
		prjName = prjName.replace("（","lquot");
		prjName = prjName.replace("）","rquot");
		prjName = prjName.replace("+","$jia$");
		
		var strUrl="/pages/prjdetail/prjdetailinfo!showSummary.action?req="+prjName;
		//var strUrl="/pages/prjdetail/prjdetailinfo!show.action?req="+encodeURI(prjName);
	//	strUrl = encodeURI(strUrl);
		var features="670,300,transmgr.traninfo,watch";
		var resultvalue = OpenModal3(strUrl,features);
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
		
		function search()
		{
			var url="prjdetailinfo!queryNames.action";
			var params={prjName1:$("#prjName1").val()};
			jQuery.post(url, params, $(document).callbackFun, 'json');
			document.getElementById("versionNO1").disabled = true;
		}
		$.fn.callbackFun=function (json)
		 {	
		 	var nval = "";
		 	document.getElementById("versionNO1").disabled = false;	
		  	$("#versionNO1 option").remove();
		  	if(json!=null&&json.length>0)
			{	
				for(var i=0;i<json.length;i++)
				{
					if(json[i].trim() == "fqst")
					{
						nval = "";
					}
					else
					{
						nval = json[i];
					}
					 $("#versionNO1").append("<option value='"+json[i]+"'>"+nval+"</option>");
				 }
				 document.getElementById("versionNO1").focus();
				 document.getElementById("versionNO1").value = "全选";
				 document.getElementById("bodyid").focus();
			}	
		 }
		 
		 function searchPrj()
		{
			var url="prjdetailinfo!queryPrjs.action";
			var params="";
			jQuery.post(url, params, $(document).callbackFun1, 'json');
			document.getElementById("prjName1").disabled = true;
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
			var url="prjdetailinfo!selectDB.action";
			var params={prjType1:$("#prjType1").val()};
			jQuery.post(url, params, $(document).callbackFun3, 'json');
			document.getElementById("prjName1").disabled = true;
			//document.getElementById("versionNO1").disabled = true;
		}
		$.fn.callbackFun3=function (json)
		 {
		 	document.getElementById("prjName1").disabled = false;	
			//document.getElementById("versionNO1").disabled = false;
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
 	<s:form name="form1"  namespace="/pages/prjdetail" action="prjdetailinfo!list.action" method="post" >
  	<input type="hidden" name="pageNum" id="pageNum"  value="1"/>
  	<input type="hidden" name="pageSize" id="pageSize" value="20" />
	<%@include file="/inc/navigationBar.inc"%>
	<input type="hidden" name="menuid" value="<%=request.getParameter("menuid")%>">
 	<table width="100%" cellSpacing="0" cellPadding="0" class="selectTableBackground" > 
 		<tr>
 		<td>
			<fieldset  width="100%">
			<legend></legend>
			<table width="100%"  >
            	<tr>
                <td width="10%" align="center">开始月份：</td>
				<td width="30%">
				<input name="start"  type="text"  value='<s:date name="new java.util.Date()" format="yyyy-MM-dd"/>'  id="start" size="22" class="MyInput"   isSel="true" isDate="true" onFocus="showDateMonth1()" dofun="showDateMonth1()" readonly="true" maxlength="7"/> 
				</td>
				<td width="10%" align="center">结束月份：</td>
				<td width="30%"><input name="end"  type="text" id="end" value='<s:date name="new java.util.Date()" format="yyyy-MM-dd"/>' size="22" class="MyInput"   isSel="true" isDate="true" onFocus="showDateMonth2()" dofun="showDateMonth2()" readonly/></td>
                <td width="10%" align="center">项目类型：</td>
                <td width="17%"> 
                <s:select name="prjType1" id="prjType1" theme="simple" list="prjTypes" cssStyle="width:140px;" onchange="searchType();"/>
               </td>
			  </tr>
			   <tr>
				<td align="center">项目名称：</td>
                <td ><select name="prjName1" id="prjName1"  style="width:165px;"></select></td>
				<td align="right" colspan="1"> <tm:button site="1"/></td>
                <!-- 
                <td colspan="2" colspan="2">项目版本： <select name="versionNO1" id="versionNO1"  style="width:150px;"></select></td>
			   -->
			  </tr>
			</table> 
			</fieldset>  
		</td> 
		</tr>
	</table><br />
	<table width="100%" height="23" border="0" cellspacing="0" cellpadding="0" background="../../images/main/bgtop.gif">
			<tr>
				 <td width="25"  height="23" valign="middle">&nbsp;<img src="../../images/share/list.gif" width="14" height="16"></td>
				 <td class="orarowhead" colspan="2"><s:text name="operInfo.title" /></td>
				 <td colspan="2"><input type="button"   value="提交录入数据" onClick="modify();" class="MyButton"  image="../../images/share/find.gif"/></td>
				 <td colspan="2"><input type="button"   value="删除录入数据" onClick="del();" class="MyButton"  image="../../images/share/find.gif"/></td>
			  <td align="right" width="75%"><tm:button site="2"></tm:button></td>
			</tr>
		</table>
<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066">  
			<tr bgcolor="#FFFFFF">
			<td> 
				<table width="100%" cellSpacing="0" cellPadding="0">
				<tr>
					<%--<td width="6%"> 
						<div align="center"><input type="checkbox" name="chkList"  id="chkAll"   value="all"  onClick="SelAll(this)"></div> 
					</td>
					<td width="11%">
						<div align="left"><label for=chkAll><s:text name="operInfo.checkall" /></label></div>
 					</td>
 					--%><td width="83%" align="right">
						<div id="pagetag"><tm:pagetag pageName="currPage" formName="reportInfoForm" /></div>
					</td>
				</tr>
				</table>
			</td>
			</tr>
		</table>
	<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066" id=downloadList>
		<tr> 
		<td nowrap width="2%" class="oracolumncenterheader"></td>
			<td nowrap width="15%" class="oracolumncenterheader"><div align="center">项目名称</div></td>
			<td nowrap width="8%" class="oracolumncenterheader"><div align="center">月份</div></td>
			<td nowrap width="8%" class="oracolumncenterheader"><div align="center">PM</div></td>
			<td nowrap width="8%" class="oracolumncenterheader"><div align="center">质量总分</div></td>
            <td nowrap width="6%" class="oracolumncenterheader"><div align="center">总体评价</div></td>
            <td nowrap width="6%" class="oracolumncenterheader"><div align="center">过程质量</div></td>
            <td nowrap width="6%" class="oracolumncenterheader"><div align="center">进度</div></td><%--
            <td nowrap width="6%" class="oracolumncenterheader"><div align="center">缺陷清除率</div></td>
            --%><td nowrap width="6%" class="oracolumncenterheader"><div align="center">项目质量</div></td>
            <td nowrap width="6%" class="oracolumncenterheader"><div align="center">提交版本数</div></td>
            <td nowrap width="6%" class="oracolumncenterheader"><div align="center">缺陷解决率</div></td>
            <td nowrap width="6%" class="oracolumncenterheader"><div align="center">不符合测试准入次数</div></td>
               <td nowrap width="6%" class="oracolumncenterheader"><div align="center">缺陷总数</div></td>
                  <td nowrap width="6%" class="oracolumncenterheader"><div align="center">缺陷总值</div></td>
                     <td nowrap width="6%" class="oracolumncenterheader"><div align="center">缺陷密度</div></td>
            <%--
            <td nowrap width="6%" class="oracolumncenterheader"><div align="center">用例通过率</div></td>
            <td nowrap width="6%" class="oracolumncenterheader"><div align="center">工程缺陷数</div></td>
            --%>
		</tr>
		<tbody name="formlist" id="formlist"> 
  		<s:iterator  value="prjDetailList" id="tranInfo" status="row">
  		<s:if test="#row.odd == true"> 
 		<tr id="tr" class="trClass0" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
 		</s:if><s:else>
 		<tr id="tr" class="trClass1" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
 		</s:else> 
			<td><div align="center">
				<a href='javascript:show("<s:property value="prjName"/>","<s:property value="versionNO"/>","<s:property                  value="start"/>","<s:property value="end"/>")'>
					<font color="#336699"><s:property value="prjName"/></font>
		  		</a>
		  	</div></td>
		 	<td align="center"><s:property value="versionNO"/></td>
            <td align="center"><s:property value="bugSubtotal"/></a></td>
            <td align="center"><s:property value="bugSeriousRate"/></a></td>
            <td align="center"><s:property value="bugResolveRate"/></a></td>
            <td align="center"><s:property value="bugSubvalue"/></a></td>
            <td align="center"><s:property value="bugCleanRate"/></a></td>
            <td align="center"><s:property value="bugDensity"/></a></td>
            <td align="center"><s:property value="requireChangeRate"/></a></td>
            <td align="center"><s:property value="casePassRate"/></a></td>
            <td align="center"><s:property value="prjFeedbackRate"/></a></td>
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

