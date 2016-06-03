<style>
.MyCheckBox{
}
.MyButton{
}
.MyInput{
  border:1px solid #dddddd;
  backgroundColor:"#FEFEFE";
  color:"#666666";
}
.MySelect{
  border:1px solid #dddddd;
  backgroundColor:"#FEFEFE";
  color:"#666666";
  visibility: visible;
}

.CheckBoxTable{
	display:inline;
	cursor: hand;
	height:16;
}

.txtSel{
   font-size:9pt;
   color:green;
}
.txtNoSel{
   font-size:9pt;
   color:black;
}
.checkBoxImg{

}
.checkBoxDisable{
    font-size:9pt;
	color:#000000;cursor:default;
}
.ButtonTable{
	display:inline;
	cursor: hand;
	height:23;
	border:0;
	background-image: url(<%=request.getContextPath()%>/images_new/htc/btnbg.jpg);
	background:url(<%=request.getContextPath()%>/images_new/htc/btnbg.jpg);
}
.ButtonTable_td{
	height:23;
	padding-top:0;
	border:0;
	background-image: url(<%=request.getContextPath()%>/images_new/htc/btnbg.jpg);
	background-repeat: repeat-x;
	cursor:hand;
	color:#000000;
}

.ButtonDisable{
	filter:dropshadow(color=#FFFFFF,offx=1,offy=1);color:#b2b2b2;cursor:default;
}
.ButtonValue{
	color:#FFFFFF;
}
</style>