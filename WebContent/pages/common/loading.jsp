<!---------------- loading... start -------------------------------------->
<div id="loading__" style="visibility:hidden;border:5px ridge #CCFFFF; position: absolute; width: 345px; height: 100px; z-index: 9999; left: 50px; top: 100px; padding-left:4px; padding-right:4px; padding-top:1px; padding-bottom:1px; background-color:#EFEFEF">
	<table border="0" width="100%" id="table1" style="border-collapse: collapse" height="100%" cellpadding="0">
		<tr>
			<td width="74">
			<p align="center">
			<img border="0" src="<%=request.getContextPath()%>/images/common/loading_info.gif" width="40" height="40"></td>
			<td>
			<p align="center"><font color="#004D00" size="2">Loading...,Please Wait.</font></td>
		</tr>
	</table>
</div>
<script language="javascript">
function loadingShow(){
	var load = document.all("loading__");
	var w = 345;
	var h = 100;
  var leftstation = screen.width/2 - w/2;
  if(leftstation<0){
  	leftstation = 0;
  }
  var topstation = screen.height/2 - h/2;
  if(topstation<0){
  	topstation=0;
  }
	load.style.left = leftstation;
	load.style.top = topstation;
	load.style.visibility = "visible";
}
function loadingHide(){document.all("loading__").style.visibility = "hidden";}
</script>
<!---------------- loading... end -------------------------------------->