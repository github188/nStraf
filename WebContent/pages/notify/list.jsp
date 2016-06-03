<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa" %>
<%@ page  import="java.util.*"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<html>
<head>
	<title>operation info</title>
	<meta http-equiv="Cache-Control" content="no-store"/>   
</head>
<link href="../../css/css_v2.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../calendar/fixDate.js"></script>
<script type="text/javascript" src="../../js/aa.js"></script>
<%@ include file="/inc/pagination.inc"%>

<script type="text/javascript"> 
String.prototype.trim = function()
{
	return this.replace(/(^\s*)|(\s*$)/g, "");
}
//添加页面
function add(){
	var strUrl="/pages/notify/notifyInfo!addPage.action";
	var features="800,600,notify.addnew,notify";	
	var resultvalue =OpenModal(strUrl,features);	
	 refreshList();
}

function query()
{
	var pageNum = document.getElementById("pageNum").value;
	var notifyNum =  document.getElementById("notifyNum").value;
	var title =  document.getElementById("title").value;
	var type =  document.getElementById("optionType").value;
	var status =  document.getElementById("status").value;
	if(type.trim() == "请选择"){
		type = ""
	}
	if(status.trim() == "请选择"){
		status = ""
	}
	var startTime =  document.getElementById("startTime").value;
	var endTime =  document.getElementById("endTime").value;
	
	var actionUrl = "<%=request.getContextPath()%>/pages/notify/notifyInfo!refresh.action?form=refresh&pageNum="+pageNum+"&notifyNum="+notifyNum+"&title="+title+"&type="+type+"&status="+status+"&startTime="+startTime+"&endTime="+endTime;
	actionUrl = encodeURI(actionUrl,"UTF-8");
	var method="setHTML";
	<%int k = 0 ; %>
	//执行ajax请求,其中method是产生显示list信息的html的方法的名字，该方法是需要在自己的页面中单独实现的,且方法的签名必须是methodName(entry,entryInfo)
	sendAjaxRequest(actionUrl,method,pageNum,true);
}

//构建显示dataList的HTML
function setHTML(entry,entryInfo)
{
	var html = "";
	    html+= '<tr id="tr'+entryInfo['notifyNum']+'" class="trClass<%=k%2%>" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>';
		html+= '<td nowrap align="center"><input type="checkbox" name="notifyNum" value="'+entryInfo['notifyNum']+'"/></td>';
 		html+= '<td nowrap width="12%">';
  		html+= '<div align="center">'+entryInfo['notifyNum']+'</div>';
  		html+= '</td>';
 		html+= '<td nowrap width="12%">';
  		html+= '<div align="center">'+entryInfo['type']+'</div>';
  		html+= '</td>';
		html+= '<td nowrap width="12%">';
 		html+= '<div align="center">'
 		html+= '<a href="javascript:showNotifyInfo('+"'"+entryInfo['notifyNum']+"'"+')"><font color="#3366FF">'+entryInfo['title']+'</font></a>';
		html+= '</div></td>';
		html+= '<td nowrap width="10%">';
 		html+= '<div align="center">';
 		if(entryInfo["status"]=='0'){
 			html += '<font color="blue">新建</font>';
 		}
 		if(entryInfo["status"]=='1'){
 			html += '<font color="gray">不通过</font>';
 		}
 		if(entryInfo["status"]=='2'){
 			html += '<font color="red">待审批</font>';
 		}
 		if(entryInfo["status"]=='3'){
 			html += '<font color="green">已发送</font>';
 		}
 		html+= '</div>';
		html+= '</td>';
		html+= '<td nowrap width="12%">';
 		html+= '<div align="center">'+entryInfo["username"]+'</div>';
		html+= '</td>';
		html+= '<td nowrap width="14%"><div align="center">'+entryInfo['writeTime'].substring(0,16)+'</div></td>';
		html+= '<td nowrap width="12%">';
 		html+= '<div align="center">'+entryInfo["approverName"]+'</div>';
		html+= '</td>';
		html+= '<td nowrap width="7%">';
		html+= '<div align="center">';
		if(entryInfo['oatype']=='1')   
   			html+= '是';	
   		else
   		    html+= '否';    
   		     	        
  		html+= '</div>';
  		html+= '</td>';
 		html+= '<td nowrap width="7%">';
		html+= '<div align="center">';
		
		if(entryInfo['emailtype']=='1')   
   			html+= '是';	
   		else
   		    html+= '否';    
   		     	        
  		html+= '</div>';
  		html+= '</td>';
 		/* html+= '<td nowrap width="7%">';
		html+= '<div align="center">';
		
		if(entryInfo['mobiletype']=='1')   
   			html+= '是';	
   		else
   		    html+= '否';    
   		     	        
  		html+= '</div>';
  		html+= '</td>';  */
		html+= '</tr>' ;
 		<% k++;%>
	return html;
}

//获取所选复选框
function  GetSelIds()
{
    var idList="";
	//var  em= document.all.tags("input");
	var em= document.getElementsByName("notifyNum");
	for(var  i=0;i<em.length;i++)
	{
	   if(em[i].type=="checkbox"&&em[i].value!="all")
	   {
		   if(em[i].checked){
			   idList+=","+em[i].value.split(",")[0];
		   }
	   }
	}
	
	 if(idList=="")
	    return "";
	 return idList.substring(1);
}

//全选
function  SelAll(chkAll)
{
     //var em=document.all.tags("input");
     var em= document.getElementsByName("notifyNum");
	   for(var  i=0;i<em.length;i++)
	   {
	       if(em[i].type=="checkbox")
		   {
		       em[i].checked=chkAll.checked
		   }
	   }
}
//提交审核
function UpAuditing(){
	 var idList=GetSelIds(); 
	 if(idList=="")
   {   
	   alert("请选择需要提交审核的通知");
	   return false;
   }
	 var ids = idList.split(',');
		for(var i=0;i<ids.length;i++){
			if($("#tr"+ids[i]+" td:eq(4)").text().trim()!="新建"&&$("#tr"+ids[i]+" td:eq(4)").text().trim()!="不通过"){
				alert($("#tr"+ids[i]+" td:eq(1)").text()+"的审核状态不允许再次提交审核");
				return false;
			}
		}
 	if (confirm('确定要提交审核吗？'))
    {
	   var strUrl="/pages/notify/notifyInfo!auditing.action?notifyids="+idList;
       var returnValue=OpenModal(strUrl,"600,380,operInfo.delete,um");
       refreshList();
    }
}

function modify(){
	var idList=GetSelIds();
	var ids = idList.split(',');
	 if(idList=="")
  {   
	   alert("请选择需要修改的通知");
	   return false;
  }
  if(ids.length>1){
	 alert('一次只能修改一条通知');
		return false;
	}
  var strUrl="/pages/notify/notifyInfo!modifyPage.action?notifyNum="+idList;
  var features="800,600,notify.updateTitle,notify";
	var resultvalue = OpenModal(strUrl,features);
	refreshList();
}

function auditing(){
	var idList=GetSelIds();
	var ids = idList.split(',');
	 if(idList=="")
  {   
	   alert("请选择需要审批的通知");
	   return false;
  }
  if(ids.length>1){
	 alert('一次只能审批一条通知');
		return false;
	}
  if($("#tr"+idList+" td:eq(4)").text().trim()!="待审批"){
		alert("只有审核状态为待审批的通知");
		return false;
	}
  var strUrl="/pages/notify/notifyInfo!auditPage.action?notifyNum="+idList;
  var features="800,600,notify.updateTitle,notify";
	var resultvalue = OpenModal(strUrl,features);
	refreshList();
}

function del(){
	var idList=GetSelIds();
	 if(idList=="")
  {   
	   alert("请选择需要删除的通知");
	   return false;
  }
  var strUrl="/pages/notify/notifyInfo!delete.action?notifyids="+idList;
  var features="800,600,operInfo.updateTitle,um";
  var resultvalue = OpenModal(strUrl,features);
  refreshList();
}

function showNotifyInfo(notifyNum){
	var strUrl="/pages/notify/notifyInfo!view.action?notifyNum="+notifyNum;
	var features="800,600,notify.view,notify";
	var resultvalue = OpenModal(strUrl,features);

	refreshList();

}

</script>
<body id="bodyid" leftmargin="0" topmargin="0">
<s:form name="sysUserForm" action="notifyInfo!list.action"  namespace="/pages/notify" method="post" >
    
    <!-- 如果页面中用到了分页标签，请记得加上下面这两个隐藏标签 -->
	<input type="hidden" name="pageNum" id="pageNum"  value="1"/>
  	<input type="hidden" name="pageSize" id="pageSize" value="20" />
	<input type="hidden" name="userid" id="userid"  value="<s:property value='#request.userid'/>"/>
  	
  <%@include file="/inc/navigationBar.inc"%>
		<input type="hidden" name="menuid" value="<%=request.getParameter("menuid")%>">
        	<table width="100%" cellSpacing="0" cellPadding="0"> 
                    <tr>
                        <td >              
							 <table width="100%" class="select_area">
							          <tr> 
							            <td width="5%" class="input_label"> <s:text name="notify.list.notifyNum"/><s:text name="label.colon"/></td>
							            <td width="7%"><input name="notifyNum" type="text" id="notifyNum" > </td>
							            <td width="5%" class="input_label"> <s:text name="notify.list.title"/><s:text name="label.colon"/></td>
							            <td ><input name="title" type="text" id="title" /></td>
							          	<td width="5%" class="input_label"> <s:text name="notify.list.type"/><s:text name="label.colon"/></td>
							            <td  width="7%">
							            	<select name="type"  id="optionType">
										      	<option><s:text name="notify.option"></s:text></option>
										      	<s:iterator value="#request.typeMap">
										      		<option value="<s:property value="key"/>"><s:property value="value"/></option>
										      	</s:iterator>
										      </select>
							             </td>
							            <td width="5%" class="input_label"> <s:text name="notify.list.status"/><s:text name="label.colon"/></td>
							            <td width="7%">
							            	<select name="status" id="status">
												<option value=""><s:text name="notify.option"/></option>
								            		<s:iterator value="#request.statusMap">
								            			<option value="<s:property value="key"/>"><s:property value="value"/></option>
								            		</s:iterator>
											</select>
							            </td>
							          	
							           
							          </tr>
							          <tr>
							          	 <td width="7%" class="input_label">填写开始时间<s:text name="label.colon"/></td>
							          	 <td width="7%">
							          	 	<input name="startTime" type="text"
											id="startTime" size="17" maxlength="13" class="MyInput"
											value='<s:date name="startTime" format="yyyy-MM-dd"/>'>
											</td>
											<td width="7%" class="input_label">填写结束时间<s:text name="label.colon"/></td>
											<td width="7%">
											<input name="endTime" type="text"
											id="endTime" size="17" maxlength="13" class="MyInput"
											value='<s:date name="endTime" format="yyyy-MM-dd"/>'>
							          	 </td>
							          	  <td align="right" colspan="8"><tm:button site="1"/></td>
							          	</tr>
							        </table>
                        </td>
                    </tr>
			</table>
			
			<br>

			<table width="100%" height="23" border="0" cellspacing="0" cellpadding="0" class="bgbuttonselect">
			  <tr>
			    <td width="25"  height="23" valign="middle"></td>
			    <td class="orarowhead"><s:text name="operInfo.title"/></td>
			    <td	align="right" width="75%">
			       <tm:button site="2"></tm:button>
			    </td>
			  </tr>
			</table>

			

		  <table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066" id="downloadList">
		    <tr> 
		      <td nowrap width="5%" class="oracolumncenterheader"><s:text name="operInfo.checkall"/></td>
		      <td nowrap width="12%" class="oracolumncenterheader"><div align="center">流水号</div></td>
		      <td nowrap width="12%" class="oracolumncenterheader"><div align="center"><s:text name="notify.list.type"/></div></td>
		      <td nowrap class="oracolumncenterheader"><div align="center"><s:text name="notify.list.title"/></div></td>
		      <td nowrap width="10%" class="oracolumncenterheader"><div align="center"><s:text name="notify.list.status"/></div></td>
		      <td nowrap width="8%" class="oracolumncenterheader"><div align="center"><s:text name="notify.list.sender"/></div></td>
		      <td nowrap width="14%" class="oracolumncenterheader"><div align="center"><s:text name="notify.list.writetime"/></div></td>
		      <td nowrap width="8%" class="oracolumncenterheader"><div align="center"><s:text name="notify.list.approval"/></div></td>
		      <td nowrap width="8%" class="oracolumncenterheader"><div align="center"><s:text name="notify.send.oa"/></div></td> 
		      <td nowrap width="8%" class="oracolumncenterheader"><div align="center">邮件发送</div></td>
		      <%-- <td nowrap width="12%" class="oracolumncenterheader"><div align="center"><s:text name="notify.send.mobile"/></div></td> --%>
		    </tr>
		    <tbody id="formlist">   
			     <s:iterator value="notify" id="notify" status="s">      
			    <tr id='tr<s:property value="notifyNum"/>' class="trClass<s:property value='#s.index%2'/>" oriClass=""> 
			    <td nowrap align="center"><input type="checkbox" name="notifyNum" value='<s:property value="notifyNum"/>'/></td>
			      
			    <td nowrap width="12%">
			    <div align="center" id="type">
			    		<s:property value="notifyNum"/>
			    	</div>
			    </td>
			    <td nowrap width="12%">
			    <div align="center" id="type">
			    		<s:property value="type"/>
			    	</div>
			    </td>
			    <td nowrap width="12%">
			    <div align="center" id="title">
			    	<a href='javascript:showNotifyInfo("<s:property value="notifyNum"/>")'><font color="#3366FF">
				    	<s:property value="title"/>
				      </a>
			    	</div>
			    </td>
			    <td nowrap width="10%">
			    	<div align="center">
			    		<s:if test="status==0"><font color="blue">新建</font></s:if>
				    	<s:if test="status==2">
				    		<font color="red">待审批</font>
				    	</s:if>
				    	<s:if test="status==1">
				    		<font color="gray">不通过</font>
				    	</s:if>
				    	<s:if test="status==3">
				    		<font color="green">已发送</font>
				    	</s:if>
			    	</div>
			    </td>
			    <td nowrap width="12%">
			    <div align="center" id="sender">
			    		<s:property value="username"/>
			    	</div>
			    </td>
			     <td nowrap width="14%"><div align="center"><s:date name="writeTime"  format="yyyy-MM-dd  HH:mm"/></div></td>
			    <td nowrap width="12%">
			    <div align="center" id="approver">
			    		<s:property value="approverName"/>
			    	</div>
			    </td>
			     <td nowrap width="7%">
				     <div align="center">     
					     <s:if test='oatype==1'>
					          	是
					     </s:if>     
					     <s:else>
					        	  否
					     </s:else>  
				     </div>
			     </td>
			     <td nowrap width="7%">
				     <div align="center">     
					     <s:if test='emailtype==1'>
					          	是
					     </s:if>     
					     <s:else>
					        	  否
					     </s:else>  
				     </div>
			     </td>
			    <%--  <td nowrap width="7%">
				     <div align="center">     
					     <s:if test='mobiletype==1'>
					          	是
					     </s:if>     
					     <s:else>
					        	  否
					     </s:else>  
				     </div>
			     </td> --%>
			    </tr>
			 </s:iterator> 
		    </tbody>
		  </table>
		  <table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#FFFFFF">  
			  <tr bgcolor="#FFFFFF"> 
			  	<td>
			                <table width="100%" cellSpacing="0" cellPadding="0">
			                    <tr>
			    <td width="6%"> 
			      <div align="center"> 
			        <input type="checkbox" name="all"  id=chkAll   value="all"  onClick="SelAll(this)">
			      </div>
			    </td>
			    <td width="11%"> 
			      <div align="left"><label for="chkAll"><s:text name="operInfo.checkall"/></label></div>
			    </td>
			    <td width="83%" align="right">
				<div id="pagetag"><tm:pagetag pageName="currPage" formName="sysUserForm" /></div>
				</td></tr></table>
					</td>
				</tr>
		  </table>
 </s:form>
</body>
</html>