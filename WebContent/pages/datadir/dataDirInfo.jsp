<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/calendar.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/xmlHtc.inc"%>
<%@ include file="/pages/common/loading.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<html>
<head>
	<title>addOrModify</title>
</head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script type="text/javascript" src="../../js/aa.js"></script>
<%@include file="/inc/pagination.inc"%>
<script type="text/javascript">
/*  function CloseWin(){
     window.close()
 } */
 
 function validateForm()
 {
 	var key = dataDirForm.key.value;
   var value = dataDirForm.value.value;
   var note = dataDirForm.note.value;
   var noteEn = dataDirForm.noteEn.value;
   if(key==""){
   		alert("项目主键不能为空")
   		return false
   }
   if(note==""){
   		alert("项目说明不能为空")
   		return false
   }
   if(noteEn==""){
   		alert("英文说明不能为空")
   		return false
   }
   window.returnValue=true;
   return true;
 }
 
 function  SubmitForm()
 {
   if(validateForm)
   {
       var action="<%=session.getAttribute("action")%>";
	   var parentid=dataDirForm.parentid.value;
	   //alert(parentid);
	   var id=dataDirForm.id.value;
	   //var id="<bean:write name ="sysDatadir" property="id"/>";
      
       if(dataDirForm.newAnOther==null)//修改
       {
       	 var strUrl="<%=request.getContextPath()%>/pages/datadir/dataDir!save.action?action="+action+"&parentid="+parentid+"&id="+id;
       	 
       }
       else //新增
       {
	   		var newAnOther=dataDirForm.newAnOther.checked;
	   		//alert(newAnOther);
	   	    var strUrl="<%=request.getContextPath()%>/pages/datadir/dataDir!save.action?newAnOther="+newAnOther;
	   	 //alert();
   		}
   		dataDirForm.action=	strUrl;
        dataDirForm.submit();
   }
 }
function setFlag()
{
	var flag = dataDirForm.flag.value;
	if( flag == "add")
		dataDirForm.newAnOther.checked = true;
}

</script>
<body id="bodyid"  leftmargin="0" topmargin="0" onLoad="setFlag()">
<s:iterator value="sysDatadir">
<form name="dataDirForm"  focus="note" method="post">
<input type="hidden" name="flag" value="<%=request.getParameter("flag") %>"/>
<input type="hidden" name="parentid" value='<s:property value="parentid" />'/>
<input type="hidden" name="id" value="<%=request.getParameter("id") %>" />;
<table width="90%" cellspacing="0" cellpadding="0" align="center" class="popnewdialog1">
  <tr> 
    <td> 
	 <fieldset class="jui_fieldset" width="100%"><!--condition-->
	      <legend>
             <s:if  test="id==null"> 
	           <s:text name="dataDir.title.add"/>
	           <input type="hidden" name="control" value="add">
			 </s:if><s:else> 
	           <s:text name="dataDir.title.modify" />
	           <input type="hidden" name="control" value="modify">
			 </s:else>
		  </legend> 
		  <br>
		  <font color="#FF0000">
				<html:messages id="msg" message="true" >
				   <s:property value="msg"/>
				</html:messages>
			</font>
		 <table align="center">
			<s:if test="dirPath!=null">
			 <tr> 
				<td width="100" align="right" height="17"><s:text name="dataDir.current.path" /><s:text name="label.colon"/></td>
				<td colspan="3" height="17"><input name="dirPath" type="text" size="30"  onKeyUp="this.value=this.value.replace(/(^\s*)|(\s*$)/g, '')" onafterpaste="this.value=this.value.replace(/(^\s*)|(\s*$)/g, '')"  value='<s:property value="dirPath"/>' maxlength="100" readonly></td>
			  </tr>
		  </s:if>		  
          <tr>
          	<td width="100" align="right">
          		<s:text name="dataDir.path"/>
          		<s:text name="label.colon"/>          	</td>
          	<td colspan="3" height="17">
          		<s:property value="path"/>          	</td>
          </tr>
          <tr>   
         	 <td width="100" align="right">
          		 <!--项目说明 -->
          		<s:text name="label.note"/><s:text name="label.colon"/>         	 </td>
         	 <td colspan="3">
           	 	<input name="note" type="text" size="30"  onKeyUp="this.value=this.value.replace(/(^\s*)|(\s*$)/g, '')" onafterpaste="this.value=this.value.replace(/(^\s*)|(\s*$)/g, '')" 
           	 	       value='<s:property value="note"/>' maxlength="50"><font color="#FF0000">*</font>         	 </td>
          </tr>
         
          <tr>   
          	<td width="100" align="right">
          		<!--英文说明 -->
          		<s:text name="label.note.en" /><s:text name="label.colon"/>          	</td>
            <td colspan="3">
				<input name="noteEn" type="text" size="30" onKeyUp="this.value=this.value.replace(/(^\s*)|(\s*$)/g, '')" onafterpaste="this.value=this.value.replace(/(^\s*)|(\s*$)/g, '')"   
				       value='<s:property value="noteEn"/>' maxlength="50"><font color="#FF0000">*</font>			</td>
          </tr>
          
          
          <tr>   
         	<td width="100" align="right">
         	    <!--项目主键 -->
         	 	<s:text name="label.key"/><s:text name="label.colon"/>          	</td>
            <td colspan="3">
			<input name="key" type="text" size="30"  onkeyup="value=value.replace(/[\W]/g,'') " onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))"  
			       value='<s:property value="key"/>' maxlength="30"><font color="#FF0000">*</font>			</td>
          </tr>
          
          <tr> 
            <td width="100" align="right">
            	<!--项目值-->
            	<s:text name="label.value"/><s:text name="label.colon"/>            </td>
           <td colspan="12" nowrap bgcolor="#FFFFFF"><div align="left"><textarea name="value" type="text" size="50" maxlength="900"  id="value" rows="8"> <s:property value="value"/></textarea>	</div></td>
          </tr>
        </table>
     </fieldset>
   </td>
  </tr>
</table>
<br/>
<table width="90%" align="center" cellSpacing="0" cellPadding="0">
	<tr>
		<td>
			<fieldset class="jui_fieldset" width="100%">
			<!--tips -->
			<legend><s:text name="tips.title"/></legend>
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
  					<tr bgcolor="#FFFFFF">
	      				<td  align="left"> 
		  					<s:text name="dataDirInfo.tips.content"/>
       				    </td>
  					</tr>
				</table>
              </fieldset>
            </td>
           </tr>
</table>
<br>

 <table width="90%" cellSpacing="0" cellPadding="0">
     <tr>
        <td>
        <!--   <fieldset class="jui_fieldset" width="100%">
          <legend><s:text name="label.operate"/></legend>-->
		  <table width="100%" border="0" cellpadding="0" cellspacing="0">
  			<tr bgcolor="#FFFFFF">
	      		<td  align="center"> 
            		<input type="button" name="btnSave" value='<s:text name="button.ok"/>' class="MyButton"  image="../../images/share/yes1.gif" onClick="SubmitForm()">
            		<input type="button" name="btnClose" value='<s:text name="button.close"/>' class="MyButton"  image="../../images/share/f_closed.gif" onClick="closeModal()">
		    		<s:if test="id == null"> 
						<input type="checkbox" name="newAnOther" id="newAnOther" value="1">
						<label><s:text name="dataDir.label.addAnOther"/></label>
			 		</s:if>
        		 </td>
  			</tr>
		 </table>
		<!--  </fieldset>-->
  	 </td>
 	</tr>
</table>
</form>
</s:iterator>
<%@ include file="/inc/showActionMessage.inc"%>
</body>
</html>