<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>自动化测试页面</title>
	<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
	<script type="text/javascript" src="../../js/tablesort.js"></script>
	<script type="text/javascript" src="../../calendar/fixDate.js"></script>
	<script type="text/javascript" src="../../js/aa.js"></script>
<script type="text/javascript">

	var testType="";
	var standard="";
	var machineNo="";
function commit()
{	
	testType="";
	machineNo="";
	standard="";
	
	if(document.getElementById("versionNo").value == "" || document.getElementById("versionNo").value == null)
	{
		alert("请输入版本号");
		return;
	}
	
	if(document.getElementById("qc_username").value == "" || document.getElementById("qc_username").value == null)
	{
		alert("请输入QC用户名");
		return;
	}
	
	if(document.getElementById("qc_password").value == "" || document.getElementById("qc_password").value == null)
	{
		alert("请输入QC密码");
		return;
	}
	
	$("input[name='testType']").each(function ()
	{
			if($(this).attr("checked"))
			{
				testType=testType+$(this).val()+",";
			}
	});
	if(testType=="")
	{
		alert("请选择测试类型");
		return;
	}
	
	$("input[name='standard']").each(function ()
	{
				if($(this).attr("checked"))
				{
					standard=standard+$(this).val()+",";
				}	
	});
	if(standard=="")
	{
		alert("请选择支持标准");
		return;
	}
	

	$("input[name='machineNo']").each(function ()
	{
				if($(this).attr("checked"))
				{
					machineNo=machineNo+$(this).val()+",";
				}	
	});
	if(machineNo=="")
	{
		alert("请选择测试机型");
		return;
	}
	document.getElementById("btn").disabled = true;
	search();
}	

function Validate(itemName,pattern){
			 var Require= /.+/;
			 var Email= /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
			 var Phone= /^((\(\d{2,3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/;
			var Mobile= /^((\(\d{2,3}\))|(\d{3}\-))?13\d{9}$/;
			var Url= /^http:\/\/[A-Za-z0-9]+\.[A-Za-z0-9]+[\/=\?%\-&_~`@[\]\':+!]*([^<>\"\"])*$/;
		   var IdCard = /^\d{15}(\d{2}[A-Za-z0-9])?$/;
		   var Currency = /^\d+(\.\d+)?$/;
		   var Number= /^\d+$/;
		   var Zip = /^[1-9]\d{5}$/;
		   var QQ = /^[1-9]\d{4,8}$/;
		   var Integer = /^[-\+]?\d+$/;
		   var integer = /^[+]?\d+$/;
		   var Double= /^[-\+]?\d+(\.\d+)?$/;
		   var double = /^[+]?\d+(\.\d+)?$/;
		   var English = /^([A-Za-z]|[,\!\*\.\ \(\)\[\]\{\}<>\?\\\/\'\"])+$/;
		   var Chinese = /^[\u0391-\uFFE5]+$/;
		   var BankCard = /^([0-9]|[,]|[;])+([;])+$/;
		
			//var itemNameValue=document.getElementsByName(itemName)[0].value
			var itemNameValue=itemName;
			
				var flag;
			switch(pattern){ 
			 case "Require":
				 flag = Require.test(itemNameValue);
				  break;
			 case "Email":
				 flag = Email.test(itemNameValue);
				  break;
			 case "Phone":
				 flag = Phone.test(itemNameValue);
				  break;
			 case "Mobile":
				 flag = Mobile.test(itemNameValue);
				  break;
			 case "Url":
				 flag = Url.test(itemNameValue);
				  break;
			 case "IdCard":
				 flag = IdCard.test(itemNameValue);
				  break;
			 case "Currency":
				 flag = Currency.test(itemNameValue);
				  break;
			 case "Number":
				 flag = Number.test(itemNameValue);
					  break;
			 case "Zip":
				 flag = Zip.test(itemNameValue);
				  break;
			 case "QQ":
				 flag = QQ.test(itemNameValue);
				  break;
			 case "integer":
				 flag = integer.test(itemNameValue);
				  break;		  
			 case "Integer":
			// if (itemNameValue.length>0)
				 flag = Integer.test(itemNameValue);
			//else 
			//	flag=true
				  break;
			 case "Double":
				 flag = Double.test(itemNameValue);
				  break;
			 case "double":
					 flag = double.test(itemNameValue);
					  break;
			 case "English":
				 flag = English.test(itemNameValue);
				  break;
			 case "Chinese":
				 flag = Chinese.test(itemNameValue);
				  break;
			 case "BankCard":
				 flag = BankCard.test(itemNameValue);
				  break;		  
			default :
				flag = false;
				break;
			}
		//	if (!flag){
		//	alert(msg);
		//	document.getElementsByName(itemName)[0].focus();
		//	}
		   return flag;
		
		}
		
		String.prototype.trim = function()
		{
			return this.replace(/(^\s*)|(\s*$)/g, "");
		}
		
		function blurHdl(val)
		{
			if(val!="" || val!=null)
			{
				if(!Validate(val.value, "Number"))
				{
					alert("请输入数字");
				}
			}
		}

function setExecIP(value)
{
	var sIdx = value.selectedIndex;
	var strTmp = "";
	switch(sIdx)
	{
		case 0:
			strTmp = "10.1.89.27";	
			break;	
		case 1:
			strTmp = "10.1.89.29";	
			break;	
		case 2:
			strTmp = "10.1.89.29";	
			break;	
	}
	document.getElementById("execIP").value = strTmp;
}

		function search()
		{	
			//alert("machineNo:"+machineNo);
			//alert("testType:"+testType);
			//alert("standard:"+standard);
			var url="autoTestExec.action";
			var params={dailyBuildIP:$("#dailyBuildIP").val(),execIP:$("#execIP").val(),prjName:$("#prjName").val(),standard:standard,testType:testType,machineNo:machineNo,versionNo:$("#versionNo").val(),qc_username:$("#qc_username").val(),qc_password:$("#qc_password").val()};
			$("#status").css("color","green");
			$("#status").text("正在与远程服务器进行连接...");
			$.post(url, params,function(data){
				$("#status").css("color","red");
				document.getElementById("btn").disabled = false;
				//0为正常,1为服务器忙,2为参数无效,3为登陆失败,4为客户端版本低,9为无法连接
				if(data==0){
					$("#status").text("正常");
					document.getElementById("btn").disabled = true;
				}else if(data==1){
					$("#status").text("服务器忙");
				}else if(data==2){
					$("#status").text("参数无效");
				}else if(data==3){
					$("#status").text("登陆失败");
				}else if(data==4){
					$("#status").text("客户端版本低");
				}else {
					$("#status").text("无法连接");
				}
				
			}, 'text');
		}
		
		//$.fn.callbackFun=function(json)
		 //{	
		 //alert("hdhd");
		  	//if(json!=null&&json.length>0)
			//{	
				//alert(json[0]);
			//}
		 //}
</script>
</head>

<body>
<form id="form1" name="form1" method="post" action="<%=request.getContextPath()%>/pages/auto/autoTestExec.action">
<table width="90%" border="1" cellspacing="0">
    <tr>
    	<td colspan="8" scope="col" bgcolor="#787878"><div align="left" style="font-size:18px; font-weight:bold">默认配置</div></td>
    </tr>
    <tr>
    	<td width="12%" bgcolor="#999999">DailyBuild IP:</td>
        <td width="20%"><input type="text" name="dailyBuildIP" id="dailyBuildIP" readonly value="10.1.3.195" style="border:none" /></td>
        <td width="12%" bgcolor="#999999">执行机IP:</td>
        <td width="20%"><input type="text" name="execIP" id="execIP"  value="10.1.89.27" style="border:none"/></td>
    </tr>
    <tr>
    	<td colspan="8" scope="col" bgcolor="#787878"><div align="left" style="font-size:18px; font-weight:bold">测试项</div></td>
    </tr>
    <tr>
    	<td bgcolor="#999999">项目名称</td>
        <td>
            <select onChange="setExecIP(this)" name="prjName" id="prjName" style="width:160px">
                <option selected="selected" value="CATalyst3.0R4">CATalyst3.0 R4</option>
                <option value="CATalyst3.0R5">CATalyst3.0 R5</option>

            </select>
        </td>
        <td bgcolor="#999999">版本号<font color="#FF0000">*</font></td>
        <td><input name="versionNo" id="versionNo" type="text" size="8" maxlength="3" onBlur="blurHdl(this)"/></td>
    </tr>
    <tr>
    	<td bgcolor="#999999">支持标准<font color="#FF0000">*</font></td>
        <td colspan="3"><input type="checkbox" name="standard" id="standard" value="NotWosa" checked="true"/>非WOSA
        	&nbsp;&nbsp;&nbsp;
        	<input type="checkbox" name="standard" id="standard" value="Wosa"/>WOSA
        </td>
    </tr>
    <tr>
    	<td bgcolor="#999999">测试类型<font color="#FF0000">*</font></td>
        <td colspan="3"><input type="checkbox" name="testType" id="testType" value="1"/>自检
        	&nbsp;&nbsp;&nbsp;
        	<input type="checkbox" name="testType" id="testType" value="2" checked="true"/>冒烟
            &nbsp;&nbsp;&nbsp;
            <input type="checkbox" name="testType" id="testType" value="3"/>标准
            &nbsp;&nbsp;&nbsp;
            <input type="checkbox" name="testType" id="testType" value="4"/>扩展
            &nbsp;&nbsp;&nbsp;
            <input type="checkbox" name="testType" id="testType" value="5"/>分类</td>
    </tr>
	<tr>
    	<td bgcolor="#999999">测试机型<font color="#FF0000">*</font></td>
        <td colspan="3"><input type="checkbox" name="machineNo" id="machineNo" value="H22" checked="true"/>H22
        				&nbsp;&nbsp;&nbsp;
        				<input type="checkbox" name="machineNo" id="machineNo" value="H68" checked="true"/>H68
                        &nbsp;&nbsp;&nbsp;
                        <input type="checkbox" name="machineNo" id="machineNo" value="H22N"/>H22N
                        &nbsp;&nbsp;&nbsp;
                        <input type="checkbox" name="machineNo" id="machineNo" value="H68N"/>H68N</td>
    </tr>
    <tr>
    	<td colspan="8" scope="col" bgcolor="#787878"><div align="left" style="font-size:18px; font-weight:bold">用户登录</div></td>
    </tr>
    <tr>
    	<td bgcolor="#999999">QC用户名<font color="#FF0000">*</font></td>
        <td><input name="qc_username" id="qc_username" type="text" size="25" maxlength="8" /></td>
        <td bgcolor="#999999">QC密码<font color="#FF0000">*</font></td>
        <td><input name="qc_password" id="qc_password" type="password" size="25" maxlength="20" /></td>
    </tr>
    <tr>
    	<td colspan="4" align="center">
        	<input type="button" value="确定" onClick="commit()" id="btn" style="width:100px; height:25px" />
        </td>
    </tr>
</table>
</form>

<!-- 状态显示位置-->
<div id="status"></div>

</body>
</html>
