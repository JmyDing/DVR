package com.hzsun.www.mediaSelvet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;

import com.hzsun.www.Utils.GetConfig;
import com.hzsun.www.Utils.Log4J;
import com.hzsun.www.Utils.XmlUtil;
import com.hzsun.www.relayServlet.RelayServlet;

public class SocketParse {

	private  Socket client;
	private InputStream in;
	private OutputStream out;
	private String key;
	private String device;
	SocketParse(Socket client){
		this.client=client;
		
		try {
//			this.client.setSendBufferSize();
			in=this.client.getInputStream();
			out=this.client.getOutputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log4J.getLogger().info(e.toString());
		}
		
	}
	
	public String readString(){
		byte[] bt=new byte[1024];
		try {
			in.read(bt);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log4J.getLogger().info(e.toString());
		}
		return new String(bt);
	}
	
	
	
	public void send(byte[] bt){
		try {
			out.write(bt);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			
			Log4J.getLogger().info("非法关闭端口！！");
			RelayServlet  rls=ProxyServlet.getRS(device);
			Integer code=rls.removeClient(key,device);
			String userName=key.substring(0, key.indexOf("-"));
			String time=key.substring(key.lastIndexOf("-")+1);
			Map<String,String>  sendMap=new HashMap<String ,String>();
			if(code.equals(1)){
				sendMap.put("Code", "1");
				sendMap.put("Msg", "MediaBye not delete client");
				sendMap.put("MsgNo", "");
			}else if(code.equals(2)){
				sendMap.put("Code", "2");
				sendMap.put("Msg", "MediaBye delete client");
				String  msgno=ProxyServlet.getMSGNO(device);
				sendMap.put("MsgNo",msgno);
			}
			sendMap.put("Type","MediaBye");
			sendMap.put("UserName", userName);
			sendMap.put("DeviceName", device);
			sendMap.put("Time",time);
			String info=XmlUtil.createXml(sendMap);
			Socket  client=new Socket();
	    	SocketAddress sip=new InetSocketAddress(GetConfig.getInstance().getSIPAddress(),GetConfig.getInstance().getSIPPort());
	 		
	 		try {
	 			client.connect(sip);
				client.getOutputStream().write(info.getBytes());
				client.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	 		
	 		Log4J.getLogger().info(e.toString());
		}
	}
	

	public String getKey() {
		return key;
	}

	public String getDevice() {
		return device;
	}
	

	public void setKey(String key) {
		this.key = key;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public void close(){
		try {
			client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log4J.getLogger().info(e.toString());
		}
	}
	
	public boolean isclose(){
		return client.isClosed();
	}
	
}
