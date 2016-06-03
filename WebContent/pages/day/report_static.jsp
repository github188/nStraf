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
	<script type="text/javascript" src="../../js/jquery.js"></script>
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
                    <legend><s:text name="日报统计" /></legend>
                    <table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066">
                    <br/>
                    <tr>
                    <td nowrap align="center" width="14%" class="oracolumncenterheader"><div align="center" >日期</div></td>
            <td nowrap align="center" width="10%" class="oracolumncenterheader"><div align="center">星期</div></td>
			<td nowrap align="center" width="8%" class="oracolumncenterheader"><div align="center">人数</div></td>
			<td nowrap align="center" width="14%" class="oracolumncenterheader"><div align="center">工作量</div></td>
            <td nowrap align="center" width="54%" class="oracolumncenterheader"><div align="center">未写日报人员</div></td>
                    	<%--<th align="center" width="20%" bgcolor="#999999">升级人</th>
                        <th align="center" width="20%" bgcolor="#999999">升级日期</th>
                        <th align="center" width="60%" bgcolor="#999999">升级版本</th>
                    --%></tr>
              <s:iterator  value="dayStatic" id="pd">
                     <tr>
                          <td bgcolor="#FFFFFF" align="center"><s:date name="startDate" format="yyyy-MM-dd"/></td>
                           <td bgcolor="#FFFFFF" align="center">
                           		<s:if test='weekDay.equals("星期六")||weekDay.equals("星期日")'>
                           		<font color='red'>
                           		<s:property value="weekDay"/>
                           		</font>
                           		</s:if>
                           		<s:else>
                           		<s:property value="weekDay"/>
                           		</s:else>
                           </td>
                          <td bgcolor="#FFFFFF" align="center"><s:property value="personNum"/></td>
                          <td bgcolor="#FFFFFF" align="center"><s:property value="taskTime"/>
                            <s:if test="taskTime-8*(personNum)<0">
                          		<font color="red">
                          		(<s:property value="taskTime-8*(personNum)"/>)
                          		</font>
                          </s:if>
                          <s:else>
                          		(<s:property value="taskTime-8*(personNum)"/>)
                          </s:else>
                          </td>
                          <td bgcolor="#FFFFFF" align="left"><s:property value="personDetail" escape="false"/></td>
                    </tr> 
              </s:iterator> 
         
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