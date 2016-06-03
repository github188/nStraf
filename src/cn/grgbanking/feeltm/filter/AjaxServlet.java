package cn.grgbanking.feeltm.filter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
@SuppressWarnings({"serial", "unchecked"})
public class AjaxServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html; charset=gb2312");
		PrintWriter out = response.getWriter();
		//JSONObject jsonDemo = new jsonTest().jsonDemo();
	
		List list = new ArrayList<VO>();
		
		for(int i = 0 ; i < 20 ; i ++)
		{
			VO vo = new VO("weiling"+i,"weiling"+i,"weiling"+i,"weiling"+i,"weiling"+i,i);
		
			list.add(vo);
		}
		JSONObject jsonDemo = ObjectListToJSON.toJSON2(list, "VO");

		out.print(jsonDemo);

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);

	}

}
