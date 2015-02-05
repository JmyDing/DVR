package com.hzsun.www.mediaSelvet;

import java.util.Timer;

import com.hzsun.www.Utils.GetConfig;
import com.hzsun.www.Utils.Log4J;

public class StartUp {
	private  ProxyServlet mms;
	private  ClientServlet cls;
	private  Integer mmsport,clsport;
	
		
	
	public StartUp (){

		try {
			Integer mmsport=GetConfig.getInstance().getServiceport();
			Integer clsport=GetConfig.getInstance().getCLIENTPORT();
			Log4J.getLogger().info("媒体服务器端口："+mmsport);
			mms=new ProxyServlet(mmsport);
			cls=new ClientServlet(clsport);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log4J.getLogger().info(e.toString());
		}	
	}
	
	public void start(){
		mms.start();
		cls.start();
	}
	
	public static void main(String[] args){
		
		StartUp st=new StartUp();
		st.start();
		Timer time=new Timer();
		ClearTime clear=new ClearTime(); 
		time.schedule(clear, 10*60*1000,10*60*1000);
		
	}
}
