﻿<%@ page contentType="text/html; charset=UTF-8" %>


<%@page import="java.util.*,
                 com.opensymphony.workflow.Workflow,
                 com.opensymphony.workflow.basic.BasicWorkflow,
                 com.opensymphony.workflow.spi.Step,
                 com.opensymphony.workflow.query.Expression,
                 com.opensymphony.workflow.query.FieldExpression,
                 com.opensymphony.workflow.query.NestedExpression,
                 com.opensymphony.workflow.query.WorkflowExpressionQuery,
                 com.opensymphony.workflow.loader.WorkflowDescriptor,
                 com.opensymphony.workflow.loader.ActionDescriptor"%>
<%@page import="cn.grgbanking.feeltm.login.domain.UserModel"%>
<%@page import="oracle.jdbc.driver.Const"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<html>
<head>
<STYLE>
.MyButton{
  behavior:url(<%=request.getContextPath()%>/htc/button.htc);} 
tr{nowrap;} 
td{height:19;}  
</STYLE>
<title>queryPage</title>
</head>
<link href="../../css/css_v2.css" type="text/css" rel="stylesheet">
<link href="../../css/htc.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../../js/Validator.js"></script>
<script type="text/javascript" src="../../js/tablesort.js"></script>

<script type="text/javascript">

function validateInput(){ 
	var s=document.getElementById("approveRst")
	if(!s){
		alert('您在当前流程状态中无处理权限!');
		return false;
	}
	if (!Validate('approveRst','Require')){
	return false;
	}	
    if(!Validate('approveRst','Integer')){  
	alert('请选择审批动作!');
	return false;
	}	
	if (!Validate('approveNote','Require')){  
		alert('请输入审核意见!');
		return false;
	}
	
	if(!changebyte(bankmaPlanForm.approveNote,1000)){
	  alert('审核 备注 输入字节数超出范围!');
	  return false;
	 }
	
	var currentStepIdValue=document.getElementById("currentStepId").value;
	if( currentStepIdValue!="4" && currentStepIdValue!="40" && currentStepIdValue!="13" && currentStepIdValue!="130" )
    {	
	var approveResult=document.getElementById("approveResult").value;
	if(!(approveResult=="拒绝"||approveResult=="取消流程"||approveResult=="修改招牌勘察结果"||approveResult=="修改平面设计图"||approveResult=="收回流程"||approveResult=="退回"))
	{
	var next_Handler=document.getElementById("nextHandler").value;
	 if(next_Handler==undefined||next_Handler=="" )
	 {
	  alert("请选择转交人员!");
	  return false;
	 }
	 }
	}
	
	  return true;
}

function countByte(str){
　　var byteLen=0,len=str.length;
　　if(str){
　　　　for(var i=0; i<len; i++){
　　　　　　if(str.charCodeAt(i)>255){
　　　　　　　　byteLen += 2;
　　　　　　}
　　　　　　else{
　　　　　　　　byteLen++;
　　　　　　}
　　　　}
　　}
   return byteLen;
}


function SubmitAprrovement(){
	//if(validateInput()){
		var nextHandlerName = document.getElementById("nextHandlerName");
		var disp = document.getElementById("selectHandler").style.display;
		if(disp!="none"){
			var str = nextHandlerName.value;
			if(str.length<=0){
				alert("请选择下一步处理人");
				return;
			}
		}
		auditInfoForm.submit();
	//}
	
}

function setApproveResult(approveResult){
	//alert(approveResult);
	document.getElementById("approveResult").value=approveResult;
	var currentStepIdValue=document.getElementById("currentStepId").value;
	if( currentStepIdValue!="4" && currentStepIdValue!="40" && currentStepIdValue!="13" && currentStepIdValue!="130" )
    {
	if(approveResult=="拒绝"||approveResult=="取消流程"||approveResult=="回收流程"||approveResult=="退回"||approveResult=="通过")
	document.getElementById("selectHandler").style.display="none";
	else
	document.getElementById("selectHandler").style.display="block";
	}
}

//选择流程下一步的OWNER
function selectOneUser(obj){
	  //var objValue=obj.value;
	  //var strUrl="/pages/um/sysUser.do?action=selectOneUser&userid="+objValue;
	 // var strUrl="/pages/um/sysUser.do?action=selectOneUser";
		//var features="800,500,operInfo.viewOperator,um";
		//var returnValue=OpenModal(strUrl,features);
		//if(returnValue != null){
		//	try{
		//	obj.value = returnValue[0];
		//	auditInfoForm.nextHandlerName.value=returnValue[1];
		//	}catch(e){}
		//}
	 var objValue=obj.value;
	  //var strUrl="/pages/um/sysUser.do?action=selectOneUser&userid="+objValue;
	  var strUrl="/pages/um/sysUserInfo!getUsrUsrgrp.action";
		var features="300,300,rule.nextUser,rule";
		var returnValue=OpenModal(strUrl,features);
		if(returnValue != null){
			try{
			obj.value = returnValue[0];
			//alert(returnValue.length);
			//blackRegulationForm.reversionName.value=returnValue[0];
			}catch(e){}
		}
		
}

function viewBlackListInfo(applyId){	
	var strUrl="/pages/rule/blackRegulation!show.action?applyId=" + applyId;
 	var returnValue=OpenModal(strUrl,"400,300,bankManager.viewDesignDetail,bankManage")
}

function viewSignboardPic(bankId){	
	var strUrl="/pages/bankManage/signboardView.do?action=show4FlowAudit&bankId="+bankId;
 	var returnValue=OpenModal(strUrl,"600,500,bankManager.viewDesignDetail,bankManage")
}

function strlength(str){
    var l=str.length;
    var n=l
    for (var i=0;i<l;i++)
    {
        if (str.charCodeAt(i)<0||str.charCodeAt(i)>255) n++
    }
    return n        
}

function changebyte(obj,length){
    var l=strlength(obj.value)
    if (l<=length) {
        if (document.all!=null) document.all(obj.name+"Tip").innerText="还可以输入"+(length-l)+"字节"
    }
    else
    {
        document.all(obj.name+"Tip").innerText="输入字节数超出范围";
        return false;
    }
    return true
}

</script>

<body id="bodyid" leftmargin="10" topmargin="10">
<form name="auditInfoForm"  action="<%=request.getContextPath()%>/pages/audit/auditInfo!update.action"  method="post" >	
  <s:iterator value="auditInfo">
<fieldset  width="100%">
		<legend>申请信息</legend> 
<table width="100%"  border="0" cellpadding="1" cellspacing="1" bgcolor="#000066" >
<tr class="trClass0">
  <td nowrap  width="16%" align="right"><s:text name="audit.info.applyId" /><s:text name="label.colon"/></td>
  <td nowrap  width="18%">
   <input type="hidden" name="action" value="saveAudit"/>
   <input type="hidden" name="id" id="id" value="<s:property value="id"/>"/>
  	<s:property value="applyId"/>
  </td>
  <td nowrap  width="16%" align="right"><s:text name="audit.info.applydate" /><s:text name="label.colon"/></td>
  <td nowrap  width="17%">
  	<s:property value="applayDate"/>
  </td>
  <td nowrap  width="16%" align="right"><s:text name="audit.info.applyType" /><s:text name="label.colon"/></td>
  <td nowrap  width="*">
  	<tm:dataDir beanName="auditInfo" property="applytyp" path="ruleMgr.applyType" />
  </td>
  </tr>
  
  <tr class="trClass1">
  <td nowrap  width="16%" align="right"><s:text name="audit.info.creater" /><s:text name="label.colon"/></td>
  <td nowrap  width="18%"><s:property value="applyName" /></td>
  <td nowrap  width="16%" align="right"><s:text name="audit.orgNo" /><s:text name="label.colon"/></td>
  <td nowrap  width="17%"><s:property value="orgId" /></td>
  <td nowrap  width="16%" align="right"></td>
  <td nowrap  width="*"></td>
  </tr>
  
  <%
  	String flow_name = (String)request.getSession().getAttribute("applytyp");
  %>
  <%
  	String flow_chart_name = "";
  	if(flow_name != null && flow_name.equals("1"))
  		flow_chart_name = "blacklist_apply";
  	else
  		flow_chart_name = "repeat_apply";
  %>
  
 </table>
 </fieldset>

<br />

<fieldset  width="100%">
		<legend>申请详情</legend> 
<table width="100%" border="0" cellpadding="1" cellspacing="1"  >
		<tr>
			<td nowrap >
			<DIV align="center">
  <%
  if(flow_name!=null&&flow_name.equals("1"))
  {
  %>			  
    		    <iframe id="ifmRst" src='<%=request.getContextPath()%>/pages/rule/blackRegulation!info.action?applyId=<s:property value="applyId"/>' width="100%"
					height="50px" marginheight="0" frameborder="0" border="0" scrolling="no" noresize>
				</iframe>
  <% 
  }
  else
  {
  %>
  				<iframe id="ifRep" src='<%=request.getContextPath() %>/pages/rule/repeatRegulation!info.action?applyId=<s:property value="applyId"/>' width="100%"
  					height="50px" marginheight="0" frameborder="0" border="0" scrolling="no" noresize>
  				</iframe>
  <%  
  }
  %>  				
  			</DIV>
			 </td>
		</tr>
	</table>	
</fieldset>
<br />

<fieldset class="jui_fieldset" width="100%">
		<legend>申请审核记录</legend> 
<table width="100%"  border="0" cellpadding="1" cellspacing="1" bgcolor="#000066" >
<tr class="oracolumncenterheader">
    <td align="center" width="10%">经办人</td>
    <td align="center" width="15%">审批结果</td>
    <td align="center" width="20%">审批时间</td>
    <td align="center" width="*">审批备注</td>
</tr>
	<s:iterator id="auditLog" value="auditLogList" status="index">
	<tr class="trClass0"  onMouseOut=TrMove(this) onMouseOver=TrMove(this)> 
		<td   nowrap align="center"><s:property value="username" /></td>
		<td   nowrap align="center"><s:property value="applyResult" /></td>
		<td   nowrap align="center"><s:date name="applayDate" format="yyyy-MM-dd HH:mm:ss" /></td>
		<td   nowrap align="center"><s:property value="applyNote"/></td>
	</tr>
	</s:iterator>

</table>



<br /><br />
<table>
<tr>
	<td nowrap  width="20%">审批动作</td>
	<td nowrap  colspan="3">
		<%
			UserModel userModel = (UserModel)request.getSession().
				getAttribute(Constants.LOGIN_USER_KEY);
		
			/** 工作流流 开始 */
	  		Workflow wf = new BasicWorkflow(userModel.getUserid());
	  		long wfid = Long.parseLong((String) request.getSession().getAttribute("wfid"));
			System.out.println(request.getSession().getAttribute("wfid"));  
			String lastActionName="";
			String currentStepValue="0";
	  		try {
	    		WorkflowDescriptor wd = wf.getWorkflowDescriptor(wf.getWorkflowName(wfid)); //取得当前流程的所有描述
	    		List stepList=wf.getCurrentSteps(wfid);
		    	if(stepList.size()>0)  {
				    Step currentStep=(Step)stepList.get(0);
				    currentStepValue=String.valueOf(currentStep.getStepId());
	        	}    
	      int[] actions = wf.getAvailableActions(wfid, null);//取得当前流程所有可用ACTION
	      for (int i = 0; i < actions.length; i++) {
	        String name = wd.getAction(actions[i]).getName();//取得各个ACTION的名字，如“通过”，“拒绝”等
	        System.out.println("name:"+name+" Action:"+actions[i]);
	        lastActionName=name;
	        %>
	        <input type="radio" name="approveRst" value='<%= actions[i] %>' checked onclick="setApproveResult('<%=name %>')"><%= name %>&nbsp;&nbsp;
		  <%
		  }
	  	}catch(Exception ex){
	    System.out.println("取得工作列表时出现异常");
	    out.println("取得工作列表时出现异常,请联系系统管理员!");
	    ex.printStackTrace();
	  	} %>
	<input type="hidden" name="approveResult" id="approveResult" value='<%=lastActionName %>'/>
	<input type="hidden" name="currentStepId" id="currentStepId" value='<%=currentStepValue %>'/>
 	</td>
</tr>
<tr>
	<td nowrap  width="20%">
	审批备注
	</td>
	<td nowrap colspan="3">
	<textarea name="approveNote" id="approveNote" cols="50" rows="8" onkeydown="changebyte(this,1000)" onkeyup="return changebyte(this,1000)" onmouseup="return changebyte(this,1000)" onmousedown="return changebyte(this,1000)"></textarea>
	</td>	
</tr>
<tr>
	<td nowrap  width="20%">
	&nbsp;
	</td>
<td colspan="3"><SPAN id=approveNoteTip><SCRIPT language=JavaScript>changebyte(auditInfoForm.approveNote,1000);</SCRIPT></SPAN></td>
</tr>
<tr id="selectHandler" style="display:none">
 <td nowrap >
<s:text name="rule.nextUser" /><s:text name="label.colon" />
 </td>
 <td nowrap colspan="3">
 <input type="text" name="nextHandlerName" id="nextHandlerName"  class="InputReadOnly"/>
		<img src="../../images/share/view.gif" width="18"
		height="17"  
		style="CURSOR: hand"
		onclick="selectOneUser(auditInfoForm.nextHandlerName)"
		>
<input type="hidden" name="nextHandler" id="nextHandler" />
 </td>
</tr>
</table>
</fieldset>



<table width="100%" border="0" cellpadding="1" cellspacing="1"  id=downloadList>
<tr>
<td nowrap  align="center">
<input type="button" name="btnNext" value="<s:text name="button.ok"/>" class="MyButton" AccessKey="S" onClick="SubmitAprrovement()" image="../../images/share/save.gif">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" name="btnClose" value="<s:text name="button.close"/>" class="MyButton" AccessKey="C" onClick="window.close()" image="../../images/share/f_closed.gif">
</td>
</tr>
</table>

  </s:iterator>
</form>
</body>
</html>