package com.hzsun.www.relayServlet;

import java.io.IOException;
import java.lang.reflect.Proxy;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.hzsun.www.Utils.Log4J;
import com.hzsun.www.mediaSelvet.HandleMessage;
import com.hzsun.www.mediaSelvet.ProxyServlet;

public class RelayServlet  extends Thread{

	private LinkedBlockingQueue<byte[]>  queen ; 
	private	 ReceiveServlet  receivce;
	private  SendServlet   send;
	private  Map<String,HandleMessage> map;
	public  RelayServlet(String key,Integer port) throws IOException{
		map=ProxyServlet.getSS(key);
		if(map==null){
			ProxyServlet.setSS(key, new HashMap<String,HandleMessage>());
			map=ProxyServlet.getSS(key);
		}
		queen=new LinkedBlockingQueue<byte[]>(1024*1024);
		receivce=new ReceiveServlet(port, queen,key);
		send=new SendServlet(map, queen);
	}
	
	
	public void  go(){
		receivce.start();
		send.start();
	}
	
	public void addClient(String key){
		map.put(key, null);
	}
	
	public Integer removeClient(String key,String deviceName){
		Integer size=map.size();
		Log4J.getLogger().info(deviceName+"SIZE=>"+size);
		if(size>1){
			synchronized (map) {
				map.remove(key);
			}
			HandleMessage handle=map.get(key);
			
			if(handle!=null){
				handle.close();
			}
			return 1;
		}else{
			synchronized (map) {
				map.remove(key);
			}
			HandleMessage handle=map.get(key);
			if(handle!=null){
				handle.close();
			}
			
			if(receivce!=null){
				this.close();
			}
			
			ProxyServlet.removeRS(deviceName);
			ProxyServlet.removeSS(deviceName);
			return 2;
		}
		
	}
	
	public 	Socket getReciveSocket(){
		return receivce.getSocket();
	}
	
	
	public  void close(){
		System.out.println("关闭socket");
		receivce.setRun();
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			Log4J.getLogger().info(e.toString());
		}
		System.out.println("如果没有关闭再关闭一次关闭socket");
		try {
			receivce.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		go();
	}
	
	
	
	
	
}
