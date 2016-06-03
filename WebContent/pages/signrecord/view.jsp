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
	</script>
<body id="bodyid"  leftmargin="0" topmargin="20" >
<form name="reportInfoForm" action="<%=request.getContextPath() %>/pages/signrecord/signRecord!approvalsign.action" method="post">
<table width="90%" align="center" cellPadding="1" height="70%" cellSpacing="1" class="popnewdialog1">
		<tr height="10%">
		<td class="input_tablehead" ><h4>详细签到信息</h4></td>
        </tr>
	<tr height="90%">
<td>
        
        <table class="user_info_banner" height="10%">
        	<tr>
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
            <table class="input_table"  height="90%" style="word-break:break-all;word-wrap:break-word;">
            <tr height="10%">
            	<td class="input_label2" width="20%"><div align="center" ><font color="#000000">签到时间：</font></div></td>
                      <td nowrap bgcolor="#FFFFFF" width="30%" ><div align="left">
                      	<s:date name="signTime" format="yyyy-MM-dd HH:mm:ss"/>
               </div></td>
             
             	<td class="input_label2" width="20%"><div align="center" ><font color="#000000">签到地址：</font></div></td>
                      <td  nowrap bgcolor="#FFFFFF" width="30%" ><div align="left">
                      	<div style="position:relative;">  
						  	<s:property value="areaName"/> 
						</div>
                </div></td>
                </tr>
                
                <tr height="10%">
             	<td class="input_label2" width="20%"><div align="center" ><font color="#000000">签到类型：</font></div></td>
                      <td nowrap bgcolor="#FFFFFF" width="30%" ><div align="left">
                      	<div style="position:relative;">  
	                      	<s:if test="type==3">补签到</s:if>
			            	<s:elseif test="type==2&&approveStatus!='无'">备注签到</s:elseif>
			            	<s:else>正常签到</s:else>
						</div>
                </div></td>
                <td class="input_label2" width="20%"><div align="center" ><font color="#000000">签到地点：</font></div></td>
                      <td nowrap bgcolor="#FFFFFF" width="30%" ><div align="left">
                      	<div style="position:relative;">  
	                      	<s:if test="vilid==1">范围内</s:if>
            				<s:if test="vilid==0">范围外</s:if>
            				<s:if test="vilid==-1">正在识别中</s:if>
						</div>
                </div></td>
                </tr>
                
                <tr height="10%">
             	<td class="input_label2" width="20%"><div align="center" ><font color="#000000">审核状态：</font></div></td>
                      <td nowrap bgcolor="#FFFFFF" width="30%" ><div align="left">
                      	<div style="position:relative;"> 
                      	 	<s:if test="approveStatus==0">新增</s:if>
			            	<s:elseif test="approveStatus==2">项目经理审核中</s:elseif>
			            	<s:elseif test="approveStatus==3">部门经理审核中</s:elseif>
			            	<s:elseif test="approveStatus==4">行政审核中</font></s:elseif>
			            	<s:elseif test="approveStatus==5">审核不通过</s:elseif>
			            	<s:elseif test="approveStatus==6">审核通过</s:elseif>
			            	<s:elseif test="approveStatus==1">修改中</s:elseif> 
						</div>
                </div></td>
                <td class="input_label2" width="20%"><div align="center" ><font color="#000000">审核人：</font></div></td>
                      <td nowrap bgcolor="#FFFFFF" width="30%" ><div align="left">
                      	<div style="position:relative;">  
	                      	<s:property value="approvePerson"/>
						</div>
                </div></td>
                </tr>
                
                <tr height="30%">
                <td class="input_label2" ><div align="center" ><font color="#000000">说明：</font></div></td>
                      <td colspan="3" bgcolor="#FFFFFF" width="100%"  style="white-space:pre-line;"><div align="left">
                      	${signNote }
                      </div></td>
            </tr>
                <s:if test="#request.approveRecord.length()>0">
                <tr height="40%">
                
                <td class="input_label2" ><div align="center" ><font color="#000000">审核记录：</font></div></td>
                      <td colspan="3" bgcolor="#FFFFFF" width="100%"  ><div align="left">
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
<table width="100%" cellpadding="0"  cellspacing="0" height="10%" align="center"> 
	<tr>
		<td align="center"> 
			<input type="button" name="return" value='<s:text name="button.close"/>' class="MyButton"  onclick="closeModal();" image="../../images/share/f_closed.gif"> 
		</td> 
	</tr>  
</table> 
</form>
</body>  
</html> 

