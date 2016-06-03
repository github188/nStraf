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
<%@ include file="/inc/deptSelect.inc"%>
<script type="text/javascript"> 
String.prototype.trim = function()
{
	return this.replace(/(^\s*)|(\s*$)/g, "");
}
//添加页面
function add(){
	var strUrl="/pages/leave/leaveInfo!addPage.action";
	var features="800,650,user.leave.addpage,hols";	
	var resultvalue =OpenModal(strUrl,features);	
	query();
}


function query()
{
	var pageNum = document.getElementById("pageNum").value;
	var deptcode =  document.getElementById("queryDept").value;
	if(deptcode=='---请选择部门---'){
		deptcode="";
	}
	var grpcode =  document.getElementById("queryGroup").value;
	if(grpcode=='---请选择项目名称---'){
		grpcode="";
	}
	var userid =  document.getElementById("queryUserName").value;
	var type =  document.getElementById("type").value;
	var startTime =  document.getElementById("startTime").value;
	var endTime =  document.getElementById("endTime").value;
	
	var actionUrl = "<%=request.getContextPath()%>/pages/leave/leaveInfo!refresh.action?form=refresh&pageNum="+pageNum+"&deptcode="+deptcode+"&grpcode="+grpcode+"&userid="+userid+"&type="+type+"&startTime="+startTime+"&endTime="+endTime;
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
 		html+= '<tr id="tr'+entryInfo['id']+'" class="trClass<%=k%2%>" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>';
		html+= '<td width="5%" align="center"><input type="checkbox" name="id" value="'+entryInfo['id']+'"/></td>';
		html+= '<td width="8%">';
   		html+= '<div align="center" id="userid">';
    	html+= '<a href="javascript:showLeaveInfo('+"'"+entryInfo['id']+"'"+')"><font color="#3366FF">'+entryInfo['username']+'</font></a>';
   		html+= '</div>';
 	 	html+= '<td width="11%">';
  		html+= '<div align="center">'+entryInfo['deptName']+'</div>';
  		html+= '</td>';
 	 	html+= '<td width="14%">';
  		html+= '<div align="center">'+entryInfo['grpName']+'</div>';
  		html+= '</td>';
		html+= '<td width="5%">';
 		html+= '<div align="center">'+entryInfo["type"]+'</div>';
		html+= '</td>';
		html+= '<td  width="11%">';
 		html+= '<div align="center">'+entryInfo["startTime"].substring(0,19)+'</div>';
		html+= '</td>';
		html+= '<td  width="11%">';
 		html+= '<div align="center">'+entryInfo["endTime"].substring(0,19)+'</div>';
		html+= '</td>';
		html+= '<td width="8%">';
 		html+= '<div align="center">'+entryInfo["sumtime"]+'</div>';
		html+= '</td>';
		html+= '<td width="8%">';
		html+= '<div align="center">'+entryInfo["status"];
 		html+= '</div>';
		html+= '</td>';
		html+= '<td width="8%">';
 		html+= '<div align="center">'+entryInfo["approverName"]+'</div>';
		html+= '</td>';
		html+= '<td width="11%">';
 		html+= '<div align="center">'+entryInfo["subTime"].substring(0,19)+'</div>';
		html+= '</td>';
		html+= '</tr>' ;
		
 		<% k++;%>
	return html;
}

function showLeaveInfo(id){
	var strUrl="/pages/leave/leaveInfo!view.action?id="+id;
	var features="600,600,user.leave.detail,hols";
	var resultvalue = OpenModal(strUrl,features);

	query();

}

//获取所选复选框
function  GetSelIds()
{
    var idList="";
	var  em= document.all.tags("input");
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
     var em=document.all.tags("input");
	   for(var  i=0;i<em.length;i++)
	   {
	       if(em[i].type=="checkbox")
		   {
		       em[i].checked=chkAll.checked
		   }
	   }
}

function modify(){
	var idList=GetSelIds();
	var ids = idList.split(',');
	 if(idList=="")
  {   
	   alert("请选择需要修改的请假申请");
	   return false;
  }
	 if(ids.length>1){
		 alert('一次只能修改一条请假申请');
			return false;
	}
  var strUrl="/pages/leave/leaveInfo!modifyPage.action?id="+idList;
	var features="800,650,user.leave.update,hols";
	var resultvalue = OpenModal(strUrl,features);
		query();
}

function auditing(){
	var idList=GetSelIds();
	var ids = idList.split(',');
	 if(idList=="")
  {   
	   alert("请选择需要审核的请假申请");
	   return false;
  }
	if(ids.length>1){
		 alert('一次只能审批一条请假申请');
			return false;
	}
	if($("#tr"+idList+" td:eq(8)").text().trim()!="待审批"){
		alert("只有审核状态为待审批的请假申请");
		return false;
	}
	
  var strUrl="/pages/leave/leaveInfo!auditPage.action?id="+idList;
	var features="800,600,user.leave.update,hols";
	var resultvalue = OpenModal(strUrl,features);
		query();
}

function del(){
	var idList=GetSelIds();
	 if(idList=="")
  {   
	   alert("请选择需要删除的请假申请");
	   return false;
  }
	 if(confirm("确定删除该申请吗？")){
	  var strUrl="/pages/leave/leaveInfo!delete.action?id="+idList;
	  var features="800,600,user.hols.deleteTitle,hols";
	  var resultvalue = OpenModal(strUrl,features);
		query();
	 }
}

function showNotifyInfo(id){
	var strUrl="/pages/leave/leaveInfo!view.action?id="+id;
	var features="800,650,operInfo.viewOperator,um";
	var resultvalue = OpenModal(strUrl,features);

	query();

}

//提交审核
function UpAuditing(){
	 var idList=GetSelIds(); 
	
	 if(idList=="")
   {   
	   alert("请选择需要提交审核的申请");
	   return false;
   }
	var ids = idList.split(',');
	for(var i=0;i<ids.length;i++){
		if($("#tr"+ids[i]+" td:eq(8)").text().trim()!="新增"&&$("#tr"+ids[i]+" td:eq(8)").text().trim()!="审批不通过"){
			alert($("#tr"+ids[i]+" td:eq(1)").text()+"的审核状态不允许再次提交审核");
			return false;
		}
	}
 	if (confirm('确定要提交审核吗？'))
    {
	   var strUrl="/pages/leave/leaveInfo!auditing.action?id="+idList;
       var returnValue=OpenModal(strUrl,"600,380,operInfo.delete,um");
	   query();
    }
}

</script>
<body id="bodyid" leftmargin="0" topmargin="0">
<s:form name="leaveForm" action="leaveInfo!list.action"  namespace="/pages/leave" method="post" >
    
    <!-- 如果页面中用到了分页标签，请记得加上下面这两个隐藏标签 -->
	<input type="hidden" name="pageNum" id="pageNum"  value="1"/>  	
  	<input type="hidden" name="pageSize" id="pageSize" value="20" />
  <%@include file="/inc/navigationBar.inc"%>
		<input type="hidden" name="menuid" value="<%=request.getParameter("menuid")%>">
        	<table width="100%" cellSpacing="0" cellPadding="0"> 
                    <tr>
                        <td >
					       <table width="100%" class="select_area">
					          <tr> 
							           <tm:deptSelect 
								deptId="queryDept" 
								deptName="deptcode"
								groupId="queryGroup"
								groupName="grpcode"
								userId="queryUserName"
								userName="userids" 
								isloadName="false" 
								deptHeadKey="---请选择部门---" 
								deptHeadValue="" 
								userHeadKey="----请选择人员----" 
								userHeadValue=""  
								groupHeadKey="---请选择项目名称---"
								groupHeadValue=""
								labelDept="部门 :" 
								labelGroup="项目名称:" 
								labelUser="姓名 :" 
								deptLabelClass="align:right; width:5%;class:input_label"
								deptClass="align:left;width:12%;" 
								groupLabelClass="align:right; width:5%;class:input_label"
								groupClass="align:left;width:12%;" 
								userLabelClass="align:right; width:5%;class:input_label"
								userClass="align:left;width:15%;" 
								>
							</tm:deptSelect>
							           <td align="right"><tm:button site="1"/></td>
							          </tr>
							    <tr>
							    <td align="right" class="input_label">类别：</td>
							      <td>
							            	<select name="type" id="type"
												style="width: 120px">
												<option value="">--请选择--</option>
								            		<s:iterator value="#request.typeMap" id="type">
								            			<option value="<s:property value='value'/>"><s:property value="value"/></option>
								            		</s:iterator>
											</select>
							            </td>
							          	 <td width="10%" align="right" class="input_label"> <s:text name="user.leave.start"/><s:text name="label.colon"/></td>
							          	 <td width="20%">
							          	 	<input  name="startTime" type="text"
											id="startTime" size="12" maxlength="12" class="MyInput"
											value='<s:date name="startTime" format="yyyy-MM-dd"/>'>
											</td>
											 <td width="10%"  align="right" class="input_label"> <s:text name="user.leave.end"/><s:text name="label.colon"/></td>
											<td width="20%">
											<input  name="endTime" type="text"
											id="endTime" size="12" maxlength="12" class="MyInput"
											value='<s:date name="endTime" format="yyyy-MM-dd"/>'>
							          	 </td>
							          </tr>
							        </table> 
                        </td>
                    </tr>
			</table>
			
			<br>

			<table width="100%" height="23" border="0" cellspacing="0" cellpadding="0" class="bgbuttonselect">
			  <tr>
			    <td width="25"  height="23" valign="middle">&nbsp;</td>
			    <td class="orarowhead"><s:text name="operInfo.title"/></td>
			    <td	align="right" width="75%">
			       <tm:button site="2"></tm:button>
			    </td>
			  </tr>
			</table>



		  <table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066" id="downloadList">
		    <tr> 
		      <td nowrap width="2%" class="oracolumncenterheader"></td>
		      <td nowrap width="7%" class="oracolumncenterheader"><div align="center"><s:text name="user.hols.username"/></div></td>
		      <td nowrap width="8%" class="oracolumncenterheader"><div align="center"><s:text name="user.hols.department"/></div></td>
		      <td nowrap width="14%" class="oracolumncenterheader"><div align="center">项目</div></td>
		      <td nowrap width="5%" class="oracolumncenterheader"><div align="center"><s:text name="user.leave.type"/></div></td>
		      <td nowrap width="15%" class="oracolumncenterheader"><div align="center"><s:text name="user.leave.start"/></div></td>
		      <td nowrap width="15%" class="oracolumncenterheader"><div align="center"><s:text name="user.leave.end"/></div></td>
		      <td nowrap width="6%" class="oracolumncenterheader"><div align="center"><s:text name="user.leave.timelong"/></div></td>
		      <td nowrap width="6%" class="oracolumncenterheader"><div align="center"><s:text name="user.leave.status"/></div></td>
		      <td nowrap width="7%" class="oracolumncenterheader"><div align="center"><s:text name="user.leave.approver"/></div></td>
		      <td nowrap class="oracolumncenterheader"><div align="center"><s:text name="user.leave.submit"/></div></td>
		    </tr>
		    <tbody id="formlist">   
			     <s:iterator value="#request.leaves" id="leave" status="s">      
			    <tr id='tr<s:property value="id"/>' class="trClass<s:property value='#s.index%2'/>" oriClass=""> 
			    <td  align="center" width="5%"><input type="checkbox" name="id" value='<s:property value="id"/>'/></td>
			    <td width="8%">
				    <div align="center">
				    	<a href='javascript:showLeaveInfo("<s:property value="id"/>")'><font color="#3366FF">
				    	<s:property value="username"/>
				      </a>
				    </div>
			    </td>  
			    <td width="11%">
			    <div align="center" id="deptName">
			    		<s:property value="deptName"/>
			    	</div>
			    </td>
					<td align="center" style="white-space: nowrap;"><span
						title='<s:property value="grpName"/>'> <s:if
								test="grpName.length()>17">
								<s:property value="grpName.substring(0,17)+'...'" />
							</s:if>
							<s:else><s:property value="grpName"/></s:else>
							 <span>
					</td>
			    <td width="5%">
			    <div align="center" id="type">
			    		<s:property value="type"/>
			    	</div>
			    </td>
			    <td  width="11%" nowrap="nowrap">
			    <div align="center" id="startTime">
			    		<s:date name="startTime" format="yyyy-MM-dd HH:mm:ss"/>
			    	</div>
			    </td>
			    <td  width="11%" nowrap="nowrap">
			    <div align="center" id="endTime">
			    		<s:date name="endTime" format="yyyy-MM-dd HH:mm:ss"/>
			    	</div>
			    </td>
			    <td width="8%">
			    <div align="center" id="sumtime">
			    		<s:property value="sumtime"/>
			    	</div>
			    </td>
			    <td width="8%">
			    <div align="center" id="status">
			    	<s:property value="status"/>
			    	</div>
			    </td>
			    <td width="8%">
			    <div align="center" id="approver">
			    		<s:property value="approverName"/>
			    	</div>
			    </td>
			    <td  width="11%" nowrap="	nowrap">
			    <div align="center" id="subTime">
			    	<s:date name="subTime" format="yyyy-MM-dd HH:mm:ss"/>
			    	</div>
			    </td>
			    </tr>
			 </s:iterator> 
		    </tbody>
		  </table>
		  			<table   bgcolor="#FFFFFF" width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#FFFFFF">  
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