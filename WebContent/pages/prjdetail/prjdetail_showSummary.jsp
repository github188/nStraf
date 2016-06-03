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
                  	<!--缺陷总数 缺陷严重性 缺陷解决率 缺陷总值 -->
                      <td align="center" bgcolor="#CCCCCC" rowspan="2">汇总</td> 
                      <td align="center" bgcolor="#CCCCCC" colspan="4">项目名称</td> 
                      <td align="center" bgcolor="#CCCCCC" colspan="2">质量总分</td> 
                      <td align="center" bgcolor="#CCCCCC" colspan="2">质量平均分</td>
                      <td align="center" bgcolor="#CCCCCC" colspan="2">提交版本数</td>
                      <td align="center" bgcolor="#CCCCCC" colspan="2">缺陷解决率</td>  
                  </tr>
                  <tr>
                      <td align="center" bgcolor="#FFFFFF" colspan="4"><div><input name="prjdev.prjName" type="text" id="bugzm"  value='<s:property value="prjdetail.prjName"/>' readonly style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; text-align:left"></div></td> 
                      <td align="center" bgcolor="#FFFFFF" colspan="2"><div><input name="prjdev.bugSubtotal" type="text" id="bugzm" size="10" value='<s:property value="prjdetail.totalPoint"/>' readonly style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; text-align:center"></div></td> 
                      <td align="center" bgcolor="#FFFFFF" colspan="2"><input name="prjdev.bugSeriousRate" type="text" id="bugyz" size="10" value='<s:property value="prjdetail.avgPoint"/>' readonly style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; text-align:center"></td>
                      <td align="center" bgcolor="#FFFFFF" colspan="2"><input name="prjdev.bugResloveRate" type="text" id="bugyb" size="10" value='<s:property value="prjdetail.versionNum"/>' readonly style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; text-align:center"></td>  
                      <td align="center" bgcolor="#FFFFFF" colspan="2"><input name="prjdev.bugSubvalue" type="text" id="bugjg" size="10" value='<s:property value="prjdetail.bugResolveRate"/>' readonly style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; text-align:center"></td>
                
                     
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