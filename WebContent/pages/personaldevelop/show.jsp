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
	<form name="reportInfoForm" action="<%=request.getContextPath() %>/pages/personaldevelop/personaldevelopinfo!save.action"   method="post">
		<input type="hidden" id="id" name="id"
				value='<s:property value="id"/>'>
		<table width="80%" align="center" cellPadding="0" cellSpacing="0">

				<tr>
                                                    
					<td>
    <fieldset class="jui_fieldset" width="100%">
                        
							<legend>
								<s:text name="能力状况分析" />
							</legend>
							<table width="570" align="center" border="0" cellspacing="1"
								cellpadding="1" bgcolor="#583F70" height="153">
						    <br />
                  <tr>
                <td align="center" width="15%" bgcolor="#999999"><div align="center"> 年份</div></td>
                <td  bgcolor="#FFFFFF" ><input name="abilityanalyse.createyear" type="text" size="15" maxlength="40"
											id="createyear" 
											 value='<s:property value="abilityanalyse.createyear"/>' readonly="readonly">                </td>
                <td width="15%" align="center" bgcolor="#999999"><div align="center"> 考核人 </div></td>
                <td   bgcolor="#FFFFFF"><input name="abilityanalyse.name" type="text"  id="name" value='<s:property value="abilityanalyse.name"/>' size="15"  maxlength="20" readonly="readonly">                </td>
                <td width="15%" align="center" bgcolor="#999999"><div align="center"> 组别 </div></td>
                <td   bgcolor="#FFFFFF"><input name="abilityanalyse.groupname" type="text" id="groupname" value='<s:property value="abilityanalyse.groupname"/>' size="16"  maxlength="20" readonly="readonly">    </td>
              </tr>
								<tr>
									<td align="center" width="20%" bgcolor="#999999">
						  <div align="center" style="width: 100px">
											自我优势分析
										</div>
									</td>
							  <td colspan="5" width="80%" bgcolor="#FFFFFF" >
                       	<textarea cols="69" rows="11" name="abilityanalyse.analyse"
												id="analyse" readonly><s:property value="abilityanalyse.analyse"/></textarea>
									</td>
							  </tr>
					           <tr>
                                <td align="center" width="15%" bgcolor="#999999"><div align="center"> 上级审核状态<font color="#FF0000">*</font></div></td>
                <td  bgcolor="#FFFFFF" ><input name="abilityanalyse.headmanupditing" type="text" size="15" maxlength="40"
											id="headmanupditing" value='<s:property value="abilityanalyse.headmanupditing"/>' readonly="readonly">
                                            </td>
                              <td align="center" width="15%" bgcolor="#999999"><div align="center"> 上上级审核状态<font color="#FF0000">*</font></div></td>
                <td  bgcolor="#FFFFFF" ><input name="abilityanalyse.manageupauditing" type="text" size="15" maxlength="40"
											id="manageupauditing" value='<s:property value="abilityanalyse.manageupauditing"/>' readonly="readonly"></td>
					    
                             <td align="center" width="15%" bgcolor="#999999"><div align="center"> 总状态<font color="#FF0000">*</font></div></td>
                <td  bgcolor="#FFFFFF" ><input name="abilityanalyse.status" type="text" size="16" maxlength="40" 
											id="status" value='<s:property value="abilityanalyse.status"/>' readonly="readonly"></td>
                              </tr>
							</table>
                            

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
