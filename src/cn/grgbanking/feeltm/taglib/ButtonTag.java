package cn.grgbanking.feeltm.taglib;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import cn.grgbanking.feeltm.domain.MenuOperate;
import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.util.Constants;

@SuppressWarnings({"serial", "unchecked"})
public class ButtonTag extends TagSupport {
	protected String menuid;

	public String getMenuid() {
		return menuid;
	}

	public void setMenuid(String menuid) {
		this.menuid = menuid;
	}

	protected String type;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	//�����Ҫ��ʾ�İ�ťid����ֻ�����û�Ȩ�޷�Χ�ڲŻ���ʾ
	protected String operid;

	public String getOperid() {
		return operid;
	}

	public void setOperid(String operid) {
		this.operid = operid;
	}
	/*
	 * 按钮位置
	 */
	protected String site; 
	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	/**
	 * @return
	 * @throws javax.servlet.jsp.JspException
	 */
	public int doEndTag() throws JspException {

		try {
			JspWriter out = pageContext.getOut();
			List list=null;
			String id="";
			if (type==null){
				id=(String)((HttpServletRequest)pageContext.getRequest()).getParameter("menuid");
			}else if (type.equals("request")){
				id=(String)((HttpServletRequest)pageContext.getRequest()).getParameter("menuid");
			}else if (type.equals("session")){
				id=(String)((HttpServletRequest)pageContext.getRequest()).getSession().getAttribute(menuid);
			}else{
				id=(String)((HttpServletRequest)pageContext.getRequest()).getParameter("menuid");
			}
			
			if (id!=null && !id.equals("")) {
				UserModel userModel = (UserModel)((HttpServletRequest)pageContext.getRequest()).getSession().getAttribute(Constants.LOGIN_USER_KEY);
				list=(List) ((HashMap)Constants.MENU_OPERID_MAP.get(userModel.getUserid())).get(id);         
			    if(list==null){
			    	return super.doEndTag();
			    }
			}else{
				return super.doEndTag();
			}
			
			//������operid���ѿ���ʾ�İ�ťid��operHaMap�洢
			HashMap operHaMap = new HashMap();
			if(operid!=null){
				String[] operids = operid.split(",");
				for(int i=0;i<operids.length;i++){
					operHaMap.put(operids[i], operids[i]);
				}
			}
			
			
			StringBuffer buttons = new StringBuffer();
			HashMap buttonMap = new HashMap();
			int firstflag = 0;
			for (ListIterator it = list.listIterator(); it.hasNext();) {

				MenuOperate mo = (MenuOperate) it.next();
				
				if(operid!=null){
					String operid_t = (String)operHaMap.get(mo.getOperid());
					if(operid_t==null){						
						continue;
					}
				}				
				if(site!=null && !site.equals("0") && mo.getSite()!=null){
					if(!mo.getSite().equals(site)){
						continue;
					}
				}
				buttons = new StringBuffer();
				buttons.append("<input type=\"");
				buttons.append(mo.getTypes());
				buttons.append("\" name=\"Submit1\" value=\'");
				buttons.append(mo.getOpername());
				buttons.append("\' class=\"MyButton\" ");
				if (mo.getKeys() != null && !mo.getKeys().equals(""))
				{
					buttons.append("AccessKey=\"");
					buttons.append(mo.getKeys());
					buttons.append("\" ");
				}
				if (mo.getClickname() != null && !mo.getClickname().equals("") && !mo.getClickname().equals("#"))
				{
					buttons.append("onclick=\"");
					if("query()".equals(mo.getClickname())){
						//点击查询时，让查询页面回到第一页
						buttons.append("document.getElementById("+"'"+"pageNum"+"'"+").value = 1 ;"+mo.getClickname());
					}
					else{
						buttons.append(mo.getClickname());
					}
					buttons.append("\" ");
				}
				if (mo.getPicpath() != null && !mo.getPicpath().equals(""))
				{
					buttons.append("image=\"");
					buttons.append(mo.getPicpath());
					buttons.append("\" ");
				}
				buttons.append(">");
				
				//���û�����ư�ťid��ֱ�����Ȩ�޷�Χ�ڵ����а�ť
				if(operid==null){
					if(firstflag>0){
			    		out.print("&nbsp;&nbsp;");
					}
			    	out.print(buttons);
			    	firstflag++;
				}else{
					buttonMap.put(mo.getOperid(), buttons);
				}
			}
			
			if(operid!=null){
				//��ݰ�ťid��˳�������ʾ
				String[] operids = operid.split(",");
				for(int i=0;i<operids.length;i++){
					StringBuffer buttonb = (StringBuffer)buttonMap.get(operids[i]);
				    if(buttonb!=null){
				    	if(firstflag>0){
				    		out.print("&nbsp;&nbsp;");
						}
				    	out.print(buttonb);
				    	firstflag++;
				    }
				}
			}
			
		} catch (Exception e) {
			throw new JspTagException("IOException" + e.toString());
		}
		return super.doEndTag();
	}

	public int doStartTag() throws JspTagException {

		return EVAL_BODY_INCLUDE;

	}




}
