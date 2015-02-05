package com.hzsun.www.Message;



public abstract class ParseXml {

	protected String Type;
	
	
	public    abstract  void parse(String xml);

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}
	
	
	
	
	
}
