<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<html>
<head>
	<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script language="javascript" src="../../js/tablesort.js"></script>
</head>

<script language="javascript">
/* function closeModal(){
		
 	window.close();
} */
	
	
  
</script>

<body id="bodyid">
<input type="hidden"  name="userId" id="userId" value="<s:property value='userId'/>"/>
<table width="90%" align="center" cellPadding="0" cellSpacing="0" height="100%">
  <tr>
  		<td> 
        
      <table class="input_table">
      <tr>
        <td class="input_tablehead"> 查看商机详情信息 </td>
      </tr>
        <s:iterator value="prjChanceList" id="customInfo">    
        <tr> 
          <td width="15%" class="input_label2"> <div align="center">客户：</div></td>
          <td bgcolor="#FFFFFF" width="35%" height="20"  align="left"> <s:property value="client"/> 
          </td>
          <td width="15%" class="input_label2"> <div align="center">负责人：</div></td>
          <td bgcolor="#FFFFFF" width="35%" height="20"  align="left"> <s:property  value="followMan" /> 
          </td>
        </tr>
        <tr> 
          <td width="10%" class="input_label2"> <div align="center"> 项目名称：</div></td>
          <td bgcolor="#FFFFFF" width="35%" height="20"  align="left"> <s:property   value="prjName" /> 
          </td>
          <td width="10%" class="input_label2"> <div align="center"> 合同金额：</div></td>
          <td bgcolor="#FFFFFF" width="35%" height="20"  align="left"> <s:property   value="total" /> 
          </td>
        </tr>
        <tr> 
          <td width="10%" class="input_label2"> <div align=center> 创建时间：</div></td>
          <td bgcolor="#FFFFFF" width="35%" height="20"  align="left"> <s:date name="creatDate" format="yyyy-MM-dd"/> 
          </td>
          <td width="10%" class="input_label2"><div align="center">   更新日期：</div></td>
          <td bgcolor="#FFFFFF" width="35%" height="20" align="left"> <s:date name="upDate" format="yyyy-MM-dd"/>
          </td>
        </tr>
        <tr> 
          <td width="10%" class="input_label2"> <div align="center"> 进度：</div></td>
          <td bgcolor="#FFFFFF" width="35%" height="20"  align="left"> <s:property   value="fare" /> 
          </td>
          <td width="10%" class="input_label2"> <div align="center"> 更新人：</div></td>
          <td bgcolor="#FFFFFF" width="35%" height="20"  align="left"> <s:property   value="updateMan" /> 
          </td>
        </tr>
       <tr> 
          <td width="10%" class="input_label2"><div align="center">  商机描述：</div></td>
          <td bgcolor="#FFFFFF" width="35%" height="20" align="left" colspan="3"> 
          	<textarea cols="50" rows="3"  readonly><s:property value="description"/></textarea>
          	
          </td>
        </tr>
        <tr> 
          <td width="10%" class="input_label2"><div align="center">  中标分析：</div></td>
          <td bgcolor="#FFFFFF" width="35%" height="20" align="left" colspan="3"> 
          	<textarea cols="50" rows="3"  readonly><s:property value="analyse"/></textarea>
          	
          </td>
        </tr>
        <tr> 
          <td width="10%" class="input_label2"> <div align="center"> 风险：</div></td>
          <td bgcolor="#FFFFFF" width="35%" height="20" align="left" colspan="3"> 
          	<textarea cols="50" rows="3"  readonly><s:property value="risk"/></textarea>
          	
          </td>
        </tr>
        
        <tr> 
          <td width="10%" class="input_label2"><div align="center">  备注：</div></td>
          <td bgcolor="#FFFFFF" width="35%" height="20" align="left" colspan="3"> 
          	<textarea cols="50" rows="3"  readonly><s:property value="note"/></textarea>
          	
          </td>
        </tr>
        <tr>
        </s:iterator>
      </table>
        </td>   
  </tr>
  <tr>
     <td align="center">
				<input type="button" name="return"
					value='<s:text  name="button.close"/>' class="MyButton"
					onclick="closeModal()" image="../../images/share/f_closed.gif">
      </td>    
  </tr>
</table>

</body>
</html>