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
				<legend><s:text name="项目附加信息" /></legend>
				<table width="90%" align="center" border="1" cellspacing="0" cellpadding="0" bordercolorlight="#583F70">
                <br/>
                  <tr>
                      <td align="center" bgcolor="#CCCCCC" rowspan="2">缺陷数</td> 
                      <td align="center" bgcolor="#CCCCCC" colspan="2">致命</td> 
                      <td align="center" bgcolor="#CCCCCC" colspan="2">严重</td>
                      <td align="center" bgcolor="#CCCCCC" colspan="2">一般</td>  
                      <td align="center" bgcolor="#CCCCCC" colspan="2">警告</td>
                      <td align="center" bgcolor="#CCCCCC" colspan="2">建议</td>
                      <td align="center" bgcolor="#CCCCCC" colspan="2">拒绝</td>
                      <td align="center" bgcolor="#CCCCCC" colspan="2">无用例对应</td> 
                      <td align="center" bgcolor="#CCCCCC" colspan="2"><font color="#0033FF">缺陷总数</font></td>
                  </tr>
                  <tr>
                  <!--private String bugFatal;
	private String bugSerious;
	private String bugGeneral;
	private String bugWarn;
	private String bugSuguest;
	private String bugReject;
	private String bugNoCase;
	private String subtotal;
	private String caseExecNum;
	private String caseExecTime;-->
                      <td align="center" bgcolor="#FFFFFF" colspan="2"><div><input name="prjtest.bugOpen" type="text" id="bugzm" size="10" value='<s:property value="prjtest.bugFatal"/>' readonly style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; text-align:center"></div></td> 
                      <td align="center" bgcolor="#FFFFFF" colspan="2"><input name="prjtest.bugReopen" type="text" id="bugyz" size="10" value='<s:property value="prjtest.bugSerious"/>' readonly style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; text-align:center"></td>
                      <td align="center" bgcolor="#FFFFFF" colspan="2"><input name="prjtest.bugFix" type="text" id="bugyb" size="10" value='<s:property value="prjtest.bugGeneral"/>' readonly style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; text-align:center"></td>  
                      <td align="center" bgcolor="#FFFFFF" colspan="2"><input name="prjtest.bugReject" type="text" id="bugjg" size="10" value='<s:property value="prjtest.bugWarn"/>' readonly style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; text-align:center"></td>
                      <td align="center" bgcolor="#FFFFFF" colspan="2"><input name="prjtest.bugUnresolve" type="text" id="bugjy" size="10" value='<s:property value="prjtest.bugSuguest"/>' readonly style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; text-align:center"></td> 
                      <td align="center" bgcolor="#FFFFFF" colspan="2"><input name="prjtest.bugUnresolve" type="text" id="bugjy" size="10" value='<s:property value="prjtest.bugReject"/>' readonly style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; text-align:center"></td> 
                      <td align="center" bgcolor="#FFFFFF" colspan="2"><input name="prjtest.bugUnresolve" type="text" id="bugjy" size="10" value='<s:property value="prjtest.bugNoCase"/>' readonly style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; text-align:center"></td> 
                      <td align="center" bgcolor="#FFFFFF" colspan="2"><input name="prjtest.bugSubtotal" type="text" id="bugSubtotal" size="10" value='<s:property value="prjtest.subtotal"/>' readonly style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; text-align:center; color:#0033FF"></td>
                  </tr>
                  <tr>
                      <td align="center" bgcolor="#CCCCCC">用例执行数</td> 
                      <td colspan="7" align="center" bgcolor="#FFFFFF"><input name="prjtest.bugSubvalue" type="text" id="bugzz" size="10" value='<s:property value="prjtest.caseExecNum"/>' readonly style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; text-align:center"></td> 
                      <td colspan="3" align="center" bgcolor="#CCCCCC">用例执行时间（分钟）</td>
                      <td colspan="5" align="center" bgcolor="#FFFFFF"><input name="prjtest.codeLine" type="text" id="codeLine" size="10" value='<s:property value="prjtest.caseExecTime"/>' readonly style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; text-align:center"></td>
                  </tr>
                  <!--<tr>
                      <td width="20%" align="center" bgcolor="#CCCCCC">需求数</td> 
                      <td align="center" width="10%" bgcolor="#CCCCCC"></td> 
                      <td align="center" width="10%" bgcolor="#CCCCCC"></td>
                      <td align="center" width="10%" bgcolor="#CCCCCC"></td>  
                      <td align="center" width="10%" bgcolor="#CCCCCC"></td>
                      <td align="center" width="10%" bgcolor="#CCCCCC"></td> 
                      <td align="center" width="10%" bgcolor="#CCCCCC"></td>
                      <td align="center" width="10%" bgcolor="#CCCCCC"></td>  
                      <td align="center" width="10%" bgcolor="#CCCCCC"></td>
                      <td align="center" width="10%" bgcolor="#CCCCCC"></td> 
                      <td align="center" width="10%" bgcolor="#CCCCCC"></td>
                      <td align="center" width="10%" bgcolor="#CCCCCC"></td>  
                      <td align="center" width="10%" bgcolor="#CCCCCC"></td>
                  </tr> -->
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