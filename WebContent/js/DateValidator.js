
/*************************************************
Validator 表单验证
itemName:表单名
pattern;验证类型
*************************************************/
	function DateValidate(beginDate, endDate){
	
	var Require= /.+/;

    var begin=document.getElementsByName(beginDate)[0].value
    var end=document.getElementsByName(endDate)[0].value

	var flag=true;

    if(Require.test(begin) && Require.test(end))
    	if( begin > end)
    		flag = false;
   return flag

}
