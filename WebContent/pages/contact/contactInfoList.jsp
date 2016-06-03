<!--20110107 11:59-->
<%@page import="java.util.Date"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@page import="cn.grgbanking.feeltm.login.domain.UserModel"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa" %>
<html>
<head><title></title></head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../calendar/fixDate.js"></script>
<script type="text/javascript" src="../../js/aa.js"></script>
<script type="text/javascript" src="../../js/customTableSort.js"></script>
<%@ include file="/inc/pagination.inc"%>
<%@ include file="/inc/deptSelect.inc"%>

<script type="text/javascript"> 
			var firstLogin = true;
			var Num = 21,Num2 = 20;
			var pageNumTmp = 1;
			String.prototype.trim = function()
			{
				return this.replace(/(^\s*)|(\s*$)/g, "");
			}
		   	/* function document.onkeydown()
		    {	
				var e=event.srcElement;        	
				if(event.keyCode==13) 
				{
					document.getElementById("bodyid").focus();
					query();
				}
	  		} */
	 		function query(){
	 			
				var userId = document.getElementById("username").value.trim();
				var deptName = document.getElementById("deptName").value.trim();
				
				var groupName =  document.getElementById("groupName").value.trim();
				var pageNum = document.getElementById("pageNum").value;
				//排序
				var regulation =  $($activatedCSBtn).attr("name");
				var orderField =  $($activatedCSBtn).attr("orderField");
				if(pageNumTmp == 0 || pageNum == 1)
				{
					Num = 1;
				
				}
				else if(pageNumTmp == (pageNum+1))
				{
					Num = Num - Num2 - 20;
			
				}
				else if(pageNumTmp < pageNum)
				{
					Num+= (pageNum-pageNumTmp)*20-Num2;
				
				}
				else if(pageNumTmp > pageNum)
				{
					Num+= (pageNum-pageNumTmp)*20-Num2;
				
				}
				else if(pageNumTmp == pageNum)
				{
					Num-= Num2;
				}
				Num2 = 0;
				pageNumTmp = pageNum;
				var actionUrl = "<%=request.getContextPath()%>/pages/contact/contactAction!refresh.action?from=refresh&pageNum="+pageNum;
	            actionUrl += "&userId="+userId;
	            actionUrl += "&deptName="+deptName;
	            actionUrl += "&groupName="+groupName;
	            actionUrl += "&regulation="+ regulation +"&orderField="+orderField;
				actionUrl=encodeURI(actionUrl);
				var method="setHTML";
				<%int k = 0;%>
		   		sendAjaxRequest(actionUrl,method,pageNum,true);
		}
		
		function setHTML(entry,entryInfo){
			var html = '';
			var str = "javascript:show(\""+entryInfo["id"]+"\")";
			//var status=entryInfo["status"];
			var color='';
			var time = entryInfo["update"];
			html += '<tr class="trClass<%=k % 2%>" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>';
			html += '<td>';
			html += '<input type="checkbox" name="chkList" value="' + entryInfo["id"] + '" />';
			html += '</td>';
			//html += '<td align="center"><font color="#3366FF">' + Num + '</font></td>';
			html += '<td align="center">' +entryInfo["conName"] + '</td>';
			html += '<td align="center">' +entryInfo["deptName"] + '</td>';
			html += '<td align="center">' +entryInfo["groupName"] + '</td>';
			//html += '<td>' +'<font color=\''+color+'\'>'+entryInfo["status"] +'</font>' +'</td>';
			html += '<td align="center">' +entryInfo["conMobile"] + '</td>';
			html += '<td align="center">' +entryInfo["conTel"] + '</td>';
			html += '<td align="center">' +entryInfo["conEmail"] + '</td>';
			html += '<td align="center">' +entryInfo["updateManId"] + '</td>';
			html += '<td align="center">' +time.substr(0,19)+ '</td>';			
			
	  		html += '</tr>';
			Num++;
			Num2++;
	 		<%k++;%>;
			showdiv.style.display = "none";
			return html;
		}
		
		function  GetSelIds(){
			var idList="";
			var  em= document.getElementsByTagName("input");
			for(var  i=0;i<em.length;i++){
			  if(em[i].type=="checkbox"){
			      if(em[i].checked){
			        idList+=","+em[i].value.split(",")[0];
			  		}
			  } 
		 	} 
			if(idList=="") 
			   return "";
			return idList.substring(1);
		}
		function  SelAll(chkAll){
			var   em=document.getElementsByTagName("input");
			for(var  i=0;i<em.length;i++){
				if(em[i].type=="checkbox")
			    	em[i].checked=chkAll.checked
			}
		}
		
		function del() {

			var idList=GetSelIds();
		  	if(idList=="") {
			  	alert('<s:text name="errorMsg.notInputDelete" />');
				return false;
		  	}
			if(confirm('<s:text name="确认删除该记录吗？" />')) {
			 	var strUrl="/pages/contact/contactAction!delete.action?ids="+idList;
			 	OpenModal(strUrl,"600,380,contactInfo.delete,um")
			   	refreshList();
			}
		}

		function modify(){
			var aa=document.getElementsByName("chkList");
			var itemId;
			var j=0;
			for (var i=0; i<aa.length; i++){
			   if (aa[i].checked){
					itemId=aa[i].value;
					j=j+1;
				}
			}  
			if (j==0)
			 	alert('<s:text name="operator.update" />')
			 else if (j>1)
			 	alert('<s:text name="operator.updateone" />')
			else{
			  	var strUrl="/pages/contact/contactAction!edit.action?id="+itemId;
			  	var features="1000,550,tmlInfo.updateTitle,tmlInfo";
				OpenModal(strUrl,features);
				refreshList();
			}
		}
	function add(){
      
    	OpenModal('/pages/contact/contactAction!add.action','1000,500,contactInfo.addContactTitle,contactInfo');
    	refreshList();
	}
		
				
		function show(id) {
			var strUrl="/pages/contact/contactAction!show.action?ids="+id;
			var features="1000,500,transmgr.traninfo,watch";
			var resultvalue = OpenModal(strUrl,features);
		}
		function openURL(url){
			window.open(url);
	}
	
	function toExport(){
			var conName = document.getElementById("username").value.trim();
			var deptName = document.getElementById("deptName").value.trim();
			var groupName = document.getElementById("groupName").value.trim();
			var timeStamp = "<%=new Date().getTime() %>";
		    var actionUrl = "contactAction!exportData.action?timeStamp="+timeStamp;
			actionUrl += "&userId="+conName;
            actionUrl += "&deptName="+deptName;
            actionUrl += "&groupName="+groupName;
			actionUrl=encodeURI(actionUrl);
			window.open(actionUrl,'newwindow',"toolbar=no,menubar=no ,location=no, width=700, height=400, top=100, left=100");	
			//window.location.href=strReturn;
			//window.close();
			
		}
		
		function fromExport(){
			OpenModal('/pages/contact/importData.jsp','1000,500,contactInfo.importContactTitle,contactInfo');
		    refreshList();
		}
	</script>

<body id="bodyid" leftmargin="0" topmargin="0">
	<s:form name="contactInfoForm" namespace="/pages/contact"
		action="contactAction!list.action" method="post">
		<input type="hidden" name="pageNum" id="pageNum" value="1" />
  	    <input type="hidden" name="pageSize" id="pageSize" value="20" />
		<%@include file="/inc/navigationBar.inc"%>
		<input type="hidden" name="menuid"
			value="<%=request.getParameter("menuid")%>">
		<table width="100%" cellSpacing="0" cellPadding="0"
			>
			<tr>
				<td >
						<table width="100%" class="select_area">
							<tr>
								
								<tm:deptSelect deptId="deptName" userId="username" groupId="groupName" 
												isloadName="false" 
												deptHeadKey="---请选择部门---" deptHeadValue="全选" 
												userHeadKey="----请选择人员----" userHeadValue="全选" 
												groupHeadKey="---请选择项目名称---" groupHeadValue="全选" 
												labelDept="部门：" labelGroup="项目名称：" labelUser="姓名：" 
												deptLabelClass="align:right;class:input_label" 
												groupLabelClass="align:right;class:input_label" 
												userLabelClass="align:right;class:input_label"
												lableStyle="width:4%;" selectStyle="width:10%;" >
											</tm:deptSelect>
		 
								<%-- <td width="4%" align="center">部门:</td>
								<td width="10%" align="left"><s:select
										list="#request.deptMap" listKey="value" listValue="value"
										style="width:163px;" headerKey="全选" headerValue="全选"
										onchange="search()" id="deptName" name="deptName"></s:select>
								</td>

								<td width="4%" align="center">组别:</td>
								<td width="6%"><s:select list="#request.groupList"
										listKey="grpname" listValue="grpname" style="width:90px;"
										headerKey="全选" headerValue="全选" onchange="search()"
										id="groupName" name="groupName"></s:select></td>
								<td width="4%" align="center">姓名:</td>
								<td width="8%" align="left"><select name="username"
									id="username" style="width: 90px;"></select></td>
								<script type="text/javascript">

                search();
                </script> --%>
								<td width="1%"></td>
								<td width="15%">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <tm:button
										site="1"></tm:button>
								</td>
							</tr>
						</table>
					</td>
			</tr>
		</table>
		<br />
		<table width="100%" height="23" border="0" cellspacing="0"
			cellpadding="0" class="bgbuttonselect">
			<tr>
				<td class="orarowhead"><s:text name="operInfo.title" />
				</td>
				<td style="margin-left: 10px;"><input class="customSortBtn"  type="button" name="asc" value="部门" orderField="deptName" 
			    actionUrl="<%=request.getContextPath()%>/pages/contact/contactAction!refresh.action"  /></td>
			    <td style="margin-left: 10px;"><input class="customSortBtn" type="button"   value="更新时间" orderField="update" 
			    actionUrl="<%=request.getContextPath()%>/pages/contact/contactAction!refresh.action"  /></td>
				<td align="right" width="75%"><tm:button site="2"></tm:button>
				</td>
			</tr>
		</table>
		<div id="showdiv"
			style='display: none; z-index: 999; background: white; height: 39px; line-height: 50px; width: 42px; position: absolute; top: 236px; left: 670px;'>
			<img src="../../images/loading.gif">
		</div>
		
		<table width="100%" border="0" cellpadding="1" cellspacing="1"
			bgcolor="#000066" id=downloadList>
			<tr>
				<td nowrap class="oracolumncenterheader" width="2%"></td>
				<!--<td nowrap width="4%" class="oracolumncenterheader"><div
						align="center"><s:text name="form.number" /></div>
				</td> -->
				<td nowrap width="7%" class="oracolumncenterheader"><div
						align="center"><s:text name="label.userName" /></div>
				</td>
				<td nowrap width="10%" class="oracolumncenterheader"><div
						align="center"><s:text name="label.deptName" /></div>
				</td>
				<td nowrap class="oracolumncenterheader"><div
						align="center"><s:text name="lable.projectName" /></div>
				</td>
				<td nowrap width="11%" class="oracolumncenterheader"><div
						align="center"><s:text name="form.Mobile" /></div>
				</td>
				<td nowrap width="11%" class="oracolumncenterheader"><div
						align="center"><s:text name="form.Tel" /></div>
				</td>
				<td nowrap class="oracolumncenterheader"><div
						align="center"><s:text name="form.Email" /></div>
				</td>
				<td nowrap width="7%" class="oracolumncenterheader"><div
						align="center"><s:text name="form.updateMan" /></div>
				</td>
				<td nowrap width="15%" class="oracolumncenterheader"><div
						align="center"><s:text name="form.updateTime" /></div>
				</td>

			</tr>
			<tbody name="formlist" id="formlist" align="center">
				<s:iterator value="#request.contactlist" id="contactInfo"
					status="row">
					<s:if test="#row.odd == true">
						<tr id="tr" class="trClass0" oriClass="" onMouseOut=TrMove(this)
							onMouseOver=TrMove(this)>
					</s:if>
					<s:else>
						<tr id="tr" class="trClass1" oriClass="" onMouseOut=TrMove(this)
							onMouseOver=TrMove(this)>
					</s:else>
					<td nowrap align="center"><input type="checkbox"
						name="chkList" value="<s:property value='id'/>" />
					</td>
					<!-- <td align="center">
						<div align="center">
							<font color="#3366FF"> <s:property value="#row.count" />
							</font>
						</div></td> -->
					<td align="center"><s:property value="conName" />
					</td>
					<td><s:property value="deptName" />
					</td>
					<td align="center"><s:property value="groupName" />
					</td>
					<td align="center"><s:property value="conMobile" />
					</td>
					<td align="center"><s:property value="conTel" />
					</td>
					<td align="center"><s:property value="conEmail" />
					</td>
					<td align="center"><s:property value="updateManId" />
					</td>
					<td align="center"> <s:date name="update" format="yyyy-MM-dd HH:mm:ss"/>
					</td>
					</tr>
				</s:iterator>
			</tbody>
		</table>
		<table width="100%" border="0" cellpadding="1" cellspacing="1"
			bgcolor="#FFFFFF">
			<tr >
				<td>
					<table width="100%" cellSpacing="0" cellPadding="0">
						<tr>
							<!--<td width="6%"> 
				<div align="center"><input type="checkbox" name="chkList"  id="chkAll"   value="all"  onClick="SelAll(this)"></div> 
			</td>
			<td width="11%">
				<div align="left"><label for=chkAll><s:text name="operInfo.checkall" /></label></div>
				</td>-->
							<td width="83%" align="right">
								<div id="pagetag">
									<tm:pagetag pageName="currPage" formName="contactInfoForm" />
								</div></td>
						</tr>
					</table></td>
			</tr>
		</table>
	</s:form>
</body>
</html>

