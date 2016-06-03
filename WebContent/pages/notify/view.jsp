<!--20100107 14:00-->
<%@ page contentType="text/html; charset=UTF-8" %>
<%@page import="cn.grgbanking.feeltm.login.domain.UserModel"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
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
	<script language='JavaScript' src="../../js/jquery-1.11.0.js"></script>
	<script type="text/javascript" >
	/* function closeModal(){
 		if(!confirm('您确认关闭此页面吗？'))
		{
			return;				
		}
	 	window.close();
	} */
	
	function sureModal(){
		window.close();
	}
	</script>
<title>查看页面</title>
</head>

<body id="bodyid">
	<input type = "hidden" name="notifyNum" value="<s:property value='#request.notify.notifyNum'/>">
<table width="660"  class="input_table">
	<tr>
		<td class="input_tablehead"> 通知通告 </td>
	</tr>
	<tr>
    <td width="95" class="input_label2"><div ><s:text name="notify.list.writetime"/>：</div></td>
    <td class="input_writable">
    <s:date name="#request.notify.writeTime"  format="yyyy-MM-dd HH:mm"/>
    </td>
  </tr>
  	<tr>
    <td width="35%" class="input_label2"><div ><s:text name="notify.list.type"/>：
    </div></td>
    <td class="input_writable">
      <label></label>  
      <input name="notify.title" type="text" id="title" size="90" value=<s:property value='#request.notify.type'/> readonly="readonly">
  </tr>
  <tr>
    <td class="input_label2"><div ><s:text name="notify.list.title"/>：
    </div></td>
    <td class="input_writable">
      <label></label> <input name="notify.title" type="text" id="title" size="90" value=<s:property value='#request.notify.title'/> readonly="readonly"></td>
  </tr>
  <tr>
    <td class="input_label2"><div ><s:text name="notify.list.content"/>：</div></td>
    <td class="input_writable"><textarea name="notify.content" id="content" cols="68" rows="10" readonly="readonly"><s:property value="#request.notify.content"></s:property></textarea></td>
  </tr>
  <tr>
    <td class="input_label2"><div ><s:text name="notify.list.sendtype"/>：</div></td>
    <td class="input_writable">
    	<input name="notify.oatype" type="checkbox" value="1" id="oa" <s:if test='1==#request.notify.oatype'>checked</s:if>/ readonly="readonly">OA发送
    	<input name="notify.emailtype" type="checkbox" value="1" id="email" <s:if test='1==#request.notify.emailtype'>checked</s:if>/ readonly="readonly">邮件发送
    </td>
  </tr>
  <tr>
    <td class="input_label2"><div ><s:text name="notify.send.zhusong"/>：</div></td>
    <td class="input_writable"><input type="text" name="mainnames" id="mainsSee" size="90" value="<s:property value='#request.mainnames'/>" readonly="readonly">
    <input type="hidden" name="mainids" id="mainsHidden" size="90" value="<s:property value='#request.mainids'/>" readonly="readonly">
    </td>
  </tr>
  <tr>
    <td class="input_label2"><div ><s:text name="notify.send.chaosong"/>：</div></td>
    <td class="input_writable">
    <input type="text" name="extrasnames" id="extrasSee" size="90" value="<s:property value='#request.extranames'/>" readonly="readonly">
    <input type="hidden" name="extrasids" id="extrasHidden" size="90" value="<s:property value='#request.extraids'/>" readonly="readonly">
    </td>
     
  </tr>
  <tr>
    <td class="input_label2"><div><s:text name="notify.list.status"/>：</div></td>
    <td class="input_writable">
    	<s:if test="#request.notify.status==0">新建</s:if>
    	<s:if test="#request.notify.status==2">
    		待审批
    	</s:if>
    	<s:if test="#request.notify.status==1">
    		不通过
    	</s:if>
    	<s:if test="#request.notify.status==3">
    		审批通过
    	</s:if>
    </td>
     
  </tr>
  <tr>
    <td class="input_label2"><div ><s:text name="notify.list.sender"/>：</div></td>
    <td class="input_writable">
		&nbsp;&nbsp;<s:property value="#request.notify.username"/>    	
    </td>
  </tr>
   <s:if test="#request.record.length>0">
	  <tr>
	    <td class="input_label2"><div ><s:text name="notify.approval.record"/>：</div></td>
	    <td class="input_writable">
	    	<textarea name="record" cols="68" rows="10" readonly="readonly"><s:property value="#request.record"/></textarea>
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
