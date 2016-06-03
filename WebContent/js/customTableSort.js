/**
 * 此为自定义排序按钮:
 * <input class="customSortBtn"  type="button" name="desc" value="部门" orderField="" actionUrl="" />
 * 其中class、type、value为必输项。
 * class:统一为customSortBtn
 * name: 升序 asc ↑,降序 desc ↓
 * value:排序字段名称
 * orderField:实体类中字段名
 * actionUrl:请求数据的ACTION,请传入完整路径
 * 此外，在分页查询时由于调用了QUERY方法，导致结果未进行排序，所以需要在QUERY方法中在URL后面
 * 加上&orderField=$activatedCSBtn.orderField&regulation$activatedCSBtn.orderField两个参数
 * 后台只需取得这两个参数并加入HQL语句中即可
 * @author lping1 2014.9.28
 */
var $activatedCSBtn;//当前点击的排序按钮，主要用于分页查询或条件查询时获得排序参数
//初始化按钮,监听点击事件
$(function(){
	 $initCustomSortBtn();
	 $(".customSortBtn").click(function(){
		 $customSort(this);
	 });
});
 /*
 对表格数据进行排序
 */
 function $customSort(ele){
	$swapOrder(ele);
	$activatedCSBtn = ele;
	var pageNum = document.getElementById("pageNum").value;
	if(pageNum.indexOf(".")!=-1){
		pageNum = 1;
	}
	var regulation = $($activatedCSBtn).attr("name");
	var actionUrl = $(ele).attr("actionUrl");
	var fullActionUrl = actionUrl +'?regulation=' + regulation + '&orderField=' + $(ele).attr("orderField") +'&from=refresh';
	fullActionUrl = encodeURI(fullActionUrl);
	var method="setHTML";
	sendAjaxRequest(fullActionUrl,method,pageNum,true);
 } 
 
 /*
   改变控件的value，达到递增/递减切换的效果
 @param ele：控件对象本身
 */
 function $swapOrder(ele){
	 $initOtherCustomBtn(ele);
	 var name = $(ele).attr("name");
	 var value = $(ele).attr("value");
	 if(name=="asc" || name=="ASC"){
		 $(ele).attr("name","desc");
		 var newVal = value.replace(" ↑","").replace(" ↓","");
		 $(ele).val(newVal + " ↓");
	 }else if(name=="desc" || name=="DESC"){
		 $(ele).attr("name","asc");
		 var newVal = value.replace(" ↑","").replace(" ↓","");
		 $(ele).val(newVal + " ↑");
	 }
 }
 //根据默认name初始化排序控件
 function $initCustomSortBtn(){
     var ele = document.createElement("input");
     $(ele).attr("name","");
     $(ele).attr("orderField","");
     $(ele).attr("actionUrl","");
	 $activatedCSBtn = ele;//默认激活一个无参input
	 $("input.customSortBtn").each(function(){
		 var reg = $(this).attr("name");
		 if(reg=='asc' || reg=='ASC'){
			 var newVal = this.value.replace(" ↑","").replace(" ↓","");
			 if(this.value==newVal){
				 this.value += " ↑";
			 }
		 }else{
			 $(this).attr("name","desc");
			 var newVal = this.value.replace(" ↑","").replace(" ↓","");
			 if(this.value==newVal){
				 this.value += " ↓";
			 }
		 }
	 });
 }
 //点击某一排序按钮时，初始化其它排序按钮
 function $initOtherCustomBtn(ele){
	 $("input.customSortBtn").each(function(){
		 if(this!=ele){
			 $(this).attr("name","desc");
			 var value = $(this).val();
			 var newVal = value.replace("↑","").replace("↓","").replace(" ","");
			 $(this).val(newVal + " ↓") ;
		 }
	 });
 }