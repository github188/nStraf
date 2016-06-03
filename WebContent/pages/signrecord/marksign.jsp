<%@ page contentType="text/html; charset=UTF-8"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page isELIgnored="false" %> 
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
			if(document.getElementById("approvePerson").value=="")
			{
				alert("请选择审核人员");
				document.getElementById("approvePerson").focus();
				return;
			}
			if(document.getElementById("signNote").value=="")
			{
				alert("请填写补签说明");
				document.getElementById("signNote").focus();
				return;
			}
			window.returnValue=true;
			reportInfoForm.submit();
		}
		
		$(function(){
			var isDeptManager = "<%=request.getAttribute("isDeptManager")%>";
			if(isDeptManager=="true"){
				$("#approveTr").hide();
			}
			$("#signTime").datetimepicker({  
				timeFormat: 'HH:mm:ss',
				stepHour: 1,
				stepMinute: 1,
				stepSecond: 5,
				dateFormat:'yy-mm-dd',  //更改时间显示模式  
				//showAnim:"slide",       //显示日历的效果slide、fadeIn、show等  
				changeMonth:true,       //是否显示月份的下拉菜单，默认为false  
				changeYear:true,        //是否显示年份的下拉菜单，默认为false  
				//showWeek:true,          //是否显示星期,默认为false  
				//showButtonPanel:true,   //是否显示取消按钮，并含有current,close按钮，datePicker默认为false,datetimepicker默认为：ture  
				//closeText:'close'      //设置关闭按钮的值  
				//yearRange:'2010:2012',  //显示可供选择的年份  
				//defaultDate:+7          //表示默认日期是在当前日期加上7天  
				timeText: '时间',
				hourText: '小时',
				minuteText: '分钟',
				secondText: '秒',
				//showButtonPanel:false,
				//controlType:'select',
				closeText: 'Close'
		      });
		
		});
		
	</script>
<body id="bodyid"  leftmargin="0" topmargin="20" >
<form name="reportInfoForm" action="<%=request.getContextPath() %>/pages/signrecord/signRecord!updateMarksign.action" method="post">
<table width="90%" align="center" cellPadding="1" cellSpacing="1" class="popnewdialog1">
       <tr height="10%">
			<td class="input_tablehead" ><h4>备注签到信息</h4></td>
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
            	<td class="input_label2" colspan="2"><div align="right" ><font color="#000000">签到时间：</font></div></td>
                      <td colspan="5" nowrap bgcolor="#FFFFFF" width="100%" ><div align="left">
                      	<input name="signTime" id="signTime" size="22"
										style="text-align: left;"
										value='<s:date name="signTime" format="yyyy-MM-dd HH:mm:ss"/>'
										readonly="true" class="input_readonly" />
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
              <tr id="approveTr">
                <td class="input_label2" colspan="2"><div align="right" ><font color="#000000">审核人员：</font></div></td>
                 <td colspan="5" nowrap bgcolor="#FFFFFF" width="100%" ><div align="left">
                      	<s:select name="approvePerson" id="approvePerson"  list="#request.approvePerson" listKey="userid" listValue="username"></s:select>
                </div></td>
                </tr>
              <tr>
            	<td class="input_label2" colspan="2"><div align="right" ><font color="#000000">备注说明：</font></div></td>
                      <td colspan="5" nowrap bgcolor="#FFFFFF" width="100%" ><div align="left">
                      	<s:textarea name="signNote" id="signNote" style="width:100%;"  rows="4" cols="80" onblur="limitLen(250,this)"></s:textarea>
                      </div></td>
            </tr>
            <s:if test="#request.approveRecord.length()>0">
                <tr height="40%;">
                
                <td class="input_label2" colspan="2"><div align="right" ><font color="#000000">审核记录：</font></div></td>
                      <td colspan="5" bgcolor="#FFFFFF" width="100%"  ><div align="left">
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

