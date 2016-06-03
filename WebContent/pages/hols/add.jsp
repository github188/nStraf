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
	function save(type){
			window.returnValue=true;
			if(type=="0"){
				var usernames = document.getElementById("mainsSee").value;
				var yeartime = document.getElementById("yeartime").value;
				var deferredtime = document.getElementById("deferredtime").value;
				var reg = new RegExp("^[0-9]*[.][0-9]*$");
				var patten = /^-?\d+\.?\d{0,2}?$/;
				if(usernames==""){
					alert("请选择人员");
					document.getElementById("mainsSee").focus();
					return false;
				}
				if(yeartime==""){
					alert("请输入年假时间");
					document.getElementById("yeartime").focus();
					return false;
				}
				if(deferredtime==""){
					alert("请输入可调休时间");
					document.getElementById("deferredtime").focus();
					return false;
				}
				if(!patten.test(yeartime)){
					alert("年假时间应该为2位小数或整数");
					document.getElementById("yeartime").focus();
					return false;
				}
				if(!patten.test(deferredtime)){
					alert("可调休时间应该为2位小数或整数");
					document.getElementById("deferredtime").focus();
					return false;
				}
				$("#ok").attr("disabled","disabled");
				holsForm0.submit();
			}
			if(type=="1"){
				var deptcode = document.getElementById("deptName").value;
				if(deptcode==""){
					alert("选择部门");
					document.getElementById("deptName").focus();
					return false;
				}
				$("#ok").attr("disabled","disabled");
				holsForm1.submit();
			}
			if(type=="2"){
				var grpcode = document.getElementById("grgcode").value;
				if(grpcode==""){
					alert("选择项目组");
					document.getElementById("grgcode").focus();
					return false;
				}
				$("#ok").attr("disabled","disabled");
				holsForm2.submit();
			}
		}
	
	function selectMainPeople(see,hidden){
		var strUrl="/pages/notify/notifyInfo!select.action";
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
	
	function closeModal(){
 		if(!confirm('您确认关闭此页面吗？'))
		{
			return;				
		}
	 	window.close();
	}
	
	function selectType(type){
		if(type=='0'){
			document.getElementById('personDiv').style.display = "";
			document.getElementById('departmentDiv').style.display = "none";
			document.getElementById('groupnameDiv').style.display = "none";
		}
		if(type=='1'){
			document.getElementById('personDiv').style.display = "none";
			document.getElementById('departmentDiv').style.display = "";
			document.getElementById('groupnameDiv').style.display = "none";
		}
		if(type=='2'){
			document.getElementById('personDiv').style.display = "none";
			document.getElementById('departmentDiv').style.display = "none";
			document.getElementById('groupnameDiv').style.display = "";
		}
	}
	</script>
<title>新增页面</title>
</head>

<body id="bodyid">
<div>
	<input type="button" id="person" onclick="selectType('0')" value="个人增加">
	<input type="button" id="department" onclick="selectType('1')" value="按部门添加">
	<input type="button" id="groupname" onclick="selectType('2')" value="按项目组添加">
</div>
<form name="holsForm0" action="<%=request.getContextPath() %>/pages/hols/holsInfo!save.action?type=0"   method="post">
<div id="personDiv">
<table width="400"  class="input_table" style="background-color: #FFFFFF">
<tr>
	<td class="input_tablehead">新增假期数据</td>
</tr>
<tr>
    <td class="input_label2"><div align="right"><s:text name="user.hols.username"/>
     <font color="#FF0000">*</font></div></div></td>
    <td><textarea rows="5" cols="40" name="usernames" id="mainsSee" readonly="readonly"></textarea>
    <input type="hidden" name="userids" id="mainsHidden" value="<s:property value='#request.mainids'/>">
    <input type="button" value="选择" id="zhusong" onclick="selectMainPeople('mainsSee','mainsHidden')"/>
    </td>
  </tr>
  <tr>
    <td class="input_label2"><div align="right"><s:text name="user.hols.year"/>
    <font color="#FF0000">*</font></div></td>
    <td>
      <input name="yeartime" type="text" id="yeartime" maxlength="7"
      	onblur="this.value=this.value.replace(/[^\d+\.]/g,'')" onkeyup="this.value=this.value.replace(/[^\d+\.]/g,'')" onkeypress="this.value=this.value.replace(/[^\d+\.]/g,'')" >
      <s:text name="user.hols.timetype1"/></td>
  </tr>
  <tr>
    <td class="input_label2"><div align="right"><s:text name="user.hols.deferred"/>
    <font color="#FF0000">*</font></div>
   </td>
    <td>
      <label></label> <input name="deferredtime" type="text" id="deferredtime" maxlength="7"
      	onblur="this.value=this.value.replace(/[^\d+\.]/g,'')" onkeyup="this.value=this.value.replace(/[^\d+\.]/g,'')" onkeypress="this.value=this.value.replace(/[^\d+\.]/g,'')">
       <s:text name="user.hols.timetype1"/></td>
  </tr>
</table>
<table width="80%" cellpadding="0" cellspacing="0" align="center"> 
		<tr>
			<td align="center"> 
				<input type="button" name="ok" id="ok" value='<s:text  name="grpInfo.ok" />' class="MyButton"  onclick="save('0');"   image="../../images/share/yes1.gif"> 
				<input type="button" name="return" value='<s:text  name="button.close"/>' class="MyButton"  onclick="closeModal();" image="../../images/share/f_closed.gif"> 
				</td> 
  		</tr>  
</table> 
    </form>
    </div>
    <form name="holsForm1" action="<%=request.getContextPath() %>/pages/hols/holsInfo!save.action?type=1"   method="post">
<div id="departmentDiv" style="display:none">
<table width="400" class="input_table">
<tr>
	<td class="input_tablehead">新增假期数据</td>
</tr>
<tr>
    <td class="input_label2"><div align="right"><s:text name="user.hols.department"/>
     <font color="#FF0000">*</font></div></div></td>
    <td bgcolor="#ffffff">
    <select name="deptcode" id="deptName">
			<option value=""><s:text name="staff.add.option"/></option>
				<s:iterator value="#request.deptMap" id="dept">
					   <option value="<s:property value='key'/>"><s:property value="value"/></option>
				</s:iterator>
	</select>
    </td>
  </tr>
</table>
<table width="80%" cellpadding="0" cellspacing="0" align="center"> 
		<tr>
			<td align="center"> 
				<input type="button" name="ok" id="ok" value='<s:text  name="grpInfo.ok" />' class="MyButton"  onclick="save('1');"   image="../../images/share/yes1.gif"> 
				<input type="button" name="return" value='<s:text  name="button.close"/>' class="MyButton"  onclick="closeModal();" image="../../images/share/f_closed.gif"> 
				</td> 
  		</tr>  
</table> 
    </form>
    </div>
    <form name="holsForm2" action="<%=request.getContextPath() %>/pages/hols/holsInfo!save.action?type=2"   method="post">
<div id="groupnameDiv" style="display:none">
<table width="400"  class="input_table">
<tr>
	<td class="input_tablehead">新增假期数据</td>
</tr>
<tr>
    <td class="input_label2"><div align="right"><s:text name="user.hols.group"/>
     <font color="#FF0000">*</font></div></div></td>
    <td bgcolor="#ffffff">
    <select name="grpcode" id="grgcode" style="width:50%">
			<option value=""><s:text name="staff.add.option"/></option>
				<s:iterator value="#request.usrGroups" id="group">
					   <option value="<s:property value='code'/>"><s:property value="name"/></option>
				</s:iterator>
	</select>
    </td>
  </tr>
</table>
<table width="80%" cellpadding="0" cellspacing="0" align="center"> 
		<tr>
			<td align="center"> 
				<input type="button" name="ok" id="ok" value='<s:text  name="grpInfo.ok" />' class="MyButton"  onclick="save('2');"   image="../../images/share/yes1.gif"> 
				<input type="button" name="return" value='<s:text  name="button.close"/>' class="MyButton"  onclick="closeModal();" image="../../images/share/f_closed.gif"> 
				</td> 
  		</tr>  
</table> 
    </div>
    </form>
</body>
</html>
