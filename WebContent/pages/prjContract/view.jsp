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

<body id="bodyid"  leftmargin="0" topmargin="10">
 	 
<table width="90%" align="center" cellPadding="0" cellSpacing="0" height="100%">
			<input type="hidden"  name="userId" id="userId" value="<s:property value='userId'/>"/>
        <tr><td>
      <table class="input_table">
      <tr>
	       <td class="input_tablehead"> 查看合同详情信息 </td></tr>
        <s:iterator value="prjContractList" id="prjContractInfo">    
        <tr> 
          <td width="15%" class="input_label2"> 项目名称：</td>
          <td bgcolor="#FFFFFF" width="35%" height="20"  align="left"> 
          <s:property value="prjName"/>
           
          </td>
          <td  width="15%" class="input_label2"> 项目经理：</td>
          <td bgcolor="#FFFFFF" width="35%" height="20"  align="left"> 
          <s:property  value="prjManager" />
          
          </td>
        </tr>
        <tr> 
          <td width="10%" class="input_label2">  甲方：</td>
          <td bgcolor="#FFFFFF" width="35%" height="20"  align="left"> 
          <s:property   value="client" />
          
          </td>
          <td width="10%" class="input_label2">  合同状态：</td>
          <td bgcolor="#FFFFFF" width="35%" height="20"  align="left"> 
          <s:property   value="status" />
          </td>
        </tr>
        <tr> 
          <td width="10%" class="input_label2">  项目开始时间：</td>
          <td bgcolor="#FFFFFF" width="35%" height="20"  align="left"> 
          <s:date name="startDate" format="yyyy-MM-dd"/>
           
          </td>
          <td width="10%" class="input_label2" >  项目结束时间：</td>
          <td bgcolor="#FFFFFF" width="35%" height="20" align="left"> 
           <s:date name="endDate" format="yyyy-MM-dd"/>
          
          </td>
        </tr>
        <tr> 
          <td width="10%" class="input_label2">  项目交付时间：</td>
          <td bgcolor="#FFFFFF" width="35%" height="20" align="left"> 
          <s:date name="finishDate" format="yyyy-MM-dd"/>
          
          </td>
          <td width="10%" class="input_label2">  更新时间：</td>
          <td bgcolor="#FFFFFF" width="35%" height="20" align="left">
          <s:date name="upDate" format="yyyy-MM-dd"/>
          
          </td>
        </tr>
         <tr> 
          <td width="10%" class="input_label2">  项目签订时间：</td>
          <td bgcolor="#FFFFFF" width="35%" height="20" align="left"> 
          <s:date name="signDate" format="yyyy-MM-dd"/>
          </td>
           <td width="10%" class="input_label2">  进度：</td>
          <td bgcolor="#FFFFFF" width="35%" height="20" align="left"> 
          <s:property value="fare"/>
          </td>
        </tr>
       <tr> 
          <td width="10%" class="input_label2">  合同金额：</td>
          <td bgcolor="#FFFFFF" width="35%" height="20" align="left"> 
          <s:property value="total"/>
          	元
          </td>
          <td width="10%" class="input_label2">  已交付金额：</td>
          <td bgcolor="#FFFFFF" width="35%" height="20" align="left"> 
          <s:property value="payment"/>
          	元
          </td>
        </tr>
        <tr> 
          <td width="10%" class="input_label2">  更新人：</td>
          <td bgcolor="#FFFFFF" width="35%" height="20" align="left"> 
			<s:property value="updateMan"/>          	
          </td>
        </tr>
        <tr> 
          <td width="10%" class="input_label2">  合同风险：</td>
          <td bgcolor="#FFFFFF" width="35%" height="20" align="left" colspan="3"> 
          	
          	<textarea cols="65" rows="6"  readonly><s:property value="risk"/></textarea>
          </td>
        </tr>
        
        <tr> 
          <td width="10%" class="input_label2">  备注：</td>
          <td bgcolor="#FFFFFF" width="35%" height="20" align="left" colspan="3"> 
          	<textarea cols="65" rows="6"  readonly><s:property value="note"/></textarea>
          </td>
        </tr>
        <tr>
        </s:iterator>
      </table>
        </td>   
  </tr>
  <tr>
     <td align="center"> <br>
				<input type="button" name="return"
					value='<s:text  name="button.close"/>' class="MyButton"
					onclick="closeModal()" image="../../images/share/f_closed.gif">
      </td>    
  </tr>
</table>

</body>
</html>