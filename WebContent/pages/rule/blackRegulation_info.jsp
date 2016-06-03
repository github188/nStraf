<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<html> 
 	<head>
 		<title><s:text name="rule.title.info" /></title>
 		<script>
	function showTerm(id){
		var strUrl="/pages/rule/blackRegulation!show.action?ids="+id+"&menuid=<%=request.getParameter("menuid")%>";
		var features="600,500,rule.title.info,rule";
		var resultvalue = OpenModal(strUrl,features);
  		if(resultvalue!=null)
			query();
	}
 		</script>
 	</head>
	<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
	<script type="text/javascript" src="../../js/tablesort.js"></script>
	
<body>
<form action="/pages/rule/blackRegulation!info.action"   method="post">
	<s:iterator value="blackRegulation" id="blackRegulation">
	<table width="100%"  border="0" cellpadding="1" cellspacing="1" bgcolor="#000066" >
		<tr class="trClass0">
 		 <td nowrap  width="16%" align="right"><s:text name="rule.list.regulation" /><s:text name="label.colon"/></td>
 		 <td nowrap  width="18%"><s:property value="regulation" /></td>
 		 
 		 <td nowrap  width="16%" align="right"><s:text name="rule.list.date" /><s:text name="label.colon"/></td>
		 <td nowrap  width="17%">
		  	<s:date name="createDate" format="yyyy-MM-dd HH:mm:ss" />
		 </td>
		 
		 <td nowrap  width="16%" align="right"><s:text name="rule.list.moneyType" /><s:text name="label.colon"/></td>
		 <td nowrap  width="*">
		  <tm:dataDir beanName="blackRegulation" property="moneyType" path="ruleMgr.moneyType" />
		 </td>
 		</tr>
 		
 		<tr class="trClass1">
 			<td nowrap  width="16%" align="right"><s:text name="rule.list.denomination" /><s:text name="label.colon"/></td>
			<td nowrap  width="18%"><tm:dataDir beanName="blackRegulation" property="moneyDenomination" path="ruleMgr.moneyDenomination" /></td>
			<td nowrap  width="16%" align="right"><s:text name="rule.list.creater" /><s:text name="label.colon"/></td>
			<td nowrap  width="17%"><s:property value="createName"/></td>
			<td nowrap  width="16%" align="right"><s:text name="rule.termNum"/><s:text name="label.colon"/></td>
			<td nowrap  width="*" ><a href=javascript:showTerm('<s:property value="id" />')> <s:property value="termNum"/> </a></td>
 		</tr>
 	</table>
 	</s:iterator>
 </form>
  
</body>  
</html> 

