/**
 * 
 */
package cn.grgbanking.feeltm.personDay.webapp;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.personDay.bean.SimplePersonDayBean;
import cn.grgbanking.feeltm.personDay.domain.PersonDay;
import cn.grgbanking.feeltm.personDay.service.PersonDayService;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.MsgBox;
import cn.grgbanking.framework.webapp.BaseAction;

/**
 * 人日统计ACTION
 * @author ping
 *
 */
@SuppressWarnings("serial")
public class PersonDayAction extends BaseAction {
	//人日统计JSON数据
	private String personDayJson;
	//人日统计详情对象
	private PersonDay personDay;
	/**人日统计service*/
	@Autowired
	private PersonDayService service;
	
	/**
	 * 加载近十年的人日统计数据 
	 * @author lping1 2014-10-14
	 */
	public String listPersonDayData(){
		Map<String,List<SimplePersonDayBean>> yearDataMap = new HashMap<String,List<SimplePersonDayBean>>();
		try {
			yearDataMap=service.getSimplePersonDayData();
			JSONObject json = JSONObject.fromObject(yearDataMap);	
			personDayJson = json.toString();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		request.setAttribute("personDayJson", personDayJson);
		return "personDayList";
	}
	/**
	 * 加载近十年的人日统计数据 
	 * @author lping1 2014-10-14
	 */
	public String refreshPersonDayData(){
		Map<String,List<SimplePersonDayBean>> yearDataMap = service.getSimplePersonDayData();
		try {
			JSONObject json = JSONObject.fromObject(yearDataMap);	
			personDayJson = json.toString();
			ajaxPrint(personDayJson);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
	
	private void ajaxPrint(String str){
		try{
			HttpServletResponse response = ServletActionContext.getResponse(); 
	        response.setContentType("application/json");  
	        response.setCharacterEncoding("UTF-8");  
	        PrintWriter writer = response.getWriter();
	        writer.print(str);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 点击月份加载该月人日详情
	 * @return
	 */
	public String PersonDayDetailByMonth(){
		String projectId = request.getParameter("projectId");
		String year =  request.getParameter("year");
		String month = request.getParameter("month");
		int monthInt = Integer.parseInt(month);
		if (monthInt<=9) {
			month = "0"+month;
		}
		List list = service.queryPersonDayByclick(projectId, year, month);
		if (list.size()>0) {
			personDay = (PersonDay) list.get(0);
				
//			String estimateDetail = "{\"result\":[{\"noLogDate\":\"2014-11-03\",\"userId\":\"hzyang1\",\"userName\":\"胡正洋\"},{\"noLogDate\":\"2014-11-03\",\"userId\":\"bxping1\",\"userName\":\"白雪平\"},{\"noLogDate\":\"2014-11-03\",\"userId\":\"jhbo\",\"userName\":\"蒋海波\"}"
//					+ ",{\"noLogDate\":\"2014-11-03\",\"userId\":\"hzyang1\",\"userName\":\"胡正洋\"},{\"noLogDate\":\"2014-11-03\",\"userId\":\"bxping1\",\"userName\":\"白雪平\"},{\"noLogDate\":\"2014-11-03\",\"userId\":\"jhbo\",\"userName\":\"蒋海波\"}],\"count\":3}";
			
		    //总体核算
			float sum =0;
			float confirm = 0;
			float err = 0;
			if (StringUtils.isNotBlank(personDay.getError())) {
				
				err = Float.parseFloat(personDay.getError());
			}
			if (StringUtils.isNotBlank(personDay.getPersonDayConfirm())) {
				confirm = Float.parseFloat(personDay.getPersonDayConfirm());
			}
			sum = err + confirm;
			//保留一位小数
			sum = (float)(Math.round(sum*10))/10;
			request.setAttribute("sum",sum);
			String estimateDetail = personDay.getEstimateDetail();
			JSONObject json = JSONObject.fromObject(estimateDetail);
			request.setAttribute("estimateDetail",json.toString());
		}
		return "personDayDetail";
	}
	/**
	 * 修改人日
	 * @return
	 */
	public String update(){
		UserModel userModel = (UserModel) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
		String id  = request.getParameter("id");
		String personDayEdit = request.getParameter("personDayEdit");
		String note = request.getParameter("note");
		try {
			note = URLDecoder.decode(note, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		List list = service.queryPersonDayById(id);
		PersonDay updatePersonDay = (PersonDay) list.get(0);
		updatePersonDay.setPersonDayEdit(personDayEdit);
		updatePersonDay.setNote(note);
		updatePersonDay.setIsEdit("true");
		updatePersonDay.setUpdateTime(new Date());
		updatePersonDay.setUpdateuserId(userModel.getUserid());
		updatePersonDay.setUpdateUsername(userModel.getUsername());
		boolean  flag = service.updatePersonDay(updatePersonDay);
		if (flag) {
			MsgBox msgBox = new MsgBox(request, getText("修改成功"));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			this.addActionMessage(getText("修改成功"));
			return "msgBox";
		}
		else{
			MsgBox msgBox = new MsgBox(request, getText("修改失败"));
			msgBox.setButtonType(MsgBox.BUTTON_CLOSE);
			this.addActionMessage(getText("修改失败"));
			return "msgBox";
		}
	}
	
	
	
	/*----------------getter&setter-------------------------------------*/

	public String getPersonDayJson() {
		return personDayJson;
	}

	public void setPersonDayJson(String personDayJson) {
		this.personDayJson = personDayJson;
	}

	public PersonDay getPersonDay() {
		return personDay;
	}

	public void setPersonDay(PersonDay personDay) {
		this.personDay = personDay;
	}
	
	
}
