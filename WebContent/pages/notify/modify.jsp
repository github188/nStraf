<!--20100107 14:00-->
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.text.*"%>
<%@ page import="java.util.Date,cn.grgbanking.feeltm.util.DateUtil"%>
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
			 if(document.getElementById("type").value=="")
			{
				alert("请填写标题类型");
				document.getElementById("type").focus();
				return;
			}
			if(document.getElementById("title").value=="")
			{
				alert("请填写通知标题");
				document.getElementById("title").focus();
				return;
			}
			if(document.getElementById("content").value=="")
			{
				document.getElementById("content").focus();
				alert("请填写标题内容");
				return;
			}
			 if(idList==""){
				alert("请填写发送方式");
				return "";
			} 
			if(document.getElementById("mainsSee").value=="")
			{
				document.getElementById("mainsSee").focus();
				alert("请选择主送人");
				return;
			}
			if(document.getElementById("approver").value=="")
			{
				document.getElementById("approver").focus();
				alert("请选择审批人");
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
<title>修改页面</title>
</head>

<body id="bodyid">
	<form name="notifyForm" action="<%=request.getContextPath() %>/pages/notify/notifyInfo!save.action"   method="post">
	<input type = "hidden" name="notifyNum" value="<s:property value='#request.notify.notifyNum'/>">
<table width="660" height="232" class="input_table">
	<tr>
		<td class="input_tablehead"> 固定资产登记 </td>
	</tr>
	<tr>
    <td width="95" class="input_label2"><div ><s:text name="notify.list.writetime"/></div></td>
    <td class="input_writable">
		<span title="<%=new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date())%>"><%=new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date())%></span>
    </td>
  </tr>
  <tr>
    <td width="25%" class="input_label2"><div ><s:text name="notify.list.type"/>
    <font color="#FF0000">*</font></div></td>
    <td class="input_writable">
      <label></label>  
      <select name="notify.type" style="width:150" id="type">
      	<option><s:text name="notify.option"></s:text></option>
      	<s:iterator value="#request.typeMap">
      		<option value="<s:property value='key'/>" <s:if test='key==#request.notify.type'>selected</s:if>><s:property value="value"/></option>
      	</s:iterator>
      </select></td>
  </tr>
  <tr>
    <td width="95" class="input_label2"><div ><s:text name="notify.list.title"/>
    <font color="#FF0000">*</font></div></td>
    <td class="input_writable">
      <label></label> <input name="notify.title" type="text" id="title" size="100%" value=<s:property value='#request.notify.title'/>></td>
  </tr>
  <tr>
    <td class="input_label2"><div ><s:text name="notify.list.content"/></div></td>
    <td class="input_writable"><textarea name="notify.content" id="content" cols="75" rows="10"><s:property value="#request.notify.content"></s:property></textarea></td>
  </tr>
  <tr>
    <td class="input_label2"><div ><s:text name="notify.list.sendtype"/></div></td>
    <td class="input_writable"> 
    	<input name="notify.oatype" type="checkbox" value="1" id="oa" <s:if test='1==#request.notify.oatype'>checked</s:if>/>OA发送
    	<input name="notify.emailtype" type="checkbox" value="1" id="email" <s:if test='1==#request.notify.emailtype'>checked</s:if>/>邮件发送
    	<!-- <input name="notify.mobiletype" type="checkbox" value="1" id="mobile" <s:if test='1==#request.notify.mobiletype'>checked</s:if>/>手机发送 -->
    </td>
  </tr>
  <tr>
    <td class="input_label2"><div ><s:text name="notify.send.zhusong"/></div></td>
    <td class="input_writable"><input type="text" name="mainnames" id="mainsSee" size="90" value="<s:property value='#request.mainnames'/>" readonly="readonly">
    <input type="hidden" name="mainids" id="mainsHidden" size="90" value="<s:property value='#request.mainids'/>">
    	<input type="button" value="选择" id="zhusong" onclick="selectMainPeople('mainsSee','mainsHidden')"/>
    </td>
  </tr>
  <tr>
    <td class="input_label2"><div ><s:text name="notify.send.chaosong"/></div></td>
    <td class="input_writable">
    <input type="text" name="extrasnames" id="extrasSee" size="90" value="<s:property value='#request.extranames'/>" readonly="readonly">
    <input type="hidden" name="extrasids" id="extrasHidden" size="90" value="<s:property value='#request.extraids'/>">
    <input type="button" value="选择" id="chaosong" onclick="selectMainPeople('extrasSee','extrasHidden')"/>
    </td>
     
  </tr>
  <tr>
    <td class="input_label2"><div ><s:text name="notify.list.status"/></div></td>
    <td class="input_writable">
    	<s:if test="#request.notify.status==0">新建</s:if>
    	<s:if test="#request.notify.status==2">
    		待审批
    	</s:if>
    	<s:if test="#request.notify.status==1">
    		返回修改
    	</s:if>
    	<s:if test="#request.notify.status==3">
    		审批通过
    	</s:if>
    </td>
     
  </tr>
  <tr>
    <td class="input_label2"><div ><s:text name="notify.list.sender"/></div></td>
    <td class="input_writable">
		&nbsp;&nbsp;<s:property value="#request.notify.username"/>    	
    </td>
  </tr>
   <tr>
    <td class="input_label2"><div ><s:text name="notify.list.approval"/></div></td>
    <td class="input_writable">
    	<select name="notify.approver" id="approver">
    		<option value="">&nbsp;<s:text name="notify.option"></s:text>&nbsp;</option>
    		<s:iterator value="#request.approvallist" var="list">
    			<option value="<s:property value='userid'/>" <s:if test="userid==#request.notify.approver">selected</s:if>><s:property value="username"/> </option>
    		</s:iterator>
    	</select>
    </td>
  </tr>
  
  <s:if test="#request.record.length>0">
	  <tr>
	    <td class="input_label2"><div ><s:text name="notify.approval.record"/></div></td>
	    <td class="input_writable">
	    	<textarea name="record" cols="50" rows="10" readonly="readonly"><s:property value="#request.record"></s:property></textarea>
	    </td></tr>
    </s:if>
    
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
