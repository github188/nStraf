package cn.grgbanking.feeltm.contact.webapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import cn.grgbanking.feeltm.config.BusnDataDir;
import cn.grgbanking.feeltm.contact.service.ContactService;
import cn.grgbanking.feeltm.domain.SysUser;
import cn.grgbanking.feeltm.domain.UsrContacts;
import cn.grgbanking.feeltm.domain.UsrGroup;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.project.domain.Project;
import cn.grgbanking.feeltm.project.service.ProjectService;
import cn.grgbanking.feeltm.staff.service.StaffInfoService;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.JSONUtil;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.util.BeanUtils;
import cn.grgbanking.framework.util.Page;
import cn.grgbanking.framework.webapp.BaseAction;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;


@SuppressWarnings( { "serial", "unchecked" })
public class ContactAction extends BaseAction  implements ModelDriven<UsrContacts>{

	@Autowired
	private ContactService contactService;
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private StaffInfoService staffInfoService;

	private UsrContacts usrContact = new UsrContacts();
	
	public String pageNum;
	public String pageSize;
	
	private File file; 
	
	/**
	 * 查询所有人员通讯信息，带有分页
	 * @return
	 * @throws Exception
	 */
	public String listAll()throws Exception{
		int pagenum = 1;
		int pagesize = 20;
		if (pageNum!= null&&pageNum.length() > 0)
			pagenum = Integer.parseInt(pageNum);
		if (pageSize!= null&&pageSize.length() > 0)
			pagesize = Integer.parseInt(pageSize);
		String orderField = request.getParameter("orderField");
		String regulation = request.getParameter("regulation");
//		UserModel user = (UserModel)session.get(Constants.LOGIN_USER_KEY);
		
		//Page page = contactService.getContactPage(pagenum, pagesize);
		Page page = contactService.getContactPageBycondition(usrContact,orderField,regulation,pagenum, pagesize);
		request.setAttribute("currPage", page);
		List<UsrContacts> list = page.getQueryResult();		//获取所有查询对象集合
		
		this.setUsrContactBylist(list);//根据获取的对象集合，设置其邮箱部门组别信息
		//this.setDataDir();
		request.setAttribute("contactlist", list);
		request.getSession().setAttribute("contactinfo.menuid",  request.getParameter("menuid"));
		return "listsuccess";
	}
	
	/**
	 * 根据查询条件查询
	 * @return
	 * @throws Exception
	 */
	public String query() throws Exception {
		try {
			String from = request.getParameter("from");
			int pagenum = 1;
			int pagesize = 20;
			if (pageNum!= null&&pageNum.length() > 0)
				pagenum = Integer.parseInt(pageNum);
			if (pageSize!= null&&pageSize.length() > 0)
				pagesize = Integer.parseInt(pageSize);
			String orderField = request.getParameter("orderField");
			String regulation = request.getParameter("regulation");
			
			//根据查询条件查询数据
			Page page = contactService.getContactPageBycondition(usrContact,orderField,regulation,pagenum, pagesize);
			//createDate, createMan, update_man, category, pn,pageNum, pageSize, raiseEndDate
			request.setAttribute("currPage", page);
			List list = (List<Object>)page.getQueryResult();
			this.setUsrContactBylist(list);
			
			if (from != null && from.equals("refresh")) {
				Map map = new LinkedHashMap();
				map.put("pageCount", String.valueOf(page.getPageCount()));
				map.put("recordCount", String.valueOf(page.getRecordCount()));
				JSONUtil jsonUtil = new JSONUtil(
						"cn.grgbanking.feeltm.domain.UsrContacts");
				JSONArray jsonObj = jsonUtil.toJSON(list, map);//将查询数据转换为jsonArray，
				JSONObject input = new JSONObject();
				if(page.getRecordCount()==0){
					input.put("pageCount", String.valueOf(page.getPageCount()+1));	
					input.put("recordCount", String.valueOf(page.getRecordCount()));
					input.put("jsonObj", jsonObj);	
				}
				else{
					input.put("pageCount", String.valueOf(page.getPageCount()));
					input.put("recordCount", String.valueOf(page.getRecordCount()));
					input.put("jsonObj", jsonObj);	
				}
				input.put("jsonObj", jsonObj);		//将数据以json的方式传入前台						
				PrintWriter out = response.getWriter();
				out.print(input);
				return "";
			} else {
				ActionContext.getContext().put("behaviorList", list);
				return "query";
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		return "query";
	}
	
	/**
	 * 刷新页面
	 */
	public void refresh() {
		try {
			query();
		} catch (Exception e) {
			e.printStackTrace();
		}
		;
	}
	
	/**
	 * 根据部门或小组查询部门或小组下的人员姓名
	 * @return
	 * @throws Exception
	 */
	public String queryNames() throws Exception{
		response.setHeader( "Cache-Control", "no-cache" );
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/javascript;charset=UTF-8");
		PrintWriter out=response.getWriter();
		List<Object> list=contactService.getNames(usrContact);
		JSONArray jsonArray=new JSONArray(list);
		out.print(jsonArray);
		out.flush();
		out.close();
		return null;
	}
	
	/**
	 * 增加信息页面中，根据选择部门或小组查询没有添加通讯录的员工信息
	 * @return
	 * @throws Exception
	 */
	public String queryNameId() throws Exception{
		response.setHeader( "Cache-Control", "no-cache" );
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/javascript;charset=UTF-8");
		PrintWriter out=response.getWriter();
		String deptValue = request.getParameter("deptValue");//获取部门下拉框的首先项值
		List<Object> list = contactService.getNoContacts(usrContact,deptValue);
		JSONUtil jsonUtil = new JSONUtil(
				"cn.grgbanking.feeltm.domain.SysUser");
		JSONArray jsonObj = jsonUtil.toJSON(list, null);
		//JSONObject input = new JSONObject();
		//input.put("json", jsonObj);
		//JSONArray jsonArray=new JSONArray(list);
		out.print(jsonObj);
		out.flush();
		out.close();
		return null;
	}
	
	/**
	 * 添加通讯录信息
	 * @return
	 */
	public String add(){
//		List<SysUser> list = contactService.getNoContacts(usrContact);
//		request.setAttribute("list", list);
//		this.setDataDir();
		return "add";
	}
	
	/**
	 * 保存添加的信息
	 * @return
	 */
	public String save(){
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		UsrContacts usrcontact = new UsrContacts();
		String newAnOther = request.getParameter("newAnOther");
		
		//页面传入过来的是名字加id，姓名（Id），需要分离出姓名
		usrContact.setConName(usrContact.getConName().substring(0,usrContact.getConName().indexOf('(')));
		MsgBox msgBox;
		try {

			Date date = new Date();
			BeanUtils.copyProperties(usrcontact, usrContact);
			usrcontact.setUpdate(date);
			//usrcontact.setUpdateManId(userModel.getUserid());
			usrcontact.setUpdateManId(userModel.getUsername());
			if (!contactService.isExitsContactInfo(usrcontact)) {//判断此用户是否已存在，如存在则不添加
				boolean flag = contactService.addContactInfo(usrcontact);
				if (flag == true) {		//添加是否成功
					SysLog.operLog(request, Constants.OPER_ADD_VALUE, usrcontact.getConName());// 记录日志
					SysLog.info("User:" + userModel.getUserid()
							+ " add a UserContact : " + usrcontact.getConName());
					msgBox = new MsgBox(request, getText("add.ok"));
					msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
					this.addActionMessage(getText("add.ok"));
				} else {
					msgBox = new MsgBox(request,
							getText("operInfoform.addfaile"));
					msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
					this.addActionMessage(getText("operInfoform.addfaile"));
				}
			} else {
				msgBox = new MsgBox(request, getText("operInfoform.addfaile"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
				this.addActionMessage(getText("operInfoform.addfaile"));
			}
		} catch (Exception e) {
			// e.printStackTrace();
			SysLog.error(request,
					"error in (ContactAction.java-save())");
			SysLog.error("User:" + userModel.getUserid()
					+ "failed to add a UserContact : " + usrcontact.getConName());
			SysLog.error(e);

			msgBox = new MsgBox(request, getText("operInfoform.exit"));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			this.addActionMessage(getText("operInfoform.addfaile"));
		}

		boolean hasActionMessage = this.hasActionMessages();
		request.setAttribute("hasActionMessage", hasActionMessage);

		// 没有点“提交后继续新增”
		if (newAnOther.equals("true")) {
			return "add";
		}
		return "msgBox";
	}
	
	/**
	 * 进入编辑数据页面，得到被编辑原数据，判断是否有权限修改
	 * @return
	 * @throws Exception
	 */
	public String edit() throws Exception {
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		UsrContacts usrcontacts = null;
		try {
			usrcontacts =contactService.getContactById(usrContact.getId());
			ActionContext.getContext().getValueStack().pop();				//清空页面堆栈中的顶层数据
			ActionContext.getContext().getValueStack().push(usrcontacts); 	//将需修改的原数据放入顶层堆栈中，方便页面获取
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
		}
		boolean hasEdit = false;
		for(String groupId:userModel.getGroupids()){
			if(groupId.equals("hrxz")){
				hasEdit = true;
			}
		}
		if(!hasEdit)		//根据级别判断是否需要限制修改权限，当级别较低则需要判断是否修改本人信息，级别高的用户可直接修改
		{
			if(!usrcontacts.getConName().equals(userModel.getUsername()))
			{
				MsgBox msgBox1 = new MsgBox(request,getText("operInfoform.updatefaile",new String[]{"您只能修改您个人的通讯方式 "}));
				msgBox1.setButtonType(MsgBox.BUTTON_CLOSE);
				return "msgBox";
			}
		}
		return "edit";
	 }
	
	/**
	 * 修改数据到数据库中
	 * @return
	 * @throws Exception
	 */
	public String update() throws Exception {
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		UsrContacts usrcontact = null;
		try {
			usrcontact =contactService.getContactById(usrContact.getId());
			
			Date date = new Date();
			usrcontact.setUpdate(date);
			usrcontact.setConTel(usrContact.getConTel());
			usrcontact.setConMobile(usrContact.getConMobile());
			usrcontact.setNote(usrContact.getNote());
			//usrcontact.setUpdateManId(userModel.getUserid());
			usrcontact.setUpdateManId(userModel.getUsername());
			//更新用户表
			SysUser user = staffInfoService.findUserByUserid(usrcontact.getUserId());
			user.setMobile(usrcontact.getConMobile());
			user.setTel(usrcontact.getConTel());
			staffInfoService.updateStaffInfo(user);
			boolean flag = contactService.updateContactInfo(usrcontact);
			if (flag == true) {
				MsgBox msgBox = new MsgBox(request,
						getText("operInfoform.updateok"));
				msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request,
					getText("operInfoform.updatefaile"), new String[] { e
							.toString() });
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		}
		return "msgBox";
	}
	
	/**
	 * 批量删除通讯录
	 * @return
	 * @throws Exception
	 */
	public String delete() throws Exception {
		try {
			String ids = request.getParameter("ids");
			int iCount = 0;
			String[] sids = ids.split(",");
			if("all".equals(sids[0])){
				String[] arr = new String[sids.length-1];
				System.arraycopy(sids, 1, arr, 0, arr.length);
				sids = arr;
			}
			if (sids != null) {
				for (int i = 0; i < sids.length; i++) {
					UsrContacts temp = contactService.getContactById(sids[i]);
					contactService.delete(temp);
					iCount++;
				}
			}
			MsgBox msgBox = new MsgBox(request, getText("rule.del.ok"), "rule",
					new String[] { String.valueOf(iCount) });
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		} catch (Exception e) {
			e.printStackTrace();
			SysLog.error(e);
			MsgBox msgBox = new MsgBox(request, getText("operator.addfaile"));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		}
		return "msgBox";
	} 
	
	
	public String exportData() throws Exception{
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		String orderField = request.getParameter("orderField");
		String regulation = request.getParameter("regulation");
		Page page = contactService.getContactPageBycondition(usrContact,orderField,regulation,1, 3000);
		List list = (List<Object>)page.getQueryResult();
		this.setUsrContactBylist(list);
		SimpleDateFormat fo=new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String filename = "运通信息通讯录-"+fo.format(date);//设置文件名
		if(!usrContact.getDeptName().equals("全选")){
			Map<String,String> deptkeyValue = BusnDataDir.getMapKeyValue("staffManager.department");
			filename = "运通信息 "+deptkeyValue.get(usrContact.getDeptName()).trim()+" 通讯录"+fo.format(date);
		}
		OutputStream  os = response.getOutputStream();// 取得输出流 
		response.reset();// 清空输出流  
        response.setHeader("Content-disposition","attachment;filename="+new String((filename).getBytes(),"ISO8859_1")+".xls");// 设定输出文件头  
       /* response.setContentType("application/msexcel");// 定义输出类型  
*/	
        response.setContentType("application/vnd.ms-excel");
        /*ExportExcel ee = new ExportExcel();
		ee.exportData(list, os, userModel);*/
        
     // 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("UsrContact");
		
		/**	** ** *设置字体* ** ** **/
		HSSFFont defaultFont = wb.createFont(); 
		defaultFont.setFontName("宋体");
		defaultFont.setFontHeightInPoints((short)12);
		
		HSSFFont headfont = wb.createFont();
		headfont.setFontName("黑体");
		headfont.setFontHeightInPoints((short)30);// 字体大小   
		headfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		
		HSSFFont cellfont = wb.createFont();
		headfont.setFontName("黑体");
		headfont.setFontHeightInPoints((short)25);// 字体大小   
		headfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		
		HSSFFont allFont = wb.createFont();
		allFont.setFontName("黑体");
		allFont.setColor(HSSFColor.RED.index);
		allFont.setFontHeightInPoints((short)12);// 字体大小   
		allFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		
		/**	** ** *设置样式* ** ** **/
		HSSFCellStyle defaultStyle = this.getCellStyle(wb,true, defaultFont,(short)-2, (short)-2, HSSFCellStyle.ALIGN_CENTER);
		HSSFCellStyle tileStyle = this.getCellStyle(wb, false,headfont, (short)-2, (short)-2, HSSFCellStyle.ALIGN_CENTER);
		HSSFCellStyle dateStyle = this.getCellStyle(wb, false,defaultFont,(short)-2, (short)-2, HSSFCellStyle.ALIGN_CENTER);
		HSSFCellStyle upDateStyle = this.getCellStyle(wb, true,defaultFont,(short)-2, (short)-2, HSSFCellStyle.ALIGN_CENTER);
		HSSFCellStyle cellNameStyle = this.getCellStyle(wb,true,cellfont, HSSFColor.GREY_25_PERCENT.index, HSSFColor.LIME.index, HSSFCellStyle.ALIGN_CENTER); 
		HSSFCellStyle countStyle = this.getCellStyle(wb,false,  cellfont, (short)-2, (short)-2, HSSFCellStyle.ALIGN_RIGHT);
		HSSFCellStyle allStyle = this.getCellStyle(wb, false, allFont, (short)-2, (short)-2, HSSFCellStyle.ALIGN_LEFT);
		
		/**	** ** *设置文本格式* ** ** **/
		HSSFDataFormat format = wb.createDataFormat();
		defaultStyle.setDataFormat(format.getFormat("@"));
		dateStyle.setDataFormat(format.getFormat("m/d/yy h:mm"));
		upDateStyle.setDataFormat(format.getFormat("m/d/yy h:mm"));
		
		
		/**		创建标题		*/
		HSSFRow titleRow = sheet.createRow((int) 0);
		titleRow.setHeight((short)(2*512));
		wb = this.setExcelValue(wb, 0, "运通信息通讯录", 0, (short)0, tileStyle);
		sheet.addMergedRegion(new Region(0, (short)0, 0, (short)10));
		
		
		/**		创建日期时间	及导出者	*/
		HSSFRow dateRow = sheet.createRow((int) 1);
		wb = this.setExcelValue(wb, 0, "制表日期：", 1, (short)3, dateStyle);
		wb = this.setExcelValue(wb, 0, new Date(), 1, (short)4, dateStyle);
		wb = this.setExcelValue(wb, 0, "By:"+userModel.getUsername(), 1, (short)9, dateStyle);
		
		/**		创建列名		*/
		HSSFRow cellNameRow = sheet.createRow((int) 2);
		wb = this.setExcelValue(wb, 0, "序号", 2, (short)0, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "Id", 2, (short)1, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "姓名", 2, (short)2, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "部门", 2,(short) 3, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "组别", 2, (short)4, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "电话", 2, (short)5, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "手机", 2, (short)6, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "邮箱", 2, (short)7, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "备注", 2, (short)8, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "更新人", 2, (short)9, cellNameStyle);
		wb = this.setExcelValue(wb, 0, "更新时间", 2, (short)10, cellNameStyle);
		
		
		int i=0;
		/**	** ** *循环数据，并将数据列入Excel表中* ** ** **/
		for(;i<list.size();i++){
			
			UsrContacts usrContact = (UsrContacts) list.get(i);
			HSSFRow row = sheet.createRow(i+3);
			row.setHeight((short)512);
			
			HSSFCell cell = row.createCell((short)0);
			
			/*cell.setCellStyle(defaultStyle);
			cell.setCellValue(i+1+"");*/
			wb = this.setExcelValue(wb, 0, i+1, i+3, (short)0, defaultStyle);
			sheet.setColumnWidth((short)0, (short) (3*512));
			/*cell = row.createCell((short)1);
			cell.setCellStyle(defaultStyle);
			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellValue(usrContact.getUserId());*/
			wb = this.setExcelValue(wb, 0, usrContact.getUserId(), i+3, (short)1, defaultStyle);
			
			sheet.setColumnWidth((short)1, (short) (5*512));
			cell = row.createCell((short)2);
			cell.setCellStyle(defaultStyle);
			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellValue(usrContact.getConName());
			sheet.setColumnWidth((short)2, (short) (5*512));
			cell = row.createCell((short)3);
			cell.setCellStyle(defaultStyle);
			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellValue(usrContact.getDeptName());
			sheet.setColumnWidth((short)3, (short) (8*512));
			cell = row.createCell((short)4);
			cell.setCellStyle(defaultStyle);
			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellValue(usrContact.getGroupName());
			sheet.setColumnWidth((short)4, (short) (16*512));
			cell = row.createCell((short)5);
			cell.setCellStyle(defaultStyle);
			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellValue(usrContact.getConTel());
			sheet.setColumnWidth((short)5, (short) (8*512));
			cell = row.createCell((short)6);
			cell.setCellStyle(defaultStyle);
			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellValue(usrContact.getConMobile());
			sheet.setColumnWidth((short)6, (short) (8*512));
			cell = row.createCell((short)7);
			cell.setCellStyle(defaultStyle);
			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellValue(usrContact.getConEmail());
			sheet.setColumnWidth((short)7, (short) (13*512));
			cell = row.createCell((short)8);
			cell.setCellStyle(defaultStyle);
			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellValue(usrContact.getNote());
			sheet.setColumnWidth((short)8, (short) (13*512));
			cell = row.createCell((short)9);
			cell.setCellStyle(defaultStyle);
			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellValue(usrContact.getUpdateManId());
			sheet.setColumnWidth((short)9, (short) (5*512));
			cell = row.createCell((short)10);
			cell.setCellStyle(upDateStyle);
			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellValue(usrContact.getUpdate());
			sheet.setColumnWidth((short)10, (short) (10*512));
		}
		/**	** ** *创建合计行* ** ** **/
		HSSFRow row = sheet.createRow(i+3);
		wb = this.setExcelValue(wb, 0, "合计:", i+3, (short)1, countStyle);
		wb = this.setExcelValue(wb, 0, i+" 人", i+3, (short)2, allStyle);
		
		wb.write(os);
		os.close();
        
		//清理刷新缓冲区，将缓存中的数据将数据导出excel
		os.flush();
		//关闭os
		if(os!=null){
			os.close();
		}
		
		return null;
	}
	
	public String importData() throws Exception{
		UserModel userModel = (UserModel) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		
		if (file == null) {
			throw new Exception("文件为空！");
		}
		//获取文件流，并创建对应的excel对象
		POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(file));
		HSSFWorkbook wb =  new HSSFWorkbook(fs);
		int sheetNumber = wb.getNumberOfSheets();
		List<UsrContacts> contactSaveList = new ArrayList<UsrContacts>();	//数据添加集合
		List<UsrContacts> contactUpdateList = new ArrayList<UsrContacts>();	//数据修改集合
		String error_UsrContact = "";		//错误信息
		int failNum = 0;
		
		/**	** ** *循环每一个sheet，添加数据* ** ** **/
		for(int shNum=0;shNum<sheetNumber;shNum++){
			HSSFSheet sheet = wb.getSheetAt(shNum);
			HSSFRow headRow = sheet.getRow(0);
			
			if(headRow==null){//判断sheet中第一行是否有数据，没有数据则不进行下一步
				continue;
			}
			if(headRow.getCell((short)0)!=null){
				headRow.getCell((short)0).setCellType(HSSFCell.CELL_TYPE_STRING);
			}
			
			/**	** ** *获取表格标题，如标题不对，则格式不对* ** ** **/
			String titel = headRow.getCell((short)0).getStringCellValue()==null?"":headRow.getCell((short)0).getStringCellValue();
			if(titel.equals("运通信息通讯录")){
				long rowNumber = sheet.getPhysicalNumberOfRows();	//sheet中的行数
				HSSFRow cNameRow = sheet.getRow(2);	//获取列标题行，并判断对应列标题是否正确，防止数据导入出错
				String idName = "";
				String nameName = "";
				String telName = "";
				String mobileName = "";
				String noteName = "";
				if(cNameRow.getCell((short)1)!=null)
					cNameRow.getCell((short)1).setCellType(HSSFCell.CELL_TYPE_STRING);
				idName = cNameRow.getCell((short)1).getStringCellValue()==null?"": cNameRow.getCell((short)1).getStringCellValue();
				if(cNameRow.getCell((short)2)!=null)
					cNameRow.getCell((short)2).setCellType(HSSFCell.CELL_TYPE_STRING);
				nameName = cNameRow.getCell((short)2).getStringCellValue()==null?"": cNameRow.getCell((short)2).getStringCellValue();
				if(cNameRow.getCell((short)5)!=null)
					cNameRow.getCell((short)5).setCellType(HSSFCell.CELL_TYPE_STRING);
				telName = cNameRow.getCell((short)5).getStringCellValue()==null?"": cNameRow.getCell((short)5).getStringCellValue();
				if(cNameRow.getCell((short)6)!=null)
					cNameRow.getCell((short)6).setCellType(HSSFCell.CELL_TYPE_STRING);
				mobileName = cNameRow.getCell((short)6).getStringCellValue()==null?"": cNameRow.getCell((short)6).getStringCellValue();
				if(cNameRow.getCell((short)8)!=null)
					cNameRow.getCell((short)8).setCellType(HSSFCell.CELL_TYPE_STRING);
				noteName = cNameRow.getCell((short)8).getStringCellValue()==null?"": cNameRow.getCell((short)8).getStringCellValue();
				if(!idName.equals("Id")){
					error_UsrContact += "此文件用户标识列不存在或者格式不对；";
				}else if(!nameName.equals("姓名")){
					error_UsrContact += "此文件用户姓名列不存在或者格式不对；";
				}else if(!telName.equals("电话")){
					error_UsrContact += "此文件用户电话列不存在或者格式不对；";
				}else if(!mobileName.equals("手机")){
					error_UsrContact += "此文件用户手机列不存在或者格式不对；";
				}else if(!noteName.equals("备注")){
					error_UsrContact += "此文件用户备注列不存在或者格式不对；";
				}else{
				
					/**	** ** *循环excel表格中的数据，并判断格式，存入或修改数据库相应值* ** ** **/
				for(int rowNum=3;rowNum<rowNumber;rowNum++){
					int wrongRow = rowNum+1;
					HSSFRow row = sheet.getRow(rowNum);
					UsrContacts usrContacts = new UsrContacts();
					String userId = "";
					if(row.getCell((short)1)!=null)
						row.getCell((short)1).setCellType(HSSFCell.CELL_TYPE_STRING);
					userId = row.getCell((short)1).getStringCellValue()==null?"": row.getCell((short)1).getStringCellValue();
					String flagname = "";
					if(row.getCell((short)2)!=null)
						row.getCell((short)2).setCellType(HSSFCell.CELL_TYPE_STRING);
					flagname = row.getCell((short)2).getStringCellValue()==null?"": row.getCell((short)2).getStringCellValue();
					if("".equals(userId) && "".equals(flagname)){
						continue;
					}else if("合计:".equals(userId)){
						continue;
					}else if("".equals(userId)){
						error_UsrContact += "第"+ wrongRow+"行，用户Id为空；";
						failNum++;
						continue;
					}else{
						String type = "";
						usrContacts.setUserId(userId);
						if(contactService.isExitsContactInfo(usrContacts)){
							type = "update";
						}else if(contactService.isExitsUserInfo(usrContacts)){
							type = "save";
						}else{
							error_UsrContact += "第"+ wrongRow+"行，用户\""+userId+"\"不存在；";
							failNum++;
							continue;
						}
						String conName = "";
						if(row.getCell((short)2)!=null)
							row.getCell((short)2).setCellType(HSSFCell.CELL_TYPE_STRING);
						conName = row.getCell((short)2).getStringCellValue()==null?"": row.getCell((short)2).getStringCellValue();
						if(conName.equals("")){
							error_UsrContact += "第"+ wrongRow+" 行，用户姓名为空；";
							failNum++;
							continue;
						}else{
							usrContacts.setConName(conName);
							String conTel = "";
							if(row.getCell((short)5)!=null)
								row.getCell((short)5).setCellType(HSSFCell.CELL_TYPE_STRING);
							conTel = row.getCell((short)5).getStringCellValue()==null?"": row.getCell((short)5).getStringCellValue();
							usrContacts.setConTel(conTel);
							String conMobile = "";
							if(row.getCell((short)6)!=null)
								row.getCell((short)6).setCellType(HSSFCell.CELL_TYPE_STRING);
							conMobile = row.getCell((short)6).getStringCellValue()==null?"": row.getCell((short)6).getStringCellValue();
							usrContacts.setConMobile(conMobile);
							String note = "";
							if(row.getCell((short)8)!=null)
								row.getCell((short)8).setCellType(HSSFCell.CELL_TYPE_STRING);
							note = row.getCell((short)8).getStringCellValue()==null?"": row.getCell((short)8).getStringCellValue();
							usrContacts.setNote(note);
							Date date = new Date();
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							usrContacts.setUpdate(date);
							//usrContacts.setUpdateManId(userModel.getUserid());
							usrContacts.setUpdateManId(userModel.getUsername());
							if(type.equals("update")){
								contactUpdateList.add(usrContacts);
							}else if(type.equals("save")){
								contactSaveList.add(usrContacts);
							}
						}
					}
				}
				}
			}else {
				error_UsrContact += "此文件标题错误，格式不对；";
			}
		}
		contactService.addContactListInfo(contactSaveList);
		contactService.updateContactListInfo(contactUpdateList);
		SysLog.operLog(request, Constants.OPER_ADD_VALUE, contactSaveList.size()+"个人");// 记录日志
		SysLog.info("User:" + userModel.getUserid()
				+ " add "+contactSaveList.size()+" UserContact ,update "+contactUpdateList.size()+"UserContact ,"+error_UsrContact );
		
		MsgBox msgBox = new MsgBox(request,error_UsrContact+",成功添加了"+contactSaveList.size()+"条记录，修改了"+contactUpdateList.size()+"条记录");
		msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
		return "msgBox";
	}
	
	/**
	 * 设置样式方法，
	 * @param wb
	 * @param hasBorder
	 * @param defaultFont
	 * @param FillForeColor
	 * @param FillbackColor
	 * @param local
	 * @return
	 */
	private HSSFCellStyle getCellStyle(HSSFWorkbook wb,boolean hasBorder,HSSFFont defaultFont,short FillForeColor,short FillbackColor,short local){
		HSSFCellStyle style = wb.createCellStyle();	
		
		/**  黑色粗体边框    */
		if(hasBorder){
			style.setBorderBottom(HSSFCellStyle.BORDER_THIN);	
			style.setBottomBorderColor(HSSFColor.BLACK.index);	
			style.setBorderLeft(HSSFCellStyle.BORDER_THIN);		
			style.setLeftBorderColor(HSSFColor.BLACK.index);
			style.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style.setRightBorderColor(HSSFColor.BLACK.index);
			style.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style.setTopBorderColor(HSSFColor.BLACK.index);
		}
		/** 	fontColor字体		*/
		if(defaultFont!=null){
			style.setFont(defaultFont);
		}
		
		/**		FillColor前景填充		*/
		if(FillForeColor!=(short)-2){
			style.setFillForegroundColor(FillForeColor);
			style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		}
		if(FillForeColor!=(short)-2){
			style.setFillBackgroundColor(FillForeColor);
			style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		}
		/**		设置格式 		*/
		if(local!=-2){
			style.setAlignment(local);
		}
		style.setWrapText(true);//自动换行
		return style;
	}
	
	/**
	 * 设置每一个cell值以及其样式
	 * @param wb
	 * @param sheetNo
	 * @param value
	 * @param rowNo
	 * @param cellNo
	 * @param defaultStyle
	 * @return
	 */
	private HSSFWorkbook setExcelValue(HSSFWorkbook wb,int sheetNo,Object value,int rowNo,short cellNo,HSSFCellStyle defaultStyle){
		HSSFSheet sheet = wb.getSheetAt(sheetNo);
		HSSFRow row = sheet.getRow(rowNo);
		HSSFCell cell = row.createCell(cellNo);
		if(value instanceof Integer){
			cell.setCellValue(Integer.valueOf(value.toString()));
		}else if(value instanceof String){
			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellValue(value.toString());
		}else if(value instanceof Date){
			cell.setCellValue((Date)value);
		}else if(value instanceof Double){
			cell.setCellValue((Double)value);
		}else if(value instanceof Calendar){
			cell.setCellValue((Calendar)value);
		}else if(value instanceof Boolean){
			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellValue(value.toString().equals("true")?"是":"否");
		}else if(value == null){
			cell.setCellValue(value.toString());
		}else {
			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellValue(value.toString());
		}
		if(defaultStyle!=null){
			cell.setCellStyle(defaultStyle);
		}
		return wb;
	}
	
	/**
	 * 根据查询出的人员，在相关人员信息表中查找所在部门和分组
	 * @param list
	 */
	private void setUsrContactBylist(List<UsrContacts> list){
		Map<String,String> deptkeyValue = BusnDataDir.getMapKeyValue("staffManager.department");
		for(UsrContacts us:list){
			Object[] usr = contactService.getContactInfoByUser(us.getUserId());
			if(usr!=null){
				us.setConEmail(usr[3]==null?"":usr[3].toString());
				us.setDeptName(usr[4]==null?"":deptkeyValue.get(usr[4].toString()));
				List<Project> groupList = projectService.getProjectByUserId(us.getUserId());
				String groupname = "";
				for(Project usg:groupList){
					groupname +=","+usg.getName();
				}
				if(groupname.length()>0){
					us.setGroupName(groupname.substring(1));
				}
			}
		}
	}
	
	/**
	 * 查询数据字典中部门以及小组信息，并放入request中
	 */
	private void setDataDir(){
		List<UsrGroup> usrgroup = contactService.getALlusrgroup();
		Map<String,String> deptkeyValue = BusnDataDir.getMap("staffManager.department");
		request.setAttribute("deptMap", deptkeyValue);
		request.setAttribute("groupList", usrgroup);
	}
	
	public ContactService getContactService() {
		return contactService;
	}

	public void setContactService(ContactService contactService) {
		this.contactService = contactService;
	}

	
	public UsrContacts getUsrContact() {
		return usrContact;
	}

	public void setUsrContact(UsrContacts usrContact) {
		this.usrContact = usrContact;
	}

	public String getPageNum() {
		return pageNum;
	}

	public void setPageNum(String pageNum) {
		this.pageNum = pageNum;
	}

	public String getPageSize() {
		return pageSize;
	}

	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}

	public UsrContacts getModel() {
		return usrContact;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
	
	
}
