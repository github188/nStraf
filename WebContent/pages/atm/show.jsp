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
	      
	</script>
<body id="bodyid"  leftmargin="0" topmargin="10" onLoad="init();">
	<form name="reportInfoForm" action="<%=request.getContextPath() %>/pages/atm/atminfo!save.action"   method="post">
		<input type="hidden" id="id" name="id"
				value="<s:property value="id"/>">
		<table width="80%" align="center" cellPadding="0" cellSpacing="0">
            <tr>
                <td>
                <fieldset class="jui_fieldset" width="100%">
                    <legend><s:text name="机器汇总信息" /></legend>
                    <table width="652" align="center" border="0" cellspacing="1" cellpadding="1" bgcolor="#583F70" height="153">
                    <br/>
                        <tr>
                          <td align="center" width="15%" bgcolor="#999999"><div align="center" style="width:63px">机器型号</div></td>  
                          <td bgcolor="#FFFFFF" width="15%" ><s:property value="atm.machineNo" />&nbsp;</td>
                          <td align="center" width="15%" bgcolor="#999999"><div align="center" style="width:63px">设备编号</div></td>
                          <td bgcolor="#FFFFFF" width="15%" ><s:property value="atm.deviceNo"/>&nbsp;</td>  
                        <td align="center" width="15%" bgcolor="#999999"><div align="center" style="width:63px">设备状态</div></td> 
                      <td align="center" bgcolor="#FFFFFF"><tm:dataDir beanName="atm" property ="configStatus" path="systemConfig.configStatus"/>
		 </td>
                          
		 </td>
                      </tr> 
                      <tr>
                         <td align="center" bgcolor="#999999" width="15%"><div align="center" style="width:63px">IP地址</div></td>
                          <td bgcolor="#FFFFFF" width="15%" ><s:property value="atm.netIP"/>&nbsp;</td>
                          <td align="center" width="15%" bgcolor="#999999"><div align="center" style="width:63px">借用者</div></td>  
                          <td bgcolor="#FFFFFF"><s:property value="atm.browerer"/>&nbsp;</td>
                          <td align="center" width="15%" bgcolor="#999999"><div align="center" style="width:63px">借用日期</div></td> 
                         <td bgcolor="#FFFFFF" nowrap="nowrap" width="20%"> <s:date name="atm.BrowerDate" format="yyyy-MM-dd "/>&nbsp;</td> 
                       </tr> <br/>
          
                      <tr>     
                           <td align="center" bgcolor="#999999" width="20%">借用OA流水号</td>
                          <td bgcolor="#FFFFFF"><s:property value="atm.browerOA"/>&nbsp;</td>
                          <td align="center" width="20%" bgcolor="#999999"><div align="center" style="width:63px">工控机型号</div></td>  
                          <td bgcolor="#FFFFFF"><s:property value="atm.controllerNo"/>&nbsp;</td>
                                <td align="center" width="15%" bgcolor="#999999"><div align="center" style="width:63px">归还日期</div></td>
                                         <td bgcolor="#FFFFFF"  nowrap="nowrap" width="20%"><s:date name="atm.returnDate" format="yyyy-MM-dd "/>&nbsp;</td>                           
                      </tr>
                        <tr>
                          <td align="center" bgcolor="#999999" width="20%"><div align="center" style="width:63px">内存</div></td>
                          <td bgcolor="#FFFFFF" colspan="5"><s:property value="atm.memory"/>&nbsp;</td>
                        </tr>
                         <tr>
                         <td align="center" width="20%" bgcolor="#999999"><div align="center" style="width:63px">CPU</div></td>
                         <td bgcolor="#FFFFFF" colspan="5"><s:property value="atm.cpu"/>&nbsp;</td>  
                   
                        
                          </tr>
                          <tr>
                          <td align="center" width="20%" bgcolor="#999999"><div align="center" style="width:63px">显卡</div></td>  
                          <td bgcolor="#FFFFFF" colspan="5"><s:property value="atm.vga"/>&nbsp;</td>
                          
                      </tr>
                       <tr>
                       <td align="center" width="20%" bgcolor="#999999"><div align="center" style="width:63px">网卡</div></td>
                       <td bgcolor="#FFFFFF" colspan="5"><s:property value="atm.netCard"/>&nbsp;</td> 
                         
                          </tr>
                          <tr>
                          <td align="center" bgcolor="#999999" width="20%"><div align="center" style="width:63px">声卡</div></td>
                          <td bgcolor="#FFFFFF" colspan="5"><s:property value="atm.soundCard"/>&nbsp;</td>
                         
                          </tr>
                          <tr>
                          <td align="center" width="20%" bgcolor="#999999"><div align="center" style="width:63px">操作系统</div></td>  
                          <td bgcolor="#FFFFFF" colspan="5"><s:property value="atm.os"/>&nbsp;</td>
                          
                      </tr>
                        <tr>
                           <td align="center" bgcolor="#999999" width="20%"><div align="center" style="width:63px">IE版本</div></td>
                          <td bgcolor="#FFFFFF" colspan="5"><s:property value="atm.ieVersion"/>&nbsp;</td>
                          </tr>
                          <tr>
                          <td align="center" width="20%" bgcolor="#999999"><div align="center" style="width:63px">补丁版本</div></td>
                          <td bgcolor="#FFFFFF" colspan="5"><s:property value="atm.patch"/>&nbsp;</td>  
                      </tr>
                     
                         <tr>
                          <td align="center" width="20%" bgcolor="#999999"><div align="center" style="width:63px">分区及大小</div></td>
                           <td bgcolor="#FFFFFF"  colspan="5">
                          	<s:property value="atm.space"/>
                        </tr>
                
                    </table><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td><td valign="top"><br></td>
                </fieldset>
                </td> 
            </tr> 
    </table>
<br/>
<br/>
<table width="80%" cellpadding="0" cellspacing="0" align="center"> 
		<tr>
			<td align="center"> 
				<input type="button" name="return" value='<s:text  name="button.close"/>' class="MyButton"  onclick="closeModal();" image="../../images/share/f_closed.gif"> 
			</td> 
  		</tr>  
 	</table> 
			
 	</form>
    
</body>  
</html> 