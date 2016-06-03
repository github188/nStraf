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
<%
	String seename = (String)request.getAttribute("seename");
	String seeid = (String)request.getAttribute("seeid");
%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
	<script type="text/javascript" src="../../js/tablesort.js"></script>
	<script type="text/javascript" src="../../calendar/fixDate.js"></script>
	<script type="text/javascript" src="../../js/aa.js"></script>
<title>新增页面</title>
<style>
*,body,ul,h1,h2{ margin:0; padding:0; list-style:none;}
body{font:12px "宋体"; padding-top:20px;}
a{ color:#777;border:none;}
#menu { width:100%; margin:auto;}
 #menu h1 { font-size:12px; border:#C60 1px solid; margin-top:1px;  background-color:#F93;}
 #menu h2 { font-size:12px; border:#E7E7E7 1px solid; border-top-color:#FFF; background-color:#F4F4F4;}
 #menu ul { padding-left:15px; height:100px;border:#E7E7E7 1px solid; border-top:none;overflow:auto;}
 #menu a { display:block; padding:5px 0 3px 10px; text-decoration:none; overflow:hidden;}
 #menu a:hover{ color:#6F0; background:#000;}
 #menu .no {display:none;}
 #menu .h1 a{color:#6F0;}
 #menu .h2 a{color:#06F;}
 #menu  h1 a{color:#FFF;}
</style>
<script type="text/javascript">
	function backuserid(flag){
		if(flag==1){
			var idList="";
			//var  em= document.all.tags("input");
			var em = document.getElementsByTagName('input');
			for(var  i=0;i<em.length;i++)
			{
				  if(em[i].type=="checkbox")
				  {
					   if(em[i].checked){
						   if(em[i].value!=""){
						   	idList+=","+em[i].value.split(",")[0];
						   }
					   }
				   }
			}
			if(idList==""){
				alert("请选择员工");
			}else{
				idList =  idList.substring(1);
				if(navigator.userAgent.indexOf("MSIE") != -1){//ie浏览器
					window.returnValue=idList;
					window.close();
				}else{
					var seename = "<%=seename%>";
					var seeid = "<%=seeid%>";
					parent.selectPeople(idList,seename,seeid);
				}
			}
		}
		if(flag==0){
			if(navigator.userAgent.indexOf("MSIE") != -1){//ie浏览器
				window.returnValue="";
				window.close();
			}else{
				parent.closeSelectPeople();
			}
		}
	}
	function ShowMenu(obj,n){
		 var Nav = obj.parentNode;
		 if(!Nav.id){
		  var BName = Nav.getElementsByTagName("ul");
		  var HName = Nav.getElementsByTagName("h2");
		  var t = 2;
		 }else{
		  var BName = document.getElementById(Nav.id).getElementsByTagName("span");
		  var HName = document.getElementById(Nav.id).getElementsByTagName("h1");
		  var t = 1;
		 }
		 for(var i=0; i<HName.length;i++){
		  HName[i].innerHTML = HName[i].innerHTML.replace("-","+");
		  HName[i].className = "";
		 }
		 obj.className = "h" + t;
		 for(var i=0; i<BName.length; i++){if(i!=n){BName[i].className = "no";}}
		 if(BName[n].className == "no"){
		  BName[n].className = "";
		  obj.innerHTML = obj.innerHTML.replace("+","-");
		 }else{
		  BName[n].className = "no";
		  obj.className = "";
		  obj.innerHTML = obj.innerHTML.replace("-","+");
		 }
		}
//全选
function  SelAll(all,chkAll)
{
	var names = document.getElementsByName(chkAll);
	for(var i=0;i<names.length;i++){
		if(names[i].type=="checkbox"){
			names[i].checked=all.checked;
		}
	}
}

</script>
</head>

<body id="menu">

 <h1 onClick="javascript:ShowMenu(this,0)"><a href="javascript:void(0)">按部门选择</a></a></h1>
 <span class="no">
 <s:iterator value="#request.deptUser" id="dept" status="status">
	  <h2 onClick="javascript:ShowMenu(this,<s:property value="#status.index"/>)"><a href="javascript:void(0)">
	  <input type="checkbox" value="" onclick="SelAll(this,'<s:property value="key"/>')"> 
	  <s:property value="#request.deptmaMap[key]"></s:property></a></h2>
	  <ul class="no">
	  	<s:iterator value="value">
		   <a href="javascript:void(0)"><input name="<s:property value="key"/>" type="checkbox" value="<s:property value="userid"/>:<s:property value="username"/>"><s:property value="username"/>(<s:property value="userid"/>)</a>
	   </s:iterator>
	  </ul>
  </s:iterator>
 </span>
 <h1 onClick="javascript:ShowMenu(this,1)"><a href="javascript:void(0)">按项目组选择</a></a></h1>
 <span class="no">
 <s:iterator value="#request.usrGroups" status="status">
	  <h2 onClick="javascript:ShowMenu(this,<s:property value="#status.index"/>)">
	  <a href="javascript:void(0)"><input type="checkbox" value="" onclick="SelAll(this,'<s:property value="code"/>')"> 
	  <s:property value="name"/> </a></h2>
	  <ul class="no">
	  <s:iterator value="#request.grpUser[code]">
	   <a href="javascript:void(0)"><input name="<s:property value="code"/>" type="checkbox" value="<s:property value="userid"/>:<s:property value="username"/>"><s:property value="username"/>(<s:property value="userid"/>)</a>
	   </s:iterator>
	  </ul>
  </s:iterator>
 </span>
 <div align="center">
 <input type="button" value="确定" onclick="backuserid(1)">
 <input type="button" value="取消" onclick="backuserid(0)">
</div>
</body>
</html>
