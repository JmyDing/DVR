package com.hzsun.www.Message;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.hzsun.www.Utils.Log4J;
import com.hzsun.www.Utils.Utils;

public class MediaByeXml extends ParseXml {

	private String Code;
	private String Msg;
	private  String  time;
	private String  DeviceName;
	private String UserName;
	private String MsgNo;
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

	public String getCode() {
		return Code;
	}

	public String getMsg() {
		return Msg;
	}



	public void setCode(String code) {
		Code = code;
	}

	public void setMsg(String msg) {
		Msg = msg;
	}

	
	
	
	public String getMsgNo() {
		return MsgNo;
	}

	public void setMsgNo(String msgNo) {
		MsgNo = msgNo;
	}

	@Override
	public String toString() {
		return "MediaByeXml [Code=" + Code + ", DeviceName=" + DeviceName
				+ ", Msg=" + Msg + ", MsgNo=" + MsgNo + ", UserName="
				+ UserName + ", time=" + time + ", Type=" + Type + "]";
	}

	

	

	

	

	
	
	
	
	
}
