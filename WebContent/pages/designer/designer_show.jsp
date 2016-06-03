<!--20100107 14:00-->
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/xmlHtc.inc"%>
<%@ include file="/pages/common/loading.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
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
	</script>
<body id="bodyid"  leftmargin="0" topmargin="10" >
	<form action="/pages/prjtest/prjtestinfo!show.action"  method="post">
		<table width="90%" align="center" cellPadding="0" cellSpacing="0">
		<tr>
			<td>
			<fieldset class="jui_fieldset" width="100%">
				<legend><s:text name="用例数" /></legend>
				<table width="90%" align="center" border="1" cellspacing="0" cellpadding="0" bordercolorlight="#583F70">
                <br/>
                  <tr>
                      <td align="center" bgcolor="#CCCCCC" rowspan="2">用例状态</td> 
                      <td align="center" bgcolor="#CCCCCC" colspan="2">新增</td> 
                      <td align="center" bgcolor="#CCCCCC" colspan="2">修改</td> 
                      <td align="center" bgcolor="#CCCCCC" colspan="2">删除</td>
                      <td align="center" bgcolor="#CCCCCC" colspan="2">已评审</td>  
                      <td align="center" bgcolor="#CCCCCC" colspan="2">未评审</td>
                      <td align="center" bgcolor="#CCCCCC" colspan="2">实现自动化用例</td>
                      <td align="center" bgcolor="#CCCCCC" colspan="2">未定义</td>
                  </tr>
                  <tr>
                      <td align="center" bgcolor="#FFFFFF" colspan="2"><div><input name="prjtest.bugOpen" type="text" id="bugzm" size="10" value='<s:property value="detail.add"/>' readonly style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; text-align:center"></div></td> 
                      <td align="center" bgcolor="#FFFFFF" colspan="2"><input name="prjtest.bugReopen" type="text" id="bugyz" size="10" value='<s:property value="detail.update"/>' readonly style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; text-align:center"></td>
                      <td align="center" bgcolor="#FFFFFF" colspan="2"><input name="prjtest.bugFix" type="text" id="bugyb" size="10" value='<s:property value="detail.delete"/>' readonly style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; text-align:center"></td>  
                      <td align="center" bgcolor="#FFFFFF" colspan="2"><input name="prjtest.bugReject" type="text" id="bugjg" size="10" value='<s:property value="detail.audited"/>' readonly style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; text-align:center"></td>
                      <td align="center" bgcolor="#FFFFFF" colspan="2"><input name="prjtest.bugUnresolve" type="text" id="bugjy" size="10" value='<s:property value="detail.unAudit"/>' readonly style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; text-align:center"></td> 
                      <td align="center" bgcolor="#FFFFFF" colspan="2"><input name="prjtest.bugUnresolve" type="text" id="bugjy" size="10" value='<s:property value="detail.autoFlag"/>' readonly style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; text-align:center"></td> 
                      <td align="center" bgcolor="#FFFFFF" colspan="2"><input name="prjtest.bugUnresolve" type="text" id="bugjy" size="10" value='<s:property value="detail.undefined"/>' readonly style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; text-align:center"></td> 
                  </tr>
                </table>
			</fieldset>
			</td> 
		</tr> 
		<tr>
			<td align="center"> 
				<br/><input type="button" name="return" value='<s:text name="button.close"/>' class="MyButton"  onclick="closeModal();" image="../../images/share/f_closed.gif"> 
			</td> 
  		</tr>  
 		</table> 
 	</form>
</body>  
</html> 