package httpcall;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.grgbanking.feeltm.config.UserDataRoleConfig;

public class Server {
    public static void post(HttpServletRequest request,HttpServletResponse response){
        String result = "";
        try {
            request.setCharacterEncoding("UTF-8");
            BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String line = "";
            StringBuffer buf = new StringBuffer();
            while ( (line = br.readLine()) != null ) {
                buf.append(line);
            }
            result = buf.toString();
            String[] param = result.split("\\|");
            String user_id=param[0];
            String key = param[1];
            boolean getInfo = UserDataRoleConfig.viewAllDataByUserid(result.split("\\|")[0], result.split("\\|")[1]);
           
            
            response.setHeader("cache-control", "no-cache");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html");
            OutputStreamWriter out = new OutputStreamWriter(response.getOutputStream());    
            out.write(getInfo+"");
            out.flush();
            out.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

