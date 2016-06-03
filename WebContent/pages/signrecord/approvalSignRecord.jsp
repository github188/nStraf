<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page isELIgnored="false" %> 
<%@page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.*" %>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<link rel="StyleSheet" href="../../plugin/jqueryui/css/smoothness/jquery-ui-1.10.4.custom.css" type="text/css" />
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../js/jquery-1.11.0.js"></script>
<script type="text/javascript" src="../../plugin/jqueryui/js/jquery-ui-1.10.4.custom.js"></script>
<script type="text/javascript" src="../../plugin/jqueryui/js/jquery-ui-cn.js"></script>
<script type="text/javascript" src="../../plugin/jqueryui/js/jquery-ui-timepicker-addon.js"></script>
<script type="text/javascript" src="../../js/common.js"></script>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/pages/common/loading.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<html> 
	<head></head>
	<script type="text/javascript">
		String.prototype.trim = function()
		{
			return this.replace(/(^\s*)|(\s*$)/g, "");
		}
		
		function save(){
			if(document.getElementById("approveStatus").value.trim()=="5"&&document.getElementById("signResult").value.trim()=="")
			{
				alert("请填写审核不通过原因");
				document.getElementById("signResult").focus();
				return;
			}
			window.returnValue=true;
			reportInfoForm.submit();
		}
		
		$(function(){
			$("#approveStatus").change(function(){
				if($(this).val()==6){
					if($("#signResult").val().trim==""){
						$("#signResult").html("情况属实");
					}
					var num = $("#approvePerson").children().length;
					if(num>0){
						$("#nextTd").show();
						$("#nextTd").prev().attr("colspan","2");
						$("#selectTd").show();
					}
				}else{
					//$("#signResult").html("");
					$("#nextTd").hide();
					$("#selectTd").hide();
					$("#nextTd").prev().attr("colspan","5");
				}
			});
			var appstatus = "<%=request.getAttribute("appstatus")%>";
			if(appstatus=="3"){
				$("#nextUser").hide();
			}
		});
	</script>
<body id="bodyid"  leftmargin="0" topmargin="10" >
<form name="reportInfoForm" action="<%=request.getContextPath() %>/pages/signrecord/signRecord!approvalsign.action" method="post">
<table width="90%" align="center" cellPadding="1" cellSpacing="1" class="popnewdialog1">

<tr height="10%">
			<td class="input_tablehead" ><h4>审核签到信息</h4></td>
	    </tr>
	<tr height="90%">
<td>
        <table class="user_info_banner" height="10%">
        	<tr height="20%"  class="user_info_banner">
        	<s:hidden name="id" id="id" />
        		<td align="left" width="13%" nowrap="nowrap"><b><div align="left" style="padding-top:4">姓名：<s:property value='username'/></div></b></td> 
            	<td align="left" width="7%" nowrap="nowrap"></td> 
        	 	<td align="left" width="13%" nowrap="nowrap"><b><div align="left" style="padding-top:4">部门：<s:property value='deptName'/></div></b></td> 
            	<td align="left" width="7%" nowrap="nowrap"></td> 
            	<td align="left" width="13%" nowrap="nowrap"><b><s:if test="grpName.length()>0">  <div align="right'" style="padding-top:4">项目名称：<s:property value='grpName'/></div></s:if></b></td>  
                <td align="left" width="7%" nowrap="nowrap"></td>  
                <td align="left" width="40%" nowrap="nowrap"></td> 
            </tr>
            </table>
            <table class="input_table" height="90%"  style="word-break:break-all;word-wrap:break-word;">
            <tr>
            	<td class="input_label2" colspan="2"><div align="right" ><font color="#000000">签到时间：</font></div></td>
                      <td colspan="5" nowrap bgcolor="#FFFFFF" width="100%" ><div align="left">
                      	<s:date name="signTime" format="yyyy-MM-dd HH:mm:ss"/>
               </div></td>
              </tr>
              <tr>
             	<td class="input_label2" colspan="2"><div align="right" ><font color="#000000">签到地址：</font></div></td>
                      <td colspan="5" nowrap bgcolor="#FFFFFF" width="100%" ><div align="left">
                      	<div style="position:relative;">  
						  	<s:property value="areaName"/> 
						</div>
                </div></td>
                </tr>
                
                <tr>
             	<td class="input_label2" colspan="2"><div align="right" ><font color="#000000">签到类型：</font></div></td>
                      <td colspan="5" nowrap bgcolor="#FFFFFF" width="100%" ><div align="left">
                      	<div style="position:relative;">  
	                      	<s:if test="type==3">补签到</s:if>
			            	<s:elseif test="type==2&&approveStatus!='无'">备注签到</s:elseif>
			            	<s:else>正常签到</s:else>
						</div>
                </div></td>
                </tr>
                <tr height="20%;">
                <td class="input_label2" colspan="2"><div align="right" ><font color="#000000">说明：</font></div></td>
                      <td colspan="5" bgcolor="#FFFFFF" width="100%" style="white-space:pre-line;"><div align="left">
                      	${signNote }
                      </div></td>
            </tr>
            <tr id="nextUser">
            <td class="input_label2" colspan="2" id="nextTd"><div align="right" ><font color="#000000">转交下一步审核人：</font></div></td>
                 <td colspan="5" nowrap bgcolor="#FFFFFF" width="100%" id="selectTd"><div align="left" name="nexthide">
                      	<s:select name="approvePerson" id="approvePerson"  list="#request.approvePerson" listKey="userid" listValue="username" ></s:select>
                </div></td>
            </tr>
              <tr>
                <td class="input_label2" colspan="2"><div align="right" ><font color="#000000">审核结果：</font></div></td>
                 <td colspan="5" nowrap bgcolor="#FFFFFF" width="100%" ><div align="left">
                      	<s:select name="approveStatus" id="approveStatus"  list="#{'6':'审核通过','5':'审核不通过' }" listKey="key" listValue="value"></s:select>
                </div></td>
                </tr>
                
                <tr height="20%;">
            	<td class="input_label2" colspan="2" ><div align="right" ><font color="#000000">审核结论：</font></div></td>
                      <td colspan="5" nowrap bgcolor="#FFFFFF" width="100%" ><div align="left">
                      	<s:textarea name="signResult" id="signResult" style="width:100%;"  rows="4" cols="80" onblur="limitLen(250,this)"></s:textarea>
                      </div></td>
           		 </tr>
                <s:if test="#request.approveRecord.length()>0">
                <tr height="30%;">
                
                <td class="input_label2" colspan="2"><div align="right" ><font color="#000000">审核记录：</font></div></td>
                      <td colspan="5" bgcolor="#FFFFFF" width="100%" ><div align="left">
                      	<!-- <s:property value="#request.arealist" /> id="approveRecord" style="width:100%;"  rows="4" cols="80" onblur="limitLen(250,this)"/>-->
						${approveRecord }
                      </div></td>
           		</tr>
               </s:if> 
        </table>
        
</td> 
</tr>
</table>
<br/>
<table width="100%" cellpadding="0" cellspacing="0" align="center"> 
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

