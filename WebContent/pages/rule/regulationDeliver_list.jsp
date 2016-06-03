<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa"%>
<html>
	<head>
		<title>RegulationDeliver query</title>
	</head>
	<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
	<script type="text/javascript" src="../../js/tablesort.js"></script>
	<script type="text/javascript" src="../../calendar/fixDate.js"></script>
	<script type="text/javascript" src="../../js/aa.js"></script>
	<script type="text/javascript" src="../../js/jquery.js"></script>
	<%@ include file="/inc/pagination.inc"%>
	<script type="text/javascript"> 
	function query(){
		var pageNum = document.getElementById("pageNum").value;
		var termid = document.getElementById("termid").value;
		var type = document.getElementById("type").value;
		var status = document.getElementById("status").value;
		var actionUrl = "<%=request.getContextPath()%>/pages/rule/regulationDeliver!refresh.action?from=refresh"+"&termid="+termid+"&type="+type+"&status="+status+"&pageNum="+pageNum;
		actionUrl=encodeURI(actionUrl);
		var method="setHTML";
		<%int k = 0;%>
	   	sendAjaxRequest(actionUrl,method,pageNum,true);
	}


	function setHTML(entry,entryInfo){
		var html = "";
		html += '<tr id="tr" class="trClass<%=(k % 2)%>" oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)>';
		html += '<td nowrap align="center"><input type="checkbox" name="chkList" value="' + entryInfo["id"] + '" /></td>';
		html += '<td nowrap width="12%"><div align="center">';
			html += '<a href=javascript:show("' + entryInfo["id"] + '")>' + entryInfo["termid"] + '</a>';
		html += '</div></td>';
		//html += '<td nowrap width="12%"><div align="center">' + entryInfo["role"] + '</div></td>';
		html += '<td nowrap width="6%"><div align="center">'+entryInfo["type"]+'</div></td>';
		html += '<td nowrap width="6%"><div align="center">'+entryInfo["status"]+'</div></td>';
		html += '<td nowrap width="12%"><div align="center">' + entryInfo["date"] + '</div></td>';
		html += '</tr>';
		<%k++;%>;
		return html;
	}
	
	function show(ids){
		var strUrl="/pages/rule/regulationDeliver!show.action?ids="+ids;
		var features="600,500,operInfo.updateTitle,um";
		var resultvalue = OpenModal(strUrl,features);
		 query();
	}

	function  SelAll(chkAll){
	 	var   em=document.all.tags("input");
	  	for(var  i=0;i<em.length;i++){
	  		if(em[i].type=="checkbox")
	    		em[i].checked=chkAll.checked
		}
	}

	function regulationDeliver() {
		//alert("in");
		var strUrl="/pages/rule/regulationDeliver!deliver.action";
		var features="600,500,operInfo.updateTitle,um";
		var resultvalue = OpenModal(strUrl,features);
		query();
	}  
</script>
	<body id="bodyid" leftmargin="0" topmargin="0">
		<form name="regulationDeliverForm" namespace="/pages/rule"
			action="regulationDeliver!query.action" method="post">
			<input type="hidden" name="pageNum" id="pageNum" value="1" />
			<%@include file="/inc/navigationBar.inc"%>
			<input type="hidden" name="menuid"
				value="<%=request.getParameter("menuid")%>">
			<table width="100%" height="23" border="0" cellspacing="0"
				cellpadding="0" class="bgbuttonselect">
				<td>
					<fieldset width="100%">
						<legend>
							<s:text name="queryCondition.query" />
						</legend>
						<table width="100%">
							<tr>
								<td width="10%" align="right">
									<s:text name="rule.termid" />
									<s:text name="label.colon" />
								</td>
								<td width="10%">
									<input name="termid" type="text" id="termid" class="MyInput">
								</td>
								<td width="10%" align="right">
									<s:text name="rule.applyType" />
									<s:text name="label.colon" />
								</td>
								<td width="10%">
									<tm:tmSelect name="type" id="type" selType="dataDir"
										beforeOption="all" path="ruleMgr.applyType" />
								</td>
								<td width="10%" align="right">
									<s:text name="rule.reguStatus" />
									<s:text name="label.colon" />
								</td>
								<td width="10%">
									<tm:tmSelect name="status" id="status" selType="dataDir"
										beforeOption="all" path="ruleMgr.reguStatus" />
								</td>
								<td width="10%" align="right">
									&nbsp;
								</td>
								<td width="15%" align="right">
									&nbsp;
								</td>
								<td width="15%" align="right">
									<tm:button site="1" />
								</td>
							</tr>
						</table>
					</fieldset>
				</td>
			</table><br/>
			<table width="100%" border="0" cellpadding="1" cellspacing="1"
				bgcolor="#000066">
				<tr bgcolor="#FFFFFF">
					<td>
						<table width="100%" cellSpacing="0" cellPadding="0">
							<tr>
								<td width="6%">
									<div align="center">
										<input type="checkbox" name="all" id="chkAll" value="all"
											onClick="SelAll(this)">
									</div>
								</td>
								<td width="11%">
									<div align="left">
										<label for=chkAll>
											<s:text name="operInfo.checkall" />
										</label>
									</div>
								</td>
								<td width="83%" align="right">
									<div id="pagetag">
										<tm:pagetag pageName="currPage"
											formName="regulationDeliverForm" />
									</div>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
			<table width="100%" border="0" cellpadding="1" cellspacing="1"
				bgcolor="#000066" id="downloadList">
				<tr>
					<td nowrap width="4%" class="oracolumncenterheader">
						<s:text name="label.select" />
					</td>
					<td nowrap width="12%" class="oracolumncenterheader">
						<div align="center">
							<s:text name="fixplan.termid" />
						</div>
					</td>
					<!--  <td nowrap width="12%" class="oracolumncenterheader">
						<div align="center">
							<s:text name="rule.list.regulation" />
						</div>
					</td>-->
					<td nowrap width="6%" class="oracolumncenterheader">
						<div align="center">
							<s:text name="rule.applyType" />
						</div>
					</td>
					<td nowrap width="6%" class = "oracolumncenterheader">
						<div align="center">
							<s:text name="rule.reguStatus"/>
						</div>
					</td>
					<td nowrap width="12%" class="oracolumncenterheader">
						<div align="center">
							<s:text name="rule.list.date" />
						</div>
					</td>
				</tr>
				<tbody name="formlist" id="formlist">
					<s:iterator value="regulationDeliverList" id="regulationDeliver"
						status="row">
						<s:if test="#row.odd == true">
							<tr id="tr" class="trClass0" oriClass="" onMouseOut=TrMove(this)
								onMouseOver=TrMove(this)>
						</s:if>
						<s:else>
							<tr id="tr" class="trClass1" oriClass="" onMouseOut=TrMove(this)
								onMouseOver=TrMove(this)>
						</s:else>
						<td nowrap align="center">
							<input type="checkbox" name="chkList"
								value='<s:property value="id"/>' />
						</td>
						<td nowrap width="12%">
							<div align="center">
								<s:property value="termid" />
							</div>
						</td>
						<!--  <td nowrap width="12%">
							<div align="center">
								<s:property value="role" />
							</div>
						</td>-->
						<td nowrap width="6%">
							<div align="center">
								<tm:dataDir beanName="regulationDeliver" property="type" path="ruleMgr.applyType"/>
							</div>
						</td>
						<td nowrap width="6%">
							<div align="center">
							<tm:dataDir beanName="regulationDeliver" property="status" path="ruleMgr.reguStatus"/>
							</div>
						</td>
						<td nowrap width="12%">
							<div align="center">
								<s:property value="date" />
							</div>
						</td>
						</tr>
					</s:iterator>
				</tbody>
			</table>
		</form>
	</body>
</html>

