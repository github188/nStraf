<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/xmlHtc.inc"%>
<%@ include file="/pages/common/loading.jsp"%>
<%@ page import="java.util.Date,cn.grgbanking.feeltm.util.DateUtil"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<html>
<head>
</head>
<link href="../../css/css_v2.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../js/Validator.js"></script>
<script language="javascript" src="../../js/jquery-1.11.0.js"></script>
<script type="text/javascript">

	function closeModal(){
	  window.close();
	}
	function save(){
	    window.returnValue=true;
	    reportInfoForm.submit();
	}
	$(function(){
		$("textarea").text("本次更新内容:\r");
	});
	
</script>
<body id="bodyid"  leftmargin="0" topmargin="10" >
<form name="reportInfoForm" action="<%=request.getContextPath() %>/pages/client/clientUpload!upload.action" method="post" enctype="multipart/form-data">
<table width="100%" align="center" cellPadding="1" cellSpacing="1" class="popnewdialog1">
<tr>
<td>
	<table width="100%" class="input_table" >
		<tr style="height:50px;text-align: center;background-color:#FFFFFF;" align="center">
			<td colspan="4"><font style="font-size:22px;font-weight:bold;">手机端升级包上传</font></td>
		</tr>
		<tr style="">
			<td width="20%" align="right">选择文件:</td>
			<td width="80%" align="left" colspan="3"> 
				<s:file name="upload" id="file" cssStyle="MyButton" size="62"></s:file>
			</td>
		</tr>
		<tr style="">
			<td width="20%" align="right">版本:</td>
			<td width="20%" style="text-align: left;">
				<input name="version" value='<s:property value="#request.nextVersion" />' />
			</td>
			<td width="20%" align="right">类型:</td>
			<td width="40%">
				<select name="status">
        			<option value="0">选择更新</option>
        			<option value="1">强制更新</option>
        		</select>
			</td>
		</tr>
		<tr style="">
      		<td width="15%" align="right">更新提示:</td>
          	<td width="90%" align="left" colspan="3">
          		<textarea name="note" rows="5" cols="55"></textarea>
          	</td>
		</tr>	
		
	</table>
</td> 
</tr>
</table>
<br/>
<table width="90%" cellpadding="0" cellspacing="0" align="center"> 
<tr>
	<td align="center"> 
	<br/>
 		<input type="button" name="ok"  value='<s:text name="grpInfo.ok" />' class="MyButton" onClick="save()"  image="../../images/share/yes1.gif"> 
		<input type="button" name="return" value='<s:text name="button.close"/>' class="MyButton"  onclick="closeModal();" image="../../images/share/f_closed.gif"> 
	</td> 
</tr>  
</table> 
</form>
</body>  
</html>