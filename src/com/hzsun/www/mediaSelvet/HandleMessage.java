package com.hzsun.www.mediaSelvet;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.hzsun.www.Utils.GetConfig;
import com.hzsun.www.Utils.Log4J;
import com.hzsun.www.Utils.XmlUtil;

public class HandleMessage extends Thread {
	private SocketParse  parse;
	private  Boolean isRun;
	HandleMessage(Socket socket){
		this.parse=new SocketParse(socket);
		isRun=true;
	}
	
	public SocketParse getParse() {
		return parse;
	}
	
	public void  parse() throws DocumentException{
		
		String  info=parse.readString().trim();
		Log4J.getLogger().info("客户端服务器收到消息=》"+info);
		if(info.contains("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")){
			Document doc = DocumentHelper.parseText(info); 
	         Element rootElt = doc.getRootElement(); 
	         String Type=rootElt.elementText("Type");
	         if(Type.equalsIgnoreCase("Media")){
	        	 mediaPlay(rootElt);
	         }else if (Type.equalsIgnoreCase("PlaybackPlay")){
	        	 mediaPlaybackPlay(rootElt);
	         }
	         
		}
			
		
	}
	
	
	public  void send (byte[]  bt){
		
		parse.send(bt);
	}
	
	
	public  void  close(){
		synchronized (isRun) {
			isRun=false;
		}
		parse.close();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		try {
			parse();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			Log4J.getLogger().info(e.toString());
		}
		
	}
	
	
	
	public String getKey() {
		return parse.getKey();
	}

	public String getDevice() {
		return parse.getDevice();
	}
	

	public void setKey(String key) {
		parse.setKey(key);
	}

	public void setDevice(String device) {
		parse.setDevice(device);
	}
	
	public boolean isClosed(){
		return parse.isclose();
	}
	
	
	
	
	private void  mediaPlay(Element rootElt){
		 String info;
		 String DeviceName=rootElt.elementText("DeviceName");
		 String UserName=rootElt.elementText("UserName");
	   	 String Time=rootElt.elementText("Time");
	   	 String ChannelId=rootElt.elementText("ChannelId");
	   	 String key=UserName+"-"+DeviceName+"-"+Time;
	   	 String MediaIp=rootElt.elementText("MediaIp");
	   	 String MediaPort=rootElt.elementText("MediaPort");
	   	 String PlayOption=rootElt.elementText("PlayOption");
	   	 String devicekey=DeviceName+"-"+ChannelId;
	        if("0".equalsIgnoreCase(PlayOption)){
	       	 Map<String,HandleMessage> map=ProxyServlet.getSS(devicekey);
	       	 map.put(key, this);
	       	 setKey(key);
	       	 setDevice(devicekey);
	       	 System.out.println("添加map"+this);
	        }else if("1".equalsIgnoreCase(PlayOption)){
	       	
				try {
					
					 Map<String,String>  sendMap=new HashMap<String ,String>();
					 sendMap.put("Type","MediaInvite");
					 sendMap.put("MediaIp",MediaIp);
					 sendMap.put("MediaPort",MediaPort);
					 sendMap.put("UserName", UserName);
					 sendMap.put("DeviceName", DeviceName);
					 sendMap.put("Time",Time);
					 sendMap.put("ChannelId", ChannelId);
		        	 info=XmlUtil.createXml(sendMap);
		        	 System.err.println("发送的消息"+info);
	//	     		DatagramPacket packet = new DatagramPacket(info.getBytes(),info.length(),ServiceAddress.getSipAddress());				
	//				socket.send(packet);
	//				System.err.println("发送的消息"+info);
	//				socket.close();
					
					Socket  client=new Socket();
		        	SocketAddress sip=new InetSocketAddress(GetConfig.getInstance().getSIPAddress(),GetConfig.getInstance().getSIPPort());
		     		client.connect(sip);
		     		client.getOutputStream().write(info.getBytes());
		     		client.close();
		     		System.err.println("发送的消息"+info);
		     		
		     		 Map<String,HandleMessage> map=ProxyServlet.getSS(devicekey);
		     		 if(map==null){
		     			ProxyServlet.setSS(devicekey,new HashMap<String,HandleMessage>());
		     		 }
		     		map=ProxyServlet.getSS(devicekey);
		        	 map.put(key, this);
		        	 setKey(key);
		        	 setDevice(devicekey);
		        	 System.out.println("添加map"+this);
				} catch (IOException e ) {
					// TODO Auto-generated catch block
					Log4J.getLogger().info(e.toString());
				}
	       	
	        }
			
		}
		
		private void  mediaPlaybackPlay(Element rootElt){
			 String info;
			 String DeviceName=rootElt.elementText("DeviceName");
			 String UserName=rootElt.elementText("UserName");
		   	 String Time=rootElt.elementText("Time");
		   	 String ChannelId=rootElt.elementText("ChannelId");
		   	 String key=UserName+"-"+DeviceName+"-"+Time;
		   	 String MediaIp=rootElt.elementText("MediaIp");
		   	 String MediaPort=rootElt.elementText("MediaPort");
		   	 String PlayOption=rootElt.elementText("PlayOption");
		 	 String beginTime=rootElt.elementText("BeginTime");
			String  endTime=rootElt.elementText("EndTime");
			String recordId=rootElt.elementText("RecordId");	
			String Dv=DeviceName+"-"+beginTime+"-"+"-"+endTime+"-"+recordId+"-"+ChannelId;
				try {
//					socket = new DatagramSocket();
					 Map<String,String>  sendMap=new HashMap<String ,String>();
					 sendMap.put("Type","MediaPlaybackInvite");
					 sendMap.put("MediaIp",MediaIp);
					 sendMap.put("MediaPort",MediaPort);
					 sendMap.put("UserName", UserName);
					 sendMap.put("DeviceName", DeviceName);
					 sendMap.put("Time",Time);
					 sendMap.put("ChannelId", ChannelId);
					 sendMap.put("BeginTime", beginTime);
					 sendMap.put("EndTime", endTime);
					 sendMap.put("RecordId", recordId);
		        	 info=XmlUtil.createXml(sendMap);
					Socket  client=new Socket();
		        	SocketAddress sip=new InetSocketAddress(GetConfig.getInstance().getSIPAddress(),GetConfig.getInstance().getSIPPort());
		     		client.connect(sip);
		     		client.getOutputStream().write(info.getBytes());
		     		client.close();
		     		System.err.println("发送的消息"+info);
		     		
		     		 Map<String,HandleMessage> map=ProxyServlet.getSS(Dv);
		     		 if(map==null){
		     			ProxyServlet.setSS(Dv,new HashMap<String,HandleMessage>());
		     		 }
		     		map=ProxyServlet.getSS(Dv);
		        	 map.put(key, this);
		        	 Log4J.getLogger().info(Dv+"Size添加后的=>"+map.size());
		        	 setKey(key);
		        	 setDevice(Dv);
		        	 System.out.println("添加map"+this);
				} catch (IOException e ) {
					// TODO Auto-generated catch block
					Log4J.getLogger().info(e.toString());
				}
		   	 
		}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
