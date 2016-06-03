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


<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
	<script type="text/javascript" src="../../js/tablesort.js"></script>
	<script type="text/javascript" src="../../calendar/fixDate.js"></script>
	<script type="text/javascript" src="../../js/aa.js"></script>
	<script type="text/javascript" >
	
	function closeModal(){
 		if(!confirm('您确认关闭此页面吗？'))
		{
			return;				
		}
	 	window.close();
	}
	
	</script>
<title>详细页面</title>
</head>

<body id="bodyid">
	<input type="hidden" name="id" value="<s:property value='#request.leave.id'/>">
<table width="550"  class="input_table"  style="word-wrap: break-word; word-break: break-all;">
<tr>
	<td class="input_tablehead">请假详情 </td>
</tr>
<tr height="25px">
    <td width="25%" class="input_label2"><div ><s:text name="user.hols.username"/>：</div></td>
    <td style="background-color: #FFFFFF">
		<s:property value="#request.leave.username"/>    	
    </td>
  </tr>
  <tr height="25px">
    <td width="25%" class="input_label2"><div ><s:text name="user.leave.type"/>：</div></td>
    <td style="background-color: #FFFFFF">
      <s:property value="#request.leave.type"/> 
      </td>
  </tr>
  <tr height="25px">
    <td width="25%" class="input_label2"><div ><s:text name="user.leave.reason"/>：</div></td>
    <td width="75%" style="background-color: #FFFFFF">
     	<s:property  value="#request.leave.reason" /> 
    </td>
  </tr>
  <tr height="25px">
    <td class="input_label2"><div ><s:text name="user.leave.start"/>：</div></td>
    <td style="background-color: #FFFFFF">
    	<s:date name='#request.leave.startTime' format='yyyy-MM-dd HH:mm:ss'/>
    </td>
  </tr>
  <tr height="25px">
    <td class="input_label2"><div ><s:text name="user.leave.end"/>：</div></td>
    <td style="background-color: #FFFFFF">
    	<s:date name='#request.leave.endTime' format='yyyy-MM-dd HH:mm:ss'/>
    </td>
  </tr>
     <tr height="25px">
    <td class="input_label2"><div ><s:text name="user.leave.timelong"/>：</div></td>
    <td style="background-color: #FFFFFF">
    	<s:property value='#request.leave.sumtime'/>
    </td>
  </tr>
  <tr height="25px">
    <td class="input_label2"><div ><s:text name="user.leave.status"/>：</div></td>
    <td style="background-color: #FFFFFF">
    	<s:if test="#request.leave.status==0">新建</s:if>
    	<s:if test="#request.leave.status==2">
    		待审批
    	</s:if>
    	<s:if test="#request.leave.status==1">
    		审批不通过
    	</s:if>
    	<s:if test="#request.leave.status==3">
    		审批通过
    	</s:if>
    </td>
  </tr>
   <tr height="25px">
    <td class="input_label2"><div ><s:text name="user.leave.approver"/>：</div></td>
    <td style="background-color: #FFFFFF">
    	<s:property value='#request.leave.approverName'/>
    </td>
  </tr>
 <!--  
  <tr height="25px">
    <td class="input_label2"><div ><s:text name="user.leave.status"/>：</div></td>
    <td style="background-color: #FFFFFF">
    	<s:if test="#request.leave.status==0">新建</s:if>
    	<s:if test="#request.leave.status==2">
    		待审批
    	</s:if>
    	<s:if test="#request.leave.status==1">
    		返回修改
    	</s:if>
    	<s:if test="#request.leave.status==3">
    		审核通过
    	</s:if>
    </td>
  </tr> 
  --> 
 <s:if test="#request.record.length()>0">
   <tr height="25px">
    <td class="input_label2"><div ><s:text name="notify.approval.record"/>：</div></td>
    <td style="background-color: #FFFFFF;width: 75%">
    	<textarea cols="20" rows="10" class="text_area" readonly="readonly" style="width: 100%"><s:property value="#request.record"/></textarea>
    </td></tr>
    </s:if>
</table>

<table width="80%" cellpadding="0" cellspacing="0" align="center"> 
		<tr>
			<td align="center"> 
				<input type="button" name="return" value='<s:text  name="button.close"/>' class="MyButton"  onclick="closeModal();" image="../../images/share/f_closed.gif"> 
				</td> 
  		</tr>  
 	</table> 
</body>
</html>
