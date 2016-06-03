<!--20100107 14:00-->
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/dialog.inc"%>
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
	<script language="javascript" src="../../js/aa.js"></script>
	<script language="javascript" src="../../js/jquery.js"></script>
	<script type="text/javascript">
	
	function closeModal(){

	 	window.close();
	}
	
	function selectVersion(id) {
		var logValue=document.getElementById(id).innerHTML; 
		//alert(logValue);
	    //rValue = new Array(logValue,'00')
	    formsubmit(logValue);
	}

	function formsubmit(rValue){
		
		//parent.returnValue = rValue;
	    window.returnValue = rValue;
	    parent.close();
	}
	      
	</script>
<body id="bodyid"  leftmargin="0" topmargin="10" >
		<table width="80%" align="center" cellPadding="0" cellSpacing="0">
            <tr>
                <td>
                <fieldset class="jui_fieldset" width="100%">
                    <legend><s:text name="升级历史列表" /></legend>
                    <table width="652" align="center" border="0" cellspacing="1" cellpadding="1" bgcolor="#583F70" height="70">
                    <br/>
                    <tr>
                    	<th align="center" width="10%" bgcolor="#999999"> 选项 </th>
                    	<th align="center" width="10%" bgcolor="#999999">升级人</th>
                        <th align="center" width="20%" bgcolor="#999999">升级日期</th>
                        <th align="center" width="40%" bgcolor="#999999">升级版本</th>
                    </tr>
          	<s:if test="logs!=null && logs.size>0"> 
              <s:iterator  value="logs" >
                     <tr>
                     	 <td bgcolor="#FFFFFF" align="center" ><input type="radio" onClick="javascript:selectVersion('<s:property value="id"/>');"</td>
                          <td bgcolor="#FFFFFF" align="center" ><s:property value="update_man"/></td>
                          <td bgcolor="#FFFFFF" align="center" ><s:date name="update_date" format="yyyy-MM-dd HH:mm:ss"/></td>
                          <td bgcolor="#FFFFFF" align="center" id='<s:property value="id"/>'><s:property value="update_version" escape="false"/></td>
                    </tr> 
              </s:iterator> 
           </s:if > 
           <s:else>
                 <td bgcolor="#FFFFFF"align="center">--</td>
           	<td bgcolor="#FFFFFF"align="center">--</td>
                 <td bgcolor="#FFFFFF" align="center">--</td>
                 <td bgcolor="#FFFFFF"align="center">--</td>
           </s:else>
                    </table><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td>
                </fieldset>
                </td> 
            </tr> 
    </table>
<br/>
<br/>
<table width="80%" cellpadding="0" cellspacing="0" align="center"> 
		<tr>
			<td align="center"> 
				<input type="button" name="return" value='<s:text  name="button.close"/>' class="MyButton"  onclick="closeModal();" image="../../images/share/f_closed.gif"> 
			</td> 
  		</tr>  
 	</table> 
			
 	</form>
    
    
</body>  
</html> 