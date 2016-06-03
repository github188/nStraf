<%@ page contentType="text/html; charset=UTF-8" %>


<%@page import="java.util.*"%>
<%@page import="oracle.jdbc.driver.Const"%>
<%@page import="com.esri.arcgis.geometry.Path"%>
<%@ include file="/inc/expire.inc"%>
<%@ include file="/inc/htc.inc"%>
<%@ include file="/inc/taglib.inc"%>
<%@ include file="/inc/dialog.inc"%>
<%@taglib uri="/struts-tags" prefix="s" %>

<%
	String id="";
	id=request.getParameter("id");
	System.out.println(id);
	String showFlag="";
	showFlag=request.getParameter("showFlag");
	
	//String opType = request.getParameter("opType");
	String showid=request.getParameter("showid");
	if(showid!=null&&!showid.equals("")){
		id=(showid.split("@@"))[0];
		showFlag=(showid.split("@@"))[1];
	}
	System.out.println(id);
	System.out.println("flag:"+showFlag);
 %>
<html>
<head>



<link href="../../css/css_v2.css" type="text/css" rel="stylesheet">
<link href="../../css/htc.css" rel="stylesheet" type="text/css">
<style type="text/css">
body,div,span,h1,h2,h3,h4,ul,li,img,p,b,textarea,input,select,form,a,table,tr,td
	{
	margin: 0;
	padding: 0;
	list-style-type: none;
	font-size: 14px;
	color: #0e4871;
	z-index: inherit;
}
body {
	height: 100%;
	background-color: #ffffff;
}
.TacBlockDiv { /*环节块状显示Div*/
	width: 100%;
}

.TacBlock { /*环节块状Table*/
	width: 290px;
	float: left;
	margin: 5px 5px 0 0;
	border: #aad3f0 1px solid;
	height:140px;
}

.FFBlock { /*环节块状Table*/
	float: left;
	margin: 10px;
}

.FFBlock td {
	padding: 3px;
	white-space: nowrap;
}

.TacBlock td {
	padding: 4px;
}

.ActBlock {
	width: 60px;
	float: left;
	margin: 0 30px 10px 0;
}

/*
新增select样式
*/
.tacSelect { /*tacinfoSelect CSS*/
	background-color: #ffffff;
	border: #a3bae9 1px solid;
	width: 216px;
}

.actformSelect {
	background-color: #ffffff;
	border: #a3bae9 1px solid;
	width: 373px;
}

.Select_200 {
	background-color: #ffffff;
	border: #a3bae9 1px solid;
	width: 200px;
}

.actinfoSelect { /*tacinfoSelect CSS*/
	background-color: #ffffff;
	border: #a3bae9 1px solid;
	width: 120px;
}

.TabTable { /*环节tab CSS整体样式*/
	margin-top: 10px;
	margin-left: 16px;
}

.TabBg { /*环节tab背景样式*/
	background-repeat: repeat-x;
	background-position: 0px -477px;
	height: 24px;
}

.tabNav { /*环节tab导航菜单一级菜单整体样式*/
	margin-top: 1px;
	float: left;
	height: 24px;
}

.tabNav li { /*环节tab导航菜单一级菜单列表样式*/
	width: 103px;
	height: 24px;
	float: left;
	position: relative;
	margin: 0px;
	padding: 0px;
	overflow:hidden;
}

.tabNav li a {
	position: relative;
	background-image: url(../../images/Skin.gif);
	background-position: 0 -544px;
	margin-right: 0px;
	display: block;
	width: 103px;
	height: 24px;
	padding-top: 6px;
	text-align: center;
	cursor: pointer;
}

.tabNav li a:hover {
	text-decoration: none
	height:24px;
}

.tabNav li a.on {
	background-position: 0 -512px;
	font-weight: bold;
	height:24px;
}

.tabNav li a.on:hover {
	text-decoration: none;
	height:24px;
}

.tabBoder { /*环节tab对应页边框样式*/
	border-bottom: 1px solid #7b99bf;
	border-left: 1px solid #7b99bf;
	border-right: 1px solid #7b99bf;
}
.tabBoderTop { /*环节tab对应页边框样式*/
	border-bottom: 1px solid #7b99bf;
	height:24px;
}
/*
新增button样式
*/
.btn_2_14 { /*显示两个～三个汉字的按钮样式14px*/
	background: url(../../images/Skin.gif);
	border: none;
	background-position: -2px -267px;
	background-repeat: no-repeat;
	background-color: Transparent;
	width: 60px;
	height: 22px;
	text-align: center;
	font-size: 14px;
}

.btn_2_14m { /*显示多个汉字的按钮样式14px*/
	background: url(../../images/Skin.gif);
	border: none;
	background-position: -2px -291px;
	background-repeat: no-repeat;
	background-color: Transparent;
	width: 75px;
	height: 21px;
	text-align: center;
	font-size: 14px;
}

.btn_6_12 { /*显示四个～六个汉字的按钮样式12px*/
	background: url(../images/Skin.gif);
	border: none;
	background-position: -2px -291px;
	background-repeat: no-repeat;
	background-color: Transparent;
	width: 75px;
	height: 22px;
	text-align: center;
	font-size: 12px;
}


</style>
<script type="text/javascript" src="../../js/Validator.js"></script>
<script type="text/javascript" src="../../js/tablesort.js"></script>
<script language="javascript" src="../../js/jquery.js"></script>

<script language="JavaScript">
		function goto(obj,url,target)
		{
			
			var iCount = 0;
			var alist =  document.getElementById("ulTab").getElementsByTagName("a");
		    var liCount = alist.length;
		    for(var i=0; i<liCount; i++)
		    {
		        if(alist[i].firstChild.innerText == obj.firstChild.innerText)
					iCount = i;
				alist[i].className = "";
		    }
		    obj.className = "on";
		     var clientId=$("#id").val();
		    if(null!=clientId&&""!=clientId)
		    {
		    	url=url+"?id="+clientId;
		    	document.getElementById("td1").width="412";
		    }
		    else{
		    	document.getElementById("td1").width="100";
		    }
		     var showFlag=$("#showFlag").val();
		    if(null!=showFlag&&""!=showFlag)
		    {
		    	url=url+"&showFlag="+showFlag
		    }
		   	for(var i=0; i<liCount; i++)
		   		$("#actmainFrame"+i).hide();
		   	$("#actmainFrame"+iCount).show();
		   	if(url!=$("#actmainFrame"+iCount).attr("src"))
		   		$("#actmainFrame"+iCount).attr("src",url);
			
		    //$("#actmainFrame").attr("src",url);
		}
		
		function showLi(){
			document.getElementById("td1").width="412";
			document.getElementById("tab1").style.display="inline";
		    document.getElementById("tab2").style.display="inline";
		   // document.getElementById("tab3").style.display="inline";
		}
		
		//-->
		</script>
</head>
<body class="body" onLoad="goto(document.getElementById( 'task' ),'<%=request.getContextPath() %>/pages/atm/atminfo!add.action','actmainFrame')">
	<input type="hidden" name="id" id="id" value='<%=id==null?"":id%>'> 
	<input type="hidden" name="showFlag" id="showFlag" value="<%=showFlag==null?"":showFlag%>">	
	<table width="98%" height="98%" border="0" align='center' cellspacing="0" cellpadding="0">
	  <tr>
	    <td valign="top" height="23" ><table width="100%" border="0" cellspacing="0" cellpadding="0">
	      <tr>
	        <td  style="width:309" id="td1">
            <div class="tabNav">
	          <ul id="ulTab">
	          	 <li> <a href="javascript:void(0);" id="task" onClick="goto(this,'<%=request.getContextPath() %>/pages/atm/atminfo!add.action','actmainFrame')"><span class="TabbedPanelsTab">机器概况</span></a></li>
	             <li style="display:none;" id="tab1"> <a href="javascript:void(0);" id="order" onClick="goto(this,'<%=request.getContextPath() %>/pages/atm/atminfo!addDeviceConfig.action','actmainFrame')"><span class="TabbedPanelsTab">设备配置情况</span></a></li>
	             <li style="display:none;" id="tab2"> <a href="javascript:void(0);" id="order" onClick="goto(this,'<%=request.getContextPath() %>/pages/atm/atminfo!addMediaVersion.action','actmainFrame')"><span class="TabbedPanelsTab">机器介质版本</span></a></li>
               	
               </ul>
	          </div>
	          </td>
			<td  class="tabBoderTop">&nbsp;</td>
			</tr>
		</table>
		</td>
		</tr>
		<tr>
			<td valign="top" class="tabBoder">
			<iframe width="100%" height="100%" id="actmainFrame0" name="actmainFrame0" frameborder="0" scrolling="no"
			src=''></iframe>
			<iframe width="100%" height="100%" id="actmainFrame1" name="actmainFrame1" frameborder="0" scrolling="no"
			src='' style="display:none"></iframe>
			<iframe width="100%" height="100%" id="actmainFrame2" name="actmainFrame2" frameborder="0" scrolling="no"
			src='' style="display:none"></iframe>
			<!-- <iframe width="100%" height="100%" id="actmainFrame3" name="actmainFrame3" frameborder="0" scrolling="no"
			src='' style="display:none"></iframe>
				 -->
			</td>                                                                                
		
		
		</tr>
		</table>
		</td>
		</tr>
		</table>
		</tr>
		</table>
		</td>
		<td class="panlborderinner" width="1px"></td>
		<td class="panlborder" width="1px"></td>
		</tr>
		</table>
		</td>
		</tr>
		</table>
		</td>
			</tr>
		</table>
	</body>
</html>