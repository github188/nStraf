<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="cn.grgbanking.feeltm.dayLog.domain.*" %>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../js/Validator.js"></script>
<script type="text/javascript" src="../../js/DateValidator.js"></script>
<script type="text/javascript" src="../../js/jquery-1.4.2.js"></script>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/xmlHtc.inc"%>
<%@ include file="/pages/common/loading.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<html> 
	<head></head>
	<script type="text/javascript">
		
		/* function closeModal(){
			if(!confirm('您确认关闭此页面吗？'))
			{
				return;				
			}
		 	window.close();
		} */
		
		
		
		function save() {
			if($("#myVote").val().length>500){
				alert("审核内容不能超过500个字!");
				return;
			}
			window.returnValue=true;
			reportInfoForm.submit();
		}

		$(function(){
			//计算总工时
			calculateSumShow();
			
		});
		
	
		//计算总工时
		function calculateSumShow(){
			var sum=0;
			$("div[name='subtotal']").find("input").each(function(){
				sum+=$(this).val()*1;
			});
			$("#sumShow").html(sum);
		}
		
	
	</script>
<body id="bodyid"  leftmargin="0" topmargin="10" >
<form name="reportInfoForm" action="<%=request.getContextPath() %>/pages/daylog/logInfo!audit.action" method="post">
<input name="firstDaylogId" type="hidden" value="<s:property value='#request.firstDaylogId'/>">
<input name="logDate" type="hidden" value="<s:property value='#request.logDate'/>">

<table width="100%" align="center" cellPadding="1" cellSpacing="1" class="popnewdialog1" bgcolor="#FFFFFF">
<tr>
<td bgcolor="#FFFFFF">
        <table width="100%" class="input_table">
        	<tr>
				<td class="input_tablehead"><a style="font-weight:bold;">工作日志</a></td>
        	</tr>
        	<tr>
        		<td align="left" width="10%" nowrap="nowrap"><b><div align="left" style="padding-top:4">姓名：<s:property value='#request.userNameStr'/></div></b></td> 
            	<td align="left" width="3%" nowrap="nowrap"></td> 
        	 	<td align="left" width="10%" nowrap="nowrap"><b><div align="left" style="padding-top:4">部门：<s:property value='#request.deptNameStr'/></div></b></td> 
        	 	<td align="left" width="3%" nowrap="nowrap"></td> 
            	<td align="right" nowrap="nowrap"></td>  
            	<td align="right" width="10%" nowrap="nowrap"><b><div align="right'" style="padding-top:4">日期：</div></td>  
                <td align="left" width="20%"><div align="left">
                	<s:property value='#request.logDate'/>
                </div></b></td>
            	<td align="right" width="10%" nowrap="nowrap">
	            	<div align="right">工时总计 <font color="#FF0000"><span id="sumShow">0</span></font> 小时</div>
                </td>
            </tr>
        </table>
        <s:iterator value="daylogList" id="daylog" status="row">
		<table name='taskTable' width="100%" align="center" border="0" cellspacing="1" cellpadding="1"  bgcolor="#583F70">
        	<tr>
				<td bgcolor="#FFFFFF" align="center"><s:property value='#row.index+1'/></td>
                <td bgcolor="#FFFFFF" width="97%">
        		<table width="100%" border="0" align="center" cellspacing="1" cellpadding="1" bgcolor="#583F70">
   		    		<tr >
                          <td colspan="2" class="column_label"><div align="center"><font color="#000000">项目名称</font></div></td>
                          <td class="column_label" width="12%"><div align="center"><font color="#000000">类别</font></div></td>
                          <td class="column_label" width="12%"><div align="center"><font color="#000000">状态</font></div></td>
                          <td class="column_label" width="12%"><div align="center"><font color="#000000">完成%</font></div></td>
                          <td class="column_label" width="12%"><div align="center"><font color="#000000">计划/新增</font></div></td>
                          <td class="column_label" width="12%"><div align="center"><font color="#000000">工时</font></div></td>
                    </tr>
                    <tr>
                       	<td colspan="2" nowrap bgcolor="#FFFFFF">
                       		<div align="left">
                       			<input type="text" style="border-color:transparent;" class="input_readonly" readonly="readonly"  size="43" maxlength="43" value="<s:property value='prjName'/>">
                       		</div>
                       	</td>
				        <td nowrap bgcolor="#FFFFFF">
				        	<div align="left">
				        		<input type="text" style="border-color:transparent;" class="input_readonly" readonly="readonly"  size="10" maxlength="10" value="<s:property value='type'/>">
				        	</div>
                        </td>
                        <td nowrap bgcolor="#FFFFFF">
                        	<div align="left">
                        		<input type="text" style="border-color:transparent;" class="input_readonly" readonly="readonly"  size="10" maxlength="10" value="<s:property value='statu'/>">
                            </div>
                        </td>
                        <td nowrap bgcolor="#FFFFFF">
                        	<div align="left">
                        		<input type="text" style="border-color:transparent;" class="input_readonly" readonly="readonly"  size="10" maxlength="10" value="<s:property value='finishRate'/>">
                        	</div>
                        </td>
                        <td nowrap bgcolor="#FFFFFF">
                        	<div align="left">
                        		<input type="text" style="border-color:transparent;" class="input_readonly" readonly="readonly"  size="10" maxlength="10" value="<s:property value='planOrAdd'/>">
                        	</div>
                        </td>
                        <td nowrap bgcolor="#FFFFFF" align="center">
                        	<div align="left" name="subtotal"> 
                        		<input type="text" style="border-color:transparent;" class="input_readonly"  readonly="readonly"  size="10" maxlength="10" value="<s:property value='subTotal'/>">
                        	</div>
                        </td>
                  </tr>
                  <tr>
                      <td class="input_label2"><div align="center"><font color="#000000">任务描述</font></div></td>
                      <td colspan="6" nowrap bgcolor="#FFFFFF" >
                      	<div align="left">
                      		<textarea style="border-color:transparent;text-align: left;"  readonly="readonly" type="text"  rows="3" cols="62" ><s:property value='desc'/></textarea>
                      	</div>
                      </td>
                  </tr>
                  <tr>
                  	<td class="input_label2"><div align="center" style="width:80px;"><font color="#000000">补充说明</font></div></td>
                      <td colspan="6" nowrap bgcolor="#FFFFFF" >
                      		<div align="left">
                      			<textarea type="text" style="border-color:transparent;"  readonly="readonly"  rows="3" cols="62" ><s:property value='reason'/></textarea>
                      		</div>
                      </td>
                  </tr>
              </table>
              </td>
              </tr>
			</table>
		</s:iterator>
	<br>
	<fieldset class="jui_fieldset" width="100%">
		<legend><a style="font-weight:bold;">审核</a></legend>
		<br>
		<!-- 审核-->
		<table width="100%" align="center" border="0" cellspacing="1" cellpadding="1"  bgcolor="#583F70">
	          <tr>
	           	<td nowrap bgcolor="#A5A5A5" width="10%"><div align="center">审核结果</div></td>
	           	<td nowrap bgcolor="#FFFFFF" >
	              	<select name="dateDayLog.auditStatus" id="result" style="width:90%;"">
			    		<option value="审核通过">审核通过</option>
			    		<option value="审核不通过">审核不通过</option>
			    	</select>
	          	</td>
	          </tr>
	          <tr>
	           	<td nowrap bgcolor="#A5A5A5" width="10%"><div align="center">审核意见</div></td>
	           	<td nowrap bgcolor="#FFFFFF" >
	              	<div align="left">
	              		<textarea type="text"  rows="3" cols="80" id="myVote" name='dateDayLog.auditContent'></textarea>
	              	</div>
	          		</td>
	          </tr>
	          <tr>
				<td width="10%" align="center" bgcolor="#A5A5A5"><fontcolor="#000000">审核记录</font></td>
				<td colspan="5" bgcolor="#ffffff">
					<textarea cols="80" rows="8" readonly="readonly"><s:property value="#request.record" /></textarea>
				</td>
			 </tr>
		</table>
	</fieldset>
</td> 
</tr>
</table>
<br/>
<table width="90%" cellpadding="0" cellspacing="0" align="center"> 
<tr>
	<td align="center"> 
		<input type="button" name="ok"  value='<s:text name="grpInfo.ok" />' class="MyButton" onClick="save()"  image="../../images/share/yes1.gif"> 
		<input type="button" name="return" value='<s:text name="button.close"/>' class="MyButton"  onclick="closeModal(true);" image="../../images/share/f_closed.gif"> 
	</td> 
</tr>  
</table> 
</form>
  
</body>  
</html> 

