<!--20100107 14:00-->
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/xmlHtc.inc"%>
<%@ include file="/pages/common/loading.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<html>
	<head></head>
	<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
	<script type="text/javascript" src="../../js/tablesort.js"></script>
	<script type="text/javascript" src="../../js/Validator.js"></script>
	<script type="text/javascript" src="../../js/DateValidator.js"></script>
	<script language="javascript" src="../../js/aa.js"></script>
	<script language="javascript" src="../../js/jquery.js"></script>
	<script type="text/javascript">
		var iTaskIndex = 1;			// the number of the task tables
		var selAllFlag = true;
		var tableTaskIndex = 2;		// table in table's index
		var iTaskTotalpd = 20;		// the total number of tasks
		var oldBorderStyle, oldBgColor;
		
		function closeModal(){
	 		if(!confirm('您确认关闭此页面吗？'))
			{
				return;				
			}
		 	window.close();
		}
		function save(){
			if(!validateInputInfo1())
			{
				return ;
			}
			
			if(!validateInputInfo2())
			{
				return ;
			}
			if(document.getElementById("machineNo").value.trim()=="")
			{
				alert("请输入机器型号");
				return;
			}
			if(document.getElementById("deviceNo").value.trim()=="")
			{
				alert("请输入设备编号");
				return;
			}
			if(document.getElementById("netIP").value.trim()=="")
			{
				alert("请输入IP地址");
				return;
			}
			else if(! Validate(document.getElementById("netIP").value, "ip"))
			{
				alert("请输入正确的IP地址");
				return;
			}
			if(document.getElementById("browerer").value.trim()=="")
			{
				alert("请输入借用者的名字");
				return;
			}
			if(document.getElementById("BrowerDate").value.trim()=="")
			{
				alert("请选择借用的日期");
				return;
			}
			if(document.getElementById("space").value.length>100){
         		alert("分区及大小的长度不能超过100");
         		return false;
        	}
			window.returnValue=true;
			reportInfoForm.submit();
		}

		String.prototype.trim = function()
		{
			return this.replace(/(^\s*)|(\s*$)/g, "");
		}
		
			function Validate(itemName,pattern){
			 var Require= /.+/;
			 var Email= /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
			 var Phone= /^((\(\d{2,3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/;
			var Mobile= /^((\(\d{2,3}\))|(\d{3}\-))?13\d{9}$/;
			var Url= /^(http|ftp|svn):\/\/[A-Za-z0-9]+\.[A-Za-z0-9]+[\/=\?%\-&_~`@[\]\':+!]*([^<>\"\"])*$/;
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
		   var ip=/^(((2[0-4]\d)|(25[0-5]))|(1\d{2})|([1-9]\d)|(\d))[.](((2[0-4]\d)|(25[0-5]))|(1\d{2})|([1-9]\d)|(\d))[.](((2[0-4]\d)|(25[0-5]))|(1\d{2})|([1-9]\d)|(\d))[.](((2[0-4]\d)|(25[0-5]))|(1\d{2})|([1-9]\d)|(\d))$/;
		//var ip = /^(([1-9]|([1-9]\d)|(1\d\d)|(2([0-4]\d|5[0-5])))\.)((d|([1-9]\d)|(1\d\d)|(2([0-4]\d|5[0-5])))\.){2}([1-9]|([1-9]\d)|(1\d\d)|(2([0-4]\d|5[0-5])))$/;
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
			case "ip":
				 flag = ip.test(itemNameValue);
				  break;
			case "ShareUrl":
				flag = ShareUrl.test(itemNameValue);	  
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
		function checkHdl(val)
		{
			if(val.value.trim()=="" || val.value == null)
			{
				alert("必填项不能为空");
			}
		}

		  function init()
	      {
	      	var id=$("#id").val();
	      	if(""!=id&&null!=id)
	      	{   
	      		parent.document.getElementById("id").value=id;
	      		if(parent.showLi)
	      			parent.showLi();
	      	}
	      	return false;
	      }   
	      
	       function selDate(id)
	      {
	      	var obj=document.getElementById(id);
	      	ShowDate(obj);
	      }  

	       function check()
			{
	    	   if(document.getElementById("deviceNo").value.trim()=="")
				{
					alert("请输入设备编号");
					$("#deviceNo").focus();
					return;
				}
				var url="atminfo!check.action";
				var params={deviceNo:$("#deviceNo").val()};
				jQuery.post(url, params, $(document).callbackFun1, 'json');
			}
			$.fn.callbackFun1=function (json)
			 {	
			  	if(json!=null&&json==false)
				{	
				//	$("#deviceNo").focus();
					document.getElementById("ok").disabled = true;
					alert("设备编号与之前输入的相同，请重新输入");
					return;
				}else{
			
					document.getElementById("ok").disabled = false;
					return;
				}	
	
			 }
			
			
			function validateInputInfo1(){
				var re=/^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$/g;   	  
			    var re1=/^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$/g;   	  
			  	var thisDate = reportInfoForm.BrowerDate.value.trim();
					if(thisDate.length>0){
					var a = re1.test(thisDate);
					if(!a){
						alert('日期格式不正确,请使用日期选择!');
						return false;
						}
					 }
					  return true;
				 }		
			function validateInputInfo2(){
				var re=/^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$/g;   	  
			    var re1=/^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$/g;   	  
			  	var thisDate = reportInfoForm.returnDate.value.trim();
					if(thisDate.length>0){
					var a = re1.test(thisDate);
					if(!a){
						alert('日期格式不正确,请使用日期选择!');
						return false;
						}
					 }
					  return true;
				 }		
	</script>
	<body id="bodyid" leftmargin="0" topmargin="10" onLoad="init();">
		<form name="reportInfoForm" method="post" action="<%=request.getContextPath() %>/pages/atm/atminfo!save.action">
			<input type="hidden" id="id" name="id"
				value="<s:property value="id"/>">
			<table width="80%" align="center" cellPadding="0" cellSpacing="0">
				<tr>
					<td>
						<fieldset class="jui_fieldset" width="100%">
							<legend>
								<s:text name="机器汇总信息" />
							</legend>
							<table width="652" align="center" border="0" cellspacing="1"
								cellpadding="1" bgcolor="#583F70" height="153">
								<br />
								<tr>
									<td align="center" width="20%" bgcolor="#999999">
										<div align="center" style="width: 63px">
											机器型号
											<font color="#FF0000">*</font>
										</div>
									</td>
									<td bgcolor="#FFFFFF">
										<input name="atm.machineNo" type="text" id="machineNo"
											maxlength="20" size="20"
											value='<s:property value="atm.machineNo"/>'>
										&nbsp;
									</td>
									<td align="center" width="20%" bgcolor="#999999">
										<div align="center" style="width: 63px">
											设备编号
											<font color="#FF0000">*</font>
										</div>
									</td>
									<td bgcolor="#FFFFFF">
										<input name="atm.deviceNo" type="text" id="deviceNo"
											maxlength="20" size="20"
											value='<s:property value="atm.deviceNo"/>'
											onblur="check();">
										&nbsp;
									</td>
									<td align="center" width="20%" bgcolor="#999999">
										<div align="center" style="width: 63px">
											设备状态
											<font color="#FF0000">*</font>
										</div>
									</td>
                    
									<td bgcolor="#FFFFFF">  

										<tm:tmSelect name="atm.configStatus" id="configStatus"
											selType="dataDir" path="systemConfig.configStatus" 
											style="width:80%" />
										    &nbsp;
									</td>
                                                                       <s:if test="atm.configStatus!=null&&!atm.configStatus.equals('')">
									<script language="javascript">
						            		$("#configStatus").val('<s:property value='atm.configStatus'/>');
						            		</script>
                                                                        </s:if>
								</tr>
								<tr>
									<td align="center" bgcolor="#999999" width="20%">
										<div align="center" style="width: 63px">
											IP地址
											<font color="#FF0000">*</font>
										</div>
									</td>
									<td bgcolor="#FFFFFF">
										<input name="atm.netIP" type="text" id="netIP" maxlength="20"
											size="20" value='<s:property value="atm.netIP"/>'>
										&nbsp;
									</td>
									<td align="center" width="20%" bgcolor="#999999">
										<div align="center" style="width: 63px">
											借用者
											<font color="#FF0000">*</font>
										</div>
									</td>
									<td bgcolor="#FFFFFF">
										<input name="atm.browerer" type="text" id="browerer"
											maxlength="20" size="20"
											value='<s:property value="atm.browerer"/>'>
										&nbsp;
									</td>
									<td align="center" width="20%" bgcolor="#999999">
										<div align="center" style="width: 63px">
											借用日期
											<font color="#FF0000">*</font>
										</div>
									</td>
									<td bgcolor="#FFFFFF" nowrap="nowrap" width="20%">
										<input name="atm.BrowerDate" type="text" class="MyInput"
											id="BrowerDate" maxlength="20" isSel="true" isDate="true"
											onFocus="ShowDate(this)" dofun="ShowDate(this)" size="18"
											value='<s:date name="atm.BrowerDate" format="yyyy-MM-dd"/>'>
									</td>

								</tr>
								<br />

								<tr>
									<td align="center" bgcolor="#999999" width="20%">
										<div align="center" style="width: 63px">
											借用OA流水号
										</div>
									</td>
									<td bgcolor="#FFFFFF">
										<input name="atm.browerOA" type="text" id="browerOA"
											maxlength="20" size="20"
											value='<s:property value="atm.browerOA"/>'>
										&nbsp;
									</td>
									<td align="center" width="20%" bgcolor="#999999">
										<div align="center" style="width: 63px">
											工控机型号
										</div>
									</td>
									<td bgcolor="#FFFFFF">
										<input name="atm.controllerNo" type="text" id="controllerNo"
											maxlength="20" size="20"
											value='<s:property value="atm.controllerNo"/>'>
										&nbsp;
									</td>
									<td align="center" width="20%" bgcolor="#999999">
										<div align="center" style="width: 63px">
											归还日期
										</div>
									</td>
									<td bgcolor="#FFFFFF" nowrap="nowrap" width="20%">
										<input name="atm.returnDate" type="text" class="MyInput"
											id="returnDate" maxlength="20" isSel="true" isDate="true"
											onFocus="ShowDate(this)" dofun="ShowDate(this)" size="18"
											value='<s:date name="atm.returnDate" format="yyyy-MM-dd"/>'>
									</td>

								</tr>
								<tr>
									<td align="center" bgcolor="#999999" width="20%">
										<div align="center" style="width: 63px">
											内存
										</div>
									</td>
									<td bgcolor="#FFFFFF" colspan="5" align="center">
										<input name="atm.memory" type="text" id="memory"
											maxlength="100" size="100"
											value='<s:property value="atm.memory"/>'>
										&nbsp;
									</td>
									</tr>
									<tr>
									<td align="center" width="20%" bgcolor="#999999" >
									<div align="center" style="width: 63px">
										CPU
									</div>
								</td>
								<td bgcolor="#FFFFFF" colspan="5" align="center">
									<input name="atm.cpu" type="text" id="cpu" maxlength="100"
										size="100" value='<s:property value="atm.cpu"/>'>
									&nbsp;
								</td>
								</tr>
								<tr>
								<td align="center" width="20%" bgcolor="#999999">
								<div align="center" style="width: 63px">
									显卡
								</div>
							</td>
							<td bgcolor="#FFFFFF" colspan="5" align="center">
								<input name="atm.vga" type="text" id="vga" maxlength="100"
									size="100" value='<s:property value="atm.vga"/>'>
								&nbsp;
							</td>
							</tr>
							<tr>
							<td align="center" width="20%" bgcolor="#999999">
								<div align="center" style="width: 63px">
									网卡
								</div>
							</td>
							<td bgcolor="#FFFFFF" colspan="5" align="center">
								<input name="atm.netCard" type="text" id="netCard"
									maxlength="100" size="100"
									value='<s:property value="atm.netCard"/>'>
								&nbsp;
							</td>
								</tr>
								<tr>
									<td align="center" bgcolor="#999999" width="20%">
										<div align="center" style="width: 63px">
											声卡
										</div>
									</td>
									<td bgcolor="#FFFFFF" colspan="5" align="center">
										<input name="atm.soundCard" type="text" id="soundCard"
											maxlength="100" size="100"
											value='<s:property value="atm.soundCard"/>'>
										&nbsp;
									</td>
									</tr>
									<tr>
									<td align="center" width="20%" bgcolor="#999999">
									<div align="center" style="width: 63px">
										操作系统
									</div>
								</td>
								<td bgcolor="#FFFFFF" colspan="5" align="center">
									<input name="atm.os" type="text" id="os" maxlength="100"
										size="100" value='<s:property value="atm.os"/>'>
									&nbsp;
								</td>
							</tr>
							<tr>
							<td align="center" bgcolor="#999999" width="20%" >
							<div align="center" style="width: 63px">
								IE版本
							</div>
						</td>
						<td bgcolor="#FFFFFF" colspan="5" align="center">
							<input name="atm.ieVersion" type="text" id="ieVersion"
								maxlength="100" size="100"
								value='<s:property value="atm.ieVersion"/>'>&nbsp;
						</td>
						
				</tr>
				 <tr>
				<td align="center" width="20%" bgcolor="#999999">
				<div align="center" style="width: 63px">
					补丁版本
				</div>
			</td>
			<td bgcolor="#FFFFFF" colspan="5" align="center" >
				<input name="atm.patch" type="text" id="patch" maxlength="100"
					size="100" value='<s:property value="atm.patch"/>'>
				&nbsp;
			</td>
							</tr>
								<tr>
									<td align="center" bgcolor="#999999">
										<div align="center" style="width: 63px">
											分区及大小
										</div>
									</td>
									<td bgcolor="#FFFFFF" colspan="5" align="center">
										<input name="atm.space" type="text" id="space" size="100"
											value='<s:property value="atm.space"/>'>
										</td>&nbsp;
								</tr>
							</table>
					<td valign="top">
						<br>
					</td>
					<td valign="top">
						<br>
					</td>
					<td valign="top">
						<br>
					</td>
					<td valign="top">
						<br>
					</td>
					<td valign="top">
						<br>
					</td>
					<td valign="top">
						<br>
					</td>
					<td valign="top">
						<br>
					</td>
					<td valign="top">
						<br>
					</td>
					<td valign="top">
						<br>
					</td>
					<td valign="top">
						<br>
					</td>
					<td valign="top">
						<br>
					</td>
					<td valign="top">
						<br>
					</td>
					<td valign="top">
						<br>
					</td>
					<td valign="top">
						<br>
					</td>
					<td valign="top">
						<br>
					</td>
					<td valign="top">
						<br>
					</td>
					<td valign="top">
						<br>
					</td>
					<td valign="top">
						<br>
					</td>
					<td valign="top">
						<br>
					</td>
					<td valign="top">
						<br>
					</td>
					</fieldset>
					</td>
				</tr>
			</table>
			<br />
			<br />
			<table width="80%" cellpadding="0" cellspacing="0" align="center">
				<tr>
					<td align="center">
						<input type="button" name="ok" id="ok"
							value='<s:text  name="grpInfo.ok" />' class="MyButton"
							onclick="save()" image="../../images/share/yes1.gif">
						<input type="button" name="return"
							value='<s:text  name="button.close"/>' class="MyButton"
							onclick="closeModal();" image="../../images/share/f_closed.gif">
					</td>
				</tr>
			</table>

		</form>

	</body>
</html>
