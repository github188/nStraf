<!--20100107 14:00-->
<%@ page contentType="text/html; charset=UTF-8" %>
<%@page import="cn.grgbanking.feeltm.login.domain.UserModel"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa" %>
<html>
<head><title>tool query</title></head>
	<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
	<script type="text/javascript" src="../../js/tablesort.js"></script>
	<script type="text/javascript" src="../../calendar/fixDate.js"></script>
	<script type="text/javascript" src="../../js/aa.js"></script>
	<%@ include file="/inc/pagination.inc"%>
	<script type="text/javascript">
	
	function closeModal(){

	 	window.close();
	}
	      
	</script>
<body id="bodyid"  leftmargin="0" topmargin="10" >
		<table width="80%" align="center" cellPadding="0" cellSpacing="0">
            <tr>
                <td>
                <fieldset class="jui_fieldset" style="width:100%">
                    <legend><s:text name="版本概览" /></legend>
                    <table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066">
                    <br/>
                    <tr>
                    <td nowrap align="center" width="12%" class="oracolumncenterheader"><div align="center" >项目版本</div></td>
            <td nowrap align="center" width="13%" class="oracolumncenterheader"><div align="center">提交版本日期</div></td>
			<td nowrap align="center" width="8%" class="oracolumncenterheader"><div align="center">质量得分</div></td>
			<td nowrap align="center" width="8%" class="oracolumncenterheader"><div align="center">版本质量</div></td>
            <td nowrap align="center" width="6%" class="oracolumncenterheader"><div align="center">缺陷总数</div></td>
            <td nowrap align="center" width="6%" class="oracolumncenterheader"><div align="center">缺陷严重性</div></td>
            <td nowrap align="center" width="6%" class="oracolumncenterheader" ><div align="center">缺陷总值</div></td>
             <td nowrap align="center" width="6%" class="oracolumncenterheader"><div align="center">缺陷密度</div></td>
                    	<%--<th align="center" width="20%" bgcolor="#999999">升级人</th>
                        <th align="center" width="20%" bgcolor="#999999">升级日期</th>
                        <th align="center" width="60%" bgcolor="#999999">升级版本</th>
                    --%></tr>
          	<s:if test="prjVersionDetail!=null && prjVersionDetail.size>0"> 
              <s:iterator  value="prjVersionDetail" id="pd">
                     <tr height="">
                          <td bgcolor="#FFFFFF" align="center"><s:property value="versionNO"/></td>
                          <td bgcolor="#FFFFFF" align="center"><s:property value="detectionDate"/></td>
                          <td bgcolor="#FFFFFF" align="center"><s:property value="qualityPoint"/></td>
                          <td bgcolor="#FFFFFF" align="center"><s:property value="versionQuality"/></td>
                          <td bgcolor="#FFFFFF" align="center"><s:property value="validBugCount"/></td>
                          <td bgcolor="#FFFFFF" align="center"><s:property value="bugSerious"/></td>
                          <td bgcolor="#FFFFFF" align="center"><s:property value="bugTotalValue"/></td>
                          <td bgcolor="#FFFFFF" align="center"><s:property value="bugDensity"/></td>
                          
                    </tr> 
              </s:iterator> 
           </s:if > 
           <s:else>
           		<td bgcolor="#FFFFFF" align="center">--</td>
                 <td bgcolor="#FFFFFF" align="center">--</td>
                 <td bgcolor="#FFFFFF" align="center">--</td>
           		<td bgcolor="#FFFFFF" align="center">--</td>
                 <td bgcolor="#FFFFFF" align="center">--</td>
                 <td bgcolor="#FFFFFF" align="center">--</td>
           		<td bgcolor="#FFFFFF" align="center">--</td>
                 <td bgcolor="#FFFFFF" align="center">--</td>
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