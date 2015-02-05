package com.hzsun.www.Message;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.hzsun.www.Utils.Log4J;
import com.hzsun.www.Utils.Utils;

public class MediaPlayXml extends ParseXml {

	private  String  time;
	private String  DeviceName;
	private String UserName;
	private Integer ChannelId;
	@Override
	public void parse(String xml) {
		// TODO Auto-generated method stub
		
		Document doc;
		try {
			doc = DocumentHelper.parseText(xml);
			Element rootElt = doc.getRootElement();
			DeviceName=rootElt.elementText("DeviceName");
			UserName=rootElt.elementText("UserName");
			time=rootElt.elementText("Time");
			ChannelId=Utils.formatInteger(rootElt.elementText("ChannelId"));
			super.Type=rootElt.elementText("Type");
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			Log4J.getLogger().info(e.toString());
		} // 将字符串转为XML  
		
	}

	public String getDeviceName() {
		return DeviceName;
	}

	public String getUserName() {
		return UserName;
	}

	public void setDeviceName(String deviceName) {
		DeviceName = deviceName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	
	
	
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public Integer getChannelId() {
		return ChannelId;
	}

	public void setChannelId(Integer channelId) {
		ChannelId = channelId;
	}

	@Override
	public String toString() {
		return "MediaPlayXml [time=" + time + ", DeviceName=" + DeviceName
				+ ", UserName=" + UserName + ", Type=" + Type + "]";
	}


	

	

	
	
	
	
	
}
