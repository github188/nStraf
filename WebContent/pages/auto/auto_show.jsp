<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/xmlHtc.inc"%>
<%@ include file="/pages/common/loading.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<html> 
 <head></head>
	<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
	<script type="text/javascript" src="../../js/tablesort.js"></script>
	<script type="text/javascript" src="../../js/Validator.js"></script>
	<script type="text/javascript" src="../../js/DateValidator.js"></script>
	<script type="text/javascript">
		function closeModal(){
	 		window.close();
		}
		function show(szURL) 
		{ 
			var IE4=(document.all) ? 1 : 0;                                          
			var NN4=(document.layers) ? 1 : 0;                                          
			if (IE4) {                                          
			var theSource2='<iframe id="mg" src='+szURL+' width="101%" height="530"></iframe>';                                         
			document.write(theSource2);      
			} else if (NN4) {                                          
			var theSource2='<layer id="mg" src='+szURL+' width="101%" height=530></layer>';                                          
			document.write(theSource2);                                          
			} 
		} 
	</script>
<body id="bodyid"  leftmargin="0" topmargin="10" >
	<form action="/pages/auto/autoinfo!show.action"   method="post">
    <input type="hidden" id="szStatus" value='<s:property value="info.status"/>' />
		<table width="95%" align="center" cellPadding="1" cellSpacing="1">
		<tr>
			<td>
			<fieldset class="jui_fieldset" width="100%">
				<legend><s:text name="详细信息" /></legend>
				<table width="100%" align="center" border="0" cellspacing="1" cellpadding="1" bgcolor="#583F70">
      <tr>
        <td colspan="4" bgcolor="#99CCCC"><div align="left"><strong>项目概述</strong></div></td>
      </tr>
      <tr>
        <td width="20%" bgcolor="#999999">项目名称</td>
        <td width="30%" bgcolor="#FFFFFF"><s:property value="info.prjName"/></td>
        <td width="20%" bgcolor="#999999"><div>版本</div></td>
        <td width="30%" bgcolor="#FFFFFF"><div id="verid"><s:property value="info.versionNo"/></div></td>
      </tr> 
      <tr>
        <td width="20%" bgcolor="#999999">执行者</td>
        <td width="30%" bgcolor="#FFFFFF"><s:property value="info.username"/></td>
        <td width="20%" bgcolor="#999999">执行机IP</td>
        <td width="30%" bgcolor="#FFFFFF"><s:property value="info.execIP"/></td>
      </tr> 
       <tr>
        <td width="20%" bgcolor="#999999">执行时间</td>
        <td width="30%" bgcolor="#FFFFFF"><s:property value="info.execTime"/></td>
        <td width="20%" bgcolor="#999999">结束时间</td>
        <td width="30%" bgcolor="#FFFFFF"><s:property value="info.endTime"/></td>
      </tr> 
      <tr>
      	<td width="100%" colspan="4"><script language='JavaScript'>
		var szStatus = $("#szStatus").val();
		var szPro = '<s:property value="info.prjName"/>';
		if(szStatus.indexOf("正在运行")!=-1)
		{
			var szURL = "http://" + '<s:property value="info.execIP"/>' + ":8080/index4tp.html";
			if(szPro.indexOf("Driver")!=-1)
			{
				szURL = "http://" + '<s:property value="info.execIP"/>' + ":8080/pms/index4dtp.html";
			}
			show(szURL);
		}
        </script></td>
      </tr>
    </table>
			</fieldset>
			</td> 
		</tr> 
		<tr>
			<td align="center"> 
            	<br/><input type="button" name="return" value='刷新' class="MyButton"  onclick="ref1()" image="../../images/share/f_closed.gif" /> 
				&nbsp;<input type="button" name="return" value='<s:text  name="button.close"/>' class="MyButton"  onclick="closeModal();" image="../../images/share/f_closed.gif"> 
			</td> 
  		</tr>  
 		</table> 
 	</form>
    <script type="text/javascript">
	if($("#verid").html().indexOf("%s")!=-1)
	{
		$("#verid").html('最新版本');
	}
	function ref1()
	{
		window.navigate(window.location);
	}
	
	for(var i=0; i<document.getElementsByTagName("textarea").length; i++)
	{
		var tmpStr = document.getElementsByTagName("textarea")[i].value.trim();
		var tmpStrlen = tmpStr.length;
		var tmpLfCount = tmpStr.split("\n").length;
		var tmpCols = parseInt(document.getElementsByTagName("textarea")[i].cols, 10);
		var calcRows = Math.ceil(tmpStrlen/tmpCols);
		var nosetFlag = false;
		if(calcRows == 0)
		{
			calcRows = 1;
		}
		if(calcRows>=4)
		{
			document.getElementsByTagName("textarea")[i].style.overflowY = "hidden";
			calcRows = 1;
			document.getElementsByTagName("textarea")[i].cols = tmpCols - 1;
			document.getElementsByTagName("textarea")[i].rows = calcRows + 1;
			nosetFlag = true;
		}
		if(!nosetFlag)
		{
			document.getElementsByTagName("textarea")[i].rows = calcRows ;
		}
		document.getElementsByTagName("textarea")[i].value = tmpStr;
	}
	</script>
</body>  
</html> 