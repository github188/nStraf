<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<html>
	<head><title>group info</title></head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script language="javascript" src="../../js/tablesort.js"></script>

<%@ include file="/inc/pagination.inc"%>	
<script language="JavaScript">
function query()
{
	var pageNum = document.getElementById("pageNum").value;
	var projectName = document.getElementById("projectName").value;
	var select=document.getElementById("isEnd");
	var index = select.selectedIndex; // 选中索引
	var isEndValue = select.options[index].value; // 选中值
	if(pageNum.indexOf(".")!=-1){
		pageNum = 1;
	}
	var actionUrl = "<%=request.getContextPath()%>/pages/project/project!refresh.action?form=refresh&pageNum="+pageNum;
	actionUrl += "&projectName="+projectName;
	actionUrl += "&isEndValue="+isEndValue;
	
	actionUrl = encodeURI(actionUrl,"UTF-8");
	var method="setHTML";
	
	<%
	int j = 0;//记录的索引
	int k = 1;
	%>
	sendAjaxRequest(actionUrl,method,pageNum,true);
}


//构建显示dataList的HTML
function setHTML(entry,entryInfo)
{
	var attType="",attEntryTime="",attExitTime="";
	if(entryInfo['attendances']!="" && entryInfo['attendances'][0].attendanceType){
		attType=entryInfo['attendances'][0].attendanceType;
	}
	if(entryInfo['attendances']!="" && entryInfo['attendances'][0].entryTime!=undefined){
		attEntryTime=entryInfo['attendances'][0].entryTime.substring(11,19);
	}
	if(entryInfo['attendances']!="" && entryInfo['attendances'][0].exitTime!=undefined){
		attExitTime=entryInfo['attendances'][0].exitTime.substring(11,19);
	}
	var html = "";
    html+=' <tr id="tr<%=++k%>" class="trClass<%=j%2%>" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)> ';
    html+= '<td><input type="checkbox" name="id" value="'+entryInfo['id']+'"/></td>';
    html+='<td width="25%">';
    html+= '<a href="javascript:showProjectInfo('+"'"+entryInfo['id']+"'"+')"><font color="#3366FF">'+entryInfo['name']+'</font></a>';
   	html += '</td>';
   	html+='<td align="center">'+entryInfo['projectType']+'</td>';
   	html+='<td align="center">'+entryInfo['proManager']+'</td>';
   	html+='<td align="center">'+entryInfo['members']+'</td>';
   	html+='<td align="center">'+entryInfo['planStartTime'].substring(0,10)+'</td>';
   	html+='<td align="center">'+entryInfo['planEndTime'].substring(0,10)+'</td>';
   	html+='<td align="center">'+attType+'</td>';
   	html+='<td align="center">'+attEntryTime+'</td>';
   	html+='<td align="center">'+attExitTime+'</td>'; 
   	var isVisual;
   	if(entryInfo['isVisual']=='0'){
   		isVisual = "否";
   	}else if(entryInfo['isVisual']=='1'){
   		isVisual = "是";
   	}
   	html+='<td align="center">'+isVisual+'</td>'; 
   	html+='<td align="center">'+entryInfo['isEnd']+'</td>'; 
    html+='</tr>';
    <% j++;%>//每调用一次该方法，索引值加1

	return html;
}

function showProjectInfo(projectId){
	var strUrl="/pages/project/project!view.action?projectId="+projectId;
	var features="1000,800,project.view,um";
	var resultvalue = OpenModal(strUrl,features);
}

//获取所选复选框
function  GetSelIds()
{	
    var idList="";
    $("input[type='checkbox']").each(function(){
    	if($(this).is(":checked")){
		   idList+=","+$(this).val().split(",")[0];
    	}
    })
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

function ShowTitleModal(theURL,features){
	OpenModal(theURL,features);
	window.location.href="<%=request.getContextPath()%>/pages/um/userGroup.do";
} 

function del(){
	var idList=GetSelIds();
	 if(idList=="")
	 {   
		   alert("请选择需要删除的项目");
		   return false;
	 }
	if (confirm("确定要删除该项目吗"))
    {
		var strUrl="/pages/project/project!delete.action?projectId="+idList;
 		var returnValue=OpenModal(strUrl,"520,380,back.title.del,org"); 
 		refreshList();
    }
}


function add(){	
    //var strUrl="/pages/project/add_project.jsp";
    var strUrl="/pages/project/project!toAdd.action";
	var feature="1000,800,system.project.add,staffManager";
 	var returnValue=OpenModal(strUrl,feature);
 	//location="/nStraf/pages/project/project!toAdd.action";
 	refreshList();
	
}

function GroupUserMenu(groupcode){
	var strUrl="/pages/um/sysUserGroup!findAllUserByProject.action?groupId="+groupcode;
	var feature="520,380,grpInfo.updateTitle,um";
 	var returnValue=OpenModal(strUrl,feature);
}

function modify(){
	var idList=GetSelIds();
	var ids = idList.split(',');
	 if(idList=="")
  {   
	   alert("请选择需要修改的项目组");
	   return false;
  }
	 if(ids.length>1){
		 alert('一次只能修改一条项目组');
			return false;
		}
  var strUrl="/pages/project/project!modifyPage.action?projectId="+idList;
	var features="1000,800,user.leave.update,hols";
	var resultvalue = OpenModal(strUrl,features);
	refreshList();
}

function distribution(){
	var idList=GetSelIds();
	var ids = idList.split(',');
	 if(idList=="")
  	{   
	   alert("请选择需要分配人员的项目");
	   return false;
  	}
	 if(ids.length>1){
		 alert('一次只能操作一个项目');
			return false;
		}
	var strUrl="/pages/project/project!distribution.action?projectId="+idList;
	var feature="1000,880,grpInfo.updateTitle,um";
 	var returnValue=OpenModal(strUrl,feature);
 	//location.href="/nStraf/pages/project/project!distribution.action?projectId="+idList;
 	refreshList();
}

/*同步人员分配数据*/
function synchronize(){
	var strUrl="/pages/project/project!synchronizeUserProject.action";
	var feature="800,600,grpInfo.updateTitle,um";
	alert("同步需要较长时间，请耐心等待");
 	var returnValue=OpenModal(strUrl,feature);
}
function toExport(){
	var actionUrl = "<%=request.getContextPath()%>/pages/project/project!exportData.action?";
	var projectName = document.getElementById("projectName").value;
	var select=document.getElementById("isEnd");
	var index = select.selectedIndex; // 选中索引
	var isEndValue = select.options[index].value; // 选中值

	if(projectName!=''){
		actionUrl += "&projectName="+projectName;
	}
	if(isEndValue!=''){
		actionUrl += "&isEndValue="+isEndValue;
	}
	window.open(actionUrl,'newwindow',"toolbar=no,menubar=no ,location=no, width=700, height=400, top=100, left=100");	
	window.close();
}
</script>
	<body id="bodyid" leftmargin="0" topmargin="0">
		<s:form name="projectFrom"  action="project!list.action"  namespace="/pages/project" method="post" >
		<!-- 如果页面中用到了分页标签，请记得加上下面这两个隐藏标签 -->
			<input type="hidden" name="pageNum" id="pageNum" value="1" />
			<input type="hidden" name="pageSize" id="pageSize" value="20" />
	  <%@include file="/inc/navigationBar.inc"%>

		<table width="100%" cellSpacing="0" cellPadding="0" margin-top="20px"
					margin-button="20px">
					<tr height="50px">
						<td width="8%" align="right" class="input_label" style="  text-align: right; font-weight: 600; white-space: nowrap;">项目名称:</td>
						<td width="10%">
							<input  name="projectName" style="width:124px;height:22px;margin-left:5px;" type="text"
								id="projectName" size="40" maxlength="40" >
						</td>
						<td width="8%" align="right" class="input_label" style="  text-align: right; font-weight: 600; white-space: nowrap;">是否完结:</td>
						<td width="10%">
							<select id="isEnd" >
							<option value="" selected="selected"></option>
							<option value="1">是</option>
							<option value="0">否</option>
						</select>
						</td>
						<td>
							<div>
								<input style="margin-left:20px;" type="button" value="查询"  onclick="query()" >
							</div>
						</td>
					</tr>
		</table>

		<table width="100%" height="20" border="0" cellspacing="0" cellpadding="0" background="../../images/main/bgtop.gif">
			<tr>
				<td width="25" valign="middle">
					&nbsp;
					<img src="../../images/share/list.gif" width="14" height="16">
				</td>
				<td class="orarowhead">
					<s:text name="grpInfo.table2title"/>
				</td>
				<td	align="right" width="50%">
    				<tm:button site="2"></tm:button>
    			</td>
			</tr>
		</table>		
		
  <table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066" id=downloadList>
    <tr> 
      <td width="3%"  class="oracolumncenterheader" style="CURSOR: hand"> 
        <div align="center"> 选择</div>
      </td>
      <td  class="oracolumncenterheader"> 
		<div align="center">项目名称</div>
      </td>
      <td   class="oracolumncenterheader"> 
		<div align="center">项目类型</div>
      </td>
      <td  class="oracolumncenterheader"> 
		<div align="center">项目负责人</div>
      </td>
      <td width="6%" class="oracolumncenterheader"> 
		<div align="center">成员人数</div>
      </td>
      <td   class="oracolumncenterheader"> 
		<div align="center">计划开始时间</div>
      </td>
      <td  class="oracolumncenterheader"> 
		<div align="center">计划结束时间</div>
      </td>
      <td   class="oracolumncenterheader"> 
		<div align="center">考勤类型</div>
      </td>
      <td  class="oracolumncenterheader"> 
		<div align="center">考勤时间(进入)</div>
      </td>
      <td class="oracolumncenterheader"> 
		<div align="center">考勤时间(退出)</div>
      </td>
      <td class="oracolumncenterheader"> 
		<div align="center">是否虚拟项目</div>
      </td>
      <td class="oracolumncenterheader"> 
		<div align="center">是否完结</div>
      </td>
    </tr>
    <tbody id="formlist">
    <%
    int i=1;
    int index = 0;
    %>
   
    <s:iterator id="grp" value="#request.projects" status="status"> 
    <tr id="<%="tr"+ ++i%>" class="trClass<%=(index%2)%>" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)> 
       <td align="center" ><input type="checkbox" name="id" value='<s:property value="id"/>'/></td> 
      <td align="left"> 
      	<a href='javascript:showProjectInfo("<s:property value="id"/>")'><font color="#3366FF">
      	<s:property value="name"/> 
      	</font></a></td>
      <td align="center"> <s:property value="projectType"/> </td>
      <td align="center"> <s:property value="proManager"/> </td>
      <td align="center"> <s:property value="members"/> </td>
      <td align="center"> <s:date name="planStartTime" format="yyyy-MM-dd"/> </td>
      <td align="center"> <s:date name="planEndTime" format="yyyy-MM-dd"/> </td>
      <td align="center"> <s:property value="attendances[0].attendanceType"/></td>
      <td align="center"> <s:date name="attendances[0].entryTime" format="HH:mm:ss"/> </td>
      <td align="center"> <s:date name="attendances[0].exitTime" format="HH:mm:ss"/> </td>
      <td align="center">
      	 <s:if test="isVisual==0">否</s:if>
      	 <s:else>是</s:else>
      </td>
      <td align="center"> <s:property value='isEnd'/></td>
    </tr>
    <%
    index ++;
     %>
    </s:iterator> 
    <table bgcolor="#FFFFFF" width="100%" border="0" cellpadding="1"
			cellspacing="1" bgcolor="#FFFFFF">
			<tr bgcolor="#FFFFFF">
				<td>
					<table width="100%" cellSpacing="0" cellPadding="0">
						<tr>
							<td width="6%">
								
							</td>
							<td width="11%">
								
							</td>
							<td width="83%" align="right">
								<div id="pagetag">
									<tm:pagetag pageName="currPage" formName="projectFrom" />
								</div>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
</tbody>
  </table>
</s:form>		

	</body>
</html>
