<!--20100107 14:00-->
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
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
		
	</script>
<body id="bodyid"  leftmargin="0" topmargin="10" >
	<form name="reportInfoForm" action="<%=request.getContextPath() %>/pages/finance/financeinfo!update.action"   method="post">
<table width="80%" align="center" cellPadding="0" cellSpacing="0">
            <tr>
                <td>
                <fieldset class="jui_fieldset" width="100%">
                    <legend><s:text name="汇总" /></legend>
              <table width="100%" align="center" border="0" cellspacing="1" cellpadding="1" bgcolor="#583F70">
            <br/>
                        <tr>
                          <td width="12%" height="28" align="center" bgcolor="#999999"><div align="center" >当前余额</div></td>
                          <td width="13%" bgcolor="#FFFFFF"><s:property value='info.currentBalance'/>&nbsp;</td>
                            
                          <td width="12%" align="center" bgcolor="#999999"><div align="center" >总收入</div></td>
                          <td width="13%" bgcolor="#FFFFFF"><s:property value='info.incomeSum'/></td> 
                          
                          <td width="12%" align="center" bgcolor="#999999"><div align="center" >总支出</div></td>
                          <td width="13%" bgcolor="#FFFFFF"><s:property value='info.paySum'/></td> 
                          
                          <td width="12%" align="center" bgcolor="#999999"><div align="center" >管理人</div></td>
                          <td width="13%" bgcolor="#FFFFFF"><s:property value='info.manage'/></td> 
                </tr> 
                    </table>
                </fieldset>
                </td> 
            </tr> 
    </table>
<br/>
<br/>
<table width="80%" cellpadding="0" cellspacing="0" align="center"> 
		<tr>
			<td align="center"><input type="button" name="return" value='<s:text  name="button.close"/>' class="MyButton"  onclick="closeModal();" image="../../images/share/f_closed.gif">			</td> 
  		</tr>  
 	</table> 
			
</form>
    
</body>  
</html> 