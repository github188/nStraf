<!--20100107 14:00-->
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
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
		/* var iTaskIndex = 1;			// the number of the task tables
		var selAllFlag = true;
		var tableTaskIndex = 2;		// table in table's index
		var iTaskTotalpd = 20;		// the total number of tasks
		var oldBorderStyle, oldBgColor; */
		
		function closeModal(){
	 		if(!confirm('您确认关闭此页面吗？'))
			{
				return;				
			}
		 	window.close();
		}
		function save(){
                       
			if(document.getElementById("atmCore").value.trim()=="")
			{
				alert("请输入机芯名称");
				return;
			}
			if(document.getElementById("readCard").value.trim()=="")
			{
				alert("请输入读卡器名称");
				return;
			}
			if(document.getElementById("key").value.trim()=="")
			{
				alert("请输入键盘名称");
				return;
			}
				if(document.getElementById("runningPrinter").value.trim()=="")
			{
				alert("请输入流水打印机名称");
				return;
			}
				if(document.getElementById("proofPrinter").value.trim()=="")
			{
				alert("请输入凭条打印机名称");
				return;
			}
				if(document.getElementById("coreDoor").value.trim()=="")
			{
				alert("请输入机芯门名称");
				return;
			}
				if(document.getElementById("backdoor").value.trim()=="")
			{
				alert("请输入后台维护的名称");
				return;
			}
				if(document.getElementById("dispatcher").value.trim()=="")
			{
				alert("请输入串口分配器的名称");
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
	</script>
<body id="bodyid"  leftmargin="0" topmargin="10" onLoad="init();">
	<form name="reportInfoForm" method="post">
		<input type="hidden" id="id" name="id"
				value='<s:property value="id"/>'>
		<table width="80%" align="center" cellPadding="0" cellSpacing="0">
            <tr>
                <td>
                <fieldset class="jui_fieldset" width="100%">
                    <legend>设备配置信息</legend>
                    <table width="652" align="center" border="0" cellspacing="1" cellpadding="1" bgcolor="#583F70" height="153">
                    <br/>
                    <tr>
                    	<th align="center" width="20%" bgcolor="#999999">部件名称</th>
                        <th align="center" width="30%" bgcolor="#999999">设备模块名称<font color="#FF0000">*</font></th>
                        <th align="center" width="20%" bgcolor="#999999">通讯类型</th>
                        <th align="center" width="20%" bgcolor="#999999">PC串口</th>
                        <th align="center" width="10%" bgcolor="#999999">串口分配端口</th>
                    </tr>   
                     <tr>
                          <td align="center" width="20%" bgcolor="#999999"><div align="center" style="width:63px">机芯</div></td>  
                          <td bgcolor="#FFFFFF"><input name="config.atmCore" type="text" id="atmCore" maxlength="20" size="20" value='<s:property value="config.atmCore"/>'  style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; color:#000000" readonly>&nbsp;</td>
                           <td bgcolor="#FFFFFF"><tm:tmSelect name="config.core_trans_type" id="core_trans_type" selType="dataDir" path="systemConfig.communicateType" style="width:80%"/>
                            &nbsp;</td>
                          <s:if test="config.core_trans_type!=null&&!config.core_trans_type.equals('')">
									<script language="javascript">
						            		$("#core_trans_type").val('<s:property value='config.core_trans_type'/>');
						            		</script>
                                                                        </s:if>
                               
                            <td bgcolor="#FFFFFF"><tm:tmSelect name="config.core_pc_sata" id="core_pc_sata" selType="dataDir" path="systemConfig.pc_port" style="width:80%"/>
                            &nbsp;</td>
                                <s:if test="config.core_pc_sata!=null&&!config.core_pc_sata.equals('')">
									<script language="javascript">
						            		$("#core_pc_sata").val('<s:property value='config.core_pc_sata'/>');
						            		</script>
                                                                        </s:if>
                              
                               <td bgcolor="#FFFFFF"><input name="config.core_port" type="text" id="machineNo" maxlength="20" size="20" value='<s:property value="config.core_port"/>'  style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; color:#000000" readonly>&nbsp;</td>
                     </tr> 
                    
                     <tr>
                          <td align="center" width="20%" bgcolor="#999999"><div align="center" style="width:63px">读卡器</div></td>
                          <td bgcolor="#FFFFFF"><input name="config.readCard" type="text" id="readCard" maxlength="20" size="20" value='<s:property value="config.readCard"/>'  style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; color:#000000" readonly>&nbsp;</td>
                           <td bgcolor="#FFFFFF"><tm:tmSelect name="config.read_trans_type" id="read_trans_type" selType="dataDir" path="systemConfig.communicateType" style="width:80%"/>
                            &nbsp;</td>
                              <s:if test="config.read_trans_type!=null&&!config.read_trans_type.equals('')">
									<script language="javascript">
						            		$("#read_trans_type").val('<s:property value='config.read_trans_type'/>');
						            		</script>
                                                                        </s:if>
                               
                             <td bgcolor="#FFFFFF"><tm:tmSelect name="config.read_pc_sata" id="read_pc_sata" selType="dataDir" path="systemConfig.pc_port" style="width:80%"/>
                            &nbsp;</td>
                                <s:if test="config.read_pc_sata!=null&&!config.read_pc_sata.equals('')">
									<script language="javascript">
						            		$("#read_pc_sata").val('<s:property value='config.read_pc_sata'/>');
						            		</script>
                                                                        </s:if>
                             <td bgcolor="#FFFFFF"><input name="config.read_port" type="text" id="machineNo" maxlength="20" size="20" value='<s:property value="config.read_port"/>'  style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; color:#000000" readonly>&nbsp;</td>
                    </tr>
    
                     <tr>
                          <td align="center" width="20%" bgcolor="#999999"><div align="center" style="width:63px">键盘</div></td>  
                          <td bgcolor="#FFFFFF"><input name="config.key" type="text" id="key" maxlength="20" size="20" value='<s:property value="config.key"/>'  style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; color:#000000" readonly>&nbsp;</td>
                           <td bgcolor="#FFFFFF"><tm:tmSelect name="config.key_trans_type" id="key_trans_type" selType="dataDir" path="systemConfig.communicateType" style="width:80%"/>
                            &nbsp;</td>
                         <s:if test="config.key_trans_type!=null&&!config.key_trans_type.equals('')">
									<script language="javascript">
						            		$("#key_trans_type").val('<s:property value='config.key_trans_type'/>');
						            		</script>
                                                                        </s:if>
                             <td bgcolor="#FFFFFF"><tm:tmSelect name="config.key_pc_sata" id="key_pc_sata" selType="dataDir" path="systemConfig.pc_port" style="width:80%"/>
                            &nbsp;</td>
                                <s:if test="config.key_pc_sata!=null&&!config.key_pc_sata.equals('')">
									<script language="javascript">
						            		$("#key_pc_sata").val('<s:property value='config.key_pc_sata'/>');
						            		</script>
                                                                        </s:if>
                             <td bgcolor="#FFFFFF"><input name="config.key_port" type="text" id="machineNo" maxlength="20" size="20" value='<s:property value="config.key_port"/>'  style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; color:#000000" readonly>&nbsp;</td>
                    </tr>
                  
                     <tr>
                        <td align="center" width="20%" bgcolor="#999999"><div align="center" style="width:63px">流水打印机</div></td>
                          <td bgcolor="#FFFFFF"><input name="config.runningPrinter" type="text" id="runningPrinter" maxlength="20" size="20" value='<s:property value="config.runningPrinter"/>'  style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; color:#000000" readonly>&nbsp;</td>
                           <td bgcolor="#FFFFFF"><tm:tmSelect name="config.running_trans_type" id="running_trans_type" selType="dataDir" path="systemConfig.communicateType" style="width:80%"/>
                            &nbsp;</td>
                             <s:if test="config.running_trans_type!=null&&!config.running_trans_type.equals('')">
									<script language="javascript">
						            		$("#running_trans_type").val('<s:property value='config.running_trans_type'/>');
						            		</script>
                                                                        </s:if>
                            <td bgcolor="#FFFFFF"><tm:tmSelect name="config.running_pc_sata" id="running_pc_sata" selType="dataDir" path="systemConfig.pc_port" style="width:80%"/>
                            &nbsp;</td>
                                <s:if test="config.running_pc_sata!=null&&!config.running_pc_sata.equals('')">
									<script language="javascript">
						            		$("#running_pc_sata").val('<s:property value='config.running_pc_sata'/>');
						            		</script>
                                                                        </s:if>
                             <td bgcolor="#FFFFFF"><input name="config.running_port" type="text" id="machineNo" maxlength="20" size="20" value='<s:property value="config.running_port"/>'  style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; color:#000000" readonly>&nbsp;</td>
                    </tr>
   
                     <tr>
                        <td align="center" width="20%" bgcolor="#999999"><div align="center" style="width:63px">凭条打印机</div></td>  
                          <td bgcolor="#FFFFFF"><input name="config.proofPrinter" type="text" id="proofPrinter" maxlength="20" size="20" value='<s:property value="config.proofPrinter"/>'  style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; color:#000000" readonly>&nbsp;</td>
                           <td bgcolor="#FFFFFF"><tm:tmSelect name="config.proof_trans_type" id="proof_trans_type" selType="dataDir" path="systemConfig.communicateType" style="width:80%"/>
                            &nbsp;</td>
                              <s:if test="config.proof_trans_type!=null&&!config.proof_trans_type.equals('')">
									<script language="javascript">
						            		$("#proof_trans_type").val('<s:property value='config.proof_trans_type'/>');
						            		</script>
                                                                        </s:if>
                           <td bgcolor="#FFFFFF"><tm:tmSelect name="config.proof_pc_sata" id="proof_pc_sata" selType="dataDir" path="systemConfig.pc_port" style="width:80%"/>
                            &nbsp;</td>
                                <s:if test="config.proof_pc_sata!=null&&!config.proof_pc_sata.equals('')">
									<script language="javascript">
						            		$("#proof_pc_sata").val('<s:property value='config.proof_pc_sata'/>');
						            		</script>
                                                                        </s:if>
                             <td bgcolor="#FFFFFF"><input name="config.proof_port" type="text" id="machineNo" maxlength="20" size="20" value='<s:property value="config.proof_port"/>'  style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; color:#000000" readonly>&nbsp;</td>
                    </tr>
      
                     <tr>
                          <td align="center" width="20%" bgcolor="#999999"><div align="center" style="width:63px">机芯门</div></td>
                          <td bgcolor="#FFFFFF"><input name="config.coreDoor" type="text" id="coreDoor" maxlength="20" size="20" value='<s:property value="config.coreDoor"/>'  style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; color:#000000" readonly>&nbsp;</td>
                           <td bgcolor="#FFFFFF"><tm:tmSelect name="config.door_trans_type" id="door_trans_type" selType="dataDir" path="systemConfig.communicateType" style="width:80%"/>
                            &nbsp;</td>
                               <s:if test="config.door_trans_type!=null&&!config.door_trans_type.equals('')">
									<script language="javascript">
						            		$("#door_trans_type").val('<s:property value='config.door_trans_type'/>');
						            		</script>
                                                                        </s:if>
                           <td bgcolor="#FFFFFF"><tm:tmSelect name="config.door_pc_sata" id="door_pc_sata" selType="dataDir" path="systemConfig.pc_port" style="width:80%"/>
                            &nbsp;</td>
                                <s:if test="config.door_pc_sata!=null&&!config.door_pc_sata.equals('')">
									<script language="javascript">
						            		$("#door_pc_sata").val('<s:property value='config.door_pc_sata'/>');
						            		</script>
                                                                        </s:if>
                             <td bgcolor="#FFFFFF"><input name="config.door_port" type="text" id="machineNo" maxlength="20" size="20" value='<s:property value="config.door_port"/>'  style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; color:#000000" readonly>&nbsp;</td>
                    </tr>

                     <tr>
                          <td align="center" width="20%" bgcolor="#999999"><div align="center" style="width:63px">后台维护</div></td>
                          <td bgcolor="#FFFFFF"><input name="config.backdoor" type="text" id="backdoor" maxlength="20" size="20" value='<s:property value="config.backdoor"/>'  style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; color:#000000" readonly>&nbsp;</td>
                           <td bgcolor="#FFFFFF"><tm:tmSelect name="config.back_trans_type" id="back_trans_type" selType="dataDir" path="systemConfig.communicateType" style="width:80%"/>
                            &nbsp;</td>
                              <s:if test="config.back_trans_type!=null&&!config.back_trans_type.equals('')">
									<script language="javascript">
						            		$("#back_trans_type").val('<s:property value='config.back_trans_type'/>');
						            		</script>
                                                                        </s:if>
                             <td bgcolor="#FFFFFF"><tm:tmSelect name="config.back_pc_sata" id="back_pc_sata" selType="dataDir" path="systemConfig.pc_port" style="width:80%"/>
                            &nbsp;</td>
                                <s:if test="config.back_pc_sata!=null&&!config.back_pc_sata.equals('')">
									<script language="javascript">
						            		$("#back_pc_sata").val('<s:property value='config.back_pc_sata'/>');
						            		</script>
                                                                        </s:if>
                             <td bgcolor="#FFFFFF"><input name="config.back_port" type="text" id="machineNo" maxlength="20" size="20" value='<s:property value="config.back_port"/>'  style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; color:#000000" readonly>&nbsp;</td>
                    </tr>

                     <tr>
                        <td align="center" width="20%" bgcolor="#999999"><div align="center" style="width:63px">串口分配器</div></td>
                          <td bgcolor="#FFFFFF"><input name="config.dispatcher" type="text" id="dispatcher" maxlength="20" size="20" value='<s:property value="config.dispatcher"/>'  style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; color:#000000" readonly>&nbsp;</td>
                           <td bgcolor="#FFFFFF"><tm:tmSelect name="config.dispatcher_trans_type" id="dispatcher_trans_type" selType="dataDir" path="systemConfig.communicateType" style="width:80%"/>
                            &nbsp;</td>
                              <s:if test="config.dispatcher_trans_type!=null&&!config.dispatcher_trans_type.equals('')">
									<script language="javascript">
						            		$("#dispatcher_trans_type").val('<s:property value='config.dispatcher_trans_type'/>');
						            		</script>
                                                     </s:if> 
				  <td bgcolor="#FFFFFF"><tm:tmSelect name="config.dispatcher_pc_sata" id="dispatcher_pc_sata" selType="dataDir" path="systemConfig.pc_port" style="width:80%"/>
                            &nbsp;</td>
                                <s:if test="config.dispatcher_pc_sata!=null&&!config.dispatcher_pc_sata.equals('')">
									<script language="javascript">
						            		$("#dispatcher_pc_sata").val('<s:property value='config.dispatcher_pc_sata'/>');
						            		</script>
                                           </s:if>				
                             <td bgcolor="#FFFFFF"><input name="config.dispatcher_port" type="text" id="machineNo" maxlength="20" size="20" value='<s:property value="config.dispatcher_port"/>'  style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; color:#000000" readonly>&nbsp;</td>
                    </tr>
                     <tr>
                          <td align="center" width="20%" bgcolor="#999999"><div align="center" style="width:63px">DVR</div></td>  
                          <td bgcolor="#FFFFFF"><input name="config.dvr" type="text" id="dvr" maxlength="20" size="20" value='<s:property value="config.dvr"/>'  style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; color:#000000" readonly>&nbsp;</td>
                          <td bgcolor="#FFFFFF"><tm:tmSelect name="config.dvr_trans_type" id="dvr_trans_type" selType="dataDir" path="systemConfig.communicateType" style="width:80%"/>
                           &nbsp;</td>
                             <s:if test="config.dvr_trans_type!=null&&!config.dvr_trans_type.equals('')">
									<script language="javascript">
						            		$("#dvr_trans_type").val('<s:property value='config.dvr_trans_type'/>');
						            		</script>
                                             </s:if>
                            <td bgcolor="#FFFFFF"><tm:tmSelect name="config.dvr_pc_sata" id="dvr_pc_sata" selType="dataDir" path="systemConfig.pc_port" style="width:80%"/>
                           &nbsp;</td>
                               <s:if test="config.dvr_pc_sata!=null&&!config.dvr_pc_sata.equals('')">
									<script language="javascript">
						            		$("#dvr_pc_sata").val('<s:property value='config.dvr_pc_sata'/>');
						            		</script>
                                              </s:if>
                            <td bgcolor="#FFFFFF"><input name="config.dvr_port" type="text" id="machineNo" maxlength="20" size="20" value='<s:property value="config.dvr_port"/>'  style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; color:#000000" readonly>&nbsp;</td>
                    </tr>
                    
                     <tr>
                          <td align="center" width="20%" bgcolor="#999999"><div align="center" style="width:63px">MOXA卡</div></td>
                          <td bgcolor="#FFFFFF"><input name="config.moxaCard" type="text" id="moxaCard" maxlength="20" size="20" value='<s:property value="config.moxaCard"/>'  style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; color:#000000" readonly>&nbsp;</td>
                          <td bgcolor="#FFFFFF"><tm:tmSelect name="config.moxaCard_trans_type" id="moxaCard_trans_type" selType="dataDir" path="systemConfig.communicateType" style="width:80%"/>
                           &nbsp;</td>
                             <s:if test="config.moxaCard_trans_type!=null&&!config.moxaCard_trans_type.equals('')">
									<script language="javascript">
						            		$("#moxaCard_trans_type").val('<s:property value='config.moxaCard_trans_type'/>');
						            		</script>
                                             </s:if>
                              
                            <td bgcolor="#FFFFFF"><tm:tmSelect name="config.moxaCard_pc_sata" id="moxaCard_pc_sata" selType="dataDir" path="systemConfig.pc_port" style="width:80%"/>
                           &nbsp;</td>
                               <s:if test="config.moxaCard_pc_sata!=null&&!config.moxaCard_pc_sata.equals('')">
									<script language="javascript">
						            		$("#moxaCard_pc_sata").val('<s:property value='config.moxaCard_pc_sata'/>');
						            		</script>
                                               </s:if>
                            <td bgcolor="#FFFFFF"><input name="config.moxaCard_port" type="text" id="machineNo" maxlength="20" size="20" value='<s:property value="config.moxaCard_port"/>' style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; color:#000000" readonly>&nbsp;</td>
                    </tr>
                     <tr>
                          <td align="center" width="20%" bgcolor="#999999"><div align="center" style="width:63px">触摸屏</div></td>
                          <td bgcolor="#FFFFFF"><input readonly value='<s:property value="config.touchScreen"/>' style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; color:#000000">&nbsp;</td>
                          <td bgcolor="#FFFFFF"><tm:tmSelect name="config.touch_trans_type" id="touch_trans_type" selType="dataDir" path="systemConfig.communicateType" style="width:80%"/>
                           &nbsp;</td>
                             <s:if test="config.touch_trans_type!=null&&!config.touch_trans_type.equals('')">
									<script language="javascript">
						            		$("#touch_trans_type").val('<s:property value='config.touch_trans_type'/>');
						            		</script>
                                             </s:if>
                            <td bgcolor="#FFFFFF"><tm:tmSelect name="config.touch_pc_sata" id="touch_pc_sata" selType="dataDir" path="systemConfig.pc_port" style="width:80%"/>
                           &nbsp;</td>
                               <s:if test="config.touch_pc_sata!=null&&!config.touch_pc_sata.equals('')">
									<script language="javascript">
						            		$("#touch_pc_sata").val('<s:property value='config.touch_pc_sata'/>');
						            		</script>
                                  </s:if>
                            <td bgcolor="#FFFFFF"><input name="config.touch_port" readonly type="text" d="machineNo" maxlength="20" size="20" value='<s:property value="config.touch_port"/>' style="background-color:#FFFFFF; border-right-style:none; border-bottom-style:none; border-left-style:none; border-top-style:none; color:#000000">&nbsp;</td>
                     </tr>
                         
                    </table><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td>
               </td>
                </fieldset>  
            </tr> 
    </table>

<br/>
<br/>
<table width="80%" cellpadding="0" cellspacing="0" align="center"> 
		<tr>
			<td align="center"> <input type="button" name="return" value='<s:text  name="button.close"/>' class="MyButton"  onclick="closeModal();" image="../../images/share/f_closed.gif"> 
			</td> 
  		</tr>  
 	</table> 
			
 	</form>
    
</body>  
</html> 