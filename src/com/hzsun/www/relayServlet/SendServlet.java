package com.hzsun.www.relayServlet;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.hzsun.www.Utils.Log4J;
import com.hzsun.www.mediaSelvet.HandleMessage;

public class SendServlet extends Thread  {
	private  Map<String,HandleMessage> map;
	private LinkedBlockingQueue<byte[]> queen;
	private Boolean  isrun;
	public SendServlet( Map<String,HandleMessage> map, LinkedBlockingQueue<byte[]> queen){
		this.queen=queen;
		this.map=map;
		isrun=true;
	}
	
	private void send() throws Exception{
		byte[] send;
		Iterator<String> it;
		while(isrun){
			
			byte[]  data=queen.take();
			System.out.println("TACKSIZE="+queen.size());		
			if(data==null){
				continue;
			}
			
			
			synchronized (map) {
				it=map.keySet().iterator();
//				Log4J.getLogger().info("sendClient-----"+map.size());
				while(it.hasNext()){
					String str=(String) it.next();
					HandleMessage handle=map.get(str);
					if(handle!=null){
						handle.send(data);						
					}
				}
			}
			
		}
	}
	
	
	
	public void setRun(){
		synchronized (isrun) {
			isrun=false;
//			queen.stopTake();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		try {
			send();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log4J.getLogger().info(e.toString());
		}
	}
	
	
	private byte[] getPayload(byte[] rtpData, int length) {
		byte info = rtpData[0];
		int paddCount = 0;
		if((info&0x20) != 0) {
			paddCount = (rtpData[length - 1] &0xff);
		}
		int payloadLength = length - 12 - paddCount;
		byte[] payload = new byte[payloadLength];
		System.arraycopy(rtpData, 12, payload, 0, payloadLength);
		return payload;
	}
	
	
	public byte[] getLength(int length){
		
		byte[] buf = new byte[4];
		buf[3] = (byte) ((length >> 24) & 0xff);
		buf[2] = (byte) ((length >> 16) & 0xff);
		buf[1] = (byte) ((length >> 8) & 0xff);
		buf[0] = (byte) (length  & 0xff);
		return buf;
	}
	
	
	
}
