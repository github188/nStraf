<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@ include file="/inc/xmlHtc.inc"%>
<%@ include file="/pages/common/loading.jsp"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<%@ page import="cn.grgbanking.feeltm.util.HtmlUtil,java.util.*"%>

<html>
<head>
	<title></title>
</head>
<link href="../../css/css_v2.css" type=text/css rel=stylesheet>
<script language="javascript" src="../../js/jquery-1.11.0.js"></script>
<script type="text/javascript">
function MoveUp(){
	    if(null == $('#lstItem').val()){
	    	alert('请选择一项');
	    	return false;
	    }
	    //选中的索引,从0开始
	    var optionIndex = $('#lstItem').get(0).selectedIndex;
	    //如果选中的不在最上面,表示可以移动
	    if(optionIndex > 0){
	    	$('#lstItem option:selected').insertBefore($('#lstItem option:selected').prev('option'));
	    }

    /*for (i=1; i<menuInfoSortForm.lstItem.all.length; i++)
	{
        if (menuInfoSortForm.lstItem.item(i).selected) {

          st = menuInfoSortForm.lstItem.item(i-1).value ;
          menuInfoSortForm.lstItem.item(i-1).value = menuInfoSortForm.lstItem.item(i).value;
          menuInfoSortForm.lstItem.item(i).value = st;

          st = menuInfoSortForm.lstItem.item(i-1).text ;
          menuInfoSortForm.lstItem.item(i-1).text = menuInfoSortForm.lstItem.item(i).text;
          menuInfoSortForm.lstItem.item(i).text = st;

          menuInfoSortForm.lstItem.item(i-1).selected = true;
          menuInfoSortForm.lstItem.item(i).selected = false;
        }
    }*/
}

function MoveDown() {
	if(null == $('#lstItem').val()){
	    alert('请选择一项');
	    return false;
	}
    //索引的长度,从1开始
    var optionLength = $('#lstItem')[0].options.length;
    //选中的索引,从0开始
    var optionIndex = $('#lstItem').get(0).selectedIndex;
    //如果选择的不在最下面,表示可以向下
    if(optionIndex < (optionLength-1)){
    	$('#lstItem option:selected').insertAfter($('#lstItem option:selected').next('option'));
    }

    /*for (i=menuInfoSortForm.lstItem.all.length-2; i>=0; i--) {
        if (menuInfoSortForm.lstItem.item(i).selected) {

          st = menuInfoSortForm.lstItem.item(i+1).value ;
          menuInfoSortForm.lstItem.item(i+1).value = menuInfoSortForm.lstItem.item(i).value;
          menuInfoSortForm.lstItem.item(i).value = st;

          st = menuInfoSortForm.lstItem.item(i+1).text ;
          menuInfoSortForm.lstItem.item(i+1).text = menuInfoSortForm.lstItem.item(i).text;
          menuInfoSortForm.lstItem.item(i).text = st;

          menuInfoSortForm.lstItem.item(i+1).selected = true;
          menuInfoSortForm.lstItem.item(i).selected = false;
        }
    }*/
}

function SubmitForm(){
    var ids = "";
    var options = $('#lstItem')[0].options;
    for(var i=0; i<options.length;i++){
		ids = ids + ',' + options[i].value;
    }
    if(ids.length>0){
		ids=ids.substring(1);
		$("#idList").val(ids);
     	return true;
	}else{
		alert("<s:text name="sort.save.noItems"/>");
		return false;
	}
    /*var st = "";
    for (i=0; i<menuInfoSortForm.lstItem.all.length; i++) {
        st += menuInfoSortForm.lstItem.item(i).value + ",";
    }
    
	  if(st.length>0)
	  {
	     st=st.substring(0,st.length-1);
	  	 menuInfoSortForm.idList.value = st;
       window.returnValue=true
       return true;
	  }else{
	     alert("<s:text name="sort.save.noItems"/>");
	     return false;
     }*/
     
}
 /* function CloseWin(){
	 if(navigator.userAgent.indexOf("MSIE") != -1){//ie版本浏览器
     	window.close();
	 }else{
		parent.close();
	 }
 } */
</script>
<body id="bodyid"  leftmargin="10" topmargin="10" style="overflow-x:hidden">	
<form action="<%=request.getContextPath() %>/pages/menu/MenuInfoSort!saveSort.action" name="menuInfoSortForm" method="post" onsubmit="return SubmitForm()">

<table width="100%" cellspacing="0" cellpadding="0">
  <tr> 
    <td> 
	    <fieldset class="jui_fieldset" width="100%"><!--condition-->
	      <legend><s:text  name="menuInfo.title.sort"/></legend> 
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td width="80%" align="center">
                    <select name="lstItem" id="lstItem" multiple style="width: 80%; height: 180;">
                     
						<s:iterator value="menuInfoLst">
							<option  value='<s:property value="menuid"/>'>
							
							<s:property value="menuitem"/></option>
						</s:iterator>
                    </select>
                  </td>
                  <td width="20%">
                  	<input type="button" name="btnup" value='<s:text name="button.up"/>' class="MyButton"     image="<%=request.getContextPath()%>/images/share/up.png"  onClick="MoveUp()">
                  	<br>
                    <br>
                    <input type="button" name="btndown" value='<s:text name="button.down"/>' class="MyButton"  image="<%=request.getContextPath()%>/images/share/down.png" onClick="MoveDown()"></td>
                </tr>
              </table>
     </fieldset>
   </td>
  </tr>
</table>
<br>
<table width="100%" cellSpacing="0" cellPadding="0">
  <tr>
    <td>
      <fieldset class="jui_fieldset" width="100%">
        <!--operate -->
        <legend><s:text name="label.operate"/></legend>
        <table width="100%" border="0" cellpadding="0" cellspacing="0">
          <tr bgcolor="#FFFFFF">
        	  <td  align="center"> 
              <input type="submit" name="btnSave" value='<s:text name="button.submit"/>' class="MyButton"  image="<%=request.getContextPath()%>/images/share/save.gif">
              <input type="button" name="btnClose" value='<s:text name="button.close"/>' class="MyButton"  image="<%=request.getContextPath()%>/images/share/f_closed.gif" onclick="closeModal()">
            </td>
          </tr>
        </table>
      </fieldset>
    </td>
  </tr>
</table>
<br>
<table width="100%" cellSpacing="0" cellPadding="0">
  <tr>
    <td>
      <fieldset class="jui_fieldset" width="100%">
	      <!--tips -->
        <legend><s:text name="tips.title"/></legend>
        <table width="100%" border="0" cellpadding="0" cellspacing="0">
          <tr bgcolor="#FFFFFF">
        	  <td  align="left"> 
        		  <s:text name="sort.tips.content"/>
            </td>
          </tr>
        </table>
      </fieldset>
    </td>
  </tr>
</table>
<input type="Hidden" name="idList" id="idList" value="">
</form>
</body>
</html>