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
	<form action="/pages/prjdev/prjdevinfo!show.action"  method="post">
		<table width="90%" align="center" cellPadding="0" cellSpacing="0">
		<tr>
			<td>
			<fieldset class="jui_fieldset" width="100%">
				<legend><s:text name="项目附加信息" /></legend>
				<table width="90%" align="center" border="1" cellspacing="0" cellpadding="0" bordercolorlight="#583F70">
                <br/>
                  <tr>
                      <td align="center" bgcolor="#CCCCCC" rowspan="2">缺陷数</td> 
                      <td align="center" bgcolor="#CCCCCC" colspan="2">打开</td> 
                      <td align="center" bgcolor="#CCCCCC" colspan="2">重新打开</td>
                      <td align="center" bgcolor="#CCCCCC" colspan="2">修复</td>  
                      <td align="center" bgcolor="#CCCCCC" colspan="2">拒绝</td>
                      <td align="center" bgcolor="#CCCCCC" colspan="2">未解决缺陷数</td> 
                      <td align="center" bgcolor="#CCCCCC" colspan="2"><font color="#0033FF">缺陷总数</font></td>
                  </tr>
                  <tr>
                      <td align="center" bgcolor="#FFFFFF" colspan="2"><div><input name="prjdev.bugOpen" type="text" id="bugzm" size="10" value='<s:property value="prjdev.bugOpen"/>' readonly style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; text-align:center"></div></td> 
                      <td align="center" bgcolor="#FFFFFF" colspan="2"><input name="prjdev.bugReopen" type="text" id="bugyz" size="10" value='<s:property value="prjdev.bugReopen"/>' readonly style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; text-align:center"></td>
                      <td align="center" bgcolor="#FFFFFF" colspan="2"><input name="prjdev.bugFix" type="text" id="bugyb" size="10" value='<s:property value="prjdev.bugFix"/>' readonly style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; text-align:center"></td>  
                      <td align="center" bgcolor="#FFFFFF" colspan="2"><input name="prjdev.bugReject" type="text" id="bugjg" size="10" value='<s:property value="prjdev.bugReject"/>' readonly style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; text-align:center"></td>
                      <td align="center" bgcolor="#FFFFFF" colspan="2"><input name="prjdev.bugUnresolve" type="text" id="bugjy" size="10" value='<s:property value="prjdev.bugUnresolve"/>' readonly style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; text-align:center"></td> 
                      <td align="center" bgcolor="#FFFFFF" colspan="2"><input name="prjdev.bugSubtotal" type="text" id="bugSubtotal" size="10" value='<s:property value="prjdev.bugSubtotal"/>' readonly style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; text-align:center; color:#0033FF"></td>
                  </tr>
                  <!--<tr>
                      <td align="center" bgcolor="#CCCCCC">缺陷总值</td> 
                      <td colspan="5" align="center" bgcolor="#FFFFFF"><input name="prjdev.bugSubvalue" type="text" id="bugzz" size="10" value='<s:property value="prjdev.bugSubvalue"/>' readonly style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; text-align:center"></td> 
                      <td colspan="2" align="center" bgcolor="#CCCCCC">代码行数</td>
                      <td colspan="5" align="center" bgcolor="#FFFFFF"><input name="prjdev.codeLine" type="text" id="codeLine" size="10" value='<s:property value="prjdev.codeLine"/>' readonly style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; text-align:center"></td>
                  </tr>-->
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