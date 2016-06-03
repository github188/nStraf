<!--20100107 14:00-->
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/dialog.inc"%>
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

			if(document.getElementById("runningPrinterVersion").value.trim()=="")
			{
				alert("请输入流水打印机介质的版本");
				return;
			}
			if(document.getElementById("proofPrinterVersion").value.trim()=="")
			{
				alert("请输入凭条打印机介质的版本");
				return;
			}
				if(document.getElementById("readCardVersion").value.trim()=="")
			{
				alert("请输入读卡器介质的版本");
				return;
			}
				if(document.getElementById("doorVersion").value.trim()=="")
			{
				alert("请输入机芯门介质的版本");
				return;
			}
				if(document.getElementById("keyVersion").value.trim()=="")
			{
				alert("请输入键盘介质的版本");
				return;
			}
				if(document.getElementById("backDoorVersion").value.trim()=="")
			{
				alert("请输入后台维护介质的版本");
				return;
			}
				if(document.getElementById("coreVersion").value.trim()=="")
			{
				alert("请输入机芯介质的版本");
				return;
			}
				if(document.getElementById("otherVersion").value.trim()=="")
			{
				alert("请输入其他介质的版本");
				return;
			}
			
			window.returnValue=true;
			reportInfoForm.submit();
		}
		
		String.prototype.trim = function()
		{
			return this.replace(/(^\s*)|(\s*$)/g, "");
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
		  
		    function showLog(value,value2)
		  {
	
		  	var aa = document.getElementById(value).value;
		  	 var id=document.getElementById("id").value;
			 var strUrl="/pages/atm/atminfo!showLogs1.action?mediaValue="+aa+"&id="+id;
		  	 //alert(strUrl);
             var features="800,700,transmgr.traninfo,watch";
             var resultvalue = OpenModal(strUrl,features); 
			if(resultvalue!=null){
				
				resultvalue=resultvalue.replace(/<BR>/g,"\r\n");
				document.getElementById(value2).value=resultvalue;

				//	$("#clientmgrId").val(returnValue[0]);
			}else{
				
			}
			
		  } 
		  
	</script>
	<body id="bodyid" leftmargin="0" topmargin="10" onLoad="init();">
		<form name="reportInfoForm"
			action="<%=request.getContextPath()%>/pages/atm/atminfo!saveVersion.action"
			method="post">
			<input type="hidden" id="id" name="id"
				value="<s:property value="id"/>">
			<table width="80%" align="center" cellPadding="0" cellSpacing="0">
				<tr>
					<td>
						<fieldset class="jui_fieldset" width="100%">
							<legend>
							机器介质版本
							</legend>
							<table width="652" align="center" border="0" cellspacing="1"
								cellpadding="1" bgcolor="#583F70" height="153">
								<br />
								<tr>
									<th align="center" width="20%" bgcolor="#999999">
										部件名称
									</th>
									<th align="center" width="30%" bgcolor="#999999">
										模块型号(序列号)<font color="#FF0000">*</font>
									</th>
									<th align="center" width="35%" bgcolor="#999999">
										版本<font color="#FF0000">*</font>
									</th>
									<th align="center" width="15%" bgcolor="#999999">
										升级历史
									</th>
								</tr>

								<tr>
									<td align="center" width="20%" bgcolor="#999999">
										<div align="center" style="width: 63px">
											流水打印机
										</div>
									</td>
									<td bgcolor="#FFFFFF" align="left">
										<select name="media.runningPrinter"  id="runningPrinter" align="center" style="width:70%">
											<option value="a1">DJP-617</option>
											<option value="a2">DJP-330</option>
										</select>
										&nbsp;
									</td>
									<s:if test="media.runningPrinter!=null&&!media.runningPrinter.equals('')">
											<script language="javascript">
						            		$("#runningPrinter").val('<s:property value='media.runningPrinter'/>');
						            		</script>
									</s:if>
									<td bgcolor="#FFFFFF" align="center">
										<input name="media.runningPrinterVersion" type="text"
											id="runningPrinterVersion" maxlength="20" size="20"
											value='<s:property value="media.runningPrinterVersion" />'>
											
										&nbsp;
									</td>
                                    <td bgcolor="#FFFFFF" align="center">	                                         				    <input type="button" value="历史详情" onClick="javascript:showLog('runningPrinter','runningPrinterVersion');"/>
									</td>
								</tr>

								<tr>
									<td align="center" width="20%" bgcolor="#999999">
										<div align="center" style="width: 63px">
											凭条打印机
										</div>
									</td>
									
									<td bgcolor="#FFFFFF" align="left">
										<select name="media.proofPrinter"  id="proofPrinter" align="center" style="width:70%">
											<option value="b1">USB普通凭条</option>
											<option value="b2">USB大纸卷</option>
											<option value="b2">串口凭条</option>
										</select>
										&nbsp;
									</td>
									<s:if test="media.proofPrinter!=null&&!media.proofPrinter.equals('')">
											<script language="javascript">
						            		$("#proofPrinter").val('<s:property value='media.proofPrinter'/>');
						            		</script>
									</s:if>
									
									<td bgcolor="#FFFFFF" align="center">
										<input name="media.proofPrinterVersion" type="text"
											id="proofPrinterVersion" maxlength="20" size="20"
											value='<s:property value="media.proofPrinterVersion"/>'>
										&nbsp;
									</td>
										<td bgcolor="#FFFFFF" align="center">
<input type="button" value="历史详情" onClick="javascript:showLog('proofPrinter','proofPrinterVersion');"/>
									</td>
								</tr>

								<tr>
									<td align="center" width="20%" bgcolor="#999999">
										<div align="center" style="width: 63px">
											读卡器
										</div>
									</td>
									
									<td bgcolor="#FFFFFF"  align="left">
										<select name="media.readCard"  id="readCard" align="center" style="width:70%">
											<option value="c1">Sankyo</option>
											<option value="c2">Omron</option>
										</select>
										&nbsp;
									</td>
									<s:if test="media.readCard!=null&&!media.readCard.equals('')">
											<script language="javascript">
						            		$("#readCard").val('<s:property value='media.readCard'/>');
						            		</script>
									</s:if>
									
									<td bgcolor="#FFFFFF" align="center">
										<input name="media.readCardVersion" type="text"
											id="readCardVersion" maxlength="20" size="20"
											value='<s:property value="media.readCardVersion"/>'>
										&nbsp;
									</td>
									   <td bgcolor="#FFFFFF" align="center">
			<input type="button" value="历史详情" onClick="javascript:showLog('readCard','readCardVersion');"/>
									</td>
								</tr>

								<tr>
									<td align="center" width="20%" bgcolor="#999999">
										<div align="center" style="width: 63px">
											闸门
										</div>
									</td>
									
									<td bgcolor="#FFFFFF" align="left">
										<select name="media.door"  id="door" align="center" style="width:70%">
											<option value="d1">WST-001</option>
											<option value="d2">WST-002</option>
											<option value="d3">存取款闸门</option>
                                            <option value="d4">摇摆式闸门</option>
                                            <option value="d1">无</option>
										</select>
										&nbsp;
									</td>
									<s:if test="media.door!=null&&!media.door.equals('')">
											<script language="javascript">
						            		$("#door").val('<s:property value='media.door'/>');
						            		</script>
									</s:if>
									
									<td bgcolor="#FFFFFF" align="center">
										<input name="media.doorVersion" type="text"
											id="doorVersion" maxlength="20" size="20"
											value='<s:property value="media.doorVersion"/>'>
										&nbsp;
									</td>
										<td bgcolor="#FFFFFF" align="center">
			<input type="button" value="历史详情" onClick="javascript:showLog('door','doorVersion');"/>
									</td>
								</tr>

								<tr>
									<td align="center" width="20%" bgcolor="#999999">
										<div align="center" style="width: 63px">
											键盘
										</div>
									</td>
									
									<td bgcolor="#FFFFFF" align="left">
										<select name="media.key"  id="key" align="center" style="width:70%">
											<option value="e1">EPP</option>
											<option value="e2">EKP</option>
										</select>
										&nbsp;
									</td>
									<s:if test="media.key!=null&&!media.key.equals('')">
											<script language="javascript">
						            		$("#key").val('<s:property value='media.key'/>');
						            		</script>
									</s:if>
									
									<td bgcolor="#FFFFFF" align="center">
										<input name="media.keyVersion" type="text" id="keyVersion"
											maxlength="20" size="20"
											value='<s:property value="media.keyVersion"/>'>
										&nbsp;
									</td>
									<td bgcolor="#FFFFFF" align="center">
					<input type="button" value="历史详情" onClick="javascript:showLog('key','keyVersion');"/>
									</td>
								</tr>

								<tr>
									<td align="center" width="20%" bgcolor="#999999">
										<div align="center" style="width: 75px">
											后台维护终端
										</div>
									</td>
	
									<td bgcolor="#FFFFFF" align="center">
										<select name="media.backDoor"  id="backdoor" align="center" style="width:70%">
											<option value="f1">USB后台</option>
											<option value="f2">手持式后台</option>
											<option value="f2">可视化维护</option>
											<option value="f2">无</option>
											<option value="f2">前置维护</option>
										</select>
										&nbsp;
									</td>
									<s:if test="media.backDoor!=null&&!media.backDoor.equals('')">
											<script language="javascript">
						            		$("#backdoor").val('<s:property value='media.backDoor'/>');
						            		</script>
									</s:if>
									
									<td bgcolor="#FFFFFF" align="center">
										<input name="media.backDoorVersion" type="text"
											id="backDoorVersion" maxlength="20" size="20"
											value='<s:property value="media.backDoorVersion"/>'>
										&nbsp;
									</td>	
                                      <td bgcolor="#FFFFFF" align="center">
				<input type="button" value="历史详情" onClick="javascript:showLog('backdoor','backDoorVersion');"/>
									</td>	
								</tr>

								<tr>
									<td align="center" width="20%" bgcolor="#999999">
										<div align="center" style="width: 63px">
											其他
										</div>
									</td>
									
									<td bgcolor="#FFFFFF" align="left">
										<select name="media.other"  id="other" align="center" style="width:70%">
											<option value="g1">串口分配器</option>
											<option value="g2">I/O扩展板</option>
											<option value="g3">8路串口板</option>
										</select>
										&nbsp;
									</td>
									<s:if test="media.other!=null&&!media.other.equals('')">
											<script language="javascript">
						            		$("#other").val('<s:property value='media.other'/>');
						            		</script>
									</s:if>
									
									<td bgcolor="#FFFFFF" align="center">
										<input name="media.otherVersion" type="text"
											id="otherVersion" maxlength="20" size="20"
											value='<s:property value="media.otherVersion"/>'>
										&nbsp;	
									</td>
									<td bgcolor="#FFFFFF" align="center">
	<input type="button" value="历史详情" onClick="javascript:showLog('other','otherVersion');"/>
									</td>
								</tr>
                                
                                <tr >
									<td align="center" width="20%" bgcolor="#999999">
										<div align="center" style="width: 63px">
											机芯
										</div>
									</td>
									<td bgcolor="#FFFFFF" align="center">
										<select name="media.core"  id="core" align="center" style="width:70%">
											<option value="h1">NMD100</option>
											<option value="h2">8240</option>
											<option value="h3">9250</option>
											<option value="h4">HCM</option>
										</select>
										&nbsp;
									</td>
									<s:if test="media.core!=null&&!media.core.equals('')">
											<script language="javascript">
						            		$("#core").val('<s:property value='media.core'/>');
						            		</script>
									</s:if>
									
									<td bgcolor="#FFFFFF" rowspan="5" align="center">
						<textarea name="media.coreVersion" id="coreVersion" rows="10" style="width:100%"><s:property value="media.coreVersion"/></textarea>
										
									</td>
										<td bgcolor="#FFFFFF" align="center">
			<input type="button" value="历史详情" onClick="javascript:showLog('core','coreVersion');"/>
									</td>
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
						<input type="button" name="ok"
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
