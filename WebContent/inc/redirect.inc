<%@ page contentType="text/html; charset=UTF-8" %>
<script language="javascript">
function redirect(actionto){
		var actionUrl = "/nStraf/pages/menu/MenuInfo!getParentMenuInfo.action?actionto="+actionto;
		actionUrl=encodeURI(actionUrl);
		$.ajax({
			url : actionUrl,
			data : {},
			type : 'POST',
			dataType : 'json',
			timeout : 30000,
			success : function(data) {
				 self.location="<%=request.getContextPath()%>"+actionto+"?menuid="+data.MenuId; 
				 var flag = self.parent.frames["topFrame"].trueOrFalse(data.topMenuOrder);//flag = true;需要跳转，flag = false;不需要跳转
				 if(flag){
					 self.parent.frames["leftFrame"].location = "<%=request.getContextPath()%>/login!leftMenu.action?menuid="+data.topMenuId+"&open="+data.leftContentOrder+"&changeBg="+data.leftMenuOrder;
				 }
				 else{
					 self.parent.frames["leftFrame"].redirectOriginal(data.leftContentOrder,data.leftMenuOrder);
				 }
				 self.parent.frames["topFrame"].changeTdBg(data.topMenuOrder); 
			},
			error : function(data, data1) {
				alert("跳转失败!");
			}
		});
	}
	</script>