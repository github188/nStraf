 var html="";
 var pageCount=0;
 var recordCount=0;

function getPageTagHTML()
{
	var pageNum = document.getElementById("pageNum").value;

	var pagetag = "";
	pagetag += '<table border="0" cellspacing="1" cellpadding="0" class="Pagination_table">';
	pagetag += '<input type="hidden" name="pageSize" value="10">'
	pagetag += '<td nowrap class="Pagination">'
	
	//last page
	if(pageNum>1 && pageNum<= pageCount)
		pagetag += '<INPUT class=BtnDisable type=button value="<s:property value="%{getText('page.nav.forward')}"/>" name=lastpage onClick="MovePrevious()" >'
	else
		pagetag += '<INPUT class=BtnDisable type=button value="<s:property value="%{getText('page.nav.forward')}"/>" name=lastpage  disabled="disabled" onClick="MovePrevious()">'
	
	pagetag += '</td>'
	pagetag += '<td nowrap class="Pagination">'
	
	//next page
	if(pageNum>=1 && pageNum<pageCount)
		pagetag += '<INPUT class=Btn type=button value="<s:property value="%{getText('page.nav.backward')}"/>" name=nextpage onClick="MoveNext()"></td>'
	else
		pagetag += '<INPUT class=Btn type=button value="<s:property value="%{getText('page.nav.backward')}"/>" name=nextpage onClick="MoveNext()"  disabled="disabled"></td>'
	
	pagetag += '</td>'
	pagetag += '<td nowrap class="Pagination"><s:property value="%{getText('page.nav.total')}"/>'+recordCount+'<s:property value="%{getText('page.nav.size')}"/>'+pageCount+'<s:property value="%{getText('page.nav.page')}"/></td>'
	pagetag += '<td nowrap class="Pagination"><s:property value="%{getText('page.nav.go')}"/>'
	pagetag += '<input type="text" name="gotoPageNo" id="gotoPageNo" maxlength="8" class=textbox size="2" value="'+pageNum+'" onfocus="this.select()" onKeyPress="if(window.event.keyCode==13) GotoPage()">'
	pagetag += '<s:property value="%{getText('page.nav.page')}"/>'
	pagetag += '<INPUT class=Btn type=button value=GO name=GO onclick="GotoPage()">'
	pagetag += '</td>'
	pagetag += '</tr>'
	pagetag += '</table>'
 
    return pagetag;
}