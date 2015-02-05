package com.hzsun.www.mediaSelvet;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;

import com.hzsun.www.Utils.GetConfig;

public class ServiceAddress {
	private static InetSocketAddress sipaddress;
	
	
	
	
	
	public static InetSocketAddress getSipAddress() throws UnknownHostException{
		if(sipaddress==null){
			sipaddress=new InetSocketAddress
				(GetConfig.getInstance().getSIPAddress(),GetConfig.getInstance().getSIPPort());
			return sipaddress;
		}
		return sipaddress;
	}
	
	

}	
	
