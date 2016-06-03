package cn.grgbanking.feeltm.filter;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.grgbanking.feeltm.login.domain.UserModel;
import cn.grgbanking.feeltm.util.Constants;
import cn.grgbanking.feeltm.util.DateUtil;

@SuppressWarnings("serial")
public class LoginFilter extends HttpServlet implements Filter {
	private static boolean printFlag;
	final private static String[] system_url = { "index.jsp",
			"login!checkuser", "login!timeout", "main.jsp", "menuset.jsp",
			"topFrame.jsp", "isTimeOut", "logout",
			"monthlyInfo!getMonthlyAttenceInfo",
			"monthlyInfo!getMonthlyDeptInfo",
			"monthlyInfo!getMonthlyChangeInfo",
			"monthlyInfo!getTotalInfo",
			"monthlyInfo!getMonthlyProjectInfo",
			"monthlyInfo!getMonthlyContractInfo",
			"monthlyInfo!getMonthlyGoodEmployeeInfo",
			"monthlyInfo!getMonthlyInfoAuth",
			"personalHomeInfo!getPersonalHomeInfo",
			"service!checkAccountBinds",
			"/pages/remoteHttpInvoke/",
			"myAdviceandResponse",
			"modalDialog_notlogin.jsp",
			"/pages/client/"
			};

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws ServletException, IOException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		if (printFlag) {
			printAccess(req);
		}

		boolean haveFind = false;
		UserModel userModel = (UserModel) req.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		// userModel=null;
		if (userModel == null) {
			for (int i = 0; i < system_url.length; i++) {

				if (req.getRequestURI().indexOf(system_url[i]) >= 0) {
					haveFind = true;
					break;
				}
			}

			if ("/nStraf/".equals(req.getRequestURI())) {
				res.sendRedirect(req.getContextPath() + "/index.jsp");
				return;
			}

			if (!haveFind && userModel == null) {

				req.getSession().removeAttribute(Constants.LOGIN_USER_KEY);

				res.sendRedirect(req.getContextPath()
						+ "/pages/common/isTimeOut.jsp");
				return;
			}
		}

		chain.doFilter(req, res);
	}

	private void printAccess(HttpServletRequest req) {
		System.out.println(req.getRemoteHost() + "����ַ����� "
				+ req.getRequestURI() + " ������ "
				+ DateUtil.getTimeString(new Date()) + "");
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		String printLog = filterConfig.getInitParameter("printLog");
		if (printLog != null && printLog.equals("true")) {
			printFlag = true;
		} else {
			printFlag = false;
		}
	}

	public void destroy() {
	}

}
