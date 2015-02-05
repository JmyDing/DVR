package com.hzsun.www.mediaSelvet;



import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.DocumentException;

import com.hzsun.www.Message.GetPort;
import com.hzsun.www.Message.MediaByeXml;
import com.hzsun.www.Message.MediaPlayXml;
import com.hzsun.www.Message.ParseXml;
import com.hzsun.www.Message.XmlFactory;
import com.hzsun.www.Utils.GetConfig;
import com.hzsun.www.Utils.Log4J;
import com.hzsun.www.Utils.XmlUtil;
import com.hzsun.www.relayServlet.RelayServlet;


public class PackageProcess  implements Runnable {
	
	public static final Logger logger=Logger.getLogger(PackageProcess.class);
	
	private Socket client;
	private InputStream in;
	private OutputStream out;

	
	public PackageProcess (Socket client){
		this.client=client;
		try {
			in=this.client.getInputStream();
			out=this.client.getOutputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@SuppressWarnings("unused")
	private  ParseXml parsePacket(String info) throws DocumentException{
//		SocketAddress remoteAddress=data.getSocketAddress();
		Log4J.getLogger().info("代理服务端收到的消息=》"+info);
		if(info.contains("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")){
			return XmlFactory.parse(info);
		}else{
		return null;
		}
	}
	
	
	public String readString(){
		byte[] bt=new byte[1024];
		try {
			int i=in.read(bt);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log4J.getLogger().info(e.toString());
		}
		return new String(bt).trim();
	}
	
	
	
	public void send(byte[] bt){
		try {
			out.write(bt);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log4J.getLogger().info(e.toString());
		}
	}
	
	
	
	
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			String read=readString();
			ParseXml xml= parsePacket(read);
			if(xml instanceof MediaPlayXml){
				try {
					handlerMediaPlay((MediaPlayXml) xml);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Log4J.getLogger().info(e.toString());
				}
			}else if(xml instanceof MediaByeXml){
				
				try {
					handleBye((MediaByeXml) xml);
				}  catch (IOException e) {
					// TODO Auto-generated catch block
					Log4J.getLogger().info(e.toString());
				}
			}
			
			
			
			
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private  void handlerMediaPlay(MediaPlayXml xml) throws IOException{
		String device=xml.getDeviceName().trim();
		String time=xml.getTime().trim();
		String userName=xml.getUserName().trim();
		Integer channelId=xml.getChannelId();
		String key=userName+"-"+device+"-"+time;
		String devicekey=device+"-"+channelId;
		Map<String,String>  sendMap=new HashMap<String ,String>();

		RelayServlet  rls=ProxyServlet.getRS(devicekey);
		Socket ss=null;
		if(rls!=null){
			ss=rls.getReciveSocket();
		}
		System.out.println("转发服务器="+rls);
		System.out.println("ReceviceSocket="+ss);
		if(rls==null || ss==null){
			
			Integer port=GetPort.getPort();
			System.out.println("接受端口=》"+port);
			if(port>0){
				sendMap.put("Type","MediaPlay");
				sendMap.put("MediaIp",GetConfig.getInstance().getMMSERVICE());
				sendMap.put("MediaPort", Integer.toString(port));
				sendMap.put("Code", "1");
				sendMap.put("Msg", "MediaPlaysuccess");
				if(rls==null){
					rls=new RelayServlet(devicekey,port);
//					rls.addClient(key);
					rls.start();
				}
				
				ProxyServlet.setRS(devicekey, rls);
				ProxyServlet.setMSGNO(devicekey, key);
				
			}else{
				sendMap.put("Type","MediaRelay");
				sendMap.put("MediaIp","");
				sendMap.put("MediaPort","" );
				sendMap.put("Code", "2");
				sendMap.put("Msg", "the port can not instance");
			}
		}else{
			rls.addClient(key);
			sendMap.put("Type","MediaRelay");
			sendMap.put("MediaIp","");
			sendMap.put("MediaPort","" );
			sendMap.put("Code", "1");
			sendMap.put("Msg", "MediaRelaySuccess");
		}
		sendMap.put("ClientIp",GetConfig.getInstance().getMMSERVICE() );
		sendMap.put("ClientPort",Integer.toString(GetConfig.getInstance().getCLIENTPORT()));
		sendMap.put("UserName", userName);
		sendMap.put("DeviceName", device);
		sendMap.put("Time",time);
		String info=XmlUtil.createXml(sendMap);
		this.send(info.getBytes());
		
//		Socket  client=new Socket();
//    	SocketAddress sip=new InetSocketAddress(GetConfig.getInstance().getSIPAddress(),GetConfig.getInstance().getSIPPort());
// 		client.connect(sip);
// 		client.getOutputStream().write(info.getBytes());
// 		client.close();
 		System.err.println("发送的消息"+info);
//		System.out.println("发出消息=》"+info);
		
		
	}
	
	
	private  void handleBye(MediaByeXml xml) throws IOException, UnknownHostException{
		String device=xml.getDeviceName().trim();
		String time=xml.getTime().trim();
		String userName=xml.getUserName().trim();
		Integer channelId=xml.getChannelId();
		String deviceKey=device+"-"+channelId;
		String key=userName+"-"+device+"-"+time;
		RelayServlet  rls=ProxyServlet.getRS(deviceKey);
		Integer code=rls.removeClient(key,deviceKey);
		Map<String,String>  sendMap=new HashMap<String ,String>();
		if(code.equals(1)){
			sendMap.put("Code", "1");
			sendMap.put("Msg", "MediaBye not delete client");
			sendMap.put("MsgNo", "");
		}else if(code.equals(2)){
			sendMap.put("Code", "2");
			sendMap.put("Msg", "MediaBye delete client");
			String  msgno=ProxyServlet.getMSGNO(device);
			sendMap.put("MsgNo","");
			System.out.println("MsgNo="+msgno);
		}
		sendMap.put("Type","MediaBye");
		sendMap.put("UserName", userName);
		sendMap.put("DeviceName", device);
		sendMap.put("Time",time);
		sendMap.put("ChannelId",channelId.toString());
		System.out.println("Key="+key);
		System.out.println("ChannelId="+channelId);
		String info=XmlUtil.createXml(sendMap);
		this.send(info.getBytes());
 		System.err.println("发送的消息"+info);
 		
		Log4J.getLogger().info("发出消息=》"+info);
		
	}
	
	
	
	
}
