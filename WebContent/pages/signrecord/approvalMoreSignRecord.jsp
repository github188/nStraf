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
<%
	String status = request.getAttribute("appstatus").toString();
	String title = "";
	String round = "";
	if("3".equals(status)){
		title = "项目经理";
		round = "部门经理";
	}else if("2".equals(status)){
		title = "部门经理";
		round = "项目经理";
	}
%>

<html> 
	<head></head>
	<script type="text/javascript">
		String.prototype.trim = function(){
			return this.replace(/(^\s*)|(\s*$)/g, "");
		}
		
		function save(){
			var noDealId = GetSelIds();
			document.getElementById("noDealId").value=noDealId;
			if(document.getElementById("approveStatus").value.trim()=="5"&&document.getElementById("signResult").value.trim()==""){
				alert("请填写审核不通过原因");
				document.getElementById("signResult").focus();
				return;
			}
			window.returnValue=true;
			reportInfoForm.submit();
		}
		
		 function showInfo(id){
			var strUrl="/pages/signrecord/signRecord!view.action?id="+id;
			var features="800,600,signrecord.title_view";	
			var resultvalue =OpenModal(strUrl,features);	
		 }
		 
		 function  GetSelIds(){
		    var idList="";
		 	var  em= document.getElementsByName("chkList");
		 	for(var  i=0;i<em.length;i++){
		 	   if(em[i].type=="checkbox"){
		 		   if(!em[i].checked){
		 			   idList+=","+em[i].value.split(",")[0];
		 		   }
		 	   }
		 	}
		 	if(idList=="")
		 	    return "";
		 	return idList.substring(1);
		 }
		 
		 //全选按钮事件
		function  SelAll(chkAll) {
		    var chkSet = document.getElementsByName("chkList");
		    for(var i=0;i<chkSet.length;i++){
			    if(chkSet[i].name=="chkList"){
				   chkSet[i].checked=chkAll.checked;
				}   
			 }
		}
	</script>
<body id="bodyid"  leftmargin="0" topmargin="10" >
<form name="reportInfoForm" action="<%=request.getContextPath() %>/pages/signrecord/signRecord!approvalmoresign.action" method="post">
<table width="95%" align="center" cellPadding="1" cellSpacing="1" class="popnewdialog1">
	<tr height="95%">
		<td>
			<table width="100%" height="23" border="0" cellspacing="0" cellpadding="0" class="bgbuttonselect">
				<tr>
					<td class="orarowhead">当前环节：<font color="red"><%=round %>审核</font></td>
				</tr>
			</table>
            <table class="input_table" height="90%"  style="word-break:break-all;word-wrap:break-word;">
            <tr>
                <td style="text-align: center;background-color: #FFFFFF;white-space:nowrap;width:10%">
                	<div align="right" ><font color="#000000">审核结果：</font></div>
                </td>
                <td  nowrap bgcolor="#FFFFFF" width="90%" >
                	<div align="left">
                      	<s:select name="approveStatus" id="approveStatus"  list="#{'6':'审核通过','5':'审核不通过' }" listKey="key" listValue="value"></s:select>
                	</div>
                </td>
			</tr>
			<tr height="20%;">
            	<td style="text-align: center;background-color: #FFFFFF;white-space:nowrap;width:10%">
            		<div align="right" ><font color="#000000">审核结论：</font></div>
            	</td>
				<td nowrap bgcolor="#FFFFFF" width="90%" >
					<div align="left">
                      	<s:textarea name="signResult" id="signResult" style="width:100%;"  rows="4" cols="80" onblur="limitLen(250,this)"></s:textarea>
                    </div>
				</td>
			</tr>
            <!--<s:if test="#request.approveRecord.length()>0">
            <tr height="30%;">
                <td style="text-align: center;background-color: #FFFFFF;white-space:nowrap;width:10%">
                	<div align="right" ><font color="#000000">审核记录：</font></div>
                </td>
				<td bgcolor="#FFFFFF" width="90%" >
					<div style="text-align:left;height:150px;overflow:auto; ">
						${approveRecord }
                    </div>
                </td>
			</tr>
			</s:if> -->
            <tr>
            	<td class="input_label2" colspan="2">
            		<input type="hidden" name="noDealId" id="noDealId" value="" />
					<input type="hidden" name="pageNum" id="pageNum" value="1" />
					<input type="hidden" name="pageSize" id="pageSize" value="10" />
					<input type="hidden" name="menuid" value="<%=request.getParameter("menuid")%>">
					<table width="100%" height="23" border="0" cellspacing="0" cellpadding="0" class="bgbuttonselect">
						<tr>
							<td class="orarowhead">移动签到数据</td>
							<td align="right" width="75%"></td>
						</tr>
					</table>
					<div id="showdiv" style='display: none; z-index: 999; background: white; height: 39px; line-height: 50px; width: 42px; position: absolute; top: 236px; left: 670px;'>
						<img src="../../images/loading.gif">
					</div>
					<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066" id=downloadList>
						<tr>
							<td nowrap class="oracolumncenterheader" width="5%"><input type="checkbox" name="chkAll" id="chkAll" value="all" checked onClick="SelAll(this)"></td>
							<td nowrap width="8%" class="oracolumncenterheader"><div align="center">姓名</div></td>
							<td nowrap width="16%" class="oracolumncenterheader"><div align="center">签到日期</div></td>
							<td nowrap width="8%" class="oracolumncenterheader"><div align="center">补签类型</div></td>
							<td nowrap width="10%" class="oracolumncenterheader"><div align="center"><%=title %></div></td>
							<td nowrap width="45%" class="oracolumncenterheader"><div align="center">备注</div></td>
							<td nowrap width="8%" class="oracolumncenterheader"><div align="center">查看详情</div></td>
						</tr>
						<tbody name="formlist" id="formlist" align="center">
							<s:iterator value="signlist" id="tranInfo" status="row">
								<s:if test="#row.odd == true">
									<tr id="tr" class="trClass0" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
								</s:if>
								<s:else>
									<tr id="tr" class="trClass1" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>
								</s:else>
									<td nowrap align="center">
										<input type="checkbox" name="chkList" value="<s:property value='Id'/>" checked/>
									</td>
									<td><s:property value="username" /></td>
									<td><s:date name="signTime" format="yyyy-MM-dd HH:mm:ss" /></td>
									<td>
										<s:if test="type==3"><font color="navy">补签到</font></s:if>
						            	<s:elseif test="type==2&&approveStatus!='无'"><font color="brown">备注签到</font></s:elseif>
						            	<s:else>正常签到</s:else>
									</td>
									<td><s:property value="signwhere" /></td>
									<td align="left"><s:property value="signNote" /></td>
									<td>
										<a href='javascript:showInfo("<s:property value="Id"/>")'><font color="blue">查看详情</font></a>
									</td>
								</tr>
							</s:iterator>
						</tbody>
					</table>
               	</td>
			</tr>
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

