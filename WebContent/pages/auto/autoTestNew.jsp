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
	var g_pro = {};
	var testType="";
	var standard="";
	var machineNo="";
function commit()
{	
	testType="";
	machineNo="";
	standard="";
	var szVer = $.trim(document.getElementById("versionNo").value);
	var szPatchVer = $.trim(document.getElementById("patchVersionNo").value);
	document.getElementById("versionNo").value = szVer;
	document.getElementById("patchVersionNo").value = szPatchVer;
	if(!blurHdl(szVer))
	{
		alert("版本号格式为“大版本号-小版本号”或“版本号”");
		return;
	}
	var patchVal = $("#patchOrNot").val();
	if(patchVal=="是")
	{
		if(!blurHdl(szPatchVer))
		{
			alert("版本号格式为“大版本号-小版本号”或“版本号”");
			return;
		}
	}
	document.getElementById("btn").disabled = true;
	search1();
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
		   var autoVersion = /^\d+(-\d+)?$/;
		
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
			 case "autoVersion":
			 	 flag = autoVersion.test(itemNameValue);
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
			if(val=="")
			{
				return false;
			}
			else if(val!="" || val!=null)
			{
				if(!Validate(val, "autoVersion"))
				{
					return false;
				}
				else
				{
					return true;
				}
			}
		}

function setExecIP(value)
{
	var sIdx = value.selectedIndex;
	var strIP = g_pro[sIdx][1];
	var strPort = g_pro[sIdx][2];
	var strFileFormat = g_pro[sIdx][3];
	if($.trim($("#versionNo").val())!="")
	{
		strFileFormat = strFileFormat.replace(/%s/, $.trim($("#versionNo").val()) );
	}
	$("#execIP").val(strIP);
	$("#execPort").val(strPort);
	$("#fileFormat").val(strFileFormat);
}

function setPatchOrNot(value)
{
	var patchVal = $("#patchOrNot").val();
	if(patchVal=="否")
	{
		$("#patchVersionNo").hide();
	}
	else
	{
		$("#patchVersionNo").show();
	}
}

		function search1()
		{	
			var strFileFormat = $.trim($("#fileFormat").val());
			if($.trim($("#versionNo").val())!="")
			{
				strFileFormat = strFileFormat.replace(/%s/, $.trim($("#versionNo").val()) );
			}
			//判断是否为补丁包
			var patchVal = $("#patchOrNot").val();
			if(patchVal=="是")
			{
				var patchVersionNo = $("#patchVersionNo").val();
				var pos = strFileFormat.lastIndexOf(".exe");
				if(pos!=-1)
				{
					strFileFormat = strFileFormat.substr(0, pos);
				}				
				strFileFormat = strFileFormat + "@HotFix_b" + patchVersionNo;
				//strFileFormat = strFileFormat + ".exe";
			}
			var url="autoTestExec!execTest.action";
			var params={"execIP":$.trim($("#execIP").val()),"execPort":$.trim($("#execPort").val()),"version":strFileFormat,
						"proName":$.trim($("#prjName").val())};
			$("#status").css("color","green");
			$("#status").text("正在与远程服务器进行连接...");
			$.ajax({
				type:'POST',
				url:url,
				data:params,
				timeout: 3000,
				dataType:'text',
				success:function(data)
				{
					document.getElementById("btn").disabled = false;
					$("#status").css("color","blue");
					if(data==0){
						$("#status").css("color","green");
						$("#status").text("正在执行");
						document.getElementById("btn").disabled = false;
					}else if(data==1){
						$("#status").text("服务器忙");
						var ians = confirm("是否进行排队等待?");
						if(ians)
						{
							$.ajax({
								type:'POST',
								url: "autoTestExec!execTestQueue.action",
								data:params,
								dataType:'text',
								success:function(data)
								{
									if(data==1)
									{
										$("#status").css("color","green");
										$("#status").text("排队成功");
									}
									else
									{
										$("#status").css("color","red");
										$("#status").text("排队失败");
									}
								},
								error:function(data)
								{
									$("#status").css("color","red");
									$("#status").text("排队失败");
								}
							});
							$("#status").text("排队等待");
						}
					}else if(data==2){
						$("#status").text("参数无效");
					}else if(data==3){
						$("#status").text("登陆失败");
					}else if(data==4){
						$("#status").text("服务器端错误");
					}else if(data==9){
						$("#status").css("color","red");
						$("#status").text("无法连接");
					}else if(data==10){
						$("#status").text("客户端错误");
					}
				},
				error:function(data)
				{
					$("#status").css("color","red");
					$("#status").text("连接失败");
					document.getElementById("btn").disabled = false;
				}
			});
		}
		
		function search()
		{
			var url="autoTestExec!queryPro.action";
			var params={};
			jQuery.post(url, params, $(document).callbackFun, 'json');
		}
		$.fn.callbackFun=function (json)
		 {	
		 	var i=0;
		  	$("#prjName option").remove();
		  	if(json!=null&&json.rows.length>0)
			{	
				for(i=0;i<json.rows.length;i++)
				{
					if(json.rows[i].pn.trim()!="")
					{
						g_pro[i] = [json.rows[i].pn.trim(), json.rows[i].ip.trim(), json.rows[i].port.trim(), json.rows[i].fileformat.trim()];
						$("#prjName").append("<option value='"+json.rows[i].pn+"'>"+json.rows[i].pn+"</option>");
					}
				 }
			}
			var strIP = g_pro[i-1][1];
			var strPort = g_pro[i-1][2];
			$("#execIP").val(strIP);
			$("#execPort").val(strPort);
			$("#fileFormat").val(g_pro[i-1][3]);
		 }
</script>
</head>

<body>
<form id="form1" name="form1" method="post" action="<%=request.getContextPath()%>/pages/auto/autoTestExec.action">
<table width="90%" border="1" cellspacing="0">
    <tr>
    	<td colspan="8" scope="col" bgcolor="#787878"><div align="left" style="font-size:18px; font-weight:bold">连接配置</div></td>
    </tr>
    <tr>
    	<td width="12%" bgcolor="#999999">执行机IP:</td>
        <td width="20%"><input type="text" name="execIP" id="execIP" readonly value="10.1.3.195" style="border:none" /></td>
        <td width="12%" bgcolor="#999999">端口:</td>
        <td width="20%"><input type="text" name="execPort" id="execPort"  value="10.1.89.27" style="border:none"/></td>
    </tr>
    <tr>
    	<td colspan="8" scope="col" bgcolor="#787878"><div align="left" style="font-size:18px; font-weight:bold">测试项</div></td>
    </tr>
    <tr>
    	<td bgcolor="#999999">项目名称</td>
        <td>
            <select onChange="setExecIP(this)" name="prjName" id="prjName" style="width:160px">
            </select>
        </td>
        <td bgcolor="#999999">完整包版本号<font color="#FF0000">*</font></td>
        <td><input name="versionNo" id="versionNo" type="text" size="8" maxlength="5"/></td>
    </tr>
    <tr>
    	<td bgcolor="#999999">是否补丁包</td>
        <td>
            <select onChange="setPatchOrNot(this)" name="patchOrNot" id="patchOrNot" style="width:160px">
            	<option>是</option>
                <option selected>否</option>
            </select>
        </td>
        <td bgcolor="#999999">补丁包版本号<font color="#FF0000">*</font></td>
        <td><input name="patchVersionNo" id="patchVersionNo" type="text" size="8" maxlength="5"/></td>
    </tr>
    <tr>
    	<td colspan="4" align="center">
        	<input type="button" value="确定" onClick="commit()" id="btn" style="width:100px; height:25px" />
        </td>
    </tr>
</table>
<input name="fileFormat" id="fileFormat" type="hidden" value="" />
</form>

<!-- 状态显示位置-->
<div id="status"></div>
<script type="text/javascript">
search();
var patchVal = $("#patchOrNot").val();
if(patchVal=="否")
{
	$("#patchVersionNo").hide();
}
</script>
<div>
使用说明：<br/>
1.对于SP的自动化测试，完整包版本号的输入格式为“大版本号-小版本号”，比如“12-63”；<br/>
2.如果要测试最新版本，完整包版本号的输入格式为“大版本号”，比如“12”；<br/>
3.补丁包版本的输入格式同完整包版本号。
</div>
</body>
</html>
