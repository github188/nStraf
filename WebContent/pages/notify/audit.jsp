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
	function save(){
		    var idList="";
			//var  em= document.all.tags("input");
			var em = document.getElementsByTagName("input");
			for(var  i=0;i<em.length;i++)
			{
			   if(em[i].type=="checkbox")
			   {
				   if(em[i].checked){
					   idList+=em[i].value;
				   }
			   }
			}
			if(idList==""){
				alert("请填写发送方式");
				return "";
			} 
			if(document.getElementById("result").value=="")
			{
				document.getElementById("result").focus();
				alert("请填写审批结果");
				return;
			}
			if(document.getElementById("option").value=="")
			{
				document.getElementById("option").focus();
				alert("请填写审批意见");
				return;
			}
			window.returnValue=true;
			$("#ok").attr("disabled","disabled");
			notifyForm.submit();
		}
	
	function selectMainPeople(see,hidden){
		var strUrl="/pages/notify/notifyInfo!select.action?see="+see+"&hidden="+hidden;
		var feature="520,380,notidy.addTitle,notify";
	 	var returnValue=OpenModal(strUrl,feature);
	 	if(returnValue!=null && returnValue!=""){
		 	var values = returnValue.split(",");
		 	var name = ",";
		 	var id = ",";
		 	 for(var i=0;i<values.length;i++){
		 		 var temp = values[i].split(":");
		 		id = id + temp[0]+",";
		 		name = name + temp[1]+","; 
		 	} 
		 	name = name.substring(1);
		 	id =  id.substring(1);
		 	document.getElementById(see).value = name;
		 	document.getElementById(hidden).value = id; 
	 	}
	}
	
	/* function closeModal(){
 		if(!confirm('您确认关闭此页面吗？'))
		{
			return;				
		}
	 	window.close();
	} */
	
	$(function(){
		$("#result").change(function(){
			if($(this).val()==0){
				$("#option").html("同意");
			}else{
				$("#option").html("");
			}
		});
	});
	function setSelectPeopleValue(idList,see,hidden){
		if(idList!=null && idList!=""){
		 	var values = idList.split(",");
		 	var name = ",";
		 	var id = ",";
		 	 for(var i=0;i<values.length;i++){
		 		 var temp = values[i].split(":");
		 		id = id + temp[0]+",";
		 		name = name + temp[1]+","; 
		 	} 
		 	name = name.substring(1);
		 	id =  id.substring(1);
		 	document.getElementById(see).value = name;
		 	document.getElementById(hidden).value = id; 
	 	}
	}
	</script>
<title>审批页面</title>
</head>

<body id="bodyid">
	<form name="notifyForm" action="<%=request.getContextPath() %>/pages/notify/notifyInfo!audit.action"   method="post">
	<input type = "hidden" name="notifyNum" value="<s:property value='#request.notify.notifyNum'/>">
<table width="660" border="1">
  <tr>
    <td width="25%" bgcolor="#999999"><div align="right"><s:text name="notify.list.type"/>
    <font color="#FF0000">*</font></div></td>
    <td>
      <input type="text" id="title" size="40" value=<s:property value='#request.notify.type'/> readonly="readonly">
  </tr>
  <tr>
    <td bgcolor="#999999"><div align="right"><s:text name="notify.list.title"/>
    <font color="#FF0000">*</font></div></td>
    <td>
       <input name="notify.title" type="text" id="title" style="width:100%;" value=<s:property value='#request.notify.title'/> readonly="readonly"></td>
  </tr>
  <tr>
    <td bgcolor="#999999"><div align="right"><s:text name="notify.list.content"/></div></td>
    <td><textarea name="notify.content" id="content" cols="75" rows="10" readonly="readonly"><s:property value="#request.notify.content"></s:property></textarea></td>
  </tr>
  <tr>
    <td bgcolor="#999999"><div align="right"><s:text name="notify.list.sendtype"/></div></td>
    <td>
    	<input name="notify.oatype" type="checkbox" value="1" id="oa" <s:if test='1==#request.notify.oatype'>checked</s:if>/>OA发送
    	<input name="notify.emailtype" type="checkbox" value="1" id="email" <s:if test='1==#request.notify.emailtype'>checked</s:if>/>邮件发送
    	<!-- <input name="notify.mobiletype" type="checkbox" value="1" id="mobile" <s:if test='1==#request.notify.mobiletype'>checked</s:if>/>手机发送 -->
    </td>
  </tr>
  <tr>
    <td bgcolor="#999999"><div align="right"><s:text name="notify.send.zhusong"/></div></td>
    <td><input type="text" name="mainnames" id="mainsSee" style="width:100%;" value="<s:property value='#request.mainnames'/>" readonly="readonly">
    <input type="hidden" name="mainids" id="mainsHidden" size="90" value="<s:property value='#request.mainids'/>" >
    </td>
  </tr>
  <tr>
    <td bgcolor="#999999"><div align="right"><s:text name="notify.send.chaosong"/></div></td>
    <td>
    <input type="text" name="extrasnames" id="extrasSee" style="width:100%;" value="<s:property value='#request.extranames'/>" readonly="readonly">
    <input type="hidden" name="extrasids" id="extrasHidden" size="90" value="<s:property value='#request.extraids'/>">
    </td>
     
  </tr>
  <tr>
    <td bgcolor="#999999"><div align="right"><s:text name="notify.list.status"/></div></td>
    <td>
    	待审批
    </td>
     
  </tr>
  <tr>
    <td bgcolor="#999999"><div align="right"><s:text name="notify.list.sender"/></div></td>
    <td>
		&nbsp;&nbsp;<s:property value="#request.notify.username"/>    	
    </td>
  </tr>
  <tr>
    <td bgcolor="#999999"><div align="right"><s:text name="notify.approval.result"/></div></td>
	    <td>
	    	<select name="result" id="result">
	    		<option value="">请选择</option>
	    		<option value="0">通过</option>
	    		<option value="1">不通过</option>
	    	</select>
	    </td>
    </tr>
  <tr>
    <td bgcolor="#999999"><div align="right"><s:text name="notify.approval.option"/></div></td>
    <td>
    	<textarea name="option" id="option" cols="50" rows="5"></textarea>
    </td></tr>
  <tr>
    <td bgcolor="#999999"><div align="right"><s:text name="notify.approval.record"/></div></td>
    <td>
    	<textarea name="record" cols="50" rows="10" readonly="readonly"></textarea>
    </td></tr>
</table>

<table width="80%" cellpadding="0" cellspacing="0" align="center"> 
		<tr>
			<td align="center"> 
				<input type="button" name="ok" id="ok" value='<s:text  name="grpInfo.ok" />' class="MyButton"  onclick="save();"   image="../../images/share/yes1.gif"> 
				<input type="button" name="return" value='<s:text  name="button.close"/>' class="MyButton"  onclick="closeModal(true);" image="../../images/share/f_closed.gif"> 
				</td> 
  		</tr>  
 	</table> 
    </form>
</body>
</html>
