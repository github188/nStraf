<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ taglib uri="http://ajaxanywhere.sourceforge.net/" prefix="aa" %>
<html>
<head>
	<title>test</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>

</head>
<script language="javascript" src="../../js/tablesort.js"></script>
<script language="javascript" src="js/geogPages.js"></script>
<script language="javascript" src="../../js/Validator.js"></script>
<script language="javascript" src="../../js/aa.js"></script>
<script language="JavaScript" type="text/JavaScript">

	  <!-- 所要提交的表单 -->
		ajaxAnywhere.formName="geogAreaForm";
	 <!-- 所要刷新的区域 -->
		ajaxAnywhere.getZonesToReload = function(){
		return "formlist1";
		} 
	  ajaxAnywhere.handlePrevousRequestAborted = function(){
	  }
	  ajaxAnywhere.handleException   = function(){
	  }
	  ajaxAnywhere.handleHttpErrorCode  = function(){
	  }
    //隐藏loading图片
    ajaxAnywhere.showLoadingMessage = function(){};
    
function  GetSelIds(){
     var idList=""
	  var aa = document.getElementsByName("deleteAreaid");
    for (var i=0; i<aa.length; i++){
     if(aa[i].checked){ 
	 idList+=aa[i].value;
	 if (i<aa.length-1)
  		idList+=",";
	}
  }
  if(idList=="")
	    return ""
	 return idList
}

function deleteInfo(formName) {
var idList=GetSelIds();
  if(IsDeleteCheckValues(this,'deleteAreaid')){
  	document.forms[formName].action.value="delete";
 	doyou = confirm('<bean:message  key="back.deleteinfo" bundle="geog"/>'); //Your question.
  if (doyou == true){
  	var strUrl="/pages/geog/areaMgr.do?action=delete&deleteAreaid="+idList+"&parentid="+geogAreaForm.parentid.value
 	  var returnValue=OpenModal(strUrl,"520,380,back.title.del,org"); 	
	  submitInfo();
   	}
   }
	else{   
    alert('<bean:message  key="del.selectNone"/>');
	}
}

function modify(){
   var aa=document.getElementsByName("deleteAreaid");
   var id
   var j=0;
   for (var i=0; i<aa.length; i++){
   		if (aa[i].checked){
			id=aa[i].value;
			j=j+1;
		}
   }
	if (j==0)
 		alert('<bean:message  key="operator.update" />')
 	else if (j>1)
 		alert('<bean:message  key="operator.updateone" />')
	else{
		var strUrl="/pages/geog/areaMgr.do?action=modify&amp;areaid="+id;
		var features="600,400,operator.modify,geog";
		var retValue = OpenModal(strUrl,features);
		if(retValue!=null){
			submitInfo();
		}
	}
}

function del() {
	deleteInfo('geogAreaForm')
}

function add(){
	var retValue = OpenModal("/pages/geog/geogArea_insert.jsp","600,400,geogarea.addinfo,geog")
	if( retValue != null){
		submitInfo();
	}
}

function submitInfo()
{
	geogAreaForm.action.value="list";	
	ajaxAnywhere.submitAJAX();
}

</script>

<body id="bodyid"  leftmargin="0" topmargin="10">
<html:form method="post"  action="/pages/geog/areaMgr.do">
		<table width="100%" cellSpacing="0" cellPadding="0">
			<tr>
				<td>
					<fieldset class="jui_fieldset" width="100%">
						<legend>
							<bean:message key="label.operator" bundle="org"/>
						</legend>
						<table width="100%" border="0" cellpadding="0" cellspacing="0">
							<tr bgcolor="#FFFFFF">
								
            <td align="center">		
			  <html:hidden property="parentid" name="geogAreaForms"/>
			  <input name="action" type="hidden" readonly="true">
				 <tm:button  menuid="geog.menuid" type="session"/>	
								</td>
							</tr>
						</table>
					</fieldset>
				</td>
			</tr>
		</table>	
<br>
<aa:zone name="formlist1">
<table width="100%" height="20" border="0" cellpadding="0" cellspacing="0" background="../../images/main/bgtop.gif">
  <tr>
    <td width="25" valign="middle">&nbsp;<img src="../../images/share/list.gif" width="14" height="16"></td>
    <td class="orarowhead"><bean:message  key="geogarea.postiton" bundle="geog"/>： <bean:write  name="parentName"/></td>
  </tr>
</table>
<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066">
  <tr bgcolor="#FFFFFF"> 
  	<td>
    <table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr bgcolor="#FFFFFF"> 
    <td width="14%"> <div align="center">
	        <input type="checkbox" name="all"  id="all"   value="CheckAll"  onClick="checkAll(this,'deleteAreaid','geogAreaForm')">
		 <bean:message  key="operator.checkAll" bundle="geog"/>
      </div></td>
  
    <td width="75%">
    	<table width="100%" border="0" cellspacing="0" cellpadding="0">
    		<tr>
    			<td height="22"><div align="right"><bean:message  key="geogarea.areanum" bundle="geog"/>：

                  <bean:write  name="countAllNum"/></div></td>
    			<td width="21%" height="22" valign="middle">
    				<div align="center">
    					</div></td>
    			<td height="22"></td></tr></table></td>
  </tr>
</table>
	</td>
  </tr>
</table>

<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#000066" id=downloadList>
  <tr> 
    <td onClick="sortTable(downloadList,0)" class="oracolumncenterheader" style="CURSOR: hand" > 
      <bean:message  key="operator.checkAll" bundle="geog"/></td>
    <td  onclick="sortTable(downloadList,1)" class="oracolumncenterheader" style="CURSOR: hand"><bean:message key="label.label"/></td>
    <td onClick="sortTable(downloadList,2)" class="oracolumncenterheader" style="CURSOR: hand"><bean:message  key="geogarea.areaname" bundle="geog"/></td>
    <td onClick="sortTable(downloadList,3)" class="oracolumncenterheader" style="CURSOR: hand"><bean:message  key="geogarea.areaid" bundle="geog"/></td>
    <td onClick="sortTable(downloadList,4)" class="oracolumncenterheader" style="CURSOR: hand"><bean:message  key="geogarea.note" bundle="geog"/></td>
  </tr>
  <logic:notEqual name="geogAreaForms" property="parentid" value="Top_Parentid" > 
  <tr id="tr18" class="oracletdtwo" onClick=""> 
    <td width="8%"> <div align="left"> </div></td>
    <td colspan="4" > <div align="left"> <html:link page="/pages/geog/areaMgr.do?action=back"  paramId="parentid" paramName="geogAreaForms" paramProperty="parentid"> 
        <bean:message  key="operator.backup" bundle="geog"/> </html:link></div>
      <div align="center"></div>
      <div align="center"></div>
      <div align="center"></div>
      <div align="center"></div>
      <div align="center"></div></tr>
  </logic:notEqual> 
  <logic:present name="geogAreaList">
  <logic:iterate id="geogArea" name="geogAreaList" indexId="index"> 
  <tr  class="trClass<%=(index.intValue()%2)%>"  oriClass="" onMouseOut=TrMove(this) onMouseOver=TrMove(this)> 
    <td width="8%"> <div align="center"> 
        <input name="deleteAreaid" type="checkbox"  id="deleteAreaid"   value='<bean:write  name="geogArea" property="areaid"/>'>
      </div></td>
    <td width="15%" ><div align="center"> <logic:greaterThan name="geogArea" property="childnum"   value="0" > 
        <img src="../../images/share/foldericon.gif" > 
        </logic:greaterThan> <logic:equal  name="geogArea" property="childnum" value="0" > 
        <img src="../../images/share/modify.gif" > 
        </logic:equal> 
        <!--bean:message  key="operator.modify" bundle="org"/-->
      </div></td>
    <td width="15%"> <div align="center"> <html:link page="/pages/geog/areaMgr.do?action=query" paramId="parentid" paramName="geogArea" paramProperty="areaid"><bean:write  name="geogArea" property="name"/> 
        </html:link> </div></td>
    <td width="15%"> <div align="center"><bean:write  name="geogArea" property="areaid"/></div></td>
    <td width="15%"> <div align="center"><bean:write  name="geogArea" property="note"/></div></td>
  </tr>
  </logic:iterate> 
  </logic:present>
</table>
</aa:zone>
</html:form>
</body>
</html>