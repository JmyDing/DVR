package com.hzsun.www.relayServlet;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.hzsun.www.Utils.Log4J;
import com.hzsun.www.mediaSelvet.ProxyServlet;

public class ReceiveServlet  extends Thread {
	private LinkedBlockingQueue<byte[]>  queen ;
	private ServerSocket  service;
	private Socket socket;
	private Boolean  isrun;
	private String  deviceName;
	public ReceiveServlet(Integer port,LinkedBlockingQueue<byte[]> queen,String device) throws  IOException{
		InetSocketAddress socketAddress = new InetSocketAddress(InetAddress.getLocalHost(), port);  
		service = new ServerSocket(port);
        this.queen=queen;
        isrun=true;
        deviceName=device;
	}
	
	
	public Socket getSocket(){
		 return socket;
	}
	private void accpet() throws IOException{
		socket=service.accept();
		socket.setReceiveBufferSize(1024*1024*2);
	}
	
	public  void receive() throws IOException{
			
			byte[] receive ,buffer ;
//			DatagramPacket packet;
			Integer length=1;
			InputStream  in=socket.getInputStream();
			while(isrun){
//				queen.add(new byte[]{1,2,3});
//				System.out.println("Receive="+queen.size());
				int totalCount=0,count=0;
				 try {
					 byte[] lengthbuffer=new byte[4];
					 
					 while(totalCount < 4) {
//						 System.out.println("----");
//						 	if(!socket.isConnected()){
//						 		isrun=false ;
//						 		break;
//						 	}
							count = in.read(lengthbuffer, totalCount, 4 - totalCount);
							if(count == -1) {
								isrun=false;
								Log4J.getLogger().info("读取null非法关闭");
								break;
							}
							totalCount += count;
						}
					totalCount = 0;
					int  lg= (lengthbuffer[0] & 0xff) + ((lengthbuffer[1] & 0xff)<<8) + ((lengthbuffer[2] & 0xff)<<16) + ((lengthbuffer[3] & 0xff)<<24);
					
//					if(lg<1){
//						continue;
//					}
					queen.add(lengthbuffer);
					System.out.println("RqueenSize="+queen.size());
					buffer=new byte[lg];
					while(totalCount < lg) {
						System.out.println("+++++");
					   count = in.read(buffer, totalCount, lg - totalCount);
					    if(count == -1) continue;
					    totalCount += count;
					}				
					queen.add(buffer);
					if(queen.size()>1000){
						setRun();
						close();
						ProxyServlet.removeRS(deviceName);
						ProxyServlet.removeSS(deviceName);
					}
//					Log4J.getLogger().info("receiveMessageSize-----"+queen.size());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					setRun();
					close();
					ProxyServlet.removeRS(deviceName);
					ProxyServlet.removeSS(deviceName);
					Log4J.getLogger().info(e.toString());
				}
			}
			
			socket.close();
			service.close();
			ProxyServlet.removeRS(deviceName);
			ProxyServlet.removeSS(deviceName);
	}
	
	
	public  void  close() throws IOException{
		if(socket!=null && !socket.isClosed()){
			socket.close();
		}
		service.close();
	}
	
	
	public void setRun(){
		synchronized (isrun) {
			isrun=false;
		}
	}
	
	
	
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		try {
			accpet();
			receive();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	

	
	
	
}
