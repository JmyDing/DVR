package com.hzsun.www.Message;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import com.hzsun.www.Utils.GetConfig;

public class GetPort {
	private static List<Integer> portList=new ArrayList<Integer>();
	static{
		for(int i=GetConfig.getInstance().getMIN_PORT();i<=GetConfig.getInstance().getMAX_PORT();i++){
			portList.add(i);
		}
	}
	
	public static Boolean  isUse(Integer port){
		DatagramSocket socket=null;
		try {
			 socket=new DatagramSocket(port);
			socket.close();
			return true;
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			
			return false;
		}
	}
	
	public static Integer getPort(){
		for(Integer port:portList){
			boolean use=isUse(port);
			if(use){
//				portList.remove(port);
				return port;
			}
		}
		return 0;
		
	}
	
	@SuppressWarnings("unchecked")
	public static void recycle(Integer port){
		portList.add(port);
	}
	
}
