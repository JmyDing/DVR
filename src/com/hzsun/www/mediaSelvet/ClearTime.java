package com.hzsun.www.mediaSelvet;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TimerTask;

import com.hzsun.www.Utils.Log4J;
import com.hzsun.www.relayServlet.RelayServlet;

public class ClearTime extends TimerTask  {

	
	public void  clear (){
		Log4J.getLogger().info("执行定时器");
		Map<String,Map<String,HandleMessage>> ss=ProxyServlet.getSS();
		synchronized (ss) {
			Set<String> set=ss.keySet();
			Iterator<String> it=set.iterator();
			while(it.hasNext()){
				String key=it.next();
				Map<String,HandleMessage> map=ss.get(key);
				if(map==null) {
					continue;
				}
				Set<String> hst=map.keySet();
				Iterator<String> hit=hst.iterator();
				while(hit.hasNext()){
					String ks=hit.next();
					HandleMessage handler=map.get(ks);
					
					if(handler==null || handler.isClosed()){
						map.remove(ks);
						Log4J.getLogger().info("定时器非法移除sendService=》"+ks);
					}
				}
				
				if(map.size()==0){
				RelayServlet rs=ProxyServlet.getRS(key);
				if(rs!=null){
					rs.close();
					Log4J.getLogger().info("定时器非法移除relayservice=》"+key);
				}
				ProxyServlet.removeSS(key);
				ProxyServlet.removeRS(key);
				ProxyServlet.removeMSGNO(key);
				}
				
			}
		}
		
		
	}
	
	
	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		clear ();
	}

}
