package cn.grgbanking.feeltm.service;

/**
 * <p>Title: FEEL</p>
 * <p>Description:ͨ�õı���������</p>
 * <p>��Ҫ��request����ExportParam����:request.setAttribute</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: GRGBanking  Co.,Ltd</p>
 * @author lvjm
 * @version 1.0
 */
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class ReportServlet extends HttpServlet {
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		OutputStream out = response.getOutputStream();
		try {

		} catch (Exception ee) {
			ee.printStackTrace();
			

		} finally {
			try {
				out.flush();
				out.close();

			} catch (Exception ee) {
				ee.printStackTrace();
			}
		}// finally
	}
	// ------------------------------------------------------------------------------
}