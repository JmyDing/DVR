package com.hzsun.www.mediaSelvet;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.hzsun.www.Utils.Log4J;
import com.hzsun.www.relayServlet.RelayServlet;


public class ProxyServlet extends Thread  {
	private  ServerSocket  service=null;  
	private ThreadPoolExecutor udpThreadPool;
	private static final  Map<String,RelayServlet>  RS=new ConcurrentHashMap<String,RelayServlet>();
	private static final Map<String,Map<String,HandleMessage>>  SS=new ConcurrentHashMap<String,Map<String,HandleMessage>>();
	private static final Map<String,String>  MSGNO=new HashMap<String,String>();
	 public ProxyServlet(int port) throws Exception { 	
	        service = new ServerSocket(port); 
	 		BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(100);
	 		RejectedExecutionHandler handler = new ThreadPoolExecutor.DiscardPolicy();
	 		TimeUnit unit = TimeUnit.SECONDS;
	 		
	 		queue = new ArrayBlockingQueue<Runnable>(100);
	 		handler = new ThreadPoolExecutor.DiscardPolicy();
	 		udpThreadPool = new ThreadPoolExecutor(50,
	 				50 * 2, 3, unit, queue, handler);
	    } 
	 
	
	 public void  startUp(){
		 
		 while (true){
			 byte[]  buffer=new byte[1024];
			 try {
				Socket client=service.accept();
				PackageProcess processor= new PackageProcess(client);
				udpThreadPool.execute(processor);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log4J.getLogger().info(e.toString());
			}
		 }
	 }
	 
	


	@Override
	public void run() {
		// TODO Auto-generated method stub
		startUp();
	}
	
	public static RelayServlet getRS(String key){
		return RS.get(key);
	}
	
	public static void setRS(String key , RelayServlet service){
		RS.put(key, service);
	}

	public static void removeRS(String key ){
		RS.remove(key);
	}
	
	public static String getMSGNO(String key){
		return MSGNO.get(key);
	}
	
	public static void setMSGNO(String key ,String service){
		MSGNO.put(key, service);
	}
	public static void removeMSGNO(String key ){
		MSGNO.remove(key);
	}
	
	public static Map<String,HandleMessage> getSS(String key){
		return SS.get(key);
	}
	
	public static void setSS(String key ,Map<String,HandleMessage> service){
		SS.put(key, service);
	}
	
	public static void removeSS(String key ){
		SS.remove(key);
	}
	
	public static  Map<String,Map<String,HandleMessage>>  getSS(){
		return SS;
	}
	
	
	
	
	
	
}
