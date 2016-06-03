package cn.grgbanking.feeltm.parseMsg;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import cn.grgbanking.feeltm.parseMsg.appMessage.R2002Msg;

public class TestSocketServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			System.out.println("start socket server ");
			ServerSocket server = new ServerSocket(5678); // 在端口5678上注册服务
			Socket client = server.accept(); // 监听窗口,等待连接
			//BufferedReader in = new BufferedReader(new InputStreamReader(client
					//.getInputStream()));

			//BufferedReader serverInput = new BufferedReader(
					//new InputStreamReader(System.in));
			PrintWriter ut = new PrintWriter(client.getOutputStream());
			System.out.println(" socket server  accept");
			while (true) {
				// String str=in.read();// .readLine(); //// 读取从client传来的数据信息

				// System.out.println(str); //服务器控制台输出数据信息
				R2002Msg smsg = new R2002Msg();
				smsg.setCheckresult("ok");
				smsg.setFilepath("c:/project/teswt/");
				smsg.setDevid("Z1000001");
				smsg.setMessageno("testmessagen");
				smsg.setOgcd("00000000");
				smsg.setTelno("5601");
				ut.println(new String(smsg.packMessage())); // 服务器向客户端发送信息:has
															// receive....
				ut.flush();
				client.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
