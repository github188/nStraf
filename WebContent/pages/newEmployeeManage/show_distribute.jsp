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
	</script>
<body id="bodyid"  leftmargin="0" topmargin="10" >
		<table width="80%" class="input_table">
            <tr>
                <td>
                    
                    <table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066">
                    <tr><td class="input_tablehead"><s:text name="课程详情" /></td></tr>
                    <tr>
                        <td nowrap align="center" width="8%" class="oracolumncenterheader"><div align="center" >课程编号</div></td>
                        <td nowrap align="center" width="8%" class="oracolumncenterheader"><div align="center">课程名称</div></td>
                        <td nowrap align="center" width="8%" class="oracolumncenterheader"><div align="center">类别</div></td>
                        <td nowrap align="center" width="14%" class="oracolumncenterheader"><div align="center">计划完成日期</div></td>
                        <td nowrap align="center" width="54%" class="oracolumncenterheader"><div align="center">优先级</div></td>
                        <td nowrap align="center" width="54%" class="oracolumncenterheader"><div align="center">完成百分比</div></td>
                        <td nowrap align="center" width="54%" class="oracolumncenterheader"><div align="center">实际完成日期</div></td>
                        <td nowrap align="center" width="54%" class="oracolumncenterheader"><div align="center">完成效果</div></td>
					</tr>
                    
              <s:iterator  value="ecList" id="pd">
                     <tr>
                       <td bgcolor="#FFFFFF" align="center">	<s:property value="cid"/></td>
                          <td bgcolor="#FFFFFF" align="center"><s:property value="courseName"/></td>
                           <td bgcolor="#FFFFFF" align="center">
                           		<s:property value="category"/>
                           </td>
                          <td bgcolor="#FFFFFF" align="center">
                          	<s:date name="planFinishDate" format="yyyy-MM-dd" />
                          </td>
                          <td bgcolor="#FFFFFF" align="center">
                          	<s:property value="prioryLevel"/>
                          </td>
                          <td bgcolor="#FFFFFF" align="center">
                          	<s:property value="finishPercent"/>
                          </td>
                          <td bgcolor="#FFFFFF" align="left">
                          <s:date name="actualFinishDate" format="yyyy-MM-dd" />
                         	
                          </td>
                          <td bgcolor="#FFFFFF" align="left"
                         	 <s:property value="finishEffect"/>
                          </td>
                    </tr> 
              </s:iterator> 
         
                    </table><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td>
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