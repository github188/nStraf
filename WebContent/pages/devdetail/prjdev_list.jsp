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
<head><title>tool query</title>
<script src="Scripts/AC_ActiveX.js" type="text/javascript"></script>
<script src="Scripts/AC_RunActiveContent.js" type="text/javascript"></script>
</head>
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
	var rank=0;
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

		 //var cishu=0;
		function query(){
			//if(!validateInputInfo()){
				//return ;
			//}
			if(!g_query)
			{
				return ;
			}
			g_query = false;
			rank=0;
			var start =  document.getElementById("start").value;
			var end =  document.getElementById("end").value;
			var pageNum = document.getElementById("pageNum").value;
//			var prjName = document.getElementById("prjName1").value;
		//	var versionNO = document.getElementById("versionNO1").value;
			var devName = document.getElementById("devName1").value;
			//prjName = prjName.replace("+","$jia$");
			//versionNO = versionNO.replace("+","$jia$");
			var showdiv = document.getElementById("showdiv");
           		showdiv.style.display = "block";
			var actionUrl = "<%=request.getContextPath()%>/pages/devdetail/devdetailinfo!refresh.action?from=refresh&pageNum="+pageNum;
			//actionUrl += "&prjName="+prjName;
			//actionUrl += "&versionNO="+versionNO;
			actionUrl += "&devName="+devName;
			actionUrl += "&start="+start+"&end="+end;
			actionUrl=encodeURI(actionUrl);
			var method="setHTML";
			<%int k = 0 ; %>
	   		sendAjaxRequest(actionUrl,method,pageNum,true);
	   		document.getElementById("progressBar").style.display="block";	
	   		setTimeout("delay();",5000);
	   		//document.getElementById("progressBar").style.display="none"; 	
			// if(cishu<60){
		    //	setTimeout("delay();",1000);
		     //	cishu++;      
		  //  }else{
		   //	document.getElementById("progressBar").style.display="none";	
			//}
		}
		function setHTML(entry,entryInfo){
	 	document.getElementById("progressBar").style.display="none";
				var html = '';
				var devName = entryInfo["devName"];
				var devName_en=entryInfo["devName_en"];
			var start =  document.getElementById("start").value;
			var end =  document.getElementById("end").value;
				var tmpAllUrl =  devName_en + "\",\"" +start+"\",\""+end ;
				//tmpAllUrl = encodeURI(tmpAllUrl);
				//var str = "javascript:show(\""+ tmpAllUrl +"\")";
				var str = "javascript:show(\"" + tmpAllUrl + "\")";
			html += '<tr class="trClass<%=k%2 %>" oriClass="" align="center" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>';
			rank++;
			if(devName==''){
				html += '<td>无</td>';
			}
			else{
			html += '<td>' + rank+ '</td>';
			html += '<td align="center"><a href=\''+str+'\'><font color="#336699">' +entryInfo["devName"] + '</font></a></td>';
			html += '<td>' +entryInfo["dept"] + '</td>';
			html += '<td>' +entryInfo["totalPoint"] + '</td>';

			html += '<td>' +entryInfo["qualityEvalute"] + '</td>';
			html += '<td>' +entryInfo["bugReopenRate"] + '</td>';
			html += '<td>' +entryInfo["avrFixTime"] + '</td>';
			html += '<td>' +entryInfo["FixRate"] + '</td>';
			html += '<td>' +entryInfo["bugSubtotal"] + '</td>';
			html += '<td>' +entryInfo["bugTotalValue"] + '</td>';
			}
	  		html += '</tr>';
	 	<% k++;%>;
		showdiv.style.display = "none";
		return html;
		}
				
		function show(devName,start,end) {
		if(start==null || start=="")
		{
			start = "fq";
		}
		if(end==null || end=="")
		{
			end = "fq";
		}
		var strUrl="/pages/devdetail/devdetailinfo!show.action?req="+devName+"@"+start+"@"+end+"@";
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
		 
		 function searchType()
		{
			var url="../prjdetail/prjdetailinfo!selectDB.action";
			var params={prjType1:$("#prjType1").val()};
			//alert($("#prjType1").val());
			jQuery.post(url, params, $(document).callbackFun3, 'json');
			
		}
		$.fn.callbackFun3=function (json)
		 {
			document.getElementById("devName1").disabled =false;
			var params={prjType1:$("#prjType1").val()};
		  	$("#devName1 option").remove();
		  	if(json!=null&&json.length>0)
			{	
				var url="devdetailinfo!queryDevNames.action";
				jQuery.post(url, params, $(document).callbackFun4, 'json');
				 document.getElementById("bodyid").focus();
			}	
		 }
		$.fn.callbackFun4=function (json){
	 		//document.getElementById("devName1").disabled = false;
			//$("#devName1 option").remove();
			if(json!=null&&json.length>0)
			{   
				var html="";
				html+="<option value='all' selected>全选</option>";
				for(var i=0;i<json.length;i++)
				{
					 //var str="<option value='"+json[i]+"'>"+json[i]+"</option>";
					 //alert();
					 html+="<option value='"+json[i]+"'>"+json[i]+"</option>";
				 }
				$("#devName1").append(html);
			}
	}
		
	</script>
 <body id="bodyid" leftmargin="0" topmargin="0" onLoad="searchType();"><br>
 	<s:form name="reportInfoForm"  namespace="/pages/prjdetail" action="prjdetailinfo!list.action" method="post" >
  	<input type="hidden" name="pageNum" id="pageNum"  value="1"/>
	<%@include file="/inc/navigationBar.inc"%>
	<input type="hidden" name="menuid" value="<%=request.getParameter("menuid")%>">
 	<table width="100%" cellSpacing="0" cellPadding="0" class="selectTableBackground"> 
 		<tr>
 		<td>
			<fieldset  width="100%">
			<legend></legend>
			<table width="100%">
            	<tr>
                <td width="10%" align="center">开始时间：</td>
				<td width="25%"><input name="start"  type="text" id="start" size="22" class="MyInput"   isSel="true" isDate="true" onFocus="ShowDate(this)" dofun="ShowDate(this)" /> </td>
				<td width="10%" align="center">结束时间：</td>
				<td width="25%"><input name="end"  type="text" id="end" size="22" class="MyInput"   isSel="true" isDate="true" onFocus="ShowDate(this)" dofun="ShowDate(this)" /></td>
                
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
			   <td width="10%" align="center">项目类型：</td>
               <td width="25%"> <s:select name="prjType1" id="prjType1" theme="simple" list="prjTypes" cssStyle="width:160px;" onchange="searchType();"></s:select>
                <td width="10%" align="center"> 开发人员：</td>                       
                <td width="25%"><select name="devName1" id="devName1"  style="width:100px;"></select>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<tm:button site="1"/></td>
			  </tr>
			</table> 
			<div id="progressBar" style="display: none;
			position: absolute;
			top: 85px;
			left: 0;
			width: 100%;
			height: 100%;
			z-index: 5000;
			background-color: #ccc;
			-moz-opacity: 0.5;
			opacity: .50;
			filter: alpha( opacity = 50 );
			text-align: center;">
  			<script type="text/javascript">
AC_AX_RunContent( 'classid','clsid:D27CDB6E-AE6D-11cf-96B8-444553540000','codebase','http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,29,0','width','462','height','100','src','../../images/share/progressBar1.swf','quality','high','pluginspage','http://www.macromedia.com/go/getflashplayer','type','application/x-shockwave-flash','movie','../../images/share/progressBar1.swf' ); //end AC code
</script><noscript><object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,29,0" width="462" height="100">
  				<param name="movie" value="../../images/share/progressBar1.swf">
  				<param name="quality" value="high">
  				<embed src="../../images/share/progressBar1.swf" quality="high" pluginspage="http://www.macromedia.com/go/getflashplayer" type="application/x-shockwave-flash" width="462" height="100"></embed>
			</object></noscript>
		</div>
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
		    <td nowrap width="3%" class="oracolumncenterheader"><div align="center">排名</div></td>
            <td nowrap width="6%" class="oracolumncenterheader"><div align="center">开发人员</div></td>
            <td nowrap width="10%" class="oracolumncenterheader"><div align="center">部门</div></td>
            <td nowrap width="6%" class="oracolumncenterheader"><div align="center">分值</div></td>
            <td nowrap width="6%" class="oracolumncenterheader"><div align="center">质量评价</div></td>
            <td nowrap width="10%" class="oracolumncenterheader"><div align="center">缺陷ReOpen率</div></td>
            <td nowrap width="6%" class="oracolumncenterheader"><div align="center">平均缺陷修复时间(天)</div></td>
            <td nowrap width="10%" class="oracolumncenterheader"><div align="center">缺陷修复率</div></td>
            <td nowrap width="6%" class="oracolumncenterheader"><div align="center">缺陷总数</div></td>
            <td nowrap width="6%" class="oracolumncenterheader"><div align="center">缺陷总值</div></td>
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
            <td align="center"><s:property value="devName"/></a></td>
            <td align="center"><s:property value="bugReopenRate"/></a></td>
            <td align="center"><s:property value="avrFixTime"/></a></td>
            <td align="center"><s:property value="FixRate"/></a></td>
	    </tr>
		</s:iterator>  
 		</tbody> 
 	</table> 
 </s:form>
  <script type="text/javascript">
 	document.getElementById("prjType1").value ='<%=((cn.grgbanking.feeltm.domain.testsys.ProjectDB)session.getAttribute("globalDB")).getPrjName()%>';
 	document.getElementById("devName1").value='all';
 </script>
</body>
</html>

