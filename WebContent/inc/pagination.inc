<script language="javascript">
/**
 *This method is to send ajax request to the given action
 * actionUrl: URL
 * method:the method to set the html of the list
 * pageNum:the number of the current page
 * ifPage:whether to paginate or not
 * setRecordCount:whether to show recordCount or not
**/
function sendAjaxRequest(actionUrl,method,pageNum,ifPage,setRecordCount){
   try{
   	$("#formlist").empty();
   }catch(e){
   }
  
   $.ajax({
     type:"post",
     url:actionUrl,
	 dataType:"json",
	 cache: false,
	 async:true,
	 success:function(data){
	var pageCount= data.pageCount;
	var recordCount = data.recordCount;
	var data2=data.jsonObj;
	   parseData(data2,method,pageNum,ifPage,setRecordCount,pageCount,recordCount);
		
		 }
   });   
}

/**
 *This method is to parse the data received
 *data:the data received
 *method:
 *pageNum:
 **/
function parseData(data,method,pageNum,ifPage,setRecordCount,pageCount,recordCount)
{
	var content = "";
	
	var fullMethdName = method+"(entry,entryInfo)";		
	  $.each(data,function(entry,entryInfo){
	 		
	  		content += eval(fullMethdName);
	  		
	  		
	  });
	 

	  if(ifPage)//如果要分页
	  {
	     $("#pagetag").html( getPageTagHTML(pageCount,recordCount,pageNum));
	  }
	  
	  $("#formlist").html(content);
	
 
	  if(setRecordCount)//如果要显示"共有记录数"
	  {
	 
	   	$("#recordCount").html(getRecordCountHTML(recordCount));
	  }
}

/**
 *显示分页标签
 **/
function getPageTagHTML(pageCount,recordCount,pageNum)
{
	var pagetag = "";
	pagetag += '<table border="0" cellspacing="1" cellpadding="0" class="Pagination_table">';
	pagetag += '<input type="hidden" name="pageSize" value="10">'
	pagetag += '<td nowrap class="Pagination">'
	
	pageNum = parseInt(pageNum);
	pageCount = parseInt(pageCount);
	
	//上一页
	if(pageNum>1 && pageNum<= pageCount)
		pagetag += '<INPUT class="BtnDisable" type="button" value="<s:text name="page.nav.forward"/>" name=lastpage onClick="MovePrevious()" >'
    else if(pageNum>pageCount)		
        pagetag += '<INPUT class="BtnDisable" type="button" value="<s:text name="page.nav.forward"/>" name=lastpage onClick="MovePrevious()" >'
	else 
		pagetag += '<INPUT class="BtnDisable" type="button" value="<s:text name="page.nav.forward"/>" name=lastpage  disabled="disabled" onClick="MovePrevious()">'
	
	pagetag += '</td>'
	pagetag += '<td nowrap class="Pagination">'
	
	//下一页
	if(pageNum>=1 && pageNum<pageCount)
		pagetag += '<INPUT class="Btn" type="button" value="<s:text name="page.nav.backward"/>" name="nextpage" onClick="MoveNext()"></td>'
	else
		pagetag += '<INPUT class="Btn" type="button" value="<s:text name="page.nav.backward"/>" name="nextpage" onClick="MoveNext()"  disabled="disabled"></td>'
	
	pagetag += '</td>'
	pagetag += '<td nowrap class="Pagination"><s:text name="page.nav.total"/>'+recordCount+'<s:text name="page.nav.size"/>'+pageCount+'<s:text name="page.nav.page"/></td>'
	pagetag += '<td nowrap class="Pagination"><s:text name="page.nav.go"/>'
	pagetag += '<input type="text" name="gotoPageNo" id="gotoPageNo" maxlength="8" class="textbox" size="2" value="'+pageNum+'" onfocus="this.select()"  onkeyup="this.value=this.value.replace(/[^\\d+]/g,\'\')" onKeyPress="if(window.event.keyCode==13)GotoPage()">'
	pagetag += '<s:text name="page.nav.page"/>'
	pagetag += '<INPUT class="Btn" type="button" value="GO" name="GO" onclick="GotoPage()">'
	pagetag += '</td>'
	pagetag += '</tr>'
	pagetag += '</table>'
 
    return pagetag;
}

/***
* 显示：共有记录数
*/
function getRecordCountHTML(recordCount)
{
	var recordCountHTML = "";
		recordCountHTML += '<tr><td height="22"><s:text name="page.nav.total"/>'
	    recordCountHTML += recordCount;
	    recordCountHTML += '</td>'
	    recordCountHTML += '<td width="21%" height="22" valign="middle">'
	    recordCountHTML += '<div align="center"> </div>'
	    recordCountHTML += ' </td>'
	    recordCountHTML += '<td height="22">&nbsp;&nbsp; '
	    recordCountHTML += ' </td>'
	    recordCountHTML += ' </tr>'
	    
	    return recordCountHTML;
}



</script>