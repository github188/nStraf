<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<html>
<head>
<title>system operator list</title>
<link href="../../css/css_v2.css" rel="stylesheet" type="text/css">
<link href="../../css/htc.css" rel="stylesheet" type="text/css">
</head>
<script language="javascript" src="../../js/tablesort.js"></script>
<script language="javascript" src="../../js/Validator.js"></script>
<script language="javascript" src="../../js/aa.js"></script>

<script language="javascript" src="../../js/jquery.js"></script>

<%@ include file="/inc/pagination.inc"%>
<script language="JavaScript" type="text/JavaScript">
function query()
{
	var pageNum = document.getElementById("pageNum").value;
	var beginDate =  document.getElementById("beginDate").value;
	var endDate =  document.getElementById("endDate").value;
	var userid = document.getElementById("userid").value;
	if(check())
	{	
		var actionUrl = "<%=request.getContextPath()%>/pages/logmgr/sysOperLog!refresh.action?from=refresh&pageNum="+pageNum+"&beginDate="+beginDate+"&endDate="+endDate+"&userid="+userid;
		
		var method="setHTML";
		<%int k = 0 ; %>
		//执行ajax请求,其中method是产生显示list信息的html的方法的名字，该方法是需要在自己的页面中单独实现的,且方法的签名必须是methodName(entry,entryInfo)
		sendAjaxRequest(actionUrl,method,pageNum,true);
	}
}

//构建显示dataList的HTML
function setHTML(entry,entryInfo)
{

	var html = "";
		html+='<tr id="tr'+entryInfo["id"]+'" class="trClass<%=k%2%>" oriClass="" onMouseOut="TrMove(this)" onMouseOver="TrMove(this)">' 
		html+='<td nowrap align="center"><input type="checkbox" name="chkList" value="'+entryInfo['id']+'"/></td>'
		html+='<td nowrap >'+entryInfo["userid"]+'</td>'
		html+='<td nowrap>'+entryInfo["username"]+'</td>'
		html+='<td nowrap>'+entryInfo["logtime"]+'</td>'
		html+='<td nowrap>'+entryInfo["operid"]+'</td>'
		html+='<td nowrap>'+entryInfo["result"]+'</td>'
		html+='<td nowrap>'+entryInfo["note"]+'</td>'
		html+='</tr>'
		
		<% k ++;%>
	return html;
}

 
function  SelAll(chkAll) {
     var chkSet = document.all.tags("input")
     for(var i=0;i<chkSet.length;i++)
	 {
	    if(chkSet[i].name=="chkList")
		{
		   chkSet[i].checked=chkAll.checked
		}   
	 }
}

function  GetSelIds() {
     var idList=""
     var chkSet = document.all.tags("input")
     for(var i=0;i<chkSet.length;i++)
	 {
	    if(chkSet[i].name=="chkList")
		{
			if(chkSet[i].checked)
			{
				idList+=","+chkSet[i].value
			}    
		}   
	 }
	 
	if(idList=="")
		return ""
	return idList.substring(1)
}


function del() {
	var idList = GetSelIds();
	if(idList == "") {
       alert('请选择要删除的内容')
	   return false
	}

	if (confirm('确定删除信息？')) {
		sysOperLogForm.pageNum.value = sysOperLogForm.pageNum.value
		sysOperLogForm.pageSize.value = sysOperLogForm.pageSize.value
		sysOperLogForm.idList.value = idList
		sysOperLogForm.action = "<%=request.getContextPath()%>/pages/logmgr/operLogdel?idList="+idList
		sysOperLogForm.submit()
		query();
	}
}
function check()
{
	var beginDate =  document.getElementById("beginDate").value;
	var endDate =  document.getElementById("endDate").value;
	if(beginDate != "" && endDate == "")
	{
		if(! IsDateTime(beginDate))
		{
			alert("开始时间格式不正确")
			return false;
		}
	}
	else if(endDate != "" && beginDate == "")
	{
		if(! IsDateTime(endDate))
		{
			alert("结束时间格式不正确")
			return false;
		}
	}
	else if(beginDate != "" && endDate != "")
	{
		if (beginDate > endDate) {
			alert("结束时间应大于开始时间")
			return false;
		}
	}
	return true;
}


function IsDateTime(str){      
    if(str.length!=0){    
        var reg = /^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2}) (\d{1,2}):(\d{1,2}):(\d{1,2})$/;     
        var r = str.match(reg);     
        if(r==null)   
        {
        	//alert("日期格式不正确!"); //请将“日期”改成你需要验证的属性名称!
        	return false;
        }
        
         return true;   
    }
    else
    	return false
   
}   


function clearAll() {
	if (confirm('清除所有的信息吗？')) {
		sysOperLogForm.action = "<%=request.getContextPath()%>/pages/logmgr/sysOperLog!delete"
		sysOperLogForm.submit()
	}	
}
</script>
<body id="bodyid"  leftmargin="0" topmargin="0">
<form  name="sysOperLogForm"  method="post" action="<%=request.getContextPath()%>/pages/logmgr/sysOperLog!list.action" onsubmit="return false;">
<%@include file="/inc/navigationBar.inc"%>

 <!-- 如果页面中用到了分页标签，请记得加上下面这两个隐藏标签 -->
	<input type="hidden" name="pageNum" id="pageNum"  value="1"/>
	
<table width="100%" cellspacing="0" cellpadding="0"  class="selectTableBackground">
<input type="hidden" name="menuid" value="<%=request.getParameter("menuid")%>">
  <tr> 
    <td> 
		<fieldset width="100%"><legend><s:text name="label.condition"/></legend> 
		<table width="100%">
        	<tr> 
          <td width="10%" align="right"><s:text name="um.usertime"/><s:text name="label.colon"/></td>
          <td width="40%">
					<table border="0" cellpadding="0" cellspacing="0" align="left">
						<tr>
							<td height="18"> 
								<input name="beginDate"  type="text" id="beginDate" size="12" class="MyInput" isSel="true" isDate="true" onFocus="ShowDateTime(this)" dofun="ShowDateTime(this)" > 
							</td>
							<td> 
								<div align="center">&nbsp;<s:text name="calendar.to"/>&nbsp;</div>
							</td>
							<td height="18"> 
								<input name="endDate"  type="text" id="endDate" size="12" class="MyInput" isSel="true" isDate="true" onFocus="ShowDateTime(this)" dofun="ShowDateTime(this)" > 
							</td>
						</tr>
					</table>
		  	   </td>          	
         <td width="18%" align="right">
	         <s:text name="um.userid"/>
	         <s:text name="label.colon"/>
         </td>
         <td width="10%">
          	<input type="text" name="userid" id="userid"  style="width:60%"/>
         </td>
         <td width="25%" align="right">
            <tm:button site="1"></tm:button>
            <input type="reset" name="btnModal2" value='<s:text name="button.reset"/>'  class="MyButton" id="btnModal"    image="../../images/share/Modify_icon.gif">
         </td>
			</tr>
		</table>
		</fieldset>
	</td>
  </tr>
</table>
<br>

<input type="hidden" name="idList">

<table width="100%" height="20" border="0" cellspacing="0" cellpadding="0" background="../../images/main/bgtop.gif">
  <tr>
    <td width="25" valign="middle">&nbsp;<img src="../../images/share/list.gif" width="14" height="16"></td>
    <td class="orarowhead"><s:text name="label.query.result"/></td>
   
    <td	align="right" width="75%">
       <tm:button site="2"></tm:button>
    </td>
  </tr>
</table>
<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066">  
        <tr bgcolor="#FFFFFF"> 
           <td>
             <table width="100%" cellSpacing="0" cellPadding="0">
                <tr>
                    <td width="5%"> 
	                    <div align="center">
	                    	<input type="checkbox" name="chkAll" id="chkAll" value="all" onClick="SelAll(this)">
	                    </div>
                    </td>
                    <td width="12%"> 
                    	<div align="left"><label for=chkAll><s:text name="label.select.all"/></label></div>
                     </td>
                     <td width="83%"   align="right">
                        <div id="pagetag"> <tm:pagetag pageName="currPage" formName="sysOperLog"  /></div>        
                      </td>
                </tr>
            </table>
      </td>
    </tr>
</table>
<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066" id=downloadList>
	<tr class="oracolumncenterheader"> 
		<td width="5%" align="center"><s:text name="label.select"/></td>
		<td width="10%">操作员编号</td>
	  	<td width="10%">操作员名称</td>
		<td width="19%">操作时间</td>
	  	<td width="12%">操作类型</td>
	  	<td width="10%">操作结果</td>
	  	<td width="36%"><s:text name="um.note"/></td>
	</tr>
	
	<%
		int index = 0;
	 %>
	<tbody id="formlist">
	 <s:iterator value="loglist" >    
	<tr id="tr<s:property value="id"/>" class="trClass<%=index%2%>" oriClass="" onMouseOut="TrMove(this)" onMouseOver="TrMove(this)"> 
	<td nowrap align="center"><input type="checkbox" name="chkList" value='<s:property value="id"/>'></td>
	<td nowrap ><s:property value="userid"/></td>
	<td nowrap><s:property value="username"/></td>
	<td nowrap><s:property value="logtime"/></td>
	<td nowrap><s:property value="operid"/></td>
	<td nowrap><s:property value="result"/></td>
	<td nowrap><s:property value="note"/></td>
	</tr>
	<%
		index ++;
	 %>
	</s:iterator>
	</tbody>
</table>

</form>
</body>
</html>