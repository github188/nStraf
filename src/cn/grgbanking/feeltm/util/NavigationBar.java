package cn.grgbanking.feeltm.util;

/**
 * <p>Title: FEEL View4.0.0</p>
 * <p>Description:��ݵ�ǰ���ܲ˵�menuno�����Ӧ�ĵ�����Ͳ˵���Ϣ</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: grgbanking</p>
 * @author ljming
 * @version 1.0
 */
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.grgbanking.feeltm.context.BaseApplicationContext;
import cn.grgbanking.feeltm.domain.MenuInfo;
import cn.grgbanking.feeltm.log.SysLog;
import cn.grgbanking.feeltm.menu.dao.MenuInfoDao;

@SuppressWarnings("unchecked")
@Service
@Transactional
public class NavigationBar {
	private NavigationBar() {
	}

	// -------------------------------------------------------------------------------
	/**
	 * ����ϵͳ������ �ѵ�ǰ�˵���Ŀ���浽session��:
	 * setAttribute(Constants.CURRENT_MENU_ITEM,menuInfo.getMenuitem());
	 * 
	 * @param pageContext
	 * @return
	 * @throws javax.servlet.jsp.JspException
	 */
	public static final String getNavigation(PageContext pageContext)
			throws javax.servlet.jsp.JspException {
		// �˵���֮��ķָ��ͷ
		String midflag = TagResources.message(pageContext,
				"index.function.flag");
		/*request.getContextPath();*/
		HttpServletRequest request = (HttpServletRequest) pageContext
				.getRequest();
		String menuid = request.getParameter("menuid");
		if (menuid != null) {
			pageContext.getSession().setAttribute("menuid", menuid);
		} else {
			menuid = (String) pageContext.getSession().getAttribute("menuid");
		}

		try {

			MenuInfoDao menuInfoDAO = (MenuInfoDao) BaseApplicationContext
					.getAppContext().getBean("menuInfoDao");

			ArrayList aryPath = new ArrayList();
			MenuInfo menuInfo = menuInfoDAO.findById(menuid);
			while (menuInfo != null) {
				aryPath.add(menuInfo);
				if (menuInfo.getParentid().equals(MenuInfoDao.TOP_PARENT_ID))
					break;
				menuInfo = menuInfoDAO.findById(menuInfo.getParentid());
			}
			int iSize = aryPath.size();
			if (iSize == 0) {
				request.setAttribute(Constants.CURRENT_MENU_ITEM, "");
				return "";
			}

			StringBuffer sbfPath = new StringBuffer();

			for (int i = iSize - 1; i > 0; i--) {
				if (i != iSize - 1)
					
					sbfPath.append(midflag);
				menuInfo = (MenuInfo) aryPath.get(i);
				//Actionto存在调用的路径以"/pages/"开头。
				String Actionto = menuInfo.getActionto();
				if(Actionto.contains(".action")){
					sbfPath.append("<a href=\"/nStraf"+menuInfo.getActionto()+"?menuid="+menuInfo.getMenuid()+"\" class=\"navigation\" target=\"mainframe\">");
				sbfPath.append(StringEscapeUtils.escapeHtml(menuInfo
						.getMenuitem()));
				sbfPath.append("</a>");
				}
				else{
					sbfPath.append(StringEscapeUtils.escapeHtml(menuInfo
							.getMenuitem()));
				}
			}
			menuInfo = (MenuInfo) aryPath.get(0);
			sbfPath.append(midflag);
			/*sbfPath
					.append(StringEscapeUtils
							.escapeHtml(menuInfo.getMenuitem()));*/
			//Actionto存在调用的路径以"/pages/"开头。
			String Actionto = menuInfo.getActionto();
			if(Actionto.contains(".action")){
				sbfPath.append("<a href=\"/nStraf"+menuInfo.getActionto()+"?menuid="+menuInfo.getMenuid()+"\" class=\"navigation\" target=\"mainframe\">");
			sbfPath.append(StringEscapeUtils.escapeHtml(menuInfo
					.getMenuitem()));
			sbfPath.append("</a>");
			}
			else{
				sbfPath.append(StringEscapeUtils.escapeHtml(menuInfo
						.getMenuitem()));
			}
			pageContext.getSession().setAttribute(menuInfo.getMenuid(),
					menuInfo);

			return sbfPath.toString();
		} catch (Exception ee) {
			SysLog.error(ee);
			return "";
		}
	}

	// -------------------------------------------------------------------------------
}