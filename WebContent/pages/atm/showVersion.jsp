<!--20100107 14:00-->
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/xmlHtc.inc"%>
<%@ include file="/pages/common/loading.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<html> 
 <head></head>
	<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
	<script type="text/javascript" src="../../js/tablesort.js"></script>
	<script type="text/javascript" src="../../js/Validator.js"></script>
	<script type="text/javascript" src="../../js/DateValidator.js"></script>
	<script language="javascript" src="../../js/aa.js"></script>
	<script language="javascript" src="../../js/jquery.js"></script>
	<script type="text/javascript">
	
	function closeModal(){
 		if(!confirm('您确认关闭此页面吗？'))
		{
			return;				
		}
	 	window.close();
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

	      function showLog(value)
		  {
		  	var aa = document.getElementById(value).value;
             var id = document.getElementById('id').value;
			 var strUrl="/pages/atm/atminfo!showLogs.action?mediaValue="+aa+"&id="+id;
          //   alert(strUrl);
             var features="800,700,transmgr.traninfo,watch";
             var resultvalue = OpenModal(strUrl,features); 
		  } 
	      
	</script>
<body id="bodyid"  leftmargin="0" topmargin="10" onLoad="init();">
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
								<s:text name="机器介质版本" />
							</legend>
							<table width="652" align="center" border="0" cellspacing="1"
								cellpadding="1" bgcolor="#583F70" height="153">
								<br />
								<tr>
									<th align="center" width="25%" bgcolor="#999999">
										部件名称
									</th>
									<th align="center" width="30%" bgcolor="#999999">
										模块型号(序列号)<font color="#FF0000">*</font>
									</th>
									<th align="center" width="30%" bgcolor="#999999">
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
										<select name="media.runningPrinter"  id="runningPrinter" align="center" style="width:70%" >
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
							
											<s:property value="media.runningPrinterVersion" />
											
										&nbsp;
									</td>
									<td bgcolor="#FFFFFF" align="center">	 
									<input type="button" value="历史详情" onClick="javascript:showLog('runningPrinter');"/>
									</td>
									
								</tr>

								<tr>
									<td align="center" width="20%" bgcolor="#999999">
										<div align="center" style="width: 63px">
											凭条打印机
										</div>
									</td>
									
									<td bgcolor="#FFFFFF" align="left">
										<select name="media.proofPrinter"  id="proofPrinter" align="center" style="width:70%" >
											<option value="b1">USB普通凭条</option>
											<option value="b2">USB大纸卷</option>
										</select>
										&nbsp;
									</td>
									<s:if test="media.proofPrinter!=null&&!media.proofPrinter.equals('')">
											<script language="javascript">
						            		$("#proofPrinter").val('<s:property value='media.proofPrinter'/>');
						            		</script>
									</s:if>
									
									<td bgcolor="#FFFFFF" align="center">
										<s:property value="media.proofPrinterVersion"/>
										&nbsp;
									</td>
									<td bgcolor="#FFFFFF" align="center">
                            <input type="button" value="历史详情" onClick="javascript:showLog('proofPrinter');"/>
									</td>
								</tr>

								<tr>
									<td align="center" width="20%" bgcolor="#999999">
										<div align="center" style="width: 63px">
											读卡器
										</div>
									</td>
									
									<td bgcolor="#FFFFFF"  align="left">
										<select name="media.readCard"  id="readCard" align="center" style="width:70%" >
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
										<s:property value="media.readCardVersion"/>
										&nbsp;
									</td>
                                    <td bgcolor="#FFFFFF" align="center">
			<input type="button" value="历史详情" onClick="javascript:showLog('readCard');"/>
									</td>
									
								</tr>

								<tr>
									<td align="center" width="20%" bgcolor="#999999">
										<div align="center" style="width: 63px">
											闸门
										</div>
									</td>
									
									<td bgcolor="#FFFFFF" align="left">
										<select name="media.door"  id="door" align="center" style="width:70%" >
											<option value="d1">WST-001</option>
											<option value="d2">WST-002</option>
											<option value="d3">H68大闸门</option>
                                            <option value="d4">H68N大闸门</option>
										</select>
										&nbsp;
									</td>
									<s:if test="media.door!=null&&!media.door.equals('')">
											<script language="javascript">
						            		$("#door").val('<s:property value='media.door'/>');
						            		</script>
									</s:if>
									
									<td bgcolor="#FFFFFF" align="center">
										<s:property value="media.doorVersion"/>
										&nbsp;
									</td>
									<td bgcolor="#FFFFFF" align="center">
			<input type="button" value="历史详情" onClick="javascript:showLog('door');"/>
									</td>
								</tr>

								<tr>
									<td align="center" width="20%" bgcolor="#999999">
										<div align="center" style="width: 63px">
											键盘
										</div>
									</td>
									
									<td bgcolor="#FFFFFF" align="left">
										<select name="media.key"  id="key" align="center" style="width:70%" >
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
										<s:property value="media.keyVersion"/>
										&nbsp;
									</td>
									<td bgcolor="#FFFFFF" align="center">
					<input type="button" value="历史详情" onClick="javascript:showLog('key');"/>
									</td>
								</tr>

								<tr>
									<td align="center" width="20%" bgcolor="#999999">
										<div align="center" style="width: 75px">
											后台维护终端
										</div>
									</td>
	
									<td bgcolor="#FFFFFF" align="center">
										<select name="media.backDoor"  id="backdoor" align="center" style="width:70%" >
											<option value="f1">USB后台</option>
											<option value="f2">手持式后台</option>
										</select>
										&nbsp;
									</td>
									<s:if test="media.backDoor!=null&&!media.backDoor.equals('')">
											<script language="javascript">
						            		$("#backdoor").val('<s:property value='media.backDoor'/>');
						            		</script>
									</s:if>
									
									<td bgcolor="#FFFFFF" align="center">
										<s:property value="media.backDoorVersion"/>
										&nbsp;
									</td>		
                                    <td bgcolor="#FFFFFF" align="center">
				<input type="button" value="历史详情" onClick="javascript:showLog('backdoor');"/>
									</td>
								</tr>

								<tr>
									<td align="center" width="20%" bgcolor="#999999">
										<div align="center" style="width: 63px">
											其他
										</div>
									</td>
									
									<td bgcolor="#FFFFFF" align="left">
										<select name="media.other"  id="other" align="center" style="width:70%" >
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
										<s:property value="media.otherVersion"/>
										&nbsp;	
									</td>
									<td bgcolor="#FFFFFF" align="center">
	                   <input type="button" value="历史详情" onClick="javascript:showLog('other');"/>
									</td>
								</tr>
                                <tr >
									<td align="center" width="20%" bgcolor="#999999">
										<div align="center" style="width: 63px">
											机芯
										</div>
									</td>
									<td bgcolor="#FFFFFF" align="center">
										<select name="media.core"  id="core" align="center" style="width:70%" >
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
									<td bgcolor="#FFFFFF" align="center">
										<s:property value="media.coreVersion"/>
										&nbsp;	
									</td>					
        
									<td bgcolor="#FFFFFF" align="center">
			<input type="button" value="历史详情" onClick="javascript:showLog('core');"/>
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
						<input type="button" name="return"
							value='<s:text  name="button.close"/>' class="MyButton"
							onclick="closeModal();" image="../../images/share/f_closed.gif">
					</td>
				</tr>
			</table>

		</form>
    
</body>  
</html> 