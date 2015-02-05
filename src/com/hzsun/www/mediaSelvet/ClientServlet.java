package com.hzsun.www.mediaSelvet;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.hzsun.www.Utils.Log4J;


public class ClientServlet extends Thread {
	
	private ServerSocket service;
	
	public ClientServlet(Integer port){
		try {
			service=new ServerSocket(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log4J.getLogger().info(e.toString());
		}
	}
	
	public  void accept(){
		while(true){
			try {
				Socket  client=service.accept();
				System.out.println("****************");
				HandleMessage handler=new HandleMessage(client);
				handler.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log4J.getLogger().info(e.toString());
			}
		}
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		accept();
	}
	
	
}
